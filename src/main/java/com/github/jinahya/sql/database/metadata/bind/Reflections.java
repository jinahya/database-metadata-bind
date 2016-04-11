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
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class Reflections {

    private static final Logger logger = getLogger(Metadata.class.getName());

//    private static final Map<Class<?>, Class<?>> WRAPPERS;
//
//
//    static {
//        final Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>(9);
//        map.put(boolean.class, Boolean.class);
//        map.put(byte.class, Byte.class);
//        map.put(char.class, Character.class);
//        map.put(double.class, Double.class);
//        map.put(float.class, Float.class);
//        map.put(int.class, Integer.class);
//        map.put(long.class, Long.class);
//        map.put(short.class, Short.class);
//        map.put(void.class, Void.class);
//        WRAPPERS = Collections.unmodifiableMap(map);
//    }
//
//
//    static Class<?> wrapper(final Class<?> primitive) {
//
//        if (primitive == null) {
//            throw new NullPointerException("null primitive");
//        }
//
//        if (!primitive.isPrimitive()) {
//            throw new IllegalArgumentException("not primitive: " + primitive);
//        }
//
//        return WRAPPERS.get(primitive);
//    }
    static Field field(final Class<?> declaring, final String name)
            throws NoSuchFieldException {
        try {
            return declaring.getDeclaredField(name);
        } catch (final NoSuchFieldException nsfe) {
            final Class<?> superclass = declaring.getSuperclass();
            if (superclass == null) {
                throw nsfe;
            }
            return field(superclass, name);
        }
    }

//    static List<Field> fields(
//        final Class<?> declaring, final List<Field> fields,
//        final Class<? extends Annotation> annotationClass,
//        final Class<? extends Annotation>... otherAnnotationClasses) {
//
//        for (final Field declaredField : declaring.getDeclaredFields()) {
//            if (declaredField.getAnnotation(annotationClass) != null) {
//                fields.add(declaredField);
//                continue;
//            }
//            for (final Class<? extends Annotation> otherAnnotationClass
//                 : otherAnnotationClasses) {
//                if (declaredField.getAnnotation(otherAnnotationClass) != null) {
//                    fields.add(declaredField);
//                }
//            }
//        }
//
//        final Class<?> superclass = declaring.getSuperclass();
//        if (superclass == null) {
//            return fields;
//        }
//
//        return fields(superclass, fields, annotationClass,
//                      otherAnnotationClasses);
//    }
//    static List<Field> fields(
//        final Class<?> declaring,
//        final Class<? extends Annotation> annotationClass,
//        final Class<? extends Annotation>... otherAnnotationClasses) {
//
//        final List<Field> fieldList = new ArrayList<Field>();
//
//        return fields(declaring, fieldList, annotationClass,
//                      otherAnnotationClasses);
//    }
//    static Field findField(
//        final Class<?> declaringClass,
//        final Class<? extends Annotation> annotationClass,
//        final Class<? extends Annotation>... otherAnnotationClasses) {
//
//        final List<Field> parentFields = Reflections.fields(
//            declaringClass, annotationClass, otherAnnotationClasses);
//
//        return parentFields.isEmpty() ? null : parentFields.get(0);
//    }
//    private static String capitalize(final String name) {
//
//        return name.substring(0, 1).toUpperCase() + name.substring(1);
//    }
//    private static String getterName(final String fieldName) {
//
//        return "get" + capitalize(fieldName);
//    }
//
//
//    private static String getterName(final Field field) {
//
//        return getterName(field.getName());
//    }
//
//
//    private static String setterName(final String fieldName) {
//
//        return "set" + capitalize(fieldName);
//    }
//
//
//    private static String setterName(final Field field) {
//
//        return setterName(field.getName());
//    }
//    static Object fieldValue(final Field field, final Object bean)
//        throws IllegalAccessException {
//
//        if (!field.isAccessible()) {
//            field.setAccessible(true);
//        }
//
//        return field.get(bean);
//    }
//
//
//    static <T> Object fieldValue(final Class<? super T> declaring,
//                                 final String name, final T obj)
//        throws NoSuchFieldException, IllegalAccessException {
//
//        return fieldValue(field(declaring, name), obj);
//    }
//
//
//    static <T> Object fieldValueHelper(final Class<T> declaring,
//                                       final String name, final Object obj)
//        throws NoSuchFieldException, IllegalAccessException {
//
//        return fieldValue(declaring, name, declaring.cast(obj));
//    }
//    static void fieldValue(final Field field, final Object bean,
//                           final Object value)
//        throws IllegalAccessException {
//
//        if (!field.isAccessible()) {
//            field.setAccessible(true);
//        }
//
//        try {
//            field.set(bean, value);
//            return;
//        } catch (final IllegalArgumentException iae) {
//        }
//
//        try {
//            field.set(bean, Values.adapt(field.getType(), value, field));
//        } catch (final IllegalArgumentException iae) {
//            logger.log(Level.WARNING, "failed to set value({0}) to field({1})",
//                       new Object[]{value, field});
//        }
//    }
//
//
//    static <T> void fieldValue(final Class<? super T> beanClass,
//                               final String fieldName, final T beanInstance,
//                               final Object fieldValue)
//        throws NoSuchFieldException, IllegalAccessException {
//
//        Reflections.fieldValue(field(beanClass, fieldName), beanInstance,
//                               fieldValue);
//    }
//
//
//    static <T> void fieldValueHelper(final Class<T> beanClass,
//                                     final String fieldName,
//                                     final Object beanInstance,
//                                     final Object fieldValue)
//        throws NoSuchFieldException, IllegalAccessException {
//
//        fieldValue(beanClass, fieldName, beanClass.cast(beanInstance),
//                   fieldValue);
//    }
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

//    static Type parentType(final Class<?> childType)
//        throws ReflectiveOperationException {
//
//        if (!Child.class.isAssignableFrom(childType)) {
//            throw new IllegalArgumentException(
//                "childClass(" + childType + ") is not assignable to "
//                + Child.class);
//        }
//
//        Type parentType = null;
//
//        for (final Type i : childType.getGenericInterfaces()) {
//            if (!Child.class.equals(i)) {
//                continue;
//            }
//            final ParameterizedType p = (ParameterizedType) i;
//            final Type[] a = p.getActualTypeArguments();
//            parentType = a[0];
//        }
//
//        return parentType;
//    }
    private Reflections() {
        super();
    }
}
