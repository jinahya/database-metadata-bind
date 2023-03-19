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

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumns(String, String, String, String)
 * @see Nullable
 */
//@ParentOf(ColumnPrivilege.class)
@Setter
@Getter
public class Column extends AbstractMetadataType {

    private static final long serialVersionUID = -409653682729081530L;

    static final Comparator<Column> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Column::getTableCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Column::getTableSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Column::getTableName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparingInt(Column::getOrdinalPosition);

    static final Comparator<Column> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Column::getTableCat, nullsFirst(naturalOrder()))
                    .thenComparing(Column::getTableSchem, nullsFirst(naturalOrder()))
                    .thenComparing(Column::getTableName, nullsFirst(naturalOrder()))
                    .thenComparingInt(Column::getOrdinalPosition);

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
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    /**
     * Constants for {@value #COLUMN_LABEL_NULLABLE} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum Nullable implements _IntFieldEnum<Nullable> {

        /**
         * A value for {@link DatabaseMetaData#columnNoNulls}({@value DatabaseMetaData#columnNoNulls}).
         */
        COLUMN_NO_NULLS(DatabaseMetaData.columnNoNulls),// 0

        /**
         * A value for {@link DatabaseMetaData#columnNullable}({@value DatabaseMetaData#columnNullable}).
         */
        COLUMN_NULLABLE(DatabaseMetaData.columnNullable), // 1

        /**
         * A value for {@link DatabaseMetaData#columnNullableUnknown}({@value DatabaseMetaData#columnNullableUnknown}).
         */
        COLUMN_NULLABLE_UNKNOWN(DatabaseMetaData.columnNullableUnknown) // 2
        ;

        /**
         * Finds the value for specified {@link Column#COLUMN_LABEL_NULLABLE} column value.
         *
         * @param nullable the value of {@link Column#COLUMN_LABEL_NULLABLE} column to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static Nullable valueOfNullable(final int nullable) {
            return _IntFieldEnum.valueOfFieldValue(Nullable.class, nullable);
        }

        Nullable(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    public static final String COLUMN_LABEL_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

    public static final String COLUMN_LABEL_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",columnName=" + columnName +
               ",dataType=" + dataType +
               ",typeName=" + typeName +
               ",columnSize=" + columnSize +
               ",bufferLength=" + bufferLength +
               ",decimalDigits=" + decimalDigits +
               ",numPrecRadix=" + numPrecRadix +
               ",nullable=" + nullable +
               ",remarks=" + remarks +
               ",columnDef=" + columnDef +
               ",sqlDataType=" + sqlDataType +
               ",sqlDatetimeSub=" + sqlDatetimeSub +
               ",charOctetLength=" + charOctetLength +
               ",ordinalPosition=" + ordinalPosition +
               ",isNullable=" + isNullable +
               ",scopeCatalog=" + scopeCatalog +
               ",scopeSchema=" + scopeSchema +
               ",scopeTable=" + scopeTable +
               ",sourceDataType=" + sourceDataType +
               ",isAutoincrement=" + isAutoincrement +
               ",isGeneratedcolumn=" + isGeneratedcolumn +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Column)) return false;
        final Column that = (Column) obj;
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

    /**
     * Returns current value of {@code tableCat} field.
     *
     * @return current value of {@code tableCat} field.
     */
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Replaces current value of {@code tableCat} field with specified value.
     *
     * @param tableCat new value for {@code tableCat} field.
     */
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

    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(final Integer nullable) {
        this.nullable = nullable;
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
    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_NotUsedBySpecification
    @_ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_NullableByVendor("Apache Derby")
    @_NotNull
    @_ColumnLabel("NUM_PREC_RADIX")
    private Integer numPrecRadix;

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @_ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @_NullableByVendor("Apache Derby")
    @_NotNull
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @_NotNull
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @_NullableBySpecification
    @_ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @_NullableBySpecification
    @_ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @_NullableBySpecification
    @_ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    @_NullableBySpecification
    @_ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;

    @_ColumnLabel(COLUMN_LABEL_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @_ColumnLabel(COLUMN_LABEL_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;

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

    Nullable getNullableAsEnum() {
        return Optional.ofNullable(getNullable())
                .map(Nullable::valueOfNullable)
                .orElse(null);
    }

    void setNullableAsEnum(final Nullable nullableAsEnum) {
        setNullable(
                Optional.ofNullable(nullableAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }
}
