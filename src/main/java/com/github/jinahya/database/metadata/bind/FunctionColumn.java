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
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding results of {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String, Collection)
 */
@XmlRootElement
@ChildOf(Function.class)
public class FunctionColumn
        implements MetadataType {

    private static final long serialVersionUID = -7445156446214062680L;

    // ---------------------------------------------------------------------------------------- COLUMN_TYPE / columnType
    public static final String COLUMN_NAME_COLUMN_TYPE = "COLUMN_TYPE";

    public static final String ATTRIBUTE_NAME_COLUMN_TYPE = "columnType";

    // --------------------------------------------------------------------------------------------- NULLABLE / nullable
    public static final String COLUMN_NAME_NULLABLE = "NULLABLE";

    public static final String ATTRIBUTE_NAME_NULLABLE = "nullable";

    // ---------------------------------------------------------------------------------------- IS_NULLABLE / isNullable
    public static final String COLUMN_NAME_IS_NULLABLE = "IS_NULLABLE";

    public static final String ATTRIBUTE_NAME_IS_NULLABLE = "isNullable";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for {@link #COLUMN_NAME_COLUMN_TYPE} column values of the results of {@link
     * DatabaseMetaData#getFunctionColumns(String, String, String, String)}.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @XmlEnum
    public enum ColumnType implements IntFieldEnum<ColumnType> {

        /**
         * Constant for {@link DatabaseMetaData#functionColumnUnknown}({@value java.sql.DatabaseMetaData#functionColumnUnknown}).
         */
        FUNCTION_COLUMN_UNKNOWN(DatabaseMetaData.functionColumnUnknown), // 0

        /**
         * Constants for {@link DatabaseMetaData#functionColumnIn}({@value java.sql.DatabaseMetaData#functionColumnIn}).
         */
        FUNCTION_COLUMN_IN(DatabaseMetaData.functionColumnIn), // 1

        /**
         * Constants for {@link DatabaseMetaData#functionColumnInOut}({@value java.sql.DatabaseMetaData#functionColumnInOut}).
         */
        FUNCTION_COLUMN_IN_OUT(DatabaseMetaData.functionColumnInOut), // 2

        /**
         * Constants for {@link DatabaseMetaData#functionColumnOut}({@value java.sql.DatabaseMetaData#functionColumnOut}).
         */
        FUNCTION_COLUMN_OUT(DatabaseMetaData.functionColumnOut), // 3

        /**
         * Constant for {@link DatabaseMetaData#functionReturn}({@value java.sql.DatabaseMetaData#functionReturn}).
         */
        // https://stackoverflow.com/a/46647586/330457
        FUNCTION_COLUMN_RETURN(DatabaseMetaData.functionReturn), // 4

        /**
         * Constants for {@link DatabaseMetaData#functionColumnResult}({@value java.sql.DatabaseMetaData#functionColumnResult}).
         */
        FUNCTION_COLUMN_RESULT(DatabaseMetaData.functionColumnResult); // 5

        /**
         * Returns the constant whose raw value equals to given. An {@link IllegalArgumentException} will be thrown if
         * no constant matches.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given.
         */
        public static ColumnType valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(ColumnType.class, rawValue);
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
     * Constants for {@link #COLUMN_NAME_NULLABLE} column values of the results of {@link
     * DatabaseMetaData#getFunctionColumns(String, String, String, String)}.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @XmlEnum
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#functionNoNulls}({@value java.sql.DatabaseMetaData#functionNoNulls}).
         */
        FUNCTION_NO_NULLS(DatabaseMetaData.functionNoNulls),

        /**
         * Constant for {@link DatabaseMetaData#functionNullable}({@value java.sql.DatabaseMetaData#functionNullable}).
         */
        FUNCTION_NULLABLE(DatabaseMetaData.functionNullable),

        /**
         * Constant for {@link DatabaseMetaData#functionNullableUnknown}({@value java.sql.DatabaseMetaData#functionNullableUnknown}).
         */
        FUNCTION_NULLABLE_UNKNOWN(DatabaseMetaData.functionNullableUnknown);

        /**
         * Returns the constant whose raw value equals to given. An instance of {@link IllegalArgumentException} will be
         * throw if no constants matches.
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
     * Constants for {@link #COLUMN_NAME_IS_NULLABLE} column values of the results of {@link
     * DatabaseMetaData#getFunctionColumns(String, String, String, String)}.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @XmlEnum
    public enum IsNullable implements FieldEnum<IsNullable, String> {

        /**
         * Constant for {@code ""}.
         */
        UNKNOWN(""),

        /**
         * Constant for {@code "YES"}.
         */
        YES("YES"),

        /**
         * Constant for {@code "NO"}.
         */
        NO("NO");

        /**
         * Returns the constant whose {@link #getRawValue() rawValue} matches to specified value.
         *
         * @param rawValue the raw value to match.
         * @return the value of {@code rawValue}.
         */
        public static IsNullable valueOfRawValue(final String rawValue) {
            return FieldEnums.valueOfRawValue(IsNullable.class, rawValue);
        }

        /**
         * Creates a new instance with specified raw value.
         *
         * @param rawValue the raw value.
         */
        IsNullable(final String rawValue) {
            this.rawValue = requireNonNull(rawValue, "rawValue is null");
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public String getRawValue() {
            return rawValue;
        }

        private final String rawValue;
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

    // -----------------------------------------------------------------------------------------------------------------
    public String getFunctionCat() {
        return functionCat;
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    public String getFunctionSchem() {
        return functionSchem;
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    public short getColumnType() {
        return columnType;
    }

    public void setColumnType(final short columnType) {
        this.columnType = columnType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public Short getScale() {
        return scale;
    }

    public void setScale(final Short scale) {
        this.scale = scale;
    }

    public short getRadix() {
        return radix;
    }

    public void setRadix(final short radix) {
        this.radix = radix;
    }

    public short getNullable() {
        return nullable;
    }

    public void setNullable(final short nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

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

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    private String columnName;

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
    private short radix;

    @XmlElement(required = true)
    @Label("NULLABLE")
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
