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
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Objects;

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
    void acceptProperties__() {
        applyContext(c -> {
            try {
                c.acceptProperties((p, r) -> {
                    log.debug("{}: {}", p, r);
                });
            } catch (final IntrospectionException ie) {
                throw new RuntimeException(ie);
            }
            return null;
        });
    }

    @Test
    void acceptValues__() {
        applyContext(c -> {
            c.acceptValues((m, r) -> {
                log.debug("{}: {}", m.getName(), r);
            });
            return null;
        });
    }

    @Test
    void metadata() {
        final var metadata = applyContext(c -> {
            return Metadata.newInstance(c);
        });
        __Validation_Test_Utils.requireValid(metadata);
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
                final var functions = context.getFunctions(null, null, "%");
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
    void tables() throws SQLException, IOException {
        try (var connection = connect()) {
            final var metaData = connection.getMetaData();
            final var context = new Context(metaData);
            final var tables = context.getTables((String) null, null, "%", null);
            json(metaData, "tables", tables);
            for (var table : tables) {
                final var columnPrivileges = context.getColumnPrivileges(table, "%");
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
}
