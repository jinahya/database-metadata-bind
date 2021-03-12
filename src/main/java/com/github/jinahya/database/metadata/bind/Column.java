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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An entity class for columns
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class Column implements MetadataType {

    private static final long serialVersionUID = -409653682729081530L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for {@code NULLABLE} column values from {@link DatabaseMetaData#getColumns(String, String, String,
     * String)}.
     *
     * @see DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#columnNoNulls}({@value java.sql.DatabaseMetaData#columnNoNulls}).
         */
        COLUMN_NO_NULLS(DatabaseMetaData.columnNoNulls),

        /**
         * Constant for {@link DatabaseMetaData#columnNullable}({@value java.sql.DatabaseMetaData#columnNullable}).
         */
        COLUMN_NULLABLE(DatabaseMetaData.columnNullable),

        /**
         * Constant for {@link DatabaseMetaData#columnNullableUnknown}({@value java.sql.DatabaseMetaData#columnNullableUnknown}).
         */
        COLUMN_NULLABLE_UNKNOWN(DatabaseMetaData.columnNullableUnknown);

        /**
         * Returns the constant whose raw value equals to given. An {@link IllegalArgumentException} will be thrown if
         * no constants matches.
         *
         * @param rawValue the value value
         * @return the constant whose raw value equals to given.
         */
        public static Nullable valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Nullable.class, rawValue);
        }

        Nullable(final int rawValue) {
            this.rawValue = rawValue;
        }

        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_NAME_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

    public static final String ATTRIBUTE_NAME_IS_AUTOINCREMENT = "isAutoincrement";

    /**
     * Constants for {@value #COLUMN_NAME_IS_AUTOINCREMENT} colum value from the result of {@link
     * DatabaseMetaData#getColumns(String, String, String, String)}.
     */
    public enum IsAutoincrement implements FieldEnum<IsAutoincrement, String> {

        YES("YES"),

        NO("NO"),

        UNKNOWN("");

        /**
         * Returns the constant whose {@link #getRawValue() rawValue} matches to specified value.
         *
         * @param rawValue the value for {@link #getRawValue() rawValue} to match.
         * @return the constant whose {@link #getRawValue() rawValue} matches to {@code rawValue}.
         */
        public static IsAutoincrement valueOfRawValue(final String rawValue) {
            return FieldEnums.valueOfRawValue(IsAutoincrement.class, rawValue);
        }

        IsAutoincrement(final String rawValue) {
            this.rawValue = Objects.requireNonNull(rawValue, "rawValue is null");
        }

        @Override
        public String getRawValue() {
            return rawValue;
        }

        private final String rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_NAME_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    public static final String ATTRIBUTE_NAME_IS_GENERATEDCOLUMN = "isGeneratedcolumn";

    /**
     * Constants for {@value #COLUMN_NAME_IS_GENERATEDCOLUMN} colum value from the result of {@link
     * DatabaseMetaData#getColumns(String, String, String, String)}.
     */
    public enum IsGeneratedcolumn implements FieldEnum<IsGeneratedcolumn, String> {

        YES("YES"),

        NO("NO"),

        UNKNOWN("");

        /**
         * Returns the constant whose {@link #getRawValue() rawValue} matches to specified value.
         *
         * @param rawValue the value for {@link #getRawValue() rawValue} to match.
         * @return the constant whose {@link #getRawValue() rawValue} matches to {@code rawValue}.
         */
        public static IsGeneratedcolumn valueOfRawValue(final String rawValue) {
            return FieldEnums.valueOfRawValue(IsGeneratedcolumn.class, rawValue);
        }

        IsGeneratedcolumn(final String rawValue) {
            this.rawValue = Objects.requireNonNull(rawValue, "rawValue is null");
        }

        @Override
        public String getRawValue() {
            return rawValue;
        }

        private final String rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new column.
     */
    public Column() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "tableCat=" + tableCat
               + ",tableSchem=" + tableSchem
               + ",tableName=" + tableName
               + ",columnName=" + columnName
               + ",dataType=" + dataType
               + ",typeName=" + typeName
               + ",columnSize=" + columnSize
               + ",bufferLength=" + bufferLength
               + ",decimalDigits=" + decimalDigits
               + ",numPrecRadix=" + numPrecRadix
               + ",nullable=" + nullable
               + ",remarks=" + remarks
               + ",columnDef=" + columnDef
               + ",sqlDataType=" + sqlDataType
               + ",sqlDatetimeSub=" + sqlDatetimeSub
               + ",charOctetLength=" + charOctetLength
               + ",ordinalPosition=" + ordinalPosition
               + ",isNullable=" + isNullable
               + ",scopeCatalog=" + scopeCatalog
               + ",scopeSchema=" + scopeSchema
               + ",scopeTable=" + scopeTable
               + ",sourceDataType=" + sourceDataType
               + ",isAutoincrement=" + isAutoincrement
               + ",isGeneratedcolumn=" + isGeneratedcolumn
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Column that = (Column) obj;
        return dataType == that.dataType
               && columnSize == that.columnSize
               && numPrecRadix == that.numPrecRadix
               && nullable == that.nullable
               && charOctetLength == that.charOctetLength
               && ordinalPosition == that.ordinalPosition
               && Objects.equals(tableCat, that.tableCat)
               && Objects.equals(tableSchem, that.tableSchem)
               && Objects.equals(tableName, that.tableName)
               && Objects.equals(columnName, that.columnName)
               && Objects.equals(typeName, that.typeName)
               && Objects.equals(bufferLength, that.bufferLength)
               && Objects.equals(decimalDigits, that.decimalDigits)
               && Objects.equals(remarks, that.remarks)
               && Objects.equals(columnDef, that.columnDef)
               && Objects.equals(sqlDataType, that.sqlDataType)
               && Objects.equals(sqlDatetimeSub, that.sqlDatetimeSub)
               && Objects.equals(isNullable, that.isNullable)
               && Objects.equals(scopeCatalog, that.scopeCatalog)
               && Objects.equals(scopeSchema, that.scopeSchema)
               && Objects.equals(scopeTable, that.scopeTable)
               && Objects.equals(sourceDataType, that.sourceDataType)
               && Objects.equals(isAutoincrement, that.isAutoincrement)
               && Objects.equals(isGeneratedcolumn, that.isGeneratedcolumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat,
                            tableSchem,
                            tableName,
                            columnName,
                            dataType,
                            typeName,
                            columnSize,
                            bufferLength,
                            decimalDigits,
                            numPrecRadix,
                            nullable,
                            remarks,
                            columnDef,
                            sqlDataType,
                            sqlDatetimeSub,
                            charOctetLength,
                            ordinalPosition,
                            isNullable,
                            scopeCatalog,
                            scopeSchema,
                            scopeTable,
                            sourceDataType,
                            isAutoincrement,
                            isGeneratedcolumn);
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize
    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    // ---------------------------------------------------------------------------------------------------- bufferLength
    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- numPrecRadix
    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(final int numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public int getNullable() {
        return nullable;
    }

    public void setNullable(final int nullable) {
        this.nullable = nullable;
    }

    public Nullable getNullableAsEnum() {
        return Nullable.valueOfRawValue(getNullable());
    }

    public void setNullableAsEnum(final Nullable nullableAsEnum) {
        Objects.requireNonNull(nullableAsEnum, "nullableAsEnum is null");
        setNullable(nullableAsEnum.getRawValue());
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ------------------------------------------------------------------------------------------------------- columnDef
    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(final String columnDef) {
        this.columnDef = columnDef;
    }

    // ----------------------------------------------------------------------------------------------------- sqlDataType
    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    // -------------------------------------------------------------------------------------------------- sqlDatetimeSub
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    // ------------------------------------------------------------------------------------------------- charOctetLength
    public int getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final int charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable
    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ---------------------------------------------------------------------------------------------------- scopeCatalog
    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(final String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    // ----------------------------------------------------------------------------------------------------- scopeSchema
    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(final String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    // ------------------------------------------------------------------------------------------------------ scopeTable
    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(final String scopeTable) {
        this.scopeTable = scopeTable;
    }

    // -------------------------------------------------------------------------------------------------- sourceDataType
    public Short getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(final Short sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    // ------------------------------------------------------------------------------------------------- isAutoincrement
    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(final String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    public IsAutoincrement getIsAutoincrementAsEnum() {
        return Optional.ofNullable(getIsAutoincrement()).map(IsAutoincrement::valueOfRawValue).orElse(null);
    }

    public void setIsAutoincrementAsEnum(final IsAutoincrement isAutoincrementAsEnum) {
        setIsAutoincrement(Optional.ofNullable(isAutoincrementAsEnum).map(FieldEnum::getRawValue).orElse(null));
    }

    // ----------------------------------------------------------------------------------------------- isGeneratedcolumn
    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

    public void setIsGeneratedcolumn(final String isGeneratedcolumn) {
        this.isGeneratedcolumn = isGeneratedcolumn;
    }

    public IsGeneratedcolumn getIsGeneratedcolumnAsEnum() {
        return Optional.ofNullable(getIsGeneratedcolumn()).map(IsGeneratedcolumn::valueOfRawValue).orElse(null);
    }

    public void setIsGeneratedcolumnAsEnum(final IsGeneratedcolumn isGeneratedcolumnAsEnum) {
        setIsGeneratedcolumn(Optional.ofNullable(isGeneratedcolumnAsEnum).map(FieldEnum::getRawValue).orElse(null));
    }

    // ------------------------------------------------------------------------------------------------ columnPrivileges

    /**
     * Returns column privileges of this column.
     *
     * @return column privileges of this column.
     */
    public @NotNull List<@Valid @NotNull ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<>();
        }
        return columnPrivileges;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true)
    @Label("COLUMN_SIZE")
    private int columnSize;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("BUFFER_LENGTH")
    private Integer bufferLength;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(required = true)
    @Label("NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement(required = true)
    @Label("NULLABLE")
    private int nullable;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("COLUMN_DEF")
    private String columnDef;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(required = true)
    @Label("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(required = true)
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCOPE_CATALOG")
    private String scopeCatalog;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCOPE_SCHEMA")
    private String scopeSchema;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCOPE_TABLE")
    private String scopeTable;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SOURCE_DATA_TYPE")
    private Short sourceDataType;

    @XmlElement(required = true)
    @Label(COLUMN_NAME_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @XmlElement(required = true)
    @Label(COLUMN_NAME_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull ColumnPrivilege> columnPrivileges;
}
