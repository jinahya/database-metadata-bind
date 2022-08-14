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

import java.sql.DriverManager;

@Slf4j
class TestcontainersMysqlTest
        extends TestcontainersTest {

    private static final String URL = "jdbc:tc:mysql:5.7.39:///ignorant?TC_DAEMON=true";

    private static final String USER = "root";

    private static final String PASSWORD = "test";

    @Disabled
    @Test
    void test() throws Exception {
//        final String jdbcUrl = container.getJdbcUrl();
//        final String username = container.getUsername();
//        final String password = container.getPassword();
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
//        try (var connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            final var context = Context.newInstance(connection);
            final var metadata = Metadata.newInstance(context);
        }
    }

//    @Container
//    public MySQLContainer<?> container
//            = new MySQLContainer<>(DockerImageName.parse("mysql:5.7"))
//            .withExposedPorts(3306)
//            .waitingFor(new WaitAllStrategy(WaitAllStrategy.Mode.WITH_INDIVIDUAL_TIMEOUTS_ONLY))
//            ;
}
