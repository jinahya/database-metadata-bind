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

import ch.vorburger.mariadb4j.DB;
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

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MariaDbEmbeddedTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    private static DB DB_;

    // -------------------------------------------------------------------------
    @BeforeClass
    private static void beforeClass() throws Exception {
        DB_ = DB.newEmbeddedDB(3306);
        DB_.start();
        logger.debug("embedded mariadb started");
    }

    @AfterClass
    private static void afterClass() throws Exception {
        DB_.stop();
        logger.debug("embedded mariadb stopped");
        DB_ = null;
    }

    @Test
    public void retrieve() throws Exception {
        final Metadata metadata;
        final String url = "jdbc:mysql://localhost/test";
        final String user = "root";
        final String password = "";
        try (Connection connection = getConnection(url, user, password)) {
            logger.debug("connection: {}", connection);
            final DatabaseMetaData database = connection.getMetaData();
            logger.debug("database: {}", database);
            final MetadataContext context = new MetadataContext(database);
            context.suppress("a"
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
                "target", "mariadb.embedded.metadata.xml");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            marshaller.marshal(metadata, outputStream);
            outputStream.flush();
        }
    }
}
