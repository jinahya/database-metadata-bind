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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_CAT = "PROCEDURE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_SCHEM = "PROCEDURE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_NAME = "PROCEDURE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PRECISION = "PRECISION";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_LENGTH = "LENGTH";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCALE = "SCALE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_RADIX = "RADIX";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_DEF = "COLUMN_DEF";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SQL_DATA_TYPE = "SQL_DATA_TYPE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SQL_DATETIME_SUB = "SQL_DATETIME_SUB";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_ORDINAL_POSITION = "ORDINAL_POSITION";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SPECIFIC_NAME = "SPECIFIC_NAME";

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

    /**
     * Returns the value of {@code PROCEDURE_CAT} column.
     *
     * @return the value of {@code PROCEDURE_CAT} column.
     */
    public String getProcedureCat() {
        return procedureCat;
    }

    /**
     * Sets the value of {@code PROCEDURE_CAT} column.
     *
     * @param procedureCat the value of {@code PROCEDURE_CAT} column.
     */
    protected void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    // -------------------------------------------------------------------------------------------------- procedureSchem

    /**
     * Returns the value of {@code PROCEDURE_SCHEM} column.
     *
     * @return the value of {@code PROCEDURE_SCHEM} column.
     */
    public String getProcedureSchem() {
        return procedureSchem;
    }

    /**
     * Sets the value of {@code PROCEDURE_SCHEM} column.
     *
     * @param procedureSchem the value of {@code PROCEDURE_SCHEM} column.
     */
    protected void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    // --------------------------------------------------------------------------------------------------- procedureName

    /**
     * Returns the value of {@code PROCEDURE_NAME} column.
     *
     * @return the value of {@code PROCEDURE_NAME} column.
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * Sets the value of {@code PROCEDURE_NAME} column.
     *
     * @param procedureName the value of {@code PROCEDURE_NAME} column.
     */
    protected void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName

    /**
     * Returns the value of {@code COLUMN_NAME} column.
     *
     * @return the value of {@code COLUMN_NAME} column.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the value of {@code COLUMN_NAME} column.
     *
     * @param columnName the value of {@code COLUMN_NAME} column.
     */
    protected void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ------------------------------------------------------------------------------------------------------ columnType

    /**
     * Returns the value of {@value #COLUMN_LABEL_COLUMN_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_COLUMN_TYPE} column.
     */
    public Integer getColumnType() {
        return columnType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_COLUMN_TYPE} column.
     *
     * @param columnType the value of {@value #COLUMN_LABEL_COLUMN_TYPE} column.
     */
    protected void setColumnType(final Integer columnType) {
        this.columnType = columnType;
    }

    // -------------------------------------------------------------------------------------------------------- dataType

    /**
     * Returns the value of {@code DATA_TYPE} column.
     *
     * @return the value of {@code DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@code DATA_TYPE} column.
     *
     * @param dataType the value of {@code DATA_TYPE} column.
     */
    protected void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName

    /**
     * Returns the value of {@code TYPE_NAME} column.
     *
     * @return the value of {@code TYPE_NAME} column.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of {@code TYPE_NAME} column.
     *
     * @param typeName the value of {@code TYPE_NAME} column.
     */
    protected void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------- precision

    /**
     * Returns the value of {@code PRECISION} column.
     *
     * @return the value of {@code PRECISION} column.
     */
    public Integer getPrecision() {
        return precision;
    }

    /**
     * Sets the value of {@code PRECISION} column.
     *
     * @param precision the value of {@code PRECISION} column.
     */
    protected void setPrecision(final Integer precision) {
        this.precision = precision;
    }

    // ---------------------------------------------------------------------------------------------------------- length

    /**
     * Returns the value of {@code LENGTH} column.
     *
     * @return the value of {@code LENGTH} column.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Sets the value of {@code LENGTH} column.
     *
     * @param length the value of {@code LENGTH} column.
     */
    protected void setLength(final Integer length) {
        this.length = length;
    }

    // ----------------------------------------------------------------------------------------------------------- scale

    /**
     * Returns the value of {@code SCALE} column.
     *
     * @return the value of {@code SCALE} column.
     */
    public Integer getScale() {
        return scale;
    }

    /**
     * Sets the value of {@code SCALE} column.
     *
     * @param scale the value of {@code SCALE} column.
     */
    protected void setScale(final Integer scale) {
        this.scale = scale;
    }

    // ----------------------------------------------------------------------------------------------------------- radix

    /**
     * Returns the value of {@code RADIX} column.
     *
     * @return the value of {@code RADIX} column.
     */
    public Integer getRadix() {
        return radix;
    }

    /**
     * Sets the value of {@code RADIX} column.
     *
     * @param radix the value of {@code RADIX} column.
     */
    protected void setRadix(final Integer radix) {
        this.radix = radix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable

    /**
     * Returns the value of {@value #COLUMN_LABEL_NULLABLE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_NULLABLE} column.
     */
    public Integer getNullable() {
        return nullable;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_NULLABLE} column.
     *
     * @param nullable the value of {@value #COLUMN_LABEL_NULLABLE} column.
     */
    protected void setNullable(final Integer nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------------- remarks

    /**
     * Returns the value of {@code REMARKS} column.
     *
     * @return the value of {@code REMARKS} column.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@code REMARKS} column.
     *
     * @param remarks the value of {@code REMARKS} column.
     */
    protected void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ------------------------------------------------------------------------------------------------------- columnDef

    /**
     * Returns the value of {@code COLUMN_DEF} column.
     *
     * @return the value of {@code COLUMN_DEF} column.
     */
    public String getColumnDef() {
        return columnDef;
    }

    /**
     * Sets the value of {@code COLUMN_DEF} column.
     *
     * @param columnDef the value of {@code COLUMN_DEF} column.
     */
    protected void setColumnDef(final String columnDef) {
        this.columnDef = columnDef;
    }

    // ----------------------------------------------------------------------------------------------------- sqlDataType

    /**
     * Returns the value of {@code SQL_DATA_TYPE} column.
     *
     * @return the value of {@code SQL_DATA_TYPE} column.
     */
    public Integer getSqlDataType() {
        return sqlDataType;
    }

    /**
     * Sets the value of {@code SQL_DATA_TYPE} column.
     *
     * @param sqlDataType the value of {@code SQL_DATA_TYPE} column.
     */
    protected void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    // -------------------------------------------------------------------------------------------------- sqlDatetimeSub

    /**
     * Returns the value of {@code SQL_DATETIME_SUB} column.
     *
     * @return the value of {@code SQL_DATETIME_SUB} column.
     */
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    /**
     * Sets the value of {@code SQL_DATETIME_SUB} column.
     *
     * @param sqlDatetimeSub the value of {@code SQL_DATETIME_SUB} column.
     */
    protected void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    // ------------------------------------------------------------------------------------------------- charOctetLength

    /**
     * Returns the value of {@code CHAR_OCTET_LENGTH} column.
     *
     * @return the value of {@code CHAR_OCTET_LENGTH} column.
     */
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    /**
     * Sets the value of {@code CHAR_OCTET_LENGTH} column.
     *
     * @param charOctetLength the value of {@code CHAR_OCTET_LENGTH} column.
     */
    protected void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition

    /**
     * Returns the value of {@code ORDINAL_POSITION} column.
     *
     * @return the value of {@code ORDINAL_POSITION} column.
     */
    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
     * Sets the value of {@code ORDINAL_POSITION} column.
     *
     * @param ordinalPosition the value of {@code ORDINAL_POSITION} column.
     */
    protected void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable

    /**
     * Returns the value of {@code IS_NULLABLE} column.
     *
     * @return the value of {@code IS_NULLABLE} column.
     */
    public String getIsNullable() {
        return isNullable;
    }

    /**
     * Sets the value of {@code IS_NULLABLE} column.
     *
     * @param isNullable the value of {@code IS_NULLABLE} column.
     */
    protected void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ---------------------------------------------------------------------------------------------------- specificName

    /**
     * Returns the value of {@code SPECIFIC_NAME} column.
     *
     * @return the value of {@code SPECIFIC_NAME} column.
     */
    public String getSpecificName() {
        return specificName;
    }

    /**
     * Sets the value of {@code SPECIFIC_NAME} column.
     *
     * @param specificName the value of {@code SPECIFIC_NAME} column.
     */
    protected void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_CAT)
    private String procedureCat;

    
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_SCHEM)
    private String procedureSchem;

    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_NAME)
    private String procedureName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_COLUMN_TYPE)
    private Integer columnType;

    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PRECISION)
    private Integer precision;

    @_ColumnLabel(COLUMN_LABEL_LENGTH)
    private Integer length;

    
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SCALE)
    private Integer scale;

    @_ColumnLabel(COLUMN_LABEL_RADIX)
    private Integer radix;

    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_DEF)
    private String columnDef;

    @_ReservedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SQL_DATA_TYPE)
    private Integer sqlDataType;

    @_ReservedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SQL_DATETIME_SUB)
    private Integer sqlDatetimeSub;

    
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_CHAR_OCTET_LENGTH)
    private Integer charOctetLength;

    @_ColumnLabel(COLUMN_LABEL_ORDINAL_POSITION)
    private Integer ordinalPosition;

    @_ColumnLabel(COLUMN_LABEL_IS_NULLABLE)
    private String isNullable;

    // https://github.com/microsoft/mssql-jdbc/issues/2320
    @_ColumnLabel(COLUMN_LABEL_SPECIFIC_NAME)
    private String specificName;
}
