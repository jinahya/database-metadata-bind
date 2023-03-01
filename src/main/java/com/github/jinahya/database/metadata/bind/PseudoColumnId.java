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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
final class PseudoColumnId extends AbstractMetadataTypeId<PseudoColumnId, PseudoColumn> {

    private static final long serialVersionUID = 7459854669925402253L;

    public static final Comparator<PseudoColumnId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(PseudoColumnId::getTableId, TableId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(PseudoColumnId::getColumnName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<PseudoColumnId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(PseudoColumnId::getTableId, TableId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(PseudoColumnId::getColumnName);

    public static PseudoColumnId of(final TableId tableId, final String columnName) {
        Objects.requireNonNull(tableId, "tableId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        return builder()
                .tableId(tableId)
                .columnName(columnName)
                .build();
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableId=" + tableId +
               ",columnName=" + columnName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PseudoColumnId)) return false;
        final PseudoColumnId that = (PseudoColumnId) obj;
        return tableId.equals(that.tableId) &&
               columnName.equals(that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, columnName);
    }

    private final TableId tableId;

    private final String columnName;
}
