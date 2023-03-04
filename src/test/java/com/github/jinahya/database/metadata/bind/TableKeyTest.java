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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

abstract class TableKeyTest<T extends TableKey<T>> extends AbstractMetadataTypeTest<T> {

    TableKeyTest(final Class<T> typeClass) {
        super(typeClass);
    }

    @Override
    T typeInstance() {
        final T instance = super.typeInstance();
        instance.setPktableName("");
        instance.setPkcolumnName("");
        instance.setFktableName("");
        instance.setFkcolumnName("");
        return instance;
    }

    @Nested
    class DeferrabilityAsEnumTest {

        @Test
        void getDeferrabilityAsEnum__() {
            final var spy = typeSpy();
            assertThatThrownBy(spy::getDeferrabilityAsEnum)
                    .isInstanceOf(IllegalArgumentException.class);
            verify(spy, times(1)).getDeferrability();
        }

        @Test
        void setDeferrabilityAsEnum_NullPointerException_Null() {
            final var spy = typeSpy();
            assertThatThrownBy(() -> spy.setDeferrabilityAsEnum(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @EnumSource(TableKeyDeferrability.class)
        @ParameterizedTest
        void setDeferrabilityAsEnum__(final TableKeyDeferrability deferrabilityAsEnum) {
            final var spy = typeSpy();
            spy.setDeferrabilityAsEnum(deferrabilityAsEnum);
            verify(spy, times(1)).setDeferrability(deferrabilityAsEnum.fieldValueAsInt());
        }
    }
}
