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
import java.util.List;

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
            try {
                final var metadata = Metadata.newInstance(context);
                final var name = TestUtils.getFilenamePrefix(context) + " - metadata";
                _XmlBindingTestUtils.writeToFile(Metadata.class, metadata, name);
                _JsonBindingTestUtils.writeToFile(metadata, name);
            } catch (final Exception e) {
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void catalogs__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final var catalogs = new ArrayList<>();
            context.getCatalogs(catalogs::add);
            if (catalogs.isEmpty()) {
                catalogs.add(Catalog.newVirtualInstance());
            }
            assertThat(catalogs)
                    .allSatisfy(c -> {
                        final var string = assertDoesNotThrow(c::toString);
                        final var hashCode = assertDoesNotThrow(c::hashCode);
                    });
            for (final var catalog : catalogs) {
//                catalog.retrieveChildren(context);
            }
            final var name = "catalogs";
//            _XmlBindingTestUtils.test(context, name, Catalog.class, catalogs);
//            _JsonBindingTestUtils.test(context, name, Catalog.class, catalogs);
        }
    }

    @Test
    void getClientInfoProperties__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final List<ClientInfoProperty> clientInfoProperties = new ArrayList<>();
            context.getClientInfoProperties(clientInfoProperties::add);
            assertThat(clientInfoProperties)
                    .allSatisfy(c -> {
                        final var string = assertDoesNotThrow(c::toString);
                        final var hashCode = assertDoesNotThrow(c::hashCode);
                    });
            for (final var clientInfoProperty : clientInfoProperties) {
                clientInfoProperty.retrieveChildren(context);
            }
            final var name = TestUtils.getFilenamePrefix(context) + " - clientInfoProperties";
            final var pathname = name + ".xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(ClientInfoProperty.class, clientInfoProperties, target);
            _JsonBindingTestUtils.writeToFile(context, "clientInfoProperties", clientInfoProperties);
            _JsonBindingTestUtils.serializeAndDeserialize(
                    ClientInfoProperty.class,
                    clientInfoProperties,
                    (e, a) -> {
                        assertThat(a)
                                .usingRecursiveComparison()
                                .isEqualTo(e);
                    });
        }
    }

    @Test
    void getColumns__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final List<Column> columns = new ArrayList<>();
            context.getColumns(null, null, "%", "%", columns::add);
            assertThat(columns)
                    .allSatisfy(c -> {
                        final var string = assertDoesNotThrow(c::toString);
                        final var hashCode = assertDoesNotThrow(c::hashCode);
                    });
            for (final var column : columns) {
                column.retrieveChildren(context);
            }
            final var name = TestUtils.getFilenamePrefix(context) + " - columns";
            final var pathname = name + ".xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Column.class, columns, target);
            _JsonBindingTestUtils.writeToFile(context, "columns", columns);
            _JsonBindingTestUtils.serializeAndDeserialize(
                    Column.class,
                    columns,
                    (e, a) -> {
                        assertThat(a)
                                .usingRecursiveComparison()
                                .isEqualTo(e);
                    });
        }
    }

    @Test
    void getCrossReferences__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final var crossReferences = CrossReference.getAllInstances(context, new ArrayList<>());
            assertThat(crossReferences)
                    .allSatisfy(c -> {
                        final var string = assertDoesNotThrow(c::toString);
                        final var hashCode = assertDoesNotThrow(c::hashCode);
                    });
            for (final var crossReference : crossReferences) {
                crossReference.retrieveChildren(context);
            }
            final var name = TestUtils.getFilenamePrefix(context) + " - crossReferences";
            final var pathname = name + ".xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(CrossReference.class, crossReferences, target);
            _JsonBindingTestUtils.writeToFile(context, "crossReferences", crossReferences);
            _JsonBindingTestUtils.serializeAndDeserialize(
                    CrossReference.class,
                    crossReferences,
                    (e, a) -> {
                        assertThat(a)
                                .usingRecursiveComparison()
                                .isEqualTo(e);
                    });
        }
    }

    @Test
    void getFunctions__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final List<Function> functions = new ArrayList<>();
            context.getFunctions(null, null, "%", functions::add);
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
            _JsonBindingTestUtils.writeToFile(context, "functions", functions);
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
    void getProcedures__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final List<Procedure> procedures = new ArrayList<>();
            context.getProcedures(null, null, "%", procedures::add);
            for (final var procedure : procedures) {
                final var string = assertDoesNotThrow(procedure::toString);
                final var hashCode = assertDoesNotThrow(procedure::hashCode);
                procedure.retrieveChildren(context);
            }
            final var prefix = TestUtils.getFilenamePrefix(context);
            final var pathname = prefix + " - procedures.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Procedure.class, procedures, target);
            _JsonBindingTestUtils.writeToFile(context, "procedures", procedures);
        }
    }

    @Test
    void getPseudoColumns__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final List<PseudoColumn> pseudoColumns = new ArrayList<>();
            context.getPseudoColumns(null, null, "%", "%", pseudoColumns::add);
            for (final var pseudoColumn : pseudoColumns) {
                final var string = assertDoesNotThrow(pseudoColumn::toString);
                final var hashCode = assertDoesNotThrow(pseudoColumn::hashCode);
                pseudoColumn.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - pseudoColumns.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(PseudoColumn.class, pseudoColumns, target);
            _JsonBindingTestUtils.writeToFile(context, "pseudoColumns", pseudoColumns);
        }
    }

    @Test
    void getSchemas__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var schemas = context.getSchemas(null, null);
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
            _JsonBindingTestUtils.writeToFile(context, "schemas", schemas);
        }
    }

    @Test
    void getSuperTables__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var superTables = context.getSuperTables(null, "%", "%");
            for (final var superTable : superTables) {
                log.debug("super table: {}", superTable);
                superTable.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - superTables.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(SuperTable.class, superTables, target);
            _JsonBindingTestUtils.writeToFile(context, "superTables", superTables);
        }
    }

    @Test
    void getSuperTypes__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var superTypes = context.getSuperTypes(null, null, "%");
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
    void getTables__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var tables = context.getTables(null, null, "%", null);
            TestUtils.testEquals(tables);
            for (final var table : tables) {
                final var string = assertDoesNotThrow(table::toString);
                final var hashCode = assertDoesNotThrow(table::hashCode);
                table.retrieveChildren(context);
            }
            final var pathname = TestUtils.getFilenamePrefix(context) + " - tables.xml";
            final var target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(Table.class, tables, target);
            {
                _JsonBindingTestUtils.writeToFile(context, "tables", tables);
                _JsonBindingTestUtils.serializeAndDeserialize(
                        Table.class,
                        tables,
                        (e, a) -> {
                            assertThat(a)
                                    .usingRecursiveComparison()
                                    .isEqualTo(e);
                        });
            }
        }
    }

    @Test
    void getTablePrivileges__() throws SQLException, JAXBException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var tablePrivileges = context.getTablePrivileges(null, null, "%");
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
            final var typeInfo = context.getTypeInfo();
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
    void getUDTs__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var UDTs = context.getUDTs(null, null, "%", null);
            for (final var udt : UDTs) {
                log.debug("UDT: {}", udt);
                final var string = assertDoesNotThrow(udt::toString);
                final var hashCode = assertDoesNotThrow(udt::hashCode);
                udt.retrieveChildren(context);
            }
            final String pathname = TestUtils.getFilenamePrefix(context) + " - UDTs.xml";
            final File target = Paths.get("target", pathname).toFile();
            Wrapper.marshalFormatted(UDT.class, UDTs, target);
            _JsonBindingTestUtils.serializeAndDeserialize(UDT.class, UDTs, (e, a) -> {
                assertThat(a.getAttributes())
                        .hasSameSizeAs(e.getAttributes())
                        .allSatisfy(attr -> {
                        });
            });
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
