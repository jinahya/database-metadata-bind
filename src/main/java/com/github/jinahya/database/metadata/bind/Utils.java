/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static java.beans.Introspector.decapitalize;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;
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
    static Field field(final Class<?> klass, final String name) throws NoSuchFieldException {
        try {
            return klass.getDeclaredField(name);
        } catch (final NoSuchFieldException nsfe) {
            final Class<?> superclass = klass.getSuperclass();
            if (superclass == null) {
                throw nsfe;
            }
            return field(superclass, name);
        }
    }

    private static <T extends Annotation> Map<Field, T> fields(
            final Class<?> klass, final Class<T> type, final Map<Field, T> map) {
        for (final Field field : klass.getDeclaredFields()) {
            final T value = field.getAnnotation(type);
            if (value == null) {
                continue;
            }
            map.put(field, value);
        }
        final Class<?> superclass = klass.getSuperclass();
        return superclass == null ? map : fields(superclass, type, map);
    }

    static <T extends Annotation> Map<Field, T> fields(final Class<?> c, final Class<T> a) {
        return fields(c, a, new HashMap<>());
    }

    /**
     * Returns a set of column labels of given result set.
     *
     * @param results the result set from which column labels are read.
     * @return a set of column labels
     * @throws SQLException if a database error occurs.
     * @see ResultSet#getMetaData()
     */
    static Set<String> labels(final ResultSet results) throws SQLException {
        final ResultSetMetaData metadata = results.getMetaData();
        final int count = metadata.getColumnCount();
        final Set<String> labels = new HashSet<>(count);
        for (int i = 1; i <= count; i++) {
            labels.add(metadata.getColumnLabel(i).toUpperCase());
        }
        return labels;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static String suppressionPath(final Class<?> klass, final String name) {
        return decapitalize(klass.getSimpleName()) + "/" + name;
    }

    static String suppressionPath(final Class<?> klass, final Field field) {
        return suppressionPath(klass, field.getName());
    }

    static String suppressionPath(final Field field) {
        return suppressionPath(field.getDeclaringClass(), field);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Deprecated
    static void field(final Field field, final Object obj, final Object value) throws ReflectiveOperationException {
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
                field.setBoolean(obj, (Boolean) value);
                return;
            }
            if (type == byte.class) {
                field.setByte(obj, (Byte) value);
                return;
            }
            if (type == char.class) {
                field.setChar(obj, (Character) value);
                return;
            }
            if (type == double.class) {
                field.setDouble(obj, (Double) value);
                return;
            }
            if (type == float.class) {
                field.setFloat(obj, (Float) value);
                return;
            }
            if (type == int.class) {
                field.setInt(obj, (Integer) value);
                return;
            }
            if (type == long.class) {
//                field.setLong(obj, (Long) value);
                if (value instanceof Number) {
                    field.setLong(obj, ((Number) value).longValue());
                    return;
                }
                return;
            }
            if (type == short.class) {
                if (value instanceof Number) {
                    field.setShort(obj, ((Number) value).shortValue());
                    return;
                }
            }
        }
        try {
            field.set(obj, value);
        } catch (final IllegalArgumentException iae) {
            if (type == Boolean.class) {
            }
            if (type == Byte.class) {
            }
            if (type == Short.class) {
                if (value instanceof Number) {
                    field.set(obj, ((Number) value).shortValue());
                    return;
                }
            }
            if (type == Integer.class) {
                if (value instanceof Number) {
                    field.set(obj, ((Number) value).intValue());
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
    private Utils() {
        super();
    }
}
