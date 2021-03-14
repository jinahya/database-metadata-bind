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

import javax.xml.bind.JAXBException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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

    @Test
    void writeFiles() throws SQLException, JAXBException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class)
                    .levelForSqlException(Level.FINE);
            ContextTests.writeFiles(context);
        }
    }

    @Test
    void catalogs() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class)
                    .levelForSqlException(Level.FINE);
            context.getCatalogs(new ArrayList<>()).forEach(catalog -> {
                final String string = assertDoesNotThrow(catalog::toString);
                final int hashCode = assertDoesNotThrow(catalog::hashCode);
                final String tableCat = (String) catalog.getLabeledValue(Catalog.COLUMN_LABEL_TABLE_CAT);
                assertThat(tableCat).isNotNull();
            });
        }
    }

    @Test
    void schemas() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class)
                    .levelForSqlException(Level.FINE);
            final List<Schema> schemas = context.getSchemas(null, null, new ArrayList<>());
            TestUtils.testEquals(schemas);
            for (final Schema schema : schemas) {
                final String string = assertDoesNotThrow(schema::toString);
                final int hashCode = assertDoesNotThrow(schema::hashCode);
                final String tableCatalog = (String) schema.getLabeledValue(Schema.COLUMN_LABEL_TABLE_CATALOG);
                final String tableSchem = (String) schema.getLabeledValue(Schema.COLUMN_LABEL_TABLE_SCHEM);
            }
        }
    }

    @Test
    void tables() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class)
                    .levelForSqlException(Level.FINE);
            final List<Table> tables = context.getTables(null, null, null, null, new ArrayList<>());
            TestUtils.testEquals(tables);
            for (final Table table : tables) {
                final String string = assertDoesNotThrow(table::toString);
                final int hashCode = assertDoesNotThrow(table::hashCode);
                final String tableCat = (String) table.getLabeledValue(Table.COLUMN_LABEL_TABLE_CAT);
                final String tableSchem = (String) table.getLabeledValue(Table.COLUMN_LABEL_TABLE_SCHEM);
                final String tableName = (String) table.getLabeledValue(Table.COLUMN_LABEL_TABLE_NAME);
            }
        }
    }

    @Test
    void deletesAreDetected() throws SQLException {
        try (Connection connection = connect()) {
            final Context context = Context.newInstance(connection)
                    .suppress(SQLFeatureNotSupportedException.class)
                    .levelForSqlException(Level.FINE);
            assertThat(DeletesAreDetected.getAllInstances(context))
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
                    .suppress(SQLFeatureNotSupportedException.class)
                    .levelForSqlException(Level.FINE);
            assertThat(InsertsAreDetected.getAllInstances(context))
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
                    .suppress(SQLFeatureNotSupportedException.class)
                    .levelForSqlException(Level.FINE);
            assertThat(UpdatesAreDetected.getAllInstances(context))
                    .doesNotContainNull()
                    .hasSize(ResultSetType.values().length)
                    .satisfies(TestUtils::testEquals)
                    .allSatisfy(v -> {
                        final String string = assertDoesNotThrow(v::toString);
                        final int hashCode = assertDoesNotThrow(v::hashCode);
                    });
        }
    }
}
