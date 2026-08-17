package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The parent interface for binding database metadata types.
 * <p>
 * Implementations are {@link Serializable}, with one deliberate exclusion: the
 * {@link #getUnknownColumns() unknown columns} of an instance are <em>not</em> part of its serialized form. See
 * {@link #getUnknownColumns()} for why.
 * <p>
 * Implementations are <strong>not thread-safe</strong>, and are not intended to be. An instance is a mutable holder
 * that the binder populates field by field after construction; only the binder writes, and it does so from the single
 * thread that walks the result set. Publishing an instance to other threads is the caller's responsibility, and must
 * happen after binding completes and through a safe-publication mechanism of the caller's choosing - handing the
 * instance over via a {@code synchronized} block, a {@code volatile} field, a concurrent collection, or the
 * happens-before edge of the executor that produced it. Once published this way an instance is effectively immutable,
 * since nothing outside the package can mutate it: setters are package-private and
 * {@link #getUnknownColumns()} returns an unmodifiable view.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public interface MetadataType
        extends Serializable {

    /**
     * Returns the value bound to the column of the specified label, of the specified type.
     * <p>
     * The value is first looked up among the {@link _ColumnLabel}-annotated fields of this instance's runtime class
     * (and its superclasses); if no field is mapped to the specified label, the value is looked up in
     * {@link #getUnknownColumns() unknown columns}.
     * <p>
     * The {@code label} is matched <em>case-insensitively</em>, by
     * {@linkplain String#toUpperCase(Locale) upper-casing} it with {@link Locale#ROOT}, which is
     * how the labels a driver reports are normalized while binding. Both lookups apply that same normalization, so
     * {@code getValue(String.class, "table_cat")} and {@code getValue(String.class, "TABLE_CAT")} are equivalent.
     *
     * @param type  the type of the value.
     * @param label the column label whose bound value is returned.
     * @param <T>   value type parameter.
     * @return an {@link Optional} of the value bound to the {@code label}; an {@link Optional#empty() empty} optional
     * if no value is mapped to the {@code label}, or the mapped value is {@code null}.
     * @throws ClassCastException if the mapped value is not assignable to the specified {@code type}.
     */
    default <T> Optional<T> getValue(final Class<T> type, final String label) {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(label, "label is null");
        final var normalized = label.toUpperCase(Locale.ROOT);
        final var field = ContextUtils.getLabeledFields(getClass()).get(normalized);
        if (field != null) {
            final Object value;
            try {
                value = field.get(this);
            } catch (final IllegalAccessException iae) {
                throw new RuntimeException("failed to get value of " + field + " from " + this, iae);
            }
            return Optional.ofNullable(value).map(type::cast);
        }
        return Optional.ofNullable(getUnknownColumns().get(normalized)).map(type::cast);
    }

    /**
     * Returns an unmodifiable view of unknown columns and values.
     * <p>
     * An <em>unknown column</em> is a column present in the driver's result set for which this type declares no
     * {@link _ColumnLabel}-annotated field, that is, a vendor extension beyond the columns the JDBC specification
     * defines for the corresponding {@link java.sql.DatabaseMetaData} method. Values are exactly what
     * {@link java.sql.ResultSet#getObject(String)} returned, so their runtime types are constrained only by the
     * driver.
     * <p>
     * These entries are deliberately excluded from the serialized form of an instance, as they are from the XML and
     * JSON bindings. {@link java.sql.ResultSet#getObject(String)} is permitted to return values that do not implement
     * {@link Serializable} at all - {@link java.sql.Array}, {@link java.sql.Blob}, {@link java.sql.Clob},
     * {@link java.sql.Ref}, {@link java.sql.RowId}, {@link java.sql.SQLXML}, and {@link java.sql.Struct} are each
     * declared without it - and Java serialization aborts an entire object graph on the first such value rather than
     * skipping the offending entry. Retaining unknown columns would therefore make serializing any metadata type
     * succeed or fail depending on which driver produced it. Read whatever is needed from this map before
     * serializing.
     * <p>
     * Consequently, an instance restored by deserialization has no unknown columns; this method returns an empty map
     * rather than {@code null}.
     * <p>
     * For the same reason, unknown columns are omitted from {@link Object#toString() toString()}, which renders only
     * the {@link _ColumnLabel}-annotated fields. This method is the only access point: rendering a value the driver
     * chose would mean calling {@code toString()} on a locator that has no meaningful contract for it, that may no
     * longer be valid, and that may hold an unbounded amount of text.
     *
     * @return an unmodifiable view of unknown columns and values.
     */
    Map<String, Object> getUnknownColumns();
}
