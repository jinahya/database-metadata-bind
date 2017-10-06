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

import static com.github.jinahya.database.metadata.bind.JaxbTests.store;
import static com.github.jinahya.database.metadata.bind.MetadataContext.getCatalogs;
import static java.lang.invoke.MethodHandles.lookup;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test for SQLite.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MemorySqliteTest extends MemoryTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    private static final String CONNECTION_URL = "jdbc:sqlite::memory:";

    // -------------------------------------------------------------------------
    @BeforeClass
    private static void beforeClass() throws SQLException {
    }

    @AfterClass
    private static void afterClass() throws SQLException {
    }

    // -------------------------------------------------------------------------
    @Test(enabled = true)
    public void test() throws Exception {
        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData database = connection.getMetaData();
            final MetadataContext context = new MetadataContext(database);
            context.suppress("schema/functions");
            final List<Catalog> catalogs = getCatalogs(context, true);
            store(Catalog.class, catalogs, "memory.sqlite.catalogs");
            store(ClientInfoProperty.class,
                  context.getClientInfoProperties(),
                  "memory.sqlite.clientInfoProperties");
            store(TableType.class, context.getTableTypes(),
                  "memory.sqlite.tableTypes");
            store(TypeInfo.class, context.getTypeInfo(),
                  "memory.sqlite.typeInfo");
        }
//        final JAXBContext context = JAXBContext.newInstance(Metadata.class);
//        final Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        final File file = new File("target", "sqlite.memory.metadata.xml");
//        try (OutputStream outputStream = new FileOutputStream(file)) {
//            marshaller.marshal(metadata, outputStream);
//            outputStream.flush();
//        }
    }
}
