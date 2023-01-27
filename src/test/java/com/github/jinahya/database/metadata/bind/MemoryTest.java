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
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
abstract class MemoryTest {

    /**
     * Returns a connection
     *
     * @return a connection.
     * @throws SQLException if a database error occurs.
     */
    protected abstract Connection connect() throws SQLException;

    Context context(final Connection connection) throws SQLException {
        return Context.newInstance(connection);
    }

    @Test
    void test() throws SQLException {
        try (var connection = connect()) {
            final var context = context(connection);
            ContextTests.test(context);
        }
    }

    @Test
    void tables() throws SQLException {
        try (var connection = connect()) {
            final var context = context(connection);
            ContextTests.info(context);
            final var tables = context.getTables(null, null, "%", null);
            tables.stream().map(Table::getTableCat).distinct().forEach(
                    tc -> log.debug("tableCat: {}", Optional.ofNullable(tc).map(v -> '\'' + v + '\'').orElse(null))
            );
            tables.stream().map(Table::getTableSchem).distinct().forEach(
                    ts -> log.debug("tableSchem: {}", Optional.ofNullable(ts).map(v -> '\'' + v + '\'').orElse(null))
            );
        }
    }
}
