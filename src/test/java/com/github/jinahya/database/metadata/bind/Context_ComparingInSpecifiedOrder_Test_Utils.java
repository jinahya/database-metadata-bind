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
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

final class Context_ComparingInSpecifiedOrder_Test_Utils {

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

    static void assertComparingInSpecifiedOrder(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        assertFunctionColumns(context);
        assertProcedureColumns(context);
        assertPortedKeysInSpecifiedOrder(context);
    }

    static void assertPortedKeysInSpecifiedOrder(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        assertImportedKeys(context);
        assertExportedKeys(context);
        assertCrossReference(context);
    }

    private static void assertFunctionColumns(final Context context) throws SQLException {
        try {
            assertEachAccepted(
                    FunctionColumn.comparingInSpecifiedOrder(
                            java.util.function.UnaryOperator.identity(),
                            ContextUtils.nullOrdered(context, String.CASE_INSENSITIVE_ORDER)
                    ),
                    c -> context.getFunctionColumnsAndAcceptEach(null, null, "%", "%", c)
            );
        } catch (final SQLFeatureNotSupportedException ignored) {
            // The driver does not expose function columns.
        }
    }

    private static void assertProcedureColumns(final Context context) throws SQLException {
        try {
            assertEachAccepted(
                    ProcedureColumn.comparingInSpecifiedOrder(
                            java.util.function.UnaryOperator.identity(),
                            ContextUtils.nullOrdered(context, String.CASE_INSENSITIVE_ORDER)
                    ),
                    c -> context.getProcedureColumnsAndAcceptEach(null, null, "%", "%", c)
            );
        } catch (final SQLFeatureNotSupportedException ignored) {
            // The driver does not expose procedure columns.
        }
    }

    private static void assertImportedKeys(final Context context) throws SQLException {
        final var child = tableName(context, "DMB_CHILD");
        assertEachAccepted(
                ImportedKey.comparingInSpecifiedOrder(
                        java.util.function.UnaryOperator.identity(),
                        ContextUtils.nullOrdered(context, String.CASE_INSENSITIVE_ORDER)
                ),
                c -> context.getImportedKeysAndAcceptEach(null, null, child, c)
        );
    }

    private static void assertExportedKeys(final Context context) throws SQLException {
        final var parent = tableName(context, "DMB_PARENT");
        assertEachAccepted(
                ExportedKey.comparingInSpecifiedOrder(
                        java.util.function.UnaryOperator.identity(),
                        ContextUtils.nullOrdered(context, String.CASE_INSENSITIVE_ORDER)
                ),
                c -> context.getExportedKeysAndAcceptEach(null, null, parent, c)
        );
    }

    private static void assertCrossReference(final Context context) throws SQLException {
        final var parent = tableName(context, "DMB_PARENT");
        final var child = tableName(context, "DMB_CHILD");
        assertEachAccepted(
                CrossReference.comparingInSpecifiedOrder(
                        java.util.function.UnaryOperator.identity(),
                        ContextUtils.nullOrdered(context, String.CASE_INSENSITIVE_ORDER)
                ),
                c -> context.getCrossReferenceAndAcceptEach(
                        null,
                        null,
                        parent,
                        null,
                        null,
                        child,
                        c
                )
        );
    }

    private static <T> void assertEachAccepted(final Comparator<? super T> comparator,
                                               final AcceptEach<T> acceptEach)
            throws SQLException {
        final var previous = new AtomicReference<T>();
        acceptEach.accept(value -> {
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

    @FunctionalInterface
    private interface AcceptEach<T> {

        void accept(Consumer<? super T> consumer) throws SQLException;
    }

    private Context_ComparingInSpecifiedOrder_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
