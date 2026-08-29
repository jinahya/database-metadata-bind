package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.entry;

@Slf4j
abstract class AbstractMetadataType_Test<T extends AbstractMetadataType>
        extends MetadataType_Test<T> {

    private static final Pattern FIELD_NAME_IN_TO_STRING_PATTERN = Pattern.compile("\\b([A-Za-z][A-Za-z0-9_]*)=");

    AbstractMetadataType_Test(final Class<T> typeClass) {
        super(typeClass);
    }

    /**
     * Verifies that {@link AbstractMetadataType#unknownColumns} is created by the constructor, rather than on first
     * use. The eager map is what removes the check-then-act that a lazy accessor would otherwise perform on an
     * unsynchronized field.
     */
    @Test
    void unknownColumns_InitializedEagerly_() {
        final var instance = newTypeInstance();
        assertThat(instance.unknownColumns)
                .as("backing unknownColumns of a freshly constructed %s", typeClass.getSimpleName())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void getUnknownColumns_ReturnsUnmodifiableView_() {
        final var instance = newTypeInstance();
        final var label = "UNKNOWN_COLUMN";
        final var value = "UNKNOWN_VALUE";
        instance.putUnknownColumn(label, value);
        final var unknownColumns = instance.getUnknownColumns();
        assertThat(unknownColumns).containsEntry(label, value);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> unknownColumns.clear());
        assertThat(instance.getUnknownColumns()).containsEntry(label, value);
    }

    /**
     * Verifies the documented serialized form: {@link _ColumnLabel}-annotated fields survive a round-trip, while
     * {@link AbstractMetadataType#unknownColumns} is dropped because it is {@code transient}.
     * <p>
     * The non-serializable unknown value is the point of the test. {@link java.sql.ResultSet#getObject(String)} may
     * return values that do not implement {@link java.io.Serializable}, and Java serialization aborts the whole
     * object graph on the first such value; keeping the field {@code transient} is what makes serializing a metadata
     * type independent of which driver produced it.
     */
    @Test
    void serialization_KeepsLabeledFieldsAndDropsUnknownColumns_() throws Exception {
        final var instance = newTypeInstance();
        final Map<Field, String> expected = new LinkedHashMap<>();
        for (final var field : ContextUtils.getBindingFields(typeClass).keySet()) {
            if (field.getType() != String.class) {
                continue;
            }
            final var value = field.getName() + "-value";
            field.set(instance, value);
            expected.put(field, value);
        }
        instance.putUnknownColumn("UNKNOWN_SERIALIZABLE", "value");
        instance.putUnknownColumn("UNKNOWN_NOT_SERIALIZABLE", new Object());
        assertThat(instance.getUnknownColumns()).hasSize(2);

        final var deserialized = roundTrip(instance);

        for (final var entry : expected.entrySet()) {
            assertThat(entry.getKey().get(deserialized))
                    .as("%s.%s after deserialization", typeClass.getSimpleName(), entry.getKey().getName())
                    .isEqualTo(entry.getValue());
        }
        assertThat(deserialized.getUnknownColumns())
                .as("unknown columns of a deserialized %s", typeClass.getSimpleName())
                .isNotNull()
                .isEmpty();
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> deserialized.getUnknownColumns().put("K", "V"));

        // deserialization runs neither the constructor nor a transient field's restore, so the backing map exists
        // only because readObject recreates it; without that, the write path below throws.
        deserialized.putUnknownColumn("RESTORED", "value");
        assertThat(deserialized.getUnknownColumns())
                .as("unknown columns written to a deserialized %s", typeClass.getSimpleName())
                .containsExactly(entry("RESTORED", "value"));
    }

    private T roundTrip(final T instance) throws IOException, ClassNotFoundException {
        final var baos = new ByteArrayOutputStream();
        try (var oos = new ObjectOutputStream(baos)) {
            oos.writeObject(instance);
        }
        try (var ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            return typeClass.cast(ois.readObject());
        }
    }

    /**
     * Verifies that {@link Object#toString() toString()} renders exactly the {@link _ColumnLabel}-annotated fields,
     * and nothing else.
     * <p>
     * {@link AbstractMetadataType#unknownColumns} is deliberately absent. Its values are whatever
     * {@link java.sql.ResultSet#getObject(String)} returned, so interpolating them would call {@code toString()} on a
     * driver-chosen object - a locator with no meaningful {@code toString()} contract, possibly already invalid, and
     * possibly unbounded in size. {@link AbstractMetadataType#getUnknownColumns()} is the only access point.
     */
    @Test
    void toString_ContainsExactlyColumnLabeledFields_() {
        final var actual = namesInToString(newTypeInstance().toString());
        assertThat(actual)
                .as("field names in %s.toString()", typeClass.getSimpleName())
                .containsExactlyElementsOf(namesExpectedInToString());
    }

    /**
     * Verifies that no unknown column reaches {@link Object#toString() toString()}, neither its label nor its value,
     * and that a value whose {@code toString()} throws cannot make {@code toString()} throw.
     */
    @Test
    void toString_ExcludesUnknownColumns_() {
        final var instance = newTypeInstance();
        instance.putUnknownColumn("UNKNOWN_COLUMN", "UNKNOWN_VALUE");
        instance.putUnknownColumn("HOSTILE_COLUMN", new Object() {
            @Override
            public String toString() {
                throw new UnsupportedOperationException("should never be invoked by toString()");
            }
        });
        final var string = instance.toString();
        assertThat(string)
                .as("%s.toString() with unknown columns present", typeClass.getSimpleName())
                .doesNotContain("unknownColumns")
                .doesNotContain("UNKNOWN_COLUMN")
                .doesNotContain("UNKNOWN_VALUE")
                .doesNotContain("HOSTILE_COLUMN");
        assertThat(namesInToString(string))
                .containsExactlyElementsOf(namesExpectedInToString());
    }

    private Set<String> namesExpectedInToString() {
        final var expected = new LinkedHashSet<String>();
        for (Class<?> c = typeClass; c != null && c != AbstractMetadataType.class; c = c.getSuperclass()) {
            for (final var field : c.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (!field.isAnnotationPresent(_ColumnLabel.class)) {
                    continue;
                }
                expected.add(field.getName());
            }
        }
        return expected;
    }

    private Set<String> namesInToString(final String string) {
        final var actual = new LinkedHashSet<String>();
        final var matcher = FIELD_NAME_IN_TO_STRING_PATTERN.matcher(string);
        while (matcher.find()) {
            actual.add(matcher.group(1));
        }
        return actual;
    }
}
