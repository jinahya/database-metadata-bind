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
        "functionName", "columnName", "columnType", "dataType", "typeName",
        "precision", "length", "scale", "radix", "nullable", "remarks",
        "charOctetLength", "ordinalPosition", "isNullable", "specificName"
    }
)
public class FunctionColumn {


    // ------------------------------------------------------------- functionCat
    public String getFunctionCat() {

        return functionCat;
    }


    public void setFunctionCat(final String functionCat) {

        this.functionCat = functionCat;
    }


    // ----------------------------------------------------------- functionSchem
    public String getFunctionSchem() {

        return functionSchem;
    }


    public void setFunctionSchem(final String functionSchem) {

        this.functionSchem = functionSchem;
    }


    // ------------------------------------------------------------ functionName
    public String getFunctionName() {

        return functionName;
    }


    public void setFunctionName(final String functionName) {

        this.functionName = functionName;
    }


    // -------------------------------------------------------------- columnName
    public String getColumnName() {

        return columnName;
    }


    public void setColumnName(final String columnName) {

        this.columnName = columnName;
    }


    // -------------------------------------------------------------- columnType
    public short getColumnType() {

        return columnType;
    }


    public void setColumnType(final short columnType) {

        this.columnType = columnType;
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


    // --------------------------------------------------------------- precision
    public int getPrecision() {

        return precision;
    }


    public void setPrecision(final int precision) {

        this.precision = precision;
    }


    // ------------------------------------------------------------------ length
    public int getLength() {

        return length;
    }


    public void setLength(final int length) {

        this.length = length;
    }


    // ------------------------------------------------------------------- scale
    public Short getScale() {

        return scale;
    }


    public void setScale(final Short scale) {

        this.scale = scale;
    }


    // ------------------------------------------------------------------- radix
    public short getRadix() {

        return radix;
    }


    public void setRadix(final short radix) {

        this.radix = radix;
    }


    // ---------------------------------------------------------------- nullable
    public short getNullable() {

        return nullable;
    }


    public void setNullable(final short nullable) {

        this.nullable = nullable;
    }


    // ----------------------------------------------------------------- remarks
    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(final String remarks) {

        this.remarks = remarks;
    }


    // --------------------------------------------------------- charOctetLength
    public Integer getCharOctetLength() {

        return charOctetLength;
    }


    public void setCharOctetLength(final Integer charOctetLength) {

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
    public String isNullable() {

        return isNullable;
    }


    public void setIsNullable(final String isNullable) {

        this.isNullable = isNullable;
    }


    // ------------------------------------------------------------ specificName
    public String getSpecificName() {

        return specificName;
    }


    public void setSpecificName(final String specificName) {

        this.specificName = specificName;
    }


    // ------------------------------------------------------------------ schema
    public Schema getSchema() {

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    @ColumnLabel("FUNCTION_CAT")
    @XmlAttribute
    @NillableBySpecification
    private String functionCat;


    @ColumnLabel("FUNCTION_SCHEM")
    @XmlAttribute
    @NillableBySpecification
    private String functionSchem;


    @ColumnLabel("FUNCTION_NAME")
    @XmlElement(required = true)
    private String functionName;


    @ColumnLabel("COLUMN_NAME")
    @XmlElement(required = true)
    private String columnName;


    @ColumnLabel("COLUMN_TYPE")
    @XmlElement(required = true)
    private short columnType;


    @ColumnLabel("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;


    @ColumnLabel("TYPE_NAME")
    @XmlElement(required = true)
    private String typeName;


    @ColumnLabel("PRECISION")
    @XmlElement(required = true)
    private int precision;


    @ColumnLabel("LENGTH")
    @XmlElement(required = true)
    private int length;


    @ColumnLabel("SCALE")
    @XmlElement(required = true)
    private Short scale;


    @ColumnLabel("RADIX")
    @XmlElement(required = true)
    private short radix;


    @ColumnLabel("NULLABLE")
    @XmlElement(required = true)
    private short nullable;


    @ColumnLabel("REMARKS")
    @XmlElement(required = true)
    private String remarks;


    @ColumnLabel("CHAR_OCTET_LENGTH")
    @XmlElement(required = true)
    @NillableBySpecification
    private Integer charOctetLength;


    @ColumnLabel("ORDINAL_POSITION")
    @XmlElement(required = true)
    private int ordinalPosition;


    @ColumnLabel("IS_NULLABLE")
    @XmlElement(required = true)
    private String isNullable;


    @ColumnLabel("SPECIFIC_NAME")
    @XmlElement(required = true)
    private String specificName;


    @XmlTransient
    private Schema schema;


}

