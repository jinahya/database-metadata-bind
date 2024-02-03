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

import com.github.jinahya.database.metadata.bind.VersionColumn.PseudoColumn;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.ReflectionUtils;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTestUtils {

    private static final boolean READ_TABLE_PRIVILEGES = false;

    private static final boolean READ_COLUMN_PRIVILEGES = false;

    static void info(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        databaseProductName = context.databaseMetaData.getDatabaseProductName();
        log.info("databaseProductName: {}", databaseProductName);
        log.info("databaseProductVersion: {}", context.databaseMetaData.getDatabaseProductVersion());
        log.info("databaseMajorVersion: {}", context.databaseMetaData.getDatabaseMajorVersion());
        log.info("databaseMinorVersion: {}", context.databaseMetaData.getDatabaseMinorVersion());
        log.info("driverName: {}", context.databaseMetaData.getDriverName());
        log.info("driverVersion: {}", context.databaseMetaData.getDriverVersion());
        log.info("driverMajorVersion: {}", context.databaseMetaData.getDriverMajorVersion());
        log.info("driverMinorVersion: {}", context.databaseMetaData.getDriverMinorVersion());
        log.info("catalogSeparator: {}", context.databaseMetaData.getCatalogSeparator());
        log.info("catalogTerm: {}", context.databaseMetaData.getCatalogTerm());
        log.info("schemaTerm: {}", context.databaseMetaData.getSchemaTerm());
    }

    private static <T> T common(final T value) {
        Objects.requireNonNull(value, "value is null");
        {
            final var string = value.toString();
        }
        {
            final var hashCode = value.hashCode();
        }
        if (value instanceof MetadataType) {
            final var unmappedValues = ((MetadataType) value).getUnmappedValues();
            if (!unmappedValues.isEmpty()) {
                log.warn("unmapped values of {}: {}", value.getClass().getSimpleName(), unmappedValues);
            }
        }
        // .toBuilder().build()
        ReflectionUtils.findMethod(value.getClass(), "toBuilder")
                .map(m -> {
                    try {
                        return m.invoke(value);
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    }
                })
                .flatMap(b -> ReflectionUtils.findMethod(b.getClass(), "build")
                        .map(m -> {
                            try {
                                return m.invoke(b);
                            } catch (final ReflectiveOperationException row) {
                                throw new RuntimeException(row);
                            }
                        })
                );
        return value;
    }

    private static void thrown(final String message, final Throwable throwable) {
        Objects.requireNonNull(message, "message is null");
        Objects.requireNonNull(throwable, "throwable is null");
        if (throwable instanceof SQLFeatureNotSupportedException) {
            return;
        }
        log.error("{}", message, throwable);
    }

    // -----------------------------------------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------------------------------------
    private static String databaseProductName;

    static void test(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        {
            databaseProductName = context.databaseMetaData.getDatabaseProductName();
            info(context);
        }
        {
            final List<Catalog> catalogs;
            try {
                catalogs = context.getCatalogs();
                if (catalogs.isEmpty()) {
                    log.info("no catalogs retrieved");
                    catalogs.add(Catalog.newVirtualInstance());
                }
                catalogs(context, catalogs);
            } catch (final SQLException sqle) {
                thrown("failed; getCatalogs", sqle);
            }
        }
        try {
            final var clientInfoProperties = context.getClientInfoProperties();
            clientInfoProperties(context, clientInfoProperties);
        } catch (final SQLException sqle) {
            thrown("failed; getClientInfoProperties", sqle);
        }
        try {
            final var functions = context.getFunctions(null, null, "%");
            functions(context, functions);
        } catch (final SQLException sqle) {
            thrown("failed; getFunctions", sqle);
        }
        try {
            final var procedures = context.getProcedures(null, null, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            thrown("failed; getProcedures", sqle);
        }
        try {
            final var schemas = context.getSchemas();
            if (schemas.isEmpty()) {
                schemas.add(Schema.newVirtualInstance());
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            thrown("failed; getSchemas", sqle);
        }
        try {
            final var schemas = context.getSchemas((String) null, null);
            if (schemas.isEmpty()) {
                schemas.add(Schema.newVirtualInstance());
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            thrown("failed; getSchemas", sqle);
        }
        try {
            final var tableTypes = context.getTableTypes();
            tableTypes(context, tableTypes);
        } catch (final SQLException sqle) {
            thrown("failed; getTableTypes", sqle);
        }
        try {
            final var tables = context.getTables(null, null, "%", null);
            tables(context, tables);
        } catch (final SQLException sqle) {
            thrown("failed; getTables", sqle);
        }
        try {
            final var typeInfo = context.getTypeInfo();
            typeInfo(context, typeInfo);
        } catch (final SQLException sqle) {
            thrown("failed; getTypeInfo", sqle);
        }
        try {
            final var udts = context.getUDTs(null, null, "%", null);
            udts(context, udts);
        } catch (final SQLException sqle) {
            thrown("failed; getUDTs", sqle);
        }
        try {
            final Set<String> NumericFunctions = context.getNumericFunctions();
            log.debug("numeric functions: {}", NumericFunctions);
        } catch (final SQLException sqle) {
            thrown("failed; getNumericFunctions", sqle);
        }
        try {
            final Set<String> SQLKeywords = context.getSQLKeywords();
            log.debug("sql keywords: {}", SQLKeywords);
        } catch (final SQLException sqle) {
            thrown("failed; getSQLKeywords", sqle);
        }
        try {
            final Set<String> results = context.getStringFunctions();
            log.debug("string functions: {}", results);
        } catch (final SQLException sqle) {
            thrown("failed; getStringFunctions", sqle);
        }
        try {
            final Set<String> results = context.getSystemFunctions();
            log.debug("system functions: {}", results);
        } catch (final SQLException sqle) {
            thrown("failed; getSystemFunctions", sqle);
        }
        try {
            final Set<String> results = context.getTimeDateFunctions();
            log.debug("time date functions: {}", results);
        } catch (final SQLException sqle) {
            thrown("failed; getTimeDateFunctions", sqle);
        }
    }

    static void attributes(final Context context, final List<? extends Attribute> attributes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attributes, "attributes is null");
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(attributes)
                        .doesNotContainNull()
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(Attribute.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(Attribute.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        for (final var attribute : attributes) {
            MetadataTypeTestUtils.accessors(attribute);
            attribute(context, attribute);
        }
    }

    static void attribute(final Context context, final Attribute attribute) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attribute, "attribute is null");
        {
            assertThat(attribute.getTypeName()).isNotNull();
            assertThat(attribute.getAttrName()).isNotNull();
            final var dataType = attribute.getDataType();
            assertDoesNotThrow(() -> JDBCType.valueOf(dataType));
            assertThat(attribute.getAttrTypeName()).isNotNull();
            assertDoesNotThrow(() -> Attribute.Nullable.valueOfNullable(attribute.getNullable()));
            assertThat(attribute.getIsNullable()).isNotNull();
        }
        {
            common(attribute);
        }
    }

    static void bestRowIdentifier(final Context context, final List<? extends BestRowIdentifier> bestRowIdentifier)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(bestRowIdentifier, "bestRowIdentifier is null");
        assertThat(bestRowIdentifier).isSortedAccordingTo(BestRowIdentifier.COMPARING_SCOPE);
        assertThat(bestRowIdentifier).isSorted();
        for (final var bestRowIdentifier_ : bestRowIdentifier) {
            MetadataTypeTestUtils.accessors(bestRowIdentifier_);
            bestRowIdentifier(context, bestRowIdentifier_);
        }
    }

    static void bestRowIdentifier(final Context context, final BestRowIdentifier bestRowIdentifier)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(bestRowIdentifier, "bestRowIdentifier is null");
        {
            final var scope = bestRowIdentifier.getScope();
            assertDoesNotThrow(() -> BestRowIdentifier.Scope.valueOfScope(scope));
            assertThat(bestRowIdentifier.getColumnName()).isNotNull();
            assertDoesNotThrow(() -> JDBCType.valueOf(bestRowIdentifier.getDataType()));
            assertThat(bestRowIdentifier.getTypeName()).isNotNull();
            final int pseudoColumn = bestRowIdentifier.getPseudoColumn();
            assertDoesNotThrow(() -> BestRowIdentifier.PseudoColumn.valueOfPseudoColumn(pseudoColumn));
        }
        {
            common(bestRowIdentifier);
        }
    }

    static void catalogs(final Context context, final List<? extends Catalog> catalogs) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalogs, "catalogs is null");
        assertThat(catalogs).doesNotHaveDuplicates().satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(Catalog.CASE_INSENSITIVE_ORDER),
                l -> assertThat(l).isSortedAccordingTo(Catalog.LEXICOGRAPHIC_ORDER)
        );
        for (final var catalog : catalogs) {
            MetadataTypeTestUtils.accessors(catalog);
            catalog(context, catalog);
        }
    }

    static void catalog(final Context context, final Catalog catalog) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalog, "catalog is null");
        {
            assertThat(catalog.getTableCat()).isNotNull();
        }
        common(catalog);
        try {
            final var schemas = context.getSchemas(catalog, "%");
            {
                final var databaseProductNames = Set.of(
                        // https://sourceforge.net/p/hsqldb/bugs/1671/
                        DatabaseProductNames.HSQL_DATABASE_ENGINE
                );
                if (!databaseProductNames.contains(databaseProductName)) {
                    assertThat(schemas).satisfiesAnyOf(
                            l -> assertThat(l).isSortedAccordingTo(Schema.CASE_INSENSITIVE_ORDER),
                            l -> assertThat(l).isSortedAccordingTo(Schema.LEXICOGRAPHIC_ORDER)
                    );
                }
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            thrown("failed: getSchemas", sqle);
        }
        if (READ_TABLE_PRIVILEGES) {
            try {
                final var tablePrivileges = context.getTablePrivileges(
                        Optional.ofNullable(catalog.getTableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY),
                        "%",
                        "%"
                );
                tablePrivileges(context, tablePrivileges);
            } catch (final SQLException sqle) {
                thrown("failed; getTablePrivileges", sqle);
            }
        }
    }

    private static void clientInfoProperties(final Context context,
                                             final List<? extends ClientInfoProperty> clientInfoProperties)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(clientInfoProperties, "clientInfoProperties is null");
        assertThat(clientInfoProperties)
                .doesNotHaveDuplicates();
        for (final var clientInfoProperty : clientInfoProperties) {
            MetadataTypeTestUtils.accessors(clientInfoProperty);
            clientInfoProperty(context, clientInfoProperty);
        }
    }

    private static void clientInfoProperty(final Context context, final ClientInfoProperty clientInfoProperty)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(clientInfoProperty, "clientInfoProperty is null");
        common(clientInfoProperty);
    }

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
            MetadataTypeTestUtils.accessors(column);
            column(context, column);
        }
    }

    static void column(final Context context, final Column column) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(column, "column is null");
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
            final var value = Column.Nullable.valueOfNullable(column.getNullable());
        }
    }

    static void columnPrivileges(final Context context, final List<? extends ColumnPrivilege> columnPrivileges)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columnPrivileges, "columnPrivileges is null");
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.POSTGRE_SQL
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columnPrivileges).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var columnPrivilege : columnPrivileges) {
            MetadataTypeTestUtils.accessors(columnPrivilege);
            columnPrivilege(context, columnPrivilege);
        }
    }

    static void columnPrivilege(final Context context, final ColumnPrivilege columnPrivilege) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columnPrivilege, "columnPrivilege is null");
        common(columnPrivilege);
    }

    static void crossReference(final Context context, List<? extends CrossReference> crossReference)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(crossReference, "crossReference is null");
        assertThat(crossReference).satisfiesAnyOf(
                cr -> assertThat(cr).isSortedAccordingTo(CrossReference.CASE_INSENSITIVE_ORDER),
                cr -> assertThat(cr).isSortedAccordingTo(CrossReference.LEXICOGRAPHIC_ORDER)
        );
        for (final var crossReference_ : crossReference) {
            MetadataTypeTestUtils.accessors(crossReference_);
            crossReference(context, crossReference_);
        }
    }

    static void crossReference(final Context context, final CrossReference crossReference) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(crossReference, "crossReference is null");
        common(crossReference);
    }

    static void exportedKeys(final Context context, final List<? extends ExportedKey> exportedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKeys, "exportedKeys is null");
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(exportedKeys).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ExportedKey.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ExportedKey.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var exportedKey : exportedKeys) {
            MetadataTypeTestUtils.accessors(exportedKey);
            exportedKey(context, exportedKey);
        }
    }

    private static void exportedKey(final Context context, final ExportedKey exportedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKey, "exportedKey is null");
        common(exportedKey);
    }

    private static void functions(final Context context, final List<? extends Function> functions) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functions, "functions is null");
        assertThat(functions).doesNotHaveDuplicates().satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(Function.CASE_INSENSITIVE_ORDER),
                l -> assertThat(l).isSortedAccordingTo(Function.LEXICOGRAPHIC_ORDER)
        );
        for (final var function : functions) {
            MetadataTypeTestUtils.accessors(function);
            function(context, function);
        }
    }

    private static void function(final Context context, final Function function) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        common(function);
        try {
            final var functionColumns = context.getFunctionColumns(function, "%");
            functionColumns(context, function, functionColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getFunctionColumns", sqle);
        }
    }

    private static void functionColumns(final Context context, final Function function,
                                        final List<? extends FunctionColumn> functionColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        Objects.requireNonNull(functionColumns, "functionColumns is null");
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.POSTGRE_SQL
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(functionColumns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(FunctionColumn.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(FunctionColumn.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var functionColumn : functionColumns) {
            MetadataTypeTestUtils.accessors(functionColumn);
            functionColumn(context, function, functionColumn);
        }
    }

    private static void functionColumn(final Context context, final Function function,
                                       final FunctionColumn functionColumn)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        Objects.requireNonNull(functionColumn, "functionColumn is null");
        common(functionColumn);
        final var columnType = FunctionColumn.ColumnType.valueOfColumnType(functionColumn.getColumnType());
    }

    static void importedKeys(final Context context, final List<? extends ImportedKey> importedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKeys, "importedKeys is null");
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(importedKeys).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ImportedKey.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ImportedKey.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var importedKey : importedKeys) {
            MetadataTypeTestUtils.accessors(importedKey);
            importedKey(context, importedKey);
        }
    }

    static void importedKey(final Context context, final ImportedKey importedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKey, "importedKey is null");
        common(importedKey);
    }

    static void indexInfo(final Context context, final List<? extends IndexInfo> indexInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(indexInfo, "indexInfo is null");
        {
            final var databaseProductNames = Set.of(
                    ""
                    // https://bugs.mysql.com/bug.php?id=109803
//                    DatabaseProductNames.MY_SQL
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(indexInfo).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(IndexInfo.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(IndexInfo.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var indexInfo_ : indexInfo) {
            MetadataTypeTestUtils.accessors(indexInfo_);
            indexInfo(context, indexInfo_);
        }
    }

    static void indexInfo(final Context context, final IndexInfo indexInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(indexInfo, "indexInfo is null");
        common(indexInfo);
    }

    private static void procedures(final Context context, final List<? extends Procedure> procedures)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedures, "procedures is null");
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.HSQL_DATABASE_ENGINE
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(procedures)
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(Procedure.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(Procedure.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.HSQL_DATABASE_ENGINE
            );
            if (!databaseProductNames.contains(databaseProductName)) {
//                assertThat(procedures)
//                        .extracting(Procedure::getProcedureId)
//                        .satisfiesAnyOf(
//                                l -> assertThat(l).isSortedAccordingTo(ProcedureId.CASE_INSENSITIVE_ORDER),
//                                l -> assertThat(l).isSortedAccordingTo(ProcedureId.LEXICOGRAPHIC_ORDER)
//                        );
            }
        }
        for (final var procedure : procedures) {
            MetadataTypeTestUtils.accessors(procedure);
            procedure(context, procedure);
        }
    }

    private static void procedure(final Context context, final Procedure procedure) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedure, "procedure is null");
        common(procedure);
        try {
            final var procedureColumns = context.getProcedureColumns(procedure, "%");
            assertThat(procedureColumns).doesNotHaveDuplicates();
            procedureColumns(context, procedureColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getProcedureColumns", sqle);
        }
    }

    private static void procedureColumns(final Context context, final List<? extends ProcedureColumn> procedureColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumns, "procedureColumns is null");
        assertThat(procedureColumns)
                .isSortedAccordingTo(ProcedureColumn.CASE_INSENSITIVE_ORDER);
        for (final var procedureColumn : procedureColumns) {
            MetadataTypeTestUtils.accessors(procedureColumn);
            procedureColumn(context, procedureColumn);
        }
    }

    private static void procedureColumn(final Context context, final ProcedureColumn procedureColumn)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumn, "procedureColumn is null");
        common(procedureColumn);
    }

    static void schemas(final Context context, final List<? extends Schema> schemas) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schemas, "schemas is null");
        for (final var schema : schemas) {
            MetadataTypeTestUtils.accessors(schema);
            schema(context, schema);
        }
    }

    static void schema(final Context context, final Schema schema) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schema, "schema is null");
        {
            assertThat(schema.getTableSchem()).isNotNull();
        }
        common(schema);
        try {
            final var superTables = context.getSuperTables(schema, "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            thrown("failed; getSuperTables", sqle);
        }
        try {
            final var superTypes = context.getSuperTypes(schema, "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            thrown("failed: getSuperTypes", sqle);
        }
        if (READ_TABLE_PRIVILEGES) {
            try {
                final var tablePrivileges = context.getTablePrivileges(schema, "%");
                tablePrivileges(context, tablePrivileges);
            } catch (final SQLException sqle) {
                thrown("failed; getTablePrivileges", sqle);
            }
        }
    }

    static void superTypes(final Context context, final List<? extends SuperType> superTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTypes, "superTypes is null");
        for (final var superType : superTypes) {
            MetadataTypeTestUtils.accessors(superType);
            superType(context, superType);
        }
    }

    static void superType(final Context context, final SuperType superType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superType, "superType is null");
        {
            assertThat(superType.getTypeName()).isNotNull();
            assertThat(superType.getSupertypeName()).isNotNull();
        }
        {
            common(superType);
        }
    }

    static void tables(final Context context, final List<? extends Table> tables) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tables, "tables is null");
        {
            final var databaseProductNames = Set.of(
                    // https://sourceforge.net/p/hsqldb/bugs/1672/
                    DatabaseProductNames.HSQL_DATABASE_ENGINE,
                    DatabaseProductNames.MARIA_DB,
                    DatabaseProductNames.POSTGRE_SQL
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(tables).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Table.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Table.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var table : tables) {
            table(context, table);
        }
        for (int i = 0; i < 10 && i < tables.size(); i++) { // table 아 많으면 오래 걸린다.
            final var parent = tables.get(i);
            for (final var fktable : tables) {
                final var crossReference = context.getCrossReference(
                        parent.getTableCat(), parent.getTableSchem(), parent.getTableName(),
                        fktable.getTableCat(), fktable.getTableSchem(), fktable.getTableName());
                crossReference(context, crossReference);
            }
        }
    }

    static void table(final Context context, final Table table) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(table, "table is null");
        {
            assertThat(table.getTableName()).isNotNull();
//            assertThat(table.getTableType()).isNotNull();
        }
        common(table);
        try {
            for (final var scope : BestRowIdentifier.Scope.values()) {
                for (final boolean nullable : new boolean[] {true, false}) {
                    final var bestRowIdentifier =
                            context.getBestRowIdentifier(table, scope.fieldValueAsInt(), nullable);
                    bestRowIdentifier(context, bestRowIdentifier);
                }
            }
        } catch (final SQLException sqle) {
            thrown("failed; getBestRowIdentifier", sqle);
        }
        try {
            final var columns = context.getColumns(table, "%");
            columns(context, columns);
        } catch (final SQLException sqle) {
            thrown("failed; getColumns", sqle);
        }
        if (READ_COLUMN_PRIVILEGES) {
            try {
                final var columnPrivileges = context.getColumnPrivileges(table, "%");
                assertThat(columnPrivileges).doesNotHaveDuplicates();
                columnPrivileges(context, columnPrivileges);
            } catch (final SQLException sqle) {
                thrown("failed; getColumnPrivileges", sqle);
            }
        }
        try {
            final var exportedKeys = context.getExportedKeys(table);
            assertThat(exportedKeys).doesNotHaveDuplicates();
            exportedKeys(context, exportedKeys);
        } catch (final SQLException sqle) {
            thrown("failed; getExportedKeys", sqle);
        }
        try {
            final var importedKeys = context.getImportedKeys(table);
            assertThat(importedKeys).doesNotHaveDuplicates();
            importedKeys(context, importedKeys);
        } catch (final SQLException sqle) {
            thrown("failed; getImportedKeys", sqle);
        }
        try {
            for (final boolean unique : new boolean[] {true, false}) {
                for (final boolean approximate : new boolean[] {true, false}) {
                    final var indexInfo = context.getIndexInfo(table, unique, approximate);
                    indexInfo(context, indexInfo);
                }
            }
        } catch (final SQLException sqle) {
            thrown("failed; getIndexInfo", sqle);
        }
        try {
            final var primaryKeys = context.getPrimaryKeys(table);
            assertThat(primaryKeys).doesNotHaveDuplicates();
            primaryKeys(context, primaryKeys);
        } catch (final SQLException sqle) {
            thrown("failed; getPrimaryKeys", sqle);
        }
        try {
            final var pseudoColumns = context.getPseudoColumns(table, "%");
            assertThat(pseudoColumns).doesNotHaveDuplicates();
            pseudoColumns(context, pseudoColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getPseudoColumns", sqle);
        }
        if (READ_TABLE_PRIVILEGES) {
            try {
                final var tablePrivileges = context.getTablePrivileges(table);
                tablePrivileges(context, tablePrivileges);
            } catch (final SQLException sqle) {
                thrown("failed; getTablePrivileges", sqle);
            }
        }
        try {
            final var versionColumns = context.getVersionColumns(table);
            assertThat(versionColumns).doesNotHaveDuplicates();
            versionColumns(context, versionColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getVersionColumns", sqle);
        }
    }

    static void primaryKeys(final Context context, final List<? extends PrimaryKey> primaryKeys) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKeys, "primaryKeys is null");
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.HSQL_DATABASE_ENGINE // https://sourceforge.net/p/hsqldb/bugs/1673/
                    , DatabaseProductNames.POSTGRE_SQL
                    , DatabaseProductNames.MY_SQL // https://bugs.mysql.com/bug.php?id=109808
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(primaryKeys).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(PrimaryKey.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(PrimaryKey.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var primaryKey : primaryKeys) {
            MetadataTypeTestUtils.accessors(primaryKey);
            primaryKey(context, primaryKey);
        }
    }

    private static void primaryKey(final Context context, final PrimaryKey primaryKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKey, "primaryKey is null");
        common(primaryKey);
    }

    static void pseudoColumns(final Context context,
                              final List<? extends com.github.jinahya.database.metadata.bind.PseudoColumn> pseudoColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumns, "pseudoColumns is null");
        assertThat(pseudoColumns).satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(
                        com.github.jinahya.database.metadata.bind.PseudoColumn.CASE_INSENSITIVE_ORDER),
                l -> assertThat(l).isSortedAccordingTo(
                        com.github.jinahya.database.metadata.bind.PseudoColumn.LEXICOGRAPHIC_ORDER)
        );
        for (final var pseudoColumn : pseudoColumns) {
            MetadataTypeTestUtils.accessors(pseudoColumn);
            pseudoColumn(context, pseudoColumn);
        }
    }

    static void pseudoColumn(final Context context,
                             final com.github.jinahya.database.metadata.bind.PseudoColumn pseudoColumn)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumn, "pseudoColumn is null");
        common(pseudoColumn);
    }

    static void superTables(final Context context, final List<? extends SuperTable> superTables) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTables, "superTables is null");
        for (final var superTable : superTables) {
            MetadataTypeTestUtils.accessors(superTable);
            superTable(context, superTable);
        }
    }

    static void superTable(final Context context, final SuperTable superTable) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTable, "superTable is null");
    }

    static void tablePrivileges(final Context context, final List<? extends TablePrivilege> tablePrivileges)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tablePrivileges, "tablePrivileges is null");
        if (!tablePrivileges.isEmpty()) {
//            log.debug("tablePrivileges: {}", tablePrivileges);
        }
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.HSQL_DATABASE_ENGINE
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(tablePrivileges).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(TablePrivilege.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(TablePrivilege.CASE_INSENSITIVE_ORDER)
                );
            }
        }
        for (final var tablePrivilege : tablePrivileges) {
            MetadataTypeTestUtils.accessors(tablePrivilege);
            tablePrivilege(context, tablePrivilege);
        }
    }

    static void tablePrivilege(final Context context, final TablePrivilege tablePrivilege) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tablePrivilege, "tablePrivilege is null");
        common(tablePrivilege);
    }

    static void tableTypes(final Context context, final List<? extends TableType> tableTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableTypes, "tableTypes is null");
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.MARIA_DB // https://jira.mariadb.org/browse/CONJ-1049
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(tableTypes).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(TableType.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(TableType.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var tableType : tableTypes) {
            MetadataTypeTestUtils.accessors(tableType);
            tableType(context, tableType);
        }
    }

    static void tableType(final Context context, final TableType tableType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableType, "tableType is null");
        {
            assertThat(tableType.getTableType()).isNotNull();
        }
        {
            common(tableType);
        }
    }

    static void typeInfo(final Context context, final List<? extends TypeInfo> typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.MY_SQL // https://bugs.mysql.com/bug.php?id=109931
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(typeInfo).isSortedAccordingTo(TypeInfo.COMPARING_DATA_TYPE);
            }
        }
        for (final var typeInfo_ : typeInfo) {
            MetadataTypeTestUtils.accessors(typeInfo_);
            typeInfo(context, typeInfo_);
        }
    }

    static void typeInfo(final Context context, final TypeInfo typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        {
            assertThat(typeInfo.getTypeName()).isNotNull();
            assertDoesNotThrow(() -> JDBCType.valueOf(typeInfo.getDataType()));
            assertDoesNotThrow(() -> TypeInfo.Nullable.valueOfNullable(typeInfo.getNullable()));
            assertDoesNotThrow(() -> TypeInfo.Searchable.valueOfSearchable(typeInfo.getSearchable()));
        }
        {
            common(typeInfo);
        }
        {
            final var value = TypeInfo.Nullable.valueOfNullable(typeInfo.getNullable());
        }
        {
            final var value = TypeInfo.Searchable.valueOfSearchable(typeInfo.getSearchable());
        }
    }

    static void udts(final Context context, final List<? extends UDT> udts) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udts, "udts is null");
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(udts).doesNotContainNull().satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(UDT.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(UDT.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var udt : udts) {
            MetadataTypeTestUtils.accessors(udt);
            udt(context, udt);
        }
    }

    static void udt(final Context context, final UDT udt) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udt, "udt is null");
        {
            assertThat(udt.getTypeName()).isNotNull();
//            assertThat(udt.getClassName()).isNotNull();
            assertThat(udt.getDataType()).isIn(Types.JAVA_OBJECT, Types.STRUCT, Types.DISTINCT);
            assertDoesNotThrow(() -> JDBCType.valueOf(udt.getDataType()));
        }
        try {
            final var attributes = context.getAttributes(udt, "%");
            attributes(context, attributes);
        } catch (final SQLException sqle) {
            thrown("failed; getAttributes", sqle);
        }
    }

    static void versionColumns(final Context context, final List<? extends VersionColumn> versionColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(versionColumns, "versionColumns is null");
        for (final var versionColumn : versionColumns) {
            MetadataTypeTestUtils.accessors(versionColumn);
            versionColumn(context, versionColumn);
        }
    }

    static void versionColumn(final Context context, final VersionColumn versionColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(versionColumn, "versionColumn is null");
        {
            assertDoesNotThrow(() -> JDBCType.valueOf(versionColumn.getDataType()));
            assertThat(versionColumn.getTypeName()).isNotNull();
            assertDoesNotThrow(() -> PseudoColumn.valueOfPseudoColumn(versionColumn.getPseudoColumn()));
        }
        {
            common(versionColumn);
        }
    }

    static void testOrdering(final Context context) throws SQLException {
        {
            databaseProductName = context.databaseMetaData.getDatabaseProductName();
            info(context);
        }
        try {
            final var attributes = context.getAttributes(null, null, "%", "%");
            assertThat(attributes)
                    .satisfiesAnyOf(
                            l -> assertThat(l).isSortedAccordingTo(Attribute.CASE_INSENSITIVE_ORDER),
                            l -> assertThat(l).isSortedAccordingTo(Attribute.LEXICOGRAPHIC_ORDER)
                    );
        } catch (final SQLException sqle) {
            thrown("failed; getAttributes", sqle);
        }
        try {
            for (final var scope : BestRowIdentifier.Scope.values()) {
                for (final boolean nullable : new boolean[] {true, false}) {
                    final var bestRowIdentifier =
                            context.getBestRowIdentifier(null, null, "%", scope.fieldValueAsInt(), nullable);
                    assertThat(bestRowIdentifier).isSorted();
                    assertThat(bestRowIdentifier)
                            .isSortedAccordingTo(BestRowIdentifier.COMPARING_SCOPE);
                }
            }
        } catch (final SQLException sqle) {
            thrown("failed; getBestRowIdentifier", sqle);
        }
        try {
            final var catalogs = context.getCatalogs();
            assertThat(catalogs).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Catalog.CASE_INSENSITIVE_ORDER),
                    l -> assertThat(l).isSortedAccordingTo(Catalog.LEXICOGRAPHIC_ORDER)
            );
        } catch (final SQLException sqle) {
            thrown("failed; getCatalogs", sqle);
        }
        try {
            final var clientInfoProperties = context.getClientInfoProperties();
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.MARIA_DB
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(clientInfoProperties).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ClientInfoProperty.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ClientInfoProperty.LEXICOGRAPHIC_ORDER)
                );
            }
        } catch (final SQLException sqle) {
            thrown("failed; getCatalogs", sqle);
        }
        if (READ_COLUMN_PRIVILEGES) {
            try {
                final var columnPrivileges = context.getColumnPrivileges(null, null, "%", "%");
                assertThat(columnPrivileges).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.LEXICOGRAPHIC_ORDER)
                );
            } catch (final SQLException sqle) {
                thrown("failed; getColumnPrivileges", sqle);
            }
        }
        try {
            final var columns = context.getColumns(null, null, "%", "%");
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.HSQL_DATABASE_ENGINE,
                    DatabaseProductNames.MARIA_DB
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Column.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Column.LEXICOGRAPHIC_ORDER)
                );
            }
        } catch (final SQLException sqle) {
            thrown("failed; getColumns", sqle);
        }
        try {
            final var functionColumns = context.getFunctionColumns(null, null, "%", "%");
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(functionColumns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(FunctionColumn.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(FunctionColumn.LEXICOGRAPHIC_ORDER)
                );
            }
        } catch (final SQLException sqle) {
            thrown("failed; getFunctionColumns", sqle);
        }
        try {
            final var functions = context.getFunctions(null, null, "%");
            assertThat(functions).doesNotHaveDuplicates();
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(functions).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Function.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Function.LEXICOGRAPHIC_ORDER)
                );
            }
        } catch (final SQLException sqle) {
            thrown("failed; getFunctionColumns", sqle);
        }
        try {
            final var procedureColumns = context.getProcedureColumns(null, null, "%", "%");
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(procedureColumns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ProcedureColumn.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ProcedureColumn.LEXICOGRAPHIC_ORDER)
                );
            }
        } catch (final SQLException sqle) {
            thrown("failed; getProcedureColumns", sqle);
        }
        try {
            final var procedures = context.getProcedures(null, null, "%");
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.HSQL_DATABASE_ENGINE
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(procedures).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Procedure.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Procedure.LEXICOGRAPHIC_ORDER)
                );
            }
        } catch (final SQLException sqle) {
            thrown("failed; getProcedureColumns", sqle);
        }
        try {
            final var schemas = context.getSchemas((String) null, "%");
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(schemas).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Schema.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Schema.LEXICOGRAPHIC_ORDER)
                );
            }
        } catch (final SQLException sqle) {
            thrown("failed; getSchemas", sqle);
        }
        try {
            final var superTables = context.getSuperTables(null, "%", "%");
            assertThat(superTables).doesNotHaveDuplicates();
        } catch (final SQLException sqle) {
            thrown("failed; getSuperTables", sqle);
        }
        try {
            final var superTypes = context.getSuperTypes(null, "%", "%");
            assertThat(superTypes).doesNotHaveDuplicates();
        } catch (final SQLException sqle) {
            thrown("failed; getSuperTypes", sqle);
        }
        if (READ_TABLE_PRIVILEGES) {
            try {
                final var tablePrivileges = context.getTablePrivileges(null, null, "%");
                assertThat(tablePrivileges).doesNotHaveDuplicates();
                assertThat(tablePrivileges).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(TablePrivilege.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(TablePrivilege.LEXICOGRAPHIC_ORDER)
                );
            } catch (final SQLException sqle) {
                thrown("failed; getTablePrivileges", sqle);
            }
        }
        try {
            final var tables = context.getTables(null, null, "%", null);
            assertThat(tables).doesNotHaveDuplicates();
            {
                final var databaseProductNames = Set.of(
                        DatabaseProductNames.HSQL_DATABASE_ENGINE
                        , DatabaseProductNames.MARIA_DB
                        , DatabaseProductNames.POSTGRE_SQL
                );
                if (!databaseProductNames.contains(databaseProductName)) {
                    assertThat(tables).satisfiesAnyOf(
                            l -> assertThat(l).isSortedAccordingTo(Table.CASE_INSENSITIVE_ORDER),
                            l -> assertThat(l).isSortedAccordingTo(Table.LEXICOGRAPHIC_ORDER)
                    );
                }
            }
            for (final var table : tables) {
                try {
                    for (final var scope : BestRowIdentifier.Scope.values()) {
                        for (final boolean nullable : new boolean[] {true, false}) {
                            final var bestRowIdentifier =
                                    context.getBestRowIdentifier(table, scope.fieldValueAsInt(), nullable);
                            assertThat(bestRowIdentifier)
                                    .doesNotHaveDuplicates()
                                    .isSortedAccordingTo(BestRowIdentifier.COMPARING_SCOPE);
                            if (false && !bestRowIdentifier.isEmpty()) { // 'actual...'
                                assertThat(bestRowIdentifier)
                                        .extracting(BestRowIdentifier::getScope)
                                        .containsOnly(scope.fieldValueAsInt());
                            }
                        }
                    }
                } catch (final SQLException sqle) {
                    thrown("failed: getBestRowIdentifier", sqle);
                }
                if (READ_COLUMN_PRIVILEGES) {
                    try {
                        final var columnPrivileges = context.getColumnPrivileges(table, "%");
                        assertThat(columnPrivileges)
                                .doesNotHaveDuplicates()
                                .satisfiesAnyOf(
                                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.CASE_INSENSITIVE_ORDER),
                                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.LEXICOGRAPHIC_ORDER)
                                );
                    } catch (final SQLException sqle) {
                        thrown("failed: getColumnPrivileges", sqle);
                    }
                }
                try {
                    final var exportedKeys = context.getExportedKeys(table);
                    assertThat(exportedKeys)
                            .doesNotHaveDuplicates()
                            .satisfiesAnyOf(
                                    l -> assertThat(l).isSortedAccordingTo(ExportedKey.CASE_INSENSITIVE_ORDER),
                                    l -> assertThat(l).isSortedAccordingTo(ExportedKey.LEXICOGRAPHIC_ORDER)
                            );
                } catch (final SQLException sqle) {
                    thrown("failed: getExportedKeys", sqle);
                }
                try {
                    final var importedKeys = context.getImportedKeys(table);
                    assertThat(importedKeys)
                            .doesNotHaveDuplicates()
                            .satisfiesAnyOf(
                                    l -> assertThat(l).isSortedAccordingTo(ImportedKey.CASE_INSENSITIVE_ORDER),
                                    l -> assertThat(l).isSortedAccordingTo(ImportedKey.LEXICOGRAPHIC_ORDER)
                            );
                } catch (final SQLException sqle) {
                    thrown("failed: getImportedKeys", sqle);
                }
                try {
                    for (final boolean unique : new boolean[] {false, true}) {
                        for (final boolean approximate : new boolean[] {false, true}) {
                            final var indexInfo = context.getIndexInfo(table, unique, approximate);
                            assertThat(indexInfo)
                                    .doesNotHaveDuplicates()
                                    .satisfiesAnyOf(
                                            l -> assertThat(l).isSortedAccordingTo(IndexInfo.CASE_INSENSITIVE_ORDER),
                                            l -> assertThat(l).isSortedAccordingTo(IndexInfo.LEXICOGRAPHIC_ORDER)
                                    );
                        }
                    }
                } catch (final SQLException sqle) {
                    thrown("failed: getIndexInfo", sqle);
                }
                try {
                    final var primaryKeys = context.getPrimaryKeys(table);
                    final var databaseProductNames = Set.of(
                            DatabaseProductNames.HSQL_DATABASE_ENGINE
                            , DatabaseProductNames.MY_SQL
                            , DatabaseProductNames.POSTGRE_SQL
                    );
                    if (!databaseProductNames.contains(databaseProductName)) {
                        assertThat(primaryKeys)
                                .doesNotHaveDuplicates()
                                .satisfiesAnyOf(
                                        l -> assertThat(l).isSortedAccordingTo(PrimaryKey.CASE_INSENSITIVE_ORDER),
                                        l -> assertThat(l).isSortedAccordingTo(PrimaryKey.LEXICOGRAPHIC_ORDER)
                                );
                    }
                } catch (final SQLException sqle) {
                    thrown("failed: getPrimaryKeys", sqle);
                }
                try {
                    final var versionColumns = context.getVersionColumns(table); // unordered
                } catch (final SQLException sqle) {
                    thrown("failed: getVersionColumns", sqle);
                }
            }
        } catch (final SQLException sqle) {
            thrown("failed; getTables", sqle);
        }
        try {
            final var typeInfo = context.getTypeInfo();
            assertThat(typeInfo).doesNotHaveDuplicates();
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.H2
                    , DatabaseProductNames.HSQL_DATABASE_ENGINE
                    , DatabaseProductNames.MY_SQL
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(typeInfo).isSortedAccordingTo(TypeInfo.COMPARING_DATA_TYPE);
            }
        } catch (final SQLException sqle) {
            thrown("failed; getTypeInfo", sqle);
        }
        try {
            final var udts = context.getUDTs(null, null, "%", null);
            assertThat(udts).doesNotHaveDuplicates();
            final var databaseProductNames = Set.of(
//                    DatabaseProductNames.H2,
//                    DatabaseProductNames.HSQL_DATABASE_ENGINE
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(udts).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(UDT.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(UDT.LEXICOGRAPHIC_ORDER)
                );
            }
        } catch (final SQLException sqle) {
            thrown("failed; getTypeInfo", sqle);
        }
    }

    static List<Table> getTables__(final Context context, final String catalog, final String schemaPattern,
                                   final String tableNamePattern, final String[] types)
            throws SQLException {
        {
            info(context);
        }
        final var tables = context.getTables(catalog, schemaPattern, tableNamePattern, types);
        assertThat(tables).doesNotHaveDuplicates();
        {
            final var databaseProductNames = Set.of(
                    // https://sourceforge.net/p/hsqldb/bugs/1672/
                    DatabaseProductNames.HSQL_DATABASE_ENGINE
                    , DatabaseProductNames.MARIA_DB
                    , DatabaseProductNames.POSTGRE_SQL
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(tables).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Table.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Table.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var table : tables) {
            common(table);
        }
        return tables;
    }

    static List<Column> getColumns__(final Context context, final String catalog, final String schemaPattern,
                                     final String tableNamePattern, final String columnNamePattern)
            throws SQLException {
        {
            info(context);
        }
        final var columns = context.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        assertThat(columns).doesNotHaveDuplicates();
        {
            final var databaseProductNames = Set.of(
                    DatabaseProductNames.HSQL_DATABASE_ENGINE,
                    DatabaseProductNames.MARIA_DB
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Column.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Column.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var column : columns) {
            common(column);
        }
        return columns;
    }

    private ContextTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
