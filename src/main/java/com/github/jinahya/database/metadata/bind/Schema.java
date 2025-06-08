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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSchemas(String, String)
 */
@_ChildOf(Catalog.class)
@_ParentOf(Table.class)
public class Schema
        extends AbstractMetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Schema> comparing(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(Schema::getTableCatalog, comparator)
                .thenComparing(Schema::getTableSchem, comparator);
    }

    static Comparator<Schema> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return comparing(ContextUtils.nullPrecedence(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

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

    static Schema of(final Catalog tableCatalog, final String tableSchem) {
        return of(
                Optional.ofNullable(tableCatalog)
                        .map(Catalog::getTableCat)
                        .orElse(null),
                tableSchem
        );
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    @SuppressWarnings({
            "java:S2637" // "@NonNull" values should not be set to null
    })
    protected Schema() {
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

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (Schema) obj;
        return Objects.equals(tableSchem, that.tableSchem) &&
               Objects.equals(tableCatalog, that.tableCatalog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableSchem, tableCatalog);
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    @Nonnull
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(@Nonnull final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ---------------------------------------------------------------------------------------------------- tableCatalog
    @Nullable
    public String getTableCatalog() {
        return tableCatalog;
    }

    protected void setTableCatalog(@Nullable final String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nonnull
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @SuppressWarnings({
            "java:S116" // Field names should comply with a naming convention
    })
    private transient Catalog tableCatalog_;

    @SuppressWarnings({
            "java:S100" // Method names should comply with a naming convention
    })
    Catalog getTableCatalog_() {
        if (tableCatalog_ == null) {
            tableCatalog_ = Catalog.of(tableCatalog);
        }
        return tableCatalog_;
    }

    @SuppressWarnings({
            "java:S100", // Method names should comply with a naming convention
            "java:S117"  // Local variable and method parameter names should comply with a naming convention
    })
    void setTableCatalog_(final Catalog tableCatalog_) {
        this.tableCatalog_ = tableCatalog_;
        setTableCatalog(
                Optional.ofNullable(this.tableCatalog_)
                        .map(Catalog::getTableCat)
                        .orElse(null)
        );
    }
}
