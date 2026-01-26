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
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getBestRowIdentifier(String, String, String, int, boolean)
 * @see PseudoColumn
 */
@_ChildOf(Table.class)
@EqualsAndHashCode(callSuper = true)
public class BestRowIdentifier
        extends AbstractMetadataType {

    private static final long serialVersionUID = -1512051574198028399L;

    // -----------------------------------------------------------------------------------------------------------------
    private static Comparator<BestRowIdentifier> comparingInSpecifiedOrder(
            final Comparator<? super Integer> comparator) {
        return Comparator
                .comparing(BestRowIdentifier::getScope, comparator);
    }

    static Comparator<BestRowIdentifier> comparingInSpecifiedOrder() {
        return comparingInSpecifiedOrder(Comparator.naturalOrder());
    }

    static Comparator<BestRowIdentifier> comparingInSpecifiedOrder(final Context context)
            throws SQLException {
        return comparingInSpecifiedOrder(
                ContextUtils.nullPrecedence(context, Comparator.naturalOrder())
        );
    }

    // ----------------------------------------------------------------------------------------------------------- SCOPE

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE = "SCOPE";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // ------------------------------------------------------------------------------------------------------- DATA_TYPE

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // --------------------------------------------------------------------------------------------------- PSEUDO_COLUMN

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    public BestRowIdentifier() {
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
    public Integer getScope() {
        return scope;
    }

    public void setScope(final Integer scope) {
        this.scope = scope;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize
    
    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(final Integer columnSize) {
        this.columnSize = columnSize;
    }

    // ---------------------------------------------------------------------------------------------------- bufferLength
    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits
    
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- pseudoColumn
    public Integer getPseudoColumn() {
        return pseudoColumn;
    }

    public void setPseudoColumn(final Integer pseudoColumn) {
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

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_NotUsedBySpecification
    @_ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    
    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;
}
