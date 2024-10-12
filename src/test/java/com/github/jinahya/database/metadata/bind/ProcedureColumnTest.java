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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.BDDMockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ProcedureColumnTest
        extends AbstractMetadataTypeTest<ProcedureColumn> {

    @Nested
    class ColumnTypeTest
            extends _IntFieldEnumTest<ProcedureColumn.ColumnType> {

        ColumnTypeTest() {
            super(ProcedureColumn.ColumnType.class);
        }
    }

    @Nested
    class NullableTest
            extends _IntFieldEnumTest<ProcedureColumn.Nullable> {

        NullableTest() {
            super(ProcedureColumn.Nullable.class);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    ProcedureColumnTest() {
        super(ProcedureColumn.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    ProcedureColumn newTypeInstance() {
        final ProcedureColumn instance = super.newTypeInstance();
        instance.setProcedureName("");
        instance.setColumnName("");
        instance.setSpecificName("");
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nested
    class ColumnTypeAsEnumTest {

        @EnumSource(ProcedureColumn.ColumnType.class)
        @ParameterizedTest
        void getColumnTypeAsEnum__(final ProcedureColumn.ColumnType expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            BDDMockito.given(instance.getColumnType()).willReturn(expected.fieldValueAsInt());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getColumnTypeAsEnum();
            // ---------------------------------------------------------------------------------------------------- then
            assertThat(actual).isSameAs(expected);
        }

        @EnumSource(ProcedureColumn.ColumnType.class)
        @ParameterizedTest
        void setColumnTypeAsEnum__(final ProcedureColumn.ColumnType columnTypeAsEnum) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setColumnTypeAsEnum(columnTypeAsEnum);
            // ---------------------------------------------------------------------------------------------------- then
            verify(instance, times(1)).setColumnType(columnTypeAsEnum.fieldValueAsInt());
        }
    }

    @Nested
    class NullableAsEnumTest {

        @EnumSource(ProcedureColumn.Nullable.class)
        @ParameterizedTest
        void getNullableAsEnum__(final ProcedureColumn.Nullable expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            BDDMockito.given(instance.getNullable()).willReturn(expected.fieldValueAsInt());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getNullableAsEnum();
            // ---------------------------------------------------------------------------------------------------- then
            assertThat(actual).isSameAs(expected);
        }

        @EnumSource(ProcedureColumn.Nullable.class)
        @ParameterizedTest
        void setNullableAsEnum__(final ProcedureColumn.Nullable nullableAsEnum) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setNullableAsEnum(nullableAsEnum);
            // ---------------------------------------------------------------------------------------------------- then
            verify(instance, times(1)).setNullable(nullableAsEnum.fieldValueAsInt());
        }
    }

    @Nested
    class IsNullableTest
            extends HasIsNullableTest<ProcedureColumn> {

        IsNullableTest() {
            super(ProcedureColumnTest.this::newTypeSpy);
        }
    }
}
