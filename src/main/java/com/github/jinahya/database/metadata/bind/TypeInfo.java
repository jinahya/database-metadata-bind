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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;

/**
 * An entity class for binding results of {@link java.sql.DatabaseMetaData#getTypeInfo() getTypeInfo()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTypeInfo()
 * @see NullableEnum
 * @see SearchableEnum
 */
//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class TypeInfo extends AbstractMetadataType {

    private static final long serialVersionUID = -3964147654019495313L;

    public static final Comparator<TypeInfo> COMPARING_DATA_TYPE = Comparator.comparingInt(TypeInfo::getDataType);

    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    public static final String COLUMN_LABEL_PRECISION = "PRECISION";

    public static final String COLUMN_LABEL_LITERAL_PREFIX = "LITERAL_PREFIX";

    public static final String COLUMN_LABEL_LITERAL_SUFFIX = "LITERAL_SUFFIX";

    public static final String COLUMN_LABEL_CREATE_PARAMS = "CREATE_PARAMS";

    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public enum NullableEnum implements _IntFieldEnum<NullableEnum> {

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
        public static NullableEnum valueOfNullable(final int nullable) {
            return _IntFieldEnum.valueOfFieldValue(NullableEnum.class, nullable);
        }

        NullableEnum(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    public static final String COLUMN_LABEL_CASE_SENSITIVE = "CASE_SENSITIVE";

    public static final String COLUMN_LABEL_SEARCHABLE = "SEARCHABLE";

    public enum SearchableEnum implements _IntFieldEnum<SearchableEnum> {

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
        public static SearchableEnum valueOfSearchable(final int searchable) {
            return _IntFieldEnum.valueOfFieldValue(SearchableEnum.class, searchable);
        }

        SearchableEnum(final int fieldValue) {
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
        return dataType == that.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataType);
    }

    @ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private int dataType;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_PRECISION)
    private Integer precision;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_LITERAL_PREFIX)
    private String literalPrefix;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_LITERAL_SUFFIX)
    private String literalSuffix;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_CREATE_PARAMS)
    private String createParams;

    @ColumnLabel(COLUMN_LABEL_NULLABLE)
    private int nullable;

    @ColumnLabel(COLUMN_LABEL_CASE_SENSITIVE)
    private boolean caseSensitive;

    @ColumnLabel(COLUMN_LABEL_SEARCHABLE)
    private int searchable;

    @NotUsedBySpecification
    @ColumnLabel("UNSIGNED_ATTRIBUTE")
    private Boolean unsignedAttribute;

    @ColumnLabel("FIXED_PREC_SCALE")
    private boolean fixedPrecScale;

    @ColumnLabel("AUTO_INCREMENT")
    private boolean autoIncrement;

    @NullableBySpecification
    @ColumnLabel("LOCAL_TYPE_NAME")
    private String localTypeName;

    @ColumnLabel("MINIMUM_SCALE")
    private int minimumScale;

    @ColumnLabel("MAXIMUM_SCALE")
    private int maximumScale;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;
}
