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

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static java.sql.DriverManager.getConnection;

/**
 * Test with MariaDB4j.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://github.com/vorburger/MariaDB4j">MariaDB4j (GitHub)</a>
 */
@Slf4j
class EmbeddedMariadbTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static DB DB__;

    private static String URL;

    private static final String USER = "root";

    private static final String PASSWORD = "";

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeAll
    static void beforeClass() throws Exception {
        final DBConfigurationBuilder builder = DBConfigurationBuilder.newBuilder();
        builder.setPort(0);
        DB__ = DB.newEmbeddedDB(builder.build());
        DB__.start();
        log.debug("embedded mariadb started");
        URL = builder.getURL("test");
        log.debug("url: {}", URL);
    }

    @AfterAll
    static void afterClass() throws Exception {
        DB__.stop();
        log.debug("embedded mariadb stopped");
        DB__ = null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Disabled
    @Test
    void writeToFiles() throws Exception {
        try (Connection connection = getConnection(URL, USER, PASSWORD)) {
            log.debug("connection: {}", connection);
            final MetadataContext context = MetadataContext.newInstance(connection);
            final DatabaseMetadata metadata = DatabaseMetadata.newInstance(context);
            MetadataTests.writeToFiles(metadata, "embedded.mariadb.metadata");
        }
    }
}
