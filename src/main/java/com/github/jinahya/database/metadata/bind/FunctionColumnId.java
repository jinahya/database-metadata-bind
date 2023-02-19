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

    public static final Comparator<FunctionColumnId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(FunctionColumnId::getFunctionId, FunctionId.CASE_INSENSITIVE_ORDER);

    public static final Comparator<FunctionColumnId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(FunctionColumnId::getFunctionId, FunctionId.LEXICOGRAPHIC_ORDER);

    public static FunctionColumnId of(final FunctionId functionId, final String columnName, final int columnType) {
        Objects.requireNonNull(functionId, "functionId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        if (columnType < 0) {
            throw new IllegalArgumentException("negative columnType: " + columnType);
        }
        return builder()
                .functionId(functionId)
                .columnName(columnName)
                .columnType(columnType)
                .build();
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "functionId=" + functionId +
               ",columnName='" + columnName + '\'' +
               ",columnType=" + columnType +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionColumnId)) return false;
        final FunctionColumnId that = (FunctionColumnId) obj;
        return columnType == that.columnType
               && functionId.equals(that.functionId)
               && columnName.equals(that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionId, columnName, columnType);
    }

    private final FunctionId functionId;

    private final String columnName;

    private final int columnType;
}
