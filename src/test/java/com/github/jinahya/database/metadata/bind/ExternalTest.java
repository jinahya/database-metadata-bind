package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static com.github.jinahya.database.metadata.bind.JaxbTests.store;
import static com.github.jinahya.database.metadata.bind.MetadataContext.getCatalogs;
import static java.sql.DriverManager.getConnection;
import static org.testng.Assert.assertNotNull;

/**
 * Test class for remote MySQL.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
public class ExternalTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void test() throws SQLException, JAXBException {
        final String client = System.getProperty("client");
        assertNotNull(client, "client is null");
        log.info("using {}", client);
        final String url = System.getProperty("url");
        assertNotNull(url, "url is null");
        log.info("url: {}", url);
        final String user = System.getProperty("user");
        assertNotNull(user, "user is null");
        final String password = System.getProperty("password");
        assertNotNull(password, "password is null");
        log.info("connecting...");
        try (Connection connection = getConnection(url, user, password)) {
            log.info("connected: {}", connection);
            final DatabaseMetaData metadata = connection.getMetaData();
            final MetadataContext context = new MetadataContext(metadata);
            final String paths = System.getProperty("paths");
            if (paths != null) {
                for (String path : paths.split(",")) {
                    path = path.trim();
                    if (path.isEmpty()) {
                        continue;
                    }
                    log.info("suppressing {}", path);
                    context.addSuppressionPaths(path);
                }
            }
            final List<Catalog> catalogs = getCatalogs(context, true);
            store(Catalog.class, catalogs, "external.catalogs");
            store(ClientInfoProperty.class, context.getClientInfoProperties(), "external.clientInfoProperties");
            store(TableType.class, context.getTableTypes(), "external.tableTypes");
            store(TypeInfo.class, context.getTypeInfo(), "external.typeInfo");
        }
    }
}
