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
import java.util.Comparator;
import java.util.Optional;

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
public class TypeInfo extends AbstractMetadataType {

    private static final long serialVersionUID = -3964147654019495313L;

    // -----------------------------------------------------------------------------------------------------------------
    static final Comparator<TypeInfo> COMPARING_DATA_TYPE = Comparator.comparingInt(TypeInfo::getDataType);

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PRECISION = "PRECISION";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_LITERAL_PREFIX = "LITERAL_PREFIX";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_LITERAL_SUFFIX = "LITERAL_SUFFIX";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CREATE_PARAMS = "CREATE_PARAMS";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     *
     * @see Nullable
     */
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    /**
     * Constants for the {@value #COLUMN_LABEL_NULLABLE} column value.
     */
    public enum Nullable implements _IntFieldEnum<Nullable> {

        /**
         * A value for {@link DatabaseMetaData#typeNoNulls}({@value DatabaseMetaData#typeNoNulls}).
         */
        TYPE_NO_NULLS(DatabaseMetaData.typeNoNulls),// 0

        /**
         * A value for {@link DatabaseMetaData#typeNullable}({@value DatabaseMetaData#typeNullable}).
         */
        TYPE_NULLABLE(DatabaseMetaData.typeNullable), // 1

        /**
         * A value for {@link DatabaseMetaData#typeNullableUnknown}({@value DatabaseMetaData#typeNullableUnknown}).
         */
        TYPE_NULLABLE_UNKNOWN(DatabaseMetaData.typeNullableUnknown); // 2

        /**
         * Finds the value for specified {@link TypeInfo#COLUMN_LABEL_SEARCHABLE} column value.
         *
         * @param fieldValue the {@link TypeInfo#COLUMN_LABEL_SEARCHABLE} column value to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static Nullable valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(Nullable.class, fieldValue);
        }

        Nullable(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CASE_SENSITIVE = "CASE_SENSITIVE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     *
     * @see Searchable
     */
    public static final String COLUMN_LABEL_SEARCHABLE = "SEARCHABLE";

    /**
     * Constants for the {@value #COLUMN_LABEL_SEARCHABLE} column value.
     */
    public enum Searchable implements _IntFieldEnum<Searchable> {

        /**
         * A value for {@link DatabaseMetaData#typePredNone}({@value DatabaseMetaData#typePredNone}).
         */
        TYPE_PRED_NONE(DatabaseMetaData.typePredNone),// 0

        /**
         * A value for {@link DatabaseMetaData#typePredChar}({@value DatabaseMetaData#typePredChar}).
         */
        TYPE_PRED_CHAR(DatabaseMetaData.typePredChar), // 1

        /**
         * A value for {@link DatabaseMetaData#typePredBasic}({@value DatabaseMetaData#typePredBasic}).
         */
        TYPE_PRED_BASIC(DatabaseMetaData.typePredBasic), // 2

        /**
         * A value for {@link DatabaseMetaData#typeSearchable}({@value DatabaseMetaData#typeSearchable}).
         */
        TYPE_SEARCHABLE(DatabaseMetaData.typeSearchable); // 3

        /**
         * Finds the value for specified {@link TypeInfo#COLUMN_LABEL_SEARCHABLE} column value.
         *
         * @param fieldValue the {@link TypeInfo#COLUMN_LABEL_SEARCHABLE} column value to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static Searchable valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(Searchable.class, fieldValue);
        }

        Searchable(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // ------------------------------------------------------------------------------------------------------ searchable
    Searchable getSearchableAsEnum() {
        return Optional.ofNullable(getSearchable())
                .map(Searchable::valueOfFieldValue)
                .orElse(null);
    }

    void setSearchableAsEnum(final Searchable searchableAsEnum) {
        setSearchable(
                Optional.ofNullable(searchableAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    @EqualsAndHashCode.Include
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PRECISION)
    private Integer precision;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_LITERAL_PREFIX)
    private String literalPrefix;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_LITERAL_SUFFIX)
    private String literalSuffix;

    @jakarta.annotation.Nullable
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

    @jakarta.annotation.Nullable
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
