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
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
public class ExportedKey {


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schema
     * @param table
     * @param exportedKeys
     *
     * @throws SQLException if a database access error occurs.
     *
     * @see DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final String catalog, final String schema, final String table,
        final Collection<? super ExportedKey> exportedKeys)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(exportedKeys, "null exportedKeys");

        if (suppression.isSuppressed(Table.SUPPRESSION_PATH_EXPORTED_KEYS)) {
            return;
        }

        final ResultSet resultSet = database.getExportedKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                exportedKeys.add(ColumnRetriever.retrieve(
                    ExportedKey.class, suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Table table)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(table, "null table");

        retrieve(database, suppression,
                 table.getSchema().getCatalog().getTableCat(),
                 table.getSchema().getTableSchem(), table.getTableName(),
                 table.getExportedKeys());

        for (final ExportedKey exportedKey : table.getExportedKeys()) {
            exportedKey.setTable(table);
        }
    }


    /**
     * Creates a new instance.
     */
    public ExportedKey() {

        super();
    }


    public String getPktableCat() {
        return pktableCat;
    }


    public void setPktableCat(String pktableCat) {
        this.pktableCat = pktableCat;
    }


    public String getPktableSchem() {
        return pktableSchem;
    }


    public void setPktableSchem(String pktableSchem) {
        this.pktableSchem = pktableSchem;
    }


    public String getPktableName() {
        return pktableName;
    }


    public void setPktableName(String pktableName) {
        this.pktableName = pktableName;
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {
        return table;
    }


    public void setTable(Table table) {
        this.table = table;
    }


    public String getPkcolumnName() {
        return pkcolumnName;
    }


    public void setPkcolumnName(String pkcolumnName) {
        this.pkcolumnName = pkcolumnName;
    }


    public String getFktableCat() {
        return fktableCat;
    }


    public void setFktableCat(String fktableCat) {
        this.fktableCat = fktableCat;
    }


    public String getFktableSchem() {
        return fktableSchem;
    }


    public void setFktableSchem(String fktableSchem) {
        this.fktableSchem = fktableSchem;
    }


    public String getFktableName() {
        return fktableName;
    }


    public void setFktableName(String fktableName) {
        this.fktableName = fktableName;
    }


    public Column getFkcolumnName() {
        return fkcolumnName;
    }


    public void setFkcolumnName(Column fkcolumnName) {
        this.fkcolumnName = fkcolumnName;
    }


    public short getKeySeq() {
        return keySeq;
    }


    public void setKeySeq(short keySeq) {
        this.keySeq = keySeq;
    }


    public short getUpdateRule() {
        return updateRule;
    }


    public void setUpdateRule(short updateRule) {
        this.updateRule = updateRule;
    }


    public short getDeleteRule() {
        return deleteRule;
    }


    public void setDeleteRule(short deleteRule) {
        this.deleteRule = deleteRule;
    }


    public String getFkName() {
        return fkName;
    }


    public void setFkName(String fkName) {
        this.fkName = fkName;
    }


    public String getPkName() {
        return pkName;
    }


    public void setPkName(String pkName) {
        this.pkName = pkName;
    }


    public short getDeferrability() {
        return deferrability;
    }


    public void setDeferrability(short deferrability) {
        this.deferrability = deferrability;
    }


    /**
     * primary key table catalog (may be {@code null}).
     */
    @ColumnLabel("PKTABLE_CAT")
    @SuppressionPath("exportedKey/pktableCat")
    @XmlAttribute
    private String pktableCat;


    /**
     * primary key table schema (may be {@code null}).
     */
    @ColumnLabel("PKTABLE_SCHEM")
    @SuppressionPath("exportedKey/pktableSchem")
    @XmlAttribute
    private String pktableSchem;


    /**
     * primary key table name.
     */
    @ColumnLabel("PKTABLE_NAME")
    //@SuppressionPath("exportedKey/pktableName")
    @XmlAttribute
    private String pktableName;


    /**
     * parent table.
     */
    @XmlTransient
    private Table table;


    /**
     * primary key column name.
     */
    @ColumnLabel("PKCOLUMN_NAME")
    @XmlElement(required = true)
    protected String pkcolumnName;


    /**
     * foreign key table catalog (may be {@code null}) being exported (may be
     * {@code null}).
     */
    @ColumnLabel("FKTABLE_CAT")
    @XmlElement(nillable = true, required = true)
    @XmlElementNillableBySpecification
    protected String fktableCat;


    /**
     * foreign key table schema (may be {@code null}) being exported (may be
     * {@code null}).
     *
     */
    @ColumnLabel("FKTABLE_NAME")
    @XmlElement(nillable = true, required = true)
    @XmlElementNillableBySpecification
    protected String fktableSchem;


    /**
     * foreign key table name being exported.
     */
    @ColumnLabel("FKTABLE_NAME")
    @XmlElement(required = true)
    protected String fktableName;


    /**
     * foreign key column name being exported.
     */
    @ColumnLabel("FKCOLUMN_NAME")
    @XmlElement(required = true)
    protected Column fkcolumnName;


    /**
     * sequence number within foreign key( a value of 1 represents the first
     * column of the foreign key, a value of 2 would represent the second column
     * within the foreign key).
     */
    @ColumnLabel("FKCOLUMN_NAME")
    @XmlElement(required = true)
    protected short keySeq;


    /**
     * What happens to foreign key when primary is updated:.
     * <ul>
     * <li>{@link java.sql.DatabaseMetaData#importedNoAction} - do not allow
     * update of primary key if it has been imported</li>
     * <li>{@link java.sql.DatabaseMetaData#importedKeyCascade} - change
     * imported key to agree with primary key update</li>
     * <li>{@link java.sql.DatabaseMetaData#importedKeySetNull} - change
     * imported key to NULL if its primary key has been updated</li>
     * <li>{@link java.sql.DatabaseMetaData#importedKeySetDefault} - change
     * imported key to default values if its primary key has been updated</li>
     * <li>{@link java.sql.DatabaseMetaData#importedKeyRestrict} - same as
     * importedKeyNoAction (for ODBC 2.x compatibility)</li>
     * </ul>
     */
    @ColumnLabel("UPDATE_RULE")
    @XmlElement(required = true)
    protected short updateRule;


    /**
     * What happens to the foreign key when primary is deleted.
     * <ul>
     * <li>{@link java.sql.DatabaseMetaData#importedKeyNoAction} - do not allow
     * delete of primary key if it has been imported</li>
     * <li>{@link java.sql.DatabaseMetaData#importedKeyCascade} - delete rows
     * that import a deleted key</li>
     * <li>{@link java.sql.DatabaseMetaData#importedKeySetNull} - change
     * imported key to NULL if its primary key has been deleted</li>
     * <li>{@link java.sql.DatabaseMetaData#importedKeyRestrict} - same as
     * importedKeyNoAction (for ODBC 2.x compatibility)</li>
     * <li>{@link java.sql.DatabaseMetaData#importedKeySetDefault} - change
     * imported key to default if its primary key has been deleted</li>
     * </ul>
     */
    @ColumnLabel("DELETE_RULE")
    @XmlElement(required = true)
    protected short deleteRule;


    /**
     * foreign key name (may be {@code null}).
     */
    @ColumnLabel("FK_NAME")
    @XmlElement(required = true)
    protected String fkName;


    /**
     * primary key name (may be {@code null}).
     */
    @ColumnLabel("PK_NAME")
    @XmlElement(required = true)
    protected String pkName;


    /**
     * can the evaluation of foreign key constraints be deferred until commit.
     * <ul>
     * <li>importedKeyInitiallyDeferred - see SQL92 for definition</li>
     * <li>importedKeyInitiallyImmediate - see SQL92 for definition</li>
     * <li>importedKeyNotDeferrable - see SQL92 for definition</li>
     * </ul>
     */
    @ColumnLabel("DEFERRABILITY")
    protected short deferrability;


}

