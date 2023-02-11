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
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@SuperBuilder(toBuilder = true)
public final class FunctionColumnId implements MetadataTypeId<FunctionColumnId, FunctionColumn> {

    private static final long serialVersionUID = 7221973324274278465L;

    private static final Comparator<FunctionColumnId> COMPARATOR =
            Comparator.comparing(FunctionColumnId::getFunctionId);

    public static FunctionColumnId of(final FunctionId functionId, final String columnName) {
        Objects.requireNonNull(functionId, "functionId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        return builder()
                .functionId(functionId)
                .columnName(columnName)
                .build();
    }

    public static FunctionColumnId of(final String functionCat, final String functionSchem, final String functionName,
                                      final String specificName, final String columnName) {
        return of(FunctionId.of(functionCat, functionSchem, functionName, specificName), columnName);
    }

    @Override
    public int compareTo(final FunctionColumnId o) {
        return COMPARATOR.compare(this, Objects.requireNonNull(o, "o is null"));
    }

    private final FunctionId functionId;

    private final String columnName;
}
