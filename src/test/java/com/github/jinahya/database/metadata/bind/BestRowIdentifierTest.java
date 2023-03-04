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

/**
 * A class for testing {@link BestRowIdentifier} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class BestRowIdentifierTest extends AbstractMetadataTypeTest<BestRowIdentifier> {

    BestRowIdentifierTest() {
        super(BestRowIdentifier.class);
    }

    @DisplayName("PseudoColumnEnum")
    @Nested
    class PseudoColumnEnumTest extends _IntFieldEnumTest<BestRowIdentifier.PseudoColumnEnum> {

        PseudoColumnEnumTest() {
            super(BestRowIdentifier.PseudoColumnEnum.class);
        }
    }

    @DisplayName("ScopeEnum")
    @Nested
    class ScopeEnumTest extends _IntFieldEnumTest<BestRowIdentifier.ScopeEnum> {

        ScopeEnumTest() {
            super(BestRowIdentifier.ScopeEnum.class);
        }
    }

    @DisplayName("pseudoColumnAsEnum")
    @Nested
    class PseudoColumnAsEnumTest {

        @DisplayName("getPseudoColumnAsEnum()")
        @Test
        void getPseudoColumnAsEnum__() {
            final var spy = typeSpy();
            final var pseudoColumnAsEnum = spy.getPseudoColumnAsEnum();
            verify(spy, times(1)).getPseudoColumn();
            assertThat(pseudoColumnAsEnum)
                    .isSameAs(BestRowIdentifier.PseudoColumnEnum.valueOfPseudoColumn(spy.getPseudoColumn()));
        }

        @DisplayName("setPseudoColumnAsEnum(null)")
        @Test
        void setPseudoColumnAsEnum_NullPointerException_Null() {
            final var instance = typeInstance();
            assertThatThrownBy(() -> instance.setPseudoColumnAsEnum(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("setPseudoColumnAsEnum(non-null)")
        @EnumSource(BestRowIdentifier.PseudoColumnEnum.class)
        @ParameterizedTest
        void setPseudoColumnAsEnum__(final BestRowIdentifier.PseudoColumnEnum pseudoColumnAsEnum) {
            final var spy = typeSpy();
            spy.setPseudoColumnAsEnum(pseudoColumnAsEnum);
            verify(spy, times(1)).setPseudoColumn(pseudoColumnAsEnum.fieldValueAsInt());
        }
    }

    @DisplayName("scopeAsEnum")
    @Nested
    class ScopeAsEnumTest {

        @DisplayName("getScopeAsEnum()")
        @Test
        void getScopeAsEnum__() {
            final var spy = typeSpy();
            final var scopeAsEnum = spy.getScopeAsEnum();
            verify(spy, times(1)).getScope();
            assertThat(scopeAsEnum)
                    .isSameAs(BestRowIdentifier.ScopeEnum.valueOfScope(spy.getScope()));
        }

        @DisplayName("setScopeAsEnum(null)")
        @Test
        void setScopeAsEnum_NullPointerException_Null() {
            final var instance = typeInstance();
            assertThatThrownBy(() -> instance.setScopeAsEnum(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("setScopeAsEnum(non-null)")
        @EnumSource(BestRowIdentifier.ScopeEnum.class)
        @ParameterizedTest
        void setScopeAsEnum__(final BestRowIdentifier.ScopeEnum scopeAsEnum) {
            final var spy = typeSpy();
            spy.setScopeAsEnum(scopeAsEnum);
            verify(spy, times(1)).setScope(scopeAsEnum.fieldValueAsInt());
        }
    }
}
