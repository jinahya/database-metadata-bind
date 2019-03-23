/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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
import java.util.logging.Logger;

import static java.sql.DatabaseMetaData.versionColumnNotPseudo;
import static java.sql.DatabaseMetaData.versionColumnPseudo;
import static java.sql.DatabaseMetaData.versionColumnUnknown;
import static java.util.logging.Logger.getLogger;

/**
 * An entity class for version columns.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)
 */
@XmlRootElement
@XmlType(propOrder = {
        "scope", "columnName", "dataType", "typeName", "columnSize", "bufferLength", "decimalDigits", "pseudoColumn"
})
public class VersionColumn implements Serializable {

    private static final long serialVersionUID = 3587959398829593292L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(VersionColumn.class.getName());

    // -------------------------------------------------------------------------

    /**
     * Constants for pseudo column values of version columns.
     *
     * @see DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)
     */
    public static enum PseudoColumn implements IntFieldEnum<PseudoColumn> {

        /**
         * Constant for {@link DatabaseMetaData#versionColumnUnknown} whose value is {@value
         * DatabaseMetaData#versionColumnUnknown}.
         */
        VERSION_COLUMN_NO_NULLS(versionColumnUnknown),
        /**
         * Constant for {@link DatabaseMetaData#versionColumnNotPseudo} whose value is {@value
         * DatabaseMetaData#versionColumnNotPseudo}.
         */
        VERSION_COLUMN_NULLABLE(versionColumnNotPseudo),
        /**
         * Constant for {@link DatabaseMetaData#versionColumnPseudo} whose value is {@value
         * DatabaseMetaData#versionColumnPseudo}.
         */
        VERSION_COLUMN_NULLABLE_UNKNOWN(versionColumnPseudo);

        // ---------------------------------------------------------------------

        /**
         * Returns the constant whose raw value equals to given. An instance of {@link IllegalArgumentException} will be
         * throw if no constants matches.
         *
         * @param rawValue the value value
         * @return the constant whose raw value equals to given.
         */
        public static PseudoColumn valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(PseudoColumn.class, rawValue);
        }

        // ---------------------------------------------------------------------
        private PseudoColumn(final int rawValue) {
            this.rawValue = rawValue;
        }

        // ---------------------------------------------------------------------

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        // ---------------------------------------------------------------------
        private final int rawValue;
    }

    // -------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public VersionColumn() {
        super();
    }

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "scope=" + scope
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
    public Short getScope() {
        return scope;
    }

    public void setScope(final Short scope) {
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
    public int getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(final int bufferLength) {
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

    /**
     * Returns current value of {@code pseudoColumn} property.
     *
     * @return current value of {@code pseudoColumn} property.
     * @see PseudoColumn
     */
    public short getPseudoColumn() {
        return pseudoColumn;
    }

    /**
     * Replace the value of {@code pseudoColumn} property with given.
     *
     * @param pseudoColumn new value for {@code pseudoColumn} property.
     */
    public void setPseudoColumn(final short pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }

    // -------------------------------------------------------------------------
    @XmlElement(nillable = true)
    @Bind(label = "SCOPE", unused = true)
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

    @XmlElement
    @Bind(label = "BUFFER_LENGTH")
    private int bufferLength;

    @XmlElement(nillable = true)
    @Bind(label = "DECIMAL_DIGITS", nillable = true)
    private Short decimalDigits;

    @XmlElement
    @Bind(label = "PSEUDO_COLUMN")
    private short pseudoColumn;
}
