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

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@_ChildOf(Table.class)
public class PrimaryKey extends AbstractMetadataType {

    private static final long serialVersionUID = 3159826510060898330L;

    static final Comparator<PrimaryKey> CASE_INSENSITIVE_ORDER
            = Comparator.comparing(PrimaryKey::getColumnName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<PrimaryKey> LEXICOGRAPHIC_ORDER
            = Comparator.comparing(PrimaryKey::getColumnName, nullsFirst(naturalOrder()));

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_KEY_SEQ = "KEY_SEQ";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PK_NAME = "PK_NAME";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",columnName=" + columnName +
               ",keySeq=" + keySeq +
               ",pkName=" + pkName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PrimaryKey)) return false;
        final PrimaryKey that = (PrimaryKey) obj;
        return Objects.equals(tableCatNonNull(), that.tableCatNonNull()) &&
               Objects.equals(tableSchemNonNull(), that.tableSchemNonNull()) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                tableCatNonNull(),
                tableSchemNonNull(),
                tableName,
                columnName
        );
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    public Integer getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(final Integer keySeq) {
        this.keySeq = keySeq;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(final String pkName) {
        this.pkName = pkName;
    }

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_KEY_SEQ)
    private Integer keySeq;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PK_NAME)
    private String pkName;

    String tableCatNonNull() {
        final String tableCat_ = getTableCat();
        if (tableCat_ != null) {
            return tableCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String tableSchemNonNull() {
        final String tableSchem_ = getTableSchem();
        if (tableSchem_ != null) {
            return tableSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }
}
