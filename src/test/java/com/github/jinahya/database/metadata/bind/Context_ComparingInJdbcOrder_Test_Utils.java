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

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
        final List<T> values;
        try {
            values = query.get(context);
        } catch (final SQLFeatureNotSupportedException ignored) {
            return;
        } catch (final Throwable t) {
            failures.add(t);
            return;
        }
        try {
            assertInSpecifiedOrder(comparatorFactory.get(context), values);
        } catch (final Throwable t) {
            failures.add(t);
        }
    }

    private static <T> void assertInSpecifiedOrder(final Comparator<? super T> comparator, final Iterable<T> values) {
        final var previous = new AtomicReference<T>();
        values.forEach(value -> {
            final var last = previous.getAndSet(value);
            if (last != null && comparator.compare(last, value) > 0) {
                throw new AssertionError("values are not emitted in specified order; previous=<" +
                                         last + ">, current=<" + value + ">");
            }
        });
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
