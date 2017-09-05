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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * An entity class for binding information from
 * {@link java.sql.DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String) DatabaseMetaData.getColumns}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "columnName", "dataType", "typeName", "columnSize", "bufferLength",
    "decimalDigits", "numPrecRadix", "nullable", "remarks", "columnDef",
    "sqlDataType", "sqlDatetimeSub", "charOctetLength", "ordinalPosition",
    "isNullable", "scopeCatalog", "scopeSchema", "scopeTable",
    "sourceDataType", "isAutoincrement", "isGeneratedcolumn",
    // ---------------------------------------------------------------------
    "columnPrivileges"
})
public class Column implements Serializable {

    private static final long serialVersionUID = -409653682729081530L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(Column.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + ",tableSchem=" + tableSchem
               + ",tableName=" + tableName
               + ",columnName=" + columnName
               + ",dataType=" + dataType
               + ",typeName=" + typeName
               + ",columnSize=" + columnSize
               + ",bufferLength=" + bufferLength
               + ",decimalDigits=" + decimalDigits
               + ",numPrecRadix=" + numPrecRadix
               + ",nullable=" + nullable
               + ",remarks=" + remarks
               + ",columnDef=" + columnDef
               + ",sqlDataType=" + sqlDataType
               + ",sqlDatetimeSub=" + sqlDatetimeSub
               + ",charOctetLength=" + charOctetLength
               + ",ordinalPosition=" + ordinalPosition
               + ",isNullable=" + isNullable
               + ",scopeCatalog=" + scopeCatalog
               + ",scopeSchema=" + scopeSchema
               + ",scopeTable=" + scopeTable
               + ",sourceDataType=" + sourceDataType
               + ",isAutoincrement=" + isAutoincrement
               + ",isGeneratedcolumn=" + isGeneratedcolumn
               + "}";
    }

    // ---------------------------------------------------------------- tableCat
//    public String getTableCat() {
//        return tableCat;
//    }
//
//    public void setTableCat(final String tableCat) {
//        this.tableCat = tableCat;
//    }
    // -------------------------------------------------------------- tableSchem
//    public String getTableSchem() {
//        return tableSchem;
//    }
//
//    public void setTableSchem(final String tableSchem) {
//        this.tableSchem = tableSchem;
//    }
    // --------------------------------------------------------------- tableName
//    public String getTableName() {
//        return tableName;
//    }
//
//    public void setTableName(final String tableName) {
//        this.tableName = tableName;
//    }
    // -------------------------------------------------------------- columnName
//    public String getColumnName() {
//        return columnName;
//    }
//
//    public void setColumnName(final String columnName) {
//        this.columnName = columnName;
//    }
    // ---------------------------------------------------------------- dataType
//    public int getDataType() {
//        return dataType;
//    }
//
//    public void setDataType(final int dataType) {
//        this.dataType = dataType;
//    }
    // ---------------------------------------------------------------- typeName
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(final String typeName) {
//        this.typeName = typeName;
//    }
    // -------------------------------------------------------------- columnSize
//    public int getColumnSize() {
//        return columnSize;
//    }
//
//    public void setColumnSize(int columnSize) {
//        this.columnSize = columnSize;
//    }
    // ------------------------------------------------------------ bufferLength
//    public Integer getBufferLength() {
//        return bufferLength;
//    }
//
//    public void setBufferLength(final Integer bufferLength) {
//        this.bufferLength = bufferLength;
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
    // --------------------------------------------------------------- columnDef
//    public String getColumnDef() {
//        return columnDef;
//    }
//
//    public void setColumnDef(final String columnDef) {
//        this.columnDef = columnDef;
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
    // ------------------------------------------------------------ scopeCatalog
//    public String getScopeCatalog() {
//        return scopeCatalog;
//    }
//
//    public void setScopeCatalog(final String scopeCatalog) {
//        this.scopeCatalog = scopeCatalog;
//    }
    // ------------------------------------------------------------- scopeSchema
//    public String getScopeSchema() {
//        return scopeSchema;
//    }
//
//    public void setScopeSchema(final String scopeSchema) {
//        this.scopeSchema = scopeSchema;
//    }
    // -------------------------------------------------------------- scopeTable
//    public String getScopeTable() {
//        return scopeTable;
//    }
//
//    public void setScopeTable(final String scopeTable) {
//        this.scopeTable = scopeTable;
//    }
    // ---------------------------------------------------------- sourceDataType
//    public Short getSourceDataType() {
//        return sourceDataType;
//    }
//
//    public void setSourceDataType(final Short sourceDataType) {
//        this.sourceDataType = sourceDataType;
//    }
    // --------------------------------------------------------- isAutoincrement
//    public String getIsAutoincrement() {
//        return isAutoincrement;
//    }
//
//    public void setIsAutoincrement(final String isAutoincrement) {
//        this.isAutoincrement = isAutoincrement;
//    }
    // ------------------------------------------------------- isGeneratedcolumn
//    public String getIsGeneratedcolumn() {
//        return isGeneratedcolumn;
//    }
//
//    public void setIsGeneratedcolumn(final String isGeneratedcolumn) {
//        this.isGeneratedcolumn = isGeneratedcolumn;
//    }
    // -------------------------------------------------------- columnPrivileges
    public List<ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<ColumnPrivilege>();
        }
        return columnPrivileges;
    }

    @Deprecated
    public void setColumnPrivileges(
            final List<ColumnPrivilege> columnPrivileges) {
        this.columnPrivileges = columnPrivileges;
    }

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
    @Labeled("TYPE_NAME")
    @Getter
    @Setter
    private String typeName;

    @XmlElement(required = true)
    @Labeled("COLUMN_SIZE")
    @Getter
    @Setter
    private int columnSize;

    @XmlElement(nillable = true, required = true)
    @Labeled("BUFFER_LENGTH")
    @Unused
    @Getter
    @Setter
    private Integer bufferLength;

    @XmlElement(required = true)
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
    @Labeled("COLUMN_DEF")
    @Nillable
    @Getter
    @Setter
    private String columnDef;

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
    @Labeled("SCOPE_CATALOG")
    @Nillable
    @Getter
    @Setter
    private String scopeCatalog;

    @XmlElement(nillable = true, required = true)
    @Labeled("SCOPE_SCHEMA")
    @Nillable
    @Getter
    @Setter
    private String scopeSchema;

    @XmlElement(nillable = true, required = true)
    @Labeled("SCOPE_TABLE")
    @Nillable
    @Getter
    @Setter
    private String scopeTable;

    @XmlElement(nillable = true, required = true)
    @Labeled("SOURCE_DATA_TYPE")
    @Nillable
    @Getter
    @Setter
    private Short sourceDataType;

    @XmlElement(required = true)
    @Labeled("IS_AUTOINCREMENT")
    @Getter
    @Setter
    private String isAutoincrement;

    @XmlElement(required = true)
    @Labeled("IS_GENERATEDCOLUMN")
    @Getter
    @Setter
    private String isGeneratedcolumn;

    // -------------------------------------------------------------------------
    @XmlElementRef
    @Invokable(name = "getColumnPrivileges",
               types = {String.class, String.class, String.class, String.class},
               args = {
                   @Literals({":tableCat", ":tableSchem", ":tableName",
                              ":columnName"})
               }
    )
    private List<ColumnPrivilege> columnPrivileges;
}
