package com.github.jinahya.database.metadata.bind;

import java.util.Optional;

public interface HasIsNullableEnum {

    /**
     * Returns current value of {@code isNullable} property.
     *
     * @return current value of the {@code isNullable} property.
     */
    String getIsNullable();

    /**
     * Replace current value of {@code isNullable} property with specified value.
     *
     * @param isNullable new value for the {@code isNullable} property.
     */
    void setIsNullable(String isNullable);

    default IsNullableEnum getIsNullableAsEnum() {
        return Optional.ofNullable(getIsNullable())
                .map(IsNullableEnum::valueOfFieldValue)
                .orElse(null);
    }

    default void setIsNullableAsEnum(final IsNullableEnum isNullableAsEnum) {
        setIsNullable(
                Optional.ofNullable(isNullableAsEnum)
                        .map(_FieldEnum::fieldValue)
                        .orElse(null)
        );
    }
}
