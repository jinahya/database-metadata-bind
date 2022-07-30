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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

abstract class FieldEnumTest<E extends Enum<E> & FieldEnum<E, T>, T> {

    protected FieldEnumTest(final Class<E> enumClass, final Class<T> fieldClass) {
        super();
        this.enumClass = requireNonNull(enumClass, "enumClass is null");
        this.fieldClass = requireNonNull(fieldClass, "fieldClass is null");
    }

    /**
     * Returns a stream of raw values of all enum constants.
     *
     * @return a stream of raw values of all enum constants.
     */
    protected Stream<T> rawValueStream() {
        // https://bugs.openjdk.java.net/browse/JDK-8142476
        //return Arrays.stream(enumClass.getEnumConstants()).map(FieldEnum::getRawValue);
        return Arrays.stream(enumClass.getEnumConstants()).map(c -> c.getRawValue());
    }

    /**
     * Returns a list of raw values of all enum constants.
     *
     * @return a list of raw values of all enum constants.
     */
    protected List<T> rawValueList() {
        return rawValueStream().collect(Collectors.toList());
    }

    /**
     * Asserts {@code valueOfRawValue(int)} method, defined in specified enum class, invoked with each enum constant's
     * {@link IntFieldEnum#rawValue() rawValue} returns the same enum constant.
     */
    @DisplayName("valueOfRawValue(c.getRawValue()) returns c")
    @Test
    void valueOfRawValue_ReturnsTheConstant_ForItsRawValue() throws ReflectiveOperationException {
        final Method method = enumClass.getMethod("valueOfRawValue", fieldClass);
        for (final E enumConstant : enumClass.getEnumConstants()) {
            @SuppressWarnings({"unchecked"})
            final E value = (E) method.invoke(null, enumConstant.getRawValue());
            assertThat(value).isNotNull().isSameAs(enumConstant);
        }
    }

    @DisplayName("values.rawValue() are unique to each other")
    @Test
    void getRawValue_NoDuplicates_ForAll() {
        // id this the best you got?
        assertThat(rawValueStream().distinct().collect(Collectors.toList()))
                .hasSameElementsAs(rawValueList());
    }

    @DisplayName("getRawValue() does not throw any exceptions")
    @Test
    void getRawValue_DoesNotThrow_() {
        for (final E enumConstant : enumClass.getEnumConstants()) {
            // still occurs with 1.8.0_281
            // https://stackoverflow.com/a/35584758/330457
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8141508
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8142476
            // final int rawValue = assertDoesNotThrow(enumConstant::getRawValue);
            final T rawValue = assertDoesNotThrow(() -> enumConstant.getRawValue());
        }
    }

    protected final Class<E> enumClass;

    protected final Class<T> fieldClass;
}
