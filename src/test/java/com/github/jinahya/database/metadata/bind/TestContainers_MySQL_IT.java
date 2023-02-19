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
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Disabled
@Testcontainers
@Slf4j
class TestContainers_MySQL_IT extends TestContainers_$_IT {

    static final String DATABASE_PRODUCT_NAME = "MySQL";

    @Container
    private static final MySQLContainer<?> CONTAINER;

    static {
        final DockerImageName NAME = DockerImageName.parse("mysql:latest");
        CONTAINER = new MySQLContainer<>(NAME);
    }

    @Override
    Connection connect() throws SQLException {
        final var url = CONTAINER.getJdbcUrl();
        final var user = CONTAINER.getUsername();
        final var password = CONTAINER.getPassword();
        return DriverManager.getConnection(url, user, password);
    }

    @Test
    void __columns() throws SQLException {
        try (var connection = connect()) {
            final var metadata = connection.getMetaData();
            try (var columns = metadata.getColumns(null, null, "%", "%")) {
                while (columns.next()) {
                    final var ordinal_position = columns.getInt("ORDINAL_POSITION");
                }
            }
        }
    }
}
