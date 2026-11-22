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

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
final class Context_ComparingInJdbcOrder_Test_Utils {

    static void preparePortedKeyTables(final Statement statement) throws SQLException {
        Objects.requireNonNull(statement, "statement is null");
        executeIgnoring(statement, "drop table DMB_CHILD");
        executeIgnoring(statement, "drop table DMB_PARENT");
        statement.executeUpdate("create table DMB_PARENT (ID integer not null primary key)");
        statement.executeUpdate(
                "create table DMB_CHILD (" +
                "ID integer not null primary key, " +
                "PARENT_ID integer, " +
                "constraint DMB_CHILD_PARENT_FK foreign key (PARENT_ID) references DMB_PARENT(ID)" +
                ")"
        );
    }

    static void assertComparingInJdbcOrder(final Context context) throws SQLException {
        assertComparingInJdbcOrder(context, "comparingInJdbcOrder");
    }

    static void assertComparingInJdbcOrder(final Context context, final String name) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(name, "name is null");
        final var failures = new ArrayList<Throwable>();
        assertEach(context, failures, "getAttributes",
                   c -> Attribute.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getAttributes(null, null, "%", "%"));
        assertEach(context, failures, "getBestRowIdentifier",
                   BestRowIdentifier::comparingInJdbcOrder,
                   c -> c.getBestRowIdentifier(null, null, tableName(c, "DMB_CHILD"),
                                               BestRowIdentifier.COLUMN_VALUE_SCOPE_BEST_ROW_SESSION, true));
        assertEach(context, failures, "getCatalogs",
                   c -> Catalog.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   Context::getCatalogs);
        assertEach(context, failures, "getClientInfoProperties",
                   c -> ClientInfoProperty.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   Context::getClientInfoProperties);
        assertEach(context, failures, "getColumnPrivileges",
                   c -> ColumnPrivilege.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getColumnPrivileges(null, null, tableName(c, "DMB_CHILD"), "%"));
        assertEach(context, failures, "getColumns",
                   c -> Column.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getColumns(null, null, tableName(c, "DMB_%"), "%"));
        assertEach(context, failures, "getCrossReference",
                   c -> CrossReference.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getCrossReference(null, null, tableName(c, "DMB_PARENT"),
                                            null, null, tableName(c, "DMB_CHILD")));
        assertEach(context, failures, "getExportedKeys",
                   c -> ExportedKey.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getExportedKeys(null, null, tableName(c, "DMB_PARENT")));
        assertEach(context, failures, "getFunctions",
                   c -> Function.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getFunctions(null, null, "%"));
        assertEach(context, failures, "getFunctionColumns",
                   c -> FunctionColumn.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getFunctionColumns(null, null, "%", "%"));
        assertEach(context, failures, "getImportedKeys",
                   c -> ImportedKey.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getImportedKeys(null, null, tableName(c, "DMB_CHILD")));
        assertEach(context, failures, "getIndexInfo",
                   c -> IndexInfo.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getIndexInfo(null, null, tableName(c, "DMB_CHILD"), false, false));
        assertEach(context, failures, "getPrimaryKeys",
                   c -> PrimaryKey.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getPrimaryKeys(null, null, tableName(c, "DMB_CHILD")));
        assertEach(context, failures, "getProcedureColumns",
                   c -> ProcedureColumn.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getProcedureColumns(null, null, "%", "%"));
        assertEach(context, failures, "getProcedures",
                   c -> Procedure.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getProcedures(null, null, "%"));
        assertEach(context, failures, "getPseudoColumns",
                   c -> PseudoColumn.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getPseudoColumns(null, null, tableName(c, "DMB_CHILD"), "%"));
        assertEach(context, failures, "getSchemas",
                   c -> Schema.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getSchemas(null, null));
        assertEach(context, failures, "getTablePrivileges",
                   c -> TablePrivilege.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getTablePrivileges(null, null, tableName(c, "DMB_%")));
        assertEach(context, failures, "getTableTypes",
                   c -> TableType.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   Context::getTableTypes);
        assertEach(context, failures, "getTables",
                   c -> Table.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getTables(null, null, tableName(c, "DMB_%"), (String[]) null));
        assertEach(context, failures, "getTypeInfo",
                   TypeInfo::comparingInJdbcOrder,
                   Context::getTypeInfo);
        assertEach(context, failures, "getUDTs",
                   c -> UDT.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getUDTs(null, null, "%", (int[]) null));
        assertNoFailures(failures);
    }

    static void assertPortedKeysInSpecifiedOrder(final Context context) throws SQLException {
        assertPortedKeysInSpecifiedOrder(context, "portedKeys");
    }

    static void assertPortedKeysInSpecifiedOrder(final Context context, final String name) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(name, "name is null");
        final var failures = new ArrayList<Throwable>();
        assertEach(context, failures, "getImportedKeys",
                   c -> ImportedKey.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getImportedKeys(null, null, tableName(c, "DMB_CHILD")));
        assertEach(context, failures, "getExportedKeys",
                   c -> ExportedKey.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getExportedKeys(null, null, tableName(c, "DMB_PARENT")));
        assertEach(context, failures, "getCrossReference",
                   c -> CrossReference.comparingInJdbcOrder(c, String.CASE_INSENSITIVE_ORDER),
                   c -> c.getCrossReference(null, null, tableName(c, "DMB_PARENT"),
                                            null, null, tableName(c, "DMB_CHILD")));
        assertNoFailures(failures);
    }

    private static <T> void assertEach(final Context context, final List<? super Throwable> failures,
                                       final String api,
                                       final ComparatorFactory<T> comparatorFactory,
                                       final MetadataTypeQuery<T> query) {
        // a driver that fails the method, or returns its rows out of the specified order, deviates from the
        // specification; that is logged, not failed - only a defect of binding or of a comparator fails
        try {
            warnUnlessInSpecifiedOrder(api, comparatorFactory.get(context), query.get(context));
        } catch (final SQLException sqle) {
            log.warn("failed to verify the order of {}", api, sqle);
        } catch (final Throwable t) {
            failures.add(t);
        }
    }

    // shared with Context_Test_Utils: a driver's out-of-order rows are logged, not failed - see #89
    static <T> void warnUnlessInSpecifiedOrder(final String api, final Comparator<? super T> comparator,
                                               final List<T> values) {
        for (int i = 1; i < values.size(); i++) {
            final var previous = values.get(i - 1);
            final var current = values.get(i);
            if (comparator.compare(previous, current) > 0) {
                log.warn("{} values are not emitted in specified order; previous=<{}>, current=<{}>",
                         api, previous, current);
                return;
            }
        }
    }

    private static String tableName(final Context context, final String name) throws SQLException {
        if (context.metadata.storesLowerCaseIdentifiers()) {
            return name.toLowerCase(Locale.ROOT);
        }
        if (context.metadata.storesUpperCaseIdentifiers()) {
            return name.toUpperCase(Locale.ROOT);
        }
        return name;
    }

    private static void executeIgnoring(final Statement statement, final String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (final SQLException ignored) {
            // Table may not exist.
        }
    }

    private static void assertNoFailures(final List<? extends Throwable> failures) {
        if (failures.isEmpty()) {
            return;
        }
        final var failure = new AssertionError("failed to verify comparingInJdbcOrder");
        failures.forEach(failure::addSuppressed);
        throw failure;
    }

    @FunctionalInterface
    private interface ComparatorFactory<T> {

        Comparator<T> get(Context context) throws SQLException;
    }

    @FunctionalInterface
    private interface MetadataTypeQuery<T> {

        List<T> get(Context context) throws SQLException;
    }

    private Context_ComparingInJdbcOrder_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
