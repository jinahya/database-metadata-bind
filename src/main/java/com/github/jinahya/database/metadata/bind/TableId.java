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

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public final class TableId implements MetadataTypeId {

    private static final long serialVersionUID = -7614201161734063836L;

    public static TableId of(final SchemaId schemaId, final String tableName) {
        return TableId.builder()
                .schemaId(schemaId)
                .tableName(tableName)
                .build();
    }

    public static TableId of(final String tableCat, final String tableSchem, final String tableName) {
        return of(SchemaId.of(tableCat, tableSchem), tableName);
    }

    private final SchemaId schemaId;

    private final String tableName;
}
