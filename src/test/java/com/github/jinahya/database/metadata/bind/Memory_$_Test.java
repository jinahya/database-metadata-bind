package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
abstract class Memory_$_Test {

    /**
     * Returns a connection
     *
     * @return a connection.
     * @throws SQLException if a database error occurs.
     */
    abstract Connection connect() throws SQLException;

    // ------------------------------------------------------------------------------------------------------ connection
    <R> R applyConnection(final java.util.function.Function<? super Connection, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        try (var connection = connect()) {
            return function.apply(connection);
        } catch (final SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    // --------------------------------------------------------------------------------------------------------- context
    <R> R applyContext(final java.util.function.Function<? super Context, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return applyConnection(c -> {
            try {
                return function.apply(Context.newInstance(c));
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void test() throws SQLException {
        applyContext(c -> {
            try {
                Context_Test_Utils.test(c);
                return null;
            } catch (final SQLException sqle) {
                if (sqle instanceof SQLFeatureNotSupportedException sqlfnse) {
                    log.warn("not supported", sqlfnse);
                    return null;
                }
                throw new RuntimeException(sqle);
            }
        });
    }

    @Test
    void directMetadataMappings_getAndForEach() throws SQLException {
        applyContext(context -> {
            try {
                final var tables = unsupportedAsEmpty(() -> context.getTables(null, null, "%", null));
                final var table = tables.isEmpty() ? null : tables.get(0);
                assertDirect("attributes", () -> context.getAttributes(null, null, "%", "%"),
                             c -> context.forEachAttribute(null, null, "%", "%", c));
                assertDirect("catalogs", context::getCatalogs, c -> context.forEachCatalog(c));
                assertDirect("clientInfoProperties", context::getClientInfoProperties,
                             c -> context.forEachClientInfoProperty(c));
                assertDirect("columns", () -> context.getColumns(null, null, "%", "%"),
                             c -> context.forEachColumn(null, null, "%", "%", c));
                assertDirect("functions", () -> context.getFunctions(null, null, "%"),
                             c -> context.forEachFunction(null, null, "%", c));
                assertDirect("functionColumns", () -> context.getFunctionColumns(null, null, "%", "%"),
                             c -> context.forEachFunctionColumn(null, null, "%", "%", c));
                assertDirect("procedureColumns", () -> context.getProcedureColumns(null, null, "%", "%"),
                             c -> context.forEachProcedureColumn(null, null, "%", "%", c));
                assertDirect("procedures", () -> context.getProcedures(null, null, "%"),
                             c -> context.forEachProcedure(null, null, "%", c));
                assertDirect("pseudoColumns", () -> context.getPseudoColumns(null, null, "%", "%"),
                             c -> context.forEachPseudoColumn(null, null, "%", "%", c));
                assertDirect("schemas", context::getSchemas, c -> context.forEachSchema(c));
                assertDirect("schemasPattern", () -> context.getSchemas(null, "%"),
                             c -> context.forEachSchema(null, "%", c));
                assertDirect("superTables", () -> context.getSuperTables(null, "%", "%"),
                             c -> context.forEachSuperTable(null, "%", "%", c));
                assertDirect("superTypes", () -> context.getSuperTypes(null, "%", "%"),
                             c -> context.forEachSuperType(null, "%", "%", c));
                assertDirect("tablePrivileges", () -> context.getTablePrivileges(null, null, "%"),
                             c -> context.forEachTablePrivilege(null, null, "%", c));
                assertDirect("tableTypes", context::getTableTypes, c -> context.forEachTableType(c));
                assertDirect("tables", () -> context.getTables(null, null, "%", null),
                             c -> context.forEachTable(null, null, "%", null, c));
                assertDirect("typeInfo", context::getTypeInfo, c -> context.forEachTypeInfo(c));
                assertDirect("udts", () -> context.getUDTs(null, null, "%", null),
                             c -> context.forEachUDT(null, null, "%", null, c));
                if (table != null) {
                    assertDirect("bestRowIdentifier", () -> context.getBestRowIdentifier(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                         BestRowIdentifier.COLUMN_VALUE_SCOPE_BEST_ROW_SESSION, true),
                                 c -> context.forEachBestRowIdentifier(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                         BestRowIdentifier.COLUMN_VALUE_SCOPE_BEST_ROW_SESSION, true, c));
                    assertDirect("columnPrivileges", () -> context.getColumnPrivileges(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), "%"),
                                 c -> context.forEachColumnPrivilege(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), "%", c));
                    assertDirect("exportedKeys", () -> context.getExportedKeys(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName()),
                                 c -> context.forEachExportedKey(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), c));
                    assertDirect("importedKeys", () -> context.getImportedKeys(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName()),
                                 c -> context.forEachImportedKey(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), c));
                    assertDirect("indexInfo", () -> context.getIndexInfo(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), false,
                                         false),
                                 c -> context.forEachIndexInfo(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), false,
                                         false, c));
                    assertDirect("primaryKeys", () -> context.getPrimaryKeys(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName()),
                                 c -> context.forEachPrimaryKey(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), c));
                    assertDirect("versionColumns", () -> context.getVersionColumns(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName()),
                                 c -> context.forEachVersionColumn(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), c));
                }
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
            return null;
        });
    }

    @Test
    void comparingInSpecifiedOrder_failFree() throws SQLException {
        try (var connection = connect();
             var statement = connection.createStatement()) {
            try {
                Context_ComparingInSpecifiedOrder_Test_Utils.preparePortedKeyTables(statement);
                Context_ComparingInSpecifiedOrder_Test_Utils.assertComparingInSpecifiedOrder(
                        Context.newInstance(connection),
                        getClass().getSimpleName()
                );
            } catch (final Throwable t) {
                log.warn("failed to verify comparingInSpecifiedOrder; test={}", getClass().getSimpleName(), t);
            }
        }
    }

    /**
     * Creates a table with a two-column composite primary key, then reads it back via
     * {@link Context#getPrimaryKeys(String, String, String)} and logs the observed per-column behavior. Used to verify
     * cross-engine {@code getPrimaryKeys} reality (ordering, {@code PK_NAME}, {@code TABLE_CAT}/{@code TABLE_SCHEM}
     * population) that the empty in-memory smoke tests cannot exercise.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void primaryKeys_composite() throws SQLException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var product = metaData.getDatabaseProductName();
            try (var statement = connection.createStatement()) {
                // NOTE: a failing statement auto-closes on DuckDB, so use IF EXISTS instead of catching
                statement.execute("DROP TABLE IF EXISTS PK_DEMO");
                statement.execute("""
                                          CREATE TABLE PK_DEMO (
                                              B_COL INTEGER NOT NULL,
                                              A_COL INTEGER NOT NULL,
                                              PAYLOAD VARCHAR(10),
                                              CONSTRAINT PK_DEMO_PK PRIMARY KEY (B_COL, A_COL)
                                          )""");
            }
            final var context = Context.newInstance(connection);
            final var keys = context.getPrimaryKeys(null, null, "PK_DEMO");
            assertThat(keys).as("primary keys of PK_DEMO on %s", product).hasSize(2);
            // as returned by the driver (no re-sorting), so we can observe the driver's own order
            for (final var pk : keys) {
                log.info("PK_VERIFY product=[{}] columnName={} keySeq={} pkName={} tableCat={} tableSchem={}",
                         product, pk.getColumnName(), pk.getKeySeq(), pk.getPkName(),
                         pk.getTableCat(), pk.getTableSchem());
            }
            final var firstColumn = keys.get(0).getColumnName();
            log.info("PK_VERIFY_SUMMARY product=[{}] firstReturnedColumn={} orderedByColumnName={} pkNameNull={} "
                     + "tableCatNull={} tableSchemNull={}",
                     product, firstColumn, "A_COL".equals(firstColumn),
                     keys.get(0).getPkName() == null,
                     keys.get(0).getTableCat() == null,
                     keys.get(0).getTableSchem() == null);
        }
    }

    private static <T> List<T> unsupportedAsEmpty(final Query<T> query) throws SQLException {
        try {
            return query.get();
        } catch (final SQLFeatureNotSupportedException ignored) {
            return List.of();
        }
    }

    private static <T> void assertDirect(final String name, final Query<T> query, final Iteration<T> iteration)
            throws SQLException {
        final List<T> values;
        try {
            values = query.get();
        } catch (final SQLFeatureNotSupportedException ignored) {
            return;
        }
        final var accepted = new ArrayList<T>();
        try {
            iteration.accept(accepted::add);
        } catch (final SQLFeatureNotSupportedException ignored) {
            return;
        }
        assertThat(accepted).as(name).hasSameSizeAs(values);
    }

    @FunctionalInterface
    private interface Query<T> {

        List<T> get() throws SQLException;
    }

    @FunctionalInterface
    private interface Iteration<T> {

        void accept(Consumer<? super T> consumer) throws SQLException;
    }

    @Nested
    class FlattenTest {

        @Test
        void catalogs() throws SQLException {
            withContext(c -> supported("getCatalogs", c::getCatalogs));
        }

        @Test
        void schemas() throws SQLException {
            withContext(c -> supported("getSchemas", () -> c.getSchemas(null, null)));
        }

        @Test
        void schemas__() throws SQLException {
            withContext(c -> supported("getSchemas()", c::getSchemas));
        }

        @Test
        void tables() throws SQLException {
            withContext(c -> supported("getTables", () -> c.getTables(null, null, "%", null)));
        }

        @Test
        void functions() throws SQLException {
            withContext(c -> supported("getFunctions", () -> c.getFunctions(null, null, "%")));
        }

        @Test
        void procedures() throws SQLException {
            withContext(c -> supported("getProcedures", () -> c.getProcedures(null, null, "%")));
        }

        @Test
        void udts() throws SQLException {
            withContext(c -> supported("getUDTs", () -> c.getUDTs(null, null, "%", null)));
        }

        @Test
        void attributes() throws SQLException {
            withContext(c -> supported("getAttributes", () -> c.getAttributes(null, null, "%", "%")));
        }

        @Test
        void clientInfoProperties() throws SQLException {
            withContext(c -> supported("getClientInfoProperties", c::getClientInfoProperties));
        }

        @Test
        void columns() throws SQLException {
            withContext(c -> supported("getColumns", () -> c.getColumns(null, null, "%", "%")));
        }

        @Test
        void crossReference() throws SQLException {
            withContext(c -> supported(
                    "getCrossReference",
                    () -> c.getCrossReference(null, null, "%", null, null, "%")
            ));
        }

        @Test
        void functionColumns() throws SQLException {
            withContext(c -> supported(
                    "getFunctionColumns",
                    () -> c.getFunctionColumns(null, null, "%", "%")
            ));
        }

        @Test
        void procedureColumns() throws SQLException {
            withContext(c -> supported(
                    "getProcedureColumns",
                    () -> c.getProcedureColumns(null, null, "%", "%")
            ));
        }

        @Test
        void superTables() throws SQLException {
            withContext(c -> supported("getSuperTables", () -> c.getSuperTables(null, "%", "%")));
        }

        @Test
        void superTypes() throws SQLException {
            withContext(c -> supported("getSuperTypes", () -> c.getSuperTypes(null, "%", "%")));
        }

        @Test
        void tableTypes() throws SQLException {
            withContext(c -> supported("getTableTypes", c::getTableTypes));
        }

        @Test
        void typeInfo() throws SQLException {
            withContext(c -> supported("getTypeInfo", c::getTypeInfo));
        }

        @Test
        void numericFunctions() throws SQLException {
            withContext(c -> supported("getNumericFunctions", c::getNumericFunctions));
        }

        @Test
        void sqlKeywords() throws SQLException {
            withContext(c -> supported("getSQLKeywords", c::getSQLKeywords));
        }

        @Test
        void stringFunctions() throws SQLException {
            withContext(c -> supported("getStringFunctions", c::getStringFunctions));
        }

        @Test
        void systemFunctions() throws SQLException {
            withContext(c -> supported("getSystemFunctions", c::getSystemFunctions));
        }

        @Test
        void timeDateFunctions() throws SQLException {
            withContext(c -> supported("getTimeDateFunctions", c::getTimeDateFunctions));
        }

        private void withContext(final SqlContextConsumer consumer) throws SQLException {
            try (var connection = connect()) {
                final var context = Context.newInstance(connection);
                consumer.accept(context);
            }
        }

        private void withCatalogs(final SqlBiConsumer<Catalog> consumer) throws SQLException {
            withContext(c -> {
                for (final var catalog : supported("getCatalogs", c::getCatalogs)) {
                    consumer.accept(c, catalog);
                }
            });
        }

        private void withSchemas(final SqlBiConsumer<Schema> consumer) throws SQLException {
            withContext(c -> {
                for (final var schema : supported("getSchemas", () -> c.getSchemas(null, null))) {
                    consumer.accept(c, schema);
                }
            });
        }

        private void withTables(final SqlBiConsumer<Table> consumer) throws SQLException {
            withContext(c -> {
                for (final var table : supported("getTables", () -> c.getTables(null, null, "%", null))) {
                    consumer.accept(c, table);
                }
            });
        }

        private void withFunctions(final SqlBiConsumer<Function> consumer) throws SQLException {
            withContext(c -> {
                for (final var function : supported("getFunctions", () -> c.getFunctions(null, null, "%"))) {
                    consumer.accept(c, function);
                }
            });
        }

        private void withProcedures(final SqlBiConsumer<Procedure> consumer) throws SQLException {
            withContext(c -> {
                for (final var procedure : supported("getProcedures", () -> c.getProcedures(null, null, "%"))) {
                    consumer.accept(c, procedure);
                }
            });
        }

        private void withUdts(final SqlBiConsumer<UDT> consumer) throws SQLException {
            withContext(c -> {
                for (final var udt : supported("getUDTs", () -> c.getUDTs(null, null, "%", null))) {
                    consumer.accept(c, udt);
                }
            });
        }

        private <E> List<E> supported(final String name, final SqlListSupplier<E> supplier) throws SQLException {
            try {
                final var values = supplier.get();
                assertThat(values)
                        .as(name)
                        .isNotNull()
                        .doesNotContainNull();
                return values;
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                log.warn("not supported: {}", name, sqlfnse);
                return List.of();
            }
        }

        @FunctionalInterface
        private interface SqlListSupplier<E> {

            List<E> get() throws SQLException;
        }

        @FunctionalInterface
        private interface SqlContextConsumer {

            void accept(Context context) throws SQLException;
        }

        @FunctionalInterface
        private interface SqlBiConsumer<E> {

            void accept(Context context, E value) throws SQLException;
        }
    }
}
