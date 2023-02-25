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

class FunctionColumnTest extends AbstractMetadataTypeTest<FunctionColumn> {

    FunctionColumnTest() {
        super(FunctionColumn.class);
    }

    @DisplayName("ColumnType")
    @Nested
    class ColumnTypeEnumTest extends _IntFieldEnumTest<FunctionColumn.ColumnTypeEnum> {

        ColumnTypeEnumTest() {
            super(FunctionColumn.ColumnTypeEnum.class);
        }
    }

    @DisplayName("ColumnTypeAsEnum")
    @Nested
    class ColumnTypeEnumAsEnumTest {

        @Test
        void getColumnTypeAsEnum__() {
            final var spy = typeSpy();
            final var actual = spy.getColumnTypeAsEnum();
            verify(spy, times(1)).getColumnType();
            assertThat(actual)
                    .isSameAs(FunctionColumn.ColumnTypeEnum.valueOfColumnType(spy.getColumnType()));
        }

        @Test
        void setColumnTypeAsEnum_NullPointerException_Null() {
            final var spy = typeSpy();
            assertThatThrownBy(() -> spy.setColumnTypeAsEnum(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @EnumSource(FunctionColumn.ColumnTypeEnum.class)
        @ParameterizedTest
        void setColumnTypeAsEnum__(final FunctionColumn.ColumnTypeEnum columnTypeAsEnum) {
            final var spy = typeSpy();
            spy.setColumnTypeAsEnum(columnTypeAsEnum);
            verify(spy, times(1)).setColumnType(columnTypeAsEnum.fieldValueAsInt());
        }
    }

    @Override
    FunctionColumn typeInstance() {
        final FunctionColumn instance = super.typeInstance();
        instance.setFunctionName("");
        instance.setColumnName("");
        instance.setSpecificName("");
        return instance;
    }
}
