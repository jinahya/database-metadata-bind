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
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public final class ColumnRetriever {


    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(ColumnRetriever.class);


    public static <T> T retrieve(final Class<T> type, final T instance,
                                 final Suppression suppression,
                                 final ResultSet resultSet)
        throws SQLException {

//        final List<Field> collectionFields = new ArrayList<Field>();
        for (final Field field : type.getDeclaredFields()) {

            if (field.isSynthetic()) {
                continue;
            }

            final int modifiers = field.getModifiers();

            if (Modifier.isStatic(modifiers)) {
                continue;
            }

            if (field.getAnnotation(XmlTransient.class) != null) {
                continue;
            }

            if (field.getAnnotation(NotUsed.class) != null) {
                continue;
            }

            final String columnLabel;
            {
                final ColumnLabel a = field.getAnnotation(ColumnLabel.class);
                if (a == null) {
                    LOGGER.debug("field without @ColumnLabel: {}#{}({})",
                                 type.getName(), field.getName(),
                                 field.getType());
                    continue;
                }
                columnLabel = a.value();
            }

            final String suppressionPath;
            {
                final SuppressionPath a
                    = field.getAnnotation(SuppressionPath.class);
                suppressionPath = a != null ? a.value() : null;
            }
            if (suppressionPath != null) {
                final SuppressionPathBuilder suppressionPathBuilder
                    = SuppressionPathBuilder.newInstance(field);
                final String expected
                    = suppressionPathBuilder.getSuppressionPath();
                assert expected.equals(suppressionPath) :
                    "suppressionPath missmatched; expected="
                    + expected + "; actual=" + suppressionPath
                    + "; in " + type.getName() + "#" + field.getName();
            }
            if (suppressionPath != null
                && suppression.isSuppressed(suppressionPath)) {
                LOGGER.debug("field suppressed: {}#{} by {}",
                             type.getName(), field.getName(), suppressionPath);
                continue;
            }

            if (Collection.class.isAssignableFrom(field.getType())) {
//                collectionFields.add(field);
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
                    field.set(instance,
                              (Boolean) resultSet.getObject(columnLabel));
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
                Object value = (Number) resultSet.getObject(columnLabel);
                if (value != null && !(value instanceof Short)) {
                    value = new Short(((Number) value).shortValue());
                }
                try {
                    field.set(instance, value);
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
                Object value = (Number) resultSet.getObject(columnLabel);
                if (value != null && !(value instanceof Integer)) {
                    value = new Integer(((Number) value).intValue());
                }
                try {
                    field.set(instance, value);
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
                Object value = (Number) resultSet.getObject(columnLabel);
                if (value != null && !(value instanceof Long)) {
                    value = new Long(((Number) value).longValue());
                }
                try {
                    field.set(instance, value);
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

            LOGGER.debug("field skipped: {}#{}({})", type.getName(),
                         field.getName(), field.getType());
        }

        return instance;
    }


    public static <T> T retrieve(final Class<T> type,
                                 final Suppression suppression,
                                 final ResultSet resultSet)
        throws SQLException {

        if (type == null) {
            throw new NullPointerException("type");
        }

        final T instance;
        try {
            instance = type.newInstance();
        } catch (final InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

        return retrieve(type, instance, suppression, resultSet);
    }


    private ColumnRetriever() {

        super();
    }


}

