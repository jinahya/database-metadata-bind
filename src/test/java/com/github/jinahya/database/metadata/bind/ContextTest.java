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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * A class for testing {@link Context} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class ContextTest {

    /**
     * Verifies that all {@link DatabaseMetaData} methods returning {@link ResultSet} have corresponding {@code public}
     * methods in {@link Context} with identical signatures that return {@link List}.
     *
     * @throws ReflectiveOperationException if a reflective operation fails.
     */
    @DisplayName("all ...(...)ResultSet method bound")
    @Test
    void assertAllMethodsBound() throws ReflectiveOperationException {
        for (final var method : DatabaseMetaData.class.getMethods()) {
            final int modifiers = method.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                continue;
            }
            if (method.getDeclaringClass() != DatabaseMetaData.class) {
                continue;
            }
            if (!ResultSet.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }
            log.debug("method: {}", method);
            final var name = method.getName();
            {
                final var found = Context.class.getMethod(name, method.getParameterTypes());
                assertThat(found.getModifiers()).satisfies(m -> {
                    assertThat(Modifier.isStatic(m)).isFalse();
                });
                assertThat(found.getReturnType()).isEqualTo(List.class);
            }
        }
    }

    @Test
    void forEachMethods_DelegateToAcceptEachMethods_BypassingListMethods() throws SQLException {
        final var context = spy(new Context(mock(DatabaseMetaData.class)));
        // ------------------------------------------------------------------------------------------------ attributes
        {
            final Consumer<? super Attribute> consumer = mockConsumer();
            doNothing().when(context).getAttributesAndAcceptEach(null, "s", "t", "a", consumer);
            context.forEachAttribute(null, "s", "t", "a", consumer);
            verify(context).getAttributesAndAcceptEach(null, "s", "t", "a", consumer);
            verify(context, never()).getAttributes(null, "s", "t", "a");
        }
        // --------------------------------------------------------------------------------------- bestRowIdentifier
        {
            final Consumer<? super BestRowIdentifier> consumer = mockConsumer();
            doNothing().when(context).getBestRowIdentifierAndAcceptEach(null, "s", "t", 1, true, consumer);
            context.forEachBestRowIdentifier(null, "s", "t", 1, true, consumer);
            verify(context).getBestRowIdentifierAndAcceptEach(null, "s", "t", 1, true, consumer);
            verify(context, never()).getBestRowIdentifier(null, "s", "t", 1, true);
        }
        // ------------------------------------------------------------------------------------------------ catalogs
        {
            final Consumer<? super Catalog> consumer = mockConsumer();
            doNothing().when(context).getCatalogsAndAcceptEach(consumer);
            context.forEachCatalog(consumer);
            verify(context).getCatalogsAndAcceptEach(consumer);
            verify(context, never()).getCatalogs();
        }
        // ------------------------------------------------------------------------------------- clientInfoProperties
        {
            final Consumer<? super ClientInfoProperty> consumer = mockConsumer();
            doNothing().when(context).getClientInfoPropertiesAndAcceptEach(consumer);
            context.forEachClientInfoProperty(consumer);
            verify(context).getClientInfoPropertiesAndAcceptEach(consumer);
            verify(context, never()).getClientInfoProperties();
        }
        // -------------------------------------------------------------------------------------------- columnPrivileges
        {
            final Consumer<? super ColumnPrivilege> consumer = mockConsumer();
            doNothing().when(context).getColumnPrivilegesAndAcceptEach(null, "s", "t", "c", consumer);
            context.forEachColumnPrivilege(null, "s", "t", "c", consumer);
            verify(context).getColumnPrivilegesAndAcceptEach(null, "s", "t", "c", consumer);
            verify(context, never()).getColumnPrivileges(null, "s", "t", "c");
        }
        // -------------------------------------------------------------------------------------------------- columns
        {
            final Consumer<? super Column> consumer = mockConsumer();
            doNothing().when(context).getColumnsAndAcceptEach(null, "s", "t", "c", consumer);
            context.forEachColumn(null, "s", "t", "c", consumer);
            verify(context).getColumnsAndAcceptEach(null, "s", "t", "c", consumer);
            verify(context, never()).getColumns(null, "s", "t", "c");
        }
        // --------------------------------------------------------------------------------------------- crossReference
        {
            final Consumer<? super CrossReference> consumer = mockConsumer();
            doNothing().when(context).getCrossReferenceAndAcceptEach(null, "ps", "pt", null, "fs", "ft", consumer);
            context.forEachCrossReference(null, "ps", "pt", null, "fs", "ft", consumer);
            verify(context).getCrossReferenceAndAcceptEach(null, "ps", "pt", null, "fs", "ft", consumer);
            verify(context, never()).getCrossReference(null, "ps", "pt", null, "fs", "ft");
        }
        // ----------------------------------------------------------------------------------------------- exportedKeys
        {
            final Consumer<? super ExportedKey> consumer = mockConsumer();
            doNothing().when(context).getExportedKeysAndAcceptEach(null, "s", "t", consumer);
            context.forEachExportedKey(null, "s", "t", consumer);
            verify(context).getExportedKeysAndAcceptEach(null, "s", "t", consumer);
            verify(context, never()).getExportedKeys(null, "s", "t");
        }
        // ------------------------------------------------------------------------------------------------ functions
        {
            final Consumer<? super Function> consumer = mockConsumer();
            doNothing().when(context).getFunctionsAndAcceptEach(null, "s", "f", consumer);
            context.forEachFunction(null, "s", "f", consumer);
            verify(context).getFunctionsAndAcceptEach(null, "s", "f", consumer);
            verify(context, never()).getFunctions(null, "s", "f");
        }
        // -------------------------------------------------------------------------------------------- functionColumns
        {
            final Consumer<? super FunctionColumn> consumer = mockConsumer();
            doNothing().when(context).getFunctionColumnsAndAcceptEach(null, "s", "f", "c", consumer);
            context.forEachFunctionColumn(null, "s", "f", "c", consumer);
            verify(context).getFunctionColumnsAndAcceptEach(null, "s", "f", "c", consumer);
            verify(context, never()).getFunctionColumns(null, "s", "f", "c");
        }
        // ----------------------------------------------------------------------------------------------- importedKeys
        {
            final Consumer<? super ImportedKey> consumer = mockConsumer();
            doNothing().when(context).getImportedKeysAndAcceptEach(null, "s", "t", consumer);
            context.forEachImportedKey(null, "s", "t", consumer);
            verify(context).getImportedKeysAndAcceptEach(null, "s", "t", consumer);
            verify(context, never()).getImportedKeys(null, "s", "t");
        }
        // ------------------------------------------------------------------------------------------------- indexInfo
        {
            final Consumer<? super IndexInfo> consumer = mockConsumer();
            doNothing().when(context).getIndexInfoAndAcceptEach(null, "s", "t", true, false, consumer);
            context.forEachIndexInfo(null, "s", "t", true, false, consumer);
            verify(context).getIndexInfoAndAcceptEach(null, "s", "t", true, false, consumer);
            verify(context, never()).getIndexInfo(null, "s", "t", true, false);
        }
        // ----------------------------------------------------------------------------------------------- primaryKeys
        {
            final Consumer<? super PrimaryKey> consumer = mockConsumer();
            doNothing().when(context).getPrimaryKeysAndAcceptEach(null, "s", "t", consumer);
            context.forEachPrimaryKey(null, "s", "t", consumer);
            verify(context).getPrimaryKeysAndAcceptEach(null, "s", "t", consumer);
            verify(context, never()).getPrimaryKeys(null, "s", "t");
        }
        // -------------------------------------------------------------------------------------------- procedureColumns
        {
            final Consumer<? super ProcedureColumn> consumer = mockConsumer();
            doNothing().when(context).getProcedureColumnsAndAcceptEach(null, "s", "p", "c", consumer);
            context.forEachProcedureColumn(null, "s", "p", "c", consumer);
            verify(context).getProcedureColumnsAndAcceptEach(null, "s", "p", "c", consumer);
            verify(context, never()).getProcedureColumns(null, "s", "p", "c");
        }
        // ------------------------------------------------------------------------------------------------ procedures
        {
            final Consumer<? super Procedure> consumer = mockConsumer();
            doNothing().when(context).getProceduresAndAcceptEach(null, "s", "p", consumer);
            context.forEachProcedure(null, "s", "p", consumer);
            verify(context).getProceduresAndAcceptEach(null, "s", "p", consumer);
            verify(context, never()).getProcedures(null, "s", "p");
        }
        // --------------------------------------------------------------------------------------------- pseudoColumns
        {
            final Consumer<? super PseudoColumn> consumer = mockConsumer();
            doNothing().when(context).getPseudoColumnsAndAcceptEach(null, "s", "t", "c", consumer);
            context.forEachPseudoColumn(null, "s", "t", "c", consumer);
            verify(context).getPseudoColumnsAndAcceptEach(null, "s", "t", "c", consumer);
            verify(context, never()).getPseudoColumns(null, "s", "t", "c");
        }
        // -------------------------------------------------------------------------------------------------- schemas
        {
            final Consumer<? super Schema> consumer = mockConsumer();
            doNothing().when(context).getSchemasAndAcceptEach(consumer);
            context.forEachSchema(consumer);
            verify(context).getSchemasAndAcceptEach(consumer);
            verify(context, never()).getSchemas();
        }
        {
            final Consumer<? super Schema> consumer = mockConsumer();
            doNothing().when(context).getSchemasAndAcceptEach(null, "s", consumer);
            context.forEachSchema(null, "s", consumer);
            verify(context).getSchemasAndAcceptEach(null, "s", consumer);
            verify(context, never()).getSchemas(null, "s");
        }
        // ----------------------------------------------------------------------------------------------- superTables
        {
            final Consumer<? super SuperTable> consumer = mockConsumer();
            doNothing().when(context).getSuperTablesAndAcceptEach(null, "s", "t", consumer);
            context.forEachSuperTable(null, "s", "t", consumer);
            verify(context).getSuperTablesAndAcceptEach(null, "s", "t", consumer);
            verify(context, never()).getSuperTables(null, "s", "t");
        }
        // ------------------------------------------------------------------------------------------------ superTypes
        {
            final Consumer<? super SuperType> consumer = mockConsumer();
            doNothing().when(context).getSuperTypesAndAcceptEach(null, "s", "t", consumer);
            context.forEachSuperType(null, "s", "t", consumer);
            verify(context).getSuperTypesAndAcceptEach(null, "s", "t", consumer);
            verify(context, never()).getSuperTypes(null, "s", "t");
        }
        // -------------------------------------------------------------------------------------------- tablePrivileges
        {
            final Consumer<? super TablePrivilege> consumer = mockConsumer();
            doNothing().when(context).getTablePrivilegesAndAcceptEach(null, "s", "t", consumer);
            context.forEachTablePrivilege(null, "s", "t", consumer);
            verify(context).getTablePrivilegesAndAcceptEach(null, "s", "t", consumer);
            verify(context, never()).getTablePrivileges(null, "s", "t");
        }
        // ----------------------------------------------------------------------------------------------- tableTypes
        {
            final Consumer<? super TableType> consumer = mockConsumer();
            doNothing().when(context).getTableTypesAndAcceptEach(consumer);
            context.forEachTableType(consumer);
            verify(context).getTableTypesAndAcceptEach(consumer);
            verify(context, never()).getTableTypes();
        }
        // --------------------------------------------------------------------------------------------------- tables
        {
            final Consumer<? super Table> consumer = mockConsumer();
            final String[] types = {"TABLE"};
            doNothing().when(context).getTablesAndAcceptEach(null, "s", "t", types, consumer);
            context.forEachTable(null, "s", "t", types, consumer);
            verify(context).getTablesAndAcceptEach(null, "s", "t", types, consumer);
            verify(context, never()).getTables(null, "s", "t", types);
        }
        // ------------------------------------------------------------------------------------------------ typeInfo
        {
            final Consumer<? super TypeInfo> consumer = mockConsumer();
            doNothing().when(context).getTypeInfoAndAcceptEach(consumer);
            context.forEachTypeInfo(consumer);
            verify(context).getTypeInfoAndAcceptEach(consumer);
            verify(context, never()).getTypeInfo();
        }
        // ----------------------------------------------------------------------------------------------------- udts
        {
            final Consumer<? super UDT> consumer = mockConsumer();
            final int[] types = {java.sql.Types.STRUCT};
            doNothing().when(context).getUDTsAndAcceptEach(null, "s", "t", types, consumer);
            context.forEachUDT(null, "s", "t", types, consumer);
            verify(context).getUDTsAndAcceptEach(null, "s", "t", types, consumer);
            verify(context, never()).getUDTs(null, "s", "t", types);
        }
        // --------------------------------------------------------------------------------------------- versionColumns
        {
            final Consumer<? super VersionColumn> consumer = mockConsumer();
            doNothing().when(context).getVersionColumnsAndAcceptEach(null, "s", "t", consumer);
            context.forEachVersionColumn(null, "s", "t", consumer);
            verify(context).getVersionColumnsAndAcceptEach(null, "s", "t", consumer);
            verify(context, never()).getVersionColumns(null, "s", "t");
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Consumer<? super T> mockConsumer() {
        return mock(Consumer.class);
    }
}
