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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 * A context class for retrieving information from an instance of {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Context {

    private static final Logger logger = getLogger(Context.class.getName());

    /**
     * Creates a new instance from specified connection.
     *
     * @param connection the connection.
     * @return a new instance.
     * @throws SQLException if a database error occurs.
     */
    public static Context newInstance(final Connection connection) throws SQLException {
        requireNonNull(connection, "connection is null");
        if (connection.isClosed()) {
            throw new IllegalArgumentException("connection is closed");
        }
        return new Context(connection.getMetaData());
    }

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance with specified database meta date.
     *
     * @param metadata the database meta data to hold.
     */
    Context(final DatabaseMetaData metadata) {
        super();
        this.databaseMetaData = requireNonNull(metadata, "metadata is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Binds given value from specified result set.
     *
     * @param <T>      value type parameter
     * @param results  the result set from which the value is bound.
     * @param type     the type of the value.
     * @param instance the value.
     * @return given {@code value}.
     * @throws SQLException if a database error occurs.
     */
    private <T extends MetadataType> T bind(final ResultSet results, final Class<T> type, final T instance)
            throws SQLException {
        final Set<String> resultSetLabels = Utils.getLabels(results);
        for (final Entry<Field, Label> labeledField : getLabeledFields(type).entrySet()) {
            final Field field = labeledField.getKey();
            final Label label = labeledField.getValue();
            if (!resultSetLabels.remove(label.value())) {
                logger.warning(() -> String.format("unknown label; %s on %s", label, field));
                continue;
            }
            if (field.getAnnotation(Unused.class) != null) {
                continue;
            }
            if (field.getAnnotation(Reserved.class) != null) {
                continue;
            }
            try {
                Utils.setFieldValue(field, instance, results, label.value());
            } catch (final ReflectiveOperationException roe) {
                logger.log(SEVERE, String.format("failed to set %s", field), roe);
            }
        }
        if (logger.isLoggable(Level.FINE)) {
            for (final String remainedLabel : resultSetLabels) {
                final Object value = results.getObject(remainedLabel);
                logger.fine(() -> String.format("remained result for %s; label: %s, value: %s",
                                                type, remainedLabel, value));
            }
        }
        return instance;
    }

    /**
     * Binds all records as given type and adds them to specified collection.
     *
     * @param <T>        binding type parameter
     * @param results    the records to bind.
     * @param type       the type of instances.
     * @param collection the collection to which bound instances are added
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    private <T extends MetadataType, C extends Collection<@Valid @NotNull ? super T>> @NotNull C bind(
            @NotNull final ResultSet results, @NotNull final Class<T> type, @NotNull final C collection)
            throws SQLException {
        requireNonNull(results, "results is null");
        requireNonNull(type, "type is null");
        requireNonNull(collection, "collection is null");
        while (results.next()) {
            final T value;
            try {
                value = type.getConstructor().newInstance();
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException("failed to instantiate " + type, roe);
            }
            collection.add(bind(results, type, value));
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
     * @param <T>                  element type parameter of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getAttributes(String, String, String, String)
     */
    public <T extends Collection<? super Attribute>> @NotNull T getAttributes(
            final String catalog, final String schemaPattern, final String typeNamePattern,
            final String attributeNamePattern, @NotNull final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getAttributes(
                catalog, schemaPattern, typeNamePattern, attributeNamePattern)) {
            if (results != null) {
                bind(results, Attribute.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getAttributes(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schemaPattern, typeNamePattern, attributeNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
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
     * @see #getAttributes(String, String, String, String, Collection)
     */
    public @NotNull List<@Valid @NotNull Attribute> getAttributes(final String catalog, final String schemaPattern,
                                                                  final String typeNamePattern,
                                                                  final String attributeNamePattern)
            throws SQLException {
        return getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern, new ArrayList<>());
    }

    private void getAttributes(final UDT udt) throws SQLException {
        requireNonNull(udt, "udt is null");
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
     * boolean)} method with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter
     * @param schema     a value for {@code schema} parameter
     * @param table      a value for {@code table} parameter
     * @param scope      a value for {@code scope} parameter
     * @param nullable   a value for {@code nullable} parameter
     * @param collection the collection to which bound values are added.
     * @param <T>        collection element type parameter
     * @return given {@code collection}.
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getBestRowIdentifier(%1$s, %2$s, %3$s, %4$d, %5$b",
                                           catalog, schema, table, scope, nullable));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int,
     * boolean)} with given arguments and returns bound values.
     *
     * @param catalog  the value for {@code catalog} parameter
     * @param schema   the value for {@code schema} parameter
     * @param table    the value for {@code table} parameter
     * @param scope    the value for {@code scope} parameter
     * @param nullable the value for {@code nullable} parameter
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getBestRowIdentifier(String, String, String, int, boolean, Collection)
     */
    public List<BestRowIdentifier> getBestRowIdentifier(final String catalog, final String schema, final String table,
                                                        final int scope, final boolean nullable)
            throws SQLException {
        return getBestRowIdentifier(catalog, schema, table, scope, nullable, new ArrayList<>());
    }

    private void getBestRowIdentifier(@NotNull final Table table) throws SQLException {
        for (final BestRowIdentifier.Scope scope : BestRowIdentifier.Scope.values()) {
            getBestRowIdentifier(table.getSchema().getCatalog().getTableCat(),
                                 table.getSchema().getTableSchem(),
                                 table.getTableName(),
                                 scope.getRawValue(),
                                 true,
                                 table.getBestRowIdentifiers())
                    .forEach(i -> {
                        i.setTable(table);
                    });
        }
    }

    // ----------------------------------------------------------------------------------------------------- catCatalogs

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method and adds all bound entities to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <T>        element type parameter of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super Catalog>> @NotNull T getCatalogs(@NotNull final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getCatalogs()) {
            if (results != null) {
                bind(results, Catalog.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, "failed to getCatalogs()", sqle);
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method and returns bound values.
     *
     * @return a list of categories.
     * @throws SQLException if a database error occurs.
     * @see #getCatalogs(Collection)
     */
    public List<Catalog> getCatalogs() throws SQLException {
        return getCatalogs(new ArrayList<>());
    }

    // ----------------------------------------------------------------------------------------- getClientInfoProperties

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <T>        element type parameter of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public <T extends Collection<? super ClientInfoProperty>> T getClientInfoProperties(final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getClientInfoProperties()) {
            if (results != null) {
                bind(results, ClientInfoProperty.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, "failed to getClientInfoProperties()", sqle);
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and returns bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getClientInfoProperties(Collection)
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
     * @param collection        the collection to which bound values are added.
     * @param <T>               element type parameter of the {@code collection}
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getColumnPrivileges(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schema, table, columnNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
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
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getColumnPrivileges(String, String, String, String, Collection)
     */
    public List<ColumnPrivilege> getColumnPrivileges(
            final String catalog, final String schema, final String table, final String columnNamePattern)
            throws SQLException {
        return getColumnPrivileges(catalog, schema, table, columnNamePattern, new ArrayList<>());
    }

    private void getColumnPrivileges(final Column column) throws SQLException {
        requireNonNull(column, "column is null");
        getColumnPrivileges(column.getParent().getParent().getParent().getTableCat(),
                            column.getParent().getParent().getTableSchem(),
                            column.getParent().getTableName(),
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
     * @param collection        the collection to which bound values are added.
     * @param <T>               element type parameter of the {@code collection}
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getColumns(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schemaPattern, tableNamePattern, columnNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
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
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getColumns(String, String, String, String, Collection)
     */
    public List<Column> getColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                                   final String columnNamePattern)
            throws SQLException {
        return getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
    }

    private void getColumns(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getColumns(table.getParent().getParent().getTableCat(),
                   table.getParent().getTableSchem(),
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
     * java.lang.String, java.lang.String, java.lang.String)} method with given arguments and adds bound values to
     * specified collection.
     *
     * @param parentCatalog  a value for {@code parentCatalog} parameter
     * @param parentSchema   a value for {@code parentSchema} parameter
     * @param parentTable    a value for {@code parentTable} parameter
     * @param foreignCatalog a value for {@code foreignCatalog} parameter
     * @param foreignSchema  av value for {@code foreignSchema} parameter
     * @param foreignTable   a value for {@code foreignTable} parameter
     * @param collection     the collection to which bound values are added.
     * @param <T>            {@code collection}'s element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super CrossReference>> T getCrossReferences(
            final String parentCatalog, final String parentSchema, final String parentTable,
            final String foreignCatalog, final String foreignSchema, final String foreignTable,
            final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getCrossReference(
                parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable)) {
            if (results != null) {
                bind(results, CrossReference.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getCrossReferences(%1$s, %2$s, %3$s, %4$s, %5$s, %6$s)",
                                           parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema,
                                           foreignTable));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)} method with given arguments and returns bound values.
     *
     * @param parentCatalog  a value for {@code parentCatalog} parameter
     * @param parentSchema   a value for {@code parentSchema} parameter
     * @param parentTable    a value for {@code parentTable} parameter
     * @param foreignCatalog a value for {@code foreignCatalog} parameter
     * @param foreignSchema  av value for {@code foreignSchema} parameter
     * @param foreignTable   a value for {@code foreignTable} parameter
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getCrossReferences(String, String, String, String, String, String, Collection)
     */
    public List<CrossReference> getCrossReferences(
            final String parentCatalog, final String parentSchema, final String parentTable,
            final String foreignCatalog, final String foreignSchema, final String foreignTable)
            throws SQLException {
        return getCrossReferences(parentCatalog, parentSchema, parentTable,
                                  foreignCatalog, foreignSchema, foreignTable,
                                  new ArrayList<>());
    }

    // ---------------------------------------------------------------------------------------------------- getFunctions

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and adds bound values to specified collection.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param collection          the collection to which bound values are added.
     * @param <T>                 {@code collection}'s element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     */
    public <T extends Collection<@Valid @NotNull ? super Function>> @NotNull T getFunctions(
            final String catalog, final String schemaPattern, final String functionNamePattern,
            @NotNull final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getFunctions(catalog, schemaPattern, functionNamePattern)) {
            if (results != null) {
                bind(results, Function.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getFunctions(%1$s, %2$s, %3$s)",
                                           catalog, schemaPattern, functionNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and returns bound values.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getFunctions(String, String, String, Collection)
     */
    public @NotNull List<@Valid @NotNull Function> getFunctions(final String catalog, final String schemaPattern,
                                                                final String functionNamePattern)
            throws SQLException {
        return getFunctions(catalog, schemaPattern, functionNamePattern, new ArrayList<>());
    }

    private void getFunctions(@Valid @NotNull final Schema schema) throws SQLException {
        requireNonNull(schema, "schema is null");
        getFunctions(schema.getCatalog().getTableCat(),
                     schema.getTableSchem(),
                     null,
                     schema.getFunctions())
                .forEach(f -> {
                    f.setSchema(schema);
                });
        for (final Function function : schema.getFunctions()) {
            getFunctionColumns(function);
        }
    }

    // --------------------------------------------------------------------------------------------- getFunctionsColumns

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and adds bound values to specified collection.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for {@code columnNamePattern} parameter.
     * @param collection          the collection to which bound values are added.
     * @param <T>                 {@code collection}'s element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctionColumns(String, String, String, String)
     */
    public <T extends Collection<@Valid @NotNull ? super FunctionColumn>> @NotNull T getFunctionColumns(
            final String catalog, final String schemaPattern, final String functionNamePattern,
            final String columnNamePattern, @NotNull final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getFunctionColumns(
                catalog, schemaPattern, functionNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, FunctionColumn.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getFunctionColumns(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schemaPattern, functionNamePattern, columnNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and returns bound values.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for {@code columnNamePattern} parameter.
     * @return a lsit of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getFunctionColumns(String, String, String, String, Collection)
     */
    public @NotNull List<@Valid @NotNull FunctionColumn> getFunctionColumns(
            final String catalog, final String schemaPattern, final String functionNamePattern,
            final String columnNamePattern)
            throws SQLException {
        return getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern, new ArrayList<>());
    }

    private void getFunctionColumns(@NotNull final Function function) throws SQLException {
        requireNonNull(function, "function is null");
        getFunctionColumns(function.getParent().getParent().getTableCat(),
                           function.getParent().getTableSchem(),
                           function.getFunctionName(),
                           null,
                           function.getFunctionColumns())
                .forEach(c -> {
                    c.setFunction(function);
                });
    }

    // ------------------------------------------------------------------------------------------------- getExportedKeys

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter
     * @param schema     a value for {@code schema} parameter
     * @param table      a value for {@code table} parameter
     * @param collection the collection to which bound values are added.
     * @param <T>        {@code collection}'s element type parameter
     * @return given {@code collection}.
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getExportedKeys(%1$s, %2$s, %3$s)", catalog, schema, table));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and returns bound values.
     *
     * @param catalog a value for {@code catalog} parameter
     * @param schema  a value for {@code schema} parameter
     * @param table   a value for {@code table} parameter
     * @return a list bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ExportedKey> getExportedKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        return getExportedKeys(catalog, schema, table, new ArrayList<>());
    }

    private void getExportedKeys(final Table table) throws SQLException {
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
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <T>        {@code collection}'s element type parameter
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getImportedKeys(%1$s, %2$s, %3$s)", catalog, schema, table));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and returns bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getImportedKeys(String, String, String, Collection)
     */
    public List<ImportedKey> getImportedKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        return getImportedKeys(catalog, schema, table, new ArrayList<>());
    }

    private void getImportedKeys(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getImportedKeys(table.getSchema().getCatalog().getTableCat(),
                        table.getSchema().getTableSchem(),
                        table.getTableName(),
                        table.getImportedKeys())
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
     * @param <T>         {@code collection}'s element type parameter
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getIndexInfo(%1$s, %2$s, %3$s)", catalog, schema, table));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean,
     * boolean)} method with given arguments and returns bound values.
     *
     * @param catalog     a value for {@code catalog} parameter.
     * @param schema      a value for {@code schema} parameter.
     * @param table       a value for {@code table} parameter.
     * @param unique      a value for {@code unique} parameter.
     * @param approximate a value for {@code approximate} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getIndexInfo(String, String, String, boolean, boolean, Collection)
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
     * @param <T>        {@code collection}'s element type parameter
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getPrimaryKeys(%1$s, %2$s, %3$s)", catalog, schema, table));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    private void getPrimaryKeys(final Table table) throws SQLException {
        getPrimaryKeys(table.getSchema().getCatalog().getTableCat(),
                       table.getSchema().getTableSchem(),
                       table.getTableName(),
                       table.getPrimaryKeys())
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
     * @param collection           the collection to which bound values are added.
     * @param <T>                  {@code collection}'s element type parameter
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getProcedureColumns(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schemaPattern, procedureNamePattern, columnNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and returns bound values.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ProcedureColumn> getProcedureColumns(
            final String catalog, final String schemaPattern, final String procedureNamePattern,
            final String columnNamePattern)
            throws SQLException {
        return getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern, new ArrayList<>());
    }

    private void getProcedureColumns(final Procedure procedure) throws SQLException {
        requireNonNull(procedure, "procedure is null");
        getProcedureColumns(procedure.getSchema().getCatalog().getTableCat(),
                            procedure.getSchema().getTableSchem(),
                            procedure.getProcedureName(),
                            null,
                            procedure.getProcedureColumns())
                .forEach(c -> {
                    c.setProcedure(procedure);
                });
    }

    // --------------------------------------------------------------------------------------------------- getProcedures

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and adds bounds values to specified collection.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <T>                  {@code collection}'s element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super Procedure>> T getProcedures(final String catalog, final String schemaPattern,
                                                                     final String procedureNamePattern,
                                                                     final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            if (results != null) {
                bind(results, Procedure.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getProcedures(%1$s, %2$s, %3$s)",
                                           catalog, schemaPattern, procedureNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and returns bound values.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public List<Procedure> getProcedures(final String catalog, final String schemaPattern,
                                         final String procedureNamePattern)
            throws SQLException {
        return getProcedures(catalog, schemaPattern, procedureNamePattern, new ArrayList<>());
    }

    private void getProcedures(final Schema schema) throws SQLException {
        requireNonNull(schema, "schema is null");
        getProcedures(schema.getCatalog().getTableCat(),
                      schema.getTableSchem(),
                      null,
                      schema.getProcedures())
                .forEach(p -> {
                    p.setSchema(schema);
                });
        for (final Procedure procedure : schema.getProcedures()) {
            getProcedureColumns(procedure);
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
     * @param <T>               {@code collection}'s element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super PseudoColumn>> T getPseudoColumns(
            final String catalog, final String schemaPattern, final String tableNamePattern,
            final String columnNamePattern, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getPseudoColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, PseudoColumn.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getPseudoColumns(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schemaPattern, tableNamePattern, columnNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
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
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getPseudoColumns(String, String, String, String, Collection)
     */
    public List<PseudoColumn> getPseudoColumns(
            final String catalog, final String schemaPattern, final String tableNamePattern,
            final String columnNamePattern)
            throws SQLException {
        return getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
    }

    private void getPseudoColumns(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
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
     * Invokes {@link DatabaseMetaData#getSchemas()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <T>        {@code collection}'s element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas()
     */
    public <T extends Collection<? super SchemaName>> T getSchemas(final T collection) throws SQLException {
        try (ResultSet results = databaseMetaData.getSchemas()) {
            if (results != null) {
                bind(results, SchemaName.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, "failed to getSchemas()", sqle);
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method and returns bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getSchemas(Collection)
     */
    public List<SchemaName> getSchemas() throws SQLException {
        return getSchemas(new ArrayList<>());
    }

    // ------------------------------------------------------------------------------------------------------ getSchemas

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method with given arguments and adds bounds values to
     * specified collection.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @param collection    the collection to which bound values are added.
     * @param <T>           {@code collection}'s element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas(String, String)
     */
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getSchemas(%1$s, %2$s)", catalog, schemaPattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method with given arguments and returns bound
     * values.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getSchemas(String, String, Collection)
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

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method with given arguments and adds
     * bounds values to specified collection.
     *
     * @param catalog          a value for {@code catalog}.
     * @param schemaPattern    a value for {@code schemaPattern}.
     * @param tableNamePattern a value for {@code tableNamePattern}.
     * @param collection       the collection to which bound values are added.
     * @param <T>              {@code collection}'s element type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSuperTables(String, String, String)
     */
    public <T extends Collection<? super SuperTable>> T getSuperTables(final String catalog, final String schemaPattern,
                                                                       final String tableNamePattern,
                                                                       final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                bind(results, SuperTable.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getSuperTables(%1$s, %2$s, %3$s)",
                                           catalog, schemaPattern, tableNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method with given arguments and returns
     * bound values.
     *
     * @param catalog          a value for {@code catalog}.
     * @param schemaPattern    a value for {@code schemaPattern}.
     * @param tableNamePattern a value for {@code tableNamePattern}.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTables(String, String, String, Collection)
     */
    public List<SuperTable> getSuperTables(final String catalog, final String schemaPattern,
                                           final String tableNamePattern)
            throws SQLException {
        return getSuperTables(catalog, schemaPattern, tableNamePattern, new ArrayList<>());
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

    /**
     * Invokes {@link DatabaseMetaData#getSuperTypes(String, String, String)} method with given arguments and adds bound
     * values to specified collection.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter.
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param collection      the collection to which bound values are added.
     * @param <T>             element type parameter of the {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super SuperType>> T getSuperTypes(final String catalog, final String schemaPattern,
                                                                     final String typeNamePattern,
                                                                     final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
            if (results != null) {
                bind(results, SuperType.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getSuperTypes(%1$s, %2$s, %3$s)",
                                           catalog, schemaPattern, typeNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    private void getSuperTypes(final UDT udt) throws SQLException {
        requireNonNull(udt, "udt is null");
        getSuperTypes(ofNullable(udt.getSchema()).map(Schema::getCatalog).map(Catalog::getTableCat).orElse(null),
                      ofNullable(udt.getSchema()).map(Schema::getTableSchem).orElse(null),
                      udt.getTypeName(),
                      udt.getSuperTypes())
                .forEach(t -> {
                    t.setUDT(udt);
                });
    }

    // ---------------------------------------------------------------------------------------------- getTablePrivileges

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param collection       the collection to which bound values are added.
     * @param <T>              element type parameter of the {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super TablePrivilege>> T getTablePrivileges(
            final String catalog, final String schemaPattern, final String tableNamePattern, final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                bind(results, TablePrivilege.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getTablePrivileges(%1$s, %2$s, %3$s)",
                                           catalog, schemaPattern, tableNamePattern));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
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
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getTablePrivileges(String, String, String, Collection)
     */
    public List<TablePrivilege> getTablePrivileges(final String catalog, final String schemaPattern,
                                                   final String tableNamePattern)
            throws SQLException {
        return getTablePrivileges(catalog, schemaPattern, tableNamePattern, new ArrayList<>());
    }

    private void getTablePrivileges(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getTablePrivileges(table.getParent().getParent().getTableCat(),
                           Optional.ofNullable(table.getSchema()).map(Schema::getTableSchem).orElse(null),
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
     * @param <T>        element type parameter of the {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super TableType>> T getTableTypes(final T collection) throws SQLException {
        requireNonNull(collection, "collection is null");
        try {
            try (ResultSet results = databaseMetaData.getTableTypes()) {
                if (results != null) {
                    bind(results, TableType.class, collection);
                }
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, "failed to getTableTypes()", sqle);
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method and returns bound values.
     *
     * @return a list of bound values.
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
     * @param <T>              element type parameter of the {@code collection}.
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getTables(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schemaPattern, tableNamePattern, Arrays.toString(types)));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String[])} method with given arguments and returns bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTables(String, String, String, Collection)
     */
    public List<Table> getTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                                 final String[] types)
            throws SQLException {
        return getTables(catalog, schemaPattern, tableNamePattern, types, new ArrayList<>());
    }

    private void getTables(final Schema schema) throws SQLException {
        requireNonNull(schema, "schema is null");
        getTables(schema.getParent().getTableCat(),
                  schema.getTableSchem(),
                  null,
                  null,
                  schema.getTables())
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
     * @param <T>        element type parameter of the {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super TypeInfo>> T getTypeInfo(final T collection) throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTypeInfo()) {
            if (results != null) {
                bind(results, TypeInfo.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, "failed to getTypeInfo()", sqle);
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method and returns bound values.
     *
     * @return a list of bound values.
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
     * @param <T>             element type parameter of the {@code collection}.
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getUDTs(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schemaPattern, typeNamePattern, Arrays.toString(types)));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
            }
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])} method with
     * given arguments and returns bound values.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param types           a value for {@code type} parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public List<UDT> getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern,
                             final int[] types)
            throws SQLException {
        return getUDTs(catalog, schemaPattern, typeNamePattern, types, new ArrayList<>());
    }

    private void getUDTs(final Schema schema) throws SQLException {
        requireNonNull(schema, "schema is null");
        getUDTs(schema.getParent().getTableCat(),
                schema.getTableSchem(),
                null,
                null,
                schema.getUDTs())
                .forEach(t -> {
                    t.setSchema(schema);
                });
        for (final UDT udt : schema.getUDTs()) {
            getAttributes(udt);
        }
        for (final UDT udt : schema.getUDTs()) {
            getSuperTypes(udt);
        }
    }

    // ----------------------------------------------------------------------------------------------- getVersionColumns

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <T>        element type parameter of the {@code collection}
     * @return given {@code collection}.
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
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getVersionColumns(%1$s, %2$s, %3$s)", catalog, schema, table));
            if (!isSuppressed(sqle.getClass())) {
                throw sqle;
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
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     * @see #getVersionColumns(String, String, String, Collection)
     */
    public List<VersionColumn> getVersionColumns(final String catalog, final String schema, final String table)
            throws SQLException {
        return getVersionColumns(catalog, schema, table, new ArrayList<>());
    }

    private void getVersionColumns(final Table table) throws SQLException {
        requireNonNull(table, "table is null");
        getVersionColumns(ofNullable(table.getSchema()).map(Schema::getCatalog).map(Catalog::getTableCat).orElse(null),
                          ofNullable(table.getSchema()).map(Schema::getTableSchem).orElse(null),
                          table.getTableName(),
                          table.getVersionColumns())
                .forEach(c -> {
                    c.setTable(table);
                });
    }

    // -----------------------------------------------------------------------------------------------------------------
    public <T extends SQLException> Context suppress(final Class<T> exceptionClass) {
        suppressedExceptionClasses.add(requireNonNull(exceptionClass, "exceptionClass is null"));
        return this;
    }

    <T extends SQLException> boolean isSuppressed(final Class<T> exceptionClass) {
        if (suppressedExceptionClasses.contains(requireNonNull(exceptionClass, "exceptionClass is null"))) {
            return true;
        }
        for (final Class<?> suppressedExceptionClass : suppressedExceptionClasses) {
            if (suppressedExceptionClass.isAssignableFrom(exceptionClass)) {
                return true;
            }
        }
        return false;
    }

    boolean isSuppressed(final SQLException sqle) {
        return isSuppressed(sqle.getClass());
    }

    void throwIfNotSuppressed(final SQLException sqle) throws SQLException {
        requireNonNull(sqle, "sqle is null");
        if (!isSuppressed(sqle)) {
            throw sqle;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Map<Field, Label> getLabeledFields(final Class<?> clazz) {
        requireNonNull(clazz, "clazz is null");
        return classesAndLabeledFields.computeIfAbsent(clazz, c -> Utils.getFieldsAnnotatedWith(c, Label.class));
    }

    // -----------------------------------------------------------------------------------------------------------------
    final DatabaseMetaData databaseMetaData;

    // -----------------------------------------------------------------------------------------------------------------
    private final Map<Class<?>, Map<Field, Label>> classesAndLabeledFields = new HashMap<>();

    private final Set<Class<?>> suppressedExceptionClasses = new HashSet<>();
}
