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

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

import static java.util.Objects.requireNonNull;

@XmlTransient
interface MetadataType extends Serializable {

    /**
     * Returns current value of a field labeled with specified value.
     *
     * @param label the label.
     * @return the current value of the field labeled with {@code label}.
     */
    default Object getLabeledValue(final String label) {
        requireNonNull(label, "label is null");
        final Field field = Utils.getLabeledField(getClass(), label);
        if (field == null) {
            throw new IllegalArgumentException("no field labeled as " + label);
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.get(this);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to get value from " + field);
        }
    }

    /**
     * Returns current value of a field labeled with specified value as a {@code boolean} value.
     *
     * @param label the label.
     * @return the current value of the field labeled with {@code label}.
     */
    default Optional<Boolean> getLabeledValueAsBoolean(final String label) {
        final Object value = getLabeledValue(label);
        if (value == null) {
            return Optional.empty();
        }
        if (value instanceof Boolean) {
            return Optional.of((Boolean) value);
        }
        return Optional.of(Boolean.valueOf(String.valueOf(value)));
    }

    /**
     * Returns current value of a field labeled with specified value as an {@code int} value.
     *
     * @param label the label.
     * @return the current value of the field labeled with {@code label}.
     */
    default OptionalInt getLabeledValueAsInt(final String label) {
        final Object value = getLabeledValue(label);
        if (value == null) {
            return OptionalInt.empty();
        }
        if (value instanceof Number) {
            return OptionalInt.of(((Number) value).intValue());
        }
        try {
            return OptionalInt.of(Integer.parseInt(String.valueOf(value)));
        } catch (final NumberFormatException nfe) {
            // empty;
        }
        throw new IllegalArgumentException("unable to return " + value + " as an int value");
    }

    /**
     * Returns current value of a field labeled with specified value as a {@code long} value.
     *
     * @param label the label.
     * @return the current value of the field labeled with {@code label}.
     */
    default OptionalLong getLabeledValueAsLong(final String label) {
        final Object value = getLabeledValue(label);
        if (value == null) {
            return OptionalLong.empty();
        }
        if (value instanceof Number) {
            return OptionalLong.of(((Number) value).longValue());
        }
        try {
            return OptionalLong.of(Long.parseLong(String.valueOf(value)));
        } catch (final NumberFormatException nfe) {
            // empty;
        }
        throw new IllegalArgumentException("unable to return " + value + " as a long value");
    }
}
