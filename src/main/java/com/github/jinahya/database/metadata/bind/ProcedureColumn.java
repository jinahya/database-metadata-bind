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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getProcedureColumns(String, String, String, String)
 */

@_ChildOf(Procedure.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class ProcedureColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3894753719381358829L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<ProcedureColumn> comparing(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(ProcedureColumn::getProcedureCat, comparator)
                .thenComparing(ProcedureColumn::getProcedureSchem, comparator)
                .thenComparing(ProcedureColumn::getProcedureName, comparator)
                .thenComparing(ProcedureColumn::getSpecificName, comparator);
    }

    static Comparator<ProcedureColumn> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return comparing(ContextUtils.nullPrecedence(context, comparator));
    }

    // ----------------------------------------------------------------------------------------------------- COLUMN_TYPE
    public static final String COLUMN_LABEL_COLUMN_TYPE = "COLUMN_TYPE";

    /**
     * A value for {@value #COLUMN_LABEL_COLUMN_TYPE} label. The value is
     * {@link DatabaseMetaData#procedureColumnUnknown}({@value DatabaseMetaData#procedureColumnUnknown}).
     */
    public static final int COLUMN_VALUE_COLUMN_TYPE_PROCEDURE_COLUMN_UNKNOWN = DatabaseMetaData.procedureColumnUnknown;

    public static final int COLUMN_VALUE_COLUMN_TYPE_PROCEDURE_COLUMN_IN = DatabaseMetaData.procedureColumnIn;

    public static final int COLUMN_VALUE_COLUMN_TYPE_PROCEDURE_COLUMN_OUT = DatabaseMetaData.procedureColumnOut; // 4

    public static final int COLUMN_VALUE_COLUMN_TYPE_PROCEDURE_COLUMN_RETURN = DatabaseMetaData.procedureColumnReturn;

    public static final int COLUMN_VALUE_COLUMN_TYPE_PROCEDURE_COLUMN_RESULT = DatabaseMetaData.procedureColumnResult;

    // -------------------------------------------------------------------------------------------------------- NULLABLE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_NAME_NULLABLE = "NULLABLE";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected ProcedureColumn() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "procedureCat=" + procedureCat +
               ",procedureSchem=" + procedureSchem +
               ",procedureName=" + procedureName +
               ",columnName=" + columnName +
               ",columnType=" + columnType +
               ",dataType=" + dataType +
               ",typeName=" + typeName +
               ",precision=" + precision +
               ",length=" + length +
               ",scale=" + scale +
               ",radix=" + radix +
               ",nullable=" + nullable +
               ",remarks=" + remarks +
               ",columnDef=" + columnDef +
               ",sqlDataType=" + sqlDataType +
               ",sqlDatetimeSub=" + sqlDatetimeSub +
               ",charOctetLength=" + charOctetLength +
               ",ordinalPosition=" + ordinalPosition +
               ",isNullable=" + isNullable +
               ",specificName=" + specificName +
               '}';
    }

    // ---------------------------------------------------------------------------------------------------- procedureCat

    
    public String getProcedureCat() {
        return procedureCat;
    }

    protected void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    
    public String getProcedureSchem() {
        return procedureSchem;
    }

    protected void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    public String getProcedureName() {
        return procedureName;
    }

    protected void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    public String getColumnName() {
        return columnName;
    }

    protected void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnType() {
        return columnType;
    }

    protected void setColumnType(final Integer columnType) {
        this.columnType = columnType;
    }

    public Integer getDataType() {
        return dataType;
    }

    protected void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    protected void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    
    public Integer getPrecision() {
        return precision;
    }

    protected void setPrecision(final Integer precision) {
        this.precision = precision;
    }

    public Integer getLength() {
        return length;
    }

    protected void setLength(final Integer length) {
        this.length = length;
    }

    
    public Integer getScale() {
        return scale;
    }

    protected void setScale(final Integer scale) {
        this.scale = scale;
    }

    public Integer getRadix() {
        return radix;
    }

    protected void setRadix(final Integer radix) {
        this.radix = radix;
    }

    public Integer getNullable() {
        return nullable;
    }

    protected void setNullable(final Integer nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    protected void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    
    public String getColumnDef() {
        return columnDef;
    }

    protected void setColumnDef(final String columnDef) {
        this.columnDef = columnDef;
    }

    public Integer getSqlDataType() {
        return sqlDataType;
    }

    protected void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    protected void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    protected void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    protected void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    protected void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    public String getSpecificName() {
        return specificName;
    }

    protected void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    
    @_NullableBySpecification
    @_ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @_ColumnLabel("PROCEDURE_NAME")
    private String procedureName;

    @_ColumnLabel("COLUMN_NAME")

    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_COLUMN_TYPE)
    private Integer columnType;

    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    
    @_NullableBySpecification
    @_ColumnLabel("PRECISION")
    private Integer precision;

    @_ColumnLabel("LENGTH")
    private Integer length;

    
    @_NullableBySpecification
    @_ColumnLabel("SCALE")
    private Integer scale;

    @_ColumnLabel("RADIX")
    private Integer radix;

    @_ColumnLabel("NULLABLE")
    private Integer nullable;

    @_ColumnLabel("REMARKS")
    private String remarks;

    
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @_ReservedBySpecification
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_ReservedBySpecification
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    
    @_NullableBySpecification
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    // https://github.com/microsoft/mssql-jdbc/issues/2320
    @_ColumnLabel("SPECIFIC_NAME")

    private String specificName;
}
