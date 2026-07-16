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

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The parent interface for binding database metadata types.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public interface MetadataType
        extends Serializable {

    /**
     * Returns the value bound to the column of the specified label, of the specified type.
     * <p>
     * The value is first looked up among the {@link _ColumnLabel}-annotated fields of this instance's runtime class
     * (and its superclasses); if no field is mapped to the specified label, the value is looked up in
     * {@link #getUnknownColumns() unknown columns}.
     *
     * @param type  the type of the value.
     * @param label the column label whose bound value is returned.
     * @param <T>   value type parameter.
     * @return an {@link Optional} of the value bound to the {@code label}; an {@link Optional#empty() empty} optional
     * if no value is mapped to the {@code label}, or the mapped value is {@code null}.
     * @throws ClassCastException if the mapped value is not assignable to the specified {@code type}.
     */
    default <T> Optional<T> getValue(final Class<T> type, final String label) {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(label, "label is null");
        for (final var entry : ContextUtils.getFieldsAnnotatedWith(getClass(), _ColumnLabel.class).entrySet()) {
            if (!entry.getValue().value().equals(label)) {
                continue;
            }
            final var field = entry.getKey();
            final Object value;
            try {
                value = field.get(this);
            } catch (final IllegalAccessException iae) {
                throw new RuntimeException("failed to get value of " + field + " from " + this, iae);
            }
            return Optional.ofNullable(value).map(type::cast);
        }
        return Optional.ofNullable(getUnknownColumns().get(label)).map(type::cast);
    }

    /**
     * Returns an unmodifiable view of unknown columns and values.
     *
     * @return an unmodifiable view of unknown columns and values.
     */
    Map<String, Object> getUnknownColumns();
}
