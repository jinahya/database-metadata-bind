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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for Apache Derby in memory.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class Memory_Derby_Test extends Memory_$_Test {

    private static final String DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";

    private static final Class<?> DRIVER_CLASS;

    static {
        try {
            DRIVER_CLASS = Class.forName(DRIVER_NAME);
            log.debug("driver class: {}", DRIVER_CLASS);
        } catch (final ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            throw new InstantiationError(cnfe.getMessage());
        }
    }

    private static final String CONNECTION_URL = "jdbc:derby:memory:test";

    @BeforeAll
    static void create() throws SQLException {
        final var properties = new Properties();
        properties.put("create", "true");
        try (var connection = getConnection(CONNECTION_URL, properties)) {
            log.debug("connection: {}", connection);
        }
    }

    @AfterAll
    static void shutdown() {
        final var properties = new Properties();
        properties.put("shutdown", "true");
        try {
            getConnection(CONNECTION_URL, properties).close();
        } catch (final SQLException sqle) {
            // https://builds.apache.org/job/Derby-docs/lastSuccessfulBuild/artifact/trunk/out/ref/rrefattrib16471.html
            // this is expected
            // Shutdown commands always raise SQLExceptions.
            return;
        }
        fail("an instance of SQLException should've been thrown");
    }

    @Override
    protected Connection connect() throws SQLException {
        return getConnection(CONNECTION_URL);
    }

    @Override
    void metadata(final Metadata metadata) throws Exception {
        super.metadata(metadata);
        JakartaXmlBindingTestUtils.marshal(CONNECTION_URL, Metadata.class, metadata);
    }
}
