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
import javax.xml.bind.annotation.XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "columnName", "dataType", "typeName", "columnSize", "decimalDigits",
        "numPrecRadix", "nullable", "remarks", "columnDef", "charOctetLength",
        "ordinalPosition", "isNullable", "scopeCatalog", "scopeSchema",
        "scopeTable", "sourceDataType", "isAutoincrement", "isGeneratedcolumn"
    }
)
public class Column {


    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Column.class);


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param columnNamePattern
     * @param columns
     *
     * @throws SQLException if a database access error occurs.
     *
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final String catalog,
                                final String schemaPattern,
                                final String tableNamePattern,
                                final String columnNamePattern,
                                final Collection<? super Column> columns)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (columns == null) {
            throw new NullPointerException("null columns");
        }

        if (suppression.isSuppressed(Table.SUPPRESSION_PATH_COLUMNS)) {
            return;
        }

        final ResultSet resultSet = database.getColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            while (resultSet.next()) {
                columns.add(ColumnRetriever.retrieve(
                    Column.class, suppression, resultSet));
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
                 null,
                 table.getColumns());

        for (final Column column : table.getColumns()) {
            column.table = table;
        }
    }


    /**
     * Creates a new instance.
     */
    public Column() {

        super();
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


    // ---------------------------------------------------------------- NULLABLE
    public int getNullable() {

        return nullable;
    }


    public void setNullable(final int nullable) {

        this.nullable = nullable;
    }


    // ----------------------------------------------------------------- REMARKS
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


    /**
     * table catalog (may be {@code null}).
     */
    @ColumnLabel("TABLE_CAT")
    @SuppressionPath("column/tableCat")
    @XmlAttribute
    private String tableCat;


    /**
     * table schema (may be {@code null}).
     */
    @ColumnLabel("TABLE_SCHEM")
    @SuppressionPath("column/tableSchem")
    @XmlAttribute
    private String tableSchem;


    /**
     * table name.
     */
    @ColumnLabel("TABLE_NAME")
    @SuppressionPath("column/tableName")
    @XmlAttribute
    private String tableName;


    /**
     * column name.
     */
    @ColumnLabel("COLUMN_NAME")
    @SuppressionPath("column/columnName")
    @XmlElement(nillable = false, required = true)
    String columnName;


    /**
     * SQL type from {@code java.sql.Types}.
     */
    @ColumnLabel("DATA_TYPE")
    @SuppressionPath("column/dataType")
    @XmlElement(nillable = true, required = true)
    Integer dataType;


    /**
     * Data source dependent type name, for a UDT the type name is fully
     * qualified.
     */
    @ColumnLabel("TYPE_NAME")
    @SuppressionPath("column/typeName")
    @XmlElement(nillable = true, required = true)
    String typeName;


    /**
     * column size.
     */
    @ColumnLabel("COLUMN_SIZE")
    @SuppressionPath("column/columnSize")
    @XmlElement(nillable = true, required = true)
    Integer columnSize;


    /**
     * is not used.
     */
    @ColumnLabel("BUFFER_LENGTH")
    @SuppressionPath("column/bufferLength")
    @NotUsed
    Object bufferLength;


    /**
     * the number of fractional digits. Null is returned for data types where
     * DECIMAL_DIGITS is not applicable.
     */
    @ColumnLabel("DECIMAL_DIGITS")
    @SuppressionPath("column/decimalDigits")
    @XmlElement(nillable = true, required = true)
    Integer decimalDigits;


    /**
     * Radix (typically either 10 or 2).
     */
    @ColumnLabel("NUM_PREC_RADIX")
    @SuppressionPath("column/numPrecRadix")
    @XmlElement(nillable = true, required = true)
    Integer numPrecRadix;


    /**
     * is NULL allowed.
     * <ul>
     * <li>{@link DatabaseMetaData#columnNoNulls} - might not allow NULL
     * values</li>
     * <li>{@link DatabaseMetaData#columnNullable} - definitely allows NULL
     * values</li>
     * <li>{@link DatabaseMetaData#columnNullableUnknown} - nullability
     * unknown</li>
     * </ul>
     */
    @ColumnLabel("NULLABLE")
    @SuppressionPath("column/nullable")
    @XmlElement(nillable = true, required = true)
    Integer nullable;


    /**
     * comment describing column (may be {@code null})
     */
    @ColumnLabel("REMARKS")
    @SuppressionPath("column/remarks")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    String remarks;


    /**
     * default value for the column, which should be interpreted as a string
     * when the value is enclosed in single quotes (may be {@code null})
     */
    @ColumnLabel("COLUMN_DEF")
    @SuppressionPath("column/columnDef")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    String columnDef;


    /**
     * unused.
     */
    @ColumnLabel("SQL_DATA_TYPE")
    @NotUsed
    @SuppressionPath("column/sqlDataType")
    @XmlTransient
    Integer sqlDataType;


    /**
     * unused.
     */
    @ColumnLabel("SQL_DATETIME_SUB")
    @NotUsed
    @SuppressionPath("column/sqlDatetimeSub")
    @XmlTransient
    Integer sqlDatetimeSub;


    /**
     * for char types the maximum number of bytes in the column.
     */
    @ColumnLabel("CHAR_OCTET_LENGTH")
    @SuppressionPath("column/charOctetLength")
    @XmlElement(nillable = true, required = true)
    Integer charOctetLength;


    /**
     * index of column in table (starting at 1).
     */
    @ColumnLabel("ORDINAL_POSITION")
    @SuppressionPath("column/ordinalPosition")
    @XmlElement(nillable = true, required = true)
    Integer ordinalPosition;


    /**
     * ISO rules are used to determine the nullability for a column.
     * <ul>
     * <li>YES --- if the column can include NULLs</li>
     * <li>NO --- if the column cannot include NULLs</li>
     * <li>empty string --- if the nullability for the column is unknown</li>
     * </ul>
     */
    @ColumnLabel("IS_NULLABLE")
    @SuppressionPath("column/isNullable")
    @XmlElement(nillable = true, required = true)
    String isNullable;


    /**
     * catalog of table that is the scope of a reference attribute (null if
     * DATA_TYPE isn't REF).
     */
    @ColumnLabel("SCOPE_CATALOG")
    @SuppressionPath("column/scopeCatalog")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    String scopeCatalog;


    /**
     * schema of table that is the scope of a reference attribute (null if the
     * DATA_TYPE isn't REF).
     */
    @ColumnLabel("SCOPE_SCHEMA")
    @SuppressionPath("column/scopeSchema")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    String scopeSchema;


    /**
     * table name that this the scope of a reference attribute (null if the
     * DATA_TYPE isn't REF).
     */
    @ColumnLabel("SCOPE_TABLE")
    @SuppressionPath("column/scopeTable")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    String scopeTable;


    /**
     * source type of a distinct type or user-generated Ref type, SQL type from
     * {@link java.sql.Types} ({@code null} if {@link #dataType} isn't
     * {@link java.sql.Types#DISTINCT} or user-generated
     * {@link java.sql.Types#REF})
     */
    @ColumnLabel("SOURCE_DATA_TYPE")
    @SuppressionPath("column/sourceDataType")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    String sourceDataType;


    /**
     * Indicates whether this column is auto incremented.
     * <ul>
     * <li>YES --- if the column is auto incremented</li>
     * <li>NO --- if the column is not auto incremented</li>
     * <li>empty string --- if it cannot be determined whether the column is
     * auto incremented</li>
     * </ul>
     */
    @ColumnLabel("IS_AUTOINCREMENT")
    @SuppressionPath("column/isAutoincrement")
    @XmlElement(nillable = true, required = true)
    String isAutoincrement;


    /**
     * Indicates whether this is a generated column.
     * </ul>
     * <li>YES --- if this a generated column</li>
     * <li>NO --- if this not a generated column</li>
     * <li>empty string --- if it cannot be determined whether this is a
     * generated column</li>
     * </ul>
     */
    @ColumnLabel("IS_GENERATEDCOLUMN")
    @SuppressionPath("column/isGeneratedcolumn")
    @XmlElement(nillable = true, required = true)
    String isGeneratedcolumn;


    /**
     * parent table.
     */
    @XmlTransient
    private Table table;


}

