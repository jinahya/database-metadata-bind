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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

import static java.sql.DriverManager.getConnection;

/**
 * Test with Embedded PostgreSQL Server.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://github.com/yandex-qatools/postgresql-embedded">Embedded
 * PostgreSQL Server (GitHub)</a>
 */
@Slf4j
public class EmbeddedPostgresqlTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static EmbeddedPostgres EMBEDDED_POSTGRES;

    private static String URL;

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeClass
    private static void beforeClass() throws Exception {
        EMBEDDED_POSTGRES = new EmbeddedPostgres();
        log.debug("embedded postgres constructed");
        URL = EMBEDDED_POSTGRES.start("localhost", 5432, "dbName", "userName", "password");
        log.debug("embedded postgres started on {}", URL);
    }

    @AfterClass
    private static void afterClass() throws Exception {
        EMBEDDED_POSTGRES.stop();
        log.debug("embedded postgres stopped");
    }

    // -------------------------------------------------------------------------
    @Test(enabled = true)
    public void store() throws Exception {
        try (Connection connection = getConnection(URL)) {
            log.debug("connection: {}", connection);
            final DatabaseMetaData metadata = connection.getMetaData();
            final MetadataContext context = new MetadataContext(metadata);
            context.addSuppressionPaths(
                    "column/isGeneratedcolumn", // null value
                    "column/scopeCatalog", // null value
                    "schema/functions",
                    "procedureColumn/charOctetLength", // null value
                    "procedureColumn/length", // null value
                    "procedureColumn/precision", // null value
                    "procedureColumn/radix", // null value
                    "procedureColumn/remarks", // null value
                    "table/pseudoColumns", // not yet implemented
                    "table/refGeneration", // not yet implemented
                    "table/selfReferencingColName", // not yet implemented
                    "table/superTables", // not yet implemented
                    "table/typeCat", // null value
                    "table/typeName", // null value
                    "table/typeSchem", // not yet implemented
                    "UDT/attributes", // not yet implemented
                    "UDT/className", // null value
                    "UDT/remarks", // null value
                    "UDT/superTypes", // not yet implemented
                    "versionColumn/bufferLength", // null value
                    "versionColumn/columnSize" // null value
            );
            final List<Catalog> catalogs = MetadataContext.getCatalogs(context, true);
            JaxbTests.store(Catalog.class, catalogs, "embedded.postresql.catalogs");
        }
    }
}
