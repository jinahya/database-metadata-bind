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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@XmlRootElement
@Getter
public final class Metadata implements Serializable {

    private static final long serialVersionUID = -1427536135902153234L;

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------
    public static Metadata newInstance(final Context context) {
        final Metadata instance = new Metadata();
        try {
            context.addAttributes(null, null, "%", "%", instance.attributes);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get attributes");
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
            logger.log(Level.SEVERE, sqle, () -> "failed to get columnsPrivileges");
        }
        try {
            context.addCrossReference(null, null, "%", null, null, "%", instance.crossReference_);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to get crossReference");
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
                    logger.log(Level.SEVERE, sqle, () -> "failed to getIndexInfo");
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
            context.addTypeInfo(instance.typeInfo_);
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
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.numericFunctions.addAll(context.getNumericFunctions());
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to getNumericFunctions");
        }
        try {
            instance.SQLKeywords.addAll(context.getSQLKeywords());
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to getSQLKeywords");
        }
        try {
            instance.stringFunctions.addAll(context.getStringFunctions());
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to getStringFunctions");
        }
        try {
            instance.systemFunctions.addAll(context.getSystemFunctions());
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to getSystemFunctions");
        }
        try {
            instance.timeDateFunctions.addAll(context.getTimeDateFunctions());
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE, sqle, () -> "failed to getTimeDateFunctions");
        }
        // -------------------------------------------------------------------------------------------------------------
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Metadata() {
        super();
    }

    // ------------------------------------------------------------------------------------------------------ attributes
    public Stream<Attribute> getAttributesOf(final UDT udt) {
        Objects.requireNonNull(udt, "udt is null");
        return getAttributes()
                .stream()
                .filter(v -> Attribute.IS_OF.test(v, udt));
    }

    // ---------------------------------------------------------------------------------------------- bestRowIdentifiers
    public Stream<BestRowIdentifier> getBestRowIdentifiersOf(final int scope, final boolean nullable, final Table table,
                                                             final Table... otherTables) {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(otherTables, "otherTables is null");
        return getBestRowIdentifiers()
                .computeIfAbsent(scope, k -> new HashMap<>())
                .computeIfAbsent(nullable, k -> new ArrayList<>())
                .stream()
                .filter(v -> {
                    if (v.isOf(table)) {
                        return true;
                    }
                    for (final Table otherTable : otherTables) {
                        if (v.isOf(otherTable)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    // -------------------------------------------------------------------------------------------------------- catalogs
    List<Catalog> getCatalogs_() {
        final List<Catalog> catalogs = getCatalogs();
        if (catalogs.isEmpty()) {
            catalogs.add(Catalog.of(null));
        }
        return catalogs;
    }

    // -------------------------------------------------------------------------------------------- clientInfoProperties
    public Stream<Column> getColumnsOf(final Table table, final Table... otherTables) {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(otherTables, "otherTables is null");
        return getColumns()
                .stream()
                .filter(v -> {
                    if (v.isOf(table)) {
                        return true;
                    }
                    for (final Table otherTable : otherTables) {
                        if (v.isOf(otherTable)) {
                            return true;
                        }
                    }
                    return false;
                });
    }
    // --------------------------------------------------------------------------------------------------------- columns

    // ------------------------------------------------------------------------------------------------ columnPrivileges
    public Stream<ColumnPrivilege> getColumnPrivilegesOf(final Column column, final Column... otherColumns) {
        return getColumnPrivileges().stream().filter(v -> {
            if (v.isOf(column)) {
                return true;
            }
            for (final Column otherColumn : otherColumns) {
                if (v.isOf(otherColumn)) {
                    return true;
                }
            }
            return false;
        });
    }

    // -------------------------------------------------------------------------------------------------- crossReference
    public Stream<CrossReference> getCrossReferenceOfPktable(final Table pktable,
                                                             final Table... otherPktables) {
        Objects.requireNonNull(pktable, "pktable is null");
        Objects.requireNonNull(otherPktables, "otherPktables is null");
        return getCrossReference_().stream()
                .filter(v -> {
                    if (v.isOfPktable(pktable)) {
                        return true;
                    }
                    for (final Table otherPktable : otherPktables) {
                        if (v.isOfPktable(otherPktable)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    public Stream<CrossReference> getCrossReferenceOfFktable(final Table fktable,
                                                             final Table... otherFktables) {
        Objects.requireNonNull(fktable, "fktable is null");
        Objects.requireNonNull(otherFktables, "otherFktables is null");
        return getCrossReference_().stream()
                .filter(v -> {
                    if (v.isOfFktable(fktable)) {
                        return true;
                    }
                    for (final Table otherFktable : otherFktables) {
                        if (v.isOfFktable(otherFktable)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    // ----------------------------------------------------------------------------------------------------- exportedKey
    public Stream<ExportedKey> getExportedKeysOfPktable(final Table pktable) {
        Objects.requireNonNull(pktable, "pktable is null");
        return getExportedKeys().stream().filter(v -> TableKey.IS_OF_PKTABLE.test(v, pktable));
    }

    public Stream<ExportedKey> getExportedKeysOfFktable(final Table fktable) {
        Objects.requireNonNull(fktable, "fktable is null");
        return getExportedKeys().stream().filter(v -> TableKey.IS_OF_FKTABLE.test(v, fktable));
    }

    // ------------------------------------------------------------------------------------------------------- functions
    public Stream<Function> getFunctionsOf(final Catalog catalog) {
        Objects.requireNonNull(catalog, "catalog is null");
        return getFunctions().stream().filter(v -> v.isOf(catalog));
    }

    public Stream<Function> getFunctionsOf(final Schema schema) {
        Objects.requireNonNull(schema, "schema is null");
        return getFunctions().stream().filter(v -> v.isOf(schema));
    }

    // ------------------------------------------------------------------------------------------------- functionColumns
    public Stream<FunctionColumn> getFunctionColumnsOf(final Function function) {
        Objects.requireNonNull(function, "function is null");
        return getFunctionColumns().stream().filter(v -> FunctionColumn.IS_OF.test(v, function));
    }

    // ----------------------------------------------------------------------------------------------------- importedKey
    public Stream<ImportedKey> getImportedKeysOfPktable(final Table pktable) {
        Objects.requireNonNull(pktable, "pktable is null");
        return getImportedKeys().stream().filter(v -> TableKey.IS_OF_PKTABLE.test(v, pktable));
    }

    public Stream<ImportedKey> getImportedKeysOfFktable(final Table fktable) {
        Objects.requireNonNull(fktable, "fktable is null");
        return getImportedKeys().stream().filter(v -> TableKey.IS_OF_FKTABLE.test(v, fktable));
    }

    // ------------------------------------------------------------------------------------------------------- indexInfo
    public Stream<IndexInfo> getIndexInfoOf(final boolean unique, final boolean approximate, final Table table) {
        Objects.requireNonNull(table, "table is null");
        return getIndexInfo()
                .get(unique)
                .get(approximate)
                .stream()
                .filter(v -> IndexInfo.IS_OF.test(v, table));
    }

    // ----------------------------------------------------------------------------------------------------- primaryKeys
    public Stream<PrimaryKey> getPrimaryKeysOf(final Table table) {
        Objects.requireNonNull(table, "table is null");
        return getPrimaryKeys().stream().filter(v -> PrimaryKey.IS_OF.test(v, table));
    }

    // ------------------------------------------------------------------------------------------------------ procedures
    public Stream<Procedure> getProceduresOf(final Catalog catalog) {
        Objects.requireNonNull(catalog, "catalog is null");
        return getProcedures().stream().filter(v -> Procedure.IS_OF_CATALOG.test(v, catalog));
    }

    public Stream<Procedure> getProceduresOf(final Schema schema) {
        Objects.requireNonNull(schema, "schema is null");
        return getProcedures()
                .stream()
                .filter(v -> Procedure.IS_OF_SCHEMA.test(v, schema));
    }

    // ------------------------------------------------------------------------------------------------ procedureColumns
    public Stream<ProcedureColumn> getProcedureColumnsOf(final Procedure procedure) {
        Objects.requireNonNull(procedure, "procedure is null");
        return getProcedureColumns()
                .stream()
                .filter(v -> ProcedureColumn.IS_OF.test(v, procedure));
    }

    // ---------------------------------------------------------------------------------------------------- pseudoColumn
    public Stream<PseudoColumn> getPseudoColumnsOf(final Table table) {
        Objects.requireNonNull(table, "table is null");
        return getPseudoColumns()
                .stream()
                .filter(v -> PseudoColumn.IS_OF.test(v, table));
    }

    // --------------------------------------------------------------------------------------------------------- schemas
    List<Schema> getSchemas_() {
        final List<Schema> schemas = getSchemas();
        if (schemas.isEmpty()) {
            schemas.add(Schema.of(null, null));
        }
        return schemas;
    }

    public Stream<Schema> getSchemasOf(final Catalog catalog) {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSchemas().stream().filter(s -> Schema.IS_OF.test(s, catalog));
    }

    // ----------------------------------------------------------------------------------------------------- superTables
    public Stream<SuperTable> getSuperTablesOf(final Table table) {
        Objects.requireNonNull(table, "table is null");
        return getSuperTables()
                .stream()
                .filter(v -> SuperTable.IS_OF.test(v, table));
    }

    // ------------------------------------------------------------------------------------------------------ superTypes
    public Stream<SuperType> getSuperTypesOf(final UDT udt) {
        Objects.requireNonNull(udt, "udt is null");
        return getSuperTypes()
                .stream()
                .filter(v -> SuperType.IS_OF.test(v, udt));
    }

    // ---------------------------------------------------------------------------------------------------------- tables
    public Stream<Table> getTablesOf(final Catalog catalog) {
        Objects.requireNonNull(catalog, "catalog is null");
        return getTables().stream().filter(v -> Table.IS_OF_CATALOG.test(v, catalog));
    }

    public Stream<Table> getTablesOf(final Schema schema) {
        Objects.requireNonNull(schema, "schema is null");
        return getTables().stream().filter(v -> Table.IS_OF_SCHEMA.test(v, schema));
    }

    // ------------------------------------------------------------------------------------------------- tablePrivileges
    public Stream<TablePrivilege> getTablePrivilegesOf(final Table table) {
        Objects.requireNonNull(table, "table is null");
        return getTablePrivileges()
                .stream()
                .filter(v -> TablePrivilege.IS_OF.test(v, table));
    }

    // ------------------------------------------------------------------------------------------------------------ UDTs
    public Stream<UDT> getUDTsOf(final Catalog catalog) {
        Objects.requireNonNull(catalog, "catalog is null");
        return getUDTs()
                .stream()
                .filter(v -> UDT.IS_OF_CATALOG.test(v, catalog));
    }

    public Stream<UDT> getUDTsOf(final Schema schema) {
        Objects.requireNonNull(schema, "schema is null");
        return getUDTs()
                .stream()
                .filter(v -> UDT.IS_OF_SCHEMA.test(v, schema));
    }

    // -------------------------------------------------------------------------------------------------- versionColumns
    public Stream<VersionColumn> getVersionColumnsOf(final Table table) {
        Objects.requireNonNull(table, "table is null");
        return getVersionColumns()
                .stream()
                .filter(v -> VersionColumn.IS_OF.test(v, table));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementWrapper
    @XmlElement(name = "attribute")
    private final List<@Valid @NotNull Attribute> attributes = new ArrayList<>();

    @XmlTransient
    private final Map<
            @NotNull Integer,
            @NotNull Map<
                    @NotNull Boolean,
                    @NotNull List<@Valid @NotNull BestRowIdentifier>
                    >
            > bestRowIdentifiers = new HashMap<>();

    @XmlElement
    private final List<@Valid @NotNull Catalog> catalogs = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "clientInfoProperty")
    private final List<@Valid @NotNull ClientInfoProperty> clientInfoProperties = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "column")
    private final List<@Valid @NotNull Column> columns = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "columnPrivilege")
    private final List<@Valid @NotNull ColumnPrivilege> columnPrivileges = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "crossReference")
    private final List<@Valid @NotNull CrossReference> crossReference_ = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "exportedKey")
    private final List<@Valid @NotNull ExportedKey> exportedKeys = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "function")
    private final List<@Valid @NotNull Function> functions = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "functionColumn")
    private final List<@Valid @NotNull FunctionColumn> functionColumns = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "importedKey")
    private final List<@Valid @NotNull ImportedKey> importedKeys = new ArrayList<>();

//    @XmlElementWrapper
    @XmlElement(name = "indexInfoWrapper")
    @XmlJavaTypeAdapter(IndexInfoAdapter.class)
    private final Map<
            @NotNull Boolean,
            @NotNull Map<
                    @NotNull Boolean,
                    @NotNull List<@Valid @NotNull IndexInfo>
                    >
            > indexInfo = new HashMap<>();

    @XmlElementWrapper
    @XmlElement(name = "primaryKey")
    private final List<@Valid @NotNull PrimaryKey> primaryKeys = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "procedure")
    private final List<@Valid @NotNull Procedure> procedures = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "procedureColumn")
    private final List<@Valid @NotNull ProcedureColumn> procedureColumns = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "pseudoColumn")
    private final List<@Valid @NotNull PseudoColumn> pseudoColumns = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "schema")
    private final List<@Valid @NotNull Schema> schemas = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "superTable")
    private final List<@Valid @NotNull SuperTable> superTables = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "superType")
    private final List<@Valid @NotNull SuperType> superTypes = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "table")
    private final List<@Valid @NotNull Table> tables = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "tablePrivilege")
    private final List<@Valid @NotNull TablePrivilege> tablePrivileges = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "tableType")
    private final List<@Valid @NotNull TableType> tableTypes = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "typeInfo")
    private final List<@Valid @NotNull TypeInfo> typeInfo_ = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "UDT")
    private final List<@Valid @NotNull UDT> UDTs = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "versionColumn")
    private final List<@Valid @NotNull VersionColumn> versionColumns = new ArrayList<>();

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementWrapper
    @XmlElement(name = "numericFunction")
    private final List<@NotBlank String> numericFunctions = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "SQLKeyword")
    private final List<@NotBlank String> SQLKeywords = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "stringFunction")
    private final List<@NotBlank String> stringFunctions = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "systemFunction")
    private final List<@NotBlank String> systemFunctions = new ArrayList<>();

    @XmlElementWrapper
    @XmlElement(name = "timeDateFunction")
    private final List<@NotBlank String> timeDateFunctions = new ArrayList<>();
}
