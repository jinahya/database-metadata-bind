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
import javax.xml.bind.annotation.XmlType;

/**
 * A entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "tableName", "tableType", "remarks", "typeCat", "typeSchem", "typeName",
    "selfReferencingColName", "refGeneration",
    // ---------------------------------------------------------------------
    "bestRowIdentifiers", "columns", "exportedKeys", "importedKeys",
    "indexInfo", "primaryKeys", "pseudoColumns", "superTables",
    "tablePrivileges", "versionColumns"
})
public class Table extends AbstractChild<Schema> {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + ",tableSchem=" + tableSchem
               + ",tableName=" + tableName
               + ",tableType=" + tableType
               + ",remarks=" + remarks
               + ",typeCat=" + typeCat
               + ",typeSchem=" + typeSchem
               + ",typeName=" + typeName
               + ",selfReferencingColName=" + selfReferencingColName
               + ",refGeneration=" + refGeneration
               + "}";
    }

    // ---------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
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

    // ----------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ----------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // --------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // ---------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
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

    // ------------------------------------------------------------------ schema
    // just for class diagram
    @Deprecated
    private Schema getSchema() {
        return getParent();
    }

//    public void setSchema(final Schema schema) {
//
//        setParent(schema);
//    }
    // ------------------------------------------------------ bestRowIdentifiers
    public List<BestRowIdentifier> getBestRowIdentifiers() {
        if (bestRowIdentifiers == null) {
            bestRowIdentifiers = new ArrayList<BestRowIdentifier>();
        }
        return bestRowIdentifiers;
    }

    public void setBestRowIdentifiers(List<BestRowIdentifier> bestRowIdentifiers) {
        this.bestRowIdentifiers = bestRowIdentifiers;
    }

    // ----------------------------------------------------------------- columns
    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<Column>();
        }
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    // ------------------------------------------------------------ exportedKeys
    public List<ExportedKey> getExportedKeys() {
        if (exportedKeys == null) {
            exportedKeys = new ArrayList<ExportedKey>();
        }
        return exportedKeys;
    }

    public void setExportedKeys(List<ExportedKey> exportedKeys) {
        this.exportedKeys = exportedKeys;
    }

    // ------------------------------------------------------------ importedKeys
    public List<ImportedKey> getImportedKeys() {
        if (importedKeys == null) {
            importedKeys = new ArrayList<ImportedKey>();
        }
        return importedKeys;
    }

    public void setImportedKeys(List<ImportedKey> importedKeys) {
        this.importedKeys = importedKeys;
    }

    // --------------------------------------------------------------- indexInfo
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

    public void setIndexInfo(List<IndexInfo> indexInfo) {
        this.indexInfo = indexInfo;
    }

    // ------------------------------------------------------------- primaryKeys
    public List<PrimaryKey> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<PrimaryKey>();
        }
        return primaryKeys;
    }

    public void setPrimaryKeys(List<PrimaryKey> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    // ----------------------------------------------------------- pseudoColumns
    public List<PseudoColumn> getPseudoColumns() {
        if (pseudoColumns == null) {
            pseudoColumns = new ArrayList<PseudoColumn>();
        }
        return pseudoColumns;
    }

    public void setPseudoColumns(List<PseudoColumn> pseudoColumns) {
        this.pseudoColumns = pseudoColumns;
    }

    // ------------------------------------------------------------- superTables
    public List<SuperTable> getSuperTables() {
        if (superTables == null) {
            superTables = new ArrayList<SuperTable>();
        }
        return superTables;
    }

    public void setSuperTables(List<SuperTable> superTables) {
        this.superTables = superTables;
    }

    // --------------------------------------------------------- tablePrivileges
    public List<TablePrivilege> getTablePrivileges() {
        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<TablePrivilege>();
        }
        return tablePrivileges;
    }

    public void setTablePrivileges(List<TablePrivilege> tablePrivileges) {
        this.tablePrivileges = tablePrivileges;
    }

    // ---------------------------------------------------------- versionColumns
    public List<VersionColumn> getVersionColumns() {
        if (versionColumns == null) {
            versionColumns = new ArrayList<VersionColumn>();
        }
        return versionColumns;
    }

    public void setVersionColumns(List<VersionColumn> versionColumns) {
        this.versionColumns = versionColumns;
    }

    // -------------------------------------------------------------------------
    @Label("TABLE_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String tableCat;

    @Label("TABLE_SCHEM")
    @NillableBySpecification
    @XmlAttribute
    private String tableSchem;

    @Label("TABLE_NAME")
    @XmlElement(required = true)
    private String tableName;

    @Label("TABLE_TYPE")
    @XmlElement(required = true)
    private String tableType;

    @Label("REMARKS")
    @XmlElement(required = true)
    private String remarks;

    @Label("TYPE_CAT")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String typeCat;

    @Label("TYPE_SCHEM")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String typeSchem;

    @Label("TYPE_NAME")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String typeName;

    @Label("SELF_REFERENCING_COL_NAME")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String selfReferencingColName;

    @Label("REF_GENERATION")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String refGeneration;

    @Invocation(
            name = "getBestRowIdentifier",
            types = {
                String.class, String.class, String.class, int.class, boolean.class
            },
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName",
                                 "0", // bestRowTemporaty
                                 "true"}),
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName",
                                 "1", // bestRowTransaction
                                 "true"}),
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName",
                                 "2", // bestRowSession
                                 "true"})}
    )
    @XmlElementRef
    private List<BestRowIdentifier> bestRowIdentifiers;

    @Invocation(
            name = "getColumns",
            types = {String.class, String.class, String.class, String.class},
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName", "null"})
            }
    )
    @XmlElementRef
    private List<Column> columns;

    @Invocation(
            name = "getExportedKeys",
            types = {String.class, String.class, String.class},
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    @XmlElementRef
    private List<ExportedKey> exportedKeys;

    @Invocation(
            name = "getImportedKeys",
            types = {String.class, String.class, String.class},
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    @XmlElementRef
    private List<ImportedKey> importedKeys;

    @Invocation(
            name = "getIndexInfo",
            types = {
                String.class, String.class, String.class, boolean.class,
                boolean.class
            },
            argsarr = {
                @InvocationArgs({
            ":tableCat", ":tableSchem", ":tableName", "false", "false"
        })
            }
    )
    @XmlElementRef
    private List<IndexInfo> indexInfo;

    @Invocation(
            name = "getPrimaryKeys",
            types = {String.class, String.class, String.class},
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    @XmlElementRef
    private List<PrimaryKey> primaryKeys;

    @Invocation(
            name = "getPseudoColumns",
            types = {String.class, String.class, String.class, String.class},
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName", "null"})
            }
    )
    @XmlElementRef
    private List<PseudoColumn> pseudoColumns;

    @Invocation(
            name = "getSuperTables",
            types = {String.class, String.class, String.class},
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    @XmlElementRef
    private List<SuperTable> superTables;

    @Invocation(
            name = "getTablePrivileges",
            types = {String.class, String.class, String.class},
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    @XmlElementRef
    private List<TablePrivilege> tablePrivileges;

    @Invocation(
            name = "getVersionColumns",
            types = {String.class, String.class, String.class},
            argsarr = {
                @InvocationArgs({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    @XmlElementRef
    private List<VersionColumn> versionColumns;
}
