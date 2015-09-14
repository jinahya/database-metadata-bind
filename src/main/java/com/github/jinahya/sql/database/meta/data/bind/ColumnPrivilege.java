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


package com.github.jinahya.sql.database.meta.data.bind;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "grantor", "grantee", "privilege", "isGrantable"
    }
)
public class ColumnPrivilege {


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schema
     * @param table
     * @param columnNamePattern
     * @param columnPrivileges
     *
     * @throws SQLException
     * @see DatabaseMetaData#getColumnPrivileges(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final String catalog, final String schema, final String table,
        final String columnNamePattern,
        final Collection<? super ColumnPrivilege> columnPrivileges)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (columnPrivileges == null) {
            throw new NullPointerException("null columnPrivileges");
        }

        if (suppression.isSuppressed(
            Table.SUPPRESSION_PATH_COLUMN_PRIVILEGES)) {
            return;
        }

        final ResultSet resultSet = database.getColumnPrivileges(
            catalog, schema, table, columnNamePattern);
        try {
            while (resultSet.next()) {
                columnPrivileges.add(ColumnRetriever.retrieve(
                    ColumnPrivilege.class, suppression, resultSet));
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
            throw new NullPointerException("null table");
        }

        retrieve(database, suppression,
                 table.getSchema().getCatalog().getTableCat(),
                 table.getSchema().getTableSchem(), table.getTableName(),
                 null,
                 table.getColumnPrivileges());

        for (final ColumnPrivilege columnPrivilege
             : table.getColumnPrivileges()) {
            columnPrivilege.setTable(table);
        }
    }


    /**
     * Creates a new instance.
     */
    public ColumnPrivilege() {

        super();
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    // ----------------------------------------------------------------- GRANTOR
    public String getGrantor() {

        return grantor;
    }


    public void setGrantor(final String grantor) {

        this.grantor = grantor;
    }


    // ----------------------------------------------------------------- GRANTEE
    public String getGrantee() {

        return grantee;
    }


    public void setGrantee(String grantee) {

        this.grantee = grantee;
    }


    // --------------------------------------------------------------- PRIVILEGE
    public String getPrivilege() {

        return privilege;
    }


    public void setPrivilege(final String privilege) {

        this.privilege = privilege;
    }


    // ------------------------------------------------------------ IS_GRANTABLE
    public String getIsGrantable() {

        return isGrantable;
    }


    public void setIsGrantable(final String isGrantable) {

        this.isGrantable = isGrantable;
    }


    @ColumnLabel("TABLE_CAT")
    @SuppressionPath("columnPrivilege/tableCat")
    @XmlAttribute
    private String tableCat;


    @ColumnLabel("TABLE_SCHEM")
    @SuppressionPath("columnPrivilege/tableSchem")
    @XmlAttribute
    private String tableSchem;


    @ColumnLabel("TABLE_NAME")
    //@SuppressionPath("columnPrivileg/tableName")
    @XmlAttribute
    private String tableName;


    @XmlTransient
    private Table table;


    @ColumnLabel("GRANTOR")
    @XmlElement(nillable = true, required = true)
    String grantor;


    @ColumnLabel("GRANTEE")
    @XmlElement(required = true)
    String grantee;


    @ColumnLabel("PRIVILEGE")
    @XmlElement(required = true)
    String privilege;


    @ColumnLabel("IS_GRANTABLE")
    @XmlElement(nillable = true, required = true)
    String isGrantable;


}

