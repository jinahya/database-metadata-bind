package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class Context_Test_Utils {

    static String artifactFileNamePrefix(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        final var metadata = context.metadata;
        final var value = String.join(
                "-",
                normalizeFileNamePart(metadata.getDatabaseProductName()),
                normalizeFileNamePart(metadata.getDatabaseProductVersion()),
                normalizeFileNamePart(metadata.getDriverName()),
                normalizeFileNamePart(metadata.getDriverVersion())
        );
        if (!value.matches("[a-z0-9][a-z0-9-]*")) {
            throw new IllegalStateException("invalid artifact file name prefix: " + value);
        }
        return value;
    }

    private static String normalizeFileNamePart(final String value) {
        if (value == null) {
            return "unknown";
        }
        final var normalized = value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");
        return normalized.isBlank() ? "unknown" : normalized;
    }

    private static String databaseProductName(final Context context) throws SQLException {
        return context.metadata.getDatabaseProductName();
    }

    // ----------------------------------------------------------------------------------------------- bestRowIdentifier
    private static void bestRowIdentifier(final Context context, final List<? extends BestRowIdentifier> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(BestRowIdentifier.comparingInJdbcOrder(context))
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            bestRowIdentifier(context, value);
        }
    }

    private static void bestRowIdentifier(final Context context, final BestRowIdentifier value)
            throws SQLException {
        MetadataType_Test_Utils.verify(value);
        {
            final var scope = value.getScope();
//            assertDoesNotThrow(() -> BestRowIdentifier.Scope.valueOfFieldValue(scope));
            assertThat(value.getColumnName()).isNotNull();
            assertDoesNotThrow(() -> JDBCType.valueOf(value.getDataType()));
            assertThat(value.getTypeName()).isNotNull();
            final int pseudoColumn = value.getPseudoColumn();
//            assertDoesNotThrow(() -> BestRowIdentifier.PseudoColumn.valueOfFieldValue(pseudoColumn));
        }
    }

    // --------------------------------------------------------------------------------------------------------- columns
    private static void columns(final Context context, final List<? extends Column> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(Column.comparingInJdbcOrder(context, String.CASE_INSENSITIVE_ORDER))
                .allSatisfy(c -> {
                    assertThat(c.getTableName()).isNotNull();
                    assertThat(c.getColumnName()).isNotNull();
                    assertThat(c.getDataType()).isNotNull();
                    assertThat(c.getTypeName()).isNotNull();
//                    assertThat(c.getNumPrecRadix()).isNotNull(); // some returns null
                    assertThat(c.getNullable())
                            .isNotNull()
                            .isIn(Column.COLUMN_VALUES_NULLABLE);
                    assertThat(c.getOrdinalPosition()).isNotNull().isPositive();
                    assertThat(c.getIsNullable())
                            .isNotNull()
                            .isIn(Column.COLUMN_VALUES_IS_NULLABLE);
                    assertThat(c.getIsAutoincrement())
                            .isNotNull()
                            .isIn(Column.COLUMN_VALUES_IS_AUTOINCREMENT);
                    assertThat(c.getIsGeneratedcolumn())
                            .isNotNull()
                            .isIn(Column.COLUMN_VALUES_IS_GENERATEDCOLUMN);
                })
        ;
        for (final var value : values) {
            column(context, value);
        }
    }

    private static void column(final Context context, final Column value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        {
            assertThat(value.getTableName()).isNotNull();
            assertThat(value.getColumnName()).isNotNull();
            assertThat(value.getOrdinalPosition()).isPositive();
            assertThat(value.getIsNullable()).isNotNull();
            assertThat(value.getIsAutoincrement()).isNotNull();
            assertThat(value.getIsGeneratedcolumn()).isNotNull();
        }

        // -------------------------------------------------------------------------------------------- columnPrivileges
        try {
            final var columnPrivileges = context.getColumnPrivileges(
                    value.getTableCat(),
                    value.getScopeSchema(),
                    value.getTableName(),
                    value.getColumnName()
            );
            columnPrivileges(context, columnPrivileges);
        } catch (final SQLException sqle) {
            // empty
        }
    }

    // ------------------------------------------------------------------------------------------------ columnPrivileges
    private static void columnPrivileges(final Context context, final List<? extends ColumnPrivilege> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(ColumnPrivilege.comparingInJdbcOrder(
//                        UnaryOperator.identity(),
//                        ContextUtils.withDatabaseNullOrdering(context, String.CASE_INSENSITIVE_ORDER)))
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            columnPrivilege(context, value);
        }
    }

    private static void columnPrivilege(final Context context, final ColumnPrivilege value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
//        final var isGrantableAsEnum = columnPrivilege.getIsGrantableAsEnum();
    }

    // ---------------------------------------------------------------------------------------------------- exportedKeys
    private static void exportedKeys(final Context context, final List<? extends ExportedKey> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(ExportedKey.comparingInJdbcOrder(
//                        UnaryOperator.identity(),
//                        ContextUtils.withDatabaseNullOrdering(context, String.CASE_INSENSITIVE_ORDER)))
                .allSatisfy(e -> {
                });
        for (final var value : values) {
            exportedKey(context, value);
        }
    }

    private static void exportedKey(final Context context, final ExportedKey value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // ------------------------------------------------------------------------------------------------------- functions
    static void functions(final Context context, final List<? extends Function> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(Function.comparingInJdbcOrder(
//                        UnaryOperator.identity(),
//                        ContextUtils.withDatabaseNullOrdering(context, String.CASE_INSENSITIVE_ORDER)))
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            function(context, value);
        }
    }

    private static void function(final Context context, final Function value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        try {
            final var functionColumns = context.getFunctionColumnsOf(value, "%");
            functionColumns(context, functionColumns);
        } catch (final SQLException sqle) {
            log.error("failed to get functions for {}", value, sqle);
        }
    }

    // ------------------------------------------------------------------------------------------------- functionColumns
    private static void functionColumns(final Context context, final List<? extends FunctionColumn> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            functionColumn(context, value);
        }
    }

    private static void functionColumn(final Context context, final FunctionColumn value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
//        final var columnType = FunctionColumn.ColumnType.valueOfFieldValue(functionColumn.getColumnType());
    }

    // ---------------------------------------------------------------------------------------------------- importedKeys
    private static void importedKeys(final Context context, final List<? extends ImportedKey> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .allSatisfy(i -> {
                });
        for (final var value : values) {
            importedKey(context, value);
        }
    }

    private static void importedKey(final Context context, final ImportedKey value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // ------------------------------------------------------------------------------------------------------- indexInfo
    private static void indexInfo(final Context context, final List<IndexInfo> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(IndexInfo.comparingInJdbcOrder(
//                        UnaryOperator.identity(),
//                        ContextUtils.withDatabaseNullOrdering(context, String.CASE_INSENSITIVE_ORDER)))
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            indexInfo(context, value);
        }
    }

    private static void indexInfo(final Context context, final IndexInfo value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // ------------------------------------------------------------------------------------------------------ procedures
    private static void procedures(final Context context, final List<? extends Procedure> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(Procedure.comparingInJdbcOrder(
//                        UnaryOperator.identity(),
//                        ContextUtils.withDatabaseNullOrdering(context, String.CASE_INSENSITIVE_ORDER)))
                .allSatisfy(v -> {
                });
        if (!databaseProductName(context).equals(DatabaseProductNames.MARIA_DB) &&
            !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
//            assertThat(values).isSortedAccordingTo(
//                    Procedure.comparingInJdbcOrder(context, String.CASE_INSENSITIVE_ORDER));
        }
        for (final var value : values) {
            procedure(context, value);
        }
    }

    private static void procedure(final Context context, final Procedure value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        {
            final var procedureColumns = context.getProcedureColumnsOf(value, "%");
            procedureColumns(context, procedureColumns);
        }
    }

    // ------------------------------------------------------------------------------------------------ procedureColumns
    private static void procedureColumns(final Context context, final List<? extends ProcedureColumn> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            procedureColumn(context, value);
        }
    }

    private static void procedureColumn(final Context context, final ProcedureColumn value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // --------------------------------------------------------------------------------------------------------- schemas
    static void schemas(final Context context, final List<? extends Schema> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(Schema.comparingInJdbcOrder(context, String.CASE_INSENSITIVE_ORDER))
                .allSatisfy(v -> {
                })
        ;
        for (final var value : values) {
            schema(context, value);
        }
    }

    private static void schema(final Context context, final Schema value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        // -------------------------------------------------------------------------------------------------- procedures
        try {
            final var procedures = context.getProceduresOf(value, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTablesOf(value, "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypesOf(value, "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------------ tables
        try {
            final var tables = context.getTablesOf(value, "%", (String[]) null);
            tables(context, tables);
        } catch (final SQLException sqle) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var tablePrivileges = context.getTablePrivilegesOf(value, "%");
            tablePrivileges(context, tablePrivileges);
        } catch (final SQLException sqle) {
            // empty
        }
    }

    // ------------------------------------------------------------------------------------------------------ superTypes
    private static void superTypes(final Context context, final List<? extends SuperType> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            superType(context, value);
        }
    }

    private static void superType(final Context context, final SuperType value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        assertThat(value).satisfies(v -> {
            assertThat(v.getTypeName()).isNotNull();
            assertThat(v.getSupertypeName()).isNotNull();
        });
    }

    // ---------------------------------------------------------------------------------------------------------- tables
    static void tables(final Context context, final List<? extends Table> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(Table.comparingInJdbcOrder(
//                        UnaryOperator.identity(),
//                        ContextUtils.withDatabaseNullOrdering(context, Comparator.naturalOrder())))
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            table(context, value);
        }
    }

    private static void table(final Context context, final Table value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        // ------------------------------------------------------------------------------------------- bestRowIdentifier
        for (final int scope : BestRowIdentifier.COLUMN_VALUES_SCOPE) {
            for (final boolean nullable : new boolean[] {true, false}) {
                try {
                    final var values = context.getBestRowIdentifier(
                            value.getTableCat(),
                            value.getTableSchem(),
                            value.getTableName(),
                            scope,
                            nullable
                    );
                    bestRowIdentifier(context, values);
                } catch (final SQLException sqle) {
                    log.error("failed to getBestRowIdentifier({}, {}, {})", value, scope, nullable, sqle);
                }
            }
        }
        // ----------------------------------------------------------------------------------------------------- columns
        try {
            final var values = context.getColumns(
                    value.getTableCat(),
                    value.getTableSchem(),
                    value.getTableName(),
                    "%"
            );
            columns(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getColumns({})", value, sqle);
        }
        // -------------------------------------------------------------------------------------------- columnPrivileges
        try {
            final var values = context.getColumnPrivileges(
                    value.getTableCat(),
                    value.getTableSchem(),
                    value.getTableName(),
                    "%"
            );
            columnPrivileges(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getColumnPrivileges({})", value, sqle);
        }
        // ------------------------------------------------------------------------------------------------ exportedKeys
        try {
            final var values = context.getExportedKeysOf(value);
            exportedKeys(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getExportedKeys({})", value, sqle);
        }
        // ------------------------------------------------------------------------------------------------ importedKeys
        try {
            final var values = context.getImportedKeysOf(value);
            importedKeys(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getImportedKey({})", value, sqle);
        }
        // --------------------------------------------------------------------------------------------------- indexInfo
        for (final boolean unique : new boolean[] {true, false}) {
            for (final boolean approximate : new boolean[] {true, false}) {
                try {
                    final var values = context.getIndexInfoOf(value, unique, approximate);
                    indexInfo(context, values);
                } catch (final SQLException sqle) {
                    log.error("failed to getIndexInfo({}, {}, {})", value, unique, approximate, sqle);
                }
            }
        }
        // ------------------------------------------------------------------------------------------------- primaryKeys
        try {
            final var values = context.getPrimaryKeysOf(value);
            primaryKeys(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getPrimaryKeys({})", value, sqle);
        }
        // ----------------------------------------------------------------------------------------------- pseudoColumns
        {
            final var columnNamePattern = "%";
            try {
                final var values = context.getPseudoColumnsOf(value, columnNamePattern);
                pseudoColumns(context, values);
            } catch (final SQLException sqle) {
                log.error("failed to getPseudoColumns({}, {})", value, columnNamePattern, sqle);
            }
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var values = context.getSuperTablesOf(value);
            superTables(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getSuperTables({})", value, sqle);
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var values = context.getTablePrivilegesOf(value);
            assertThat(values).allSatisfy(tp -> {
                assertThat(tp.getTableCat()).isEqualTo(value.getTableCat());
                assertThat(tp.getTableSchem()).isEqualTo(value.getTableSchem());
                assertThat(tp.getTableName()).isEqualTo(value.getTableName());
            });
            tablePrivileges(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getTablePrivileges({})", value);
        }
        // ---------------------------------------------------------------------------------------------- versionColumns
        try {
            final var values = context.getVersionColumnsOf(value);
            versionColumns(context, values);
        } catch (final SQLException sqle) {
            log.debug("failed to getVersionColumns({})", value, sqle);
        }
    }

    // ----------------------------------------------------------------------------------------------------- primaryKeys
    private static void primaryKeys(final Context context, final List<? extends PrimaryKey> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(PrimaryKey.comparingInJdbcOrder(
//                        UnaryOperator.identity(),
//                        ContextUtils.withDatabaseNullOrdering(context, String.CASE_INSENSITIVE_ORDER)))
                .allSatisfy(p -> {
                });
        if (!databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL) &&
            !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
//            assertThat(values).isSortedAccordingTo(
//                    PrimaryKey.comparing(context, String.CASE_INSENSITIVE_ORDER));
        }
        for (final var value : values) {
            primaryKey(context, value);
        }
    }

    private static void primaryKey(final Context context, final PrimaryKey value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // --------------------------------------------------------------------------------------------------- pseudoColumns
    private static void pseudoColumns(final Context context, final List<? extends PseudoColumn> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(PseudoColumn.comparingInJdbcOrder(context, String.CASE_INSENSITIVE_ORDER))
                .allSatisfy(p -> {
                });
        for (final var value : values) {
            pseudoColumn(context, value);
        }
    }

    private static void pseudoColumn(final Context context, final PseudoColumn value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // ----------------------------------------------------------------------------------------------------- superTables
    private static void superTables(final Context context, final List<? extends SuperTable> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                // getSuperTables does not specify a result ordering; no comparator to assert against
                .allSatisfy(s -> {
                });
        for (final var superTable : values) {
            superTable(context, superTable);
        }
    }

    private static void superTable(final Context context, final SuperTable value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // ------------------------------------------------------------------------------------------------- tablePrivileges
    private static void tablePrivileges(final Context context, final List<? extends TablePrivilege> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(TablePrivilege.comparingInJdbcOrder(
//                        UnaryOperator.identity(),
//                        ContextUtils.withDatabaseNullOrdering(context, String.CASE_INSENSITIVE_ORDER)))
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            tablePrivilege(context, value);
        }
    }

    private static void tablePrivilege(final Context context, final TablePrivilege value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // -------------------------------------------------------------------------------------------------- versionColumns
    private static void versionColumns(final Context context, final List<? extends VersionColumn> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                // getVersionColumns() are unordered per JDBC spec
                .allSatisfy(v -> {
                });
        for (final var value : values) {
            versionColumn(context, value);
        }
    }

    private static void versionColumn(final Context context, final VersionColumn value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        assertDoesNotThrow(() -> JDBCType.valueOf(value.getDataType()));
//        assertDoesNotThrow(() -> VersionColumn.PseudoColumn.valueOfFieldValue(versionColumn.getPseudoColumn()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Context_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
