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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;

final class Utils {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = getLogger(Utils.class.getName());

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

    // -----------------------------------------------------------------------------------------------------------------
    static void setFieldValue(final Field field, final Object obj, final ResultSet results, final String label)
            throws SQLException, ReflectiveOperationException {
        requireNonNull(field, "field is null");
        requireNonNull(obj, "obj is null");
        requireNonNull(results, "results is null");
        requireNonNull(label, "label is null");
        assert field.isAccessible();
        final Class<?> type = field.getType();
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                field.setBoolean(obj, results.getBoolean(label));
                return;
            } else if (type == byte.class) {
                logger.severe(() -> format("byte field found, fix me: %1$s", field));
                return;
            } else if (type == char.class) {
                logger.severe(() -> format("char field found, fix me: %1$s", field));
                return;
            } else if (type == double.class) {
                logger.severe(() -> format("double field found, fix me: %1$s", field));
                return;
            } else if (type == float.class) {
                logger.severe(() -> format("float field found, fix me: %1$s", field));
                return;
            } else if (type == int.class) {
                field.setInt(obj, results.getInt(label));
                return;
            } else if (type == long.class) {
                field.setLong(obj, results.getLong(label));
                return;
            } else if (type == short.class) {
                field.setShort(obj, results.getShort(label));
                return;
            }
        }
        assert !type.isPrimitive();
        final Object value = results.getObject(label);
        if (value == null) {
            if (type.isPrimitive()) {
                logger.warning(() -> format("null value for a primitive field: %s", field));
            }
            if (field.getAnnotation(MayBeNull.class) == null) {
                if (field.getAnnotation(MayBeNullByVendor.class) == null) {
                    logger.warning(() -> format("null value for a non-null field: %s", field));
                }
            }
            return;
        }
        assert value != null;
        try {
            field.set(obj, value);
            return;
        } catch (final IllegalArgumentException iae) {
            final Level level = Level.FINE;
            if (logger.isLoggable(level)) {
                logger.log(level, iae, () -> format("unable to set %1$s with %2$s(%3$s) labeled as %4$s",
                                                    field, value, value.getClass(), label));
            }
        }
        if (value instanceof Number) {
            if (type == Boolean.class) {
                logger.severe(() -> format("Boolean field found, fix me: %1$s", field));
                return;
            }
            if (type == Byte.class) {
                logger.severe(() -> format("Byte field found, fix me: %1$s", field));
                return;
            }
            if (type == Short.class) {
                field.set(obj, ((Number) value).shortValue());
                return;
            }
            if (type == Integer.class) {
                field.set(obj, ((Number) value).intValue());
                return;
            }
            if (type == Long.class) {
                logger.severe(() -> format("Long field found, fix me: %1$s", field));
                return;
            }
            if (type == Character.class) {
                logger.severe(() -> format("Character field found, fix me: %1$s", field));
                return;
            }
            if (type == Float.class) {
                logger.severe(() -> format("Float field found, fix me: %1$s", field));
                return;
            }
            if (type == Double.class) {
                logger.severe(() -> format("Double field found, fix me: %1$s", field));
                return;
            }
        }
        logger.severe(() -> format("failed to set value; label: %s, value: %s, field: %s", label, value, field));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
