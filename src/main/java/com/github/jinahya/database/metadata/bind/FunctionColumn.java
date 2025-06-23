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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 */
@_ChildOf(Function.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class FunctionColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = -7445156446214062680L;

    // -----------------------------------------------------------------------------------------------------------------

    static Comparator<FunctionColumn> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return Comparator.comparing(FunctionColumn::getFunctionCat, comparator)
                .thenComparing(FunctionColumn::getFunctionSchem, comparator)
                .thenComparing(FunctionColumn::getFunctionName, comparator)
                .thenComparing(FunctionColumn::getSpecificName, comparator);
    }

    static Comparator<FunctionColumn> comparingInSpecifiedOrder(final Context context,
                                                                final Comparator<? super String> comparator)
            throws SQLException {
        return comparingInSpecifiedOrder(
                ContextUtils.nullPrecedence(context, comparator)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_FUNCTION_CAT = "FUNCTION_CAT";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_FUNCTION_NAME = "FUNCTION_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_COLUMN_TYPE = "COLUMN_TYPE";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    public FunctionColumn() {
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
    @Nullable
    public String getFunctionCat() {
        return functionCat;
    }

    public void setFunctionCat(@Nullable final String functionCat) {
        this.functionCat = functionCat;
    }

    // --------------------------------------------------------------------------------------------------- functionSchem
    @Nullable
    public String getFunctionSchem() {
        return functionSchem;
    }

    public void setFunctionSchem(@Nullable final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    // ---------------------------------------------------------------------------------------------------- functionName
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ------------------------------------------------------------------------------------------------------ columnType
    public Integer getColumnType() {
        return columnType;
    }

    public void setColumnType(final Integer columnType) {
        this.columnType = columnType;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------- precision
    @Nullable
    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(@Nullable final Integer precision) {
        this.precision = precision;
    }

    // ---------------------------------------------------------------------------------------------------------- length
    public Integer getLength() {
        return length;
    }

    public void setLength(final Integer length) {
        this.length = length;
    }

    // ----------------------------------------------------------------------------------------------------------- scale
    @Nullable
    public Integer getScale() {
        return scale;
    }

    public void setScale(@Nullable final Integer scale) {
        this.scale = scale;
    }

    // ----------------------------------------------------------------------------------------------------------- radix
    public Integer getRadix() {
        return radix;
    }

    public void setRadix(final Integer radix) {
        this.radix = radix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(final Integer nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ------------------------------------------------------------------------------------------------- charOctetLength
    @Nullable
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(@Nullable final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition
    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable
    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ---------------------------------------------------------------------------------------------------- specificName
    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
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
    @_ColumnLabel("COLUMN_TYPE")
    private Integer columnType;

    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("PRECISION")
    private Integer precision;

    @_ColumnLabel("LENGTH")
    private Integer length;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCALE")
    private Integer scale;

    @_ColumnLabel("RADIX")
    private Integer radix;

    @_ColumnLabel("NULLABLE")
    private Integer nullable;

    @_ColumnLabel("REMARKS")
    private String remarks;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @PositiveOrZero
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @Pattern(regexp = MetadataTypeConstants.PATTERN_REGEXP_YES_NO_OR_EMPTY)
    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    // https://github.com/microsoft/mssql-jdbc/issues/849
    @_ColumnLabel("SPECIFIC_NAME")
    private String specificName;
}
