package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
final class ContextTestUtils {

    static void assertCatalogIdsAreSorted(final Stream<? extends CatalogId> catalogIds) {
        Objects.requireNonNull(catalogIds, "catalogIds is null");
        assertThat(catalogIds)
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(CatalogId.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(CatalogId.LEXICOGRAPHIC_ORDER)
                );
    }

    static List<? extends CatalogId> assertCatalogIdsAreSorted(final List<? extends CatalogId> catalogIds) {
        assertCatalogIdsAreSorted(catalogIds.stream());
        return catalogIds;
    }

    static void assertColumnIdsSorted(final List<ColumnId> columnIds) {
        assertThat(Objects.requireNonNull(columnIds, "columnIds is null")).satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(ColumnId.CASE_INSENSITIVE_ORDER),
                l -> assertThat(l).isSortedAccordingTo(ColumnId.LEXICOGRAPHIC_ORDER)
        );
    }

    static void assertColumnsSorted(final List<Column> columns) {
        assertThat(Objects.requireNonNull(columns, "columns is null")).satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(Column.CASE_INSENSITIVE_ORDER),
                l -> assertThat(l).isSortedAccordingTo(Column.LEXICOGRAPHIC_ORDER)
        );
    }

    static void assertSchemaIdsSorted(final List<SchemaId> schemaIds) {
        assertThat(Objects.requireNonNull(schemaIds, "schemaIds is null")).satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(SchemaId.CASE_INSENSITIVE_ORDER),
                l -> assertThat(l).isSortedAccordingTo(SchemaId.LEXICOGRAPHIC_ORDER)
        );
    }

    static void assertSchemasSorted(final List<Schema> schemas) {
        assertThat(Objects.requireNonNull(schemas, "schemas is null")).satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(Schema.CASE_INSENSITIVE_ORDER),
                l -> assertThat(l).isSortedAccordingTo(Schema.LEXICOGRAPHIC_ORDER)
        );
    }

    static void assertTableIdsAreSorted(final List<? extends Table> tables) {
        assertTablesAreSorted(tables);
        assertThat(tables)
                .extracting(Table::getTableId)
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(TableId.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(TableId.LEXICOGRAPHIC_ORDER)
                );
    }

    static void assertTablesAreSorted(final List<? extends Table> tables) {
        Objects.requireNonNull(tables, "tables is null");
        assertThat(tables).satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(Table.CASE_INSENSITIVE_ORDER),
                l -> assertThat(l).isSortedAccordingTo(Table.LEXICOGRAPHIC_ORDER)
        );
    }
}
