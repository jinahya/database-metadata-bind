package com.github.jinahya.database.metadata.bind;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

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
     * Asserts {@code valueOfRawValue(int)} method, defined in specified enum class, invoked with each enum constant's
     * {@link IntFieldEnum#getRawValue() rawValue} returns the same enum constant.
     */
    @DisplayName("valueOfRawValue(c.getRawValue()) returns c")
    @Test
    void valueOfRawValue_ReturnsTheConstant_ForItsRawValue() throws ReflectiveOperationException {
        final Method method = enumClass.getMethod("valueOfRawValue", int.class);
        for (final E enumConstant : enumClass.getEnumConstants()) {
            @SuppressWarnings({"unchecked"})
            final E value = (E) method.invoke(null, enumConstant.getRawValue());
            assertThat(value).isNotNull().isSameAs(enumConstant);
        }
    }

    @DisplayName("getRawValue() does not throw any exceptions")
    @Test
    void getRawValue_DoesNotThrow_() {
        final Set<Integer> rawValues = new HashSet<>();
        for (final E enumConstant : enumClass.getEnumConstants()) {
            // still occurs with 1.8.0_281
            // https://stackoverflow.com/a/35584758/330457
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8141508
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8142476
            // final int rawValue = assertDoesNotThrow(enumConstant::getRawValue);
            final int rawValue = assertDoesNotThrow(() -> enumConstant.getRawValue());
            assertThat(rawValues.add(rawValue)).isTrue();
        }
    }

    /**
     * The enum class to test.
     */
    protected final Class<E> enumClass;
}