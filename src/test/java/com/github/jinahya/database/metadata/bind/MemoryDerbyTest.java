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

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static com.github.jinahya.database.metadata.bind.JaxbTests.store;
import static com.github.jinahya.database.metadata.bind.MetadataContext.getCatalogs;
import static java.sql.DriverManager.getConnection;
import static org.testng.Assert.fail;

/**
 * Test clss for Apache Derby in memory.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
public class MemoryDerbyTest extends MemoryTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static final String DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";

    private static final Class<?> DRIVER_CLASS;

    static {
        try {
            DRIVER_CLASS = Class.forName(DRIVER_NAME);
        } catch (final ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            throw new InstantiationError(cnfe.getMessage());
        }
    }

    private static final String CONNECTION_URL = "jdbc:derby:memory:test";

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    @Test(enabled = true)
    public void test() throws Exception {
        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            final MetadataContext context = new MetadataContext(metadata);
            context.addSuppressionPaths(
                    "column/charOctetLength",
                    "column/numPrecRadix",
                    "functionColumn/radix",
                    "functionColumn/remarks", // null value
                    "functionColumn/scale",
                    "indexInfo/cardinality",
                    "indexInfo/pages",
                    "procedureColumn/radix",
                    "procedureColumn/remarks",
                    "typeInfo/autoIncrement", // null value
                    "typeInfo/maximumScale", // null value
                    "typeInfo/minimumScale", // null value
                    "typeInfo/numPrecRadix", // null value
                    "typeInfo/precision", // null value
                    "typeInfo/unsignedAttribute" // null value
            );
            final List<Catalog> catalogs = getCatalogs(context, true);
            store(Catalog.class, catalogs, "memory.derby.catalogs");
            store(ClientInfoProperty.class, context.getClientInfoProperties(), "memory.derby.clientInfoProperties");
            store(TableType.class, context.getTableTypes(), "memory.derby.tableTypes");
            store(TypeInfo.class, context.getTypeInfo(), "memory.derby.typeInfo");
        }
    }

    @Test(enabled = false)
    public void pattern() throws Exception {
        try (Connection connection = getConnection(CONNECTION_URL)) {
            final DatabaseMetaData metadata = connection.getMetaData();
            final MetadataContext context = new MetadataContext(metadata);
            for (final Table table : context.getTables(null, null, "SYS%", null)) {
                log.debug("table.name: {}", table.getTableName());
            }
            for (final Table table : context.getTables(null, null, "%", null)) {
                log.debug("table.name: {}", table.getTableName());
            }
        }
    }
}
