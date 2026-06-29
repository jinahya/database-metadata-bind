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
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.junit.jupiter.Container;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//https://java.testcontainers.org/modules/databases/oraclefree/
//@Disabled("takes too long!")
@Slf4j
class TestContainers_OracleFree_IT
        extends TestContainers_$_IT {

    private static final String IMAGE_NAME = "container-registry.oracle.com/database/free:latest-lite";

    @Container
//    private static final OracleContainer CONTAINER = new OracleContainer("gvenzl/oracle-free:slim-faststart")
//    private static final OracleContainer CONTAINER = new OracleContainer(IMAGE_NAME)
    private static final JdbcDatabaseContainer<?> CONTAINER =
            new org.testcontainers.oracle.OracleContainer("gvenzl/oracle-free:slim-faststart")
                    .withDatabaseName("testDB")
                    .withUsername("testUser")
                    .withPassword("testPassword");

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    Connection connect() throws SQLException {
        final var jdbcUrl = CONTAINER.getJdbcUrl();
        final var username = CONTAINER.getUsername();
        final var password = CONTAINER.getPassword();
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
