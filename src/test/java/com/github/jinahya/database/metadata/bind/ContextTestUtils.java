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

import java.lang.reflect.InvocationHandler;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.github.jinahya.database.metadata.bind._Assertions.assertType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTestUtils {

    private static String databaseProductName(final Context context) throws SQLException {
        return context.metadata.getDatabaseProductName();
    }

    // -----------------------------------------------------------------------------------------------------------------
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
                if (cause != null) {
                    if (cause instanceof SQLFeatureNotSupportedException) {
                        log.info("not supported; {}; {}", m.getName(), cause.getMessage());
                    } else {
                        log.error("failed to invoke {}.{}({})", p, m.getName(), args, cause);
                    }
                    throw cause;
                }
                throw t;
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void test(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");

        context.listeners.add(new Context.Listener() {
            @Override
            public void bound(final MetadataType value) {
                __Validation_Test_Utils.requireValid(value);
            }
        });

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
        } catch (final SQLException sqle) {
            // empty
        }
        // ---------------------------------------------------------------------------------------------- crossReference
        try {
            final var crossReference = context.getCrossReference(
                    null,
                    null,
                    "%",
                    null,
                    null,
                    "%"
            );
            crossReference(context, crossReference);
        } catch (final SQLException sqle) {
            // empty
        }
        // ----------------------------------------------------------------------------------- functions/functionColumns
        try {
            final var functions = context.getFunctions(null, null, "%");
            functions(context, functions);
            functions.stream()
                    .filter(e1 -> e1.getFunctionCat() != null)
                    .collect(Collectors.groupingBy(Function::getFunctionCat)).forEach((fc, l1) -> {
                        try {
                            functions(context, l1);
                        } catch (final SQLException e) {
                            // empty
                        }
                        l1.stream()
                                .filter(e2 -> e2.getFunctionSchem() != null)
                                .collect(Collectors.groupingBy(Function::getFunctionSchem)).forEach((fs, l2) -> {
                                    assertThat(l2).doesNotHaveDuplicates();
                                    try {
                                        functions(context, l1);
                                    } catch (final SQLException e) {
                                        // empty
                                    }
                                });
                    });
        } catch (final SQLException sqle) {
            // empty
        }
        // --------------------------------------------------------------------------------- procedures/procedureColumns
        try {
            final var procedures = context.getProcedures(null, null, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        try {
            final var schemas = context.getSchemas();
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        try {
            final var schemas = context.getSchemas((String) null, null);
            if (schemas.isEmpty()) {
                schemas.add(Schema.of((String) null, null));
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- tableTypes
        try {
            final var tableTypes = context.getTableTypes();
            tableTypes(context, tableTypes);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------------ tables
        try {
            final var tables = context.getTables((String) null, null, "%", null);
            tables(context, tables);
        } catch (final SQLException sqle) {
            // empty
        }
        // ---------------------------------------------------------------------------------------------------- typeInfo
        try {
            final var typeInfo = context.getTypeInfo();
            assertThat(typeInfo)
                    .doesNotHaveDuplicates()
                    .isSortedAccordingTo(TypeInfo.comparator(context))
                    .allSatisfy(v -> {
                        assertThat(v.getNullable()).isIn(TypeInfo.COLUMN_VALUE_NULLABLE_TYPE_NO_NULLS,
                                                         TypeInfo.COLUMN_VALUE_NULLABLE_TYPE_NULLABLE,
                                                         TypeInfo.COLUMN_VALUE_NULLABLE_TYPE_NULLABLE_UNKNOWN);
                    })
            ;
            typeInfo(context, typeInfo);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------------- udts
        try {
            final var udts = context.getUDTs((String) null, (String) null, "%", null);
            udts(context, udts);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------- numericFunctions
        try {
            final var numericFunctions = context.getNumericFunctions();
            assertThat(numericFunctions).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLException sqle) {
            // empty
        }
        // ---------------------------------------------------------------------------------------------- getSQLKeywords
        try {
            final var SQLKeywords = context.getSQLKeywords();
            assertThat(SQLKeywords).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------ getStringFunctions
        try {
            final var stringFunctions = context.getStringFunctions();
            assertThat(stringFunctions).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------ getSystemFunctions
        try {
            final var systemFunctions = context.getSystemFunctions();
            assertThat(systemFunctions).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLException sqle) {
            // empty
        }
        // ---------------------------------------------------------------------------------------- getTimeDateFunctions
        try {
            final var timeDateFunction = context.getTimeDateFunctions();
            assertThat(timeDateFunction).isNotNull().doesNotContainNull().allSatisfy(v -> {
                assertThat(v).isNotBlank();
            });
        } catch (final SQLException sqle) {
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
        MetadataType_Test_Utils.verify(attribute);
        {
            assertThat(attribute.getTypeName()).isNotNull();
            assertThat(attribute.getAttrName()).isNotNull();
            final var dataType = attribute.getDataType();
            assertDoesNotThrow(() -> JDBCType.valueOf(dataType));
            assertThat(attribute.getAttrTypeName()).isNotNull();
//            assertDoesNotThrow(() -> Attribute.Nullable.valueOfFieldValue(attribute.getNullable()));
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
        MetadataType_Test_Utils.verify(bestRowIdentifier);
        {
            final var scope = bestRowIdentifier.getScope();
//            assertDoesNotThrow(() -> BestRowIdentifier.Scope.valueOfFieldValue(scope));
            assertThat(bestRowIdentifier.getColumnName()).isNotNull();
            assertDoesNotThrow(() -> JDBCType.valueOf(bestRowIdentifier.getDataType()));
            assertThat(bestRowIdentifier.getTypeName()).isNotNull();
            final int pseudoColumn = bestRowIdentifier.getPseudoColumn();
//            assertDoesNotThrow(() -> BestRowIdentifier.PseudoColumn.valueOfFieldValue(pseudoColumn));
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
        MetadataType_Test_Utils.verify(catalog);
        // -------------------------------------------------------------------------------------------------- procedures
        try {
            final var procedures = context.getProcedures(catalog, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        try {
            final var schemas = context.getSchemas(catalog, "%");
            if (schemas.isEmpty()) {
                schemas.add(Schema.of((String) null, null));
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(catalog.getTableCat(), "%", "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(catalog.getTableCat(), "%", "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------------ tables
        try {
            final var tables = context.getTables(catalog.getTableCat(), null, "%", null);
            if (!databaseProductName(context).equals(DatabaseProductNames.APACHE_DERBY) &&
                !databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL)) {
                tables.forEach(t -> {
                    assertType(t).isOf(catalog);
                });
            }
            tables(context, tables);
        } catch (final SQLException sqle) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var tablePrivileges = context.getTablePrivileges(catalog.getTableCat(), "%", "%");
            if (!databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL)) {
                assertThat(tablePrivileges).allSatisfy(tp -> {
                    assertThat(tp.getTableCat()).isEqualTo(catalog.getTableCat());
                });
            }
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLException sqle) {
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
        MetadataType_Test_Utils.verify(clientInfoProperty);
    }

    // --------------------------------------------------------------------------------------------------------- columns
    private static void columns(final Context context, final List<? extends Column> columns) throws SQLException {
        assertThat(columns).isNotNull().doesNotContainNull();
        if (true) {
            assertThat(columns).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(columns).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Column.comparingAsSpecified(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(Column.comparingAsSpecified(context, Comparator.naturalOrder()))
            );
        }
        for (final var column : columns) {
            column(context, column);
        }
    }

    private static void column(final Context context, final Column column) throws SQLException {
        MetadataType_Test_Utils.verify(column);
        {
            assertThat(column.getTableName()).isNotNull();
            assertThat(column.getColumnName()).isNotNull();
//            assertDoesNotThrow(() -> JDBCType.valueOf(column.getDataType()));
            assertThat(column.getOrdinalPosition()).isPositive();
            assertThat(column.getIsNullable()).isNotNull();
            assertThat(column.getIsAutoincrement()).isNotNull();
            assertThat(column.getIsGeneratedcolumn()).isNotNull();
        }
//        assertThatCode(() -> {
//            final var value = Column.Nullable.valueOfFieldValue(column.getNullable());
//        }).doesNotThrowAnyException();
//        assertThatCode(() -> {
//            final var isAutoincrementAsEnum = column.getIsAutoincrementAsEnum();
//        }).doesNotThrowAnyException();
//        assertThatCode(() -> {
//            final var isGeneratedcolumnAsEnum = column.getIsGeneratedcolumnAsEnum();
//        }).doesNotThrowAnyException();

        // -------------------------------------------------------------------------------------------- columnPrivileges
        try {
            final var columnPrivileges = context.getColumnPrivileges(column);
            columnPrivileges(context, columnPrivileges);
        } catch (final SQLException sqle) {
            // empty
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
        MetadataType_Test_Utils.verify(columnPrivilege);
//        final var isGrantableAsEnum = columnPrivilege.getIsGrantableAsEnum();
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
                            CrossReference.comparingSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(CrossReference.comparingSpecifiedOrder(context, Comparator.naturalOrder()))
            );
        }
        for (final var v : crossReference) {
            crossReference(context, v);
        }
    }

    private static void crossReference(final Context context, final CrossReference crossReference) throws SQLException {
        MetadataType_Test_Utils.verify(crossReference);
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
                            ExportedKey.specifiedOrder(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(
                            ExportedKey.specifiedOrder(context, Comparator.naturalOrder()))
            );
        }
        for (final var exportedKey : exportedKeys) {
            exportedKey(context, exportedKey);
        }
    }

    private static void exportedKey(final Context context, final ExportedKey exportedKey) throws SQLException {
        MetadataType_Test_Utils.verify(exportedKey);
    }

    // ------------------------------------------------------------------------------------------------------- functions
    static void functions(final Context context, final List<? extends Function> functions) throws SQLException {
        {
            final var set = new HashSet<Function>();
            functions.forEach(f -> {
                if (!set.add(f)) {
                    log.error("duplicate function: {}", f);
                }
            });
        }
        if (!List.of(
                DatabaseProductNames.POSTGRE_SQL,
                DatabaseProductNames.ORACLE
        ).contains(databaseProductName(context))) {
            assertThat(functions).doesNotHaveDuplicates();
        }
        if (!List.of(
                DatabaseProductNames.MARIA_DB,
                DatabaseProductNames.POSTGRE_SQL,
                DatabaseProductNames.MICROSOFT_SQL_SERVER,
                DatabaseProductNames.ORACLE
        ).contains(databaseProductName(context))) {
            // https://github.com/microsoft/mssql-jdbc/issues/2321
            assertThat(functions).satisfiesAnyOf(
                    l -> assertThat(l).isSortedAccordingTo(Function.specifiedOrder(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(Function.specifiedOrder(context, Comparator.naturalOrder()))
            );
        }
        for (final var function : functions) {
            function(context, function);
        }
    }

    private static void function(final Context context, final Function function) throws SQLException {
        MetadataType_Test_Utils.verify(function);
        try {
            final var functionColumns = context.getFunctionColumns(function, "%");
            functionColumns(context, functionColumns);
        } catch (final SQLException sqle) {
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
                    l -> assertThat(l).isSortedAccordingTo(
                            FunctionColumn.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(FunctionColumn.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var functionColumn : functionColumns) {
            functionColumn(context, functionColumn);
        }
    }

    private static void functionColumn(final Context context, final FunctionColumn functionColumn)
            throws SQLException {
        MetadataType_Test_Utils.verify(functionColumn);
//        final var columnType = FunctionColumn.ColumnType.valueOfFieldValue(functionColumn.getColumnType());
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
                            ImportedKey.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l).isSortedAccordingTo(
                            ImportedKey.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var importedKey : importedKeys) {
            importedKey(context, importedKey);
        }
    }

    private static void importedKey(final Context context, final ImportedKey importedKey) throws SQLException {
        MetadataType_Test_Utils.verify(importedKey);
        assertThatCode(() -> {
            final var string = importedKey.toString();
        }).doesNotThrowAnyException();
        assertThatCode(() -> {
            final var hashCode = importedKey.hashCode();
        }).doesNotThrowAnyException();
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
        MetadataType_Test_Utils.verify(indexInfo);
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
        MetadataType_Test_Utils.verify(procedure);
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
        MetadataType_Test_Utils.verify(procedureColumn);
        assertThatCode(() -> {
            final var isNullable = procedureColumn.getIsNullable();
        }).doesNotThrowAnyException();
    }

    // --------------------------------------------------------------------------------------------------------- schemas
    static void schemas(final Context context, final List<? extends Schema> schemas) throws SQLException {
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
        MetadataType_Test_Utils.verify(schema);
        // -------------------------------------------------------------------------------------------------- procedures
        try {
            final var procedures = context.getProcedures(schema);
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(schema, "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(schema, "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------------ tables
        try {
            final var tables = context.getTables(schema, "%", null);
            tables(context, tables);
        } catch (final SQLException sqle) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var tablePrivileges = context.getTablePrivileges(schema, "%");
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLException sqle) {
            // empty
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
        MetadataType_Test_Utils.verify(superType);
        assertThat(superType).satisfies(v -> {
            assertThat(v.getTypeName()).isNotNull();
            assertThat(v.getSupertypeName()).isNotNull();
        });
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
        MetadataType_Test_Utils.verify(table);
        // -------------------------------------------------------------------------------------------------------------
        {
            assertThat(table.getTableCatalog_())
                    .isNotNull()
                    .isEqualTo(Catalog.of(table.getTableCat()));
            assertThat(table.getTableSchema_())
                    .isNotNull()
                    .isEqualTo(Schema.of(table.getTableCat(), table.getTableSchem()));
            assertThat(table.getTypeCatalog_())
                    .isNotNull()
                    .isEqualTo(Catalog.of(table.getTypeCat()));
            assertThat(table.getTypeSchema_())
                    .isNotNull()
                    .isEqualTo(Schema.of(table.getTypeCat(), table.getTypeSchem()));
        }
        assertThat(table.getRefGeneration()).satisfiesAnyOf(
                rg -> assertThat(rg).isNull(),
                rg -> assertThat(rg).isIn(
                        Table.COLUMN_VALUE_REF_GENERATION_DERIVED,
                        Table.COLUMN_VALUE_REF_GENERATION_SYSTEM,
                        Table.COLUMN_VALUE_REF_GENERATION_USER
                )
        );
        // ------------------------------------------------------------------------------------------- bestRowIdentifier
//        for (final BestRowIdentifier.Scope scope : BestRowIdentifier.Scope.values()) {
//            for (final boolean nullable : new boolean[] {true, false}) {
//                try {
//                    final var bestRowIdentifier =
//                            context.getBestRowIdentifier(table, scope.fieldValueAsInt(), nullable);
//                    bestRowIdentifier(context, bestRowIdentifier);
//                } catch (final SQLException sqle) {
//                    // empty
//                }
//            }
//        }
        // ----------------------------------------------------------------------------------------------------- columns
        try {
            final var columns = context.getColumns(table);
            columns(context, columns);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------- columnPrivileges
        try {
            final var columnPrivileges = context.getColumnPrivileges(table);
            columnPrivileges(context, columnPrivileges);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------ exportedKeys
        try {
            final var exportedKeys = context.getExportedKeys(table);
            exportedKeys(context, exportedKeys);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------ importedKeys
        try {
            final var importedKeys = context.getImportedKeys(table);
            importedKeys(context, importedKeys);
        } catch (final SQLException sqle) {
            // empty
        }
        // --------------------------------------------------------------------------------------------------- indexInfo
        for (final boolean unique : new boolean[] {true, false}) {
            for (final boolean approximate : new boolean[] {true, false}) {
                try {
                    final var indexInfo = context.getIndexInfo(table, unique, approximate);
                    indexInfo(context, indexInfo);
                } catch (final SQLException sqle) {
                    // empty
                }
            }
        }
        // ------------------------------------------------------------------------------------------------- primaryKeys
        try {
            final var primaryKeys = context.getPrimaryKeys(table);
            primaryKeys(context, primaryKeys);
        } catch (final SQLException sqle) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------- pseudoColumns
        try {
            final var pseudoColumns = context.getPseudoColumns(table, "%");
            pseudoColumns(context, pseudoColumns);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(table);
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var tablePrivileges = context.getTablePrivileges(table);
            assertThat(tablePrivileges).allSatisfy(tp -> {
                assertThat(tp.getTableCat()).isEqualTo(table.getTableCat());
                assertThat(tp.getTableSchem()).isEqualTo(table.getTableSchem());
                assertThat(tp.getTableName()).isEqualTo(table.getTableName());
            });
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLException sqle) {
            // empty
        }
        // ---------------------------------------------------------------------------------------------- versionColumns
        try {
            final var versionColumns = context.getVersionColumns(table);
            versionColumns(context, versionColumns);
        } catch (final SQLException sqle) {
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
            && !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)
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
        MetadataType_Test_Utils.verify(primaryKey);
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
        MetadataType_Test_Utils.verify(pseudoColumn);
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
        MetadataType_Test_Utils.verify(superTable);
    }

    // ------------------------------------------------------------------------------------------------- tablePrivileges
    private static void tablePrivileges(final Context context, final List<? extends TablePrivilege> tablePrivileges)
            throws SQLException {
        assertThat(tablePrivileges)
                .doesNotContainNull()
                .doesNotHaveDuplicates();
        if (!databaseProductName(context).equals(DatabaseProductNames.MY_SQL)) {
            assertThat(tablePrivileges).doesNotHaveDuplicates();
        }
        if (true) {
            assertThat(tablePrivileges).satisfiesAnyOf(
                    l -> assertThat(l)
                            .isSortedAccordingTo(TablePrivilege.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                    l -> assertThat(l)
                            .isSortedAccordingTo(TablePrivilege.comparing(context, Comparator.naturalOrder()))
            );
        }
        for (final var tablePrivilege : tablePrivileges) {
            tablePrivilege(context, tablePrivilege);
        }
    }

    private static void tablePrivilege(final Context context, final TablePrivilege tablePrivilege) throws SQLException {
        MetadataType_Test_Utils.verify(tablePrivilege);
    }

    // ------------------------------------------------------------------------------------------------------ tableTypes
    static void tableTypes(final Context context, final List<? extends TableType> tableTypes)
            throws SQLException {
        assertThat(tableTypes).doesNotContainNull();
        assertThat(tableTypes).doesNotHaveDuplicates();
        assertThat(tableTypes).satisfiesAnyOf(
                l -> assertThat(l).isSortedAccordingTo(TableType.comparing(context, String.CASE_INSENSITIVE_ORDER)),
                l -> assertThat(l).isSortedAccordingTo(TableType.comparing(context, Comparator.naturalOrder()))
        );
        for (final var tableType : tableTypes) {
            tableType(context, tableType);
        }
    }

    private static void tableType(final Context context, final TableType tableType) throws SQLException {
        MetadataType_Test_Utils.verify(tableType);
        {
            assertThat(tableType.getTableType())
                    .isNotBlank()
                    .doesNotStartWithWhitespaces()
                    .doesNotEndWithWhitespaces();
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
            assertThat(typeInfo).isSortedAccordingTo(TypeInfo.comparator(context));
        }
        for (final var typeInfo_ : typeInfo) {
            typeInfo(context, typeInfo_);
        }
    }

    private static void typeInfo(final Context context, final TypeInfo typeInfo) throws SQLException {
        MetadataType_Test_Utils.verify(typeInfo);
        {
            assertThat(typeInfo.getTypeName()).isNotNull();
            //assertDoesNotThrow(() -> JDBCType.valueOf(typeInfo.getDataType())); // mssqlserver
//            assertDoesNotThrow(() -> TypeInfo.Nullable.valueOfFieldValue(typeInfo.getNullable()));
//            assertDoesNotThrow(() -> TypeInfo.Searchable.valueOfFieldValue(typeInfo.getSearchable()));
        }
        {
//            final var value = TypeInfo.Nullable.valueOfFieldValue(typeInfo.getNullable());
        }
        {
//            final var value = TypeInfo.Searchable.valueOfFieldValue(typeInfo.getSearchable());
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
        MetadataType_Test_Utils.verify(udt);
        {
            assertThat(udt.getTypeName()).isNotNull();
            assertThat(udt.getDataType()).isIn(Types.JAVA_OBJECT, Types.STRUCT, Types.DISTINCT);
            assertDoesNotThrow(() -> JDBCType.valueOf(udt.getDataType()));
        }
        // ------------------------------------------------------------------------------------------------- .attributes
        udt.getAttributes(context, "%").forEach(a -> {
            log.debug("attribute: {}", a);
        });
        // ------------------------------------------------------------------------------------------------- .superTypes
        udt.getSuperTypes(context).forEach(st -> {
            log.debug("superType: {}", st);
        });
        // -------------------------------------------------------------------------------------------------- .superUDTs
        udt.getSuperUDTs(context, null).forEach(sudt -> {
            log.debug("superUDT: {}", sudt);
        });
        // -------------------------------------------------------------------------------------------------- attributes
        try {
            final var attributes = context.getAttributes(udt, "%");
            attributes(context, attributes);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(udt);
            assertThat(superTypes).allSatisfy(st -> {
                assertType(st).isOf(udt);
            });
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
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
        MetadataType_Test_Utils.verify(versionColumn);
        assertDoesNotThrow(() -> JDBCType.valueOf(versionColumn.getDataType()));
        assertDoesNotThrow(() -> VersionColumn.PseudoColumn.valueOfFieldValue(versionColumn.getPseudoColumn()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ContextTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
