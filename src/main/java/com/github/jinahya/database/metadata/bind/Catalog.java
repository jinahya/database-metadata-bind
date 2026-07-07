package com.github.jinahya.database.metadata.bind;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

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

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getCatalogs()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see java.sql.DatabaseMetaData#getCatalogs()
 * @see Context#getCatalogs()
 */
@_ParentOf(Table.class)
@_ParentOf(Schema.class)
@_ParentOf(Procedure.class)
@_ParentOf(Function.class)
@_ParentOf(UDT.class)
@_ChildOfNone
@SuppressWarnings({
        "java:S2637"
})
@XmlRootElement(name = "catalog")
@XmlType(name = "catalog")
public class Catalog
        extends AbstractMetadataType {

    @Serial
    private static final long serialVersionUID = 6239185259128825953L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator ordering elements in the order documented by
     * {@link java.sql.DatabaseMetaData#getCatalogs()}, placing {@code null} values as the specified context's database
     * sorts them.
     *
     * @param context    a context whose metadata determines the {@code null} ordering.
     * @param comparator a comparator for comparing (non-{@code null}) string values.
     * @return a comparator ordering elements in the order documented by
     * {@link java.sql.DatabaseMetaData#getCatalogs()}.
     * @throws SQLException if a database access error occurs.
     * @see ContextUtils#withDatabaseNullOrdering(Context, Comparator, ContextUtils.SortDirection)
     */
    static Comparator<Catalog> comparingInJdbcOrder(final Context context,
                                                    final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final var s = ContextUtils.withDatabaseNullOrdering(context, comparator, ContextUtils.SortDirection.ASCENDING);
        return Comparator.comparing(Catalog::getTableCat, s);
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

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
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

    /**
     * Returns the effective value of {@value #COLUMN_LABEL_TABLE_CAT} column, with {@code null} normalized to an empty
     * string.
     *
     * @return the effective value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    @JsonbProperty
    @XmlAttribute
    String getEffectiveTableCat() {
        return tableCat == null ? "" : tableCat;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;
}
