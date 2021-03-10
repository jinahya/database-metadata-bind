package com.github.jinahya.database.metadata.bind;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
abstract class IntFieldEnumTest<E extends Enum<E> & IntFieldEnum<E>> {

    protected static <E extends Enum<E> & IntFieldEnum<E>> void valueOfRawValue_Same_ForRawValue(
            final Class<E> enumClass) {
        requireNonNull(enumClass, "enumClass is null");
        final Method method;
        try {
            method = enumClass.getMethod("valueOfRawValue", int.class);
        } catch (final NoSuchMethodException nsme) {
            log.warn("no valueOfRawValue(int) method found from {}", enumClass, nsme);
            return;
        }
        for (final E enumConstant : enumClass.getEnumConstants()) {
            final int rawValue = enumConstant.getRawValue();
            try {
                @SuppressWarnings({"unchecked"})
                final E value = (E) method.invoke(null, rawValue);
                assertThat(value).isNotNull().isSameAs(enumConstant);
            } catch (final ReflectiveOperationException roe) {
                log.error("failed to invoke {} on {} with {}", method, enumClass, rawValue);
            }
        }
    }

    protected IntFieldEnumTest(final Class<E> enumClass) {
        super();
        this.enumClass = requireNonNull(enumClass, "enumClass is null");
    }

    @Test
    void valueOfRawValue__() {
        valueOfRawValue_Same_ForRawValue(enumClass);
    }

    @Test
    void getRawValue__() {
        final Set<Integer> rawValues = new HashSet<>();
        for (final E enumConstant : enumClass.getEnumConstants()) {
            final int rawValue = assertDoesNotThrow(enumConstant::getRawValue);
            assertThat(rawValues.add(rawValue)).isTrue();
        }
    }

    protected final Class<E> enumClass;
}