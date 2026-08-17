package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.common.reflect.ClassPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A class for testing {@link ContextUtils}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class ContextUtilsTest {

    // -------------------------------------------------------------------------------------------- getLabeledFields(c)
    @DisplayName("getLabeledFields(c) is computed once and reused")
    @Test
    void getLabeledFields__Cached() {
        assertThat(ContextUtils.getLabeledFields(Table.class))
                .isSameAs(ContextUtils.getLabeledFields(Table.class));
    }

    @DisplayName("getLabeledFields(c) is indexed by the upper-cased column label")
    @Test
    void getLabeledFields__IndexedByUpperCasedLabel() {
        final var labeledFields = ContextUtils.getLabeledFields(Table.class);
        final var bindingFields = ContextUtils.getBindingFields(Table.class);
        assertThat(labeledFields).hasSameSizeAs(bindingFields);
        bindingFields.forEach((field, label) -> {
            assertThat(labeledFields)
                    .as("field of %1$s", label.value())
                    .containsEntry(label.value().toUpperCase(Locale.ROOT), field);
        });
        assertThat(labeledFields.keySet()).allSatisfy(l -> assertThat(l).isEqualTo(l.toUpperCase(Locale.ROOT)));
    }

    @DisplayName("getLabeledFields(c) matches the labels the binder reads from a result set")
    @Test
    void getLabeledFields__KeyedAsGetLabels() throws SQLException {
        final var labeledFields = ContextUtils.getLabeledFields(Table.class);
        final var metadata = mock(ResultSetMetaData.class);
        final var labels = List.copyOf(labeledFields.keySet());
        when(metadata.getColumnCount()).thenReturn(labels.size());
        for (int i = 0; i < labels.size(); i++) {
            // a driver reporting the labels in a different case must still resolve to the same fields
            when(metadata.getColumnLabel(i + 1)).thenReturn(labels.get(i).toLowerCase(Locale.ROOT));
        }
        final var results = mock(ResultSet.class);
        when(results.getMetaData()).thenReturn(metadata);
        assertThat(ContextUtils.getLabels(results)).isEqualTo(labeledFields.keySet());
    }

    @DisplayName("getLabeledFields(c) loses no field, for any metadata type")
    @Test
    void getLabeledFields__NoLabelCollision() throws IOException {
        final var types = ClassPath.from(ContextUtilsTest.class.getClassLoader())
                .getAllClasses()
                .stream()
                .filter(ci -> !ci.getName().equals("module-info"))
                .filter(ci -> ci.getName().startsWith(ContextUtilsTest.class.getPackageName()))
                .map(ClassPath.ClassInfo::load)
                .filter(MetadataType.class::isAssignableFrom)
                .filter(c -> !c.isInterface())
                .toList();
        assertThat(types).isNotEmpty();
        for (final var type : types) {
            // indexing by label must not merge two fields; the assertion inside getLabeledFields guards the
            // same invariant, but only for classes some other test happens to bind
            assertThat(ContextUtils.getLabeledFields(type))
                    .as("labeled fields of %1$s", type.getSimpleName())
                    .hasSameSizeAs(ContextUtils.getBindingFields(type));
        }
    }

    @DisplayName("getLabeledFields(c) fields are accessible")
    @Test
    void getLabeledFields__AccessibleFields() {
        final var instance = new Table();
        assertThat(ContextUtils.getLabeledFields(Table.class).values())
                .isNotEmpty()
                .allSatisfy(f -> assertThat(f.canAccess(instance)).isTrue());
    }

    // ------------------------------------------------------------------------------------- withDatabaseNullOrdering()
    @Test
    void withDatabaseNullOrdering__NullsFirst__WhenAtStartRegardlessOfDirection() throws SQLException {
        final var context = context(true, false, false, false);
        assertNullsFirst(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextConstants.SortDirection.ASCENDING));
        assertNullsFirst(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextConstants.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__NullsLast__WhenAtEndRegardlessOfDirection() throws SQLException {
        final var context = context(false, true, false, false);
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextConstants.SortDirection.ASCENDING));
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextConstants.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__DirectionAware__WhenLow() throws SQLException {
        final var context = context(false, false, true, false);
        assertNullsFirst(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextConstants.SortDirection.ASCENDING));
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextConstants.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__DirectionAware__WhenHigh() throws SQLException {
        final var context = context(false, false, false, true);
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextConstants.SortDirection.ASCENDING));
        assertNullsFirst(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextConstants.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__NullsLast__WhenUnknown() throws SQLException {
        final var context = context(false, false, false, false);
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextConstants.SortDirection.ASCENDING));
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextConstants.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__AbsolutePositionPrecedesDomainPosition__WhenInconsistent() throws SQLException {
        final var context = context(false, true, true, false);
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextConstants.SortDirection.ASCENDING));
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextConstants.SortDirection.DESCENDING));
    }

    private static Context context(final boolean atStart, final boolean atEnd, final boolean low, final boolean high)
            throws SQLException {
        final DatabaseMetaData metadata = mock(DatabaseMetaData.class);
        when(metadata.nullsAreSortedAtStart()).thenReturn(atStart);
        when(metadata.nullsAreSortedAtEnd()).thenReturn(atEnd);
        when(metadata.nullsAreSortedLow()).thenReturn(low);
        when(metadata.nullsAreSortedHigh()).thenReturn(high);
        return new Context(metadata);
    }

    private static void assertNullsFirst(final Comparator<? super String> comparator) {
        assertThat(comparator.compare(null, null)).isZero();
        assertThat(comparator.compare(null, "a")).isNegative();
        assertThat(comparator.compare("a", null)).isPositive();
    }

    private static void assertNullsLast(final Comparator<? super String> comparator) {
        assertThat(comparator.compare(null, null)).isZero();
        assertThat(comparator.compare(null, "a")).isPositive();
        assertThat(comparator.compare("a", null)).isNegative();
    }
}
