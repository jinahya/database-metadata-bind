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

final class ProcedureColumnId extends AbstractMetadataTypeId<ProcedureColumnId, ProcedureColumn> {

    private static final long serialVersionUID = 7459854669925402253L;

//    static final Comparator<ProcedureColumnId> COMPARING_IN_CASE_INSENSITIVE_ORDER =
//            Comparator.<ProcedureColumnId, SchemaId>comparing(pci -> pci.getProcedureId().getSchemaId(),
//                                                              SchemaId.CASE_INSENSITIVE_ORDER)
//                    .thenComparing(ProcedureColumnId::getColumnName, String.CASE_INSENSITIVE_ORDER);
//
//    static final Comparator<ProcedureColumnId> COMPARING_IN_LEXICOGRAPHIC_ORDER =
//            Comparator.<ProcedureColumnId, SchemaId>comparing(pci -> pci.getProcedureId().getSchemaId(),
//                                                              SchemaId.LEXICOGRAPHIC_ORDER)
//                    .thenComparing(ProcedureColumnId::getColumnName);

    static ProcedureColumnId of(final ProcedureId procedureId, final String columnName, final int columnType) {
        Objects.requireNonNull(procedureId, "procedureId is null");
        Objects.requireNonNull(columnName, "columnName is null");
        return new ProcedureColumnId(procedureId, columnName, columnType);
    }

    public ProcedureColumnId(final ProcedureId procedureId, final String columnName, final int columnType) {
        super();
        this.procedureId = Objects.requireNonNull(procedureId, "procedureId is null");
        this.columnName = Objects.requireNonNull(columnName, "columnName is null");
        this.columnType = Objects.requireNonNull(columnType, "columnType is null");
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "procedureId=" + procedureId +
               ",columnName=" + columnName +
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

    public ProcedureId getProcedureId() {
        return procedureId;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getColumnType() {
        return columnType;
    }

    private final ProcedureId procedureId;

    private final String columnName;

    private final int columnType;
}
