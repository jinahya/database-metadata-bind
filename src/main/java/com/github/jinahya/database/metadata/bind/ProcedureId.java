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

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
final class ProcedureId extends AbstractMetadataTypeId<ProcedureId, Procedure> {

    private static final long serialVersionUID = 673730280554507464L;

    /**
     * Creates a new instance with specified arguments.
     *
     * @param schemaId     an id of the schema in which the procedure resides.
     * @param specificName a value of {@value Procedure#COLUMN_LABEL_SPECIFIC_NAME} column.
     * @return a new instance.
     */
    public static ProcedureId of(final SchemaId schemaId, final String specificName) {
        Objects.requireNonNull(schemaId, "schemaId is null");
        Objects.requireNonNull(specificName, "specificName is null");
        return builder()
                .schemaId(schemaId)
                .specificName(specificName)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
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
        return schemaId.equals(that.schemaId) &&
               specificName.equals(that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaId, specificName);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the {@code schemaId} of this procedure id.
     *
     * @return the {@code schemaId} of this procedure id.
     */
    public SchemaId getSchemaId() {
        return schemaId;
    }

    /**
     * Returns the {@code specificName} of this procedure id.
     *
     * @return the {@code specificName} of this procedure id.
     */
    public String getSpecificName() {
        return specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final SchemaId schemaId;

    private final String specificName;
}
