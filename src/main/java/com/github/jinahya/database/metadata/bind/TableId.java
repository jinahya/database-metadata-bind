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
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class TableId implements MetadataTypeId<TableId, Table> {

    private static final long serialVersionUID = -7614201161734063836L;

    public static final Comparator<TableId> COMPARING_CASE_INSENSITIVE =
            Comparator.comparing(TableId::getSchemaId, SchemaId.COMPARING_IN_CASE_INSENSITIVE_ORDER)
                    .thenComparing(TableId::getTableName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<TableId> COMPARING_NATURAL =
            Comparator.comparing(TableId::getSchemaId, SchemaId.COMPARING_IN_NATURAL_ORDER)
                    .thenComparing(TableId::getTableName);

    public static TableId of(final SchemaId schemaId, final String tableName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(tableName, "tableName is null");
        return builder()
                .schemaId(schemaId)
                .tableName(tableName)
                .build();
    }

    public static TableId of(final String tableCat, final String tableSchem, final String tableName) {
        return of(SchemaId.of(tableCat, tableSchem), tableName);
    }

    private final SchemaId schemaId;

    private final String tableName;
}
