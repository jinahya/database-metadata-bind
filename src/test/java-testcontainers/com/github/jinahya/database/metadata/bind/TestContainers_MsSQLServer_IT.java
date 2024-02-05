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
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// https://java.testcontainers.org/modules/databases/mssqlserver/
// https://github.com/microsoft/mssql-docker/issues/668
//@Disabled // 안 돎
//@Testcontainers
@Slf4j
class TestContainers_MsSQLServer_IT extends TestContainers_$_IT {

        private static final String FULL_IMAGE_NAME = "mcr.microsoft.com/mssql/server:2022-latest";
//    private static final String FULL_IMAGE_NAME = "mcr.microsoft.com/azure-sql-edge:latest";
//    private static final String FULL_IMAGE_NAME = "azure-sql-edge:latest";

    //    @Container
    private static MSSQLServerContainer<?> CONTAINER;

//    static {
//        // https://www.testcontainers.org/modules/databases/oraclexe/
//        final DockerImageName NAME = DockerImageName.parse("mcr.microsoft.com/mssql/server:2022-latest");
//        CONTAINER = new MSSQLServerContainer<>(NAME)
//                .acceptLicense()
//                .withEnv("MSSQL_PID", "Developer")
//                .withUrlParam("encrypt", "false")
//    }

    @BeforeAll
    static void start() {
        final DockerImageName name = DockerImageName.parse(FULL_IMAGE_NAME);
        CONTAINER = new MSSQLServerContainer<>(name).acceptLicense();
        CONTAINER.start();
//        final var timeout = Duration.ofSeconds(10L);
//        log.debug("awaiting for {}", timeout);
//        Awaitility.await()
//                .atMost(timeout)
//                .pollDelay(Duration.ofSeconds(1L))
//                .untilAsserted(() -> {
//                    Assertions.assertTrue(CONTAINER.isRunning());
//                });
    }

    @AfterAll
    static void stop() {
        CONTAINER.stop();
    }

    @Override
    Connection connect() throws SQLException {
        final var url = CONTAINER.getJdbcUrl();
        final var user = CONTAINER.getUsername();
        final var password = CONTAINER.getPassword();
        return DriverManager.getConnection(url, user, password);
    }
}
