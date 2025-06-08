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
import org.mockito.BDDMockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AttributeTest
        extends AbstractMetadataTypeTest<Attribute> {

    @DisplayName("Nullable")
    @Nested
    class NullableTest
            extends _IntFieldEnumTest<Attribute.Nullable> {

        NullableTest() {
            super(Attribute.Nullable.class);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    AttributeTest() {
        super(Attribute.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    Attribute newTypeInstance() {
        final Attribute instance = super.newTypeInstance();
        instance.attrName = "";
        instance.typeName = "";
        instance.ordinalPosition = 1;
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @DisplayName("NullableAsEnum")
    @Nested
    class NullableAsEnumTest {

        @DisplayName("getNullableAsEnum()Nullable")
        @EnumSource(Attribute.Nullable.class)
        @ParameterizedTest
        void getNullableAsEnum__(final Attribute.Nullable expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            BDDMockito.given(instance.getNullable()).willReturn(expected.fieldValueAsInt());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getNullableAsEnum();
            // ---------------------------------------------------------------------------------------------------- then
            assertThat(actual).isSameAs(expected);
        }

        @DisplayName("setNullableAsEnum(Nullable)")
        @EnumSource(Attribute.Nullable.class)
        @ParameterizedTest
        void setNullableAsEnum__(final Attribute.Nullable nullableAsEnum) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setNullableAsEnum(nullableAsEnum);
            // ---------------------------------------------------------------------------------------------------- then
            verify(instance, times(1)).setNullable(nullableAsEnum.fieldValueAsInt());
        }
    }

    @DisplayName("IsNullable")
    @Nested
    class IsNullableTest
            extends HasIsNullableTest<Attribute> {

        IsNullableTest() {
            super(AttributeTest.this::newTypeSpy);
        }
    }
}
