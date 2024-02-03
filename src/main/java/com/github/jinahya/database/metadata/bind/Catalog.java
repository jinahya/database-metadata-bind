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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;
import java.util.function.Consumer;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getCatalogs()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getCatalogs(Consumer)
 */
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Catalog extends AbstractMetadataType {

    private static final long serialVersionUID = 6239185259128825953L;

    /**
     * A comparator compares catalogs with their {@link #getTableCat()} values in a case-insensitive manner.
     */
    static final Comparator<Catalog> CASE_INSENSITIVE_ORDER
            = Comparator.comparing(Catalog::getTableCat, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<Catalog> LEXICOGRAPHIC_ORDER
            = Comparator.comparing(Catalog::getTableCat, nullsFirst(naturalOrder()));

    // -----------------------------------------------------------------------------------------------------------------
    static Catalog of(final String tableCat) {
        final Catalog instance = new Catalog();
        instance.setTableCat(tableCat);
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    /**
     * An empty value for {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    static final String COLUMN_VALUE_TABLE_CAT_EMPTY = "";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a new instance whose {@code tableCat} is {@value #COLUMN_VALUE_TABLE_CAT_EMPTY}.
     *
     * @return a new virtual instance.
     */
    static Catalog newVirtualInstance() {
        final Catalog instance = new Catalog();
        instance.setTableCat(COLUMN_VALUE_TABLE_CAT_EMPTY);
        return instance;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    @EqualsAndHashCode.Include
    String tableCatNonNull() {
        if (tableCat == null) {
            return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
        }
        return tableCat;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    @EqualsAndHashCode.Exclude
    private String tableCat;
}
