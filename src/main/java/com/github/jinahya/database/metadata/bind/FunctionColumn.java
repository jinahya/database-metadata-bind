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

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 */
@_ChildOf(Function.class)
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FunctionColumn extends AbstractMetadataType {

    private static final long serialVersionUID = -7445156446214062680L;

    static final Comparator<FunctionColumn> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(FunctionColumn::functionCatNonNull, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(FunctionColumn::functionSchemNonNull, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(FunctionColumn::getFunctionName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(FunctionColumn::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<FunctionColumn> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(FunctionColumn::functionCatNonNull)
                    .thenComparing(FunctionColumn::functionSchemNonNull)
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
    public enum ColumnType implements _IntFieldEnum<ColumnType> {

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
        public static ColumnType valueOfColumnType(final int columnType) {
            return _IntFieldEnum.valueOfFieldValue(ColumnType.class, columnType);
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

    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnType() {
        return columnType;
    }

    public void setColumnType(final Integer columnType) {
        this.columnType = columnType;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    private String functionCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    private String functionSchem;

    @_ColumnLabel(COLUMN_LABEL_FUNCTION_NAME)
    private String functionName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @_NotNull
    @_ColumnLabel("COLUMN_TYPE")
    private Integer columnType;

    @_NotNull
    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_NullableBySpecification
    @_ColumnLabel("PRECISION")
    private Integer precision;

    @_NotNull
    @_ColumnLabel("LENGTH")
    private Integer length;

    @_NullableBySpecification
    @_ColumnLabel("SCALE")
    private Integer scale;

    @_NullableByVendor("Apache Derby")
    @_NotNull
    @_ColumnLabel("RADIX")
    private Integer radix;

    @_NotNull
    @_ColumnLabel("NULLABLE")
    private Integer nullable;

    @_NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7100
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @_NotNull
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @_ColumnLabel("SPECIFIC_NAME")
    private String specificName;

    String functionCatNonNull() {
        final String functionCat_ = getFunctionCat();
        if (functionCat_ != null) {
            return functionCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String functionSchemNonNull() {
        final String functionSchem_ = getFunctionSchem();
        if (functionSchem_ != null) {
            return functionSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }

    ColumnType getColumnTypeAsEnum() {
        return Optional.ofNullable(getColumnType())
                .map(ColumnType::valueOfColumnType)
                .orElse(null);
    }

    void setColumnTypeAsEnum(final ColumnType columnTypeAsEnum) {
        setColumnType(
                Optional.ofNullable(columnTypeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }
}
