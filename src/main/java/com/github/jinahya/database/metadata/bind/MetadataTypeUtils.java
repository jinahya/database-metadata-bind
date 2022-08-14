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

import java.lang.reflect.Field;
import java.util.Objects;

final class MetadataTypeUtils {

    static <T extends MetadataType> Object getLabeledValue(final Class<T> clazz, final T instance, final String label) {
        Objects.requireNonNull(clazz, "clazz is null");
        Objects.requireNonNull(instance, "instance is null");
        Objects.requireNonNull(label, "label is null");
        final Field field = Utils.getLabeledField(clazz, label);
        if (field == null) {
            throw new IllegalArgumentException("no field labeled as " + label);
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.get(instance);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to get value from " + field);
        }
    }

    private MetadataTypeUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
