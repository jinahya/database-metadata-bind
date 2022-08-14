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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
abstract class MemoryTest {

    /**
     * Creates a new instance.
     */
    MemoryTest() {
        super();
    }

    /**
     * Returns a connection
     *
     * @return a connection.
     * @throws SQLException if a database error occurs.
     */
    protected abstract Connection connect() throws SQLException;

    Context context(final Connection connection) throws SQLException {
        return Context.newInstance(connection);
    }

    Metadata metadata(final Context context) throws SQLException {
        return Metadata.newInstance(context);
    }

    protected <R> R applyContext(final java.util.function.Function<? super Context, ? extends R> function)
            throws SQLException {
        requireNonNull(function, "function is null");
        try (Connection connection = connect()) {
            final Context context = context(connection);
            return function.apply(context);
        }
    }

    protected <R> R applyMetadata(final java.util.function.Function<? super Metadata, ? extends R> function)
            throws SQLException {
        requireNonNull(function, "function is null");
        return applyContext(c -> {
            try {
                final Metadata metadata = metadata(c);
                return function.apply(metadata);
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        });
    }

    @Test
    void writeMetadata() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final var metadata = Metadata.newInstance(context);
            final var name = TestUtils.getFilenamePrefix(context) + " - metadata";
            JaxbTests.writeToFile(Metadata.class, metadata, name);
            JsonbTests.writeToFile(metadata, name);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void getCatalogs__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final var catalogs = context.getCatalogs(new ArrayList<>());
            if (catalogs.isEmpty()) {
                catalogs.add(Catalog.newVirtualInstance());
            }
            assertThat(catalogs)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var catalog : catalogs) {
                catalog.retrieveChildren(context);
            }
            final var name = TestUtils.getFilenamePrefix(context) + " - catalogs";
            final var pathname = name + ".xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Catalog.class, catalogs, target);
            JsonbTests.writeToFile(catalogs, name);
        }
    }

    @Test
    void getFunctions__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var functions = context.getFunctions(null, null, "%", new ArrayList<>());
            assertThat(functions)
                    .doesNotContainNull()
                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var function : functions) {
                function.retrieveChildren(context);
            }
            final var name = TestUtils.getFilenamePrefix(context) + " - functions";
            final var pathname = name + ".xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Function.class, functions, target);
            JsonbTests.writeToFile(functions, name);
        }
    }

    @Test
    void deletesAreDetected() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = DeletesAreDetected.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - deletesAreDetected.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(DeletesAreDetected.class, allInstance, target);
        }
    }

    @Test
    void insertsAreDetected() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = InsertsAreDetected.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - insertsAreDetected.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(InsertsAreDetected.class, allInstance, target);
        }
    }

    @Test
    void updatesAreDetected() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = UpdatesAreDetected.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - updatesAreDetected.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(UpdatesAreDetected.class, allInstance, target);
        }
    }

    @Test
    void othersDeletesAreVisible__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = OthersDeletesAreVisible.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - othersDeletesAreVisible.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OthersDeletesAreVisible.class, allInstance, target);
        }
    }

    @Test
    void othersInsertsAreVisible__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = OthersInsertsAreVisible.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - othersInsertsAreVisible.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OthersInsertsAreVisible.class, allInstance, target);
        }
    }

    @Test
    void othersUpdatesAreVisible__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = OthersUpdatesAreVisible.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - othersUpdatesAreVisible.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OthersUpdatesAreVisible.class, allInstance, target);
        }
    }

    @Test
    void ownDeletesAreVisible__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = OwnDeletesAreVisible.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - ownDeletesAreVisible.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OwnDeletesAreVisible.class, allInstance, target);
        }
    }

    @Test
    void ownInsertsAreVisible__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = OwnInsertsAreVisible.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - ownInsertsAreVisible.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OwnInsertsAreVisible.class, allInstance, target);
        }
    }

    @Test
    void ownUpdatesAreVisible__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstance = OwnUpdatesAreVisible.getAllInstances(context, new ArrayList<>());
            assertThat(allInstance)
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var each : allInstance) {
                each.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - ownUpdatesAreVisible.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(OwnUpdatesAreVisible.class, allInstance, target);
        }
    }

    @Test
    void getProcedures__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var procedures = context.getProcedures(null, null, "%", new ArrayList<>());
            for (final var procedure : procedures) {
                final var string = assertDoesNotThrow(procedure::toString);
                final var hashCode = assertDoesNotThrow(procedure::hashCode);
                procedure.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - procedures.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Procedure.class, procedures, target);
        }
    }

    @Test
    void getPseudoColumns__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var pseudoColumns = context.getPseudoColumns(null, null, "%", "%", new ArrayList<>());
            for (final var pseudoColumn : pseudoColumns) {
                final var string = assertDoesNotThrow(pseudoColumn::toString);
                final var hashCode = assertDoesNotThrow(pseudoColumn::hashCode);
                pseudoColumn.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - pseudoColumns.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(PseudoColumn.class, pseudoColumns, target);
        }
    }

    @Test
    void getSchemas__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var schemas = context.getSchemas(null, null, new ArrayList<>());
            if (schemas.isEmpty()) {
                schemas.add(Schema.newVirtualInstance());
            }
            TestUtils.testEquals(schemas);
            for (final var schema : schemas) {
                final var string = assertDoesNotThrow(schema::toString);
                final var hashCode = assertDoesNotThrow(schema::hashCode);
                schema.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - schemas.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Schema.class, schemas, target);
        }
    }

    @Test
    void getSuperTables__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var superTables = context.getSuperTables(null, "%", "%", new ArrayList<>());
            for (final var superTable : superTables) {
                log.debug("super table: {}", superTable);
                superTable.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - superTables.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SuperTable.class, superTables, target);
        }
    }

    @Test
    void getSuperTypes__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var superTypes = context.getSuperTypes(null, null, "%", new ArrayList<>());
            for (final var superType : superTypes) {
                assertThat(superType.extractType())
                        .isNotNull();
                assertThat(superType.extractSuperType())
                        .isNotNull();
                superType.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - superTypes.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SuperType.class, superTypes, target);
        }
    }

    @Test
    void supportsConvert__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstances = SupportsConvert.getAllInstances(context, new ArrayList<>());
            assertThat(allInstances)
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            for (final var supportConvert : allInstances) {
                supportConvert.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - supportsConvert.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SupportsConvert.class, allInstances, target);
        }
    }

    @Test
    void supportsResultSetConcurrency__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstances = SupportsResultSetConcurrency.getAllInstances(context, new ArrayList<>());
            assertThat(allInstances)
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            final var pathname = TestUtils.getFilenamePrefix(context) + " - supportsResultSetConcurrency.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SupportsResultSetConcurrency.class, allInstances, target);
        }
    }

    @Test
    void supportsResultSetHoldability__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstances = SupportsResultSetHoldability.getAllInstances(context, new ArrayList<>());
            assertThat(allInstances)
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            final var pathname = TestUtils.getFilenamePrefix(context) + " - supportsResultSetHoldability.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SupportsResultSetHoldability.class, allInstances, target);
        }
    }

    @Test
    void supportsResultSetType__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstances = SupportsResultSetType.getAllInstances(context, new ArrayList<>());
            assertThat(allInstances)
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            final var pathname = TestUtils.getFilenamePrefix(context) + " - supportsResultSetType.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SupportsResultSetType.class, allInstances, target);
        }
    }

    @Test
    void supportsTransactionIsolationLevel__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var allInstances = SupportsTransactionIsolationLevel.getAllInstances(context, new ArrayList<>());
            assertThat(allInstances)
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
            final var pathname = TestUtils.getFilenamePrefix(context) + " - supportsTransactionIsolationLevel.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SupportsTransactionIsolationLevel.class, allInstances, target);
        }
    }

    @Test
    void getTables__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var tables = context.getTables(null, null, "%", null, new ArrayList<>());
            TestUtils.testEquals(tables);
            for (final var table : tables) {
                final var string = assertDoesNotThrow(table::toString);
                final var hashCode = assertDoesNotThrow(table::hashCode);
                table.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - tables.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Table.class, tables, target);
        }
    }

    @Test
    void getTablePrivileges__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var tablePrivileges = context.getTablePrivileges(null, null, "%", new ArrayList<>());
            for (final var tablePrivilege : tablePrivileges) {
                log.debug("tablePrivilege: {}", tablePrivilege);
                final var string = assertDoesNotThrow(tablePrivilege::toString);
                final var hashCode = assertDoesNotThrow(tablePrivilege::hashCode);
                tablePrivilege.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - tablePrivileges.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(TablePrivilege.class, tablePrivileges, target);
        }
    }

    @Test
    void getTypeInfo__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var typeInfo = context.getTypeInfo(new ArrayList<>());
            for (final var each : typeInfo) {
                log.debug("typeInfo: {}", each);
                final var string = assertDoesNotThrow(each::toString);
                final var hashCode = assertDoesNotThrow(each::hashCode);
                each.retrieveChildren(context);
            }
            final String pathname = TestUtils.getFilenamePrefix(context) + " - typeInfo.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(TypeInfo.class, typeInfo, target);
        }
    }

    @Test
    void getUDTs__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var UDTs = context.getUDTs(null, null, "%", null, new ArrayList<>());
            for (final var udt : UDTs) {
                log.debug("UDT: {}", udt);
                final var string = assertDoesNotThrow(udt::toString);
                final var hashCode = assertDoesNotThrow(udt::hashCode);
                udt.retrieveChildren(context);
            }
            final String pathname = TestUtils.getFilenamePrefix(context) + " - UDTs.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(UDT.class, UDTs, target);
        }
    }

    @Disabled
    @Test
    void writeContextToFiles() throws SQLException {
        applyContext(c -> {
            try {
                ContextTestUtils.writeFiles(c);
                return null;
            } catch (SQLException | JAXBException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
