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

import org.junit.jupiter.api.Test;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A class for testing {@link ContextUtils}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class ContextUtilsTest {

    @Test
    void withDatabaseNullOrdering__NullsFirst__WhenAtStartRegardlessOfDirection() throws SQLException {
        final var context = context(true, false, false, false);
        assertNullsFirst(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextUtils.SortDirection.ASCENDING));
        assertNullsFirst(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextUtils.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__NullsLast__WhenAtEndRegardlessOfDirection() throws SQLException {
        final var context = context(false, true, false, false);
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextUtils.SortDirection.ASCENDING));
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextUtils.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__DirectionAware__WhenLow() throws SQLException {
        final var context = context(false, false, true, false);
        assertNullsFirst(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextUtils.SortDirection.ASCENDING));
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextUtils.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__DirectionAware__WhenHigh() throws SQLException {
        final var context = context(false, false, false, true);
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextUtils.SortDirection.ASCENDING));
        assertNullsFirst(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextUtils.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__NullsLast__WhenUnknown() throws SQLException {
        final var context = context(false, false, false, false);
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextUtils.SortDirection.ASCENDING));
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextUtils.SortDirection.DESCENDING));
    }

    @Test
    void withDatabaseNullOrdering__AbsolutePositionPrecedesDomainPosition__WhenInconsistent() throws SQLException {
        final var context = context(false, true, true, false);
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.naturalOrder(), ContextUtils.SortDirection.ASCENDING));
        assertNullsLast(ContextUtils.withDatabaseNullOrdering(
                context, Comparator.reverseOrder(), ContextUtils.SortDirection.DESCENDING));
    }

    @SuppressWarnings("deprecation")
    @Test
    void nullOrdered__SameAsAscending__WhenDirectionOmitted() throws SQLException {
        final var context = context(false, false, true, false);
        assertThat(ContextUtils.nullOrdered(context, Comparator.<String>naturalOrder()).compare(null, "a"))
                .isEqualTo(ContextUtils.withDatabaseNullOrdering(
                        context, Comparator.<String>naturalOrder(), ContextUtils.SortDirection.ASCENDING)
                                   .compare(null, "a"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void nullOrdered__SameAsPreferredMethod__WhenDirectionSpecified() throws SQLException {
        final var context = context(false, false, false, true);
        assertThat(ContextUtils.nullOrdered(
                context, Comparator.<String>reverseOrder(), ContextUtils.SortDirection.DESCENDING).compare(null, "a"))
                .isEqualTo(ContextUtils.withDatabaseNullOrdering(
                        context, Comparator.<String>reverseOrder(), ContextUtils.SortDirection.DESCENDING)
                                   .compare(null, "a"));
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
