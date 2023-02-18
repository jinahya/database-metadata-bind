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
final class FunctionColumnId implements MetadataTypeId<FunctionColumnId, FunctionColumn> {

    private static final long serialVersionUID = 7221973324274278465L;

    public static final Comparator<FunctionColumnId> COMPARING_CASE_INSENSITIVE =
            Comparator.comparing(FunctionColumnId::getFunctionId, FunctionId.COMPARING_CASE_INSENSITIVE);

    public static final Comparator<FunctionColumnId> COMPARING_NATURAL =
            Comparator.comparing(FunctionColumnId::getFunctionId, FunctionId.COMPARING_NATURAL);

    public static FunctionColumnId of(final FunctionId functionId, final String columnName, final int columnType) {
        Objects.requireNonNull(functionId, "functionId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        return builder()
                .functionId(functionId)
                .columnName(columnName)
                .columnType(columnType)
                .build();
    }

    public static FunctionColumnId of(final String functionCat, final String functionSchem, final String functionName,
                                      final String specificName, final String columnName, final int columnType) {
        return of(FunctionId.of(functionCat, functionSchem, functionName, specificName), columnName, columnType);
    }

    private final FunctionId functionId;

    private final String columnName;

    private final int columnType;
}
