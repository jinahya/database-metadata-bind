package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 */
@_ChildOf(Function.class)
public class FunctionColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = -7445156446214062680L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>FUNCTION_CAT</code>, <code>FUNCTION_SCHEM</code>, <code>FUNCTION_NAME</code>,
     * <code>SPECIFIC_NAME</code>, and <code>ORDINAL_POSITION</code>.
     * </blockquote>
     *
     * @param operator   a unary operator for adjusting string values; applied only to non-{@code null} values.
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see DatabaseMetaData#getFunctionColumns(String, String, String, String)
     */
    static Comparator<FunctionColumn> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                                final Comparator<? super String> comparator) {
        Objects.requireNonNull(operator, "operator is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final UnaryOperator<String> op = v -> v == null ? null : operator.apply(v);
        return Comparator
                .<FunctionColumn, String>comparing(v -> op.apply(v.getFunctionCat()), comparator)
                .thenComparing(v -> op.apply(v.getFunctionSchem()), comparator)
                .thenComparing(v -> op.apply(v.getFunctionName()), comparator)
                .thenComparing(v -> op.apply(v.getSpecificName()), comparator)
                .thenComparing(FunctionColumn::getOrdinalPosition, Comparator.nullsFirst(Comparator.naturalOrder()));
    }

    // ---------------------------------------------------------------------------------------------------- FUNCTION_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_CAT = "FUNCTION_CAT";

    // -------------------------------------------------------------------------------------------------- FUNCTION_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    // --------------------------------------------------------------------------------------------------- FUNCTION_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_NAME = "FUNCTION_NAME";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // ----------------------------------------------------------------------------------------------------- COLUMN_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_TYPE = "COLUMN_TYPE";

    // ------------------------------------------------------------------------------------------------------- DATA_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // ------------------------------------------------------------------------------------------------------- TYPE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // ------------------------------------------------------------------------------------------------------- PRECISION

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PRECISION = "PRECISION";

    // ---------------------------------------------------------------------------------------------------------- LENGTH

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_LENGTH = "LENGTH";

    // ----------------------------------------------------------------------------------------------------------- SCALE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCALE = "SCALE";

    // ----------------------------------------------------------------------------------------------------------- RADIX

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_RADIX = "RADIX";

    // -------------------------------------------------------------------------------------------------------- NULLABLE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    // --------------------------------------------------------------------------------------------------------- REMARKS

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // ----------------------------------------------------------------------------------------------- CHAR_OCTET_LENGTH

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";

    // ------------------------------------------------------------------------------------------------ ORDINAL_POSITION

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_ORDINAL_POSITION = "ORDINAL_POSITION";

    // ----------------------------------------------------------------------------------------------------- IS_NULLABLE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    // --------------------------------------------------------------------------------------------------- SPECIFIC_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SPECIFIC_NAME = "SPECIFIC_NAME";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    FunctionColumn() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "functionCat=" + functionCat +
               ",functionSchem=" + functionSchem +
               ",functionName=" + functionName +
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
               ",charOctetLength=" + charOctetLength +
               ",ordinalPosition=" + ordinalPosition +
               ",isNullable=" + isNullable +
               ",specificName=" + specificName +
               '}';
    }

    // ----------------------------------------------------------------------------------------------------- functionCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_FUNCTION_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FUNCTION_CAT} column.
     */
    @Nullable
    public String getFunctionCat() {
        return functionCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FUNCTION_CAT} column.
     *
     * @param functionCat the value of {@value #COLUMN_LABEL_FUNCTION_CAT} column.
     */
    void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    // --------------------------------------------------------------------------------------------------- functionSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_FUNCTION_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FUNCTION_SCHEM} column.
     */
    @Nullable
    public String getFunctionSchem() {
        return functionSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FUNCTION_SCHEM} column.
     *
     * @param functionSchem the value of {@value #COLUMN_LABEL_FUNCTION_SCHEM} column.
     */
    void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    // ---------------------------------------------------------------------------------------------------- functionName

    /**
     * Returns the value of {@value #COLUMN_LABEL_FUNCTION_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FUNCTION_NAME} column.
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FUNCTION_NAME} column.
     *
     * @param functionName the value of {@value #COLUMN_LABEL_FUNCTION_NAME} column.
     */
    void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName

    /**
     * Returns the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @param columnName the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    void setColumnName(final String columnName) {
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
    void setColumnType(final Integer columnType) {
        this.columnType = columnType;
    }

    // -------------------------------------------------------------------------------------------------------- dataType

    /**
     * Returns the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @param dataType the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     *
     * @param typeName the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     */
    void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------- precision

    /**
     * Returns the value of {@value #COLUMN_LABEL_PRECISION} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PRECISION} column.
     */
    @Nullable
    public Integer getPrecision() {
        return precision;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PRECISION} column.
     *
     * @param precision the value of {@value #COLUMN_LABEL_PRECISION} column.
     */
    void setPrecision(final Integer precision) {
        this.precision = precision;
    }

    // ---------------------------------------------------------------------------------------------------------- length

    /**
     * Returns the value of {@value #COLUMN_LABEL_LENGTH} column.
     *
     * @return the value of {@value #COLUMN_LABEL_LENGTH} column.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_LENGTH} column.
     *
     * @param length the value of {@value #COLUMN_LABEL_LENGTH} column.
     */
    void setLength(final Integer length) {
        this.length = length;
    }

    // ----------------------------------------------------------------------------------------------------------- scale

    /**
     * Returns the value of {@value #COLUMN_LABEL_SCALE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SCALE} column.
     */
    @Nullable
    public Integer getScale() {
        return scale;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SCALE} column.
     *
     * @param scale the value of {@value #COLUMN_LABEL_SCALE} column.
     */
    void setScale(final Integer scale) {
        this.scale = scale;
    }

    // ----------------------------------------------------------------------------------------------------------- radix

    /**
     * Returns the value of {@value #COLUMN_LABEL_RADIX} column.
     *
     * @return the value of {@value #COLUMN_LABEL_RADIX} column.
     */
    public Integer getRadix() {
        return radix;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_RADIX} column.
     *
     * @param radix the value of {@value #COLUMN_LABEL_RADIX} column.
     */
    void setRadix(final Integer radix) {
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
    void setNullable(final Integer nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------------- remarks

    /**
     * Returns the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @return the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @param remarks the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ------------------------------------------------------------------------------------------------- charOctetLength

    /**
     * Returns the value of {@value #COLUMN_LABEL_CHAR_OCTET_LENGTH} column.
     *
     * @return the value of {@value #COLUMN_LABEL_CHAR_OCTET_LENGTH} column.
     */
    @Nullable
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_CHAR_OCTET_LENGTH} column.
     *
     * @param charOctetLength the value of {@value #COLUMN_LABEL_CHAR_OCTET_LENGTH} column.
     */
    void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition

    /**
     * Returns the value of {@value #COLUMN_LABEL_ORDINAL_POSITION} column.
     *
     * @return the value of {@value #COLUMN_LABEL_ORDINAL_POSITION} column.
     */
    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_ORDINAL_POSITION} column.
     *
     * @param ordinalPosition the value of {@value #COLUMN_LABEL_ORDINAL_POSITION} column.
     */
    void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable

    /**
     * Returns the value of {@value #COLUMN_LABEL_IS_NULLABLE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_IS_NULLABLE} column.
     */
    public String getIsNullable() {
        return isNullable;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_IS_NULLABLE} column.
     *
     * @param isNullable the value of {@value #COLUMN_LABEL_IS_NULLABLE} column.
     */
    void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ---------------------------------------------------------------------------------------------------- specificName

    /**
     * Returns the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     */
    public String getSpecificName() {
        return specificName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     *
     * @param specificName the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     */
    void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    private String functionCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    private String functionSchem;

    @_ColumnLabel(COLUMN_LABEL_FUNCTION_NAME)
    private String functionName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_COLUMN_TYPE)
    private Integer columnType;

    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PRECISION)
    private Integer precision;

    @_ColumnLabel(COLUMN_LABEL_LENGTH)
    private Integer length;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SCALE)
    private Integer scale;

    @_ColumnLabel(COLUMN_LABEL_RADIX)
    private Integer radix;

    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_CHAR_OCTET_LENGTH)
    private Integer charOctetLength;

    @_ColumnLabel(COLUMN_LABEL_ORDINAL_POSITION)
    private Integer ordinalPosition;

    @_ColumnLabel(COLUMN_LABEL_IS_NULLABLE)
    private String isNullable;

    // https://github.com/microsoft/mssql-jdbc/issues/849
    @_ColumnLabel(COLUMN_LABEL_SPECIFIC_NAME)
    private String specificName;
}
