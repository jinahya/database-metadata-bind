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


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "tableName", "tableType", "remarks", "typeCat", "typeSchem", "typeName",
        "selfReferencingColName", "refGeneration",
        "bestRowIdentifiers", "columns", "columnPrivileges", "exportedKeys",
        "importedKeys", "indexInfo", "primaryKeys", "tablePrivileges",
        "versionColumns"
    }
)
public class Table {


    /**
     * Suppression path value for {@link #bestRowIdentifiers}.
     */
    public static final String SUPPRESSION_PATH_BEST_ROW_IDENTIFIERS
        = "table/bestRowIdentifiers";


    /**
     * Suppression path value for {@link #columns}.
     */
    public static final String SUPPRESSION_PATH_COLUMNS = "table/columns";


    /**
     * Suppression path value for {@link #columnPrivileges}.
     */
    public static final String SUPPRESSION_PATH_COLUMN_PRIVILEGES
        = "table/columnPrivileges";


    /**
     * Suppression path value for {@link #exportedKeys}.
     */
    public static final String SUPPRESSION_PATH_EXPORTED_KEYS
        = "table/exportedKeys";


    /**
     * Suppression path value for {@link #exportedKeys}.
     */
    public static final String SUPPRESSION_PATH_IMPORTED_KEYS
        = "table/importedKeys";


    /**
     * Suppression path value for {@link #indexInfo}.
     */
    public static final String SUPPRESSION_PATH_INDEX_INFO
        = "table/indexInfo";


    /**
     * Suppression path value for {@link #primaryKeys}.
     */
    public static final String SUPPRESSION_PATH_PRIMARY_KEYS
        = "table/primaryKeys";


    /**
     * Suppression path value for {@link #tablePrivileges}.
     */
    public static final String SUPPRESSION_PATH_TABLE_PRIVILEGES
        = "table/tablePrivileges";


    /**
     * Suppression path value for {@link #versionColumns}.
     */
    public static final String SUPPRESSION_PATH_VERSION_COLUMNS
        = "table/versionColumns";


    // ---------------------------------------------------------------- tableCat
    public String getTableCat() {

        return tableCat;
    }


    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {

        return tableSchem;
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


    // -------------------------------------------------------- columnPrivileges
    public List<ColumnPrivilege> getColumnPrivileges() {

        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<ColumnPrivilege>();
        }

        return columnPrivileges;
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

        if (columnName == null) {
            throw new NullPointerException("columnName");
        }

        for (final Column column : getColumns()) {
            if (columnName.equals(column.getColumnName())) {
                return column;
            }
        }

        return null;
    }


    // ------------------------------------------------------------------ schema
    public Schema getSchema() {

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    // ------------------------------------------------------------ exportedKeys
    public List<ExportedKey> getExportedKeys() {

        if (exportedKeys == null) {
            exportedKeys = new ArrayList<ExportedKey>();
        }

        return exportedKeys;
    }


    // ------------------------------------------------------------ importedKeys
    public List<ImportedKey> getImportedKeys() {

        if (importedKeys == null) {
            importedKeys = new ArrayList<ImportedKey>();
        }

        return importedKeys;
    }


    // ----------------------------------------------------------------- indices
    public List<IndexInfo> getIndexInfo() {

        if (indexInfo == null) {
            indexInfo = new ArrayList<IndexInfo>();
        }

        return indexInfo;
    }


    public List<String> getIndexColumnNames(final String indexName) {

        if (indexName == null) {
            throw new NullPointerException("null indexName");
        }

        final List<String> indexColumnNames = new ArrayList<String>();

        for (final IndexInfo indexInfo_ : getIndexInfo()) {
            if (!indexName.equals(indexInfo_.getIndexName())) {
                continue;
            }
            indexColumnNames.add(indexInfo_.getColumnName());
        }

        return indexColumnNames;
    }


    public List<String> getIndexNames(final Boolean nonUnique,
                                      final Short... types) {

        final List<String> indexNames = new ArrayList<String>();

        for (final IndexInfo indexInfo_ : getIndexInfo()) {
            if (nonUnique != null
                && indexInfo_.getNonUnique() != nonUnique.booleanValue()) {
                continue;
            }
            if (types != null) {
                boolean typeMatches = false;
                for (final Short type : types) {
                    if (type == null) {
                        throw new NullPointerException("null type");
                    }
                    if (type.shortValue() == indexInfo_.getType()) {
                        typeMatches = true;
                        break;
                    }
                }
                if (!typeMatches) {
                    continue;
                }
            }
            final String indexName = indexInfo_.getIndexName();
            if (indexNames.contains(indexName)) {
                continue;
            }
            indexNames.add(indexName);
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
    @XmlAttribute
    private String tableCat;


    @ColumnLabel("TABLE_SCHEM")
    @XmlAttribute
    private String tableSchem;


    @ColumnLabel("TABLE_NAME")
    @XmlElement(required = true)
    private String tableName;


    @ColumnLabel("TABLE_TYPE")
    @XmlElement(required = true)
    private String tableType;


    @ColumnLabel("REMARKS")
    @XmlElement(required = true)
    private String remarks;


    @ColumnLabel("TYPE_CAT")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String typeCat;


    @ColumnLabel("TYPE_SCHEM")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String typeSchem;


    @ColumnLabel("TYPE_NAME")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String typeName;


    @ColumnLabel("SELF_REFERENCING_COL_NAME")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String selfReferencingColName;


    @ColumnLabel("REF_GENERATION")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String refGeneration;


    @XmlTransient
    private Schema schema;


    @XmlElementRef
    private List<BestRowIdentifier> bestRowIdentifiers;


    @XmlElementRef
    private List<Column> columns;


    @XmlElementRef
    private List<ColumnPrivilege> columnPrivileges;


    @XmlElementRef
    private List<ExportedKey> exportedKeys;


    @XmlElementRef
    private List<ImportedKey> importedKeys;


    @XmlElementRef
    private List<IndexInfo> indexInfo;


    @XmlElementRef
    private List<PrimaryKey> primaryKeys;


    @XmlElementRef
    private List<TablePrivilege> tablePrivileges;


    @XmlElementRef
    private List<VersionColumn> versionColumns;


}

