package com.github.jinahya.database.metadata.bind;

import java.util.Objects;

abstract class _FieldEnumTest<E extends Enum<E> & _FieldEnum<E, T>, T> {

    _FieldEnumTest(final Class<E> enumClass) {
        super();
        this.enumClass = Objects.requireNonNull(enumClass, "enumClass is null");
    }

    final Class<E> enumClass;
}