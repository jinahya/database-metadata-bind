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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import javax.xml.bind.JAXBContext;
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
@Test(enabled = false)
public class MckoiEmbeddedTest {


    private static final Logger logger = getLogger(MckoiEmbeddedTest.class);


    private static final String DRIVER_NAME = "com.mckoi.JDBCDriver";


    private static final String CONNECTION_URL
        = "jdbc:mckoi:local://./target/test-classes/mckoi/db.conf";


    @BeforeClass
    private static void beforeClass() throws Exception {

        Class.forName(DRIVER_NAME);
    }


    @AfterClass
    private static void afterClass() throws Exception {

    }


    @Test
    public void retrieve() throws Exception {

        final Metadata metadata;

        try (Connection connection
            = getConnection(CONNECTION_URL, "mckoi", "mckoi")) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            context.addSuppressions(
                "metadata/clientInfoProperties",
                "metadata/databaseMajorVersion",
                "metadata/databaseMinorVersion",
                "metadata/generatedKeyAlwaysReturned",
                "metadata/locatorsUpdateCopy",
                "metadata/SQLStateType",
                "schema/functions",
                "schema/UDTs"
            );
            metadata = context.getMetadata();
        }

        final JAXBContext context = JAXBContext.newInstance(Metadata.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        final File file = new File("target", "mckoi.embedded.metadata.xml");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            marshaller.marshal(metadata, outputStream);
            outputStream.flush();
        }
    }

}

