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
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for columns
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
    /**
     * Constants for nullabilities of table columns.
     *
     * @see DatabaseMetaData#getColumns(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public static enum Nullable {

        /**
         * Constant for {@link DatabaseMetaData#columnNoNulls}.
         */
        NO_NULLS(DatabaseMetaData.columnNoNulls),
        /**
         * Constant for {@link DatabaseMetaData#columnNullable}.
         */
        NULLABLE(DatabaseMetaData.columnNullable),
        /**
         * Constant for {@link DatabaseMetaData#columnNullableUnknown}.
         */
        NULLABLE_UNKNOWN(DatabaseMetaData.columnNullableUnknown);

        // ---------------------------------------------------------------------
        /**
         * Returns the constant whose raw value equals to given. An instance of
         * {@link IllegalArgumentException} will be throw if no constants
         * matches.
         *
         * @param rawValue the value value
         * @return the constant whose raw value equals to given.
         */
        public static Nullable valueOf(final int rawValue) {
            for (final Nullable value : values()) {
                if (value.rawValue == rawValue) {
                    return value;
                }
            }
            throw new IllegalArgumentException("no constant for " + rawValue);
        }

        // ---------------------------------------------------------------------
        private Nullable(final int rawValue) {
            this.rawValue = rawValue;
        }

        // ---------------------------------------------------------------------
        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        public int getRawValue() {
            return rawValue;
        }

        // ---------------------------------------------------------------------
        private final int rawValue;
    }

    // -------------------------------------------------------------------------
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
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // --------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // -------------------------------------------------------------- columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
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

    // -------------------------------------------------------------- columnSize
    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    // ------------------------------------------------------------ bufferLength
    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
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

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Label("TABLE_CAT")
    @Bind(label = "TABLE_CAT", nillable = true)
    @Nillable
    private String tableCat;

    @XmlAttribute
    @Label("TABLE_SCHEM")
    @Bind(label = "TABLE_SCHEM", nillable = true)
    @Nillable
    private String tableSchem;

    @XmlAttribute
    @Label("TABLE_NAME")
    @Bind(label = "TABLE_NAME")
    private String tableName;

    // -------------------------------------------------------------------------
    @XmlElement
    @Label("COLUMN_NAME")
    @Bind(label = "COLUMN_NAME")
    private String columnName;

    @XmlElement
    @Label("DATA_TYPE")
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Label("TYPE_NAME")
    @Bind(label = "TYPE_NAME")
    private String typeName;

    @XmlElement
    @Label("COLUMN_SIZE")
    @Bind(label = "COLUMN_SIZE")
    private int columnSize;

    @XmlElement(nillable = true)
    @Label("BUFFER_LENGTH")
    @Bind(label = "BUFFER_LENGTH", unused = true)
    @Unused
    private Integer bufferLength;

    @XmlElement
    @Label("DECIMAL_DIGITS")
    @Bind(label = "DECIMAL_DIGITS", nillable = true)
    @Nillable
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
    @Label("COLUMN_DEF")
    @Bind(label = "COLUMN_DEF", nillable = true)
    @Nillable
    private String columnDef;

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
    @Label("SCOPE_CATALOG")
    @Bind(label = "SCOPE_CATALOG", nillable = true)
    @Nillable
    private String scopeCatalog;

    @XmlElement(nillable = true)
    @Label("SCOPE_SCHEMA")
    @Bind(label = "SCOPE_SCHEMA", nillable = true)
    @Nillable
    private String scopeSchema;

    @XmlElement(nillable = true)
    @Label("SCOPE_TABLE")
    @Bind(label = "SCOPE_TABLE", nillable = true)
    @Nillable
    private String scopeTable;

    @XmlElement(nillable = true)
    @Label("SOURCE_DATA_TYPE")
    @Bind(label = "SOURCE_DATA_TYPE", nillable = true)
    @Nillable
    private Short sourceDataType;

    @XmlElement
    @Label("IS_AUTOINCREMENT")
    @Bind(label = "IS_AUTOINCREMENT")
    private String isAutoincrement;

    @XmlElement
    @Label("IS_GENERATEDCOLUMN")
    @Bind(label = "IS_GENERATEDCOLUMN")
    private String isGeneratedcolumn;

    // -------------------------------------------------------------------------
    @XmlElementRef
    @Invoke(name = "getColumnPrivileges",
            types = {String.class, String.class, String.class, String.class},
            parameters = {
                @Literals({":tableCat", ":tableSchem", ":tableName",
                           ":columnName"})
            }
    )
    private List<ColumnPrivilege> columnPrivileges;

    // -------------------------------------------------------------------------
    @Deprecated
    private Table table;
}
