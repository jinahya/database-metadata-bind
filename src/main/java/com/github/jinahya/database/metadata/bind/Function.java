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

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctions(String, String, String)
 */

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Function
        extends AbstractMetadataType {

    private static final long serialVersionUID = -3318947900237453301L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Function> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(Function::getFunctionCat, ContextUtils.nulls(context, comparator))
                .thenComparing(Function::getFunctionSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(Function::getFunctionName, ContextUtils.nulls(context, comparator))
                .thenComparing(Function::getSpecificName, ContextUtils.nulls(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_CAT = "FUNCTION_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_TYPE = "FUNCTION_TYPE";

    /**
     * Constants for {@value #COLUMN_LABEL_FUNCTION_TYPE} column values.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum FunctionType
            implements _IntFieldEnum<FunctionType> {

        /**
         * A value for {@link DatabaseMetaData#functionResultUnknown}({@value DatabaseMetaData#functionResultUnknown}).
         */
        FUNCTION_RESULT_UNKNOWN(DatabaseMetaData.functionResultUnknown),// 0

        /**
         * A value for {@link DatabaseMetaData#functionNoTable}({@value DatabaseMetaData#functionNoTable}).
         */
        FUNCTION_NO_TABLE(DatabaseMetaData.functionNoTable), // 1

        /**
         * A value for {@link DatabaseMetaData#functionReturnsTable}({@value DatabaseMetaData#functionReturnsTable}).
         */
        FUNCTION_RETURNS_TABLE(DatabaseMetaData.functionReturnsTable); // 2

        /**
         * Finds the value for specified {@link Function#COLUMN_LABEL_FUNCTION_TYPE} column value.
         *
         * @param fieldValue the {@link Function#COLUMN_LABEL_FUNCTION_TYPE} column value to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static FunctionType valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(FunctionType.class, fieldValue);
        }

        FunctionType(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // ---------------------------------------------------------------------------------------------------- functionType

    public FunctionType getFunctionTypeAsEnum() {
        return Optional.ofNullable(getFunctionType())
                .map(FunctionType::valueOfFieldValue)
                .orElse(null);
    }

    public void setFunctionTypeAsEnum(final FunctionType functionTypeAsEnum) {
        setFunctionType(
                Optional.ofNullable(functionTypeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    @EqualsAndHashCode.Include
    private String functionCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    @EqualsAndHashCode.Include
    private String functionSchem;

    @_ColumnLabel("FUNCTION_NAME")
    @EqualsAndHashCode.Include
    private String functionName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_ColumnLabel("FUNCTION_TYPE")
    private Integer functionType;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/849
    @_ColumnLabel("SPECIFIC_NAME")
    @EqualsAndHashCode.Include
    private String specificName;
}
