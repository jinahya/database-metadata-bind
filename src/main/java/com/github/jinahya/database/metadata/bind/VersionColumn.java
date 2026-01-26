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

import java.sql.DatabaseMetaData;

/**
 * A class for binding results of the {@link DatabaseMetaData#getVersionColumns(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getVersionColumns(String, String, String)
 */
@_ChildOf(Table.class)
@EqualsAndHashCode(callSuper = true)
public class VersionColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3587959398829593292L;

    // ----------------------------------------------------------------------------------------------------------- SCOPE
    public static final String COLUMN_LABEL_SCOPE = "SCOPE";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_SIZE = "COLUMN_SIZE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_BUFFER_LENGTH = "BUFFER_LENGTH";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DECIMAL_DIGITS = "DECIMAL_DIGITS";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    VersionColumn() {
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

    // ----------------------------------------------------------------------------------------------------------- scope

    /**
     * Returns the value of {@value COLUMN_LABEL_SCOPE} column.
     *
     * @return the value of {@value COLUMN_LABEL_SCOPE} column.
     */
    public Integer getScope() {
        return scope;
    }

    /**
     * Sets the value of {@value COLUMN_LABEL_SCOPE} column.
     *
     * @param scope the value of {@value COLUMN_LABEL_SCOPE} column.
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
     * Returns the value of {@code DATA_TYPE} column.
     *
     * @return the value of {@code DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@code DATA_TYPE} column.
     *
     * @param dataType the value of {@code DATA_TYPE} column.
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
    public void setPseudoColumn(final Integer pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SCOPE)
    private Integer scope;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_SIZE)
    private Integer columnSize;

    @_ColumnLabel(COLUMN_LABEL_BUFFER_LENGTH)
    private Integer bufferLength;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_DECIMAL_DIGITS)
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;
}
