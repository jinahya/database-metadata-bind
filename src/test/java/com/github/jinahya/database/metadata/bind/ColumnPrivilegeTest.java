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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ColumnPrivilegeTest
        extends AbstractMetadataTypeTest<ColumnPrivilege> {

    @Nested
    class IsGrantableTest
            extends _FieldEnumTest<ColumnPrivilege.IsGrantable, String> {

        IsGrantableTest() {
            super(ColumnPrivilege.IsGrantable.class);
        }
    }

    ColumnPrivilegeTest() {
        super(ColumnPrivilege.class);
    }

    @Nested
    class IsGrantableAsEnumTest {

        @EnumSource(ColumnPrivilege.IsGrantable.class)
        @ParameterizedTest
        void getIsGrantableAsEnum__(final ColumnPrivilege.IsGrantable expected) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            given(instance.getIsGrantable()).willReturn(expected.fieldValue());
            // ---------------------------------------------------------------------------------------------------- when
            final var actual = instance.getIsGrantableAsEnum();
            // ---------------------------------------------------------------------------------------------------- then
            assertThat(actual).isSameAs(expected);
        }

        @EnumSource(ColumnPrivilege.IsGrantable.class)
        @ParameterizedTest
        void setIsGrantableAsEnum__(final ColumnPrivilege.IsGrantable isGrantableAsEnum) {
            // --------------------------------------------------------------------------------------------------- given
            final var instance = newTypeSpy();
            // ---------------------------------------------------------------------------------------------------- when
            instance.setIsGrantableAsEnum(isGrantableAsEnum);
            // ---------------------------------------------------------------------------------------------------- then
            verify(instance, times(1)).setIsGrantable(isGrantableAsEnum.fieldValue());
        }
    }
}
