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
import static java.beans.Introspector.getBeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 * A utility class for {@code java.bean} package.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class Beans {


    private static final Logger logger = getLogger(Beans.class.getName());


    static List<PropertyDescriptor> getPropertyDescriptors(
        final Class<?> beanClass,
        final Class<? extends Annotation> annotationClass) {

        final BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(beanClass);
        } catch (final IntrospectionException ie) {
            ie.printStackTrace(System.err);
            return emptyList();
        }

        final PropertyDescriptor[] propertyDescriptors
            = beanInfo.getPropertyDescriptors();
        if (propertyDescriptors == null) {
            return emptyList();
        }

        final List<PropertyDescriptor> list
            = new ArrayList<PropertyDescriptor>(propertyDescriptors.length);
        for (final PropertyDescriptor propertyDescriptor
             : propertyDescriptors) {
            if (Annotations.getAnnotation(
                annotationClass, propertyDescriptor, beanClass) == null) {
                continue;
            }
            list.add(propertyDescriptor);
        }

        return list;
    }


    static Object getPropertyValue(final PropertyDescriptor propertyDescriptor,
                                   final Object beanInstance)
        throws ReflectiveOperationException {

        final Method readMethod = propertyDescriptor.getReadMethod();
        if (readMethod != null) {
            return readMethod.invoke(beanInstance);
        }

        return Reflections.getFieldValueHelper(beanInstance.getClass(),
                                               propertyDescriptor.getName(),
                                               beanInstance);
    }


    static Object getPropertyValue(final Class<?> beanClass,
                                   final String propertyName,
                                   final Object beanInstance)
        throws ReflectiveOperationException {

        final PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor
                = new PropertyDescriptor(propertyName, beanClass);
        } catch (final IntrospectionException ie) {
            throw new RuntimeException(ie);
        }

        return getPropertyValue(propertyDescriptor, beanInstance);
    }


    @SuppressWarnings("unchecked")
    static <T> void setPropertyValue(
        final PropertyDescriptor propertyDescriptor, final T beanInstance,
        Object propertyValue)
        throws ReflectiveOperationException {

        final Class<?> propertyType = propertyDescriptor.getPropertyType();
        propertyValue = Values.adapt(
            propertyType, propertyValue, propertyDescriptor);

        final Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod != null) {
            writeMethod.invoke(beanInstance, propertyValue);
            return;
        }

        if (propertyType != null
            && Collection.class.isAssignableFrom(propertyType)
            && Collection.class.isInstance(propertyValue)) {
            final Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                ((Collection<Object>) readMethod.invoke(beanInstance))
                    .addAll((Collection<? extends Object>) propertyValue);
                return;
            }
        }

        Reflections.setFieldValueHelper(beanInstance.getClass(),
                                        propertyDescriptor.getName(),
                                        beanInstance, propertyValue);
    }


    static void setPropertyValue(final Class<?> beanClass,
                                 final String propertyName,
                                 final Object beanInstance,
                                 final Object propertyValue)
        throws ReflectiveOperationException {

        final PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor
                = new PropertyDescriptor(propertyName, beanClass);
        } catch (final IntrospectionException ie) {
            throw new RuntimeException(ie);
        }

        setPropertyValue(propertyDescriptor, beanInstance, propertyValue);
    }


    static void setParent(final Class<?> childClass,
                          final Iterable<?> childBeans,
                          final Object parentBean)
        throws ReflectiveOperationException {

        final Class<?> parentType = parentBean.getClass();

        final BeanInfo beanInfo;
        try {
            beanInfo = getBeanInfo(childClass);
        } catch (final IntrospectionException ie) {
            ie.printStackTrace(System.err);
            return;
        }
        for (final PropertyDescriptor propertyDescriptor
             : beanInfo.getPropertyDescriptors()) {
            final Class<?> propertyType = propertyDescriptor.getPropertyType();
            if (!propertyType.equals(parentType)) {
                continue;
            }
            for (final Object childBean : childBeans) {
                setPropertyValue(propertyDescriptor, childBean, parentBean);
            }
            break;
        }
    }


    private Beans() {

        super();
    }


}

