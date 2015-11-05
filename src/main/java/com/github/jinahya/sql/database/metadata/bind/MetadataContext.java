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


    public MetadataContext(final DatabaseMetaData databaseMetaData) {

        super();

        if (databaseMetaData == null) {
            throw new NullPointerException("databaseMetaData");
        }

        this.metaData = databaseMetaData;
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


    public MetadataContext addSuppressionPaths(
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


    <T> T bindSingle(final ResultSet results, final Class<T> type, final T instance) throws SQLException {
        for (final Field field : type.getDeclaredFields()) {
            if (field.getAnnotation(NotUsed.class) != null) {
                logger.log(Level.FINE, "@NotUsed: {0}", field);
                continue;
            }
            final String columnLabel = ColumnLabels.get(field);
            if (columnLabel == null) {
                logger.log(Level.FINE, "no @ColumnLabel: {0}", field);
                continue;
            }
            final String suppressionPath = SuppressionPaths.get(field, type);
            if (suppressionPath != null && suppressed(suppressionPath)) {
                logger.log(Level.FINE, "suppressed by {0}: {1}", new Object[]{suppressionPath, field});
                continue;
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            final int columnIndex = results.findColumn(columnLabel);
            final Class<?> fieldType = field.getType();
            final boolean fieldPrimitive = fieldType.isPrimitive();
            final Object object = results.getObject(columnLabel);
            if (object == null && fieldPrimitive) {
                logger.log(Level.WARNING, "null returned: {0}", field);
            }
            if (Boolean.TYPE.equals(field.getType())) {
                final boolean value = results.getBoolean(columnLabel);
                try {
                    field.setBoolean(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Boolean.class.equals(field.getType())) {
                final Boolean value = results.getObject(columnLabel, Boolean.class);
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Short.TYPE.equals(field.getType())) {
                final short value = results.getShort(columnLabel);
                try {
                    field.setShort(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Short.class.equals(field.getType())) {
                Object value = results.getObject(columnLabel);
                if (value != null && !(value instanceof Short)) {
                    logger.log(Level.INFO, "casting {0}({1}) for {2}", new Object[]{value.getClass(), value, field});
                    if (value instanceof Number) {
                        value = ((Number) value).shortValue();
                        logger.log(Level.INFO, "casted: {0}", value);
                    }
                }
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Integer.TYPE.equals(field.getType())) {
                final int value = results.getInt(columnLabel);
                try {
                    field.setInt(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Integer.class.equals(field.getType())) {
                Object value = results.getObject(columnLabel);
                if (value != null && !(value instanceof Integer)) {
                    logger.log(Level.INFO, "casting {0}({1}) for {2}", new Object[]{value.getClass(), value, field});
                    if (value instanceof Number) {
                        value = ((Number) value).intValue();
                        logger.log(Level.INFO, "casted: ({0})", value);
                    }
                }
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Long.TYPE.equals(field.getType())) {
                final long value = results.getLong(columnLabel);
                try {
                    field.setLong(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (Long.class.equals(field.getType())) {
                Object value = results.getObject(columnLabel);
                if (value != null && !(value instanceof Integer)) {
                    logger.log(Level.INFO, "casting {0}({1}) for {2}", new Object[]{value.getClass(), value, field});
                    if (value instanceof Number) {
                        value = ((Number) value).longValue();
                        logger.log(Level.INFO, "casted: {0}", value);
                    }
                }
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
            if (String.class.equals(field.getType())) {
                final String value = results.getString(columnLabel);
                try {
                    field.set(instance, value);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException(iae);
                }
                continue;
            }
        }
        return instance;
    }


    <T> T bindSingle(final ResultSet results, final Class<T> type) throws SQLException {
        final T instance;
        try {
            instance = type.newInstance();
        } catch (final InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
        return bindSingle(results, type, instance);
    }


    <T> List<T> list(final ResultSet results, final Class<T> type, final List<T> list) throws SQLException {
        while (results.next()) {
            final T instance;
            try {
                instance = type.newInstance();
            } catch (final InstantiationException ie) {
                throw new RuntimeException(ie);
            } catch (final IllegalAccessException iae) {
                throw new RuntimeException(iae);
            }
            list.add(bindSingle(results, type, instance));
        }
        return list;
    }


    <T> List<T> list(final ResultSet results, final Class<T> type) throws SQLException {
        return list(results, type, new ArrayList<T>());
    }


    private boolean column(final Field field) {

        return false;
    }


    private <T> boolean method(final Class<T> parentType, final T parentInstance, final Field field)
        throws ReflectiveOperationException {

        final MethodInvocation methodInvocation = field.getAnnotation(MethodInvocation.class);
        if (methodInvocation == null) {
            return false;
        }
        final String methodName = methodInvocation.name();
        final Class<?>[] methodParameterTypes = methodInvocation.types();
        for (final MethodInvocationArgs invocationArgs : methodInvocation.args()) {
            final String[] argNames = invocationArgs.value();
            final Object[] args = new Object[argNames.length];
            for (int i = 0; i < argNames.length; i++) {
                final String argName = argNames[i];
                if ("null".equals(argName)) {
                    args[i] = null;
                    continue;
                }
                if (argName.startsWith(":")) {
                    final Field f
                        = parentType.getDeclaredField(argName.substring(1));
                    if (f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    args[i] = field.get(parentInstance);
                    continue;
                }
                args[i] = methodParameterTypes[i]
                    .getMethod("valueOf", String.class)
                    .invoke(null, argName);
            }
            final Object fieldValue = DatabaseMetaData.class.getMethod(methodName, methodParameterTypes).invoke(metaData, args);
        }

        return false;
    }


    public Metadata getMetadata() throws SQLException {

        final Metadata instance = new Metadata();
        if (!suppressed("metadata/URL")) {
            instance.setUrl(metaData.getURL());
        }
        if (!suppressed("metadata/allProceduresAreCallable")) {
            instance.setAllProceduresAreCallable(metaData.allProceduresAreCallable());
        }
        if (!suppressed("metadata/allTablesAreSelectable")) {
            instance.setAllTablesAreSelectable(metaData.allTablesAreSelectable());
        }
        if (!suppressed("metadata/autoCommitFailureClosesAllResultSets")) {
            instance.setAutoCommitFailureClosesAllResultSets(
                metaData.autoCommitFailureClosesAllResultSets());
        }
        if (!suppressed("metadata/catalogSeparator")) {
            instance.setCatalogSeparator(metaData.getCatalogSeparator());
        }
        if (!suppressed("metadata/catalogTerm")) {
            instance.setCatalogTerm(metaData.getCatalogTerm());
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
                metaData.getConnection().toString());
        }
        if (!suppressed("metadata/dataDefinitionCausesTransactionCommit")) {
            instance.setDataDefinitionCausesTransactionCommit(
                metaData.dataDefinitionCausesTransactionCommit());
        }
        if (!suppressed("metadata/dataDefinitionIgnoredInTransactions")) {
            instance.setDataDefinitionIgnoredInTransactions(
                metaData.dataDefinitionIgnoredInTransactions());
        }
        instance.setDatabaseMajorVersion(metaData.getDatabaseMajorVersion());
        instance.setDatabaseMinorVersion(metaData.getDatabaseMinorVersion());
        instance.setDatabaseProductName(metaData.getDatabaseProductName());
        instance.setDatabaseProductVersion(metaData.getDatabaseProductVersion());

        instance.setDriverMajorVersion(metaData.getDriverMajorVersion());
        instance.setDriverMinorVersion(metaData.getDriverMinorVersion());
        instance.setDriverName(metaData.getDriverName());
        instance.setDriverVersion(metaData.getDriverVersion());

        instance.setNumericFunctions(metaData.getNumericFunctions());
        instance.setStringFunctions(metaData.getStringFunctions());
        instance.setSystemFunctions(metaData.getSystemFunctions());
        instance.setTimeDateFunctions(metaData.getTimeDateFunctions());

        if (!suppressed("metadata/deletesAreDetected")) {
            for (final int type : DeletesAreDetected.TYPES) {
                instance.getDeletesAreDetected().add(
                    new DeletesAreDetected()
                    .type(type).
                    value(metaData.deletesAreDetected(type)));
            }
        }
        if (!suppressed("metadata/doesMaxRowSizeIncludeBlobs")) {
            instance.setDoesMaxRowSizeIncludeBlobs(
                metaData.doesMaxRowSizeIncludeBlobs());
        }
        if (!suppressed("metadata/generatedKeyAlwaysReturned")) {
            instance.setGeneratedKeyAlwaysReturned(
                metaData.generatedKeyAlwaysReturned());
        }
        if (!suppressed("metadata/insertsAreDetected")) {
            for (final int type : InsertsAreDetected.TYPES) {
                instance.getInsertsAreDetected().add(
                    new InsertsAreDetected()
                    .type(type).
                    value(metaData.insertsAreDetected(type)));
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
                field.set(instance, method.invoke(metaData));
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
            instance.setProcedureTerm(metaData.getProcedureTerm());
        }
        if (!suppressed("metadata/resultSetHoldabiltiy")) {
            instance.setResultSetHoldability(metaData.getResultSetHoldability());
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
            instance.setUserName(metaData.getUserName());
        }

        return instance;
    }


    public List<Attribute> getAttributes(final String catalog,
                                         final String schemaPattern,
                                         final String typeNamePattern,
                                         final String attributeNamePattern)
        throws SQLException {

        final List<Attribute> list = new ArrayList<Attribute>();

        final ResultSet results = metaData.getAttributes(
            catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        try {
            list(results, Attribute.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<BestRowIdentifier> getBestRowIdentifier(
        final String catalog, final String schema, final String table,
        final int scope, final boolean nullable)
        throws SQLException {

        final List<BestRowIdentifier> list = new ArrayList<BestRowIdentifier>();

        final ResultSet results = metaData.getBestRowIdentifier(
            catalog, schema, table, scope, nullable);
        try {
            list(results, BestRowIdentifier.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Catalog> getCatalogs() throws SQLException {

        final List<Catalog> list = new ArrayList<Catalog>();

        final ResultSet results = metaData.getCatalogs();
        try {
            list(results, Catalog.class, list);
        } finally {
            results.close();
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

        final ResultSet results = metaData.getClientInfoProperties();
        if (results == null) {
            logger.log(Level.WARNING, "null from getClientInfoProperties");
            return list;
        }
        try {
            list(results, ClientInfoProperty.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Column> getColumns(final String catalog,
                                   final String schemaPattern,
                                   final String tableNamePattern,
                                   final String columnNamePattern)
        throws SQLException {

        final List<Column> list = new ArrayList<Column>();

        final ResultSet resultSet = metaData.getColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            list(resultSet, Column.class, list);
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

        final ResultSet results = metaData.getColumnPrivileges(
            catalog, schema, table, columnNamePattern);
        try {
            list(results, ColumnPrivilege.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<FunctionColumn> getFunctionColumns(
        final String catalog, final String schemaPattern,
        final String functionNamePattern, final String columnNamePattern)
        throws SQLException {

        final List<FunctionColumn> list = new ArrayList<FunctionColumn>();

        final ResultSet results = metaData.getFunctionColumns(
            catalog, schemaPattern, functionNamePattern, columnNamePattern);
        if (results == null) {
            logger.warning("getFunctionColumns -> null");
        }
        if (results != null) {
            try {
                list(results, FunctionColumn.class, list);
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

        final List<Function> list = new ArrayList<Function>();

        final ResultSet results = metaData.getFunctions(
            catalog, schemaPattern, functionNamePattern);
        if (results == null) {
            logger.warning("null from getFunctions");
        }
        if (results != null) {
            try {
                list(results, Function.class, list);
            } finally {
                results.close();
            }
        }

        return list;
    }


    public List<ExportedKey> getExportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<ExportedKey> list = new ArrayList<ExportedKey>();

        final ResultSet results = metaData.getExportedKeys(
            catalog, schema, table);
        try {
            list(results, ExportedKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ImportedKey> getImportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<ImportedKey> list = new ArrayList<ImportedKey>();

        final ResultSet results = metaData.getImportedKeys(
            catalog, schema, table);
        try {
            list(results, ImportedKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<IndexInfo> getIndexInfo(
        final String catalog, final String schema, final String table,
        final boolean unique, final boolean approximate)
        throws SQLException {

        final List<IndexInfo> list = new ArrayList<IndexInfo>();

        final ResultSet results = metaData.getIndexInfo(
            catalog, schema, table, unique, approximate);
        try {
            list(results, IndexInfo.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<PrimaryKey> getPrimaryKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<PrimaryKey> list = new ArrayList<PrimaryKey>();

        final ResultSet results = metaData.getPrimaryKeys(
            catalog, schema, table);
        try {
            list(results, PrimaryKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ProcedureColumn> getProcedureColumns(
        final String catalog, final String schemaPattern,
        final String procedureNamePattern, final String columnNamePattern)
        throws SQLException {

        final List<ProcedureColumn> list = new ArrayList<ProcedureColumn>();

        final ResultSet results = metaData.getProcedureColumns(
            catalog, schemaPattern, procedureNamePattern, columnNamePattern);
        try {
            list(results, ProcedureColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Procedure> getProcedures(final String catalog,
                                         final String schemaPattern,
                                         final String procedureNamePattern)
        throws SQLException {

        final List<Procedure> list = new ArrayList<Procedure>();

        final ResultSet results = metaData.getProcedures(
            catalog, schemaPattern, procedureNamePattern);
        try {
            list(results, Procedure.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<PseudoColumn> getPseudoColumns(final String catalog,
                                               final String schemaPattern,
                                               final String tableNamePattern,
                                               final String columnNamePattern)
        throws SQLException {

        final List<PseudoColumn> list = new ArrayList<PseudoColumn>();

        final ResultSet results = metaData.getPseudoColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        if (results == null) {
            logger.log(Level.WARNING, "null returned from getPseudoColumns");
        } else {
            try {
                list(results, PseudoColumn.class, list);
            } finally {
                results.close();
            }
        }

        return list;
    }


    public List<SchemaName> getSchemas() throws SQLException {

        final List<SchemaName> list = new ArrayList<SchemaName>();

        final ResultSet results = metaData.getSchemas();
        if (results == null) {
            logger.warning("null from getSchemas");
        }
        if (results != null) {
            try {
                list(results, SchemaName.class, list);
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

        final ResultSet results = metaData.getSchemas(
            catalog, schemaPattern);
        if (results == null) {
            logger.warning("null returned from getSchemas");
        }
        if (results != null) {
            try {
                list(results, Schema.class, list);
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

        final ResultSet results = metaData.getTables(
            catalog, schemaPattern, tableNamePattern, types);
        if (results == null) {
            logger.warning("null returned from getTables");
        }
        if (results != null) {
            try {
                list(results, Table.class, list);
            } finally {
                results.close();
            }
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

        final ResultSet results = metaData.getTablePrivileges(
            catalog, schemaPattern, tableNamePattern);
        try {
            list(results, TablePrivilege.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<TableType> getTableTypes() throws SQLException {

        final List<TableType> list = new ArrayList<TableType>();

        final ResultSet results = metaData.getTableTypes();
        try {
            list(results, TableType.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<TypeInfo> getTypeInfo() throws SQLException {

        final List<TypeInfo> list = new ArrayList<TypeInfo>();

        final ResultSet results = metaData.getTypeInfo();
        try {
            list(results, TypeInfo.class, list);
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

        final ResultSet results = metaData.getUDTs(
            catalog, schemaPattern, typeNamePattern, types);
        try {
            list(results, UserDefinedType.class, list);
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

        final ResultSet results = metaData.getVersionColumns(
            catalog, schema, table);
        try {
            list(results, VersionColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    private final DatabaseMetaData metaData;


    private Set<String> suppressionPaths;


}

