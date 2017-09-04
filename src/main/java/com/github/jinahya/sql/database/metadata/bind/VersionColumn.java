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
package com.github.jinahya.sql.database.metadata.bind;

import java.io.Serializable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "scope", "columnName", "dataType", "typeName", "columnSize",
    "bufferLength", "decimalDigits", "pseudoColumn"
})
public class VersionColumn implements Serializable {

    private static final long serialVersionUID = 3587959398829593292L;

    // -------------------------------------------------------------------------
    private static final Logger logger
            = getLogger(VersionColumn.class.getName());

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
//    public Short getScope() {
//        return scope;
//    }
//
//    public void setScope(final Short scope) {
//        this.scope = scope;
//    }
    // -------------------------------------------------------------- columnName
//    public String getColumnName() {
//        return columnName;
//    }
//
//    public void setColumnName(final String columnName) {
//        this.columnName = columnName;
//    }
    // ---------------------------------------------------------------- dataType
//    public int getDataType() {
//        return dataType;
//    }
//
//    public void setDataType(final int dataType) {
//        this.dataType = dataType;
//    }
    // ---------------------------------------------------------------- typeName
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(final String typeName) {
//        this.typeName = typeName;
//    }
    // -------------------------------------------------------------- columnSize
//    public int getColumnSize() {
//        return columnSize;
//    }
//
//    public void setColumnSize(final int columnSize) {
//        this.columnSize = columnSize;
//    }
    // ------------------------------------------------------------ bufferLength
//    public int getBufferLength() {
//        return bufferLength;
//    }
//
//    public void setBufferLength(final int bufferLength) {
//        this.bufferLength = bufferLength;
//    }
    // ----------------------------------------------------------- decimalDigits
//    public Short getDecimalDigits() {
//        return decimalDigits;
//    }
//
//    public void setDecimalDigits(final Short decimalDigits) {
//        this.decimalDigits = decimalDigits;
//    }
    // ------------------------------------------------------------ pseudoColumn
//    public short getPseudoColumn() {
//        return pseudoColumn;
//    }
//
//    public void setPseudoColumn(final short pseudoColumn) {
//        this.pseudoColumn = pseudoColumn;
//    }
    // -------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @Labeled("SCOPE")
    @Unused
    @Getter
    @Setter
    private Short scope;

    @XmlElement(required = true)
    @Labeled("COLUMN_NAME")
    @Getter
    @Setter
    private String columnName;

    @XmlElement(required = true)
    @Labeled("DATA_TYPE")
    @Getter
    @Setter
    private int dataType;

    @XmlElement(required = true)
    @Labeled("TYPE_NAME")
    @Getter
    @Setter
    private String typeName;

    @XmlElement(required = true)
    @Labeled("COLUMN_SIZE")
    @Getter
    @Setter
    private int columnSize;

    @XmlElement(required = true)
    @Labeled("BUFFER_LENGTH")
    @Getter
    @Setter
    private int bufferLength;

    @XmlElement(nillable = true, required = true)
    @Labeled("DECIMAL_DIGITS")
    @Nillable
    @Getter
    @Setter
    private Short decimalDigits;

    @XmlElement(required = true)
    @Labeled("PSEUDO_COLUMN")
    @Getter
    @Setter
    private short pseudoColumn;
}
