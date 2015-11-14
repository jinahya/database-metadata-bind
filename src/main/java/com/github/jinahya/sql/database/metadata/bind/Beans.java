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
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 * A utility class for {@code java.bean} package.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Beans {


    private static final Logger logger = getLogger(Beans.class.getName());


    /**
     * Returns a list of {@code PropertyDescriptor} which each is annotated with
     * specified.
     *
     * @param beanClass bean class
     * @param annotationClass annotation class
     *
     * @return a list of {@code PropertyDescriptor}
     *
     * @throws IntrospectionException if introspection error occurs.
     */
    static List<PropertyDescriptor> getPropertyDescriptors(
        final Class<?> beanClass,
        final Class<? extends Annotation> annotationClass)
        throws IntrospectionException {

        final BeanInfo info = Introspector.getBeanInfo(beanClass);
        final PropertyDescriptor[] array = info.getPropertyDescriptors();
        if (array == null) {
            return emptyList();
        }

        final List<PropertyDescriptor> list
            = new ArrayList<PropertyDescriptor>(array.length);
        for (final PropertyDescriptor descriptor : array) {
            if (Annotations.getAnnotation(
                annotationClass, descriptor, beanClass) == null) {
                continue;
            }
            list.add(descriptor);
        }

        return list;
    }


    /**
     * Returns the value of specified property.
     *
     * @param propertyDescriptor property descriptor.
     * @param beanInstance bean instance.
     *
     * @return the value of property.
     *
     * @throws ReflectiveOperationException if a reflection error occurs.
     */
    static Object getPropertyValue(final PropertyDescriptor propertyDescriptor,
                                   final Object beanInstance)
        throws ReflectiveOperationException {

        final Method readMethod = propertyDescriptor.getReadMethod();
        if (readMethod != null) {
            return readMethod.invoke(beanInstance);
        }

        logger.log(Level.WARNING, "accessing field directly: {0}",
                   new Object[]{propertyDescriptor});

        return Reflections.getFieldValueHelper(beanInstance.getClass(),
                                               propertyDescriptor.getName(),
                                               beanInstance);
    }


    /**
     * Introspect a property with specified property name and returns the value
     * of the property.
     *
     * @param beanClass bean class
     * @param propertyName property name
     * @param beanInstance bean instance
     *
     * @return the value of the property.
     *
     * @throws IntrospectionException if failed to introspect the property with
     * {@code propertyName}.
     * @throws ReflectiveOperationException if a reflection error occurs.
     * @see #getPropertyValue(java.beans.PropertyDescriptor, java.lang.Object)
     */
    static Object getPropertyValue(final Class<?> beanClass,
                                   final String propertyName,
                                   final Object beanInstance)
        throws IntrospectionException, ReflectiveOperationException {

        final PropertyDescriptor propertyDescriptor
            = new PropertyDescriptor(propertyName, beanClass);

        return getPropertyValue(propertyDescriptor, beanInstance);
    }


    /**
     * Sets a property value.
     *
     * @param propertyDescriptor property descriptor
     * @param beanInstance bean instance
     * @param propertyValue the property value to set
     *
     * @throws ReflectiveOperationException
     */
    @SuppressWarnings("unchecked")
    static void setPropertyValue(
        final PropertyDescriptor propertyDescriptor, final Object beanInstance,
        Object propertyValue)
        throws ReflectiveOperationException {

        final Class<?> propertyType = propertyDescriptor.getPropertyType();
        if (propertyType == null) {
            throw new RuntimeException(
                "type cannot be determined: " + propertyDescriptor);
        }
        propertyValue = Values.adapt(
            propertyType, propertyValue, propertyDescriptor);

        final Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod != null) {
            writeMethod.invoke(beanInstance, propertyValue);
            return;
        }

        if (Collection.class.isAssignableFrom(propertyType)
            && Collection.class.isInstance(propertyValue)) {
            final Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                final Collection<Object> collectionValue
                    = (Collection<Object>) readMethod.invoke(beanInstance);
                if (collectionValue != null) {
                    collectionValue.addAll(
                        (Collection<? extends Object>) propertyValue);
                }
                return;
            }
        }

        logger.log(Level.WARNING, "accessing field directly: {0}",
                   new Object[]{propertyDescriptor});

        Reflections.setFieldValueHelper(beanInstance.getClass(),
                                        propertyDescriptor.getName(),
                                        beanInstance, propertyValue);
    }


    /**
     * Finds a property whose name matches to given and sets with given value.
     *
     * @param beanClass bean class
     * @param propertyName property name
     * @param beanInstance bean instance
     * @param propertyValue property value
     *
     * @throws IntrospectionException if failed to introspect
     * @throws ReflectiveOperationException if a reflection error occurs.
     * @see #setPropertyValue(java.beans.PropertyDescriptor, java.lang.Object,
     * java.lang.Object)
     */
    static void setPropertyValue(final Class<?> beanClass,
                                 final String propertyName,
                                 final Object beanInstance,
                                 final Object propertyValue)
        throws IntrospectionException, ReflectiveOperationException {

        final PropertyDescriptor propertyDescriptor
            = new PropertyDescriptor(propertyName, beanClass);

        setPropertyValue(propertyDescriptor, beanInstance, propertyValue);
    }


    private Beans() {

        super();
    }


}

