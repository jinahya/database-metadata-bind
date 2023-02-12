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
public final class PrimaryKeyId implements MetadataTypeId<PrimaryKeyId, PrimaryKey> {

    private static final long serialVersionUID = -111977405695306679L;

    private static final Comparator<PrimaryKeyId> COMPARING_AS_SPECIFIED =
            Comparator.comparing(PrimaryKeyId::getTableId)
                    .thenComparing(PrimaryKeyId::getColumnName, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER));

    private static final Comparator<PrimaryKeyId> COMPARING_KEY_SEQ =
            Comparator.comparing(PrimaryKeyId::getTableId)
                    .thenComparingInt(PrimaryKeyId::getKeySeq);

    static PrimaryKeyId of(final TableId tableId, final String columName, final int keySeq) {
        Objects.requireNonNull(tableId, "tableId is null");
        Objects.requireNonNull(columName, "columName is null");
        if (keySeq <= 0) {
            throw new IllegalArgumentException("non-positive keySeq: " + keySeq);
        }
        return builder()
                .tableId(tableId)
                .columnName(columName)
                .keySeq(keySeq)
                .build();
    }

    static PrimaryKeyId of(final String tableCat, final String tableSchem, final String tableName,
                           final String columnName, final int keySeq) {
        return of(TableId.of(tableCat, tableSchem, tableName), columnName, keySeq);
    }

    public static PrimaryKeyId of(final TableId tableId, final String columName) {
        return of(tableId, columName, 1);
    }

    public static PrimaryKeyId of(final String tableCat, final String tableSchem, final String tableName,
                                  final String columnName) {
        return of(tableCat, tableSchem, tableName, columnName, 1);
    }

    @Override
    public int compareTo(final PrimaryKeyId o) {
        return COMPARING_AS_SPECIFIED.compare(this, Objects.requireNonNull(o, "o is null"));
    }

    private final TableId tableId;

    private final String columnName;

    @EqualsAndHashCode.Exclude
    private final int keySeq;
}
