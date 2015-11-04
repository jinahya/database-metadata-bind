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


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class ColumnRetriever {


    static <T> T retrieve(final MetadataContext context,
                          final Class<T> type, final T instance,
                          final ResultSet resultSet)
        throws SQLException {

        for (final Field field : type.getDeclaredFields()) {

            if (field.getAnnotation(NotUsed.class) != null) {
                continue;
            }

            final String columnLabel = ColumnLabels.get(field);
            if (columnLabel == null) {
                continue;
            }
            //System.out.println("columnLabel: " + columnLabel);

            final String suppressionPath = SuppressionPaths.get(field, type);
            if (suppressionPath != null
                && context.suppressed(suppressionPath)) {
                continue;
            }

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            if (Boolean.TYPE.equals(field.getType())) {
                try {
                    field.setBoolean(
                        instance, resultSet.getBoolean(columnLabel));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }

            if (Boolean.class.equals(field.getType())) {
                try {
                    field.set(
                        instance, resultSet.getObject(
                            columnLabel, Boolean.class));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }

            if (Short.TYPE.equals(field.getType())) {
                try {
                    field.setShort(instance, resultSet.getShort(columnLabel));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }

            if (Short.class.equals(field.getType())) {
                try {
                    field.set(instance,
                              resultSet.getObject(columnLabel, Short.class));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }

            if (Integer.TYPE.equals(field.getType())) {
                try {
                    field.setInt(instance, resultSet.getInt(columnLabel));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }

            if (Integer.class.equals(field.getType())) {
                try {
                    field.set(instance, resultSet.getObject(columnLabel));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }

            if (Long.TYPE.equals(field.getType())) {
                try {
                    field.setLong(instance, resultSet.getLong(columnLabel));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }

            if (Long.class.equals(field.getType())) {
                try {
                    field.set(instance, resultSet.getObject(columnLabel));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }

            if (String.class.equals(field.getType())) {
                try {
                    field.set(instance, resultSet.getString(columnLabel));
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
        }

        return instance;
    }


    public static <T> T retrieve(final MetadataContext context,
                                 final Class<T> type, final ResultSet resultSet)
        throws SQLException {

        if (type == null) {
            throw new NullPointerException("null type");
        }

        final T instance;
        try {
            instance = type.newInstance();
        } catch (final InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

        return retrieve(context, type, instance, resultSet);
    }


    private ColumnRetriever() {

        super();
    }


}

