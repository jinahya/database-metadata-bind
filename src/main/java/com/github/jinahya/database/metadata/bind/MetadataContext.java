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
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
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
    public static List<Schema> getSchemas(final MetadataContext context,
                                          final String catalog,
                                          final boolean nonempty)
            throws SQLException {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        final List<Schema> schemas = context.getSchemas(catalog, null);
        if (schemas.isEmpty() && nonempty) {
            logger.fine("adding an empty schema");
            final Schema schema = new Schema();
            schema.virtual = true;
            schema.setTableCatalog(catalog);
            schema.setTableSchem("");
            if (!context.suppressed("schema/functions")) {
                schema.getFunctions().addAll(context.getFunctions(
                        schema.getTableCatalog(), schema.getTableSchem(),
                        null));
            }
            if (!context.suppressed("schema/procedures")) {
                schema.getProcedures().addAll(context.getProcedures(
                        schema.getTableCatalog(), schema.getTableSchem(),
                        null));
            }
            if (!context.suppressed("schema/tables")) {
                schema.getTables().addAll(context.getTables(
                        schema.getTableCatalog(), schema.getTableSchem(), null,
                        null));
            }
            if (!context.suppressed("schema/UDTs")) {
                schema.getUDTs().addAll(context.getUDTs(
                        schema.getTableCatalog(), schema.getTableSchem(), null,
                        null));
            }
            schemas.add(schema);
        }
        return schemas;
    }

    public static List<Catalog> getCatalogs(final MetadataContext context,
                                            final boolean nonempty)
            throws SQLException {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        final List<Catalog> catalogs = context.getCatalogs();
        if (catalogs.isEmpty() && nonempty) {
            logger.fine("adding an empty catalog");
            final Catalog catalog = new Catalog();
            catalog.virtual = true;
            catalog.setTableCat("");
            catalogs.add(catalog);
            if (!context.suppressed("catalog/schemas")) {
                catalog.getSchemas().addAll(
                        context.getSchemas(catalog.getTableCat(), ""));
            }
        }
        if (!context.suppressed("catalog/schemas")) {
            boolean allempty = true;
            for (final Catalog catalog : catalogs) {
                if (!catalog.getSchemas().isEmpty()) {
                    allempty = false;
                    break;
                }
            }
            if (allempty) {
                logger.warning("schemas are all empty");
                for (final Catalog catalog : catalogs) {
                    catalog.getSchemas().addAll(
                            getSchemas(context, catalog.getTableCat(), true));
                }
            }
        }
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
            throws SQLException {
        final Set<String> labels = labels(results);
        for (final Entry<Field, Bind> bfield : bfields(type).entrySet()) {
            final Field field = bfield.getKey();
            final Bind bind = bfield.getValue();
            final String label = bind.label();
            final String path = path(type, field);
            final String formatted = format(
                    "field=%s, path=%s, bind=%s", field, path, bind);
            if (!labels.remove(label)) {
                logger.warning(format("unknown; %s", formatted));
                continue;
            }
            if (suppressed(path)) {
                logger.fine(format("suppressed; %s", formatted));
                continue;
            }
            if (bind.unused()) {
                logger.fine(format("unused: %s", formatted));
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
                field(field, instance, results, label);
            } catch (final ReflectiveOperationException roe) {
                logger.severe(format("failed to set %s with %s on %s",
                                     field, value, instance));
                continue;
            }
        }
        if (false) {
            for (String label : labels) {
                final Object value = results.getObject(label);
                logger.fine(format("unhandled; klass=%s, label=%s, value=%s",
                                   type, label, value));
            }
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
            final Class<?> ptype = ptype(field);
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
                bind((ResultSet) result, ptype, fvalue);
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
            throws SQLException {
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
     * Invokes
     * {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter.
     * @param schemaPattern the value for {@code schemaPattern} parameter.
     * @param typeNamePattern the value for {@code typeNamePattern} parameter.
     * @param attributeNamePattern the value for {@code attributeNamePattern}
     * parameter.
     * @return a list of {@link Attribute}
     * @throws SQLException if a database error occurs.
     */
    public List<Attribute> getAttributes(final String catalog,
                                         final String schemaPattern,
                                         final String typeNamePattern,
                                         final String attributeNamePattern)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schema the value for {@code schema} parameter
     * @param table the value for {@code table} parameter
     * @param scope the value for {@code scope} parameter
     * @param nullable the value for {@code nullable} parameter
     * @return a list of {@link BestRowIdentifier}
     * @throws SQLException if a database error occurs.
     */
    public List<BestRowIdentifier> getBestRowIdentifier(
            final String catalog, final String schema, final String table,
            final int scope, final boolean nullable)
            throws SQLException {
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
     * Invokes {@link DatabaseMetaData#getCatalogs()} and returns bound
     * information.
     *
     * @return a list of {@link Catalog}
     * @throws SQLException if a database error occurs.
     */
    public List<Catalog> getCatalogs() throws SQLException {
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
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} and returns
     * bound information.
     *
     * @return a list of {@link ClientInfoProperty}
     * @throws SQLException if a database error occurs.
     */
    public List<ClientInfoProperty> getClientInfoProperties()
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schemaPattern the value for {@code schemaPattern} parameter
     * @param tableNamePattern the value for {@code tableNameSchema} parameter
     * @param columnNamePattern the value for {@code columnNamePattern}
     * parameter
     * @return a list of {@link Column}
     * @throws SQLException if a database error occurs.
     */
    public List<Column> getColumns(final String catalog,
                                   final String schemaPattern,
                                   final String tableNamePattern,
                                   final String columnNamePattern)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schema the value for {@code schema} parameter
     * @param table the value for {@code table} parameter
     * @param columnNamePattern the value for {@code columnNamePattern}
     * parameter
     * @return a list of {@link ColumnPrivilege}
     * @throws SQLException if a database error occurs.
     */
    public List<ColumnPrivilege> getColumnPrivileges(
            final String catalog, final String schema, final String table,
            final String columnNamePattern)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param parentCatalog the value for {@code parentCatalog} parameter
     * @param parentSchema the value for {@code parentSchema} parameter
     * @param parentTable the value for {@code parentTable} parameter
     * @param foreignCatalog the value for {@code foreignCatalog} parameter
     * @param foreignSchema the value for {@code foreignSchema} parameter
     * @param foreignTable the value for {@code foreignTable} parameter
     * @return a list of {@link CrossReference}s.
     * @throws SQLException if a database error occurs.
     */
    public List<CrossReference> getCrossReferences(
            final String parentCatalog, final String parentSchema,
            final String parentTable,
            final String foreignCatalog, final String foreignSchema,
            final String foreignTable)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schemaPattern the value for {@code schemaPattern} parameter
     * @param functionNamePattern the value for {@code functionNamePattern}
     * parameter
     * @param columnNamePattern the value for {@code columnNamePattern}
     * parameter
     * @return a list of {@link FunctionColumn}s
     * @throws SQLException if a database error occurs.
     */
    public List<FunctionColumn> getFunctionColumns(
            final String catalog, final String schemaPattern,
            final String functionNamePattern, final String columnNamePattern)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schemaPattern the value for {@code schemaPattern} parameter
     * @param functionNamePattern the value for {@code functionNamePattern}
     * parameter
     * @return a list of {@link Function}s
     * @throws SQLException if a database error occurs.
     */
    public List<Function> getFunctions(final String catalog,
                                       final String schemaPattern,
                                       final String functionNamePattern)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schema the value for {@code schema} parameter
     * @param table the value for {@code table} parameter
     * @return a list of {@link ExportedKey}s
     * @throws SQLException if a database error occurs.
     */
    public List<ExportedKey> getExportedKeys(
            final String catalog, final String schema, final String table)
            throws SQLException {
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
     * Invokes
     * {@link DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog catalog the value for {@code catalog} parameter
     * @param schema schema the value for {@code schema} parameter
     * @param table table the value for {@code table} parameter
     * @return a list of {@link ImportedKey}.
     * @throws SQLException if a database error occurs.
     */
    public List<ImportedKey> getImportedKeys(
            final String catalog, final String schema, final String table)
            throws SQLException {
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
     * Invokes
     * {@link DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)}
     * with given arguments and returns bound information.
     *
     * @param catalog catalog the value for {@code catalog} parameter
     * @param schema schema the value for {@code schema} parameter
     * @param table table the value for {@code table} parameter
     * @param unique unique the value for {@code unique} parameter
     * @param approximate approximate the value for {@code approximage}
     * parameter
     * @return a list of {@link IndexInfo}
     * @throws SQLException if a database error occurs.
     */
    public List<IndexInfo> getIndexInfo(
            final String catalog, final String schema, final String table,
            final boolean unique, final boolean approximate)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schema the value for {@code schema} parameter
     * @param table the value for {@code table} parameter
     * @return a list of {@link PrimaryKey}s
     * @throws SQLException if a database error occurs.
     */
    public List<PrimaryKey> getPrimaryKeys(
            final String catalog, final String schema, final String table)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schemaPattern the value for {@code schemaPattern} parameter
     * @param procedureNamePattern the value for {@code procedureNamePattern}
     * parameter
     * @param columnNamePattern the value for {@code columnNamePattern}
     * parameter
     * @return a list of {@link ProcedureColumn}s
     * @throws SQLException if a database error occurs.
     */
    public List<ProcedureColumn> getProcedureColumns(
            final String catalog, final String schemaPattern,
            final String procedureNamePattern, final String columnNamePattern)
            throws SQLException {
        final List<ProcedureColumn> list = new ArrayList<ProcedureColumn>();
        final ResultSet results = metadata.getProcedureColumns(
                catalog, schemaPattern, procedureNamePattern,
                columnNamePattern);
        try {
            bind(results, ProcedureColumn.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schemaPattern the value for {@code schemaPattern} parameter
     * @param procedureNamePattern the value for {@code procedureNamePattern}
     * parameter
     * @return a list of {@link Procedure}s
     * @throws SQLException if a database error occurs.
     */
    public List<Procedure> getProcedures(final String catalog,
                                         final String schemaPattern,
                                         final String procedureNamePattern)
            throws SQLException {
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
     * Invokes
     * {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
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
            throws SQLException {
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
     * Invokes {@link DatabaseMetaData#getSchemas()} and returns bound
     * information.
     *
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     */
    public List<SchemaName> getSchemas() throws SQLException {
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
     * Invokes
     * {@link DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter.
     * @param schemaPattern the value for {@code schemaPattern} parameter.
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     */
    public List<Schema> getSchemas(final String catalog,
                                   final String schemaPattern)
            throws SQLException {
        final List<Schema> list = new ArrayList<Schema>();
        final ResultSet results = metadata.getSchemas(catalog, schemaPattern);
        if (results == null) {
            // SQLite!!!
            logger.warning("null result set returned");
            return list;
        }
        try {
            bind(results, Schema.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schemaPattern the value for {@code schemaPattern} parameter
     * @param tableNamePattern the value for {@code tableNamePattern} parameter
     * @param types the value for {@code types} parameter
     * @return a list of tables
     * @throws SQLException if a database error occurs.
     */
    public List<Table> getTables(final String catalog,
                                 final String schemaPattern,
                                 final String tableNamePattern,
                                 final String[] types)
            throws SQLException {
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schemaPattern the value for {@code schemaPattern} parameter
     * @param tableNamePattern the value for {@code tableNamePattern} parameter
     * @return a list of {@link TablePrivilege}
     * @throws SQLException if a database error occurs.
     */
    public List<TablePrivilege> getTablePrivileges(
            final String catalog, final String schemaPattern,
            final String tableNamePattern)
            throws SQLException {
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

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} and returns bound
     * information.
     *
     * @return a list of {@link TableType}.
     * @throws SQLException if a database error occurs.
     */
    public List<TableType> getTableTypes() throws SQLException {
        final List<TableType> list = new ArrayList<TableType>();
        final ResultSet results = metadata.getTableTypes();
        try {
            bind(results, TableType.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} and returns bound
     * information.
     *
     * @return a list of {@link TypeInfo}
     * @throws SQLException if a database error occurs.
     */
    public List<TypeInfo> getTypeInfo() throws SQLException {
        final List<TypeInfo> list = new ArrayList<TypeInfo>();
        final ResultSet results = metadata.getTypeInfo();
        try {
            bind(results, TypeInfo.class, list);
        } finally {
            results.close();
        }
        return list;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])}
     * with given arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter.
     * @param schemaPattern the value for {@code schemaPattern} parameter
     * @param typeNamePattern the value for {@code typeNamePattern} parameter.
     * @param types the value for {@code type} parameter
     * @return a list of {@link UDT}.
     * @throws SQLException if a database error occurs.
     */
    public List<UDT> getUDTs(final String catalog, final String schemaPattern,
                             final String typeNamePattern, final int[] types)
            throws SQLException {
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
     * Invokes
     * {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)}
     * with given arguments and returns bound information.
     *
     * @param catalog catalog the value for {@code catalog} parameter
     * @param schema schema the value for {@code schema} parameter
     * @param table table the value for {@code table} parameter
     * @return a list of {@link VersionColumn}
     * @throws SQLException if a database access error occurs.
     */
    public List<VersionColumn> getVersionColumns(final String catalog,
                                                 final String schema,
                                                 final String table)
            throws SQLException {
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
    @Deprecated
    private DatabaseMetaData getMetaData() {
        return metadata;
    }

    // ------------------------------------------------------------ suppressions
    private Set<String> getSuppressions() {
        if (suppressions == null) {
            suppressions = new HashSet<String>();
        }
        return suppressions;
    }

    private void suppress(final String path) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        if (getSuppressions().add(path)) {
            logger.fine(format("duplicate suppression path: %s", path));
        }
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
        suppress(path);
        if (otherPaths != null) {
            for (final String otherPath : otherPaths) {
                suppress(otherPath);
            }
        }
        return this;
    }

    private boolean suppressed(final String path) {
        return getSuppressions().contains(path);
    }

    // ------------------------------------------------------------------- alias
    private Map<String, String> getAliases() {
        if (aliases == null) {
            aliases = new HashMap<String, String>();
        }
        return aliases;
    }

    /**
     * Add path aliases and returns this instance.
     *
     * @param path the path to alias.
     * @param alias the alias value.
     * @return this instance.
     */
    private MetadataContext alias(final String path, final String alias) {
        final String previous = getAliases().put(path, alias);
        return this;
    }

    private String alias(final String path) {
        return getAliases().get(path);
    }

    // ----------------------------------------------------------------- bfields
    private Map<Field, Bind> bfields(final Class<?> klass) {
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
                bfields.put(klass, unmodifiableMap(value));
            } catch (final ReflectiveOperationException roe) {
                logger.severe(format(
                        "failed to get fields from %s annotated with %s",
                        klass, Bind.class));
                return emptyMap();
            }
        }
        return value;
    }

    // ----------------------------------------------------------------- ifields
    private Map<Field, Invoke> ifields(final Class<?> klass) {
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
                ifields.put(klass, unmodifiableMap(value));
            } catch (final ReflectiveOperationException roe) {
                logger.severe(format(
                        "failed to get fields from %s annotated with %s",
                        klass, Invoke.class));
                return emptyMap();
            }
        }
        return value;
    }

    // ------------------------------------------------------------------ ptypes
    private Class<?> ptype(final Field field) {
        if (field == null) {
            throw new NullPointerException("field is null");
        }
        Class<?> ptype = ptypes.get(field);
        if (ptype == null) {
            final ParameterizedType parameterizedType
                    = (ParameterizedType) field.getGenericType();
            ptype = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            ptypes.put(field, ptype);

        }
        return ptype;
    }

    // -------------------------------------------------------------------------
    private final DatabaseMetaData metadata;

    private Set<String> suppressions;

    private Map<String, String> aliases;

    // fields with @Bind
    private final transient Map<Class<?>, Map<Field, Bind>> bfields
            = new HashMap<Class<?>, Map<Field, Bind>>();

    // fields with @Invoke
    private final transient Map<Class<?>, Map<Field, Invoke>> ifields
            = new HashMap<Class<?>, Map<Field, Invoke>>();

    // parameterized types of java.util.List fields
    private final transient Map<Field, Class<?>> ptypes
            = new HashMap<Field, Class<?>>();
}
