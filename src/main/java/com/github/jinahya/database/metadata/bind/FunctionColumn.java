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

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

/**
 * A class for binding results of the {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 */

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class FunctionColumn
        extends AbstractMetadataType
        implements HasIsNullableEnum {

    private static final long serialVersionUID = -7445156446214062680L;

    // -----------------------------------------------------------------------------------------------------------------

    static Comparator<FunctionColumn> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(FunctionColumn::getFunctionCat, ContextUtils.nulls(context, comparator))
                .thenComparing(FunctionColumn::getFunctionSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(FunctionColumn::getFunctionName, ContextUtils.nulls(context, comparator))
                .thenComparing(FunctionColumn::getSpecificName, ContextUtils.nulls(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_FUNCTION_CAT = "FUNCTION_CAT";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_FUNCTION_NAME = "FUNCTION_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_COLUMN_TYPE = "COLUMN_TYPE";

    /**
     * Constants for {@value #COLUMN_LABEL_COLUMN_TYPE} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum ColumnType
            implements _IntFieldEnum<ColumnType> {

        /**
         * A value for {@link DatabaseMetaData#functionColumnUnknown}({@value DatabaseMetaData#functionColumnUnknown}).
         */
        FUNCTION_COLUMN_UNKNOWN(DatabaseMetaData.functionColumnUnknown), // 0

        /**
         * A value for {@link DatabaseMetaData#functionColumnIn}({@value DatabaseMetaData#functionColumnIn}).
         */
        FUNCTION_COLUMN_IN(DatabaseMetaData.functionColumnIn),           // 1

        /**
         * A value for {@link DatabaseMetaData#functionColumnInOut}({@value DatabaseMetaData#functionColumnInOut}).
         */
        FUNCTION_COLUMN_IN_OUT(DatabaseMetaData.functionColumnInOut),    // 2

        /**
         * A value for {@link DatabaseMetaData#functionColumnOut}({@value DatabaseMetaData#functionColumnOut}).
         */
        FUNCTION_COLUMN_OUT(DatabaseMetaData.functionColumnOut),         // 3

        /**
         * A value for {@link DatabaseMetaData#functionReturn}({@value DatabaseMetaData#functionReturn}).
         */
//        FUNCTION_COLUMN_RETURN(4),
        FUNCTION_RETURN(DatabaseMetaData.functionReturn),                // 4

        /**
         * A value for {@link DatabaseMetaData#functionColumnResult}({@value DatabaseMetaData#functionColumnResult}).
         */
        FUNCTION_COLUMN_RESULT(DatabaseMetaData.functionColumnResult);   // 5

        /**
         * Finds the value for specified {@link FunctionColumn#COLUMN_LABEL_COLUMN_TYPE} column value.
         *
         * @param fieldValue the {@link FunctionColumn#COLUMN_LABEL_COLUMN_TYPE} column value to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static ColumnType valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(ColumnType.class, fieldValue);
        }

        ColumnType(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public enum Nullable
            implements _IntFieldEnum<Nullable> {

        FUNCTION_NO_NULLS(DatabaseMetaData.functionNoNulls),                // 0

        FUNCTION_NULLABLE(DatabaseMetaData.functionNullable),               // 1

        FUNCTION_NULLABLE_UNKNOWN(DatabaseMetaData.functionNullableUnknown) // 2
        ;

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
    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    // ------------------------------------------------------------------------------------------------------ columnType
    ColumnType getColumnTypeAsEnum() {
        return Optional.ofNullable(getColumnType())
                .map(ColumnType::valueOfFieldValue)
                .orElse(null);
    }

    void setColumnTypeAsEnum(final ColumnType columnTypeAsEnum) {
        setColumnType(
                Optional.ofNullable(columnTypeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition
    @AssertTrue(message = "ordinalPosition should be 0 when columnType is functionReturn(5)")
    private boolean isOrdinalPositionZeroWhenColumnTypeIsProcedureColumnReturn() {
        if (ordinalPosition == null || columnType == null || columnType != DatabaseMetaData.functionReturn) {
            return true;
        }
        return ordinalPosition == 0;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    @EqualsAndHashCode.Include
    private String functionCat;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    @EqualsAndHashCode.Include
    private String functionSchem;

    @NotBlank
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_NAME)
    @EqualsAndHashCode.Include
    private String functionName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    @EqualsAndHashCode.Include
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel("COLUMN_TYPE")
    private Integer columnType;

    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("PRECISION")
    private Integer precision;

    @_ColumnLabel("LENGTH")
    private Integer length;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCALE")
    private Integer scale;

    @_ColumnLabel("RADIX")
    private Integer radix;

    @_ColumnLabel("NULLABLE")
    private Integer nullable;

    @_ColumnLabel("REMARKS")
    private String remarks;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @PositiveOrZero
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @Pattern(regexp = YesNoEmptyConstants.REGEXP_YES_NO_EMPTY)
    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/849
    @_ColumnLabel("SPECIFIC_NAME")
    @EqualsAndHashCode.Include
    private String specificName;
}
