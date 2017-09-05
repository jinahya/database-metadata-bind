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
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 * DatabaseMetaData#getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "functionName", "columnName", "columnType", "dataType", "typeName",
    "precision", "length", "scale", "radix", "nullable", "remarks",
    "charOctetLength", "ordinalPosition", "isNullable", "specificName"
})
public class FunctionColumn implements Serializable {

    private static final long serialVersionUID = -7445156446214062680L;

    // -------------------------------------------------------------------------
    private static final Logger logger
            = getLogger(FunctionColumn.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "functionCat=" + functionCat
               + ",functionSchem=" + functionSchem
               + ",functionName=" + functionName
               + ",columnName=" + columnName
               + ",columnType=" + columnType
               + ",dataType=" + dataType
               + ",typeName=" + typeName
               + ",precision=" + precision
               + ",length=" + length
               + ",scale=" + scale
               + ",radix=" + radix
               + ",nullable=" + nullable
               + ",remarks=" + remarks
               + ",charOctetLength=" + charOctetLength
               + ",ordinalPosition=" + ordinalPosition
               + ",isNullable=" + isNullable
               + ",specificName=" + specificName
               + "}";
    }

//    // ------------------------------------------------------------- functionCat
//    public String getFunctionCat() {
//        return functionCat;
//    }
//
//    public void setFunctionCat(final String functionCat) {
//        this.functionCat = functionCat;
//    }
//
//    // ----------------------------------------------------------- functionSchem
//    public String getFunctionSchem() {
//        return functionSchem;
//    }
//
//    public void setFunctionSchem(final String functionSchem) {
//        this.functionSchem = functionSchem;
//    }
//
//    // ------------------------------------------------------------ functionName
//    public String getFunctionName() {
//        return functionName;
//    }
//
//    public void setFunctionName(final String functionName) {
//        this.functionName = functionName;
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
//    // -------------------------------------------------------------- columnType
//    public short getColumnType() {
//        return columnType;
//    }
//
//    public void setColumnType(final short columnType) {
//        this.columnType = columnType;
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
//    // ---------------------------------------------------------------- typeName
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(final String typeName) {
//        this.typeName = typeName;
//    }
//
//    // --------------------------------------------------------------- precision
//    public int getPrecision() {
//        return precision;
//    }
//
//    public void setPrecision(final int precision) {
//        this.precision = precision;
//    }
//
//    // ------------------------------------------------------------------ length
//    public int getLength() {
//        return length;
//    }
//
//    public void setLength(final int length) {
//        this.length = length;
//    }
//
//    // ------------------------------------------------------------------- scale
//    public Short getScale() {
//        return scale;
//    }
//
//    public void setScale(final Short scale) {
//        this.scale = scale;
//    }
//
//    // ------------------------------------------------------------------- radix
//    public short getRadix() {
//        return radix;
//    }
//
//    public void setRadix(final short radix) {
//        this.radix = radix;
//    }
//
//    // ---------------------------------------------------------------- nullable
//    public short getNullable() {
//        return nullable;
//    }
//
//    public void setNullable(final short nullable) {
//        this.nullable = nullable;
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
//    public Integer getCharOctetLength() {
//        return charOctetLength;
//    }
//
//    public void setCharOctetLength(final Integer charOctetLength) {
//        this.charOctetLength = charOctetLength;
//    }
//
//    // --------------------------------------------------------- ordinalPosition
//    public int getOrdinalPosition() {
//        return ordinalPosition;
//    }
//
//    public void setOrdinalPosition(final int ordinalPosition) {
//        this.ordinalPosition = ordinalPosition;
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
//
//    // ------------------------------------------------------------ specificName
//    public String getSpecificName() {
//        return specificName;
//    }
//
//    public void setSpecificName(final String specificName) {
//        this.specificName = specificName;
//    }
    // -------------------------------------------------------------------------
    @XmlAttribute
    @Labeled("FUNCTION_CAT")
    @Nillable
    @Getter
    @Setter
    private String functionCat;

    @XmlAttribute
    @Labeled("FUNCTION_SCHEM")
    @Nillable
    @Getter
    @Setter
    private String functionSchem;

    @XmlAttribute
    @Labeled("FUNCTION_NAME")
    @Getter
    @Setter
    private String functionName;

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    @Labeled("COLUMN_NAME")
    @Getter
    @Setter
    private String columnName;

    @XmlElement(required = true)
    @Labeled("COLUMN_TYPE")
    @Getter
    @Setter
    private short columnType;

    @XmlElement(required = true)
    @Labeled("DATA_TYPE")
    @Getter
    @Setter
    private int dataType;

    @Labeled("TYPE_NAME")
    @XmlElement(required = true)
    @Getter
    @Setter
    private String typeName;

    @XmlElement(required = true)
    @Labeled("PRECISION")
    @Getter
    @Setter
    private int precision;

    @XmlElement(required = true)
    @Labeled("LENGTH")
    @Getter
    @Setter
    private int length;

    @XmlElement(required = true)
    @Labeled("SCALE")
    @Getter
    @Setter
    private Short scale;

    @XmlElement(required = true)
    @Labeled("RADIX")
    @Getter
    @Setter
    private short radix;

    @XmlElement(required = true)
    @Labeled("NULLABLE")
    @Getter
    @Setter
    private short nullable;

    @XmlElement(required = true)
    @Labeled("REMARKS")
    @Getter
    @Setter
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @Labeled("CHAR_OCTET_LENGTH")
    @Nillable
    @Getter
    @Setter
    private Integer charOctetLength;

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

    @XmlElement(required = true)
    @Labeled("SPECIFIC_NAME")
    @Getter
    @Setter
    private String specificName;
}
