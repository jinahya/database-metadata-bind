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

import java.sql.DatabaseMetaData;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class MetadataTestUtils {

    static void verify(final Metadata metadata) {
        ValidationTestUtils.requireValid(metadata);
        // -------------------------------------------------------------------------------------------------- attributes
        {
            final var attributes = metadata.getAttributes();
            assertThat(attributes)
                    .isNotNull()
                    .doesNotContainNull()
                    .doesNotHaveDuplicates();
        }
        {
            for (final var udt : metadata.getUDTs()) {
                final var attributes = metadata.getAttributesOf(udt);
                assertThat(attributes)
                        .isNotNull()
                        .doesNotContainNull()
                        .satisfiesAnyOf(
                                l -> assertThat(l).isEmpty(),
                                l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                                    assertThat(Attribute.IS_OF.test(v, udt)).isTrue();
                                })
                        );
            }
        }
        // ------------------------------------------------------------------------------------------ bestRowIdentifiers
        {
            final var bestRowIdentifiers = metadata.getBestRowIdentifiers();
            bestRowIdentifiers.forEach((scope, m) -> {
                m.forEach((nullalbe, l) -> {
                    assertThat(l).doesNotHaveDuplicates();
                });
            });
        }
        {
            for (final int scope : new int[] {DatabaseMetaData.bestRowTemporary, DatabaseMetaData.bestRowTransaction,
                                              DatabaseMetaData.bestRowSession}) {
                for (final boolean nullable : new boolean[] {true, false}) {
                    for (final var table : metadata.getTables()) {
                        final var bestRowIdentifiers = metadata.getBestRowIdentifiersOf(scope, nullable, table, table);
                        assertThat(bestRowIdentifiers).satisfiesAnyOf(
                                s -> assertThat(s).isEmpty(),
                                s -> assertThat(s).isNotEmpty().allSatisfy(v -> {
                                    assertThat(v.getScope()).isEqualTo(scope);
                                    assertThat(v.isOf(table)).isTrue();
                                })
                        );
                    }
                }
            }
        }
        // ----------------------------------------------------------------------------------------------------- columns
        {
            final var columns = metadata.getColumns();
            assertThat(columns).doesNotHaveDuplicates();
        }
        {
            for (final var table : metadata.getTables()) {
                final var columns = metadata.getColumnsOf(table, table);
                assertThat(columns).isNotNull().allSatisfy(v -> {
                    assertThat(v.getTableCat()).isEqualTo(table.getTableCat());
                    assertThat(v.getTableSchem()).isEqualTo(table.getTableSchem());
                    assertThat(v.getTableName()).isEqualTo(table.getTableName());
                });
            }
        }
        // -------------------------------------------------------------------------------------------- columnPrivileges
        {
            final var columnPrivileges = metadata.getColumnPrivileges();
            assertThat(columnPrivileges).doesNotHaveDuplicates();
        }
        {
            for (final var column : metadata.getColumns()) {
                final var columnPrivileges = metadata.getColumnPrivilegesOf(column, column);
                assertThat(columnPrivileges).satisfiesAnyOf(
                        v -> assertThat(v).isEmpty(),
                        v -> assertThat(v).isNotEmpty().allSatisfy(v2 -> {
                            assertThat(v2.isOf(column)).isTrue();
                        })
                );
            }
        }
        // ---------------------------------------------------------------------------------------------- crossReference
        {
            final var crossReference = metadata.getCrossReference_();
            assertThat(crossReference).doesNotHaveDuplicates();
        }
        {
            for (final var table : metadata.getTables()) {
                {
                    final var crossReference = metadata.getCrossReferenceOfPktable(table, table);
                    assertThat(crossReference).satisfiesAnyOf(
                            l -> assertThat(l).isEmpty(),
                            l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                                assertThat(v.isOfPktable(table)).isTrue();
                            })
                    );
                }
                {
                    final var crossReference = metadata.getCrossReferenceOfFktable(table, table);
                    assertThat(crossReference).satisfiesAnyOf(
                            l -> assertThat(l).isEmpty(),
                            l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                                assertThat(v.isOfFktable(table)).isTrue();
                            })
                    );
                }
            }
        }
        // ------------------------------------------------------------------------------------------------ exportedKeys
        {
            final var exportedKeys = metadata.getExportedKeys();
        }
        {
            for (final var table : metadata.getTables()) {
                final var exportedKeys = metadata.getExportedKeysOfPktable(table);
                assertThat(exportedKeys).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> {
                            assertThat(l).isNotEmpty().allSatisfy(v -> {
                                assertThat(TableKey.IS_OF_PKTABLE.test(v, table)).isTrue();
                            });
                        }
                );
            }
        }
        {
            for (final var table : metadata.getTables()) {
                final var exportedKeys = metadata.getExportedKeysOfFktable(table);
                assertThat(exportedKeys).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> {
                            assertThat(l).isNotEmpty().allSatisfy(v -> {
                                assertThat(TableKey.IS_OF_FKTABLE.test(v, table)).isTrue();
                            });
                        }
                );
            }
        }
        // --------------------------------------------------------------------------------------------------- functions
        {
            final var functions = metadata.getFunctions();
        }
        {
            for (final var catalog : metadata.getCatalogs()) {
                final var functions = metadata.getFunctionsOf(catalog);
                assertThat(functions).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(v.isOf(catalog)).isTrue();
                        })
                );
            }
        }
        {
            for (final var schema : metadata.getSchemas()) {
                final var functions = metadata.getFunctionsOf(schema);
                assertThat(functions).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(v.isOf(schema)).isTrue();
                        })
                );
            }
        }
        // --------------------------------------------------------------------------------------------- functionColumns
        {
            final var functionColumns = metadata.getFunctionColumns();
            assertThat(functionColumns).doesNotHaveDuplicates();
        }
        {
            for (final var function : metadata.getFunctions()) {
                final var functionColumns = metadata.getFunctionColumnsOf(function);
                assertThat(functionColumns).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(FunctionColumn.IS_OF.test(v, function)).isTrue();
                        })
                );
            }
        }
        // ------------------------------------------------------------------------------------------------ importedKeys
        {
            final var importedKeys = metadata.getImportedKeys();
        }
        {
            for (final var table : metadata.getTables()) {
                final var importedKeys = metadata.getImportedKeysOfPktable(table);
                assertThat(importedKeys).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> {
                            assertThat(l).isNotEmpty().allSatisfy(v -> {
                                assertThat(TableKey.IS_OF_PKTABLE.test(v, table)).isTrue();
                            });
                        }
                );
            }
        }
        {
            for (final var table : metadata.getTables()) {
                final var importedKeys = metadata.getImportedKeysOfFktable(table);
                assertThat(importedKeys).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> {
                            assertThat(l).isNotEmpty().allSatisfy(v -> {
                                assertThat(TableKey.IS_OF_FKTABLE.test(v, table)).isTrue();
                            });
                        }
                );
            }
        }
        // --------------------------------------------------------------------------------------------------- indexInfo
        {
            final var indexInfo = metadata.getIndexInfo();
        }
        {
            for (final var unique : new boolean[] {true, false}) {
                for (final var approximate : new boolean[] {true, false}) {
                    final var indexInfo = metadata.getIndexInfo().get(unique).get(approximate);
                    assertThat(indexInfo).doesNotHaveDuplicates();
                }
            }
        }
        {
            for (final var unique : new boolean[] {true, false}) {
                for (final var approximate : new boolean[] {true, false}) {
                    for (final var table : metadata.getTables()) {
                        final var indexInfo = metadata.getIndexInfoOf(unique, approximate, table);
                        assertThat(indexInfo).satisfiesAnyOf(
                                l -> assertThat(l).isEmpty(),
                                l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                                    assertThat(IndexInfo.IS_OF.test(v, table)).isTrue();
                                })
                        );
                    }
                }
            }
        }
        // -------------------------------------------------------------------------------------------------- primaryKey
        {
            final var primaryKeys = metadata.getPrimaryKeys();
            assertThat(primaryKeys).doesNotHaveDuplicates();
        }
        {
            for (final var table : metadata.getTables()) {
                final var primaryKeys = metadata.getPrimaryKeysOf(table);
                assertThat(primaryKeys).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(PrimaryKey.IS_OF.test(v, table)).isTrue();
                        })
                );
            }
        }
        // -------------------------------------------------------------------------------------------------- procedures
        {
            final var procedures = metadata.getProcedures();
            assertThat(procedures).doesNotHaveDuplicates();
        }
        {
            for (final var catalog : metadata.getCatalogs_()) {
                final var procedures = metadata.getProceduresOf(catalog);
                assertThat(procedures).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(Procedure.IS_OF_CATALOG.test(v, catalog)).isTrue();
                        })
                );
            }
        }
        {
            for (final var schema : metadata.getSchemas_()) {
                final var procedures = metadata.getProceduresOf(schema);
                assertThat(procedures).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(Procedure.IS_OF_SCHEMA.test(v, schema)).isTrue();
                        })
                );
            }
        }
        // -------------------------------------------------------------------------------------------- procedureColumns
        {
            final var procedureColumns = metadata.getProcedureColumns();
            assertThat(procedureColumns).doesNotHaveDuplicates();
        }
        {
            for (final var procedure : metadata.getProcedures()) {
                final var procedureColumns = metadata.getProcedureColumnsOf(procedure);
                assertThat(procedureColumns).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(ProcedureColumn.IS_OF.test(v, procedure)).isTrue();
                        })
                );
            }
        }
        // ------------------------------------------------------------------------------------------------ pseudoColumn
        {
            final var pseudoColumns = metadata.getPseudoColumns();
            assertThat(pseudoColumns).doesNotHaveDuplicates();
        }
        {
            for (final var table : metadata.getTables()) {
                final var pseudoColumns = metadata.getPseudoColumnsOf(table);
                assertThat(pseudoColumns).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(PseudoColumn.IS_OF.test(v, table)).isTrue();
                        })
                );
            }
        }
        // ----------------------------------------------------------------------------------------------------- schemas
        {
            final var schemas = metadata.getSchemas();
            assertThat(schemas).doesNotHaveDuplicates();
        }
        {
            for (final var catalog : metadata.getCatalogs_()) {
                final var schemas = metadata.getSchemasOf(catalog);
                assertThat(schemas).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(Schema.IS_OF.test(v, catalog)).isTrue();
                        })
                );
            }
        }
        // ------------------------------------------------------------------------------------------------- superTables
        {
            final var superTables = metadata.getSuperTables();
            assertThat(superTables).doesNotHaveDuplicates();
        }
        {
            for (final var table : metadata.getTables()) {
                final var superTables = metadata.getSuperTablesOf(table);
                assertThat(superTables).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(SuperTable.IS_OF.test(v, table)).isTrue();
                        })
                );
            }
        }
        // -------------------------------------------------------------------------------------------------- superTypes
        {
            final var superTypes = metadata.getSuperTypes();
            assertThat(superTypes).doesNotHaveDuplicates();
        }
        {
            for (final var udt : metadata.getUDTs()) {
                final var superTypes = metadata.getSuperTypesOf(udt);
                assertThat(superTypes).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(SuperType.IS_OF.test(v, udt)).isTrue();
                        })
                );
            }
        }
        // ------------------------------------------------------------------------------------------------------ tables
        {
            final var tables = metadata.getTables();
            assertThat(tables).doesNotHaveDuplicates();
        }
        {
            for (final var catalog : metadata.getCatalogs_()) {
                final var tables = metadata.getTablesOf(catalog);
                assertThat(tables).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(Table.IS_OF_CATALOG.test(v, catalog)).isTrue();
                        })
                );
            }
        }
        {
            for (final var schema : metadata.getSchemas_()) {
                final var tables = metadata.getTablesOf(schema);
                assertThat(tables).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(Table.IS_OF_SCHEMA.test(v, schema)).isTrue();
                        })
                );
            }
        }
        // --------------------------------------------------------------------------------------------- tablePrivileges
        {
            final var tablePrivileges = metadata.getTablePrivileges();
            assertThat(tablePrivileges).doesNotHaveDuplicates();
        }
        {
            for (final var table : metadata.getTables()) {
                final var tablePrivileges = metadata.getTablePrivilegesOf(table);
                assertThat(tablePrivileges).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(TablePrivilege.IS_OF.test(v, table)).isTrue();
                        })
                );
            }
        }
        // -------------------------------------------------------------------------------------------------- tableTypes
        {
            final var tableTypes = metadata.getTableTypes();
            assertThat(tableTypes).doesNotHaveDuplicates();
        }
        // ---------------------------------------------------------------------------------------------------- typeInfo
        {
            final var typeInfo = metadata.getTypeInfo_();
            assertThat(typeInfo).doesNotHaveDuplicates();
        }
        // -------------------------------------------------------------------------------------------------------- UDTs
        {
            final var udts = metadata.getUDTs();
            assertThat(udts).doesNotHaveDuplicates();
        }
        {
            for (final var catalog : metadata.getCatalogs_()) {
                final var udts = metadata.getUDTsOf(catalog);
                assertThat(udts).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(UDT.IS_OF_CATALOG.test(v, catalog)).isTrue();
                        })
                );
            }
        }
        {
            for (final var schema : metadata.getSchemas_()) {
                final var udts = metadata.getUDTsOf(schema);
                assertThat(udts).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(UDT.IS_OF_SCHEMA.test(v, schema)).isTrue();
                        })
                );
            }
        }
        // --------------------------------------------------------------------------------------------- versionColumns
        {
            final var versionColumns = metadata.getVersionColumns();
            assertThat(versionColumns).doesNotHaveDuplicates();
        }
        {
            for (final var table : metadata.getTables()) {
                final var versionColumns = metadata.getVersionColumnsOf(table);
                assertThat(versionColumns).satisfiesAnyOf(
                        l -> assertThat(l).isEmpty(),
                        l -> assertThat(l).isNotEmpty().allSatisfy(v -> {
                            assertThat(VersionColumn.IS_OF.test(v, table)).isTrue();
                        })
                );
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        {
            assertThat(metadata.getNumericFunctions())
                    .doesNotContainNull()
                    .allSatisfy(v -> {
                        assertThat(v)
                                .isNotBlank()
                        ;
                    });
        }
        {
            assertThat(metadata.getSQLKeywords())
                    .doesNotContainNull()
                    .doesNotHaveDuplicates()
                    .allSatisfy(v -> {
                        assertThat(v)
                                .isNotBlank()
                        ;
                    });
        }
        {
            assertThat(metadata.getStringFunctions())
                    .doesNotContainNull()
                    .allSatisfy(v -> {
                        assertThat(v)
                                .isNotBlank()
                        ;
                    });
        }
        {
            assertThat(metadata.getSystemFunctions())
                    .doesNotContainNull()
                    .allSatisfy(v -> {
                        assertThat(v)
                                .isNotBlank()
                        ;
                    });
        }
        {
            assertThat(metadata.getTimeDateFunctions())
                    .doesNotContainNull()
                    .allSatisfy(v -> {
                        assertThat(v)
                                .isNotBlank()
                                .doesNotContainAnyWhitespaces();
                    });
        }
    }

    private MetadataTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
