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
import java.lang.reflect.ParameterizedType;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.reflect.FieldUtils;


/**
 * A context class for retrieving information from an instance of
 * {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MetadataContext {


    private static final Logger logger
        = Logger.getLogger(Metadata.class.getName());


    public static final String DMDB_SUPPRESS_UNKNOWN_COLUMNS
        = "dmdb.suppressUnknownColumns";


    public static final String DMDB_SUPPRESS_UNKNOWN_METHODS
        = "dmdb.suppressUnknownMethods";


    public static final String DMDB_EMPTY_CATALOG_IF_NONE
        = "dmdb.emptyCatalogIfNone";


    public static final String DMDB_EMPTY_SCHEMA_IF_NONE
        = "dmdb.emptySchemaIfNone";


    private static String suppression(final Class<?> klass, final Field field) {

        return decapitalize(klass.getSimpleName()) + "/" + field.getName();
    }


    /**
     * Creates a new instance with given {@code DatabaseMetaData}.
     *
     * @param context the {@code DatabaseMetaData} instance to hold.
     */
    public MetadataContext(final DatabaseMetaData context) {

        super();

        this.context = Objects.requireNonNull(context, "null context");
    }


    private boolean suppression(final String suppression) {

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (suppressions == null) {
            suppressions = new TreeSet<String>();
        }

        return suppressions.add(suppression);
    }


    /**
     * Add suppression paths.
     *
     * @param suppression the first suppression
     * @param otherSuppressions other suppressions
     *
     * @return this
     */
    public MetadataContext suppressions(
        final String suppression, final String... otherSuppressions) {

        suppression(suppression);

        if (otherSuppressions != null) {
            for (final String otherSuppression : otherSuppressions) {
                suppression(otherSuppression);
            }
        }

        return this;
    }


    private boolean suppressed(final String suppression) {

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (suppressions == null) {
            return false;
        }

        return suppressions.contains(suppression);
    }


    private void setValue(final Field field, final Object bean,
                          final Object value, final Object[] args)
        throws ReflectiveOperationException, SQLException {

        final Class<?> fieldType = field.getType();
        if (fieldType == List.class) {
            if (value == null) {
                return;
            }
            @SuppressWarnings("unchecked")
            List<Object> list
                = (List<Object>) Values.get(field.getName(), bean);
            if (list == null) {
                list = new ArrayList<Object>();
                Values.set(field.getName(), bean, list);
            }
            final Class<?> elementType
                = (Class<?>) ((ParameterizedType) field.getGenericType())
                .getActualTypeArguments()[0];
            if (value instanceof ResultSet) {
                bindAll((ResultSet) value, elementType, list);
                Values.setParent(elementType, list, bean);
                return;
            }
            list.add(elementType
                .getDeclaredMethod("valueOf", Object[].class, Object.class)
                .invoke(null, args, value));
            Values.setParent(elementType, list, value);
            return;
        }

        Values.set(field.getName(), bean, value);
    }


    private <T> T bindSingle(final ResultSet results, final Class<T> klass,
                             final T instance)
        throws SQLException, ReflectiveOperationException {

        if (results != null) {
            final Set<String> labels = ResultSets.getColumnLabels(results);
            final Field[] fields
                = FieldUtils.getFieldsWithAnnotation(klass, Label.class);
            for (final Field field : fields) {
                final String label = field.getAnnotation(Label.class).value();
                final String suppression = suppression(klass, field);
                final String info = String.format(
                    "field=%s, label=%s, suppression=%s", field, label,
                    suppression);
                if (suppressed(suppression)) {
                    logger.log(Level.FINE, "suppressed; {0}", info);
                    continue;
                }
                if (!labels.remove(label)) {
                    final String message = "unknown column; " + info;
                    if (!suppressUnknownColumns()) {
                        throw new RuntimeException(message);
                    }
                    logger.warning(message);
                    continue;
                }
                final Object value;
                try {
                    value = results.getObject(label);
                } catch (final Exception e) {
                    final String message = "failed to get value; " + info;
                    logger.severe(message);
                    if (e instanceof SQLException) {
                        throw (SQLException) e;
                    }
                    throw new RuntimeException(e);
                }
                Values.set(field.getName(), instance, value);
            }
            if (!labels.isEmpty()) {
                for (final String label : labels) {
                    final Object resultValue = results.getObject(label);
                    logger.log(Level.WARNING, "unknown result; {0}({1}) {2}",
                               new Object[]{label, resultValue, klass});
//                    Set<String> sets = unknownResults.get(klass);
//                    if (sets == null) {
//                        sets = new HashSet<String>();
//                        unknownResults.put(klass, sets);
//                    }
//                    if (!sets.add(label)) {
//                        continue;
//                    }
//                    logger.log(Level.WARNING, "unknown result; {0} {1}",
//                               new Object[]{label, klass});
                }
            }
        }

        final List<Field> fields = FieldUtils.getFieldsListWithAnnotation(
            klass, Invocation.class);
        for (final Field field : fields) {
            final Invocation invocation = field.getAnnotation(Invocation.class);
            final String suppression = suppression(klass, field);
            final String info = String.format(
                "field=%s, invocation=%s, suppression=%s",
                field, invocation, suppression);
            if (suppressed(suppression)) {
                logger.log(Level.FINE, "suppressed; {0}", new Object[]{info});
                continue;
            }
            final String name = invocation.name();
//            getMethodNames().remove(name);
            final Class<?>[] types = invocation.types();
            final Method method;
            try {
                method = DatabaseMetaData.class.getMethod(name, types);
            } catch (final NoSuchMethodException nsme) {
                final String message = "unknown methods; " + info;
                if (!suppressUnknownMethods()) {
                    throw new RuntimeException(message);
                }
                logger.warning(message);
                continue;
            }
            for (final InvocationArgs invocationArgs : invocation.argsarr()) {
                final String[] names = invocationArgs.value();
                final Object[] args = Invocations.args(
                    klass, instance, types, names);
                final Object value;
                try {
                    value = method.invoke(context, args);
                } catch (final Exception e) {
                    logger.log(Level.SEVERE, "failed to invoke" + info, e);
                    throw new RuntimeException(e);
                } catch (final AbstractMethodError ame) {
                    logger.log(Level.SEVERE, "failed by abstract" + info, ame);
                    throw ame;
                }
                setValue(field, instance, value, args);
            }
        }

        if (TableDomain.class.isAssignableFrom(klass)) {
//            getMethodNames().remove("getCrossReference");
            final List<Table> tables = ((TableDomain) instance).getTables();
            final List<CrossReference> crossReferences
                = getCrossReferences(tables);
            ((TableDomain) instance).setCrossReferences(crossReferences);
        }

        return instance;
    }


    private <T> T bindSingle(final ResultSet results, final Class<T> klass)
        throws SQLException, ReflectiveOperationException {

        return bindSingle(results, klass, klass.newInstance());
    }


    private <T> List<? super T> bindAll(final ResultSet results,
                                        final Class<T> klass,
                                        final List<? super T> instances)
        throws SQLException, ReflectiveOperationException {

        while (results.next()) {
            instances.add(bindSingle(results, klass, klass.newInstance()));
        }

        return instances;
    }


    private <T> List<? super T> bindAll(final ResultSet sults,
                                        final Class<T> klass)
        throws SQLException, ReflectiveOperationException {

        return bindAll(sults, klass, new ArrayList<T>());
    }


    /**
     * Binds all information.
     *
     * @return a Metadata
     *
     * @throws SQLException if a database occurs.
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    public Metadata getMetadata()
        throws SQLException, ReflectiveOperationException {

        if (!getProperties().containsKey(DMDB_EMPTY_CATALOG_IF_NONE)) {
            emptyCatalogIfNone(true);
        }

        if (!getProperties().containsKey(DMDB_EMPTY_SCHEMA_IF_NONE)) {
            emptySchemaIfNone(true);
        }

        final Metadata metadata = bindSingle(null, Metadata.class);

        final List<Catalog> catalogs = metadata.getCatalogs();
        if (catalogs.isEmpty() && emptyCatalogIfNone()) {
            final Catalog catalog = new Catalog();
            catalog.setTableCat("");
            catalog.setParent(metadata);
            logger.log(Level.FINE, "adding an empty catalog: {0}",
                       new Object[]{catalog});
            catalogs.add(catalog);
            bindSingle(null, Catalog.class, catalog);
        }
        for (final Catalog catalog : catalogs) {
            final List<Schema> schemas = catalog.getSchemas();
            if (schemas.isEmpty() && emptySchemaIfNone()) {
                final Schema schema = new Schema();
                schema.setTableCatalog(catalog.getTableCat());
                schema.setTableSchem("");
                schema.setParent(catalog);
                logger.log(Level.FINE, "adding an empty schema: {0}",
                           new Object[]{schema});
                schemas.add(schema);
                bindSingle(null, Schema.class, schema);
            }
        }

        if (!suppressed("metadata/supportsConvert")) {
//            getMethodNames().remove("supportsConvert");
            final List<SDTSDTBoolean> supportsConvert
                = new ArrayList<SDTSDTBoolean>();
            metadata.setSupportsConvert(supportsConvert);
            supportsConvert.add(
                new SDTSDTBoolean()
                .fromType(null)
                .toType(null)
                .value(context.supportsConvert()));
            final Set<Integer> sqlTypes = Reflections.sqlTypes();
            for (final int fromType : sqlTypes) {
                for (final int toType : sqlTypes) {
                    supportsConvert.add(
                        new SDTSDTBoolean()
                        .fromType(fromType)
                        .toType(toType)
                        .value(context.supportsConvert(fromType, toType)));
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

        final ResultSet results = context.getAttributes(
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

        final ResultSet results = context.getBestRowIdentifier(
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
     *
     * @throws SQLException if a database error occurs.
     * @throws ReflectiveOperationException if a reflection error occurs.
     *
     * @see DatabaseMetaData#getCatalogs()
     */
    public List<Catalog> getCatalogs()
        throws SQLException, ReflectiveOperationException {

        final List<Catalog> list = new ArrayList<Catalog>();

        final ResultSet results = context.getCatalogs();
        try {
            bindAll(results, Catalog.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ClientInfoProperty> getClientInfoProperties()
        throws SQLException, ReflectiveOperationException {

        final List<ClientInfoProperty> list
            = new ArrayList<ClientInfoProperty>();

        final ResultSet results = context.getClientInfoProperties();

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

        final ResultSet resultSet = context.getColumns(
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

        final ResultSet results = context.getColumnPrivileges(
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

        final ResultSet results = context.getCrossReference(
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

        final ResultSet results = context.getFunctionColumns(
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

        final ResultSet results = context.getFunctions(
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

        final ResultSet results = context.getExportedKeys(
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

        final ResultSet results = context.getImportedKeys(
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

        final ResultSet results = context.getIndexInfo(
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

        final ResultSet results = context.getPrimaryKeys(
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

        final ResultSet results = context.getProcedureColumns(
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

        final ResultSet results = context.getProcedures(
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

        final ResultSet results = context.getPseudoColumns(
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

        final ResultSet results = context.getSchemas();
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

        final ResultSet results = context.getSchemas(catalog, schemaPattern);
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

        final ResultSet results = context.getTables(
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

        final ResultSet results = context.getTablePrivileges(
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

        final ResultSet results = context.getTableTypes();
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

        final ResultSet results = context.getTypeInfo();
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

        final ResultSet results = context.getUDTs(
            catalog, schemaPattern, typeNamePattern, types);
        try {
            bindAll(results, UDT.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<VersionColumn> getVersionColumns(final String catalog,
                                                 final String schema,
                                                 final String table)
        throws SQLException, ReflectiveOperationException {

        final List<VersionColumn> list = new ArrayList<VersionColumn>();

        final ResultSet results = context.getVersionColumns(
            catalog, schema, table);
        try {
            bindAll(results, VersionColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    protected Map<String, Object> getProperties() {

        if (properties == null) {
            properties = new HashMap<String, Object>();
        }

        return properties;
    }


    protected Object getProperty(final String name) {

        return getProperties().get(name);
    }


    protected boolean getPropertyAsBoolean(final String name) {

        final Object value = getProperty(name);

        if (!(value instanceof Boolean)) {
            return false;
        }

        return (Boolean) value;
    }


    protected Object setProperty(final String name, final Object value) {

        if (value == null) {
            return getProperties().remove(name);
        }

        return getProperties().put(name, value);
    }


    public boolean suppressUnknownColumns() {

        return getPropertyAsBoolean(DMDB_SUPPRESS_UNKNOWN_COLUMNS);
    }


    public MetadataContext suppressUnknownColumns(
        final boolean suppressUnknownColumns) {

        setProperty(DMDB_SUPPRESS_UNKNOWN_COLUMNS, suppressUnknownColumns);

        return this;
    }


    public boolean suppressUnknownMethods() {

        return getPropertyAsBoolean(DMDB_SUPPRESS_UNKNOWN_METHODS);
    }


    public MetadataContext suppressUnknownMethods(
        final boolean suppressUnknownMethods) {

        setProperty(DMDB_SUPPRESS_UNKNOWN_METHODS, suppressUnknownMethods);

        return this;
    }


    public boolean emptyCatalogIfNone() {

        return getPropertyAsBoolean(DMDB_EMPTY_CATALOG_IF_NONE);
    }


    public MetadataContext emptyCatalogIfNone(
        final boolean emptyCatalogIfNone) {

        setProperty(DMDB_EMPTY_CATALOG_IF_NONE, emptyCatalogIfNone);

        return this;
    }


    public boolean emptySchemaIfNone() {

        return getPropertyAsBoolean(DMDB_EMPTY_SCHEMA_IF_NONE);
    }


    public MetadataContext emptySchemaIfNone(final boolean emptySchemaIfNone) {

        setProperty(DMDB_EMPTY_SCHEMA_IF_NONE, emptySchemaIfNone);

        return this;
    }


//    private Set<String> getMethodNames() {
//
//        if (methodNames == null) {
//            methodNames = new HashSet<String>();
//            for (final Method method : DatabaseMetaData.class.getMethods()) {
//                if (method.getDeclaringClass() != DatabaseMetaData.class) {
//                    continue;
//                }
//                final int modifier = method.getModifiers();
//                if (Modifier.isStatic(modifier)) {
//                    continue;
//                }
//                if (!Modifier.isPublic(modifier)) {
//                    continue;
//                }
//                methodNames.add(method.getName());
//            }
//        }
//
//        return methodNames;
//    }
    private final DatabaseMetaData context;


    private Map<String, Object> properties;


    private Set<String> suppressions;

//    private transient Set<String> methodNames;
//    private final Map<Class<?>, Set<String>> unknownResults
//        = new HashMap<Class<?>, Set<String>>();
}

