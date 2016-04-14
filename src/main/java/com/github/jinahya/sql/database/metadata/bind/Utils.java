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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import static java.beans.Introspector.decapitalize;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class Utils {

    private static final Logger logger = getLogger(Metadata.class.getName());

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
            throw new NullPointerException("null primitive");
        }
        if (!primitiveClass.isPrimitive()) {
            throw new IllegalArgumentException(
                    "not primitive: " + primitiveClass);
        }
        return WRAPPER_CLASSES.get(primitiveClass);
    }

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

    static <T extends Annotation> Map<Field, T> annotatedFields(
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
            final Class<?> declaringClass, final Class<T> annotationType)
            throws ReflectiveOperationException {
        return annotatedFields(declaringClass, annotationType,
                               new HashMap<Field, T>());
    }

    static Set<Integer> sqlTypes() throws IllegalAccessException {
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

    static Set<String> columnLabels(final ResultSet resultSet)
            throws SQLException {
        final ResultSetMetaData rsmd = resultSet.getMetaData();
        final int columnCount = rsmd.getColumnCount();
        final Set<String> columnLabels = new HashSet<String>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            columnLabels.add(rsmd.getColumnLabel(i));
        }
        return columnLabels;
    }

    static Object propertyValue(final String name, final Object bean)
            throws ReflectiveOperationException {
        final Class<?> klass = bean.getClass();
        try {
            final BeanInfo info = Introspector.getBeanInfo(klass);
            for (final PropertyDescriptor descriptor
                 : info.getPropertyDescriptors()) {
                if (name.equals(descriptor.getName())) {
                    final Method reader = descriptor.getReadMethod();
                    if (reader != null) {
                        if (!reader.isAccessible()) {
                            reader.setAccessible(true);
                        }
                        return reader.invoke(bean);
                    }
                    break;
                }
            }
        } catch (final IntrospectionException ie) {
            ie.printStackTrace(System.err);
        }
        logger.log(Level.WARNING, "direct field accessing;name={0};bean={1}",
                   new Object[]{name, bean});
        final Field field = findField(klass, name);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(bean);
    }

    static void propertyValue(final String name, final Object bean,
                              final Object value)
            throws ReflectiveOperationException {
        final Class<?> type = bean.getClass();
        try {
            final BeanInfo info = Introspector.getBeanInfo(type);
            for (final PropertyDescriptor descriptor
                 : info.getPropertyDescriptors()) {
                if (!descriptor.getName().equals(name)) {
                    continue;
                }
                if (descriptor.getPropertyType() == null) {
                    logger.log(Level.WARNING,
                               "null property type: {0}", descriptor);
                }
                final Method writer = descriptor.getWriteMethod();
                if (writer == null) {
                    continue;
                }
                final Object adapted = adaptValue(descriptor, value);
                if (!writer.isAccessible()) {
                    writer.setAccessible(true);
                }
                writer.invoke(bean, adapted);
                return;
            }
        } catch (final IntrospectionException ie) {
            ie.printStackTrace(System.err);
        }
        logger.log(Level.WARNING,
                   "direct field accessing;name={0};bean={1},value={2}",
                   new Object[]{name, bean, value});
        final Field field = findField(type, name);
        final Object adapted = adaptValue(field, value);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(bean, adapted);
    }

    @Deprecated
    static Object adaptValue_(final Class<?> type, final Object value,
                              final Object target) {
        if (type != null && type.isInstance(value)) {
            return value;
        }
        if (type != null && !type.isPrimitive() && value == null) {
            return value;
        }
        final Class<?> valueType = value == null ? null : value.getClass();
        if (Boolean.TYPE.equals(type)) {
            if (value != null && Number.class.isInstance(value)) {
                return ((Number) value).intValue() != 0;
            }
            if (value == null || !Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return false;
            }
            return value;
        }
        if (Boolean.class.equals(type)) {
            if (value != null && Number.class.isInstance(value)) {
                return ((Number) value).intValue() != 0;
            }
            if (value != null && !Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return Boolean.FALSE;
            }
            return value;
        }
        if (Short.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return (short) 0;
            }
            if (Short.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).shortValue();
        }
        if (Short.class.equals(type)) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return null;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).shortValue();
        }
        if (Integer.TYPE.equals(type)) {
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            }
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return 0;
            }
            if (Integer.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).intValue();
        }
        if (Integer.class.equals(type)) {
            if (value instanceof String) {
                return Integer.valueOf((String) value);
            }
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return null;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).intValue();
        }
        if (Long.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return 0L;
            }
            if (Long.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).longValue();
        }
        if (Long.class.equals(type)) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return null;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).longValue();
        }
        logger.log(Level.WARNING, "unadapted value={0}({1}), field={2}",
                   new Object[]{value, valueType, target});
        return value;
    }

    static Object adaptValue(final Class<?> type, final Object value) {
        if (type != null && type.isInstance(value)) {
            return value;
        }
        if (type != null && !type.isPrimitive()
            && value == null) {
            return value;
        }
        final Class<?> valueType = value == null ? null : value.getClass();
        if (Boolean.TYPE.equals(type)) {
            if (value != null && Number.class.isInstance(value)) {
                return ((Number) value).intValue() != 0;
            }
            if (value == null || !Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "unadaptable;type={0};value={1}({2})",
                           new Object[]{type, value, valueType});
                return false;
            }
            return value;
        }
        if (Boolean.class.equals(type)) {
            if (value != null && Number.class.isInstance(value)) {
                return ((Number) value).intValue() != 0;
            }
            if (value != null && !Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "unadaptable;type={0};value={1}({2})",
                           new Object[]{type, value, valueType});
                return null;
            }
            return value;
        }
        if (Short.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "unadaptable;type={0};value={1}({2})",
                           new Object[]{type, value, valueType});
                return (short) 0;
            }
            if (Short.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).shortValue();
        }
        if (Short.class.equals(type)) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "unadaptable;type={0};value={1}({2})",
                           new Object[]{type, value, valueType});
                return (short) 0;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).shortValue();
        }
        if (Integer.TYPE.equals(type)) {
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            }
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "unadaptable;type={0};value={1}({2})",
                           new Object[]{type, value, valueType});
                return 0;
            }
            if (Integer.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).intValue();
        }
        if (Integer.class.equals(type)) {
            if (value instanceof String) {
                return Integer.valueOf((String) value);
            }
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "unadaptable;type={0};value={1}({2})",
                           new Object[]{type, value, valueType});
                return 0;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).intValue();
        }
        if (Long.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "unadaptable;type={0};value={1}({2})",
                           new Object[]{type, value, valueType});
                return 0L;
            }
            if (Long.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).longValue();
        }
        if (Long.class.equals(type)) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "unadaptable;type={0};value={1}({2})",
                           new Object[]{type, value, valueType});
                return 0L;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).longValue();
        }
        logger.log(Level.WARNING, "unadapted;type={0};value={1}({2})",
                   new Object[]{type, value, valueType});
        return value;
    }

    static Object adaptValue(final PropertyDescriptor descriptor,
                             final Object value) {
        return adaptValue(descriptor.getPropertyType(), value);
    }

    static Object adaptValue(final Field field, final Object value) {
        return adaptValue(field.getType(), value);
    }

    static String suppression(final Class<?> declaring, final Field field) {
        if (!field.getDeclaringClass().isAssignableFrom(declaring)) {
            throw new IllegalArgumentException(
                    "wrong declaring class(" + declaring + ") for field("
                    + field + ")");
        }
        return decapitalize(declaring.getSimpleName()) + "/" + field.getName();
    }

    static String suppression(final Field field) {
        return suppression(field.getDeclaringClass(), field);
    }

    static String suppression(final Class<?> beanClass,
                              final PropertyDescriptor propertyDescriptor) {
        return decapitalize(beanClass.getSimpleName()) + "/"
               + propertyDescriptor.getName();
    }

    private Utils() {
        super();
    }
}
