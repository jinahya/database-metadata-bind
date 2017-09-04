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
//    public String getTypeCat() {
//        return typeCat;
//    }
//
//    public void setTypeCat(final String typeCat) {
//        this.typeCat = typeCat;
//    }
    // --------------------------------------------------------------- typeSchem
//    public String getTypeSchem() {
//        return typeSchem;
//    }
//
//    public void setTypeSchem(final String typeSchem) {
//        this.typeSchem = typeSchem;
//    }
    // ---------------------------------------------------------------- typeName
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(final String typeName) {
//        this.typeName = typeName;
//    }
    // ---------------------------------------------------------------- attrName
//    public String getAttrName() {
//        return attrName;
//    }
//
//    public void setAttrName(final String attrName) {
//        this.attrName = attrName;
//    }
    // ---------------------------------------------------------------- dataType
//    public int getDataType() {
//        return dataType;
//    }
//
//    public void setDataType(final int dataType) {
//        this.dataType = dataType;
//    }
    // ------------------------------------------------------------ attrTypeName
//    public String getAttrTypeName() {
//        return attrTypeName;
//    }
//
//    public void setAttrTypeName(final String attrTypeName) {
//        this.attrTypeName = attrTypeName;
//    }
    // ---------------------------------------------------------------- attrSize
//    public int getAttrSize() {
//        return attrSize;
//    }
//
//    public void setAttrSize(final int attrSize) {
//        this.attrSize = attrSize;
//    }
    // ----------------------------------------------------------- decimalDigits
//    public Integer getDecimalDigits() {
//        return decimalDigits;
//    }
//
//    public void setDecimalDigits(final Integer decimalDigits) {
//        this.decimalDigits = decimalDigits;
//    }
    // ------------------------------------------------------------ numPrecRadix
//    public int getNumPrecRadix() {
//        return numPrecRadix;
//    }
//
//    public void setNumPrecRadix(final int numPrecRadix) {
//        this.numPrecRadix = numPrecRadix;
//    }
    // ---------------------------------------------------------------- nullable
//    public int getNullable() {
//        return nullable;
//    }
//
//    public void setNullable(final int nullable) {
//        this.nullable = nullable;
//    }
    // ----------------------------------------------------------------- remarks
//    public String getRemarks() {
//        return remarks;
//    }
//
//    public void setRemarks(final String remarks) {
//        this.remarks = remarks;
//    }
    // ----------------------------------------------------------------- attrDef
//    public String getAttrDef() {
//        return attrDef;
//    }
//
//    public void setAttrDef(final String attrDef) {
//        this.attrDef = attrDef;
//    }
    // ------------------------------------------------------------- sqlDataType
//    public Integer getSqlDataType() {
//        return sqlDataType;
//    }
//
//    public void setSqlDataType(final Integer sqlDataType) {
//        this.sqlDataType = sqlDataType;
//    }
    // ---------------------------------------------------------- sqlDatetimeSub
//    public Integer getSqlDatetimeSub() {
//        return sqlDatetimeSub;
//    }
//
//    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
//        this.sqlDatetimeSub = sqlDatetimeSub;
//    }
    // --------------------------------------------------------- charOctetLength
//    public int getCharOctetLength() {
//        return charOctetLength;
//    }
//
//    public void setCharOctetLength(final int charOctetLength) {
//        this.charOctetLength = charOctetLength;
//    }
    // --------------------------------------------------------- ordinalPosition
//    public int getOrdinalPosition() {
//        return ordinalPosition;
//    }
//
//    public void setOrdinalPosition(final int ordinalPosition) {
//        this.ordinalPosition = ordinalPosition;
//    }
    // -------------------------------------------------------------- isNullable
//    public String getIsNullable() {
//        return isNullable;
//    }
//
//    public void setIsNullable(final String isNullable) {
//        this.isNullable = isNullable;
//    }
    // ---------------------------------------------------------- sourceDataType
//    public Short getSourceDataType() {
//        return sourceDataType;
//    }
//
//    public void setSourceDataType(final Short sourceDataType) {
//        this.sourceDataType = sourceDataType;
//    }
    // -------------------------------------------------------------------------
    @XmlAttribute
    @Labeled("TYPE_CAT")
    @Nillable
    @Getter
    @Setter
    private String typeCat;

    @XmlAttribute
    @Labeled("TYPE_SCHEM")
    @Nillable
    @Getter
    @Setter
    private String typeSchem;

    @XmlAttribute
    @Labeled("TYPE_NAME")
    @Getter
    @Setter
    private String typeName;

    @XmlElement(required = true)
    @Labeled("ATTR_NAME")
    @Getter
    @Setter
    private String attrName;

    @XmlElement(required = true)
    @Labeled("DATA_TYPE")
    @Getter
    @Setter
    private int dataType;

    @XmlElement(required = true)
    @Labeled("ATTR_TYPE_NAME")
    @Getter
    @Setter
    private String attrTypeName;

    @XmlElement(required = true)
    @Labeled("ATTR_SIZE")
    @Getter
    @Setter
    private int attrSize;

    @XmlElement(required = true)
    @Labeled("DECIMAL_DIGITS")
    @Getter
    @Setter
    private Integer decimalDigits;

    @XmlElement(required = true)
    @Labeled("NUM_PREC_RADIX")
    @Getter
    @Setter
    private int numPrecRadix;

    @XmlElement(required = true)
    @Labeled("NULLABLE")
    @Getter
    @Setter
    private int nullable;

    @XmlElement(nillable = true, required = true)
    @Labeled("REMARKS")
    @Nillable
    @Getter
    @Setter
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @Labeled("ATTR_DEF")
    @Nillable
    @Getter
    @Setter
    private String attrDef;

    @XmlElement(nillable = true, required = true)
    @Labeled("SQL_DATA_TYPE")
    @Unused
    @Getter
    @Setter
    private Integer sqlDataType;

    @XmlElement(nillable = true, required = true)
    @Labeled("SQL_DATETIME_SUB")
    @Unused
    @Getter
    @Setter
    private Integer sqlDatetimeSub;

    @XmlElement(required = true)
    @Labeled("CHAR_OCTET_LENGTH")
    @Getter
    @Setter
    private int charOctetLength;

    @XmlElement(required = true)
    @Labeled("ORDINAL_POSITION")
    @Getter
    @Setter
    private int ordinalPosition;

    @XmlElement(required = true)
    @Labeled("IS_NULLABLE")
    @Getter
    @Setter
    private String isNullable;

    @XmlElement(nillable = true, required = true)
    @Labeled("SOURCE_DATA_TYPE")
    @Nillable
    @Getter
    @Setter
    private Short sourceDataType;
}
