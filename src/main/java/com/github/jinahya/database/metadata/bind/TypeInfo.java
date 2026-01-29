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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getTypeInfo()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTypeInfo()
 */

public class TypeInfo
        extends AbstractMetadataType {

    private static final long serialVersionUID = -3964147654019495313L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    static Comparator<TypeInfo> comparingInSpecifiedOrder(final Context context,
                                                          final Comparator<? super Integer> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator.comparing(TypeInfo::getDataType, comparator); // NOT nullable
    }

    static Comparator<TypeInfo> comparingInSpecifiedOrder(final Context context)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, Comparator.naturalOrder());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PRECISION = "PRECISION";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_LITERAL_PREFIX = "LITERAL_PREFIX";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_LITERAL_SUFFIX = "LITERAL_SUFFIX";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CREATE_PARAMS = "CREATE_PARAMS";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public static final int COLUMN_VALUE_NULLABLE_TYPE_NO_NULLS = DatabaseMetaData.typeNoNulls; // 0

    public static final int COLUMN_VALUE_NULLABLE_TYPE_NULLABLE = DatabaseMetaData.typeNullable; // 1

    public static final int COLUMN_VALUE_NULLABLE_TYPE_NULLABLE_UNKNOWN = DatabaseMetaData.typeNullableUnknown; // 2

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CASE_SENSITIVE = "CASE_SENSITIVE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SEARCHABLE = "SEARCHABLE";

    public static final int COLUMN_VALUE_SEARCHABLE_TYPE_PREC_NONE = DatabaseMetaData.typePredNone; // 0

    public static final int COLUMN_VALUE_SEARCHABLE_TYPE_PREC_CHAR = DatabaseMetaData.typePredChar; // 1

    public static final int COLUMN_VALUE_SEARCHABLE_TYPE_PREC_BASIC = DatabaseMetaData.typePredBasic; // 2

    public static final int COLUMN_VALUE_SEARCHABLE_TYPE_SEARCHABLE = DatabaseMetaData.typeSearchable; // 3

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_UNSIGNED_ATTRIBUTE = "UNSIGNED_ATTRIBUTE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FIXED_PREC_SCALE = "FIXED_PREC_SCALE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_AUTO_INCREMENT = "AUTO_INCREMENT";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_LOCAL_TYPE_NAME = "LOCAL_TYPE_NAME";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_MINIMUM_SCALE = "MINIMUM_SCALE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_MAXIMUM_SCALE = "MAXIMUM_SCALE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SQL_DATA_TYPE = "SQL_DATA_TYPE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SQL_DATETIME_SUB = "SQL_DATETIME_SUB";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_NUM_PREC_RADIX = "NUM_PREC_RADIX";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    TypeInfo() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "typeName=" + typeName +
               ",dataType=" + dataType +
               ",precision=" + precision +
               ",literalPrefix=" + literalPrefix +
               ",literalSuffix=" + literalSuffix +
               ",createParams=" + createParams +
               ",nullable=" + nullable +
               ",caseSensitive=" + caseSensitive +
               ",searchable=" + searchable +
               ",unsignedAttribute=" + unsignedAttribute +
               ",fixedPrecScale=" + fixedPrecScale +
               ",autoIncrement=" + autoIncrement +
               ",localTypeName=" + localTypeName +
               ",minimumScale=" + minimumScale +
               ",maximumScale=" + maximumScale +
               ",sqlDataType=" + sqlDataType +
               ",sqlDatetimeSub=" + sqlDatetimeSub +
               ",numPrecRadix=" + numPrecRadix +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (TypeInfo) obj;
        return Objects.equals(typeName, that.typeName) &&
               Objects.equals(dataType, that.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeName, dataType);
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

    // ------------------------------------------------------------------------------------------------------- precision

    /**
     * Returns the value of {@value #COLUMN_LABEL_PRECISION} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PRECISION} column.
     */
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

    // --------------------------------------------------------------------------------------------------- literalPrefix

    /**
     * Returns the value of {@value #COLUMN_LABEL_LITERAL_PREFIX} column.
     *
     * @return the value of {@value #COLUMN_LABEL_LITERAL_PREFIX} column.
     */
    public String getLiteralPrefix() {
        return literalPrefix;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_LITERAL_PREFIX} column.
     *
     * @param literalPrefix the value of {@value #COLUMN_LABEL_LITERAL_PREFIX} column.
     */
    void setLiteralPrefix(final String literalPrefix) {
        this.literalPrefix = literalPrefix;
    }

    // --------------------------------------------------------------------------------------------------- literalSuffix

    /**
     * Returns the value of {@value #COLUMN_LABEL_LITERAL_SUFFIX} column.
     *
     * @return the value of {@value #COLUMN_LABEL_LITERAL_SUFFIX} column.
     */
    public String getLiteralSuffix() {
        return literalSuffix;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_LITERAL_SUFFIX} column.
     *
     * @param literalSuffix the value of {@value #COLUMN_LABEL_LITERAL_SUFFIX} column.
     */
    void setLiteralSuffix(final String literalSuffix) {
        this.literalSuffix = literalSuffix;
    }

    // ---------------------------------------------------------------------------------------------------- createParams

    /**
     * Returns the value of {@value #COLUMN_LABEL_CREATE_PARAMS} column.
     *
     * @return the value of {@value #COLUMN_LABEL_CREATE_PARAMS} column.
     */
    public String getCreateParams() {
        return createParams;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_CREATE_PARAMS} column.
     *
     * @param createParams the value of {@value #COLUMN_LABEL_CREATE_PARAMS} column.
     */
    void setCreateParams(final String createParams) {
        this.createParams = createParams;
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

    // --------------------------------------------------------------------------------------------------- caseSensitive

    /**
     * Returns the value of {@value #COLUMN_LABEL_CASE_SENSITIVE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_CASE_SENSITIVE} column.
     */
    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_CASE_SENSITIVE} column.
     *
     * @param caseSensitive the value of {@value #COLUMN_LABEL_CASE_SENSITIVE} column.
     */
    void setCaseSensitive(final Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    // ------------------------------------------------------------------------------------------------------ searchable

    /**
     * Returns the value of {@value #COLUMN_LABEL_SEARCHABLE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SEARCHABLE} column.
     */
    public Integer getSearchable() {
        return searchable;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SEARCHABLE} column.
     *
     * @param searchable the value of {@value #COLUMN_LABEL_SEARCHABLE} column.
     */
    void setSearchable(final Integer searchable) {
        this.searchable = searchable;
    }

    // ----------------------------------------------------------------------------------------------- unsignedAttribute

    /**
     * Returns the value of {@value #COLUMN_LABEL_UNSIGNED_ATTRIBUTE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_UNSIGNED_ATTRIBUTE} column.
     */
    public Boolean getUnsignedAttribute() {
        return unsignedAttribute;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_UNSIGNED_ATTRIBUTE} column.
     *
     * @param unsignedAttribute the value of {@value #COLUMN_LABEL_UNSIGNED_ATTRIBUTE} column.
     */
    void setUnsignedAttribute(final Boolean unsignedAttribute) {
        this.unsignedAttribute = unsignedAttribute;
    }

    // -------------------------------------------------------------------------------------------------- fixedPrecScale

    /**
     * Returns the value of {@value #COLUMN_LABEL_FIXED_PREC_SCALE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FIXED_PREC_SCALE} column.
     */
    public Boolean getFixedPrecScale() {
        return fixedPrecScale;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FIXED_PREC_SCALE} column.
     *
     * @param fixedPrecScale the value of {@value #COLUMN_LABEL_FIXED_PREC_SCALE} column.
     */
    void setFixedPrecScale(final Boolean fixedPrecScale) {
        this.fixedPrecScale = fixedPrecScale;
    }

    // --------------------------------------------------------------------------------------------------- autoIncrement

    /**
     * Returns the value of {@value #COLUMN_LABEL_AUTO_INCREMENT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_AUTO_INCREMENT} column.
     */
    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_AUTO_INCREMENT} column.
     *
     * @param autoIncrement the value of {@value #COLUMN_LABEL_AUTO_INCREMENT} column.
     */
    void setAutoIncrement(final Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    // --------------------------------------------------------------------------------------------------- localTypeName

    /**
     * Returns the value of {@value #COLUMN_LABEL_LOCAL_TYPE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_LOCAL_TYPE_NAME} column.
     */
    public String getLocalTypeName() {
        return localTypeName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_LOCAL_TYPE_NAME} column.
     *
     * @param localTypeName the value of {@value #COLUMN_LABEL_LOCAL_TYPE_NAME} column.
     */
    void setLocalTypeName(final String localTypeName) {
        this.localTypeName = localTypeName;
    }

    // ---------------------------------------------------------------------------------------------------- minimumScale

    /**
     * Returns the value of {@value #COLUMN_LABEL_MINIMUM_SCALE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_MINIMUM_SCALE} column.
     */
    public Integer getMinimumScale() {
        return minimumScale;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_MINIMUM_SCALE} column.
     *
     * @param minimumScale the value of {@value #COLUMN_LABEL_MINIMUM_SCALE} column.
     */
    void setMinimumScale(final Integer minimumScale) {
        this.minimumScale = minimumScale;
    }

    // ---------------------------------------------------------------------------------------------------- maximumScale

    /**
     * Returns the value of {@value #COLUMN_LABEL_MAXIMUM_SCALE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_MAXIMUM_SCALE} column.
     */
    public Integer getMaximumScale() {
        return maximumScale;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_MAXIMUM_SCALE} column.
     *
     * @param maximumScale the value of {@value #COLUMN_LABEL_MAXIMUM_SCALE} column.
     */
    void setMaximumScale(final Integer maximumScale) {
        this.maximumScale = maximumScale;
    }

    // ----------------------------------------------------------------------------------------------------- sqlDataType

    /**
     * Returns the value of {@value #COLUMN_LABEL_SQL_DATA_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SQL_DATA_TYPE} column.
     */
    public Integer getSqlDataType() {
        return sqlDataType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SQL_DATA_TYPE} column.
     *
     * @param sqlDataType the value of {@value #COLUMN_LABEL_SQL_DATA_TYPE} column.
     */
    void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    // -------------------------------------------------------------------------------------------------- sqlDatetimeSub

    /**
     * Returns the value of {@value #COLUMN_LABEL_SQL_DATETIME_SUB} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SQL_DATETIME_SUB} column.
     */
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SQL_DATETIME_SUB} column.
     *
     * @param sqlDatetimeSub the value of {@value #COLUMN_LABEL_SQL_DATETIME_SUB} column.
     */
    void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    // ---------------------------------------------------------------------------------------------------- numPrecRadix

    /**
     * Returns the value of {@value #COLUMN_LABEL_NUM_PREC_RADIX} column.
     *
     * @return the value of {@value #COLUMN_LABEL_NUM_PREC_RADIX} column.
     */
    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_NUM_PREC_RADIX} column.
     *
     * @param numPrecRadix the value of {@value #COLUMN_LABEL_NUM_PREC_RADIX} column.
     */
    void setNumPrecRadix(final Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PRECISION)
    private Integer precision;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_LITERAL_PREFIX)
    private String literalPrefix;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_LITERAL_SUFFIX)
    private String literalSuffix;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_CREATE_PARAMS)
    private String createParams;

    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @_ColumnLabel(COLUMN_LABEL_CASE_SENSITIVE)
    private Boolean caseSensitive;

    @_ColumnLabel(COLUMN_LABEL_SEARCHABLE)
    private Integer searchable;

    @org.jspecify.annotations.Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_UNSIGNED_ATTRIBUTE)
    private Boolean unsignedAttribute;

    @_ColumnLabel(COLUMN_LABEL_FIXED_PREC_SCALE)
    private Boolean fixedPrecScale;

    @_ColumnLabel(COLUMN_LABEL_AUTO_INCREMENT)
    private Boolean autoIncrement;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_LOCAL_TYPE_NAME)
    private String localTypeName;

    @_ColumnLabel(COLUMN_LABEL_MINIMUM_SCALE)
    private Integer minimumScale;

    @_ColumnLabel(COLUMN_LABEL_MAXIMUM_SCALE)
    private Integer maximumScale;

    @org.jspecify.annotations.Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SQL_DATA_TYPE)
    private Integer sqlDataType;

    @org.jspecify.annotations.Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SQL_DATETIME_SUB)
    private Integer sqlDatetimeSub;

    @_ColumnLabel(COLUMN_LABEL_NUM_PREC_RADIX)
    private Integer numPrecRadix;
}
