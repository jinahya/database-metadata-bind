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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
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
                logger.severe(() -> String.format("unknown label; %s on %s", label, field));
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
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String,
//     * java.lang.String)} method with given arguments and returns bound values.
//     *
//     * @param catalog              a value for {@code catalog} parameter.
//     * @param schemaPattern        a value for {@code schemaPattern} parameter.
//     * @param typeNamePattern      a value for {@code typeNamePattern} parameter.
//     * @param attributeNamePattern a value for {@code attributeNamePattern} parameter.
//     * @return a list of attributes.
//     * @throws SQLException if a database error occurs.
//     * @see #getAttributes(String, String, String, String, Collection)
//     */
//    public @NotNull List<@Valid @NotNull Attribute> getAttributes(final String catalog, final String schemaPattern,
//                                                                  final String typeNamePattern,
//                                                                  final String attributeNamePattern)
//            throws SQLException {
//        return getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern, new ArrayList<>());
//    }

    // -------------------------------------------------------------------------------------------- getBestRowIdentifier

    /**
     * Invokes {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int,
     * boolean)} method with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param scope      a value for {@code scope} parameter.
     * @param nullable   a value for {@code nullable} parameter.
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)
     */
    public <T extends Collection<? super BestRowIdentifier>> @NotNull T getBestRowIdentifier(
            final String catalog, final String schema, final String table, final int scope, final boolean nullable,
            @NotNull final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            if (results != null) {
                bind(results, BestRowIdentifier.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getBestRowIdentifier(%1$s, %2$s, %3$s, %4$d, %5$b",
                                           catalog, schema, table, scope, nullable));
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int,
//     * boolean)} with given arguments and returns bound values.
//     *
//     * @param catalog  the value for {@code catalog} parameter
//     * @param schema   the value for {@code schema} parameter
//     * @param table    the value for {@code table} parameter
//     * @param scope    the value for {@code scope} parameter
//     * @param nullable the value for {@code nullable} parameter
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getBestRowIdentifier(String, String, String, int, boolean, Collection)
//     */
//    public @NotNull List<@Valid @NotNull BestRowIdentifier> getBestRowIdentifier(
//            final String catalog, final String schema, final String table, final int scope, final boolean nullable)
//            throws SQLException {
//        return getBestRowIdentifier(catalog, schema, table, scope, nullable, new ArrayList<>());
//    }

    // ----------------------------------------------------------------------------------------------------- catCatalogs

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method and adds all bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super Catalog>> @NotEmpty T getCatalogs(final @NotNull T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getCatalogs()) {
            if (results != null) {
                bind(results, Catalog.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, "failed to getCatalogs()", sqle);
            throwIfNotSuppressed(sqle);
        }
        if (collection.isEmpty()) {
            collection.add(Catalog.newVirtualInstance());
        }
        for (final Object element : collection) {
            if (element instanceof Catalog) {
                final Catalog catalog = (Catalog) element;
                getSchemas(catalog.getTableCat(), null, catalog.getSchemas());
            }
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getCatalogs()} method and returns bound values.
//     *
//     * @return a list of categories.
//     * @throws SQLException if a database error occurs.
//     * @see #getCatalogs(Collection)
//     */
//    @NotEmpty List<@Valid @NotNull Catalog> getCatalogs() throws SQLException {
//        return getCatalogs(new ArrayList<>());
//    }

    // ----------------------------------------------------------------------------------------- getClientInfoProperties

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public <T extends Collection<? super ClientInfoProperty>> T getClientInfoProperties(final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getClientInfoProperties()) {
            if (results != null) {
                bind(results, ClientInfoProperty.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, "failed to getClientInfoProperties()", sqle);
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and returns bound values.
//     *
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getClientInfoProperties(Collection)
//     */
//    public @NotNull List<@Valid @NotNull ClientInfoProperty> getClientInfoProperties() throws SQLException {
//        return getClientInfoProperties(new ArrayList<>());
//    }

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
     * @param <T>               the type of elements in {@code collection}.
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
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String,
//     * java.lang.String)} method with given arguments and returns bound values.
//     *
//     * @param catalog           a value for {@code catalog} parameter.
//     * @param schema            a value for {@code schema} parameter.
//     * @param table             a value for {@code table} parameter.
//     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getColumnPrivileges(String, String, String, String, Collection)
//     */
//    public List<ColumnPrivilege> getColumnPrivileges(
//            final String catalog, final String schema, final String table, final String columnNamePattern)
//            throws SQLException {
//        return getColumnPrivileges(catalog, schema, table, columnNamePattern, new ArrayList<>());
//    }

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
     * @param <T>               the type of elements in {@code collection}.
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
            throwIfNotSuppressed(sqle);
        }
        for (final Object element : collection) {
            if (element instanceof Column) {
                final Column column = (Column) element;
                getColumnPrivileges(column.getTableCat(), column.getTableSchem(), column.getTableName(),
                                    column.getColumnName(), column.getColumnPrivileges());
            }
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String,
//     * java.lang.String)} method with given arguments and returns bound values.
//     *
//     * @param catalog           a value for {@code catalog} parameter.
//     * @param schemaPattern     a value for {@code schemaPattern} parameter.
//     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
//     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getColumns(String, String, String, String, Collection)
//     */
//    public List<Column> getColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
//                                   final String columnNamePattern)
//            throws SQLException {
//        return getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
//    }

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
     * @param <T>            the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super CrossReference>> T getCrossReference(
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
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String,
//     * java.lang.String, java.lang.String, java.lang.String)} method with given arguments and returns bound values.
//     *
//     * @param parentCatalog  a value for {@code parentCatalog} parameter
//     * @param parentSchema   a value for {@code parentSchema} parameter
//     * @param parentTable    a value for {@code parentTable} parameter
//     * @param foreignCatalog a value for {@code foreignCatalog} parameter
//     * @param foreignSchema  av value for {@code foreignSchema} parameter
//     * @param foreignTable   a value for {@code foreignTable} parameter
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getCrossReferences(String, String, String, String, String, String, Collection)
//     */
//    public List<CrossReference> getCrossReferences(
//            final String parentCatalog, final String parentSchema, final String parentTable,
//            final String foreignCatalog, final String foreignSchema, final String foreignTable)
//            throws SQLException {
//        return getCrossReferences(parentCatalog, parentSchema, parentTable,
//                                  foreignCatalog, foreignSchema, foreignTable,
//                                  new ArrayList<>());
//    }

    // ---------------------------------------------------------------------------------------------------- getFunctions

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and adds bound values to specified collection.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param collection          the collection to which bound values are added.
     * @param <T>                 the type of elements in {@code collection}.
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
            throwIfNotSuppressed(sqle);
        }
        for (final Object element : collection) {
            if (element instanceof Function) {
                final Function function = (Function) element;
                getFunctionColumns(catalog, schemaPattern, function.getFunctionName(), null,
                                   function.getFunctionColumns());
            }
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)} method with
//     * given arguments and returns bound values.
//     *
//     * @param catalog             a value for {@code catalog} parameter.
//     * @param schemaPattern       a value for {@code schemaPattern} parameter.
//     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
//     * @return given {@code collection}.
//     * @throws SQLException if a database error occurs.
//     * @see #getFunctions(String, String, String, Collection)
//     */
//    public @NotNull List<@Valid @NotNull Function> getFunctions(final String catalog, final String schemaPattern,
//                                                                final String functionNamePattern)
//            throws SQLException {
//        return getFunctions(catalog, schemaPattern, functionNamePattern, new ArrayList<>());
//    }

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
     * @param <T>                 the type of elements in {@code collection}.
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

//    /**
//     * Invokes {@link DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String,
//     * java.lang.String)} method with given arguments and returns bound values.
//     *
//     * @param catalog             a value for {@code catalog} parameter.
//     * @param schemaPattern       a value for {@code schemaPattern} parameter.
//     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
//     * @param columnNamePattern   a value for {@code columnNamePattern} parameter.
//     * @return a lsit of bound values.
//     * @throws SQLException if a database access error occurs.
//     * @see #getFunctionColumns(String, String, String, String, Collection)
//     */
//    public @NotNull List<@Valid @NotNull FunctionColumn> getFunctionColumns(
//            final String catalog, final String schemaPattern, final String functionNamePattern,
//            final String columnNamePattern)
//            throws SQLException {
//        return getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern, new ArrayList<>());
//    }

    // ------------------------------------------------------------------------------------------------- getExportedKeys

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter
     * @param schema     a value for {@code schema} parameter
     * @param table      a value for {@code table} parameter
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
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

//    /**
//     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
//     * with given arguments and returns bound values.
//     *
//     * @param catalog a value for {@code catalog} parameter
//     * @param schema  a value for {@code schema} parameter
//     * @param table   a value for {@code table} parameter
//     * @return a list bound values.
//     * @throws SQLException if a database error occurs.
//     */
//    public List<ExportedKey> getExportedKeys(final String catalog, final String schema, final String table)
//            throws SQLException {
//        return getExportedKeys(catalog, schema, table, new ArrayList<>());
//    }

    // ------------------------------------------------------------------------------------------------- getImportedKeys

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
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

//    /**
//     * Invokes {@link DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
//     * with given arguments and returns bound values.
//     *
//     * @param catalog a value for {@code catalog} parameter.
//     * @param schema  a value for {@code schema} parameter.
//     * @param table   a value for {@code table} parameter.
//     * @return given {@code collection}.
//     * @throws SQLException if a database error occurs.
//     * @see #getImportedKeys(String, String, String, Collection)
//     */
//    public List<ImportedKey> getImportedKeys(final String catalog, final String schema, final String table)
//            throws SQLException {
//        return getImportedKeys(catalog, schema, table, new ArrayList<>());
//    }

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
     * @param <T>         the type of elements in {@code collection}.
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

//    /**
//     * Invokes {@link DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean,
//     * boolean)} method with given arguments and returns bound values.
//     *
//     * @param catalog     a value for {@code catalog} parameter.
//     * @param schema      a value for {@code schema} parameter.
//     * @param table       a value for {@code table} parameter.
//     * @param unique      a value for {@code unique} parameter.
//     * @param approximate a value for {@code approximate} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getIndexInfo(String, String, String, boolean, boolean, Collection)
//     */
//    public List<IndexInfo> getIndexInfo(final String catalog, final String schema, final String table,
//                                        final boolean unique, final boolean approximate)
//            throws SQLException {
//        return getIndexInfo(catalog, schema, table, unique, approximate, new ArrayList<>());
//    }

    // -------------------------------------------------------------------------------------------------- getPrimaryKeys

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPrimaryKeys(String, String, String)
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
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)} method with
//     * given arguments and returns bound values.
//     *
//     * @param catalog a value for {@code catalog} parameter.
//     * @param schema  a value for {@code schema} parameter.
//     * @param table   a value for {@code table} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getPrimaryKeys(String, String, String, Collection)
//     */
//    public List<PrimaryKey> getPrimaryKeys(final String catalog, final String schema, final String table)
//            throws SQLException {
//        return getPrimaryKeys(catalog, schema, table, new ArrayList<>());
//    }

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
     * @param <T>                  the type of elements in {@code collection}.
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

//    /**
//     * Invokes {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String,
//     * java.lang.String)} method with given arguments and returns bound values.
//     *
//     * @param catalog              a value for {@code catalog} parameter.
//     * @param schemaPattern        a value for {@code schemaPattern} parameter.
//     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
//     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     */
//    public List<ProcedureColumn> getProcedureColumns(
//            final String catalog, final String schemaPattern, final String procedureNamePattern,
//            final String columnNamePattern)
//            throws SQLException {
//        return getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern, new ArrayList<>());
//    }

    // --------------------------------------------------------------------------------------------------- getProcedures

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments and adds bounds values to specified collection.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <T>                  the type of elements in {@code collection}.
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
        for (final Object element : collection) {
            if (element instanceof Procedure) {
                final Procedure procedure = (Procedure) element;
                getProcedureColumns(catalog, schemaPattern, procedure.getProcedureName(), null,
                                    procedure.getProcedureColumns());
            }
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method with
//     * given arguments and returns bound values.
//     *
//     * @param catalog              a value for {@code catalog} parameter.
//     * @param schemaPattern        a value for {@code schemaPattern} parameter.
//     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
//     * @return given {@code collection}.
//     * @throws SQLException if a database error occurs.
//     */
//    public List<Procedure> getProcedures(final String catalog, final String schemaPattern,
//                                         final String procedureNamePattern)
//            throws SQLException {
//        return getProcedures(catalog, schemaPattern, procedureNamePattern, new ArrayList<>());
//    }

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
     * @param <T>               the type of elements in {@code collection}.
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
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String,
//     * java.lang.String)} method with given arguments and returns bound values.
//     *
//     * @param catalog           a value for {@code catalog} parameter.
//     * @param schemaPattern     a value for {@code schemaPattern} parameter.
//     * @param tableNamePattern  a value for {@code tableNamePattern} parameter.
//     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getPseudoColumns(String, String, String, String, Collection)
//     */
//    public List<PseudoColumn> getPseudoColumns(
//            final String catalog, final String schemaPattern, final String tableNamePattern,
//            final String columnNamePattern)
//            throws SQLException {
//        return getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
//    }

    // ------------------------------------------------------------------------------------------------------ getSchemas

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
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

//    /**
//     * Invokes {@link DatabaseMetaData#getSchemas()} method and returns bound values.
//     *
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getSchemas(Collection)
//     */
//    public List<SchemaName> getSchemas() throws SQLException {
//        return getSchemas(new ArrayList<>());
//    }

    // ------------------------------------------------------------------------------------------------------ getSchemas

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method with given arguments and adds bounds values to
     * specified collection.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @param collection    the collection to which bound values are added.
     * @param <T>           the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas(String, String)
     */
    public <T extends Collection<? super Schema>> @NotEmpty T getSchemas(
            final String catalog, final String schemaPattern, @NotNull final T collection)
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
            throwIfNotSuppressed(sqle);
        }
        if (collection.isEmpty()) {
            collection.add(Schema.newVirtualInstance(null));
        }
        for (final Object element : collection) {
            if (element instanceof Schema) {
                final Schema schema = (Schema) element;
                final String schemaPattern_ = schema.isVirtual() ? "" : schema.getTableSchem();
                getFunctions(catalog, schemaPattern_, null, schema.getFunctions());
                getProcedures(catalog, schemaPattern_, null, schema.getProcedures());
                getTables(catalog, schemaPattern_, null, null, schema.getTables());
                getUDTs(catalog, schemaPattern_, null, null, schema.getUDTs());
            }
        }
        return collection;
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
     * @param <T>              the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSuperTables(String, String, String)
     */
    public <T extends Collection<? super SuperTable>> @NotNull T getSuperTables(
            final String catalog, final String schemaPattern, final String tableNamePattern,
            final @NotNull T collection)
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

//    /**
//     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method with given arguments and returns
//     * bound values.
//     *
//     * @param catalog          a value for {@code catalog}.
//     * @param schemaPattern    a value for {@code schemaPattern}.
//     * @param tableNamePattern a value for {@code tableNamePattern}.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getSuperTables(String, String, String, Collection)
//     */
//    public @NotNull List<@Valid @NotNull SuperTable> getSuperTables(final String catalog, final String schemaPattern,
//                                                                    final String tableNamePattern)
//            throws SQLException {
//        return getSuperTables(catalog, schemaPattern, tableNamePattern, new ArrayList<>());
//    }

    // --------------------------------------------------------------------------------------------------- getSuperTypes

    /**
     * Invokes {@link DatabaseMetaData#getSuperTypes(String, String, String)} method with given arguments and adds bound
     * values to specified collection.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter.
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param collection      the collection to which bound values are added.
     * @param <T>             the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super SuperType>> @NotNull T getSuperTypes(
            final String catalog, final String schemaPattern, final String typeNamePattern, @NotNull final T collection)
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
            throwIfNotSuppressed(sqle);
        }
        return collection;
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
     * @param <T>              the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super TablePrivilege>> @NotNull T getTablePrivileges(
            final String catalog, final String schemaPattern, final String tableNamePattern,
            @NotNull final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                bind(results, TablePrivilege.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getTablePrivileges(%1$s, %2$s, %3$s)",
                                           catalog, schemaPattern, tableNamePattern));
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method
//     * with given arguments and returns bound values.
//     *
//     * @param catalog          a value for {@code catalog} parameter.
//     * @param schemaPattern    a value for {@code schemaPattern} parameter.
//     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getTablePrivileges(String, String, String, Collection)
//     */
//    public @NotNull List<@Valid @NotNull TablePrivilege> getTablePrivileges(
//            final String catalog, final String schemaPattern, final String tableNamePattern)
//            throws SQLException {
//        return getTablePrivileges(catalog, schemaPattern, tableNamePattern, new ArrayList<>());
//    }

    // --------------------------------------------------------------------------------------------------- getTableTypes

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
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

//    /**
//     * Invokes {@link DatabaseMetaData#getTableTypes()} method and returns bound values.
//     *
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     */
//    public List<TableType> getTableTypes() throws SQLException {
//        return getTableTypes(new ArrayList<>());
//    }

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
     * @param <T>              the type of elements in {@code collection}.
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
            throwIfNotSuppressed(sqle);
        }
        for (final Object element : collection) {
            if (element instanceof Table) {
                final Table table = (Table) element;
                for (final BestRowIdentifier.Scope scope : BestRowIdentifier.Scope.values()) {
                    getBestRowIdentifier(table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                         scope.getRawValue(), true, table.getBestRowIdentifiers());
                }
                getColumns(table.getTableCat(), table.getTableSchem(), table.getTableName(), null, table.getColumns());
                getExportedKeys(table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                table.getExportedKeys());
                getImportedKeys(table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                table.getImportedKeys());
                getIndexInfo(table.getTableCat(), table.getTableSchem(), table.getTableName(), true, true,
                             table.getIndexInfo());
                getPrimaryKeys(table.getTableCat(), table.getTableSchem(), table.getTableName(),
                               table.getPrimaryKeys());
                getPseudoColumns(table.getTableCat(), table.getTableSchem(), table.getTableName(), null,
                                 table.getPseudoColumns());
                getSuperTables(table.getTableCat(), table.getTableSchem(), table.getTableName(),
                               table.getSuperTables());
                getTablePrivileges(table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                   table.getTablePrivileges());
                getVersionColumns(table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                  table.getVersionColumns());
            }
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String,
//     * java.lang.String[])} method with given arguments and returns bound values.
//     *
//     * @param catalog          a value for {@code catalog} parameter.
//     * @param schemaPattern    a value for {@code schemaPattern} parameter.
//     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
//     * @param types            a value for {@code types} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     * @see #getSuperTables(String, String, String, Collection)
//     */
//    public List<Table> getTables(final String catalog, final String schemaPattern, final String tableNamePattern,
//                                 final String[] types)
//            throws SQLException {
//        return getTables(catalog, schemaPattern, tableNamePattern, types, new ArrayList<>());
//    }

    // ----------------------------------------------------------------------------------------------------- getTypeInfo

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
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

//    /**
//     * Invokes {@link DatabaseMetaData#getTypeInfo()} method and returns bound values.
//     *
//     * @return a list of bound values.
//     * @throws SQLException if a database error occurs.
//     */
//    public List<TypeInfo> getTypeInfo() throws SQLException {
//        return getTypeInfo(new ArrayList<>());
//    }

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
     * @param <T>             the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <T extends Collection<? super UDT>> @NotNull T getUDTs(
            final String catalog, final String schemaPattern, final String typeNamePattern, final int[] types,
            @NotNull final T collection)
            throws SQLException {
        try (ResultSet results = databaseMetaData.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
            if (results != null) {
                bind(results, UDT.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getUDTs(%1$s, %2$s, %3$s, %4$s)",
                                           catalog, schemaPattern, typeNamePattern, Arrays.toString(types)));
            throwIfNotSuppressed(sqle);
        }
        for (final Object element : collection) {
            final UDT udt = (UDT) element;
            getAttributes(udt.getTypeCat(), udt.getTypeSchem(), udt.getTypeName(), null, udt.getAttributes());
            getSuperTypes(udt.getTypeCat(), udt.getTypeSchem(), udt.getTypeName(), udt.getSuperTypes());
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])} method with
//     * given arguments and returns bound values.
//     *
//     * @param catalog         a value for {@code catalog} parameter.
//     * @param schemaPattern   a value for {@code schemaPattern} parameter
//     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
//     * @param types           a value for {@code type} parameter
//     * @return given {@code collection}.
//     * @throws SQLException if a database error occurs.
//     */
//    public List<UDT> getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern,
//                             final int[] types)
//            throws SQLException {
//        return getUDTs(catalog, schemaPattern, typeNamePattern, types, new ArrayList<>());
//    }

    // ----------------------------------------------------------------------------------------------- getVersionColumns

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <T>        the type of elements in {@code collection}.
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     */
    public <T extends Collection<? super VersionColumn>> @NotNull T getVersionColumns(
            final String catalog, final String schema, final String table, @NotNull final T collection)
            throws SQLException {
        requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getVersionColumns(catalog, schema, table)) {
            if (results != null) {
                bind(results, VersionColumn.class, collection);
            }
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to getVersionColumns(%1$s, %2$s, %3$s)", catalog, schema, table));
            throwIfNotSuppressed(sqle);
        }
        return collection;
    }

//    /**
//     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method
//     * with given arguments and returns bound values.
//     *
//     * @param catalog a value for {@code catalog} parameter.
//     * @param schema  a value for {@code schema} parameter.
//     * @param table   a value for {@code table} parameter.
//     * @return a list of bound values.
//     * @throws SQLException if a database access error occurs.
//     * @see #getVersionColumns(String, String, String, Collection)
//     */
//    public List<VersionColumn> getVersionColumns(final String catalog, final String schema, final String table)
//            throws SQLException {
//        return getVersionColumns(catalog, schema, table, new ArrayList<>());
//    }

    // ---------------------------------------------------------------------------------------------- deletesAreDetected

    /**
     * Invokes {@link DatabaseMetaData#deletesAreDetected(int)} with specified argument and returns a bound value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} may be {@code null} when the thrown {@link SQLException} has been
     * suppressed.
     * @throws SQLException if a database access error occurs.
     */
    public @Valid @NotNull DeletesAreDetected deletesAreDetected(final int type) throws SQLException {
        final DeletesAreDetected value = new DeletesAreDetected();
        value.setType(type);
        try {
            value.setTypeName(ResultSetType.valueOfRawValue(value.getType()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning("unknown type: " + type);
        }
        try {
            value.setValue(databaseMetaData.deletesAreDetected(value.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke deletesAreDetected(%1$d)", value.getType()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    @Valid @NotNull DeletesAreDetected deletesAreDetected(final ResultSetType type) throws SQLException {
        return deletesAreDetected(type.getRawValue());
    }

    // ---------------------------------------------------------------------------------------------- insertsAreDetected

    /**
     * Invokes {@link DatabaseMetaData#insertsAreDetected(int)} with specified argument and returns a bound value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} may be {@code null} when the thrown {@link SQLException} has been
     * suppressed.
     * @throws SQLException if a database access error occurs.
     */
    public @Valid @NotNull InsertsAreDetected insertsAreDetected(final int type) throws SQLException {
        final InsertsAreDetected value = new InsertsAreDetected();
        value.setType(type);
        try {
            value.setTypeName(ResultSetType.valueOfRawValue(value.getType()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning(() -> "unknown type: " + type);
        }
        try {
            value.setValue(databaseMetaData.insertsAreDetected(value.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke insertsAreDetected(%1$d)", value.getType()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    @Valid @NotNull InsertsAreDetected insertsAreDetected(final ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        return insertsAreDetected(type.getRawValue());
    }

    // ---------------------------------------------------------------------------------------------- updatesAreDetected

    /**
     * Invokes {@link DatabaseMetaData#updatesAreDetected(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     */
    public @Valid @NotNull UpdatesAreDetected updatesAreDetected(final int type) throws SQLException {
        final UpdatesAreDetected value = new UpdatesAreDetected();
        value.setType(type);
        value.setTypeName(ResultSetType.valueOfRawValue(value.getType()).name());
        try {
            value.setValue(databaseMetaData.updatesAreDetected(value.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke updatesAreDetected(%1$d)", value.getType()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    @Valid @NotNull UpdatesAreDetected updatesAreDetected(final @NotNull ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        return updatesAreDetected(type.getRawValue());
    }

    // ----------------------------------------------------------------------------------------- othersDeletesAreVisible
    @Valid @NotNull OthersDeletesAreVisible othersDeletesAreVisible(final ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        final OthersDeletesAreVisible result = new OthersDeletesAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersDeletesAreVisible(result.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke othersDeletesAreVisible(%1$d)", result.getType()));
            throwIfNotSuppressed(sqle);
        }
        return result;
    }

    /**
     * Invokes {@link DatabaseMetaData#othersDeletesAreVisible(int)} method with specified value and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#othersDeletesAreVisible(int)
     * @see ResultSet#TYPE_FORWARD_ONLY
     * @see ResultSet#TYPE_SCROLL_INSENSITIVE
     * @see ResultSet#TYPE_SCROLL_SENSITIVE
     */
    public @Valid @NotNull OthersDeletesAreVisible othersDeletesAreVisible(final int type) throws SQLException {
        return othersDeletesAreVisible(ResultSetType.valueOfRawValue(type));
    }

    // ----------------------------------------------------------------------------------------- othersInsertsAreVisible
    @Valid @NotNull OthersInsertsAreVisible othersInsertsAreVisible(final ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        final OthersInsertsAreVisible result = new OthersInsertsAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersInsertsAreVisible(result.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke othersInsertsAreVisible(%1$d)", result.getType()));
            throwIfNotSuppressed(sqle);
        }
        return result;
    }

    /**
     * Invokes {@link DatabaseMetaData#othersInsertsAreVisible(int)} method with specified value and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#othersInsertsAreVisible(int)
     * @see ResultSet#TYPE_FORWARD_ONLY
     * @see ResultSet#TYPE_SCROLL_INSENSITIVE
     * @see ResultSet#TYPE_SCROLL_SENSITIVE
     */
    public @Valid @NotNull OthersInsertsAreVisible othersInsertsAreVisible(final int type) throws SQLException {
        return othersInsertsAreVisible(ResultSetType.valueOfRawValue(type));
    }

    // ----------------------------------------------------------------------------------------- othersUpdatesAreVisible
    @Valid @NotNull OthersUpdatesAreVisible othersUpdatesAreVisible(final ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        final OthersUpdatesAreVisible result = new OthersUpdatesAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersUpdatesAreVisible(result.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke othersUpdatesAreVisible(%1$d)", result.getType()));
            throwIfNotSuppressed(sqle);
        }
        return result;
    }

    /**
     * Invokes {@link DatabaseMetaData#othersUpdatesAreVisible(int)} method with specified value and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#othersUpdatesAreVisible(int)
     * @see ResultSet#TYPE_FORWARD_ONLY
     * @see ResultSet#TYPE_SCROLL_INSENSITIVE
     * @see ResultSet#TYPE_SCROLL_SENSITIVE
     */
    public @Valid @NotNull OthersUpdatesAreVisible othersUpdatesAreVisible(final int type) throws SQLException {
        return othersUpdatesAreVisible(ResultSetType.valueOfRawValue(type));
    }

    // -------------------------------------------------------------------------------------------- ownDeletesAreVisible
    @Valid @NotNull OwnDeletesAreVisible ownDeletesAreVisible(final ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        final OwnDeletesAreVisible value = new OwnDeletesAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownDeletesAreVisible(value.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke ownDeletesAreVisible(%1$d)", value.getType()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#ownDeletesAreVisible(int)} method with specified value and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#ownDeletesAreVisible(int)
     * @see ResultSet#TYPE_FORWARD_ONLY
     * @see ResultSet#TYPE_SCROLL_INSENSITIVE
     * @see ResultSet#TYPE_SCROLL_SENSITIVE
     */
    public @Valid @NotNull OwnDeletesAreVisible ownDeletesAreVisible(final int type) throws SQLException {
        return ownDeletesAreVisible(ResultSetType.valueOfRawValue(type));
    }

    // -------------------------------------------------------------------------------------------- ownInsertsAreVisible
    @Valid @NotNull OwnInsertsAreVisible ownInsertsAreVisible(final ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        final OwnInsertsAreVisible value = new OwnInsertsAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownInsertsAreVisible(value.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke ownInsertsAreVisible(%1$d)", value.getType()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#ownInsertsAreVisible(int)} method with specified value and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#ownInsertsAreVisible(int)
     * @see ResultSet#TYPE_FORWARD_ONLY
     * @see ResultSet#TYPE_SCROLL_INSENSITIVE
     * @see ResultSet#TYPE_SCROLL_SENSITIVE
     */
    public @Valid @NotNull OwnInsertsAreVisible ownInsertsAreVisible(final int type) throws SQLException {
        return ownInsertsAreVisible(ResultSetType.valueOfRawValue(type));
    }

    // -------------------------------------------------------------------------------------------- ownUpdatesAreVisible
    @Valid @NotNull OwnUpdatesAreVisible ownUpdatesAreVisible(final ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        final OwnUpdatesAreVisible value = new OwnUpdatesAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownUpdatesAreVisible(value.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke ownUpdatesAreVisible(%1$d)", value.getType()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#ownUpdatesAreVisible(int)} method with  specified type and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#ownUpdatesAreVisible(int)
     * @see ResultSet#TYPE_FORWARD_ONLY
     * @see ResultSet#TYPE_SCROLL_INSENSITIVE
     * @see ResultSet#TYPE_SCROLL_SENSITIVE
     */
    public @Valid @NotNull OwnUpdatesAreVisible ownUpdatesAreVisible(final int type) throws SQLException {
        return ownUpdatesAreVisible(ResultSetType.valueOfRawValue(type));
    }

    // ------------------------------------------------------------------------------------------------- supportsConvert

    /**
     * Invokes {@link DatabaseMetaData#supportsConvert(int, int)} method with given arguments and returns a bound value.
     * The result may have {@code null} {@code value} when the {@link SQLException} has been {@link #suppress(Class)
     * suppressed}.
     *
     * @param fromType a value for {@code fromType} parameter.
     * @param toType   a value for {@code toType} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsConvert(int, int)
     */
    public SupportsConvert supportsConvert(final int fromType, final int toType) throws SQLException {
        final SupportsConvert value = new SupportsConvert();
        value.setFromType(fromType);
        try {
            value.setFromTypeName(JDBCType.valueOf(value.getFromType()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning("unknown fromType: " + fromType);
        }
        value.setToType(toType);
        try {
            value.setToTypeName(JDBCType.valueOf(value.getToType()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning("unknown toType: " + toType);
        }
        try {
            value.setValue(databaseMetaData.supportsConvert(value.getFromType(), value.getToType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke supportsConvert(%1$d, %2$d)", value.getFromType(),
                                           value.getToType()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    @NotNull SupportsConvert supportsConvert(final @NotNull JDBCType fromType, final @NotNull JDBCType toType)
            throws SQLException {
        requireNonNull(fromType, "fromType is null");
        requireNonNull(toType, "toType is null");
        return supportsConvert(fromType.getVendorTypeNumber(), toType.getVendorTypeNumber());
    }

    // ------------------------------------------------------------------------------------ supportsResultSetConcurrency

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetConcurrency(int, int)} method with given arguments and returns a
     * bound value. The result may have {@code null} {@code value} when the {@link SQLException} has been {@link
     * #suppress(Class) suppressed}.
     *
     * @param type        a value for {@code type} parameter.
     * @param concurrency a value for {@code concurrency} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsResultSetConcurrency(int, int)
     */
    public SupportsResultSetConcurrency supportsResultSetConcurrency(final int type, final int concurrency)
            throws SQLException {
        final SupportsResultSetConcurrency value = new SupportsResultSetConcurrency();
        value.setType(type);
        try {
            value.setTypeName(JDBCType.valueOf(value.getType()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning("unknown type: " + type);
        }
        value.setConcurrency(concurrency);
        try {
            value.setConcurrencyName(ResultSetConcurrency.valueOfRawValue(value.getConcurrency()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning("unknown concurrency: " + concurrency);
        }
        try {
            value.setValue(databaseMetaData.supportsResultSetConcurrency(value.getType(), value.getConcurrency()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke supportsResultSetConcurrency(%1$d, %2$d)",
                                           value.getType(), value.getConcurrency()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    @NotNull SupportsResultSetConcurrency supportsResultSetConcurrency(final @NotNull ResultSetType type,
                                                                       final @NotNull ResultSetConcurrency concurrency)
            throws SQLException {
        requireNonNull(type, "type is null");
        requireNonNull(concurrency, "concurrency is null");
        return supportsResultSetConcurrency(type.getRawValue(), concurrency.getRawValue());
    }

    // ------------------------------------------------------------------------------------ supportsResultSetHoldability

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetHoldability(int)} method with given argument and returns a bound
     * value. The result may have {@code null} {@code value} when the {@link SQLException} has been {@link
     * #suppress(Class) suppressed}.
     *
     * @param holdability a value for {@code holdability} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsResultSetHoldability(int)
     */
    public SupportsResultSetHoldability supportsResultSetHoldability(final int holdability)
            throws SQLException {
        final SupportsResultSetHoldability value = new SupportsResultSetHoldability();
        value.setHoldability(holdability);
        try {
            value.setHoldabilityName(ResultSetHoldability.valueOfRawValue(value.getHoldability()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning("unknown holdability: " + holdability);
        }
        try {
            value.setValue(databaseMetaData.supportsResultSetHoldability(value.getHoldability()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke supportsResultSetHoldability(%1$d)",
                                           value.getHoldability()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    @NotNull SupportsResultSetHoldability supportsResultSetHoldability(final @NotNull ResultSetHoldability holdability)
            throws SQLException {
        requireNonNull(holdability, "holdability is null");
        return supportsResultSetHoldability(holdability.getRawValue());
    }

    // ------------------------------------------------------------------------------------ supportsResultSetType

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetType(int)} method with given argument and returns a bound value.
     * The result may have {@code null} {@code value} when the {@link SQLException} has been {@link #suppress(Class)
     * suppressed}.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsResultSetType(int)
     */
    public SupportsResultSetType supportsResultSetType(final int type) throws SQLException {
        final SupportsResultSetType value = new SupportsResultSetType();
        value.setType(type);
        try {
            value.setTypeName(ResultSetType.valueOfRawValue(value.getType()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning("unknown holdability: " + type);
        }
        try {
            value.setValue(databaseMetaData.supportsResultSetType(value.getType()));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke supportsResultSetType(%1$d)", value.getType()));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    @NotNull SupportsResultSetType supportsResultSetType(final @NotNull ResultSetType type) throws SQLException {
        requireNonNull(type, "type is null");
        return supportsResultSetType(type.getRawValue());
    }

    // ------------------------------------------------------------------------------- supportsTransactionIsolationLevel

    /**
     * Invokes {@link DatabaseMetaData#supportsTransactionIsolationLevel(int)} method with given argument and returns a
     * bound value. The result may have {@code null} {@code value} when the {@link SQLException} has been {@link
     * #suppress(Class) suppressed}.
     *
     * @param level a value for {@code level} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsTransactionIsolationLevel(int)
     */
    public SupportsTransactionIsolationLevel supportsTransactionIsolationLevel(final int level)
            throws SQLException {
        final SupportsTransactionIsolationLevel value = new SupportsTransactionIsolationLevel();
        value.setLevel(level);
        try {
            value.setLevelName(ConnectionTransactionIsolationLevel.valueOfRawValue(value.getLevel()).name());
        } catch (final IllegalArgumentException iae) {
            logger.warning("unknown lavel: " + level);
        }
        try {
            value.setValue(databaseMetaData.supportsTransactionIsolationLevel(level));
        } catch (final SQLException sqle) {
            logger.log(Level.WARNING, sqle,
                       () -> String.format("failed to invoke supportsTransactionIsolationLevel(%1$d)", level));
            throwIfNotSuppressed(sqle);
        }
        return value;
    }

    @NotNull SupportsTransactionIsolationLevel supportsTransactionIsolationLevel(
            final @NotNull ConnectionTransactionIsolationLevel level)
            throws SQLException {
        requireNonNull(level, "level is null");
        return supportsTransactionIsolationLevel(level.getRawValue());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public <T extends SQLException> Context suppress(final Class<T> exceptionClass) {
        suppressedExceptionClasses.add(requireNonNull(exceptionClass, "exceptionClass is null"));
        return this;
    }

    private <T extends SQLException> boolean isSuppressed(final Class<T> exceptionClass) {
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
