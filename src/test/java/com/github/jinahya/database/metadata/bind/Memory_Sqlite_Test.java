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
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for SQLite.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class Memory_Sqlite_Test extends Memory_$_Test {

    private static final String CONNECTION_URL = "jdbc:sqlite::memory:";

    @Override
    protected Connection connect() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL);
    }

    @Disabled("https://github.com/xerial/sqlite-jdbc/issues/837 which has been fixed")
    @Test
    void getColumns_CATLOG_CATALOG() throws SQLException {
        try (var connection = DriverManager.getConnection("jdbc:sqlite::memory:")) {
            final var meta = connection.getMetaData();
            log.debug("driverName: {}", meta.getDriverName());
            log.debug("driverVersion: {}", meta.getDriverVersion());
            log.debug("driverMajorVersion: {}", meta.getDriverMajorVersion());
            log.debug("driverMinorVersion: {}", meta.getDriverMinorVersion());
            log.debug("databaseProductName: {}", meta.getDatabaseProductName());
            log.debug("databaseProductVersion: {}", meta.getDatabaseProductVersion());
            log.debug("databaseMajorVersion: {}", meta.getDatabaseMajorVersion());
            log.debug("databaseMinorVersion: {}", meta.getDatabaseMinorVersion());
            try (ResultSet results = meta.getColumns("", "", "%", "%")) {
                final var labels = new HashSet<>();
                final var rsmd = results.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    labels.add(rsmd.getColumnLabel(i));
                }
                labels.forEach(l -> {
                    log.debug("label: {}", l);
                });
            }
        }
    }

    @Disabled // https://github.com/xerial/sqlite-jdbc/issues/859
    @Test
    void getColumns__INTEGER_DATA_TYPE() throws SQLException {
        try (var connection = connect()) {
            final var md = connection.getMetaData();
            try (var results = md.getColumns(null, null, "%", "%")) {
                final var rsmd = results.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    final var label = rsmd.getColumnLabel(i);
                    if (!"DATA_TYPE".equals(label)) {
                        continue;
                    }
                    final var type = rsmd.getColumnType(i);
                    assertThat(type).isEqualTo(Types.INTEGER);
                    assertThat(results.getObject(i)).isInstanceOf(Integer.class);
                }
            }
        }
    }

    public static void main(final String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:")) {
            final DatabaseMetaData dmd = connection.getMetaData();
            try (ResultSet results = dmd.getColumns(null, null, "%", "%")) {
                final ResultSetMetaData rsmd = results.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    final String label = rsmd.getColumnLabel(i);
                    if (!"DATA_TYPE".equals(label)) {
                        continue;
                    }
                    final int type = rsmd.getColumnType(i);
                    assert type == Types.INTEGER;
                    assert results.getObject(i) instanceof Integer;
                }
            }
        }
    }
}
