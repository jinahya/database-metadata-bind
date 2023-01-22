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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTests {

    private static void common(final AbstractMetadataType value) {
        Objects.requireNonNull(value, "value is null");
        final var string = value.toString();
        final var hashCode = value.hashCode();
        if (!value.getUnmappedValues().isEmpty()) {
            log.info("has unmapped values: {}", value);
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

    static void test(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        List<Catalog> catalogs = null;
        {
            try {
                catalogs = context.getCatalogs();
            } catch (final SQLException sqle) {
                log.error("failed; getCatalogs", sqle);
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
        } catch (final SQLException sqle) {
            log.error("failed; getTableTypes", sqle);
        }
        try {
            final var typeInfos = context.getTypeInfo();
            for (final var typeInfo_ : typeInfos) {
                typeInfo(context, typeInfo_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var deletesAreDetected = DeletesAreDetected.getAllValues(context);
            for (final var deletesAreDetected_ : deletesAreDetected) {
                deletesAreDetected(context, deletesAreDetected_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var insertsAreDetected = InsertsAreDetected.getAllValues(context);
            for (final var insertsAreDetected_ : insertsAreDetected) {
                insertsAreDetected(context, insertsAreDetected_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var updatesAreDetected = UpdatesAreDetected.getAllValues(context);
            for (final var updatesAreDetected_ : updatesAreDetected) {
                updatesAreDetected(context, updatesAreDetected_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var othersDeletesAreVisible = OthersDeletesAreVisible.getAllValues(context);
            for (final var othersDeletesAreVisible_ : othersDeletesAreVisible) {
                othersDeletesAreVisible(context, othersDeletesAreVisible_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var othersInsertsAreVisible = OthersInsertsAreVisible.getAllValues(context);
            for (final var othersInsertsAreVisible_ : othersInsertsAreVisible) {
                othersInsertsAreVisible(context, othersInsertsAreVisible_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var othersUpdatesAreVisible = OthersUpdatesAreVisible.getAllValues(context);
            for (final var othersUpdatesAreVisible_ : othersUpdatesAreVisible) {
                othersUpdatesAreVisible(context, othersUpdatesAreVisible_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var ownDeletesAreVisible = OwnDeletesAreVisible.getAllValues(context);
            for (final var ownDeletesAreVisible_ : ownDeletesAreVisible) {
                ownDeletesAreVisible(context, ownDeletesAreVisible_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var ownInsertsAreVisible = OwnInsertsAreVisible.getAllValues(context);
            for (final var ownInsertsAreVisible_ : ownInsertsAreVisible) {
                ownInsertsAreVisible(context, ownInsertsAreVisible_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        try {
            final var ownUpdatesAreVisible = OwnUpdatesAreVisible.getAllValues(context);
            for (final var ownUpdatesAreVisible_ : ownUpdatesAreVisible) {
                ownUpdatesAreVisible(context, ownUpdatesAreVisible_);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfos", sqle);
        }
        log.debug("supportsTransactions: {}", context.databaseMetaData.supportsTransactions());
        try {
            log.debug("supportsTransactions: {}", context.databaseMetaData.supportsTransactions());
        } catch (final SQLException sqle) {
            log.error("failed; supportsTransactions", sqle);
        }
        try {
            log.debug("supportsUnion: {}", context.databaseMetaData.supportsUnion());
        } catch (final SQLException sqle) {
            log.error("failed; supportsUnion", sqle);
        }
        try {
            log.debug("supportsUnionAll: {}", context.databaseMetaData.supportsUnionAll());
        } catch (final SQLException sqle) {
            log.error("failed; supportsUnionAll", sqle);
        }
        try {
            log.debug("usesLocalFilePerTable: {}", context.databaseMetaData.usesLocalFilePerTable());
        } catch (final SQLException sqle) {
            log.error("failed; usesLocalFilePerTable", sqle);
        }
        try {
            log.debug("usesLocalFiles: {}", context.databaseMetaData.usesLocalFiles());
        } catch (final SQLException sqle) {
            log.error("failed; usesLocalFiles", sqle);
        }
    }

    static void attribute(final Context context, final Attribute attribute) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attribute, "attribute is null");
        common(attribute);
    }

    static void bestRowIdentifier(final Context context, final BestRowIdentifier bestRowIdentifier)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(bestRowIdentifier, "bestRowIdentifier is null");
        common(bestRowIdentifier);
    }

    static void catalog(final Context context, final Catalog catalog) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalog, "catalog is null");
        common(catalog);
        try {
            final var attributes = catalog.getAttributes(context, null, "%", "%");
            for (final var attribute : attributes) {
                attribute(context, attribute);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSchema", sqle);
        }
        try {
            final var columns = catalog.getColumns(context, null, "%", "%");
            for (final var column : columns) {
                column(context, column);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSchema", sqle);
        }
        try {
            final var functionColumns = catalog.getFunctionColumns(context, null, "%", "%");
            for (final var functionColumn : functionColumns) {
                functionColumn(context, functionColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSchema", sqle);
        }
        try {
            final var functions = catalog.getFunctions(context, null, "%");
            for (final var function : functions) {
                function(context, function);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSchema", sqle);
        }
        try {
            final var procedureColumns = catalog.getProcedureColumns(context, null, "%", "%");
            for (final var procedureColumn : procedureColumns) {
                procedureColumn(context, procedureColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSchema", sqle);
        }
        try {
            final var procedures = catalog.getProcedures(context, null, "%");
            for (final var procedure : procedures) {
                procedure(context, procedure);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getProcedures", sqle);
        }
        try {
            final var pseudoColumns = catalog.getPseudoColumns(context, null, "%", "%");
            for (final var pseudoColumn : pseudoColumns) {
                pseudoColumn(context, pseudoColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getPseudoColumns", sqle);
        }
        try {
            final var procedureColumns = catalog.getProcedureColumns(context, null, "%", "%");
            for (final var procedureColumn : procedureColumns) {
                procedureColumn(context, procedureColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getProcedureColumns", sqle);
        }
        try {
            final var procedures = catalog.getProcedures(context, null, "%");
            for (final var procedure : procedures) {
                procedure(context, procedure);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getProcedures", sqle);
        }
        try {
            final var schemas = catalog.getSchemas(context, null);
            assertThat(schemas).allSatisfy(s -> {
//                assertThat(s.getTableCatalog()).isEqualTo(catalog.getTableCat()); // derby
            });
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
        } catch (final SQLException sqle) {
            log.error("failed; getSchema", sqle);
        }
        try {
            final var superTables = catalog.getSuperTables(context, "%", "%");
            for (final var superTable : superTables) {
                superTable(context, superTable);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSuperTables", sqle);
        }
        try {
            final var superTypes = catalog.getSuperTypes(context, "%", "%");
            for (final var superType : superTypes) {
                superType(context, superType);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSuperTables", sqle);
        }
        try {
            final var tablePrivileges = catalog.getTablePrivileges(context, null, "%");
            for (final var tablePrivilege : tablePrivileges) {
                tablePrivilege(context, tablePrivilege);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTablePrivileges", sqle);
        }
        try {
            final var tables = catalog.getTables(context, null, "%", null);
            for (final var table : tables) {
                table(context, table);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTables", sqle);
        }
        try {
            final var udts = catalog.getUDTs(context, null, "%", null);
            for (final var udt : udts) {
                udt(context, udt);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getUDTs", sqle);
        }
    }

    static void deletesAreDetected(final Context context, final DeletesAreDetected deletesAreDetected)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(deletesAreDetected, "deletesAreDetected is null");
        common(deletesAreDetected);
    }

    static void insertsAreDetected(final Context context, final InsertsAreDetected insertsAreDetected)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(insertsAreDetected, "insertsAreDetected is null");
        common(insertsAreDetected);
    }

    static void othersDeletesAreVisible(final Context context, final OthersDeletesAreVisible othersDeletesAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(othersDeletesAreVisible, "othersDeletesAreVisible is null");
        common(othersDeletesAreVisible);
    }

    static void othersInsertsAreVisible(final Context context, final OthersInsertsAreVisible othersInsertsAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(othersInsertsAreVisible, "othersInsertsAreVisible is null");
        common(othersInsertsAreVisible);
    }

    static void othersUpdatesAreVisible(final Context context, final OthersUpdatesAreVisible othersUpdatesAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(othersUpdatesAreVisible, "othersUpdatesAreVisible is null");
        common(othersUpdatesAreVisible);
    }

    static void ownDeletesAreVisible(final Context context, final OwnDeletesAreVisible ownDeletesAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(ownDeletesAreVisible, "ownDeletesAreVisible is null");
        common(ownDeletesAreVisible);
    }

    static void ownInsertsAreVisible(final Context context, final OwnInsertsAreVisible ownInsertsAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(ownInsertsAreVisible, "ownInsertsAreVisible is null");
        common(ownInsertsAreVisible);
    }

    static void ownUpdatesAreVisible(final Context context, final OwnUpdatesAreVisible ownUpdatesAreVisible)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(ownUpdatesAreVisible, "ownUpdatesAreVisible is null");
        common(ownUpdatesAreVisible);
    }

    static void updatesAreDetected(final Context context, final UpdatesAreDetected updatesAreDetected)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(updatesAreDetected, "updatesAreDetected is null");
        common(updatesAreDetected);
    }

    static void schema(final Context context, final Schema schema) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schema, "schema is null");
        common(schema);
        try {
            final var attributes = schema.getAttributes(context, "%", "%");
            for (final var attribute : attributes) {
                attribute(context, attribute);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getAttributes", sqle);
        }
        try {
            final var columns = schema.getColumns(context, "%", "%");
            for (final var column : columns) {
                column(context, column);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getColumns", sqle);
        }
        try {
            final var functionColumns = schema.getFunctionColumns(context, "%", "%");
            for (final var functionColumn : functionColumns) {
                functionColumn(context, functionColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getFunctionColumns", sqle);
        }
        try {
            final var functions = schema.getFunctions(context, "%");
            for (final var function : functions) {
                function(context, function);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getFunctions", sqle);
        }
        try {
            final var procedureColumns = schema.getProcedureColumns(context, "%", "%");
            for (final var procedureColumn : procedureColumns) {
                procedureColumn(context, procedureColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getProcedureColumns", sqle);
        }
        try {
            final var procedures = schema.getProcedures(context, "%");
            for (final var procedure : procedures) {
                procedure(context, procedure);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getProcedures", sqle);
        }
        try {
            final var pseudoColumns = schema.getPseudoColumns(context, "%", "%");
            for (final var pseudoColumn : pseudoColumns) {
                pseudoColumn(context, pseudoColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getPseudoColumns", sqle);
        }
        try {
            final var superTables = schema.getSuperTables(context, "%");
            for (final var superTable : superTables) {
                superTable(context, superTable);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSuperTables", sqle);
        }
        try {
            final var superTypes = schema.getSuperTypes(context, "%");
            for (final var superType : superTypes) {
                superType(context, superType);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getSuperTypes", sqle);
        }
        try {
            final var tablePrivileges = schema.getTablePrivileges(context, "%");
            for (final var tablePrivilege : tablePrivileges) {
                tablePrivilege(context, tablePrivilege);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTablePrivileges", sqle);
        }
        try {
            final var tables = schema.getTables(context, "%", null);
            for (final var table : tables) {
                table(context, table);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTables", sqle);
        }
        try {
            final var udts = schema.getUDTs(context, "%", null);
            for (final var udt : udts) {
                udt(context, udt);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getUDTs", sqle);
        }
    }

    static void function(final Context context, final Function function) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        common(function);
        final var functionColumns = function.getFunctionColumns(context, "%");
        for (final var functionColumn : functionColumns) {
            functionColumn(context, functionColumn);
        }
    }

    static void functionColumn(final Context context, final FunctionColumn functionColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionColumn, "functionColumn is null");
        common(functionColumn);
    }

    static void procedure(final Context context, final Procedure procedure) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedure, "procedure is null");
        common(procedure);
        final var procedureColumns = procedure.getProcedureColumns(context, "%");
        assertThat(procedureColumns).allSatisfy(pc -> {
//            assertThat(pc.getProcedureCat()).isEqualTo(procedure.getProcedureCat()); // derby
            assertThat(pc.getProcedureSchem()).isEqualTo(procedure.getProcedureSchem());
            assertThat(pc.getProcedureName()).isEqualTo(procedure.getProcedureName());
        });
        for (final var procedureColumn : procedureColumns) {
            procedureColumn(context, procedureColumn);
        }
    }

    static void superType(final Context context, final SuperType superType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superType, "superType is null");
        common(superType);
    }

    static void procedureColumn(final Context context, final ProcedureColumn procedureColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumn, "procedureColumn is null");
        common(procedureColumn);
    }

    static void table(final Context context, final Table table) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(table, "table is null");
        common(table);
        try {
            for (final var scope : BestRowIdentifier.scopes()) {
                final var bestRowIdentifier = table.getBestRowIdentifier(context, scope, true);
                for (final var bestRowIdentifier_ : bestRowIdentifier) {
                    bestRowIdentifier(context, bestRowIdentifier_);
                }
            }
        } catch (final SQLException sqle) {
            log.error("failed; getBestRowIdentifier", sqle);
        }
        try {
            final var columnPrivileges = table.getColumnPrivileges(context, "%");
            for (final var columnPrivilege : columnPrivileges) {
                columnPrivilege(context, columnPrivilege);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getColumnPrivileges", sqle);
        }
        try {
            final var columns = table.getColumns(context, "%");
            for (final var column : columns) {
                column(context, column);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getColumns", sqle);
        }
        try {
            final var exportedKeys = table.getExportedKeys(context);
            for (final var exportedKey : exportedKeys) {
                exportedKey(context, exportedKey);
            }
        } catch (final SQLException sqle) {
            // https://github.com/xerial/sqlite-jdbc/issues/831
            log.error("failed; getExportedKeys", sqle);
        }
        try {
            final var importedKeys = table.getImportedKeys(context);
            for (final var importedKey : importedKeys) {
                importedKey(context, importedKey);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getImportedKeys", sqle);
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
        } catch (final SQLException sqle) {
            log.error("not supported", sqle);
        }
        try {
            final var primaryKeys = table.getPrimaryKeys(context);
            for (final var primaryKey : primaryKeys) {
                primaryKey(context, primaryKey);
            }
        } catch (final SQLException sqle) {
            // https://github.com/xerial/sqlite-jdbc/issues/831
            log.error("failed; getPrimaryKeys", sqle);
        }
        try {
            final var pseudoColumns = table.getPseudoColumns(context, "%");
            assertThat(pseudoColumns).allSatisfy(pc -> {
                assertThat(pc.getTableCat()).isEqualTo(table.getTableCat());
                assertThat(pc.getTableSchem()).isEqualTo(table.getTableSchem());
            });
            for (final var pseudoColumn : pseudoColumns) {
                pseudoColumn(context, pseudoColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getPseudoColumns", sqle);
        }
        try {
            final var tablePrivileges = table.getTablePrivileges(context);
            for (final var tablePrivilege : tablePrivileges) {
                tablePrivilege(context, tablePrivilege);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getTablePrivileges", sqle);
        }
        try {
            final var versionColumns = table.getVersionColumns(context);
            assertThat(versionColumns).allSatisfy(vc -> {
            });
            for (final var versionColumn : versionColumns) {
                versionColumn(context, versionColumn);
            }
        } catch (final SQLException sqle) {
            log.error("failed; getVersionColumns", sqle);
        }
    }

    static void tableType(final Context context, final TableType tableType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableType, "tableType is null");
        common(tableType);
    }

    static void typeInfo(final Context context, final TypeInfo typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        common(typeInfo);
    }

    static void column(final Context context, final Column column) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(column, "column is null");
        try {
            final var columnPrivileges = column.getColumnPrivileges(context);
            for (final var columnPrivilege : columnPrivileges) {
                columnPrivilege(context, columnPrivilege);
            }
        } catch (SQLException sqle) {
            log.error("failed; getColumnPrivileges", sqle);
        }
    }

    static void columnPrivilege(final Context context, final ColumnPrivilege columnPrivilege) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columnPrivilege, "columnPrivilege is null");
        common(columnPrivilege);
    }

    static void exportedKey(final Context context, final ExportedKey exportedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKey, "exportedKey is null");
        common(exportedKey);
    }

    static void importedKey(final Context context, final ImportedKey importedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKey, "importedKey is null");
        common(importedKey);
    }

    static void indexInfo(final Context context, final IndexInfo indexInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(indexInfo, "indexInfo is null");
        common(indexInfo);
    }

    static void primaryKey(final Context context, final PrimaryKey primaryKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKey, "primaryKey is null");
        common(primaryKey);
    }

    static void pseudoColumn(final Context context, final PseudoColumn pseudoColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumn, "pseudoColumn is null");
        common(pseudoColumn);
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
        common(tablePrivilege);
    }

    static void versionColumn(final Context context, final VersionColumn versionColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(versionColumn, "versionColumn is null");
        common(versionColumn);
        final var rebuild = versionColumn.toBuilder().build();
    }

    static void udt(final Context context, final UDT udt) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udt, "udt is null");
        common(udt);
        final var attributes = udt.getAttributes(context, "%");
        for (final var attribute : attributes) {
            attribute(context, attribute);
        }
    }

    private ContextTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
