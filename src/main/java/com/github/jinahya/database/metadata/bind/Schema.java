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

import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSchemas(String, String)
 */
public class Schema extends AbstractMetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    static final Comparator<Schema> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Schema::getTableCatalog, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Schema::getTableSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<Schema> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Schema::getTableCatalog, nullsFirst(naturalOrder()))
                    .thenComparing(Schema::getTableSchem, nullsFirst(naturalOrder()));

    /**
     * Returns a new instance whose {@code tableCatalog} is {@value Catalog#COLUMN_VALUE_TABLE_CAT_EMPTY} and whose
     * {@code tableSchem} is {@value #COLUMN_VALUE_TABLE_SCHEM_EMPTY}.
     *
     * @return a new virtual instance.
     */
    public static Schema newVirtualInstance() {
        final Schema instance = new Schema();
        instance.setTableCatalog(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
        instance.setTableSchem(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
        return instance;
    }

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * A column value of {@value} for {@value #COLUMN_LABEL_TABLE_SCHEM}.
     */
    public static final String COLUMN_VALUE_TABLE_SCHEM_EMPTY = "";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCatalog=" + tableCatalog +
               ",tableSchem=" + tableSchem +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Schema)) return false;
        final Schema that = (Schema) obj;
        return Objects.equals(tableCatalogNonNull(), that.tableCatalogNonNull()) &&
               Objects.equals(tableSchem, that.tableSchem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                tableCatalogNonNull(),
                tableSchem
        );
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(final String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;

    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    String tableCatalogNonNull() {
        final String tableCatalog_ = getTableCatalog();
        if (tableCatalog_ != null) {
            return tableCatalog_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }
}
