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

import org.jspecify.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class of utilities for binding {@link java.sql.DatabaseMetaData} result sets to metadata types.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class ContextUtils {

    /**
     * A cache mapping each class to its {@link #getLabeledFields(Class) labeled fields}.
     */
    private static final Map<Class<?>, Map<String, Field>> CLASSES_AND_LABELED_FIELDS = new ConcurrentHashMap<>();

    /**
     * Returns a map of all declared fields of the specified class (and its superclasses) that are annotated with
     * {@link _ColumnLabel}, each mapped to its annotation.
     * <p>
     * Every matching field is made {@linkplain Field#setAccessible(boolean) accessible} so that its value can later be
     * read or written reflectively. The search walks up through superclasses.
     *
     * @param c the class whose declared fields, along with those of its superclasses, are inspected.
     * @return a new map of matching fields and their {@link _ColumnLabel} annotations; may be empty but never
     * {@code null}.
     */
    @SuppressWarnings({
            "java:S3011" // setAccessible
    })
    static Map<Field, _ColumnLabel> getBindingFields(final Class<?> c) {
        // this method reads _ColumnLabel off fields only, which is sound exactly while the annotation stays
        // FIELD-targeted. Kept as a single assert expression: Target#value() returns a defensively cloned array on
        // every call, so hoisting it into a local would allocate on every invocation even with assertions disabled.
        assert Arrays.equals(_ColumnLabel.class.getAnnotation(Target.class).value(),
                             new ElementType[] {ElementType.FIELD})
                : "@_ColumnLabel is no longer FIELD-only: "
                  + Arrays.toString(_ColumnLabel.class.getAnnotation(Target.class).value());
        final Map<Field, _ColumnLabel> fields = new HashMap<>();
        for (Class<?> k = c; k != null; k = k.getSuperclass()) {
            for (final Field field : k.getDeclaredFields()) {
                final _ColumnLabel label = field.getAnnotation(_ColumnLabel.class);
                if (label == null) {
                    continue;
                }
                field.setAccessible(true);
                final var previous = fields.put(field, label);
                assert previous == null;
            }
        }
        return fields;
    }

    /**
     * Returns the {@link _ColumnLabel}-annotated fields of the specified class (and its superclasses), indexed by
     * column label, computing and caching the index on the first request for each class.
     * <p>
     * Labels are {@linkplain String#toUpperCase(Locale) upper-cased} with {@link Locale#ROOT}, exactly as
     * {@link #getLabels(ResultSet)} normalizes the labels a driver reports, so that a field is looked up here by the
     * same key under which its column arrives.
     *
     * @param c the class whose labeled fields are returned.
     * @return an unmodifiable map of upper-cased column labels and their
     * {@linkplain Field#setAccessible(boolean) accessible} fields; may be empty but never {@code null}.
     * @see #getBindingFields(Class)
     */
    static Map<String, Field> getLabeledFields(final Class<?> c) {
        Objects.requireNonNull(c, "c is null");
        return CLASSES_AND_LABELED_FIELDS.computeIfAbsent(c, k -> {
            final Map<String, Field> labeledFields = new HashMap<>();
            getBindingFields(k).forEach((field, label) -> {
                final var previous = labeledFields.put(label.value().toUpperCase(Locale.ROOT), field);
                assert previous == null : "duplicate label: " + label.value() + "; " + field + "; " + previous;
            });
            return Map.copyOf(labeledFields);
        });
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
     * The value is first read with {@link ResultSet#getObject(String)} and assigned directly when it is {@code null}
     * or an instance of the field's type. Otherwise a series of coercion attempts are made: the value is re-read using
     * a type-specific accessor ({@link ResultSet#getBoolean(String)}, {@link ResultSet#getShort(String)},
     * {@link ResultSet#getInt(String)}, or {@link ResultSet#getLong(String)}) matching the field type, and finally
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
        assert field != null;
        assert obj != null;
        assert results != null;
        assert label != null;
//        Objects.requireNonNull(field, "field is null");
//        Objects.requireNonNull(obj, "obj is null");
//        Objects.requireNonNull(results, "results is null");
//        Objects.requireNonNull(label, "label is null");
        assert field.canAccess(obj);
        final Class<?> fieldType = field.getType();
        assert !fieldType.isPrimitive();
        final Object value = results.getObject(label);
        // Field#set throws IllegalArgumentException exactly when the value is not assignable to the field type, and
        // fieldType is guaranteed non-primitive here, so isInstance is that same test asked in advance. Asking it
        // instead of catching keeps a column whose driver type never matches the field type - a Short for an Integer
        // field, say - from throwing and catching one exception per row for the lifetime of the result set. A null
        // value is always assignable to a reference field, so it is admitted here and leaves the field null.
        if (value == null || fieldType.isInstance(value)) {
            field.set(obj, value);
            return;
        }
        // Past this point getObject() proved the column is non-NULL, so the primitive accessors below
        // (getBoolean/getShort/getInt/getLong) cannot return a NULL-coerced default; wasNull() is unnecessary.
        // Keep this guard before any primitive read to avoid the classic NULL-as-0/false ambiguity.
        // The value is not assignable to the field, so let's try to coerce the type
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
        } catch (final Exception e) {
            // All attempts have failed; wrap as a coercion failure with the underlying cause preserved.
            // Note: getObject(label, type) reports an unsupported conversion as an SQLException, so any
            // SQLException here is treated as a coercion failure rather than propagated.
            throw new RuntimeException(
                    String.format("failed to set; label: %s, value: %s (%s), field: %s",
                                  label, value, value.getClass().getName(), field),
                    e);
        }
    }

    /**
     * Returns a null-safe variant of the specified comparator whose handling of {@code null} elements reflects how the
     * underlying database sorts {@code null} values for the specified sort direction.
     * <p>
     * {@link java.sql.DatabaseMetaData#nullsAreSortedAtStart()} and
     * {@link java.sql.DatabaseMetaData#nullsAreSortedAtEnd()} describe absolute result positions, so they are applied
     * independently of {@code direction}. {@link java.sql.DatabaseMetaData#nullsAreSortedLow()} and
     * {@link java.sql.DatabaseMetaData#nullsAreSortedHigh()} describe the value domain, so their result position
     * depends on {@code direction}.
     *
     * @param context    the context whose metadata determines the {@code null} ordering.
     * @param comparator the comparator to wrap.
     * @param direction  the direction in which the specified comparator orders non-{@code null} values.
     * @param <T>        the type of elements compared.
     * @return a null-safe comparator wrapping the specified comparator.
     * @throws SQLException if a database error occurs while querying the {@code null} ordering.
     */
    static <T> Comparator<@Nullable T> withDatabaseNullOrdering(final Context context,
                                                                final Comparator<? super T> comparator,
                                                                final ContextConstants.SortDirection direction)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(direction, "direction is null");
        if (context.metadata.nullsAreSortedAtStart()) {
            return Comparator.nullsFirst(comparator);
        }
        if (context.metadata.nullsAreSortedAtEnd()) {
            return Comparator.nullsLast(comparator);
        }
        if (context.metadata.nullsAreSortedLow()) {
            return direction == ContextConstants.SortDirection.ASCENDING
                   ? Comparator.nullsFirst(comparator)
                   : Comparator.nullsLast(comparator);
        }
        if (context.metadata.nullsAreSortedHigh()) {
            return direction == ContextConstants.SortDirection.ASCENDING
                   ? Comparator.nullsLast(comparator)
                   : Comparator.nullsFirst(comparator);
        }
        return Comparator.nullsLast(comparator);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns all public static final {@code int} values declared in {@link Types}.
     *
     * @return all public static final {@code int} values declared in {@link Types}.
     */
    static int[] javaSqlTypesValues() {
        final Field[] fields = Types.class.getFields();
        final int[] values = new int[fields.length];
        int count = 0;
        for (final Field field : fields) {
            if (field.getDeclaringClass() != Types.class) {
                continue;
            }
            if (field.getType() != int.class) {
                continue;
            }
            final int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers) || !Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)) {
                continue;
            }
            try {
                values[count++] = field.getInt(null);
            } catch (final IllegalAccessException iae) {
                throw new AssertionError("failed to read " + field, iae);
            }
        }
        final int[] result = Arrays.copyOf(values, count);
        Arrays.sort(result);
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ContextUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
