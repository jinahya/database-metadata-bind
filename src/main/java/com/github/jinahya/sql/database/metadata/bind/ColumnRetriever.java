/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class ColumnRetriever {


    private static final Logger logger
        = getLogger(ColumnRetriever.class.getName());


    static <T> T single(final MetadataContext context, final ResultSet results,
                        final Class<T> type, final T instance)
        throws SQLException {

        for (final Field field : type.getDeclaredFields()) {
            if (field.getAnnotation(NotUsed.class) != null) {
                logger.log(Level.FINE, "@NotUsed: {0}", field);
                continue;
            }
            final String columnLabel = ColumnLabels.get(field);
            if (columnLabel == null) {
                logger.log(Level.FINE, "no @ColumnLabel: {0}", field);
                continue;
            }
            final String suppressionPath = SuppressionPaths.get(field, type);
            if (suppressionPath != null && context.suppressed(suppressionPath)) {
                logger.log(Level.FINE, "suppressed by {0}: {1}",
                           new Object[]{suppressionPath, field});
                continue;
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            final int columnIndex = results.findColumn(columnLabel);
            final Class<?> fieldType = field.getType();
            final boolean fieldPrimitive = fieldType.isPrimitive();
            final Object object = results.getObject(columnLabel);
            if (object == null && fieldPrimitive) {
                logger.log(Level.WARNING, "null returned: {0}", field);
            }
            if (Boolean.TYPE.equals(field.getType())) {
                final boolean value = results.getBoolean(columnLabel);
                try {
                    field.setBoolean(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Boolean.class.equals(field.getType())) {
                final Boolean value
                    = results.getObject(columnLabel, Boolean.class);
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Short.TYPE.equals(field.getType())) {
                final short value = results.getShort(columnLabel);
                try {
                    field.setShort(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Short.class.equals(field.getType())) {
                Object value = results.getObject(columnLabel);
                if (value != null && !(value instanceof Short)) {
                    logger.log(Level.INFO, "casting {0}({1}) for {2}",
                               new Object[]{value.getClass(), value, field});
                    if (value instanceof Number) {
                        value = ((Number) value).shortValue();
                        logger.log(Level.INFO, "casted: {0}", value);
                    }
                }
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Integer.TYPE.equals(field.getType())) {
                final int value = results.getInt(columnLabel);
                try {
                    field.setInt(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Integer.class.equals(field.getType())) {
                Object value = results.getObject(columnLabel);
                if (value != null && !(value instanceof Integer)) {
                    logger.log(Level.INFO, "casting {0}({1}) for {2}",
                               new Object[]{value.getClass(), value, field});
                    if (value instanceof Number) {
                        value = ((Number) value).intValue();
                        logger.log(Level.INFO, "casted: ({0})", value);
                    }
                }
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Long.TYPE.equals(field.getType())) {
                final long value = results.getLong(columnLabel);
                try {
                    field.setLong(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Long.class.equals(field.getType())) {
                Object value = results.getObject(columnLabel);
                if (value != null && !(value instanceof Integer)) {
                    logger.log(Level.INFO, "casting {0}({1}) for {2}",
                               new Object[]{value.getClass(), value, field});
                    if (value instanceof Number) {
                        value = ((Number) value).longValue();
                        logger.log(Level.INFO, "casted: {0}", value);
                    }
                }
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (String.class.equals(field.getType())) {
                final String value = results.getString(columnLabel);
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
        }

        return instance;
    }


    static <T> T single(final MetadataContext context, final ResultSet results,
                        final Class<T> type)
        throws SQLException {

        final T instance;
        try {
            instance = type.newInstance();
        } catch (final InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

        return single(context, results, type, instance);
    }


    static <T> List<T> list(final MetadataContext context,
                            final ResultSet results, final Class<T> type,
                            final List<T> list)
        throws SQLException {

        while (results.next()) {
            final T instance;
            try {
                instance = type.newInstance();
            } catch (final InstantiationException ie) {
                throw new RuntimeException(ie);
            } catch (final IllegalAccessException iae) {
                throw new RuntimeException(iae);
            }
            list.add(single(context, results, type, instance));
        }

        return list;
    }


    static <T> List<T> list(final MetadataContext context,
                            final ResultSet results, final Class<T> type)
        throws SQLException {

        return list(context, results, type, new ArrayList<T>());
    }


    private ColumnRetriever() {

        super();
    }


}

