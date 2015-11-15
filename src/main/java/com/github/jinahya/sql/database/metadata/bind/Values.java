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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Values {


    private static final Logger LOGGER = getLogger(Metadata.class.getName());


    static Object get(final Field field, final Object obj)
        throws ReflectiveOperationException {

        try {
            final PropertyDescriptor descriptor = new PropertyDescriptor(
                field.getName(), field.getDeclaringClass());
            final Method reader = descriptor.getReadMethod();
            if (reader != null) {
                return reader.invoke(obj);
            }
        } catch (final IntrospectionException ie) {
            ie.printStackTrace(System.err);
            LOGGER.log(Level.WARNING,
                       "failed to create property descriptor for {0}",
                       new Object[]{field});
        }

        LOGGER.log(Level.WARNING, "getting value directly from {0}",
                   new Object[]{field});
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        return field.get(obj);
    }


    static void set(final Field field, final Object obj, final Object value)
        throws ReflectiveOperationException {

        try {
            final PropertyDescriptor descriptor = new PropertyDescriptor(
                field.getName(), field.getDeclaringClass());
            final Method writer = descriptor.getWriteMethod();
            if (writer != null) {
                writer.invoke(obj, adapt(descriptor, value));
                return;
            }
        } catch (final IntrospectionException ie) {
            ie.printStackTrace(System.err);
            LOGGER.log(Level.WARNING,
                       "failed to create property descriptor for {0}",
                       new Object[]{field});
        }

        LOGGER.log(Level.WARNING, "setting value directly to {0} with {1}",
                   new Object[]{field, value});
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(obj, adapt(field, value));
    }


    static void setParent(final Class<?> type, final Iterable<?> children,
                          final Object parent)
        throws ReflectiveOperationException {

        if (!Child.class.isAssignableFrom(type)) {
            LOGGER.log(Level.WARNING, "type({0}) is not assignable to {1}",
                       new Object[]{type, Child.class});
            return;
        }

        final Method method = type.getMethod("setParent", Object.class);
        for (final Object childBean : children) {
            method.invoke(childBean, parent);
        }
    }


    static Object adapt(final Class<?> type, final Object value,
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
                LOGGER.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
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
                LOGGER.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return Boolean.FALSE;
            }
            return value;
        }

        if (Short.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                LOGGER.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
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
                LOGGER.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return null;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).shortValue();
        }

        if (Integer.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                LOGGER.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return 0;
            }
            if (Integer.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).intValue();
        }

        if (Integer.class.equals(type)) {
            if (value != null && !Number.class.isInstance(value)) {
                LOGGER.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
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
                LOGGER.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
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
                LOGGER.log(Level.WARNING, "cannot adapt {0}({1}) for {2}",
                           new Object[]{value, valueType, target});
                return null;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).longValue();
        }

        LOGGER.log(Level.WARNING, "unadapted value={0}({1}), field={2}",
                   new Object[]{value, valueType, target});

        return value;
    }


    static Object adapt(final PropertyDescriptor descriptor, final Object value) {

        return adapt(descriptor.getPropertyType(), value, descriptor);
    }


    static Object adapt(final Field field, final Object value) {

        return adapt(field.getType(), value, field);
    }


    private Values() {

        super();
    }


}

