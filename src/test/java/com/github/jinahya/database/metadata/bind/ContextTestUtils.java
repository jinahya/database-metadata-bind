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

import javax.xml.bind.JAXBException;
import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTestUtils {

    static void writeCatalogs(final Context context) throws SQLException, JAXBException {
        final List<Catalog> catalogs = context.getCatalogs(new ArrayList<>());
        final String pathname = TestUtils.getFilenamePrefix(context) + " - catalogs.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(Catalog.class, catalogs, target);
    }

    static void writeDeletesAreDetected(final Context context) throws SQLException, JAXBException {
        requireNonNull(context, "context is null");
        final List<DeletesAreDetected> all = DeletesAreDetected.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                .doesNotContainNull();
        final String pathname = TestUtils.getFilenamePrefix(context) + " - deletesAreDetected.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(DeletesAreDetected.class, all, target);
    }

    static void writeInsertsAreDetected(final Context context) throws SQLException, JAXBException {
        final List<InsertsAreDetected> all = InsertsAreDetected.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                .doesNotContainNull();
        final String pathname = TestUtils.getFilenamePrefix(context) + " - insertsAreDetected.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(InsertsAreDetected.class, all, target);
    }

    static void writeOthersDeletesAreVisible(final Context context) throws SQLException, JAXBException {
        final List<OthersDeletesAreVisible> all = OthersDeletesAreVisible.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .doesNotContainNull()
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
        final String pathname = TestUtils.getFilenamePrefix(context) + " - othersDeletesAreVisible.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(OthersDeletesAreVisible.class, all, target);
    }

    static void writeOthersInsertsAreVisible(final Context context) throws SQLException, JAXBException {
        final List<OthersInsertsAreVisible> all = OthersInsertsAreVisible.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .doesNotContainNull()
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
        final String pathname = TestUtils.getFilenamePrefix(context) + " - othersInsertsAreVisible.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(OthersInsertsAreVisible.class, all, target);
    }

    static void writeOthersUpdatesAreVisible(final Context context) throws SQLException, JAXBException {
        final List<OthersUpdatesAreVisible> all = OthersUpdatesAreVisible.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .doesNotContainNull()
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
        final String pathname = TestUtils.getFilenamePrefix(context) + " - othersUpdatesAreVisible.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(OthersUpdatesAreVisible.class, all, target);
    }

    static void writeOwnDeletesAreVisible(final Context context) throws SQLException, JAXBException {
        final List<OwnDeletesAreVisible> all = OwnDeletesAreVisible.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .doesNotContainNull()
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
        final String pathname = TestUtils.getFilenamePrefix(context) + " - ownDeletesAreVisible.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(OwnDeletesAreVisible.class, all, target);
    }

    static void writeOwnInsertsAreVisible(final Context context) throws SQLException, JAXBException {
        final List<OwnInsertsAreVisible> all = OwnInsertsAreVisible.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .doesNotContainNull()
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
        final String pathname = TestUtils.getFilenamePrefix(context) + " - ownInsertsAreVisible.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(OwnInsertsAreVisible.class, all, target);
    }

    static void writeOwnUpdatesAreVisible(final Context context) throws SQLException, JAXBException {
        final List<OwnUpdatesAreVisible> all = OwnUpdatesAreVisible.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .doesNotContainNull()
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length);
        final String pathname = TestUtils.getFilenamePrefix(context) + " - ownUpdatesAreVisible.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(OwnUpdatesAreVisible.class, all, target);
    }

    static void writeSchemas(final Context context) throws SQLException, JAXBException {
        final List<Schema> schemas = context.getSchemas((String) null, null, new ArrayList<>());
        final String pathname = TestUtils.getFilenamePrefix(context) + " - schemas.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(Schema.class, schemas, target);
    }

    static void writeSupportsConvert(final Context context) throws SQLException, JAXBException {
        requireNonNull(context, "context is null");
        final List<SupportsConvert> all = SupportsConvert.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .doesNotContainNull();
        final String pathname = TestUtils.getFilenamePrefix(context) + " - supportsConvert.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(SupportsConvert.class, all, target);
    }

    static void writeTables(final Context context) throws SQLException, JAXBException {
        final List<Table> tables = context.getTables(null, null, null, null, new ArrayList<>());
        final String pathname = TestUtils.getFilenamePrefix(context) + " - tables.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(Table.class, tables, target);
    }

    static void writeTypeInfo(final Context context) throws SQLException, JAXBException {
        final List<TypeInfo> typeInfo = context.getTypeInfo(new ArrayList<>());
        final String pathname = TestUtils.getFilenamePrefix(context) + " - typeInfo.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(TypeInfo.class, typeInfo, target);
    }

    static void writeUpdatesAreDetected(final Context context) throws SQLException, JAXBException {
        final List<UpdatesAreDetected> all = UpdatesAreDetected.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                .doesNotContainNull();
        final String pathname = TestUtils.getFilenamePrefix(context) + " - updatesAreDetected.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(UpdatesAreDetected.class, all, target);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void writeFiles(final Context context) throws SQLException, JAXBException {
        requireNonNull(context, "context is null");
        writeCatalogs(context);
        writeSupportsConvert(context);
        writeTypeInfo(context);
        writeDeletesAreDetected(context);
        writeInsertsAreDetected(context);
        writeUpdatesAreDetected(context);
        writeOthersDeletesAreVisible(context);
        writeOthersInsertsAreVisible(context);
        writeOthersUpdatesAreVisible(context);
        writeOwnDeletesAreVisible(context);
        writeOwnInsertsAreVisible(context);
        writeOwnUpdatesAreVisible(context);
        writeSchemas(context);
        writeTables(context);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ContextTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
