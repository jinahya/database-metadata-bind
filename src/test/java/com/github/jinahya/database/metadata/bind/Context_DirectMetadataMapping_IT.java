package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class Context_DirectMetadataMapping_IT {

    @Test
    void directMappings_GetAndForEachReturnSameCounts() throws SQLException {
        try (var connection = DriverManager.getConnection("jdbc:h2:mem:context_direct_metadata_mapping")) {
            try (var statement = connection.createStatement()) {
                statement.execute("""
                                          CREATE TABLE DIRECT_PARENT (
                                              ID INTEGER NOT NULL,
                                              NAME VARCHAR(32),
                                              CONSTRAINT DIRECT_PARENT_PK PRIMARY KEY (ID)
                                          )""");
                statement.execute("CREATE INDEX DIRECT_PARENT_NAME_IDX ON DIRECT_PARENT (NAME)");
                statement.execute("""
                                          CREATE TABLE DIRECT_CHILD (
                                              ID INTEGER NOT NULL PRIMARY KEY,
                                              PARENT_ID INTEGER,
                                              CONSTRAINT DIRECT_CHILD_PARENT_FK
                                                  FOREIGN KEY (PARENT_ID) REFERENCES DIRECT_PARENT (ID)
                                          )""");
            }
            final var context = Context.newInstance(connection);
            assertDirect("attributes", () -> context.getAttributes(null, null, "%", "%"),
                         c -> context.forEachAttribute(null, null, "%", "%", c));
            assertDirect("bestRowIdentifier", () -> context.getBestRowIdentifier(
                                 null, null, "DIRECT_PARENT",
                                 BestRowIdentifier.COLUMN_VALUE_SCOPE_BEST_ROW_SESSION, true),
                         c -> context.forEachBestRowIdentifier(
                                 null, null, "DIRECT_PARENT",
                                 BestRowIdentifier.COLUMN_VALUE_SCOPE_BEST_ROW_SESSION, true, c));
            assertDirect("catalogs", context::getCatalogs, c -> context.forEachCatalog(c));
            assertDirect("clientInfoProperties", context::getClientInfoProperties,
                         c -> context.forEachClientInfoProperty(c));
            assertDirect("columnPrivileges", () -> context.getColumnPrivileges(null, null, "DIRECT_PARENT", "%"),
                         c -> context.forEachColumnPrivilege(null, null, "DIRECT_PARENT", "%", c));
            assertDirect("columns", () -> context.getColumns(null, null, "%", "%"),
                         c -> context.forEachColumn(null, null, "%", "%", c));
            assertDirect("crossReference", () -> context.getCrossReference(
                                 null, null, "DIRECT_PARENT", null, null, "DIRECT_CHILD"),
                         c -> context.forEachCrossReference(
                                 null, null, "DIRECT_PARENT", null, null, "DIRECT_CHILD", c));
            assertDirect("exportedKeys", () -> context.getExportedKeys(null, null, "DIRECT_PARENT"),
                         c -> context.forEachExportedKey(null, null, "DIRECT_PARENT", c));
            assertDirect("functions", () -> context.getFunctions(null, null, "%"),
                         c -> context.forEachFunction(null, null, "%", c));
            assertDirect("functionColumns", () -> context.getFunctionColumns(null, null, "%", "%"),
                         c -> context.forEachFunctionColumn(null, null, "%", "%", c));
            assertDirect("importedKeys", () -> context.getImportedKeys(null, null, "DIRECT_CHILD"),
                         c -> context.forEachImportedKey(null, null, "DIRECT_CHILD", c));
            assertDirect("indexInfo", () -> context.getIndexInfo(null, null, "DIRECT_PARENT", false, false),
                         c -> context.forEachIndexInfo(null, null, "DIRECT_PARENT", false, false, c));
            assertDirect("primaryKeys", () -> context.getPrimaryKeys(null, null, "DIRECT_PARENT"),
                         c -> context.forEachPrimaryKey(null, null, "DIRECT_PARENT", c));
            assertDirect("procedureColumns", () -> context.getProcedureColumns(null, null, "%", "%"),
                         c -> context.forEachProcedureColumn(null, null, "%", "%", c));
            assertDirect("procedures", () -> context.getProcedures(null, null, "%"),
                         c -> context.forEachProcedure(null, null, "%", c));
            assertDirect("pseudoColumns", () -> context.getPseudoColumns(null, null, "%", "%"),
                         c -> context.forEachPseudoColumn(null, null, "%", "%", c));
            assertDirect("schemas", context::getSchemas, context::forEachSchema);
            assertDirect("schemasPattern", () -> context.getSchemas(null, "%"),
                         c -> context.forEachSchema(null, "%", c));
            assertDirect("superTables", () -> context.getSuperTables(null, "%", "%"),
                         c -> context.forEachSuperTable(null, "%", "%", c));
            assertDirect("superTypes", () -> context.getSuperTypes(null, "%", "%"),
                         c -> context.forEachSuperType(null, "%", "%", c));
            assertDirect("tablePrivileges", () -> context.getTablePrivileges(null, null, "%"),
                         c -> context.forEachTablePrivilege(null, null, "%", c));
            assertDirect("tableTypes", context::getTableTypes, c -> context.forEachTableType(c));
            assertDirect("tables", () -> context.getTables(null, null, "%", null),
                         c -> context.forEachTable(null, null, "%", null, c));
            assertDirect("typeInfo", context::getTypeInfo, c -> context.forEachTypeInfo(c));
            assertDirect("udts", () -> context.getUDTs(null, null, "%", null),
                         c -> context.forEachUDT(null, null, "%", null, c));
            assertDirect("versionColumns", () -> context.getVersionColumns(null, null, "DIRECT_PARENT"),
                         c -> context.forEachVersionColumn(null, null, "DIRECT_PARENT", c));
        }
    }

    private static <T> void assertDirect(final String name, final Query<T> query, final Iteration<T> iteration)
            throws SQLException {
        final List<T> values;
        try {
            values = query.get();
        } catch (final SQLFeatureNotSupportedException ignored) {
            return;
        }
        final var accepted = new ArrayList<T>();
        try {
            iteration.accept(accepted::add);
        } catch (final SQLFeatureNotSupportedException ignored) {
            return;
        }
        assertThat(accepted).as(name).hasSameSizeAs(values);
    }

    @FunctionalInterface
    private interface Query<T> {

        List<T> get() throws SQLException;
    }

    @FunctionalInterface
    private interface Iteration<T> {

        void accept(Consumer<? super T> consumer) throws SQLException;
    }
}
