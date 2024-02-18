package com.github.jinahya.database.metadata.bind;

import java.util.Optional;

public interface HasNullableEnum<E extends Enum<E> & NullableEnum<E>> {

    /**
     * Returns current value of {@code nullable} property.
     *
     * @return current value of the {@code nullable} property.
     */
    Integer getNullable();

    /**
     * Replace current value of {@code nullable} property with specified value.
     *
     * @param nullable new value for the {@code nullable} property.
     */
    void setNullable(Integer nullable);

    E getNullableAsEnum();

    default void setNullableAsEnum(final E nullableAsEnum) {
        setNullable(
                Optional.ofNullable(nullableAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }
}
