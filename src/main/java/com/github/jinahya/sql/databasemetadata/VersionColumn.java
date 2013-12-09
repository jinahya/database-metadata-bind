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


package com.github.jinahya.sql.databasemetadata;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "columnName", "dataType", "typeName", "columnSize", "bufferLength",
        "decimalDigits", "pseudoColumn"
    })
public class VersionColumn {


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schema
     * @param table
     * @param versionColumns
     *
     * @throws SQLException
     *
     * @see DatabaseMetaData#getVersionColumns(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final String catalog, final String schema, final String table,
        final Collection<? super VersionColumn> versionColumns)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (versionColumns == null) {
            throw new NullPointerException("versionColumns");
        }

        if (suppression.isSuppressed(Table.SUPPRESSION_PATH_VERSION_COLUMNS)) {
            return;
        }

        final ResultSet resultSet = database.getVersionColumns(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                versionColumns.add(ColumnRetriever.retrieve(
                    VersionColumn.class, suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Table table)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (table == null) {
            throw new NullPointerException("table");
        }

        retrieve(database, suppression,
                 table.getSchema().getCatalog().getTableCat(),
                 table.getSchema().getTableSchem(),
                 table.getTableName(),
                 table.getVersionColumns());

        for (final VersionColumn versionColumn : table.getVersionColumns()) {
            versionColumn.setTable(table);
        }
    }


    /**
     * Creates a new instance.
     */
    public VersionColumn() {

        super();
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
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


    // ------------------------------------------------------------ bufferLength
    public int getBufferLength() {

        return bufferLength;
    }


    public void setBufferLength(final int bufferLength) {

        this.bufferLength = bufferLength;
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


    @XmlTransient
    private Table table;


    @ColumnLabel("COLUMN_NAME")
    @XmlElement(required = true)
    private String columnName;


    @ColumnLabel("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;


    @ColumnLabel("TYPE_NAME")
    @XmlElement(required = true)
    private String typeName;


    @ColumnLabel("COLUMN_SIZE")
    @XmlElement(required = true)
    private int columnSize;


    @ColumnLabel("BUFFER_LENGTH")
    @XmlElement(required = true)
    private int bufferLength;


    @ColumnLabel("DECIMAL_DIGITS")
    @XmlElement(nillable = true, required = true)
    private Short decimalDigits;


    @ColumnLabel("PSEUDO_COLUMN")
    @XmlElement(required = true)
    private short pseudoColumn;


}

