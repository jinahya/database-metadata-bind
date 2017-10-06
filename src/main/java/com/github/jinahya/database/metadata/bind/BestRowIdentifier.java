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

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for best row identifiers of tables.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getBestRowIdentifier(java.lang.String, java.lang.String,
 * java.lang.String, int, boolean)
 */
@XmlRootElement
@XmlType(propOrder = {
    "scope", "columnName", "dataType", "typeName", "columnSize",
    "bufferLength", "decimalDigits", "pseudoColumn"
})
public class BestRowIdentifier implements Serializable {

    private static final long serialVersionUID = -6733770602373723371L;

    // -------------------------------------------------------------------------
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
    @Label("SCOPE")
    @Bind(label = "SCOPE")
    private short scope;

    @XmlElement
    @Label("COLUMN_NAME")
    @Bind(label = "COLUMN_NAME")
    private String columnName;

    @XmlElement
    @Label("DATA_TYPE")
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Label("TYPE_NAME")
    @Bind(label = "TYPE_NAME")
    private String typeName;

    @XmlElement
    @Label("COLUMN_SIZE")
    @Bind(label = "COLUMN_SIZE")
    private int columnSize;

    @XmlElement(nillable = true)
    @Unused
    @Label("BUFFER_LENGTH")
    @Bind(label = "BUFFER_LENGTH", unused = true)
    private Integer bufferLength;

    @XmlElement(nillable = true)
    @Nillable
    @Label("DECIMAL_DIGITS")
    @Bind(label = "DECIMAL_DIGITS", nillable = true)
    private Short decimalDigits;

    @XmlElement
    @Label("PSEUDO_COLUMN")
    @Bind(label = "PSEUDO_COLUMN")
    private short pseudoColumn;
    
    // -------------------------------------------------------------------------
    @Deprecated
    private Table table;
}
