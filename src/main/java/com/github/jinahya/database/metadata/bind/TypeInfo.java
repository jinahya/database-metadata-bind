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

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getTypeInfo() getTypeInfo()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class TypeInfo implements MetadataType {

    private static final long serialVersionUID = -3964147654019495313L;

    // --------------------------------------------------------------------------------------------- NULLABLE / nullable
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String ATTRIBUTE_NAME_NULLABLE = "nullable";

    // ----------------------------------------------------------------------------------------- SEARCHABLE / searchable
    public static final String COLUMN_NAME_SEARCHABLE = "searchable";

    public static final String ATTRIBUTE_NAME_SEARCHABLE = "searchable";

    /**
     * Constants for {@value com.github.jinahya.database.metadata.bind.TypeInfo#COLUMN_NAME_NULLABLE} column values of a
     * result of {@link DatabaseMetaData#getTypeInfo()} method.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#typeNoNulls}({@value java.sql.DatabaseMetaData#typeNoNulls}).
         */
        TYPE_NO_NULLS(DatabaseMetaData.typeNoNulls), // 0

        /**
         * Constant for {@link DatabaseMetaData#typeNullable}({@value java.sql.DatabaseMetaData#typeNullable}).
         */
        TYPE_NULLABLE(DatabaseMetaData.typeNullable), // 1

        /**
         * Constant for
         * {@link DatabaseMetaData#typeNullableUnknown}({@value java.sql.DatabaseMetaData#typeNullableUnknown}).
         */
        TYPE_NULLABLE_UNKNOWN(DatabaseMetaData.typeNullableUnknown); // 2

        /**
         * Returns the constant whose raw value matches to given. An instance of {@link IllegalArgumentException} will
         * be thrown if no value matches.
         *
         * @param rawValue the raw value
         * @return the matched constant
         */
        public static Nullable valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Nullable.class, rawValue);
        }

        /**
         * Creates a new instance with specified raw value.
         *
         * @param rawValue the raw value.
         */
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

        /**
         * The raw value of this constant.
         */
        private final int rawValue;
    }

    /**
     * Constants for {@value com.github.jinahya.database.metadata.bind.TypeInfo#COLUMN_NAME_SEARCHABLE} column values of
     * a result of {@link DatabaseMetaData#getTypeInfo()} method.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum Searchable implements IntFieldEnum<Searchable> {

        /**
         * Constant for {@link DatabaseMetaData#typePredNone}({@value java.sql.DatabaseMetaData#typePredNone}).
         */
        TYPE_PRED_NONE(DatabaseMetaData.typePredNone), // 0

        /**
         * Constant for {@link DatabaseMetaData#typePredChar}({@value java.sql.DatabaseMetaData#typePredChar}).
         */
        TYPE_PRED_CHAR(DatabaseMetaData.typePredChar), // 1

        /**
         * Constant for {@link DatabaseMetaData#typePredBasic}({@value java.sql.DatabaseMetaData#typePredBasic}).
         */
        TYPE_PRED_BASIC(DatabaseMetaData.typePredBasic), // 2

        /**
         * Constant for {@link DatabaseMetaData#typeSearchable}({@value java.sql.DatabaseMetaData#typeSearchable}).
         */
        TYPE_SEARCHABLE(DatabaseMetaData.typeSearchable); // 3

        /**
         * Returns the constant whose raw value matches to given. An instance of {@link IllegalArgumentException} will
         * be thrown if no value matches.
         *
         * @param rawValue the raw value.
         * @return the matched constant.
         */
        public static Searchable valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Searchable.class, rawValue);
        }

        /**
         * Creates a new instance with specified raw value.
         *
         * @param rawValue the raw value.
         */
        Searchable(final int rawValue) {
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

        /**
         * The raw value of this constant.
         */
        private final int rawValue;
    }

    /**
     * Creates a new instance.
     */
    public TypeInfo() {
        super();
    }

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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final TypeInfo that = (TypeInfo) obj;
        return dataType == that.dataType
               && precision == that.precision
               && nullable == that.nullable
               && caseSensitive == that.caseSensitive
               && searchable == that.searchable
               && unsignedAttribute == that.unsignedAttribute
               && fixedPrecScale == that.fixedPrecScale
               && autoIncrement == that.autoIncrement
               && minimumScale == that.minimumScale
               && maximumScale == that.maximumScale
               && numPrecRadix == that.numPrecRadix
               && Objects.equals(typeName, that.typeName)
               && Objects.equals(literalPrefix, that.literalPrefix)
               && Objects.equals(literalSuffix, that.literalSuffix)
               && Objects.equals(createParams, that.createParams)
               && Objects.equals(localTypeName, that.localTypeName)
               && Objects.equals(sqlDataType, that.sqlDataType)
               && Objects.equals(sqlDatetimeSub, that.sqlDatetimeSub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName,
                            dataType,
                            precision,
                            literalPrefix,
                            literalSuffix,
                            createParams,
                            nullable,
                            caseSensitive,
                            searchable,
                            unsignedAttribute,
                            fixedPrecScale,
                            autoIncrement,
                            localTypeName,
                            minimumScale,
                            maximumScale,
                            sqlDataType,
                            sqlDatetimeSub,
                            numPrecRadix);
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

    public void setNullableAsInt(final int nullableAsInt) {
        setNullable((short) nullableAsInt);
    }

    public @NotNull Nullable getNullableAsEnum() {
        return Nullable.valueOfRawValue(getNullable());
    }

    public void setNullableAsEnum(final @NotNull Nullable nullableAsEnum) {
        requireNonNull(nullableAsEnum, "nullableAsEnum is null");
        setNullableAsInt(nullableAsEnum.getRawValue());
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

    public void setSearchableAsInt(final int searchableAsInt) {
        setSearchable((short) searchableAsInt);
    }

    public @NotNull Searchable getSearchableAsEnum() {
        return Searchable.valueOfRawValue(getSearchable());
    }

    public void setSearchableAsEnum(final @NotNull Searchable searchableAsEnum) {
        requireNonNull(searchableAsEnum, "searchableAsEnum is null");
        setSearchableAsInt(searchableAsEnum.getRawValue());
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

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("PRECISION")
    private int precision;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("LITERAL_PREFIX")
    private String literalPrefix;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("LITERAL_SUFFIX")
    private String literalSuffix;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("CREATE_PARAMS")
    private String createParams;

    @XmlElement(required = true)
    @Label("NULLABLE")
    private short nullable;

    @XmlElement(required = true)
    @Label("CASE_SENSITIVE")
    private boolean caseSensitive;

    @XmlElement(required = true)
    @Label("SEARCHABLE")
    private short searchable;

    @XmlElement(required = true)
    @Label("UNSIGNED_ATTRIBUTE")
    private boolean unsignedAttribute;

    @XmlElement(required = true)
    @Label("FIXED_PREC_SCALE")
    private boolean fixedPrecScale;

    @XmlElement(required = true)
    @Label("AUTO_INCREMENT")
    private boolean autoIncrement;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("LOCAL_TYPE_NAME")
    private String localTypeName;

    @XmlElement(required = true)
    @Label("MINIMUM_SCALE")
    private short minimumScale;

    @XmlElement(required = true)
    @Label("MAXIMUM_SCALE")
    private short maximumScale;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(required = true)
    @Label("NUM_PREC_RADIX")
    private int numPrecRadix;
}
