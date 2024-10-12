package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2024 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Objects;

/**
 * Constants for {@link IsNullableConstants#COLUMN_LABEL_IS_NULLABLE} column.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
enum IsNullableEnum
        implements _FieldEnum<IsNullableEnum, String> {

    /**
     * A value for {@value IsNullableConstants#COLUMN_VALUE_IS_NULLABLE_YES}.
     */
    YES(IsNullableConstants.COLUMN_VALUE_IS_NULLABLE_YES),

    /**
     * A value for {@value IsNullableConstants#COLUMN_VALUE_IS_NULLABLE_NO}.
     */
    NO(IsNullableConstants.COLUMN_VALUE_IS_NULLABLE_NO),

    /**
     * A value for {@value IsNullableConstants#COLUMN_VALUE_IS_NULLABLE_EMPTY}.
     */
    EMPTY(IsNullableConstants.COLUMN_VALUE_IS_NULLABLE_EMPTY);

    /**
     * Returns the value whose {@link #fieldValue() fieldValue} matches specified value.
     *
     * @param fieldValue the value of {@link #fieldValue() fieldValue} to match.
     * @return the value whose {@link #fieldValue() fieldValue} matches {@code fieldValue}.
     * @throws IllegalArgumentException when no value matches.
     */
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
