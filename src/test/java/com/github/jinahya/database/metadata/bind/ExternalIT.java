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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

/**
 * Test class for remote databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
class ExternalIT {

    @EnabledIfSystemProperty(named = "password", matches = ".+")
    @EnabledIfSystemProperty(named = "user", matches = ".+")
    @EnabledIfSystemProperty(named = "url", matches = ".+")
    @Test
    void writeToFiles() throws Exception {
        final String url = System.getProperty("url");
        final String user = System.getProperty("user");
        final String password = System.getProperty("password");
        log.info("connecting...");
        try (Connection connection = getConnection(url, user, password)) {
            log.info("connected: {}", connection);
            final Context context = Context.newInstance(connection)
                    .suppress(SQLException.class);
            ContextTestUtils.writeFiles(context);
        }
    }
}
