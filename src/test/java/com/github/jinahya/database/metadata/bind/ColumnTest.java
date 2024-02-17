package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ColumnTest
        extends AbstractMetadataTypeTest<Column> {

    ColumnTest() {
        super(Column.class);
    }

    @DisplayName("NullableEnum")
    @Nested
    class NullableTest
            extends _IntFieldEnumTest<Column.Nullable> {

        NullableTest() {
            super(Column.Nullable.class);
        }
    }

    @Override
    Column newTypeInstance() {
        final var instance = super.newTypeInstance();
        instance.setTableName("");
        instance.setColumnName("");
        instance.setOrdinalPosition(1);
        return instance;
    }

    @DisplayName("nullableAsEnum")
    @Nested
    class NullableAsEnumTest {

        @Test
        void getNullableAsEnum__() {
            final var spy = newTypeSpy();
            final var nullableAsEnum = spy.getNullableAsEnum();
            verify(spy, times(1)).getNullable();
        }

        @Test
        void setNullableAsEnum_Null_Null() {
            final var spy = newTypeSpy();
            spy.setNullableAsEnum(null);
            verify(spy, times(1)).setNullable(null);
        }

        @EnumSource(Column.Nullable.class)
        @ParameterizedTest
        void setNullableAsEnum__(final Column.Nullable nullableAsEnum) {
            final var spy = newTypeSpy();
            spy.setNullableAsEnum(nullableAsEnum);
            verify(spy, times(1))
                    .setNullable(nullableAsEnum.fieldValueAsInt());
        }
    }
}
