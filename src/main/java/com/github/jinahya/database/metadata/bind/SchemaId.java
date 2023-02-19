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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class SchemaId implements MetadataTypeId<SchemaId, Schema> {

    private static final long serialVersionUID = -9112917204279422378L;

    public static final Comparator<SchemaId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(SchemaId::getCatalogId, CatalogId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(SchemaId::getTableSchem, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<SchemaId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(SchemaId::getCatalogId, CatalogId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(SchemaId::getTableSchem);

    /**
     * Creates a new instance with specified arguments.
     *
     * @param catalogId  a catalog identifier.
     * @param tableSchem a value of {@value Schema#COLUMN_LABEL_TABLE_SCHEM} column.
     * @return a new instance.
     */
    public static SchemaId of(final CatalogId catalogId, final String tableSchem) {
        Objects.requireNonNull(catalogId, "catalogId is null");
        Objects.requireNonNull(tableSchem, "tableSchem is null");
        return builder()
                .catalogId(catalogId)
                .tableSchem(tableSchem)
                .build();
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param tableCatalog a value of {@value Schema#COLUMN_LABEL_TABLE_CATALOG} column.
     * @param tableSchem   a value of {@value Schema#COLUMN_LABEL_TABLE_SCHEM} column.
     * @return a new instance.
     */
    public static SchemaId of(final String tableCatalog, final String tableSchem) {
        return of(CatalogId.of(tableCatalog), tableSchem);
    }

    @Override
    public String toString() {
        return super.toString() + +'{' +
               "catalogId=" + catalogId +
               ",tableSchem='" + tableSchem + '\'' +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SchemaId)) return false;
        final SchemaId that = (SchemaId) obj;
        return catalogId.equals(that.catalogId)
               && tableSchem.equals(that.tableSchem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalogId, tableSchem);
    }

    private final CatalogId catalogId;

    private final String tableSchem;
}
