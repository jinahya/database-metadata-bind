package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A class of utilities for binding {@link java.sql.DatabaseMetaData} result sets to metadata types.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class ContextUtils {

    private static final System.Logger logger = System.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Collects, into the specified map, all declared fields of the specified class (and its superclasses) annotated
     * with the specified annotation type, mapping each field to its annotation value.
     * <p>
     * Each matching non-enum-constant field is made {@linkplain Field#setAccessible(boolean) accessible} so that its
     * value can later be read or written reflectively. The search recurses into superclasses.
     *
     * @param c   the class whose declared fields, along with those of its superclasses, are inspected.
     * @param a   the annotation type to look for.
     * @param m   the map into which matching fields and their annotation values are put.
     * @param <T> annotation type parameter
     * @return the specified map, with matching fields added.
     */
    @SuppressWarnings({
            "java:S3011" // setAccessible
    })
    private static <T extends Annotation> Map<Field, T> getFieldsAnnotatedWith(
            final Class<?> c, final Class<T> a, final Map<Field, T> m) {
        for (final Field field : c.getDeclaredFields()) {
            final T value = field.getAnnotation(a);
            if (value == null) {
                continue;
            }
            if (!field.isEnumConstant()) {
                field.setAccessible(true);
            }
            m.put(field, value);
        }
        final Class<?> superclass = c.getSuperclass();
        return superclass == null ? m : getFieldsAnnotatedWith(superclass, a, m);
    }

    /**
     * Returns a map of all declared fields of the specified class (and its superclasses) annotated with the specified
     * annotation type, each mapped to its annotation value.
     *
     * @param c   the class whose declared fields, along with those of its superclasses, are inspected.
     * @param a   the annotation type to look for.
     * @param <T> annotation type parameter
     * @return a new map of matching fields and their annotation values; may be empty but never {@code null}.
     */
    static <T extends Annotation> Map<Field, T> getFieldsAnnotatedWith(final Class<?> c, final Class<T> a) {
        return getFieldsAnnotatedWith(c, a, new HashMap<>());
    }

    /**
     * Returns a set of column labels of given result set.
     *
     * @param results the result set from which column labels are read.
     * @return a set of column labels
     * @throws SQLException if a database error occurs.
     * @see ResultSet#getMetaData()
     */
    static Set<String> getLabels(final ResultSet results) throws SQLException {
        final ResultSetMetaData metadata = results.getMetaData();
        final int count = metadata.getColumnCount();
        final Set<String> labels = new HashSet<>(count);
        for (int i = 1; i <= count; i++) {
            labels.add(metadata.getColumnLabel(i).toUpperCase(Locale.ROOT));
        }
        return labels;
    }

    /**
     * Reads the value of the specified column from the specified result set and sets it to the specified field of the
     * specified object.
     * <p>
     * The value is first read with {@link ResultSet#getObject(String)} and a direct assignment is attempted. When the
     * read value is not directly assignable to the field's type, a series of coercion attempts are made: a {@code null}
     * value is left unset; otherwise the value is re-read using a type-specific accessor
     * ({@link ResultSet#getBoolean(String)}, {@link ResultSet#getShort(String)}, {@link ResultSet#getInt(String)}, or
     * {@link ResultSet#getLong(String)}) matching the field type, and finally
     * {@link ResultSet#getObject(String, Class)} is tried. If all coercion attempts fail, a {@link RuntimeException} is
     * thrown.
     *
     * @param field   the field to set; must be accessible on {@code obj} and of a non-primitive type.
     * @param obj     the object whose field is set.
     * @param results the result set from which the value is read.
     * @param label   the label of the column to read.
     * @throws SQLException                 if a database error occurs.
     * @throws ReflectiveOperationException if setting the field reflectively fails.
     * @throws RuntimeException             if the value can neither be assigned directly nor coerced to the field
     *                                      type.
     */
    @SuppressWarnings({
            "java:S3011" // accessibility bypass
    })
    static void setFieldValue(final Field field, final Object obj, final ResultSet results, final String label)
            throws SQLException, ReflectiveOperationException {
        Objects.requireNonNull(field, "field is null");
        Objects.requireNonNull(obj, "obj is null");
        Objects.requireNonNull(results, "results is null");
        Objects.requireNonNull(label, "label is null");
        assert field.canAccess(obj);
        final Class<?> fieldType = field.getType();
        assert !fieldType.isPrimitive();
        final Object value = results.getObject(label);
        try {
            field.set(obj, value);
            return;
        } catch (final IllegalArgumentException iae) {
            // This block is reached if getObject() returns a type that is not directly assignable
            // to the field type (e.g., a Short for an Integer field).
            // We can now try to perform a conversion.
        }
        if (value == null) {
            return;
        }
        // The initial assignment failed, so let's try to coerce the type
        // by asking the JDBC driver to do the conversion for us.
        if (fieldType == Boolean.class) {
            field.set(obj, results.getBoolean(label));
            return;
        }
        if (fieldType == Short.class) {
            field.set(obj, results.getShort(label));
            return;
        }
        if (fieldType == Integer.class) {
            field.set(obj, results.getInt(label));
            return;
        }
        if (fieldType == Long.class) {
            field.set(obj, results.getLong(label));
            return;
        }
        // As a last resort, try the modern getObject(label, type) method.
        try {
            field.set(obj, results.getObject(label, fieldType));
            return;
        } catch (final Exception e) {
            // empty
        }
        // If we've reached this point, all attempts have failed.
        logger.log(
                System.Logger.Level.ERROR,
                () -> String.format("failed to set; label: %s, value: %s (%s), field: %s",
                                    label, value, value.getClass().getName(), field)
        );
        throw new RuntimeException("failed to set " + value + " for " + field);
    }

    /**
     * Returns a null-safe variant of the specified comparator whose handling of {@code null} elements reflects how the
     * underlying database sorts {@code null} values.
     * <p>
     * When the database {@linkplain java.sql.DatabaseMetaData#nullsAreSortedAtStart() sorts nulls at the start} or
     * {@linkplain java.sql.DatabaseMetaData#nullsAreSortedLow() sorts nulls low}, the result orders {@code null} first
     * ({@link Comparator#nullsFirst(Comparator)}); otherwise it orders {@code null} last
     * ({@link Comparator#nullsLast(Comparator)}).
     *
     * @param context    the context whose metadata determines the {@code null} ordering.
     * @param comparator the comparator to wrap.
     * @param <T>        the type of elements compared.
     * @return a null-safe comparator wrapping the specified comparator.
     * @throws SQLException if a database error occurs while querying the {@code null} ordering.
     */
    public static <T> Comparator<T> nullOrdered(final Context context, final Comparator<? super T> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        if (context.metadata.nullsAreSortedAtStart() || context.metadata.nullsAreSortedLow()) {
            return Comparator.nullsFirst(comparator);
        } else {
            return Comparator.nullsLast(comparator);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ContextUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
