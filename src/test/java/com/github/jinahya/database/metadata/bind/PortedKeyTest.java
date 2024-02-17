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
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

abstract class PortedKeyTest<T extends PortedKey>
        extends AbstractMetadataTypeTest<T> {

    PortedKeyTest(final Class<T> typeClass) {
        super(typeClass);
    }

    @Override
    T newTypeInstance() {
        final T instance = super.newTypeInstance();
        instance.setPktableName(null);
        instance.setPkcolumnName(null);
        instance.setFktableName("");
        instance.setFkcolumnName("");
        return instance;
    }

    @Nested
    class UpdateRuleAsEnumTest {

        @Test
        void getUpdateRuleAsEnum__() {
            final var spy = Mockito.spy(newTypeInstance());
            assertThat(spy.getUpdateRuleAsEnum()).isNull();
            verify(spy, times(1)).getUpdateRule();
        }

        @Test
        void setUpdateRuleAsEnum_Null_Null() {
            final var spy = Mockito.spy(newTypeInstance());
            spy.setUpdateRuleAsEnum(null);
            verify(spy, times(1)).setUpdateRule(null);
        }

        @EnumSource(PortedKey.TableKeyUpdateRule.class)
        @ParameterizedTest
        void setUpdateRuleAsEnum__(final PortedKey.TableKeyUpdateRule updateRuleAsEnum) {
            final var spy = Mockito.spy(newTypeInstance());
            spy.setUpdateRuleAsEnum(updateRuleAsEnum);
            verify(spy, times(1)).setUpdateRule(updateRuleAsEnum.fieldValueAsInt());
        }
    }

    @Nested
    class DeleteRuleAsEnumTest {

        @Test
        void getDeleteRuleAsEnum__() {
            final var spy = newTypeSpy();
            assertThat(spy.getDeleteRuleAsEnum()).isNull();
            verify(spy, times(1)).getDeleteRule();
        }

        @Test
        void setDeleteRuleAsEnum_Null_Null() {
            final var spy = newTypeSpy();
            spy.setDeleteRuleAsEnum(null);
            verify(spy, times(1)).setDeleteRule(null);
        }

        @EnumSource(PortedKey.TableKeyDeleteRule.class)
        @ParameterizedTest
        void setDeleteRuleAsEnum__(final PortedKey.TableKeyDeleteRule deleteRuleAsEnum) {
            final var spy = newTypeSpy();
            spy.setDeleteRuleAsEnum(deleteRuleAsEnum);
            verify(spy, times(1)).setDeleteRule(deleteRuleAsEnum.fieldValueAsInt());
        }
    }

    @Nested
    class DeferrabilityAsEnumTest {

        @Test
        void getDeferrabilityAsEnum__() {
            final var spy = newTypeSpy();
            assertThat(spy.getDeferrabilityAsEnum()).isNull();
            verify(spy, times(1)).getDeferrability();
        }

        @Test
        void setDeferrabilityAsEnum_Null_Null() {
            final var spy = newTypeSpy();
            spy.setDeferrabilityAsEnum(null);
            verify(spy, times(1)).setDeferrability(null);
        }

        @EnumSource(PortedKey.TableKeyDeferrability.class)
        @ParameterizedTest
        void setDeferrabilityAsEnum__(final PortedKey.TableKeyDeferrability deferrabilityAsEnum) {
            final var spy = newTypeSpy();
            spy.setDeferrabilityAsEnum(deferrabilityAsEnum);
            verify(spy, times(1)).setDeferrability(deferrabilityAsEnum.fieldValueAsInt());
        }
    }
}
