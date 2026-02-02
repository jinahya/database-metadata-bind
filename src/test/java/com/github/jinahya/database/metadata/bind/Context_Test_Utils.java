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
import java.util.List;
import java.util.Objects;
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
final class Context_Test_Utils {

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
        {
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
            final var tables = context.getTables((String) null, null, "%", (String[]) null);
            tables(context, tables);
        } catch (final SQLException sqle) {
            // empty
        }
        // ---------------------------------------------------------------------------------------------------- typeInfo
        try {
            final var typeInfo = context.getTypeInfo();
            assertThat(typeInfo)
                    .doesNotHaveDuplicates()
                    .isSortedAccordingTo(TypeInfo.comparingInSpecifiedOrder())
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
        assertThat(attributes)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(Attribute.comparingInSpecifiedOrder(context))
                .allSatisfy(a -> {
                });
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
    private static void bestRowIdentifier(final Context context, final List<? extends BestRowIdentifier> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(BestRowIdentifier.comparingInSpecifiedOrder(context))
                .isSortedAccordingTo(BestRowIdentifier.comparingInSpecifiedOrder())
                .allSatisfy(b -> {
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

    // -------------------------------------------------------------------------------------------------------- catalogs
    private static void catalogs(final Context context, final List<? extends Catalog> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(Catalog.comparingInSpecifiedOrder(context))
                .allSatisfy(v -> {
                })
        ;
        for (final var value : values) {
            catalog(context, value);
        }
    }

    private static void catalog(final Context context, final Catalog value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        // -------------------------------------------------------------------------------------------------- procedures
        try {
            final var procedures = context.getProcedures(value, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            // empty
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        try {
            final var schemas = context.getSchemas(value, "%");
            if (schemas.isEmpty()) {
                schemas.add(Schema.of((String) null, null));
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(value.getTableCat(), "%", "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(value.getTableCat(), "%", "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------------ tables
        try {
            final var values = context.getTables(value.getTableCat(), null, "%", (String[]) null);
            if (!databaseProductName(context).equals(DatabaseProductNames.APACHE_DERBY) &&
                !databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL)) {
                values.forEach(t -> {
                    assertType(t).isOf(value);
                });
            }
            tables(context, values);
        } catch (final SQLException sqle) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var values = context.getTablePrivileges(value.getTableCat(), "%", "%");
            if (!databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL)) {
                assertThat(values).allSatisfy(tp -> {
                    assertThat(tp.getTableCat()).isEqualTo(value.getTableCat());
                });
            }
            tablePrivileges(context, values);
        } catch (final SQLException sqle) {
            // empty
        }
    }

    // -------------------------------------------------------------------------------------------- clientInfoProperties
    private static void clientInfoProperties(final Context context, final List<? extends ClientInfoProperty> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(ClientInfoProperty.comparingInSpecifiedOrder(context))
                .allSatisfy(c -> {
                });
//        if (!databaseProductName(context).equals(DatabaseProductNames.MARIA_DB)) {
//            // https://jira.mariadb.org/browse/CONJ-1159
//            assertThat(values).isSortedAccordingTo(
//                    ClientInfoProperty.comparingInSpecifiedOrder(context));
//        }
        for (final var value : values) {
            clientInfoProperty(context, value);
        }
    }

    private static void clientInfoProperty(final Context context, final ClientInfoProperty value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // --------------------------------------------------------------------------------------------------------- columns
    private static void columns(final Context context, final List<? extends Column> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(Column.comparingInSpecifiedOrder(context))
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
//            assertDoesNotThrow(() -> JDBCType.valueOf(column.getDataType()));
            assertThat(value.getOrdinalPosition()).isPositive();
            assertThat(value.getIsNullable()).isNotNull();
            assertThat(value.getIsAutoincrement()).isNotNull();
            assertThat(value.getIsGeneratedcolumn()).isNotNull();
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
            final var columnPrivileges = context.getColumnPrivileges(value);
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
//                .isSortedAccordingTo(ColumnPrivilege.comparingInSpecifiedOrder(context))
                .allSatisfy(c -> {
                });
        for (final var value : values) {
            columnPrivilege(context, value);
        }
    }

    private static void columnPrivilege(final Context context, final ColumnPrivilege value)
            throws SQLException {
        MetadataType_Test_Utils.verify(value);
//        final var isGrantableAsEnum = columnPrivilege.getIsGrantableAsEnum();
    }

    // -------------------------------------------------------------------------------------------------- crossReference
    private static void crossReference(final Context context, final List<CrossReference> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(CrossReference.comparingInSpecifiedOrder(context))
                .allSatisfy(c -> {
                });
        for (final var value : values) {
            crossReference(context, value);
        }
    }

    private static void crossReference(final Context context, final CrossReference value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // ---------------------------------------------------------------------------------------------------- exportedKeys
    private static void exportedKeys(final Context context, final List<? extends ExportedKey> values)
            throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(ExportedKey.comparingInSpecifiedOrder(context))
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
                .isSortedAccordingTo(Function.comparingInSpecifiedOrder(context));
        for (final var value : values) {
            function(context, value);
        }
    }

    private static void function(final Context context, final Function value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        try {
            final var functionColumns = context.getFunctionColumns(value, "%");
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
                .isSortedAccordingTo(FunctionColumn.comparingInSpecifiedOrder(context))
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
                .isSortedAccordingTo(ImportedKey.comparingInSpecifiedOrder(context))
                .allSatisfy(i -> {
                });
        for (final var value : values) {
            importedKey(context, value);
        }
    }

    private static void importedKey(final Context context, final ImportedKey value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        assertThatCode(() -> {
            final var string = value.toString();
        }).doesNotThrowAnyException();
        assertThatCode(() -> {
            final var hashCode = value.hashCode();
        }).doesNotThrowAnyException();
    }

    // ------------------------------------------------------------------------------------------------------- indexInfo
    private static void indexInfo(final Context context, final List<IndexInfo> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(IndexInfo.comparingInSpecifiedOrder(context))
                .allSatisfy(i -> {
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
                .allSatisfy(p -> {
                });
        if (!databaseProductName(context).equals(DatabaseProductNames.MARIA_DB) &&
            !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            assertThat(values).isSortedAccordingTo(
                    Procedure.comparing(context, String.CASE_INSENSITIVE_ORDER));
        }
        for (final var value : values) {
            procedure(context, value);
        }
    }

    private static void procedure(final Context context, final Procedure value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        {
            final var procedureColumns = context.getProcedureColumns(value, "%");
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
                .isSortedAccordingTo(ProcedureColumn.comparing(context, String.CASE_INSENSITIVE_ORDER))
                .allSatisfy(p -> {
                });
        for (final var value : values) {
            procedureColumn(context, value);
        }
    }

    private static void procedureColumn(final Context context, final ProcedureColumn value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        assertThatCode(() -> {
            final var isNullable = value.getIsNullable();
        }).doesNotThrowAnyException();
    }

    // --------------------------------------------------------------------------------------------------------- schemas
    static void schemas(final Context context, final List<? extends Schema> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(Schema.comparingInSpecifiedOrder(context))
                .allSatisfy(s -> {
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
            final var procedures = context.getProcedures(value);
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var superTables = context.getSuperTables(value, "%");
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(value, "%");
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            // empty
        }
        // ------------------------------------------------------------------------------------------------------ tables
        try {
            final var tables = context.getTables(value, "%", (String[]) null);
            tables(context, tables);
        } catch (final SQLException sqle) {
            // empty
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var tablePrivileges = context.getTablePrivileges(value, "%");
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
                .allSatisfy(s -> {
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
                .allSatisfy(t -> {
                });
        for (final var value : values) {
            table(context, value);
        }
    }

    private static void table(final Context context, final Table value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        // -------------------------------------------------------------------------------------------------------------
        assertThat(value.getRefGeneration()).satisfiesAnyOf(
                rg -> assertThat(rg).isNull(),
                rg -> assertThat(rg).isIn(
                        Table.COLUMN_VALUE_REF_GENERATION_DERIVED,
                        Table.COLUMN_VALUE_REF_GENERATION_SYSTEM,
                        Table.COLUMN_VALUE_REF_GENERATION_USER
                )
        );
        // ------------------------------------------------------------------------------------------- bestRowIdentifier
        for (final int scope : BestRowIdentifier.COLUMN_VALUES_SCOPE) {
            for (final boolean nullable : new boolean[] {true, false}) {
                try {
                    final var values = context.getBestRowIdentifier(value, scope, nullable);
                    bestRowIdentifier(context, values);
                } catch (final SQLException sqle) {
                    log.error("failed to getBestRowIdentifier({}, {}, {})", value, scope, nullable, sqle);
                }
            }
        }
        // ----------------------------------------------------------------------------------------------------- columns
        try {
            final var values = context.getColumns(value);
            columns(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getColumns({})", value, sqle);
        }
        // -------------------------------------------------------------------------------------------- columnPrivileges
        try {
            final var values = context.getColumnPrivileges(value);
            columnPrivileges(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getColumnPrivileges({})", value, sqle);
        }
        // ------------------------------------------------------------------------------------------------ exportedKeys
        try {
            final var values = context.getExportedKeys(value);
            exportedKeys(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getExportedKeys({})", value, sqle);
        }
        // ------------------------------------------------------------------------------------------------ importedKeys
        try {
            final var values = context.getImportedKeys(value);
            importedKeys(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getImportedKey({})", value, sqle);
        }
        // --------------------------------------------------------------------------------------------------- indexInfo
        for (final boolean unique : new boolean[] {true, false}) {
            for (final boolean approximate : new boolean[] {true, false}) {
                try {
                    final var values = context.getIndexInfo(value, unique, approximate);
                    indexInfo(context, values);
                } catch (final SQLException sqle) {
                    log.error("failed to getIndexInfo({}, {}, {})", value, unique, approximate, sqle);
                }
            }
        }
        // ------------------------------------------------------------------------------------------------- primaryKeys
        try {
            final var values = context.getPrimaryKeys(value);
            primaryKeys(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getPrimaryKeys({})", value, sqle);
        }
        // ----------------------------------------------------------------------------------------------- pseudoColumns
        {
            final var columnNamePattern = "%";
            try {
                final var values = context.getPseudoColumns(value, columnNamePattern);
                pseudoColumns(context, values);
            } catch (final SQLException sqle) {
                log.error("failed to getPseudoColumns({}, {})", value, columnNamePattern, sqle);
            }
        }
        // ------------------------------------------------------------------------------------------------- superTables
        try {
            final var values = context.getSuperTables(value);
            superTables(context, values);
        } catch (final SQLException sqle) {
            log.error("failed to getSuperTables({})", value, sqle);
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        try {
            final var values = context.getTablePrivileges(value);
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
            final var values = context.getVersionColumns(value);
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
                .allSatisfy(p -> {
                });
        if (!databaseProductName(context).equals(DatabaseProductNames.POSTGRE_SQL) &&
            !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
            assertThat(values).isSortedAccordingTo(
                    PrimaryKey.comparing(context, String.CASE_INSENSITIVE_ORDER));
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
                .isSortedAccordingTo(PseudoColumn.comparing(context, String.CASE_INSENSITIVE_ORDER))
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
//                .isSortedAccordingTo(TablePrivilege.comparingInSpecifiedOrder(context))
                .allSatisfy(t -> {
                });
        for (final var value : values) {
            tablePrivilege(context, value);
        }
    }

    private static void tablePrivilege(final Context context, final TablePrivilege value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
    }

    // ------------------------------------------------------------------------------------------------------ tableTypes
    static void tableTypes(final Context context, final List<? extends TableType> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(TableType.comparing(context, String.CASE_INSENSITIVE_ORDER))
                .allSatisfy(t -> {
                });
        for (final var value : values) {
            tableType(context, value);
        }
    }

    private static void tableType(final Context context, final TableType value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        {
            assertThat(value.getTableType())
                    .isNotBlank()
                    .doesNotStartWithWhitespaces()
                    .doesNotEndWithWhitespaces();
        }
    }

    // -------------------------------------------------------------------------------------------------------- typeInfo
    private static void typeInfo(final Context context, final List<? extends TypeInfo> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
//                .isSortedAccordingTo(TypeInfo.comparingInSpecifiedOrder(context))
                .isSortedAccordingTo(TypeInfo.comparingInSpecifiedOrder())
                .allSatisfy(t -> {
                });
        if (!databaseProductName(context).equals(DatabaseProductNames.MY_SQL) &&
            !databaseProductName(context).equals(DatabaseProductNames.MICROSOFT_SQL_SERVER)) {
//            assertThat(values).isSortedAccordingTo(TypeInfo.comparingInSpecifiedOrder(context));
            assertThat(values).isSortedAccordingTo(TypeInfo.comparingInSpecifiedOrder());
        }
        for (final var value : values) {
            typeInfo(context, value);
        }
    }

    private static void typeInfo(final Context context, final TypeInfo value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        {
            assertThat(value.getTypeName()).isNotNull();
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
    private static void udts(final Context context, final List<? extends UDT> values) throws SQLException {
        assertThat(values)
                .isNotNull()
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .isSortedAccordingTo(UDT.comparing(context, String.CASE_INSENSITIVE_ORDER))
                .allSatisfy(u -> {
                });
        for (final var value : values) {
            udt(context, value);
        }
    }

    private static void udt(final Context context, final UDT value) throws SQLException {
        MetadataType_Test_Utils.verify(value);
        {
            assertThat(value.getTypeName()).isNotNull();
            assertThat(value.getDataType()).isIn(Types.JAVA_OBJECT, Types.STRUCT, Types.DISTINCT);
            assertDoesNotThrow(() -> JDBCType.valueOf(value.getDataType()));
        }
        // ------------------------------------------------------------------------------------------------- .attributes
//        udt.getAttributes(context, "%").forEach(a -> {
//            log.debug("attribute: {}", a);
//        });
        // ------------------------------------------------------------------------------------------------- .superTypes
//        udt.getSuperTypes(context).forEach(st -> {
//            log.debug("superType: {}", st);
//        });
        // -------------------------------------------------------------------------------------------------- .superUDTs
//        udt.getSuperUDTs(context, null).forEach(sudt -> {
//            log.debug("superUDT: {}", sudt);
//        });
        // -------------------------------------------------------------------------------------------------- attributes
        try {
            final var attributes = context.getAttributes(value, "%");
            attributes(context, attributes);
        } catch (final SQLException sqle) {
            // empty
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        try {
            final var superTypes = context.getSuperTypes(value);
            assertThat(superTypes).allSatisfy(st -> {
                assertType(st).isOf(value);
            });
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            // empty
        }
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
