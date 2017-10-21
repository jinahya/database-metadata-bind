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
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static java.sql.DriverManager.getConnection;
import java.util.List;
import static org.testng.Assert.fail;

/**
 * Test clss for Apache Derby in memory.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MemoryDerbyTest extends MemoryTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    private static final String DRIVER_NAME
            = "org.apache.derby.jdbc.EmbeddedDriver";

    private static final Class<?> DRIVER_CLASS;

    static {
        try {
            DRIVER_CLASS = Class.forName(DRIVER_NAME);
        } catch (final ClassNotFoundException cnfe) {
            throw new InstantiationError(cnfe.getMessage());
        }
    }

    private static final String CONNECTION_URL = "jdbc:derby:memory:test";

    // -------------------------------------------------------------------------
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
            fail("an instance of SQLException should've been thrown");
        } catch (final SQLException sqle) {
            // https://builds.apache.org/job/Derby-docs/lastSuccessfulBuild/artifact/trunk/out/ref/rrefattrib16471.html
            // this is expected
            // Shutdown commands always raise SQLExceptions.
        }
    }

    // -------------------------------------------------------------------------
    @Test
    public void test() throws Exception {
        try (Connection connection = getConnection(CONNECTION_URL)) {
            logger.debug("connected: {}", connection);
            DatabaseMetaData metadata = connection.getMetaData();
            final MetadataContext context = new MetadataContext(metadata);
//            context.alias("column/scopeCatalog", "SCOPE_CATLOG");
            final List<Catalog> catalogs = getCatalogs(context, true);
            store(Catalog.class, catalogs, "memory.derby.catalogs");
            store(ClientInfoProperty.class, context.getClientInfoProperties(),
                  "memory.derby.clientInfoProperties");
            store(TableType.class, context.getTableTypes(),
                  "memory.derby.tableTypes");
            store(TypeInfo.class, context.getTypeInfo(),
                  "memory.derby.typeInfo");
        }
    }

    @Test(enabled = false)
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
