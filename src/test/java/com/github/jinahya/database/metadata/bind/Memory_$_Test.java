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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

    // -----------------------------------------------------------------------------------------------------------------
    private static File prefix(final DatabaseMetaData metaData) throws SQLException {
        final var productName = metaData.getDatabaseProductName();
        final var productVersion = metaData.getDatabaseProductVersion();
        return new File("target", productName + "_" + productVersion);
    }

    private static void json(final DatabaseMetaData metaData, final String name, final List<?> values)
            throws IOException, SQLException {
        try (var stream = new FileOutputStream(prefix(metaData) + "_" + name + ".json")) {
            new ObjectMapper().writeValue(stream, values);
        }
    }

    private static void metadataType(final DatabaseMetaData metaData, final Context context, final String name,
                                     final MetadataTypeQuery query) throws IOException, SQLException {
        try {
            json(metaData, name, query.get(context));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warn("not supported: {}", name, sqlfnse);
        }
    }

    @Test
    void metadataTypes() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = Context.newInstance(connection);
            final var tables = context.getTables(null, null, "%", null);
            final var table = tables.isEmpty() ? null : tables.get(0);
            metadataType(metaData, context, "attributes", c -> c.getAttributes(null, null, "%", "%"));
            metadataType(metaData, context, "bestRowIdentifier", c -> c.getBestRowIdentifier(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName(),
                    BestRowIdentifier.COLUMN_VALUE_SCOPE_BEST_ROW_SESSION,
                    true));
            metadataType(metaData, context, "catalogs", Context::getCatalogs);
            metadataType(metaData, context, "clientInfoProperties", Context::getClientInfoProperties);
            metadataType(metaData, context, "columnPrivileges", c -> c.getColumnPrivileges(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName(),
                    "%"));
            metadataType(metaData, context, "columns", c -> c.getColumns(null, null, "%", "%"));
            metadataType(metaData, context, "crossReference", c -> c.getCrossReference(null, null, "%", null, null,
                                                                                       "%"));
            metadataType(metaData, context, "exportedKeys", c -> c.getExportedKeys(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName()));
            metadataType(metaData, context, "functions", c -> c.getFunctions(null, null, "%"));
            metadataType(metaData, context, "functionColumns", c -> c.getFunctionColumns(null, null, "%", "%"));
            metadataType(metaData, context, "importedKeys", c -> c.getImportedKeys(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName()));
            metadataType(metaData, context, "indexInfo", c -> c.getIndexInfo(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName(),
                    false,
                    false));
            metadataType(metaData, context, "primaryKeys", c -> c.getPrimaryKeys(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName()));
            metadataType(metaData, context, "procedureColumns", c -> c.getProcedureColumns(null, null, "%", "%"));
            metadataType(metaData, context, "procedures", c -> c.getProcedures(null, null, "%"));
            metadataType(metaData, context, "pseudoColumns", c -> c.getPseudoColumns(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName(),
                    "%"));
            metadataType(metaData, context, "schemas", c -> c.getSchemas(null, null));
            metadataType(metaData, context, "superTables", c -> c.getSuperTables(null, "%", "%"));
            metadataType(metaData, context, "superTypes", c -> c.getSuperTypes(null, "%", "%"));
            metadataType(metaData, context, "tablePrivileges", c -> c.getTablePrivileges(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName()));
            metadataType(metaData, context, "tableTypes", Context::getTableTypes);
            json(metaData, "tables", tables);
            metadataType(metaData, context, "typeInfo", Context::getTypeInfo);
            metadataType(metaData, context, "udts", c -> c.getUDTs(null, null, "%", null));
            metadataType(metaData, context, "versionColumns", c -> c.getVersionColumns(
                    table == null ? null : table.getTableCat(),
                    table == null ? null : table.getTableSchem(),
                    table == null ? "" : table.getTableName()));
        }
    }

    @Test
    void catalogs() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = new Context(metaData);
            final List<Catalog> catalogs;
            try {
                catalogs = context.getCatalogs();
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                sqlfnse.printStackTrace();
                return;
            }
            json(metaData, "catalogs", catalogs);
            for (var catalog : catalogs) {
            }
        }
    }

    @Test
    void columns() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = new Context(metaData);
            final var columns = context.getColumns(null, null, "%", "%");
            json(metaData, "columns", columns);
        }
    }

    @Test
    void functions() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = new Context(metaData);
            try {
                final var functions = context.getFunctions((String) null, null, "%");
                json(metaData, "functions", functions);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                log.warn("not supported", sqlfnse);
            }
        }
    }

    @Test
    void functionColumns() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = new Context(metaData);
            try {
                final var functionColumns = context.getFunctionColumns(null, null, "%", "%");
                json(metaData, "functionColumns", functionColumns);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                log.warn("not supported", sqlfnse);
            }
        }
    }

    @Test
    void schemas() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = new Context(metaData);
            final List<Schema> schemas;
            try {
                schemas = context.getSchemas((String) null, null);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                sqlfnse.printStackTrace();
                return;
            }
            json(metaData, "schemas", schemas);
            for (var schema : schemas) {
            }
        }
    }

    @Test
    void tables() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = new Context(metaData);
            final var tables = context.getTables((String) null, null, "%", null);
            json(metaData, "tables", tables);
            for (var table : tables) {
                final var columnPrivileges = context.getColumnPrivileges(
                        table.getTableCat(),
                        table.getTableSchem(),
                        table.getTableName(),
                        "%"
                );
                log.debug("{}: {}", table, columnPrivileges);
            }
        }
    }

    @Test
    void tablesTypes() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = new Context(metaData);
            final var tableTypes = context.getTableTypes();
            json(metaData, "tableTypes", tableTypes);
        }
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

    @FunctionalInterface
    private interface MetadataTypeQuery {

        List<?> get(Context context) throws SQLException;
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
