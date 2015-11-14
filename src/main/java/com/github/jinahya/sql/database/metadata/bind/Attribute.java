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


import java.util.List;
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
@XmlType(
    propOrder = {
        "attrName", "dataType", "attrTypeName", "attrSize", "decimalDigits",
        "numPrecRadix", "nullable", "remarks", "attrDef", "sqlDataType",
        "sqlDatetimeSub", "charOctetLength", "ordinalPosition", "isNullable",
        "sourceDataType",
        // ---------------------------------------------------------------------
        "unknownResults"
    }
)
public class Attribute extends AbstractChild<UDT>
    implements Comparable<Attribute> {


    // They are ordered by TYPE_CAT, TYPE_SCHEM, TYPE_NAME and ORDINAL_POSITION.
    @Override
    public int compareTo(Attribute o) {

        if (this == o) {
            return 0;
        }

        if (typeCat == null) {
            if (o.getTypeCat() != null) {
                return -1;
            }
        } else {
            if (o.getTypeCat() == null) {
                return 1;
            }
            final int compared = typeCat.compareTo(o.getTypeCat());
            if (compared != 0) {
                return compared;
            }
        }

        if (typeSchem == null) {
            if (o.getTypeSchem() != null) {
                return -1;
            }
        } else {
            if (o.getTypeSchem() == null) {
                return 1;
            }
            final int compared = typeSchem.compareTo(o.getTypeSchem());
            if (compared != 0) {
                return compared;
            }
        }

        if (typeName == null) {
            if (o.getTypeName() != null) {
                return -1;
            }
        } else {
            if (o.getTypeName() == null) {
                return 1;
            }
            final int compared = typeName.compareTo(o.getTypeName());
            if (compared != 0) {
                return compared;
            }
        }

        return ordinalPosition - o.getOrdinalPosition();
    }


    @Override
    public String toString() {

        return super.toString() + "{"
               + "typeCat=" + typeCat
               + ", typeSchem=" + typeSchem
               + ", typeName=" + typeName
               + ", attrName=" + attrName
               + ", dataType=" + dataType
               + ", attrTypeName=" + attrTypeName
               + ", attrSize=" + attrSize
               + ", decimalDigits=" + decimalDigits
               + ", numPrecRadix=" + numPrecRadix
               + ", nullable=" + nullable
               + ", remarks=" + remarks
               + ", attrDef=" + attrDef
               + ", sqlDataType=" + sqlDataType
               + ", sqlDatetimeSub=" + sqlDatetimeSub
               + ", charOctetLength=" + charOctetLength
               + ", ordinalPosition=" + ordinalPosition
               + ", isNullable=" + isNullable
               + ", sourceDataType=" + sourceDataType
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


    // --------------------------------------------------------------------- UDT
    public UDT getUDT() {

        return getParent();
    }


    // -------------------------------------------------------------------------
    @Label("TYPE_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String typeCat;


    @Label("TYPE_SCHEM")
    @NillableBySpecification
    @XmlAttribute
    private String typeSchem;


    @Label("TYPE_NAME")
    @XmlAttribute
    private String typeName;


    @Label("ATTR_NAME")
    @XmlElement(required = true)
    private String attrName;


    @Label("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;


    @Label("ATTR_TYPE_NAME")
    @XmlElement(required = true)
    private String attrTypeName;


    @Label("ATTR_SIZE")
    @XmlElement(required = true)
    private int attrSize;


    @Label("DECIMAL_DIGITS")
    @XmlElement(required = true)
    private Integer decimalDigits;


    @Label("NUM_PREC_RADIX")
    @XmlElement(required = true)
    private int numPrecRadix;


    @Label("NULLABLE")
    @XmlElement(required = true)
    private int nullable;


    @Label("REMARKS")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String remarks;


    @Label("ATTR_DEF")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String attrDef;


    @Label("SQL_DATA_TYPE")
    @Unused
    @XmlElement(nillable = true, required = true)
    private Integer sqlDataType;


    @Label("SQL_DATETIME_SUB")
    @Unused
    @XmlElement(nillable = true, required = true)
    private Integer sqlDatetimeSub;


    @Label("CHAR_OCTET_LENGTH")
    @XmlElement(required = true)
    private int charOctetLength;


    @Label("ORDINAL_POSITION")
    @XmlElement(required = true)
    private int ordinalPosition;


    @Label("IS_NULLABLE")
    @XmlElement(required = true)
    private String isNullable;


    @Label("SOURCE_DATA_TYPE")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private Short sourceDataType;


    @XmlElement(name = "unknownResult", nillable = true)
    private List<UnknownResult> unknownResults;


}

