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
public final class ProcedureId implements MetadataTypeId<ProcedureId, Procedure> {

    private static final long serialVersionUID = 227742014479297143L;

    private static final Comparator<ProcedureId> COMPARATOR =
            Comparator.comparing(ProcedureId::getSchemaId)
                    .thenComparing(ProcedureId::getProcedureName, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(ProcedureId::getSpecificName, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static ProcedureId of(final SchemaId schemaId, final String procedureName, final String specificName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(procedureName, "procedureName is null");
        Objects.requireNonNull(specificName, "specificName is null");
        return builder()
                .schemaId(schemaId)
                .procedureName(procedureName)
                .specificName(specificName)
                .build();
    }

    static ProcedureId of(final String procedureCat, final String procedureSchem, final String procedureName,
                          final String specificName) {
        return of(SchemaId.of(procedureCat, procedureSchem), procedureName, specificName);
    }

    public static ProcedureId of(final SchemaId schemaId, final String specificName) {
        return of(schemaId, "", specificName);
    }

    public static ProcedureId of(final String procedureCat, final String procedureSchem, final String specificName) {
        return of(procedureCat, procedureSchem, "", specificName);
    }

    @Override
    public int compareTo(final ProcedureId o) {
        return COMPARATOR.compare(this, Objects.requireNonNull(o, "o is null"));
    }

    private final SchemaId schemaId;

    @EqualsAndHashCode.Exclude
    private final String procedureName;

    private final String specificName;
}
