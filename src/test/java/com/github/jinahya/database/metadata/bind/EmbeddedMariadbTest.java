/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import static com.github.jinahya.database.metadata.bind.MetadataContext.getCatalogs;
import static java.lang.invoke.MethodHandles.lookup;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import java.sql.ResultSet;
import java.util.List;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test with MariaDB4j.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://github.com/vorburger/MariaDB4j">MariaDB4j (GitHub)</a>
 */
public class EmbeddedMariadbTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    private static DB DB__;

    //private static final String URL = "jdbc:mysql://localhost/test";
    private static String URL;

    private static final String USER = "root";

    private static final String PASSWORD = "";

    // -------------------------------------------------------------------------
    @BeforeClass
    private static void beforeClass() throws Exception {
        final DBConfigurationBuilder builder
                = DBConfigurationBuilder.newBuilder();
        builder.setPort(0);
        DB__ = DB.newEmbeddedDB(builder.build());
        DB__.start();
        logger.debug("embedded mariadb started");
        URL = builder.getURL("test");
        logger.debug("url: {}", URL);
    }

    @AfterClass
    private static void afterClass() throws Exception {
        DB__.stop();
        logger.debug("embedded mariadb stopped");
        DB__ = null;
    }

    @Test
    public void printCatalogs() throws Exception {
        try (Connection connection = getConnection(URL, USER, PASSWORD)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            try (ResultSet catalogs = metadata.getCatalogs()) {
                while (catalogs.next()) {
                    final String tableCat = catalogs.getString("TABLE_CAT");
                    logger.debug("tableCat: {}", tableCat);
                }
            }
        }
    }

    @Test
    public void printSchemaNames() throws Exception {
        try (Connection connection = getConnection(URL, USER, PASSWORD)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            try (ResultSet catalogs = metadata.getSchemas()) {
                while (catalogs.next()) {
                    final String tableSchem = catalogs.getString("TABLE_SCHEM");
                    final String tableCatalog
                            = catalogs.getString("TABLE_CATALOG");
                    logger.debug("tableSchem: {}, tableCat: {}", tableSchem,
                                 tableCatalog);
                }
            }
        }
    }

    @Test
    public void printTables() throws Exception {
        try (Connection connection = getConnection(URL, USER, PASSWORD)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            try (ResultSet tables
                    = metadata.getTables(null, null, null, null)) {
                while (tables.next()) {
                    final String tableCat = tables.getString("TABLE_CAT");
                    final String tableSchem = tables.getString("TABLE_SCHEM");
                    final String tableName = tables.getString("TABLE_NAME");
                    logger.debug("tableCat: {}, tableSchem: {}, tableName: {}",
                                 tableCat, tableSchem, tableName);
                }
            }
        }
    }

    @Test
    public void storeCatalogs() throws Exception {
        try (Connection connection = getConnection(URL, USER, PASSWORD)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            final MetadataContext context = new MetadataContext(metadata);
            final List<Catalog> catalogs = getCatalogs(context, true);
            for (final Catalog catalog : catalogs) {
                MetadataContextTests.marshal(catalog, "mariadb.memory");
            }
        }
    }
}
