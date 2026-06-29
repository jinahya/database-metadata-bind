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

import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSchemas(String, String)
 */
@_ParentOf(TablePrivilege.class)
@_ParentOf(Table.class)
@_ParentOf(SuperType.class)
@_ParentOf(SuperTable.class)
@_ParentOf(Procedure.class)
@_ChildOf(Catalog.class)
@_ChildOfNone
public class Schema
        extends AbstractMetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    // The results are ordered by TABLE_CATALOG and TABLE_SCHEM.

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * The results are ordered by <code>TABLE_CATALOG</code> and <code>TABLE_SCHEM</code>.
     * </blockquote>
     *
     * @param operator   a null-safe unary operator for adjusting string values.
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see ContextUtils#nullOrdered(Context, Comparator)
     */
    static Comparator<Schema> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                        final Comparator<? super String> comparator) {
        Objects.requireNonNull(operator, "operator is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator
                .<Schema, String>comparing(v -> operator.apply(v.getTableCatalog()), comparator)
                .thenComparing(v -> operator.apply(v.getTableSchem()), comparator);
    }

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // --------------------------------------------------------------------------------------------------- TABLE_CATALOG

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS
    static Schema of(final String tableCatalog, final String tableSchem) {
        final var instance = new Schema();
        instance.setTableCatalog(tableCatalog);
        instance.setTableSchem(tableSchem);
        return instance;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    @SuppressWarnings({
            "java:S2637" // "@NonNull" values should not be set to null
    })
    Schema() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableSchem=" + tableSchem +
               ",tableCatalog=" + tableCatalog +
               '}';
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem

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
    void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ---------------------------------------------------------------------------------------------------- tableCatalog

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CATALOG} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CATALOG} column.
     */
    @Nullable
    public String getTableCatalog() {
        return tableCatalog;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_CATALOG} column.
     *
     * @param tableCatalog the value of {@value #COLUMN_LABEL_TABLE_CATALOG} column.
     */
    void setTableCatalog(final String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;
}
