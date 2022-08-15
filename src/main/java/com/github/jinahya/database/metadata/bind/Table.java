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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * A key class for categorizing best row identifiers by their {@code scope} and {@code nullable}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    @XmlRootElement
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class BestRowIdentifierCategory {

        private static final Set<BestRowIdentifierCategory> VALUES = Collections.unmodifiableSet(
                Arrays.stream(BestRowIdentifier.Scope.values())
                        .map(IntFieldEnum::rawValue)
                        .flatMap(rv -> Stream.of(new BestRowIdentifierCategory(rv, false),
                                                 new BestRowIdentifierCategory(rv, true)))
                        .collect(Collectors.toSet())
        );

        public static BestRowIdentifierCategory valueOf(final int scope, final boolean nullable) {
            return VALUES.stream()
                    .filter(v -> v.getScope() == scope && v.isNullable() == nullable)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "no value matches; scope: " + scope + ", nullable: " + nullable));
        }

        private BestRowIdentifierCategory(final int scope, final boolean nullable) {
            super();
            this.scope = scope;
            this.nullable = nullable;
        }

        private BestRowIdentifierCategory() {
            this(0, false);
        }

        /**
         * Returns the value of {@code scope} attribute.
         *
         * @return the value of {@code scope} attribute.
         */
        public int getScope() {
            return scope;
        }

        /**
         * Returns the value of {@code nullable} attribute.
         *
         * @return the value of {@code nullable} attribute.
         */
        public boolean isNullable() {
            return nullable;
        }

        @XmlAttribute(required = true)
        private final int scope;

        @XmlAttribute(required = true)
        private final boolean nullable;
    }

    /**
     * A class for wrapping categorized instances of {@link BestRowIdentifier}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    @XmlRootElement
    public static class CategorizedBestRowIdentifiers {

        public CategorizedBestRowIdentifiers(@NotNull final BestRowIdentifierCategory bestRowIdentifierCategory) {
            super();
            this.bestRowIdentifierCategory =
                    Objects.requireNonNull(bestRowIdentifierCategory, "bestRowIdentifierCategory is null");
        }

        private CategorizedBestRowIdentifiers() {
            super();
            this.bestRowIdentifierCategory = null;
        }

        @NotNull
        public List<BestRowIdentifier> getBestRowIdentifiers() {
            if (bestRowIdentifiers == null) {
                bestRowIdentifiers = new ArrayList<>();
            }
            return bestRowIdentifiers;
        }

        @XmlElement(nillable = false, required = true)
        @Valid
        @NotNull
        @Setter(AccessLevel.NONE)
        @Getter
        private final BestRowIdentifierCategory bestRowIdentifierCategory;

        @XmlElementRef
        @Setter(AccessLevel.NONE)
        @Getter(AccessLevel.NONE)
        private List<@Valid @NotNull BestRowIdentifier> bestRowIdentifiers;
    }

    @XmlRootElement
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class IndexInfoCategory {

        private static final Set<IndexInfoCategory> VALUES = Collections.unmodifiableSet(
                Stream.of(true, false)
                        .flatMap(u -> Stream.of(new IndexInfoCategory(u, false),
                                                new IndexInfoCategory(u, true)))
                        .collect(Collectors.toSet())
        );

        public static IndexInfoCategory valueOf(final boolean unique, final boolean approximate) {
            return VALUES.stream()
                    .filter(v -> v.unique == unique && v.approximate == approximate)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "no value matches; unique: " + unique + ", approximate: " + approximate));
        }

        private IndexInfoCategory() {
            this(false, false);
        }

        private IndexInfoCategory(final boolean unique, final boolean approximate) {
            super();
            this.unique = unique;
            this.approximate = approximate;
        }

        private final boolean unique;

        private final boolean approximate;
    }

    @XmlRootElement
    public static class CategorizedIndexInfo {

        public CategorizedIndexInfo(@NotNull final IndexInfoCategory indexInfoCategory) {
            super();
            this.indexInfoCategory =
                    Objects.requireNonNull(indexInfoCategory, "indexInfoCategory is null");
        }

        private CategorizedIndexInfo() {
            super();
            indexInfoCategory = null;
        }

        @NotNull
        public List<IndexInfo> getIndexInfo() {
            if (indexInfo == null) {
                indexInfo = new ArrayList<>();
            }
            return indexInfo;
        }

        @XmlElement(nillable = false, required = true)
        @Valid
        @NotNull
        @Setter(AccessLevel.NONE)
        @Getter
        private final IndexInfoCategory indexInfoCategory;

        @XmlElementRef
        @Setter(AccessLevel.NONE)
        @Getter(AccessLevel.NONE)
        private List<@Valid @NotNull IndexInfo> indexInfo;
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        {
            for (final BestRowIdentifierCategory category : BestRowIdentifierCategory.VALUES) {
                final CategorizedBestRowIdentifiers categorized = new CategorizedBestRowIdentifiers(category);
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
            for (final IndexInfoCategory category : IndexInfoCategory.VALUES) {
                final CategorizedIndexInfo categorized = new CategorizedIndexInfo(category);
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

    public List<CategorizedIndexInfo> getCategorizedIndexInfo() {
        if (categorizedIndexInfo == null) {
            categorizedIndexInfo = new ArrayList<>();
        }
        return categorizedIndexInfo;
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

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
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
