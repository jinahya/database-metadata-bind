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
    void getCatalogs__() throws Exception {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            for (final Catalog catalog : context.getCatalogs(new ArrayList<>())) {
                log.debug("catalog: {}", catalog);
            }
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

    // -------------------------------------------------------------------------------------------------- ...AreDetected
    @Test
    void deletesAreDetected() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<DeletesAreDetected> deletesAreDetectedList = DeletesAreDetected.all(context);
            assertThat(deletesAreDetectedList)
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                    .doesNotContainNull();
            deletesAreDetectedList.forEach(v -> {
                log.debug("deletesAreDetected: {}", v);
            });
        }
    }

    @Test
    void insertsAreDetected() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<InsertsAreDetected> insertsAreDetectedList = InsertsAreDetected.all(context);
            assertThat(insertsAreDetectedList)
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                    .doesNotContainNull();
            insertsAreDetectedList.forEach(v -> {
                log.debug("insertsAreDetected: {}", v);
            });
        }
    }

    @Test
    void updatesAreDetected() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<UpdatesAreDetected> updatesAreDetectedList = UpdatesAreDetected.all(context);
            assertThat(updatesAreDetectedList)
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                    .doesNotContainNull();
            updatesAreDetectedList.forEach(v -> {
                log.debug("updatesAreDetected: {}", v);
            });
        }
    }

    // --------------------------------------------------------------------------------------------------- ...AreVisible
    @Test
    void othersDeletesAreVisible() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OthersDeletesAreVisible> all = OthersDeletesAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("othersDeletesAreVisible: {}", v);
            });
        }
    }

    @Test
    void othersInsertsAreVisible() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OthersInsertsAreVisible> all = OthersInsertsAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("othersInsertsAreVisible: {}", v);
            });
        }
    }

    @Test
    void othersUpdatesAreVisible() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OthersUpdatesAreVisible> all = OthersUpdatesAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("othersUpdatesAreVisible: {}", v);
            });
        }
    }

    @Test
    void ownDeletesAreVisible() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OwnDeletesAreVisible> all = OwnDeletesAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("ownDeletesAreVisible: {}", v);
            });
        }
    }

    @Test
    void ownInsertsAreVisible() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OwnInsertsAreVisible> all = OwnInsertsAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("ownInsertsAreVisible: {}", v);
            });
        }
    }

    @Test
    void ownUpdatesAreVisible() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection).suppress(SQLFeatureNotSupportedException.class);
            final List<OwnUpdatesAreVisible> all = OwnUpdatesAreVisible.all(context);
            Assertions.assertThat(all)
                    .doesNotContainNull()
                    .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
            all.forEach(v -> {
                log.debug("ownUpdatesAreVisible: {}", v);
            });
        }
    }
}
