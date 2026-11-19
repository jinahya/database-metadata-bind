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
            assertThatThrownBy(() -> ContextNavigationUtils.getLocationsOfTables(null, "%"))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> ContextNavigationUtils.getLocationsOfTables(context, null))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(null, "%"))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(context, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void __oneNonNarrowingLookupWithVerbatimPatternAndTypes() throws SQLException {
            final var context = context();
            feed(context);
            final var types = new String[] {"VIEW"};
            ContextNavigationUtils.getLocationsOfTables(context, "A_B%");
            ContextNavigationUtils.getLocationsOfTables(context, "A_B%", types);
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
            assertThat(ContextNavigationUtils.getLocationsOfTables(context, "t"))
                    .containsExactly(
                            new Location("c2", "s1"),
                            new Location("c1", null),
                            new Location(null, "s1"),
                            new Location(null, null),
                            new Location("C2", "s1"));
        }

        @Test
        void list__emptyWhenNothingMatches() throws SQLException {
            final var context = context();
            feed(context);
            assertThat(ContextNavigationUtils.getLocationsOfTables(context, "t")).isEmpty();
        }

        @Test
        void only__nothingMatched_NoSuchElement() throws SQLException {
            final var context = context();
            feed(context);
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(context, "t"))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("'t'");
        }

        @Test
        void only__onePair_Returned() throws SQLException {
            final var context = context();
            feed(context, table("c", "s"), table("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyLocationOfTables(context, "t%"))
                    .isEqualTo(new Location("c", "s"));
        }

        @Test
        void only__catalogWithoutSchema_ReturnedWhole() throws SQLException {
            final var context = context();
            feed(context, table("tenant_a", null));
            assertThat(ContextNavigationUtils.getOnlyLocationOfTables(context, "t"))
                    .isEqualTo(new Location("tenant_a", null));
        }

        @Test
        void only__noLevelAtAll_ReturnedAsNullPair() throws SQLException {
            final var context = context();
            feed(context, table(null, null), table(null, null));
            assertThat(ContextNavigationUtils.getOnlyLocationOfTables(context, "t%"))
                    .isEqualTo(new Location(null, null));
        }

        @Test
        void only__twoSchemasInOneCatalog_IllegalArgument() throws SQLException {
            final var context = context();
            feed(context, table("tenant_a", "s1"), table("tenant_a", "s2"));
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(context, "t"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("s1")
                    .hasMessageContaining("s2");
        }

        @Test
        void only__sameSchemaInTwoCatalogs_IllegalArgument() throws SQLException {
            final var context = context();
            feed(context, table("c1", "s"), table("c2", "s"));
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(context, "t"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void only__someWithLevelSomeWithout_IllegalArgument() throws SQLException {
            final var context = context();
            feed(context, table("c", "s"), table("c", null));
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(context, "t"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void only__caseVariants_IllegalArgument() throws SQLException {
            final var context = context();
            feed(context, table("c", "s"), table("c", "S"));
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(context, "t"))
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
            assertThat(ContextNavigationUtils.getLocationsOfProcedures(context, "p_%"))
                    .containsExactly(new Location("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyLocationOfProcedures(context, "p_%"))
                    .isEqualTo(new Location("c", "s"));
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
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfProcedures(context, "RUN"))
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
            assertThat(ContextNavigationUtils.getLocationsOfFunctions(context, "f_%"))
                    .containsExactly(new Location("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyLocationOfFunctions(context, "f_%"))
                    .isEqualTo(new Location("c", "s"));
            verify(context, times(2)).forEachFunction(isNull(), isNull(), eq("f_%"), any());
        }

        @Test
        void __unsupportedLookupPropagates() throws SQLException {
            final var context = context();
            doThrow(new SQLFeatureNotSupportedException())
                    .when(context).forEachFunction(any(), any(), any(), any());
            assertThatThrownBy(() -> ContextNavigationUtils.getLocationsOfFunctions(context, "%"))
                    .isInstanceOf(SQLFeatureNotSupportedException.class);
            assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfFunctions(context, "%"))
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
            assertThat(ContextNavigationUtils.getLocationsOfUDTs(context, "u_%"))
                    .containsExactly(new Location("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyLocationOfUDTs(context, "u_%"))
                    .isEqualTo(new Location("c", "s"));
            assertThat(ContextNavigationUtils.getLocationsOfUDTs(context, "u_%", types))
                    .containsExactly(new Location("c", "s"));
            assertThat(ContextNavigationUtils.getOnlyLocationOfUDTs(context, "u_%", types))
                    .isEqualTo(new Location("c", "s"));
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
                final var location = ContextNavigationUtils.getOnlyLocationOfTables(context, "ORDERS");
                assertThat(location).isEqualTo(new Location(c.getCatalog(), "A_B"));
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
                assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(context, "EVENTS"))
                        .isInstanceOf(IllegalArgumentException.class);
                assertThat(ContextNavigationUtils.getOnlyLocationOfTables(
                        context, "EVENTS", new String[] {"BASE TABLE"}))
                        .isEqualTo(new Location(c.getCatalog(), "S1"));
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
                final var expected = new Location(c.getCatalog(), c.getSchema());
                assertThat(ContextNavigationUtils.getOnlyLocationOfTables(context, "NAVIGATIONUTILS"))
                        .isEqualTo(expected);
                assertThat(ContextNavigationUtils.getOnlyLocationOfFunctions(context, "NAVIGATIONFN"))
                        .isEqualTo(expected);
                assertThat(ContextNavigationUtils.getOnlyLocationOfProcedures(context, "NAVIGATIONPROC"))
                        .isEqualTo(expected);
                assertThat(ContextNavigationUtils.getOnlyLocationOfUDTs(context, "NAVIGATIONTYPE"))
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
                final var location = ContextNavigationUtils.getOnlyLocationOfTables(context, "NAVIGATIONUTILS");
                assertThat(location).isEqualTo(new Location(null, null));
                assertThat(context.getTables(location.catalogForLookup(), location.schemaPatternForLookup(context),
                                             "NAVIGATIONUTILS", null))
                        .extracting(Table::getTableName)
                        .containsExactly("NAVIGATIONUTILS");
                assertThatThrownBy(() -> ContextNavigationUtils.getOnlyLocationOfTables(context, "NO_SUCH"))
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
