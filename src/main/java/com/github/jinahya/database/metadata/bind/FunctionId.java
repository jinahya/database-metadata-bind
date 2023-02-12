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
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class FunctionId implements MetadataTypeId<FunctionId, Function> {

    private static final long serialVersionUID = 8614281252146063072L;

    private static final Comparator<FunctionId> COMPARATOR =
            Comparator.comparing(FunctionId::getSchemaId)
                    .thenComparing(FunctionId::getFunctionName)
                    .thenComparing(FunctionId::getSpecificName);

    static FunctionId of(final SchemaId schemaId, final String functionName, final String specificName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(functionName, "functionName is null");
        Objects.requireNonNull(specificName, "specificName is null");
        return FunctionId.builder()
                .schemaId(schemaId)
                .functionName(functionName)
                .specificName(specificName)
                .build();
    }

    static FunctionId of(final String functionCat, final String functionSchem, final String functionName,
                         final String specificName) {
        return of(SchemaId.of(functionCat, functionSchem), functionName, specificName);
    }

    public static FunctionId of(final SchemaId schemaId, final String specificName) {
        return of(schemaId, "", specificName);
    }

    public static FunctionId of(final String functionCat, final String functionSchem, final String specificName) {
        return of(functionCat, functionSchem, "", specificName);
    }

    @Override
    public int compareTo(final FunctionId o) {
        return COMPARATOR.compare(this, Objects.requireNonNull(o, "o is null"));
    }

    private final SchemaId schemaId;

    @EqualsAndHashCode.Exclude
    private final String functionName;

    private final String specificName;
}
