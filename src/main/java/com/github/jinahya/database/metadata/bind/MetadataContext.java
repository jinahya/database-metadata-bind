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

import lombok.NonNull;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import static com.github.jinahya.database.metadata.bind.Invokes.arguments;
import static com.github.jinahya.database.metadata.bind.Utils.field;
import static com.github.jinahya.database.metadata.bind.Utils.fields;
import static com.github.jinahya.database.metadata.bind.Utils.labels;
import static com.github.jinahya.database.metadata.bind.Utils.suppressionPath;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 * A context class for retrieving information from an instance of {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MetadataContext {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = getLogger(MetadataContext.class.getName());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link #getSchemas(java.lang.String, java.lang.String)} on given {@code context} with given {@code
     * catalog}.
     *
     * @param context  the context
     * @param catalog  the value for the first parameter of {@link #getSchemas(java.lang.String, java.lang.String)}.
     * @param nonempty a flag for non empty
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     */
    public static List<Schema> getSchemas(@NonNull final MetadataContext context, final String catalog,
                                          final boolean nonempty)
            throws SQLException {
        final List<Schema> schemas = context.getSchemas(catalog, null);
        if (schemas.isEmpty() && nonempty) {
            final Schema schema = new Schema();
            schema.virtual = true;
            schema.setTableCatalog(catalog);
            schema.setTableSchem("");
            if (!context.isSuppressionPath("schema/functions")) {
                schema.getFunctions().addAll(context.getFunctions(
                        schema.getTableCatalog(), schema.getTableSchem(), null));
            }
            if (!context.isSuppressionPath("schema/procedures")) {
                schema.getProcedures().addAll(context.getProcedures(
                        schema.getTableCatalog(), schema.getTableSchem(), null));
            }
            if (!context.isSuppressionPath("schema/tables")) {
                schema.getTables().addAll(context.getTables(
                        schema.getTableCatalog(), schema.getTableSchem(), null, null));
            }
            if (!context.isSuppressionPath("schema/UDTs")) {
                schema.getUDTs().addAll(context.getUDTs(
                        schema.getTableCatalog(), schema.getTableSchem(), null, null));
            }
            schemas.add(schema);
        }
        return schemas;
    }

    public static List<Schema> getSchemas(@NonNull final MetadataContext context, final String catalog)
            throws SQLException {
        return getSchemas(context, catalog, false);
    }

    /**
     * Invokes {@link #getCatalogs()} on given context.
     *
     * @param context  the context
     * @param nonempty a flag for adding a virtual instances if non retrieved.
     * @return a list of catalogs
     * @throws SQLException if a database error occurs.
     */
    public static List<Catalog> getCatalogs(@NonNull final MetadataContext context, final boolean nonempty)
            throws SQLException {
        final List<Catalog> catalogs = context.getCatalogs();
        if (catalogs.isEmpty() && nonempty) {
            final Catalog catalog = new Catalog();
            catalog.virtual = true;
            catalog.setTableCat("");
            catalogs.add(catalog);
            if (!context.isSuppressionPath("catalog/schemas")) {
                catalog.getSchemas().addAll(context.getSchemas(catalog.getTableCat(), ""));
            }
        }
        if (!context.isSuppressionPath("catalog/schemas")) {
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
                    catalog.getSchemas().addAll(getSchemas(context, catalog.getTableCat(), true));
                }
            }
        }
        return catalogs;
    }

    public static List<Catalog> getCatalogs(@NonNull final MetadataContext context) throws SQLException {
        return getCatalogs(context, false);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given instance of {@link DatabaseMetaData}.
     *
     * @param metadata the {@link DatabaseMetaData} instance to hold.
     */
    public MetadataContext(final DatabaseMetaData metadata) {
        super();
        this.databaseMetadata = requireNonNull(metadata, "databaseMetadata is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Binds given instance from specified record.
     *
     * @param <T>      instance type parameter
     * @param results  the result set from which the instance is bound
     * @param type     the type of the instance
     * @param instance the instance
     * @return given instance
     * @throws SQLException if a database error occurs.
     */
    private <T> T bind(final ResultSet results, final Class<T> type, final T instance) throws SQLException {
        final Set<String> labels = labels(results);
        for (final Entry<Field, Bind> bfield : bfields(type).entrySet()) {
            final Field field = bfield.getKey();
            final Bind bind = bfield.getValue();
            String label = bind.label();
            final String path = suppressionPath(type, field);
            if (isSuppressionPath(path)) {
                continue;
            }
            final String formatted = format("field=%s, suppressionPath=%s, bind=%s", field, path, bind);
            if (!labels.remove(label)) {
                logger.warning(format("unknown label; %s", formatted));
                continue;
            }
            if (bind.unused()) {
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
                logger.log(SEVERE, format("failed to set %s with %s on %s", field, value, instance), roe);
            }
        }
        for (String label : labels) {
            final Object value = results.getObject(label);
            if (logger.isLoggable(FINE)) {
                logger.fine(format("unhandled; klass=%s, label=%s, value=%s", type, label, value));
            }
        }
        for (final Entry<Field, Invoke> ifield : ifields(type).entrySet()) {
            final Field field = ifield.getKey();
            if (!field.getType().equals(List.class)) {
                logger.severe(format("wrong field type: %s", field.getType()));
                continue;
            }
            final Invoke invoke = ifield.getValue();
            final String path = Utils.suppressionPath(type, field);
            final String formatted = format("field=%s, suppressionPath=%s, invoke=%s", field, path, invoke);
            if (isSuppressionPath(path)) {
                if (logger.isLoggable(FINE)) {
                    logger.fine(format("skipping; %s", formatted));
                }
                continue;
            }
            final String name = invoke.name();
            final Class<?>[] types = invoke.types();
            final Method method;
            try {
                method = DatabaseMetaData.class.getMethod(name, types);
            } catch (final NoSuchMethodException nsme) {
                logger.log(SEVERE, format("unknown method; %1$s", formatted), nsme);
                continue;
            } catch (final NoSuchMethodError nsme) {
                logger.log(SEVERE, format("unknown method; %s", formatted), nsme);
                continue;
            }
            final List<Object> fvalue = new ArrayList<>();
            final Class<?> ptype = ptype(field);
            for (final Literals parameters : invoke.parameters()) {
                final String[] literals = parameters.value();
                final Object[] arguments;
                try {
                    arguments = arguments(type, instance, types, literals);
                } catch (final ReflectiveOperationException roe) {
                    logger.severe(format("failed to convert arguments from %s on %s", Arrays.toString(literals), type));
                    continue;
                }
                final Object result;
                try {
                    result = method.invoke(databaseMetadata, arguments);
                } catch (final Exception e) { // NoSuchMethod
                    logger.log(SEVERE, format("failed to invoke %s with %s", formatted, Arrays.toString(arguments)), e);
                    continue;
                } catch (final Error e) { // NoSuchMethod/AbstractMethod
                    logger.log(SEVERE, format("failed to invoke %s with %s", formatted, Arrays.toString(arguments)), e);
                    continue;
                }
                if (!(result instanceof ResultSet)) {
                    logger.severe(format("wrong result; %s for %s", result, formatted));
                    continue;
                }
                try {
                    bind((ResultSet) result, ptype, fvalue);
                } finally {
                    ((ResultSet) result).close();
                }
            }
            try {
                field.set(instance, fvalue);
            } catch (final ReflectiveOperationException roe) {
                logger.severe(format("failed to set %s with %s on %s", field, fvalue, instance));
            }
        } // end-of-invoke-field-loop
        return instance;
    }

    /**
     * Binds all records as given type and add them to specified list.
     *
     * @param <T>       binding type parameter
     * @param results   the records to bind
     * @param klass     the type of instances
     * @param instances a list to which instances are added
     * @return given list
     * @throws SQLException if a database error occurs.
     */
    private <T> List<? super T> bind(final ResultSet results, final Class<T> klass, final List<? super T> instances)
            throws SQLException {
        if (results == null) {
            throw new NullPointerException("results is null");
        }
        if (klass == null) {
            throw new NullPointerException("klass is null");
        }
        if (instances == null) {
            throw new NullPointerException("instances is null");
        }
        while (results.next()) {
            final T instance;
            try {
                instance = klass.newInstance();
            } catch (final ReflectiveOperationException roe) {
                logger.log(SEVERE, format("failed to create new instance of %s", klass), roe);
                continue;
            }
            instances.add(bind(results, klass, instance));
        }
        return instances;
    }

    /**
     * Invokes {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} with given arguments and returns bound information.
     *
     * @param catalog              the value for {@code catalog} parameter.
     * @param schemaPattern        the value for {@code schemaPattern} parameter.
     * @param typeNamePattern      the value for {@code typeNamePattern} parameter.
     * @param attributeNamePattern the value for {@code attributeNamePattern} parameter.
     * @return a list of attributes.
     * @throws SQLException if a database error occurs.
     */
    public List<Attribute> getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern,
                                         final String attributeNamePattern)
            throws SQLException {
        final List<Attribute> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getAttributes(
                catalog, schemaPattern, typeNamePattern, attributeNamePattern)) {
            if (results != null) {
                bind(results, Attribute.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int,
     * boolean)} with given arguments and returns bound information.
     *
     * @param catalog  the value for {@code catalog} parameter
     * @param schema   the value for {@code schema} parameter
     * @param table    the value for {@code table} parameter
     * @param scope    the value for {@code scope} parameter
     * @param nullable the value for {@code nullable} parameter
     * @return a list of best row identifies
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)
     */
    public List<BestRowIdentifier> getBestRowIdentifier(final String catalog, final String schema, final String table,
                                                        final int scope, final boolean nullable)
            throws SQLException {
        final List<BestRowIdentifier> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            if (results != null) {
                bind(results, BestRowIdentifier.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} and returns bound information.
     *
     * @return a list of catalogs
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getCatalogs()
     */
    public List<Catalog> getCatalogs() throws SQLException {
        final List<Catalog> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getCatalogs()) {
            if (results != null) {
                bind(results, Catalog.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} and returns bound information.
     *
     * @return a list of client info properties
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public List<ClientInfoProperty> getClientInfoProperties() throws SQLException {
        final List<ClientInfoProperty> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getClientInfoProperties()) {
            if (results != null) {
                bind(results, ClientInfoProperty.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} with given arguments and returns bound information.
     *
     * @param catalog           the value for {@code catalog} parameter
     * @param schemaPattern     the value for {@code schemaPattern} parameter
     * @param tableNamePattern  the value for {@code tableNameSchema} parameter
     * @param columnNamePattern the value for {@code columnNamePattern} parameter
     * @return a list of columns
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    public List<Column> getColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                                   final String columnNamePattern)
            throws SQLException {
        final List<Column> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, Column.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} with given arguments and returns bound information.
     *
     * @param catalog           the value for {@code catalog} parameter
     * @param schema            the value for {@code schema} parameter
     * @param table             the value for {@code table} parameter
     * @param columnNamePattern the value for {@code columnNamePattern} parameter
     * @return a list of column privileges
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    public List<ColumnPrivilege> getColumnPrivileges(final String catalog, final String schema, final String table,
                                                     final String columnNamePattern)
            throws SQLException {
        final List<ColumnPrivilege> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            if (results != null) {
                bind(results, ColumnPrivilege.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)} with given arguments and returns bound information.
     *
     * @param parentCatalog  the value for {@code parentCatalog} parameter
     * @param parentSchema   the value for {@code parentSchema} parameter
     * @param parentTable    the value for {@code parentTable} parameter
     * @param foreignCatalog the value for {@code foreignCatalog} parameter
     * @param foreignSchema  the value for {@code foreignSchema} parameter
     * @param foreignTable   the value for {@code foreignTable} parameter
     * @return a list of cross references
     * @throws SQLException if a database error occurs.
     */
    public List<CrossReference> getCrossReferences(final String parentCatalog, final String parentSchema,
                                                   final String parentTable, final String foreignCatalog,
                                                   final String foreignSchema, final String foreignTable)
            throws SQLException {
        final List<CrossReference> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getCrossReference(
                parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable)) {
            if (results != null) {
                bind(results, CrossReference.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} with given arguments and returns bound information.
     *
     * @param catalog             the value for {@code catalog} parameter
     * @param schemaPattern       the value for {@code schemaPattern} parameter
     * @param functionNamePattern the value for {@code functionNamePattern} parameter
     * @param columnNamePattern   the value for {@code columnNamePattern} parameter
     * @return a list of function columns
     * @throws SQLException if a database error occurs.
     */
    public List<FunctionColumn> getFunctionColumns(final String catalog, final String schemaPattern,
                                                   final String functionNamePattern, final String columnNamePattern)
            throws SQLException {
        final List<FunctionColumn> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getFunctionColumns(
                catalog, schemaPattern, functionNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, FunctionColumn.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} with given
     * arguments and returns bound information.
     *
     * @param catalog             the value for {@code catalog} parameter
     * @param schemaPattern       the value for {@code schemaPattern} parameter
     * @param functionNamePattern the value for {@code functionNamePattern} parameter
     * @return a list of functions
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     */
    public List<Function> getFunctions(final String catalog, final String schemaPattern,
                                       final String functionNamePattern)
            throws SQLException {
        final List<Function> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getFunctions(catalog, schemaPattern, functionNamePattern)) {
            if (results != null) {
                bind(results, Function.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} with given
     * arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schema  the value for {@code schema} parameter
     * @param table   the value for {@code table} parameter
     * @return a list of exported keys
     * @throws SQLException if a database error occurs.
     */
    public List<ExportedKey> getExportedKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        final List<ExportedKey> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getExportedKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, ExportedKey.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)} with given
     * arguments and returns bound information.
     *
     * @param catalog catalog the value for {@code catalog} parameter
     * @param schema  schema the value for {@code schema} parameter
     * @param table   table the value for {@code table} parameter
     * @return a list of imported keys
     * @throws SQLException if a database error occurs.
     */
    public List<ImportedKey> getImportedKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        final List<ImportedKey> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getImportedKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, ImportedKey.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean,
     * boolean)} with given arguments and returns bound information.
     *
     * @param catalog     catalog the value for {@code catalog} parameter
     * @param schema      schema the value for {@code schema} parameter
     * @param table       table the value for {@code table} parameter
     * @param unique      unique the value for {@code unique} parameter
     * @param approximate approximate the value for {@code approximage} parameter
     * @return a list of index info
     * @throws SQLException if a database error occurs.
     */
    public List<IndexInfo> getIndexInfo(final String catalog, final String schema, final String table,
                                        final boolean unique, final boolean approximate)
            throws SQLException {
        final List<IndexInfo> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getIndexInfo(catalog, schema, table, unique, approximate)) {
            if (results != null) {
                bind(results, IndexInfo.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)} with given
     * arguments and returns bound information.
     *
     * @param catalog the value for {@code catalog} parameter
     * @param schema  the value for {@code schema} parameter
     * @param table   the value for {@code table} parameter
     * @return a list of primary keys
     * @throws SQLException if a database error occurs.
     */
    public List<PrimaryKey> getPrimaryKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        final List<PrimaryKey> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getPrimaryKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, PrimaryKey.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} with given arguments and returns bound information.
     *
     * @param catalog              the value for {@code catalog} parameter
     * @param schemaPattern        the value for {@code schemaPattern} parameter
     * @param procedureNamePattern the value for {@code procedureNamePattern} parameter
     * @param columnNamePattern    the value for {@code columnNamePattern} parameter
     * @return a list of procedure columns
     * @throws SQLException if a database error occurs.
     */
    public List<ProcedureColumn> getProcedureColumns(final String catalog, final String schemaPattern,
                                                     final String procedureNamePattern, final String columnNamePattern)
            throws SQLException {
        final List<ProcedureColumn> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getProcedureColumns(
                catalog, schemaPattern, procedureNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, ProcedureColumn.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} with given
     * arguments and returns bound information.
     *
     * @param catalog              the value for {@code catalog} parameter
     * @param schemaPattern        the value for {@code schemaPattern} parameter
     * @param procedureNamePattern the value for {@code procedureNamePattern} parameter
     * @return a list of procedures
     * @throws SQLException if a database error occurs.
     */
    public List<Procedure> getProcedures(final String catalog, final String schemaPattern,
                                         final String procedureNamePattern)
            throws SQLException {
        final List<Procedure> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            if (results != null) {
                bind(results, Procedure.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} with given arguments and returns bound information.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of pseudo columns
     * @throws SQLException if a database error occurs.
     */
    @IgnoreJRERequirement // getPseudoColumns since 1.7
    public List<PseudoColumn> getPseudoColumns(final String catalog, final String schemaPattern,
                                               final String tableNamePattern, final String columnNamePattern)
            throws SQLException {
        final List<PseudoColumn> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getPseudoColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, PseudoColumn.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} and returns bound information.
     *
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     */
    public List<SchemaName> getSchemas() throws SQLException {
        final List<SchemaName> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getSchemas()) {
            if (results != null) {
                bind(results, SchemaName.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)} with given arguments and returns
     * bound information.
     *
     * @param catalog       the value for {@code catalog} parameter.
     * @param schemaPattern the value for {@code schemaPattern} parameter.
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     */
    public List<Schema> getSchemas(final String catalog, final String schemaPattern)
            throws SQLException {
        final List<Schema> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getSchemas(catalog, schemaPattern)) {
            if (results != null) {
                bind(results, Schema.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String[])} with given arguments and returns bound information.
     *
     * @param catalog          the value for {@code catalog} parameter
     * @param schemaPattern    the value for {@code schemaPattern} parameter
     * @param tableNamePattern the value for {@code tableNamePattern} parameter
     * @param types            the value for {@code types} parameter
     * @return a list of tables
     * @throws SQLException if a database error occurs.
     */
    public List<Table> getTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                                 final String[] types)
            throws SQLException {
        final List<Table> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            if (results != null) {
                bind(results, Table.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} with
     * given arguments and returns bound information.
     *
     * @param catalog          the value for {@code catalog} parameter
     * @param schemaPattern    the value for {@code schemaPattern} parameter
     * @param tableNamePattern the value for {@code tableNamePattern} parameter
     * @return a list of table privileges
     * @throws SQLException if a database error occurs.
     */
    public List<TablePrivilege> getTablePrivileges(final String catalog, final String schemaPattern,
                                                   final String tableNamePattern)
            throws SQLException {
        final List<TablePrivilege> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                bind(results, TablePrivilege.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} and returns bound information.
     *
     * @return a list of table types
     * @throws SQLException if a database error occurs.
     */
    public List<TableType> getTableTypes() throws SQLException {
        final List<TableType> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getTableTypes()) {
            if (results != null) {
                bind(results, TableType.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} and returns bound information.
     *
     * @return a list of type info
     * @throws SQLException if a database error occurs.
     */
    public List<TypeInfo> getTypeInfo() throws SQLException {
        final List<TypeInfo> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getTypeInfo()) {
            if (results != null) {
                bind(results, TypeInfo.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])} with given
     * arguments and returns bound information.
     *
     * @param catalog         the value for {@code catalog} parameter.
     * @param schemaPattern   the value for {@code schemaPattern} parameter
     * @param typeNamePattern the value for {@code typeNamePattern} parameter.
     * @param types           the value for {@code type} parameter
     * @return a list of UDTs
     * @throws SQLException if a database error occurs.
     */
    public List<UDT> getUDTs(final String catalog, final String schemaPattern,
                             final String typeNamePattern, final int[] types)
            throws SQLException {
        final List<UDT> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
            if (results != null) {
                bind(results, UDT.class, list);
            }
        }
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} with
     * given arguments and returns bound information.
     *
     * @param catalog catalog the value for {@code catalog} parameter
     * @param schema  schema the value for {@code schema} parameter
     * @param table   table the value for {@code table} parameter
     * @return a list of version columns
     * @throws SQLException if a database access error occurs.
     */
    public List<VersionColumn> getVersionColumns(final String catalog, final String schema, final String table)
            throws SQLException {
        final List<VersionColumn> list = new ArrayList<>();
        try (ResultSet results = databaseMetadata.getVersionColumns(catalog, schema, table)) {
            if (results != null) {
                bind(results, VersionColumn.class, list);
            }
        }
        return list;
    }

    // ------------------------------------------------------------------------------------------------ databaseMetadata
    @Deprecated
    private DatabaseMetaData getMetaData() {
        return databaseMetadata;
    }

    // ------------------------------------------------------------------------------------------------- suppressedPaths
    private Set<String> getSuppressedPaths() {
        if (suppressedPaths == null) {
            suppressedPaths = new HashSet<>();
        }
        return suppressedPaths;
    }

    private void addSuppressionPath(@NonNull final String suppressionPath) {
        getSuppressedPaths().add(suppressionPath);
    }

    /**
     * Adds suppression paths and returns this instance.
     *
     * @param suppressionPath the first suppression suppressionPath
     * @param otherPaths      other suppression paths
     * @return this instance
     */
    public MetadataContext addSuppressionPaths(@NonNull final String suppressionPath,
                                               @NonNull final String... otherPaths) {
        addSuppressionPath(suppressionPath);
        for (final String otherPath : otherPaths) {
            addSuppressionPath(otherPath);
        }
        return this;
    }

    private boolean isSuppressionPath(@NonNull final String suppressionPath) {
        return getSuppressedPaths().contains(suppressionPath);
    }

    // --------------------------------------------------------------------------------------------------------- bfields
    private Map<Field, Bind> bfields(@NonNull final Class<?> klass) {
        Map<Field, Bind> value = bfields.get(klass);
        if (value == null) {
            value = fields(klass, Bind.class);
            for (Field field : value.keySet()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
            }
            bfields.put(klass, unmodifiableMap(value));
        }
        return value;
    }

    // --------------------------------------------------------------------------------------------------------- ifields
    private Map<Field, Invoke> ifields(@NonNull final Class<?> klass) {
        Map<Field, Invoke> value = ifields.get(klass);
        if (value == null) {
            value = fields(klass, Invoke.class);
            for (Field field : value.keySet()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
            }
            ifields.put(klass, unmodifiableMap(value));
        }
        return value;
    }

    // ---------------------------------------------------------------------------------------------------------- ptypes
    private Class<?> ptype(@NonNull final Field field) {
        Class<?> ptype = ptypes.get(field);
        if (ptype == null) {
            final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            ptype = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            ptypes.put(field, ptype);
        }
        return ptype;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final DatabaseMetaData databaseMetadata;

    // suppression paths
    private Set<String> suppressedPaths;

    // fields with @Bind
    private final transient Map<Class<?>, Map<Field, Bind>> bfields = new HashMap<>();

    // fields with @Invoke
    private final transient Map<Class<?>, Map<Field, Invoke>> ifields = new HashMap<>();

    // parameterized types of java.util.List fields
    private final transient Map<Field, Class<?>> ptypes = new HashMap<>();
}
