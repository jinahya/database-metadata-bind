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
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class SchemaId implements MetadataTypeId<SchemaId, Schema> {

    private static final long serialVersionUID = -9112917204279422378L;

    public static final Comparator<SchemaId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(SchemaId::getCatalogId, CatalogId.COMPARING_IN_CASE_INSENSITIVE_ORDER)
                    .thenComparing(SchemaId::getTableSchem, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<SchemaId> NATURAL_ORDER =
            Comparator.comparing(SchemaId::getCatalogId, CatalogId.COMPARING_IN_NATURAL_ORDER)
                    .thenComparing(SchemaId::getTableSchem, Comparator.naturalOrder());

    public static SchemaId of(final CatalogId catalogId, final String tableSchem) {
        Objects.requireNonNull(catalogId, "catalogId is null");
        Objects.requireNonNull(tableSchem, "tableSchem is null");
        return builder()
                .catalogId(catalogId)
                .tableSchem(tableSchem)
                .build();
    }

    public static SchemaId of(final String tableCatalog, final String tableSchem) {
        return of(CatalogId.of(tableCatalog), tableSchem);
    }

    private final CatalogId catalogId;

    private final String tableSchem;
}
