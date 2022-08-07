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
        return Context.newInstance(connection)
                .suppress(SQLFeatureNotSupportedException.class);
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

    @Test
    void catalogs() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = context(connection);
            context.getCatalogs(new ArrayList<>()).forEach(catalog -> {
                final String string = assertDoesNotThrow(catalog::toString);
                final int hashCode = assertDoesNotThrow(catalog::hashCode);
                final String tableCat = (String) MetadataTypeUtils.getLabeledValue(
                        Catalog.class, catalog, Catalog.COLUMN_LABEL_TABLE_CAT);
                assertThat(tableCat).isNotNull();
            });
        }
    }

    @Test
    void functions() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
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
    void schemas() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            final List<Schema> schemas = context.getSchemas((String) null, null, new ArrayList<>());
            TestUtils.testEquals(schemas);
            for (final Schema schema : schemas) {
                final String string = assertDoesNotThrow(schema::toString);
                final int hashCode = assertDoesNotThrow(schema::hashCode);
                final String tableCatalog = (String) MetadataTypeUtils.getLabeledValue(
                        Schema.class, schema, Schema.COLUMN_LABEL_TABLE_CATALOG);
                final String tableSchem = (String) MetadataTypeUtils.getLabeledValue(
                        Schema.class, schema, Schema.COLUMN_LABEL_TABLE_SCHEM);
            }
        }
    }

    @Test
    void deletesAreDetected() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            assertThat(DeletesAreDetected.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final String string = assertDoesNotThrow(v::toString);
                        final int hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void insertsAreDetected() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            assertThat(InsertsAreDetected.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final String string = assertDoesNotThrow(v::toString);
                        final int hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void updatesAreDetected() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            assertThat(UpdatesAreDetected.getAllInstances(context, new ArrayList<>()))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final String string = assertDoesNotThrow(v::toString);
                        final int hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }

    @Test
    void getProcedures__() throws SQLException {
        try (var connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            final List<Procedure> procedures;
            try {
                procedures = context.getProcedures(null, null, "%", new ArrayList<>());
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
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            final List<PseudoColumn> pseudoColumns;
            try {
                pseudoColumns = context.getPseudoColumns(null, null, "%", "%", new ArrayList<>());
            } catch (final SQLFeatureNotSupportedException slqfnse) {
                log.error("not supported; getPseudoColumns", slqfnse);
                return;
            }
        }
    }

    @Test
    void getTables__() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            final List<Table> tables = context.getTables(null, null, null, null, new ArrayList<>());
            TestUtils.testEquals(tables);
            for (final var table : tables) {
                final String string = assertDoesNotThrow(table::toString);
                final int hashCode = assertDoesNotThrow(table::hashCode);
                final String tableCat = (String) MetadataTypeUtils.getLabeledValue(
                        Table.class, table, Table.COLUMN_LABEL_TABLE_CAT);
                final String tableSchem = (String) MetadataTypeUtils.getLabeledValue(
                        Table.class, table, Table.COLUMN_LABEL_TABLE_SCHEM);
                final String tableName = (String) MetadataTypeUtils.getLabeledValue(
                        Table.class, table, Table.COLUMN_LABEL_TABLE_NAME);
            }
            for (final var table : tables) {
                for (final var scope : BestRowIdentifier.Scope.values()) {
                    for (final var nullable : new boolean[] {true, false}) {
                        final var bestRowIdentifiers = context.getBestRowIdentifier(
                                table.getTableCat(),
                                table.getTableSchem(),
                                table.getTableName(),
                                scope.rawValue(),
                                nullable,
                                new ArrayList<>()
                        );
                        bestRowIdentifiers.forEach(bri -> {
                            log.debug("bestRowIdentifier(scope: {}, nullable: {}): {}", scope, nullable, bri);
                        });
                    }
                }
            }
        }
    }

    @Test
    void getTablePrivileges__() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            final List<TablePrivilege> tablePrivileges = context.getTablePrivileges(null, null, "%", new ArrayList<>());
            for (final var tablePrivilege : tablePrivileges) {
                log.debug("tablePrivilege: {}", tablePrivilege);
                final String string = assertDoesNotThrow(tablePrivilege::toString);
                final int hashCode = assertDoesNotThrow(tablePrivilege::hashCode);
            }
        }
    }

    @Test
    void getTypeInfo__() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            final List<TypeInfo> typeInfo = context.getTypeInfo(new ArrayList<>());
            for (final var each : typeInfo) {
                log.debug("typeInfo: {}", each);
                final String string = assertDoesNotThrow(each::toString);
                final int hashCode = assertDoesNotThrow(each::hashCode);
            }
        }
    }

    @Test
    void getUDTs__() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class);
            final List<UDT> UDTs = context.getUDTs(null, null, "%", null, new ArrayList<>());
            for (final var udt : UDTs) {
                log.debug("UDT: {}", udt);
                final String string = assertDoesNotThrow(udt::toString);
                final int hashCode = assertDoesNotThrow(udt::hashCode);
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
