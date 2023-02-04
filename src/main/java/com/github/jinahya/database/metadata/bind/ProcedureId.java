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

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@SuperBuilder(toBuilder = true)
public final class ProcedureId implements MetadataTypeId<Procedure> {

    private static final long serialVersionUID = 227742014479297143L;

    public static ProcedureId of(final SchemaId schemaId, final String specificName) {
        return builder()
                .schemaId(schemaId)
                .specificName(specificName)
                .build();
    }

    public static ProcedureId of(final String procedureCat, final String procedureSchem, final String specificName) {
        return of(SchemaId.of(procedureCat, procedureSchem), specificName);
    }

    private final SchemaId schemaId;

    private final String specificName;
}
