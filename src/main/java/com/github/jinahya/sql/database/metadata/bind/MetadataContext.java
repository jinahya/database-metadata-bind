/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */


package com.github.jinahya.sql.database.metadata.bind;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MetadataContext {


    private static final Logger logger = getLogger(Metadata.class.getName());


//    private static <T> T fieldValue(final Field field, final Object instance, Class<T> type) {
//
//        if (field.isAccessible()) {
//            field.setAccessible(true);
//        }
//
//        try {
//            return type.cast(field.get(instance));
//        } catch (final IllegalAccessException iae) {
//            throw new RuntimeException(iae);
//        }
//    }
//
//
//    private static <T> void fieldValue(final Field field, final Object instance, T value) {
//
//        if (field.isAccessible()) {
//            field.setAccessible(true);
//        }
//
//        try {
//            field.set(instance, value);
//        } catch (final IllegalAccessException iae) {
//            throw new RuntimeException(iae);
//        }
//    }
    public MetadataContext(final DatabaseMetaData databaseMetaData) {

        super();

        if (databaseMetaData == null) {
            throw new NullPointerException("databaseMetaData");
        }

        this.databaseMetaData = databaseMetaData;
    }


    public DatabaseMetaData getDatabaseMetaData() {

        return databaseMetaData;
    }


    public boolean addSuppressionPath(final String suppressionPath) {

        if (suppressionPath == null) {
            throw new NullPointerException("null suppressionPath");
        }

        if (suppressionPaths == null) {
            suppressionPaths = new TreeSet<String>();
        }

        return suppressionPaths.add(suppressionPath);
    }


    public MetadataContext suppressionPath(
        final String suppressionPath, final String... otherSuppressionPaths) {

        addSuppressionPath(suppressionPath);

        if (otherSuppressionPaths != null) {
            for (final String otherSuppressionPath : otherSuppressionPaths) {
                addSuppressionPath(otherSuppressionPath);
            }
        }

        return this;
    }


    boolean suppressed(final String path) {

        if (path == null) {
            throw new NullPointerException("null path");
        }

        if (suppressionPaths == null) {
            return false;
        }

        return suppressionPaths.contains(path);
    }


    private boolean column(final Field field) {

        return false;
    }


    private <T> boolean method(final Class<?> type, final T instance, final Field field) {

        final MethodInvocation methodInvocation = field.getAnnotation(MethodInvocation.class);
        if (methodInvocation == null) {
            return false;
        }
        final String methodName = methodInvocation.name();
        final Class<?>[] parameterTypes = methodInvocation.types();
        for (final MethodInvocationArgs invocationArgs : methodInvocation.args()) {
            final String[] args = invocationArgs.value();
        }

        return false;
    }


    public Metadata getMetadata() throws SQLException {

        final Metadata instance = new Metadata();
        if (!suppressed("metadata/URL")) {
            instance.setUrl(databaseMetaData.getURL());
        }
        if (!suppressed("metadata/allProceduresAreCallable")) {
            instance.setAllProceduresAreCallable(databaseMetaData.allProceduresAreCallable());
        }
        if (!suppressed("metadata/allTablesAreSelectable")) {
            instance.setAllTablesAreSelectable(databaseMetaData.allTablesAreSelectable());
        }
        if (!suppressed("metadata/autoCommitFailureClosesAllResultSets")) {
            instance.setAutoCommitFailureClosesAllResultSets(
                databaseMetaData.autoCommitFailureClosesAllResultSets());
        }
        if (!suppressed("metadata/catalogSeparator")) {
            instance.setCatalogSeparator(databaseMetaData.getCatalogSeparator());
        }
        if (!suppressed("metadata/catalogTerm")) {
            instance.setCatalogTerm(databaseMetaData.getCatalogTerm());
        }
        if (!suppressed("metadata/catalogs")) {
            instance.getCatalogs().addAll(getCatalogs());
        }
        if (!suppressed("metadata/clientProperties")) {
            instance.getClientInfoProperties().addAll(
                getClientInfoProperties());
        }
        if (!suppressed("metadata/connectionString")) {
            instance.setConnectionString(
                databaseMetaData.getConnection().toString());
        }
        if (!suppressed("metadata/dataDefinitionCausesTransactionCommit")) {
            instance.setDataDefinitionCausesTransactionCommit(
                databaseMetaData.dataDefinitionCausesTransactionCommit());
        }
        if (!suppressed("metadata/dataDefinitionIgnoredInTransactions")) {
            instance.setDataDefinitionIgnoredInTransactions(
                databaseMetaData.dataDefinitionIgnoredInTransactions());
        }
        instance.setDatabaseMajorVersion(databaseMetaData.getDatabaseMajorVersion());
        instance.setDatabaseMinorVersion(databaseMetaData.getDatabaseMinorVersion());
        instance.setDatabaseProductName(databaseMetaData.getDatabaseProductName());
        instance.setDatabaseProductVersion(databaseMetaData.getDatabaseProductVersion());

        instance.setDriverMajorVersion(databaseMetaData.getDriverMajorVersion());
        instance.setDriverMinorVersion(databaseMetaData.getDriverMinorVersion());
        instance.setDriverName(databaseMetaData.getDriverName());
        instance.setDriverVersion(databaseMetaData.getDriverVersion());

        instance.setNumericFunctions(databaseMetaData.getNumericFunctions());
        instance.setStringFunctions(databaseMetaData.getStringFunctions());
        instance.setSystemFunctions(databaseMetaData.getSystemFunctions());
        instance.setTimeDateFunctions(databaseMetaData.getTimeDateFunctions());

        if (!suppressed("metadata/deletesAreDetected")) {
            for (final int type : DeletesAreDetected.TYPES) {
                instance.getDeletesAreDetected().add(
                    new DeletesAreDetected()
                    .type(type).
                    value(databaseMetaData.deletesAreDetected(type)));
            }
        }
        if (!suppressed("metadata/doesMaxRowSizeIncludeBlobs")) {
            instance.setDoesMaxRowSizeIncludeBlobs(
                databaseMetaData.doesMaxRowSizeIncludeBlobs());
        }
        if (!suppressed("metadata/generatedKeyAlwaysReturned")) {
            instance.setGeneratedKeyAlwaysReturned(
                databaseMetaData.generatedKeyAlwaysReturned());
        }
        if (!suppressed("metadata/insertsAreDetected")) {
            for (final int type : InsertsAreDetected.TYPES) {
                instance.getInsertsAreDetected().add(
                    new InsertsAreDetected()
                    .type(type).
                    value(databaseMetaData.insertsAreDetected(type)));
            }
        }

        // maxXXX
        for (final Field field : Metadata.class.getDeclaredFields()) {
            final Type genericType = field.getGenericType();
            final int modifier = field.getModifiers();
            if (Modifier.isStatic(modifier)) {
                System.err.println("static");
                continue;
            }
            final String fieldName = field.getName();
            if (!fieldName.startsWith("max")) {
                System.out.println("not max");
                continue;
            }
            final String suppresionPath = SuppressionPaths.get(field);
            if (suppressed(suppresionPath)) {
                System.err.println("suppressed?");
                continue;
            }
            System.err.println("--------------> " + field);
            final String methodName
                = "get" + fieldName.substring(0, 1).toUpperCase()
                  + fieldName.substring(1);
            final Method method;
            try {
                method = DatabaseMetaData.class.getMethod(methodName);
            } catch (final NoSuchMethodException nsme) {
                logger.log(Level.WARNING, "method not found: {0}", methodName);
                continue;
            }
            System.err.println("--------------> " + method);
            if (field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                field.set(instance, method.invoke(databaseMetaData));
            } catch (final ReflectiveOperationException roe) {
                logger.log(Level.WARNING, "failed to set: {0}", field);
                continue;
            }
        }

        if (!suppressed("metadata/maxBinaryLiteralLength")) {
        }
        if (!suppressed("metadata/maxCatalogNameLength")) {
        }
        if (!suppressed("metadata/maxCharLiteralLength")) {
        }
        if (!suppressed("metadata/maxColumnNameLength")) {
        }
        if (!suppressed("metadata/maxColumnsInGroupBy")) {
        }
        if (!suppressed("metadata/maxColumnsInIndex")) {
        }
        if (!suppressed("metadata/maxColumnsInOrderBy")) {
        }
        if (!suppressed("metadata/maxColumnsInSelect")) {
        }
        if (!suppressed("metadata/maxColumnsInTable")) {
        }
        if (!suppressed("metadata/maxConnections")) {
        }
        if (!suppressed("metadata/maxCursorNameLength")) {
        }
        if (!suppressed("metadata/maxIndexLength")) {
        }
        if (!suppressed("metadata/maxLogicalLobSize")) {
        }
        if (!suppressed("metadata/maxProcedureNameLength")) {
        }
        if (!suppressed("metadata/maxRowSize")) {
        }
        if (!suppressed("metadata/maxSchemaNameLength")) {
        }
        if (!suppressed("metadata/maxStatementLength")) {
        }
        if (!suppressed("metadata/maxStatements")) {
        }
        if (!suppressed("metadata/maxTableNameLength")) {
        }
        if (!suppressed("metadata/maxTablesInSelect")) {
        }
        if (!suppressed("metadata/maxUserNameLength")) {
        }

        if (!suppressed("metadata/procedureTerm")) {
            instance.setProcedureTerm(databaseMetaData.getProcedureTerm());
        }
        if (!suppressed("metadata/resultSetHoldabiltiy")) {
            instance.setResultSetHoldability(databaseMetaData.getResultSetHoldability());
        }
//        if (!suppressed("metadata/rowIdLifetime")) {
//            instance.setRowIdLifetime(databaseMetaData.getRowIdLifetime());
//        }
        if (!suppressed("metadata/schemaNames")) {
            instance.getSchemaNames().addAll(getSchemas());
        }
        if (!suppressed("metadata/typeInfo")) {
            instance.getTypeInfo().addAll(getTypeInfo());
        }
        if (!suppressed("metadata/tableTypes")) {
            instance.getTableTypes().addAll(getTableTypes());
        }
        if (!suppressed("metadata/userName")) {
            instance.setUserName(databaseMetaData.getUserName());
        }

        return instance;
    }


    public List<Attribute> getAttributes(final String catalog,
                                         final String schemaPattern,
                                         final String typeNamePattern,
                                         final String attributeNamePattern)
        throws SQLException {

        final List<Attribute> list = new ArrayList<Attribute>();

        final ResultSet resultSet = databaseMetaData.getAttributes(
            catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, Attribute.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<BestRowIdentifier> getBestRowIdentifier(
        final String catalog, final String schema, final String table,
        final int scope, final boolean nullable)
        throws SQLException {

        final List<BestRowIdentifier> list = new ArrayList<BestRowIdentifier>();

        final ResultSet resultSet = databaseMetaData.getBestRowIdentifier(
            catalog, schema, table, scope, nullable);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, BestRowIdentifier.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<Catalog> getCatalogs() throws SQLException {

        final List<Catalog> list = new ArrayList<Catalog>();

        final ResultSet resultSet = databaseMetaData.getCatalogs();
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, Catalog.class));
            }
        } finally {
            resultSet.close();
        }

        if (list.isEmpty()) {
            final Catalog catalog = new Catalog();
            catalog.setTableCat("");
            list.add(catalog);
        }

        if (!suppressed("catalog/schemas")) {
            for (final Catalog catalog : list) {
                catalog.getSchemas().addAll(
                    getSchemas(catalog.getTableCat(), null));
            }
        }

        return list;
    }


    public List<ClientInfoProperty> getClientInfoProperties()
        throws SQLException {

        final List<ClientInfoProperty> list
            = new ArrayList<ClientInfoProperty>();

        final ResultSet resultSet = databaseMetaData.getClientInfoProperties();
        if (resultSet == null) {
            logger.log(Level.WARNING, "null from getClientInfoProperties");
            return list;
        }
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, ClientInfoProperty.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<Column> getColumns(final String catalog,
                                   final String schemaPattern,
                                   final String tableNamePattern,
                                   final String columnNamePattern)
        throws SQLException {

        final List<Column> list = new ArrayList<Column>();

        final ResultSet resultSet = databaseMetaData.getColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, Column.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<ColumnPrivilege> getColumnPrivileges(
        final String catalog, final String schema, final String table,
        final String columnNamePattern)
        throws SQLException {

        final List<ColumnPrivilege> list = new ArrayList<ColumnPrivilege>();

        final ResultSet resultSet = databaseMetaData.getColumnPrivileges(
            catalog, schema, table, columnNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, ColumnPrivilege.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<FunctionColumn> getFunctionColumns(
        final String catalog, final String schemaPattern,
        final String functionNamePattern, final String columnNamePattern)
        throws SQLException {

        final List<FunctionColumn> list = new ArrayList<FunctionColumn>();

        final ResultSet results = databaseMetaData.getFunctionColumns(
            catalog, schemaPattern, functionNamePattern, columnNamePattern);
        if (results == null) {
            logger.warning("null from getFunctionColumns");
        }
        if (results != null) {
            try {
                while (results.next()) {
                    list.add(ColumnRetriever.single(this, results, FunctionColumn.class));
                }
            } finally {
                results.close();
            }
        }

        return list;
    }


    public List<Function> getFunctions(final String catalog,
                                       final String schemaPattern,
                                       final String functionNamePattern)
        throws SQLException {

        final List<Function> functions = new ArrayList<Function>();

        final ResultSet resultSet = databaseMetaData.getFunctions(
            catalog, schemaPattern, functionNamePattern);
        if (resultSet == null) {
            logger.warning("null from getFunctions");
        }
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    functions.add(ColumnRetriever.single(this, resultSet, Function.class));
                }
            } finally {
                resultSet.close();
            }
        }

        return functions;
    }


    public List<ExportedKey> getExportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<ExportedKey> list = new ArrayList<ExportedKey>();

        final ResultSet resultSet = databaseMetaData.getExportedKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, ExportedKey.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<ImportedKey> getImportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<ImportedKey> list = new ArrayList<ImportedKey>();

        final ResultSet resultSet = databaseMetaData.getImportedKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, ImportedKey.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<IndexInfo> getIndexInfo(
        final String catalog, final String schema, final String table,
        final boolean unique, final boolean approximate)
        throws SQLException {

        final List<IndexInfo> list = new ArrayList<IndexInfo>();

        final ResultSet resultSet = databaseMetaData.getIndexInfo(
            catalog, schema, table, unique, approximate);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, IndexInfo.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<PrimaryKey> getPrimaryKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<PrimaryKey> list = new ArrayList<PrimaryKey>();

        final ResultSet resultSet = databaseMetaData.getPrimaryKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, PrimaryKey.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<ProcedureColumn> getProcedureColumns(
        final String catalog, final String schemaPattern,
        final String procedureNamePattern, final String columnNamePattern)
        throws SQLException {

        final List<ProcedureColumn> list = new ArrayList<ProcedureColumn>();

        final ResultSet resultSet = databaseMetaData.getProcedureColumns(
            catalog, schemaPattern, procedureNamePattern, columnNamePattern);
        try {
            ColumnRetriever.list(this, resultSet, ProcedureColumn.class, list);
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<Procedure> getProcedures(final String catalog,
                                         final String schemaPattern,
                                         final String procedureNamePattern)
        throws SQLException {

        final List<Procedure> list = new ArrayList<Procedure>();

        final ResultSet resultSet = databaseMetaData.getProcedures(
            catalog, schemaPattern, procedureNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, Procedure.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<PseudoColumn> getPseudoColumns(final String catalog,
                                               final String schemaPattern,
                                               final String tableNamePattern,
                                               final String columnNamePattern)
        throws SQLException {

        final List<PseudoColumn> list = new ArrayList<PseudoColumn>();

        final ResultSet resultSet = databaseMetaData.getPseudoColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        if (resultSet == null) {
            logger.log(Level.WARNING, "null from getPseudoColumns");
            return list;
        }
        try {
            ColumnRetriever.list(this, resultSet, PseudoColumn.class, list);
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<SchemaName> getSchemas() throws SQLException {

        final List<SchemaName> list = new ArrayList<SchemaName>();

        final ResultSet results = databaseMetaData.getSchemas();
        if (results == null) {
            logger.warning("null from getSchemas");
        }
        if (results != null) {
            try {
                while (results.next()) {
                    list.add(ColumnRetriever.single(this, results, SchemaName.class));
                }
            } finally {
                results.close();
            }
        }

        return list;
    }


    public List<Schema> getSchemas(final String catalog,
                                   final String schemaPattern)
        throws SQLException {

        final List<Schema> list = new ArrayList<Schema>();

        final ResultSet results = databaseMetaData.getSchemas(
            catalog, schemaPattern);
        if (results == null) {
            logger.warning("null from getSchemas");
        }
        if (results != null) {
            try {
                while (results.next()) {
                    list.add(ColumnRetriever.single(this, results, Schema.class));
                }
            } finally {
                results.close();
            }
        }

        if (list.isEmpty()) {
            final Schema schema = new Schema();
            schema.setTableSchem("");
            list.add(schema);
        }

        for (final Schema schema : list) {
            if (!suppressed("schema/functionColumns")) {
                schema.getFunctionColumns().addAll(
                    getFunctionColumns(
                        schema.getTableCatalog(), schema.getTableSchem(), null,
                        null));
            }
            if (!suppressed("schema/functions")) {
                schema.getFunctions().addAll(
                    getFunctions(
                        schema.getTableCatalog(), schema.getTableSchem(),
                        null));
            }
            if (!suppressed("schema/procedureColumns")) {
                schema.getProcedureColumns().addAll(
                    getProcedureColumns(
                        schema.getTableCatalog(), schema.getTableSchem(), null,
                        null));
            }
            if (!suppressed("schema/procedures")) {
                schema.getProcedures().addAll(
                    getProcedures(
                        schema.getTableCatalog(), schema.getTableSchem(),
                        null));
            }
            if (!suppressed("schema/tables")) {
                schema.getTables().addAll(
                    getTables(catalog, schemaPattern, null, null));
            }
            if (!suppressed("schema/userDefinedTypes")) {
                schema.getUserDefinedTypes().addAll(
                    getUDTs(catalog, schemaPattern, null, null));
            }
        }

        return list;
    }


    public List<Table> getTables(final String catalog,
                                 final String schemaPattern,
                                 final String tableNamePattern,
                                 final String[] types)
        throws SQLException {

        final List<Table> list = new ArrayList<Table>();

        final ResultSet resultSet = databaseMetaData.getTables(
            catalog, schemaPattern, tableNamePattern, types);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, Table.class));
            }
        } finally {
            resultSet.close();
        }

        for (final Table table : list) {
            if (!suppressed("table/bestRowIdentifiers")) {
                table.getBestRowIdentifiers().addAll(getBestRowIdentifier(
                    catalog, schemaPattern, table.getTableName(),
                    DatabaseMetaData.bestRowTemporary, true));
                table.getBestRowIdentifiers().addAll(getBestRowIdentifier(
                    catalog, schemaPattern, table.getTableName(),
                    DatabaseMetaData.bestRowTransaction, true));
                table.getBestRowIdentifiers().addAll(getBestRowIdentifier(
                    catalog, schemaPattern, table.getTableName(),
                    DatabaseMetaData.bestRowSession, true));
            }
            if (!suppressed("table/columns")) {
                table.getColumns().addAll(getColumns(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName(), null));
            }
            if (!suppressed("table/columnPrivileges")) {
                table.getColumnPrivileges().addAll(getColumnPrivileges(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName(), null));
            }
            if (!suppressed("table/exportedKeys")) {
                table.getExportedKeys().addAll(getExportedKeys(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
            if (!suppressed("table/importedKeys")) {
                table.getImportedKeys().addAll(getImportedKeys(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
            if (!suppressed("table/indexInfo")) {
                table.getIndexInfo().addAll(getIndexInfo(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName(), false, false));
            }
            if (!suppressed("table/primaryKeys")) {
                table.getPrimaryKeys().addAll(getPrimaryKeys(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
            if (!suppressed("table/pseudoColumns")) {
                table.getPseudoColumns().addAll(
                    getPseudoColumns(
                        table.getTableCat(), table.getTableSchem(),
                        table.getTableName(), null));
            }
            if (!suppressed("table/tablePrivileges")) {
                table.getTablePrivileges().addAll(getTablePrivileges(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
            if (!suppressed("table/versionColumns")) {
                table.getVersionColumns().addAll(
                    getVersionColumns(
                        table.getTableCat(), table.getTableSchem(),
                        table.getTableName()));
            }
        }

        return list;
    }


    public List<TablePrivilege> getTablePrivileges(
        final String catalog, final String schemaPattern,
        final String tableNamePattern)
        throws SQLException {

        final List<TablePrivilege> list = new ArrayList<TablePrivilege>();

        final ResultSet resultSet = databaseMetaData.getTablePrivileges(
            catalog, schemaPattern, tableNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, TablePrivilege.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<TableType> getTableTypes() throws SQLException {

        final List<TableType> list = new ArrayList<TableType>();

        final ResultSet results = databaseMetaData.getTableTypes();
        try {
            ColumnRetriever.list(this, results, TableType.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<TypeInfo> getTypeInfo() throws SQLException {

        final List<TypeInfo> list = new ArrayList<TypeInfo>();

        final ResultSet results = databaseMetaData.getTypeInfo();
        try {
            ColumnRetriever.list(this, results, TypeInfo.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<UserDefinedType> getUDTs(
        final String catalog, final String schemaPattern,
        final String typeNamePattern, final int[] types)
        throws SQLException {

        final List<UserDefinedType> list = new ArrayList<UserDefinedType>();

        final ResultSet results = databaseMetaData.getUDTs(
            catalog, schemaPattern, typeNamePattern, types);
        try {
            ColumnRetriever.list(this, results, UserDefinedType.class, list);
        } finally {
            results.close();
        }

        if (!suppressed("userDefinedType/attributes")) {
            for (final UserDefinedType userDefinedType : list) {
                userDefinedType.getAttributes().addAll(getAttributes(
                    userDefinedType.getTypeCat(),
                    userDefinedType.getTypeSchem(),
                    userDefinedType.getTypeName(), null));
            }
        }

        return list;
    }


    public List<VersionColumn> getVersionColumns(final String catalog,
                                                 final String schema,
                                                 final String table)
        throws SQLException {

        final List<VersionColumn> list = new ArrayList<VersionColumn>();

        final ResultSet resultSet = databaseMetaData.getVersionColumns(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.single(this, resultSet, VersionColumn.class));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    private final DatabaseMetaData databaseMetaData;


    private Set<String> suppressionPaths;


}

