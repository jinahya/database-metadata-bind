/*
 * Copyright 2011 Jin Kwon <jinahya at gmail.com>.
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


import java.sql.DatabaseMetaData;
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
        "columnName", "dataType", "typeName", "columnSize", "decimalDigits",
        "pseudoColumn"
    }
)
public class BestRowIdentifier {


    // ------------------------------------------------------------------- scope
    public short getScope() {

        return scope;
    }


    public void setScope(final short scope) {

        this.scope = scope;
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


    public void setColumnSize(final int columnSize) {

        this.columnSize = columnSize;
    }


    // ----------------------------------------------------------- decimalDigits
    public Short getDecimalDigits() {

        return decimalDigits;
    }


    public void setDecimalDigits(final Short decimalDigits) {

        this.decimalDigits = decimalDigits;
    }


    // ------------------------------------------------------------ pseudoColumn
    public short getPseudoColumn() {

        return pseudoColumn;
    }


    public void setPseudoColumn(final short pseudoColumn) {

        this.pseudoColumn = pseudoColumn;
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    /**
     * actual scope of result.
     * <ul>
     * <li>{@link DatabaseMetaData#bestRowTemporary} - very temporary, while
     * using row</li>
     * <li>{@link DatabaseMetaData#bestRowTransaction} - valid for remainder of
     * current transaction</li>
     * <li>{@link DatabaseMetaData#bestRowSession} - valid for remainder of
     * current session</li>
     * </ul>
     */
    @ColumnLabel("SCOPE")
    @XmlAttribute
    short scope;


    /**
     * column name.
     */
    @ColumnLabel("COLUMN_NAME")
    @XmlElement(nillable = false, required = true)
    String columnName;


    /**
     * SQL data type from {@link java.sql.Types}.
     */
    @ColumnLabel("DATA_TYPE")
    @XmlElement(nillable = false, required = true)
    int dataType;


    /**
     * Data source dependent type name, for a UDT the type name is fully
     * qualified.
     */
    @ColumnLabel("TYPE_NAME")
    @XmlElement(nillable = false, required = true)
    String typeName;


    /**
     * precision.
     */
    @ColumnLabel("COLUMN_SIZE")
    @XmlElement(nillable = false, required = true)
    int columnSize;


    @ColumnLabel("BUFFER_LENGTH")
    //@XmlElement(nillable = false, required = true)
    @XmlTransient
    @NotUsed
    private int bufferLength;


    @ColumnLabel("DECIMAL_DIGITS")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private Short decimalDigits;


    @ColumnLabel("PSEUDO_COLUMN")
    @XmlElement(nillable = false, required = true)
    short pseudoColumn;


    @XmlTransient
    private Table table;


}

