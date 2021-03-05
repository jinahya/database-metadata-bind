package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A entity class for binding the result of {@link java.sql.DatabaseMetaData#getTables(java.lang.String,
 * java.lang.String, java.lang.String, java.lang.String[])}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class Table extends SchemaChild {

    // -----------------------------------------------------------------------------------------------------------------
    private static final long serialVersionUID = 6590036695540141125L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public Table() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
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
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Table that = (Table) obj;
        return Objects.equals(tableCat, that.tableCat)
               && Objects.equals(tableSchem, that.tableSchem)
               && Objects.equals(tableName, that.tableName)
               && Objects.equals(tableType, that.tableType)
               && Objects.equals(remarks, that.remarks)
               && Objects.equals(typeCat, that.typeCat)
               && Objects.equals(typeSchem, that.typeSchem)
               && Objects.equals(typeName, that.typeName)
               && Objects.equals(selfReferencingColName, that.selfReferencingColName)
               && Objects.equals(refGeneration, that.refGeneration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat,
                            tableSchem,
                            tableName,
                            tableType,
                            remarks,
                            typeCat,
                            typeSchem,
                            typeName,
                            selfReferencingColName,
                            refGeneration);
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------- tableType
    public String getTableType() {
        return tableType;
    }

    public void setTableType(final String tableType) {
        this.tableType = tableType;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------ selfReferencingColName
    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(final String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    // --------------------------------------------------------------------------------------------------- refGeneration
    public String getRefGeneration() {
        return refGeneration;
    }

    public void setRefGeneration(final String refGeneration) {
        this.refGeneration = refGeneration;
    }

    // ---------------------------------------------------------------------------------------------- bestRowIdentifiers
    List<BestRowIdentifier> getBestRowIdentifiers() {
        if (bestRowIdentifiers == null) {
            bestRowIdentifiers = new ArrayList<>();
        }
        return bestRowIdentifiers;
    }

    // --------------------------------------------------------------------------------------------------------- columns

    /**
     * Returns columns of this table.
     *
     * @return a list of columns of this table
     */
    List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    // ---------------------------------------------------------------------------------------------------- exportedKeys

    /**
     * Returns exported keys of this table.
     *
     * @return a list of exported keys.
     */
    List<ExportedKey> getExportedKeys() {
        if (exportedKeys == null) {
            exportedKeys = new ArrayList<>();
        }
        return exportedKeys;
    }

    // ---------------------------------------------------------------------------------------------------- importedKeys

    /**
     * Returns imported keys of this table.
     *
     * @return a list of imported keys of this table.
     */
    List<ImportedKey> getImportedKeys() {
        if (importedKeys == null) {
            importedKeys = new ArrayList<>();
        }
        return importedKeys;
    }

    // ------------------------------------------------------------------------------------------------------- indexInfo

    /**
     * Returns index info of this table.
     *
     * @return a list of index info of this table.
     */
    List<IndexInfo> getIndexInfo() {
        if (indexInfo == null) {
            indexInfo = new ArrayList<>();
        }
        return indexInfo;
    }

    // ----------------------------------------------------------------------------------------------------- primaryKeys

    /**
     * Returns primary keys of this table.
     *
     * @return a list of primary keys of this table.
     */
    List<PrimaryKey> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<>();
        }
        return primaryKeys;
    }

    // --------------------------------------------------------------------------------------------------- pseudoColumns

    /**
     * Returns pseudo columns of this table.
     *
     * @return a list of pseudo columns of this table.
     */
    List<PseudoColumn> getPseudoColumns() {
        if (pseudoColumns == null) {
            pseudoColumns = new ArrayList<>();
        }
        return pseudoColumns;
    }

    // ----------------------------------------------------------------------------------------------------- superTables

    /**
     * Returns super tables of this table.
     *
     * @return a list of super tables of this table.
     */
    List<SuperTable> getSuperTables() {
        if (superTables == null) {
            superTables = new ArrayList<>();
        }
        return superTables;
    }

    // ------------------------------------------------------------------------------------------------- tablePrivileges

    /**
     * Returns table privileges of this table.
     *
     * @return a list of table privileges of this table.
     */
    List<TablePrivilege> getTablePrivileges() {
        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<>();
        }
        return tablePrivileges;
    }

    // -------------------------------------------------------------------------------------------------- versionColumns

    /**
     * Returns version columns of this table.
     *
     * @return a list of version columns of this table.
     */
    List<VersionColumn> getVersionColumns() {
        if (versionColumns == null) {
            versionColumns = new ArrayList<>();
        }
        return versionColumns;
    }

    // -------------------------------------------------------------------------------------------------- crossReference
    List<CrossReference> getCrossReference() {
        if (crossReference == null) {
            return crossReference = new ArrayList<>();
        }
        return crossReference;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("TABLE_TYPE")
    private String tableType;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("REF_GENERATION")
    private String refGeneration;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull BestRowIdentifier> bestRowIdentifiers;

    @XmlElementRef
    private List<@Valid @NotNull Column> columns;

    @XmlElementRef
    private List<@Valid @NotNull ExportedKey> exportedKeys;

    @XmlElementRef
    private List<@Valid @NotNull ImportedKey> importedKeys;

    @XmlElementRef
    private List<@Valid @NotNull IndexInfo> indexInfo;

    @XmlElementRef
    private List<@Valid @NotNull PrimaryKey> primaryKeys;

    @XmlElementRef
    private List<@Valid @NotNull PseudoColumn> pseudoColumns;

    @XmlElementRef
    private List<@Valid @NotNull SuperTable> superTables;

    @XmlElementRef
    private List<@Valid @NotNull TablePrivilege> tablePrivileges;

    @XmlElementRef
    private List<@Valid @NotNull VersionColumn> versionColumns;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull CrossReference> crossReference;
}
