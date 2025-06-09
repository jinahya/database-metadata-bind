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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class VersionColumnTest
        extends AbstractMetadataType_Test<VersionColumn> {

    VersionColumnTest() {
        super(VersionColumn.class);
    }

    @DisplayName("PseudoColumnEnum")
    @Nested
    class PseudoColumnTest
            extends _IntFieldEnumTest<VersionColumn.PseudoColumn> {

        PseudoColumnTest() {
            super(VersionColumn.PseudoColumn.class);
        }

        @EnumSource(VersionColumn.PseudoColumn.class)
        @ParameterizedTest
        void valueOfPseudoColumn__(final VersionColumn.PseudoColumn pseudoColumn) {
            assertThat(VersionColumn.PseudoColumn.valueOfFieldValue(pseudoColumn.fieldValueAsInt()))
                    .isSameAs(pseudoColumn);
        }
    }

    @Nested
    class PseudoColumnAsEnumTest {

        @Test
        void getPseudoColumnAsEnum__() {
            final var instance = newTypeSpy();
            // WHEN
            final var pseudoColumnAsEnum = instance.getPseudoColumnAsEnum();
            // THEN
            verify(instance, times(1)).getPseudoColumn();
        }
    }
}
