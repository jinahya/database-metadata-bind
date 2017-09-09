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
package com.github.jinahya.database.metadata.bind;

import static com.github.jinahya.database.metadata.bind.Invokes.arguments;
import static java.lang.String.format;
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
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import static com.github.jinahya.database.metadata.bind.Utils.fields;
import static java.util.logging.Level.SEVERE;
import static com.github.jinahya.database.metadata.bind.Utils.field;
import static com.github.jinahya.database.metadata.bind.Utils.labels;
import static com.github.jinahya.database.metadata.bind.Utils.path;
import java.util.Arrays;
import static java.util.Collections.EMPTY_MAP;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

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
    public static final ThreadLocal<String> BIND = new ThreadLocal<String>();

    // -------------------------------------------------------------------------
    public static List<Schema> getSchemas(final MetadataContext context,
                                          final String catalog,
                                          final boolean nonempty)
            throws SQLException {//{//, ReflectiveOperationException {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        final List<Schema> schemas = context.getSchemas(catalog, null);
        if (schemas.isEmpty() && nonempty) {
            logger.fine("adding an empty catalog");
            schemas.add(Schema.newVirtualInstance(context, catalog));
        }
//        if (!context.suppressed(path(Schema.class, "crossReferences"))) {
//            for (final Schema schema : schemas) {
//                schema.getCrossReferences().addAll(
//                        context.getCrossReferences(schema.getTables()));
//            }
//        }
        return schemas;
    }

    public static List<Catalog> getCatalogs(final MetadataContext context,
                                            final boolean nonempty)
            throws SQLException {//{//, ReflectiveOperationException {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        final List<Catalog> catalogs = context.getCatalogs();
        if (catalogs.isEmpty() && nonempty) {
            logger.fine("adding an empty catalog");
            catalogs.add(Catalog.newVirtualInstance(context));
        }
//        if (!context.suppressed(path(Catalog.class, "crossReferences"))) {
//            for (final Catalog catalog : catalogs) {
//                catalog.getCrossReferences().addAll(
//                        context.getCrossReferences(catalog.getTables()));
//            }
//        }
        return catalogs;
    }

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance with given instance of {@link DatabaseMetaData}.
     *
     * @param metadata the {@link DatabaseMetaData} instance to hold.
     */
    public MetadataContext(final DatabaseMetaData metadata) {
        super();
        if (metadata == null) {
            throw new NullPointerException("metadata is null");
        }
        this.metadata = metadata;
    }

    // -------------------------------------------------------------------------
    private <T> T bind(final ResultSet results, final Class<T> type,
                       final T instance)
            throws SQLException {//{//, ReflectiveOperationException {
        final Set<String> labels = labels(results);
        for (final Entry<Field, Bind> bfield : bfields(type).entrySet()) {
            final Field field = bfield.getKey();
            final Bind bind = bfield.getValue();
            final String label = bind.label();
            final String path = path(type, field);
            final String formatted = format(
                    "field=%s, path=%s, bind=%s", field, path, bind);
            if (suppressed(path)) {
                logger.fine(format("skipping; %s", formatted));
                labels.remove(label);
                continue;
            }
            if (!labels.remove(label)) {
                logger.warning(format("unknown; %s", formatted));
                continue;
            }
            if (bind.unused()) {
                logger.fine(format("unused: %s", formatted));
                labels.remove(label);
                continue;
            }
            final Object value = results.getObject(label);
            if (value == null) {
                if (!bind.nillable() && !bind.unused() && !bind.reserved()) {
                    logger.warning(format("null value; %s", formatted));
                }
                if (field.getType().isPrimitive()) {
                    logger.warning(format("null value; %s", formatted));
                }
            }
            try {
                //field(field, instance, value);
                field(field, instance, results, label);
            } catch (final ReflectiveOperationException roe) {
                logger.severe(format("failed to set %s with %s on %s",
                                     field, value, instance));
                continue;
            }
        }
        for (String label : labels) {
            final Object value = results.getObject(label);
            logger.warning(format("unhandled; klass=%s, label=%s, value=%s",
                                  type, label, value));
        }
        for (final Entry<Field, Invoke> ifield : ifields(type).entrySet()) {
            final Field field = ifield.getKey();
            if (!field.getType().equals(List.class)) {
                logger.severe(format("wrong field type: %s", field.getType()));
                continue;
            }
            final Invoke invoke = ifield.getValue();
            final String path = path(type, field);
            final String formatted = format(
                    "field=%s, path=%s, invoke=%s", field, path, invoke);
            if (suppressed(path)) {
                logger.fine(format("skipping; %s", formatted));
                continue;
            }
            final String name = invoke.name();
            final Class<?>[] types = invoke.types();
            final Method method;
            try {
                method = DatabaseMetaData.class.getMethod(name, types);
            } catch (final NoSuchMethodException nsme) {
                logger.log(SEVERE, format("unknown method; {0}", formatted),
                           nsme);
                continue;
            } catch (final NoSuchMethodError nsme) {
                logger.log(SEVERE, format("unknown method; %s", formatted),
                           nsme);
                continue;
            }
            final List<Object> fvalue = new ArrayList<Object>();
            final Class<?> etype = etype(field);
            for (final Literals parameters : invoke.parameters()) {
                final String[] literals = parameters.value();
                final Object[] arguments;
                try {
                    arguments = arguments(type, instance, types, literals);
                } catch (final ReflectiveOperationException roe) {
                    logger.severe(format(
                            "failed to convert arguments from %s on %s",
                            Arrays.toString(literals), type));
                    continue;
                }
                final Object result;
                try {
                    result = method.invoke(metadata, arguments);
                } catch (final Exception e) { // NoSuchMethod
                    logger.log(SEVERE, format(
                               "failed to invoke %s with %s", formatted,
                               Arrays.toString(arguments)), e);
                    continue;
                } catch (final Error e) { // NoSuchMethod/AbstractMethod
                    logger.log(SEVERE, format(
                               "failed to invoke %s with %s",
                               formatted, Arrays.toString(arguments)), e);
                    continue;
                }
                if (!ResultSet.class.isInstance(result)) {
                    logger.severe(format(
                            "wrong result; %s for %s", result, formatted));
                    continue;
                }
//                final List<Object> fvalue = new ArrayList<Object>();
                bind((ResultSet) result, etype, fvalue);
            }
            try {
                field.set(instance, fvalue);
            } catch (final ReflectiveOperationException roe) {
                logger.severe(format("failed to set %s with %s on %s",
                                     field, fvalue, instance));
            }
        } // end-of-invoke-field-loop
        return instance;
    }

    private <T> List<? super T> bind(final ResultSet results,
                                     final Class<T> klass,
                                     final List<? super T> instances)
            throws SQLException {//{//, ReflectiveOperationException {
        while (results.next()) {
            final T instance;
            try {
                instance = klass.newInstance();
            } catch (final ReflectiveOperationException roe) {
                logger.log(SEVERE,
                           format("failed to create new instance of %s", klass),
                           roe);
                continue;
            }
            instances.add(bind(results, klass, instance));
        }
        return instances;
    }

    /**
     * Returns a list of attributes.
     *
     * @param catalog the value for {@code catalog} parameter.
     * @param schemaPattern the value for {@code schemaPattern} parameter.
     * @param typeNamePattern the value for {@code typeNamePattern} parameter.
     * @param attributeNamePattern the value for {@code attributeNamePattern}
     * parameter.
     * @return a list of attributes
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getAttributes(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public List<Attribute> getAttributes(final String catalog,
                                         final String schemaPattern,
                                         final String typeNamePattern,
                                         final String attributeNamePattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<Attribute> list = new ArrayList<Attribute>();
        final ResultSet results = metadata.getAttributes(
                catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        try {
            bind(results, Attribute.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<BestRowIdentifier> getBestRowIdentifier(
            final String catalog, final String schema, final String table,
            final int scope, final boolean nullable)
            throws SQLException {//, ReflectiveOperationException {
        final List<BestRowIdentifier> list = new ArrayList<BestRowIdentifier>();
        final ResultSet results = metadata.getBestRowIdentifier(
                catalog, schema, table, scope, nullable);
        try {
            bind(results, BestRowIdentifier.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds information from {@link DatabaseMetaData#getCatalogs()}.
     *
     * @return a list of catalogs
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getCatalogs()
     */
    public List<Catalog> getCatalogs()
            throws SQLException {//, ReflectiveOperationException {
        final List<Catalog> list = new ArrayList<Catalog>();
        final ResultSet results = metadata.getCatalogs();
        try {
            bind(results, Catalog.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds client info properties.
     *
     * @return a list of client info properties
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public List<ClientInfoProperty> getClientInfoProperties()
            throws SQLException {//, ReflectiveOperationException {
        final List<ClientInfoProperty> list
                = new ArrayList<ClientInfoProperty>();
        final ResultSet results = metadata.getClientInfoProperties();
        try {
            bind(results, ClientInfoProperty.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<Column> getColumns(final String catalog,
                                   final String schemaPattern,
                                   final String tableNamePattern,
                                   final String columnNamePattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<Column> list = new ArrayList<Column>();
        final ResultSet resultSet = metadata.getColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            bind(resultSet, Column.class, list);
        } finally {
            resultSet.close();
        }
        return list;
    }

    public List<ColumnPrivilege> getColumnPrivileges(
            final String catalog, final String schema, final String table,
            final String columnNamePattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<ColumnPrivilege> list = new ArrayList<ColumnPrivilege>();
        final ResultSet results = metadata.getColumnPrivileges(
                catalog, schema, table, columnNamePattern);
        try {
            bind(results, ColumnPrivilege.class, list);
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
            throws SQLException {//, ReflectiveOperationException {
        final List<CrossReference> list = new ArrayList<CrossReference>();
        final ResultSet results = metadata.getCrossReference(
                parentCatalog, parentSchema, parentTable, foreignCatalog,
                foreignSchema, foreignTable);
        try {
            bind(results, CrossReference.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    private List<CrossReference> getCrossReferences(final Table parentTable,
                                                    final Table foreignTable)
            throws SQLException {//, ReflectiveOperationException {
        return getCrossReferences(
                parentTable.getTableCat(), parentTable.getTableSchem(),
                parentTable.getTableName(),
                foreignTable.getTableCat(), foreignTable.getTableSchem(),
                foreignTable.getTableName());
    }

    public List<CrossReference> getCrossReferences(final List<Table> tables)
            throws SQLException {//, ReflectiveOperationException {
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
            throws SQLException {//, ReflectiveOperationException {
        final List<FunctionColumn> list = new ArrayList<FunctionColumn>();
        final ResultSet results = metadata.getFunctionColumns(
                catalog, schemaPattern, functionNamePattern, columnNamePattern);
        try {
            bind(results, FunctionColumn.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<Function> getFunctions(final String catalog,
                                       final String schemaPattern,
                                       final String functionNamePattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<Function> list = new ArrayList<Function>();
        final ResultSet results = metadata.getFunctions(
                catalog, schemaPattern, functionNamePattern);
        try {
            bind(results, Function.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<ExportedKey> getExportedKeys(
            final String catalog, final String schema, final String table)
            throws SQLException {//, ReflectiveOperationException {
        final List<ExportedKey> list = new ArrayList<ExportedKey>();
        final ResultSet results = metadata.getExportedKeys(
                catalog, schema, table);
        try {
            bind(results, ExportedKey.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds information from
     * {@link DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)}.
     *
     * @param catalog catalog
     * @param schema schema
     * @param table table
     * @return a list of bound information
     * @throws SQLException if a database error occurs.
     */
    public List<ImportedKey> getImportedKeys(
            final String catalog, final String schema, final String table)
            throws SQLException {//, ReflectiveOperationException {
        final List<ImportedKey> list = new ArrayList<ImportedKey>();
        final ResultSet results = metadata.getImportedKeys(
                catalog, schema, table);
        try {
            bind(results, ImportedKey.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds index info.
     *
     * @param catalog catalog
     * @param schema schema
     * @param table table
     * @param unique unique
     * @param approximate approximate
     * @return a list of index info
     * @throws SQLException if a database error occurs.
     */
    public List<IndexInfo> getIndexInfo(
            final String catalog, final String schema, final String table,
            final boolean unique, final boolean approximate)
            throws SQLException {//, ReflectiveOperationException {
        final List<IndexInfo> list = new ArrayList<IndexInfo>();
        final ResultSet results = metadata.getIndexInfo(
                catalog, schema, table, unique, approximate);
        try {
            bind(results, IndexInfo.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<PrimaryKey> getPrimaryKeys(
            final String catalog, final String schema, final String table)
            throws SQLException {//, ReflectiveOperationException {
        final List<PrimaryKey> list = new ArrayList<PrimaryKey>();
        final ResultSet results = metadata.getPrimaryKeys(
                catalog, schema, table);
        try {
            bind(results, PrimaryKey.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<ProcedureColumn> getProcedureColumns(
            final String catalog, final String schemaPattern,
            final String procedureNamePattern, final String columnNamePattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<ProcedureColumn> list = new ArrayList<ProcedureColumn>();
        final ResultSet results = metadata.getProcedureColumns(
                catalog, schemaPattern, procedureNamePattern, columnNamePattern);
        try {
            bind(results, ProcedureColumn.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<Procedure> getProcedures(final String catalog,
                                         final String schemaPattern,
                                         final String procedureNamePattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<Procedure> list = new ArrayList<Procedure>();
        final ResultSet results = metadata.getProcedures(
                catalog, schemaPattern, procedureNamePattern);
        try {
            bind(results, Procedure.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds pseudo columns.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of pseudo columns
     * @throws SQLException if a database error occurs.
     */
    @IgnoreJRERequirement // getPseudoColumns since 1.7
    public List<PseudoColumn> getPseudoColumns(final String catalog,
                                               final String schemaPattern,
                                               final String tableNamePattern,
                                               final String columnNamePattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<PseudoColumn> list = new ArrayList<PseudoColumn>();
        final ResultSet results = metadata.getPseudoColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            bind(results, PseudoColumn.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds information from
     * {@link DatabaseMetaData#getSchemas() getSchemas()}.
     *
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     */
    public List<SchemaName> getSchemas()
            throws SQLException {//, ReflectiveOperationException {
        final List<SchemaName> list = new ArrayList<SchemaName>();
        final ResultSet results = metadata.getSchemas();
        try {
            bind(results, SchemaName.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds schemas.
     *
     * @param catalog the value for {@code catalog} parameter.
     * @param schemaPattern the value for {@code schemaPattern} parameter.
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)
     */
    public List<Schema> getSchemas(final String catalog,
                                   final String schemaPattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<Schema> list = new ArrayList<Schema>();
        final ResultSet results = metadata.getSchemas(catalog, schemaPattern);
        if (results == null) {
            // SQLite!!!
            return list;
        }
        try {
            bind(results, Schema.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<Table> getTables(final String catalog,
                                 final String schemaPattern,
                                 final String tableNamePattern,
                                 final String[] types)
            throws SQLException {//, ReflectiveOperationException {
        final List<Table> list = new ArrayList<Table>();
        final ResultSet results = metadata.getTables(
                catalog, schemaPattern, tableNamePattern, types);
        try {
            bind(results, Table.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<TablePrivilege> getTablePrivileges(
            final String catalog, final String schemaPattern,
            final String tableNamePattern)
            throws SQLException {//, ReflectiveOperationException {
        final List<TablePrivilege> list = new ArrayList<TablePrivilege>();
        final ResultSet results = metadata.getTablePrivileges(
                catalog, schemaPattern, tableNamePattern);
        try {
            bind(results, TablePrivilege.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<TableType> getTableTypes()
            throws SQLException {//, ReflectiveOperationException {
        final List<TableType> list = new ArrayList<TableType>();
        final ResultSet results = metadata.getTableTypes();
        try {
            bind(results, TableType.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<TypeInfo> getTypeInfo()
            throws SQLException {//, ReflectiveOperationException {
        final List<TypeInfo> list = new ArrayList<TypeInfo>();
        final ResultSet results = metadata.getTypeInfo();
        try {
            bind(results, TypeInfo.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    public List<UDT> getUDTs(final String catalog, final String schemaPattern,
                             final String typeNamePattern, final int[] types)
            throws SQLException {//, ReflectiveOperationException {
        final List<UDT> list = new ArrayList<UDT>();
        final ResultSet results = metadata.getUDTs(
                catalog, schemaPattern, typeNamePattern, types);
        try {
            bind(results, UDT.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Binds version columns.
     *
     * @param catalog catalog
     * @param schema schema
     * @param table table
     * @return a list of {@link VersionColumn}
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#getVersionColumns(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public List<VersionColumn> getVersionColumns(final String catalog,
                                                 final String schema,
                                                 final String table)
            throws SQLException {//, ReflectiveOperationException {
        final List<VersionColumn> list = new ArrayList<VersionColumn>();
        final ResultSet results = metadata.getVersionColumns(
                catalog, schema, table);
        try {
            bind(results, VersionColumn.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    // ---------------------------------------------------------------- metadata
    private DatabaseMetaData getMetaData() {
        return metadata;
    }

    // ------------------------------------------------------------------- paths
    public Set<String> getPaths() {
        if (paths == null) {
            paths = new HashSet<String>();
        }
        return paths;
    }

    private boolean suppress(final String path) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        return getPaths().add(path);
    }

    /**
     * Add suppression paths and returns this instance.
     *
     * @param path the first suppression path
     * @param otherPaths other suppression paths
     * @return this instance
     */
    public MetadataContext suppress(final String path,
                                    final String... otherPaths) {
        if (suppress(path)) {
            logger.fine(format("duplicate path: %s", path));
        }
        if (otherPaths != null) {
            for (final String otherPath : otherPaths) {
                if (suppress(otherPath)) {
                    logger.fine(format("duplicate path: %s", path));
                }
            }
        }
        return this;
    }

    boolean suppressed(final String path) {
        return getPaths().contains(path);
    }

//    // ----------------------------------------------------------------- lfields
//    @Deprecated
//    private Map<Field, Label> lfields(final Class<?> type)
//            throws ReflectiveOperationException {
//        if (type == null) {
//            throw new NullPointerException("type is null");
//        }
//        Map<Field, Label> value = lfields.get(type);
//        if (value == null) {
//            value = fields(type, Label.class);
//            lfields.put(type, value);
//        }
//        return value;
//    }
    // ----------------------------------------------------------------- bfields
    @SuppressWarnings("unchecked")
    private Map<Field, Bind> bfields(final Class<?> klass) {
//            throws ReflectiveOperationException {
        if (klass == null) {
            throw new NullPointerException("klass is null");
        }
        Map<Field, Bind> value = bfields.get(klass);
        if (value == null) {
            try {
                value = fields(klass, Bind.class);
                for (Field field : value.keySet()) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                }
            } catch (final ReflectiveOperationException roe) {
                logger.severe(format(
                        "failed to get fields from %s annotated with %s",
                        klass, Bind.class));
                value = EMPTY_MAP;
            }
            bfields.put(klass, value);
        }
        return value;
    }

    // ----------------------------------------------------------------- ifields
    @SuppressWarnings("unchecked")
    private Map<Field, Invoke> ifields(final Class<?> klass) {
//            throws ReflectiveOperationException {
        if (klass == null) {
            throw new NullPointerException("klass is null");
        }
        Map<Field, Invoke> value = ifields.get(klass);
        if (value == null) {
            try {
                value = fields(klass, Invoke.class);
                for (Field field : value.keySet()) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                }
            } catch (final ReflectiveOperationException roe) {
                logger.severe(format(
                        "failed to get fields from %s annotated with %s",
                        klass, Invoke.class));
                value = EMPTY_MAP;
            }
            ifields.put(klass, value);
        }
        return value;
    }

    // ------------------------------------------------------------------ etypes
    private Class<?> etype(final Field field) {
        if (field == null) {
            throw new NullPointerException("field is null");
        }
        Class<?> etype = etypes.get(field);
        if (etype == null) {
            final ParameterizedType ptype
                    = (ParameterizedType) field.getGenericType();
            etype = (Class<?>) ptype.getActualTypeArguments()[0];
            etypes.put(field, etype);

        }
        return etype;
    }

    // -------------------------------------------------------------------------
    private final DatabaseMetaData metadata;

    private Set<String> paths;

//    @Deprecated
//    private final Map<Class<?>, Map<Field, Label>> lfields
//            = new HashMap<Class<?>, Map<Field, Label>>();
    private final Map<Class<?>, Map<Field, Bind>> bfields
            = new HashMap<Class<?>, Map<Field, Bind>>();

    private final Map<Class<?>, Map<Field, Invoke>> ifields
            = new HashMap<Class<?>, Map<Field, Invoke>>();

    private final Map<Field, Class<?>> etypes
            = new HashMap<Field, Class<?>>();
}
