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


import static java.beans.Introspector.decapitalize;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    private static final Map<Class<?>, Class<?>> WRAPPERS;


    static {
        final Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>(9);
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(short.class, Short.class);
        map.put(void.class, Void.class);
        WRAPPERS = Collections.unmodifiableMap(map);
    }


    private static String capitalize(final String name) {

        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }


    private static String getterName(final String fieldName) {

        return "get" + capitalize(fieldName);
    }


    private static String getterName(final Field field) {

        return getterName(field.getName());
    }


    private static String setterName(final String fieldName) {

        return "set" + capitalize(fieldName);
    }


    private static String setterName(final Field field) {

        return setterName(field.getName());
    }


    public static String suppressionPath(final Field field, final Class<?> klass) {

        if (field == null) {
            throw new NullPointerException("null field");
        }

        if (klass == null) {
            throw new NullPointerException("null klass");
        }

        if (!field.getDeclaringClass().isAssignableFrom(klass)) {
            throw new IllegalArgumentException(
                "klass(" + klass
                + ") is not assignable to the specified field(" + field
                + ")'s declaring class(" + field.getDeclaringClass() + ")");
        }

        return decapitalize(klass.getSimpleName()) + "/" + field.getName();
    }


    public static String suppressionPath(final Field field) {

        return suppressionPath(field, field.getDeclaringClass());
    }


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


    private Object fieldValue(final Field field, final Object obj)
        throws ReflectiveOperationException {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        return field.get(obj);
    }


    private Object fieldValue(final Class<?> type, final String name, final Object obj)
        throws ReflectiveOperationException {

        return fieldValue(type.getDeclaredField(name), obj);
    }


    private void fieldValue(final Field field, final Object obj, Object[] args, Object value)
        throws ReflectiveOperationException, SQLException {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        final Class<?> fieldType = field.getType();
        //logger.log(Level.INFO, "fieldType: {0}", new Object[]{fieldType});
        if ((!fieldType.isPrimitive() && value == null)
            || fieldType.isInstance(value)) {
            field.set(obj, value);
            return;
        }
        final Class<?> valueType = value == null ? null : value.getClass();
        //logger.log(Level.INFO, "valueType: {0}", new Object[]{valueType});
        if (fieldType == Boolean.TYPE) {
            if (!Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0}({1}) to {2}",
                           new Object[]{value, valueType, field});
                return;
            }
            field.setBoolean(obj, (Boolean) value);
            return;
        }
        if (fieldType == Short.TYPE) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            field.setShort(obj, ((Number) value).shortValue());
            return;
        }
        if (fieldType == Short.class) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            if (value != null && !Short.class.isInstance(value)) {
                value = ((Number) value).shortValue();
            }
            field.set(obj, value);
            return;
        }
        if (fieldType == Integer.TYPE) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            if (!Integer.class.isInstance(value)) {
                value = ((Number) value).intValue();
            }
            field.setInt(obj, (Integer) value);
            return;
        }
        if (fieldType == Integer.class) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            if (value != null && !Integer.class.isInstance(value)) {
                value = ((Number) value).intValue();
            }
            field.set(obj, value);
            return;
        }
        if (fieldType == Long.TYPE) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            if (Long.class.isInstance(value)) {
                value = ((Number) value).longValue();
            }
            field.setLong(obj, (Long) value);
            return;
        }
        if (fieldType == Long.class) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot set {0} to {1}",
                           new Object[]{value, field});
                return;
            }
            if (value != null && !Long.class.isInstance(value)) {
                value = ((Number) value).longValue();
            }
            field.set(obj, value);
            return;
        }
        if (fieldType == List.class) {
            @SuppressWarnings("unchecked")
            final List<Object> list = (List<Object>) field.getDeclaringClass()
                .getMethod(getterName(field)).invoke(obj);
            final Type genericType = field.getGenericType();
            if (!ParameterizedType.class.isInstance(genericType)) {
                logger.log(Level.WARNING, "not a ParameterizedType({0}): {1}",
                           new Object[]{genericType, field});
            }
            final Type elementType = ((ParameterizedType) genericType)
                .getActualTypeArguments()[0];
            final String typeName = elementType.getTypeName();
            final Class<?> typeClass = Class.forName(typeName);
            if (ResultSet.class.isInstance(value)) {
                bindAll((ResultSet) value, typeClass, list);
                return;
            }
            list.add(typeClass
                .getMethod("valueOf", Object[].class, Object.class)
                .invoke(null, args, value));
            return;
        }

        logger.log(Level.WARNING, "value({0}) not handled: {1}",
                   new Object[]{value, field});
    }


    private <T> T bindSingle(final ResultSet results, final Class<T> type,
                             final T obj)
        throws SQLException, ReflectiveOperationException {

        if (results != null) {
            for (final Field field : type.getDeclaredFields()) {
                final String suppressionPath = MetadataContext.suppressionPath(field);
                if (suppressed(suppressionPath)) {
                    //logger.log(Level.FINE, "suppressed: {0}", field);
                    continue;
                }
                final Label label = field.getAnnotation(Label.class);
                if (label == null) {
                    continue;
                }
                final Object value = results.getObject(label.value());
                fieldValue(field, obj, null, value);
            }
        }

        for (final Field field : type.getDeclaredFields()) {
            final String suppressionPath = MetadataContext.suppressionPath(field);
            if (suppressed(suppressionPath)) {
                //logger.log(Level.FINE, "suppressed: {0}", field);
                continue;
            }
            final Invocation invocation = field.getAnnotation(Invocation.class);
            //logger.log(Level.INFO, "invocation: {0}", new Object[]{invocation});
            if (invocation == null) {
                continue;
            }
            final Class<?>[] types = invocation.types();
            final Method method = DatabaseMetaData.class.getMethod(
                invocation.name(), invocation.types());
            for (final InvocationArgs invocationArgs : invocation.argsarr()) {
                final String[] names = invocationArgs.value();
                final Object[] args = new Object[names.length];
                for (int i = 0; i < names.length; i++) {
                    final String name = names[i];
                    if ("null".equals(name)) {
                        args[i] = null;
                        continue;
                    }
                    if (name.startsWith(":")) {
                        args[i] = fieldValue(
                            type.getDeclaredField(name.substring(1)), obj);
                        continue;
                    }
                    if (types[i] == String.class) {
                        args[i] = name;
                        continue;
                    }
                    if (types[i].isPrimitive()) {
                        types[i] = WRAPPERS.get(types[i]);
                    }
                    args[i] = types[i].getMethod("valueOf", String.class)
                        .invoke(null, name);
                }
                fieldValue(field, obj, args, method.invoke(metaData, args));
            }
        }

        return obj;
    }


    private <T> T bindSingle(final ResultSet results, final Class<T> type)
        throws SQLException, ReflectiveOperationException {

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


    private <T> List<? super T> bindAll(final ResultSet results,
                                        final Class<T> type,
                                        final List<? super T> list)
        throws SQLException, ReflectiveOperationException {

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


    private <T> List<? super T> bindAll(final ResultSet results, final Class<T> type)
        throws SQLException, ReflectiveOperationException {

        return bindAll(results, type, new ArrayList<T>());
    }


    public Metadata getMetadata() throws SQLException, ReflectiveOperationException {

        final Metadata instance = new Metadata();

        if (true) {
            final Metadata metadata = bindSingle(null, Metadata.class);
            final List<Catalog> catalogs = metadata.getCatalogs();
            if (catalogs.isEmpty()) {
                final Catalog catalog = new Catalog().tableCat("");
                bindSingle(null, Catalog.class, catalog);
                catalogs.add(catalog);
                final List<Schema> schema = catalog.getSchemas();
                if (schema.isEmpty()) {
                }
            }
            return metadata;
        }

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
//        if (!suppressed("metadata/connectionString")) {
//            instance.setConnectionString(
//                metaData.getConnection().toString());
//        }
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
            final String suppresionPath = MetadataContext.suppressionPath(field);
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
        throws SQLException, ReflectiveOperationException {

        final List<Attribute> list = new ArrayList<Attribute>();

        final ResultSet results = metaData.getAttributes(
            catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        try {
            MetadataContext.this.bindAll(results, Attribute.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<BestRowIdentifier> getBestRowIdentifier(
        final String catalog, final String schema, final String table,
        final int scope, final boolean nullable)
        throws SQLException, ReflectiveOperationException {

        final List<BestRowIdentifier> list = new ArrayList<BestRowIdentifier>();

        final ResultSet results = metaData.getBestRowIdentifier(
            catalog, schema, table, scope, nullable);
        try {
            MetadataContext.this.bindAll(results, BestRowIdentifier.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Catalog> getCatalogs() throws SQLException, ReflectiveOperationException {

        final List<Catalog> list = new ArrayList<Catalog>();

        final ResultSet results = metaData.getCatalogs();
        try {
            MetadataContext.this.bindAll(results, Catalog.class, list);
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
        throws SQLException, ReflectiveOperationException {

        final List<ClientInfoProperty> list
            = new ArrayList<ClientInfoProperty>();

        final ResultSet results = metaData.getClientInfoProperties();
        if (results == null) {
            logger.log(Level.WARNING, "null from getClientInfoProperties");
            return list;
        }
        try {
            MetadataContext.this.bindAll(results, ClientInfoProperty.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Column> getColumns(final String catalog,
                                   final String schemaPattern,
                                   final String tableNamePattern,
                                   final String columnNamePattern)
        throws SQLException, ReflectiveOperationException {

        final List<Column> list = new ArrayList<Column>();

        final ResultSet resultSet = metaData.getColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            MetadataContext.this.bindAll(resultSet, Column.class, list);
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<ColumnPrivilege> getColumnPrivileges(
        final String catalog, final String schema, final String table,
        final String columnNamePattern)
        throws SQLException, ReflectiveOperationException {

        final List<ColumnPrivilege> list = new ArrayList<ColumnPrivilege>();

        final ResultSet results = metaData.getColumnPrivileges(
            catalog, schema, table, columnNamePattern);
        try {
            MetadataContext.this.bindAll(results, ColumnPrivilege.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<FunctionColumn> getFunctionColumns(
        final String catalog, final String schemaPattern,
        final String functionNamePattern, final String columnNamePattern)
        throws SQLException, ReflectiveOperationException {

        final List<FunctionColumn> list = new ArrayList<FunctionColumn>();

        final ResultSet results = metaData.getFunctionColumns(
            catalog, schemaPattern, functionNamePattern, columnNamePattern);
        if (results == null) {
            logger.warning("getFunctionColumns -> null");
        }
        if (results != null) {
            try {
                MetadataContext.this.bindAll(results, FunctionColumn.class, list);
            } finally {
                results.close();
            }
        }

        return list;
    }


    public List<Function> getFunctions(final String catalog,
                                       final String schemaPattern,
                                       final String functionNamePattern)
        throws SQLException, ReflectiveOperationException {

        final List<Function> list = new ArrayList<Function>();

        final ResultSet results = metaData.getFunctions(
            catalog, schemaPattern, functionNamePattern);
        if (results == null) {
            logger.warning("null from getFunctions");
        }
        if (results != null) {
            try {
                MetadataContext.this.bindAll(results, Function.class, list);
            } finally {
                results.close();
            }
        }

        return list;
    }


    public List<ExportedKey> getExportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException, ReflectiveOperationException {

        final List<ExportedKey> list = new ArrayList<ExportedKey>();

        final ResultSet results = metaData.getExportedKeys(
            catalog, schema, table);
        try {
            MetadataContext.this.bindAll(results, ExportedKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ImportedKey> getImportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException, ReflectiveOperationException {

        final List<ImportedKey> list = new ArrayList<ImportedKey>();

        final ResultSet results = metaData.getImportedKeys(
            catalog, schema, table);
        try {
            MetadataContext.this.bindAll(results, ImportedKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<IndexInfo> getIndexInfo(
        final String catalog, final String schema, final String table,
        final boolean unique, final boolean approximate)
        throws SQLException, ReflectiveOperationException {

        final List<IndexInfo> list = new ArrayList<IndexInfo>();

        final ResultSet results = metaData.getIndexInfo(
            catalog, schema, table, unique, approximate);
        try {
            MetadataContext.this.bindAll(results, IndexInfo.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<PrimaryKey> getPrimaryKeys(
        final String catalog, final String schema, final String table)
        throws SQLException, ReflectiveOperationException {

        final List<PrimaryKey> list = new ArrayList<PrimaryKey>();

        final ResultSet results = metaData.getPrimaryKeys(
            catalog, schema, table);
        try {
            MetadataContext.this.bindAll(results, PrimaryKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ProcedureColumn> getProcedureColumns(
        final String catalog, final String schemaPattern,
        final String procedureNamePattern, final String columnNamePattern)
        throws SQLException, ReflectiveOperationException {

        final List<ProcedureColumn> list = new ArrayList<ProcedureColumn>();

        final ResultSet results = metaData.getProcedureColumns(
            catalog, schemaPattern, procedureNamePattern, columnNamePattern);
        try {
            MetadataContext.this.bindAll(results, ProcedureColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Procedure> getProcedures(final String catalog,
                                         final String schemaPattern,
                                         final String procedureNamePattern)
        throws SQLException, ReflectiveOperationException {

        final List<Procedure> list = new ArrayList<Procedure>();

        final ResultSet results = metaData.getProcedures(
            catalog, schemaPattern, procedureNamePattern);
        try {
            MetadataContext.this.bindAll(results, Procedure.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<PseudoColumn> getPseudoColumns(final String catalog,
                                               final String schemaPattern,
                                               final String tableNamePattern,
                                               final String columnNamePattern)
        throws SQLException, ReflectiveOperationException {

        final List<PseudoColumn> list = new ArrayList<PseudoColumn>();

        final ResultSet results = metaData.getPseudoColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        if (results == null) {
            logger.log(Level.WARNING, "null returned from getPseudoColumns");
        } else {
            try {
                MetadataContext.this.bindAll(results, PseudoColumn.class, list);
            } finally {
                results.close();
            }
        }

        return list;
    }


    public List<SchemaName> getSchemas() throws SQLException, ReflectiveOperationException {

        final List<SchemaName> list = new ArrayList<SchemaName>();

        final ResultSet results = metaData.getSchemas();
        if (results == null) {
            logger.warning("null from getSchemas");
        }
        if (results != null) {
            try {
                MetadataContext.this.bindAll(results, SchemaName.class, list);
            } finally {
                results.close();
            }
        }

        return list;
    }


    public List<Schema> getSchemas(final String catalog,
                                   final String schemaPattern)
        throws SQLException, ReflectiveOperationException {

        final List<Schema> list = new ArrayList<Schema>();

        final ResultSet results = metaData.getSchemas(
            catalog, schemaPattern);
        if (results == null) {
            logger.warning("null returned from getSchemas");
        }
        if (results != null) {
            try {
                MetadataContext.this.bindAll(results, Schema.class, list);
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
//                schema.getFunctionColumns().addAll(
//                    getFunctionColumns(
//                        schema.getTableCatalog(), schema.getTableSchem(), null,
//                        null));
            }
            if (!suppressed("schema/functions")) {
                schema.getFunctions().addAll(
                    getFunctions(
                        schema.getTableCatalog(), schema.getTableSchem(),
                        null));
            }
            if (!suppressed("schema/procedureColumns")) {
//                schema.getProcedureColumns().addAll(
//                    getProcedureColumns(
//                        schema.getTableCatalog(), schema.getTableSchem(), null,
//                        null));
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
        throws SQLException, ReflectiveOperationException {

        final List<Table> list = new ArrayList<Table>();

        final ResultSet results = metaData.getTables(
            catalog, schemaPattern, tableNamePattern, types);
        if (results == null) {
            logger.warning("null returned from getTables");
        }
        if (results != null) {
            try {
                MetadataContext.this.bindAll(results, Table.class, list);
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
//                table.getColumnPrivileges().addAll(getColumnPrivileges(
//                    table.getTableCat(), table.getTableSchem(),
//                    table.getTableName(), null));
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
//            if (!suppressed("table/tablePrivileges")) {
//                table.getTablePrivileges().addAll(getTablePrivileges(
//                    table.getTableCat(), table.getTableSchem(),
//                    table.getTableName()));
//            }
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
        throws SQLException, ReflectiveOperationException {

        final List<TablePrivilege> list = new ArrayList<TablePrivilege>();

        final ResultSet results = metaData.getTablePrivileges(
            catalog, schemaPattern, tableNamePattern);
        try {
            MetadataContext.this.bindAll(results, TablePrivilege.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<TableType> getTableTypes() throws SQLException, ReflectiveOperationException {

        final List<TableType> list = new ArrayList<TableType>();

        final ResultSet results = metaData.getTableTypes();
        try {
            MetadataContext.this.bindAll(results, TableType.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<TypeInfo> getTypeInfo() throws SQLException, ReflectiveOperationException {

        final List<TypeInfo> list = new ArrayList<TypeInfo>();

        final ResultSet results = metaData.getTypeInfo();
        try {
            MetadataContext.this.bindAll(results, TypeInfo.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<UserDefinedType> getUDTs(
        final String catalog, final String schemaPattern,
        final String typeNamePattern, final int[] types)
        throws SQLException, ReflectiveOperationException {

        final List<UserDefinedType> list = new ArrayList<UserDefinedType>();

        final ResultSet results = metaData.getUDTs(
            catalog, schemaPattern, typeNamePattern, types);
        try {
            MetadataContext.this.bindAll(results, UserDefinedType.class, list);
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
        throws SQLException, ReflectiveOperationException {

        final List<VersionColumn> list = new ArrayList<VersionColumn>();

        final ResultSet results = metaData.getVersionColumns(
            catalog, schema, table);
        try {
            MetadataContext.this.bindAll(results, VersionColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    private final DatabaseMetaData metaData;


    private Set<String> suppressionPaths;


}

