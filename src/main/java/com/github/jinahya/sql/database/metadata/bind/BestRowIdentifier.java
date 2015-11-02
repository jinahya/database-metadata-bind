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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
@XmlType(
    propOrder = {
        "columnName", "dataType", "typeName", "columnSize", "decimalDigits",
        "pseudoColumn"
    }
)
public class BestRowIdentifier {


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schema
     * @param table
     * @param scope
     * @param nullable
     * @param bestRowIdentifiers
     *
     * @throws SQLException if a database access error occurs.
     *
     * @see DatabaseMetaData#getBestRowIdentifier(java.lang.String,
     * java.lang.String, java.lang.String, int, boolean)
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final String catalog, final String schema, final String table,
        final int scope, final boolean nullable,
        final Collection<? super BestRowIdentifier> bestRowIdentifiers)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (bestRowIdentifiers == null) {
            throw new NullPointerException("null bestRowIdentifiers");
        }

        if (suppression.isSuppressed(
            Table.SUPPRESSION_PATH_BEST_ROW_IDENTIFIERS)) {
            return;
        }

        final ResultSet resultSet = database.getBestRowIdentifier(
            catalog, schema, table, scope, nullable);
        try {
            while (resultSet.next()) {
                bestRowIdentifiers.add(ColumnRetriever.retrieve(
                    BestRowIdentifier.class, suppression, resultSet));
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
                 table.getSchema().getTableSchem(), table.getTableName(),
                 DatabaseMetaData.bestRowTemporary, true,
                 table.getBestRowIdentifiers());

        retrieve(database, suppression,
                 table.getSchema().getCatalog().getTableCat(),
                 table.getSchema().getTableSchem(), table.getTableName(),
                 DatabaseMetaData.bestRowTransaction, true,
                 table.getBestRowIdentifiers());

        retrieve(database, suppression,
                 table.getSchema().getCatalog().getTableCat(),
                 table.getSchema().getTableSchem(), table.getTableName(),
                 DatabaseMetaData.bestRowSession, true,
                 table.getBestRowIdentifiers());

        for (final BestRowIdentifier bestRowIdentifier
             : table.getBestRowIdentifiers()) {
            bestRowIdentifier.setTable(table);
        }
    }


    /**
     * Creates a new instance.
     */
    public BestRowIdentifier() {

        super();
    }


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
    @SuppressionPath("bestRowIdentifier/scope")
    @XmlAttribute
    short scope;


    /**
     * column name.
     */
    @ColumnLabel("COLUMN_NAME")
    @SuppressionPath("bestRowIdentifier/columnName")
    @XmlElement(nillable = false, required = true)
    String columnName;


    /**
     * SQL data type from {@link java.sql.Types}.
     */
    @ColumnLabel("DATA_TYPE")
    @SuppressionPath("bestRowIdentifier/dataType")
    @XmlElement(nillable = false, required = true)
    int dataType;


    /**
     * Data source dependent type name, for a UDT the type name is fully
     * qualified.
     */
    @ColumnLabel("TYPE_NAME")
    @SuppressionPath("bestRowIdentifier/typeName")
    @XmlElement(nillable = false, required = true)
    String typeName;


    /**
     * precision.
     */
    @ColumnLabel("COLUMN_SIZE")
    @SuppressionPath("bestRowIdentifier/columnSize")
    @XmlElement(nillable = false, required = true)
    int columnSize;


    /**
     * unused.
     */
    @ColumnLabel("BUFFER_LENGTH")
    @SuppressionPath("bestRowIdentifier/bufferLength")
    //@XmlElement(nillable = false, required = true)
    @NotUsed
    int bufferLength;


    /**
     * scale - Null is returned for data types where DECIMAL_DIGITS is not
     * applicable.
     */
    @ColumnLabel("DECIMAL_DIGITS")
    @SuppressionPath("bestRowIdentifier/decimalDigits")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    Short decimalDigits;


    /**
     * is this a pseudo column like an Oracle ROWID.
     * <ul>
     * <li>{@link DatabaseMetaData#bestRowUnknown} - may or may not be pseudo
     * column</li>
     * <li>{@link DatabaseMetaData#bestRowNotPseudo} - is NOT a pseudo
     * column</li>
     * <li>{@link DatabaseMetaData#bestRowPseudo} - is a pseudo column</li>
     * </ul>
     */
    @ColumnLabel("PSEUDO_COLUMN")
    @SuppressionPath("bestRowIdentifier/pseudoColumn")
    @XmlElement(nillable = false, required = true)
    short pseudoColumn;


    /**
     * parent table.
     */
    @XmlTransient
    private Table table;


}

