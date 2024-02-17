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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

// https://hub.docker.com/r/gvenzl/oracle-xe
// https://blog.jdriven.com/2022/07/running-oracle-xe-with-testcontainers-on-apple-silicon/
// https://java.testcontainers.org/modules/databases/oraclefree/
//@Disabled("does not start; no-arm")
@Disabled("takes too long!")
@Slf4j
class TestContainers_OracleXE_IT
        extends TestContainers_$_IT {

    private static final String FULL_IMAGE_NAME = "gvenzl/oracle-xe:latest-faststart";

    private static OracleContainer CONTAINER;

    @BeforeAll
    static void start() {
        final DockerImageName name = DockerImageName.parse(FULL_IMAGE_NAME);
        CONTAINER = new OracleContainer(name)
                .withImagePullPolicy(PullPolicy.ageBased(Duration.ofDays(180L)))
                .withStartupTimeout(Duration.ofMinutes(8L))
                .withDatabaseName("testDB")
                .withUsername("testUser")
                .withPassword("testPassword");
        CONTAINER.start();
    }

    @AfterAll
    static void stop() {
        CONTAINER.stop();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    Connection connect() throws SQLException {
        final var url = CONTAINER.getJdbcUrl();
        final var user = CONTAINER.getUsername();
        final var password = CONTAINER.getPassword();
        return DriverManager.getConnection(url, user, password);
    }
}
