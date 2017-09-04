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
package com.github.jinahya.sql.database.metadata.bind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import static java.lang.invoke.MethodHandles.lookup;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PostgreSQLEmbeddedTest {

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

    @Test
    public void retrieve() throws Exception {
        final Metadata metadata;
        try (Connection connection = getConnection(URL)) {
            logger.debug("connection: {}", connection);
            final DatabaseMetaData database = connection.getMetaData();
            logger.debug("database: {}", database);
            final MetadataContext context = new MetadataContext(database);
            context.suppressions(
                    "column/isGeneratedcolumn",
                    "column/scopeCatalog",
                    "primaryKey/columnName",
                    "primaryKey/keySeq",
                    "primaryKey/pkName",
                    "primaryKey/tableCat",
                    "primaryKey/tableSchem",
                    "primaryKey/tableName",
                    "schema/functions",
                    "schema/procedures",
                    "schema/tableCatalog",
                    "schema/tableSchem",
                    "table/bestRowIdentifiers",
                    "table/indexInfo",
                    "table/refGeneration",
                    "table/remarks",
                    "table/selfReferencingColName",
                    "table/superTables",
                    "table/tableCat",
                    "table/tableSchem",
                    "table/tableName",
                    "table/tableType",
                    "table/typeCat",
                    "table/typeName",
                    "table/typeSchem",
                    "table/pseudoColumns",
                    "UDT/attributes",
                    "UDT/superTypes",
                    "versionColumn/bufferLength",
                    "versionColumn/columnSize"
            );
            try {
                metadata = context.getMetadata();
            } catch (final Exception e) {
                e.printStackTrace(System.out);
                System.out.println("---------------------> " + e);
                System.out.println("---------------------> " + e);
                System.out.println("---------------------> " + e);
                System.out.println("---------------------> " + e);
                System.out.println("---------------------> " + e);
                System.out.println("---------------------> " + e);
                System.out.println("---------------------> " + e);
                System.out.println("---------------------> " + e);
                System.out.println("---------------------> " + e);
                throw new SkipException("", e);
            }
        }
        final JAXBContext context = JAXBContext.newInstance(Metadata.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final File file = new File(
                "target", "postgresql.embedded.metadata.xml");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            marshaller.marshal(metadata, outputStream);
            outputStream.flush();
        }
    }
}
