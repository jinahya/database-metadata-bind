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

import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class ProcedureId implements MetadataTypeId<ProcedureId, Procedure> {

    private static final long serialVersionUID = 227742014479297143L;

//    public static final Comparator<ProcedureId> CASE_INSENSITIVE_ORDER =
//            Comparator.comparing(ProcedureId::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
//                    .thenComparing(ProcedureId::getSpecificName, String.CASE_INSENSITIVE_ORDER);
//
//    public static final Comparator<ProcedureId> LEXICOGRAPHIC_ORDER =
//            Comparator.comparing(ProcedureId::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
//                    .thenComparing(ProcedureId::getSpecificName);

    static ProcedureId of(final SchemaId schemaId, final String specificName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(specificName, "specificName is null");
        return builder()
                .schemaId(schemaId)
                .specificName(specificName)
                .build();
    }

    static ProcedureId of(final String procedureCat, final String procedureSchem, final String specificName) {
        return of(SchemaId.of(procedureCat, procedureSchem), specificName);
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "schemaId=" + schemaId +
               ",specificName=" + specificName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProcedureId)) return false;
        final ProcedureId that = (ProcedureId) obj;
        return schemaId.equals(that.schemaId) && specificName.equals(that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaId, specificName);
    }

    private final SchemaId schemaId;

    private final String specificName;
}
