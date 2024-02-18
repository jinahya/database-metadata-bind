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
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

class TypeInfoTest
        extends AbstractMetadataTypeTest<TypeInfo> {

    @Nested
    class NullableTest
            extends _IntFieldEnumTest<TypeInfo.Nullable> {

        NullableTest() {
            super(TypeInfo.Nullable.class);
        }
    }

    @Nested
    class SearchableTest
            extends _IntFieldEnumTest<TypeInfo.Searchable> {

        SearchableTest() {
            super(TypeInfo.Searchable.class);
        }
    }

    TypeInfoTest() {
        super(TypeInfo.class);
    }

    @Nested
    class NullableAsEnumTest {

        @EnumSource(TypeInfo.Nullable.class)
        @ParameterizedTest
        void getNullableAsEnum__(final TypeInfo.Nullable expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            given(instance.getNullable()).willReturn(expected.fieldValueAsInt());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getNullableAsEnum();
            // ---------------------------------------------------------------------------------------------------- then
            assertThat(actual).isSameAs(expected);
        }

        @EnumSource(TypeInfo.Nullable.class)
        @ParameterizedTest
        void setNullableAsEnum__(final TypeInfo.Nullable nullableAsEnum) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setNullableAsEnum(nullableAsEnum);
            // ---------------------------------------------------------------------------------------------------- then
            Mockito.verify(instance, times(1)).setNullable(nullableAsEnum.fieldValueAsInt());
        }
    }

    @Nested
    class SearchableAsEnumTest {

        @EnumSource(TypeInfo.Searchable.class)
        @ParameterizedTest
        void getSearchableAsEnum__(final TypeInfo.Searchable expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            given(instance.getSearchable()).willReturn(expected.fieldValueAsInt());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getSearchableAsEnum();
            // ---------------------------------------------------------------------------------------------------- then
            assertThat(actual).isSameAs(expected);
        }

        @EnumSource(TypeInfo.Searchable.class)
        @ParameterizedTest
        void setSearchableAsEnum__(final TypeInfo.Searchable searchableAsEnum) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setSearchableAsEnum(searchableAsEnum);
            // ---------------------------------------------------------------------------------------------------- then
            Mockito.verify(instance, times(1)).setSearchable(searchableAsEnum.fieldValueAsInt());
        }
    }
}
