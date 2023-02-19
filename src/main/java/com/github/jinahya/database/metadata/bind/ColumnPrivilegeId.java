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
public final class ColumnPrivilegeId implements MetadataTypeId<ColumnPrivilegeId, Column> {

    private static final long serialVersionUID = 7221973324274278465L;

    public static final Comparator<ColumnPrivilegeId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ColumnPrivilegeId::getColumnId, ColumnId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(ColumnPrivilegeId::getPrivilege, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<ColumnPrivilegeId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ColumnPrivilegeId::getColumnId, ColumnId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(ColumnPrivilegeId::getPrivilege);

    public static ColumnPrivilegeId of(final ColumnId columnId, final String privilege) {
        Objects.requireNonNull(columnId, "columnId is null");
        Objects.requireNonNull(privilege, "privilege is null");
        return builder()
                .columnId(columnId)
                .privilege(privilege)
                .build();
    }

    static ColumnPrivilegeId of(final String tableCat, final String tableSchem, final String tableName,
                                final String columnName, final String privilege) {
        return of(ColumnId.of(tableCat, tableSchem, tableName, columnName), privilege);
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "columnId=" + columnId +
               ",privilege='" + privilege + '\'' +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ColumnPrivilegeId)) return false;
        final ColumnPrivilegeId that = (ColumnPrivilegeId) obj;
        return columnId.equals(that.columnId)
               && privilege.equals(that.privilege);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnId, privilege);
    }

    private final ColumnId columnId;

    private final String privilege;
}
