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
import lombok.ToString;

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

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TypeInfo
        extends AbstractMetadataType {

    private static final long serialVersionUID = -3964147654019495313L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<TypeInfo> comparator(final Context context) throws SQLException {
        return Comparator.comparing(
                TypeInfo::getDataType,
                ContextUtils.nullPrecedence(context, Comparator.naturalOrder())
        );
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

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected TypeInfo() {
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

    // -----------------------------------------------------------------------------------------------------------------

    public String getTypeName() {
        return typeName;
    }

    public Integer getDataType() {
        return dataType;
    }

    public Integer getPrecision() {
        return precision;
    }

    public String getLiteralPrefix() {
        return literalPrefix;
    }

    // --------------------------------------------------------------------------------------------------- literalSuffix
    public String getLiteralSuffix() {
        return literalSuffix;
    }

    // ---------------------------------------------------------------------------------------------------- createParams
    public String getCreateParams() {
        return createParams;
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public Integer getNullable() {
        return nullable;
    }

    // --------------------------------------------------------------------------------------------------- caseSensitive
    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    // ------------------------------------------------------------------------------------------------------ searchable
    public Integer getSearchable() {
        return searchable;
    }

    // ----------------------------------------------------------------------------------------------- unsignedAttribute
    public Boolean getUnsignedAttribute() {
        return unsignedAttribute;
    }

    public Boolean getFixedPrecScale() {
        return fixedPrecScale;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public String getLocalTypeName() {
        return localTypeName;
    }

    public Integer getMinimumScale() {
        return minimumScale;
    }

    public Integer getMaximumScale() {
        return maximumScale;
    }

    public Integer getSqlDataType() {
        return sqlDataType;
    }

    // -------------------------------------------------------------------------------------------------- sqlDateTimeSub
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    // ---------------------------------------------------------------------------------------------------- numPrecRadix
    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable

    // ------------------------------------------------------------------------------------------------------ searchable

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    @EqualsAndHashCode.Include
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PRECISION)
    private Integer precision;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_LITERAL_PREFIX)
    private String literalPrefix;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_LITERAL_SUFFIX)
    private String literalSuffix;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_CREATE_PARAMS)
    private String createParams;

    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @_ColumnLabel(COLUMN_LABEL_CASE_SENSITIVE)
    private Boolean caseSensitive;

    @_ColumnLabel(COLUMN_LABEL_SEARCHABLE)
    private Integer searchable;

    @_NotUsedBySpecification
    @_ColumnLabel("UNSIGNED_ATTRIBUTE")
    private Boolean unsignedAttribute;

    @_ColumnLabel("FIXED_PREC_SCALE")
    private Boolean fixedPrecScale;

    @_ColumnLabel("AUTO_INCREMENT")
    private Boolean autoIncrement;

    @_NullableBySpecification
    @_ColumnLabel("LOCAL_TYPE_NAME")
    private String localTypeName;

    @_ColumnLabel("MINIMUM_SCALE")
    private Integer minimumScale;

    @_ColumnLabel("MAXIMUM_SCALE")
    private Integer maximumScale;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @_ColumnLabel("NUM_PREC_RADIX")
    private Integer numPrecRadix;
}
