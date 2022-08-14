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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String, Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class FunctionColumn
        implements MetadataType,
                   ChildOf<Function> {

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

    /**
     * Constants for {@link #COLUMN_NAME_COLUMN_TYPE} column values of the results of
     * {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)}.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @XmlEnum
    public enum ColumnType implements IntFieldEnum<ColumnType> {

        /**
         * Constant for
         * {@link DatabaseMetaData#functionColumnUnknown}({@value java.sql.DatabaseMetaData#functionColumnUnknown}).
         */
        FUNCTION_COLUMN_UNKNOWN(DatabaseMetaData.functionColumnUnknown), // 0

        /**
         * Constants for
         * {@link DatabaseMetaData#functionColumnIn}({@value java.sql.DatabaseMetaData#functionColumnIn}).
         */
        FUNCTION_COLUMN_IN(DatabaseMetaData.functionColumnIn), // 1

        /**
         * Constants for
         * {@link DatabaseMetaData#functionColumnInOut}({@value java.sql.DatabaseMetaData#functionColumnInOut}).
         */
        FUNCTION_COLUMN_IN_OUT(DatabaseMetaData.functionColumnInOut), // 2

        /**
         * Constants for
         * {@link DatabaseMetaData#functionColumnOut}({@value java.sql.DatabaseMetaData#functionColumnOut}).
         */
        FUNCTION_COLUMN_OUT(DatabaseMetaData.functionColumnOut), // 3

        /**
         * Constant for {@link DatabaseMetaData#functionReturn}({@value java.sql.DatabaseMetaData#functionReturn}).
         */
        // https://stackoverflow.com/a/46647586/330457
        FUNCTION_COLUMN_RETURN(DatabaseMetaData.functionReturn), // 4

        /**
         * Constants for
         * {@link DatabaseMetaData#functionColumnResult}({@value java.sql.DatabaseMetaData#functionColumnResult}).
         */
        FUNCTION_COLUMN_RESULT(DatabaseMetaData.functionColumnResult); // 5

        /**
         * Returns the constant whose raw value equals to given value. An {@link IllegalArgumentException} will be
         * thrown if no constant matches.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given value.
         */
        public static ColumnType valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(ColumnType.class, rawValue);
        }

        ColumnType(final int rawValue) {
            this.rawValue = rawValue;
        }

        @Override
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    /**
     * Constants for {@link #COLUMN_NAME_NULLABLE} column values of the results of
     * {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)}.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @XmlEnum
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#functionNoNulls}({@value java.sql.DatabaseMetaData#functionNoNulls}).
         */
        FUNCTION_NO_NULLS(DatabaseMetaData.functionNoNulls), // 0

        /**
         * Constant for {@link DatabaseMetaData#functionNullable}({@value java.sql.DatabaseMetaData#functionNullable}).
         */
        FUNCTION_NULLABLE(DatabaseMetaData.functionNullable), // 1

        /**
         * Constant for
         * {@link DatabaseMetaData#functionNullableUnknown}({@value
         * java.sql.DatabaseMetaData#functionNullableUnknown}).
         */
        FUNCTION_NULLABLE_UNKNOWN(DatabaseMetaData.functionNullableUnknown); // 2

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
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @Override
    public Function extractParent() {
        return Function.builder()
                .functionCat(getFunctionCat())
                .functionSchem(getFunctionSchem())
                .functionName(getFunctionName())
                .build();
    }

    public ColumnType getColumnTypeAsEnum() {
        return ColumnType.valueOfRawValue(getColumnType());
    }

    public void setColumnTypeAsEnum(final ColumnType columnTypeAsEnum) {
        setColumnType(Objects.requireNonNull(columnTypeAsEnum, "columnTypeAsEnum is null").rawValue());
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("FUNCTION_CAT")
    private String functionCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("FUNCTION_SCHEM")
    private String functionSchem;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("FUNCTION_NAME")
    private String functionName;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = false, required = true)
    @Label("COLUMN_TYPE")
    private int columnType;

    @XmlElement(nillable = false, required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification // > Null is returned for data types where the column size is not applicable.
    @Label("PRECISION")
    private Integer precision;

    @XmlElement(nillable = false, required = true)
    @Label("LENGTH")
    private int length;

    // https://issues.apache.org/jira/browse/DERBY-7102
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SCALE")
    private Integer scale;

    @XmlElement(nillable = false, required = true)
    @Label("RADIX")
    private int radix;

    @XmlElement(nillable = false, required = true)
    @Label("NULLABLE")
    private int nullable;

    @XmlElement(nillable = true, required = true)
    @NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7100
    @Label("REMARKS")
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @XmlElement(nillable = false, required = true)
    @Positive
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(required = true)
    @NotNull
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(nillable = true, required = true)
    @NotNull
    @Label("SPECIFIC_NAME")
    private String specificName;

    @XmlTransient
    @Valid
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Function function;
}
