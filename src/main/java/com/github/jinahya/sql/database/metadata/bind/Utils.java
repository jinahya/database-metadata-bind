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
package com.github.jinahya.sql.database.metadata.bind;

import static java.beans.Introspector.decapitalize;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

final class Utils {

    private static final Logger logger = getLogger(Utils.class.getName());

    // -------------------------------------------------------------------------
    private static final Map<Class<?>, Class<?>> WRAPPER;

    static {
        final Map<Class<?>, Class<?>> m = new HashMap<Class<?>, Class<?>>();
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
            throw new IllegalArgumentException(
                    "specified class(" + primitive + ") is not primitive");
        }
        return WRAPPER.get(primitive);
    }

    // ------------------------------------------------------- java.lang.reflect
    static Field field(final Class<?> klass, final String name)
            throws NoSuchFieldException {
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
            final Class<?> klass, final Class<T> type, final Map<Field, T> map)
            throws ReflectiveOperationException {
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

    static <T extends Annotation> Map<Field, T> fields(
            final Class<?> c, final Class<T> a)
            throws ReflectiveOperationException {
        return fields(c, a, new HashMap<Field, T>());
    }

//    // ---------------------------------------------------------------- java.sql
//    private static final Map<Integer, String> SQL_TYPES;
//
//    static {
//        final Map<Integer, String> sqlTypes = new HashMap<Integer, String>();
//        for (final Field field : Types.class.getFields()) {
//            final int modifiers = field.getModifiers();
//            if (!Modifier.isPublic(modifiers)) {
//                continue;
//            }
//            if (!Modifier.isStatic(modifiers)) {
//                continue;
//            }
//            if (!Integer.TYPE.equals(field.getType())) {
//                continue;
//            }
//            try {
//                final int type = field.getInt(null);
//                final String name = field.getName();
//                assert sqlTypes.put(type, name) == null :
//                        "duplicate sql type retrived: " + type + " / " + name;
//            } catch (final IllegalAccessException iae) {
//                logger.severe("failed to get sql type value from " + field);
//            }
//        }
//        SQL_TYPES = unmodifiableMap(sqlTypes);
//    }
//
//    static Set<Integer> sqlTypes() throws IllegalAccessException {
//        if (true) {
//            return SQL_TYPES.keySet();
//        }
//        // @todo: remove following block
//        final Set<Integer> sqlTypes = new HashSet<Integer>();
//        for (final Field field : Types.class.getFields()) {
//            final int modifiers = field.getModifiers();
//            if (!Modifier.isPublic(modifiers)) {
//                continue;
//            }
//            if (!Modifier.isStatic(modifiers)) {
//                continue;
//            }
//            if (!Integer.TYPE.equals(field.getType())) {
//                continue;
//            }
//            sqlTypes.add(field.getInt(null));
//        }
//        return sqlTypes;
//    }
//
//    static String sqlTypeName(final int value) throws IllegalAccessException {
//        if (true) {
//            return SQL_TYPES.get(value);
//        }
//        // @todo: remove following block
//        for (final Field field : Types.class.getFields()) {
//            final int modifiers = field.getModifiers();
//            if (!Modifier.isPublic(modifiers)) {
//                continue;
//            }
//            if (!Modifier.isStatic(modifiers)) {
//                continue;
//            }
//            if (!Integer.TYPE.equals(field.getType())) {
//                continue;
//            }
//            if (field.getInt(null) == value) {
//                return field.getName();
//            }
//        }
//        return null;
//    }
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
        final Set<String> labels = new HashSet<String>(count);
        for (int i = 1; i <= count; i++) {
            labels.add(metadata.getColumnLabel(i).toUpperCase());
        }
        return labels;
    }

    static String path(final Class<?> klass, final Field field) {
        return decapitalize(klass.getSimpleName()) + "/" + field.getName();
    }

    static void field(final Field field, final Object obj, final Object value)
            throws ReflectiveOperationException {
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
                field.setLong(obj, (Long) value);
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

    // -------------------------------------------------------------------------
    private Utils() {
        super();
    }
}
