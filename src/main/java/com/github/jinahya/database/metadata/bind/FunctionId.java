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
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class FunctionId extends AbstractMetadataTypeId<FunctionId, Function> {

    private static final long serialVersionUID = 8614281252146063072L;

    static FunctionId of(final SchemaId schemaId, final String specificName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(specificName, "specificName is null");
        return new FunctionId(schemaId, specificName);
    }

    static FunctionId of(final String functionCat, final String functionSchem, final String specificName) {
        return of(SchemaId.of(functionCat, functionSchem), specificName);
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "schemaId=" + schemaId +
               ",specificName=" + specificName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionId)) return false;
        final FunctionId that = (FunctionId) obj;
        return schemaId.equals(that.schemaId) &&
               specificName.equals(that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaId, specificName);
    }

    public SchemaId getSchemaId() {
        return schemaId;
    }

    public String getSpecificName() {
        return specificName;
    }

    private final SchemaId schemaId;

    private final String specificName;
}
