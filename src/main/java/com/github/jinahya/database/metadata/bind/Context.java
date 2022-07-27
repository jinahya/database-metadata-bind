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

import javax.validation.constraints.NotNull;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for retrieving information from an instance of {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Context {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Creates a new instance from specified connection.
     *
     * @param connection the connection.
     * @return a new instance.
     * @throws SQLException if a database error occurs.
     */
    public static Context newInstance(final Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "connection is null");
        if (connection.isClosed()) {
            throw new IllegalArgumentException("connection is closed");
        }
        return new Context(connection.getMetaData());
    }

    /**
     * Creates a new instance with specified database meta date.
     *
     * @param databaseMetaData the database databaseMetaData to hold.
     */
    Context(final DatabaseMetaData databaseMetaData) {
        super();
        this.databaseMetaData = Objects.requireNonNull(databaseMetaData, "databaseMetaData is null");
    }

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
                logger.warning(() -> String.format("unknown label; %1$s on %2$s", label, field));
                continue;
            }
            if (field.isAnnotationPresent(NotUsedBySpecification.class)) {
                continue;
            }
            if (field.isAnnotationPresent(Reserved.class)) {
                continue;
            }
            try {
                Utils.setFieldValue(field, instance, results, label.value());
            } catch (final ReflectiveOperationException roe) {
                logger.log(Level.SEVERE, String.format("failed to set %1$s", field), roe);
            }
        }
        if (logger.isLoggable(Level.FINE)) {
            for (final String l : resultSetLabels) {
                final Object v = results.getObject(l);
                logger.fine(() -> String.format("remained result; type: %1$s; label: %2$s, value: %3$s", type, l, v));
            }
        }
        return instance;
    }

    /**
     * Binds all records as given type and adds them to specified collection.
     *
     * @param results    the records to bind.
     * @param type       the type of instances.
     * @param collection the collection to which bound instances are added
     * @param <T>        binding type parameter
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    private <T extends MetadataType, C extends Collection<? super T>> C bind(final ResultSet results,
                                                                             final Class<T> type, final C collection)
            throws SQLException {
        Objects.requireNonNull(results, "results is null");
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(collection, "collection is null");
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

    /**
     * Invokes
     * {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method with given arguments and adds bound values to specified collection.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for {@code attributeNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <C>                  the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getAttributes(String, String, String, String)
     */
    public <C extends Collection<? super Attribute>> C getAttributes(final String catalog, final String schemaPattern,
                                                                     final String typeNamePattern,
                                                                     final String attributeNamePattern,
                                                                     final C collection)
            throws SQLException {
        Objects.requireNonNull(typeNamePattern, "typeNamePattern is null");
        Objects.requireNonNull(attributeNamePattern, "attributeNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getAttributes(
                catalog, schemaPattern, typeNamePattern, attributeNamePattern)) {
            if (results == null) {
                logger.warning(() -> String.format("null returned from getAttributes(%1$s, %2$s, %3$s, %4$s)",
                                                   catalog, schemaPattern, typeNamePattern, attributeNamePattern));
                return collection;
            }
            bind(results, Attribute.class, collection);
        } catch (final SQLException sqle) {
            logger.log(Level.SEVERE,
                       String.format("failed to getAttributes(%1$s, %2$s, %3$s, %4$s)",
                                     catalog, schemaPattern, typeNamePattern, attributeNamePattern),
                       sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves attributes of specified user-defined type.
     *
     * @param udt                  the user-defined type whose attribute are retrieved.
     * @param attributeNamePattern a value for {@code attributeNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <C>                  the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @apiNote This method invokes {@link #getAttributes(String, String, String, String, Collection)} method with
     * {@link UDT#getTypeCat() udt.typeCat}, {@link UDT#getTypeSchem() udt.typeSchem},
     * {@link UDT#getTypeName() udt.typeName}, {@code attributePattern}, and {@code collection} and returns the result.
     * @see #getAttributes(String, String, String, String, Collection)
     */
    public <C extends Collection<? super Attribute>> C getAttributes(final UDT udt, final String attributeNamePattern,
                                                                     final C collection)
            throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        return getAttributes(
                udt.getTypeCat(),
                udt.getTypeSchem(),
                Objects.requireNonNull(udt.getTypeName(), "udt.typeName is null"),
                attributeNamePattern,
                collection
        );
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)}
     * method with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param scope      a value for {@code scope} parameter.
     * @param nullable   a value for {@code nullable} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)
     * @see BestRowIdentifier.Scope
     */
    public <C extends Collection<? super BestRowIdentifier>> C getBestRowIdentifier(
            final String catalog, final String schema, final String table, final int scope, final boolean nullable,
            final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            if (results != null) {
                bind(results, BestRowIdentifier.class, collection);
            }
        } catch (final SQLException sqle) {
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves best row identifiers for specified table and adds bound values to specified collection.
     *
     * @param table      the table whose best row identifiers are retrieved.
     * @param scope      a value for {@code scope} parameter.
     * @param nullable   a value for {@code nullable} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @apiNote This method invokes {@link #getBestRowIdentifier(String, String, String, int, boolean, Collection)}
     * method with {@link Table#getTableCat() table.tableCat}, {@link Table#getTableSchem() table.tableSchem},
     * {@link Table#getTableName() table.tableName}, {@code scope}, {@code nullable}, and {@code collection} and returns
     * the result.
     * @see #getBestRowIdentifier(String, String, String, int, boolean, Collection)
     */
    public <C extends Collection<? super BestRowIdentifier>> C getBestRowIdentifier(
            final Table table, final int scope, final boolean nullable, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getBestRowIdentifier(
                table.getTableCat(),
                table.getTableSchem(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null"),
                scope,
                nullable,
                collection
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method and adds all bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super Catalog>> C getCatalogs(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getCatalogs()) {
            if (results != null) {
                bind(results, Catalog.class, collection);
            }
        } catch (final SQLException sqle) {
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    // ----------------------------------------------------------------------------------------- getClientInfoProperties

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public <C extends Collection<? super ClientInfoProperty>> C getClientInfoProperties(final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getClientInfoProperties()) {
            if (results != null) {
                bind(results, ClientInfoProperty.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getClientInfoProperties()", sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    // --------------------------------------------------------------------------------------------- getColumnPrivileges

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments and adds bound values to specified collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schema            a value for {@code schema} parameter.
     * @param table             a value for {@code table} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    public <C extends Collection<? super ColumnPrivilege>> C getColumnPrivileges(
            final String catalog, final String schema, final String table, final String columnNamePattern,
            final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            if (results != null) {
                bind(results, ColumnPrivilege.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getColumnPrivileges({}, {}, {}, {})", catalog, schema, table, columnNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves column privileges of specified table and adds bound values to specified collection.
     *
     * @param table             the table whose column privileges are retrieved.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @apiNote This method invokes {@link #getColumnPrivileges(String, String, String, String, Collection)} method with
     * {@link Table#getTableCat() table.tableCat}, {@link Table#getTableSchem() table.tableSchem},
     * {@link Table#getTableName() table.tableName}, {@code columnNamePattern}, and {@code collection} and returns the
     * result.
     * @see #getColumnPrivileges(String, String, String, String, Collection)
     */
    public <C extends Collection<? super ColumnPrivilege>> C getColumnPrivileges(
            final Table table, final String columnNamePattern, final C collection)
            throws SQLException {
        return getColumnPrivileges(table.getTableCat(), table.getTableSchem(), table.getTableName(), columnNamePattern,
                                   collection);
    }

    // ------------------------------------------------------------------------------------------------------ getColumns

    /**
     * Retrieves columns for given arguments and adds values to specified collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    public <C extends Collection<? super Column>> C getColumns(final String catalog, final String schemaPattern,
                                                               final String tableNamePattern,
                                                               final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, Column.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getColumns({}, {}, {}, {})",
//                    catalog, schemaPattern, tableNamePattern, columnNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves columns of specified table and adds bound values to specified collection.
     *
     * @param table             the table whose columns are retrieved.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @apiNote This method invokes {@link #getColumns(String, String, String, String, Collection) getColumns} method
     * with ({@link Table#getTableCat() table.tableCat}, {@link Table#getTableSchem() table.tableSchem},
     * {@link Table#getTableName() table.tableName}, {@code columnNamePattern}, {@code collection}) and returns the
     * result.
     * @see #getColumns(String, String, String, String, Collection)
     */
    public <C extends Collection<? super Column>> C getColumns(final Table table, final String columnNamePattern,
                                                               final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getColumns(table.getTableCat(), table.getTableSchem(), table.getTableName(), columnNamePattern,
                          collection);
    }

    // ----------------------------------------------------------------------------------------------- getCrossReference

    /**
     * Invokes
     * {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)} method with given arguments and adds bound values to specified collection.
     *
     * @param parentCatalog  a value for {@code parentCatalog} parameter
     * @param parentSchema   a value for {@code parentSchema} parameter
     * @param parentTable    a value for {@code parentTable} parameter
     * @param foreignCatalog a value for {@code foreignCatalog} parameter
     * @param foreignSchema  av value for {@code foreignSchema} parameter
     * @param foreignTable   a value for {@code foreignTable} parameter
     * @param collection     the collection to which bound values are added.
     * @param <C>            the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super CrossReference>> C getCrossReference(
            final String parentCatalog, final String parentSchema, final String parentTable,
            final String foreignCatalog, final String foreignSchema, final String foreignTable, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getCrossReference(
                parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable)) {
            if (results != null) {
                bind(results, CrossReference.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getCrossReferences({}, {}, {}, {}, {}, {})",
//                    parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves cross references of specified parent table and foreign table and adds bound values to specified
     * collection.
     *
     * @param parentTable  the parent table.
     * @param foreignTable the foreign table.
     * @param collection   the collection to which bound values are added.
     * @param <C>          the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @apiNote This method invokes
     * {@link #getCrossReference(String, String, String, String, String, String, Collection)} with
     * {@link Table#getTableCat() parentTable.tableCat}, {@link Table#getTableSchem() parentTable.tableSchem},
     * {@link Table#getTableName() parentTable.tableName} parentTable.tableName}
     * {@link Table#getTableCat() foreignTable.tableCat}, {@link Table#getTableSchem() foreignTable.tableSchem},
     * {@link Table#getTableName() foreignTable.tableName} foreignTable.tableName}, and {@code collection} and returns
     * the result.
     * @see #getCrossReference(String, String, String, String, String, String, Collection)
     */
    public <C extends Collection<? super CrossReference>> C getCrossReference(
            final Table parentTable, final Table foreignTable, final C collection)
            throws SQLException {
        Objects.requireNonNull(parentTable, "parentTable is null");
        Objects.requireNonNull(foreignTable, "foreignTable is null");
        return getCrossReference(parentTable.getTableCat(), parentTable.getTableSchem(), parentTable.getTableName(),
                                 foreignTable.getTableCat(), foreignTable.getTableSchem(), foreignTable.getTableName(),
                                 collection);
    }

    // ---------------------------------------------------------------------------------------------------- getFunctions

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(String, String, String)} method with given arguments and adds bound
     * values to specified collection.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param collection          the collection to which bound values are added.
     * @param <C>                 the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     * @see #getFunctionColumns(String, String, String, String, Collection)
     */
    public <C extends Collection<? super Function>> C getFunctions(
            final String catalog, final String schemaPattern, final String functionNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getFunctions(catalog, schemaPattern, functionNamePattern)) {
            if (results != null) {
                bind(results, Function.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getFunctions({}, {}, {})", catalog, schemaPattern, functionNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves functions of specified schema and adds bound values to specified collection.
     *
     * @param schema              the schema whose functions are retrieved.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param collection          the collection to which bound values are added.
     * @param <C>                 the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getFunctions(String, String, String, Collection)
     */
    public <C extends Collection<? super Function>> C getFunctions(final Schema schema,
                                                                   final String functionNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getFunctions(schema.getTableCatalog(), schema.getTableSchem(), functionNamePattern, collection);
    }

    // --------------------------------------------------------------------------------------------- getFunctionsColumns

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method with specified
     * arguments and adds bound values to specified collection.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for {@code columnNamePattern} parameter.
     * @param collection          the collection to which bound values are added.
     * @param <C>                 the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctionColumns(String, String, String, String)
     */
    public <C extends Collection<? super FunctionColumn>> C getFunctionColumns(
            final String catalog, final String schemaPattern, final String functionNamePattern,
            final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getFunctionColumns(
                catalog, schemaPattern, functionNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, FunctionColumn.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getFunctionColumns({}, {}, {}, {})",
//                    catalog, schemaPattern, functionNamePattern, columnNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves function columns with specified arguments and adds bound values to specified collection.
     *
     * @param function          the function whose function columns are retrieved.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getFunctionColumns(String, String, String, String, Collection)
     */
    public <C extends Collection<? super FunctionColumn>> C getFunctionColumns(final Function function,
                                                                               final String columnNamePattern,
                                                                               final C collection)
            throws SQLException {
        Objects.requireNonNull(function, "function is null");
        return getFunctionColumns(function.getFunctionCat(), function.getFunctionSchem(), function.getFunctionName(),
                                  columnNamePattern, collection);
    }

    // ------------------------------------------------------------------------------------------------- getExportedKeys

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super ExportedKey>> C getExportedKeys(
            final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getExportedKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, ExportedKey.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getExportedKeys({}, {}, {})", catalog, schema, table, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves exported keys of specified table and adds bound values to specified collection.
     *
     * @param table      tht table whose exported keys are retrieved.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getExportedKeys(String, String, String, Collection)
     */
    public <C extends Collection<? super ExportedKey>> C getExportedKeys(final Table table, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getExportedKeys(table.getTableCat(), table.getTableSchem(), table.getTableName(), collection);
    }

    // ------------------------------------------------------------------------------------------------- getImportedKeys

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(String, String, String)} method with given arguments and adds
     * bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getImportedKeys(String, String, String)
     */
    public <C extends Collection<? super ImportedKey>> C getImportedKeys(
            final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getImportedKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, ImportedKey.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getImportedKeys({}, {}, {})", catalog, schema, table, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves imported keys of specified table and adds bound values to specified collection.
     *
     * @param table      the table whose imported keys are retrieved.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getImportedKeys(String, String, String, Collection)
     */
    public <C extends Collection<? super ImportedKey>> C getImportedKeys(final Table table, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getImportedKeys(table.getTableCat(), table.getTableSchem(), table.getTableName(), collection);
    }

    // ---------------------------------------------------------------------------------------------------- getIndexInfo

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)} method with specified
     * arguments and adds bound values to specified collection.
     *
     * @param catalog     a value for {@code catalog} parameter.
     * @param schema      a value for {@code schema} parameter.
     * @param table       a value for {@code table} parameter.
     * @param unique      a value for {@code unique} parameter.
     * @param approximate a value for {@code approximate} parameter.
     * @param collection  the collection to which bound values are added.
     * @param <C>         the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    public <C extends Collection<? super IndexInfo>> C getIndexInfo(
            final String catalog, final String schema, final String table, final boolean unique,
            final boolean approximate, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getIndexInfo(catalog, schema, table, unique, approximate)) {
            if (results != null) {
                bind(results, IndexInfo.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getIndexInfo({}, {}, {})", catalog, schema, table, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves index info of specified table and adds bound values to specified collection.
     *
     * @param table       the table whose index info is retrieved.
     * @param unique      a value for {@code unique} parameter.
     * @param approximate a value for {@code approximate} parameter.
     * @param collection  the collection to which bound values are added.
     * @param <C>         the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getIndexInfo(String, String, String, boolean, boolean, Collection)
     */
    public <C extends Collection<? super IndexInfo>> C getIndexInfo(final Table table, final boolean unique,
                                                                    final boolean approximate, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getIndexInfo(table.getTableCat(), table.getTableSchem(), table.getTableName(), unique, approximate,
                            collection);
    }

    // -------------------------------------------------------------------------------------------------- getPrimaryKeys

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method with given arguments and adds
     * bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    public <C extends Collection<? super PrimaryKey>> C getPrimaryKeys(
            final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getPrimaryKeys(catalog, schema, table)) {
            if (results != null) {
                bind(results, PrimaryKey.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getPrimaryKeys({}, {}, {})", catalog, schema, table, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves primary keys of specified table adds bound values to specified collection.
     *
     * @param table      the table whose primary keys are retrieved.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getPrimaryKeys(String, String, String, Collection)
     */
    public <C extends Collection<? super PrimaryKey>> C getPrimaryKeys(final Table table, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPrimaryKeys(table.getTableCat(), table.getTableSchem(), table.getTableName(), collection);
    }

    // --------------------------------------------------------------------------------------------- getProcedureColumns

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)} method with given arguments
     * and adds values to specified collection.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <C>                  the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedureColumns(String, String, String, String)
     */
    public <C extends Collection<? super ProcedureColumn>> C getProcedureColumns(
            final String catalog, final String schemaPattern, final String procedureNamePattern,
            final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getProcedureColumns(
                catalog, schemaPattern, procedureNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, ProcedureColumn.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getProcedureColumns({}, {}, {}, {})",
//                    catalog, schemaPattern, procedureNamePattern, columnNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves procedure columns of specified procedure and adds values to specified collection.
     *
     * @param procedure         the procedure whose procedure columns are retrieved.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getProcedureColumns(String, String, String, String, Collection)
     */
    public <C extends Collection<? super ProcedureColumn>> C getProcedureColumns(
            final Procedure procedure, final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(procedure, "procedure is null");
        return getProcedureColumns(procedure.getProcedureCat(), procedure.getProcedureSchem(),
                                   procedure.getProcedureName(), columnNamePattern, collection);
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
     * @param <C>                  the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedures(String, String, String)
     * @see #getProcedureColumns(Procedure, String, Collection)
     */
    public <C extends Collection<? super Procedure>> C getProcedures(final String catalog, final String schemaPattern,
                                                                     final String procedureNamePattern,
                                                                     final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            if (results != null) {
                bind(results, Procedure.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getProcedures({}, {}, {})", catalog, schemaPattern, procedureNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves procedures of specified schema and adds bounds values to specified collection.
     *
     * @param schema               the schema whose procedures are retrieved.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <C>                  the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getProcedures(String, String, String, Collection)
     */
    public <C extends Collection<? super Procedure>> C getProcedures(final Schema schema,
                                                                     final String procedureNamePattern,
                                                                     final C collection)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getProcedures(schema.getTableCatalog(), schema.getTableSchem(), procedureNamePattern, collection);
    }

    // ------------------------------------------------------------------------------------------------ getPseudoColumns

    /**
     * Invokes
     * {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method with given arguments and adds bound values to specified collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPseudoColumns(String, String, String, String)
     */
    public <C extends Collection<? super PseudoColumn>> C getPseudoColumns(
            final String catalog, final String schemaPattern, final String tableNamePattern,
            final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getPseudoColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            if (results != null) {
                bind(results, PseudoColumn.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getPseudoColumns({}, {}, {}, {})",
//                    catalog, schemaPattern, tableNamePattern, columnNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves pseudo columns of specified table and adds bound values to specified collection.
     *
     * @param table             the table whose pseudo columns are retrieved.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPseudoColumns(String, String, String, String)
     */
    public <C extends Collection<? super PseudoColumn>> C getPseudoColumns(
            final Table table, final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPseudoColumns(table.getTableCat(), table.getTableSchem(), table.getTableName(), columnNamePattern,
                                collection);
    }

    // ------------------------------------------------------------------------------------------------------ getSchemas

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas()
     */
    public <C extends Collection<? super SchemaName>> C getSchemas(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSchemas()) {
            if (results != null) {
                bind(results, SchemaName.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getSchemas()", sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    // ------------------------------------------------------------------------------------------------------ getSchemas

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method with given arguments and adds bounds values to
     * specified collection.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @param collection    the collection to which bound values are added.
     * @param <C>           the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas(String, String)
     */
    public <C extends Collection<? super Schema>> C getSchemas(final String catalog, final String schemaPattern,
                                                               final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSchemas(catalog, schemaPattern)) {
            if (results != null) {
                bind(results, Schema.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getSchemas({}, {})", catalog, schemaPattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves schemas of specified catalog and adds bounds values to specified collection.
     *
     * @param catalog       the catalog whose schemas are retrieved.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @param collection    the collection to which bound values are added.
     * @param <C>           the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getSchemas(String, String, Collection)
     */
    public <C extends Collection<? super Schema>> C getSchemas(final Catalog catalog, final String schemaPattern,
                                                               final C collection)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSchemas(catalog.getTableCat(), schemaPattern, collection);
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
     * @param <C>              the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super SuperTable>> C getSuperTables(
            final String catalog, final String schemaPattern, final String tableNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                bind(results, SuperTable.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getSuperTables({}, {}, {})", catalog, schemaPattern, tableNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves super tables of specified table and adds bounds values to specified collection.
     *
     * @param table      the table whose super tables are retrieved.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTables(String, String, String, Collection)
     */
    public <C extends Collection<? super SuperTable>> C getSuperTables(final Table table, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getSuperTables(table.getTableCat(), table.getTableSchem(), table.getTableName(), collection);
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
     * @param <C>             the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super SuperType>> C getSuperTypes(final String catalog, final String schemaPattern,
                                                                     final String typeNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
            if (results != null) {
                bind(results, SuperType.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getSuperTypes({}, {}, {})", catalog, schemaPattern, typeNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves super types of specified user-defined type and adds bound values to specified collection.
     *
     * @param udt        the user-defined type whose supertypes are retrieved.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTypes(String, String, String, Collection)
     */
    public <C extends Collection<? super SuperType>> C getSuperTypes(final UDT udt, final C collection)
            throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        return getSuperTypes(udt.getTypeCat(), udt.getTypeSchem(), udt.getTypeName(), collection);
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
     * @param <C>              the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super TablePrivilege>> C getTablePrivileges(
            final String catalog, final String schemaPattern, final String tableNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                bind(results, TablePrivilege.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getTablePrivileges({}, {}, {})", catalog, schemaPattern, tableNamePattern, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves table privileges of specified table and adds bound values to specified collection.
     *
     * @param table      a value for {@code catalog} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super TablePrivilege>> C getTablePrivileges(final Table table, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getTablePrivileges(table.getTableCat(), table.getTableSchem(), table.getTableName(), collection);
    }

    // --------------------------------------------------------------------------------------------------- getTableTypes

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super TableType>> C getTableTypes(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTableTypes()) {
            if (results != null) {
                bind(results, TableType.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getTableTypes()", sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    // ------------------------------------------------------------------------------------------------------- getTables

    /**
     * Invokes
     * {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
     * method with given arguments and adds all bound values to specified collection.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @param collection       the collection to which values are added.
     * @param <C>              the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    public <C extends Collection<? super Table>> C getTables(final String catalog, final String schemaPattern,
                                                             final String tableNamePattern, final String[] types,
                                                             final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            if (results != null) {
                bind(results, Table.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getTables({}, {}, {}, {})",
//                    catalog, schemaPattern, tableNamePattern, Arrays.toString(types), sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves tables of specified schema and adds all bound values to specified collection.
     *
     * @param schema           the schema whose tables are retrieved.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @param collection       the collection to which values are added.
     * @param <C>              the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getTables(String, String, String, String[], Collection)
     */
    public <C extends Collection<? super Table>> C getTables(final Schema schema, final String tableNamePattern,
                                                             final String[] types, final C collection)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getTables(schema.getTableCatalog(), schema.getTableSchem(), tableNamePattern, types, collection);
    }

    // ----------------------------------------------------------------------------------------------------- getTypeInfo

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super TypeInfo>> C getTypeInfo(final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTypeInfo()) {
            if (results != null) {
                bind(results, TypeInfo.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getTypeInfo()", sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
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
     * @param <C>             the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getUDTs(String, String, String, int[])
     * @see #getAttributes(UDT, String, Collection)
     * @see #getSuperTypes(UDT, Collection)
     */
    public <C extends Collection<? super UDT>> C getUDTs(final String catalog, final String schemaPattern,
                                                         final String typeNamePattern, final int[] types,
                                                         final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
            if (results != null) {
                bind(results, UDT.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getUDTs({}, {}, {}, {})",
//                    catalog, schemaPattern, typeNamePattern, Arrays.toString(types), sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves type info of specified schema and adds all bound values to specified collection.
     *
     * @param schema          the schema whose type info is retrieved.
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param types           a value for {@code type} parameter
     * @param collection      the collection to which bound values are added.
     * @param <C>             the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getUDTs(String, String, String, int[], Collection)
     */
    public <C extends Collection<? super UDT>> C getUDTs(final Schema schema, final String typeNamePattern,
                                                         final int[] types, final C collection)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getUDTs(schema.getTableCatalog(), schema.getTableSchem(), typeNamePattern, types, collection);
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
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     */
    public <C extends Collection<? super VersionColumn>> C getVersionColumns(
            final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getVersionColumns(catalog, schema, table)) {
            if (results != null) {
                bind(results, VersionColumn.class, collection);
            }
        } catch (final SQLException sqle) {
            //log..error("failed to getVersionColumns({}, {}, {})", catalog, schema, table, sqle);
            throwUnlessSuppressed(sqle);
        }
        return collection;
    }

    /**
     * Retrieves version columns of specified table and adds bound values to specified collection.
     *
     * @param table      a value for {@code catalog} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see #getVersionColumns(String, String, String, Collection)
     */
    public <C extends Collection<? super VersionColumn>> C getVersionColumns(final Table table, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getVersionColumns(table.getTableCat(), table.getTableSchem(), table.getTableName(), collection);
    }

    // ---------------------------------------------------------------------------------------------- deletesAreDetected

    /**
     * Invokes {@link DatabaseMetaData#deletesAreDetected(int)} with specified argument and returns a bound value.
     *
     * @param type a value for {@code type} parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see DeletesAreDetected#getAllInstances(Context, Collection)
     */
    public DeletesAreDetected deletesAreDetected(final int type) throws SQLException {
        final DeletesAreDetected value = new DeletesAreDetected();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.deletesAreDetected(value.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke deletesAreDetected({})", value.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // ---------------------------------------------------------------------------------------------- insertsAreDetected

    /**
     * Invokes {@link DatabaseMetaData#insertsAreDetected(int)} with specified argument and returns a bound value.
     *
     * @param type a value for {@code type} parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see InsertsAreDetected#getAllInstances(Context, Collection)
     */
    public InsertsAreDetected insertsAreDetected(final int type) throws SQLException {
        final InsertsAreDetected value = new InsertsAreDetected();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.insertsAreDetected(value.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke insertsAreDetected({})", value.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // ---------------------------------------------------------------------------------------------- updatesAreDetected

    /**
     * Invokes {@link DatabaseMetaData#updatesAreDetected(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see UpdatesAreDetected#getAllInstances(Context, Collection)
     */
    public UpdatesAreDetected updatesAreDetected(final int type) throws SQLException {
        final UpdatesAreDetected value = new UpdatesAreDetected();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.updatesAreDetected(value.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke updatesAreDetected({})", value.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // ----------------------------------------------------------------------------------------- othersDeletesAreVisible

    /**
     * Invokes {@link DatabaseMetaData#othersDeletesAreVisible(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see OthersDeletesAreVisible#getAllInstances(Context, Collection)
     */
    public OthersDeletesAreVisible othersDeletesAreVisible(final int type) throws SQLException {
        final OthersDeletesAreVisible result = new OthersDeletesAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersDeletesAreVisible(result.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke othersDeletesAreVisible({})", result.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return result;
    }

    // ----------------------------------------------------------------------------------------- othersInsertsAreVisible

    /**
     * Invokes {@link DatabaseMetaData#othersInsertsAreVisible(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see OthersInsertsAreVisible#getAllInstances(Context, Collection)
     */
    public OthersInsertsAreVisible othersInsertsAreVisible(final int type) throws SQLException {
        final OthersInsertsAreVisible result = new OthersInsertsAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersInsertsAreVisible(result.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke othersInsertsAreVisible({})", result.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return result;
    }

    // ----------------------------------------------------------------------------------------- othersUpdatesAreVisible

    /**
     * Invokes {@link DatabaseMetaData#othersUpdatesAreVisible(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see OthersUpdatesAreVisible#getAllInstances(Context, Collection)
     */
    public OthersUpdatesAreVisible othersUpdatesAreVisible(final int type) throws SQLException {
        final OthersUpdatesAreVisible result = new OthersUpdatesAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersUpdatesAreVisible(result.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke othersUpdatesAreVisible({})", result.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return result;
    }

    // -------------------------------------------------------------------------------------------- ownDeletesAreVisible

    /**
     * Invokes {@link DatabaseMetaData#ownDeletesAreVisible(int)} method with specified value and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see OwnDeletesAreVisible#getAllInstances(Context, Collection)
     */
    public OwnDeletesAreVisible ownDeletesAreVisible(final int type) throws SQLException {
        final OwnDeletesAreVisible value = new OwnDeletesAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownDeletesAreVisible(value.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke ownDeletesAreVisible({})", value.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // -------------------------------------------------------------------------------------------- ownInsertsAreVisible

    /**
     * Invokes {@link DatabaseMetaData#ownInsertsAreVisible(int)} method with specified value and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see OwnInsertsAreVisible#getAllInstances(Context, Collection)
     */
    public OwnInsertsAreVisible ownInsertsAreVisible(final int type) throws SQLException {
        final OwnInsertsAreVisible value = new OwnInsertsAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownInsertsAreVisible(value.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke ownInsertsAreVisible({})", value.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // -------------------------------------------------------------------------------------------- ownUpdatesAreVisible

    /**
     * Invokes {@link DatabaseMetaData#ownUpdatesAreVisible(int)} method with  specified type and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see OwnUpdatesAreVisible#getAllInstances(Context, Collection)
     */
    public OwnUpdatesAreVisible ownUpdatesAreVisible(final int type) throws SQLException {
        final OwnUpdatesAreVisible value = new OwnUpdatesAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownUpdatesAreVisible(value.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke ownUpdatesAreVisible({})", value.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------- supportsConvert

    /**
     * Invokes {@link DatabaseMetaData#supportsConvert(int, int)} method with given arguments and returns a bound
     * value.
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
        value.setToType(toType);
        try {
            value.setValue(databaseMetaData.supportsConvert(value.getFromType(), value.getToType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke supportsConvert({}, {})", value.getFromType(), value.getToType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // ------------------------------------------------------------------------------------ supportsResultSetConcurrency

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetConcurrency(int, int)} method with given arguments and returns a
     * bound value.
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
        value.setConcurrency(concurrency);
        try {
            value.setValue(databaseMetaData.supportsResultSetConcurrency(value.getType(), value.getConcurrency()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke supportsResultSetConcurrency({}, {})",
//                    value.getType(), value.getConcurrency(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // ------------------------------------------------------------------------------------ supportsResultSetHoldability

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetHoldability(int)} method with given argument and returns a bound
     * value.
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
            value.setValue(databaseMetaData.supportsResultSetHoldability(value.getHoldability()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke supportsResultSetHoldability({})", value.getHoldability(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------- supportsResultSetType

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetType(int)} method with given argument and returns a bound value.
     * The result may have {@code null} {@code value} when the {@link SQLException} has been
     * {@link #suppress(Class) suppressed}.
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
            value.setValue(databaseMetaData.supportsResultSetType(value.getType()));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke supportsResultSetType({})", value.getType(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    // ------------------------------------------------------------------------------- supportsTransactionIsolationLevel

    /**
     * Invokes {@link DatabaseMetaData#supportsTransactionIsolationLevel(int)} method with given argument and returns a
     * bound value.
     *
     * @param level a value for {@code level} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsTransactionIsolationLevel(int)
     */
    public SupportsTransactionIsolationLevel supportsTransactionIsolationLevel(final int level) throws SQLException {
        final SupportsTransactionIsolationLevel value = new SupportsTransactionIsolationLevel();
        value.setLevel(level);
        try {
            value.setValue(databaseMetaData.supportsTransactionIsolationLevel(level));
        } catch (final SQLException sqle) {
            //log..error("failed to invoke supportsTransactionIsolationLevel({})", value.getLevel(), sqle);
            throwUnlessSuppressed(sqle);
        }
        return value;
    }

    private @NotNull
    Map<@NotNull Field, @NotNull Label> getLabeledFields(final @NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz is null");
        return classesAndLabeledFields.computeIfAbsent(clazz, c -> Utils.getFieldsAnnotatedWith(c, Label.class));
    }

    public <T extends Throwable> Context suppress(final Class<T> throwableClass) {
        Objects.requireNonNull(throwableClass, "throwableClass is null");
        suppressedThrowableClasses.add(throwableClass);
        return this;
    }

    private <T extends Throwable> boolean isSuppressed(final Class<T> exceptionClass) {
        Objects.requireNonNull(exceptionClass, "exceptionClass is null");
        if (suppressedThrowableClasses.contains(exceptionClass)) {
            return true;
        }
        return suppressedThrowableClasses.stream().anyMatch(c -> c.isAssignableFrom(exceptionClass));
    }

    private boolean isSuppressed(final Throwable t) {
        Objects.requireNonNull(t, "t is null");
        return isSuppressed(t.getClass());
    }

    private void throwUnlessSuppressed(final SQLException sqle) throws SQLException {
        Objects.requireNonNull(sqle, "sqle is null");
        if (!isSuppressed(sqle)) {
            throw sqle;
        }
        logger.fine(() -> String.format("suppressed: %1$s", sqle));
    }

    final DatabaseMetaData databaseMetaData;

    private final Map<Class<?>, Map<Field, Label>> classesAndLabeledFields = new HashMap<>();

    private final Set<Class<?>> suppressedThrowableClasses = new HashSet<>();
}
