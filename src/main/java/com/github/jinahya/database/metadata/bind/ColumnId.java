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

/**
 * An identifier for identifying a {@link Column} within a {@link Table}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@SuperBuilder(toBuilder = true)
public final class ColumnId implements MetadataTypeId<ColumnId, Column> {

    private static final long serialVersionUID = -4452694121211962289L;

    private static final Comparator<ColumnId> COMPARATOR =
            Comparator.comparing(ColumnId::getTableId)
                    .thenComparingInt(ColumnId::getOrdinalPosition);

    public static ColumnId of(final TableId tableId, final String columnName, final int ordinalPosition) {
        return builder()
                .tableId(tableId)
                .columnName(columnName)
                .ordinalPosition(ordinalPosition)
                .build();
    }

    public static ColumnId of(final String tableCat, final String tableSchem, final String tableName,
                              final String columnName, final int ordinalPosition) {
        return of(TableId.of(tableCat, tableSchem, tableName), columnName, ordinalPosition);
    }

    @Override
    public int compareTo(final ColumnId o) {
        return COMPARATOR.compare(this, Objects.requireNonNull(o, "o is null"));
    }

    private final TableId tableId;

    private final String columnName;

    @EqualsAndHashCode.Exclude
    private final int ordinalPosition;
}
