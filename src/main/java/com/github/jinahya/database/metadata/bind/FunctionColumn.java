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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 */
@_ChildOf(Function.class)
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class FunctionColumn extends AbstractMetadataType {

    private static final long serialVersionUID = -7445156446214062680L;

    static final Comparator<FunctionColumn> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(FunctionColumn::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(FunctionColumn::getFunctionName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(FunctionColumn::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<FunctionColumn> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(FunctionColumn::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(FunctionColumn::getFunctionName, nullsFirst(naturalOrder()))
                    .thenComparing(FunctionColumn::getSpecificName, nullsFirst(naturalOrder()));

    public static final String COLUMN_LABEL_FUNCTION_CAT = "FUNCTION_CAT";

    public static final String COLUMN_LABEL_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    public static final String COLUMN_LABEL_FUNCTION_NAME = "FUNCTION_NAME";

    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    public static final String COLUMN_LABEL_COLUMN_TYPE = "COLUMN_TYPE";

    /**
     * Constants for {@value #COLUMN_LABEL_COLUMN_TYPE} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum ColumnTypeEnum implements _IntFieldEnum<ColumnTypeEnum> {

        /**
         * A value for {@link DatabaseMetaData#functionColumnUnknown}({@value DatabaseMetaData#functionColumnUnknown}).
         */
        FUNCTION_COLUMN_UNKNOWN(DatabaseMetaData.functionColumnUnknown),// 0

        /**
         * A value for {@link DatabaseMetaData#functionColumnIn}({@value DatabaseMetaData#functionColumnIn}).
         */
        FUNCTION_COLUMN_IN(DatabaseMetaData.functionColumnIn), // 1

        /**
         * A value for {@link DatabaseMetaData#functionColumnInOut}({@value DatabaseMetaData#functionColumnInOut}).
         */
        FUNCTION_COLUMN_IN_OUT(DatabaseMetaData.functionColumnInOut), // 2

        /**
         * A value for {@link DatabaseMetaData#functionColumnOut}({@value DatabaseMetaData#functionColumnOut}).
         */
        FUNCTION_COLUMN_OUT(DatabaseMetaData.functionColumnOut), // 3

        /**
         * A value for {@code 4}.
         */
        FUNCTION_COLUMN_RETURN(4),

        /**
         * A value for {@link DatabaseMetaData#functionColumnResult}({@value DatabaseMetaData#functionColumnResult}).
         */
        FUNCTION_COLUMN_RESULT(DatabaseMetaData.functionColumnResult) // 5
        ;

        /**
         * Finds the value for specified {@link FunctionColumn#COLUMN_LABEL_COLUMN_TYPE} column value.
         *
         * @param columnType the {@link FunctionColumn#COLUMN_LABEL_COLUMN_TYPE} column value to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static ColumnTypeEnum valueOfColumnType(final int columnType) {
            return _IntFieldEnum.valueOfFieldValue(ColumnTypeEnum.class, columnType);
        }

        ColumnTypeEnum(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    // ------------------------------------------------------------------------------------------------ functionColumnId
    FunctionColumnId getFunctionColumnId() {
        if (functionColumnId == null) {
            return FunctionColumnId.of(
                    FunctionId.of(
                            getFunctionCatNonNull(),
                            getFunctionSchemNonNull(),
                            getSpecificName()
                    ),
                    getColumnName(),
                    getColumnType()
            );
        }
        return functionColumnId;
    }

    private FunctionId getFunctionId() {
        return getFunctionColumnId().getFunctionId();
    }

    private SchemaId getSchemaId() {
        return getFunctionId().getSchemaId();
    }

    // ----------------------------------------------------------------------------------------------------- functionCat
    String getFunctionCatNonNull() {
        return Optional.ofNullable(getFunctionCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
        functionColumnId = null;
    }

    // --------------------------------------------------------------------------------------------------- functionSchem
    String getFunctionSchemNonNull() {
        return Optional.ofNullable(getFunctionSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
        functionColumnId = null;
    }

    // ---------------------------------------------------------------------------------------------------- functionName
    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
        functionColumnId = null;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public void setColumnName(final String columnName) {
        this.columnName = columnName;
        functionColumnId = null;
    }

    // ------------------------------------------------------------------------------------------------------ columnType
    public void setColumnType(final int columnType) {
        this.columnType = columnType;
        functionColumnId = null;
    }

    ColumnTypeEnum getColumnTypeAsEnum() {
        return ColumnTypeEnum.valueOfColumnType(getColumnType());
    }

    void setColumnTypeAsEnum(final ColumnTypeEnum columnTypeAsEnum) {
        Objects.requireNonNull(columnTypeAsEnum, "columnTypeAsEnum is null");
        setColumnType(columnTypeAsEnum.fieldValueAsInt());
    }

    // ---------------------------------------------------------------------------------------------------- specificName
    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
        functionColumnId = null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient FunctionColumnId functionColumnId;

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    private String functionCat;

    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    private String functionSchem;

    @ColumnLabel(COLUMN_LABEL_FUNCTION_NAME)
    private String functionName;

    @ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @ColumnLabel("COLUMN_TYPE")
    private int columnType;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @_NullableBySpecification
    @ColumnLabel("PRECISION")
    private Integer precision;

    @ColumnLabel("LENGTH")
    private int length;

    @_NullableBySpecification
    @ColumnLabel("SCALE")
    private Integer scale;

    @ColumnLabel("RADIX")
    private int radix;

    @ColumnLabel("NULLABLE")
    private int nullable;

    @_NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7100
    @ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;
}
