/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
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
import java.io.IOException;
import static java.lang.invoke.MethodHandles.lookup;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;
import static com.github.jinahya.database.metadata.bind.JaxbTests.store;

/**
 * Test class for remote MySQL.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class ExternalTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    @Test
    public void test() throws SQLException, JAXBException, IOException {
        final String client = System.getProperty("client");
        assertNotNull(client, "client is null");
        logger.info("using {}", client);
        final String url = System.getProperty("url");
        assertNotNull(url, "url is null");
        logger.info("url: {}", url);
        final String user = System.getProperty("user");
        assertNotNull(user, "user is null");
        final String password = System.getProperty("password");
        assertNotNull(password, "password is null");
        logger.info("connecting...");
        try (Connection connection = getConnection(url, user, password)) {
            logger.info("connected: {}", connection);
            final DatabaseMetaData metadata = connection.getMetaData();
            final MetadataContext context = new MetadataContext(metadata);
            final String paths = System.getProperty("paths");
            if (paths != null) {
                for (String path : paths.split(",")) {
                    path = path.trim();
                    if (path.isEmpty()) {
                        continue;
                    }
                    logger.info("suppressing {}", path);
                    context.suppress(path);
                }
            }
            final List<Catalog> catalogs = getCatalogs(context, true);
            logger.debug("catalogs: {}", catalogs);
            store(Catalog.class, catalogs, "external.catalogs");
            store(ClientInfoProperty.class, context.getClientInfoProperties(),
                  "external.clientInfoProperties");
            store(TableType.class, context.getTableTypes(),
                  "external.tableTypes");
            store(TypeInfo.class, context.getTypeInfo(), "external.typeInfo");

        }
    }
}
