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
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        databaseProductName = context.metadata.getDatabaseProductName();
        if (true) {
            context.acceptValues((k, v) -> {
                log.debug("{}: {}", k, v);
            });
            return;
        }
        log.info("databaseProductName: {}", databaseProductName);
        log.info("databaseProductVersion: {}", context.metadata.getDatabaseProductVersion());
        log.info("databaseMajorVersion: {}", context.metadata.getDatabaseMajorVersion());
        log.info("databaseMinorVersion: {}", context.metadata.getDatabaseMinorVersion());
        log.info("driverName: {}", context.metadata.getDriverName());
        log.info("driverVersion: {}", context.metadata.getDriverVersion());
        log.info("driverMajorVersion: {}", context.metadata.getDriverMajorVersion());
        log.info("driverMinorVersion: {}", context.metadata.getDriverMinorVersion());
        log.info("catalogSeparator: {}", context.metadata.getCatalogSeparator());
        log.info("catalogTerm: {}", context.metadata.getCatalogTerm());
        log.info("schemaTerm: {}", context.metadata.getSchemaTerm());
    }

    static String name(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.metadata.getDatabaseProductName() + '_' +
               context.metadata.getDatabaseProductVersion() + '_' +
               context.metadata.getDatabaseMajorVersion() + '_' +
               context.metadata.getDatabaseMinorVersion() + '_' +
               context.metadata.getDriverName() + '_' +
               context.metadata.getDriverVersion() + '_' +
               context.metadata.getDriverMajorVersion() + '_' +
               context.metadata.getDriverMinorVersion();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static String databaseProductName;

    private static InvocationHandler proxy(final InvocationHandler handler) {
        return (p, m, args) -> {
            try {
                return handler.invoke(p, m, args);
            } catch (final Throwable t) {
                log.error("failed to invoke {}.{}({})", p, m.getName(), args, t);
                throw t;
            }
        };
    }

    private static void acceptProxy(final Context context, final Consumer<? super Context> consumer) {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            final var validator = factory.getValidator();
            try (var unloaded = new ByteBuddy()
                    .subclass(Context.class)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(proxy(new ValidationInvocationHandler(context, validator))))
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
        acceptProxy(context, p -> {
            try {
                test_(p);
            } catch (final SQLException sqle) {
                throw new RuntimeException("failed to test", sqle);
            }
        });
    }

    private static void test_(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        info(context);
        // ---------------------------------------------------------------------------------------------------- catalogs
        try {
            final var catalogs = context.getCatalogs();
            catalogs(context, catalogs);
        } catch (final SQLException sqle) {
            log.error("failed", sqle);
        }
        // -------------------------------------------------------------------------------------------- clientProperties
        if (!databaseProductName.equals(DatabaseProductNames.SQ_LITE)) {
            final var clientInfoProperties = context.getClientInfoProperties();
            clientInfoProperties(context, clientInfoProperties);
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
        // --------------------------------------------------------------------------------------------------- functions
        // --------------------------------------------------------------------------------------------- functionColumns
        if (!databaseProductName.equals(DatabaseProductNames.SQ_LITE)) {
            final var functions = context.getFunctions(null, null, "%");
            functions(context, functions);
        }
        // --------------------------------------------------------------------------------- procedures/procedureColumns
        if (true) {
            final var procedures = context.getProcedures(null, null, "%");
            procedures(context, procedures);
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        if (true) {
            final var schemas = context.getSchemas();
            schemas(context, schemas);
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        if (!databaseProductName.equals(DatabaseProductNames.SQ_LITE)) {
            final var schemas = context.getSchemas((String) null, null);
            schemas(context, schemas);
        }
        // -------------------------------------------------------------------------------------------------- tableTypes
        if (true) {
            final var tableTypes = context.getTableTypes();
            tableTypes(context, tableTypes);
        }
        // ------------------------------------------------------------------------------------------------------ tables
        if (true) {
            final var tables = context.getTables(null, null, "%", null);
            tables(context, tables);
        }
        // ---------------------------------------------------------------------------------------------------- typeInfo
        if (true) {
            final var typeInfo = context.getTypeInfo();
            typeInfo(context, typeInfo);
        }
        // -------------------------------------------------------------------------------------------------------- udts
        if (true) {
            final var udts = context.getUDTs(null, null, "%", null);
            udts(context, udts);
        }
        // -------------------------------------------------------------------------------------------- numericFunctions
        if (true) {
            final var numericFunctions = context.getNumericFunctions();
        }
        // ---------------------------------------------------------------------------------------------- getSQLKeywords
        if (true) {
            final var SQLKeywords = context.getSQLKeywords();
        }
        // ------------------------------------------------------------------------------------------ getStringFunctions
        if (true) {
            final var stringFunctions = context.getStringFunctions();
        }
        // ------------------------------------------------------------------------------------------ getSystemFunctions
        if (true) {
            final var systemFunctions = context.getSystemFunctions();
        }
        // ---------------------------------------------------------------------------------------- getTimeDateFunctions
        if (true) {
            final var timeDateFunction = context.getTimeDateFunctions();
        }
    }

    // ------------------------------------------------------------------------------------------------------ attributes
    static void attributes(final Context context, final List<? extends Attribute> attributes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attributes, "attributes is null");
        assertThat(attributes).doesNotContainNull();
        if (true) {
            assertThat(attributes).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(attributes).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Attribute.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(Attribute.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var attribute : attributes) {
            attribute(context, attribute);
        }
    }

    static void attribute(final Context context, final Attribute attribute) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attribute, "attribute is null");
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
    static void bestRowIdentifier(final Context context, final List<? extends BestRowIdentifier> bestRowIdentifier)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(bestRowIdentifier, "bestRowIdentifier is null");
        assertThat(bestRowIdentifier).doesNotContainNull();
        if (true) {
            assertThat(bestRowIdentifier).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(bestRowIdentifier).isSortedAccordingTo(BestRowIdentifier.COMPARING_SCOPE);
        }
        for (final var bestRowIdentifier_ : bestRowIdentifier) {
            bestRowIdentifier(context, bestRowIdentifier_);
        }
    }

    static void bestRowIdentifier(final Context context, final BestRowIdentifier bestRowIdentifier)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(bestRowIdentifier, "bestRowIdentifier is null");
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
    static void catalogs(final Context context, final List<? extends Catalog> catalogs) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalogs, "catalogs is null");
        assertThat(catalogs).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(catalogs).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(catalogs).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Catalog.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(Catalog.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var catalog : catalogs) {
            catalog(context, catalog);
        }
    }

    static void catalog(final Context context, final Catalog catalog) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalog, "catalog is null");
        MetadataTypeTestUtils.verify(catalog);
        // -------------------------------------------------------------------------------------------------- procedures
        try {
            final var procedures = context.getProcedures(catalog, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            log.error("failed", sqle);
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        try {
            final var schemas = context.getSchemas(catalog, "%");
            if (schemas.isEmpty()) {
                schemas.add(Schema.of(null, null));
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            log.error("failed: getSchemas", sqle);
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(catalog.getTableCat(), "%", "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            log.error("failed", sqle);
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(catalog.getTableCat(), "%", "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            log.error("failed", sqle);
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        if (true) {
            try {
                final var tablePrivileges = context.getTablePrivileges(
                        catalog.getTableCat(),
                        "%",
                        "%"
                );
                tablePrivileges(context, tablePrivileges);
            } catch (final SQLException sqle) {
                log.error("failed", sqle);
            }
        }
    }

    // -------------------------------------------------------------------------------------------- clientInfoProperties
    private static void clientInfoProperties(final Context context,
                                             final List<? extends ClientInfoProperty> clientInfoProperties)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(clientInfoProperties, "clientInfoProperties is null");
        assertThat(clientInfoProperties).doesNotHaveDuplicates();
        for (final var clientInfoProperty : clientInfoProperties) {
            clientInfoProperty(context, clientInfoProperty);
        }
    }

    private static void clientInfoProperty(final Context context, final ClientInfoProperty clientInfoProperty)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(clientInfoProperty, "clientInfoProperty is null");
        MetadataTypeTestUtils.verify(clientInfoProperty);
    }

    // --------------------------------------------------------------------------------------------------------- columns
    static void columns(final Context context, final List<? extends Column> columns) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columns, "columns is null");
        assertThat(columns).doesNotHaveDuplicates();
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Column.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Column.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var column : columns) {
            column(context, column);
        }
    }

    static void column(final Context context, final Column column) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(column, "column is null");
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
    static void columnPrivileges(final Context context, final List<? extends ColumnPrivilege> columnPrivileges)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columnPrivileges, "columnPrivileges is null");
        assertThat(columnPrivileges)
                .doesNotHaveDuplicates()
                .satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.LEXICOGRAPHIC_ORDER)
                );
        for (final var columnPrivilege : columnPrivileges) {
            columnPrivilege(context, columnPrivilege);
        }
    }

    static void columnPrivilege(final Context context, final ColumnPrivilege columnPrivilege) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columnPrivilege, "columnPrivilege is null");
        MetadataTypeTestUtils.verify(columnPrivilege);
    }

    // -------------------------------------------------------------------------------------------------- crossReference
    static void crossReference(final Context context, final List<CrossReference> crossReference) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(crossReference, "crossReference is null");
        assertThat(crossReference).doesNotHaveDuplicates()
                .satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(CrossReference.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(CrossReference.LEXICOGRAPHIC_ORDER)
                );
        for (final var v : crossReference) {
            crossReference(context, v);
        }
    }

    static void crossReference(final Context context, final CrossReference crossReference) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(crossReference, "crossReference is null");
        MetadataTypeTestUtils.verify(crossReference);
    }

    // ---------------------------------------------------------------------------------------------------- exportedKeys
    private static void exportedKeys(final Context context, final List<? extends ExportedKey> exportedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKeys, "exportedKeys is null");
        assertThat(exportedKeys)
                .doesNotHaveDuplicates()
                .satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ExportedKey.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ExportedKey.LEXICOGRAPHIC_ORDER)
                );
        for (final var exportedKey : exportedKeys) {
            exportedKey(context, exportedKey);
        }
    }

    private static void exportedKey(final Context context, final ExportedKey exportedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKey, "exportedKey is null");
        MetadataTypeTestUtils.verify(exportedKey);
    }

    // ------------------------------------------------------------------------------------------------------- functions
    private static void functions(final Context context, final List<? extends Function> functions) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functions, "functions is null");
        if (!databaseProductName.equals(DatabaseProductNames.POSTGRE_SQL)) {
            assertThat(functions)
                    .doesNotContainNull()
                    .doesNotHaveDuplicates();
        }
        if (!databaseProductName.equals(DatabaseProductNames.MARIA_DB) &&
            !databaseProductName.equals(DatabaseProductNames.POSTGRE_SQL) &&
            !databaseProductName.equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            // https://github.com/microsoft/mssql-jdbc/issues/2321
            assertThat(functions).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Function.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(Function.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var function : functions) {
            function(context, function);
        }
    }

    private static void function(final Context context, final Function function) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        MetadataTypeTestUtils.verify(function);
        try {
            final var functionColumns = context.getFunctionColumns(function, "%");
            functionColumns(context, functionColumns);
        } catch (final SQLException sqle) {
            log.error("failed to getFunctionColumns for {}", function, sqle);
        }
    }

    // ------------------------------------------------------------------------------------------------- functionColumns
    private static void functionColumns(final Context context, final List<? extends FunctionColumn> functionColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionColumns, "functionColumns is null");
        if (!databaseProductName.equals(DatabaseProductNames.POSTGRE_SQL)) {
            assertThat(functionColumns)
                    .doesNotHaveDuplicates();
        }
        if (!databaseProductName.equals(DatabaseProductNames.POSTGRE_SQL)) {
            assertThat(functionColumns).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(FunctionColumn.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(FunctionColumn.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var functionColumn : functionColumns) {
            functionColumn(context, functionColumn);
        }
    }

    private static void functionColumn(final Context context, final FunctionColumn functionColumn)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionColumn, "functionColumn is null");
        MetadataTypeTestUtils.verify(functionColumn);
        final var columnType = FunctionColumn.ColumnType.valueOfFieldValue(functionColumn.getColumnType());
    }

    // ---------------------------------------------------------------------------------------------------- importedKeys
    static void importedKeys(final Context context, final List<? extends ImportedKey> importedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKeys, "importedKeys is null");
        assertThat(importedKeys)
                .doesNotHaveDuplicates()
                .satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ImportedKey.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ImportedKey.LEXICOGRAPHIC_ORDER)
                );
        for (final var importedKey : importedKeys) {
            importedKey(context, importedKey);
        }
    }

    static void importedKey(final Context context, final ImportedKey importedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKey, "importedKey is null");
        MetadataTypeTestUtils.verify(importedKey);
    }

    // ------------------------------------------------------------------------------------------------------- indexInfo
    static void indexInfo(final Context context, final List<IndexInfo> indexInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(indexInfo, "indexInfo is null");
        assertThat(indexInfo)
                .doesNotHaveDuplicates()
                .satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(IndexInfo.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(IndexInfo.LEXICOGRAPHIC_ORDER)
                );
        for (final var indexInfo_ : indexInfo) {
            indexInfo(context, indexInfo_);
        }
    }

    static void indexInfo(final Context context, final IndexInfo indexInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(indexInfo, "indexInfo is null");
        MetadataTypeTestUtils.verify(indexInfo);
    }

    // ------------------------------------------------------------------------------------------------------ procedures
    private static void procedures(final Context context, final List<? extends Procedure> procedures)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedures, "procedures is null");
        assertThat(procedures)
                .doesNotHaveDuplicates();
        if (!databaseProductName.equals(DatabaseProductNames.HSQL_DATABASE_ENGINE) &&
            !databaseProductName.equals(DatabaseProductNames.MARIA_DB) &&
            !databaseProductName.equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            assertThat(procedures).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Procedure.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(Procedure.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var procedure : procedures) {
            procedure(context, procedure);
        }
    }

    private static void procedure(final Context context, final Procedure procedure) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedure, "procedure is null");
        MetadataTypeTestUtils.verify(procedure);
        try {
            final var procedureColumns = context.getProcedureColumns(procedure, "%");
            procedureColumns(context, procedureColumns);
        } catch (final SQLException sqle) {
            log.error("failed", sqle);
        }
    }

    // ------------------------------------------------------------------------------------------------ procedureColumns
    private static void procedureColumns(final Context context, final List<? extends ProcedureColumn> procedureColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumns, "procedureColumns is null");
        assertThat(procedureColumns).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(procedureColumns).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(procedureColumns).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(ProcedureColumn.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(ProcedureColumn.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var procedureColumn : procedureColumns) {
            procedureColumn(context, procedureColumn);
        }
    }

    private static void procedureColumn(final Context context, final ProcedureColumn procedureColumn)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumn, "procedureColumn is null");
        MetadataTypeTestUtils.verify(procedureColumn);
    }

    // --------------------------------------------------------------------------------------------------------- schemas
    static void schemas(final Context context, final List<? extends Schema> schemas) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schemas, "schemas is null");
        assertThat(schemas).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(schemas).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Schema.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(Schema.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var schema : schemas) {
            schema(context, schema);
        }
    }

    static void schema(final Context context, final Schema schema) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schema, "schema is null");
        MetadataTypeTestUtils.verify(schema);
        // -------------------------------------------------------------------------------------------------- procedures
        if (!databaseProductName.equals(DatabaseProductNames.POSTGRE_SQL)) {
            final var procedures = context.getAllProcedures(schema);
            procedures(context, procedures);
        }
        // ------------------------------------------------------------------------------------------------- superTables
        if (!databaseProductName.equals(DatabaseProductNames.POSTGRE_SQL)) {
            final var superTables = context.getSuperTables(schema, "%");
            superTables(context, superTables);
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        if (true) {
            final var superTypes = context.getSuperTypes(schema, "%");
            superTypes(context, superTypes);
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        if (true) {
            final var tablePrivileges = context.getTablePrivileges(schema, "%");
            tablePrivileges(context, tablePrivileges);
        }
    }

    // ------------------------------------------------------------------------------------------------------ superTypes
    static void superTypes(final Context context, final List<? extends SuperType> superTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTypes, "superTypes is null");
        assertThat(superTypes).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(superTypes).doesNotHaveDuplicates();
        }
        for (final var superType : superTypes) {
            superType(context, superType);
        }
    }

    static void superType(final Context context, final SuperType superType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superType, "superType is null");
        MetadataTypeTestUtils.verify(superType);
        {
            assertThat(superType.getTypeName()).isNotNull();
            assertThat(superType.getSupertypeName()).isNotNull();
        }
    }

    // ---------------------------------------------------------------------------------------------------------- tables
    static void tables(final Context context, final List<? extends Table> tables) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tables, "tables is null");
        assertThat(tables).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(tables).doesNotHaveDuplicates();
        }
        if (!databaseProductName.equals(DatabaseProductNames.APACHE_DERBY) &&
            !databaseProductName.equals(DatabaseProductNames.HSQL_DATABASE_ENGINE) &&
            !databaseProductName.equals(DatabaseProductNames.MARIA_DB) &&
            !databaseProductName.equals(DatabaseProductNames.MY_SQL) &&
            !databaseProductName.equals(DatabaseProductNames.POSTGRE_SQL) &&
            !databaseProductName.equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            // https://jira.mariadb.org/browse/CONJ-1156
            assertThat(tables).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Table.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(Table.LEXICOGRAPHIC_ORDER)
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
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(table, "table is null");
        MetadataTypeTestUtils.verify(table);
        // ------------------------------------------------------------------------------------------- bestRowIdentifier
        for (final BestRowIdentifier.Scope scope : BestRowIdentifier.Scope.values()) {
            for (final boolean nullable : new boolean[] {true, false}) {
                final var bestRowIdentifier = context.getBestRowIdentifier(table, scope.fieldValueAsInt(), nullable);
                bestRowIdentifier(context, bestRowIdentifier);
            }
        }
        // -------------------------------------------------------------------------------------------- columnPrivileges
        if (true) {
            final var columnPrivileges = context.getAllColumnPrivileges(table);
            columnPrivileges(context, columnPrivileges);
        }
        // ------------------------------------------------------------------------------------------------ exportedKeys
        if (true) {
            final var exportedKeys = context.getExportedKeys(table);
            exportedKeys(context, exportedKeys);
        }
        // ------------------------------------------------------------------------------------------------ importedKeys
        if (true) {
            final var importedKeys = context.getImportedKeys(table);
            importedKeys(context, importedKeys);
        }
        // --------------------------------------------------------------------------------------------------- indexInfo
        if (true) {
            for (final boolean unique : new boolean[] {true, false}) {
                for (final boolean approximate : new boolean[] {true, false}) {
                    final var indexInfo = context.getIndexInfo(table, unique, approximate);
                    indexInfo(context, indexInfo);
                }
            }
        }
        // ------------------------------------------------------------------------------------------------- primaryKeys
        if (true) {
            final var primaryKeys = context.getPrimaryKeys(table);
            primaryKeys(context, primaryKeys);
        }
        // ----------------------------------------------------------------------------------------------- pseudoColumns
        if (!databaseProductName.equals(DatabaseProductNames.HSQL_DATABASE_ENGINE) &&
            !databaseProductName.equals(DatabaseProductNames.SQ_LITE)) {
            final var pseudoColumns = context.getPseudoColumns(table, "%");
            pseudoColumns(context, pseudoColumns);
        }
        // ------------------------------------------------------------------------------------------------- superTables
        if (true) {
            final var superTables = context.getSuperTables(table);
            superTables(context, superTables);
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        if (true) {
            final var tablePrivileges = context.getTablePrivileges(table);
            tablePrivileges(context, tablePrivileges);
        }
        // ---------------------------------------------------------------------------------------------- versionColumns
        if (true) {
            final var versionColumns = context.getVersionColumns(table);
            versionColumns(context, versionColumns);
        }
    }

    // ----------------------------------------------------------------------------------------------------- primaryKeys
    private static void primaryKeys(final Context context, final List<? extends PrimaryKey> primaryKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKeys, "primaryKeys is null");
        assertThat(primaryKeys).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(primaryKeys).doesNotHaveDuplicates();
        }
        if (!databaseProductName.equals(DatabaseProductNames.POSTGRE_SQL)) {
            assertThat(primaryKeys).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(PrimaryKey.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(PrimaryKey.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var primaryKey : primaryKeys) {
            primaryKey(context, primaryKey);
        }
    }

    private static void primaryKey(final Context context, final PrimaryKey primaryKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKey, "primaryKey is null");
        MetadataTypeTestUtils.verify(primaryKey);
    }

    // --------------------------------------------------------------------------------------------------- pseudoColumns
    private static void pseudoColumns(final Context context, final List<? extends PseudoColumn> pseudoColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumns, "pseudoColumns is null");
        assertThat(pseudoColumns).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(pseudoColumns).doesNotContainNull();
        }
        if (true) {
            assertThat(pseudoColumns).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(PseudoColumn.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(PseudoColumn.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var pseudoColumn : pseudoColumns) {
            pseudoColumn(context, pseudoColumn);
        }
    }

    static void pseudoColumn(final Context context, final PseudoColumn pseudoColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumn, "pseudoColumn is null");
        MetadataTypeTestUtils.verify(pseudoColumn);
    }

    // ----------------------------------------------------------------------------------------------------- superTables
    static void superTables(final Context context, final List<? extends SuperTable> superTables) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTables, "superTables is null");
        assertThat(superTables).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(superTables).doesNotHaveDuplicates();
        }
        for (final var superTable : superTables) {
            superTable(context, superTable);
        }
    }

    static void superTable(final Context context, final SuperTable superTable) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTable, "superTable is null");
        MetadataTypeTestUtils.verify(superTable);
    }

    // ------------------------------------------------------------------------------------------------- tablePrivileges
    static void tablePrivileges(final Context context, final List<? extends TablePrivilege> tablePrivileges)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tablePrivileges, "tablePrivileges is null");
        assertThat(tablePrivileges).isNotNull().doesNotContainNull();
        if (!databaseProductName.equals(DatabaseProductNames.MY_SQL)) {
            assertThat(tablePrivileges).doesNotHaveDuplicates();
        }
        if (!databaseProductName.equals(DatabaseProductNames.HSQL_DATABASE_ENGINE)) {
            assertThat(tablePrivileges).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(TablePrivilege.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(TablePrivilege.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var tablePrivilege : tablePrivileges) {
            tablePrivilege(context, tablePrivilege);
        }
    }

    static void tablePrivilege(final Context context, final TablePrivilege tablePrivilege) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tablePrivilege, "tablePrivilege is null");
        MetadataTypeTestUtils.verify(tablePrivilege);
    }

    // ------------------------------------------------------------------------------------------------------ tableTypes
    static void tableTypes(final Context context, final List<? extends TableType> tableTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableTypes, "tableTypes is null");
        assertThat(tableTypes).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(tableTypes).doesNotContainNull();
        }
        if (true) {
            assertThat(tableTypes).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(TableType.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(TableType.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var tableType : tableTypes) {
            tableType(context, tableType);
        }
    }

    static void tableType(final Context context, final TableType tableType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableType, "tableType is null");
        MetadataTypeTestUtils.verify(tableType);
        {
            assertThat(tableType.getTableType()).isNotNull();
        }
    }

    // -------------------------------------------------------------------------------------------------------- typeInfo
    private static void typeInfo(final Context context, final List<? extends TypeInfo> typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        assertThat(typeInfo).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(typeInfo).doesNotHaveDuplicates();
        }
        if (!databaseProductName.equals(DatabaseProductNames.MY_SQL) &&
            !databaseProductName.equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            assertThat(typeInfo).isSortedAccordingTo(TypeInfo.COMPARING_DATA_TYPE);
        }
        for (final var typeInfo_ : typeInfo) {
            typeInfo(context, typeInfo_);
        }
    }

    static void typeInfo(final Context context, final TypeInfo typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
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
    static void udts(final Context context, final List<? extends UDT> udts) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udts, "udts is null");
        assertThat(udts).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(udts).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(udts).doesNotContainNull().satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(UDT.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(UDT.LEXICOGRAPHIC_ORDER)
            );
        }
        for (final var udt : udts) {
            udt(context, udt);
        }
    }

    static void udt(final Context context, final UDT udt) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udt, "udt is null");
        MetadataTypeTestUtils.verify(udt);
        {
            assertThat(udt.getTypeName()).isNotNull();
//            assertThat(udt.getClassName()).isNotNull();
            assertThat(udt.getDataType()).isIn(Types.JAVA_OBJECT, Types.STRUCT, Types.DISTINCT);
            assertDoesNotThrow(() -> JDBCType.valueOf(udt.getDataType()));
        }
        // -------------------------------------------------------------------------------------------------- attributes
        if (true) {
            final var attributes = context.getAttributes(udt, "%");
            attributes(context, attributes);
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        if (true) {
            final var superTypes = context.getSuperTypes(udt);
            superTypes(context, superTypes);
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
            assertThat(versionColumns).satisfiesAnyOf(l -> {
            });
        }
        for (final var versionColumn : versionColumns) {
            versionColumn(context, versionColumn);
        }
    }

    private static void versionColumn(final Context context, final VersionColumn versionColumn) throws SQLException {
        {
            assertDoesNotThrow(() -> JDBCType.valueOf(versionColumn.getDataType()));
            assertThat(versionColumn.getTypeName()).isNotNull();
            assertDoesNotThrow(() -> VersionColumn.PseudoColumn.valueOfFieldValue(versionColumn.getPseudoColumn()));
        }
        MetadataTypeTestUtils.verify(versionColumn);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ContextTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
