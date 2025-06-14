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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the value of {@value COLUMN_LABEL_SCOPE} column.
     *
     * @return the value of {@value COLUMN_LABEL_SCOPE} column.
     */
    public Integer getScope() {
        return scope;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getColumnName() {
        return columnName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Integer getDataType() {
        return dataType;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getTypeName() {
        return typeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Integer getColumnSize() {
        return columnSize;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Integer getBufferLength() {
        return bufferLength;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- pseudoColumn
    public Integer getPseudoColumn() {
        return pseudoColumn;
    }

    public void setPseudoColumn(Integer pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NotUsedBySpecification
    @_ColumnLabel("SCOPE")
    private Integer scope;

    @_ColumnLabel("COLUMN_NAME")

    private String columnName;

    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;
}
