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
import static java.lang.String.format;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import static java.util.Optional.ofNullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PostgreSQLEmbeddedTest {


    private static final Logger logger
        = getLogger(PostgreSQLEmbeddedTest.class);


    private static PostgresConfig config;


    private static PostgresProcess process = null;


    @BeforeClass
    private static void beforeClass() throws Exception {

        final PostgresStarter<PostgresExecutable, PostgresProcess> starter
            = PostgresStarter.getDefaultInstance();
        config = PostgresConfig.defaultWithDbName("test", "test", "test");
        final PostgresExecutable exec = starter.prepare(config);
        process = exec.start();
    }


    @AfterClass
    private static void afterClass() throws Exception {

        ofNullable(process).ifPresent(PostgresProcess::stop);
    }


    @Test
    public void retrieve() throws Exception {

        final Metadata metadata;

        final String url = format(
            "jdbc:postgresql://%s:%s/%s?user=%s&password=%s",
            config.net().host(),
            config.net().port(),
            config.storage().dbName(),
            config.credentials().username(),
            config.credentials().password());

        try (Connection connection = getConnection(url)) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            context.addSuppressions(
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
                "UDT/attributes"
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

        final File file
            = new File("target", "postgresql.embedded.metadata.xml");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            marshaller.marshal(metadata, outputStream);
            outputStream.flush();
        }
    }

}

