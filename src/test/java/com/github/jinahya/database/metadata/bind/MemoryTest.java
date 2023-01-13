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
import org.junit.jupiter.api.Test;

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
            } catch (final Exception e) {
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void catalogs__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final var catalogs = context.getCatalogs();
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
        }
    }

    @Test
    void getSuperTypes__() throws SQLException {
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
        }
    }

    @Test
    void supportsConvert__() throws SQLException {
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
        }
    }

    @Test
    void getTablePrivileges__() throws SQLException {
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
        }
    }

    @Test
    void getTypeInfo__() throws SQLException {
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
        }
    }
}
