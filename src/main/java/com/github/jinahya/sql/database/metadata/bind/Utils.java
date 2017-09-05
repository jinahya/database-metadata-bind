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
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

final class Utils {

    private static final Logger logger = getLogger(Metadata.class.getName());

    // --------------------------------------------------------------- java.lang
    private static final List<Class<?>> PRIMITIVE_CLASSES = asList(
            boolean.class,
            byte.class,
            short.class,
            int.class,
            long.class,
            char.class,
            float.class,
            double.class,
            void.class
    );

    private static final Map<Class<?>, Class<?>> WRAPPER_CLASSES;

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
        WRAPPER_CLASSES = Collections.unmodifiableMap(m);
    }

    static Class<?> wrapperClass(final Class<?> primitiveClass) {
        if (primitiveClass == null) {
            throw new NullPointerException("primitiveClass is null");
        }
        if (!primitiveClass.isPrimitive()) {
            throw new IllegalArgumentException(
                    "specified class(" + primitiveClass + ") is not primitive");
        }
        return WRAPPER_CLASSES.get(primitiveClass);
    }

    // ------------------------------------------------------- java.lang.reflect
    static Field findField(final Class<?> declaringClass,
                           final String fieldName)
            throws NoSuchFieldException {
        try {
            return declaringClass.getDeclaredField(fieldName);
        } catch (final NoSuchFieldException nsfe) {
            final Class<?> superclass = declaringClass.getSuperclass();
            if (superclass == null) {
                throw nsfe;
            }
            return findField(superclass, fieldName);
        }
    }

    private static <T extends Annotation> Map<Field, T> annotatedFields(
            final Class<?> declaringClass, final Class<T> annotationType,
            final Map<Field, T> annotatedFields)
            throws ReflectiveOperationException {
        for (final Field declaredField : declaringClass.getDeclaredFields()) {
            final T annotationValue
                    = declaredField.getAnnotation(annotationType);
            if (annotationValue == null) {
                continue;
            }
            annotatedFields.put(declaredField, annotationValue);
        }
        final Class<?> superclass = declaringClass.getSuperclass();
        return superclass == null ? annotatedFields
               : annotatedFields(superclass, annotationType, annotatedFields);
    }

    static <T extends Annotation> Map<Field, T> annotatedFields(
            final Class<?> declaringClass, final Class<T> annotationType) {
        try {
            return annotatedFields(declaringClass, annotationType,
                                   new HashMap<Field, T>());
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    // ---------------------------------------------------------------- java.sql
    private static final Map<Integer, String> SQL_TYPES;

    static {
        final Map<Integer, String> sqlTypes = new HashMap<Integer, String>();
        for (final Field field : Types.class.getFields()) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers)) {
                continue;
            }
            if (!Modifier.isStatic(modifiers)) {
                continue;
            }
            if (!Integer.TYPE.equals(field.getType())) {
                continue;
            }
            try {
                final int type = field.getInt(null);
                final String name = field.getName();
                assert sqlTypes.put(type, name) == null :
                        "duplicate sql type retrived: " + type + " / " + name;
            } catch (final IllegalAccessException iae) {
                logger.severe("failed to get sql type value from " + field);
            }
        }
        SQL_TYPES = Collections.unmodifiableMap(sqlTypes);
    }

    static Set<Integer> sqlTypes() throws IllegalAccessException {
        if (true) {
            return SQL_TYPES.keySet();
        }
        // @todo: remove following block
        final Set<Integer> sqlTypes = new HashSet<Integer>();
        for (final Field field : Types.class.getFields()) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers)) {
                continue;
            }
            if (!Modifier.isStatic(modifiers)) {
                continue;
            }
            if (!Integer.TYPE.equals(field.getType())) {
                continue;
            }
            sqlTypes.add(field.getInt(null));
        }
        return sqlTypes;
    }

    static String sqlTypeName(final int value) throws IllegalAccessException {
        if (true) {
            return SQL_TYPES.get(value);
        }
        // @todo: remove following block
        for (final Field field : Types.class.getFields()) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers)) {
                continue;
            }
            if (!Modifier.isStatic(modifiers)) {
                continue;
            }
            if (!Integer.TYPE.equals(field.getType())) {
                continue;
            }
            if (field.getInt(null) == value) {
                return field.getName();
            }
        }
        return null;
    }

    /**
     * Returns a set of column labels of given result set.
     *
     * @param results the result set from which column labels are read.
     * @return a set of column labels
     * @throws SQLException if a database error occurs.
     * @see ResultSet#getMetaData()
     */
    static Set<String> columnLabels(final ResultSet results)
            throws SQLException {
        final ResultSetMetaData rsmd = results.getMetaData();
        final int columnCount = rsmd.getColumnCount();
        final Set<String> columnLabels = new HashSet<String>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            columnLabels.add(rsmd.getColumnLabel(i).toUpperCase());
        }
        return columnLabels;
    }

    static String suppressionPath(final Class<?> klass, final Field field) {
        return decapitalize(klass.getSimpleName()) + "/" + field.getName();
    }

    static void setFieldValue(final Field field, final Object bean,
                              final Object value)
            throws ReflectiveOperationException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        final Class<?> fieldType = field.getType();
        if (fieldType.isPrimitive()) {
            if (value == null) {
                // @todo: WARN
                return;
            }
            if (fieldType == boolean.class) {
                field.setBoolean(bean, (Boolean) value);
                return;
            }
            if (fieldType == byte.class) {
                field.setByte(bean, (Byte) value);
                return;
            }
            if (fieldType == char.class) {
                field.setChar(bean, (Character) value);
                return;
            }
            if (fieldType == double.class) {
                field.setDouble(bean, (Double) value);
                return;
            }
            if (fieldType == float.class) {
                field.setFloat(bean, (Float) value);
                return;
            }
            if (fieldType == int.class) {
                field.setInt(bean, (Integer) value);
                return;
            }
            if (fieldType == long.class) {
                field.setLong(bean, (Long) value);
                return;
            }
            if (fieldType == short.class) {
                if (value instanceof Number) {
                    field.setShort(bean, ((Number) value).shortValue());
                    return;
                }
            }
        }
        try {
            field.set(bean, value);
        } catch (final IllegalArgumentException iae) {
            if (fieldType == Boolean.class) {
            }
            if (fieldType == Byte.class) {
            }
            if (fieldType == Short.class) {
                if (value instanceof Number) {
                    field.set(bean, ((Number) value).shortValue());
                    return;
                }
            }
            if (fieldType == Integer.class) {
            }
            if (fieldType == Long.class) {
            }
            if (fieldType == Character.class) {
            }
            if (fieldType == Float.class) {
            }
            if (fieldType == Double.class) {
            }
            throw iae;
        }
    }

    // -------------------------------------------------------------------------
    private Utils() {
        super();
    }
}
