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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
abstract class MemoryTest {

    abstract Connection connect() throws SQLException;

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void writeToFiles() throws Exception {
        try (Connection connection = connect()) {
            final MetadataContext context = MetadataContext.newInstance(connection);
            final DatabaseMetadata metadata = DatabaseMetadata.newInstance(context);
            final String name;
            {
                final String simpleName = getClass().getSimpleName();
                name = ("memory." + (simpleName.substring(6, simpleName.indexOf("Test"))) + ".metadata").toLowerCase();
            }
            MetadataTests.writeToFiles(metadata, name);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void getCatalogs__() throws Exception {
        try (Connection connection = connect()) {
            final MetadataContext metadataContext = MetadataContext.newInstance(connection);
            for (final Catalog catalog : metadataContext.getCatalogs(new ArrayList<>())) {
                log.debug("catalog: {}", catalog);
            }
        }
    }

    @Test
    void getSchemas__() throws Exception {
        try (Connection connection = connect()) {
            final MetadataContext metadataContext = MetadataContext.newInstance(connection);
            for (final Schema schema : metadataContext.getSchemas(null, null, new ArrayList<>())) {
                log.debug("schema: {}", schema);
            }
        }
    }

    @Test
    void getTables__() throws Exception {
        try (Connection connection = connect()) {
            final MetadataContext metadataContext = MetadataContext.newInstance(connection);
            for (final Table table : metadataContext.getTables(null, null, null, null, new ArrayList<>())) {
                log.debug("table: {}", table);
            }
        }
    }
}
