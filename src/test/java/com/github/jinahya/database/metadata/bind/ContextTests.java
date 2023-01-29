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
import org.junit.platform.commons.util.ReflectionUtils;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTests {

    static void info(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        log.debug("databaseProductName: {}", context.databaseMetaData.getDatabaseProductName());
        log.debug("databaseProductVersion: {}", context.databaseMetaData.getDatabaseProductVersion());
        log.debug("databaseMajorVersion: {}", context.databaseMetaData.getDatabaseMajorVersion());
        log.debug("databaseMinorVersion: {}", context.databaseMetaData.getDatabaseMinorVersion());
        log.debug("driverName: {}", context.databaseMetaData.getDriverName());
        log.debug("driverVersion: {}", context.databaseMetaData.getDriverVersion());
        log.debug("driverMajorVersion: {}", context.databaseMetaData.getDriverMajorVersion());
        log.debug("driverMinorVersion: {}", context.databaseMetaData.getDriverMinorVersion());
        log.debug("catalogSeparator: {}", context.databaseMetaData.getCatalogSeparator());
        log.debug("catalogTerm: {}", context.databaseMetaData.getCatalogTerm());
        log.debug("schemaTerm: {}", context.databaseMetaData.getSchemaTerm());
    }

    private static void common(final Object value) {
        Objects.requireNonNull(value, "value is null");
        final var string = value.toString();
        final var hashCode = value.hashCode();
        if (value instanceof MetadataType) {
            final var unmappedValues = ((MetadataType) value).getUnmappedValues();
            if (!unmappedValues.isEmpty()) {
                log.warn("has unmapped values: {}", value);
            }
        }
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
                        }))
        ;
    }

    private static void thrown(final String message, final Throwable throwable) {
        Objects.requireNonNull(message, "message is null");
        Objects.requireNonNull(throwable, "throwable is null");
        if (throwable instanceof SQLFeatureNotSupportedException) {
            return;
        }
        log.error("{}", message, throwable);
    }

    private static void uniqueCatalogIds(final Context context) throws SQLException {
        final var catalogs = context.getCatalogs();
        final var groups = catalogs.stream().collect(Collectors.groupingBy(Catalog::getCatalogId));
        assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                .isEmpty();
        assertThat(catalogs)
                .extracting(Catalog::getCatalogId)
                .doesNotHaveDuplicates();
    }

    private static void uniqueExportedKeyFktableIds(final Context context) throws SQLException {
        final var tables = context.getTables(null, null, "%", null);
        for (final var table : tables) {
            try {
                final var exportedKeys = context.getExportedKeys(null, null, table.getTableName());
                final var groups = exportedKeys.stream().collect(Collectors.groupingBy(TableKey::getFktableId));
                assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                        .isEmpty();
                assertThat(exportedKeys)
                        .extracting(TableKey::getFktableId)
                        .doesNotHaveDuplicates();
            } catch (final SQLException sqle) {
            }
        }
    }

    private static void uniqueExportedKeyPktableIds(final Context context) throws SQLException {
        final var tables = context.getTables(null, null, "%", null);
        for (final var table : tables) {
            try {
                final var exportedKeys = context.getExportedKeys(null, null, table.getTableName());
                final var groups = exportedKeys.stream().collect(Collectors.groupingBy(TableKey::getPktableId));
                assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                        .isEmpty();
                assertThat(exportedKeys)
                        .extracting(TableKey::getPktableId)
                        .doesNotHaveDuplicates();
            } catch (final SQLException sqle) {
            }
        }
    }

    private static void uniqueColumnIds(final Context context) throws SQLException {
        final var columns = context.getColumns(null, null, "%", "%");
        final var groups = columns.stream().collect(Collectors.groupingBy(Column::getColumnId));
        assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                .isEmpty();
        assertThat(columns)
                .extracting(Column::getColumnId)
                .doesNotHaveDuplicates();
    }

    private static void uniqueFunctionIds(final Context context) throws SQLException {
        try {
            final var functions = context.getFunctions(null, null, "%");
            final var groups = functions.stream().collect(Collectors.groupingBy(Function::getFunctionId));
            assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                    .isEmpty();
            assertThat(functions)
                    .extracting(Function::getFunctionId)
                    .doesNotHaveDuplicates();
        } catch (final SQLException sqle) {
        }
    }

    private static void uniqueFunctionColumnIds(final Context context) throws SQLException {
        try {
            final var functionColumns = context.getFunctionColumns(null, null, "%", "%");
            final var groups =
                    functionColumns.stream().collect(Collectors.groupingBy(FunctionColumn::getFunctionColumnId));
//            assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
//                    .isEmpty();
//            assertThat(functionColumns)
//                    .extracting(FunctionColumn::getFunctionColumnId)
//                    .doesNotHaveDuplicates();
        } catch (final SQLException sqle) {
        }
    }

    private static void uniqueImportedKeyFktableIds(final Context context) throws SQLException {
        final var tables = context.getTables(null, null, "%", null);
        for (final var table : tables) {
            final var importedKeys = context.getImportedKeys(null, null, table.getTableName());
            final var groups = importedKeys.stream().collect(Collectors.groupingBy(TableKey::getFktableId));
            assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                    .isEmpty();
            assertThat(importedKeys)
                    .extracting(TableKey::getFktableId)
                    .doesNotHaveDuplicates();
        }
    }

    private static void uniqueImportedKeyPktableIds(final Context context) throws SQLException {
        final var tables = context.getTables(null, null, "%", null);
        for (final var table : tables) {
            final var importedKeys = context.getImportedKeys(null, null, table.getTableName());
            final var groups = importedKeys.stream().collect(Collectors.groupingBy(TableKey::getPktableId));
            assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                    .isEmpty();
            assertThat(importedKeys)
                    .extracting(TableKey::getPktableId)
                    .doesNotHaveDuplicates();
        }
    }

    private static void uniqueProcedureIds(final Context context) throws SQLException {
        final var procedures = context.getProcedures(null, null, "%");
        final var groups = procedures.stream().collect(Collectors.groupingBy(Procedure::getProcedureId));
        assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                .isEmpty();
        assertThat(procedures)
                .extracting(Procedure::getProcedureId)
                .doesNotHaveDuplicates();
    }

    private static void uniqueProcedureColumnIds(final Context context) throws SQLException {
        final var procedureColumns = context.getProcedureColumns(null, null, "%", "%");
        final var groups =
                procedureColumns.stream().collect(Collectors.groupingBy(ProcedureColumn::getProcedureColumnId));
//        assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
//                .isEmpty();
//        assertThat(procedureColumns)
//                .extracting(ProcedureColumn::getProcedureColumnId)
//                .doesNotHaveDuplicates();
    }

    private static void uniqueTableIds(final Context context) throws SQLException {
        final var tables = context.getTables(null, null, "%", null);
        final var groups = tables.stream().collect(Collectors.groupingBy(Table::getTableId));
        assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                .isEmpty();
        assertThat(tables)
                .extracting(Table::getTableId)
                .doesNotHaveDuplicates();
    }

    private static void uniqueSchemaIds(final Context context) throws SQLException {
        try {
            final var schemas = context.getSchemas(null, "%");
            final var groups = schemas.stream().collect(Collectors.groupingBy(Schema::getSchemaId));
            assertThat(groups.entrySet().stream().filter(e -> e.getValue().size() > 1))
                    .isEmpty();
            assertThat(schemas)
                    .extracting(Schema::getSchemaId)
                    .doesNotHaveDuplicates();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
        }
    }

    private static String databaseProductName;

    static void test(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        {
            databaseProductName = context.databaseMetaData.getDatabaseProductName();
            info(context);
        }
        uniqueCatalogIds(context);
        uniqueColumnIds(context);
        uniqueExportedKeyFktableIds(context);
        uniqueExportedKeyPktableIds(context);
        uniqueFunctionIds(context);
        uniqueFunctionColumnIds(context);
        uniqueImportedKeyFktableIds(context);
        uniqueImportedKeyPktableIds(context);
        uniqueProcedureIds(context);
        uniqueProcedureColumnIds(context);
        uniqueSchemaIds(context);
        uniqueTableIds(context);
        final List<Catalog> catalogs = new ArrayList<>();
        {
            try {
                context.getCatalogs(catalogs::add);
            } catch (final SQLException sqle) {
                thrown("failed; getCatalogs", sqle);
            }
            if (catalogs.isEmpty()) {
                catalogs.add(Catalog.builder().tableCat(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY).build());
            }
            catalogs(context, catalogs);
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
            final var functionColumns = context.getFunctionColumns(null, null, "%", "%");
            functionColumns(context, functionColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getFunctionColumns", sqle);
        }
        try {
            final var procedures = context.getProcedures(null, null, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            thrown("failed; getProcedures", sqle);
        }
        try {
            final var procedureColumns = context.getProcedureColumns(null, null, "%", "%");
            procedureColumns(context, procedureColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getProcedureColumns", sqle);
        }
        try {
            final var schemas = context.getSchemas(null, null);
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
            thrown("failed; getTypeInfo", sqle);
        }
    }

    static void attributes(final Context context, final List<? extends Attribute> attributes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attributes, "attributes is null");
        assertThat(attributes).isSortedAccordingTo(Attribute.COMPARING_TYPE_CAT_TYPE_SCHEM_TYPE_NAME);
        for (final var attribute : attributes) {
            attribute(context, attribute);
        }
    }

    static void attribute(final Context context, final Attribute attribute) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attribute, "attribute is null");
        common(attribute);
    }

    static void bestRowIdentifier(final Context context, final List<? extends BestRowIdentifier> bestRowIdentifier)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(bestRowIdentifier, "bestRowIdentifier is null");
        assertThat(bestRowIdentifier).isSortedAccordingTo(BestRowIdentifier.COMPARING_SCOPE);
        for (final var bestRowIdentifier_ : bestRowIdentifier) {
            bestRowIdentifier(context, bestRowIdentifier_);
        }
    }

    static void bestRowIdentifier(final Context context, final BestRowIdentifier bestRowIdentifier)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(bestRowIdentifier, "bestRowIdentifier is null");
        common(bestRowIdentifier);
    }

    static void catalogs(final Context context, final List<? extends Catalog> catalogs) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalogs, "catalogs is null");
        assertThat(catalogs).isSortedAccordingTo(Catalog.COMPARING_TABLE_CAT);
        assertThat(catalogs)
                .extracting(Catalog::getCatalogId)
                .doesNotHaveDuplicates();
        catalogs.stream().map(Catalog::getCatalogId).forEach(ContextTests::common);
        for (final var catalog : catalogs) {
            catalog(context, catalog);
        }
    }

    static void catalog(final Context context, final Catalog catalog) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalog, "catalog is null");
        common(catalog);
        try {
            final var columns = catalog.getColumns(context, null, "%", "%");
            columns(context, columns);
        } catch (final SQLException sqle) {
            thrown("failed: getColumns", sqle);
        }
        try {
            final var schemas = context.getSchemas(catalog.getTableCatNonNull(), "%");
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            thrown("failed: getSchemas", sqle);
        }
        try {
            final var tablePrivileges = catalog.getTablePrivileges(context, null, "%");
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLException sqle) {
            thrown("failed; getTablePrivileges", sqle);
        }
    }

    private static void clientInfoProperties(final Context context,
                                             final List<? extends ClientInfoProperty> clientInfoProperties)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(clientInfoProperties, "clientInfoProperties is null");
        for (final var clientInfoProperty : clientInfoProperties) {
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
        {
            final var databaseProductNames = Set.of(
                    TestcontainersMariadbIT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columns).isSortedAccordingTo(
                        Column.COMPARING_TABLE_CAT_TABLE_SCHEM_TABLE_NAME_ORDINAL_POSITION);
            }
        }
        assertThat(columns)
                .extracting(Column::getColumnId)
                .doesNotHaveDuplicates();
        for (final var column : columns) {
            column(context, column);
        }
    }

    static void column(final Context context, final Column column) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(column, "column is null");
        common(column);
    }

    static void columnPrivileges(final Context context, final List<? extends ColumnPrivilege> columnPrivileges)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columnPrivileges, "columnPrivileges is null");
        assertThat(columnPrivileges).isSortedAccordingTo(ColumnPrivilege.COMPARING_COLUMN_NAME_PRIVILEGE);
        for (final var columnPrivilege : columnPrivileges) {
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
        assertThat(crossReference).isSortedAccordingTo(
                CrossReference.COMPARING_FKTABLE_CAT_FKTABLE_SCHEM_FKTABLE_NAME_KEY_SEQ);
        assertThat(crossReference)
                .extracting(CrossReference::getPkcolumnId)
                .doesNotHaveDuplicates();
        assertThat(crossReference)
                .extracting(CrossReference::getFkcolumnId)
                .doesNotHaveDuplicates();
        for (final var crossReference_ : crossReference) {
            crossReference(context, crossReference_);
        }
    }

    static void crossReference(final Context context, final CrossReference crossReference) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(crossReference, "crossReference is null");
        log.debug("crossReference: {}", crossReference);
        common(crossReference);
    }

    static void exportedKeys(final Context context, final List<? extends ExportedKey> exportedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKeys, "exportedKeys is null");
        assertThat(exportedKeys).isSortedAccordingTo(TableKey.COMPARING_FKTABLE_CAT_FKTABLE_SCHEM_FKTABLE_NAME_KEY_SEQ);
        assertThat(exportedKeys)
                .extracting(ExportedKey::getPktableId)
                .doesNotHaveDuplicates();
        assertThat(exportedKeys)
                .extracting(ExportedKey::getFktableId)
                .doesNotHaveDuplicates();
        for (final var exportedKey : exportedKeys) {
            exportedKey(context, exportedKey);
        }
    }

    private static void exportedKey(final Context context, final ExportedKey exportedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKey, "exportedKey is null");
        common(exportedKey);
        common(exportedKey.getPktableId());
        common(exportedKey.getFktableId());
    }

    private static void functions(final Context context, final List<? extends Function> functions) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functions, "functions is null");
        assertThat(functions).isSortedAccordingTo(
                Function.COMPARING_FUNCTION_CAT_FUNCTION_SCHEM_FUNCTION_NAME_SPECIFIC_NAME);
        final Map<FunctionId, List<Function>> groups = functions.stream()
                .collect(Collectors.groupingBy(Function::getFunctionId));
        groups.entrySet().stream().filter(e -> e.getValue().size() > 1).forEach(e -> {
            log.debug("duplicate function: " + e.getKey());
            log.debug("\tduplicate function: " + e.getValue());
        });
        assertThat(functions)
                .extracting(Function::getFunctionId)
                .doesNotHaveDuplicates();
        for (final var function : functions) {
            function(context, function);
        }
    }

    private static void function(final Context context, final Function function) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        common(function);
        common(function.getFunctionId());
        try {
            final var functionColumns = function.getFunctionColumns(context, "%");
            functionColumns(context, functionColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getFunctionColumns", sqle);
        }
    }

    private static void functionColumns(final Context context, final List<? extends FunctionColumn> functionColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionColumns, "functionColumns is null");
        assertThat(functionColumns).isSortedAccordingTo(
                FunctionColumn.COMPARING_FUNCTION_CAT_FUNCTION_SCHEM_FUNCTION_NAME_SPECIFIC_NAME);
        for (final var functionColumn : functionColumns) {
            functionColumn(context, functionColumn);
        }
    }

    private static void functionColumn(final Context context, final FunctionColumn functionColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionColumn, "functionColumn is null");
        common(functionColumn);
        common(functionColumn.getFunctionColumnId());
    }

    static void importedKeys(final Context context, final List<? extends ImportedKey> importedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKeys, "importedKeys is null");
        assertThat(importedKeys).isSortedAccordingTo(TableKey.COMPARING_FKTABLE_CAT_FKTABLE_SCHEM_FKTABLE_NAME_KEY_SEQ);
        assertThat(importedKeys)
                .extracting(ImportedKey::getPktableId)
                .doesNotHaveDuplicates();
        assertThat(importedKeys)
                .extracting(ImportedKey::getFktableId)
                .doesNotHaveDuplicates();
        for (final var importedKey : importedKeys) {
            importedKey(context, importedKey);
        }
    }

    static void importedKey(final Context context, final ImportedKey importedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKey, "importedKey is null");
        common(importedKey);
        common(importedKey.getPktableId());
        common(importedKey.getFktableId());
    }

    static void indexInfo(final Context context, final List<? extends IndexInfo> indexInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(indexInfo, "indexInfo is null");
        if (!databaseProductName.equals("MySQL")) {
            // https://bugs.mysql.com/bug.php?id=109803
            assertThat(indexInfo).isSortedAccordingTo(IndexInfo.COMPARING_NON_UNIQUE_TYPE_INDEX_NAME_ORDINAL_POSITION);
        }
        for (final var indexInfo_ : indexInfo) {
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
                    MemoryHsqlTest.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(procedures).isSortedAccordingTo(
                        Procedure.COMPARING_PROCEDURE_CAT_PROCEDURE_SCHEM_PROCEDURE_NAME_SPECIFIC_NAME);
            }
        }
        assertThat(procedures)
                .extracting(Procedure::getProcedureId)
                .doesNotHaveDuplicates();
        procedures.stream().map(Procedure::getProcedureId).forEach(ContextTests::common);
        for (final var procedure : procedures) {
            procedure(context, procedure);
        }
    }

    private static void procedure(final Context context, final Procedure procedure) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedure, "procedure is null");
        common(procedure);
        common(procedure.getProcedureId());
        try {
            final var procedureColumns = procedure.getProcedureColumns(context, "%");
            procedureColumns(context, procedureColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getProcedureColumns", sqle);
        }
    }

    private static void procedureColumns(final Context context, final List<? extends ProcedureColumn> procedureColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumns, "procedureColumns is null");
        assertThat(procedureColumns).isSortedAccordingTo(
                ProcedureColumn.COMPARING_PROCEDURE_CAT_PROCEDURE_SCHEM_PROCEDURE_NAME_SPECIFIC_NAME);
        for (final var procedureColumn : procedureColumns) {
            procedureColumn(context, procedureColumn);
        }
    }

    private static void procedureColumn(final Context context, final ProcedureColumn procedureColumn)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumn, "procedureColumn is null");
        common(procedureColumn);
        common(procedureColumn.getProcedureColumnId());
    }

    static void schemas(final Context context, final List<? extends Schema> schemas) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schemas, "schemas is null");
        {
            final var databaseProductNames = Set.of(
                    MemoryHsqlTest.DATABASE_PRODUCT_NAME // https://sourceforge.net/p/hsqldb/bugs/1671/
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(schemas).isSortedAccordingTo(Schema.COMPARING_TABLE_CATALOG_TABLE_SCHEM);
            }
        }
        assertThat(schemas)
                .extracting(Schema::getSchemaId)
                .doesNotHaveDuplicates();
        for (final var schema : schemas) {
            schema(context, schema);
        }
    }

    static void schema(final Context context, final Schema schema) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schema, "schema is null");
        common(schema);
        common(schema.getSchemaId());
    }

    static void superTypes(final Context context, final List<? extends SuperType> superTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTypes, "superTypes is null");
        assertThat(superTypes)
                .extracting(SuperType::getTypeId)
                .doesNotHaveDuplicates();
        for (final var superType : superTypes) {
            superType(context, superType);
        }
    }

    static void superType(final Context context, final SuperType superType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superType, "superType is null");
        common(superType);
        common(superType.getTypeId());
        common(superType.getSupertypeId());
    }

    static void tables(final Context context, final List<? extends Table> tables) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tables, "tables is null");
        {
            final var databaseProductNames = Set.of(
                    // https://sourceforge.net/p/hsqldb/bugs/1672/
                    MemoryHsqlTest.DATABASE_PRODUCT_NAME,
                    TestcontainersMariadbIT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(tables).isSortedAccordingTo(Table.COMPARING_TABLE_TYPE_TABLE_CAT_TABLE_SCHEM_TABLE_NAME);
            }
        }
        assertThat(tables)
                .extracting(Table::getTableId)
                .doesNotHaveDuplicates();
        tables.stream().map(Table::getTableId).forEach(ContextTests::common);
        for (final var table : tables) {
            table(context, table);
        }
        for (final var parent : tables) {
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
        common(table);
        try {
            for (final var scope : BestRowIdentifier.scopes()) {
                for (final boolean nullable : new boolean[] {true, false}) {
                    final var bestRowIdentifier = table.getBestRowIdentifier(context, scope, nullable);
                    bestRowIdentifier(context, bestRowIdentifier);
                }
            }
        } catch (final SQLException sqle) {
            thrown("failed; getBestRowIdentifier", sqle);
        }
        try {
            final var columnPrivileges = table.getColumnPrivileges(context, "%");
            columnPrivileges(context, columnPrivileges);
        } catch (final SQLException sqle) {
            thrown("failed; getColumnPrivileges", sqle);
        }
        {
            final var columns = context.getColumns(
                    table.getTableCatNonNull(),
                    table.getTableSchemNonNull(),
                    table.getTableName(),
                    "%"
            );
            columns(context, columns);
        }
        try {
            final var exportedKeys = table.getExportedKeys(context);
            exportedKeys(context, exportedKeys);
        } catch (final SQLException sqle) {
            thrown("failed; getExportedKeys", sqle);
        }
        try {
            final var importedKeys = table.getImportedKeys(context);
            importedKeys(context, importedKeys);
        } catch (final SQLException sqle) {
            thrown("failed; getImportedKeys", sqle);
        }
        try {
            for (final boolean unique : new boolean[] {true, false}) {
                for (final boolean approximate : new boolean[] {true, false}) {
                    final var indexInfo = context.getIndexInfo(
                            table.getTableCatNonNull(),
                            table.getTableSchemNonNull(),
                            table.getTableName(),
                            unique,
                            approximate
                    );
                    indexInfo(context, indexInfo);
                }
            }
        } catch (final SQLException sqle) {
            thrown("failed; getIndexInfo", sqle);
        }
        try {
            final var versionColumns = table.getVersionColumns(context);
            versionColumns(context, versionColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getVersionColumns", sqle);
        }
    }

    static void tableTypes(final Context context, final List<? extends TableType> tableTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableTypes, "tableTypes is null");
        assertThat(tableTypes).isSortedAccordingTo(TableType.COMPARING_TABLE_TYPE);
        final var databaseProductNames = Set.of(
                "MariaDB"
        );
        if (!databaseProductNames.contains(databaseProductName)) {
            assertThat(tableTypes).isSortedAccordingTo(TableType.COMPARING_TABLE_TYPE);
        }
        for (final var tableType : tableTypes) {
            tableType(context, tableType);
        }
    }

    static void tableType(final Context context, final TableType tableType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableType, "tableType is null");
        common(tableType);
    }

    static void typeInfo(final Context context, final List<? extends TypeInfo> typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        {
            final var databaseProductNames = Set.of(
                    // https://github.com/xerial/sqlite-jdbc/issues/832 fixed! awaiting a new version...
                    MemorySqliteTest.DATABASE_PRODUCT_NAME,
                    // https://bugs.mysql.com/bug.php?id=109807
                    TestcontainersMysqlIT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(typeInfo).isSortedAccordingTo(TypeInfo.COMPARING_DATA_TYPE);
            }
        }
        for (final var typeInfo_ : typeInfo) {
            typeInfo(context, typeInfo_);
        }
    }

    static void typeInfo(final Context context, final TypeInfo typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        common(typeInfo);
    }

    static void primaryKeys(final Context context, final List<? extends PrimaryKey> primaryKeys) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKeys, "primaryKeys is null");
        final var databaseProductNames = Set.of(
                MemoryHsqlTest.DATABASE_PRODUCT_NAME,
                "PostgreSQL",
                "MySQL"
        );
        if (!databaseProductNames.contains(databaseProductName)) {
            // https://sourceforge.net/p/hsqldb/bugs/1673/
            // https://bugs.mysql.com/bug.php?id=109808
            assertThat(primaryKeys).isSortedAccordingTo(PrimaryKey.COMPARING_COLUMN_NAME);
        }
        for (final var primaryKey : primaryKeys) {
            primaryKey(context, primaryKey);
        }
    }

    static void primaryKey(final Context context, final PrimaryKey primaryKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKey, "primaryKey is null");
        common(primaryKey);
    }

    static void pseudoColumns(final Context context, final List<? extends PseudoColumn> pseudoColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumns, "pseudoColumns is null");
        assertThat(pseudoColumns).isSortedAccordingTo(
                PseudoColumn.COMPARING_TABLE_CAT_TABLE_SCHEM_TABLE_NAME_COLUMN_NAME);
        for (final var pseudoColumn : pseudoColumns) {
            pseudoColumn(context, pseudoColumn);
        }
    }

    static void pseudoColumn(final Context context, final PseudoColumn pseudoColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumn, "pseudoColumn is null");
        common(pseudoColumn);
    }

    static void superTables(final Context context, final List<? extends SuperTable> superTables) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTables, "superTables is null");
        for (final var superTable : superTables) {
            superTable(context, superTable);
        }
    }

    static void superTable(final Context context, final SuperTable superTable) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTable, "superTable is null");
        common(superTable);
    }

    static void tablePrivileges(final Context context, final List<? extends TablePrivilege> tablePrivileges)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tablePrivileges, "tablePrivileges is null");
        {
            final var databaseProductNames = Set.of(
                    MemoryHsqlTest.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(tablePrivileges).isSortedAccordingTo(
                        TablePrivilege.COMPARING_TABLE_CAT_TABLE_SCHEM_TABLE_NAME_PRIVILEGE);
            }
        }
        for (final var tablePrivilege : tablePrivileges) {
            tablePrivilege(context, tablePrivilege);
        }
    }

    static void tablePrivilege(final Context context, final TablePrivilege tablePrivilege) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tablePrivilege, "tablePrivilege is null");
        common(tablePrivilege);
    }

    static void udts(final Context context, final List<? extends UDT> udts) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udts, "udts is null");
        assertThat(udts).isSortedAccordingTo(UDT.COMPARING_DATA_TYPE_TYPE_CAT_TYPE_SCHEM_TYPE_NAME);
        assertThat(udts)
                .extracting(UDT::getTypeId)
                .doesNotHaveDuplicates();
        for (final var udt : udts) {
            udt(context, udt);
        }
    }

    static void udt(final Context context, final UDT udt) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udt, "udt is null");
        common(udt);
        try {
            final var attributes = udt.getAttributes(context, "%");
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
            versionColumn(context, versionColumn);
        }
    }

    static void versionColumn(final Context context, final VersionColumn versionColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(versionColumn, "versionColumn is null");
        common(versionColumn);
    }

    private ContextTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
