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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Objects;

import static java.sql.DatabaseMetaData.*;

/**
 * An entity class for function columns.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 */
@XmlRootElement
public class FunctionColumn extends FunctionChild {

    private static final long serialVersionUID = -7445156446214062680L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for column types of function columns.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public enum ColumnType implements IntFieldEnum<ColumnType> {

        /**
         * Constant for {@link DatabaseMetaData#functionColumnUnknown}.
         */
        FUNCTION_COLUMN_UNKNOWN(functionColumnUnknown), // 0

        /**
         * Constants for {@link DatabaseMetaData#functionColumnIn}.
         */
        FUNCTION_COLUMN_IN(functionColumnIn), // 1

        /**
         * Constants for {@link DatabaseMetaData#functionColumnInOut}.
         */
        FUNCTION_COLUMN_IN_OUT(functionColumnInOut), // 2

        /**
         * Constants for {@link DatabaseMetaData#functionColumnOut}.
         */
        FUNCTION_COLUMN_OUT(functionColumnOut), // 3

        /**
         * Constant for {@link DatabaseMetaData#functionReturn}.
         */
        // https://stackoverflow.com/a/46647586/330457
        FUNCTION_COLUMN_RETURN(functionReturn), // 4

        /**
         * Constants for {@link DatabaseMetaData#functionColumnResult}.
         */
        FUNCTION_COLUMN_RESULT(functionColumnResult); // 5

        /**
         * Returns the constant whose raw value equals to given. An instance of {@link IllegalArgumentException} will be
         * thrown if no constant matches.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given.
         */
        public static ColumnType valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(ColumnType.class, rawValue);
        }

        ColumnType(final int rawValue) {
            this.rawValue = rawValue;
        }

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    /**
     * Constants for nullabilities of columns.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#functionNoNulls}.
         */
        FUNCTION_NO_NULLS(functionNoNulls),

        /**
         * Constant for {@link DatabaseMetaData#functionNullable}.
         */
        FUNCTION_NULLABLE(functionNullable),

        /**
         * Constant for {@link DatabaseMetaData#functionNullableUnknown}.
         */
        FUNCTION_NULLABLE_UNKNOWN(functionNullableUnknown);

        /**
         * Returns the constant whose raw value equals to given. An instance of {@link IllegalArgumentException} will be
         * throw if no constants matches.
         *
         * @param rawValue the value value
         * @return the constant whose raw value equals to given.
         */
        public static Nullable valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(Nullable.class, rawValue);
        }

        Nullable(final int rawValue) {
            this.rawValue = rawValue;
        }

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public FunctionColumn() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
                + "functionCat=" + functionCat
                + ",functionSchem=" + functionSchem
                + ",functionName=" + functionName
                + ",columnName=" + columnName
                + ",columnType=" + columnType
                + ",dataType=" + dataType
                + ",typeName=" + typeName
                + ",precision=" + precision
                + ",length=" + length
                + ",scale=" + scale
                + ",radix=" + radix
                + ",nullable=" + nullable
                + ",remarks=" + remarks
                + ",charOctetLength=" + charOctetLength
                + ",ordinalPosition=" + ordinalPosition
                + ",isNullable=" + isNullable
                + ",specificName=" + specificName
                + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final FunctionColumn that = (FunctionColumn) obj;
        return columnType == that.columnType
                && dataType == that.dataType
                && precision == that.precision
                && length == that.length
                && radix == that.radix
                && nullable == that.nullable
                && ordinalPosition == that.ordinalPosition
                && Objects.equals(functionCat, that.functionCat)
                && Objects.equals(functionSchem, that.functionSchem)
                && Objects.equals(functionName, that.functionName)
                && Objects.equals(columnName, that.columnName)
                && Objects.equals(typeName, that.typeName)
                && Objects.equals(scale, that.scale)
                && Objects.equals(remarks, that.remarks)
                && Objects.equals(charOctetLength, that.charOctetLength)
                && Objects.equals(isNullable, that.isNullable)
                && Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionCat,
                functionSchem,
                functionName,
                columnName,
                columnType,
                dataType,
                typeName,
                precision,
                length,
                scale,
                radix,
                nullable,
                remarks,
                charOctetLength,
                ordinalPosition,
                isNullable,
                specificName);
    }

    // ----------------------------------------------------------------------------------------------------- functionCat
    public String getFunctionCat() {
        return functionCat;
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    // --------------------------------------------------------------------------------------------------- functionSchem
    public String getFunctionSchem() {
        return functionSchem;
    }

    public void setFunctionSchem(final String functionSchem) {
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
    public short getColumnType() {
        return columnType;
    }

    public void setColumnType(final short columnType) {
        this.columnType = columnType;
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

    // ------------------------------------------------------------------------------------------------------- precision
    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    // ---------------------------------------------------------------------------------------------------------- length
    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    // ----------------------------------------------------------------------------------------------------------- scale
    public Short getScale() {
        return scale;
    }

    public void setScale(final Short scale) {
        this.scale = scale;
    }

    // ----------------------------------------------------------------------------------------------------------- radix
    public short getRadix() {
        return radix;
    }

    public void setRadix(final short radix) {
        this.radix = radix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public short getNullable() {
        return nullable;
    }

    public void setNullable(final short nullable) {
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
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final Integer charOctetLength) {
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

    // ---------------------------------------------------------------------------------------------------- specificName
    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("FUNCTION_CAT")
    private String functionCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("FUNCTION_SCHEM")
    private String functionSchem;

    @XmlElement(required = true)
    @Label("FUNCTION_NAME")
    private String functionName;

    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    @Bind(label = "COLUMN_NAME")
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("COLUMN_TYPE")
    private short columnType;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true)
    @Label("PRECISION")
    private int precision;

    @XmlElement(required = true)
    @Label("LENGTH")
    private int length;

    // https://issues.apache.org/jira/browse/DERBY-7102
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCALE")
    private Short scale;

    @XmlElement(required = true)
    @Label("RADIX")
    @Bind(label = "RADIX")
    private short radix;

    @XmlElement(required = true)
    @Label("NULLABLE")
    @Bind(label = "NULLABLE")
    private short nullable;

    @XmlElement(required = true, nillable = true)
    @MayBeNullByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7100
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(required = true)
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(required = true)
    @Label("SPECIFIC_NAME")
    private String specificName;
}
