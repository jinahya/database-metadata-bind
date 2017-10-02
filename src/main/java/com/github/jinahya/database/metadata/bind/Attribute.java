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

import java.io.Serializable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for type attributes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getAttributes(java.lang.String, java.lang.String,
 * java.lang.String, java.lang.String)
 */
@XmlRootElement
@XmlType(propOrder = {
    "attrName", "dataType", "attrTypeName", "attrSize", "decimalDigits",
    "numPrecRadix", "nullable", "remarks", "attrDef", "sqlDataType",
    "sqlDatetimeSub", "charOctetLength", "ordinalPosition", "isNullable",
    "sourceDataType"
})
public class Attribute implements Serializable {

    private static final long serialVersionUID = 4555190007114217973L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(Attribute.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "typeCat=" + typeCat
               + ",typeSchem=" + typeSchem
               + ",typeName=" + typeName
               + ",attrName=" + attrName
               + ",dataType=" + dataType
               + ",attrTypeName=" + attrTypeName
               + ",attrSize=" + attrSize
               + ",decimalDigits=" + decimalDigits
               + ",numPrecRadix=" + numPrecRadix
               + ",nullable=" + nullable
               + ",remarks=" + remarks
               + ",attrDef=" + attrDef
               + ",sqlDataType=" + sqlDataType
               + ",sqlDatetimeSub=" + sqlDatetimeSub
               + ",charOctetLength=" + charOctetLength
               + ",ordinalPosition=" + ordinalPosition
               + ",isNullable=" + isNullable
               + ",sourceDataType=" + sourceDataType
               + "}";
    }

    // ----------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // --------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // ---------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ---------------------------------------------------------------- attrName
    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(final String attrName) {
        this.attrName = attrName;
    }

    // ---------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // ------------------------------------------------------------ attrTypeName
    public String getAttrTypeName() {
        return attrTypeName;
    }

    public void setAttrTypeName(final String attrTypeName) {
        this.attrTypeName = attrTypeName;
    }

    // ---------------------------------------------------------------- attrSize
    public int getAttrSize() {
        return attrSize;
    }

    public void setAttrSize(final int attrSize) {
        this.attrSize = attrSize;
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

    // ---------------------------------------------------------------- nullable
    public int getNullable() {
        return nullable;
    }

    public void setNullable(final int nullable) {
        this.nullable = nullable;
    }

    // ----------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ----------------------------------------------------------------- attrDef
    public String getAttrDef() {
        return attrDef;
    }

    public void setAttrDef(final String attrDef) {
        this.attrDef = attrDef;
    }

    // ------------------------------------------------------------- sqlDataType
    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    // ---------------------------------------------------------- sqlDatetimeSub
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    // --------------------------------------------------------- charOctetLength
    public int getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final int charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // --------------------------------------------------------- ordinalPosition
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // -------------------------------------------------------------- isNullable
    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ---------------------------------------------------------- sourceDataType
    public Short getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(final Short sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Label("TYPE_CAT")
    @Bind(label = "TYPE_CAT", nillable = true)
    @Nillable
    private String typeCat;

    @XmlAttribute
    @Label("TYPE_SCHEM")
    @Bind(label = "TYPE_SCHEM", nillable = true)
    @Nillable
    private String typeSchem;

    @XmlAttribute
    @Label("TYPE_NAME")
    @Bind(label = "TYPE_NAME")
    private String typeName;

    // -------------------------------------------------------------------------
    @XmlElement
    @Label("ATTR_NAME")
    @Bind(label = "ATTR_NAME")
    private String attrName;

    @XmlElement
    @Label("DATA_TYPE")
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Label("ATTR_TYPE_NAME")
    @Bind(label = "ATTR_TYPE_NAME")
    private String attrTypeName;

    @XmlElement
    @Label("ATTR_SIZE")
    @Bind(label = "ATTR_SIZE")
    private int attrSize;

    @XmlElement
    @Label("DECIMAL_DIGITS")
    @Bind(label = "DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement
    @Label("NUM_PREC_RADIX")
    @Bind(label = "NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement
    @Label("NULLABLE")
    @Bind(label = "NULLABLE")
    private int nullable;

    @XmlElement(nillable = true)
    @Label("REMARKS")
    @Bind(label = "REMARKS", nillable = true)
    @Nillable
    private String remarks;

    @XmlElement(nillable = true)
    @Label("ATTR_DEF")
    @Bind(label = "ATTR_DEF", nillable = true)
    @Nillable
    private String attrDef;

    @XmlElement(nillable = true)
    @Label("SQL_DATA_TYPE")
    @Bind(label = "SQL_DATA_TYPE", unused = true)
    @Unused
    private Integer sqlDataType;

    @XmlElement(nillable = true)
    @Label("SQL_DATETIME_SUB")
    @Bind(label = "SQL_DATETIME_SUB", unused = true)
    @Unused
    private Integer sqlDatetimeSub;

    @XmlElement
    @Label("CHAR_OCTET_LENGTH")
    @Bind(label = "CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement
    @Label("ORDINAL_POSITION")
    @Bind(label = "ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement
    @Label("IS_NULLABLE")
    @Bind(label = "IS_NULLABLE")
    private String isNullable;

    @XmlElement(nillable = true)
    @Nillable
    @Label("SOURCE_DATA_TYPE")
    @Bind(label = "SOURCE_DATA_TYPE", nillable = true)
    private Short sourceDataType;
}
