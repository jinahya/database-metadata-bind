package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.invoke.MethodHandles;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Setter
@Getter
@EqualsAndHashCode
public final class Metadata {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public static Metadata newInstance(final Context context) {
        final Metadata instance = new Metadata();
        try {
            context.addAttributes(null, null, "%", "%", instance.attributes);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get columns");
        }
        for (final int scope : new int[] {DatabaseMetaData.bestRowTemporary, DatabaseMetaData.bestRowTransaction,
                                          DatabaseMetaData.bestRowSession}) {
            instance.bestRowIdentifiers.put(scope, new HashMap<>());
            for (final boolean nullable : new boolean[] {true, false}) {
                try {
                    instance.getBestRowIdentifiers().get(scope).put(
                            nullable,
                            context.addBestRowIdentifier(null, null, "%", scope, nullable, new ArrayList<>())
                    );
                } catch (final SQLException sqle) {
                    logger.log(Level.SEVERE, sqle, () -> "failed to get bestRowIdentifiers");
                }
            }
        }
        try {
            context.addCatalogs(instance.catalogs);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get catalogs");
        }
        try {
            context.addClientInfoProperties(instance.clientInfoProperties);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get clientInfoProperties");
        }
        try {
            context.addColumns(null, null, "%", "%", instance.columns);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get columns");
        }
        try {
            context.addColumnPrivileges(null, null, "%", "%", instance.columnPrivileges);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get columns");
        }
        try {
            context.addCrossReference(null, null, "%", null, null, "%", instance.crossReferences);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get columns");
        }
        try {
            context.addFunctions(null, null, "%", instance.functions);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get exportedKeys");
        }
        try {
            context.addFunctionColumns(null, null, "%", "%", instance.functionColumns);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get functionColumns");
        }
        try {
            context.addImportedKeys(null, null, "%", instance.importedKeys);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get importedKeys");
        }
        for (final boolean unique : new boolean[] {true, false}) {
            instance.indexInfo.put(unique, new HashMap<>());
            for (final boolean approximate : new boolean[] {true, false}) {
                try {
                    instance.indexInfo.get(unique).put(
                            approximate,
                            context.addIndexInfo(null, null, "%", unique, approximate, new ArrayList<>())
                    );
                } catch (final SQLException sqle) {
                    logger.log(Level.SEVERE, sqle, () -> "failed to get indexInfo");
                }
            }
        }
        try {
            context.addPrimaryKeys(null, null, "%", instance.primaryKeys);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get primaryKeys");
        }
        try {
            context.addProcedures(null, null, "%", instance.procedures);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get procedures");
        }
        try {
            context.addProcedureColumns(null, null, "%", "%", instance.procedureColumns);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get procedureColumns");
        }
        try {
            context.addPseudoColumns(null, null, "%", "%", instance.pseudoColumns);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get pseudoColumns");
        }
        try {
            context.addSchemas(null, "%", instance.schemas);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get schemas");
        }
        try {
            context.addSuperTables(null, null, "%", instance.superTables);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get superTables");
        }
        try {
            context.addSuperTypes(null, null, "%", instance.superTypes);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get superTypes");
        }
        try {
            context.addTables(null, null, "%", null, instance.tables);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get tables");
        }
        try {
            context.addTablePrivileges(null, null, "%", instance.tablePrivileges);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get tablePrivileges");
        }
        try {
            context.addTableTypes(instance.tableTypes);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get tablePrivileges");
        }
        try {
            context.addTypeInfo(instance.typeInfo);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get typeInfo");
        }
        try {
            context.addUDTs(null, null, "%", null, instance.UDTs);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get UDTs");
        }
        try {
            context.addVersionColumns(null, null, "%", instance.versionColumns);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get versionColumns");
        }
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Metadata() {
        super();
    }

    // ------------------------------------------------------------------------------------------------------ attributes

    // -----------------------------------------------------------------------------------------------------------------
    private final List<Attribute> attributes = new ArrayList<>();

    private final Map<Integer, Map<Boolean, List<BestRowIdentifier>>> bestRowIdentifiers = new HashMap<>();

    private final List<Catalog> catalogs = new ArrayList<>();

    private final List<ClientInfoProperty> clientInfoProperties = new ArrayList<>();

    private final List<Column> columns = new ArrayList<>();

    private final List<ColumnPrivilege> columnPrivileges = new ArrayList<>();

    private final List<CrossReference> crossReferences = new ArrayList<>();

    private final List<ExportedKey> exportedKeys = new ArrayList<>();

    private final List<Function> functions = new ArrayList<>();

    private final List<FunctionColumn> functionColumns = new ArrayList<>();

    private final List<ImportedKey> importedKeys = new ArrayList<>();

    private final Map<Boolean, Map<Boolean, List<IndexInfo>>> indexInfo = new HashMap<>();

    private final List<PrimaryKey> primaryKeys = new ArrayList<>();

    private final List<Procedure> procedures = new ArrayList<>();

    private final List<ProcedureColumn> procedureColumns = new ArrayList<>();

    private final List<PseudoColumn> pseudoColumns = new ArrayList<>();

    private final List<Schema> schemas = new ArrayList<>();

    private final List<SuperTable> superTables = new ArrayList<>();

    private final List<SuperType> superTypes = new ArrayList<>();

    private final List<Table> tables = new ArrayList<>();

    private final List<TablePrivilege> tablePrivileges = new ArrayList<>();

    private final List<TableType> tableTypes = new ArrayList<>();

    private final List<TypeInfo> typeInfo = new ArrayList<>();

    private final List<UDT> UDTs = new ArrayList<>();

    private final List<VersionColumn> versionColumns = new ArrayList<>();
}
