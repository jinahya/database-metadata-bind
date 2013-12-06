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


import com.github.jinahya.sql.databasemetadata.ColumnLabel;
import com.github.jinahya.sql.databasemetadata.ColumnRetriever;
import com.github.jinahya.sql.databasemetadata.Suppression;
import com.github.jinahya.sql.databasemetadata.SuppressionPath;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
        "tableName", "tableType", "remarks", "selfReferencingColName",
        "refGeneration",
        "bestRowIdentifiers", "columns", "columnPrivileges", "indices",
        "primaryKeys", "tablePrivileges", "versionColumns"
    }
)
public class Table implements Comparable<Table> {


    public static final String SUPPRESSION_PATH_BEST_ROW_IDENTIFIERS
        = "table/bestRowIdentifiers";


    public static final String SUPPRESSION_PATH_COLUMNS
        = "table/columns";


    public static final String SUPPRESSION_PATH_COLUMN_PRIVILEGES
        = "table/columnPrivileges";


    public static final String SUPPRESSION_PATH_INDICES
        = "table/indices";


    public static final String SUPPRESSION_PATH_PRIMARY_KEYS
        = "table/primaryKeys";


    public static final String SUPPRESSION_PATH_TABLE_PRIVILEGES
        = "table/tablePrivileges";


    public static final String SUPPRESSION_PATH_VERSION_COLUMNS
        = "table/versionColumns";


    /**
     *
     * @param suppression
     * @param resultSet
     *
     * @return
     *
     * @throws SQLException
     */
    public static Table retrieve(final Suppression suppression,
                                 final ResultSet resultSet)
        throws SQLException {

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(resultSet, "null resultSet");

        final Table instance = new Table();

        ColumnRetriever.retrieve(Table.class, instance, suppression, resultSet);

        return instance;
    }


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param types
     * @param tables
     *
     * @throws SQLException
     *
     * @see DatabaseMetaData#getTables(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String[])
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final String catalog,
                                final String schemaPattern,
                                final String tableNamePattern,
                                final String[] types,
                                final Collection<? super Table> tables)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(tables, "null tables");

        if (suppression.isSuppressed(Schema.SUPPRESSION_PATH_TABLES)) {
            return;
        }

        final ResultSet resultSet = database.getTables(
            catalog, schemaPattern, tableNamePattern, types);
        try {
            while (resultSet.next()) {
                tables.add(retrieve(suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Schema schema)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(schema, "null schema");

        retrieve(database, suppression,
                 schema.getCatalog().getTableCat(), schema.getTableSchem(),
                 null, null,
                 schema.getTables());

        for (final Table table : schema.getTables()) {
            table.setSchema(schema);
        }

        for (final Table table : schema.getTables()) {
            BestRowIdentifier.retrieve(database, suppression, table);
            Column.retrieve(database, suppression, table);
            Index.retrieve(database, suppression, table);
            PrimaryKey.retrieve(database, suppression, table);
            TablePrivilege.retrieve(database, suppression, table);
            VersionColumn.retrieve(database, suppression, table);
        }
    }


    /**
     * Creates a new instance.
     */
    public Table() {

        super();
    }


    @Override
    public int compareTo(final Table o) {

        Objects.requireNonNull(o, "null object");

        final int tableTypeCompared = tableType.compareTo(o.tableType);
        if (tableTypeCompared != 0) {
            return tableTypeCompared;
        }

        final int schemaCompared = schema.compareTo(o.schema);
        if (schemaCompared != 0) {
            return schemaCompared;
        }

        return tableName.compareTo(o.tableName);
    }


    // ------------------------------------------------------------------ schema
    public Schema getSchema() {

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    // --------------------------------------------------------------- tableName
    public String getTableName() {

        return tableName;
    }


    public void setTableName(final String tableName) {

        this.tableName = tableName;
    }


    // --------------------------------------------------------------- tableType
    public String getTableType() {

        return tableType;
    }


    public void setTableType(final String tableType) {

        this.tableType = tableType;
    }


    // ----------------------------------------------------------------- REMARKS
    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(final String remarks) {

        this.remarks = remarks;
    }


    // -------------------------------------------------- selfReferencingColName
    public String getSelfReferencingColName() {

        return selfReferencingColName;
    }


    public void setSelfReferencingColName(final String selfReferencingColName) {

        this.selfReferencingColName = selfReferencingColName;
    }


    // ----------------------------------------------------------- refGeneration
    public String getRefGeneration() {

        return refGeneration;
    }


    public void setRefGeneration(final String refGeneration) {

        this.refGeneration = refGeneration;
    }


    // ------------------------------------------------------ bestRowIdentifiers
    public List<BestRowIdentifier> getBestRowIdentifiers() {

        if (bestRowIdentifiers == null) {
            bestRowIdentifiers = new ArrayList<BestRowIdentifier>();
        }

        return bestRowIdentifiers;
    }


    // ----------------------------------------------------------------- columns
    public List<Column> getColumns() {

        if (columns == null) {
            columns = new ArrayList<Column>();
        }

        return columns;
    }


    /**
     * Returns a list of column names ordered by {@code ordinalPosition}.
     *
     * @return a list of column names.
     */
    public List<String> getColumnNames() {

        final List<String> columnNames
            = new ArrayList<String>(getColumns().size());

        for (final Column column : getColumns()) {
            columnNames.add(column.getColumnName());
        }

        return columnNames;
    }


    public Column getColumnByName(final String columnName) {

        Objects.requireNonNull(columnName, "null columnName");

        for (final Column column : getColumns()) {
            if (columnName.equals(column.getColumnName())) {
                return column;
            }
        }

        return null;
    }


    // -------------------------------------------------------- columnPrivileges
    public List<ColumnPrivilege> getColumnPrivileges() {

        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<ColumnPrivilege>();
        }

        return columnPrivileges;
    }


    // ----------------------------------------------------------------- indices
    public List<Index> getIndices() {

        if (indices == null) {
            indices = new ArrayList<Index>();
        }

        return indices;
    }


    public List<String> getIndexNames() {

        final List<String> indexNames
            = new ArrayList<String>(getIndices().size());

        for (final Index indexInfo : getIndices()) {
            final String indexName = indexInfo.getIndexName();
            if (!indexNames.contains(indexName)) {
                indexNames.add(indexName);
            }
        }

        return indexNames;
    }


    // ------------------------------------------------------------- primaryKey
    public List<PrimaryKey> getPrimaryKeys() {

        if (primaryKeys == null) {
            primaryKeys = new ArrayList<PrimaryKey>();
        }

        return primaryKeys;
    }


    // --------------------------------------------------------- tablePrivileges
    public List<TablePrivilege> getTablePrivileges() {

        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<TablePrivilege>();
        }

        return tablePrivileges;
    }


    // ---------------------------------------------------------- versionColumns
    public List<VersionColumn> getVersionColumns() {

        if (versionColumns == null) {
            versionColumns = new ArrayList<VersionColumn>();
        }

        return versionColumns;
    }


    @ColumnLabel("TABLE_CAT")
    @SuppressionPath("table/tableCat")
    @XmlAttribute
    private String tableCat;


    @ColumnLabel("TABLE_SCHEM")
    @SuppressionPath("table/tableSchem")
    @XmlAttribute
    private String tableSchem;


    @XmlTransient
    private Schema schema;


    @ColumnLabel("TABLE_NAME")
    @XmlElement(required = true)
    private String tableName;


    @ColumnLabel("TABLE_TYPE")
    @XmlElement(required = true)
    private String tableType;


    @ColumnLabel("REMARKS")
    @XmlElement(nillable = true, required = true)
    private String remarks;


    @ColumnLabel("SELF_REFERENCING_COL_NAME")
    @XmlElement(nillable = true, required = true)
    private String selfReferencingColName;


    @ColumnLabel("REF_GENERATION")
    @XmlElement(nillable = true, required = true)
    private String refGeneration;


    @XmlElement(name = "bestRowIdentifier")
    private List<BestRowIdentifier> bestRowIdentifiers;


    @XmlElement(name = "column")
    private List<Column> columns;


    @XmlElement(name = "columnPrivilege")
    private List<ColumnPrivilege> columnPrivileges;


    @XmlElement(name = "index")
    private List<Index> indices;


    @XmlElement(name = "primaryKey")
    private List<PrimaryKey> primaryKeys;


    @XmlElement(name = "tablePrivilege")
    private List<TablePrivilege> tablePrivileges;


    @XmlElement(name = "versionColumn")
    private List<VersionColumn> versionColumns;


}

