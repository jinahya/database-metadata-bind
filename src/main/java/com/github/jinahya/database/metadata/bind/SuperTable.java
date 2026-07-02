package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import org.jspecify.annotations.Nullable;

import java.io.Serial;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTables(String, String, String)
 */
@_ChildOf(Table.class)
public class SuperTable
        extends AbstractMetadataType {

    @Serial
    private static final long serialVersionUID = 3579710773784268831L;

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------ TABLE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ------------------------------------------------------------------------------------------------- SUPERTABLE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SUPERTABLE_NAME = "SUPERTABLE_NAME";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    SuperTable() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",supertableName=" + supertableName +
               '}';
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @param tableCat the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    String getEffectiveTableCat() {
        return tableCat == null ? "" : tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @param tableSchem the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    String getEffectiveTableSchem() {
        return tableSchem == null ? "" : tableSchem;
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
    void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // -------------------------------------------------------------------------------------------------- supertableName

    /**
     * Returns the value of {@value #COLUMN_LABEL_SUPERTABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SUPERTABLE_NAME} column.
     */
    public String getSupertableName() {
        return supertableName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SUPERTABLE_NAME} column.
     *
     * @param supertableName the value of {@value #COLUMN_LABEL_SUPERTABLE_NAME} column.
     */
    void setSupertableName(final String supertableName) {
        this.supertableName = supertableName;
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

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_SUPERTABLE_NAME)
    private String supertableName;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the table reference identified by {@value #COLUMN_LABEL_TABLE_CAT}, {@value #COLUMN_LABEL_TABLE_SCHEM},
     * and {@value #COLUMN_LABEL_TABLE_NAME}.
     *
     * @return the table reference identified by this super-table row.
     */
    Table getTableRef() {
        final var table = new Table();
        table.setTableCat(getEffectiveTableCat());
        table.setTableSchem(getEffectiveTableSchem());
        table.setTableName(tableName);
        return table;
    }

    /**
     * Returns the direct super table reference identified by {@value #COLUMN_LABEL_TABLE_CAT},
     * {@value #COLUMN_LABEL_TABLE_SCHEM}, and {@value #COLUMN_LABEL_SUPERTABLE_NAME}.
     *
     * @return the direct super table reference identified by this super-table row.
     */
    Table getSupertableRef() {
        final var table = new Table();
        table.setTableCat(getEffectiveTableCat());
        table.setTableSchem(getEffectiveTableSchem());
        table.setTableName(supertableName);
        return table;
    }
}
