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
public final class UDTId implements MetadataTypeId<UDTId, UDT> {

    private static final long serialVersionUID = 5548844214174261338L;

    public static final Comparator<UDTId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(UDTId::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(UDTId::getTypeName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<UDTId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(UDTId::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(UDTId::getTypeName);

    public static UDTId of(final SchemaId schemaId, final String typeName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(typeName, "typeName is null");
        return builder()
                .schemaId(schemaId)
                .typeName(typeName)
                .build();
    }

    static UDTId of(final String typeCat, final String typeSchem, final String typeName) {
        return of(SchemaId.of(typeCat, typeSchem), typeName);
    }

    private final SchemaId schemaId;

    private final String typeName;
}
