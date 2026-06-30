package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

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

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getCatalogs()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getCatalogsAndAcceptEach(Consumer)
 */
@_ParentOf(Table.class)
@_ParentOf(Schema.class)
@_ParentOf(Procedure.class)
@_ParentOf(Function.class)
@_ParentOf(UDT.class)
@_ChildOfNone
@XmlRootElement(name = "catalog")
@XmlType(name = "catalog")
public class Catalog
        extends AbstractMetadataType {

    @Serial
    private static final long serialVersionUID = 6239185259128825953L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * The results are ordered by catalog name.
     * </blockquote>
     *
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see java.sql.DatabaseMetaData#getCatalogs()
     */
    static Comparator<Catalog> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator.comparing(Catalog::getTableCat, comparator);
    }

    /**
     * Returns a comparator comparing values in the specified order, placing {@code null} values as the specified
     * context's database sorts them.
     *
     * @param context    a context whose metadata determines the {@code null} ordering.
     * @param comparator a comparator for comparing (non-{@code null}) string values.
     * @return a comparator comparing values in the specified order.
     * @throws SQLException if a database access error occurs.
     * @see ContextUtils#withDatabaseNullOrdering(Context, Comparator, ContextUtils.SortDirection)
     */
    static Comparator<Catalog> comparingInSpecifiedOrder(final Context context,
                                                         final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return comparingInSpecifiedOrder(
                ContextUtils.withDatabaseNullOrdering(context, comparator, ContextUtils.SortDirection.ASCENDING));
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    Catalog() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               '}';
    }

    // ---------------------------------------------------------------------------------------------- Jakarta-Validation

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
    void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    String getEffectiveTableCat() {
        return tableCat == null ? "" : tableCat;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;
}
