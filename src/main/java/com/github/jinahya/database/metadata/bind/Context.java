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

import lombok.extern.java.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * A class for retrieving information from an instance of {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Log
public class Context {

    /**
     * Creates a new instance from specified connection.
     *
     * @param connection the connection.
     * @return a new instance.
     * @throws SQLException if a database error occurs.
     */
    public static Context newInstance(final Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "connection is null");
        return new Context(connection.getMetaData());
    }

    /**
     * Creates a new instance with specified DatabaseMetaData.
     *
     * @param databaseMetaData the DatabaseMetaData to hold.
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
        final Set<String> resultLabels = Utils.getLabels(results);
        final Map<Field, _ColumnLabel> fieldLabels = new HashMap<>(getLabeledFields(type));
        for (final Iterator<Entry<Field, _ColumnLabel>> i = fieldLabels.entrySet().iterator(); i.hasNext(); ) {
            final Entry<Field, _ColumnLabel> entry = i.next();
            final Field field = entry.getKey();
            final _ColumnLabel fieldLabel = entry.getValue();
            if (!resultLabels.remove(fieldLabel.value())) {
                log.warning(() -> String.format("unknown fieldLabel; %1$s on %2$s", fieldLabel, field));
                continue;
            }
            if (field.isAnnotationPresent(_NotUsedBySpecification.class) ||
                field.isAnnotationPresent(_Reserved.class)) {
                i.remove();
                continue;
            }
            try {
                Utils.setFieldValue(field, instance, results, fieldLabel.value());
            } catch (final ReflectiveOperationException roe) {
                log.log(Level.SEVERE, String.format("failed to set %1$s", field), roe);
            }
            i.remove();
        }
        for (final String key : resultLabels) {
            final Object value = results.getObject(key);
            instance.getUnmappedValues().put(key, value);
        }
        assert fieldLabels.isEmpty() : "remaining fields " + fieldLabels;
//        if (!fieldLabels.isEmpty()) {
//            log.severe(() -> String.format("unmapped fields: %1$s", fieldLabels));
//        }
        return instance;
    }

    /**
     * Binds all records as given type and adds them to specified consumer.
     *
     * @param results  the records to bind.
     * @param type     the type of instances.
     * @param consumer the consumer to which bound instances are added
     * @param <T>      binding type parameter
     * @throws SQLException if a database error occurs.
     */
    @SuppressWarnings({
            "java:S112", // new RuntimeException
            "java:S1874", // isAccessible
            "java:S3011" // setAccessible
    })
    private <T extends MetadataType> void bind(final ResultSet results, final Class<T> type,
                                               final Consumer<? super T> consumer)
            throws SQLException {
        Objects.requireNonNull(results, "results is null");
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(consumer, "consumer is null");
        final Constructor<T> constructor;
        try {
            constructor = type.getDeclaredConstructor();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to get the default constructor; type: " + type, roe);
        }
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        while (results.next()) {
            final T value;
            try {
                value = constructor.newInstance();
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException("failed to instantiate; type: " + type, roe);
            }
            consumer.accept(bind(results, type, value));
        }
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
    @SuppressWarnings({
            "java:S112", // new RuntimeException
            "java:S1874", // isAccessible
            "java:S3011" // setAccessible
    })
    private <T extends MetadataType, C extends Collection<? super T>> C bind(
            final ResultSet results, final Class<T> type, final C collection)
            throws SQLException {
        Objects.requireNonNull(results, "results is null");
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(collection, "collection is null");
        while (results.next()) {
            final T value;
            try {
                final Constructor<T> constructor = type.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                value = constructor.newInstance();
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException("failed to instantiate " + type, roe);
            }
            collection.add(bind(results, type, value));
        }
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     * getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern)} method with given arguments, and
     * accepts each bound value to specified consumer.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for {@code attributeNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getAttributes(String, String, String, String)
     */
    void getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern,
                       final String attributeNamePattern, final Consumer<? super Attribute> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getAttributes(
                catalog, schemaPattern, typeNamePattern, attributeNamePattern)) {
            assert results != null;
            bind(results, Attribute.class, consumer);
        }
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method with given arguments, and returns a list of bound values.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for {@code attributeNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Attribute> getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern,
                                         final String attributeNamePattern)
            throws SQLException {
        final List<Attribute> list = new ArrayList<>();
        getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern, list::add);
        return list;
    }

    List<Attribute> getAttributes(final UDT udt, final String attributeNamePattern) throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        return getAttributes(
                udt.typeCatNonNull(),
                udt.typeSchemNonNull(),
                Objects.requireNonNull(udt.getTypeName(), "udt.typeName is null"),
                attributeNamePattern
        );
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)}
     * method with given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog  a value for {@code catalog} parameter.
     * @param schema   a value for {@code schema} parameter.
     * @param table    a value for {@code table} parameter.
     * @param scope    a value for {@code scope} parameter.
     * @param nullable a value for {@code nullable} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)
     */

    void getBestRowIdentifier(final String catalog, final String schema, final String table, final int scope,
                              final boolean nullable, final Consumer<? super BestRowIdentifier> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            assert results != null;
            bind(results, BestRowIdentifier.class, consumer);
        }
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)}
     * method with given arguments, and returns a list of bound values.
     *
     * @param catalog  a value for {@code catalog} parameter.
     * @param schema   a value for {@code schema} parameter.
     * @param table    a value for {@code table} parameter.
     * @param scope    a value for {@code scope} parameter.
     * @param nullable a value for {@code nullable} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<BestRowIdentifier> getBestRowIdentifier(final String catalog, final String schema, final String table,
                                                        final int scope, final boolean nullable)
            throws SQLException {
        final List<BestRowIdentifier> list = new ArrayList<>();
        getBestRowIdentifier(catalog, schema, table, scope, nullable, list::add);
        return list;
    }

    List<BestRowIdentifier> getBestRowIdentifier(final Table table, final int scope, final boolean nullable)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getBestRowIdentifier(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null"),
                scope,
                nullable
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which each bound value is accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getCatalogs()
     */
    void getCatalogs(final Consumer<? super Catalog> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getCatalogs()) {
            assert results != null;
            bind(results, Catalog.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Catalog> getCatalogs() throws SQLException {
        final List<Catalog> list = new ArrayList<>();
        getCatalogs(list::add);
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method, and accepts each bound value to specified
     * consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    void getClientInfoProperties(final Consumer<? super ClientInfoProperty> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getClientInfoProperties()) {
            assert results != null;
            bind(results, ClientInfoProperty.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ClientInfoProperty> getClientInfoProperties() throws SQLException {
        final List<ClientInfoProperty> list = new ArrayList<>();
        getClientInfoProperties(list::add);
        return list;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schema            a value for {@code schema} parameter.
     * @param table             a value for {@code table} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    void getColumnPrivileges(final String catalog, final String schema, final String table,
                             final String columnNamePattern, final Consumer<? super ColumnPrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            assert results != null;
            bind(results, ColumnPrivilege.class, consumer);
        }
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)} method with given arguments, and returns a list of bound values.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schema            a value for {@code schema} parameter.
     * @param table             a value for {@code table} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ColumnPrivilege> getColumnPrivileges(final String catalog, final String schema, final String table,
                                                     final String columnNamePattern)
            throws SQLException {
        final List<ColumnPrivilege> list = new ArrayList<>();
        getColumnPrivileges(catalog, schema, table, columnNamePattern, list::add);
        return list;
    }

    List<ColumnPrivilege> getColumnPrivileges(final Table table, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getColumnPrivileges(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null"),
                columnNamePattern
        );
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumns(String, String, String, String) getColumns(catalog, schemaPattern, *
     * tableNamePattern, columnNamePattern)} method with given arguments, and accepts each bound value to specified
     * consumer.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    void getColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                    final String columnNamePattern, final Consumer<? super Column> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern,
                                                             columnNamePattern)) {
            assert results != null;
            bind(results, Column.class, consumer);
        }
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumns(String, String, String, String) getColumns(catalog, schemaPattern,
     * tableNamePattern, columnNamePattern)} method with given arguments, and adds each bound value to specified
     * collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getColumns(String, String, String, String, Consumer)
     */
    <C extends Collection<? super Column>> C getColumns(final String catalog, final String schemaPattern,
                                                        final String tableNamePattern, final String columnNamePattern,
                                                        final C collection)
            throws SQLException {
        getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern,
                   (Consumer<? super Column>) collection::add);
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumns(String, String, String, String) getColumns(catalog, schemaPattern, *
     * tableNamePattern, columnNamePattern)} method with given arguments, and returns a list of bound values.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Column> getColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                                   final String columnNamePattern)
            throws SQLException {
        return getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getColumns(String, String, String, String) getColumns(catalog, schemaPattern, *
     * tableNamePattern, columnNamePattern)} method with given table's properties, and returns a list of bound values.
     *
     * @param table             the table.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getColumns(String, String, String, String)
     */
    List<Column> getColumns(final Table table, final String columnNamePattern) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getColumns(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null"),
                columnNamePattern
        );
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)} method with given arguments, and accepts each bound value to specified
     * consumer.
     *
     * @param parentCatalog  a value for {@code parentCatalog} parameter
     * @param parentSchema   a value for {@code parentSchema} parameter
     * @param parentTable    a value for {@code parentTable} parameter
     * @param foreignCatalog a value for {@code foreignCatalog} parameter
     * @param foreignSchema  av value for {@code foreignSchema} parameter
     * @param foreignTable   a value for {@code foreignTable} parameter
     * @param consumer       the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    void getCrossReference(final String parentCatalog, final String parentSchema, final String parentTable,
                           final String foreignCatalog, final String foreignSchema, final String foreignTable,
                           final Consumer<? super CrossReference> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getCrossReference(
                parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable)) {
            assert results != null;
            bind(results, CrossReference.class, consumer);
        }
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)} method with given arguments, and returns a list of bound values.
     *
     * @param parentCatalog  a value for {@code parentCatalog} parameter
     * @param parentSchema   a value for {@code parentSchema} parameter
     * @param parentTable    a value for {@code parentTable} parameter
     * @param foreignCatalog a value for {@code foreignCatalog} parameter
     * @param foreignSchema  av value for {@code foreignSchema} parameter
     * @param foreignTable   a value for {@code foreignTable} parameter
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<CrossReference> getCrossReference(final String parentCatalog, final String parentSchema,
                                                  final String parentTable, final String foreignCatalog,
                                                  final String foreignSchema, final String foreignTable)
            throws SQLException {
        final List<CrossReference> list = new ArrayList<>();
        getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable,
                          list::add);
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog  a value for {@code catalog} parameter.
     * @param schema   a value for {@code schema} parameter.
     * @param table    a value for {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getExportedKeys(String, String, String)
     */
    void getExportedKeys(final String catalog, final String schema, final String table,
                         final Consumer<? super ExportedKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getExportedKeys(catalog, schema, table)) {
            assert results != null;
            bind(results, ExportedKey.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments, and returns a list of bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ExportedKey> getExportedKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        final List<ExportedKey> list = new ArrayList<>();
        getExportedKeys(catalog, schema, table, list::add);
        return list;
    }

    /**
     * Retrieves exported keys of specified table.
     *
     * @param table the table whose exported keys are retrieved.
     * @return a list of exported keys of the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getExportedKeys(String, String, String)
     */
    List<ExportedKey> getExportedKeys(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getExportedKeys(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null")
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(String, String, String)} method with given arguments, and accepts
     * each bound value to specified consumer.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param consumer            the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     */
    void getFunctions(final String catalog, final String schemaPattern, final String functionNamePattern,
                      final Consumer<? super Function> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getFunctions(catalog, schemaPattern, functionNamePattern)) {
            assert results != null;
            bind(results, Function.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(String, String, String)} method with given arguments, and returns a
     * list of bound values.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Function> getFunctions(final String catalog, final String schemaPattern,
                                       final String functionNamePattern)
            throws SQLException {
        final List<Function> list = new ArrayList<>();
        getFunctions(catalog, schemaPattern, functionNamePattern, list::add);
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method with specified
     * arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for {@code columnNamePattern} parameter.
     * @param consumer            the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctionColumns(String, String, String, String)
     */
    void getFunctionColumns(final String catalog, final String schemaPattern, final String functionNamePattern,
                            final String columnNamePattern, final Consumer<? super FunctionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getFunctionColumns(
                catalog, schemaPattern, functionNamePattern, columnNamePattern)) {
            assert results != null;
            bind(results, FunctionColumn.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method with specified
     * arguments, and returns a list of bound values.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<FunctionColumn> getFunctionColumns(final String catalog, final String schemaPattern,
                                                   final String functionNamePattern, final String columnNamePattern)
            throws SQLException {
        final List<FunctionColumn> list = new ArrayList<>();
        getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern, list::add);
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method with specified
     * arguments, and returns a list of bound values.
     *
     * @param function          the function whose columns are retrieved.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getFunctionColumns(String, String, String, String)
     */
    List<FunctionColumn> getFunctionColumns(final Function function, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(function, "function is null");
        return getFunctionColumns(
                function.functionCatNonNull(),
                function.functionSchemNonNull(),
                Objects.requireNonNull(function.getFunctionName(), "function.functionName is null"),
                columnNamePattern
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(String, String, String)} method with given arguments, and accepts
     * each bound value to specified consumer.
     *
     * @param catalog  a value for {@code catalog} parameter.
     * @param schema   a value for {@code schema} parameter.
     * @param table    a value for {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getImportedKeys(String, String, String)
     */
    void getImportedKeys(final String catalog, final String schema, final String table,
                         final Consumer<? super ImportedKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getImportedKeys(catalog, schema, table)) {
            assert results != null;
            bind(results, ImportedKey.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(String, String, String)} method with given arguments, and returns
     * a list of bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ImportedKey> getImportedKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        final List<ImportedKey> list = new ArrayList<>();
        getImportedKeys(catalog, schema, table, list::add);
        return list;
    }

    /**
     * Retrieves imported keys of specified table.
     *
     * @param table the table whose imported keys are retrieved.
     * @return a list of imported keys of the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getImportedKeys(String, String, String)
     */
    List<ImportedKey> getImportedKeys(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getImportedKeys(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null")
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)} method with specified
     * arguments, and accepts bound value to specified consumer.
     *
     * @param catalog     a value for {@code catalog} parameter.
     * @param schema      a value for {@code schema} parameter.
     * @param table       a value for {@code table} parameter.
     * @param unique      a value for {@code unique} parameter.
     * @param approximate a value for {@code approximate} parameter.
     * @param consumer    the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    void getIndexInfo(final String catalog, final String schema, final String table, final boolean unique,
                      final boolean approximate, final Consumer<? super IndexInfo> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getIndexInfo(catalog, schema, table, unique, approximate)) {
            assert results != null;
            bind(results, IndexInfo.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)} method with specified
     * arguments, and returns a list of bound values.
     *
     * @param catalog     a value for {@code catalog} parameter.
     * @param schema      a value for {@code schema} parameter.
     * @param table       a value for {@code table} parameter.
     * @param unique      a value for {@code unique} parameter.
     * @param approximate a value for {@code approximate} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<IndexInfo> getIndexInfo(final String catalog, final String schema, final String table,
                                        final boolean unique, final boolean approximate)
            throws SQLException {
        final List<IndexInfo> list = new ArrayList<>();
        getIndexInfo(catalog, schema, table, unique, approximate, list::add);
        return list;
    }

    List<IndexInfo> getIndexInfo(final Table table, final boolean unique, final boolean approximate)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getIndexInfo(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null"),
                unique,
                approximate
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method with given arguments, and accepts
     * each bound value to specified consumer.
     *
     * @param catalog  a value for {@code catalog} parameter.
     * @param schema   a value for {@code schema} parameter.
     * @param table    a value for {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    void getPrimaryKeys(final String catalog, final String schema, final String table,
                        final Consumer<? super PrimaryKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getPrimaryKeys(catalog, schema, table)) {
            assert results != null;
            bind(results, PrimaryKey.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method with given arguments, and returns
     * a list of bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<PrimaryKey> getPrimaryKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        final List<PrimaryKey> list = new ArrayList<>();
        getPrimaryKeys(catalog, schema, table, list::add);
        return list;
    }

    List<PrimaryKey> getPrimaryKeys(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPrimaryKeys(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null")
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)} method with given arguments
     * , and accepts each value to specified consumer.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedureColumns(String, String, String, String)
     */
    void getProcedureColumns(final String catalog, final String schemaPattern, final String procedureNamePattern,
                             final String columnNamePattern, final Consumer<? super ProcedureColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getProcedureColumns(
                catalog, schemaPattern, procedureNamePattern, columnNamePattern)) {
            assert results != null;
            bind(results, ProcedureColumn.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)} method with given arguments
     * , and returns a list of bound values.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ProcedureColumn> getProcedureColumns(final String catalog, final String schemaPattern,
                                                     final String procedureNamePattern,
                                                     final String columnNamePattern)
            throws SQLException {
        final List<ProcedureColumn> list = new ArrayList<>();
        getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern, list::add);
        return list;
    }

    List<ProcedureColumn> getProcedureColumns(final Procedure procedure, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(procedure, "procedure is null");
        Objects.requireNonNull(columnNamePattern, "columnNamePattern is null");
        return getProcedureColumns(
                procedure.procedureCatNonNull(),
                procedure.procedureSchemNonNull(),
                procedure.getProcedureName(),
                columnNamePattern
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments, and accepts each bounds value to specified consumer.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedures(String, String, String)
     */
    void getProcedures(final String catalog, final String schemaPattern, final String procedureNamePattern,
                       final Consumer<? super Procedure> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            assert results != null;
            bind(results, Procedure.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments, and returns a lsit of bound values.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Procedure> getProcedures(final String catalog, final String schemaPattern,
                                         final String procedureNamePattern)
            throws SQLException {
        final List<Procedure> list = new ArrayList<>();
        getProcedures(catalog, schemaPattern, procedureNamePattern, list::add);
        return list;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method with given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPseudoColumns(String, String, String, String)
     */
    void getPseudoColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                          final String columnNamePattern, final Consumer<? super PseudoColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getPseudoColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            assert results != null;
            bind(results, PseudoColumn.class, consumer);
        }
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method with given arguments, and returns a list of bound values.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<PseudoColumn> getPseudoColumns(final String catalog, final String schemaPattern,
                                               final String tableNamePattern, final String columnNamePattern)
            throws SQLException {
        final List<PseudoColumn> list = new ArrayList<>();
        getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, list::add);
        return list;
    }

    List<PseudoColumn> getPseudoColumns(final Table table, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPseudoColumns(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null"),
                columnNamePattern
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas()
     */
    void getSchemas(final Consumer<? super Schema> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getSchemas()) {
            assert results != null;
            bind(results, Schema.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Schema> getSchemas() throws SQLException {
        final List<Schema> list = new ArrayList<>();
        getSchemas(list::add);
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method with given arguments, and accepts each bound
     * value to specified consumer.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @param consumer      the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas(String, String)
     */
    void getSchemas(final String catalog, final String schemaPattern, final Consumer<? super Schema> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getSchemas(catalog, schemaPattern)) {
            assert results != null;
            bind(results, Schema.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method with given arguments, and returns a list of
     * bound values.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Schema> getSchemas(final String catalog, final String schemaPattern) throws SQLException {
        final List<Schema> list = new ArrayList<>();
        getSchemas(catalog, schemaPattern, list::add);
        return list;
    }

    List<Schema> getSchemas(final Catalog catalog, final String schemaPattern) throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSchemas(
                Objects.requireNonNull(catalog.getTableCat(), "catalog.tableCat is null"),
                schemaPattern
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method with given arguments, and accepts
     * each bounds value to specified consumer.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param consumer         the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSuperTables(String, String, String)
     */
    void getSuperTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                        final Consumer<? super SuperTable> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
            assert results != null;
            bind(results, SuperTable.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method with given arguments and adds
     * bounds values to specified collection.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} paramter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<SuperTable> getSuperTables(final String catalog, final String schemaPattern,
                                           final String tableNamePattern)
            throws SQLException {
        final List<SuperTable> list = new ArrayList<>();
        getSuperTables(catalog, schemaPattern, tableNamePattern, list::add);
        return list;
    }

    List<SuperTable> getSuperTables(final Schema schema, final String tableNamePattern) throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getSuperTables(
                schema.tableCatalogNonNull(),
                Objects.requireNonNull(schema.getTableSchem(), "schema.tableSchem is null"),
                tableNamePattern
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTypes(String, String, String)} method with given arguments, and accepts
     * each bound value to specified consumer.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter.
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param consumer        the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSuperTypes(String, String, String)
     */
    void getSuperTypes(final String catalog, final String schemaPattern, final String typeNamePattern,
                       final Consumer<? super SuperType> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
            assert results != null;
            bind(results, SuperType.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTypes(String, String, String)} method with given arguments, and returns a
     * list of bound values.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter.
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<SuperType> getSuperTypes(final String catalog, final String schemaPattern, final String typeNamePattern)
            throws SQLException {
        final List<SuperType> list = new ArrayList<>();
        getSuperTypes(catalog, schemaPattern, typeNamePattern, list::add);
        return list;
    }

    List<SuperType> getSuperTypes(final Schema schema, final String typeNamePattern) throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getSuperTypes(
                schema.tableCatalogNonNull(),
                Objects.requireNonNull(schema.getTableSchem(), "schema.tableSchem is null"),
                typeNamePattern
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param consumer         the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTablePrivileges(String, String, String)
     */
    void getTablePrivileges(final String catalog, final String schemaPattern, final String tableNamePattern,
                            final Consumer<? super TablePrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            assert results != null;
            bind(results, TablePrivilege.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments, and returns a list of bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<TablePrivilege> getTablePrivileges(final String catalog, final String schemaPattern,
                                                   final String tableNamePattern)
            throws SQLException {
        final List<TablePrivilege> list = new ArrayList<>();
        getTablePrivileges(catalog, schemaPattern, tableNamePattern, list::add);
        return list;
    }

    List<TablePrivilege> getTablePrivileges(final Schema schema, final String tableNamePattern) throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getTablePrivileges(
                schema.tableCatalogNonNull(),
                Objects.requireNonNull(schema.getTableSchem(), "schema.tableSchem is null"),
                tableNamePattern
        );
    }

    List<TablePrivilege> getTablePrivileges(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getTablePrivileges(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null")
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTableTypes()
     */
    void getTableTypes(final Consumer<? super TableType> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getTableTypes()) {
            assert results != null;
            bind(results, TableType.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<TableType> getTableTypes() throws SQLException {
        final List<TableType> list = new ArrayList<>();
        getTableTypes(list::add);
        return list;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     * getTables(catalog, schemaPattern, tableNamePattern, types)} method with given arguments, and accepts each bound
     * value to specified consumer.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @param consumer         the consumer to which values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    void getTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                   final String[] types, final Consumer<? super Table> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            assert results != null;
//            bind(results, Table.class, consumer);
            bind(results, Table.class, (Consumer<? super Table>) t -> {
                consumer.accept(t);
                for (final BestRowIdentifier.Scope scope : BestRowIdentifier.Scope.values()) {
                    for (final boolean nullable : new boolean[] {true, false}) {
                        try {
                            getBestRowIdentifier(
                                    t.tableCatNonNull(),
                                    t.tableSchemNonNull(),
                                    t.getTableName(),
                                    scope.fieldValueAsInt(),
                                    nullable,
                                    bri -> {
                                        t.getBestRowIdentifierMap().computeIfAbsent(scope, s -> new HashMap<>())
                                                .computeIfAbsent(nullable, n -> new ArrayList<>())
                                                .add(bri);
                                    }
                            );
                        } catch (final SQLException sqle) {
                            throw new RuntimeException("unable to get best row identifiers for " + t, sqle);
                        }
                    }
                }
            });
        }
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     * getTables(catalog, schemaPattern, tableNamePattern, types)} method with given arguments, and adds each bound
     * values to specified collection.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @param collection       the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getTables(String, String, String, String[], Consumer)
     */

    public <C extends Collection<? super Table>> C getTables(final String catalog, final String schemaPattern,
                                                             final String tableNamePattern, final String[] types,
                                                             final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getTables(catalog, schemaPattern, tableNamePattern, types, (Consumer<? super Table>) collection::add);
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     * getTables(catalog, schemaPattern, tableNamePattern, types)} method with given arguments, and returns a list of
     * bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Table> getTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                                 final String[] types)
            throws SQLException {
        return getTables(catalog, schemaPattern, tableNamePattern, types, new ArrayList<>());
    }

    List<Table> getTables(final Schema schema, final String tableNamePattern, final String[] types)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getTables(
                schema.tableCatalogNonNull(),
                Objects.requireNonNull(schema.getTableSchem(), "schema.tableSchem is null"),
                tableNamePattern,
                types
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are added.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTypeInfo()
     */
    void getTypeInfo(final Consumer<? super TypeInfo> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getTypeInfo()) {
            assert results != null;
            bind(results, TypeInfo.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<TypeInfo> getTypeInfo() throws SQLException {
        final List<TypeInfo> list = new ArrayList<>();
        getTypeInfo(list::add);
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])} method with
     * given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param types           a value for {@code type} parameter
     * @param consumer        the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getUDTs(String, String, String, int[])
     */
    void getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern,
                 final int[] types, final Consumer<? super UDT> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
            assert results != null;
            bind(results, UDT.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])} method with
     * given arguments, and returns a list of bound values.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param types           a value for {@code type} parameter
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */

    public List<UDT> getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern,
                             final int[] types)
            throws SQLException {
        final List<UDT> list = new ArrayList<>();
        getUDTs(catalog, schemaPattern, typeNamePattern, types, list::add);
        return list;
    }

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog  a value for {@code catalog} parameter.
     * @param schema   a value for {@code schema} parameter.
     * @param table    a value for {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#getVersionColumns(String, String, String)
     */
    void getVersionColumns(final String catalog, final String schema, final String table,
                           final Consumer<? super VersionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getVersionColumns(catalog, schema, table)) {
            assert results != null;
            bind(results, VersionColumn.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments, and returns a list of bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     */
    public List<VersionColumn> getVersionColumns(final String catalog, final String schema, final String table)
            throws SQLException {
        final List<VersionColumn> list = new ArrayList<>();
        getVersionColumns(catalog, schema, table, list::add);
        return list;
    }

    List<VersionColumn> getVersionColumns(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getVersionColumns(
                table.tableCatNonNull(),
                table.tableSchemNonNull(),
                Objects.requireNonNull(table.getTableName(), "table.tableName is null")
        );
    }

    private Map<Field, _ColumnLabel> getLabeledFields(final Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz is null");
        return Collections.unmodifiableMap(
                classesAndLabeledFields.computeIfAbsent(clazz, c -> Utils.getFieldsAnnotatedWith(c, _ColumnLabel.class))
        );
    }

    final DatabaseMetaData databaseMetaData;

    private final Map<Class<?>, Map<Field, _ColumnLabel>> classesAndLabeledFields = new HashMap<>();
}
