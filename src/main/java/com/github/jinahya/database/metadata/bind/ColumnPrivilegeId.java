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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
final class ColumnPrivilegeId extends AbstractMetadataTypeId<ColumnPrivilegeId, ColumnPrivilege> {

    private static final long serialVersionUID = 7221973324274278465L;

    static final Comparator<ColumnPrivilegeId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ColumnPrivilegeId::getColumnId, ColumnId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(ColumnPrivilegeId::getPrivilege, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<ColumnPrivilegeId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ColumnPrivilegeId::getColumnId, ColumnId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(ColumnPrivilegeId::getPrivilege);

    public static ColumnPrivilegeId of(final ColumnId columnId, final String privilege) {
        Objects.requireNonNull(columnId, "columnId is null");
        Objects.requireNonNull(privilege, "privilege is null");
        return new ColumnPrivilegeId(columnId, privilege);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{' +
               "columnId=" + columnId +
               ",privilege=" + privilege +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ColumnPrivilegeId)) return false;
        final ColumnPrivilegeId that = (ColumnPrivilegeId) obj;
        return columnId.equals(that.columnId) &&
               privilege.equals(that.privilege);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnId, privilege);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final ColumnId columnId;

    private final String privilege;
}
