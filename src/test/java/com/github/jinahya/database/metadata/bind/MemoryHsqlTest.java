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
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import static com.github.jinahya.database.metadata.bind.JaxbTests.store;
import static java.sql.DriverManager.getConnection;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
public class MemoryHsqlTest extends MemoryTest {

    private static final Logger logger = getLogger(MemoryHsqlTest.class);

    // -----------------------------------------------------------------------------------------------------------------
    private static final String DRIVER_NAME = "org.hsqldb.jdbc.JDBCDriver";

    private static final Class<?> DRIVER_CLASS;

    static {
        try {
            DRIVER_CLASS = Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException cnfe) {
            throw new InstantiationError(cnfe.getMessage());
        }
    }

    private static final String CONNECTION_URL = "jdbc:hsqldb:mem:test";

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void writeToFileXml() throws Exception {
        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            final Metadata metadata = Metadata.newInstance(context);
            JaxbTests.writeToFile(Metadata.class, metadata, "memory.hsql.metadata");
        }
    }

    @Test
    void getCatalogs__() throws Exception {
        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            for (final Catalog catalog : context.getCatalogs()) {
                log.debug("catalog: {}", catalog);
            }
        }
    }

    @Test
    void getSchemas__() throws Exception {
        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            for (final Schema schema : context.getSchemas(null, null)) {
                log.debug("schema: {}", schema);
            }
        }
    }

    @Test
    void getTables__() throws Exception {
        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            for (final Table table : context.getTables(null, null, null, null)) {
                log.debug("table: {}", table);
            }
        }
    }
}
