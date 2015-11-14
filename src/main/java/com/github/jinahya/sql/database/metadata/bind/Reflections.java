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


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import static java.util.logging.Logger.getLogger;
import static java.util.logging.Logger.getLogger;
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


    private static final String FIELD_NAME_UNKNOWN_COLUMNS = "unknownResults";


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
            return Reflections.findField(superclass, name);
        }
    }


    static List<Field> listFields(
        final Class<?> declaringClass, final List<Field> fieldList,
        final Class<? extends Annotation>... annotationClasses) {

        for (final Field declaredField : declaringClass.getDeclaredFields()) {
            for (final Class<? extends Annotation> annotationClass
                 : annotationClasses) {
                if (declaredField.getAnnotation(annotationClass) != null) {
                    fieldList.add(declaredField);
                }
            }
        }

        final Class<?> superclass = declaringClass.getSuperclass();
        if (superclass == null) {
            return fieldList;
        }

        return listFields(superclass, fieldList, annotationClasses);
    }


    static Field findField(
        final Class<?> declaringClass,
        final Class<? extends Annotation>... annotationClasses) {

        final List<Field> parentFields = listFields(
            declaringClass, new ArrayList<Field>(1), annotationClasses);

        return parentFields.isEmpty() ? null : parentFields.get(0);
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

        return getFieldValue(Reflections.findField(beanClass, fieldName), beanInstance);
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

        try {
            field.set(bean, Values.adapt(field.getType(), value, field));
        } catch (final IllegalArgumentException iae) {
            logger.log(Level.WARNING, "failed to set value({0}) to field({1})",
                       new Object[]{value, field});
        }

        if (true) {
            return;
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

        setFieldValue(Reflections.findField(beanClass, fieldName), beanInstance,
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


    static Set<Integer> getSqlTypes() throws IllegalAccessException {

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


    static String getSqlTypeName(final int value)
        throws IllegalAccessException {

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


    static Type getParentType(final Class<?> childClass)
        throws ReflectiveOperationException {

        if (!Child.class.isAssignableFrom(childClass)) {
            throw new IllegalArgumentException(
                "childClass(" + childClass + ") is not assignable to "
                + Child.class);
        }

        Type parentType = null;

        for (final Type i : childClass.getGenericInterfaces()) {
            if (!Child.class.equals(i)) {
                continue;
            }
            final ParameterizedType p = (ParameterizedType) i;
            final Type[] a = p.getActualTypeArguments();
            parentType = a[0];
        }

        return parentType;
    }


    static <C, P> void setParent(final Class<C> childClass,
                                 final Iterable<? extends C> childBeans,
                                 final Class<P> parentClass, final P parentBean)
        throws ReflectiveOperationException {

        if (!Child.class.isAssignableFrom(childClass)) {
            return;
        }

        final Method method = childClass.getMethod("setParent", Object.class);
        for (final Object childBean : childBeans) {
            method.invoke(childBean, parentBean);
        }
    }


    static void setParent(final Class<?> childClass,
                          final Iterable<?> childBeans, final Object parentBean)
        throws ReflectiveOperationException {

        @SuppressWarnings("unchecked")
        final Field parentField = findField(childClass, Parent.class);
        if (parentField != null
            && parentField.getType().isAssignableFrom(parentBean.getClass())) {
            if (!parentField.isAccessible()) {
                parentField.setAccessible(true);
            }
            for (final Object childBean : childBeans) {
                parentField.set(childBean, parentBean);
            }
            return;
        }

        if (!Child.class.isAssignableFrom(childClass)) {
            return;
        }

        final Method method = childClass.getMethod("setParent", Object.class);
        for (final Object childBean : childBeans) {
            method.invoke(childBean, parentBean);
        }
    }


    static <T> void setUnknownResults(final Class<? super T> beanClass,
                                      final Set<String> columnLabels,
                                      final ResultSet resultSet,
                                      final T beanInstance)
        throws SQLException, ReflectiveOperationException {

        if (columnLabels.isEmpty()) {
            return;
        }

        final Field field;
        try {
            field = Reflections.findField(beanClass, FIELD_NAME_UNKNOWN_COLUMNS);
            //field = beanClass.getDeclaredField(FIELD_NAME_UNKNOWN_COLUMNS);
        } catch (final NoSuchFieldException nsfe) {
            logger.log(Level.WARNING, "field not found: {0} in {1}",
                       new Object[]{FIELD_NAME_UNKNOWN_COLUMNS, beanClass});
            return;
        }

        final List<UnknownResult> value
            = new ArrayList<UnknownResult>(columnLabels.size());
        for (final String columnLabel : columnLabels) {
            value.add(new UnknownResult()
                .label(columnLabel)
                .value(resultSet.getObject(columnLabel))
            );
        }

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(beanInstance, value);
    }


    private Reflections() {

        super();
    }


}

