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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Slf4j
abstract class TestContainers_$_IT {

    @BeforeAll
    static void checkDocker() throws IOException, InterruptedException {
        final var process = new ProcessBuilder()
                .command("docker", "images")
                .start();
        final int exitValue = process.waitFor();
        log.debug("exitValue: {}", exitValue);
        assumeTrue(exitValue == 0);
    }

    abstract Connection connect() throws SQLException;

    @Test
    void test() throws SQLException {
        try (var connection = connect()) {
            log.debug("connected: {}", connection);
            final var context = Context.newInstance(connection);
            ContextTests.test(context);
        }
    }

    @Test
    void schemas() throws SQLException {
        try (var connection = connect()) {
            final var metadata = connection.getMetaData();
            try (final ResultSet catalogRs = metadata.getCatalogs()) {
                while (catalogRs.next()) {
                    final String tableCat = catalogRs.getString(Catalog.COLUMN_LABEL_TABLE_CAT);
                    log.debug("tableCat: {}", tableCat);
                    try (final ResultSet schemaRs = metadata.getSchemas(tableCat, "%")) {
                        while (schemaRs.next()) {
                            final var tableCatalog = schemaRs.getString(Schema.COLUMN_LABEL_TABLE_CATALOG);
                            final var tableSchem = schemaRs.getString(Schema.COLUMN_LABEL_TABLE_SCHEM);
                            log.debug("tableCatalog: {}, tableSchem: {}", tableCatalog, tableSchem);
                        }
                    }
                }
            }
        }
    }

    @Disabled
    @Test
    void tables() throws SQLException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
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
