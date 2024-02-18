package com.github.jinahya.database.metadata.bind;

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