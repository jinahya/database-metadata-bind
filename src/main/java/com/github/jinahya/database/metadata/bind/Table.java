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
@_ParentOf(BestRowIdentifier.class)
@_ParentOf(ColumnPrivilege.class)
@_ParentOf(IndexInfo.class)
@_ParentOf(PseudoColumn.class)
@_ParentOf(VersionColumn.class)
public class Table
        extends AbstractMetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Table> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(Table::getTableType, comparator)
                .thenComparing(Table::getTableCat, comparator)
                .thenComparing(Table::getTableSchem, comparator)
                .thenComparing(Table::getTableName, comparator);
    }

    static Comparator<Table> comparingInSpecifiedOrder(final Context context,
                                                       final Comparator<? super String> comparator)
            throws SQLException {
        return comparingInSpecifiedOrder(
                ContextUtils.nullPrecedence(context, comparator)
        );
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
               '}';
    }

    public String getTableCatEffective() {
        return Optional.ofNullable(getTableCat())
                .map(String::strip)
                .map(String::toUpperCase)
                .orElse("");
    }

    public String getTableSchemEffective() {
        return Optional.ofNullable(getTableSchem())
                .map(String::strip)
                .map(String::toUpperCase)
                .orElse("");
    }

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
        final var that = (Table) obj;
        return Objects.equals(getTableCatEffective(), that.getTableCatEffective()) &&
               Objects.equals(getTableSchemEffective(), that.getTableSchemEffective()) &&
               Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTableCatEffective(), getTableSchemEffective(), tableName);
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

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @param tableCat the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
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

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @param tableSchem the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
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

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @param tableName the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
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

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     *
     * @param tableType the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     */
    public void setTableType(final String tableType) {
        this.tableType = tableType;
    }

    // --------------------------------------------------------------------------------------------------------- remarks

    /**
     * Returns the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @return the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @param remarks the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------------------------------------------------- typeCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     */
    public String getTypeCat() {
        return typeCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     *
     * @param typeCat the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     */
    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */
    public String getTypeSchem() {
        return typeSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @param typeSchem the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */
    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -------------------------------------------------------------------------------------------------------- typeName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     *
     * @param typeName the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    // --------------------------------------------------------------------------------------- selfReferencingColumnName

    /**
     * Returns the value of {@value #COLUMN_LABEL_SELF_REFERENCING_COL_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SELF_REFERENCING_COL_NAME} column.
     */
    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SELF_REFERENCING_COL_NAME} column.
     *
     * @param selfReferencingColName the value of {@value #COLUMN_LABEL_SELF_REFERENCING_COL_NAME} column.
     */
    public void setSelfReferencingColName(final String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    // --------------------------------------------------------------------------------------------------- refGeneration

    /**
     * Returns the value of {@value #COLUMN_LABEL_REF_GENERATION} column.
     *
     * @return the value of {@value #COLUMN_LABEL_REF_GENERATION} column.
     */
    public String getRefGeneration() {
        return refGeneration;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_REF_GENERATION} column.
     *
     * @param refGeneration the value of {@value #COLUMN_LABEL_REF_GENERATION} column.
     */
    public void setRefGeneration(final String refGeneration) {
        this.refGeneration = refGeneration;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
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
}
