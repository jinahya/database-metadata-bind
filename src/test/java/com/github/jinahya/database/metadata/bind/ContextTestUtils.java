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

import jakarta.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.validator.testutil.ValidationInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTestUtils {

    static void info(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        context.acceptValues((m, v) -> {
            log.debug("{}: {}", m.getName(), v);
        });
        return;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static InvocationHandler proxy(final InvocationHandler handler) {
        return (p, m, args) -> {
            try {
                return handler.invoke(p, m, args);
            } catch (final Throwable t) {
                final var cause = t.getCause();
                if (cause instanceof SQLFeatureNotSupportedException) {
                    log.info("not supported; {}; {}", m.getName(), cause.getMessage());
                } else {
                    log.error("failed to invoke {}.{}({})", p, m.getName(), args, cause);
                }
                throw cause;
            }
        };
    }

    static void proxy(final Context context, final Consumer<? super Context> consumer) {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            final var validator = factory.getValidator();
            try (var unloaded = new ByteBuddy()
                    .subclass(Context.class)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(
                            proxy(new ValidationInvocationHandler(context, validator))))
                    .make()) {
                final var loaded = unloaded.load(context.getClass().getClassLoader()).getLoaded();
                final Context instance;
                try {
                    final var constructor = loaded.getDeclaredConstructor(DatabaseMetaData.class);
                    if (!constructor.canAccess(null)) {
                        constructor.setAccessible(true);
                    }
                    instance = constructor.newInstance(context.metadata);
                } catch (final ReflectiveOperationException roe) {
                    throw new RuntimeException("failed to instantiate " + loaded, roe);
                }
                consumer.accept(instance);
            }
        }
    }

    static void test(final Context context) throws SQLException {
        proxy(context, p -> {
            try {
                test_(p);
            } catch (final SQLException sqle) {
                throw new RuntimeException("failed to test", sqle);
            }
        });
    }

    private static String databaseProductName(final Context context) throws SQLException {
        return context.metadata.getDatabaseProductName();
    }

    private static void test_(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        // ---------------------------------------------------------------------------------------------------- catalogs
        if (true) {
            final var catalogs = context.getCatalogs();
            if (catalogs.isEmpty()) {
                catalogs.add(Catalog.of(null));
            }
            catalogs(context, catalogs);
        }
        // -------------------------------------------------------------------------------------------- clientProperties
        try {
            final var clientInfoProperties = context.getClientInfoProperties();
            clientInfoProperties(context, clientInfoProperties);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
//        // ---------------------------------------------------------------------------------------------- crossReference
//        try {
//            final var crossReference = context.getCrossReference(
//                    null,
//                    null,
//                    "%",
//                    null,
//                    null,
//                    "%"
//            );
//            crossReference(context, crossReference);
//        } catch (final SQLException sqle) {
//            log.error("failed to get crossReference", sqle);
//        }
        // ----------------------------------------------------------------------------------- functions/functionColumns
        try {
            final var functions = context.getFunctions(null, null, "%");
            functions(context, functions);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // --------------------------------------------------------------------------------- procedures/procedureColumns
        try {
            final var procedures = context.getProcedures(null, null, "%");
            procedures(context, procedures);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        try {
            final var schemas = context.getSchemas();
            schemas(context, schemas);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        try {
            final var schemas = context.getSchemas((String) null, null);
            if (schemas.isEmpty()) {
                schemas.add(Schema.of(null, null));
            }
            schemas(context, schemas);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- tableTypes
        try {
            final var tableTypes = context.getTableTypes();
            tableTypes(context, tableTypes);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------------ tables
        try {
            final var tables = context.getTables(null, null, "%", null);
            tables(context, tables);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ---------------------------------------------------------------------------------------------------- typeInfo
        try {
            final var typeInfo = context.getTypeInfo();
            typeInfo(context, typeInfo);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------------- udts
        try {
            final var udts = context.getUDTs(null, null, "%", null);
            udts(context, udts);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // -------------------------------------------------------------------------------------------- numericFunctions
        try {
            final var numericFunctions = context.getNumericFunctions();
            assertThat(numericFunctions).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ---------------------------------------------------------------------------------------------- getSQLKeywords
        try {
            final var SQLKeywords = context.getSQLKeywords();
            assertThat(SQLKeywords).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------ getStringFunctions
        try {
            final var stringFunctions = context.getStringFunctions();
            assertThat(stringFunctions).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------ getSystemFunctions
        try {
            final var systemFunctions = context.getSystemFunctions();
            assertThat(systemFunctions).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ---------------------------------------------------------------------------------------- getTimeDateFunctions
        try {
            final var timeDateFunction = context.getTimeDateFunctions();
            assertThat(timeDateFunction).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
    }

    // ------------------------------------------------------------------------------------------------------ attributes
    private static void attributes(final Context context, final List<? extends Attribute> attributes)
            throws SQLException {
        assertThat(attributes).doesNotContainNull();
        if (true) {
            assertThat(attributes).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(attributes).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Attribute.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(Attribute.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var attribute : attributes) {
            attribute(context, attribute);
        }
    }

    private static void attribute(final Context context, final Attribute attribute) throws SQLException {
        MetadataTypeTestUtils.verify(attribute);
        {
            assertThat(attribute.getTypeName()).isNotNull();
            assertThat(attribute.getAttrName()).isNotNull();
            final var dataType = attribute.getDataType();
            assertDoesNotThrow(() -> JDBCType.valueOf(dataType));
            assertThat(attribute.getAttrTypeName()).isNotNull();
            assertDoesNotThrow(() -> Attribute.Nullable.valueOfFieldValue(attribute.getNullable()));
            assertThat(attribute.getIsNullable()).isNotNull();
        }
    }

    // ----------------------------------------------------------------------------------------------- bestRowIdentifier
    private static void bestRowIdentifier(final Context context,
                                          final List<? extends BestRowIdentifier> bestRowIdentifier)
            throws SQLException {
        assertThat(bestRowIdentifier).doesNotContainNull();
        if (true) {
            assertThat(bestRowIdentifier).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(bestRowIdentifier).isSortedAccordingTo(BestRowIdentifier.comparingScope(context));
        }
        for (final var bestRowIdentifier_ : bestRowIdentifier) {
            bestRowIdentifier(context, bestRowIdentifier_);
        }
    }

    private static void bestRowIdentifier(final Context context, final BestRowIdentifier bestRowIdentifier)
            throws SQLException {
        MetadataTypeTestUtils.verify(bestRowIdentifier);
        {
            final var scope = bestRowIdentifier.getScope();
            assertDoesNotThrow(() -> BestRowIdentifier.Scope.valueOfFieldValue(scope));
            assertThat(bestRowIdentifier.getColumnName()).isNotNull();
            assertDoesNotThrow(() -> JDBCType.valueOf(bestRowIdentifier.getDataType()));
            assertThat(bestRowIdentifier.getTypeName()).isNotNull();
            final int pseudoColumn = bestRowIdentifier.getPseudoColumn();
            assertDoesNotThrow(() -> BestRowIdentifier.PseudoColumn.valueOfFieldValue(pseudoColumn));
        }
    }

    // -------------------------------------------------------------------------------------------------------- catalogs
    private static void catalogs(final Context context, final List<? extends Catalog> catalogs) throws SQLException {
        assertThat(catalogs).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(catalogs).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(catalogs).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Catalog.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(Catalog.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var catalog : catalogs) {
            catalog(context, catalog);
        }
    }

    private static void catalog(final Context context, final Catalog catalog) throws SQLException {
        MetadataTypeTestUtils.verify(catalog);
        // -------------------------------------------------------------------------------------------------- procedures
        try {
            final var procedures = context.getProcedures(catalog, "%");
            procedures(context, procedures);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        try {
            final var schemas = context.getSchemas(catalog, "%");
            if (schemas.isEmpty()) {
                schemas.add(Schema.of(null, null));
            }
            schemas(context, schemas);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(catalog.getTableCat(), "%", "%");
            superTables(context, superTables);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(catalog.getTableCat(), "%", "%");
            superTypes(context, superTypes);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var tablePrivileges = context.getTablePrivileges(catalog.getTableCat(), "%", "%");
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
    }

    // -------------------------------------------------------------------------------------------- clientInfoProperties
    private static void clientInfoProperties(final Context context,
                                             final List<? extends ClientInfoProperty> clientInfoProperties)
            throws SQLException {
        assertThat(clientInfoProperties).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(clientInfoProperties).doesNotHaveDuplicates();
        }
        if (!databaseProductName(context).equals(DatabaseProductNames.MARIA_DB)) {
            // https://jira.mariadb.org/browse/CONJ-1159
            assertThat(clientInfoProperties).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(
                            ClientInfoProperty.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(
                            ClientInfoProperty.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var clientInfoProperty : clientInfoProperties) {
            clientInfoProperty(context, clientInfoProperty);
        }
    }

    private static void clientInfoProperty(final Context context, final ClientInfoProperty clientInfoProperty)
            throws SQLException {
        MetadataTypeTestUtils.verify(clientInfoProperty);
    }

    // --------------------------------------------------------------------------------------------------------- columns
    private static void columns(final Context context, final List<? extends Column> columns) throws SQLException {
        assertThat(columns).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(columns).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(columns).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Column.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(Column.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var column : columns) {
            column(context, column);
        }
    }

    private static void column(final Context context, final Column column) throws SQLException {
        MetadataTypeTestUtils.verify(column);
        {
            assertThat(column.getTableName()).isNotNull();
            assertThat(column.getColumnName()).isNotNull();
//            assertDoesNotThrow(() -> JDBCType.valueOf(column.getDataType()));
            assertThat(column.getOrdinalPosition()).isPositive();
            assertThat(column.getIsNullable()).isNotNull();
            assertThat(column.getIsAutoincrement()).isNotNull();
            assertThat(column.getIsGeneratedcolumn()).isNotNull();
        }
        {
            final var value = Column.Nullable.valueOfFieldValue(column.getNullable());
        }
    }

    // ------------------------------------------------------------------------------------------------ columnPrivileges
    private static void columnPrivileges(final Context context, final List<? extends ColumnPrivilege> columnPrivileges)
            throws SQLException {
        assertThat(columnPrivileges).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(columnPrivileges).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(columnPrivileges).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(
                            ColumnPrivilege.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(
                            ColumnPrivilege.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var columnPrivilege : columnPrivileges) {
            columnPrivilege(context, columnPrivilege);
        }
    }

    private static void columnPrivilege(final Context context, final ColumnPrivilege columnPrivilege)
            throws SQLException {
        MetadataTypeTestUtils.verify(columnPrivilege);
    }

    // -------------------------------------------------------------------------------------------------- crossReference
    private static void crossReference(final Context context, final List<CrossReference> crossReference)
            throws SQLException {
        assertThat(crossReference).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(crossReference).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(crossReference).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(
                            CrossReference.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(CrossReference.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var v : crossReference) {
            crossReference(context, v);
        }
    }

    private static void crossReference(final Context context, final CrossReference crossReference) throws SQLException {
        MetadataTypeTestUtils.verify(crossReference);
    }

    // ---------------------------------------------------------------------------------------------------- exportedKeys
    private static void exportedKeys(final Context context, final List<? extends ExportedKey> exportedKeys)
            throws SQLException {
        assertThat(exportedKeys).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(exportedKeys).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(exportedKeys).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(
                            ExportedKey.comparingFktable(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(
                            ExportedKey.comparingFktable(context, Comparator.naturalOrder()))
            );
        }
        for (final var exportedKey : exportedKeys) {
            exportedKey(context, exportedKey);
        }
    }

    private static void exportedKey(final Context context, final ExportedKey exportedKey) throws SQLException {
        MetadataTypeTestUtils.verify(exportedKey);
    }

    // ------------------------------------------------------------------------------------------------------- functions
    static void functions(final Context context, final List<? extends Function> functions) throws SQLException {
        assertThat(functions).isNotNull().doesNotContainNull();
        if (!databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL)) {
            assertThat(functions).doesNotHaveDuplicates();
        }
        if (!databaseProductName(context).equals(DatabaseProductNames.MARIA_DB)
            // https://jira.mariadb.org/browse/CONJ-1158
            && !databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL)
            && !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            // https://github.com/microsoft/mssql-jdbc/issues/2321
            assertThat(functions).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Function.comparingInCaseInsensitiveOrder(context)),
                    l -> assertThat(l).isSortedAccordingTo(Function.comparingInNaturalOrder(context))
            );
        }
        for (final var function : functions) {
            function(context, function);
        }
    }

    private static void function(final Context context, final Function function) throws SQLException {
        MetadataTypeTestUtils.verify(function);
        try {
            final var functionColumns = context.getFunctionColumns(function, "%");
            functionColumns(context, functionColumns);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
    }

    // ------------------------------------------------------------------------------------------------- functionColumns
    private static void functionColumns(final Context context, final List<? extends FunctionColumn> functionColumns)
            throws SQLException {
        assertThat(functionColumns).isNotNull().doesNotContainNull();
        if (!databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL)) {
            // https://github.com/pgjdbc/pgjdbc/issues/3127
            assertThat(functionColumns).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(functionColumns).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(FunctionColumn.comparingInCaseInsensitiveOrder(context)),
                    l -> assertThat(l).isSortedAccordingTo(FunctionColumn.comparingInNaturalOrder(context))
            );
        }
        for (final var functionColumn : functionColumns) {
            functionColumn(context, functionColumn);
        }
    }

    private static void functionColumn(final Context context, final FunctionColumn functionColumn)
            throws SQLException {
        MetadataTypeTestUtils.verify(functionColumn);
        final var columnType = FunctionColumn.ColumnType.valueOfFieldValue(functionColumn.getColumnType());
    }

    // ---------------------------------------------------------------------------------------------------- importedKeys
    private static void importedKeys(final Context context, final List<? extends ImportedKey> importedKeys)
            throws SQLException {
        assertThat(importedKeys).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(importedKeys).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(importedKeys).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(
                            ImportedKey.comparingPktable(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(
                            ImportedKey.comparingPktable_(context, Comparator.naturalOrder()))
            );
        }
        for (final var importedKey : importedKeys) {
            importedKey(context, importedKey);
        }
    }

    private static void importedKey(final Context context, final ImportedKey importedKey) throws SQLException {
        MetadataTypeTestUtils.verify(importedKey);
    }

    // ------------------------------------------------------------------------------------------------------- indexInfo
    private static void indexInfo(final Context context, final List<IndexInfo> indexInfo) throws SQLException {
        assertThat(indexInfo).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(indexInfo).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(indexInfo).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(IndexInfo.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(IndexInfo.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var v : indexInfo) {
            indexInfo(context, v);
        }
    }

    private static void indexInfo(final Context context, final IndexInfo indexInfo) throws SQLException {
        MetadataTypeTestUtils.verify(indexInfo);
    }

    // ------------------------------------------------------------------------------------------------------ procedures
    private static void procedures(final Context context, final List<? extends Procedure> procedures)
            throws SQLException {
        assertThat(procedures).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(procedures).doesNotHaveDuplicates();
        }
        if (true
            && !databaseProductName(context).equals(DatabaseProductNames.MARIA_DB) &&
            !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            assertThat(procedures).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Procedure.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(Procedure.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var procedure : procedures) {
            procedure(context, procedure);
        }
    }

    private static void procedure(final Context context, final Procedure procedure) throws SQLException {
        MetadataTypeTestUtils.verify(procedure);
        if (true) {
            final var procedureColumns = context.getProcedureColumns(procedure, "%");
            procedureColumns(context, procedureColumns);
        }
    }

    // ------------------------------------------------------------------------------------------------ procedureColumns
    private static void procedureColumns(final Context context, final List<? extends ProcedureColumn> procedureColumns)
            throws SQLException {
        assertThat(procedureColumns).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(procedureColumns).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(procedureColumns).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(
                            ProcedureColumn.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(
                            ProcedureColumn.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var procedureColumn : procedureColumns) {
            procedureColumn(context, procedureColumn);
        }
    }

    private static void procedureColumn(final Context context, final ProcedureColumn procedureColumn)
            throws SQLException {
        MetadataTypeTestUtils.verify(procedureColumn);
    }

    // --------------------------------------------------------------------------------------------------------- schemas
    private static void schemas(final Context context, final List<? extends Schema> schemas) throws SQLException {
        assertThat(schemas).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(schemas).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(schemas).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Schema.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(Schema.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var schema : schemas) {
            schema(context, schema);
        }
    }

    private static void schema(final Context context, final Schema schema) throws SQLException {
        MetadataTypeTestUtils.verify(schema);
        // -------------------------------------------------------------------------------------------------- procedures
        try {
            final var procedures = context.getAllProcedures(schema);
            procedures(context, procedures);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(schema, "%");
            superTables(context, superTables);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(schema, "%");
            superTypes(context, superTypes);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        if (true) {
            final var tablePrivileges = context.getTablePrivileges(schema, "%");
            tablePrivileges(context, tablePrivileges);
        }
    }

    // ------------------------------------------------------------------------------------------------------ superTypes
    private static void superTypes(final Context context, final List<? extends SuperType> superTypes)
            throws SQLException {
        assertThat(superTypes).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(superTypes).doesNotHaveDuplicates();
        }
        for (final var superType : superTypes) {
            superType(context, superType);
        }
    }

    private static void superType(final Context context, final SuperType superType) throws SQLException {
        MetadataTypeTestUtils.verify(superType);
        {
            assertThat(superType.getTypeName()).isNotNull();
            assertThat(superType.getSupertypeName()).isNotNull();
        }
    }

    // ---------------------------------------------------------------------------------------------------------- tables
    static void tables(final Context context, final List<? extends Table> tables) throws SQLException {
        assertThat(tables).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(tables).doesNotHaveDuplicates();
        }
        if (!databaseProductName(context).equals(DatabaseProductNames.MARIA_DB) &&
            !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            // https://jira.mariadb.org/browse/CONJ-1156
            assertThat(tables).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Table.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(Table.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var table : tables) {
            table(context, table);
        }
        if (false) {
            try (var executor = Executors.newFixedThreadPool(128, Thread.ofVirtual().factory())) {
                for (final var foreignTable : tables) {
                    for (final var parentTable : tables) {
                        executor.submit(() -> {
                            try (var connection = context.connectionSupplier.get()) {
                                final var c = Context.newInstance(connection);
                                final var crossReference = c.getCrossReference(parentTable, foreignTable);
                                log.debug("crossReference.size: {}", crossReference.size());
                                crossReference(context, crossReference);
                            } catch (final SQLException sqle) {
                                log.error("failed", sqle);
                            }
                        });
                    }
                }
            }
        }
    }

    private static void table(final Context context, final Table table) throws SQLException {
        MetadataTypeTestUtils.verify(table);
        // ------------------------------------------------------------------------------------------- bestRowIdentifier
        for (final BestRowIdentifier.Scope scope : BestRowIdentifier.Scope.values()) {
            for (final boolean nullable : new boolean[] {true, false}) {
                try {
                    final var bestRowIdentifier =
                            context.getBestRowIdentifier(table, scope.fieldValueAsInt(), nullable);
                    bestRowIdentifier(context, bestRowIdentifier);
                } catch (final SQLFeatureNotSupportedException sqlfnse) {
                    // empty
                }
            }
        }
        // ----------------------------------------------------------------------------------------------------- columns
        try {
            final var columns = context.getAllColumns(table);
            columns(context, columns);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // -------------------------------------------------------------------------------------------- columnPrivileges
        try {
            final var columnPrivileges = context.getAllColumnPrivileges(table);
            columnPrivileges(context, columnPrivileges);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------ exportedKeys
        try {
            final var exportedKeys = context.getExportedKeys(table);
            exportedKeys(context, exportedKeys);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------ importedKeys
        try {
            final var importedKeys = context.getImportedKeys(table);
            importedKeys(context, importedKeys);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // --------------------------------------------------------------------------------------------------- indexInfo
        try {
            for (final boolean unique : new boolean[] {true, false}) {
                for (final boolean approximate : new boolean[] {true, false}) {
                    final var indexInfo = context.getIndexInfo(table, unique, approximate);
                    indexInfo(context, indexInfo);
                }
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- primaryKeys
        try {
            final var primaryKeys = context.getPrimaryKeys(table);
            primaryKeys(context, primaryKeys);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------- pseudoColumns
        try {
            final var pseudoColumns = context.getPseudoColumns(table, "%");
            pseudoColumns(context, pseudoColumns);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(table);
            superTables(context, superTables);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var tablePrivileges = context.getTablePrivileges(table);
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // ---------------------------------------------------------------------------------------------- versionColumns
        try {
            final var versionColumns = context.getVersionColumns(table);
            versionColumns(context, versionColumns);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
    }

    // ----------------------------------------------------------------------------------------------------- primaryKeys
    private static void primaryKeys(final Context context, final List<? extends PrimaryKey> primaryKeys)
            throws SQLException {
        assertThat(primaryKeys).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(primaryKeys).doesNotHaveDuplicates();
        }
        if (true
            && !databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL)
        ) {
            assertThat(primaryKeys).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(
                            PrimaryKey.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(PrimaryKey.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var primaryKey : primaryKeys) {
            primaryKey(context, primaryKey);
        }
    }

    private static void primaryKey(final Context context, final PrimaryKey primaryKey) throws SQLException {
        MetadataTypeTestUtils.verify(primaryKey);
    }

    // --------------------------------------------------------------------------------------------------- pseudoColumns
    private static void pseudoColumns(final Context context, final List<? extends PseudoColumn> pseudoColumns)
            throws SQLException {
        assertThat(pseudoColumns).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(pseudoColumns).doesNotContainNull();
        }
        if (true) {
            assertThat(pseudoColumns).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(
                            PseudoColumn.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(PseudoColumn.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var pseudoColumn : pseudoColumns) {
            pseudoColumn(context, pseudoColumn);
        }
    }

    private static void pseudoColumn(final Context context, final PseudoColumn pseudoColumn) throws SQLException {
        MetadataTypeTestUtils.verify(pseudoColumn);
    }

    // ----------------------------------------------------------------------------------------------------- superTables
    private static void superTables(final Context context, final List<? extends SuperTable> superTables)
            throws SQLException {
        assertThat(superTables).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(superTables).doesNotHaveDuplicates();
        }
        for (final var superTable : superTables) {
            superTable(context, superTable);
        }
    }

    private static void superTable(final Context context, final SuperTable superTable) throws SQLException {
        MetadataTypeTestUtils.verify(superTable);
    }

    // ------------------------------------------------------------------------------------------------- tablePrivileges
    private static void tablePrivileges(final Context context, final List<? extends TablePrivilege> tablePrivileges)
            throws SQLException {
        assertThat(tablePrivileges).isNotNull().doesNotContainNull();
        if (!databaseProductName(context).equals(DatabaseProductNames.MY_SQL)) {
            assertThat(tablePrivileges).doesNotHaveDuplicates();
        }
        assertThat(tablePrivileges).satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(
                        TablePrivilege.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                l -> assertThat(l).isSortedAccordingTo(TablePrivilege.comparing(context, Comparator.naturalOrder()))
        );
        for (final var tablePrivilege : tablePrivileges) {
            tablePrivilege(context, tablePrivilege);
        }
    }

    private static void tablePrivilege(final Context context, final TablePrivilege tablePrivilege) throws SQLException {
        MetadataTypeTestUtils.verify(tablePrivilege);
    }

    // ------------------------------------------------------------------------------------------------------ tableTypes
    private static void tableTypes(final Context context, final List<? extends TableType> tableTypes)
            throws SQLException {
        assertThat(tableTypes).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(tableTypes).doesNotContainNull();
        }
        if (true) {
            assertThat(tableTypes).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(TableType.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(TableType.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var tableType : tableTypes) {
            tableType(context, tableType);
        }
    }

    private static void tableType(final Context context, final TableType tableType) throws SQLException {
        MetadataTypeTestUtils.verify(tableType);
        {
            assertThat(tableType.getTableType()).isNotNull();
        }
    }

    // -------------------------------------------------------------------------------------------------------- typeInfo
    private static void typeInfo(final Context context, final List<? extends TypeInfo> typeInfo) throws SQLException {
        assertThat(typeInfo).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(typeInfo).doesNotHaveDuplicates();
        }
        if (!databaseProductName(context).equals(DatabaseProductNames.MY_SQL) &&
            !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            assertThat(typeInfo).isSortedAccordingTo(TypeInfo.comparing(context));
        }
        for (final var typeInfo_ : typeInfo) {
            typeInfo(context, typeInfo_);
        }
    }

    private static void typeInfo(final Context context, final TypeInfo typeInfo) throws SQLException {
        MetadataTypeTestUtils.verify(typeInfo);
        {
            assertThat(typeInfo.getTypeName()).isNotNull();
            //assertDoesNotThrow(() -> JDBCType.valueOf(typeInfo.getDataType())); // mssqlserver
            assertDoesNotThrow(() -> TypeInfo.Nullable.valueOfFieldValue(typeInfo.getNullable()));
            assertDoesNotThrow(() -> TypeInfo.Searchable.valueOfFieldValue(typeInfo.getSearchable()));
        }
        {
            final var value = TypeInfo.Nullable.valueOfFieldValue(typeInfo.getNullable());
        }
        {
            final var value = TypeInfo.Searchable.valueOfFieldValue(typeInfo.getSearchable());
        }
    }

    // ------------------------------------------------------------------------------------------------------------ UDTs
    private static void udts(final Context context, final List<? extends UDT> udts) throws SQLException {
        assertThat(udts).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(udts).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(udts).doesNotContainNull().satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(UDT.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(UDT.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var udt : udts) {
            udt(context, udt);
        }
    }

    private static void udt(final Context context, final UDT udt) throws SQLException {
        MetadataTypeTestUtils.verify(udt);
        {
            assertThat(udt.getTypeName()).isNotNull();
            assertThat(udt.getDataType()).isIn(Types.JAVA_OBJECT, Types.STRUCT, Types.DISTINCT);
            assertDoesNotThrow(() -> JDBCType.valueOf(udt.getDataType()));
        }
        // -------------------------------------------------------------------------------------------------- attributes
        try {
            final var attributes = context.getAttributes(udt, "%");
            for (final var attribute : attributes) {
                assertThat(attribute).matches(a -> Attribute.IS_OF.test(a, udt));
            }
            attributes(context, attributes);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(udt);
            superTypes(context, superTypes);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            // empty
        }
    }

    // -------------------------------------------------------------------------------------------------- versionColumns
    private static void versionColumns(final Context context, final List<? extends VersionColumn> versionColumns)
            throws SQLException {
        assertThat(versionColumns).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(versionColumns).doesNotHaveDuplicates();
        }
        if (true) {
//            assertThat(versionColumns).satisfiesAnyOf(l -> {
////                 getVersionColumns() are unordered.
//            });
        }
        for (final var versionColumn : versionColumns) {
            versionColumn(context, versionColumn);
        }
    }

    private static void versionColumn(final Context context, final VersionColumn versionColumn) throws SQLException {
        MetadataTypeTestUtils.verify(versionColumn);
        assertDoesNotThrow(() -> JDBCType.valueOf(versionColumn.getDataType()));
        assertDoesNotThrow(() -> VersionColumn.PseudoColumn.valueOfFieldValue(versionColumn.getPseudoColumn()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ContextTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
