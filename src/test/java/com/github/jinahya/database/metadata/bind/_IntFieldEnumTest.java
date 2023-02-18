package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

abstract class _IntFieldEnumTest<E extends Enum<E> & _IntFieldEnum<E>> {

    _IntFieldEnumTest(final Class<E> enumClass) {
        super();
        this.enumClass = Objects.requireNonNull(enumClass, "enumClass is null");
    }

    @Test
    void fieldValueAsInt_Distinct_() {
        assertThat(enumClass.getEnumConstants())
                .extracting(_IntFieldEnum::fieldValueAsInt)
                .doesNotHaveDuplicates();
    }

    final Class<E> enumClass;
}
