package com.github.jinahya.database.metadata.bind;

import java.util.Objects;

interface _FieldEnum<E extends Enum<E> & _FieldEnum<E, T>, T> {

    static <E extends Enum<E> & _FieldEnum<E, T>, T> E valueOfFieldValue(final Class<E> enumClass, final T fieldValue) {
        for (final E enumConstant : enumClass.getEnumConstants()) {
            if (Objects.equals(enumConstant.fieldValue(), fieldValue)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("no enum constant for " + fieldValue);
    }

    T fieldValue();
}
