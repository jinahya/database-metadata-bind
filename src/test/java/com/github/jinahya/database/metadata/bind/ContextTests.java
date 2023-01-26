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

    private static String databaseProductName;

    static void test(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        databaseProductName = context.databaseMetaData.getDatabaseProductName();
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
            assertThat(catalogs).isSortedAccordingTo(Catalog.COMPARING_TABLE_CAT);
            if (catalogs.isEmpty()) {
                catalogs.add(Catalog.builder().tableCat(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY).build());
            }
            catalogs(context, catalogs);
        }
        try {
            final var tableTypes = context.getTableTypes();
            tableTypes(context, tableTypes);
        } catch (final SQLException sqle) {
            log.error("failed; getTableTypes", sqle);
        }
        try {
            final var typeInfo = context.getTypeInfo();
            typeInfo(context, typeInfo);
        } catch (final SQLException sqle) {
            log.error("failed; getTypeInfo", sqle);
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
        for (final var catalog : catalogs) {
            catalog(context, catalog);
        }
    }

    static void catalog(final Context context, final Catalog catalog) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalog, "catalog is null");
        common(catalog);
        try {
            final var attributes = catalog.getAttributes(context, null, "%", "%");
            attributes(context, attributes);
        } catch (final SQLException sqle) {
            log.error("failed; getAttributes", sqle);
        }
        try {
            final var columns = catalog.getColumns(context, null, "%", "%");
            columns(context, columns);
        } catch (final SQLException sqle) {
            log.error("failed; getColumns", sqle);
        }
        try {
            final var functionColumns = catalog.getFunctionColumns(context, null, "%", "%");
            functionColumns(context, functionColumns);
        } catch (final SQLException sqle) {
            log.error("failed; getFunctionColumns", sqle);
        }
        try {
            final var functions = catalog.getFunctions(context, null, "%");
            functions(context, functions);
        } catch (final SQLException sqle) {
            log.error("failed; getFunctions", sqle);
        }
        try {
            final var pseudoColumns = catalog.getPseudoColumns(context, null, "%", "%");
            pseudoColumns(context, pseudoColumns);
        } catch (final SQLException sqle) {
            log.error("failed; getPseudoColumns", sqle);
        }
        try {
            final var procedureColumns = catalog.getProcedureColumns(context, null, "%", "%");
            procedureColumns(context, procedureColumns);
        } catch (final SQLException sqle) {
            log.error("failed; getProcedureColumns", sqle);
        }
        try {
            final var procedures = catalog.getProcedures(context, null, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            log.error("failed; getProcedures", sqle);
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
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            log.error("failed; getSchemas", sqle);
        }
        try {
            final var superTables = catalog.getSuperTables(context, "%", "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            log.error("failed; getSuperTables", sqle);
        }
        try {
            final var superTypes = catalog.getSuperTypes(context, "%", "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            log.error("failed; getSuperTables", sqle);
        }
        try {
            final var tablePrivileges = catalog.getTablePrivileges(context, null, "%");
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLException sqle) {
            log.error("failed; getTablePrivileges", sqle);
        }
        try {
            final var tables = catalog.getTables(context, null, "%", null);
            tables(context, tables);
        } catch (final SQLException sqle) {
            log.error("failed; getTables", sqle);
        }
        try {
            final var udts = catalog.getUDTs(context, null, "%", null);
            udts(context, udts);
        } catch (final SQLException sqle) {
            log.error("failed; getUDTs", sqle);
        }
    }

    static void crossReference(final Context context, List<? extends CrossReference> crossReference)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(crossReference, "crossReference is null");
        assertThat(crossReference).isSortedAccordingTo(
                CrossReference.COMPARING_FKTABLE_CAT_FKTABLE_SCHEM_FKTABLE_NAME_KEY_SEQ);
        for (final var crossReference_ : crossReference) {
            crossReference(context, crossReference_);
        }
    }

    static void crossReference(final Context context, CrossReference crossReference) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(crossReference, "crossReference is null");
        common(crossReference);
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

    static void schemas(final Context context, final List<? extends Schema> schemas) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schemas, "schemas is null");
        // https://sourceforge.net/p/hsqldb/bugs/1671/
//            assertThat(schemas).isSortedAccordingTo(Schema.COMPARING_TABLE_CATALOG_TABLE_SCHEM);
        for (final var schema : schemas) {
            schema(context, schema);
        }
    }

    static void schema(final Context context, final Schema schema) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schema, "schema is null");
        common(schema);
        try {
            final var attributes = schema.getAttributes(context, "%", "%");
            attributes(context, attributes);
        } catch (final SQLException sqle) {
            log.error("failed; getAttributes", sqle);
        }
        try {
            final var columns = schema.getColumns(context, "%", "%");
            columns(context, columns);
        } catch (final SQLException sqle) {
            log.error("failed; getColumns", sqle);
        }
        try {
            final var functionColumns = schema.getFunctionColumns(context, "%", "%");
            functionColumns(context, functionColumns);
        } catch (final SQLException sqle) {
            log.error("failed; getFunctionColumns", sqle);
        }
        try {
            final var functions = schema.getFunctions(context, "%");
            functions(context, functions);
        } catch (final SQLException sqle) {
            log.error("failed; getFunctions", sqle);
        }
        try {
            final var procedureColumns = schema.getProcedureColumns(context, "%", "%");
            procedureColumns(context, procedureColumns);
        } catch (final SQLException sqle) {
            log.error("failed; getProcedureColumns", sqle);
        }
        try {
            final var procedures = schema.getProcedures(context, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            log.error("failed; getProcedures", sqle);
        }
        try {
            final var pseudoColumns = schema.getPseudoColumns(context, "%", "%");
            pseudoColumns(context, pseudoColumns);
        } catch (final SQLException sqle) {
            log.error("failed; getPseudoColumns", sqle);
        }
        try {
            final var superTables = schema.getSuperTables(context, "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            log.error("failed; getSuperTables", sqle);
        }
        try {
            final var superTypes = schema.getSuperTypes(context, "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            log.error("failed; getSuperTypes", sqle);
        }
        try {
            final var tablePrivileges = schema.getTablePrivileges(context, "%");
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLException sqle) {
            log.error("failed; getTablePrivileges", sqle);
        }
        try {
            final var tables = schema.getTables(context, "%", null);
            tables(context, tables);
        } catch (final SQLException sqle) {
            log.error("failed; getTables", sqle);
        }
        try {
            final var udts = schema.getUDTs(context, "%", null);
            udts(context, udts);
        } catch (final SQLException sqle) {
            log.error("failed; getUDTs", sqle);
        }
    }

    static void functions(final Context context, final List<? extends Function> functions) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functions, "functions is null");
        assertThat(functions).isSortedAccordingTo(
                Function.COMPARING_FUNCTION_CAT_FUNCTION_SCHEM_FUNCTION_NAME_SPECIFIC_NAME);
        for (final var function : functions) {
            function(context, function);
        }
    }

    static void function(final Context context, final Function function) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        common(function);
        final var functionColumns = function.getFunctionColumns(context, "%");
        functionColumns(context, functionColumns);
    }

    static void functionColumns(final Context context, final List<? extends FunctionColumn> functionColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionColumns, "functionColumns is null");
        assertThat(functionColumns).isSortedAccordingTo(
                FunctionColumn.COMPARING_FUNCTION_CAT_FUNCTION_SCHEM_FUNCTION_NAME_SPECIFIC_NAME);
        for (final var functionColumn : functionColumns) {
            functionColumn(context, functionColumn);
        }
    }

    static void functionColumn(final Context context, final FunctionColumn functionColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionColumn, "functionColumn is null");
        common(functionColumn);
    }

    static void procedures(final Context context, final List<? extends Procedure> procedures) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedures, "procedures is null");
        assertThat(procedures).isSortedAccordingTo(
                Procedure.COMPARING_PROCEDURE_CAT_PROCEDURE_SCHEM_PROCEDURE_NAME_SPECIFIC_NAME);
        for (final var procedure : procedures) {
            procedure(context, procedure);
        }
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
        procedureColumns(context, procedureColumns);
    }

    static void procedureColumns(final Context context, final List<? extends ProcedureColumn> procedureColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumns, "procedureColumns is null");
        assertThat(procedureColumns).isSortedAccordingTo(
                ProcedureColumn.COMPARING_PROCEDURE_CAT_PROCEDURE_SCHEM_PROCEDURE_NAME_SPECIFIC_NAME);
        for (final var procedureColumn : procedureColumns) {
            procedureColumn(context, procedureColumn);
        }
    }

    static void procedureColumn(final Context context, final ProcedureColumn procedureColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumn, "procedureColumn is null");
        common(procedureColumn);
    }

    static void superTypes(final Context context, final List<? extends SuperType> superTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superTypes, "superTypes is null");
        for (final var superType : superTypes) {
            superType(context, superType);
        }
    }

    static void superType(final Context context, final SuperType superType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(superType, "superType is null");
        common(superType);
    }

    static void tables(final Context context, final List<? extends Table> tables) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tables, "tables is null");
        if (!databaseProductName.equals(MemoryHsqlTest.DATABASE_PRODUCT_NAME)) {
            // https://sourceforge.net/p/hsqldb/bugs/1672/
            assertThat(tables).isSortedAccordingTo(Table.COMPARING_TABLE_TYPE_TABLE_CAT_TABLE_SCHEM_TABLE_NAME);
        }
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
                final var bestRowIdentifier = table.getBestRowIdentifier(context, scope, true);
                assertThat(bestRowIdentifier).isSortedAccordingTo(BestRowIdentifier.COMPARING_SCOPE);
                bestRowIdentifier(context, bestRowIdentifier);
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
            columns(context, columns);
        } catch (final SQLException sqle) {
            log.error("failed; getColumns", sqle);
        }
        try {
            final var exportedKeys = table.getExportedKeys(context);
            exportedKeys(context, exportedKeys);
        } catch (final SQLException sqle) {
            // https://github.com/xerial/sqlite-jdbc/issues/831
            log.error("failed; getExportedKeys", sqle);
        }
        try {
            final var importedKeys = table.getImportedKeys(context);
            importedKeys(context, importedKeys);
        } catch (final SQLException sqle) {
            log.error("failed; getImportedKeys", sqle);
        }
        try {
            for (final boolean unique : new boolean[] {true, false}) {
                for (final boolean approximate : new boolean[] {true, false}) {
                    final var indexInfo = table.getIndexInfos(context, unique, approximate);
                    indexInfo(context, indexInfo);
                }
            }
        } catch (final SQLException sqle) {
            log.error("not supported", sqle);
        }
        try {
            final var primaryKeys = table.getPrimaryKeys(context);
            primaryKeys(context, primaryKeys);
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
            pseudoColumns(context, pseudoColumns);
        } catch (final SQLException sqle) {
            log.error("failed; getPseudoColumns", sqle);
        }
        try {
            final var tablePrivileges = table.getTablePrivileges(context);
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLException sqle) {
            log.error("failed; getTablePrivileges", sqle);
        }
        try {
            final var versionColumns = table.getVersionColumns(context);
            versionColumns(context, versionColumns);
        } catch (final SQLException sqle) {
            log.error("failed; getVersionColumns", sqle);
        }
    }

    static void tableTypes(final Context context, final List<? extends TableType> tableTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableTypes, "tableTypes is null");
        assertThat(tableTypes).isSortedAccordingTo(TableType.COMPARING_TABLE_TYPE);
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
        if (!databaseProductName.equals(MemorySqliteTest.DATABASE_PRODUCT_NAME)) {
            // https://github.com/xerial/sqlite-jdbc/issues/832 fixed! awaiting a new version...
            assertThat(typeInfo).isSortedAccordingTo(TypeInfo.COMPARING_DATA_TYPE);
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

    static void columns(final Context context, final List<? extends Column> columns) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columns, "columns is null");
        assertThat(columns).isSortedAccordingTo(Column.COMPARING_TABLE_CAT_TABLE_SCHEM_TABLE_NAME_ORDINAL_POSITION);
        for (final var column : columns) {
            column(context, column);
        }
    }

    static void column(final Context context, final Column column) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(column, "column is null");
        common(column);
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

    static void exportedKeys(final Context context, final List<? extends ExportedKey> exportedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKeys, "exportedKeys is null");
        assertThat(exportedKeys).isSortedAccordingTo(TableKey.COMPARING_FKTABLE_CAT_FKTABLE_SCHEM_FKTABLE_NAME_KEY_SEQ);
        for (final var exportedKey : exportedKeys) {
            exportedKey(context, exportedKey);
        }
    }

    static void exportedKey(final Context context, final ExportedKey exportedKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKey, "exportedKey is null");
        common(exportedKey);
    }

    static void importedKeys(final Context context, final List<? extends ImportedKey> importedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKeys, "importedKeys is null");
        assertThat(importedKeys).isSortedAccordingTo(TableKey.COMPARING_FKTABLE_CAT_FKTABLE_SCHEM_FKTABLE_NAME_KEY_SEQ);
        for (final var importedKey : importedKeys) {
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
        assertThat(indexInfo).isSortedAccordingTo(IndexInfo.COMPARING_NON_UNIQUE_TYPE_INDEX_NAME_ORDINAL_POSITION);
        for (final var indexInfo_ : indexInfo) {
            indexInfo(context, indexInfo_);
        }
    }

    static void indexInfo(final Context context, final IndexInfo indexInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(indexInfo, "indexInfo is null");
        common(indexInfo);
    }

    static void primaryKeys(final Context context, final List<? extends PrimaryKey> primaryKeys) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKeys, "primaryKeys is null");
        if (!databaseProductName.equals(MemoryHsqlTest.DATABASE_PRODUCT_NAME)) {
            // https://sourceforge.net/p/hsqldb/bugs/1673/
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
        assertThat(tablePrivileges).isSortedAccordingTo(TablePrivilege.COMPARING_TABLE_CAT_TABLE_SCHEM_PRIVILEGE);
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
        for (final var udt : udts) {
            udt(context, udt);
        }
    }

    static void udt(final Context context, final UDT udt) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udt, "udt is null");
        common(udt);
        final var attributes = udt.getAttributes(context, "%");
        attributes(context, attributes);
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
