/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import static java.sql.DatabaseMetaData.functionColumnIn;
import static java.sql.DatabaseMetaData.functionColumnInOut;
import static java.sql.DatabaseMetaData.functionColumnOut;
import static java.sql.DatabaseMetaData.functionColumnResult;
import static java.sql.DatabaseMetaData.functionColumnUnknown;
import static java.sql.DatabaseMetaData.functionNoNulls;
import static java.sql.DatabaseMetaData.functionNullable;
import static java.sql.DatabaseMetaData.functionNullableUnknown;
import static java.sql.DatabaseMetaData.functionReturn;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for function columns.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String,
 * java.lang.String, java.lang.String)
 */
@XmlRootElement
@XmlType(propOrder = {
    "functionName", "columnName", "columnType", "dataType", "typeName",
    "precision", "length", "scale", "radix", "nullable", "remarks",
    "charOctetLength", "ordinalPosition", "isNullable", "specificName"
})
public class FunctionColumn implements Serializable {

    private static final long serialVersionUID = -7445156446214062680L;

    // -------------------------------------------------------------------------
    private static final Logger logger
            = getLogger(FunctionColumn.class.getName());

    // -------------------------------------------------------------------------
    /**
     * Constants for column types of function columns.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public static enum ColumnType implements IntFieldEnum<ColumnType> {

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

        // ---------------------------------------------------------------------
        /**
         * Returns the constant whose raw value equals to given. An instance of
         * {@link IllegalArgumentException} will be thrown if no constant
         * matches.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given.
         */
        public static ColumnType valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(ColumnType.class, rawValue);
        }

        // ---------------------------------------------------------------------
        private ColumnType(final int rawValue) {
            this.rawValue = rawValue;
        }

        // ---------------------------------------------------------------------
        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        // ---------------------------------------------------------------------
        private final int rawValue;
    }

    /**
     * Constants for nullabilities of columns.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public static enum Nullable implements IntFieldEnum<Nullable> {

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

        // ---------------------------------------------------------------------
        /**
         * Returns the constant whose raw value equals to given. An instance of
         * {@link IllegalArgumentException} will be throw if no constants
         * matches.
         *
         * @param rawValue the value value
         * @return the constant whose raw value equals to given.
         */
        public static Nullable valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(Nullable.class, rawValue);
        }

        // ---------------------------------------------------------------------
        private Nullable(final int rawValue) {
            this.rawValue = rawValue;
        }

        // ---------------------------------------------------------------------
        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        // ---------------------------------------------------------------------
        private final int rawValue;
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
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
               + "}";
    }

    // ------------------------------------------------------------- functionCat
    public String getFunctionCat() {
        return functionCat;
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    // ----------------------------------------------------------- functionSchem
    public String getFunctionSchem() {
        return functionSchem;
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    // ------------------------------------------------------------ functionName
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    // -------------------------------------------------------------- columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------- columnType
    public short getColumnType() {
        return columnType;
    }

    public void setColumnType(final short columnType) {
        this.columnType = columnType;
    }

    // ---------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // ---------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // --------------------------------------------------------------- precision
    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    // ------------------------------------------------------------------ length
    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    // ------------------------------------------------------------------- scale
    public Short getScale() {
        return scale;
    }

    public void setScale(final Short scale) {
        this.scale = scale;
    }

    // ------------------------------------------------------------------- radix
    public short getRadix() {
        return radix;
    }

    public void setRadix(final short radix) {
        this.radix = radix;
    }

    // ---------------------------------------------------------------- nullable
    public short getNullable() {
        return nullable;
    }

    public void setNullable(final short nullable) {
        this.nullable = nullable;
    }

    // ----------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------- charOctetLength
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // --------------------------------------------------------- ordinalPosition
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // -------------------------------------------------------------- isNullable
    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ------------------------------------------------------------ specificName
    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Bind(label = "FUNCTION_CAT", nillable = true)
    private String functionCat;

    @XmlAttribute
    @Bind(label = "FUNCTION_SCHEM", nillable = true)
    private String functionSchem;

    @XmlAttribute
    @Bind(label = "FUNCTION_NAME")
    private String functionName;

    // -------------------------------------------------------------------------
    @XmlElement
    @Bind(label = "COLUMN_NAME")
    private String columnName;

    @XmlElement
    @Bind(label = "COLUMN_TYPE")
    private short columnType;

    @XmlElement
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Bind(label = "TYPE_NAME")
    private String typeName;

    @XmlElement
    @Bind(label = "PRECISION")
    private int precision;

    @XmlElement
    @Bind(label = "LENGTH")
    private int length;

    @XmlElement
    @Bind(label = "SCALE")
    private Short scale;

    @XmlElement
    @Bind(label = "RADIX")
    private short radix;

    @XmlElement
    @Bind(label = "NULLABLE")
    private short nullable;

    @XmlElement
    @Bind(label = "REMARKS")
    private String remarks;

    @XmlElement(nillable = true)
    @Bind(label = "CHAR_OCTET_LENGTH", nillable = true)
    private Integer charOctetLength;

    @XmlElement
    @Bind(label = "ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement
    @Bind(label = "IS_NULLABLE")
    private String isNullable;

    @XmlElement
    @Bind(label = "SPECIFIC_NAME")
    private String specificName;
}
