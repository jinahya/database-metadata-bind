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

import lombok.extern.java.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

@Log
final class Utils {

    private static <T extends Annotation> Map<Field, T> getFieldsAnnotatedWith(
            final Class<?> c, final Class<T> a, final Map<Field, T> m) {
        for (final Field field : c.getDeclaredFields()) {
            final T value = field.getAnnotation(a);
            if (value == null) {
                continue;
            }
            if (!field.isEnumConstant()) {
                field.setAccessible(true);
            }
            m.put(field, value);
        }
        final Class<?> superclass = c.getSuperclass();
        return superclass == null ? m : getFieldsAnnotatedWith(superclass, a, m);
    }

    static <T extends Annotation> Map<Field, T> getFieldsAnnotatedWith(final Class<?> c, final Class<T> a) {
        return getFieldsAnnotatedWith(c, a, new HashMap<>());
    }

    /**
     * Returns a set of column labels of given result set.
     *
     * @param results the result set from which column labels are read.
     * @return a set of column labels
     * @throws SQLException if a database error occurs.
     * @see ResultSet#getMetaData()
     */
    static Set<String> getLabels(final ResultSet results) throws SQLException {
        final ResultSetMetaData metadata = results.getMetaData();
        final int count = metadata.getColumnCount();
        final Set<String> labels = new HashSet<>(count);
        for (int i = 1; i <= count; i++) {
            labels.add(metadata.getColumnLabel(i).toUpperCase());
        }
        return labels;
    }

    @SuppressWarnings({
            "deprecation", // isAccessible
            "java:S1874", // isAccessible
            "java:S3011" // accessibility bypass
    })
    static void setFieldValue(final Field field, final Object obj, final ResultSet results, final String label)
            throws SQLException, ReflectiveOperationException {
        Objects.requireNonNull(field, "field is null");
        Objects.requireNonNull(obj, "obj is null");
        Objects.requireNonNull(results, "results is null");
        Objects.requireNonNull(label, "label is null");
        assert field.isAccessible();
        final Class<?> fieldType = field.getType();
        assert !fieldType.isPrimitive();
        final Object value = results.getObject(label);
        assert value != null ||
               (field.isAnnotationPresent(_NullableBySpecification.class) ||
                field.isAnnotationPresent(_NullableByVendor.class))
                : String.format("null value; label: %1$s, field: %2$s", label, field);
        try {
            field.set(obj, value);
            return;
        } catch (final IllegalArgumentException iae) {
        }
        if (value == null) {
            return;
        }
        if (fieldType == Boolean.class) {
            if (true) {
                field.set(obj, results.getBoolean(label));
                return;
            }
            if (value instanceof String) {
                field.set(obj, Boolean.valueOf((String) value));
                return;
            }
            if (value instanceof Number) {
                field.set(obj, ((Number) value).intValue() != 0);
                return;
            }
        }
        if (fieldType == Integer.class) {
            if (true) {
                field.set(obj, results.getInt(label));
                return;
            }
            try {
                field.set(obj, ((Number) value).intValue());
                return;
            } catch (final ClassCastException cce) {
                if (value instanceof String) {
                    field.set(obj, Integer.valueOf((String) value));
                    return;
                }
            }
        }
        if (fieldType == Long.class) {
            if (true) {
                field.set(obj, results.getLong(label));
                return;
            }
            try {
                field.set(obj, ((Number) value).longValue());
                return;
            } catch (final ClassCastException cce) {
                if (value instanceof String) {
                    field.set(obj, Long.valueOf((String) value));
                    return;
                }
            }
        }
        log.log(Level.SEVERE,
                () -> String.format("failed to set; label: %1$s, value: %2$s, field: %3$s", label, value, field));
    }

    private Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
