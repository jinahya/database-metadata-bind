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

import io.vavr.CheckedFunction1;
import io.vavr.Function1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
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

    <R> R applyConnection(final java.util.function.Function<? super Connection, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        try (var connection = connect()) {
            return function.apply(connection);
        } catch (final SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

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

    <R> R applyContextUnchecked(final Function1<? super Context, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return applyContext(c -> {
            try {
                return function.apply(c);
            } catch (final Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    <R> R applyContextChecked(final CheckedFunction1<? super Context, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return applyContextUnchecked(function.unchecked());
    }

    @Test
    void test() throws SQLException {
        applyContext(c -> {
            try {
                ContextTests.test(c);
                return null;
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        });
    }

    @Test
    void testOrdering() throws SQLException {
        applyContext(c -> {
            try {
                ContextTests.testOrdering(c);
                return null;
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        });
    }

    @Test
    void getColumns__() {
        applyContextChecked(c -> {
            ContextTests.getColumns__(c, null, null, "%", "%");
            return null;
        });
    }

    @Test
    void getTables__() {
        applyContextChecked(c -> {
            ContextTests.getTables__(c, null, null, "%", null);
            return null;
        });
    }
}
