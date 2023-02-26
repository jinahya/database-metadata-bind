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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding the results of {@link java.sql.DatabaseMetaData#getTableTypes()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTableTypes()
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class TableType extends AbstractMetadataType {

    private static final long serialVersionUID = -7630634982776331078L;

    public static final Comparator<TableType> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(TableType::getTableType, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<TableType> LEXICOGRAPHIC_ORDER = Comparator.comparing(TableType::getTableType);

    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableType=" + tableType +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TableType)) return false;
        final TableType that = (TableType) obj;
        return Objects.equals(tableType, that.tableType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableType);
    }

    @ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
    private String tableType;
}
