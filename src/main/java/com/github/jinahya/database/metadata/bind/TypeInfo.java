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

import static java.sql.DatabaseMetaData.*;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getTypeInfo() getTypeInfo()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class TypeInfo implements MetadataType {

    private static final long serialVersionUID = -3964147654019495313L;

    // -------------------------------------------------------------------------

    /**
     * Constants for nullabilities of an type.
     */
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#typeNoNulls}({@value DatabaseMetaData#typeNoNulls}).
         */
        TYPE_NO_NULLS(typeNoNulls), // 1

        /**
         * Constant for {@link DatabaseMetaData#typeNullable}({@value DatabaseMetaData#typeNullable}).
         */
        TYPE_NULLABLE(typeNullable), // 1

        /**
         * Constant for {@link DatabaseMetaData#typeNullableUnknown}({@value DatabaseMetaData#typeNullableUnknown}).
         */
        TYPE_NULLABLE_UNKNOWN(typeNullableUnknown); // 2

        /**
         * Returns the constant whose raw value matches to given. An instance of {@link IllegalArgumentException} will
         * be thrown if no value matches.
         *
         * @param rawValue the raw value
         * @return the matched constant
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
    public TypeInfo() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "typeName=" + typeName
               + ",dataType=" + dataType
               + ",precision=" + precision
               + ",literalPrefix=" + literalPrefix
               + ",literalSuffix=" + literalSuffix
               + ",createParams=" + createParams
               + ",nullable=" + nullable
               + ",caseSensitive=" + caseSensitive
               + ",searchable=" + searchable
               + ",unsignedAttribute=" + unsignedAttribute
               + ",fixedPrecScale=" + fixedPrecScale
               + ",autoIncrement=" + autoIncrement
               + ",localTypeName=" + localTypeName
               + ",minimumScale=" + minimumScale
               + ",maximumScale=" + maximumScale
               + ",sqlDataType=" + sqlDataType
               + ",sqlDatetimeSub=" + sqlDatetimeSub
               + ",numPrecRadix=" + numPrecRadix
               + '}';
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ----------------------------------------------------------------------------------------------------- getDataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // ------------------------------------------------------------------------------------------------------- precesion
    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    // --------------------------------------------------------------------------------------------------- literalPrefix
    public String getLiteralPrefix() {
        return literalPrefix;
    }

    public void setLiteralPrefix(final String literalPrefix) {
        this.literalPrefix = literalPrefix;
    }

    // --------------------------------------------------------------------------------------------------- literalSuffix
    public String getLiteralSuffix() {
        return literalSuffix;
    }

    public void setLiteralSuffix(final String literalSuffix) {
        this.literalSuffix = literalSuffix;
    }

    // ---------------------------------------------------------------------------------------------------- createParams
    public String getCreateParams() {
        return createParams;
    }

    public void setCreateParams(final String createParams) {
        this.createParams = createParams;
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public short getNullable() {
        return nullable;
    }

    public void setNullable(final short nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------- caseSensitive
    public boolean getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(final boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    // ------------------------------------------------------------------------------------------------------ searchable
    public short getSearchable() {
        return searchable;
    }

    public void setSearchable(final short searchable) {
        this.searchable = searchable;
    }

    // ----------------------------------------------------------------------------------------------- unsignedAttribute
    public boolean getUnsignedAttribute() {
        return unsignedAttribute;
    }

    public void setUnsignedAttribute(final boolean unsignedAttribute) {
        this.unsignedAttribute = unsignedAttribute;
    }

    // -------------------------------------------------------------------------------------------------- fixedPrecScale
    public boolean getFixedPrecScale() {
        return fixedPrecScale;
    }

    public void setFixedPrecScale(final boolean fixedPrecScale) {
        this.fixedPrecScale = fixedPrecScale;
    }

    // --------------------------------------------------------------------------------------------------- autoIncrement
    public boolean getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(final boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    // --------------------------------------------------------------------------------------------------- localTypeName
    public String getLocalTypeName() {
        return localTypeName;
    }

    public void setLocalTypeName(final String localTypeName) {
        this.localTypeName = localTypeName;
    }

    // ---------------------------------------------------------------------------------------------------- minimumScale
    public short getMinimumScale() {
        return minimumScale;
    }

    public void setMinimumScale(final short minimumScale) {
        this.minimumScale = minimumScale;
    }

    // ---------------------------------------------------------------------------------------------------- maximumScale
    public short getMaximumScale() {
        return maximumScale;
    }

    public void setMaximumScale(final short maximumScale) {
        this.maximumScale = maximumScale;
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

    // ---------------------------------------------------------------------------------------------------- numPrecRadix
    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(final int numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement
    @Label("TYPE_NAME")
    @Bind(label = "TYPE_NAME")
    private String typeName;

    @XmlElement
    @Label("DATA_TYPE")
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Label("PRECISION")
    @Bind(label = "PRECISION")
    private int precision;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("LITERAL_PREFIX")
    @Bind(label = "LITERAL_PREFIX", nillable = true)
    private String literalPrefix;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("LITERAL_SUFFIX")
    @Bind(label = "LITERAL_SUFFIX", nillable = true)
    private String literalSuffix;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("CREATE_PARAMS")
    @Bind(label = "CREATE_PARAMS", nillable = true)
    private String createParams;

    @XmlElement
    @Label("NULLABLE")
    @Bind(label = "NULLABLE")
    private short nullable;

    @XmlElement
    @Label("CASE_SENSITIVE")
    @Bind(label = "CASE_SENSITIVE")
    private boolean caseSensitive;

    @XmlElement
    @Label("SEARCHABLE")
    @Bind(label = "SEARCHABLE")
    private short searchable;

    @XmlElement
    @Label("UNSIGNED_ATTRIBUTE")
    @Bind(label = "UNSIGNED_ATTRIBUTE")
    private boolean unsignedAttribute;

    @XmlElement
    @Label("FIXED_PREC_SCALE")
    @Bind(label = "FIXED_PREC_SCALE")
    private boolean fixedPrecScale;

    @XmlElement
    @Label("AUTO_INCREMENT")
    @Bind(label = "AUTO_INCREMENT")
    private boolean autoIncrement;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("LOCAL_TYPE_NAME")
    @Bind(label = "LOCAL_TYPE_NAME", nillable = true)
    private String localTypeName;

    @XmlElement
    @Label("MINIMUM_SCALE")
    @Bind(label = "MINIMUM_SCALE")
    private short minimumScale;

    @XmlElement
    @Label("MAXIMUM_SCALE")
    @Bind(label = "MAXIMUM_SCALE")
    private short maximumScale;

    @XmlElement(nillable = true)
    @Unused
    @Label("SQL_DATA_TYPE")
    @Bind(label = "SQL_DATA_TYPE", unused = true)
    private Integer sqlDataType;

    @XmlElement(nillable = true)
    @Unused
    @Label("SQL_DATETIME_SUB")
    @Bind(label = "SQL_DATETIME_SUB", unused = true)
    private Integer sqlDatetimeSub;

    @XmlElement
    @Label("NUM_PREC_RADIX")
    @Bind(label = "NUM_PREC_RADIX")
    private int numPrecRadix;
}
