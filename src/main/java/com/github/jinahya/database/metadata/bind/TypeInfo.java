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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getTypeInfo()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTypeInfo()
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class TypeInfo extends AbstractMetadataType {

    private static final long serialVersionUID = -3964147654019495313L;

    static final Comparator<TypeInfo> COMPARING_DATA_TYPE = Comparator.comparingInt(TypeInfo::getDataType);

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
         * @param nullable the {@link TypeInfo#COLUMN_LABEL_SEARCHABLE} column value to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static Nullable valueOfNullable(final int nullable) {
            return _IntFieldEnum.valueOfFieldValue(Nullable.class, nullable);
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

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CASE_SENSITIVE = "CASE_SENSITIVE";

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
         * @param searchable the {@link TypeInfo#COLUMN_LABEL_SEARCHABLE} column value to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static Searchable valueOfSearchable(final int searchable) {
            return _IntFieldEnum.valueOfFieldValue(Searchable.class, searchable);
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TypeInfo)) return false;
        final TypeInfo that = (TypeInfo) obj;
        return dataType == that.dataType &&
               Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, dataType);
    }

    public Integer getSearchable() {
        return searchable;
    }

    public void setSearchable(final Integer searchable) {
        this.searchable = searchable;
    }

    Searchable getSearchableAsEnum() {
        return Optional.ofNullable(getSearchable())
                .map(Searchable::valueOfSearchable)
                .orElse(null);
    }

    void setSearchableAsEnum(final Searchable searchableAsEnum) {
        setSearchable(
                Optional.ofNullable(searchableAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private int dataType;

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

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @_ColumnLabel(COLUMN_LABEL_CASE_SENSITIVE)
    private boolean caseSensitive;

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_SEARCHABLE)
    private Integer searchable;

    @_NotUsedBySpecification
    @_ColumnLabel("UNSIGNED_ATTRIBUTE")
    private Boolean unsignedAttribute;

    @_ColumnLabel("FIXED_PREC_SCALE")
    private boolean fixedPrecScale;

    @_ColumnLabel("AUTO_INCREMENT")
    private boolean autoIncrement;

    @_NullableBySpecification
    @_ColumnLabel("LOCAL_TYPE_NAME")
    private String localTypeName;

    @_ColumnLabel("MINIMUM_SCALE")
    private int minimumScale;

    @_ColumnLabel("MAXIMUM_SCALE")
    private int maximumScale;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @_ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;
}
