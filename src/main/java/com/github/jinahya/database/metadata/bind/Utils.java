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

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Slf4j
final class Utils {

    // -----------------------------------------------------------------------------------------------------------------
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

    static Field getLabeledField(final Class<?> clazz, final String label) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(label, "label is null");
        for (final Field field : clazz.getDeclaredFields()) {
            final Label annotation = field.getAnnotation(Label.class);
            if (annotation == null) {
                continue;
            }
            if (label.equalsIgnoreCase(annotation.value())) {
                return field;
            }
        }
        final Class<?> superclass = clazz.getSuperclass();
        return superclass == null ? null : getLabeledField(superclass, label);
    }

    // -----------------------------------------------------------------------------------------------------------------

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

    /**
     * Returns a map of column labels and column indices of given result set.
     *
     * @param results the result set from which column labels and indices are read.
     * @return a map of column labels and column indices.
     * @throws SQLException if a database error occurs.
     * @see ResultSet#getMetaData()
     */
    static Map<String, Integer> getLabelsAndIndices(final ResultSet results) throws SQLException {
        final ResultSetMetaData metadata = results.getMetaData();
        final int count = metadata.getColumnCount();
        final Map<String, Integer> labelsAndIndices = new HashMap<>(count);
        for (int i = 1; i <= count; i++) {
            labelsAndIndices.put(metadata.getColumnLabel(i), i);
        }
        return labelsAndIndices;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void setFieldValue(final Field field, final Object obj, final ResultSet results, final String label)
            throws SQLException, ReflectiveOperationException {
        requireNonNull(field, "field is null");
        requireNonNull(obj, "obj is null");
        requireNonNull(results, "results is null");
        requireNonNull(label, "label is null");
        assert field.isAccessible();
        final Class<?> fieldType = field.getType();
        if (fieldType.isPrimitive()) {
            if (fieldType == boolean.class) {
                field.setBoolean(obj, results.getBoolean(label));
                return;
            } else if (fieldType == byte.class) {
                log.error("byte field found, fix me: {}", field);
                return;
            } else if (fieldType == char.class) {
                log.error("char field found, fix me: {}", field);
                return;
            } else if (fieldType == double.class) {
                log.error("double field found, fix me: {}", field);
                return;
            } else if (fieldType == float.class) {
                log.error("float field found, fix me: {}", field);
                return;
            } else if (fieldType == int.class) {
                field.setInt(obj, results.getInt(label));
                return;
            } else if (fieldType == long.class) {
                field.setLong(obj, results.getLong(label));
                return;
            } else if (fieldType == short.class) {
                field.setShort(obj, results.getShort(label));
                return;
            }
        }
        assert !fieldType.isPrimitive();
        final Object value = results.getObject(label);
        if (value == null) {
            if (fieldType.isPrimitive()) {
                log.warn("null value for a primitive field: {}", field);
            }
            if (field.getAnnotation(MayBeNull.class) == null) {
                if (field.getAnnotation(MayBeNullByVendor.class) == null) {
                    log.warn("null value for a non-null field: {}", field);
                }
            }
            return;
        }
        assert value != null;
        try {
            field.set(obj, value);
            return;
        } catch (final IllegalArgumentException iae) {
            if (log.isDebugEnabled()) {
//                log.debug("unable to set {} with {}({}) labeled as {}", field, value, value.getClass(), label, iae);
            }
        }
        if (value instanceof Number) {
            if (fieldType == Boolean.class) {
                log.error("Boolean field found, fix me: {}", field);
                return;
            }
            if (fieldType == Byte.class) {
                log.error("Byte field found, fix me: {}", field);
                return;
            }
            if (fieldType == Short.class) {
                field.set(obj, ((Number) value).shortValue());
                return;
            }
            if (fieldType == Integer.class) {
                field.set(obj, ((Number) value).intValue());
                return;
            }
            if (fieldType == Long.class) {
                log.error("Long field found, fix me: {}", field);
                return;
            }
            if (fieldType == Character.class) {
                log.error("Character field found, fix me: {}", field);
                return;
            }
            if (fieldType == Float.class) {
                log.error("Float field found, fix me: {}", field);
                return;
            }
            if (fieldType == Double.class) {
                log.error("Double field found, fix me: %1$s", field);
                return;
            }
        }
        log.error("failed to set value; label: {}, value: {}, field: {}", label, value, field);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
