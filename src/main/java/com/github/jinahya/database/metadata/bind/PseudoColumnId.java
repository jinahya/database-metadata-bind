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
public final class PseudoColumnId implements MetadataTypeId<PseudoColumnId, PseudoColumn> {

    private static final long serialVersionUID = 7459854669925402253L;

    public static final Comparator<PseudoColumnId> COMPARING_IN_CASE_INSENSITIVE_ORDER =
            Comparator.comparing(PseudoColumnId::getTableId, TableId.COMPARING_CASE_INSENSITIVE)
                    .thenComparing(PseudoColumnId::getColumnName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<PseudoColumnId> COMPARING_IN_NATURAL_ORDER =
            Comparator.comparing(PseudoColumnId::getTableId, TableId.COMPARING_NATURAL)
                    .thenComparing(PseudoColumnId::getColumnName);

    public static PseudoColumnId of(final TableId tableId, final String columnName) {
        Objects.requireNonNull(tableId, "tableId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        return builder()
                .tableId(tableId)
                .columnName(columnName)
                .build();
    }

    public static PseudoColumnId of(final String tableCat, final String tableSchem, final String tableName,
                                    final String columnName) {
        return of(TableId.of(tableCat, tableSchem, tableName), columnName);
    }

    private final TableId tableId;

    private final String columnName;
}
