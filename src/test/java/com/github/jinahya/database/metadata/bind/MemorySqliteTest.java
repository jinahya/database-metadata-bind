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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

/**
 * Test for SQLite.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@org.junit.jupiter.api.condition.EnabledIfSystemProperty(named = "memory", matches = "true")
@Slf4j
class MemorySqliteTest extends MemoryTest {

    private static final String CONNECTION_URL = "jdbc:sqlite::memory:";

    @Override
    protected Connection connect() throws SQLException {
        return getConnection(CONNECTION_URL);
    }

    @Disabled("https://github.com/xerial/sqlite-jdbc/issues/831")
    @Test
    void __() throws SQLException {
        try (var connection = DriverManager.getConnection("jdbc:sqlite::memory:")) {
            final var meta = connection.getMetaData();
            log.debug("driverName: {}", meta.getDriverName());
            log.debug("driverVersion: {}", meta.getDriverVersion());
            log.debug("databaseProductVersion: {}", meta.getDatabaseProductVersion());
            log.debug("databaseProductName: {}", meta.getDatabaseProductName());
            log.debug("databaseMajorVersion: {}", meta.getDatabaseMajorVersion());
            log.debug("databaseMinorVersion: {}", meta.getDatabaseMinorVersion());
            try (ResultSet tables = meta.getTables(null, null, "%", null)) {
                while (tables.next()) {
                    final var tableCat = tables.getString("TABLE_CAT");
                    final var tableSchem = tables.getString("TABLE_SCHEM");
                    final var tableName = tables.getString("TABLE_NAME");
                    log.debug("{}/{}/{}", tableCat, tableSchem, tableName);
                    try (ResultSet importedKeys = meta.getImportedKeys(tableCat, tableSchem, tableName)) {
                        while (importedKeys.next()) {
                            final var pktableCat = importedKeys.getString("PKTABLE_CAT");
                            final var pktableSchem = importedKeys.getString("PKTABLE_SCHEM");
                            final var pktableName = importedKeys.getString("PKTABLE_NAME");
                            log.debug("importedKey.pktable: {}/{}/{}", pktableCat, pktableSchem, pktableName);
                        }
                    }
                    try (ResultSet exportedKeys = meta.getExportedKeys(tableCat, tableSchem, tableName)) {
                        while (exportedKeys.next()) {
                        }
                    }
                }
            }
        }
    }
}
