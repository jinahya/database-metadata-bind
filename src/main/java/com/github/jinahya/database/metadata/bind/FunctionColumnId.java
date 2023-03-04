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

import java.util.Objects;

final class FunctionColumnId extends AbstractMetadataTypeId<FunctionColumnId, FunctionColumn> {

    private static final long serialVersionUID = 7221973324274278465L;

    static FunctionColumnId of(final FunctionId functionId, final String columnName, final int columnType) {
        Objects.requireNonNull(functionId, "functionId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        if (columnType < 0) {
            throw new IllegalArgumentException("negative columnType: " + columnType);
        }
        return new FunctionColumnId(functionId, columnName, columnType);
    }

    private FunctionColumnId(FunctionId functionId, String columnName, int columnType) {
        super();
        this.functionId = Objects.requireNonNull(functionId, "functionId is null");
        this.columnName = Objects.requireNonNull(columnName, "columnName is null");
        this.columnType = Objects.requireNonNull(columnType, "columnType is null");
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "functionId=" + functionId +
               ",columnName=" + columnName +
               ",columnType=" + columnType +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionColumnId)) return false;
        final FunctionColumnId that = (FunctionColumnId) obj;
        return columnType == that.columnType &&
               functionId.equals(that.functionId) &&
               columnName.equals(that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionId, columnName, columnType);
    }

    public FunctionId getFunctionId() {
        return functionId;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getColumnType() {
        return columnType;
    }

    private final FunctionId functionId;

    private final String columnName;

    private final int columnType;
}
