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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "attrName", "dataType", "attrTypeName", "attrSize", "decimalDigits",
    "numPrecRadix", "nullable", "remarks", "attrDef", "sqlDataType",
    "sqlDatetimeSub", "charOctetLength", "ordinalPosition", "isNullable",
    "sourceDataType"
})
public class Attribute {

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
    @Labeled("TYPE_CAT")
    @Nillable
    @XmlAttribute
    private String typeCat;

    @Labeled("TYPE_SCHEM")
    @Nillable
    @XmlAttribute
    private String typeSchem;

    @Labeled("TYPE_NAME")
    @XmlAttribute
    private String typeName;

    @Labeled("ATTR_NAME")
    @XmlElement(required = true)
    private String attrName;

    @Labeled("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;

    @Labeled("ATTR_TYPE_NAME")
    @XmlElement(required = true)
    private String attrTypeName;

    @Labeled("ATTR_SIZE")
    @XmlElement(required = true)
    private int attrSize;

    @Labeled("DECIMAL_DIGITS")
    @XmlElement(required = true)
    private Integer decimalDigits;

    @Labeled("NUM_PREC_RADIX")
    @XmlElement(required = true)
    private int numPrecRadix;

    @Labeled("NULLABLE")
    @XmlElement(required = true)
    private int nullable;

    @Labeled("REMARKS")
    @Nillable
    @XmlElement(nillable = true, required = true)
    private String remarks;

    @Labeled("ATTR_DEF")
    @Nillable
    @XmlElement(nillable = true, required = true)
    private String attrDef;

    @Labeled("SQL_DATA_TYPE")
    @Unused
    @XmlElement(nillable = true, required = true)
    private Integer sqlDataType;

    @Labeled("SQL_DATETIME_SUB")
    @Unused
    @XmlElement(nillable = true, required = true)
    private Integer sqlDatetimeSub;

    @Labeled("CHAR_OCTET_LENGTH")
    @XmlElement(required = true)
    private int charOctetLength;

    @Labeled("ORDINAL_POSITION")
    @XmlElement(required = true)
    private int ordinalPosition;

    @Labeled("IS_NULLABLE")
    @XmlElement(required = true)
    private String isNullable;

    @Labeled("SOURCE_DATA_TYPE")
    @Nillable
    @XmlElement(nillable = true, required = true)
    private Short sourceDataType;
}
