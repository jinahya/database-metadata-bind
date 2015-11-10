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


import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class Reflections {


    private static final Logger logger = getLogger(Metadata.class.getName());


    private static final Map<Class<?>, Class<?>> WRAPPERS;


    static {
        final Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>(9);
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(short.class, Short.class);
        map.put(void.class, Void.class);
        WRAPPERS = Collections.unmodifiableMap(map);
    }


    static Class<?> wrapper(final Class<?> primitive) {

        if (primitive == null) {
            throw new NullPointerException("null primitive");
        }

        if (!primitive.isPrimitive()) {
            throw new IllegalArgumentException("not primitive: " + primitive);
        }

        return WRAPPERS.get(primitive);
    }


    static Field findField(final Class<?> klass, final String name)
        throws NoSuchFieldException {

        try {
            final Field field = klass.getDeclaredField(name);
            return field;
        } catch (final NoSuchFieldException nsfe) {
            final Class<?> superclass = klass.getSuperclass();
            if (superclass == null) {
                throw nsfe;
            }
            return findField(superclass, name);
        }
    }


    private static String capitalize(final String name) {

        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }


    private static String getterName(final String fieldName) {

        return "get" + capitalize(fieldName);
    }


    private static String getterName(final Field field) {

        return getterName(field.getName());
    }


    private static String setterName(final String fieldName) {

        return "set" + capitalize(fieldName);
    }


    private static String setterName(final Field field) {

        return setterName(field.getName());
    }


    static Object getFieldValue(final Field field, final Object bean)
        throws IllegalAccessException {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        return field.get(bean);
    }


    static <T> Object getFieldValue(final Class<? super T> beanClass,
                                    final String fieldName,
                                    final T beanInstance)
        throws NoSuchFieldException, IllegalAccessException {

        return getFieldValue(findField(beanClass, fieldName), beanInstance);
    }


    static <T> Object getFieldValueHelper(final Class<T> beanClass,
                                          final String fieldName,
                                          final Object beanInstance)
        throws NoSuchFieldException, IllegalAccessException {

        return getFieldValue(beanClass, fieldName,
                             beanClass.cast(beanInstance));
    }


    static void setFieldValue(final Field field, final Object bean,
                              Object value)
        throws IllegalAccessException {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(bean, value);
            return;
        } catch (final IllegalArgumentException iae) {
        }
        final Class<?> fieldType = field.getType();
        final Class<?> valueType = value == null ? null : value.getClass();
        if (fieldType == Boolean.TYPE) {
            if (Number.class.isInstance(value)) {
                value = ((Number) value).intValue() != 0;
            }
            if (!Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            field.setBoolean(bean, (Boolean) value);
            return;
        }
        if (fieldType == Boolean.class) {
            if (Number.class.isInstance(value)) {
                value = ((Number) value).intValue() != 0;
            }
            if (value != null && !Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            field.set(bean, value);
            return;
        }
        if (fieldType == Short.TYPE) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            field.setShort(bean, ((Number) value).shortValue());
            return;
        }
        if (fieldType == Short.class) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            if (value != null && !Short.class.isInstance(value)) {
                value = ((Number) value).shortValue();
            }
            field.set(bean, value);
            return;
        }
        if (fieldType == Integer.TYPE) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            if (Number.class.isInstance(value)) {
                value = ((Number) value).intValue();
            }
            field.setInt(bean, (Integer) value);
            return;
        }
        if (fieldType == Integer.class) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            if (value != null && !Integer.class.isInstance(value)) {
                value = ((Number) value).intValue();
            }
            field.set(bean, value);
            return;
        }
        if (fieldType == Long.TYPE) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            if (Number.class.isInstance(value)) {
                value = ((Number) value).longValue();
            }
            field.setLong(bean, (Long) value);
            return;
        }
        if (fieldType == Long.class) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            if (value != null && !Long.class.isInstance(value)) {
                value = ((Number) value).longValue();
            }
            field.set(bean, value);
            return;
        }
        if (Collection.class.isAssignableFrom(fieldType)
            && Collection.class.isInstance(value)) {
            @SuppressWarnings("unchecked")
            final Collection<Object> collection
                = (Collection<Object>) getFieldValue(field, bean);
            if (collection != null) {
                collection.addAll((Collection<? extends Object>) value);
                return;
            }
            field.set(bean, value);
            return;
        }

        logger.log(Level.WARNING, "unhandled {0}({1}) to {2}",
                   new Object[]{value, valueType, field});
    }


    static <T> void setFieldValue(final Class<? super T> beanClass,
                                  final String fieldName, final T beanInstance,
                                  final Object fieldValue)
        throws NoSuchFieldException, IllegalAccessException {

        setFieldValue(findField(beanClass, fieldName), beanInstance,
                      fieldValue);
    }


    static <T> void setFieldValueHelper(final Class<T> beanClass,
                                        final String fieldName,
                                        final Object beanInstance,
                                        final Object fieldValue)
        throws NoSuchFieldException, IllegalAccessException {

        setFieldValue(beanClass, fieldName, beanClass.cast(beanInstance),
                      fieldValue);
    }


    private Reflections() {

        super();
    }


}

