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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An entity class for pseudo columns.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 */
@XmlRootElement
public class PseudoColumn extends TableChild {

    private static final long serialVersionUID = -5612575879670895510L;

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
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
               + '}';
    }

    // ---------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // --------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
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

    // -------------------------------------------------------------- columnSize
    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(final int columnSize) {
        this.columnSize = columnSize;
    }

    // ----------------------------------------------------------- decimalDigits
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ------------------------------------------------------------ numPrecRadix
    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(final int numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // ------------------------------------------------------------- columnUsage
    public String getColumnUsage() {
        return columnUsage;
    }

    public void setColumnUsage(final String columnUsage) {
        this.columnUsage = columnUsage;
    }

    // ----------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------- charOctetLength
    public int getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final int charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // -------------------------------------------------------------- isNullable
    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute
    @MayBeNull
    @Label("TABLE_CAT")
    @Bind(label = "TABLE_CAT", nillable = true)
    private String tableCat;

    @XmlAttribute
    @MayBeNull
    @Label("TABLE_SCHEM")
    @Bind(label = "TABLE_SCHEM", nillable = true)
    private String tableSchem;

    @XmlAttribute
    @Label("TABLE_NAME")
    @Bind(label = "TABLE_NAME")
    private String tableName;

    // -------------------------------------------------------------------------
    @XmlElement
    @Label("COLUMN_NAME")
    @Bind(label = "COLUMN_NAME")
    private String columnName;

    @XmlElement
    @Label("DATA_TYPE")
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Label("COLUMN_SIZE")
    @Bind(label = "COLUMN_SIZE")
    private int columnSize;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("DECIMAL_DIGITS")
    @Bind(label = "DECIMAL_DIGITS", nillable = true)
    private Integer decimalDigits;

    @XmlElement
    @Label("NUM_PREC_RADIX")
    @Bind(label = "NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement
    @Label("COLUMN_USAGE")
    @Bind(label = "COLUMN_USAGE")
    private String columnUsage;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("REMARKS")
    @Bind(label = "REMARKS", nillable = true)
    private String remarks;

    @XmlElement
    @Label("CHAR_OCTET_LENGTH")
    @Bind(label = "CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement
    @Label("IS_NULLABLE")
    @Bind(label = "IS_NULLABLE")
    private String isNullable;
}
