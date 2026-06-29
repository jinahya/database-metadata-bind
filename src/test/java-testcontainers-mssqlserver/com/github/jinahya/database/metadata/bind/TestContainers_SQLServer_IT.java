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
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

// https://java.testcontainers.org/modules/databases/mssqlserver/
// https://github.com/microsoft/mssql-docker/issues/668
// https://github.com/microsoft/mssql-jdbc/issues/2320
// https://github.com/microsoft/mssql-jdbc/issues/849
@Disabled
@Slf4j
class TestContainers_SQLServer_IT
        extends TestContainers_$_IT {

    private static final String FULL_IMAGE_NAME = "mcr.microsoft.com/mssql/server:2022-latest";

    @Container
    private static final MSSQLServerContainer<?> CONTAINER =
            new MSSQLServerContainer<>(DockerImageName.parse(FULL_IMAGE_NAME))
                    .withImagePullPolicy(PullPolicy.ageBased(Duration.ofDays(180L)))
                    .acceptLicense();

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    Connection connect() throws SQLException {
        final var jdbcUrl = CONTAINER.getJdbcUrl();
        final var username = CONTAINER.getUsername();
        final var password = CONTAINER.getPassword();
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
