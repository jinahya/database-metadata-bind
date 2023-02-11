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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class Column_Test extends AbstractMetadataTypeTest<Column> {

    Column_Test() {
        super(Column.class);
    }

    @Nested
    class NullableAsEnumTest {

        @DisplayName("getNullableAsEnum() return 0")
        @Test
        void getNullableAsEnum__() {
            final var spy = typeSpy();
            final var nullableAsEnum = spy.getNullableAsEnum();
            assertThat(nullableAsEnum).isEqualTo(ColumnNullable.valueOfNullable(0));
            verify(spy, times(1)).getNullable();
        }

        @DisplayName("setNullableAsEnum(null) throws a NullPointerException")
        @Test
        void setNullableAsEnum_NullPointerException_Null() {
            final var spy = typeSpy();
            assertThatThrownBy(() -> spy.setNullableAsEnum(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("setNullableAsEnum(e) -> setNullable(e.fieldValueAsEnum)")
        @EnumSource(ColumnNullable.class)
        @ParameterizedTest
        void setNullableAsEnum__(final ColumnNullable nullableAsEnum) {
            final var spy = typeSpy();
            spy.setNullableAsEnum(nullableAsEnum);
            verify(spy, times(1)).setNullable(nullableAsEnum.fieldValueAsInt());
        }
    }
}
