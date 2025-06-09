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

import java.beans.IntrospectionException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;

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
    void test() throws SQLException {
        applyContext(c -> {
            c.connectionSupplier = () -> {
                try {
                    return connect();
                } catch (final SQLException sqle) {
                    throw new RuntimeException("failed to connect", sqle);
                }
            };
            try {
                Context_Test_Utils.test(c);
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
                Context_Test_Utils.info(c);
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
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
    void schemas() {
        applyContext(c -> {
            try {
                Context_Test_Utils.info(c);
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
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
            try {
                Context_Test_Utils.info(c);
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
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
}
