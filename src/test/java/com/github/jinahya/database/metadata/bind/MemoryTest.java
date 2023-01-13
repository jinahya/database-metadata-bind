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

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    void catalogs__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final var catalogs = context.getCatalogs();
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
    void getAttributes__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final List<Attribute> list = context.getAttributes(null, null, "%", "%");
            list.forEach(e -> {
                log.debug("attribute: {}", e);
                final var string = e.toString();
                final var hashCode = e.hashCode();
            });
        }
    }

    @Disabled
    @Test
    void getBestRowIdentifier__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final List<BestRowIdentifier> list = context.getBestRowIdentifier(null, null, "%", 0, true);
            list.forEach(e -> {
                log.debug("bestRowIdentifier: {}", e);
                final var string = e.toString();
                final var hashCode = e.hashCode();
            });
        }
    }

    @Test
    void getClientInfoProperties__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final var clientInfoProperties = context.getClientInfoProperties();
            clientInfoProperties.forEach(cip -> {
                log.debug("client info property: {}", cip);
                final var string = cip.toString();
                final var hashCode = cip.hashCode();
            });
        }
    }

    @Test
    void getColumns__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
            final var columns = context.getColumns(null, null, "%", "%");
            for (final var column : columns) {
                log.debug("column: {}", column);
                {
                    final var string = column.toString();
                    final var hashCode = column.hashCode();
                }
                final var columnPrivileges = context.getColumnPrivileges(
                        column.getScopeCatalog(),
                        column.getScopeSchema(),
                        column.getTableName(),
                        column.getColumnName()
                );
                for (final var columnPrivilege : columnPrivileges) {
                    log.debug("\tcolumn privilege: {}", columnPrivilege);
                    {
                        final var string = columnPrivilege.toString();
                        final var hashCode = columnPrivilege.hashCode();
                    }
                }
            }
        }
    }

    @Disabled
    @Test
    void getCrossReferences__() throws Exception {
        try (var connection = connect()) {
            final var context = context(connection);
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
            TestUtils.testEquals(schemas);
            for (final var schema : schemas) {
                final var string = assertDoesNotThrow(schema::toString);
                final var hashCode = assertDoesNotThrow(schema::hashCode);
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
            final var pathname = TestUtils.getFilenamePrefix(context) + " - superTypes.xml";
            final var target = Paths.get("target", pathname).toFile();
        }
    }

    @Test
    void getTables__() throws Exception {
        try (var connection = connect()) {
            final var context = Context.newInstance(connection);
            final var tables = context.getTables(null, null, "%", null);
            for (final var table : tables) {
                log.debug("table: {}", table);
                {
                    final var string = assertDoesNotThrow(table::toString);
                    final var hashCode = assertDoesNotThrow(table::hashCode);
                }
                for (final int scope : BestRowIdentifier.scopes()) {
                    {
                        final List<BestRowIdentifier> bestRowIdentifiers = context.getBestRowIdentifier(
                                table.getTableCat(),
                                table.getTableSchem(),
                                table.getTableName(),
                                scope,
                                true
                        );
                        bestRowIdentifiers.forEach(bri -> {
                            log.debug("bestRowIdentifier: {}", bri);
                            final var string = bri.toString();
                            final int hashCode = bri.hashCode();
                        });
                    }
                    {
                        final List<BestRowIdentifier> bestRowIdentifiers = context.getBestRowIdentifier(
                                table.getTableCat(),
                                table.getTableSchem(),
                                table.getTableName(),
                                scope,
                                false
                        );
                        bestRowIdentifiers.forEach(bri -> {
                            log.debug("bestRowIdentifier: {}", bri);
                            final var string = bri.toString();
                            final int hashCode = bri.hashCode();
                        });
                    }
                }
            }
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
            }
            final String pathname = TestUtils.getFilenamePrefix(context) + " - UDTs.xml";
            final File target = Paths.get("target", pathname).toFile();
        }
    }
}
