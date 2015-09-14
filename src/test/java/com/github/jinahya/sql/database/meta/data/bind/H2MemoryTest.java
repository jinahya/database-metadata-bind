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


package com.github.jinahya.sql.database.meta.data.bind;


import com.github.jinahya.sql.database.meta.data.bind.Suppressions;
import com.github.jinahya.sql.database.meta.data.bind.SuppressionKey;
import com.github.jinahya.sql.database.meta.data.bind.Suppression;
import com.github.jinahya.sql.database.meta.data.bind.Metadata;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class H2MemoryTest {


    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(H2MemoryTest.class);


    /**
     * driver name.
     */
    private static final String DRIVER_NAME = "org.h2.Driver";


    /**
     * connection url.
     */
    private static final String CONNECTION_URL
        = "jdbc:h2:mem:test"; //;DB_CLOSE_DELAY=-1";


    @BeforeClass
    private static void beforeClass() throws SQLException {

        LOGGER.trace("beforeClass()");
    }


    @AfterClass
    private static void afterClass() throws SQLException {

        LOGGER.trace("afterClass()");
    }


    @Test
    public void retrieve() throws SQLException, JAXBException, IOException {

        final Metadata metadata;

        try (Connection connection
            = DriverManager.getConnection(CONNECTION_URL)) {

            final DatabaseMetaData databaseMetaData = connection.getMetaData();
            final SuppressionKey suppressionKey
                = SuppressionKey.newInstance(databaseMetaData);
            LOGGER.trace("suppressionKey: {}", suppressionKey);

            final Suppressions suppressions = Suppressions.loadInstance();
            final Suppression suppression
                = suppressions.getSuppression(suppressionKey);

            metadata = Metadata.newInstance(databaseMetaData, suppression);

            //metadata.print(System.out);

            MetadataVerifier.verify(metadata);
        }

        final JAXBContext context = JAXBContext.newInstance(Metadata.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        final File file = new File("target", "h2.memory.metadata.xml");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            marshaller.marshal(metadata, outputStream);
            outputStream.flush();
        }

    }


}

