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

import java.util.Comparator;
import java.util.Objects;

final class CatalogId extends AbstractMetadataTypeId<CatalogId, Catalog> {

    private static final long serialVersionUID = 2793098695036855151L;

    static final Comparator<CatalogId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(CatalogId::getTableCat, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<CatalogId> LEXICOGRAPHIC_ORDER = Comparator.comparing(CatalogId::getTableCat);

    /**
     * Creates a new instance with specified arguments.
     *
     * @param tableCat a value of {@value Catalog#COLUMN_LABEL_TABLE_CAT} column.
     * @return a new instance.
     */
    static CatalogId of(final String tableCat) {
        Objects.requireNonNull(tableCat, "tableCat is null");
        return new CatalogId(tableCat);
    }

    CatalogId(final String tableCat) {
        super();
        this.tableCat = Objects.requireNonNull(tableCat, "tableCat is null");
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CatalogId)) return false;
        final CatalogId that = (CatalogId) obj;
        return Objects.equals(tableCat, that.tableCat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat);
    }

    public String getTableCat() {
        return tableCat;
    }

    private final String tableCat;
}
