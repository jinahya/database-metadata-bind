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

import com.github.jinahya.database.metadata.bind.BestRowIdentifier.BestRowIdentifierCategory;
import com.github.jinahya.database.metadata.bind.BestRowIdentifier.CategorizedBestRowIdentifiers;
import com.github.jinahya.database.metadata.bind.IndexInfo.CategorizedIndexInfo;
import com.github.jinahya.database.metadata.bind.IndexInfo.IndexInfoCategory;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[], Collection)
 */
@XmlRootElement
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Table
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = 6590036695540141125L;

    public static final Comparator<Table> COMPARATOR =
            Comparator.comparing(Table::getTableType)
                    .thenComparing(Table::getTableCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Table::getTableSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Table::getTableName);

    /**
     * The label of the column to which {@link #ATTRIBUTE_NAME_TABLE_CAT} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_CAT} column is bound. The value is {@value}.
     */
    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    /**
     * The label of the column to which {@link #ATTRIBUTE_NAME_TABLE_SCHEM} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_SCHEM} column is bound. The value is {@value}.
     */
    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    /**
     * The label of the column to which {@link #ATTRIBUTE_NAME_TABLE_NAME} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_NAME} column is bound. The value is {@value}.
     */
    public static final String ATTRIBUTE_NAME_TABLE_NAME = "tableName";

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        {
            for (final BestRowIdentifierCategory category : BestRowIdentifierCategory._VALUES) {
                final CategorizedBestRowIdentifiers categorized = CategorizedBestRowIdentifiers.of(category);
                context.getBestRowIdentifier(
                        getTypeCat(),
                        getTableSchem(),
                        getTableName(),
                        category.getScope(),
                        category.isNullable(),
                        categorized.getBestRowIdentifiers()
                );
                for (final BestRowIdentifier identifier : categorized.getBestRowIdentifiers()) {
                    identifier.retrieveChildren(context);
                }
                getCategorizedBestRowIdentifiers().add(categorized);
            }
        }
        {
            context.getColumns(
                    getTableCat(),
                    getTableSchem(),
                    getTableName(),
                    "%",
                    getColumns());
            for (final Column column : getColumns()) {
                column.retrieveChildren(context);
            }
        }
        {
            context.getColumnPrivileges(
                    getTableCat(),
                    getTableSchem(),
                    getTableName(),
                    "%",
                    getColumnPrivileges()
            );
            for (final ColumnPrivilege columnPrivilege : getColumnPrivileges()) {
                columnPrivilege.retrieveChildren(context);
            }
        }
        {
            context.getExportedKeys(
                    getTableCat(),
                    getTableSchem(),
                    getTableName(),
                    getExportedKeys()
            );
            for (final ExportedKey exportedKey : getExportedKeys()) {
                exportedKey.retrieveChildren(context);
            }
        }
        {
            context.getImportedKeys(
                    getTableCat(),
                    getTableSchem(),
                    getTableName(),
                    getImportedKeys()
            );
            for (final ImportedKey importedKey : getImportedKeys()) {
                importedKey.retrieveChildren(context);
            }
        }
        {
            for (final IndexInfoCategory category : IndexInfoCategory._VALUES) {
                final CategorizedIndexInfo categorized = CategorizedIndexInfo.of(category);
                context.getIndexInfo(
                        getTypeCat(),
                        getTableSchem(),
                        getTableName(),
                        category.isUnique(),
                        category.isApproximate(),
                        categorized.getIndexInfo()
                );
                for (final IndexInfo indexInfo : categorized.getIndexInfo()) {
                    indexInfo.retrieveChildren(context);
                }
                getCategorizedIndexInfo().add(categorized);
            }
        }
        {
            context.getPrimaryKeys(
                    getTableCat(),
                    getTableSchem(),
                    getTableName(),
                    getPrimaryKeys()
            );
            for (final PrimaryKey primaryKey : getPrimaryKeys()) {
                primaryKey.retrieveChildren(context);
            }
        }
        {
            context.getPseudoColumns(
                    getTableCat(),
                    getTableSchem(),
                    getTableName(),
                    "%",
                    getPseudoColumns()
            );
            for (final PseudoColumn pseudoColumn : getPseudoColumns()) {
                pseudoColumn.retrieveChildren(context);
            }
        }
        {
            context.getTablePrivileges(
                    getTableCat(),
                    getTableSchem(),
                    getTableName(),
                    getTablePrivileges()
            );
            for (final TablePrivilege tablePrivilege : getTablePrivileges()) {
                tablePrivilege.retrieveChildren(context);
            }
        }
        {
            context.getVersionColumns(
                    getTableCat(),
                    getTableSchem(),
                    getTableName(),
                    getVersionColumns()
            );
            for (final VersionColumn versionColumn : getVersionColumns()) {
                versionColumn.table = this;
                versionColumn.retrieveChildren(context);
            }
        }
    }

    @Override
    public Schema extractParent() {
        return Schema.builder()
                .tableCatalog(getTableCat())
                .tableSchem(getTableSchem())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public List<CategorizedBestRowIdentifiers> getCategorizedBestRowIdentifiers() {
        if (categorizedBestRowIdentifiers == null) {
            categorizedBestRowIdentifiers = new ArrayList<>();
        }
        return categorizedBestRowIdentifiers;
    }

    @Deprecated
    public void setCategorizedBestRowIdentifiers(
            final List<CategorizedBestRowIdentifiers> categorizedBestRowIdentifiers) {
        this.categorizedBestRowIdentifiers = categorizedBestRowIdentifiers;
    }

    public Optional<CategorizedBestRowIdentifiers> getCategorizedBestRowIdentifier(
            final BestRowIdentifierCategory category) {
        Objects.requireNonNull(category, "category is null");
        return getCategorizedBestRowIdentifiers().stream()
                .filter(i -> Objects.equals(i.getCategory(), category))
                .findFirst();
    }

    public Optional<CategorizedBestRowIdentifiers> getCategorizedBestRowIdentifier(
            final int scope, final boolean nullable) {
        return getCategorizedBestRowIdentifier(BestRowIdentifierCategory.of(scope, nullable));
    }

    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    @Deprecated
    public void setColumns(final List<Column> columns) {
        this.columns = columns;
    }

    public List<ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<>();
        }
        return columnPrivileges;
    }

    @Deprecated
    public void setColumnPrivileges(final List<ColumnPrivilege> columnPrivileges) {
        this.columnPrivileges = columnPrivileges;
    }

    public List<ExportedKey> getExportedKeys() {
        if (exportedKeys == null) {
            exportedKeys = new ArrayList<>();
        }
        return exportedKeys;
    }

    @Deprecated
    public void setExportedKeys(final List<ExportedKey> exportedKeys) {
        this.exportedKeys = exportedKeys;
    }

    public List<ImportedKey> getImportedKeys() {
        if (importedKeys == null) {
            importedKeys = new ArrayList<>();
        }
        return importedKeys;
    }

    @Deprecated
    public void setImportedKeys(final List<ImportedKey> importedKeys) {
        this.importedKeys = importedKeys;
    }

    public List<CategorizedIndexInfo> getCategorizedIndexInfo() {
        if (categorizedIndexInfo == null) {
            categorizedIndexInfo = new ArrayList<>();
        }
        return categorizedIndexInfo;
    }

    @Deprecated
    public void setCategorizedIndexInfo(final List<CategorizedIndexInfo> categorizedIndexInfo) {
        this.categorizedIndexInfo = categorizedIndexInfo;
    }

    public Optional<CategorizedIndexInfo> getCategorizedIndexInfo(final IndexInfoCategory category) {
        return getCategorizedIndexInfo()
                .stream()
                .filter(i -> Objects.equals(i.getIndexInfoCategory(), category))
                .findFirst();
    }

    public Optional<CategorizedIndexInfo> getCategorizedIndexInfo(final boolean unique, final boolean approximate) {
        return getCategorizedIndexInfo(IndexInfoCategory.of(unique, approximate));
    }

    public List<PrimaryKey> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<>();
        }
        return primaryKeys;
    }

    @Deprecated
    public void setPrimaryKeys(final List<PrimaryKey> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<PseudoColumn> getPseudoColumns() {
        if (pseudoColumns == null) {
            pseudoColumns = new ArrayList<>();
        }
        return pseudoColumns;
    }

    @Deprecated
    public void setPseudoColumns(final List<PseudoColumn> pseudoColumns) {
        this.pseudoColumns = pseudoColumns;
    }

    public List<TablePrivilege> getTablePrivileges() {
        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<>();
        }
        return tablePrivileges;
    }

    @Deprecated
    public void setTablePrivileges(final List<TablePrivilege> tablePrivileges) {
        this.tablePrivileges = tablePrivileges;
    }

    public List<VersionColumn> getVersionColumns() {
        if (versionColumns == null) {
            versionColumns = new ArrayList<>();
        }
        return versionColumns;
    }

    @Deprecated
    public void setVersionColumns(final List<VersionColumn> versionColumns) {
        this.versionColumns = versionColumns;
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @ColumnLabel("TABLE_TYPE")
    private String tableType;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("TYPE_CAT")
    private String typeCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @ColumnLabel("REF_GENERATION")
    private String refGeneration;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull CategorizedBestRowIdentifiers> categorizedBestRowIdentifiers;

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
    private List<@Valid @NotNull CategorizedIndexInfo> categorizedIndexInfo;

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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the value of {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute.
     *
     * @return the value of {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute.
     */
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Replaces the value of {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute with specified value.
     *
     * @param tableCat new value of {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute.
     */
    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    /**
     * Returns the value of {@value #ATTRIBUTE_NAME_TABLE_SCHEM} attribute.
     *
     * @return the value of {@value #ATTRIBUTE_NAME_TABLE_SCHEM} attribute.
     */
    public String getTableSchem() {
        return tableSchem;
    }

    /**
     * Replaces the value of {@value #ATTRIBUTE_NAME_TABLE_SCHEM} attribute with specified value.
     *
     * @param tableSchem new value for {@value #ATTRIBUTE_NAME_TABLE_SCHEM} attribute.
     */
    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    /**
     * Returns the value of {@value #ATTRIBUTE_NAME_TABLE_NAME} attribute.
     *
     * @return the value of {@value #ATTRIBUTE_NAME_TABLE_NAME} attribute.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Replaces the value of {@value #ATTRIBUTE_NAME_TABLE_NAME} attribute with specified value.
     *
     * @param tableName new value for {@value #ATTRIBUTE_NAME_TABLE_NAME} attribute.
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(String typeSchem) {
        this.typeSchem = typeSchem;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
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
}
