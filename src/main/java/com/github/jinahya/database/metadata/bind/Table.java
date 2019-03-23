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
package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * A entity class for binding the result of {@link java.sql.DatabaseMetaData#getTables(java.lang.String,
 * java.lang.String, java.lang.String, java.lang.String[])}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
        "tableName", "tableType", "remarks", "typeCat", "typeSchem", "typeName", "selfReferencingColName",
        "refGeneration",
        // ---------------------------------------------------------------------
        "bestRowIdentifiers", "columns", "exportedKeys", "importedKeys", "indexInfo", "primaryKeys", "pseudoColumns",
        "superTables", "tablePrivileges", "versionColumns"
})
public class Table implements Serializable {

    private static final long serialVersionUID = 6590036695540141125L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(Table.class.getName());

    // -------------------------------------------------------------------------
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

    // ------------------------------------------------------ bestRowIdentifiers
    public List<BestRowIdentifier> getBestRowIdentifiers() {
        if (bestRowIdentifiers == null) {
            bestRowIdentifiers = new ArrayList<BestRowIdentifier>();
        }
        return bestRowIdentifiers;
    }

    // ----------------------------------------------------------------- columns

    /**
     * Returns columns of this table.
     *
     * @return a list of columns of this table
     */
    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<Column>();
        }
        return columns;
    }

    // ------------------------------------------------------------ exportedKeys

    /**
     * Returns exported keys of this table.
     *
     * @return a list of exported keys.
     */
    public List<ExportedKey> getExportedKeys() {
        if (exportedKeys == null) {
            exportedKeys = new ArrayList<ExportedKey>();
        }
        return exportedKeys;
    }

    // ------------------------------------------------------------ importedKeys

    /**
     * Returns imported keys of this table.
     *
     * @return a list of imported keys of this table.
     */
    public List<ImportedKey> getImportedKeys() {
        if (importedKeys == null) {
            importedKeys = new ArrayList<ImportedKey>();
        }
        return importedKeys;
    }

    // --------------------------------------------------------------- indexInfo

    /**
     * Returns index info of this table.
     *
     * @return a list of index info of this table.
     */
    public List<IndexInfo> getIndexInfo() {
        if (indexInfo == null) {
            indexInfo = new ArrayList<IndexInfo>();
        }
        return indexInfo;
    }

    // ------------------------------------------------------------- primaryKeys

    /**
     * Returns primary keys of this table.
     *
     * @return a list of primary keys of this table.
     */
    public List<PrimaryKey> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<PrimaryKey>();
        }
        return primaryKeys;
    }

    // ----------------------------------------------------------- pseudoColumns

    /**
     * Returns pseudo columns of this table.
     *
     * @return a list of pseudo columns of this table.
     */
    public List<PseudoColumn> getPseudoColumns() {
        if (pseudoColumns == null) {
            pseudoColumns = new ArrayList<PseudoColumn>();
        }
        return pseudoColumns;
    }

    // ------------------------------------------------------------- superTables

    /**
     * Returns super tables of this table.
     *
     * @return a list of super tables of this table.
     */
    public List<SuperTable> getSuperTables() {
        if (superTables == null) {
            superTables = new ArrayList<SuperTable>();
        }
        return superTables;
    }

    // --------------------------------------------------------- tablePrivileges

    /**
     * Returns table privileges of this table.
     *
     * @return a list of table privileges of this table.
     */
    public List<TablePrivilege> getTablePrivileges() {
        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<TablePrivilege>();
        }
        return tablePrivileges;
    }

    // ---------------------------------------------------------- versionColumns

    /**
     * Returns version columns of this table.
     *
     * @return a list of version columns of this table.
     */
    public List<VersionColumn> getVersionColumns() {
        if (versionColumns == null) {
            versionColumns = new ArrayList<VersionColumn>();
        }
        return versionColumns;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Bind(label = "TABLE_CAT", nillable = true)
    private String tableCat;

    @XmlAttribute
    @Bind(label = "TABLE_SCHEM", nillable = true)
    private String tableSchem;

    // -------------------------------------------------------------------------
    @XmlElement
    @Bind(label = "TABLE_NAME")
    private String tableName;

    @XmlElement
    @Bind(label = "TABLE_TYPE")
    private String tableType;

    @XmlElement(nillable = true)
    @Bind(label = "REMARKS", nillable = true)
    private String remarks;

    @XmlElement(nillable = true)
    @Bind(label = "TYPE_CAT", nillable = true)
    private String typeCat;

    @XmlElement(nillable = true)
    @Bind(label = "TYPE_SCHEM", nillable = true)
    private String typeSchem;

    @XmlElement(nillable = true)
    @Bind(label = "TYPE_NAME", nillable = true)
    private String typeName;

    @XmlElement(nillable = true)
    @Bind(label = "SELF_REFERENCING_COL_NAME", nillable = true)
    private String selfReferencingColName;

    @XmlElement(nillable = true)
    @Bind(label = "REF_GENERATION", nillable = true)
    private String refGeneration;

    // -------------------------------------------------------------------------
    @XmlElementRef
    @Invoke(name = "getBestRowIdentifier",
            types = {
                    String.class, String.class, String.class, int.class,
                    boolean.class
            },
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName",
                               "0", // bestRowTemporaty
                               "true"})
                    ,
                    @Literals({":tableCat", ":tableSchem", ":tableName",
                               "1", // bestRowTransaction
                               "true"})
                    ,
                    @Literals({":tableCat", ":tableSchem", ":tableName",
                               "2", // bestRowSession
                               "true"})}
    )
    private List<BestRowIdentifier> bestRowIdentifiers;

    @XmlElementRef
    @Invoke(name = "getColumns",
            types = {String.class, String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName", "null"})
            }
    )
    private List<Column> columns;

    @XmlElementRef
    @Invoke(name = "getExportedKeys",
            types = {String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    private List<ExportedKey> exportedKeys;

    @XmlElementRef
    @Invoke(name = "getImportedKeys",
            types = {String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    private List<ImportedKey> importedKeys;

    @XmlElementRef
    @Invoke(name = "getIndexInfo",
            types = {
                    String.class, String.class, String.class, boolean.class,
                    boolean.class
            },
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName", "false",
                               "false"})
            }
    )
    private List<IndexInfo> indexInfo;

    @XmlElementRef
    @Invoke(name = "getPrimaryKeys",
            types = {String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    private List<PrimaryKey> primaryKeys;

    @XmlElementRef
    @Invoke(name = "getPseudoColumns",
            types = {String.class, String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName", "null"})
            }
    )
    private List<PseudoColumn> pseudoColumns;

    @XmlElementRef
    @Invoke(name = "getSuperTables",
            types = {String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    private List<SuperTable> superTables;

    @XmlElementRef
    @Invoke(name = "getTablePrivileges",
            types = {String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    private List<TablePrivilege> tablePrivileges;

    @XmlElementRef
    @Invoke(name = "getVersionColumns",
            types = {String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCat", ":tableSchem", ":tableName"})
            }
    )
    private List<VersionColumn> versionColumns;
}
