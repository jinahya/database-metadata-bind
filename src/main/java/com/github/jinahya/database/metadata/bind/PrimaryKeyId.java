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

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
final class PrimaryKeyId implements MetadataTypeId<PrimaryKeyId, PrimaryKey> {

    private static final long serialVersionUID = -111977405695306679L;

    public static final Comparator<PrimaryKeyId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(PrimaryKeyId::getTableId, TableId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(PrimaryKeyId::getColumnName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<PrimaryKeyId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(PrimaryKeyId::getTableId, TableId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(PrimaryKeyId::getColumnName);

    public static PrimaryKeyId of(final TableId tableId, final String columName) {
        Objects.requireNonNull(tableId, "tableId is null");
        Objects.requireNonNull(columName, "columName is null");
        return builder()
                .tableId(tableId)
                .columnName(columName)
                .build();
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableId=" + tableId +
               ",columnName=" + columnName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PrimaryKeyId)) return false;
        final PrimaryKeyId that = (PrimaryKeyId) obj;
        return tableId.equals(that.tableId) &&
               columnName.equals(that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                tableId,
                columnName
        );
    }

    private final TableId tableId;

    private final String columnName;
}
