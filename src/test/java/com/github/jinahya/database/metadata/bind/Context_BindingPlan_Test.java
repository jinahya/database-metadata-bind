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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that the per-result-set part of binding is resolved once rather than once per row, as decided in <a
 * href="https://github.com/jinahya/database-metadata-bind/issues/36">#36</a> (P-029).
 * <p>
 * The defect this guards was invisible in results and visible only in JDBC call counts: {@code bind(...)} re-read the
 * result-set metadata and re-matched labels to fields for every row, so the cost grew with row count while the answer
 * never changed. These tests assert the counts directly, because a correctness assertion cannot see the difference.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class Context_BindingPlan_Test {

    /**
     * Counts of the {@link ResultSetMetaData} reads made while binding one result set.
     *
     * @param getMetaData    how many times {@link ResultSet#getMetaData()} was called.
     * @param getColumnCount how many times {@link ResultSetMetaData#getColumnCount()} was called.
     * @param getColumnLabel how many times {@link ResultSetMetaData#getColumnLabel(int)} was called.
     */
    private record Counts(int getMetaData, int getColumnCount, int getColumnLabel) {

    }

    @DisplayName("result-set metadata is read once per result set, not once per row")
    @Test
    void metadataReads_DoNotScaleWithRowCount() throws SQLException {
        final var oneRow = bindCatalogs(1);
        final var manyRows = bindCatalogs(500);
        assertThat(manyRows)
                .as("metadata reads for 500 rows must equal those for 1 row")
                .isEqualTo(oneRow);
        // two labels are declared below, so a single pass over the metadata is 1 + 1 + 2
        assertThat(manyRows).isEqualTo(new Counts(1, 1, 2));
    }

    @DisplayName("an empty result set reads no metadata at all")
    @Test
    void metadataReads_AreSkippedForAnEmptyResultSet() throws SQLException {
        assertThat(bindCatalogs(0))
                .as("an empty result set must not be probed for metadata")
                .isEqualTo(new Counts(0, 0, 0));
    }

    @DisplayName("every row is still bound, with its unknown columns")
    @Test
    void binding_IsUnchanged() throws SQLException {
        final var rows = 3;
        final var results = countingResultSet(
                List.of(Catalog.COLUMN_LABEL_TABLE_CAT, "DRIVER_EXTRA"),
                rowsOf(rows),
                new AtomicInteger(),
                new AtomicInteger(),
                new AtomicInteger()
        );
        final var catalogs = new Context(metadata("getCatalogs", results)).getCatalogs();
        assertThat(catalogs).hasSize(rows);
        for (int i = 0; i < rows; i++) {
            assertThat(catalogs.get(i).getTableCat()).isEqualTo("catalog" + i);
            assertThat(catalogs.get(i).getUnknownColumns()).containsEntry("DRIVER_EXTRA", "extra" + i);
        }
    }

    @DisplayName("a column needing coercion is still coerced, on every row")
    @Test
    void coercion_StillRunsPerRow() throws SQLException {
        final List<Map<String, Object>> rows = new ArrayList<>();
        for (short i = 0; i < 3; i++) {
            final Map<String, Object> row = new LinkedHashMap<>();
            row.put(ClientInfoProperty.COLUMN_LABEL_NAME, "property" + i);
            row.put(ClientInfoProperty.COLUMN_LABEL_MAX_LEN, i); // Short, for an Integer field
            rows.add(row);
        }
        final var results = countingResultSet(
                List.of(ClientInfoProperty.COLUMN_LABEL_NAME, ClientInfoProperty.COLUMN_LABEL_MAX_LEN),
                rows,
                new AtomicInteger(),
                new AtomicInteger(),
                new AtomicInteger()
        );
        final var properties = new Context(metadata("getClientInfoProperties", results)).getClientInfoProperties();
        assertThat(properties).hasSize(3);
        for (int i = 0; i < 3; i++) {
            assertThat(properties.get(i).getMaxLen()).isEqualTo(i);
        }
    }

    // ------------------------------------------------------------------------------------------------------- helpers

    private static List<Map<String, Object>> rowsOf(final int count) {
        final List<Map<String, Object>> rows = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final Map<String, Object> row = new HashMap<>();
            row.put(Catalog.COLUMN_LABEL_TABLE_CAT, "catalog" + i);
            row.put("DRIVER_EXTRA", "extra" + i);
            rows.add(row);
        }
        return rows;
    }

    private static Counts bindCatalogs(final int rowCount) throws SQLException {
        final var getMetaData = new AtomicInteger();
        final var getColumnCount = new AtomicInteger();
        final var getColumnLabel = new AtomicInteger();
        final var results = countingResultSet(
                List.of(Catalog.COLUMN_LABEL_TABLE_CAT, "DRIVER_EXTRA"),
                rowsOf(rowCount),
                getMetaData,
                getColumnCount,
                getColumnLabel
        );
        final var catalogs = new Context(metadata("getCatalogs", results)).getCatalogs();
        assertThat(catalogs).hasSize(rowCount);
        return new Counts(getMetaData.get(), getColumnCount.get(), getColumnLabel.get());
    }

    private static ResultSet countingResultSet(final List<String> labels, final List<Map<String, Object>> rows,
                                               final AtomicInteger getMetaData, final AtomicInteger getColumnCount,
                                               final AtomicInteger getColumnLabel) {
        final var metadata = proxy(ResultSetMetaData.class, (proxy, method, args) -> {
            if (method.getName().equals("getColumnCount")) {
                getColumnCount.incrementAndGet();
                return labels.size();
            }
            if (method.getName().equals("getColumnLabel")) {
                getColumnLabel.incrementAndGet();
                return labels.get(((Integer) args[0]) - 1);
            }
            return defaultValue(method.getReturnType());
        });
        final var index = new int[] {-1};
        return proxy(ResultSet.class, (proxy, method, args) -> {
            final var name = method.getName();
            if (name.equals("next")) {
                index[0]++;
                return index[0] < rows.size();
            }
            if (name.equals("getMetaData")) {
                getMetaData.incrementAndGet();
                return metadata;
            }
            if (name.equals("getObject") && args.length == 1) {
                return rows.get(index[0]).get((String) args[0]);
            }
            if (name.equals("getShort")) {
                return ((Number) rows.get(index[0]).get((String) args[0])).shortValue();
            }
            if (name.equals("getInt")) {
                return ((Number) rows.get(index[0]).get((String) args[0])).intValue();
            }
            return defaultValue(method.getReturnType());
        });
    }

    private static DatabaseMetaData metadata(final String resultSetMethodName, final ResultSet resultSet) {
        return proxy(DatabaseMetaData.class, (proxy, method, args) -> {
            if (method.getName().equals(resultSetMethodName)) {
                return resultSet;
            }
            return defaultValue(method.getReturnType());
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(final Class<T> interfaceType, final InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[] {interfaceType},
                handler
        );
    }

    private static Object defaultValue(final Class<?> type) {
        if (type == Boolean.TYPE) {
            return false;
        }
        if (type == Short.TYPE) {
            return (short) 0;
        }
        if (type == Integer.TYPE) {
            return 0;
        }
        if (type == Long.TYPE) {
            return 0L;
        }
        return null;
    }
}
