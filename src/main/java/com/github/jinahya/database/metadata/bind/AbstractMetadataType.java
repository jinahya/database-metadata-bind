package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.xml.bind.annotation.XmlTransient;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class for implementing {@link MetadataType}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlTransient
abstract class AbstractMetadataType
        implements MetadataType {

    @Serial
    private static final long serialVersionUID = -3285362930174073345L;

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    AbstractMetadataType() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{' +
               "unknownColumns=" + unknownColumns +
               '}';
    }

    // -------------------------------------------------------------------------------------------------- unknownColumns

    /**
     * {@inheritDoc} The returned map contains result-set columns, by label, that have no field of this type mapped to
     * them.
     *
     * @return an unmodifiable view of unknown columns and their values; never {@code null}.
     */
    @Override
    @XmlTransient
    @JsonbTransient
    public Map<String, Object> getUnknownColumns() {
        return Collections.unmodifiableMap(unknownColumns);
    }

    /**
     * Associates the specified value with the specified column label in the map of
     * {@link #getUnknownColumns() unknown columns} of this instance.
     *
     * @param label the column label.
     * @param value the value mapped to the specified label.
     * @return the value previously associated with the specified label, or {@code null} if there was none.
     */
    @Nullable
    Object putUnknownColumn(final String label, final Object value) {
        return unknownColumns.put(label, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The map holding result-set columns, by label, that have no field of this type mapped to them. This map is
     * {@code transient} and is therefore not serialized.
     */
    final transient Map<String, Object> unknownColumns = new HashMap<>();
}
