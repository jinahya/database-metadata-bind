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

import static java.lang.invoke.MethodHandles.lookup;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import static java.sql.DriverManager.getConnection;
import java.util.List;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

/**
 * Test with Embedded PostgreSQL Server.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see
 * <a href="https://github.com/yandex-qatools/postgresql-embedded">Embedded
 * PostgreSQL Server (GitHub)</a>
 */
public class EmbeddedPostgresqlTest {

  private static final Logger logger = getLogger(lookup().lookupClass());

  // -------------------------------------------------------------------------
  private static EmbeddedPostgres EMBEDDED_POSTGRES;

  private static String URL;

  // -------------------------------------------------------------------------
  @BeforeClass
  private static void beforeClass() throws Exception {
//        final PostgresStarter<PostgresExecutable, PostgresProcess> starter
//                = PostgresStarter.getDefaultInstance();
//        config = PostgresConfig.defaultWithDbName("test", "test", "test");
//        final PostgresExecutable exec = starter.prepare(config);
//        process = exec.start();

    // starting Postgres
    EMBEDDED_POSTGRES = new EmbeddedPostgres();
    logger.debug("embedded postgres constructed");
    URL = EMBEDDED_POSTGRES.start(
            "localhost", 5432, "dbName", "userName", "password");
    logger.debug("embedded postgres started on {}", URL);
  }

  @AfterClass
  private static void afterClass() throws Exception {
    EMBEDDED_POSTGRES.stop();
    logger.debug("embedded postgres stopped");
  }

  // -------------------------------------------------------------------------
  @Test(enabled = true)
  public void store() throws Exception {
    try (Connection connection = getConnection(URL)) {
      logger.debug("connection: {}", connection);
      final DatabaseMetaData metadata = connection.getMetaData();
      final MetadataContext context = new MetadataContext(metadata);
      context.suppress("schema/functions");
      final List<Catalog> catalogs
              = MetadataContext.getCatalogs(context, true);
      JaxbTests.store(Catalog.class, catalogs, "embedded.postresql");
    }
  }
}
