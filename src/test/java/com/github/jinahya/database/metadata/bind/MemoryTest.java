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

import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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
    void writeMetadata() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = context(connection);
            final Metadata metadata = metadata(context);
            final String name = TestUtils.getFilenamePrefix(context) + " - metadata";
            JaxbTests.writeToFile(Metadata.class, metadata, name);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void getCatalogs__() throws SQLException {
        try (var connection = connect()) {
            final var context = context(connection);
            final var catalogs = context.getCatalogs(new ArrayList<>());
            for (final var catalog : catalogs) {
                final var string = assertDoesNotThrow(catalog::toString);
                final var hashCode = assertDoesNotThrow(catalog::hashCode);
                assertThat(catalog.getTableCat())
                        .isNotNull();
                catalog.retrieveChildren(context);
            }
        }
    }

    @Test
    void functions() throws SQLException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final List<Function> functions;
            try {
                functions = context.getFunctions(null, null, "%", new ArrayList<>());
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                log.error("getFunctions not supported", sqlfnse);
                return;
            }
            TestUtils.testEquals(functions);
            for (final Function function : functions) {
                log.debug("function: {}", function);
                context.getFunctionColumns(
                        function.getFunctionCat(),
                        function.getFunctionSchem(),
                        function.getFunctionName(),
                        "%",
                        function.getFunctionColumns()
                );
                function.getFunctionColumns().forEach(fc -> {
                    log.debug("functionColumn: {}", fc);
                });
            }
        }
    }

    @Test
    void deletesAreDetected() throws SQLException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(DeletesAreDetected.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void insertsAreDetected() throws SQLException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(InsertsAreDetected.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void updatesAreDetected() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(UpdatesAreDetected.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void othersDeletesAreVisible__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(OthersDeletesAreVisible.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void othersInsertsAreVisible__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(OthersInsertsAreVisible.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void othersUpdatesAreVisible__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(OthersUpdatesAreVisible.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void ownDeletesAreVisible__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(OwnDeletesAreVisible.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void ownInsertsAreVisible__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(OwnInsertsAreVisible.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void ownUpdatesAreVisible__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(OwnUpdatesAreVisible.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void getProcedures__() throws SQLException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var procedures = context.getProcedures(null, null, "%", new ArrayList<>());
            for (final var procedure : procedures) {
                assertThat(procedure)
                        .usingComparator(Procedure.COMPARATOR)
                        .isEqualTo(procedure);
                context.getProcedureColumns(
                        procedure.getProcedureCat(),
                        procedure.getProcedureSchem(),
                        procedure.getProcedureName(),
                        "%",
                        procedure.getProcedureColumns()
                );
                for (final var procedureColumn : procedure.getProcedureColumns()) {
                    assertThat(procedureColumn)
                            .isNotNull()
                            .usingComparator(ProcedureColumn.COMPARATOR)
                            .isEqualTo(procedureColumn);
                }
            }
        }
    }

    @Test
    void getPseudoColumns__() throws SQLException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var pseudoColumns = context.getPseudoColumns(null, null, "%", "%", new ArrayList<>());
        }
    }

    @Test
    void getSchemas__() throws SQLException {
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
                final var tableCatalog = (String) MetadataTypeUtils.getLabeledValue(
                        Schema.class, schema, Schema.LABEL_TABLE_CATALOG);
                final var tableSchem = (String) MetadataTypeUtils.getLabeledValue(
                        Schema.class, schema, Schema.LABEL_TABLE_SCHEM);
                schema.retrieveChildren(context);
            }
        }
    }

    @Test
    void getSuperTables__() throws SQLException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var superTables = context.getSuperTables(null, "%", "%", new ArrayList<>());
            for (final var superTable : superTables) {
                log.debug("super table: {}", superTable);
            }
        }
    }

    @Test
    void supportsConvert__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(SupportsConvert.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void supportsResultSetConcurrency__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(SupportsResultSetConcurrency.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void supportsResultSetHoldability__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(SupportsResultSetHoldability.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void supportsResultSetType__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(SupportsResultSetType.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void supportsTransactionIsolationLevel__() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            assertThat(SupportsTransactionIsolationLevel.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
//                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final var string = assertDoesNotThrow(v::toString);
                        final var hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void getTables__() throws SQLException {
        try (Connection connection = connect()) {
            final var context = Context.newInstance(connection);
            final var tables = context.getTables(null, null, "%", null, new ArrayList<>());
            TestUtils.testEquals(tables);
            for (final var table : tables) {
                final var string = assertDoesNotThrow(table::toString);
                final var hashCode = assertDoesNotThrow(table::hashCode);
                final var tableCat = (String) MetadataTypeUtils.getLabeledValue(
                        Table.class, table, Table.COLUMN_LABEL_TABLE_CAT);
                final var tableSchem = (String) MetadataTypeUtils.getLabeledValue(
                        Table.class, table, Table.COLUMN_LABEL_TABLE_SCHEM);
                final var tableName = (String) MetadataTypeUtils.getLabeledValue(
                        Table.class, table, Table.COLUMN_LABEL_TABLE_NAME);
            }
        }
    }

    @Test
    void getTablePrivileges__() throws SQLException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var tablePrivileges = context.getTablePrivileges(null, null, "%", new ArrayList<>());
            for (final var tablePrivilege : tablePrivileges) {
                log.debug("tablePrivilege: {}", tablePrivilege);
                final var string = assertDoesNotThrow(tablePrivilege::toString);
                final var hashCode = assertDoesNotThrow(tablePrivilege::hashCode);
            }
        }
    }

    @Test
    void getTypeInfo__() throws SQLException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var typeInfo = context.getTypeInfo(new ArrayList<>());
            for (final var each : typeInfo) {
                log.debug("typeInfo: {}", each);
                final var string = assertDoesNotThrow(each::toString);
                final var hashCode = assertDoesNotThrow(each::hashCode);
            }
        }
    }

    @Test
    void getUDTs__() throws SQLException {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var UDTs = context.getUDTs(null, null, "%", null, new ArrayList<>());
            for (final var udt : UDTs) {
                log.debug("UDT: {}", udt);
                final var string = assertDoesNotThrow(udt::toString);
                final var hashCode = assertDoesNotThrow(udt::hashCode);
            }
        }
    }

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
