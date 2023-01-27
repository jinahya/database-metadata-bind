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
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class SchemaId implements MetadataTypeId {

    private static final long serialVersionUID = -8068308747290112344L;

    public static SchemaId of(final CatalogId catalogId, final String tableSchem) {
        return SchemaId
                .builder()
                .catalogId(catalogId)
                .tableSchem(tableSchem)
                .build();
    }

    public static SchemaId of(final String tableCat, final String tableSchem) {
        return of(CatalogId.of(tableCat), tableSchem);
    }

    private CatalogId catalogId;

    private String tableSchem;
}
