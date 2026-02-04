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

import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An abstract class for implementing {@link MetadataType}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
abstract class AbstractMetadataType
        implements MetadataType {

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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }

    // -------------------------------------------------------------------------------------------------- unknownColumns
    @Override
    public Map<String, Object> getUnknownColumns() {
        return unknownColumns;
    }

    @Nullable
    Object putUnknownColumn(final String label, final Object value) {
        return unknownColumns.put(label, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final transient Map<String, Object> unknownColumns = new HashMap<>();
}
