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

import static com.github.jinahya.database.metadata.bind.MetadataContext.getCatalogs;
import static com.github.jinahya.database.metadata.bind.MetadataContextTests.store;
import static java.lang.invoke.MethodHandles.lookup;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

/**
 * Test with Embedded PostgreSQL Server.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see
 * <a href="https://github.com/yandex-qatools/postgresql-embedded">Embedded
 * PostgreSQL Server (GitHub)</a>
 */
public class EmbeddedPostgresqlTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    private static EmbeddedPostgres EMBEDDED_POSTGRES;

    private static String URL;

    // -------------------------------------------------------------------------
    @BeforeClass
    private static void beforeClass() throws Exception {
//        final PostgresStarter<PostgresExecutable, PostgresProcess> starter
//                = PostgresStarter.getDefaultInstance();
//        config = PostgresConfig.defaultWithDbName("test", "test", "test");
//        final PostgresExecutable exec = starter.prepare(config);
//        process = exec.start();

        // starting Postgres
        EMBEDDED_POSTGRES = new EmbeddedPostgres();
        logger.debug("embedded postgres constructed");
        URL = EMBEDDED_POSTGRES.start(
                "localhost", 5432, "dbName", "userName", "password");
        logger.debug("embedded postgres started on {}", URL);
    }

    @AfterClass
    private static void afterClass() throws Exception {
        EMBEDDED_POSTGRES.stop();
        logger.debug("embedded postgres stopped");
    }

    // -------------------------------------------------------------------------
    @Test(enabled = false)
    public void printCatalogs() throws Exception {
        try (Connection connection = getConnection(URL)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            try (ResultSet catalogs = metadata.getCatalogs()) {
                while (catalogs.next()) {
                    final String tableCat = catalogs.getString("TABLE_CAT");
                    logger.debug("tableCat: {}", tableCat);
                }
            }
        }
    }

    @Test(enabled = false)
    public void printSchemaNames() throws Exception {
        try (Connection connection = getConnection(URL)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            try (ResultSet schemas = metadata.getSchemas()) {
                while (schemas.next()) {
                    final String tableSchem = schemas.getString("TABLE_SCHEM");
                    final String tableCatalog
                            = schemas.getString("TABLE_CATALOG");
                    logger.debug("tableSchem: {}, tableCatalog: {}", tableSchem,
                                 tableCatalog);
                }
            }
        }
    }

    @Test(enabled = false)
    public void printSchemas() throws Exception {
        try (Connection connection = getConnection(URL)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            try (ResultSet schemas = metadata.getSchemas(null, null)) {
                while (schemas.next()) {
                    final String tableSchem = schemas.getString("TABLE_SCHEM");
                    final String tableCatalog
                            = schemas.getString("TABLE_CATALOG");
                    logger.debug("tableSchem: {}, tableCatalog: {}", tableSchem,
                                 tableCatalog);
                }
            }
        }
    }

    @Test(enabled = false)
    public void printTables() throws Exception {
        try (Connection connection = getConnection(URL)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            final List<String> tableSchems = new ArrayList<>();
            try (ResultSet schemas = metadata.getSchemas(null, null)) {
                while (schemas.next()) {
                    final String tableSchem = schemas.getString("TABLE_SCHEM");
                    final String tableCatalog
                            = schemas.getString("TABLE_CATALOG");
                    tableSchems.add(tableSchem);
                }
            }
            final List<String> tableNames = new ArrayList<>();
            for (final String tableSchem : tableSchems) {
                logger.debug("-----------------------------------------------");
                logger.debug("tableSchem: {}", tableSchem);
                try (ResultSet tables
                        = metadata.getTables(null, tableSchem, null, null)) {
                    while (tables.next()) {
                        final String tableName = tables.getString("TABLE_NAME");
                        logger.debug("tableName: {}", tableName);
                        tableNames.add(tableName);
                    }
                }
            }
            logger.debug("tableNames.size: {}", tableNames.size());
            final List<String> columnNames = new ArrayList<>();
            for (final String tableName : tableNames) {
                columnNames.clear();
                try (ResultSet columns
                        = metadata.getColumns(null, null, tableName, null)) {
                    while (columns.next()) {
                        final String columnName
                                = columns.getString("COLUMN_NAME");
                        columnNames.add(columnName);
                    }
                }
                logger.debug("columnNames.size of {}: {}", tableName,
                             columnNames.size());
            }
        }
    }

    @Test
    public void print() throws Exception {
        try (Connection connection = getConnection(URL)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            try (ResultSet ss = metadata.getSchemas(null, null)) {
                while (ss.next()) {
                    final String s = ss.getString("TABLE_SCHEM");
                    logger.debug("-------------------------------------------");
                    logger.debug("{} <<<<<<<<<<<<<<<", s);
                    try (ResultSet tt = metadata.getTables(null, s, null, null)) {
                        while (tt.next()) {
                            final String t = tt.getString("TABLE_NAME");
                            logger.debug("\t{}", t);
                            try (ResultSet cc
                                    = metadata.getColumns(null, s, t, null)) {
                                while (cc.next()) {
                                    final String c
                                            = cc.getString("COLUMN_NAME");
                                    logger.debug("\t\t{}", c);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test(enabled = true)
    public void storeCatalogs() throws Exception {
        try (Connection connection = getConnection(URL)) {
            DatabaseMetaData metadata = connection.getMetaData();
            metadata = MetadataLogger.newProxy(metadata);
            final MetadataContext context = new MetadataContext(metadata);
            context.suppress("function/functionColumns");
            context.suppress("functionColumn/charOctetLength"); // unknown
            context.suppress("functionColumn/functionCat"); // unknown
            context.suppress("functionColumn/functionSchem"); // unknown
            context.suppress("functionColumn/length"); // null
            context.suppress("functionColumn/radix"); // null
            context.suppress("functionColumn/remarks"); // null
            context.suppress("functionColumn/scale"); // null
            context.suppress("UDT/attributes"); // InvocationTargetException
            context.suppress("UDT/superTypes"); // InvocationTargetException
//            logger.debug("context.paths: {}", context.getPaths());
            for (final Schema schema : context.getSchemas(null, null)) {
                store(schema, "postgres.embedded.none");
            }
        }
    }
}
