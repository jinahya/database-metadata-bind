package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2024 Jinahya, Inc.
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.BDDMockito;

import java.util.Objects;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

abstract class HasIsNullableTest<T extends MetadataType & HasIsNullableEnum> {

    HasIsNullableTest(final Supplier<? extends T> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier, "supplier is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @DisplayName("getIsNullableAsEnum()IsNullable")
    @Nested
    class GetIsNullableAsEnumTest {

        @Test
        void __() {
            final var instance = supplier.get();
            final var actual = instance.getIsNullableAsEnum();
            assertThat(actual).isNull();
        }

        @EnumSource(IsNullableEnum.class)
        @ParameterizedTest
        void __(final IsNullableEnum expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = supplier.get();
            BDDMockito.given(instance.getIsNullable()).willReturn(expected.fieldValue());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getIsNullableAsEnum();
            // ---------------------------------------------------------------------------------------------------- when
            assertThat(actual).isSameAs(expected);
        }
    }

    @DisplayName("setIsNullableAsEnum(IsNullable)")
    @Nested
    class SetIsNullableAsEnumTest {

        @DisplayName("setIsNullableAsEnum(null)")
        @Test
        void __() {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = supplier.get();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setIsNullableAsEnum(null);
            // ---------------------------------------------------------------------------------------------------- then
            verify(instance, times(1)).setIsNullable(null);
        }

        @DisplayName("setIsNullableAsEnum(!null)")
        @EnumSource(IsNullableEnum.class)
        @ParameterizedTest
        void __(final IsNullableEnum expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = supplier.get();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setIsNullableAsEnum(expected);
            // ---------------------------------------------------------------------------------------------------- then
            verify(instance, times(1)).setIsNullable(expected.fieldValue());
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Supplier<? extends T> supplier;
}
