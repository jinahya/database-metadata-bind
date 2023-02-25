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
final class TableId implements MetadataTypeId<TableId, Table> {

    private static final long serialVersionUID = -7614201161734063836L;

    public static final Comparator<TableId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(TableId::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(TableId::getTableName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<TableId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(TableId::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(TableId::getTableName);

    /**
     * Creates a new instance with specified arguments.
     *
     * @param schemaId  a schema id.
     * @param tableName a value of {@value Table#COLUMN_LABEL_TABLE_NAME} column.
     * @return a new instance.
     * @see #of(String, String, String)
     */
    static TableId of(final SchemaId schemaId, final String tableName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(tableName, "tableName is null");
        return builder()
                .schemaId(schemaId)
                .tableName(tableName)
                .build();
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param tableCat   a value of {@value Table#COLUMN_LABEL_TABLE_CAT} column.
     * @param tableSchem a value of {@value Table#COLUMN_LABEL_TABLE_SCHEM} column.
     * @param tableName  a value of {@value Table#COLUMN_LABEL_TABLE_NAME} column.
     * @return a new instance.
     * @see #of(SchemaId, String)
     */
    static TableId of(final String tableCat, final String tableSchem, final String tableName) {
        return of(SchemaId.of(tableCat, tableSchem), tableName);
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "schemaId=" + schemaId +
               ",tableName=" + tableName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TableId)) return false;
        final TableId that = (TableId) obj;
        return schemaId.equals(that.schemaId) &&
               tableName.equals(that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                schemaId,
                tableName
        );
    }

    private final SchemaId schemaId;

    private final String tableName;
}
