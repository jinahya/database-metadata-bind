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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CrossReferenceTest
        extends AbstractMetadataTypeTest<CrossReference> {

    CrossReferenceTest() {
        super(CrossReference.class);
    }

    @Nested
    class UpdateRuleAsEnumTest {

        @EnumSource(PortedKey.TableKeyUpdateRule.class)
        @ParameterizedTest
        void getUpdateRuleAsEnum__(final PortedKey.TableKeyUpdateRule expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            given(instance.getUpdateRule()).willReturn(expected.fieldValueAsInt());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getUpdateRuleAsEnum();
            // ---------------------------------------------------------------------------------------------------------
            assertThat(actual).isSameAs(expected);
        }

        @EnumSource(PortedKey.TableKeyUpdateRule.class)
        @ParameterizedTest
        void setUpdateRuleAsEnum__(final PortedKey.TableKeyUpdateRule updateRuleAsEnum) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setUpdateRuleAsEnum(updateRuleAsEnum);
            // ---------------------------------------------------------------------------------------------------------
            verify(instance, times(1)).setUpdateRule(updateRuleAsEnum.fieldValueAsInt());
        }
    }

    @Nested
    class DeleteRuleAsEnumTest {

        @EnumSource(PortedKey.TableKeyUpdateRule.class)
        @ParameterizedTest
        void getDeleteRuleAsEnum__(final PortedKey.TableKeyUpdateRule expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            given(instance.getDeleteRule()).willReturn(expected.fieldValueAsInt());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getDeleteRuleAsEnum();
            // ---------------------------------------------------------------------------------------------------------
            assertThat(actual).isSameAs(expected);
        }

        @EnumSource(PortedKey.TableKeyUpdateRule.class)
        @ParameterizedTest
        void setUpdateRuleAsEnum__(final PortedKey.TableKeyUpdateRule deleteRuleAsEnum) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setDeleteRuleAsEnum(deleteRuleAsEnum);
            // ---------------------------------------------------------------------------------------------------------
            verify(instance, times(1)).setDeleteRule(deleteRuleAsEnum.fieldValueAsInt());
        }
    }
}
