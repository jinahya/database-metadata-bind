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
import org.testcontainers.containers.Db2Container;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

// https://java.testcontainers.org/modules/databases/db2/
@Disabled("doesn't start; no arm")
//@Testcontainers
@Slf4j
class TestContainers_Db2_IT
        extends TestContainers_$_IT {

    private static final String FULL_IMAGE_NAME = "ibmcom/db2:latest";

    //    @Container
    private static Db2Container CONTAINER;

    @BeforeAll
    static void start() {
        final var name = DockerImageName.parse(FULL_IMAGE_NAME);
        CONTAINER = new Db2Container(name)
                .withImagePullPolicy(PullPolicy.ageBased(Duration.ofDays(180L)))
                .acceptLicense()
        ;
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
