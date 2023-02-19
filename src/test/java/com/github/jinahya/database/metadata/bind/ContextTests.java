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

import com.github.jinahya.database.metadata.bind.VersionColumn.PseudoColumnEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.ReflectionUtils;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextTests {

    static void info(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        log.info("databaseProductName: {}", context.databaseMetaData.getDatabaseProductName());
        log.info("databaseProductVersion: {}", context.databaseMetaData.getDatabaseProductVersion());
        log.info("databaseMajorVersion: {}", context.databaseMetaData.getDatabaseMajorVersion());
        log.info("databaseMinorVersion: {}", context.databaseMetaData.getDatabaseMinorVersion());
        log.info("driverName: {}", context.databaseMetaData.getDriverName());
        log.info("driverVersion: {}", context.databaseMetaData.getDriverVersion());
        log.info("driverMajorVersion: {}", context.databaseMetaData.getDriverMajorVersion());
        log.info("driverMinorVersion: {}", context.databaseMetaData.getDriverMinorVersion());
        log.info("catalogSeparator: {}", context.databaseMetaData.getCatalogSeparator());
        log.info("catalogTerm: {}", context.databaseMetaData.getCatalogTerm());
        log.info("schemaTerm: {}", context.databaseMetaData.getSchemaTerm());
    }

    private static <T> T common(final T value) {
        Objects.requireNonNull(value, "value is null");
        {
            final var string = value.toString();
        }
        {
            final var hashCode = value.hashCode();
        }
        if (value instanceof MetadataType) {
            final var unmappedValues = ((MetadataType) value).getUnmappedValues();
            if (!unmappedValues.isEmpty()) {
                log.warn("has unmapped values: {}", value);
            }
        }
        // .toBuilder().build()
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
                        })
                );
        return value;
    }

    private static void thrown(final String message, final Throwable throwable) {
        Objects.requireNonNull(message, "message is null");
        Objects.requireNonNull(throwable, "throwable is null");
        if (throwable instanceof SQLFeatureNotSupportedException) {
            return;
        }
        log.error("{}", message, throwable);
    }

    private static String databaseProductName;

    static void test(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        {
            databaseProductName = context.databaseMetaData.getDatabaseProductName();
            info(context);
        }
        {
            final List<Catalog> catalogs;
            try {
                catalogs = context.getCatalogs();
                if (catalogs.isEmpty()) {
                    catalogs.add(Catalog.newVirtualInstance());
                }
                catalogs(context, catalogs);
            } catch (final SQLException sqle) {
                thrown("failed; getCatalogs", sqle);
            }
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
            final var procedures = context.getProcedures(null, null, "%");
            procedures(context, procedures);
        } catch (final SQLException sqle) {
            thrown("failed; getProcedures", sqle);
        }
        try {
            final var schemas = context.getSchemas();
            if (schemas.isEmpty()) {
                schemas.add(Schema.newVirtualInstance());
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            thrown("failed; getSchemas", sqle);
        }
        try {
            final var schemas = context.getSchemas((String) null, null);
            if (schemas.isEmpty()) {
                schemas.add(Schema.newVirtualInstance());
            }
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
            thrown("failed; getUDTs", sqle);
        }
    }

    static void attributes(final Context context, final List<? extends Attribute> attributes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attributes, "attributes is null");
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(attributes)
                        .doesNotContainNull()
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(Attribute.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(Attribute.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(attributes)
                        .extracting(Attribute::getAttributeId)
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(AttributeId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(AttributeId.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        for (final var attribute : attributes) {
            attribute(context, attribute);
        }
    }

    static void attribute(final Context context, final Attribute attribute) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(attribute, "attribute is null");
        {
            assertThat(attribute.getTypeName()).isNotNull();
            assertThat(attribute.getAttrName()).isNotNull();
            final var dataType = attribute.getDataType();
            assertDoesNotThrow(() -> JDBCType.valueOf(dataType));
            assertThat(attribute.getAttrTypeName()).isNotNull();
            assertDoesNotThrow(() -> Attribute.NullableEnum.valueOfNullable(attribute.getNullable()));
            assertThat(attribute.getIsNullable()).isNotNull();
            if (dataType == Types.REF) {
                assertThat(attribute.getScopeCatalog()).isNotNull();
                assertThat(attribute.getScopeSchema()).isNotNull();
                assertThat(attribute.getScopeTable()).isNotNull();
                assertThatThrownBy(attribute::getScopeTableId)
                        .isInstanceOf(IllegalStateException.class);
            } else {
                assertThat(attribute.getScopeCatalog()).isNull();
                assertThat(attribute.getScopeSchema()).isNull();
                assertThat(attribute.getScopeTable()).isNull();
                assertDoesNotThrow(attribute::getScopeTableId);
            }
        }
        {
            common(attribute);
            common(attribute.getAttributeId());
        }
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
        {
            final var scope = bestRowIdentifier.getScope();
            assertDoesNotThrow(() -> BestRowIdentifier.ScopeEnum.valueOfScope(scope));
            assertThat(bestRowIdentifier.getColumnName()).isNotNull();
            assertDoesNotThrow(() -> JDBCType.valueOf(bestRowIdentifier.getDataType()));
            assertThat(bestRowIdentifier.getTypeName()).isNotNull();
            final int pseudoColumn = bestRowIdentifier.getPseudoColumn();
            assertDoesNotThrow(() -> BestRowIdentifier.PseudoColumnEnum.valueOfPseudoColumn(pseudoColumn));
        }
        {
            common(bestRowIdentifier);
        }
    }

    static void catalogs(final Context context, final List<? extends Catalog> catalogs) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalogs, "catalogs is null");
        ContextTestUtils.assertCatalogsAreSorted(catalogs);
        ContextTestUtils.assertCatalogIdsAreSorted(catalogs.stream().map(Catalog::getCatalogId));
        for (final var catalog : catalogs) {
            catalog(context, catalog);
        }
    }

    static void catalog(final Context context, final Catalog catalog) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(catalog, "catalog is null");
        {
            assertThat(catalog.getTableCat()).isNotNull();
        }
        final var catalogId = common(common(catalog).getCatalogId());
        try {
            final var schemas = context.getSchemas(catalog, "%");
            {
                final var databaseProductNames = Set.of(
                        // https://sourceforge.net/p/hsqldb/bugs/1671/
                        Memory_Hsql_Test.DATABASE_PRODUCT_NAME
                );
                if (!databaseProductNames.contains(databaseProductName)) {
                    assertThat(schemas)
                            .satisfiesAnyOf(
                                    l -> assertThat(l).isSortedAccordingTo(Schema.CASE_INSENSITIVE_ORDER),
                                    l -> assertThat(l).isSortedAccordingTo(Schema.LEXICOGRAPHIC_ORDER)
                            )
                            .extracting(Schema::getSchemaId)
                            .satisfiesAnyOf(
                                    l -> assertThat(l).isSortedAccordingTo(SchemaId.CASE_INSENSITIVE_ORDER),
                                    l -> assertThat(l).isSortedAccordingTo(SchemaId.LEXICOGRAPHIC_ORDER)
                            )
                            .extracting(SchemaId::getCatalogId)
                            .satisfiesAnyOf(
                                    l -> assertThat(l).isSortedAccordingTo(CatalogId.CASE_INSENSITIVE_ORDER),
                                    l -> assertThat(l).isSortedAccordingTo(CatalogId.LEXICOGRAPHIC_ORDER)
                            );
                }
            }
            assertThat(schemas)
                    .extracting(Schema::getSchemaId)
                    .doesNotHaveDuplicates();
            final var databaseProductNames = Set.of(
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(schemas)
                        .extracting(Schema::getSchemaId)
                        .extracting(SchemaId::getCatalogId)
                        .allMatch(catalogId::equals);
            }
            schemas(context, schemas);
        } catch (final SQLException sqle) {
            thrown("failed: getSchemas", sqle);
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
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Column.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Column.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columns)
                        .extracting(Column::getColumnId)
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(ColumnId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(ColumnId.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        for (final var column : columns) {
            column(context, column);
        }
    }

    static void column(final Context context, final Column column) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(column, "column is null");
        {
            assertThat(column.getTableName()).isNotNull();
            assertThat(column.getColumnName()).isNotNull();
            assertDoesNotThrow(() -> JDBCType.valueOf(column.getDataType()));
            assertThat(column.getOrdinalPosition()).isPositive();
            assertThat(column.getIsNullable()).isNotNull();
            assertThat(column.getIsAutoincrement()).isNotNull();
            assertThat(column.getIsGeneratedcolumn()).isNotNull();
        }
        final var columnId = common(common(column).getColumnId());
        {
            final var value = Column.NullableEnum.valueOfNullable(column.getNullable());
        }
    }

    static void columnPrivileges(final Context context, final List<? extends ColumnPrivilege> columnPrivileges)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(columnPrivileges, "columnPrivileges is null");
        {
            final var databaseProductNames = Set.of(
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columnPrivileges).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ColumnPrivilege.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(columnPrivileges)
                        .extracting(ColumnPrivilege::getColumnPrivilegeId)
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(ColumnPrivilegeId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(ColumnPrivilegeId.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
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
        assertThat(crossReference).satisfiesAnyOf(
                cr -> assertThat(cr).isSortedAccordingTo(CrossReference.CASE_INSENSITIVE_ORDER),
                cr -> assertThat(cr).isSortedAccordingTo(CrossReference.LEXICOGRAPHIC_ORDER)
        );
        assertThat(crossReference)
                .extracting(CrossReference::getPkcolumnId)
                .doesNotHaveDuplicates();
        assertThat(crossReference)
                .extracting(CrossReference::getFkcolumnId)
                .doesNotHaveDuplicates()
                .isSorted();
        for (final var crossReference_ : crossReference) {
            crossReference(context, crossReference_);
        }
    }

    static void crossReference(final Context context, final CrossReference crossReference) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(crossReference, "crossReference is null");
        common(crossReference);
    }

    static void exportedKeys(final Context context, final List<? extends ExportedKey> exportedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(exportedKeys, "exportedKeys is null");
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(exportedKeys).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ExportedKey.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ExportedKey.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(exportedKeys)
                        .extracting(ExportedKey::getFktableId)
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(TableId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(TableId.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
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
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(functions).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(Function.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(Function.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
//                assertThat(functions)
//                        .extracting(Function::getFunctionId)
//                        .satisfiesAnyOf(
//                                l -> assertThat(l).isSortedAccordingTo(FunctionId.CASE_INSENSITIVE_ORDER),
//                                l -> assertThat(l).isSortedAccordingTo(FunctionId.LEXICOGRAPHIC_ORDER)
//                        )
//                ;
            }
        }
        for (final var function : functions) {
            function(context, function);
        }
    }

    private static void function(final Context context, final Function function) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        final var functionId = common(common(function).getFunctionId());
        try {
            final var functionColumns = context.getFunctionColumns(function, "%");
            assertThat(functionColumns)
                    .extracting(FunctionColumn::getFunctionColumnId)
                    .as("functionColumnIds extracted from functionColumns")
                    .doesNotHaveDuplicates();
            final var databaseProductNames = Set.of(
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            {
                if (!databaseProductNames.contains(databaseProductName)) {
                    assertThat(functionColumns)
                            .extracting(FunctionColumn::getFunctionColumnId)
                            .extracting(FunctionColumnId::getFunctionId)
                            .as("functionIds extracted from functionColumns")
                            .allMatch(functionId::equals)
                    ;
                }
            }
            functionColumns(context, function, functionColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getFunctionColumns", sqle);
        }
    }

    private static void functionColumns(final Context context, final Function function,
                                        final List<? extends FunctionColumn> functionColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        Objects.requireNonNull(functionColumns, "functionColumns is null");
        {
            final var databaseProductNames = Set.of(
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(functionColumns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(FunctionColumn.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(FunctionColumn.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(functionColumns)
                        .extracting(FunctionColumn::getFunctionColumnId)
                        .doesNotHaveDuplicates()
//                        .satisfiesAnyOf(
//                                l -> assertThat(l).isSortedAccordingTo(FunctionColumnId.CASE_INSENSITIVE_ORDER),
//                                l -> assertThat(l).isSortedAccordingTo(FunctionColumnId.LEXICOGRAPHIC_ORDER)
//                        )
                ;
            }
        }
        for (final var functionColumn : functionColumns) {
            functionColumn(context, function, functionColumn);
        }
    }

    private static void functionColumn(final Context context, final Function function,
                                       final FunctionColumn functionColumn)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(function, "function is null");
        Objects.requireNonNull(functionColumn, "functionColumn is null");
        common(functionColumn);
        common(functionColumn.getFunctionColumnId());
        final var columnType = FunctionColumn.ColumnTypeEnum.valueOfColumnType(functionColumn.getColumnType());
    }

    static void importedKeys(final Context context, final List<? extends ImportedKey> importedKeys)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(importedKeys, "importedKeys is null");
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(importedKeys).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(ImportedKey.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(ImportedKey.COMPARING_NATUAL)
                );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(importedKeys)
                        .extracting(ImportedKey::getFktableId)
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(TableId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(TableId.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
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
        {
            final var databaseProductNames = Set.of(
                    ""
                    // https://bugs.mysql.com/bug.php?id=109803
//                    TestContainers_MySQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(indexInfo).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(IndexInfo.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(IndexInfo.LEXICOGRAPHIC_ORDER)
                );
            }
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
                    Memory_Hsql_Test.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(procedures)
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(Procedure.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(Procedure.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    Memory_Hsql_Test.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
//                assertThat(procedures)
//                        .extracting(Procedure::getProcedureId)
//                        .satisfiesAnyOf(
//                                l -> assertThat(l).isSortedAccordingTo(ProcedureId.CASE_INSENSITIVE_ORDER),
//                                l -> assertThat(l).isSortedAccordingTo(ProcedureId.LEXICOGRAPHIC_ORDER)
//                        );
            }
        }
        for (final var procedure : procedures) {
            procedure(context, procedure);
        }
    }

    private static void procedure(final Context context, final Procedure procedure) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedure, "procedure is null");
        final var procedureId = common(common(procedure).getProcedureId());
        try {
            final var procedureColumns = context.getProcedureColumns(procedure, "%");
            assertThat(procedureColumns)
                    .extracting(ProcedureColumn::getProcedureColumnId)
                    .doesNotContainNull()
                    .doesNotHaveDuplicates()
                    .extracting(ProcedureColumnId::getProcedureId)
                    .allMatch(procedureId::equals);
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
                ProcedureColumn.CASE_INSENSITIVE_ORDER);
        for (final var procedureColumn : procedureColumns) {
            procedureColumn(context, procedureColumn);
            common(procedureColumn.getProcedureColumnId());
        }
    }

    private static void procedureColumn(final Context context, final ProcedureColumn procedureColumn)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureColumn, "procedureColumn is null");
        common(procedureColumn);
    }

    static void schemas(final Context context, final List<? extends Schema> schemas) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schemas, "schemas is null");
        {
            final var databaseProductNames = Set.of(
                    Memory_Hsql_Test.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(schemas)
                        .extracting(Schema::getSchemaId)
                        .doesNotHaveDuplicates()
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(SchemaId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(SchemaId.LEXICOGRAPHIC_ORDER)
                        )
                        .extracting(SchemaId::getCatalogId)
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(CatalogId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(CatalogId.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        for (final var schema : schemas) {
            schema(context, schema);
        }
    }

    static void schema(final Context context, final Schema schema) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(schema, "schema is null");
        {
            assertThat(schema.getTableSchem()).isNotNull();
        }
        final var schemaId = common(common(schema).getSchemaId());
        try {
            final var superTables = context.getSuperTables(schema, "%");
            assertThat(superTables)
                    .extracting(SuperTable::getTableId)
                    .extracting(TableId::getSchemaId)
                    .allMatch(schemaId::equals);
            assertThat(superTables)
                    .extracting(SuperTable::getSupertableId)
                    .extracting(TableId::getSchemaId)
                    .allMatch(schemaId::equals);
            superTables(context, superTables);
        } catch (final SQLException sqle) {
            thrown("failed; getSuperTables", sqle);
        }
        try {
            final var superTypes = context.getSuperTypes(schema, "%");
            assertThat(superTypes)
                    .extracting(SuperType::getTypeId)
                    .extracting(UDTId::getSchemaId)
                    .allMatch(schemaId::equals);
            if (false) { // 얜 아니다.
                assertThat(superTypes)
                        .extracting(SuperType::getSupertypeId)
                        .extracting(UDTId::getSchemaId)
                        .allMatch(schemaId::equals);
            }
            superTypes(context, superTypes);
        } catch (final SQLException sqle) {
            thrown("failed: getSuperTypes", sqle);
        }
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
        {
            assertThat(superType.getTypeName()).isNotNull();
            assertThat(superType.getSupertypeName()).isNotNull();
        }
        {
            common(superType);
            common(superType.getTypeId());
            common(superType.getSupertypeId());
        }
    }

    static void tables(final Context context, final List<? extends Table> tables) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tables, "tables is null");
        {
            final var databaseProductNames = Set.of(
                    // https://sourceforge.net/p/hsqldb/bugs/1672/
                    Memory_Hsql_Test.DATABASE_PRODUCT_NAME,
                    TestContainers_MariaDB_IT.DATABASE_PRODUCT_NAME,
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                ContextTestUtils.assertTablesAreSorted(tables);
            }
        }
        {
            final var databaseProductNames = Set.of(
                    Memory_H2_Test.DATABASE_PRODUCT_NAME,
                    // https://sourceforge.net/p/hsqldb/bugs/1672/
                    Memory_Hsql_Test.DATABASE_PRODUCT_NAME,
                    TestContainers_MariaDB_IT.DATABASE_PRODUCT_NAME,
                    TestContainers_MySQL_IT.DATABASE_PRODUCT_NAME,
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                ContextTestUtils.assertTableIdsAreSorted(tables);
            }
        }
        for (final var table : tables) {
            table(context, table);
        }
        for (int i = 0; i < 10 && i < tables.size(); i++) { // table 아 많으면 오래 걸린다.
            final var parent = tables.get(i);
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
        {
            assertThat(table.getTableName()).isNotNull();
//            assertThat(table.getTableType()).isNotNull();
        }
        final var tableId = common(common(table).getTableId());
        try {
            for (final var scope : BestRowIdentifier.scopes()) {
                for (final boolean nullable : new boolean[] {true, false}) {
                    final var bestRowIdentifier = context.getBestRowIdentifier(table, scope, nullable);
                    bestRowIdentifier(context, bestRowIdentifier);
                }
            }
        } catch (final SQLException sqle) {
            thrown("failed; getBestRowIdentifier", sqle);
        }
        try {
            final var columns = context.getColumns(table, "%");
            assertThat(columns)
                    .extracting(Column::getColumnId)
                    .doesNotContainNull()
                    .doesNotHaveDuplicates()
                    .extracting(ColumnId::getTableId)
                    .allMatch(tableId::equals);
            columns(context, columns);
        } catch (final SQLException sqle) {
            thrown("failed; getColumns", sqle);
        }
        if (false) {
            try {
                final var columnPrivileges = context.getColumnPrivileges(table, "%");
                assertThat(columnPrivileges)
                        .extracting(ColumnPrivilege::getColumnPrivilegeId)
                        .doesNotContainNull()
                        .doesNotHaveDuplicates()
                        .extracting(ColumnPrivilegeId::getColumnId)
                        .doesNotContainNull()
                        .extracting(ColumnId::getTableId)
                        .doesNotContainNull()
                        .allMatch(tableId::equals);
                columnPrivileges(context, columnPrivileges);
            } catch (final SQLException sqle) {
                thrown("failed; getColumnPrivileges", sqle);
            }
        }
        try {
            final var exportedKeys = context.getExportedKeys(table);
            assertThat(exportedKeys)
                    .extracting(TableKey::getPktableId)
                    .allMatch(tableId::equals);
            exportedKeys(context, exportedKeys);
        } catch (final SQLException sqle) {
            thrown("failed; getExportedKeys", sqle);
        }
        try {
            final var importedKeys = context.getImportedKeys(table);
            assertThat(importedKeys)
                    .extracting(TableKey::getFktableId)
                    .allMatch(tableId::equals);
            importedKeys(context, importedKeys);
        } catch (final SQLException sqle) {
            thrown("failed; getImportedKeys", sqle);
        }
        try {
            for (final boolean unique : new boolean[] {true, false}) {
                for (final boolean approximate : new boolean[] {true, false}) {
                    final var indexInfo = context.getIndexInfo(table, unique, approximate);
                    indexInfo(context, indexInfo);
                }
            }
        } catch (final SQLException sqle) {
            thrown("failed; getIndexInfo", sqle);
        }
        try {
            final var primaryKeys = context.getPrimaryKeys(table);
            assertThat(primaryKeys)
                    .extracting(PrimaryKey::getPrimaryKeyId)
                    .doesNotHaveDuplicates()
                    .extracting(PrimaryKeyId::getTableId)
                    .allMatch(tableId::equals);
            primaryKeys(context, primaryKeys);
        } catch (final SQLException sqle) {
            thrown("failed; getPrimaryKeys", sqle);
        }
        try {
            final var pseudoColumns = context.getPseudoColumns(table, "%");
            assertThat(pseudoColumns)
                    .map(PseudoColumn::getPseudoColumnId)
                    .doesNotHaveDuplicates()
                    .map(PseudoColumnId::getTableId)
                    .allMatch(tableId::equals);
            pseudoColumns(context, pseudoColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getPseudoColumns", sqle);
        }
        try {
            final var versionColumns = context.getVersionColumns(table);
            assertThat(versionColumns)
                    .extracting(v -> v.getColumnId(table))
                    .doesNotHaveDuplicates();
            versionColumns.stream().map(vc -> vc.getColumnId(table)).forEach(ContextTests::common);
            versionColumns(context, versionColumns);
        } catch (final SQLException sqle) {
            thrown("failed; getVersionColumns", sqle);
        }
    }

    static void primaryKeys(final Context context, final List<? extends PrimaryKey> primaryKeys) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKeys, "primaryKeys is null");
        {
            final var databaseProductNames = Set.of(
                    // https://sourceforge.net/p/hsqldb/bugs/1673/
                    Memory_Hsql_Test.DATABASE_PRODUCT_NAME,
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME,
                    // https://bugs.mysql.com/bug.php?id=109808
                    TestContainers_MySQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(primaryKeys).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(PrimaryKey.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(PrimaryKey.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    // https://sourceforge.net/p/hsqldb/bugs/1673/
                    Memory_Hsql_Test.DATABASE_PRODUCT_NAME,
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME,
                    // https://bugs.mysql.com/bug.php?id=109808
                    TestContainers_MySQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(primaryKeys)
                        .extracting(PrimaryKey::getPrimaryKeyId)
                        .doesNotContainNull()
                        .doesNotHaveDuplicates()
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(PrimaryKeyId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(PrimaryKeyId.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        for (final var primaryKey : primaryKeys) {
            primaryKey(context, primaryKey);
        }
    }

    private static void primaryKey(final Context context, final PrimaryKey primaryKey) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(primaryKey, "primaryKey is null");
        common(primaryKey);
        common(primaryKey.getPrimaryKeyId());
    }

    static void pseudoColumns(final Context context, final List<? extends PseudoColumn> pseudoColumns)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumns, "pseudoColumns is null");
        assertThat(pseudoColumns).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(PseudoColumn.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(PseudoColumn.LEXICOGRAPHIC_ORDER)
                )
                .extracting(PseudoColumn::getPseudoColumnId)
                .doesNotContainNull()
                .doesNotHaveDuplicates()
                .satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(PseudoColumnId.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(PseudoColumnId.LEXICOGRAPHIC_ORDER)
                );
        for (final var pseudoColumn : pseudoColumns) {
            pseudoColumn(context, pseudoColumn);
        }
    }

    static void pseudoColumn(final Context context, final PseudoColumn pseudoColumn) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(pseudoColumn, "pseudoColumn is null");
        common(pseudoColumn);
        common(pseudoColumn.getPseudoColumnId());
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
        common(superTable.getTableId());
    }

    static void tablePrivileges(final Context context, final List<? extends TablePrivilege> tablePrivileges)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tablePrivileges, "tablePrivileges is null");
        {
            final var databaseProductNames = Set.of(
                    Memory_Hsql_Test.DATABASE_PRODUCT_NAME
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

    static void tableTypes(final Context context, final List<? extends TableType> tableTypes) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableTypes, "tableTypes is null");
        {
            final var databaseProductNames = Set.of(
                    TestContainers_MariaDB_IT.DATABASE_PRODUCT_NAME // https://jira.mariadb.org/browse/CONJ-1049
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(tableTypes).satisfiesAnyOf(
                        l -> assertThat(l).isSortedAccordingTo(TableType.CASE_INSENSITIVE_ORDER),
                        l -> assertThat(l).isSortedAccordingTo(TableType.LEXICOGRAPHIC_ORDER)
                );
            }
        }
        for (final var tableType : tableTypes) {
            tableType(context, tableType);
        }
    }

    static void tableType(final Context context, final TableType tableType) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableType, "tableType is null");
        {
            assertThat(tableType.getTableType()).isNotNull();
        }
        {
            common(tableType);
        }
    }

    static void typeInfo(final Context context, final List<? extends TypeInfo> typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        {
            final var databaseProductNames = Set.of(
                    TestContainers_MySQL_IT.DATABASE_PRODUCT_NAME // https://bugs.mysql.com/bug.php?id=109931
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(typeInfo)
                        .isSortedAccordingTo(TypeInfo.COMPARING_DATA_TYPE)
                ;
            }
        }
        for (final var typeInfo_ : typeInfo) {
            typeInfo(context, typeInfo_);
        }
    }

    static void typeInfo(final Context context, final TypeInfo typeInfo) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeInfo, "typeInfo is null");
        {
            assertThat(typeInfo.getTypeName()).isNotNull();
            assertDoesNotThrow(() -> JDBCType.valueOf(typeInfo.getDataType()));
            assertDoesNotThrow(() -> TypeInfo.NullableEnum.valueOfNullable(typeInfo.getNullable()));
            assertDoesNotThrow(() -> TypeInfo.SearchableEnum.valueOfSearchable(typeInfo.getSearchable()));
        }
        {
            common(typeInfo);
        }
        {
            final var value = TypeInfo.NullableEnum.valueOfNullable(typeInfo.getNullable());
        }
        {
            final var value = TypeInfo.SearchableEnum.valueOfSearchable(typeInfo.getSearchable());
        }
    }

    static void udts(final Context context, final List<? extends UDT> udts) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udts, "udts is null");
        {
            final var databaseProductNames = Set.of(
                    ""
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(udts)
                        .doesNotContainNull()
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(UDT.COMPARING_IN_CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(UDT.COMPARING_IN_LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        {
            final var databaseProductNames = Set.of(
                    TestContainers_PostgreSQL_IT.DATABASE_PRODUCT_NAME
            );
            if (!databaseProductNames.contains(databaseProductName)) {
                assertThat(udts)
                        .extracting(UDT::getUDTId)
                        .doesNotContainNull()
                        .doesNotHaveDuplicates()
                        .satisfiesAnyOf(
                                l -> assertThat(l).isSortedAccordingTo(UDTId.CASE_INSENSITIVE_ORDER),
                                l -> assertThat(l).isSortedAccordingTo(UDTId.LEXICOGRAPHIC_ORDER)
                        );
            }
        }
        for (final var udt : udts) {
            udt(context, udt);
        }
    }

    static void udt(final Context context, final UDT udt) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(udt, "udt is null");
        {
            assertThat(udt.getTypeName()).isNotNull();
//            assertThat(udt.getClassName()).isNotNull();
//            assertThat(udt.getDataType()).isIn(Types.JAVA_OBJECT, Types.STRUCT, Types.DISTINCT);
            assertDoesNotThrow(() -> JDBCType.valueOf(udt.getDataType()));
        }
        final var udtId = common(common(udt).getUDTId());
        try {
            final var attributes = context.getAttributes(udt, "%");
            assertThat(attributes)
                    .extracting(Attribute::getAttributeId)
                    .doesNotContainNull()
                    .doesNotHaveDuplicates()
                    .extracting(AttributeId::getUdtId)
                    .allMatch(udtId::equals);
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
        {
            assertDoesNotThrow(() -> JDBCType.valueOf(versionColumn.getDataType()));
            assertThat(versionColumn.getTypeName()).isNotNull();
            assertDoesNotThrow(() -> PseudoColumnEnum.valueOfPseudoColumn(versionColumn.getPseudoColumn()));
        }
        {
            common(versionColumn);
        }
    }

    private ContextTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
