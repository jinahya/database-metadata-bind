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

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the {@link DatabaseMetaData#getColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumns(String, String, String, String)
 * @see Nullable
 */
//@ParentOf(ColumnPrivilege.class)

@_ChildOf(Table.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Column
        extends AbstractMetadataType
        implements HasIsNullableEnum,
                   HasNullableEnum<Column.Nullable> {

    private static final long serialVersionUID = -409653682729081530L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Column> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(Column::getTableCat, ContextUtils.nulls(context, comparator))
                .thenComparing(Column::getTableSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(Column::getTableName, ContextUtils.nulls(context, comparator))
                .thenComparing(Column::getOrdinalPosition, ContextUtils.nulls(context, Comparator.naturalOrder()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public static final int COLUMN_VALUE_NULLABLE_COLUMN_NO_NULLS = DatabaseMetaData.columnNoNulls;                 // 0

    public static final int COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE = DatabaseMetaData.columnNullable;                // 1

    public static final int COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE_UNKNOWN = DatabaseMetaData.columnNullableUnknown; // 2

    /**
     * Constants for {@value #COLUMN_LABEL_NULLABLE} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum Nullable
            implements NullableEnum<Nullable> {

        /**
         * A value for {@link DatabaseMetaData#columnNoNulls}({@value DatabaseMetaData#columnNoNulls}).
         */
        COLUMN_NO_NULLS(DatabaseMetaData.columnNoNulls),                // 0

        /**
         * A value for {@link DatabaseMetaData#columnNullable}({@value DatabaseMetaData#columnNullable}).
         */
        COLUMN_NULLABLE(DatabaseMetaData.columnNullable),               // 1

        /**
         * A value for {@link DatabaseMetaData#columnNullableUnknown}({@value DatabaseMetaData#columnNullableUnknown}).
         */
        COLUMN_NULLABLE_UNKNOWN(DatabaseMetaData.columnNullableUnknown) // 2
        ;

        /**
         * Finds the value for specified {@link Column#COLUMN_LABEL_NULLABLE} column value.
         *
         * @param fieldValue the value of {@link Column#COLUMN_LABEL_NULLABLE} column to match.
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
    public static final String COLUMN_LABEL_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

    public enum IsAutoincrement
            implements YesNoEmptyEnum<IsAutoincrement> {

        /**
         * Constants for {@value YesNoEmptyConstants#EMPTY}.
         */
        EMPTY(YesNoEmptyConstants.EMPTY),

        /**
         * Constants for {@value YesNoEmptyConstants#NO}.
         */
        NO(YesNoEmptyConstants.NO),

        /**
         * Constants for {@value YesNoEmptyConstants#YES}.
         */
        YES(YesNoEmptyConstants.YES);

        public static IsAutoincrement valueOfFieldValue(final String fieldValue) {
            return YesNoEmptyEnum.valueOfFieldValue(IsAutoincrement.class, fieldValue);
        }

        IsAutoincrement(final String fieldValue) {
            this.fieldValue = Objects.requireNonNull(fieldValue, "fieldValue is null");
        }

        @Override
        public String fieldValue() {
            return fieldValue;
        }

        private final String fieldValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    public enum IsGeneratedcolumn
            implements YesNoEmptyEnum<IsGeneratedcolumn> {

        /**
         * Constants for {@value YesNoEmptyConstants#EMPTY}.
         */
        EMPTY(YesNoEmptyConstants.EMPTY),

        /**
         * Constants for {@value YesNoEmptyConstants#NO}.
         */
        NO(YesNoEmptyConstants.NO),

        /**
         * Constants for {@value YesNoEmptyConstants#YES}.
         */
        YES(YesNoEmptyConstants.YES);

        public static IsGeneratedcolumn valueOfFieldValue(final String fieldValue) {
            return YesNoEmptyEnum.valueOfFieldValue(IsGeneratedcolumn.class, fieldValue);
        }

        IsGeneratedcolumn(final String fieldValue) {
            this.fieldValue = Objects.requireNonNull(fieldValue, "fieldValue is null");
        }

        @Override
        public String fieldValue() {
            return fieldValue;
        }

        private final String fieldValue;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    // ------------------------------------------------------------------------------------------------------ tableSchem

    // -------------------------------------------------------------------------------------------------------- nullable

    @Override
    public Nullable getNullableAsEnum() {
        return Optional.ofNullable(getNullable())
                .map(Nullable::valueOfFieldValue)
                .orElse(null);
    }

    // ------------------------------------------------------------------------------------------------- isAutoincrement

    /**
     * Returns current value of {@code isAutoincrement} field as one of predefined constants.
     *
     * @return current value of the {@code isAutoincrement} field as one of predefined constants.
     */
    public IsAutoincrement getIsAutoincrementAsEnum() {
        return Optional.ofNullable(getIsAutoincrement())
                .map(IsAutoincrement::valueOfFieldValue)
                .orElse(null);
    }

    /**
     * Replaces current value of {@code isAutoincrement} field with specified constant's field value.
     *
     * @param isAutoincrementAsEnum the constant whose {@link _FieldEnum#fieldValue() fieldValue} is set to the
     *                              {@code isAutoincrement} field.
     */

    public void setIsAutoincrementAsEnum(final IsAutoincrement isAutoincrementAsEnum) {
        setIsAutoincrement(
                Optional.ofNullable(isAutoincrementAsEnum)
                        .map(_FieldEnum::fieldValue)
                        .orElse(null)
        );
    }

    // ----------------------------------------------------------------------------------------------- isGeneratedcolumn
    public IsGeneratedcolumn getIsGeneratedcolumnAsEnum() {
        return Optional.ofNullable(getIsGeneratedcolumn())
                .map(IsGeneratedcolumn::valueOfFieldValue)
                .orElse(null);
    }

    public void setIsGeneratedcolumnAsEnum(final IsGeneratedcolumn isGeneratedcolumnAsEnum) {
        setIsGeneratedcolumn(
                Optional.ofNullable(isGeneratedcolumnAsEnum)
                        .map(_FieldEnum::fieldValue)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    @EqualsAndHashCode.Include
    private String tableCat;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    @EqualsAndHashCode.Include
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    @EqualsAndHashCode.Include
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_NotUsedBySpecification
    @_ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_ColumnLabel("NUM_PREC_RADIX")
    private Integer numPrecRadix;

    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @Positive
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @Pattern(regexp = YesNoEmptyConstants.REGEXP_YES_NO_EMPTY)
    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;

    @Pattern(regexp = YesNoEmptyConstants.REGEXP_YES_NO_EMPTY)
    @_ColumnLabel(COLUMN_LABEL_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @Pattern(regexp = YesNoEmptyConstants.REGEXP_YES_NO_EMPTY)
    @_ColumnLabel(COLUMN_LABEL_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;
}
