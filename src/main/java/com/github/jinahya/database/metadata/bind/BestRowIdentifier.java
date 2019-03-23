/*
 * Copyright 2011 Jin Kwon <jinahya at gmail.com>.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.sql.DatabaseMetaData;

import static java.sql.DatabaseMetaData.bestRowNotPseudo;
import static java.sql.DatabaseMetaData.bestRowPseudo;
import static java.sql.DatabaseMetaData.bestRowSession;
import static java.sql.DatabaseMetaData.bestRowTemporary;
import static java.sql.DatabaseMetaData.bestRowTransaction;
import static java.sql.DatabaseMetaData.bestRowUnknown;

/**
 * Represents best row identifiers of tables.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
 */
@XmlRootElement
@XmlType(propOrder = {
        "scope", "columnName", "dataType", "typeName", "columnSize", "bufferLength", "decimalDigits", "pseudoColumn"
})
public class BestRowIdentifier implements Serializable {

    // -----------------------------------------------------------------------------------------------------------------
    private static final long serialVersionUID = -6733770602373723371L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for the value of {@code PSEUDO_COLUMN} of best row identifies of a table.
     *
     * @see DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
     */
    public enum PseudoColumn implements IntFieldEnum<PseudoColumn> {

        /**
         * Constant for {@link DatabaseMetaData#bestRowUnknown}.
         */
        BEST_ROW_UNKNWON(bestRowUnknown),

        /**
         * Constant for {@link DatabaseMetaData#bestRowNotPseudo}.
         */
        BEST_ROW_NOT_PSEUDO(bestRowNotPseudo),

        /**
         * Constant for {@link DatabaseMetaData#bestRowPseudo}.
         */
        BEST_ROW_PSEUDO(bestRowPseudo);

        // -------------------------------------------------------------------------------------------------------------

        /**
         * Returns the constant whose raw value equals to given.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given.
         */
        public static PseudoColumn valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(PseudoColumn.class, rawValue);
        }

        // -------------------------------------------------------------------------------------------------------------
        PseudoColumn(final int rawValue) {
            this.rawValue = rawValue;
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        // -----------------------------------------------------------------------------------------------------------------
        private final int rawValue;
    }

    /**
     * Constants for best row identifiers' scope.
     *
     * @see DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
     */
    public enum Scope implements IntFieldEnum<Scope> {

        /**
         * Constant for {@link DatabaseMetaData#bestRowTemporary}.
         */
        BEST_ROW_TEMPORARY(bestRowTemporary),

        /**
         * Constant for {@link DatabaseMetaData#bestRowTransaction}.
         */
        BEST_ROW_TRANSACTION(bestRowTransaction),

        /**
         * Constant for {@link DatabaseMetaData#bestRowSession}.
         */
        BEST_ROW_SESSION(bestRowSession);

        // ---------------------------------------------------------------------

        /**
         * Returns the constant whose raw value equals to given.
         *
         * @param rawValue the raw value to compare
         * @return the constant whose raw value equals to given.
         */
        public static Scope valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(Scope.class, rawValue);
        }

        // -------------------------------------------------------------------------------------------------------------
        Scope(final int value) {
            this.rawValue = value;
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        // -------------------------------------------------------------------------------------------------------------
        private final int rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "scope=" + getScope()
               + ",columnName=" + columnName
               + ",dataType=" + dataType
               + ",typeName=" + typeName
               + ",columnSize=" + columnSize
               + ",bufferLength=" + bufferLength
               + ",decimalDigits=" + decimalDigits
               + ",pseudoColumn=" + pseudoColumn
               + "}";
    }

    // ------------------------------------------------------------------- scope
    public short getScope() {
        return scope;
    }

    public void setScope(final short scope) {
        this.scope = scope;
    }

    // -------------------------------------------------------------- columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ---------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // ---------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // -------------------------------------------------------------- columnSize
    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(final int columnSize) {
        this.columnSize = columnSize;
    }

    // ------------------------------------------------------------ bufferLength
    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    // ----------------------------------------------------------- decimalDigits
    public Short getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(final Short decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ------------------------------------------------------------ pseudoColumn
    public short getPseudoColumn() {
        return pseudoColumn;
    }

    public void setPseudoColumn(final short pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }

    // -------------------------------------------------------------------------
    @XmlElement
    @Bind(label = "SCOPE")
    private short scope;

    @XmlElement
    @Bind(label = "COLUMN_NAME")
    private String columnName;

    @XmlElement
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Bind(label = "TYPE_NAME")
    private String typeName;

    @XmlElement
    @Bind(label = "COLUMN_SIZE")
    private int columnSize;

    @XmlElement(nillable = true)
    @Bind(label = "BUFFER_LENGTH", unused = true)
    private Integer bufferLength;

    @XmlElement(nillable = true)
    @Bind(label = "DECIMAL_DIGITS", nillable = true)
    private Short decimalDigits;

    @XmlElement
    @Bind(label = "PSEUDO_COLUMN")
    private short pseudoColumn;
}
