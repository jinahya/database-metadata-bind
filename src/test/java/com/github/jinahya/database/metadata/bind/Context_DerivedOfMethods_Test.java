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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class Context_DerivedOfMethods_Test {

    @Nested
    class AttributeTest {

        @Test
        void getAttributesOf_DelegatesWithUdtTypeFields() throws SQLException {
            final var context = context();
            final var attributes = List.<Attribute>of();
            doReturn(attributes).when(context).getAttributes("tc", "ts", "tn", "a");
            assertThat(context.getAttributesOf(udt(), "a")).isSameAs(attributes);
            verify(context).getAttributes("tc", "ts", "tn", "a");
        }

        @Test
        void forEachAttributeOf_DelegatesWithUdtTypeFields() throws SQLException {
            final var context = context();
            final Consumer<? super Attribute> consumer = consumer();
            doNothing().when(context).forEachAttribute("tc", "ts", "tn", "a", consumer);
            context.forEachAttributeOf(udt(), "a", consumer);
            verify(context).forEachAttribute("tc", "ts", "tn", "a", consumer);
        }
    }

    @Nested
    class CatalogScopedTest {

        @Test
        void functions_DelegateWithCatalog() throws SQLException {
            final var context = context();
            final var functions = List.<Function>of();
            final Consumer<? super Function> consumer = consumer();
            doReturn(functions).when(context).getFunctions("cat", "s", "f");
            doNothing().when(context).forEachFunction("cat", "s", "f", consumer);
            assertThat(context.getFunctionsOf(catalog(), "s", "f")).isSameAs(functions);
            context.forEachFunctionOf(catalog(), "s", "f", consumer);
            verify(context).getFunctions("cat", "s", "f");
            verify(context).forEachFunction("cat", "s", "f", consumer);
        }

        @Test
        void procedures_DelegateWithCatalog() throws SQLException {
            final var context = context();
            final var procedures = List.<Procedure>of();
            final Consumer<? super Procedure> consumer = consumer();
            doReturn(procedures).when(context).getProcedures("cat", "s", "p");
            doNothing().when(context).forEachProcedure("cat", "s", "p", consumer);
            assertThat(context.getProceduresOf(catalog(), "s", "p")).isSameAs(procedures);
            context.forEachProcedureOf(catalog(), "s", "p", consumer);
            verify(context).getProcedures("cat", "s", "p");
            verify(context).forEachProcedure("cat", "s", "p", consumer);
        }

        @Test
        void schemas_DelegateWithCatalog() throws SQLException {
            final var context = context();
            final var schemas = List.<Schema>of();
            final Consumer<? super Schema> consumer = consumer();
            doReturn(schemas).when(context).getSchemas("cat", "s");
            doNothing().when(context).forEachSchema("cat", "s", consumer);
            assertThat(context.getSchemasOf(catalog(), "s")).isSameAs(schemas);
            context.forEachSchemaOf(catalog(), "s", consumer);
            verify(context).getSchemas("cat", "s");
            verify(context).forEachSchema("cat", "s", consumer);
        }

        @Test
        void superTables_DelegateWithCatalog() throws SQLException {
            final var context = context();
            final var superTables = List.<SuperTable>of();
            final Consumer<? super SuperTable> consumer = consumer();
            doReturn(superTables).when(context).getSuperTables("cat", "s", "t");
            doNothing().when(context).forEachSuperTable("cat", "s", "t", consumer);
            assertThat(context.getSuperTablesOf(catalog(), "s", "t")).isSameAs(superTables);
            context.forEachSuperTableOf(catalog(), "s", "t", consumer);
            verify(context).getSuperTables("cat", "s", "t");
            verify(context).forEachSuperTable("cat", "s", "t", consumer);
        }

        @Test
        void superTypes_DelegateWithCatalog() throws SQLException {
            final var context = context();
            final var superTypes = List.<SuperType>of();
            final Consumer<? super SuperType> consumer = consumer();
            doReturn(superTypes).when(context).getSuperTypes("cat", "s", "t");
            doNothing().when(context).forEachSuperType("cat", "s", "t", consumer);
            assertThat(context.getSuperTypesOf(catalog(), "s", "t")).isSameAs(superTypes);
            context.forEachSuperTypeOf(catalog(), "s", "t", consumer);
            verify(context).getSuperTypes("cat", "s", "t");
            verify(context).forEachSuperType("cat", "s", "t", consumer);
        }

        @Test
        void tablePrivileges_DelegateWithCatalog() throws SQLException {
            final var context = context();
            final var tablePrivileges = List.<TablePrivilege>of();
            final Consumer<? super TablePrivilege> consumer = consumer();
            doReturn(tablePrivileges).when(context).getTablePrivileges("cat", "s", "t");
            doNothing().when(context).forEachTablePrivilege("cat", "s", "t", consumer);
            assertThat(context.getTablePrivilegesOf(catalog(), "s", "t")).isSameAs(tablePrivileges);
            context.forEachTablePrivilegeOf(catalog(), "s", "t", consumer);
            verify(context).getTablePrivileges("cat", "s", "t");
            verify(context).forEachTablePrivilege("cat", "s", "t", consumer);
        }

        @Test
        void tables_DelegateWithCatalog() throws SQLException {
            final var context = context();
            final var tables = List.<Table>of();
            final String[] types = {"TABLE"};
            final Consumer<? super Table> consumer = consumer();
            doReturn(tables).when(context).getTables("cat", "s", "t", types);
            doNothing().when(context).forEachTable("cat", "s", "t", types, consumer);
            assertThat(context.getTablesOf(catalog(), "s", "t", types)).isSameAs(tables);
            context.forEachTableOf(catalog(), "s", "t", types, consumer);
            verify(context).getTables("cat", "s", "t", types);
            verify(context).forEachTable("cat", "s", "t", types, consumer);
        }

        @Test
        void udts_DelegateWithCatalog() throws SQLException {
            final var context = context();
            final var udts = List.<UDT>of();
            final int[] types = {java.sql.Types.STRUCT};
            final Consumer<? super UDT> consumer = consumer();
            doReturn(udts).when(context).getUDTs("cat", "s", "u", types);
            doNothing().when(context).forEachUDT("cat", "s", "u", types, consumer);
            assertThat(context.getUDTsOf(catalog(), "s", "u", types)).isSameAs(udts);
            context.forEachUDTOf(catalog(), "s", "u", types, consumer);
            verify(context).getUDTs("cat", "s", "u", types);
            verify(context).forEachUDT("cat", "s", "u", types, consumer);
        }
    }

    @Nested
    class SchemaScopedTest {

        @Test
        void functions_DelegateWithSchema() throws SQLException {
            final var context = context();
            final var functions = List.<Function>of();
            final Consumer<? super Function> consumer = consumer();
            doReturn(functions).when(context).getFunctions("sc", "ss", "f");
            doNothing().when(context).forEachFunction("sc", "ss", "f", consumer);
            assertThat(context.getFunctionsOf(schema(), "f")).isSameAs(functions);
            context.forEachFunctionOf(schema(), "f", consumer);
            verify(context).getFunctions("sc", "ss", "f");
            verify(context).forEachFunction("sc", "ss", "f", consumer);
        }

        @Test
        void procedures_DelegateWithSchema() throws SQLException {
            final var context = context();
            final var procedures = List.<Procedure>of();
            final Consumer<? super Procedure> consumer = consumer();
            doReturn(procedures).when(context).getProcedures("sc", "ss", "p");
            doNothing().when(context).forEachProcedure("sc", "ss", "p", consumer);
            assertThat(context.getProceduresOf(schema(), "p")).isSameAs(procedures);
            context.forEachProcedureOf(schema(), "p", consumer);
            verify(context).getProcedures("sc", "ss", "p");
            verify(context).forEachProcedure("sc", "ss", "p", consumer);
        }

        @Test
        void superTables_DelegateWithSchema() throws SQLException {
            final var context = context();
            final var superTables = List.<SuperTable>of();
            final Consumer<? super SuperTable> consumer = consumer();
            doReturn(superTables).when(context).getSuperTables("sc", "ss", "t");
            doNothing().when(context).forEachSuperTable("sc", "ss", "t", consumer);
            assertThat(context.getSuperTablesOf(schema(), "t")).isSameAs(superTables);
            context.forEachSuperTableOf(schema(), "t", consumer);
            verify(context).getSuperTables("sc", "ss", "t");
            verify(context).forEachSuperTable("sc", "ss", "t", consumer);
        }

        @Test
        void superTypes_DelegateWithSchema() throws SQLException {
            final var context = context();
            final var superTypes = List.<SuperType>of();
            final Consumer<? super SuperType> consumer = consumer();
            doReturn(superTypes).when(context).getSuperTypes("sc", "ss", "t");
            doNothing().when(context).forEachSuperType("sc", "ss", "t", consumer);
            assertThat(context.getSuperTypesOf(schema(), "t")).isSameAs(superTypes);
            context.forEachSuperTypeOf(schema(), "t", consumer);
            verify(context).getSuperTypes("sc", "ss", "t");
            verify(context).forEachSuperType("sc", "ss", "t", consumer);
        }

        @Test
        void tablePrivileges_DelegateWithSchema() throws SQLException {
            final var context = context();
            final var tablePrivileges = List.<TablePrivilege>of();
            final Consumer<? super TablePrivilege> consumer = consumer();
            doReturn(tablePrivileges).when(context).getTablePrivileges("sc", "ss", "t");
            doNothing().when(context).forEachTablePrivilege("sc", "ss", "t", consumer);
            assertThat(context.getTablePrivilegesOf(schema(), "t")).isSameAs(tablePrivileges);
            context.forEachTablePrivilegeOf(schema(), "t", consumer);
            verify(context).getTablePrivileges("sc", "ss", "t");
            verify(context).forEachTablePrivilege("sc", "ss", "t", consumer);
        }

        @Test
        void tables_DelegateWithSchema() throws SQLException {
            final var context = context();
            final var tables = List.<Table>of();
            final String[] types = {"TABLE"};
            final Consumer<? super Table> consumer = consumer();
            doReturn(tables).when(context).getTables("sc", "ss", "t", types);
            doNothing().when(context).forEachTable("sc", "ss", "t", types, consumer);
            assertThat(context.getTablesOf(schema(), "t", types)).isSameAs(tables);
            context.forEachTableOf(schema(), "t", types, consumer);
            verify(context).getTables("sc", "ss", "t", types);
            verify(context).forEachTable("sc", "ss", "t", types, consumer);
        }

        @Test
        void udts_DelegateWithSchema() throws SQLException {
            final var context = context();
            final var udts = List.<UDT>of();
            final int[] types = {java.sql.Types.STRUCT};
            final Consumer<? super UDT> consumer = consumer();
            doReturn(udts).when(context).getUDTs("sc", "ss", "u", types);
            doNothing().when(context).forEachUDT("sc", "ss", "u", types, consumer);
            assertThat(context.getUDTsOf(schema(), "u", types)).isSameAs(udts);
            context.forEachUDTOf(schema(), "u", types, consumer);
            verify(context).getUDTs("sc", "ss", "u", types);
            verify(context).forEachUDT("sc", "ss", "u", types, consumer);
        }
    }

    @Nested
    class TableScopedTest {

        @Test
        void dependentMetadata_DelegateWithTableFields() throws SQLException {
            final var context = context();
            final var table = table();
            final Consumer<? super BestRowIdentifier> bestRows = consumer();
            final Consumer<? super ColumnPrivilege> columnPrivileges = consumer();
            final Consumer<? super Column> columns = consumer();
            final Consumer<? super ExportedKey> exportedKeys = consumer();
            final Consumer<? super ImportedKey> importedKeys = consumer();
            final Consumer<? super IndexInfo> indexInfo = consumer();
            final Consumer<? super PrimaryKey> primaryKeys = consumer();
            final Consumer<? super PseudoColumn> pseudoColumns = consumer();
            final Consumer<? super SuperTable> superTables = consumer();
            final Consumer<? super TablePrivilege> tablePrivileges = consumer();
            final Consumer<? super VersionColumn> versionColumns = consumer();
            doNothing().when(context).forEachBestRowIdentifier("tc", "ts", "tn", 1, true, bestRows);
            context.forEachBestRowIdentifierOf(table, 1, true, bestRows);
            verify(context).forEachBestRowIdentifier("tc", "ts", "tn", 1, true, bestRows);
            doReturn(List.<BestRowIdentifier>of()).when(context).getBestRowIdentifier("tc", "ts", "tn", 1, true);
            assertThat(context.getBestRowIdentifierOf(table, 1, true)).isEmpty();
            verify(context).getBestRowIdentifier("tc", "ts", "tn", 1, true);
            doNothing().when(context).forEachColumnPrivilege("tc", "ts", "tn", "c", columnPrivileges);
            context.forEachColumnPrivilegeOf(table, "c", columnPrivileges);
            verify(context).forEachColumnPrivilege("tc", "ts", "tn", "c", columnPrivileges);
            doReturn(List.<ColumnPrivilege>of()).when(context).getColumnPrivileges("tc", "ts", "tn", "c");
            assertThat(context.getColumnPrivilegesOf(table, "c")).isEmpty();
            verify(context).getColumnPrivileges("tc", "ts", "tn", "c");
            doNothing().when(context).forEachColumn("tc", "ts", "tn", "c", columns);
            context.forEachColumnOf(table, "c", columns);
            verify(context).forEachColumn("tc", "ts", "tn", "c", columns);
            doReturn(List.<Column>of()).when(context).getColumns("tc", "ts", "tn", "c");
            assertThat(context.getColumnsOf(table, "c")).isEmpty();
            verify(context).getColumns("tc", "ts", "tn", "c");
            doNothing().when(context).forEachExportedKey("tc", "ts", "tn", exportedKeys);
            context.forEachExportedKeyOf(table, exportedKeys);
            verify(context).forEachExportedKey("tc", "ts", "tn", exportedKeys);
            doReturn(List.<ExportedKey>of()).when(context).getExportedKeys("tc", "ts", "tn");
            assertThat(context.getExportedKeysOf(table)).isEmpty();
            verify(context).getExportedKeys("tc", "ts", "tn");
            doNothing().when(context).forEachImportedKey("tc", "ts", "tn", importedKeys);
            context.forEachImportedKeyOf(table, importedKeys);
            verify(context).forEachImportedKey("tc", "ts", "tn", importedKeys);
            doReturn(List.<ImportedKey>of()).when(context).getImportedKeys("tc", "ts", "tn");
            assertThat(context.getImportedKeysOf(table)).isEmpty();
            verify(context).getImportedKeys("tc", "ts", "tn");
            doNothing().when(context).forEachIndexInfo("tc", "ts", "tn", true, false, indexInfo);
            context.forEachIndexInfoOf(table, true, false, indexInfo);
            verify(context).forEachIndexInfo("tc", "ts", "tn", true, false, indexInfo);
            doReturn(List.<IndexInfo>of()).when(context).getIndexInfo("tc", "ts", "tn", true, false);
            assertThat(context.getIndexInfoOf(table, true, false)).isEmpty();
            verify(context).getIndexInfo("tc", "ts", "tn", true, false);
            doNothing().when(context).forEachPrimaryKey("tc", "ts", "tn", primaryKeys);
            context.forEachPrimaryKeyOf(table, primaryKeys);
            verify(context).forEachPrimaryKey("tc", "ts", "tn", primaryKeys);
            doReturn(List.<PrimaryKey>of()).when(context).getPrimaryKeys("tc", "ts", "tn");
            assertThat(context.getPrimaryKeysOf(table)).isEmpty();
            verify(context).getPrimaryKeys("tc", "ts", "tn");
            doNothing().when(context).forEachPseudoColumn("tc", "ts", "tn", "p", pseudoColumns);
            context.forEachPseudoColumnOf(table, "p", pseudoColumns);
            verify(context).forEachPseudoColumn("tc", "ts", "tn", "p", pseudoColumns);
            doReturn(List.<PseudoColumn>of()).when(context).getPseudoColumns("tc", "ts", "tn", "p");
            assertThat(context.getPseudoColumnsOf(table, "p")).isEmpty();
            verify(context).getPseudoColumns("tc", "ts", "tn", "p");
            doNothing().when(context).forEachSuperTable("tc", "ts", "tn", superTables);
            context.forEachSuperTableOf(table, superTables);
            verify(context).forEachSuperTable("tc", "ts", "tn", superTables);
            doReturn(List.<SuperTable>of()).when(context).getSuperTables("tc", "ts", "tn");
            assertThat(context.getSuperTablesOf(table)).isEmpty();
            verify(context).getSuperTables("tc", "ts", "tn");
            doNothing().when(context).forEachTablePrivilege("tc", "ts", "tn", tablePrivileges);
            context.forEachTablePrivilegeOf(table, tablePrivileges);
            verify(context).forEachTablePrivilege("tc", "ts", "tn", tablePrivileges);
            doReturn(List.<TablePrivilege>of()).when(context).getTablePrivileges("tc", "ts", "tn");
            assertThat(context.getTablePrivilegesOf(table)).isEmpty();
            verify(context).getTablePrivileges("tc", "ts", "tn");
            doNothing().when(context).forEachVersionColumn("tc", "ts", "tn", versionColumns);
            context.forEachVersionColumnOf(table, versionColumns);
            verify(context).forEachVersionColumn("tc", "ts", "tn", versionColumns);
            doReturn(List.<VersionColumn>of()).when(context).getVersionColumns("tc", "ts", "tn");
            assertThat(context.getVersionColumnsOf(table)).isEmpty();
            verify(context).getVersionColumns("tc", "ts", "tn");
        }

        @Test
        void crossReference_DelegatesWithBothTables() throws SQLException {
            final var context = context();
            final var foreign = new Table();
            foreign.setTableCat("fc");
            foreign.setTableSchem("fs");
            foreign.setTableName("fn");
            final var references = List.<CrossReference>of();
            final Consumer<? super CrossReference> consumer = consumer();
            doReturn(references).when(context).getCrossReference("tc", "ts", "tn", "fc", "fs", "fn");
            doNothing().when(context).forEachCrossReference("tc", "ts", "tn", "fc", "fs", "fn", consumer);
            assertThat(context.getCrossReferenceOf(table(), foreign)).isSameAs(references);
            context.forEachCrossReferenceOf(table(), foreign, consumer);
            verify(context).getCrossReference("tc", "ts", "tn", "fc", "fs", "fn");
            verify(context).forEachCrossReference("tc", "ts", "tn", "fc", "fs", "fn", consumer);
        }
    }

    @Nested
    class ChildScopedTest {

        @Test
        void functionColumns_DelegateWithFunctionFields() throws SQLException {
            final var context = context();
            final var columns = List.<FunctionColumn>of();
            final Consumer<? super FunctionColumn> consumer = consumer();
            doReturn(columns).when(context).getFunctionColumns("fc", "fs", "fn", "c");
            doNothing().when(context).forEachFunctionColumn("fc", "fs", "fn", "c", consumer);
            assertThat(context.getFunctionColumnsOf(function(), "c")).isSameAs(columns);
            context.forEachFunctionColumnOf(function(), "c", consumer);
            verify(context).getFunctionColumns("fc", "fs", "fn", "c");
            verify(context).forEachFunctionColumn("fc", "fs", "fn", "c", consumer);
        }

        @Test
        void procedureColumns_DelegateWithProcedureFields() throws SQLException {
            final var context = context();
            final var columns = List.<ProcedureColumn>of();
            final Consumer<? super ProcedureColumn> consumer = consumer();
            doReturn(columns).when(context).getProcedureColumns("pc", "ps", "pn", "c");
            doNothing().when(context).forEachProcedureColumn("pc", "ps", "pn", "c", consumer);
            assertThat(context.getProcedureColumnsOf(procedure(), "c")).isSameAs(columns);
            context.forEachProcedureColumnOf(procedure(), "c", consumer);
            verify(context).getProcedureColumns("pc", "ps", "pn", "c");
            verify(context).forEachProcedureColumn("pc", "ps", "pn", "c", consumer);
        }

        @Test
        void superTypes_DelegateWithUdtFields() throws SQLException {
            final var context = context();
            final var superTypes = List.<SuperType>of();
            final Consumer<? super SuperType> consumer = consumer();
            doReturn(superTypes).when(context).getSuperTypes("tc", "ts", "tn");
            doNothing().when(context).forEachSuperType("tc", "ts", "tn", consumer);
            assertThat(context.getSuperTypesOf(udt())).isSameAs(superTypes);
            context.forEachSuperTypeOf(udt(), consumer);
            verify(context).getSuperTypes("tc", "ts", "tn");
            verify(context).forEachSuperType("tc", "ts", "tn", consumer);
        }
    }

    private static Context context() {
        return spy(new Context(mock(DatabaseMetaData.class)));
    }

    @SuppressWarnings("unchecked")
    private static <T> Consumer<? super T> consumer() {
        return mock(Consumer.class);
    }

    private static Catalog catalog() {
        final var catalog = new Catalog();
        catalog.setTableCat("cat");
        return catalog;
    }

    private static Schema schema() {
        final var schema = new Schema();
        schema.setTableCatalog("sc");
        schema.setTableSchem("ss");
        return schema;
    }

    private static Table table() {
        final var table = new Table();
        table.setTableCat("tc");
        table.setTableSchem("ts");
        table.setTableName("tn");
        return table;
    }

    private static Function function() {
        final var function = new Function();
        function.setFunctionCat("fc");
        function.setFunctionSchem("fs");
        function.setFunctionName("fn");
        return function;
    }

    private static Procedure procedure() {
        final var procedure = new Procedure();
        procedure.setProcedureCat("pc");
        procedure.setProcedureSchem("ps");
        procedure.setProcedureName("pn");
        return procedure;
    }

    private static UDT udt() {
        final var udt = new UDT();
        udt.setTypeCat("tc");
        udt.setTypeSchem("ts");
        udt.setTypeName("tn");
        return udt;
    }
}
