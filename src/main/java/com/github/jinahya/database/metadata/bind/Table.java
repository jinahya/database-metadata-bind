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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[])
 */

@_ChildOf(Catalog.class)
@_ChildOf(Schema.class)
@_ParentOf(Column.class)
@_ParentOf(IndexInfo.class)
@_ParentOf(PseudoColumn.class)
@_ParentOf(VersionColumn.class)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Table
        extends AbstractMetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Table> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .comparing(Table::getTableType, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(Table::getTableCat, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(Table::getTableSchem, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(Table::getTableName, comparator);
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------ TABLE_NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ------------------------------------------------------------------------------------------------------ TABLE_TYPE

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    // --------------------------------------------------------------------------------------------------------- REMARKS
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // -------------------------------------------------------------------------------------------------------- TYPE_CAT
    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    // ------------------------------------------------------------------------------------------------------ TYPE_SCHEM
    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    // ------------------------------------------------------------------------------------------------------- TYPE_NAME
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // --------------------------------------------------------------------------------------- SELF_REFERENCING_COL_NAME
    public static final String COLUMN_LABEL_SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";

    // -------------------------------------------------------------------------------------------------- REF_GENERATION
    public static final String COLUMN_LABEL_REF_GENERATION = "REF_GENERATION";

    public static final String COLUMN_VALUE_REF_GENERATION_SYSTEM = "SYSTEM";

    public static final String COLUMN_VALUE_REF_GENERATION_USER = "USER";

    public static final String COLUMN_VALUE_REF_GENERATION_DERIVED = "DERIVED";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS
    static Table of(final String tableCat, final String tableSchem, final String tableName) {
        final var table = new Table();
        table.setTableCat(tableCat);
        table.setTableSchem(tableSchem);
        table.setTableName(tableName);
        return table;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    public Table() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",tableType=" + tableType +
               ",remarks=" + remarks +
               ",typeCat=" + typeCat +
               ",typeSchem=" + typeSchem +
               ",typeName=" + typeName +
               ",selfReferencingColName=" + selfReferencingColName +
               ",refGeneration=" + refGeneration +
               ",tableCatalog_=" + tableCatalog_ +
               ",tableSchema_=" + tableSchema_ +
               ",typeCatalog_=" + typeCatalog_ +
               ",typeSchema_=" + typeSchema_ +
               '}';
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------- tableShem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------- tableType

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     */
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

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    // --------------------------------------------------------------------------------------- selfReferencingColumnName
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

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @NotBlank
    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
    private String tableType;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    private String typeSchem;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SELF_REFERENCING_COL_NAME)
    private String selfReferencingColName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_REF_GENERATION)
    private String refGeneration;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Catalog tableCatalog_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Schema tableSchema_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Catalog typeCatalog_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Schema typeSchema_;

    Catalog getTableCatalog_() {
        if (tableCatalog_ == null) {
            tableCatalog_ = Catalog.of(tableCat);
        }
        return tableCatalog_;
    }

    void setTableCatalog_(final Catalog tableCatalog_) {
        this.tableCatalog_ = tableCatalog_;
        setTableCat(
                Optional.ofNullable(this.tableCatalog_)
                        .map(Catalog::getTableCat)
                        .orElse(null)
        );
    }

    Schema getTableSchema_() {
        if (tableSchema_ == null) {
            tableSchema_ = Schema.of(getTableCatalog_(), tableSchem);
        }
        return tableSchema_;
    }

    void setTableSchema_(final Schema tableSchema_) {
        this.tableSchema_ = tableSchema_;
        setTableCatalog_(
                Optional.ofNullable(this.tableSchema_)
                        .map(Schema::getTableCatalog_)
                        .orElse(null)
        );
    }

    Catalog getTypeCatalog_() {
        if (typeCatalog_ == null) {
            typeCatalog_ = Catalog.of(typeCat);
        }
        return typeCatalog_;
    }

    void setTypeCatalog_(final Catalog typeCatalog_) {
        this.typeCatalog_ = typeCatalog_;
        setTypeCat(
                Optional.ofNullable(this.typeCatalog_)
                        .map(Catalog::getTableCat)
                        .orElse(null)
        );
    }

    Schema getTypeSchema_() {
        if (typeSchema_ == null) {
            typeSchema_ = Schema.of(getTypeCatalog_(), typeSchem);
        }
        return typeSchema_;
    }

    void setTypeSchema_(final Schema typeSchema_) {
        this.typeSchema_ = typeSchema_;
        setTypeCatalog_(
                Optional.ofNullable(this.typeSchema_)
                        .map(Schema::getTableCatalog_)
                        .orElse(null)
        );
    }
}
