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

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class FunctionId implements MetadataTypeId<FunctionId, Function> {

    private static final long serialVersionUID = 8614281252146063072L;

//    public static final Comparator<FunctionId> CASE_INSENSITIVE_ORDER =
//            Comparator.comparing(FunctionId::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
//                    .thenComparing(FunctionId::getFunctionName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
//                    .thenComparing(FunctionId::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));
//
//    public static final Comparator<FunctionId> LEXICOGRAPHIC_ORDER =
//            Comparator.comparing(FunctionId::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
//                    .thenComparing(FunctionId::getFunctionName, nullsFirst(naturalOrder()))
//                    .thenComparing(FunctionId::getSpecificName, nullsFirst(naturalOrder()));

    static FunctionId of(final SchemaId schemaId, final String specificName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(specificName, "specificName is null");
        return FunctionId.builder()
                .schemaId(schemaId)
                .specificName(specificName)
                .build();
    }

    public static FunctionId of(final String functionCat, final String functionSchem, final String specificName) {
        return of(SchemaId.of(functionCat, functionSchem), specificName);
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "schemaId=" + schemaId +
               ",specificName='" + specificName + '\'' +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionId)) return false;
        final FunctionId that = (FunctionId) obj;
        return schemaId.equals(that.schemaId)
               && specificName.equals(that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaId, specificName);
    }

    private final SchemaId schemaId;

    private final String specificName;
}
