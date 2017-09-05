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
 * {@link java.sql.DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "columnName", "columnType", "dataType", "typeName",
    "precision", "length", "scale", "radix", "nullable", "remarks",
    "columnDef", "sqlDataType", "sqlDatetimeSub", "charOctetLength",
    "ordinalPosition", "isNullable", "specificName"
})
public class ProcedureColumn implements Serializable {

    private static final long serialVersionUID = 3894753719381358829L;

    // -------------------------------------------------------------------------
    private static final Logger logger
            = getLogger(ProcedureColumn.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "procedureCat=" + procedureCat
               + ",procedureSchem=" + procedureSchem
               + ",procedureName=" + procedureName
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
               + ",columnDef=" + columnDef
               + ",sqlDataType=" + sqlDataType
               + ",sqlDatetimeSub=" + sqlDatetimeSub
               + ",charOctetLength=" + charOctetLength
               + ",ordinalPosition=" + ordinalPosition
               + ",isNullable=" + isNullable
               + ",specificName=" + specificName
               + "}";
    }

//    // ------------------------------------------------------------ procedureCat
//    public String getProcedureCat() {
//        return procedureCat;
//    }
//
//    public void setProcedureCat(final String procedureCat) {
//        this.procedureCat = procedureCat;
//    }
//
//    // ---------------------------------------------------------- procedureSchem
//    public String getProcedureSchem() {
//        return procedureSchem;
//    }
//
//    public void setProcedureSchem(final String procedureSchem) {
//        this.procedureSchem = procedureSchem;
//    }
//
//    // ----------------------------------------------------------- procedureName
//    public String getProcedureName() {
//        return procedureName;
//    }
//
//    public void setProcedureName(final String procedureName) {
//        this.procedureName = procedureName;
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
//    // --------------------------------------------------------------- columnDef
//    public String getColumnDef() {
//        return columnDef;
//    }
//
//    public void setColumnDef(final String columnDef) {
//        this.columnDef = columnDef;
//    }
//
//    // ------------------------------------------------------------- sqlDataType
//    public Integer getSqlDataType() {
//        return sqlDataType;
//    }
//
//    public void setSqlDataType(final Integer sqlDataType) {
//        this.sqlDataType = sqlDataType;
//    }
//
//    // ---------------------------------------------------------- sqlDatetimeSub
//    public Integer getSqlDatetimeSub() {
//        return sqlDatetimeSub;
//    }
//
//    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
//        this.sqlDatetimeSub = sqlDatetimeSub;
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
//    public String isNullable() {
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
    @Labeled("PROCEDURE_CAT")
    @Nillable
    @Getter
    @Setter
    private String procedureCat;

    @XmlAttribute
    @Labeled("PROCEDURE_SCHEM")
    @Nillable
    @Getter
    @Setter
    private String procedureSchem;

    @XmlAttribute
    @Labeled("PROCEDURE_NAME")
    @Getter
    @Setter
    private String procedureName;

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

    @XmlElement(required = true)
    @Labeled("TYPE_NAME")
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
    @Nillable
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
    @Labeled("COLUMN_DEF")
    @Nillable
    @Getter
    @Setter
    private String columnDef;

    @XmlElement(nillable = true, required = true)
    @Labeled("SQL_DATA_TYPE")
    @Reserved
    @Getter
    @Setter
    private Integer sqlDataType;

    @XmlElement(nillable = true, required = true)
    @Labeled("SQL_DATETIME_SUB")
    @Reserved
    @Getter
    @Setter
    private Integer sqlDatetimeSub;

    @XmlElement(nillable = true, required = true)
    @Labeled("CHAR_OCTET_LENGTH")
    @Nillable
    @Getter
    @Setter
    private Integer charOctetLength;

    @XmlElement(nillable = true, required = true)
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
