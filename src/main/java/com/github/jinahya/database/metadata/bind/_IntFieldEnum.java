package com.github.jinahya.database.metadata.bind;

interface _IntFieldEnum<E extends Enum<E> & _IntFieldEnum<E>> {

    static <E extends Enum<E> & _IntFieldEnum<E>> E valueOfFieldValue(final Class<E> enumClass, final int fieldValue) {
        for (final E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.fieldValueAsInt() == fieldValue) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("no enum constant for " + fieldValue);
    }

    int fieldValueAsInt();
}
