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

import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
final class __JavaSqlTestUtils {

    // -----------------------------------------------------------------------------------------------------------------
    static Connection connection(final String url) throws SQLException {
        final var connection = DriverManager.getConnection(url);
        log.debug("connected: {}", connection);
        return connection;
    }

    static Connection connection(final String url, final String user, final String password) throws SQLException {
        final var connection = DriverManager.getConnection(url, user, password);
        log.debug("connected: {}", connection);
        return connection;
    }

    static <R> R applyConnection(final CheckedFunction0<? extends Connection> supplier,
                                 final CheckedFunction1<? super Connection, ? extends R> function)
            throws Throwable {
        Objects.requireNonNull(supplier, "supplier is null");
        Objects.requireNonNull(function, "function is null");
        try (var connection = supplier.apply()) {
            return function.apply(connection);
        }
    }

    static <R> R applyConnection(final String url, final CheckedFunction1<? super Connection, ? extends R> function)
            throws Throwable {
        return applyConnection(() -> connection(url), function);
    }

    static <R> R applyConnection(final String url, final String user, final String password,
                                 final CheckedFunction1<? super Connection, ? extends R> function)
            throws Throwable {
        return applyConnection(() -> connection(url, user, password), function);
    }

    static void acceptConnection(final CheckedFunction0<? extends Connection> supplier,
                                 final CheckedConsumer<? super Connection> consumer)
            throws Throwable {
        applyConnection(supplier, c -> {
            consumer.accept(c);
            return null;
        });
    }

    static void acceptConnection(final String url, final CheckedConsumer<? super Connection> consumer)
            throws Throwable {
        acceptConnection(() -> connection(url), consumer);
    }

    static void acceptConnection(final String url, final String user, final String password,
                                 final CheckedConsumer<? super Connection> consumer)
            throws Throwable {
        acceptConnection(() -> connection(url, user, password), consumer);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private __JavaSqlTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
