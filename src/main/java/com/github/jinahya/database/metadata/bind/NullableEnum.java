package com.github.jinahya.database.metadata.bind;

interface NullableEnum<E extends Enum<E> & NullableEnum<E>>
        extends _IntFieldEnum<E> {

    static <E extends Enum<E> & NullableEnum<E>> E valueOfFieldValue(final Class<E> enumClass, final int fieldValue) {
        return _IntFieldEnum.valueOfFieldValue(enumClass, fieldValue);
    }
}
