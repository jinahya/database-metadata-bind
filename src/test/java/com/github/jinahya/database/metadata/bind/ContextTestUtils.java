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
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTestUtils {

    static void writeCatalogs(final Context context) throws SQLException, JAXBException {
        final var catalogs = new ArrayList<>();
        context.getCatalogs(catalogs::add);
        final String pathname = TestUtils.getFilenamePrefix(context) + " - catalogs.xml";
        final File target = Paths.get("target", pathname).toFile();
//        Wrapper.marshalFormatted(Catalog.class, catalogs, target);
    }

    static void writeDeletesAreDetected(final Context context) throws SQLException, JAXBException {
        Objects.requireNonNull(context, "context is null");
        final List<DeletesAreDetected> all = DeletesAreDetected.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .hasSizeLessThanOrEqualTo(ResultSetType.values().length)
                .doesNotContainNull();
        final String pathname = TestUtils.getFilenamePrefix(context) + " - deletesAreDetected.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(DeletesAreDetected.class, all, target);
    }

    static void writeFunctions(final Context context) throws SQLException, JAXBException {
        assert context != null;
        final List<Function> functions;
        try {
            functions = new ArrayList<>();
            context.getFunctions(null, null, "%", functions::add);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getFunctions", sqlfnse);
            return;
        }
        final String pathname = TestUtils.getFilenamePrefix(context) + " - functions.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(Function.class, functions, target);
    }

    static void writeFunctionColumns(final Context context) throws SQLException, JAXBException {
        assert context != null;
        final var productName = context.databaseMetaData.getDatabaseProductName();
        if (Objects.equals(productName, "SQLite")) {
            log.debug("skipping for product name: {}", productName);
            return;
        }
        final List<FunctionColumn> functionColumns;
        try {
            functionColumns = new ArrayList<>();
            context.getFunctionColumns(null, null, "%", "%", functionColumns::add);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getFunctionColumns", sqlfnse);
            return;
        }
        final String pathname = TestUtils.getFilenamePrefix(context) + " - functionColumns.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(FunctionColumn.class, functionColumns, target);
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

    static void writeProcedures(final Context context) throws SQLException, JAXBException {
        final List<Procedure> procedures;
        try {
            procedures = new ArrayList<>();
            context.getProcedures(null, null, "%", procedures::add);
        } catch (final SQLFeatureNotSupportedException slqfnse) {
            log.error("not supported; getProcedures", slqfnse);
            return;
        }
        for (final var procedure : procedures) {
            assertThat(procedure)
                    .usingComparator(Procedure.COMPARATOR)
                    .isEqualTo(procedure);
            context.getProcedureColumns(
                    procedure.getProcedureCat(),
                    procedure.getProcedureSchem(),
                    procedure.getProcedureName(),
                    "%",
                    procedure.getProcedureColumns()::add
            );
            for (final var procedureColumn : procedure.getProcedureColumns()) {
//                assertThat(procedureColumn)
//                        .isNotNull()
//                        .usingComparator(ProcedureColumn.COMPARATOR)
//                        .isEqualTo(procedureColumn);
            }
        }
        final String pathname = TestUtils.getFilenamePrefix(context) + " - procedures.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(Procedure.class, procedures, target);
    }

    static void writeSchemas(final Context context) throws SQLException, JAXBException {
        final List<Schema> schemas = context.getSchemas((String) null, null);
        final String pathname = TestUtils.getFilenamePrefix(context) + " - schemas.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(Schema.class, schemas, target);
    }

    static void writeSupportsConvert(final Context context) throws SQLException, JAXBException {
        Objects.requireNonNull(context, "context is null");
        final List<SupportsConvert> all = SupportsConvert.getAllInstances(context, new ArrayList<>());
        assertThat(all)
                .doesNotContainNull();
        final String pathname = TestUtils.getFilenamePrefix(context) + " - supportsConvert.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(SupportsConvert.class, all, target);
    }

    static void writeTables(final Context context) throws SQLException, JAXBException {
        final List<Table> tables = context.getTables(null, null, "%", null);
        for (final var table : tables) {
            table.retrieveChildren(context);
        }
        final String pathname = TestUtils.getFilenamePrefix(context) + " - tables.xml";
        final File target = Paths.get("target", pathname).toFile();
        Wrapper.marshalFormatted(Table.class, tables, target);
    }

    static void writeTypeInfo(final Context context) throws SQLException, JAXBException {
        final List<TypeInfo> typeInfo = context.getTypeInfo();
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

    static void writeFiles(final Context context) throws SQLException, JAXBException {
        Objects.requireNonNull(context, "context is null");
        writeCatalogs(context);
        writeSupportsConvert(context);
        writeTypeInfo(context);
        writeDeletesAreDetected(context);
        writeFunctions(context);
        writeFunctionColumns(context);
        writeInsertsAreDetected(context);
        writeUpdatesAreDetected(context);
        writeOthersDeletesAreVisible(context);
        writeOthersInsertsAreVisible(context);
        writeOthersUpdatesAreVisible(context);
        writeOwnDeletesAreVisible(context);
        writeOwnInsertsAreVisible(context);
        writeOwnUpdatesAreVisible(context);
        writeProcedures(context);
        writeSchemas(context);
        writeTables(context);
    }

    private ContextTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
