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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FunctionTest
        extends AbstractMetadataTypeTest<Function> {

    @Nested
    class FunctionTypeTest
            extends _IntFieldEnumTest<Function.FunctionType> {

        FunctionTypeTest() {
            super(Function.FunctionType.class);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    FunctionTest() {
        super(Function.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    Function newTypeInstance() {
        final Function instance = super.newTypeInstance();
        instance.setFunctionName("");
        instance.setSpecificName("");
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @DisplayName("functionTypeAsEnum")
    @Nested
    class FunctionTypeAsEnumTest {

        @DisplayName("getFunctionTypeAsEnum()FunctionType")
        @EnumSource(Function.FunctionType.class)
        @ParameterizedTest
        void getFunctionTypeAsEnum__(final Function.FunctionType functionType) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            given(instance.getFunctionType()).willReturn(functionType.fieldValueAsInt());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getFunctionTypeAsEnum();
            // ---------------------------------------------------------------------------------------------------- then
            assertThat(actual).isSameAs(functionType);
            verify(instance, times(1)).getFunctionType();
        }

        @DisplayName("getFunctionTypeAsEnum()FunctionType")
        @EnumSource(Function.FunctionType.class)
        @ParameterizedTest
        void setFunctionTypeAsEnum__(final Function.FunctionType functionType) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setFunctionTypeAsEnum(functionType);
            // ---------------------------------------------------------------------------------------------------- then
            verify(instance, times(1)).setFunctionType(functionType.fieldValueAsInt());
        }
    }
}
