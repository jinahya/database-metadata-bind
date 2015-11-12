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
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class DerbyMemoryTest {


    private static final Logger logger = getLogger(DerbyMemoryTest.class);


    private static final String DRIVER_NAME
        = "org.apache.derby.jdbc.EmbeddedDriver";


    private static final String CONNECTION_URL = "jdbc:derby:memory:test";


    @BeforeClass
    private static void beforeClass() throws SQLException {

        final Properties properties = new Properties();
        properties.put("create", "true");

        final Connection connection = getConnection(CONNECTION_URL, properties);
        try {
        } finally {
            connection.close();
        }
    }


    @AfterClass
    private static void afterClass() throws SQLException {

        final Properties properties = new Properties();
        properties.put("shutdown", "true");

        try {
            final Connection connection
                = getConnection(CONNECTION_URL, properties);
            try {
            } finally {
                connection.close();
            }
        } catch (final SQLException sqle) {
            // this is expected
            // Shutdown commands always raise SQLExceptions.
        }
    }


    @Test
    public void retrieve() throws Exception {

        final Metadata metadata;

        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            metadata = context.getMetadata();
        }

        final JAXBContext context = JAXBContext.newInstance(Metadata.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        final File dir = new File("target", "xml");
        dir.mkdir();
        final File file = new File(dir, "derby.memory.metadata.xml");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            marshaller.marshal(metadata, outputStream);
            outputStream.flush();
        }
    }


    @Test
    public void pattern() throws Exception {

        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            for (final Table table
                 : context.getTables(null, null, "SYS%", null)) {
                logger.debug("table.name: {}", table.getTableName());
            }
            for (final Table table : context.getTables(null, null, "%", null)) {
                logger.debug("table.name: {}", table.getTableName());
            }
        }
    }


}

