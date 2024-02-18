package com.github.jinahya.database.metadata.bind;

import java.util.Objects;

/**
 * Constants for {@link IsNullableConstants#COLUMN_LABEL_IS_NULLABLE} column.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public enum IsNullableEnum
        implements _FieldEnum<IsNullableEnum, String> {

    YES(IsNullableConstants.COLUMN_VALUE_IS_NULLABLE_YES),

    NO(IsNullableConstants.COLUMN_VALUE_IS_NULLABLE_NO),

    EMPTY(IsNullableConstants.COLUMN_VALUE_IS_NULLABLE_EMPTY);

    public static IsNullableEnum valueOfFieldValue(final String fieldValue) {
        return _FieldEnum.valueOfFieldValue(IsNullableEnum.class, fieldValue);
    }

    IsNullableEnum(final String fieldEnum) {
        this.fieldEnum = Objects.requireNonNull(fieldEnum, "fieldEnum is null");
    }

    @Override
    public String fieldValue() {
        return fieldEnum;
    }

    private final String fieldEnum;
}