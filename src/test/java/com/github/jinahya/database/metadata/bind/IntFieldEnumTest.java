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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * An abstract class for testing enum classes implement {@link IntFieldEnum} interface.
 *
 * @param <E> enum type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
abstract class IntFieldEnumTest<E extends Enum<E> & IntFieldEnum<E>> {

    /**
     * Creates a new instance with specified enum class.
     *
     * @param enumClass the enum class to test.
     * @see #enumClass
     */
    protected IntFieldEnumTest(final Class<E> enumClass) {
        super();
        this.enumClass = requireNonNull(enumClass, "enumClass is null");
    }

    /**
     * Returns a stream of raw values of all enum constants.
     *
     * @return a stream of raw values of all enum constants.
     */
    protected @NotNull IntStream rawValueStream() {
        // https://bugs.openjdk.java.net/browse/JDK-8142476
        // return Arrays.stream(enumClass.getEnumConstants()).mapToInt(IntFieldEnum::getRawValue);
        return Arrays.stream(enumClass.getEnumConstants()).mapToInt(c -> c.rawValue());
    }

    /**
     * Returns a stream of raw values of all enum constants.
     *
     * @return a stream of raw values of all enum constants.
     */
    protected @NotEmpty List<@NotNull Integer> rawValueList() {
        return rawValueStream().boxed().collect(Collectors.toList());
    }

    /**
     * Asserts {@code valueOfRawValue(int)} method, defined in specified enum class, invoked with each enum constant's
     * {@link IntFieldEnum#rawValue() rawValue} returns the same enum constant.
     */
    @DisplayName("valueOfRawValue(c.getRawValue()) returns c")
    @Test
    void valueOfRawValue_ReturnsTheConstant_ForItsRawValue() throws ReflectiveOperationException {
        final Method method = enumClass.getMethod("valueOfRawValue", int.class);
        for (final E enumConstant : enumClass.getEnumConstants()) {
            @SuppressWarnings({"unchecked"})
            final E value = (E) method.invoke(null, enumConstant.rawValue());
            assertThat(value).isNotNull().isSameAs(enumConstant);
        }
    }

    @DisplayName("values.rawValue() are sorted")
    @Test
    void getRawValue_Sorted_ForAll() {
        assertThat(rawValueStream()).isSorted();
    }

    @DisplayName("all raw values are unique to each other")
    @Test
    void getRawValue_NoDuplicates_ForAll() {
        // id this the best you got?
        assertThat(rawValueStream().distinct().boxed().collect(Collectors.toList())).hasSameElementsAs(rawValueList());
    }

    @DisplayName("getRawValue() does not throw any exception")
    @Test
    void getRawValue_DoesNotThrow_() {
        for (final E enumConstant : enumClass.getEnumConstants()) {
            // still occurs with 1.8.0_281
            // https://stackoverflow.com/a/35584758/330457
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8141508
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8142476
            // final int rawValue = assertDoesNotThrow(enumConstant::getRawValue);
            final int rawValue = assertDoesNotThrow(() -> enumConstant.rawValue());
        }
    }

    /**
     * The enum class to test.
     */
    protected final Class<E> enumClass;
}
