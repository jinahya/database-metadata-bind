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
import java.sql.SQLFeatureNotSupportedException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ContextNavigationUtilsTest {

    // The logic shared by every kind is covered here, through tables.
    @Nested
    class TablesTest {

        @Test
        void __nullArguments() {
            final var context = context();
            assertThatThrownBy(() -> ContextNavigationUtils.getCatalogsAndSchemasOfTables(null, "%"))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> ContextNavigationUtils.getCatalogsAndSchemasOfTables(context, null))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(null, "%"))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void __oneNonNarrowingLookupWithVerbatimPatternAndTypes() throws SQLException {
            final var context = context();
            feed(context);
            final var types = new String[] {"VIEW"};
            ContextNavigationUtils.getCatalogsAndSchemasOfTables(context, "A_B%");
            ContextNavigationUtils.getCatalogsAndSchemasOfTables(context, "A_B%", types);
            verify(context).forEachTable(isNull(), isNull(), eq("A_B%"), isNull(), any());
            verify(context).forEachTable(isNull(), isNull(), eq("A_B%"), same(types), any());
        }

        @Test
        void list__distinctPairsAsReportedInFirstSeenOrder() throws SQLException {
            final var context = context();
            feed(context,
                 table("c2", "s1"),
                 table("c1", null),
                 table(null, "s1"),
                 table("c2", "s1"),
                 table(null, null),
                 table("C2", "s1"),
                 table(null, null));
            assertThat(ContextNavigationUtils.getCatalogsAndSchemasOfTables(context, "t"))
                    .containsExactly(
                            new CatalogAndSchema("c2", "s1"),
                            new CatalogAndSchema("c1", null),
                            new CatalogAndSchema(null, "s1"),
                            new CatalogAndSchema(null, null),
                            new CatalogAndSchema("C2", "s1"));
        }

        @Test
        void list__emptyWhenNothingMatches() throws SQLException {
            final var context = context();
            feed(context);
            assertThat(ContextNavigationUtils.getCatalogsAndSchemasOfTables(context, "t")).isEmpty();
        }

        @Test
        void only__nothingMatched_NoSuchElement() throws SQLException {
            final var context = context();
            feed(context);
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "t"))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("'t'");
        }

        @Test
        void only__onePair_Returned() throws SQLException {
            final var context = context();
            feed(context, table("c", "s"), table("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "t%"))
                    .isEqualTo(new CatalogAndSchema("c", "s"));
        }

        @Test
        void only__catalogWithoutSchema_ReturnedWhole() throws SQLException {
            final var context = context();
            feed(context, table("tenant_a", null));
            assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "t"))
                    .isEqualTo(new CatalogAndSchema("tenant_a", null));
        }

        @Test
        void only__noLevelAtAll_ReturnedAsNullPair() throws SQLException {
            final var context = context();
            feed(context, table(null, null), table(null, null));
            assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "t%"))
                    .isEqualTo(new CatalogAndSchema(null, null));
        }

        @Test
        void only__twoSchemasInOneCatalog_IllegalArgument() throws SQLException {
            final var context = context();
            feed(context, table("tenant_a", "s1"), table("tenant_a", "s2"));
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "t"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("s1")
                    .hasMessageContaining("s2");
        }

        @Test
        void only__sameSchemaInTwoCatalogs_IllegalArgument() throws SQLException {
            final var context = context();
            feed(context, table("c1", "s"), table("c2", "s"));
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "t"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void only__someWithLevelSomeWithout_IllegalArgument() throws SQLException {
            final var context = context();
            feed(context, table("c", "s"), table("c", null));
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "t"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void only__caseVariants_IllegalArgument() throws SQLException {
            final var context = context();
            feed(context, table("c", "s"), table("c", "S"));
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "t"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // These pin, per kind, the lookup made, the columns read, and that a lookup failure propagates.

    @Nested
    class ProceduresTest {

        @Test
        void __lookupAndColumns() throws SQLException {
            final var context = context();
            doAnswer(i -> {
                final Consumer<? super Procedure> consumer = i.getArgument(3);
                consumer.accept(procedure("c", "s"));
                consumer.accept(procedure("c", "s"));
                return null;
            }).when(context).forEachProcedure(any(), any(), any(), any());
            assertThat(ContextNavigationUtils.getCatalogsAndSchemasOfProcedures(context, "p_%"))
                    .containsExactly(new CatalogAndSchema("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfProcedures(context, "p_%"))
                    .isEqualTo(new CatalogAndSchema("c", "s"));
            verify(context, times(2)).forEachProcedure(isNull(), isNull(), eq("p_%"), any());
        }

        @Test
        void __packagesAsReportedYieldDistinctPairs() throws SQLException {
            final var context = context();
            doAnswer(i -> {
                final Consumer<? super Procedure> consumer = i.getArgument(3);
                consumer.accept(procedure("BILLING", "APP"));
                consumer.accept(procedure("SHIPPING", "APP"));
                return null;
            }).when(context).forEachProcedure(any(), any(), any(), any());
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfProcedures(context, "RUN"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FunctionsTest {

        @Test
        void __lookupAndColumns() throws SQLException {
            final var context = context();
            doAnswer(i -> {
                final Consumer<? super Function> consumer = i.getArgument(3);
                consumer.accept(function("c", "s"));
                consumer.accept(function("c", "s"));
                return null;
            }).when(context).forEachFunction(any(), any(), any(), any());
            assertThat(ContextNavigationUtils.getCatalogsAndSchemasOfFunctions(context, "f_%"))
                    .containsExactly(new CatalogAndSchema("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfFunctions(context, "f_%"))
                    .isEqualTo(new CatalogAndSchema("c", "s"));
            verify(context, times(2)).forEachFunction(isNull(), isNull(), eq("f_%"), any());
        }

        @Test
        void __unsupportedLookupPropagates() throws SQLException {
            final var context = context();
            doThrow(new SQLFeatureNotSupportedException())
                    .when(context).forEachFunction(any(), any(), any(), any());
            assertThatThrownBy(() -> ContextNavigationUtils.getCatalogsAndSchemasOfFunctions(context, "%"))
                    .isInstanceOf(SQLFeatureNotSupportedException.class);
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfFunctions(context, "%"))
                    .isInstanceOf(SQLFeatureNotSupportedException.class);
        }
    }

    @Nested
    class UDTsTest {

        @Test
        void __lookupColumnsAndTypes() throws SQLException {
            final var context = context();
            doAnswer(i -> {
                final Consumer<? super UDT> consumer = i.getArgument(4);
                consumer.accept(udt("c", "s"));
                consumer.accept(udt("c", "s"));
                return null;
            }).when(context).forEachUDT(any(), any(), any(), any(), any());
            final var types = new int[] {java.sql.Types.DISTINCT};
            assertThat(ContextNavigationUtils.getCatalogsAndSchemasOfUDTs(context, "u_%"))
                    .containsExactly(new CatalogAndSchema("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfUDTs(context, "u_%"))
                    .isEqualTo(new CatalogAndSchema("c", "s"));
            assertThat(ContextNavigationUtils.getCatalogsAndSchemasOfUDTs(context, "u_%", types))
                    .containsExactly(new CatalogAndSchema("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfUDTs(context, "u_%", types))
                    .isEqualTo(new CatalogAndSchema("c", "s"));
            verify(context, times(2)).forEachUDT(isNull(), isNull(), eq("u_%"), isNull(), any());
            verify(context, times(2)).forEachUDT(isNull(), isNull(), eq("u_%"), same(types), any());
        }
    }

    @Nested
    class MemoryTest {

        @Test
        void h2_roundTripStaysInTheFoundSchema() throws Throwable {
            __JavaSqlTestUtils.acceptConnection("jdbc:h2:mem:navigation_utils_round_trip", c -> {
                try (var statement = c.createStatement()) {
                    statement.execute("CREATE SCHEMA A_B");
                    statement.execute("CREATE SCHEMA AXB");
                    statement.execute("CREATE TABLE A_B.ORDERS (X INT)");
                    statement.execute("CREATE TABLE AXB.OTHER (Y INT)");
                }
                final var context = Context.from(c);
                final var location = ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "ORDERS");
                assertThat(location).isEqualTo(new CatalogAndSchema(c.getCatalog(), "A_B"));
                // passed unescaped, the schema name is a pattern that also matches AXB
                assertThat(context.getTables(location.catalogForLookup(), location.schema(), "%", null))
                        .extracting(Table::getTableSchem)
                        .contains("AXB");
                assertThat(context.getTables(location.catalogForLookup(), location.schemaPatternForLookup(context),
                                             "%", null))
                        .extracting(Table::getTableName)
                        .containsExactly("ORDERS");
            });
        }

        @Test
        void h2_viewAndTableOfOneName_ambiguousUntilNarrowedByType() throws Throwable {
            __JavaSqlTestUtils.acceptConnection("jdbc:h2:mem:navigation_utils_types", c -> {
                try (var statement = c.createStatement()) {
                    statement.execute("CREATE SCHEMA S1");
                    statement.execute("CREATE SCHEMA S2");
                    statement.execute("CREATE TABLE S1.EVENTS (X INT)");
                    statement.execute("CREATE VIEW S2.EVENTS AS SELECT X FROM S1.EVENTS");
                }
                final var context = Context.from(c);
                assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "EVENTS"))
                        .isInstanceOf(IllegalArgumentException.class);
                assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(
                        context, "EVENTS", new String[] {"BASE TABLE"}))
                        .isEqualTo(new CatalogAndSchema(c.getCatalog(), "S1"));
            });
        }

        @Test
        void hsqldb() throws Throwable {
            __JavaSqlTestUtils.acceptConnection("jdbc:hsqldb:mem:navigation_utils", c -> {
                try (var statement = c.createStatement()) {
                    statement.execute("CREATE TABLE NAVIGATIONUTILS (X INT)");
                    statement.execute("CREATE FUNCTION NAVIGATIONFN(X INT) RETURNS INT RETURN X");
                    statement.execute("CREATE PROCEDURE NAVIGATIONPROC(IN X INT) READS SQL DATA"
                                      + " BEGIN ATOMIC DECLARE Y INT; SET Y = X; END");
                    statement.execute("CREATE TYPE NAVIGATIONTYPE AS INT");
                }
                final var context = Context.from(c);
                final var expected = new CatalogAndSchema(c.getCatalog(), c.getSchema());
                assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "NAVIGATIONUTILS"))
                        .isEqualTo(expected);
                assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfFunctions(context, "NAVIGATIONFN"))
                        .isEqualTo(expected);
                assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfProcedures(context, "NAVIGATIONPROC"))
                        .isEqualTo(expected);
                assertThat(ContextNavigationUtils.getOnlyCatalogAndSchemaOfUDTs(context, "NAVIGATIONTYPE"))
                        .isEqualTo(expected);
            });
        }

        @Test
        void sqlite_noLevels() throws Throwable {
            __JavaSqlTestUtils.acceptConnection("jdbc:sqlite::memory:", c -> {
                try (var statement = c.createStatement()) {
                    statement.execute("CREATE TABLE NAVIGATIONUTILS (X INT)");
                }
                final var context = Context.from(c);
                final var location = ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "NAVIGATIONUTILS");
                assertThat(location).isEqualTo(new CatalogAndSchema(null, null));
                assertThat(context.getTables(location.catalogForLookup(), location.schemaPatternForLookup(context),
                                             "NAVIGATIONUTILS", null))
                        .extracting(Table::getTableName)
                        .containsExactly("NAVIGATIONUTILS");
                assertThatThrownBy(() -> ContextNavigationUtils.getOnlyCatalogAndSchemaOfTables(context, "NO_SUCH"))
                        .isInstanceOf(NoSuchElementException.class);
            });
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static Context context() {
        return spy(new Context(mock(DatabaseMetaData.class)));
    }

    @SuppressWarnings("unchecked")
    private static void feed(final Context context, final Table... tables) throws SQLException {
        doAnswer(i -> {
            final Consumer<? super Table> consumer = i.getArgument(4);
            for (final var table : tables) {
                consumer.accept(table);
            }
            return null;
        }).when(context).forEachTable(any(), any(), any(), any(), any(Consumer.class));
    }

    private static Table table(final String tableCat, final String tableSchem) {
        final var table = new Table();
        table.setTableCat(tableCat);
        table.setTableSchem(tableSchem);
        table.setTableName("t");
        return table;
    }

    private static Procedure procedure(final String procedureCat, final String procedureSchem) {
        final var procedure = new Procedure();
        procedure.setProcedureCat(procedureCat);
        procedure.setProcedureSchem(procedureSchem);
        return procedure;
    }

    private static Function function(final String functionCat, final String functionSchem) {
        final var function = new Function();
        function.setFunctionCat(functionCat);
        function.setFunctionSchem(functionSchem);
        return function;
    }

    private static UDT udt(final String typeCat, final String typeSchem) {
        final var udt = new UDT();
        udt.setTypeCat(typeCat);
        udt.setTypeSchem(typeSchem);
        return udt;
    }
}
