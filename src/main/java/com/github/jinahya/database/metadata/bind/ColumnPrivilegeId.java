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
public final class ColumnPrivilegeId implements MetadataTypeId<ColumnPrivilegeId, Column> {

    private static final long serialVersionUID = 7221973324274278465L;

    public static final Comparator<ColumnPrivilegeId> COMPARING_CASE_INSENSITIVE =
            Comparator.comparing(ColumnPrivilegeId::getColumnId, ColumnId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(ColumnPrivilegeId::getPrivilege, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<ColumnPrivilegeId> COMPARING_NATURAL =
            Comparator.comparing(ColumnPrivilegeId::getColumnId, ColumnId.NATURAL_ORDER)
                    .thenComparing(ColumnPrivilegeId::getPrivilege);

    public static ColumnPrivilegeId of(final ColumnId columnId, final String privilege) {
        Objects.requireNonNull(columnId, "columnId is null");
        Objects.requireNonNull(privilege, "privilege is null");
        return builder()
                .columnId(columnId)
                .privilege(privilege)
                .build();
    }

    public static ColumnPrivilegeId of(final String tableCat, final String tableSchem, final String tableName,
                                       final String columnName, final String privilege) {
        return of(ColumnId.of(tableCat, tableSchem, tableName, columnName), privilege);
    }

    private final ColumnId columnId;

    private final String privilege;
}
