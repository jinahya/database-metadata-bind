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

import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class Context_MetadataCoverage_Test {

    private static final Map<String, String> FOR_EACH_NAMES = Map.ofEntries(
            Map.entry("getAttributes", "forEachAttribute"),
            Map.entry("getBestRowIdentifier", "forEachBestRowIdentifier"),
            Map.entry("getCatalogs", "forEachCatalog"),
            Map.entry("getClientInfoProperties", "forEachClientInfoProperty"),
            Map.entry("getColumnPrivileges", "forEachColumnPrivilege"),
            Map.entry("getColumns", "forEachColumn"),
            Map.entry("getCrossReference", "forEachCrossReference"),
            Map.entry("getExportedKeys", "forEachExportedKey"),
            Map.entry("getFunctions", "forEachFunction"),
            Map.entry("getFunctionColumns", "forEachFunctionColumn"),
            Map.entry("getImportedKeys", "forEachImportedKey"),
            Map.entry("getIndexInfo", "forEachIndexInfo"),
            Map.entry("getPrimaryKeys", "forEachPrimaryKey"),
            Map.entry("getProcedureColumns", "forEachProcedureColumn"),
            Map.entry("getProcedures", "forEachProcedure"),
            Map.entry("getPseudoColumns", "forEachPseudoColumn"),
            Map.entry("getSchemas", "forEachSchema"),
            Map.entry("getSuperTables", "forEachSuperTable"),
            Map.entry("getSuperTypes", "forEachSuperType"),
            Map.entry("getTablePrivileges", "forEachTablePrivilege"),
            Map.entry("getTableTypes", "forEachTableType"),
            Map.entry("getTables", "forEachTable"),
            Map.entry("getTypeInfo", "forEachTypeInfo"),
            Map.entry("getUDTs", "forEachUDT"),
            Map.entry("getVersionColumns", "forEachVersionColumn")
    );

    @DisplayName("all DatabaseMetaData ResultSet methods have Context get... bindings")
    @Test
    void resultSetMethods_HaveGetBindings() throws ReflectiveOperationException {
        for (final var method : resultSetMethods()) {
            final var found = Context.class.getMethod(method.getName(), method.getParameterTypes());
            assertThat(Modifier.isStatic(found.getModifiers())).isFalse();
            assertThat(found.getReturnType()).isEqualTo(List.class);
        }
    }

    @DisplayName("all Context get... bindings have matching forEach... bindings")
    @Test
    void getBindings_HaveForEachBindings() throws ReflectiveOperationException {
        for (final var method : resultSetMethods()) {
            final var name = method.getName();
            final var valueType = Context.class.getMethod(name, method.getParameterTypes())
                    .getGenericReturnType()
                    .getTypeName();
            assertThat(valueType).startsWith(List.class.getName());
            final var parameterTypes = Arrays.copyOf(method.getParameterTypes(), method.getParameterCount() + 1);
            parameterTypes[parameterTypes.length - 1] = Consumer.class;
            final var found = Context.class.getMethod(FOR_EACH_NAMES.get(name), parameterTypes);
            assertThat(Modifier.isStatic(found.getModifiers())).isFalse();
            assertThat(found.getReturnType()).isEqualTo(Void.TYPE);
        }
    }

    private static List<java.lang.reflect.Method> resultSetMethods() {
        return Arrays.stream(DatabaseMetaData.class.getMethods())
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .filter(m -> m.getDeclaringClass() == DatabaseMetaData.class)
                .filter(m -> ResultSet.class.isAssignableFrom(m.getReturnType()))
                .toList();
    }
}
