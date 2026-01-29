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
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getBestRowIdentifier(String, String, String, int, boolean)
 * @see PseudoColumn
 */
@_ChildOf(Table.class)
public class BestRowIdentifier
        extends AbstractMetadataType {

    private static final long serialVersionUID = -1512051574198028399L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    static Comparator<BestRowIdentifier> comparingInSpecifiedOrder(final Context context,
                                                                   final Comparator<? super Integer> comparator)
//            throws SQLException
    {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator.comparing(BestRowIdentifier::getScope, comparator); // NOT nullable
    }

    static Comparator<BestRowIdentifier> comparingInSpecifiedOrder(final Context context)
//            throws SQLException
    {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, Comparator.naturalOrder());
    }

    // ----------------------------------------------------------------------------------------------------------- SCOPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE = "SCOPE";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // ------------------------------------------------------------------------------------------------------- DATA_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // --------------------------------------------------------------------------------------------------- PSEUDO_COLUMN

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_SIZE = "COLUMN_SIZE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_BUFFER_LENGTH = "BUFFER_LENGTH";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DECIMAL_DIGITS = "DECIMAL_DIGITS";

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    BestRowIdentifier() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "scope=" + scope +
               ",columnName=" + columnName +
               ",dataType=" + dataType +
               ",typeName=" + typeName +
               ",columnSize=" + columnSize +
               ",bufferLength=" + bufferLength +
               ",decimalDigits=" + decimalDigits +
               ",pseudoColumn=" + pseudoColumn +
               '}';
    }

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
        final var that = (BestRowIdentifier) obj;
        return Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), columnName);
    }

    // ----------------------------------------------------------------------------------------------------------- scope

    /**
     * Returns the value of {@value #COLUMN_LABEL_SCOPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SCOPE} column.
     */
    public Integer getScope() {
        return scope;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SCOPE} column.
     *
     * @param scope the value of {@value #COLUMN_LABEL_SCOPE} column.
     */
    void setScope(final Integer scope) {
        this.scope = scope;
    }

    // ------------------------------------------------------------------------------------------------------ columnName

    /**
     * Returns the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @param columnName the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType

    /**
     * Returns the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @param dataType the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName

    /**
     * Returns the value of {@code TYPE_NAME} column.
     *
     * @return the value of {@code TYPE_NAME} column.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of {@code TYPE_NAME} column.
     *
     * @param typeName the value of {@code TYPE_NAME} column.
     */
    void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize

    /**
     * Returns the value of {@code COLUMN_SIZE} column.
     *
     * @return the value of {@code COLUMN_SIZE} column.
     */
    public Integer getColumnSize() {
        return columnSize;
    }

    /**
     * Sets the value of {@code COLUMN_SIZE} column.
     *
     * @param columnSize the value of {@code COLUMN_SIZE} column.
     */
    void setColumnSize(final Integer columnSize) {
        this.columnSize = columnSize;
    }

    // ---------------------------------------------------------------------------------------------------- bufferLength

    /**
     * Returns the value of {@code BUFFER_LENGTH} column.
     *
     * @return the value of {@code BUFFER_LENGTH} column.
     */
    public Integer getBufferLength() {
        return bufferLength;
    }

    /**
     * Sets the value of {@code BUFFER_LENGTH} column.
     *
     * @param bufferLength the value of {@code BUFFER_LENGTH} column.
     */
    void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits

    /**
     * Returns the value of {@code DECIMAL_DIGITS} column.
     *
     * @return the value of {@code DECIMAL_DIGITS} column.
     */
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    /**
     * Sets the value of {@code DECIMAL_DIGITS} column.
     *
     * @param decimalDigits the value of {@code DECIMAL_DIGITS} column.
     */
    void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- pseudoColumn

    /**
     * Returns the value of {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     */
    public Integer getPseudoColumn() {
        return pseudoColumn;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     *
     * @param pseudoColumn the value of {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     */
    void setPseudoColumn(final Integer pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_SCOPE)
    private Integer scope;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_SIZE)
    private Integer columnSize;

    @org.jspecify.annotations.Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_BUFFER_LENGTH)
    private Integer bufferLength;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_DECIMAL_DIGITS)
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;
}
