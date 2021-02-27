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
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.FINE;
import static java.util.logging.Logger.getLogger;

final class Utils {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = getLogger(Utils.class.getName());

    // -----------------------------------------------------------------------------------------------------------------
    private static final Map<Class<?>, Class<?>> WRAPPER;

    static {
        final Map<Class<?>, Class<?>> m = new HashMap<>();
        m.put(boolean.class, Boolean.class);
        m.put(byte.class, Byte.class);
        m.put(char.class, Character.class);
        m.put(double.class, Double.class);
        m.put(float.class, Float.class);
        m.put(int.class, Integer.class);
        m.put(long.class, Long.class);
        m.put(short.class, Short.class);
        m.put(void.class, Void.class);
        WRAPPER = unmodifiableMap(m);
    }

    static Class<?> wrapper(final Class<?> primitive) {
        if (primitive == null) {
            throw new NullPointerException("primitive is null");
        }
        if (!primitive.isPrimitive()) {
            throw new IllegalArgumentException("specified class(" + primitive + ") is not primitive");
        }
        return WRAPPER.get(primitive);
    }

    // ----------------------------------------------------------------------------------------------- java.lang.reflect
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
    @Deprecated
    static void field(final Field field, final Object instance, final Object value) throws ReflectiveOperationException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        final Class<?> type = field.getType();
        if (type.isPrimitive()) {
            if (value == null) {
                // @todo: WARN
                return;
            }
            if (type == boolean.class) {
                field.setBoolean(instance, (Boolean) value);
                return;
            }
            if (type == byte.class) {
                field.setByte(instance, (Byte) value);
                return;
            }
            if (type == char.class) {
                field.setChar(instance, (Character) value);
                return;
            }
            if (type == double.class) {
                field.setDouble(instance, (Double) value);
                return;
            }
            if (type == float.class) {
                field.setFloat(instance, (Float) value);
                return;
            }
            if (type == int.class) {
                field.setInt(instance, (Integer) value);
                return;
            }
            if (type == long.class) {
//                field.setLong(obj, (Long) value);
                if (value instanceof Number) {
                    field.setLong(instance, ((Number) value).longValue());
                    return;
                }
                return;
            }
            if (type == short.class) {
                if (value instanceof Number) {
                    field.setShort(instance, ((Number) value).shortValue());
                    return;
                }
            }
        }
        try {
            field.set(instance, value);
        } catch (final IllegalArgumentException iae) {
            if (type == Boolean.class) {
            }
            if (type == Byte.class) {
            }
            if (type == Short.class) {
                if (value instanceof Number) {
                    field.set(instance, ((Number) value).shortValue());
                    return;
                }
            }
            if (type == Integer.class) {
                if (value instanceof Number) {
                    field.set(instance, ((Number) value).intValue());
                    return;
                }
            }
            if (type == Long.class) {
            }
            if (type == Character.class) {
            }
            if (type == Float.class) {
            }
            if (type == Double.class) {
            }
            throw iae;
        }
    }

    static void field(final Field field, final Object obj, final ResultSet results, final String label)
            throws SQLException, ReflectiveOperationException {
        requireNonNull(field, "field is null");
        requireNonNull(obj, "obj is null");
        requireNonNull(results, "results is null");
        requireNonNull(label, "label is null");
        final Class<?> type = field.getType();
        final Object value = results.getObject(label);
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                //field.setBoolean(obj, results.getBoolean(label));
                return;
            } else if (type == byte.class) {
                field.setByte(obj, results.getByte(label));
                return;
            } else if (type == char.class) {
                logger.severe("field type char.class!!!");
                return;
            } else if (type == double.class) {
                field.setDouble(obj, results.getDouble(label));
                return;
            } else if (type == float.class) {
                field.setFloat(obj, results.getFloat(label));
                return;
            } else if (type == int.class) {
                field.setInt(obj, results.getInt(label));
                return;
            } else if (type == long.class) {
                field.setLong(obj, results.getLong(label));
//                if (value instanceof Number) {
//                    field.setLong(obj, ((Number) value).longValue());
//                    return;
//                }
                return;
            } else if (type == short.class) {
                field.setShort(obj, results.getShort(label));
//                if (value instanceof Number) {
//                    field.setShort(obj, ((Number) value).shortValue());
//                    return;
//                }
                return;
            }
        }
        assert !type.isPrimitive();
        try {
            field.set(obj, value);
            return;
        } catch (final IllegalArgumentException iae) {
            logger.log(FINE, format("failed to set %s on %s", value, field), iae);
        }
        if (value instanceof Number) {
            if (type == Boolean.class) {
                logger.severe(format("Boolean field: %s", field));
                return;
            }
            if (type == Byte.class) {
                logger.severe(format("Byte field: %s", field));
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
                field.set(obj, ((Number) value).longValue());
                return;
            }
            if (type == Character.class) {
                logger.severe(format("Character field: %s", field));
                return;
            }
            if (type == Float.class) {
                logger.severe(format("Float field: %s", field));
                return;
            }
            if (type == Double.class) {
                logger.severe(format("Double field: %s", field));
                return;
            }
        }
        logger.severe(format("failed to set value; label=%s, field=%s, value=%s", label, field, value));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void logSqlFeatureNotSupportedException(final Logger logger, final SQLFeatureNotSupportedException sqlfnse) {
        requireNonNull(logger, "logger is null");
        requireNonNull(sqlfnse, "sqlfnse is null");
        logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
