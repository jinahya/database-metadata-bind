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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// https://java.testcontainers.org/modules/databases/jdbc/

@Testcontainers
@Slf4j
abstract class TestContainers_$_IT {

    @BeforeAll
    static void checkDocker() throws IOException, InterruptedException {
        log.info("checking docker...");
        final var process = new ProcessBuilder()
                .command("docker", "images")
                .start();
        final int exitValue = process.waitFor();
        log.info("exitValue: {}", exitValue);
        assumeTrue(exitValue == 0);
    }

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
    void test() {
        applyConnection(c -> {
            try {
                final var context = Context.newInstance(c);
                Context_Test_Utils.test(context);
            } catch (final SQLException sqle) {
                if (sqle instanceof SQLFeatureNotSupportedException sqlfnse) {
                    log.warn("not supported", sqlfnse);
                    return null;
                }
                throw new RuntimeException(sqle);
            }
            return null;
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void functions() {
        applyContext(c -> {
            try {
                final var functions = c.getFunctions(null, null, "%");
                Context_Test_Utils.functions(c, functions);
            } catch (final SQLException sqle) {
                if (sqle instanceof SQLFeatureNotSupportedException sqlfnse) {
                    log.error("not supported", sqlfnse);
                    return null;
                }
                throw new RuntimeException(sqle);
            }
            return null;
        });
    }

    @Test
    void comparingInSpecifiedOrder() {
        applyConnection(c -> {
            try (var statement = c.createStatement()) {
                Context_ComparingInSpecifiedOrder_Test_Utils.preparePortedKeyTables(statement);
                Context_ComparingInSpecifiedOrder_Test_Utils.assertComparingInSpecifiedOrder(
                        Context.newInstance(c),
                        getClass().getSimpleName()
                );
            } catch (final SQLException sqle) {
                if (sqle instanceof SQLFeatureNotSupportedException sqlfnse) {
                    log.error("not supported", sqlfnse);
                    return null;
                }
                throw new RuntimeException(sqle);
            }
            return null;
        });
    }

    @Test
    void schemas() {
        applyContext(c -> {
            try {
                final var schemas = c.getSchemas((String) null, "%");
                Context_Test_Utils.schemas(c, schemas);
            } catch (final SQLException sqle) {
                if (sqle instanceof SQLFeatureNotSupportedException sqlfnse) {
                    log.error("not supported", sqlfnse);
                    return null;
                }
                throw new RuntimeException(sqle);
            }
            return null;
        });
    }

    @Test
    void tables() {
        applyContext(c -> {
//            try {
//                Context_Test_Utils.info(c);
//            } catch (final SQLException sqle) {
//                throw new RuntimeException(sqle);
//            }
            try {
                final var tables = c.getTables((String) null, null, "%", null);
                Context_Test_Utils.tables(c, tables);
            } catch (final SQLException sqle) {
                if (sqle instanceof SQLFeatureNotSupportedException sqlfnse) {
                    log.error("not supported", sqlfnse);
                    return null;
                }
                throw new RuntimeException(sqle);
            }
            return null;
        });
    }

    @Test
    void directMetadataMappings() {
        applyContext(context -> {
            try {
                final var tables = unsupportedAsEmpty(() -> context.getTables(null, null, "%", null));
                final var table = tables.isEmpty() ? null : tables.get(0);
                assertDirect("attributes", () -> context.getAttributes(null, null, "%", "%"),
                             c -> context.forEachAttribute(null, null, "%", "%", c));
                if (table != null) {
                    assertDirect("bestRowIdentifier", () -> context.getBestRowIdentifier(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                         BestRowIdentifier.COLUMN_VALUE_SCOPE_BEST_ROW_SESSION, true),
                                 c -> context.forEachBestRowIdentifier(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                         BestRowIdentifier.COLUMN_VALUE_SCOPE_BEST_ROW_SESSION, true, c));
                }
                assertDirect("catalogs", context::getCatalogs, c -> context.forEachCatalog(c));
                assertDirect("clientInfoProperties", context::getClientInfoProperties,
                             c -> context.forEachClientInfoProperty(c));
                if (table != null) {
                    assertDirect("columnPrivileges", () -> context.getColumnPrivileges(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), "%"),
                                 c -> context.forEachColumnPrivilege(
                                         table.getTableCat(), table.getTableSchem(), table.getTableName(), "%", c));
                }
                assertDirect("columns", () -> context.getColumns(null, null, "%", "%"),
                             c -> context.forEachColumn(null, null, "%", "%", c));
                if (table != null) {
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
                }
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
}
