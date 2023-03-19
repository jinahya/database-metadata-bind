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

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[])
 */
@Setter
@Getter
public class Table extends AbstractMetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    static final Comparator<Table> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Table::getTableType, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Table::getTableCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Table::getTableSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Table::getTableName, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<Table> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Table::getTableType, nullsFirst(naturalOrder()))
                    .thenComparing(Table::getTableCat, nullsFirst(naturalOrder()))
                    .thenComparing(Table::getTableSchem, nullsFirst(naturalOrder()))
                    .thenComparing(Table::getTableName, naturalOrder());

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
    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    /**
     * Creates a new instance.
     */
    public Table() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",tableType=" + tableType +
               ",remarks=" + remarks +
               ",typeCat=" + typeCat +
               ",typeSchem=" + typeSchem +
               ",typeName=" + typeName +
               ",selfReferencingColName=" + selfReferencingColName +
               ",refGeneration=" + refGeneration +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Table)) return false;
        final Table that = (Table) obj;
        return Objects.equals(tableCatNonNull(), that.getTableName()) &&
               Objects.equals(tableSchemNonNull(), that.tableSchemNonNull()) &&
               Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                tableCatNonNull(),
                tableSchemNonNull(),
                tableName
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

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_NullableByVendor("MariaDB")
    @_ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
    private String tableType;

    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @_ColumnLabel("TYPE_CAT")
    private String typeCat;

    @_NullableBySpecification
    @_ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @_NullableBySpecification
    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_NullableBySpecification
    @_ColumnLabel("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @_NullableBySpecification
    @_ColumnLabel("REF_GENERATION")
    private String refGeneration;

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
