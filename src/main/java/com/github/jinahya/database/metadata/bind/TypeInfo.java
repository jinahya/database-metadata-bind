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
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Objects;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getTypeInfo() getTypeInfo()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class TypeInfo
        implements MetadataType {

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
        public int rawValue() {
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
        public int rawValue() {
            return rawValue;
        }

        /**
         * The raw value of this constant.
         */
        private final int rawValue;
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
    }

    @NotNull
    public Nullable getNullableAsEnum() {
        return Nullable.valueOfRawValue(getNullable());
    }

    public void setNullableAsEnum(@NotNull final Nullable nullableAsEnum) {
        Objects.requireNonNull(nullableAsEnum, "nullableAsEnum is null");
        setNullable(nullableAsEnum.rawValue());
    }

    @NotNull
    public Searchable getSearchableAsEnum() {
        return Searchable.valueOfRawValue(getSearchable());
    }

    public void setSearchableAsEnum(@NotNull final Searchable searchableAsEnum) {
        Objects.requireNonNull(searchableAsEnum, "searchableAsEnum is null");
        setSearchable(searchableAsEnum.rawValue());
    }

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = false, required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification // > Null is returned for data types where the column size is not applicable.
    @Label("PRECISION")
    private Integer precision;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("LITERAL_PREFIX")
    private String literalPrefix;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("LITERAL_SUFFIX")
    private String literalSuffix;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("CREATE_PARAMS")
    private String createParams;

    @XmlElement(nillable = false, required = true)
    @Label("NULLABLE")
    private int nullable;

    @XmlElement(nillable = false, required = true)
    @Label("CASE_SENSITIVE")
    private boolean caseSensitive;

    @XmlElement(nillable = false, required = true)
    @Label("SEARCHABLE")
    private int searchable;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("UNSIGNED_ATTRIBUTE")
    private Boolean unsignedAttribute;

    @XmlElement(nillable = false, required = true)
    @Label("FIXED_PREC_SCALE")
    private boolean fixedPrecScale;

    @XmlElement(nillable = false, required = true)
    @Label("AUTO_INCREMENT")
    private boolean autoIncrement;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("LOCAL_TYPE_NAME")
    private String localTypeName;

    @XmlElement(required = true)
    @Label("MINIMUM_SCALE")
    private int minimumScale;

    @XmlElement(required = true)
    @Label("MAXIMUM_SCALE")
    private int maximumScale;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(nillable = false, required = true)
    @Label("NUM_PREC_RADIX")
    private int numPrecRadix;
}
