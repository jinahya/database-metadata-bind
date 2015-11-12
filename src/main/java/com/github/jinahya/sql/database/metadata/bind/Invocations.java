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


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;


/**
 * A utility class for {@link Invocation}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Invocations {


    /**
     * Find {@link Invocation} value from specified property.
     *
     * @param propertyDescriptor property descriptor
     * @param beanClass bean class
     *
     * @return an instance of {@link Invocation} or {@code null} if not found.
     */
    static Invocation get(final PropertyDescriptor propertyDescriptor,
                          final Class<?> beanClass) {

        return Annotations.getAnnotation(
            Invocation.class, propertyDescriptor, beanClass);
    }


    static <T> Object[] values(final Class<T> beanClass, final T beanInstance,
                               final Class<?>[] types, final String[] names)
        throws IntrospectionException, ReflectiveOperationException {

        final Object[] values = new Object[names.length];
        for (int i = 0; i < names.length; i++) {
            if ("null".equals(names[i])) {
                values[i] = null;
                continue;
            }
            if (names[i].startsWith(":")) {
                values[i] = Beans.getPropertyValue(
                    beanClass, names[i].substring(1), beanInstance);
                continue;
            }
            if (types[i] == String.class) {
                values[i] = names[i];
                continue;
            }
            if (types[i].isPrimitive()) {
                types[i] = Reflections.wrapper(types[i]);
            }
            values[i] = types[i]
                .getMethod("valueOf", String.class)
                .invoke(null, names[i]);
        }

        return values;
    }


    private Invocations() {

        super();

    }


}

