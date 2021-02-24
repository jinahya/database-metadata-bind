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
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
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
//                schema.getFunctions().addAll(context.getFunctions(
//                        schema.getTableCatalog(), schema.getTableSchem(), null));
            }
            if (!context.isSuppressionPath("schema/procedures")) {
//                schema.getProcedures().addAll(context.getProcedures(
//                        schema.getTableCatalog(), schema.getTableSchem(), null));
            }
            if (!context.isSuppressionPath("schema/tables")) {
//                schema.getTables().addAll(context.getTables(
//                        schema.getTableCatalog(), schema.getTableSchem(), null, null));
            }
            if (!context.isSuppressionPath("schema/UDTs")) {
//                schema.getUDTs().addAll(context.getUDTs(
//                        schema.getTableCatalog(), schema.getTableSchem(), null, null));
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
                //catalog.getSchemas().addAll(context.getSchemas(catalog.getTableCat(), ""));
            }
        }
        if (!context.isSuppressionPath("catalog/schemas")) {
            boolean allempty = true;
            for (final Catalog catalog : catalogs) {
//                if (!catalog.getSchemas().isEmpty()) {
//                    allempty = false;
//                    break;
//                }
            }
            if (allempty) {
                logger.warning("schemas are all empty");
//                for (final Catalog catalog : catalogs) {
//                    catalog.getSchemas().addAll(getSchemas(context, catalog.getTableCat(), true));
//                }
            }
        }
        return catalogs;
    }

    public static List<Catalog> getCatalogs(@NonNull final MetadataContext context) throws SQLException {
        return getCatalogs(context, false);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified database meta date.
     *
     * @param databaseMetaData the database meta data to hold.
     */
    public MetadataContext(final DatabaseMetaData databaseMetaData) {
        super();
        this.databaseMetaData = requireNonNull(databaseMetaData, "databaseMetadata is null");
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
                    result = method.invoke(databaseMetaData, arguments);
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
     * Binds all records as given type and add them to specified collection.
     *
     * @param <T>        binding type parameter
     * @param results    the records to bind.
     * @param type       the type of instances.
     * @param collection the collection to which bound instances are added
     * @return given list
     * @throws SQLException if a database error occurs.
     */
    private <T, C extends Collection<? super T>> C bind(final ResultSet results, final Class<T> type,
                                                        final C collection)
            throws SQLException {
        requireNonNull(results, "results is null");
        requireNonNull(type, "type is null");
        requireNonNull(collection, "collection is null");
        while (results.next()) {
            final T instance;
            try {
                instance = type.getConstructor().newInstance();
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException(roe);
            }
            collection.add(bind(results, type, instance));
        }
        return collection;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and adds bound values to specified collection.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for {@code attributeNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @return a list of attributes.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getAttributes(String, String, String, String)
     */
    public <T extends Collection<? super Attribute>> T getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern,
                                                                     final String attributeNamePattern,
                                                                     final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getAttributes(
                catalog, schemaPattern, typeNamePattern, attributeNamePattern)) {
            if (results != null) {
                bind(results, Attribute.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and returns bound values.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for {@code attributeNamePattern} parameter.
     * @return a list of attributes.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getAttributes(String, String, String, String)
     */
    public List<Attribute> getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern,
                                         final String attributeNamePattern)
            throws SQLException {
        return getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern, new ArrayList<>());
    }

    private void getAttributes(final UDT udt) throws SQLException {
        getAttributes(udt.getSchema().getCatalog().getTableCat(),
                      udt.getSchema().getTableSchem(),
                      udt.getTypeName(),
                      null,
                      udt.getAttributes())
                .forEach(a -> {
                    a.setUDT(udt);
                });
    }

    // -------------------------------------------------------------------------------------------- getBestRowIdentifier

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
    public <T extends Collection<? super BestRowIdentifier>> T getBestRowIdentifier(
            final String catalog, final String schema, final String table, final int scope, final boolean nullable,
            final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            if (results == null) {
                logger.log(Level.WARNING, "null results retrieved");
            } else {
                bind(results, BestRowIdentifier.class, collection);
            }
        }
        return collection;
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
        return getBestRowIdentifier(catalog, schema, table, scope, nullable, new ArrayList<>());
    }

    private void getBestRowIdentifier(final Table table) throws SQLException {
        for (final BestRowIdentifier.Scope scope : BestRowIdentifier.Scope.values()) {
            getBestRowIdentifier(table.getSchema().getCatalog().getTableCat(),
                                 table.getSchema().getTableSchem(), table.getTableName(), scope.getRawValue(), true,
                                 table.getBestRowIdentifiers())
                    .forEach(i -> {
                        i.setTable(table);
                    });
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method and adds all bound entities to specified collection.
     *
     * @param collection the collection to which all bound entities are added.
     * @param <T>        element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<Catalog>> T getCatalogs(final T collection) throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getCatalogs()) {
            if (results != null) {
                bind(results, Catalog.class, collection);
            }
        }
//        if (collection.isEmpty()) {
//            collection.add(Catalog.newVirtualInstance());
//        }
//        for (final Catalog catalog : collection) {
//            getSchemas(catalog.getTableCat(), null, catalog.getSchemas())
//                    .forEach(s -> {
//                        s.setParent(catalog);
//                    });
//        }
        return collection;
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
        try (ResultSet results = databaseMetaData.getCatalogs()) {
            if (results != null) {
                bind(results, Catalog.class, list);
            }
        }
        return list;
    }

    // ----------------------------------------------------------------------------------------- getClientInfoProperties

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public <T extends Collection<? super ClientInfoProperty>> T getClientInfoProperties(final T collection)
            throws SQLException {
        try {
            try (ResultSet results = databaseMetaData.getClientInfoProperties()) {
                if (results != null) {
                    bind(results, ClientInfoProperty.class, collection);
                }
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and returns bound values.
     *
     * @return a list of client info properties
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public List<ClientInfoProperty> getClientInfoProperties() throws SQLException {
        return getClientInfoProperties(new ArrayList<>());
    }

    // --------------------------------------------------------------------------------------------- getColumnPrivileges

    /**
     * Invokes {@link DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and adds bound values to specified collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schema            a value for {@code schema} parameter.
     * @param table             a value for {@code table} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    public <T extends Collection<? super ColumnPrivilege>> T getColumnPrivileges(
            final String catalog, final String schema, final String table, final String columnNamePattern,
            final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            if (results != null) {
                bind(results, ColumnPrivilege.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and returns bound values.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schema            a value for {@code schema} parameter.
     * @param table             a value for {@code table} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of column privileges.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    public List<ColumnPrivilege> getColumnPrivileges(final String catalog, final String schema, final String table,
                                                     final String columnNamePattern)
            throws SQLException {
        return getColumnPrivileges(catalog, schema, table, columnNamePattern, new ArrayList<>());
    }

    private void getColumnPrivileges(final Column column) throws SQLException {
        requireNonNull(column, "column is null");
        getColumnPrivileges(column.getTable().getSchema().getCatalog().getTableCat(),
                            column.getTable().getSchema().getTableSchem(),
                            column.getTable().getTableName(),
                            column.getColumnName(),
                            column.getColumnPrivileges())
                .forEach(p -> {
                    p.setColumn(column);
                });
    }

    // ------------------------------------------------------------------------------------------------------ getColumns

    /**
     * Invokes {@link DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and adds values to specified collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    public <T extends Collection<? super Column>> T getColumns(final String catalog, final String schemaPattern,
                                                               final String tableNamePattern,
                                                               final String columnNamePattern, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, Column.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and returns bound values.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of columns
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    public List<Column> getColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                                   final String columnNamePattern)
            throws SQLException {
        return getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
    }

    private void getColumns(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getColumns(table.getSchema().getCatalog().getTableCat(),
                   table.getSchema().getTableSchem(),
                   table.getTableName(),
                   null,
                   table.getColumns())
                .forEach(c -> {
                    c.setTable(table);
                });
        for (final Column column : table.getColumns()) {
            getColumnPrivileges(column);
        }
    }

    // ----------------------------------------------------------------------------------------------- getCrossReference
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
        try (ResultSet results = databaseMetaData.getCrossReference(
                parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable)) {
            if (results != null) {
                bind(results, CrossReference.class, list);
            }
        }
        return list;
    }

    // ---------------------------------------------------------------------------------------------------- getFunctions
    public <T extends Collection<? super Function>> T getFunctions(final String catalog, final String schemaPattern,
                                                                   final String functionNamePattern,
                                                                   final T collection)
            throws SQLException {
        try {
            try (ResultSet results = databaseMetaData.getFunctions(catalog, schemaPattern, functionNamePattern)) {
                if (results != null) {
                    bind(results, Function.class, collection);
                }
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        return collection;
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
        return getFunctions(catalog, schemaPattern, functionNamePattern, new ArrayList<>());
    }

    private void getFunctions(final Schema schema) throws SQLException {
        requireNonNull(schema, "schema is null");
        getFunctions(schema.getParent().getTableCat(), schema.getTableSchem(), null, schema.getFunctions())
                .forEach(f -> {
                    f.setParent(schema);
                });
        for (final Function function : schema.getFunctions()) {
            getFunctionColumns(function);
        }
    }

    // --------------------------------------------------------------------------------------------- getFunctionsColumns
    public <T extends Collection<? super FunctionColumn>> T getFunctionColumns(
            final String catalog, final String schemaPattern, final String functionNamePattern,
            final String columnNamePattern, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getFunctionColumns(
                catalog, schemaPattern, functionNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, FunctionColumn.class, collection);
            }
        }
        return collection;
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
        return getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern, new ArrayList<>());
    }

    private void getFunctionColumns(final Function function) throws SQLException {
        requireNonNull(function, "function is null");
        getFunctionColumns(function.getParent().getParent().getTableCat(), function.getParent().getTableSchem(),
                           function.getFunctionName(), null, function.getFunctionColumns())
                .forEach(c -> {
                    c.setParent(function);
                });
    }

    // ------------------------------------------------------------------------------------------------- getExportedKeys

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
    public <T extends Collection<? super ExportedKey>> T getExportedKeys(final String catalog, final String schema,
                                                                         final String table, final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getExportedKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, ExportedKey.class, collection);
            }
        }
        return collection;
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
        return getExportedKeys(catalog, schema, table, new ArrayList<>());
    }

    public void getExportedKeys(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getExportedKeys(table.getSchema().getCatalog().getTableCat(),
                        table.getSchema().getTableSchem(),
                        table.getTableName(),
                        table.getExportedKeys())
                .forEach(k -> {
                    k.setTable(table);
                });
    }

    // ------------------------------------------------------------------------------------------------- getImportedKeys

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    catalog the value for {@code catalog} parameter.
     * @param schema     schema the value for {@code schema} parameter.
     * @param table      table the value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getImportedKeys(String, String, String)
     */
    public <T extends Collection<? super ImportedKey>> T getImportedKeys(
            final String catalog, final String schema, final String table, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getImportedKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, ImportedKey.class, collection);
            }
        }
        return collection;
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
        try (ResultSet results = databaseMetaData.getImportedKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, ImportedKey.class, list);
            }
        }
        return list;
    }

    private void getImportedKeys(final Table table) throws SQLException {
        getImportedKeys(table.getSchema().getCatalog().getTableCat(),
                        table.getSchema().getTableSchem(),
                        table.getTableName(), table.getImportedKeys())
                .forEach(k -> {
                    k.setTable(table);
                });
    }

    // ---------------------------------------------------------------------------------------------------- getIndexInfo

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean,
     * boolean)} method with given arguments and adds bound values to specified collection.
     *
     * @param catalog     a value for {@code catalog} parameter.
     * @param schema      a value for {@code schema} parameter.
     * @param table       a value for {@code table} parameter.
     * @param unique      a value for {@code unique} parameter.
     * @param approximate a value for {@code approximate} parameter.
     * @param collection  the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    public <T extends Collection<? super IndexInfo>> T getIndexInfo(
            final String catalog, final String schema, final String table, final boolean unique,
            final boolean approximate, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getIndexInfo(catalog, schema, table, unique, approximate)) {
            if (results != null) {
                bind(results, IndexInfo.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean,
     * boolean)} method with given arguments and returns bounds values.
     *
     * @param catalog     a value for {@code catalog} parameter.
     * @param schema      a value for {@code schema} parameter.
     * @param table       a value for {@code table} parameter.
     * @param unique      a value for {@code unique} parameter.
     * @param approximate a value for {@code approximate} parameter.
     * @return a list of index info.
     * @throws SQLException if a database error occurs.
     */
    public List<IndexInfo> getIndexInfo(final String catalog, final String schema, final String table,
                                        final boolean unique, final boolean approximate)
            throws SQLException {
        return getIndexInfo(catalog, schema, table, unique, approximate, new ArrayList<>());
    }

    private void getIndexInfo(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getIndexInfo(table.getSchema().getCatalog().getTableCat(),
                     table.getSchema().getTableSchem(),
                     table.getTableName(),
                     false,
                     true,
                     table.getIndexInfo())
                .forEach(i -> {
                    i.setTable(table);
                });
    }

    // -------------------------------------------------------------------------------------------------- getPrimaryKeys

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super PrimaryKey>> T getPrimaryKeys(
            final String catalog, final String schema, final String table, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getPrimaryKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, PrimaryKey.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and returns bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of primary keys.
     * @throws SQLException if a database error occurs.
     */
    public List<PrimaryKey> getPrimaryKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        return getPrimaryKeys(catalog, schema, table, new ArrayList<>());
    }

    private void getPrimaryKeys(final Table table) throws SQLException {
        getPrimaryKeys(table.getSchema().getCatalog().getTableCat(),
                       table.getSchema().getTableSchem(),
                       table.getTableName(), table.getPrimaryKeys())
                .forEach(k -> {
                    k.setTable(table);
                });
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and adds values to specified collection.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
     * @param collection           the collection to which values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super ProcedureColumn>> T getProcedureColumns(
            final String catalog, final String schemaPattern, final String procedureNamePattern,
            final String columnNamePattern, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getProcedureColumns(
                catalog, schemaPattern, procedureNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, ProcedureColumn.class, collection);
            }
        }
        return collection;
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
        return getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern, new ArrayList<>());
    }

    // --------------------------------------------------------------------------------------------------- getProcedures
    public <T extends Collection<? super Procedure>> T getProcedures(final String catalog, final String schemaPattern,
                                                                     final String procedureNamePattern,
                                                                     final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            if (results != null) {
                bind(results, Procedure.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} with given
     * arguments and returns bound information.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @return a list of procedures
     * @throws SQLException if a database error occurs.
     */
    public List<Procedure> getProcedures(final String catalog, final String schemaPattern,
                                         final String procedureNamePattern)
            throws SQLException {
        return getProcedures(catalog, schemaPattern, procedureNamePattern, new ArrayList<>());
    }

    private void getProcedures(final Schema schema) throws SQLException {
        requireNonNull(schema, "schema is null");
        getProcedures(schema.getParent().getTableCat(), schema.getTableSchem(), null, schema.getProcedures())
                .forEach(p -> {
                    p.setSchema(schema);
                });
        for (final Procedure procedure : schema.getProcedures()) {
            getProcedureColumns(procedure.getSchema().getCatalog().getTableCat(), procedure.getSchema().getTableSchem(),
                                procedure.getProcedureName(), null, procedure.getProcedureColumns());
        }
    }

    // ------------------------------------------------------------------------------------------------ getPseudoColumns

    /**
     * Invokes {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and adds bound values to specified collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    @IgnoreJRERequirement // getPseudoColumns since 1.7
    public <T extends Collection<? super PseudoColumn>> T getPseudoColumns(
            final String catalog, final String schemaPattern, final String tableNamePattern,
            final String columnNamePattern, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getPseudoColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, PseudoColumn.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and returns bound values.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of pseudo columns.
     * @throws SQLException if a database error occurs.
     */
    @IgnoreJRERequirement // getPseudoColumns since 1.7
    public List<PseudoColumn> getPseudoColumns(final String catalog, final String schemaPattern,
                                               final String tableNamePattern, final String columnNamePattern)
            throws SQLException {
        return getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
    }

    private void getPseudoColumns(final Table table) throws SQLException {
        getPseudoColumns(table.getSchema().getCatalog().getTableCat(),
                         table.getSchema().getTableSchem(),
                         table.getTableName(),
                         null,
                         table.getPseudoColumns())
                .forEach(c -> {
                    c.setTable(table);
                });
    }

    // ------------------------------------------------------------------------------------------------------ getSchemas

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} and returns bound information.
     *
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     */
    public List<SchemaName> getSchemas() throws SQLException {
        final List<SchemaName> list = new ArrayList<>();
        try (ResultSet results = databaseMetaData.getSchemas()) {
            if (results != null) {
                bind(results, SchemaName.class, list);
            }
        }
        return list;
    }

    public <T extends Collection<? super Schema>> T getSchemas(final String catalog, final String schemaPattern,
                                                               final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try {
            try (ResultSet results = databaseMetaData.getSchemas(catalog, schemaPattern)) {
                if (results != null) {
                    bind(results, Schema.class, collection);
                }
            }
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)} with given arguments and returns
     * bound information.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @return a list of schemas
     * @throws SQLException if a database error occurs.
     */
    public List<Schema> getSchemas(final String catalog, final String schemaPattern) throws SQLException {
        return getSchemas(catalog, schemaPattern, new ArrayList<>());
    }

    void getSchemas(final Catalog catalog) throws SQLException {
        requireNonNull(catalog, "catalog is null");
        getSchemas(catalog.getTableCat(),
                   null,
                   catalog.getSchemas())
                .forEach(s -> {
                    s.setCatalog(catalog);
                });
        if (catalog.getSchemas().isEmpty()) {
            catalog.getSchemas().add(Schema.newVirtualInstance(catalog));
        }
        for (final Schema schema : catalog.getSchemas()) {
            getFunctions(schema);
        }
        for (final Schema schema : catalog.getSchemas()) {
            getProcedures(schema);
        }
        for (final Schema schema : catalog.getSchemas()) {
            getTables(schema);
        }
        for (final Schema schema : catalog.getSchemas()) {
            getUDTs(schema);
        }
    }

    // -------------------------------------------------------------------------------------------------- getSuperTables
    public <T extends Collection<? super SuperTable>> T getSuperTables(final String catalog, final String schemaPattern,
                                                                       final String tableNamePattern,
                                                                       final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                bind(results, SuperTable.class, collection);
            }
        }
        return collection;
    }

    private void getSuperTables(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getSuperTables(table.getSchema().getCatalog().getTableCat(),
                       table.getSchema().getTableSchem(),
                       table.getTableName(),
                       table.getSuperTables())
                .forEach(t -> {
                    t.setTable(table);
                });
    }

    // --------------------------------------------------------------------------------------------------- getSuperTypes
    public <T extends Collection<? super SuperType>> T getSuperTypes(final String catalog, final String schemaPattern,
                                                                     final String typeNamePattern,
                                                                     final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
            if (results != null) {
                bind(results, SuperType.class, collection);
            }
        }
        return collection;
    }

    private void getSuperTypes(final UDT udt) throws SQLException {
        requireNonNull(udt, "udt is null");
        getSuperTypes(udt.getSchema().getCatalog().getTableCat(),
                      udt.getSchema().getTableSchem(),
                      udt.getTypeName(),
                      udt.getSuperTypes())
                .forEach(t -> {
                    t.setUDT(udt);
                });
    }

    // ---------------------------------------------------------------------------------------------- getTablePrivileges

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and returns bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @return a list of table privileges
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super TablePrivilege>> T getTablePrivileges(
            final String catalog, final String schemaPattern, final String tableNamePattern, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                bind(results, TablePrivilege.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and returns bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @return a list of table privileges
     * @throws SQLException if a database error occurs.
     */
    public List<TablePrivilege> getTablePrivileges(final String catalog, final String schemaPattern,
                                                   final String tableNamePattern)
            throws SQLException {
        return getTablePrivileges(catalog, schemaPattern, tableNamePattern, new ArrayList<>());
    }

    private void getTablePrivileges(final Table table) throws SQLException {
        getTablePrivileges(table.getSchema().getCatalog().getTableCat(),
                           table.getSchema().getTableSchem(),
                           table.getTableName(),
                           table.getTablePrivileges())
                .forEach(p -> {
                    p.setTable(table);
                });
    }

    // --------------------------------------------------------------------------------------------------- getTableTypes

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super TableType>> T getTableTypes(final T collection) throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTableTypes()) {
            if (results != null) {
                bind(results, TableType.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} and returns bound values.
     *
     * @return a list of table types.
     * @throws SQLException if a database error occurs.
     */
    public List<TableType> getTableTypes() throws SQLException {
        return getTableTypes(new ArrayList<>());
    }

    // ------------------------------------------------------------------------------------------------------- getTables

    /**
     * Invokes {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String[])} method with given arguments and adds all bound values to specified collection.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @param collection       the collection to which values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    public <T extends Collection<? super Table>> T getTables(final String catalog, final String schemaPattern,
                                                             final String tableNamePattern, final String[] types,
                                                             final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            if (results != null) {
                bind(results, Table.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String[])} method with given arguments and returns a list of bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @return a list of tables.
     * @throws SQLException if a database error occurs.
     */
    public List<Table> getTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                                 final String[] types)
            throws SQLException {
        return getTables(catalog, schemaPattern, tableNamePattern, types, new ArrayList<>());
    }

    private void getTables(final Schema schema) throws SQLException {
        requireNonNull(schema, "schema is null");
        getTables(schema.getCatalog().getTableCat(), schema.getTableSchem(), null, null, schema.getTables())
                .forEach(t -> {
                    t.setSchema(schema);
                });
        for (final Table table : schema.getTables()) {
            getBestRowIdentifier(table);
        }
        for (final Table table : schema.getTables()) {
            getColumns(table);
        }
        for (final Table table : schema.getTables()) {
            getExportedKeys(table);
        }
        for (final Table table : schema.getTables()) {
            getImportedKeys(table);
        }
        for (final Table table : schema.getTables()) {
            getIndexInfo(table);
        }
        for (final Table table : schema.getTables()) {
            getPrimaryKeys(table);
        }
        for (final Table table : schema.getTables()) {
            getPseudoColumns(table);
        }
        for (final Table table : schema.getTables()) {
            getSuperTables(table);
        }
        for (final Table table : schema.getTables()) {
            getTablePrivileges(table);
        }
        for (final Table table : schema.getTables()) {
            getVersionColumns(table);
        }
    }

    // ----------------------------------------------------------------------------------------------------- getTypeInfo

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @return a list of type info.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super TypeInfo>> T getTypeInfo(final T collection) throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTypeInfo()) {
            if (results != null) {
                bind(results, TypeInfo.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method and returns bound values.
     *
     * @return a list of type info.
     * @throws SQLException if a database error occurs.
     */
    public List<TypeInfo> getTypeInfo() throws SQLException {
        return getTypeInfo(new ArrayList<>());
    }

    // --------------------------------------------------------------------------------------------------------- getUDTs

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])} method with
     * given arguments and adds all bound values to specified collection.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param types           a value for {@code type} parameter
     * @param collection      the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super UDT>> T getUDTs(final String catalog, final String schemaPattern,
                                                         final String typeNamePattern, final int[] types,
                                                         final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
            if (results != null) {
                bind(results, UDT.class, collection);
            }
        }
        return collection;
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
    public List<UDT> getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern,
                             final int[] types)
            throws SQLException {
        return getUDTs(catalog, schemaPattern, typeNamePattern, types, new ArrayList<>());
    }

    private void getUDTs(final Schema schema) throws SQLException {
        requireNonNull(schema, "schema is null");
        getUDTs(schema.getCatalog().getTableCat(),
                schema.getTableSchem(),
                null,
                null,
                schema.getUdts())
                .forEach(t -> {
                    t.setSchema(schema);
                });
        for (final UDT udt : schema.getUdts()) {
            getAttributes(udt);
        }
        for (final UDT udt : schema.getUdts()) {
            getSuperTypes(udt);
        }
    }

    // ----------------------------------------------------------------------------------------------- getVersionColumns

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and returns bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of version columns.
     * @throws SQLException if a database access error occurs.
     */
    public <T extends Collection<? super VersionColumn>> T getVersionColumns(
            final String catalog, final String schema, final String table, final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getVersionColumns(catalog, schema, table)) {
            if (results != null) {
                bind(results, VersionColumn.class, collection);
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and returns bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of version columns.
     * @throws SQLException if a database access error occurs.
     */
    public List<VersionColumn> getVersionColumns(final String catalog, final String schema, final String table)
            throws SQLException {
        return getVersionColumns(catalog, schema, table, new ArrayList<>());
    }

    private void getVersionColumns(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getVersionColumns(table.getSchema().getCatalog().getTableCat(),
                          table.getSchema().getTableSchem(),
                          table.getTableName(),
                          table.getVersionColumns())
                .forEach(c -> {
                    c.setTable(table);
                });
    }

    // ------------------------------------------------------------------------------------------------ databaseMetadata
    @Deprecated
    private DatabaseMetaData getMetaData() {
        return databaseMetaData;
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
    final DatabaseMetaData databaseMetaData;

    // suppression paths
    private Set<String> suppressedPaths;

    // fields with @Bind
    private final transient Map<Class<?>, Map<Field, Bind>> bfields = new HashMap<>();

    // fields with @Invoke
    private final transient Map<Class<?>, Map<Field, Invoke>> ifields = new HashMap<>();

    // parameterized types of java.util.List fields
    private final transient Map<Field, Class<?>> ptypes = new HashMap<>();
}
