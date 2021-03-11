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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
abstract class MemoryTest {

    /**
     * Returns a connection
     *
     * @return a connection.
     * @throws SQLException if a database error occurs.
     */
    abstract Connection connect() throws SQLException;

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void writeMetadataToFiles() throws Exception {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final Metadata metadata = Metadata.newInstance(context);
            final String name;
            {
                final String simpleName = getClass().getSimpleName();
                name = ("memory." + (simpleName.substring(6, simpleName.indexOf("Test"))) + ".metadata").toLowerCase();
            }
            MetadataTests.writeToFiles(metadata, name);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void getCatalogs__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<Catalog> catalogs = context.getCatalogs(new ArrayList<>());
            for (final Catalog catalog : catalogs) {
                log.debug("catalog: {}", catalog);
            }
            final String pathname = TestUtils.getFilenamePrefix(context) + ".catalogs.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Catalog.class, catalogs, target);
        }
    }

    @Test
    void getSchemas__() throws Exception {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            for (final Schema schema : context.getSchemas(null, null, new ArrayList<>())) {
                log.debug("schema: {}", schema);
            }
        }
    }

    @Test
    void getTables__() throws Exception {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            for (final Table table : context.getTables(null, null, null, null, new ArrayList<>())) {
                log.debug("table: {}", table);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------- supportsConvert
    @Test
    void supportsConvert() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<SupportsConvert> all = SupportsConvert.getAllInstances(context);
            assertThat(all)
                    .doesNotContainNull();
            all.forEach(v -> {
                log.debug("supportsConvert: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".supportsConvert.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SupportsConvert.class, all, target);
        }
    }

    // -------------------------------------------------------------------------------------------------- ...AreDetected
    @Test
    void deletesAreDetected() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<DeletesAreDetected> all = DeletesAreDetected.getAllInstances(context);
            assertThat(all)
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                    .doesNotContainNull();
            all.forEach(v -> {
                log.debug("deletesAreDetected: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".deletesAreDetected.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(DeletesAreDetected.class, all, target);
        }
    }

    @Test
    void insertsAreDetected() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<InsertsAreDetected> all = InsertsAreDetected.getAllInstances(context);
            assertThat(all)
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                    .doesNotContainNull();
            all.forEach(v -> {
                log.debug("insertsAreDetected: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".insertsAreDetected.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(InsertsAreDetected.class, all, target);
        }
    }

    @Test
    void updatesAreDetected() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<UpdatesAreDetected> all = UpdatesAreDetected.getAllInstances(context);
            assertThat(all)
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                    .doesNotContainNull();
            all.forEach(v -> {
                log.debug("updatesAreDetected: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".updatesAreDetected.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(UpdatesAreDetected.class, all, target);
        }
    }

    // --------------------------------------------------------------------------------------------------- ...AreVisible
    @Test
    void othersDeletesAreVisible() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OthersDeletesAreVisible> all = OthersDeletesAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("othersDeletesAreVisible: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".othersDeletesAreVisible.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OthersDeletesAreVisible.class, all, target);
        }
    }

    @Test
    void othersInsertsAreVisible() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OthersInsertsAreVisible> all = OthersInsertsAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("othersInsertsAreVisible: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".othersInsertsAreVisible.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OthersInsertsAreVisible.class, all, target);
        }
    }

    @Test
    void othersUpdatesAreVisible() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OthersUpdatesAreVisible> all = OthersUpdatesAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("othersUpdatesAreVisible: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".othersUpdatesAreVisible.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OthersUpdatesAreVisible.class, all, target);
        }
    }

    @Test
    void ownDeletesAreVisible() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OwnDeletesAreVisible> all = OwnDeletesAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("ownDeletesAreVisible: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".ownDeletesAreVisible.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OwnDeletesAreVisible.class, all, target);
        }
    }

    @Test
    void ownInsertsAreVisible() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OwnInsertsAreVisible> all = OwnInsertsAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("ownInsertsAreVisible: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".ownInsertsAreVisible.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OwnInsertsAreVisible.class, all, target);
        }
    }

    @Test
    void ownUpdatesAreVisible() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OwnUpdatesAreVisible> all = OwnUpdatesAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("ownUpdatesAreVisible: {}", v);
            });
            final String pathname = TestUtils.getFilenamePrefix(context) + ".ownUpdatesAreVisible.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OwnUpdatesAreVisible.class, all, target);
        }
    }
}
