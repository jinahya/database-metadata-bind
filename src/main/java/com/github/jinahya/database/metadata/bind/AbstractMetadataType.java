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

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
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
    public final Map<String, Object> getUnknownColumns() {
        return Collections.unmodifiableMap(unknownColumns());
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
        return unknownColumns().put(label, value);
    }

    /**
     * Returns the backing {@link #unknownColumns} map, lazily creating it when absent (e.g. after Java
     * deserialization, which does not restore the {@code transient} field).
     *
     * @return the backing map; never {@code null}.
     */
    private Map<String, Object> unknownColumns() {
        if (unknownColumns == null) {
            unknownColumns = new HashMap<>();
        }
        return unknownColumns;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The map holding result-set columns, by label, that have no field of this type mapped to them.
     * <p>
     * This field is excluded from every serialized form of an instance, by three different mechanisms:
     * <ul>
     *   <li><strong>Java serialization:</strong> excluded by the {@code transient} modifier, deliberately. Values here
     *       are whatever {@link java.sql.ResultSet#getObject(String)} returned and need not implement
     *       {@link java.io.Serializable}; because Java serialization aborts the whole object graph on the first
     *       non-serializable value instead of skipping it, retaining them would make serializing any metadata type
     *       succeed or fail depending on which driver produced it. See
     *       {@link MetadataType#getUnknownColumns()}.</li>
     *   <li><strong>XML (JAXB):</strong> excluded by the {@code transient} modifier alone. JAXB rejects a
     *       {@code transient} field that carries <em>any</em> JAXB annotation ("Transient field cannot have any JAXB
     *       annotations"), so {@link XmlTransient} <em>must not</em> be added here.</li>
     *   <li><strong>JSON (JSON-B):</strong> {@link JsonbTransient} is applied explicitly. Yasson also honors the
     *       {@code transient} modifier under the field-access strategy, but that is not guaranteed by the JSON-B
     *       specification; the annotation makes the exclusion portable and is invisible to JAXB.</li>
     * </ul>
     * The {@code @XmlTransient} / {@code @JsonbTransient} pair on {@link #getUnknownColumns()} covers accessor-based
     * configurations; those annotations are not consulted once binding is field-based.
     */
    @JsonbTransient
    transient Map<String, Object> unknownColumns;
}
