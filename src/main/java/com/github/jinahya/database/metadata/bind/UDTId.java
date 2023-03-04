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

final class UDTId extends AbstractMetadataTypeId<UDTId, UDT> {

    private static final long serialVersionUID = 5548844214174261338L;

    static final Comparator<UDTId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(UDTId::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(UDTId::getTypeName, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<UDTId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(UDTId::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(UDTId::getTypeName);

    static UDTId of(final SchemaId schemaId, final String typeName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(typeName, "typeName is null");
        return new UDTId(schemaId, typeName);
    }

    static UDTId of(final String typeCat, final String typeSchem, final String typeName) {
        return of(SchemaId.of(typeCat, typeSchem), typeName);
    }

    public UDTId(final SchemaId schemaId, final String typeName) {
        super();
        this.schemaId = Objects.requireNonNull(schemaId, "schemaId is null");
        this.typeName = Objects.requireNonNull(typeName, "typeName is null");
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "schemaId=" + schemaId +
               ",typeName=" + typeName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UDTId)) return false;
        final UDTId that = (UDTId) obj;
        return schemaId.equals(that.schemaId) &&
               typeName.equals(that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaId, typeName);
    }

    public SchemaId getSchemaId() {
        return schemaId;
    }

    public String getTypeName() {
        return typeName;
    }

    private final SchemaId schemaId;

    private final String typeName;
}
