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

import java.util.Comparator;
import java.util.Objects;

/**
 * An identifier for identifying a {@link Column} within a {@link Table}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class ColumnId extends AbstractMetadataTypeId<ColumnId, Column> {

    private static final long serialVersionUID = -4452694121211962289L;

    static final Comparator<ColumnId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ColumnId::getTableId, TableId.CASE_INSENSITIVE_ORDER);

    static final Comparator<ColumnId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ColumnId::getTableId, TableId.LEXICOGRAPHIC_ORDER);

    static ColumnId of(final TableId tableId, final String columnName) {
        Objects.requireNonNull(tableId, "tableId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        return new ColumnId(tableId, columnName);
    }

    ColumnId(final TableId tableId, final String columnName) {
        super();
        this.tableId = Objects.requireNonNull(tableId, "tableId is null");
        this.columnName = Objects.requireNonNull(columnName, "columnName is null");
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
        if (!(obj instanceof ColumnId)) return false;
        final ColumnId that = (ColumnId) obj;
        return tableId.equals(that.tableId) &&
               columnName.equals(that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, columnName);
    }

    public TableId getTableId() {
        return tableId;
    }

    public String getColumnName() {
        return columnName;
    }

    private final TableId tableId;

    private final String columnName;
}
