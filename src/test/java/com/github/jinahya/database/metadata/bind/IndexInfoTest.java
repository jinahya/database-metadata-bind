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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class IndexInfoTest extends AbstractMetadataTypeTest<IndexInfo> {

    IndexInfoTest() {
        super(IndexInfo.class);
    }

    @Nested
    class TypeTest extends _IntFieldEnumTest<IndexInfo.Type> {

        TypeTest() {
            super(IndexInfo.Type.class);
        }
    }

    @Nested
    class TypeAsEnumTest {

        @Test
        void getTypeAsEnum__() {
            // GIVEN
            final var instance = newTypeSpy();
            // WHEN
            final var typeAsEnum = instance.getTypeAsEnum();
            // THEN
            verify(instance, times(1)).getType();
        }

        @Test
        void setTypeAsEnum__Null() {
            // GIVEN
            final var instance = newTypeSpy();
            // WHEN
            instance.setTypeAsEnum(null);
            // THEN
            verify(instance, times(1)).setType(null);
        }

        @EnumSource(IndexInfo.Type.class)
        @ParameterizedTest
        void setTypeAsEnum__Nonnull(final IndexInfo.Type typeAsEnum) {
            // GIVEN
            final var instance = newTypeSpy();
            // WHEN
            instance.setTypeAsEnum(typeAsEnum);
            // THEN
            verify(instance, times(1)).setType(typeAsEnum.fieldValueAsInt());
        }
    }
}
