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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
abstract class AbstractMetadataType_Test<T extends AbstractMetadataType>
        extends MetadataType_Test<T> {

    AbstractMetadataType_Test(final Class<T> typeClass) {
        super(typeClass);
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

    @Test
    void toString_ContainsExactlyUnknownColumnsAndColumnLabeledFields_() {
        final var actual = namesInToString(newTypeInstance().toString());
        assertThat(actual)
                .as("field names in %s.toString()", typeClass.getSimpleName())
                .containsExactlyElementsOf(namesExpectedInToString());
    }

    private Set<String> namesExpectedInToString() {
        final var expected = new LinkedHashSet<String>();
        expected.add("unknownColumns");
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

    private static final Pattern FIELD_NAME_IN_TO_STRING_PATTERN = Pattern.compile("\\b([A-Za-z][A-Za-z0-9_]*)=");
}
