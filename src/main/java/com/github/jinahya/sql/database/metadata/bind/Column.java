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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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
        "columnName", "dataType", "typeName", "columnSize", "decimalDigits",
        "numPrecRadix", "nullable", "remarks", "columnDef", "charOctetLength",
        "ordinalPosition", "isNullable", "scopeCatalog", "scopeSchema",
        "scopeTable", "sourceDataType", "isAutoincrement", "isGeneratedcolumn",
        // ---------------------------------------------------------------------
        "columnPrivileges"
    }
)
public class Column {


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


    public String getScopeCatalog() {
        return scopeCatalog;
    }


    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }


    public String getScopeSchema() {
        return scopeSchema;
    }


    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }


    public String getScopeTable() {
        return scopeTable;
    }


    public void setScopeTable(String scopeTable) {
        this.scopeTable = scopeTable;
    }


    public String getSourceDataType() {
        return sourceDataType;
    }


    public void setSourceDataType(String sourceDataType) {
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


    public void setIsGeneratedColumn(final String isGeneratedcolumn) {

        this.isGeneratedcolumn = isGeneratedcolumn;
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    // -------------------------------------------------------- columnPrivileges
    public List<ColumnPrivilege> getColumnPrivileges() {

        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<ColumnPrivilege>();
        }

        return columnPrivileges;
    }


    @Label("TABLE_CAT")
    @XmlAttribute
    private String tableCat;


    @Label("TABLE_SCHEM")
    @XmlAttribute
    private String tableSchem;


    @Label("TABLE_NAME")
    @XmlAttribute
    private String tableName;


    @Label("COLUMN_NAME")
    @XmlElement(required = true)
    private String columnName;


    @Label("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;


    @Label("TYPE_NAME")
    @XmlElement(required = true)
    private String typeName;


    @Label("COLUMN_SIZE")
    @XmlElement(required = true)
    private int columnSize;


    @Label("BUFFER_LENGTH")
    @XmlTransient
    @NotUsed
    private Object bufferLength;


    @Label("DECIMAL_DIGITS")
    @XmlElement(required = true)
    private int decimalDigits;


    @Label("NUM_PREC_RADIX")
    @XmlElement(required = true)
    private int numPrecRadix;


    @Label("NULLABLE")
    @XmlElement(required = true)
    private int nullable;


    @Label("REMARKS")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String remarks;


    @Label("COLUMN_DEF")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String columnDef;


    @Label("SQL_DATA_TYPE")
    @XmlTransient
    @NotUsed
    private int sqlDataType;


    @Label("SQL_DATETIME_SUB")
    @XmlTransient
    @NotUsed
    private int sqlDatetimeSub;


    @Label("CHAR_OCTET_LENGTH")
    @XmlElement(required = true)
    private int charOctetLength;


    @Label("ORDINAL_POSITION")
    @XmlElement(required = true)
    private int ordinalPosition;


    @Label("IS_NULLABLE")
    @XmlElement(required = true)
    private String isNullable;


    @Label("SCOPE_CATALOG")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String scopeCatalog;


    @Label("SCOPE_SCHEMA")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String scopeSchema;


    @Label("SCOPE_TABLE")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String scopeTable;


    @Label("SOURCE_DATA_TYPE")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String sourceDataType;


    @Label("IS_AUTOINCREMENT")
    @XmlElement(required = true)
    private String isAutoincrement;


    @Label("IS_GENERATEDCOLUMN")
    @XmlElement(required = true)
    private String isGeneratedcolumn;


    @XmlTransient
    private Table table;


    @Invocation(
        name = "getColumnPrivileges",
        types = {String.class, String.class, String.class, String.class},
        argsarr = {
            @InvocationArgs({
                ":tableCat", ":tableSchem", ":tableName", ":columnName"
            })
        }
    )
    @XmlElementRef
    private List<ColumnPrivilege> columnPrivileges;


}

