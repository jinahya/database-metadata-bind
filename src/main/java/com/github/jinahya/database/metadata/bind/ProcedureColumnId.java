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

import java.util.Objects;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class ProcedureColumnId implements MetadataTypeId<ProcedureColumnId, ProcedureColumn> {

    private static final long serialVersionUID = 7459854669925402253L;

//    public static final Comparator<ProcedureColumnId> COMPARING_IN_CASE_INSENSITIVE_ORDER =
//            Comparator.<ProcedureColumnId, SchemaId>comparing(pci -> pci.getProcedureId().getSchemaId(),
//                                                              SchemaId.CASE_INSENSITIVE_ORDER)
//                    .thenComparing(ProcedureColumnId::getColumnName, String.CASE_INSENSITIVE_ORDER);
//
//    public static final Comparator<ProcedureColumnId> COMPARING_IN_LEXICOGRAPHIC_ORDER =
//            Comparator.<ProcedureColumnId, SchemaId>comparing(pci -> pci.getProcedureId().getSchemaId(),
//                                                              SchemaId.LEXICOGRAPHIC_ORDER)
//                    .thenComparing(ProcedureColumnId::getColumnName);

    public static ProcedureColumnId of(final ProcedureId procedureId, final String columnName, final int columnType) {
        Objects.requireNonNull(procedureId, "procedureId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        return builder()
                .procedureId(procedureId)
                .columnName(columnName)
                .columnType(columnType)
                .build();
    }

    static ProcedureColumnId of(final String procedureCat, final String procedureSchem, final String specificName,
                                final String columnName, final int columnType) {
        return of(ProcedureId.of(procedureCat, procedureSchem, specificName), columnName, columnType);
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "procedureId=" + procedureId +
               ",columnName='" + columnName + '\'' +
               ",columnType=" + columnType +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProcedureColumnId)) return false;
        final ProcedureColumnId that = (ProcedureColumnId) obj;
        return columnType == that.columnType &&
               procedureId.equals(that.procedureId) &&
               columnName.equals(that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(procedureId, columnName, columnType);
    }

    private final ProcedureId procedureId;

    private final String columnName;

    private final int columnType;
}
