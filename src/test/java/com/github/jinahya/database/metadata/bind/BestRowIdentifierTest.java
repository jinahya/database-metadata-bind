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

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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


    @Nested
    class EqualsTest {

        @Test
        void __() {
            /**
             * BestRowIdentifier(super=AbstractMetadataType(super=com.github.jinahya.database.metadata.bind.BestRowIdentifier@f5b2bd66, unmappedValues={}),
             *     parent=Table(super=AbstractMetadataType(super=com.github.jinahya.database.metadata.bind.Table@c34c60c9, unmappedValues={}),
             *         tableCat=null,829
             *         tableSchem=null,
             *         tableName=replication_asynchronous_connection_failover,
             *         tableType=null, remarks=null, typeCat=null, typeSchem=null, typeName=null, selfReferencingColName=null, refGeneration=null),
             *     scope=2,
             *     columnName=Channel_name,
             *     dataType=1, typeName=char, columnSize=64, bufferLength=null, decimalDigits=0, pseudoColumn=1),
             *
             * BestRowIdentifier(super=AbstractMetadataType(super=com.github.jinahya.database.metadata.bind.BestRowIdentifier@f0e272c7, unmappedValues={}),
             *     parent=Table(super=AbstractMetadataType(super=com.github.jinahya.database.metadata.bind.Table@c34c60c9, unmappedValues={}), tableCat=null, tableSchem=null,
             *         tableName=replication_asynchronous_connection_failover,
             *         tableType=null, remarks=null, typeCat=null, typeSchem=null, typeName=null, selfReferencingColName=null, refGeneration=null),
             *     scope=2,
             *     columnName=Host,
             *     dataType=1, typeName=char, columnSize=255, bufferLength=null, decimalDigits=0, pseudoColumn=1),
             */
            final var v1 = new BestRowIdentifier();
            v1.setParent(Table.of(null, null, "replication_asynchronous_connection_failover"));
            v1.setScope(2);
            v1.setColumnName("Channel_name");
            log.debug("v1.hashCode: {}", v1.hashCode());

            final var v2 = new BestRowIdentifier();
            v2.setParent(Table.of(null, null, "replication_asynchronous_connection_failover"));
            v2.setScope(2);
            v2.setColumnName("Host");
            log.debug("v2.hashCode: {}", v2.hashCode());

            assertThat(v1).isNotEqualTo(v2);
        }
    }
}
