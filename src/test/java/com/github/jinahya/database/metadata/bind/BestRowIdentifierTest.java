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
    class PseudoColumnTest extends _IntFieldEnumTest<BestRowIdentifier.PseudoColumn> {

        PseudoColumnTest() {
            super(BestRowIdentifier.PseudoColumn.class);
        }
    }

    @DisplayName("ScopeEnum")
    @Nested
    class ScopeTest extends _IntFieldEnumTest<BestRowIdentifier.Scope> {

        ScopeTest() {
            super(BestRowIdentifier.Scope.class);
        }
    }

    @DisplayName("pseudoColumnAsEnum")
    @Nested
    class PseudoColumnAsEnumTest {

        @DisplayName("getPseudoColumnAsEnum()")
        @Test
        void getPseudoColumnAsEnum__() {
            final var spy = newTypeSpy();
            final var pseudoColumnAsEnum = spy.getPseudoColumnAsEnum();
            verify(spy, times(1)).getPseudoColumn();
        }

        @DisplayName("setPseudoColumnAsEnum(null)")
        @Test
        void setPseudoColumnAsEnum_Null_Null() {
            final var spy = newTypeSpy();
            spy.setPseudoColumnAsEnum(null);
            verify(spy, times(1)).setPseudoColumn(null);
        }

        @DisplayName("setPseudoColumnAsEnum(non-null)")
        @EnumSource(BestRowIdentifier.PseudoColumn.class)
        @ParameterizedTest
        void setPseudoColumnAsEnum__(final BestRowIdentifier.PseudoColumn pseudoColumnAsEnum) {
            final var spy = newTypeSpy();
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
            final var spy = newTypeSpy();
            final var scopeAsEnum = spy.getScopeAsEnum();
            assertThat(scopeAsEnum).isNull();
            verify(spy, times(1)).getScope();
        }

        @DisplayName("setScopeAsEnum(null)")
        @Test
        void setScopeAsEnum_Null_Null() {
            final var spy = newTypeSpy();
            spy.setScopeAsEnum(null);
            verify(spy, times(1)).setScope(null);
        }

        @DisplayName("setScopeAsEnum(non-null)")
        @EnumSource(BestRowIdentifier.Scope.class)
        @ParameterizedTest
        void setScopeAsEnum__(final BestRowIdentifier.Scope scopeAsEnum) {
            final var spy = newTypeSpy();
            spy.setScopeAsEnum(scopeAsEnum);
            verify(spy, times(1)).setScope(scopeAsEnum.fieldValueAsInt());
        }
    }
}
