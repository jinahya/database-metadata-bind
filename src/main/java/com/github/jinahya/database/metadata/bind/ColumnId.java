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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

/**
 * An identifier for identifying a {@link Column} within a {@link Table}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class ColumnId implements MetadataTypeId<ColumnId, Column> {

    private static final long serialVersionUID = -4452694121211962289L;

    public static final Comparator<ColumnId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ColumnId::getTableId, TableId.CASE_INSENSITIVE_ORDER)
                    .thenComparingInt(ColumnId::getOrdinalPosition);

    public static final Comparator<ColumnId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ColumnId::getTableId, TableId.LEXICOGRAPHIC_ORDER)
                    .thenComparingInt(ColumnId::getOrdinalPosition);

    static ColumnId of(final TableId tableId, final String columnName, final int ordinalPosition) {
        Objects.requireNonNull(tableId, "tableId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        if (ordinalPosition <= 0) {
            throw new IllegalArgumentException("non-positive ordinal position: " + ordinalPosition);
        }
        return builder()
                .tableId(tableId)
                .columnName(columnName)
                .ordinalPosition(ordinalPosition)
                .build();
    }

    static ColumnId of(final String tableCat, final String tableSchem, final String tableName,
                       final String columnName, final int ordinalPosition) {
        return of(TableId.of(tableCat, tableSchem, tableName), columnName, ordinalPosition);
    }

    public static ColumnId of(final TableId tableId, final String columnName) {
        return of(tableId, columnName, 1);
    }

    static ColumnId of(final String tableCat, final String tableSchem, final String tableName,
                       final String columnName) {
        return of(tableCat, tableSchem, tableName, columnName, 1);
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableId=" + tableId +
               ",columnName='" + columnName + '\'' +
               ",ordinalPosition=" + ordinalPosition +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ColumnId)) return false;
        final ColumnId that = (ColumnId) obj;
        return tableId.equals(that.tableId)
               && columnName.equals(that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, columnName);
    }

    private final TableId tableId;

    private final String columnName;

    @EqualsAndHashCode.Exclude
    // excluded in equals/hashCode, included in toString
    private final int ordinalPosition;
}
