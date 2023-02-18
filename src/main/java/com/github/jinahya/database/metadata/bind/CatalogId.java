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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@SuperBuilder(toBuilder = true)
public final class CatalogId implements MetadataTypeId<CatalogId, Catalog> {

    private static final long serialVersionUID = 2793098695036855151L;

    public static final Comparator<CatalogId> COMPARING_IN_CASE_INSENSITIVE_ORDER =
            Comparator.comparing(CatalogId::getTableCat, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<CatalogId> COMPARING_IN_NATURAL_ORDER =
            Comparator.comparing(CatalogId::getTableCat, Comparator.naturalOrder());

    static CatalogId of(final String tableCat) {
        Objects.requireNonNull(tableCat, "tableCat is null");
        return builder()
                .tableCat(tableCat)
                .build();
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

    private final String tableCat;
}
