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


import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctions(String, String, String)
 */
@_ChildOf(Catalog.class)
public class Function
        extends AbstractMetadataType {

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (Function) obj;
        return Objects.equals(functionCat, that.functionCat) &&
               Objects.equals(functionSchem, that.functionSchem) &&
               Objects.equals(functionName, that.functionName) &&
               Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), functionCat, functionSchem, functionName, specificName);
    }

    private static final long serialVersionUID = -3318947900237453301L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Function> specifiedOrder(final Comparator<? super String> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator
                .comparing(Function::getFunctionCat, comparator)
                .thenComparing(Function::getFunctionSchem, comparator)
                .thenComparing(Function::getFunctionName, comparator)
                .thenComparing(Function::getSpecificName, comparator);
    }

    static Comparator<Function> specifiedOrder(final Context context,
                                               final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return specifiedOrder(ContextUtils.nullPrecedence(context, comparator));
    }

    // ---------------------------------------------------------------------------------------------------- FUNCTION_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_CAT = "FUNCTION_CAT";

    // -------------------------------------------------------------------------------------------------- FUNCTION_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_SCHEM = "FUNCTION_SCHEM";

    // --------------------------------------------------------------------------------------------------- FUNCTION_NAME
    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_NAME = "FUNCTION_NAME";

    // --------------------------------------------------------------------------------------------------------- REMARKS
    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // --------------------------------------------------------------------------------------------------- FUNCTION_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FUNCTION_TYPE = "FUNCTION_TYPE";

    /**
     * A value for {@link #COLUMN_LABEL_FUNCTION_TYPE} column. The value is
     * {@link DatabaseMetaData#functionResultUnknown}({@value DatabaseMetaData#functionResultUnknown}).
     * <blockquote>
     * Cannot determine if a return value or table will be returned
     * </blockquote>
     */
    public static final int COLUMN_VALUE_FUNCTION_TYPE_FUNCTION_RESULT_UNKNOWN = // 0
            DatabaseMetaData.functionResultUnknown;

    /**
     * A value for {@link #COLUMN_LABEL_FUNCTION_TYPE} column. The value is
     * {@link DatabaseMetaData#functionNoTable}({@value DatabaseMetaData#functionNoTable}).
     * <blockquote>
     * Does not return a table
     * </blockquote>
     */
    public static final int COLUMN_VALUE_FUNCTION_TYPE_FUNCTION_NO_TABLE =       // 1
            DatabaseMetaData.functionNoTable;

    /**
     * A value for {@link #COLUMN_LABEL_FUNCTION_TYPE} column. The value is
     * {@link DatabaseMetaData#functionReturnsTable}({@value DatabaseMetaData#functionReturnsTable}).
     * <blockquote>
     * Returns a table
     * </blockquote>
     */
    public static final int COLUMN_VALUE_FUNCTION_TYPE_FUNCTION_RETURNS_TABLE =  // 2
            DatabaseMetaData.functionReturnsTable;

    static final List<Integer> FUNCTION_TYPE_VALUES = List.of(
            COLUMN_VALUE_FUNCTION_TYPE_FUNCTION_RESULT_UNKNOWN,
            COLUMN_VALUE_FUNCTION_TYPE_FUNCTION_NO_TABLE,
            COLUMN_VALUE_FUNCTION_TYPE_FUNCTION_RETURNS_TABLE
    );

    // --------------------------------------------------------------------------------------------------- SPECIFIC_NAME
    public static final String COLUMN_LABEL_SPECIFIC_NAME = "SPECIFIC_NAME";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // ------------------------------------------------------------------------------------------------ java.lang.Object
    @Override
    public String toString() {
        return super.toString() + '{' +
               "functionCat=" + functionCat +
               ",functionSchem=" + functionSchem +
               ",functionName=" + functionName +
               ",remarks=" + remarks +
               ",functionType=" + functionType +
               ",specificName=" + specificName +
               '}';
    }

    // ----------------------------------------------------------------------------------------------------- functionCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_FUNCTION_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FUNCTION_CAT} column.
     */
    public String getFunctionCat() {
        return functionCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FUNCTION_CAT} column.
     *
     * @param functionCat the value of {@value #COLUMN_LABEL_FUNCTION_CAT} column.
     */
    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    // --------------------------------------------------------------------------------------------------- functionSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_FUNCTION_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FUNCTION_SCHEM} column.
     */
    public String getFunctionSchem() {
        return functionSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FUNCTION_SCHEM} column.
     *
     * @param functionSchem the value of {@value #COLUMN_LABEL_FUNCTION_SCHEM} column.
     */
    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    // ---------------------------------------------------------------------------------------------------- functionName

    /**
     * Returns the value of {@value #COLUMN_LABEL_FUNCTION_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FUNCTION_NAME} column.
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FUNCTION_NAME} column.
     *
     * @param functionName the value of {@value #COLUMN_LABEL_FUNCTION_NAME} column.
     */
    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    // --------------------------------------------------------------------------------------------------------- remarks

    /**
     * Returns the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @return the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @param remarks the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ---------------------------------------------------------------------------------------------------- functionType

    /**
     * Returns the value of {@value #COLUMN_LABEL_FUNCTION_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FUNCTION_TYPE} column.
     */
    public Integer getFunctionType() {
        return functionType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FUNCTION_TYPE} column.
     *
     * @param functionType the value of {@value #COLUMN_LABEL_FUNCTION_TYPE} column.
     */
    public void setFunctionType(final Integer functionType) {
        this.functionType = functionType;
    }

    // ---------------------------------------------------------------------------------------------------- specificName

    /**
     * Returns the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     */
    public String getSpecificName() {
        return specificName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     *
     * @param specificName the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     */
    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------

   @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_CAT)
    private String functionCat;

   @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FUNCTION_SCHEM)
    private String functionSchem;

    @_ColumnLabel(COLUMN_LABEL_FUNCTION_NAME)
    private String functionName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @_ColumnLabel(COLUMN_LABEL_FUNCTION_TYPE)
    private Integer functionType;

    @_ColumnLabel(COLUMN_LABEL_SPECIFIC_NAME)
    private String specificName;
}
