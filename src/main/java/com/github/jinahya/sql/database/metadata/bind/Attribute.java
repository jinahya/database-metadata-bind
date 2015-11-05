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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "attrName", "dataType", "attrTypeName", "attrSize", "decimalDigits",
        "numPrecRadix", "nullable", "remarks", "attrDef", "charOctetLength",
        "ordinalPosition", "isNullable", "sourceDataType"
    }
)
public class Attribute {


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


    // ---------------------------------------------------------------- NULLABLE
    public int getNullable() {

        return nullable;
    }


    public void setNullable(final int nullable) {

        this.nullable = nullable;
    }


    // ----------------------------------------------------------------- REMARKS
    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(final String remarks) {

        this.remarks = remarks;
    }


    // --------------------------------------------------------------- columnDef
    public String getColumnDef() {

        return attrDef;
    }


    public void setColumnDef(final String columnDef) {

        this.attrDef = columnDef;
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


    // --------------------------------------------------------- userDefinedType
    public UserDefinedType getUserDefinedType() {

        return userDefinedType;
    }


    public void setUserDefindedType(final UserDefinedType userDefinedType) {

        this.userDefinedType = userDefinedType;
    }


    @ColumnLabel("TYPE_CAT")
    @XmlAttribute
    private String typeCat;


    @ColumnLabel("TYPE_SCHEM")
    @XmlAttribute
    private String typeSchem;


    @ColumnLabel("TYPE_NAME")
    @XmlAttribute
    private String typeName;


    @ColumnLabel("ATTR_NAME")
    @XmlElement(required = true)
    private String attrName;


    @ColumnLabel("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;


    @ColumnLabel("ATTR_TYPE_NAME")
    @XmlElement(required = true)
    private String attrTypeName;


    @ColumnLabel("ATTR_SIZE")
    @XmlElement(required = true)
    private int attrSize;


    @ColumnLabel("DECIMAL_DIGITS")
    @XmlElement(required = true)
    private Integer decimalDigits;


    @ColumnLabel("NUM_PREC_RADIX")
    @XmlElement(required = true)
    private int numPrecRadix;


    @ColumnLabel("NULLABLE")
    @XmlElement(required = true)
    private int nullable;


    @ColumnLabel("REMARKS")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String remarks;


    @ColumnLabel("ATTR_DEF")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String attrDef;


    @ColumnLabel("SQL_DATA_TYPE")
    @XmlTransient
    @NotUsed
    private int sqlDataType;


    @ColumnLabel("SQL_DATETIME_SUB")
    @XmlTransient
    @NotUsed
    private int sqlDatetimeSub;


    @ColumnLabel("CHAR_OCTET_LENGTH")
    @XmlElement(required = true)
    private int charOctetLength;


    @ColumnLabel("ORDINAL_POSITION")
    @XmlElement(required = true)
    private int ordinalPosition;


    @ColumnLabel("IS_NULLABLE")
    @XmlElement(required = true)
    String isNullable;


    @ColumnLabel("SOURCE_DATA_TYPE")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification()
    private Short sourceDataType;


    @XmlTransient
    private UserDefinedType userDefinedType;


}

