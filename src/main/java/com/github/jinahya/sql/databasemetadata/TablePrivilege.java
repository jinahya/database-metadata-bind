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


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "grantor", "grantee", "privilege", "isGrantable"
    }
)
public class TablePrivilege {


    public static TablePrivilege retrieve(final Suppression suppression,
                                          final ResultSet resultSet)
        throws SQLException {

        if (suppression == null) { throw new NullPointerException("null suppression");}

        if (resultSet == null) { throw new NullPointerException("resultSet"); }

        final TablePrivilege instance = new TablePrivilege();

        ColumnRetriever.retrieve(TablePrivilege.class, instance, suppression,
                                 resultSet);

        return instance;
    }


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param tablePrivileges
     *
     * @throws SQLException
     *
     * @see DatabaseMetaData#getTablePrivileges(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final String catalog, final String schemaPattern,
        final String tableNamePattern,
        final Collection<? super TablePrivilege> tablePrivileges)
        throws SQLException {

        if (database == null) { throw new NullPointerException("null database");}

        if (suppression == null) { throw new NullPointerException("null suppression");}

        if (tablePrivileges == null) { throw new NullPointerException("tablePrivileges"); }

        if (suppression.isSuppressed(Table.SUPPRESSION_PATH_TABLE_PRIVILEGES)) {
            return;
        }

        final ResultSet resultSet = database.getTablePrivileges(
            catalog, schemaPattern, tableNamePattern);
        try {
            while (resultSet.next()) {
                final TablePrivilege instance = retrieve(
                    suppression, resultSet);
                tablePrivileges.add(instance);
            }
        } finally {
            resultSet.close();
        }
    }


    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Table table)
        throws SQLException {

        if (database == null) { throw new NullPointerException("null database");}

        if (suppression == null) { throw new NullPointerException("null suppression");}

        if (table == null) { throw new NullPointerException("table"); }

        retrieve(database, suppression,
                 table.getSchema().getCatalog().getTableCat(),
                 table.getSchema().getTableSchem(),
                 table.getTableName(),
                 table.getTablePrivileges());

        for (final TablePrivilege tablePrivilege : table.getTablePrivileges()) {
            tablePrivilege.setTable(table);
        }
    }


    /**
     * Creates a new instance.
     */
    public TablePrivilege() {

        super();
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    // ----------------------------------------------------------------- grantor
    public String getGrantor() {

        return grantor;
    }


    public void setGrantor(final String grantor) {

        this.grantor = grantor;
    }


    // ----------------------------------------------------------------- grantee
    public String getGrantee() {

        return grantee;
    }


    public void setGrantee(final String grantee) {

        this.grantee = grantee;
    }


    // --------------------------------------------------------------- privilege
    public String getPrivilege() {

        return privilege;
    }


    public void setPrivilege(final String privilege) {

        this.privilege = privilege;
    }


    // ------------------------------------------------------------- isGrantable
    public String getIsGrantable() {

        return isGrantable;
    }


    public void setIsGrantable(final String isGrantable) {

        this.isGrantable = isGrantable;
    }


    @ColumnLabel("TABLE_CAT")
    @SuppressionPath("tablePrivilege/tableCat")
    @XmlAttribute
    private String tableCat;


    @ColumnLabel("TABLE_SCHEM")
    @SuppressionPath("tablePrivilege/tableSchem")
    @XmlAttribute
    private String tableSchem;


    @ColumnLabel("TABLE_NAME")
    @XmlAttribute
    private String tableName;


    @XmlTransient
    private Table table;


    @ColumnLabel("GRANTOR")
    //@SuppressionPath("tablePrivilege/grantor")
    @XmlElement(nillable = true, required = true)
    private String grantor;


    @ColumnLabel("GRANTEE")
    //@SuppressionPath("tablePrivilege/grantee")
    @XmlElement(required = true)
    private String grantee;


    @ColumnLabel("PRIVILEGE")
    //@SuppressionPath("tablePrivilege/privilege")
    @XmlElement(required = true)
    private String privilege;


    @ColumnLabel("IS_GRANTABLE")
    @SuppressionPath("tablePrivilege/isGrantable")
    @XmlElement(nillable = true, required = true)
    private String isGrantable;


}

