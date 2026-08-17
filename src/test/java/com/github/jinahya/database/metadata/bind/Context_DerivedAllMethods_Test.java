package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class Context_DerivedAllMethods_Test {

    @SuppressWarnings("unchecked")
    private static <T> Consumer<? super T> mockConsumer() {
        return mock(Consumer.class);
    }

    @Nested
    class AttributeTest {

        @Test
        void forEachAttribute_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super Attribute> consumer = mockConsumer();
            doNothing().when(context).getAttributesAndAcceptEach(null, null, "%", "%", consumer);
            context.forEachAttribute(consumer);
            verify(context).getAttributesAndAcceptEach(null, null, "%", "%", consumer);
            verify(context, never()).getAttributes(null, null, "%", "%");
        }

        @Test
        void getAllAttributes_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var attributes = List.<Attribute>of();
            doReturn(attributes).when(context).getAttributes(null, null, "%", "%");
            assertThat(context.getAllAttributes()).isSameAs(attributes);
            verify(context).getAttributes(null, null, "%", "%");
        }
    }

    @Nested
    class ColumnTest {

        @Test
        void forEachColumn_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super Column> consumer = mockConsumer();
            doNothing().when(context).getColumnsAndAcceptEach(null, null, "%", "%", consumer);
            context.forEachColumn(consumer);
            verify(context).getColumnsAndAcceptEach(null, null, "%", "%", consumer);
            verify(context, never()).getColumns(null, null, "%", "%");
        }

        @Test
        void getAllColumns_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var columns = List.<Column>of();
            doReturn(columns).when(context).getColumns(null, null, "%", "%");
            assertThat(context.getAllColumns()).isSameAs(columns);
            verify(context).getColumns(null, null, "%", "%");
        }
    }

    @Nested
    class FunctionTest {

        @Test
        void forEachFunction_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super Function> consumer = mockConsumer();
            doNothing().when(context).getFunctionsAndAcceptEach(null, null, "%", consumer);
            context.forEachFunction(consumer);
            verify(context).getFunctionsAndAcceptEach(null, null, "%", consumer);
            verify(context, never()).getFunctions(null, null, "%");
        }

        @Test
        void getAllFunctions_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var functions = List.<Function>of();
            doReturn(functions).when(context).getFunctions(null, null, "%");
            assertThat(context.getAllFunctions()).isSameAs(functions);
            verify(context).getFunctions(null, null, "%");
        }
    }

    @Nested
    class FunctionColumnTest {

        @Test
        void forEachFunctionColumn_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super FunctionColumn> consumer = mockConsumer();
            doNothing().when(context).getFunctionColumnsAndAcceptEach(null, null, "%", "%", consumer);
            context.forEachFunctionColumn(consumer);
            verify(context).getFunctionColumnsAndAcceptEach(null, null, "%", "%", consumer);
            verify(context, never()).getFunctionColumns(null, null, "%", "%");
        }

        @Test
        void getAllFunctionColumns_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var functionColumns = List.<FunctionColumn>of();
            doReturn(functionColumns).when(context).getFunctionColumns(null, null, "%", "%");
            assertThat(context.getAllFunctionColumns()).isSameAs(functionColumns);
            verify(context).getFunctionColumns(null, null, "%", "%");
        }
    }

    @Nested
    class ProcedureTest {

        @Test
        void forEachProcedure_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super Procedure> consumer = mockConsumer();
            doNothing().when(context).getProceduresAndAcceptEach(null, null, "%", consumer);
            context.forEachProcedure(consumer);
            verify(context).getProceduresAndAcceptEach(null, null, "%", consumer);
            verify(context, never()).getProcedures(null, null, "%");
        }

        @Test
        void getAllProcedures_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var procedures = List.<Procedure>of();
            doReturn(procedures).when(context).getProcedures(null, null, "%");
            assertThat(context.getAllProcedures()).isSameAs(procedures);
            verify(context).getProcedures(null, null, "%");
        }
    }

    @Nested
    class ProcedureColumnTest {

        @Test
        void forEachProcedureColumn_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super ProcedureColumn> consumer = mockConsumer();
            doNothing().when(context).getProcedureColumnsAndAcceptEach(null, null, "%", "%", consumer);
            context.forEachProcedureColumn(consumer);
            verify(context).getProcedureColumnsAndAcceptEach(null, null, "%", "%", consumer);
            verify(context, never()).getProcedureColumns(null, null, "%", "%");
        }

        @Test
        void getAllProcedureColumns_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var procedureColumns = List.<ProcedureColumn>of();
            doReturn(procedureColumns).when(context).getProcedureColumns(null, null, "%", "%");
            assertThat(context.getAllProcedureColumns()).isSameAs(procedureColumns);
            verify(context).getProcedureColumns(null, null, "%", "%");
        }
    }

    @Nested
    class PseudoColumnTest {

        @Test
        void forEachPseudoColumn_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super PseudoColumn> consumer = mockConsumer();
            doNothing().when(context).getPseudoColumnsAndAcceptEach(null, null, "%", "%", consumer);
            context.forEachPseudoColumn(consumer);
            verify(context).getPseudoColumnsAndAcceptEach(null, null, "%", "%", consumer);
            verify(context, never()).getPseudoColumns(null, null, "%", "%");
        }

        @Test
        void getAllPseudoColumns_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var pseudoColumns = List.<PseudoColumn>of();
            doReturn(pseudoColumns).when(context).getPseudoColumns(null, null, "%", "%");
            assertThat(context.getAllPseudoColumns()).isSameAs(pseudoColumns);
            verify(context).getPseudoColumns(null, null, "%", "%");
        }
    }

    @Nested
    class TableTest {

        @Test
        void forEachTable_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super Table> consumer = mockConsumer();
            doNothing().when(context).getTablesAndAcceptEach(null, null, "%", null, consumer);
            context.forEachTable(consumer);
            verify(context).getTablesAndAcceptEach(null, null, "%", null, consumer);
            verify(context, never()).getTables(null, null, "%", null);
        }

        @Test
        void getAllTables_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var tables = List.<Table>of();
            doReturn(tables).when(context).getTables(null, null, "%", null);
            assertThat(context.getAllTables()).isSameAs(tables);
            verify(context).getTables(null, null, "%", null);
        }
    }

    @Nested
    class TablePrivilegeTest {

        @Test
        void forEachTablePrivilege_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super TablePrivilege> consumer = mockConsumer();
            doNothing().when(context).getTablePrivilegesAndAcceptEach(null, null, "%", consumer);
            context.forEachTablePrivilege(consumer);
            verify(context).getTablePrivilegesAndAcceptEach(null, null, "%", consumer);
            verify(context, never()).getTablePrivileges(null, null, "%");
        }

        @Test
        void getAllTablePrivileges_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var tablePrivileges = List.<TablePrivilege>of();
            doReturn(tablePrivileges).when(context).getTablePrivileges(null, null, "%");
            assertThat(context.getAllTablePrivileges()).isSameAs(tablePrivileges);
            verify(context).getTablePrivileges(null, null, "%");
        }
    }

    @Nested
    class UDTTest {

        @Test
        void forEachUDT_DelegatesToAcceptEach_BypassingList() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final Consumer<? super UDT> consumer = mockConsumer();
            doNothing().when(context).getUDTsAndAcceptEach(null, null, "%", null, consumer);
            context.forEachUDT(consumer);
            verify(context).getUDTsAndAcceptEach(null, null, "%", null, consumer);
            verify(context, never()).getUDTs(null, null, "%", null);
        }

        @Test
        void getAllUDTs_DelegatesToPatternMethodWithWildcards() throws SQLException {
            final var context = spy(new Context(mock(DatabaseMetaData.class)));
            final var udts = List.<UDT>of();
            doReturn(udts).when(context).getUDTs(null, null, "%", null);
            assertThat(context.getAllUDTs()).isSameAs(udts);
            verify(context).getUDTs(null, null, "%", null);
        }
    }
}
