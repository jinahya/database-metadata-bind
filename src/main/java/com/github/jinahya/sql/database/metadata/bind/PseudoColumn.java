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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "columnName", "dataType", "columnSize", "decimalDigits", "numPrecRadix",
    "columnUsage", "remarks", "charOctetLength", "isNullable"
})
public class PseudoColumn implements Serializable {

    private static final long serialVersionUID = -5612575879670895510L;

    // -------------------------------------------------------------------------
    private static final Logger logger
            = getLogger(PseudoColumn.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + ",tableSchem=" + tableSchem
               + ",tableName=" + tableName
               + ",columnName=" + columnName
               + ",dataType=" + dataType
               + ",columnSize=" + columnSize
               + ",decimalDigits=" + decimalDigits
               + ",numPrecRadix=" + numPrecRadix
               + ",columnUsage=" + columnUsage
               + ",remarks=" + remarks
               + ",charOctetLength=" + charOctetLength
               + ",isNullable=" + isNullable
               + "}";
    }

//    // ---------------------------------------------------------------- tableCat
//    public String getTableCat() {
//        return tableCat;
//    }
//
//    public void setTableCat(final String tableCat) {
//        this.tableCat = tableCat;
//    }
//
//    // -------------------------------------------------------------- tableSchem
//    public String getTableSchem() {
//        return tableSchem;
//    }
//
//    public void setTableSchem(final String tableSchem) {
//        this.tableSchem = tableSchem;
//    }
//
//    // --------------------------------------------------------------- tableName
//    public String getTableName() {
//        return tableName;
//    }
//
//    public void setTableName(final String tableName) {
//        this.tableName = tableName;
//    }
//
//    // -------------------------------------------------------------- columnName
//    public String getColumnName() {
//        return columnName;
//    }
//
//    public void setColumnName(final String columnName) {
//        this.columnName = columnName;
//    }
//
//    // ---------------------------------------------------------------- dataType
//    public int getDataType() {
//        return dataType;
//    }
//
//    public void setDataType(final int dataType) {
//        this.dataType = dataType;
//    }
//
//    // -------------------------------------------------------------- columnSize
//    public int getColumnSize() {
//        return columnSize;
//    }
//
//    public void setColumnSize(final int columnSize) {
//        this.columnSize = columnSize;
//    }
//
//    // ----------------------------------------------------------- decimalDigits
//    public Integer getDecimalDigits() {
//        return decimalDigits;
//    }
//
//    public void setDecimalDigits(final Integer decimalDigits) {
//        this.decimalDigits = decimalDigits;
//    }
//
//    // ------------------------------------------------------------ numPrecRadix
//    public int getNumPrecRadix() {
//        return numPrecRadix;
//    }
//
//    public void setNumPrecRadix(final int numPrecRadix) {
//        this.numPrecRadix = numPrecRadix;
//    }
//
//    // ------------------------------------------------------------- columnUsage
//    public String getColumnUsage() {
//        return columnUsage;
//    }
//
//    public void setColumnUsage(final String columnUsage) {
//        this.columnUsage = columnUsage;
//    }
//
//    // ----------------------------------------------------------------- remarks
//    public String getRemarks() {
//        return remarks;
//    }
//
//    public void setRemarks(final String remarks) {
//        this.remarks = remarks;
//    }
//
//    // --------------------------------------------------------- charOctetLength
//    public int getCharOctetLength() {
//        return charOctetLength;
//    }
//
//    public void setCharOctetLength(final int charOctetLength) {
//        this.charOctetLength = charOctetLength;
//    }
//
//    // -------------------------------------------------------------- isNullable
//    public String getIsNullable() {
//        return isNullable;
//    }
//
//    public void setIsNullable(final String isNullable) {
//        this.isNullable = isNullable;
//    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Labeled("TABLE_CAT")
    @Nillable
    @Getter
    @Setter
    private String tableCat;

    @XmlAttribute
    @Labeled("TABLE_SCHEM")
    @Nillable
    @Getter
    @Setter
    private String tableSchem;

    @XmlAttribute
    @Labeled("TABLE_NAME")
    @Getter
    @Setter
    private String tableName;

    // -------------------------------------------------------------------------
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
    @Labeled("COLUMN_SIZE")
    @Getter
    @Setter
    private int columnSize;

    @XmlElement(nillable = true, required = true)
    @Labeled("DECIMAL_DIGITS")
    @Nillable
    @Getter
    @Setter
    private Integer decimalDigits;

    @XmlElement(required = true)
    @Labeled("NUM_PREC_RADIX")
    @Getter
    @Setter
    private int numPrecRadix;

    @XmlElement(required = true)
    @Labeled("COLUMN_USAGE")
    @Getter
    @Setter
    private String columnUsage;

    @XmlElement(nillable = true, required = true)
    @Labeled("REMARKS")
    @Nillable
    @Getter
    @Setter
    private String remarks;

    @XmlElement(required = true)
    @Labeled("CHAR_OCTET_LENGTH")
    @Getter
    @Setter
    private int charOctetLength;

    @XmlElement(required = true)
    @Labeled("IS_NULLABLE")
    @Getter
    @Setter
    private String isNullable;
}
