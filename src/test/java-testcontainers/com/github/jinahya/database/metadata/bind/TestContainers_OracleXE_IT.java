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
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

// https://java.testcontainers.org/modules/databases/oraclexe/
//@Disabled("does not start; no-arm")
@Disabled("takes too long!")
@Slf4j
class TestContainers_OracleXE_IT
        extends TestContainers_$_IT {

    private static final String IMAGE_NAME = "gvenzl/oracle-xe:latest-faststart";

    @Container
    private static final OracleContainer CONTAINER = new OracleContainer(DockerImageName.parse(IMAGE_NAME))
            .withImagePullPolicy(PullPolicy.ageBased(Duration.ofDays(180L)))
//            .withStartupTimeout(Duration.ofMinutes(8L))
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
