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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Reflections {


    private static final Logger logger = getLogger(Metadata.class.getName());


    static Object fieldValue(final Field field, final Object obj) {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        try {
            return field.get(obj);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
    }


    static void fieldValue(final Field field, final Object obj, Object value)
        throws ReflectiveOperationException {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        final Class<?> fieldType = field.getType();
        if (fieldType.isInstance(value)) {
            field.set(obj, value);
            return;
        }
        if (Boolean.TYPE.equals(field.getType())) {
            if (value == null || !Boolean.TYPE.isInstance(value)) {
                logger.log(Level.WARNING, "can't set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            field.setBoolean(obj, (Boolean) value);
            return;
        }
        if (Boolean.class.equals(field.getType())) {
            field.set(obj, value);
            return;
        }
        if (Short.TYPE.equals(field.getType())) {
            if (value == null || !Short.TYPE.isInstance(value)) {
                logger.log(Level.WARNING, "can't set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            field.setShort(obj, (Short) value);
            return;
        }
        if (Short.class.equals(field.getType())) {
            if (value != null && !Short.class.isInstance(value)) {
                logger.log(Level.INFO, "casting {0}({1}) for {2}",
                           new Object[]{value.getClass(), value, field});
                if (value instanceof Number) {
                    value = ((Number) value).shortValue();
                    logger.log(Level.INFO, "casted: {0}", value);
                }
            }
            field.set(obj, value);
            return;
        }
        if (Integer.TYPE.equals(field.getType())) {
            if (value == null || !Integer.TYPE.isInstance(value)) {
                logger.log(Level.WARNING, "can't set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            field.setInt(obj, (Integer) value);
        }
        if (Integer.class.equals(field.getType())) {
            if (value != null && !(value instanceof Integer)) {
                logger.log(Level.INFO, "casting {0}({1}) for {2}",
                           new Object[]{value.getClass(), value, field});
                if (value instanceof Number) {
                    value = ((Number) value).intValue();
                    logger.log(Level.INFO, "casted: ({0})", value);
                }
            }
            field.set(obj, value);
            return;
        }
        if (Long.TYPE.equals(field.getType())) {
            if (value == null || !Long.TYPE.isInstance(value)) {
                logger.log(Level.WARNING, "can't set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            field.setLong(obj, (Long) value);
            return;
        }
        if (Long.class.equals(field.getType())) {
            if (value != null && !(value instanceof Integer)) {
                logger.log(Level.INFO, "casting {0}({1}) for {2}",
                           new Object[]{value.getClass(), value, field});
                if (value instanceof Number) {
                    value = ((Number) value).longValue();
                    logger.log(Level.INFO, "casted: {0}", value);
                }
            }
            field.set(obj, value);
            return;
        }
        if (String.class.equals(field.getType())) {
            field.set(obj, value);
            return;
        }
        if (List.class.equals(field.getType())) {
            if (List.class.isInstance(value)) {
                field.set(obj, value);
            }
            if (!ResultSet.class.isInstance(value)) {
                logger.log(Level.WARNING, "can't set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            final Type type = field.getGenericType();
            if (!ParameterizedType.class.isInstance(type)) {
                logger.log(Level.WARNING, "not a ParameterizedType({0}): {1}",
                           new Object[]{type, field});
            }
            final Type firstType
                = ((ParameterizedType) type).getActualTypeArguments()[0];
            final String firstTypeName = firstType.getTypeName();
            final Class<?> typeClass = Class.forName(firstTypeName);
        }

        field.set(obj, value);
    }


    private Reflections() {

        super();
    }


}

