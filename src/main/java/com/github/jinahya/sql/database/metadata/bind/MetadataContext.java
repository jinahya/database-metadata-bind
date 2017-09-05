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

import static com.github.jinahya.sql.database.metadata.bind.Utils.annotatedFields;
import static com.github.jinahya.sql.database.metadata.bind.Utils.columnLabels;
import static com.github.jinahya.sql.database.metadata.bind.Utils.setFieldValue;
import static com.github.jinahya.sql.database.metadata.bind.Utils.suppressionPath;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * A context class for retrieving information from an instance of
 * {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MetadataContext {

    private static final Logger logger
            = getLogger(MetadataContext.class.getName());

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance with given {@code DatabaseMetaData}.
     *
     * @param metaData the {@code DatabaseMetaData} instance to hold.
     */
    public MetadataContext(final DatabaseMetaData metaData) {
        super();
        if (metaData == null) {
            throw new NullPointerException("metaData is null");
        }
        this.metaData = metaData;
    }

    // -------------------------------------------------------------------------
    private void setValue(final Field field, final Object bean,
                          final Object value, final Object[] args)
            throws ReflectiveOperationException, SQLException {
        final Class<?> fieldType = field.getType();
        if (fieldType == List.class) {
            if (value == null) {
                return;
            }
            @SuppressWarnings("unchecked")
            List<Object> list = new ArrayList<Object>();
//                    = (List<Object>) Utils.propertyValue(field.getName(), bean);
//            if (list == null) {
//                list = new ArrayList<Object>();
//                Utils.propertyValue(field.getName(), bean, list);
//            }
//            final Class<?> elementType
//                    = (Class<?>) ((ParameterizedType) field.getGenericType())
//                            .getActualTypeArguments()[0];
            final Class<?> elementType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            if (value instanceof ResultSet) {
                bindAll((ResultSet) value, elementType, list);
            } else {
                list.add(elementType
                        .getDeclaredMethod("valueOf", Object[].class, Object.class)
                        .invoke(null, args, value));
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(bean, list);
            return;
        }
        //Utils.propertyValue(field.getName(), bean, value);
        setFieldValue(field, bean, value);
    }

    private <T> T bindSingle(final ResultSet resultSet,
                             final Class<T> bindingType,
                             final T bindingInstance)
            throws SQLException, ReflectiveOperationException {
        if (resultSet != null) { // bind columns to fields
            final Set<String> labels = columnLabels(resultSet);
//            final Map<Field, Labeled> fields
//                    = Utils.annotatedFields(bindingType, Labeled.class);
            final Map<Field, Labeled> fields = labledFields(bindingType);
            for (final Entry<Field, Labeled> entry : fields.entrySet()) {
                final Field field = entry.getKey();
                final String label = entry.getValue().value();
                final String path = suppressionPath(bindingType, field);
                final String formatted = String.format(
                        "field=%s, label=%s, suppression=%s", field, label,
                        path);
                if (suppressed(path)) {
                    logger.log(Level.FINE, "suppressed; {0}", formatted);
                    continue;
                }
                if (!labels.remove(label)) {
                    logger.log(Level.WARNING, "unknown label: {0}", formatted);
                    continue;
                }
                final Object value = resultSet.getObject(label);
                //propertyValue(field.getName(), bindingInstance, value);
                setFieldValue(field, bindingInstance, value);
                //if (!field.isAccessible()) {
                //    field.setAccessible(true);
                //}
                //field.set(bindingInstance, value);
            }
            if (!labels.isEmpty()) {
                for (final String label : labels) {
                    final Object object = resultSet.getObject(label);
                    logger.log(Level.FINE, "unknown column; {0}({1}) {2}",
                               new Object[]{label, object, bindingType});
                }
            }
        }
        // invoke
//        final Map<Field, Invokable> fields
//                = Utils.annotatedFields(bindingType, Invokable.class);
        final Map<Field, Invokable> fields = invocableFields(bindingType);
        for (final Entry<Field, Invokable> entry : fields.entrySet()) {
            final Field field = entry.getKey();
            final Invokable invocation = entry.getValue();
            final String path = suppressionPath(bindingType, field);
            final String formatted = String.format(
                    "field=%s, invocation=%s, suppression=%s",
                    field, invocation, path);
            if (suppressed(path)) {
                logger.log(Level.FINE, "suppressed; {0}", formatted);
                continue;
            }
            final String name = invocation.name();
            final Class<?>[] types = invocation.types();
            final Method method;
            try {
                method = DatabaseMetaData.class.getMethod(name, types);
            } catch (final NoSuchMethodException nsme) {
                logger.log(Level.SEVERE, "unknown invocation; {0}", formatted);
                continue;
            }
            for (final Literals invocationArgs : invocation.args()) {
                final String[] literals = invocationArgs.value();
                final Object[] values = Invocations.invocationValues(
                        bindingType, bindingInstance, types, literals);
                final Object value;
                try {
                    value = method.invoke(metaData, values);
                    setValue(field, bindingInstance, value, values);
                } catch (final Exception e) {
                    logger.log(Level.SEVERE, "failed to invoke " + formatted
                                             + " / values: " + values, e);
//                    throw new RuntimeException(e);
                } catch (final Error e) {
                    logger.log(Level.SEVERE, "failed to invoke " + formatted
                                             + " / values: " + values, e);
                    throw e;
                }
            }
        }
        if (TableDomain.class.isAssignableFrom(bindingType)) {
            final TableDomain casted = (TableDomain) bindingInstance;
            final List<Table> tables = casted.getTables();
            final List<CrossReference> references = getCrossReferences(tables);
            casted.getCrossReferences().addAll(references);
        }
        return bindingInstance;
    }

    @Deprecated
    private <T> T bindSingle(final ResultSet resultSet,
                             final Class<T> bindingType)
            throws SQLException, ReflectiveOperationException {
        return bindSingle(resultSet, bindingType, bindingType.newInstance());
    }

    private <T> List<? super T> bindAll(final ResultSet resultSet,
                                        final Class<T> bindingType,
                                        final List<? super T> bindingInstances)
            throws SQLException, ReflectiveOperationException {
        while (resultSet.next()) {
            bindingInstances.add(bindSingle(
                    resultSet, bindingType, bindingType.newInstance()));
        }
        return bindingInstances;
    }

//    private <T> List<? super T> bindAll(final ResultSet resultSet,
//                                        final Class<T> bindingType)
//            throws SQLException, ReflectiveOperationException {
//        return bindAll(resultSet, bindingType, new ArrayList<T>());
//    }
    /**
     * Binds all information.
     *
     * @return a Metadata
     * @throws SQLException if a database occurs.
     * @throws ReflectiveOperationException if a reflection error occurs
     * @deprecated
     */
    @Deprecated
    public Metadata getMetadata()
            throws SQLException, ReflectiveOperationException {
        final Metadata metadata = bindSingle(null, Metadata.class);
        final List<Catalog> catalogs = metadata.getCatalogs();
        if (catalogs.isEmpty()) {
            final Catalog catalog = new Catalog();
            catalog.virtual = Boolean.TRUE;
            catalog.setTableCat("");
            logger.log(Level.FINE, "adding an empty category, {0}", catalog);
            catalogs.add(catalog);
            bindSingle(null, Catalog.class, catalog);
        }
        for (final Catalog catalog : catalogs) {
            final List<Schema> schemas = catalog.getSchemas();
            if (schemas.isEmpty()) {
                final Schema schema = new Schema();
                schema.virtual = Boolean.TRUE;
                schema.setTableCatalog(catalog.getTableCat());
                schema.setTableSchem("");
                logger.log(Level.FINE, "adding an empty schema, {0}", schema);
                schemas.add(schema);
                bindSingle(null, Schema.class, schema);
            }
        }
        if (!suppressed("metadata/supportsConvert")) {
            final List<SDTSDTBoolean> supportsConvert
                    = new ArrayList<SDTSDTBoolean>();
            metadata.getSupportsConvert().addAll(supportsConvert);
            supportsConvert.add(
                    new SDTSDTBoolean()
                            .fromType(null)
                            .toType(null)
                            .value(metaData.supportsConvert()));
            final Set<Integer> sqlTypes = Utils.sqlTypes();
            for (final int fromType : sqlTypes) {
                for (final int toType : sqlTypes) {
                    supportsConvert.add(
                            new SDTSDTBoolean()
                                    .fromType(fromType)
                                    .toType(toType)
                                    .value(metaData.supportsConvert(fromType, toType)));
                }
            }
        }
//        for (final String methodName : getMethodNames()) {
//            logger.log(Level.INFO, "method not invoked: {0}",
//                       new Object[]{methodName});
//        }
        return metadata;
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
            bindAll(results, Attribute.class, list);
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
            bindAll(results, BestRowIdentifier.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Returns catalogs.
     *
     * @return a list of catalogs
     * @throws SQLException if a database error occurs.
     * @throws ReflectiveOperationException if a reflection error occurs.
     * @see DatabaseMetaData#getCatalogs()
     */
    public List<Catalog> getCatalogs()
            throws SQLException, ReflectiveOperationException {
        final List<Catalog> list = new ArrayList<Catalog>();
        final ResultSet results = metaData.getCatalogs();
        try {
            bindAll(results, Catalog.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds information from {@link DatabaseMetaData#getClientInfoProperties()}
     *
     * @return bound information
     * @throws SQLException if a database error occurs.
     * @throws ReflectiveOperationException
     */
    public List<ClientInfoProperty> getClientInfoProperties()
            throws SQLException, ReflectiveOperationException {
        final List<ClientInfoProperty> list
                = new ArrayList<ClientInfoProperty>();
        final ResultSet results = metaData.getClientInfoProperties();
        try {
            bindAll(results, ClientInfoProperty.class, list);
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
            bindAll(resultSet, Column.class, list);
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
            bindAll(results, ColumnPrivilege.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<CrossReference> getCrossReferences(
            final String parentCatalog, final String parentSchema,
            final String parentTable,
            final String foreignCatalog, final String foreignSchema,
            final String foreignTable)
            throws SQLException, ReflectiveOperationException {
        final List<CrossReference> list = new ArrayList<CrossReference>();
        final ResultSet results = metaData.getCrossReference(
                parentCatalog, parentSchema, parentTable, foreignCatalog,
                foreignSchema, foreignTable);
        try {
            bindAll(results, CrossReference.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<CrossReference> getCrossReferences(
            final Table parentTable,
            final Table foreignTable)
            throws SQLException, ReflectiveOperationException {
        return getCrossReferences(
                parentTable.getTableCat(), parentTable.getTableSchem(),
                parentTable.getTableName(),
                foreignTable.getTableCat(), foreignTable.getTableSchem(),
                foreignTable.getTableName());
    }

    List<CrossReference> getCrossReferences(final List<Table> tables)
            throws SQLException, ReflectiveOperationException {
        final List<CrossReference> list = new ArrayList<CrossReference>();
        for (final Table parentTable : tables) {
            for (final Table foreignTable : tables) {
                list.addAll(getCrossReferences(parentTable, foreignTable));
            }
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
        try {
            bindAll(results, FunctionColumn.class, list);
        } finally {
            results.close();
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
        try {
            bindAll(results, Function.class, list);
        } finally {
            results.close();
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
            bindAll(results, ExportedKey.class, list);
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
            bindAll(results, ImportedKey.class, list);
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
            bindAll(results, IndexInfo.class, list);
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
            bindAll(results, PrimaryKey.class, list);
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
            bindAll(results, ProcedureColumn.class, list);
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
            bindAll(results, Procedure.class, list);
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
        try {
            bindAll(results, PseudoColumn.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<SchemaName> getSchemas()
            throws SQLException, ReflectiveOperationException {
        final List<SchemaName> list = new ArrayList<SchemaName>();
        final ResultSet results = metaData.getSchemas();
        try {
            bindAll(results, SchemaName.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<Schema> getSchemas(final String catalog,
                                   final String schemaPattern)
            throws SQLException, ReflectiveOperationException {
        final List<Schema> list = new ArrayList<Schema>();
        final ResultSet results = metaData.getSchemas(catalog, schemaPattern);
        try {
            bindAll(results, Schema.class, list);
        } finally {
            results.close();
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
        try {
            bindAll(results, Table.class, list);
        } finally {
            results.close();
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
            bindAll(results, TablePrivilege.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<TableType> getTableTypes()
            throws SQLException, ReflectiveOperationException {
        final List<TableType> list = new ArrayList<TableType>();
        final ResultSet results = metaData.getTableTypes();
        try {
            bindAll(results, TableType.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<TypeInfo> getTypeInfo()
            throws SQLException, ReflectiveOperationException {
        final List<TypeInfo> list = new ArrayList<TypeInfo>();
        final ResultSet results = metaData.getTypeInfo();
        try {
            bindAll(results, TypeInfo.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<UDT> getUDTs(final String catalog, final String schemaPattern,
                             final String typeNamePattern, final int[] types)
            throws SQLException, ReflectiveOperationException {
        final List<UDT> list = new ArrayList<UDT>();
        final ResultSet results = metaData.getUDTs(
                catalog, schemaPattern, typeNamePattern, types);
        try {
            bindAll(results, UDT.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds information from
     * {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)}.
     *
     * @param catalog catalog
     * @param schema schema
     * @param table table
     * @return a list of {@link VersionColumn}
     * @throws SQLException if a database access error occurs.
     * @throws ReflectiveOperationException if a reflection error occurs
     * @see DatabaseMetaData#getVersionColumns(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public List<VersionColumn> getVersionColumns(final String catalog,
                                                 final String schema,
                                                 final String table)
            throws SQLException, ReflectiveOperationException {
        final List<VersionColumn> list = new ArrayList<VersionColumn>();
        final ResultSet results = metaData.getVersionColumns(
                catalog, schema, table);
        try {
            bindAll(results, VersionColumn.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    // ---------------------------------------------------------------- metaData
    private DatabaseMetaData getMetaData() {
        return metaData;
    }

    // --------------------------------------------------------- suppressedPaths
    public boolean suppress(final String path) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        return suppressedPaths.add(path);
    }

    /**
     * Add suppression paths.
     *
     * @param path the first suppression path
     * @param otherPaths other suppression paths
     * @return this
     */
    public MetadataContext suppress(final String path,
                                    final String... otherPaths) {
        suppress(path);
        if (otherPaths != null) {
            for (final String otherPath : otherPaths) {
                suppress(otherPath);
            }
        }
        return this;
    }

    private boolean suppressed(final String path) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        return suppressedPaths.contains(path);
    }

    // ------------------------------------------------------------ labledFields
    public Map<Field, Labeled> labledFields(final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        Map<Field, Labeled> value = labeldedFields.get(type);
        if (value == null) {
            value = annotatedFields(type, Labeled.class);
            labeldedFields.put(type, value);
        }
        return value;
    }

    // --------------------------------------------------------- invocableFields
    public Map<Field, Invokable> invocableFields(final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        Map<Field, Invokable> value = invocableFields.get(type);
        if (value == null) {
            value = annotatedFields(type, Invokable.class);
            invocableFields.put(type, value);
        }
        return value;
    }

    // -------------------------------------------------------------------------
    private final DatabaseMetaData metaData;

    private final Set<String> suppressedPaths = new HashSet<String>();

    private final Map<Class<?>, Map<Field, Labeled>> labeldedFields
            = new HashMap<Class<?>, Map<Field, Labeled>>();

    private final Map<Class<?>, Map<Field, Invokable>> invocableFields
            = new HashMap<Class<?>, Map<Field, Invokable>>();
}
