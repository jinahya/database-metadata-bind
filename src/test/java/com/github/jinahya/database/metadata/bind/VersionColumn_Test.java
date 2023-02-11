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

class VersionColumn_Test extends AbstractMetadataTypeTest<VersionColumn> {

    VersionColumn_Test() {
        super(VersionColumn.class);
    }

    @Nested
    class PseudoColumnAsEnumTest {

        @DisplayName("getPseudoColumnAsEnum() return 0")
        @Test
        void getPseudoColumnAsEnum__() {
            final var spy = typeSpy();
            final var pseudoColumnAsEnum = spy.getPseudoColumnAsEnum();
            assertThat(pseudoColumnAsEnum).isEqualTo(VersionColumnPseudoColumn.valueOfPseudoColumn(0));
            verify(spy, times(1)).getPseudoColumn();
        }

        @DisplayName("setPseudoColumnAsEnum(null) throws a NullPointerException")
        @Test
        void setPseudoColumnAsEnum_NullPointerException_Null() {
            final var spy = typeSpy();
            assertThatThrownBy(() -> spy.setPseudoColumnAsEnum(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("setPseudoColumnAsEnum(e) -> setPseudoColumn(e.fieldValueAsEnum)")
        @EnumSource(VersionColumnPseudoColumn.class)
        @ParameterizedTest
        void setPseudoColumnAsEnum__(final VersionColumnPseudoColumn pseudoColumnAsEnum) {
            final var spy = typeSpy();
            spy.setPseudoColumnAsEnum(pseudoColumnAsEnum);
            verify(spy, times(1)).setPseudoColumn(pseudoColumnAsEnum.fieldValueAsInt());
        }
    }
}
