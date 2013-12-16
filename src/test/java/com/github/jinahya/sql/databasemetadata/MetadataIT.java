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


package com.github.jinahya.sql.databasemetadata;


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
import org.testng.SkipException;
import org.testng.annotations.Test;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class MetadataIT {


    private static final Logger LOGGER
        = LoggerFactory.getLogger(MetadataIT.class);


    @Test
    public void test()
        throws ClassNotFoundException, SQLException, JAXBException,
               IOException {

        final String driverName = System.getProperty("driverName");
        if (driverName != null) {
            LOGGER.debug("driverName: {}", driverName);
            Class.forName(driverName);
        }

        final String connectionUrl = System.getProperty("connectionUrl");
        if (connectionUrl == null) {
            LOGGER.info("no connectionUrl. skipping...");
            throw new SkipException("no connectionUrl");
        }
        LOGGER.debug("connectionUrl: {}", connectionUrl);
        assert connectionUrl != null;

        try (Connection connection
            = DriverManager.getConnection(connectionUrl)) {

            final DatabaseMetaData database = connection.getMetaData();
            LOGGER.debug("database: {}", database);

            final SuppressionKey suppressionKey
                = SuppressionKey.newInstance(database);
            LOGGER.debug("suppressionKey: {}", suppressionKey);

            final Suppressions suppressions = Suppressions.loadInstance();
            LOGGER.debug("suppressions loaded");

            for (final Suppression suppression : suppressions.getSuppressions()) {
                final SuppressionKey supperssionKey = suppression.getKey();
                LOGGER.debug("loaded.suppressionKey: {}", supperssionKey);
            }

            Suppression suppression
                = suppressions.getSuppression(suppressionKey);
            if (suppression == null) {
                LOGGER.debug(
                    "no suppression matched. using an empty instance.");
                suppression = new Suppression();
            }
            assert suppression != null;
            suppression.print(System.out);

            final Metadata metadata
                = Metadata.newInstance(database, suppression);

            final JAXBContext context = JAXBContext.newInstance(Metadata.class);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                                   Boolean.TRUE);
            final File file = new File("target", "metadata.xml");
            try (OutputStream outputStream = new FileOutputStream(file)) {
                marshaller.marshal(metadata, outputStream);
                outputStream.flush();
            }


            metadata.print(System.out);
        }
    }


}

