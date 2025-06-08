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

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
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

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected Table() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final Table that = (Table) obj;
        return Objects.equals(tableType, that.tableType) &&
               Objects.equals(tableCat, that.tableCat) &&
               Objects.equals(tableSchem, that.tableSchem) &&
               Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                tableType,
                tableCat,
                tableSchem,
                tableName
        );
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

    protected void setTableCat(final String tableCat) {
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

    protected void setTableSchem(final String tableSchem) {
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

    protected void setTableName(final String tableName) {
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

    protected void setTableType(final String tableType) {
        this.tableType = tableType;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    protected void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    protected void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    protected void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    protected void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    // --------------------------------------------------------------------------------------- selfReferencingColumnName
    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    protected void setSelfReferencingColName(final String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    // --------------------------------------------------------------------------------------------------- refGeneration
    public String getRefGeneration() {
        return refGeneration;
    }

    protected void setRefGeneration(final String refGeneration) {
        this.refGeneration = refGeneration;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    @EqualsAndHashCode.Include
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    @EqualsAndHashCode.Include
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
    @EqualsAndHashCode.Include
    private String tableType;

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    private String typeSchem;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SELF_REFERENCING_COL_NAME)
    private String selfReferencingColName;

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
