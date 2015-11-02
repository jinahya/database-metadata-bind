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


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ImportedKey {


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schema
     * @param table
     * @param importedKeys
     *
     * @throws SQLException if a database access error occurs.
     *
     * @see DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final String catalog, final String schema, final String table,
        final Collection<? super ImportedKey> importedKeys)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (importedKeys == null) {
            throw new NullPointerException("importedKeys");
        }

        if (suppression.isSuppressed(Table.SUPPRESSION_PATH_IMPORTED_KEYS)) {
            return;
        }

        final ResultSet resultSet = database.getImportedKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                importedKeys.add(ColumnRetriever.retrieve(
                    ImportedKey.class, suppression, resultSet));
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
                 table.getSchema().getTableSchem(), table.getTableName(),
                 table.getImportedKeys());

        for (final ImportedKey importedKey : table.getImportedKeys()) {
            importedKey.setTable(table);
        }
    }


    /**
     * Creates a new instance.
     */
    public ImportedKey() {

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


    // ------------------------------------------------------------ pkColumnName
    public String getPkcolumnName() {

        return pkcolumnName;
    }


    public void setPkcolumnName(final String pkcolumnName) {

        this.pkcolumnName = pkcolumnName;
    }


    // ------------------------------------------------------------ fkcolumnName
    public Column getFkcolumnName() {

        return fkcolumnName;
    }


    public void setFkcolumnName(final Column fkcolumnName) {

        this.fkcolumnName = fkcolumnName;
    }


    // ------------------------------------------------------------------ keySeq
    public short getKeySeq() {

        return keySeq;
    }


    public void setKeySeq(final short keySeq) {

        this.keySeq = keySeq;
    }


    // -------------------------------------------------------------- updateRule
    public short getUpdateRule() {

        return updateRule;
    }


    public void setUpdateRule(short updateRule) {

        this.updateRule = updateRule;
    }


    // -------------------------------------------------------------- deleteRule
    public short getDeleteRule() {

        return deleteRule;
    }


    public void setDeleteRule(final short deleteRule) {

        this.deleteRule = deleteRule;
    }


    // ------------------------------------------------------------------ fnname
    public String getFkName() {

        return fkName;
    }


    public void setFkName(final String fkName) {

        this.fkName = fkName;
    }


    // ------------------------------------------------------------------ pkName
    public String getPkName() {

        return pkName;
    }


    public void setPkName(final String pkName) {

        this.pkName = pkName;
    }


    // --------------------------------------------------------- deferrerability
    /**
     * Returns current value of {@link #deferrability}.
     *
     * @return current value of {@link #deferrability}.
     */
    public short getDeferrability() {

        return deferrability;
    }


    /**
     * Replaces value of {@link #deferrability} with given.
     *
     * @param deferrability new value for {@link #deferrability}
     */
    public void setDeferrability(final short deferrability) {

        this.deferrability = deferrability;
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    /**
     * primary key table catalog being imported (may be {@code null}).
     */
    @ColumnLabel("PKTABLE_CAT")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String pktableCat;


    /**
     * primary key table schema being imported (may be {@code null}).
     */
    @ColumnLabel("PKTABLE_SCHEM")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String pktableSchem;


    /**
     * primary key table name being imported.
     */
    @ColumnLabel("PKTABLE_NAME")
    @XmlElement(required = true)
    private String pktableName;


    /**
     * primary key column name being imported.
     */
    @ColumnLabel("PKCOLUMN_NAME")
    @XmlElement(required = true)
    String pkcolumnName;


    /**
     * foreign key table catalog (may be {@code null}).
     */
    @ColumnLabel("FKTABLE_CAT")
    @SuppressionPath("importedKey/fktableCat")
    @XmlAttribute
    private String fktableCat;


    /**
     * foreign key table schema (may be {@code null}).
     */
    @ColumnLabel("FKTABLE_NAME")
    @SuppressionPath("importedKey/fktableSchem")
    @XmlAttribute
    private String fktableSchem;


    /**
     * foreign key table name.
     */
    @ColumnLabel("FKTABLE_NAME")
    @XmlAttribute
    private String fktableName;


    /**
     * foreign key column name.
     */
    @ColumnLabel("FKCOLUMN_NAME")
    @XmlElement(required = true)
    Column fkcolumnName;


    /**
     * sequence number within a foreign key( a value of 1 represents the first
     * column of the foreign key, a value of 2 would represent the second column
     * within the foreign key).
     */
    @ColumnLabel("FKCOLUMN_NAME")
    @XmlElement(required = true)
    short keySeq;


    /**
     * What happens to a foreign key when primary is updated:.
     * <ul>
     * <li>{@link java.sql.DatabaseMetaData#importedKeyNoAction} - do not allow
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
    short updateRule;


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
    short deleteRule;


    /**
     * foreign key name (may be {@code null}).
     */
    @ColumnLabel("FK_NAME")
    @XmlElement(required = true)
    String fkName;


    /**
     * primary key name (may be {@code null}).
     */
    @ColumnLabel("PK_NAME")
    @XmlElement(required = true)
    String pkName;


    /**
     * can the evaluation of foreign key constraints be deferred until commit.
     * <ul>
     * <li>{@link DatabaseMetaData#importedKeyInitiallyDeferred} - see SQL92 for
     * definition</li>
     * <li>{@link DatabaseMetaData#importedKeyInitiallyImmediate} - see SQL92
     * for definition</li>
     * <li>{@link DatabaseMetaData#importedKeyNotDeferrable} - see SQL92 for
     * definition</li>
     * </ul>
     */
    @ColumnLabel("DEFERRABILITY")
    short deferrability;


    /**
     * parent table.
     */
    @XmlTransient
    private Table table;


}

