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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for binding results of
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[], Collection)
 */
@XmlRootElement
@ChildOf(Schema.class)
@ParentOf(BestRowIdentifier.class)
@ParentOf(Column.class)
@ParentOf(ColumnPrivilege.class)
@ParentOf(ExportedKey.class)
@ParentOf(ImportedKey.class)
@ParentOf(IndexInfo.class)
@ParentOf(PrimaryKey.class)
@ParentOf(PseudoColumn.class)
@ParentOf(SuperTable.class)
@ParentOf(TablePrivilege.class)
@ParentOf(VersionColumn.class)
public class Table
        implements MetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    // -------------------------------------------------------------------------------------------- TABLE_CAT / tableCat
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    // ---------------------------------------------------------------------------------------- TABLE_SCHEM / tableSchem
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    // ------------------------------------------------------------------------------------------ TABLE_NAME / tableName
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String ATTRIBUTE_NAME_TABLE_NAME = "tableName";

    /**
     * Creates a new instance.
     */
    public Table() {
        super();
    }

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
               + ",bestRowIdentifiers=" + bestRowIdentifiers
               + ",columns=" + columns
               + ",columnPrivileges=" + columnPrivileges
               + ",indexInfo=" + indexInfo
               + ",primaryKeys=" + primaryKeys
               + ",pseudoColumns=" + pseudoColumns
               + ",superTables=" + superTables
               + ",tablePrivileges=" + tablePrivileges
               + ",versionColumns=" + versionColumns
               + '}';
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(final String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(final String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    public String getRefGeneration() {
        return refGeneration;
    }

    public void setRefGeneration(final String refGeneration) {
        this.refGeneration = refGeneration;
    }

    // ---------------------------------------------------------------------------------------------- bestRowIdentifiers
    public List<BestRowIdentifier> getBestRowIdentifiers() {
        if (bestRowIdentifiers == null) {
            bestRowIdentifiers = new ArrayList<>();
        }
        return bestRowIdentifiers;
    }

    // --------------------------------------------------------------------------------------------------------- columns
    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    // ------------------------------------------------------------------------------------------------ columnPrivileges
    public List<ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<>();
        }
        return columnPrivileges;
    }

    // ---------------------------------------------------------------------------------------------------- exportedKeys
    public List<ExportedKey> getExportedKeys() {
        if (exportedKeys == null) {
            exportedKeys = new ArrayList<>();
        }
        return exportedKeys;
    }

    // ---------------------------------------------------------------------------------------------------- importedKeys
    public List<ImportedKey> getImportedKeys() {
        if (importedKeys == null) {
            importedKeys = new ArrayList<>();
        }
        return importedKeys;
    }

    // ------------------------------------------------------------------------------------------------------- indexInfo
    public List<IndexInfo> getIndexInfo() {
        if (indexInfo == null) {
            indexInfo = new ArrayList<>();
        }
        return indexInfo;
    }

    // ----------------------------------------------------------------------------------------------------- primaryKeys
    public List<PrimaryKey> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<>();
        }
        return primaryKeys;
    }

    // --------------------------------------------------------------------------------------------------- pseudoColumns
    public List<PseudoColumn> getPseudoColumns() {
        if (pseudoColumns == null) {
            pseudoColumns = new ArrayList<>();
        }
        return pseudoColumns;
    }

    // ----------------------------------------------------------------------------------------------------- superTables
    public List<SuperTable> getSuperTables() {
        if (superTables == null) {
            superTables = new ArrayList<>();
        }
        return superTables;
    }

    // ------------------------------------------------------------------------------------------------- tablePrivileges
    public List<TablePrivilege> getTablePrivileges() {
        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<>();
        }
        return tablePrivileges;
    }

    // -------------------------------------------------------------------------------------------------- versionColumns
    public List<VersionColumn> getVersionColumns() {
        if (versionColumns == null) {
            versionColumns = new ArrayList<>();
        }
        return versionColumns;
    }

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @XmlElement(required = true)
    @Label(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @XmlElement(required = true)
    @Label("TABLE_TYPE")
    private String tableType;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("REF_GENERATION")
    private String refGeneration;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<BestRowIdentifier> bestRowIdentifiers;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Column> columns;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ColumnPrivilege> columnPrivileges;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull ExportedKey> exportedKeys;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull ImportedKey> importedKeys;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull IndexInfo> indexInfo;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull PrimaryKey> primaryKeys;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull PseudoColumn> pseudoColumns;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull SuperTable> superTables;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull TablePrivilege> tablePrivileges;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull VersionColumn> versionColumns;
}
