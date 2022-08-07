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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Table
        implements MetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    public static final Comparator<Table> COMPARATOR =
            Comparator.comparing(Table::getTableType)
                    .thenComparing(Table::getTableCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Table::getTableSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Table::getTableName);

    // -------------------------------------------------------------------------------------------- TABLE_CAT / tableCat
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    // ---------------------------------------------------------------------------------------- TABLE_SCHEM / tableSchem
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    // ------------------------------------------------------------------------------------------ TABLE_NAME / tableName
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String ATTRIBUTE_NAME_TABLE_NAME = "tableName";

    public List<BestRowIdentifier> getBestRowIdentifiers() {
        if (bestRowIdentifiers == null) {
            bestRowIdentifiers = new ArrayList<>();
        }
        return bestRowIdentifiers;
    }

    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    public List<ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<>();
        }
        return columnPrivileges;
    }

    public List<ExportedKey> getExportedKeys() {
        if (exportedKeys == null) {
            exportedKeys = new ArrayList<>();
        }
        return exportedKeys;
    }

    public List<ImportedKey> getImportedKeys() {
        if (importedKeys == null) {
            importedKeys = new ArrayList<>();
        }
        return importedKeys;
    }

    public List<IndexInfo> getIndexInfo() {
        if (indexInfo == null) {
            indexInfo = new ArrayList<>();
        }
        return indexInfo;
    }

    public List<PrimaryKey> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<>();
        }
        return primaryKeys;
    }

    public List<PseudoColumn> getPseudoColumns() {
        if (pseudoColumns == null) {
            pseudoColumns = new ArrayList<>();
        }
        return pseudoColumns;
    }

    public List<TablePrivilege> getTablePrivileges() {
        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<>();
        }
        return tablePrivileges;
    }

    public List<VersionColumn> getVersionColumns() {
        if (versionColumns == null) {
            versionColumns = new ArrayList<>();
        }
        return versionColumns;
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("TABLE_TYPE")
    private String tableType;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("REMARKS")
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("REF_GENERATION")
    private String refGeneration;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull BestRowIdentifier> bestRowIdentifiers;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Column> columns;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull ColumnPrivilege> columnPrivileges;

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
    private List<@Valid @NotNull TablePrivilege> tablePrivileges;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull VersionColumn> versionColumns;
}
