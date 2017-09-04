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
public class Column {

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

    // --------------------------------------------------------------- columnDef
    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(final String columnDef) {
        this.columnDef = columnDef;
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

    // ------------------------------------------------------------ scopeCatalog
    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(final String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    // ------------------------------------------------------------- scopeSchema
    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(final String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    // -------------------------------------------------------------- scopeTable
    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(final String scopeTable) {
        this.scopeTable = scopeTable;
    }

    // ---------------------------------------------------------- sourceDataType
    public Short getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(final Short sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    // --------------------------------------------------------- isAutoincrement
    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(final String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    // ------------------------------------------------------- isGeneratedcolumn
    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

    public void setIsGeneratedcolumn(final String isGeneratedcolumn) {
        this.isGeneratedcolumn = isGeneratedcolumn;
    }

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
    @Getter
    @Setter
    @Labeled("TABLE_CAT")
    @Nillable
    @XmlAttribute
    private String tableCat;

    @Getter
    @Setter
    @Labeled("TABLE_SCHEM")
    @Nillable
    @XmlAttribute
    private String tableSchem;

    @Getter
    @Setter
    @Labeled("TABLE_NAME")
    @XmlAttribute
    private String tableName;

    @Getter
    @Setter
    @Labeled("COLUMN_NAME")
    @XmlElement(required = true)
    private String columnName;

    @Getter
    @Setter
    @Labeled("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;

    @Getter
    @Setter
    @Labeled("TYPE_NAME")
    @XmlElement(required = true)
    private String typeName;

    @Getter
    @Setter
    @Labeled("COLUMN_SIZE")
    @XmlElement(required = true)
    private int columnSize;

    @Getter
    @Setter
    @Labeled("BUFFER_LENGTH")
    @Unused
    @XmlElement(nillable = true, required = true)
    private Integer bufferLength;

    @Labeled("DECIMAL_DIGITS")
    @Nillable
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

    @Labeled("COLUMN_DEF")
    @Nillable
    @XmlElement(nillable = true, required = true)
    private String columnDef;

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

    @Labeled("SCOPE_CATALOG")
    @Nillable
    @XmlElement(nillable = true, required = true)
    private String scopeCatalog;

    @Labeled("SCOPE_SCHEMA")
    @Nillable
    @XmlElement(nillable = true, required = true)
    private String scopeSchema;

    @Labeled("SCOPE_TABLE")
    @Nillable
    @XmlElement(nillable = true, required = true)
    private String scopeTable;

    @Labeled("SOURCE_DATA_TYPE")
    @Nillable
    @XmlElement(nillable = true, required = true)
    private Short sourceDataType;

    @Labeled("IS_AUTOINCREMENT")
    @XmlElement(required = true)
    private String isAutoincrement;

    @Labeled("IS_GENERATEDCOLUMN")
    @XmlElement(required = true)
    private String isGeneratedcolumn;

    @Invokable(name = "getColumnPrivileges",
               types = {String.class, String.class, String.class, String.class},
               args = {
                   @Literals({":tableCat", ":tableSchem", ":tableName",
                              ":columnName"})
               }
    )
    @XmlElementRef
    private List<ColumnPrivilege> columnPrivileges;
}
