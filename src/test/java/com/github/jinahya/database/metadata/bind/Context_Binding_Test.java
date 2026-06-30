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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class Context_Binding_Test {

    @Test
    void getCatalogs_BindsUnknownColumns_() throws SQLException {
        final var results = resultSet(
                List.of(Catalog.COLUMN_LABEL_TABLE_CAT, "DRIVER_EXTRA"),
                List.of(Map.of(
                        Catalog.COLUMN_LABEL_TABLE_CAT, "catalog",
                        "DRIVER_EXTRA", "extra"
                ))
        );
        final var catalogs = new Context(metadata("getCatalogs", results)).getCatalogs();
        assertThat(catalogs).singleElement().satisfies(catalog -> {
            assertThat(catalog.getTableCat()).isEqualTo("catalog");
            assertThat(catalog.getUnknownColumns()).containsEntry("DRIVER_EXTRA", "extra");
            assertThatExceptionOfType(UnsupportedOperationException.class)
                    .isThrownBy(() -> catalog.getUnknownColumns().clear());
        });
    }

    @Test
    void getClientInfoProperties_ToleratesMissingExpectedColumns_() throws SQLException {
        final var results = resultSet(
                List.of(ClientInfoProperty.COLUMN_LABEL_NAME),
                List.of(Map.of(ClientInfoProperty.COLUMN_LABEL_NAME, "property"))
        );
        final var properties = new Context(metadata("getClientInfoProperties", results)).getClientInfoProperties();
        assertThat(properties).singleElement().satisfies(property -> {
            assertThat(property.getName()).isEqualTo("property");
            assertThat(property.getMaxLen()).isNull();
            assertThat(property.getDefaultValue()).isNull();
            assertThat(property.getDescription()).isNull();
        });
    }

    @Test
    void getClientInfoProperties_CoercesIntegerFieldWithTypedGetter_() throws SQLException {
        final var results = resultSet(
                List.of(
                        ClientInfoProperty.COLUMN_LABEL_NAME,
                        ClientInfoProperty.COLUMN_LABEL_MAX_LEN,
                        ClientInfoProperty.COLUMN_LABEL_DEFAULT_VALUE,
                        ClientInfoProperty.COLUMN_LABEL_DESCRIPTION
                ),
                List.of(Map.of(
                        ClientInfoProperty.COLUMN_LABEL_NAME, "property",
                        ClientInfoProperty.COLUMN_LABEL_MAX_LEN, Short.valueOf((short) 4),
                        ClientInfoProperty.COLUMN_LABEL_DEFAULT_VALUE, "default",
                        ClientInfoProperty.COLUMN_LABEL_DESCRIPTION, "description"
                ))
        );
        final var properties = new Context(metadata("getClientInfoProperties", results)).getClientInfoProperties();
        assertThat(properties).singleElement().satisfies(property -> {
            assertThat(property.getMaxLen()).isEqualTo(4);
        });
    }

    @Test
    void getCatalogs_ThrowsOnUncoercibleValue_() {
        final var results = resultSet(
                List.of(Catalog.COLUMN_LABEL_TABLE_CAT),
                List.of(Map.of(Catalog.COLUMN_LABEL_TABLE_CAT, new Object()))
        );
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new Context(metadata("getCatalogs", results)).getCatalogs())
                .withMessageContaining("tableCat");
    }

    private static DatabaseMetaData metadata(final String resultSetMethodName, final ResultSet resultSet) {
        return proxy(DatabaseMetaData.class, (proxy, method, args) -> {
            if (method.getName().equals(resultSetMethodName)) {
                return resultSet;
            }
            return defaultValue(method.getReturnType());
        });
    }

    private static ResultSet resultSet(final List<String> labels, final List<Map<String, Object>> rows) {
        final var metadata = resultSetMetaData(labels);
        final var index = new int[] {-1};
        return proxy(ResultSet.class, (proxy, method, args) -> {
            final var name = method.getName();
            if (name.equals("next")) {
                index[0]++;
                return index[0] < rows.size();
            }
            if (name.equals("getMetaData")) {
                return metadata;
            }
            if (name.equals("getObject") && args.length == 1) {
                return rows.get(index[0]).get((String) args[0]);
            }
            if (name.equals("getObject") && args.length == 2) {
                final var value = rows.get(index[0]).get((String) args[0]);
                final var type = (Class<?>) args[1];
                if (value == null || type.isInstance(value)) {
                    return value;
                }
                throw new SQLException("unsupported conversion to " + type.getName());
            }
            if (name.equals("getBoolean")) {
                return rows.get(index[0]).get((String) args[0]);
            }
            if (name.equals("getShort")) {
                return ((Number) rows.get(index[0]).get((String) args[0])).shortValue();
            }
            if (name.equals("getInt")) {
                return ((Number) rows.get(index[0]).get((String) args[0])).intValue();
            }
            if (name.equals("getLong")) {
                return ((Number) rows.get(index[0]).get((String) args[0])).longValue();
            }
            return defaultValue(method.getReturnType());
        });
    }

    private static ResultSetMetaData resultSetMetaData(final List<String> labels) {
        return proxy(ResultSetMetaData.class, (proxy, method, args) -> {
            if (method.getName().equals("getColumnCount")) {
                return labels.size();
            }
            if (method.getName().equals("getColumnLabel")) {
                return labels.get(((Integer) args[0]) - 1);
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
        if (type == Byte.TYPE) {
            return (byte) 0;
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
        if (type == Float.TYPE) {
            return 0.0F;
        }
        if (type == Double.TYPE) {
            return 0.0D;
        }
        if (type == Character.TYPE) {
            return '\0';
        }
        return null;
    }
}
