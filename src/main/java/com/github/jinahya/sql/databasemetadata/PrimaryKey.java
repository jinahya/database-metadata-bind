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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "columnName", "keySeq", "pkName"
    }
)
public class PrimaryKey implements Comparable<PrimaryKey> {


    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(PrimaryKey.class);


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schema
     * @param table
     * @param primaryKeys
     *
     * @throws SQLException if a database access error occurs.
     *
     * @see DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final String catalog, final String schema, final String table,
        final Collection<? super PrimaryKey> primaryKeys)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (primaryKeys == null) {
            throw new NullPointerException("null primaryKeys");
        }

        if (suppression.isSuppressed(Table.SUPPRESSION_PATH_PRIMARY_KEYS)) {
            return;
        }

        final ResultSet resultSet = database.getPrimaryKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
//                LOGGER.trace("primaryKey.next()");
                primaryKeys.add(ColumnRetriever.retrieve(
                    PrimaryKey.class, suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    /**
     *
     * @param database
     * @param suppression
     * @param table
     *
     * @throws SQLException if a database access error occurs.
     */
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
            throw new NullPointerException("null table");
        }

        retrieve(database, suppression,
                 table.getSchema().getCatalog().getTableCat(),
                 table.getSchema().getTableSchem(),
                 table.getTableName(),
                 table.getPrimaryKeys());

        for (final PrimaryKey primaryKey : table.getPrimaryKeys()) {
            primaryKey.table = table;
        }
    }


    /**
     * Creates a new instance.
     */
    public PrimaryKey() {

        super();
    }


    @Override
    public int compareTo(final PrimaryKey o) {

        return columnName.compareTo(o.columnName);
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    // -------------------------------------------------------------- columnName
    public String getColumnName() {

        return columnName;
    }


    public void setColumnName(final String columnName) {

        this.columnName = columnName;
    }


    // ------------------------------------------------------------------ keySeq
    public short getKeySeq() {

        return keySeq;
    }


    public void setKeySeq(final short keySeq) {

        this.keySeq = keySeq;
    }


    // ------------------------------------------------------------------ pkName
    public String getPkName() {

        return pkName;
    }


    public void setPkName(final String pkName) {

        this.pkName = pkName;
    }


    /**
     * table catalog (may be {@code null}).
     */
    @ColumnLabel("TABLE_CAT")
    @SuppressionPath("primaryKey/tableCat")
    @XmlAttribute
    private String tableCat;


    /**
     * table schema (may be {@code null}).
     */
    @ColumnLabel("TABLE_SCHEM")
    @SuppressionPath("primaryKey/tableSchem")
    @XmlAttribute
    private String tableSchem;


    /**
     * table name.
     */
    @ColumnLabel("TABLE_Name")
    @XmlAttribute
    private String tableName;


    /**
     * parent table.
     */
    @XmlTransient
    private Table table;


    /**
     * column name.
     */
    @ColumnLabel("COLUMN_NAME")
    @XmlElement(required = true)
    String columnName;


    /**
     * sequence number within primary key. (a value of 1 represents the first
     * column of the primary key, a value of 2 would represent the second column
     * within the primary key).
     */
    @ColumnLabel("KEY_SEQ")
    //@SuppressionPath("primaryKey/keySeq")
    @XmlElement(required = true)
    short keySeq;


    /**
     * primary key name (may be {@code null})
     */
    @ColumnLabel("PK_NAME")
    @SuppressionPath("primaryKey/pkName")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String pkName;


}

