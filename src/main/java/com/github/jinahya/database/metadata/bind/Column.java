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
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumns(String, String, String, String, Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Column
        implements MetadataType,
                   ChildOf<Table> {

    private static final long serialVersionUID = -409653682729081530L;

    /**
     * Constants for {@code NULLABLE} column values from
     * {@link DatabaseMetaData#getColumns(String, String, String, String)}.
     *
     * @see DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#columnNoNulls}({@value java.sql.DatabaseMetaData#columnNoNulls}).
         */
        COLUMN_NO_NULLS(DatabaseMetaData.columnNoNulls), // 0

        /**
         * Constant for {@link DatabaseMetaData#columnNullable}({@value java.sql.DatabaseMetaData#columnNullable}).
         */
        COLUMN_NULLABLE(DatabaseMetaData.columnNullable), // 1

        /**
         * Constant for
         * {@link DatabaseMetaData#columnNullableUnknown}({@value java.sql.DatabaseMetaData#columnNullableUnknown}).
         */
        COLUMN_NULLABLE_UNKNOWN(DatabaseMetaData.columnNullableUnknown); // 2

        /**
         * Returns the constant whose raw value equals to specified value. An {@link IllegalArgumentException} will be
         * thrown if no constants matches.
         *
         * @param rawValue the raw value.
         * @return the constant whose raw value equals to the {@code rawValue}.
         */
        public static Nullable valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Nullable.class, rawValue);
        }

        Nullable(final int rawValue) {
            this.rawValue = rawValue;
        }

        @Override
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    public static final String COLUMN_NAME_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

    public static final String COLUMN_NAME_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @Override
    public Table extractParent() {
        return Table.builder()
                .tableCat(getTableCat())
                .tableSchem(getTableSchem())
                .tableName(getTableName())
                .build();
    }

    @XmlTransient
    public Nullable getNullableAsEnum() {
        return Nullable.valueOfRawValue(getNullable());
    }

    protected void setNullableAsEnum(final Nullable nullableAsEnum) {
        setNullable(Objects.requireNonNull(nullableAsEnum, "nullableAsEnum is null").rawValue());
    }

    // -----------------------------------------------------------------------------------------------------------------
//    @XmlElement(nillable = true, required = true)
    @XmlAttribute
    @NullableBySpecification
    @ColumnLabel("TABLE_CAT")
    private String tableCat;

    //    @XmlElement(nillable = true, required = true)
    @XmlAttribute
    @NullableBySpecification
    @ColumnLabel("TABLE_SCHEM")
    private String tableSchem;

    //    @XmlElement(nillable = false, required = true)
    @XmlAttribute
    @ColumnLabel("TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = false, required = true)
    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement(nillable = true, required = true)
    @ColumnLabel("NULLABLE")
    private int nullable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(nillable = false, required = true)
    @ColumnLabel("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement(nillable = false, required = true)
    @Positive
    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @ColumnLabel(COLUMN_NAME_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @ColumnLabel(COLUMN_NAME_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;

    // -----------------------------------------------------------------------------------------------------------------

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(int numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public int getNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public int getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(int charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(String scopeTable) {
        this.scopeTable = scopeTable;
    }

    public Integer getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(Integer sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

    public void setIsGeneratedcolumn(String isGeneratedcolumn) {
        this.isGeneratedcolumn = isGeneratedcolumn;
    }
}
