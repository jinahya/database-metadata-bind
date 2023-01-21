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
import java.util.Objects;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTests {

    private static void common(final Object value) {
        Objects.requireNonNull(value, "value is null");
        final var string = value.toString();
        final var hashCode = value.hashCode();
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
                .ifPresent(r -> {
                    log.debug("rebuilt: {}", r);
                })
        ;
    }

    static void test(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        List<Catalog> catalogs = null;
        {
            try {
                catalogs = context.getCatalogs();
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                log.error("not supported; getCatalogs", sqlfnse);
            }
            if (catalogs == null) {
                catalogs = new ArrayList<>();
            }
            if (catalogs.isEmpty()) {
                catalogs.add(Catalog.builder().tableCat(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY).build());
            }
            for (final var catalog : catalogs) {
                catalog(context, catalog);
            }
        }
        try {
            final var tableTypes = context.getTableTypes();
            for (final var tableType : tableTypes) {
                tableType(context, tableType);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTableTypes", sqlfnse);
        }
        try {
            final var typeInfos = context.getTypeInfo();
            for (final var typeInfo_ : typeInfos) {
                typeInfo(context, typeInfo_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var deletesAreDetected = DeletesAreDetected.getAllValues(context);
            for (final var deletesAreDetected_ : deletesAreDetected) {
                deletesAreDetected(context, deletesAreDetected_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var insertsAreDetected = InsertsAreDetected.getAllValues(context);
            for (final var insertsAreDetected_ : insertsAreDetected) {
                insertsAreDetected(context, insertsAreDetected_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var updatesAreDetected = UpdatesAreDetected.getAllValues(context);
            for (final var updatesAreDetected_ : updatesAreDetected) {
                updatesAreDetected(context, updatesAreDetected_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var othersDeletesAreVisible = OthersDeletesAreVisible.getAllValues(context);
            for (final var othersDeletesAreVisible_ : othersDeletesAreVisible) {
                othersDeletesAreVisible(context, othersDeletesAreVisible_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var othersInsertsAreVisible = OthersInsertsAreVisible.getAllValues(context);
            for (final var othersInsertsAreVisible_ : othersInsertsAreVisible) {
                othersInsertsAreVisible(context, othersInsertsAreVisible_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var othersUpdatesAreVisible = OthersUpdatesAreVisible.getAllValues(context);
            for (final var othersUpdatesAreVisible_ : othersUpdatesAreVisible) {
                othersUpdatesAreVisible(context, othersUpdatesAreVisible_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var ownDeletesAreVisible = OwnDeletesAreVisible.getAllValues(context);
            for (final var ownDeletesAreVisible_ : ownDeletesAreVisible) {
                ownDeletesAreVisible(context, ownDeletesAreVisible_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var ownInsertsAreVisible = OwnInsertsAreVisible.getAllValues(context);
            for (final var ownInsertsAreVisible_ : ownInsertsAreVisible) {
                ownInsertsAreVisible(context, ownInsertsAreVisible_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
        try {
            final var ownUpdatesAreVisible = OwnUpdatesAreVisible.getAllValues(context);
            for (final var ownUpdatesAreVisible_ : ownUpdatesAreVisible) {
                ownUpdatesAreVisible(context, ownUpdatesAreVisible_);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getTypeInfos", sqlfnse);
        }
    }

    static void attribute(final Context context, final Attribute attribute) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attribute, "attribute is null");
        log.debug("attribute: {}", attribute);
        common(attribute);
        final var rebuilt = attribute.toBuilder().build();
    }

    static void bestRowIdentifier(final Context context, final BestRowIdentifier bestRowIdentifier)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(bestRowIdentifier, "bestRowIdentifier is null");
        log.debug("bestRowIdentifier: {}", bestRowIdentifier);
        common(bestRowIdentifier);
        final var rebuilt = bestRowIdentifier.toBuilder().build();
    }

    static void catalog(final Context context, final Catalog catalog) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalog, "catalog is null");
        log.debug("catalog: {}", catalog);
        common(catalog);
        try {
            final var attributes = catalog.getAttributes(context, null, "%", "%");
            for (final var attribute : attributes) {
                attribute(context, attribute);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var columns = catalog.getColumns(context, null, "%", "%");
            for (final var column : columns) {
                column(context, column);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var functionColumns = catalog.getFunctionColumns(context, null, "%", "%");
            for (final var functionColumn : functionColumns) {
                functionColumn(context, functionColumn);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var functions = catalog.getFunctions(context, null, "%");
            for (final var function : functions) {
                function(context, function);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var procedureColumns = catalog.getProcedureColumns(context, null, "%", "%");
            for (final var procedureColumn : procedureColumns) {
                procedureColumn(context, procedureColumn);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var procedures = catalog.getProcedures(context, null, "%");
            for (final var procedure : procedures) {
                procedure(context, procedure);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var pseudoColumns = catalog.getPseudoColumns(context, null, "%", "%");
            for (final var pseudoColumn : pseudoColumns) {
                pseudoColumn(context, pseudoColumn);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var schemas = catalog.getSchemas(context, null);
            if (schemas.isEmpty()) {
                schemas.add(
                        Schema.builder()
                                .tableCatalog(catalog.getTableCat())
                                .tableSchem(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY)
                                .build()
                );
            }
            for (final var schema : schemas) {
                schema(context, schema);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var superTables = catalog.getSuperTables(context, "%", "%");
            for (final var superTable : superTables) {
                superTable(context, superTable);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var tablePrivileges = catalog.getTablePrivileges(context, null, "%");
            for (final var tablePrivilege : tablePrivileges) {
                tablePrivilege(context, tablePrivilege);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var tables = catalog.getTables(context, null, "%", null);
            for (final var table : tables) {
                table(context, table);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
    }

    static void deletesAreDetected(final Context context, final DeletesAreDetected deletesAreDetected)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(deletesAreDetected, "deletesAreDetected is null");
        log.debug("deletesAreDetected: {}", deletesAreDetected);
        common(deletesAreDetected);
        final var rebuilt = deletesAreDetected.toBuilder().build();
    }

    static void insertsAreDetected(final Context context, final InsertsAreDetected insertsAreDetected)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(insertsAreDetected, "insertsAreDetected is null");
        log.debug("insertsAreDetected: {}", insertsAreDetected);
        common(insertsAreDetected);
        final var rebuilt = insertsAreDetected.toBuilder().build();
    }

    static void othersDeletesAreVisible(final Context context, final OthersDeletesAreVisible othersDeletesAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(othersDeletesAreVisible, "othersDeletesAreVisible is null");
        log.debug("othersDeletesAreVisible: {}", othersDeletesAreVisible);
        common(othersDeletesAreVisible);
        final var rebuilt = othersDeletesAreVisible.toBuilder().build();
    }

    static void othersInsertsAreVisible(final Context context, final OthersInsertsAreVisible othersInsertsAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(othersInsertsAreVisible, "othersInsertsAreVisible is null");
        log.debug("othersInsertsAreVisible: {}", othersInsertsAreVisible);
        common(othersInsertsAreVisible);
        final var rebuilt = othersInsertsAreVisible.toBuilder().build();
    }

    static void othersUpdatesAreVisible(final Context context, final OthersUpdatesAreVisible othersUpdatesAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(othersUpdatesAreVisible, "othersUpdatesAreVisible is null");
        log.debug("othersUpdatesAreVisible: {}", othersUpdatesAreVisible);
        common(othersUpdatesAreVisible);
        final var rebuilt = othersUpdatesAreVisible.toBuilder().build();
    }

    static void ownDeletesAreVisible(final Context context, final OwnDeletesAreVisible ownDeletesAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(ownDeletesAreVisible, "ownDeletesAreVisible is null");
        log.debug("ownDeletesAreVisible: {}", ownDeletesAreVisible);
        common(ownDeletesAreVisible);
        final var rebuilt = ownDeletesAreVisible.toBuilder().build();
    }

    static void ownInsertsAreVisible(final Context context, final OwnInsertsAreVisible ownInsertsAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(ownInsertsAreVisible, "ownInsertsAreVisible is null");
        log.debug("ownInsertsAreVisible: {}", ownInsertsAreVisible);
        common(ownInsertsAreVisible);
        final var rebuilt = ownInsertsAreVisible.toBuilder().build();
    }

    static void ownUpdatesAreVisible(final Context context, final OwnUpdatesAreVisible ownUpdatesAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(ownUpdatesAreVisible, "ownUpdatesAreVisible is null");
        log.debug("ownUpdatesAreVisible: {}", ownUpdatesAreVisible);
        common(ownUpdatesAreVisible);
        final var rebuilt = ownUpdatesAreVisible.toBuilder().build();
    }

    static void updatesAreDetected(final Context context, final UpdatesAreDetected updatesAreDetected)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(updatesAreDetected, "updatesAreDetected is null");
        log.debug("updatesAreDetected: {}", updatesAreDetected);
        common(updatesAreDetected);
        final var rebuilt = updatesAreDetected.toBuilder().build();
    }

    static void schema(final Context context, final Schema schema) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schema, "schema is null");
        log.debug("schema: {}", schema);
        common(schema);
        final var rebuilt = schema.toBuilder().build();
        try {
            final var attributes = schema.getAttributes(context, "%", "%");
            for (final var attribute : attributes) {
                attribute(context, attribute);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var columns = schema.getColumns(context, "%", "%");
            for (final var column : columns) {
                column(context, column);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var functionColumns = schema.getFunctionColumns(context, "%", "%");
            for (final var functionColumn : functionColumns) {
                functionColumn(context, functionColumn);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var functions = schema.getFunctions(context, "%");
            for (final var function : functions) {
                function(context, function);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var functions = context.getFunctions(schema.getTableCatalog(), schema.getTableSchem(), "%");
            for (final var function : functions) {
                log.debug("function: {}", function);
                function(context, function);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var procedureColumns = schema.getProcedureColumns(context, "%", "%");
            for (final var procedureColumn : procedureColumns) {
                procedureColumn(context, procedureColumn);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var procedures = schema.getProcedures(context, "%");
            for (final var procedure : procedures) {
                procedure(context, procedure);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var pseudoColumns = schema.getPseudoColumns(context, "%", "%");
            for (final var pseudoColumn : pseudoColumns) {
                pseudoColumn(context, pseudoColumn);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported; getSchema", sqlfnse);
        }
        try {
            final var superTables = schema.getSuperTables(context, "%");
            for (final var superTable : superTables) {
                superTable(context, superTable);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var superTypes = schema.getSuperTypes(context, "%");
            for (final var superType : superTypes) {
                superType(context, superType);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var tablePrivileges = schema.getTablePrivileges(context, "%");
            for (final var tablePrivilege : tablePrivileges) {
                tablePrivilege(context, tablePrivilege);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var tables = schema.getTables(context, "%", null);
            for (final var table : tables) {
                table(context, table);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var udts = context.getUDTs(schema.getTableCatalog(), schema.getTableSchem(), "%", null);
            for (final var udt : udts) {
                log.debug("udt: {}", udt);
                udt(context, udt);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
    }

    static void function(final Context context, final Function function) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        log.debug("function: {}", function);
        common(function);
        final var functionColumns = function.getFunctionColumns(context, "%");
        for (final var functionColumn : functionColumns) {
            functionColumn(context, functionColumn);
        }
    }

    static void functionColumn(final Context context, final FunctionColumn functionColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionColumn, "functionColumn is null");
        log.debug("functionColumn: {}", functionColumn);
        common(functionColumn);
    }

    static void procedure(final Context context, final Procedure procedure) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedure, "procedure is null");
        log.debug("procedure: {}", procedure);
        common(procedure);
        final var procedureColumns = procedure.getProcedureColumns(context, "%");
        for (final var procedureColumn : procedureColumns) {
            procedureColumn(context, procedureColumn);
        }
    }

    static void superType(final Context context, final SuperType superType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superType, "superType is null");
        log.debug("superType: {}", superType);
        common(superType);
    }

    static void procedureColumn(final Context context, final ProcedureColumn procedureColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumn, "procedureColumn is null");
        log.debug("procedureColumn: {}", procedureColumn);
        common(procedureColumn);
    }

    static void table(final Context context, final Table table) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(table, "table is null");
        log.debug("table: {}", table);
        common(table);
        try {
            for (final var scope : BestRowIdentifier.scopes()) {
                final var bestRowIdentifier = table.getBestRowIdentifier(context, scope, true);
                for (final var bestRowIdentifier_ : bestRowIdentifier) {
                    bestRowIdentifier(context, bestRowIdentifier_);
                }
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var columnPrivileges = table.getColumnPrivileges(context, "%");
            for (final var columnPrivilege : columnPrivileges) {
                columnPrivilege(context, columnPrivilege);
            }
        } catch (SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var columns = table.getColumns(context, "%");
            for (final var column : columns) {
                log.debug("column: {}", column);
                column(context, column);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var exportedKeys = table.getExportedKeys(context);
            for (final var exportedKey : exportedKeys) {
                exportedKey(context, exportedKey);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        } catch (final SQLException sqle) {
            // https://github.com/xerial/sqlite-jdbc/issues/831
        }
        try {
            final var importedKeys = table.getImportedKeys(context);
            for (final var importedKey : importedKeys) {
                importedKey(context, importedKey);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            for (final boolean unique : new boolean[] {true, false}) {
                for (final boolean approximate : new boolean[] {true, false}) {
                    final var indexInfo = table.getIndexInfos(context, unique, approximate);
                    for (final var indexInfo_ : indexInfo) {
                        indexInfo(context, indexInfo_);
                    }
                }
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var primaryKeys = table.getPrimaryKeys(context);
            for (final var primaryKey : primaryKeys) {
                primaryKey(context, primaryKey);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        } catch (final SQLException sqle) {
            // https://github.com/xerial/sqlite-jdbc/issues/831
        }
        try {
            final var pseudoColumns = table.getPseudoColumns(context, "%");
            for (final var pseudoColumn : pseudoColumns) {
                pseudoColumn(context, pseudoColumn);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var tablePrivileges = table.getTablePrivileges(context);
            for (final var tablePrivilege : tablePrivileges) {
                tablePrivilege(context, tablePrivilege);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
        try {
            final var versionColumns = context.getVersionColumns(
                    table.getTableCat(), table.getTableSchem(), table.getTableName());
            for (final var versionColumn : versionColumns) {
                versionColumn(context, versionColumn);
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
    }

    static void tableType(final Context context, final TableType tableType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableType, "tableType is null");
        log.debug("tableType: {}", tableType);
        common(tableType);
    }

    static void typeInfo(final Context context, final TypeInfo typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        log.debug("typeInfo: {}", typeInfo);
        common(typeInfo);
    }

    static void column(final Context context, final Column column) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(column, "column is null");
        try {
            final var columnPrivileges = column.getColumnPrivileges(context);
            for (final var columnPrivilege : columnPrivileges) {
                log.debug("columnPrivilege: {}", columnPrivilege);
                columnPrivilege(context, columnPrivilege);
            }
        } catch (SQLFeatureNotSupportedException sqlfnse) {
            log.error("not supported", sqlfnse);
        }
    }

    static void columnPrivilege(final Context context, final ColumnPrivilege columnPrivilege) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columnPrivilege, "columnPrivilege is null");
        log.debug("columnPrivilege: {}", columnPrivilege);
        common(columnPrivilege);
        final var rebuilt = columnPrivilege.toBuilder().build();
    }

    static void exportedKey(final Context context, final ExportedKey exportedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKey, "exportedKey is null");
        log.debug("exportedKey: {}", exportedKey);
        common(exportedKey);
        final var rebuilt = exportedKey.toBuilder().build();
    }

    static void importedKey(final Context context, final ImportedKey importedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKey, "importedKey is null");
        log.debug("importedKey: {}", importedKey);
        common(importedKey);
    }

    static void indexInfo(final Context context, final IndexInfo indexInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(indexInfo, "indexInfo is null");
        log.debug("indexInfo: {}", indexInfo);
        common(indexInfo);
    }

    static void primaryKey(final Context context, final PrimaryKey primaryKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKey, "primaryKey is null");
        log.debug("primaryKey: {}", primaryKey);
        common(primaryKey);
    }

    static void pseudoColumn(final Context context, final PseudoColumn pseudoColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumn, "pseudoColumn is null");
        log.debug("pseudoColumn: {}", pseudoColumn);
        common(pseudoColumn);
        final var rebuilt = pseudoColumn.toBuilder().build();
    }

    static void superTable(final Context context, final SuperTable superTable) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTable, "superTable is null");
        common(superTable);
        final var rebuild = superTable.toBuilder().build();
    }

    static void tablePrivilege(final Context context, final TablePrivilege tablePrivilege) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tablePrivilege, "tablePrivilege is null");
        log.debug("tablePrivilege: {}", tablePrivilege);
        common(tablePrivilege);
    }

    static void versionColumn(final Context context, final VersionColumn versionColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(versionColumn, "versionColumn is null");
        log.debug("versionColumn: {}", versionColumn);
        common(versionColumn);
        final var rebuild = versionColumn.toBuilder().build();
    }

    static void udt(final Context context, final UDT udt) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udt, "udt is null");
        final var attributes = udt.getAttributes(context, "%");
        for (final var attribute : attributes) {
            attribute(context, attribute);
        }
    }

    private ContextTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
