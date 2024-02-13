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
import java.util.Arrays;
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
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

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
            if (field.isAnnotationPresent(_NotUsedBySpecification.class) ||
                field.isAnnotationPresent(_ReservedBySpecification.class)) {
                i.remove();
                continue;
            }
            if (field.isAnnotationPresent(_MissingByVendor.class)) {
                final String value = field.getAnnotation(_MissingByVendor.class).value();
                if (value.equals(databaseMetaData.getDatabaseProductName())) {
                    i.remove();
                    continue;
                }
            }
            if (!resultLabels.remove(fieldLabel.value())) {
                log.warning(() -> String.format("unmapped; label: %1$s; field: %2$s", fieldLabel, field));
                i.remove();
                continue;
            }
            try {
                Utils.setFieldValue(field, instance, results, fieldLabel.value());
            } catch (final ReflectiveOperationException roe) {
                log.log(Level.SEVERE, roe, () -> String.format("failed to set %1$s", field));
            }
            i.remove();
        }
        for (final Iterator<String> i = resultLabels.iterator(); i.hasNext(); i.remove()) {
            final String key = i.next();
            final Object value = results.getObject(key);
            instance.getUnmappedValues().put(key, value);
        }
        assert resultLabels.isEmpty() : "remaining result labels: " + resultLabels;
        assert fieldLabels.isEmpty() : "remaining field labels: " + fieldLabels;
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
    private <T extends MetadataType> void acceptBound(final ResultSet results, final Class<T> type,
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

    // ------------------------------------- getAttributes(catalog, schemaPatter, typeNamePattern, attributeNamePattern)
    void acceptAttributes(final String catalog, final String schemaPattern, final String typeNamePattern,
                          final String attributeNamePattern, final Consumer<? super Attribute> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getAttributes(
                catalog, schemaPattern, typeNamePattern, attributeNamePattern)) {
            assert results != null;
            acceptBound(results, Attribute.class, consumer);
        }
    }

    <T extends Collection<? super Attribute>> T addAttributes(
            final String catalog, final String schemaPattern, final String typeNamePattern,
            final String attributeNamePattern, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptAttributes(
                catalog,
                schemaPattern,
                typeNamePattern,
                attributeNamePattern,
                collection::add
        );
        return collection;
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
        return addAttributes(
                catalog,
                schemaPattern,
                typeNamePattern,
                attributeNamePattern,
                new ArrayList<>()
        );
    }

    List<Attribute> getAttributes(final UDT udt, final String attributeNamePattern) throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        return getAttributes(
                udt.getTypeCat(),
                udt.getTypeSchem(),
                udt.getTypeName(),
                attributeNamePattern
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    void acceptBestRowIdentifier(final String catalog, final String schema, final String table, final int scope,
                                 final boolean nullable, final Consumer<? super BestRowIdentifier> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        final Table parent = Table.of(catalog, schema, table);
        try (ResultSet results = databaseMetaData.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            assert results != null;
            acceptBound(results, BestRowIdentifier.class, v -> {
                consumer.accept(v);
            });
        }
    }

    <T extends Collection<? super BestRowIdentifier>> T addBestRowIdentifier(
            final String catalog, final String schema, final String table, final int scope, final boolean nullable,
            final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptBestRowIdentifier(catalog, schema, table, scope, nullable, collection::add);
        return collection;
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
     * @see DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)
     */
    public List<BestRowIdentifier> getBestRowIdentifier(final String catalog, final String schema, final String table,
                                                        final int scope, final boolean nullable)
            throws SQLException {
        return addBestRowIdentifier(catalog, schema, table, scope, nullable, new ArrayList<>());
    }

    List<BestRowIdentifier> getBestRowIdentifier(final Table table, final int scope, final boolean nullable)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getBestRowIdentifier(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName(),
                scope,
                nullable
        );
    }

    // --------------------------------------------------------------------------------------------------- getCatalogs()
    void acceptCatalogs(final Consumer<? super Catalog> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getCatalogs()) {
            assert results != null;
            acceptBound(results, Catalog.class, consumer);
        }
    }

    <T extends Collection<? super Catalog>> T addCatalogs(final T collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptCatalogs(collection::add);
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Catalog> getCatalogs() throws SQLException {
        return addCatalogs(new ArrayList<>());
    }

    // --------------------------------------------------------------------------------------- getClientInfoProperties()

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method, and accepts each bound value to specified
     * consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    void acceptClientInfoProperties(final Consumer<? super ClientInfoProperty> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getClientInfoProperties()) {
            assert results != null;
            acceptBound(results, ClientInfoProperty.class, consumer);
        }
    }

    <C extends Collection<ClientInfoProperty>> C addClientInfoProperties(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptClientInfoProperties(collection::add);
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ClientInfoProperty> getClientInfoProperties() throws SQLException {
        return addClientInfoProperties(new ArrayList<>());
    }

    // -------------------------------------------------- getColumnPrivileges(catalog, schema, table, columnNamePattern)
    void acceptColumnPrivileges(final String catalog, final String schema, final String table,
                                final String columnNamePattern, final Consumer<? super ColumnPrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            assert results != null;
            acceptBound(results, ColumnPrivilege.class, consumer);
        }
    }

    <T extends Collection<? super ColumnPrivilege>> T addColumnPrivileges(
            final String catalog, final String schema, final String table, final String columnNamePattern,
            final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptColumnPrivileges(
                catalog,
                schema,
                table,
                columnNamePattern,
                collection::add
        );
        return collection;
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
        return addColumnPrivileges(catalog, schema, table, columnNamePattern, new ArrayList<>());
    }

    List<ColumnPrivilege> getColumnPrivileges(final Table table, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getColumnPrivileges(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName(),
                columnNamePattern
        );
    }

    List<ColumnPrivilege> getColumnPrivileges(final Column column) throws SQLException {
        Objects.requireNonNull(column, "column is null");
        return getColumnPrivileges(
                column.getTableCat(),
                column.getTableSchem(),
                column.getTableName(),
                column.getColumnName()
        );
    }

    // ----------------------------------------- getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)

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
    void acceptColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                       final String columnNamePattern, final Consumer<? super Column> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            assert results != null;
            acceptBound(results, Column.class, consumer);
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
     * @see #acceptColumns(String, String, String, String, Consumer)
     */
    <T extends Collection<? super Column>> T addColumns(final String catalog, final String schemaPattern,
                                                        final String tableNamePattern, final String columnNamePattern,
                                                        final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, collection::add);
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
        return addColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
    }

    /**
     * Invokes {@link #getColumns(String, String, String, String)} method with specified table's
     * {@link Table#getTableCat() tableCat}, {@link Table#getTableSchem() tableSchem},
     * {@link Table#getTableName() tableName}, and specified column name pattern.
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
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName(),
                columnNamePattern
        );
    }

    // -------- getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable)

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
     * @param foreignSchema  a value for {@code foreignSchema} parameter
     * @param foreignTable   a value for {@code foreignTable} parameter
     * @param consumer       the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    void acceptCrossReference(final String parentCatalog, final String parentSchema, final String parentTable,
                              final String foreignCatalog, final String foreignSchema, final String foreignTable,
                              final Consumer<? super CrossReference> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getCrossReference(
                parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable)) {
            assert results != null;
            acceptBound(results, CrossReference.class, consumer);
        }
    }

    <T extends Collection<? super CrossReference>> T addCrossReference(
            final String parentCatalog, final String parentSchema, final String parentTable,
            final String foreignCatalog, final String foreignSchema, final String foreignTable,
            final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptCrossReference(
                parentCatalog, parentSchema, parentTable,
                foreignCatalog, foreignSchema, foreignTable,
                collection::add
        );
        return collection;
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
     * @param foreignSchema  a value for {@code foreignSchema} parameter
     * @param foreignTable   a value for {@code foreignTable} parameter
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<CrossReference> getCrossReference(final String parentCatalog, final String parentSchema,
                                                  final String parentTable, final String foreignCatalog,
                                                  final String foreignSchema, final String foreignTable)
            throws SQLException {
        return addCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable,
                                 new ArrayList<>());
    }

    List<CrossReference> getCrossReference(final Table parentTable, final Table foreighTable) throws SQLException {
        Objects.requireNonNull(parentTable, "parentTable is null");
        Objects.requireNonNull(foreighTable, "foreignTable is null");
        return getCrossReference(
                parentTable.getTableCat(),
                parentTable.getTableSchem(),
                parentTable.getTableName(),
                foreighTable.getTableCat(),
                foreighTable.getTableSchem(),
                foreighTable.getTableName()
        );
    }

    // ------------------------------------------------------------------------- getExportedKeys(catalog, schema, table)

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
    void acceptExportedKeys(final String catalog, final String schema, final String table,
                            final Consumer<? super ExportedKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getExportedKeys(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, ExportedKey.class, consumer);
        }
    }

    <T extends Collection<? super ExportedKey>> T addExportedKeys(final String catalog, final String schema,
                                                                  final String table, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptExportedKeys(catalog, schema, table, collection::add);
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)
     * getExportedKeys(catalog, schema, table)} method with given arguments, and returns a list of bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ExportedKey> getExportedKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        return addExportedKeys(catalog, schema, table, new ArrayList<>());
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
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName()
        );
    }

    // ------------------------------------------------------- getFunctions(catalog, schemaPattern, functionNamePattern)

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
    void acceptFunctions(final String catalog, final String schemaPattern, final String functionNamePattern,
                         final Consumer<? super Function> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getFunctions(catalog, schemaPattern, functionNamePattern)) {
            assert results != null;
            acceptBound(results, Function.class, consumer);
        }
    }

    <T extends Collection<? super Function>> T addFunctions(final String catalog, final String schemaPattern,
                                                            final String functionNamePattern, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptFunctions(catalog, schemaPattern, functionNamePattern, collection::add);
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getFunctions(String, String, String) getFunctions(catalog, schemaPattern,
     * functionNamePattern)} method with given arguments, and returns a list of bound values.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     */
    public List<Function> getFunctions(final String catalog, final String schemaPattern,
                                       final String functionNamePattern)
            throws SQLException {
        return addFunctions(
                catalog,
                schemaPattern,
                functionNamePattern,
                new ArrayList<>()
        );
    }

    // ------------------------------ getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern)

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
    void acceptFunctionColumns(final String catalog, final String schemaPattern, final String functionNamePattern,
                               final String columnNamePattern, final Consumer<? super FunctionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getFunctionColumns(
                catalog, schemaPattern, functionNamePattern, columnNamePattern)) {
            assert results != null;
            acceptBound(results, FunctionColumn.class, consumer);
        }
    }

    <T extends Collection<? super FunctionColumn>> T addFunctionColumns(
            final String catalog, final String schemaPattern, final String functionNamePattern,
            final String columnNamePattern, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern, collection::add);
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getFunctionColumns(String, String, String, String) getFunctionColumns(catalog,
     * schemaPattern, functionNamePattern, columnNamePattern)} method with specified arguments, and returns a list of
     * bound values.
     *
     * @param catalog             a value for {@code catalog} parameter.
     * @param schemaPattern       a value for {@code schemaPattern} parameter.
     * @param functionNamePattern a value for {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctionColumns(String, String, String, String)
     */
    public List<FunctionColumn> getFunctionColumns(final String catalog, final String schemaPattern,
                                                   final String functionNamePattern, final String columnNamePattern)
            throws SQLException {
        return addFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern, new ArrayList<>());
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
                function.getFunctionCat(),
                function.getFunctionSchem(),
                function.getFunctionName(),
                columnNamePattern
        );
    }

    // ------------------------------------------------------------------------- getImportedKeys(catalog, schema, table)

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
    void acceptImportedKeys(final String catalog, final String schema, final String table,
                            final Consumer<? super ImportedKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getImportedKeys(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, ImportedKey.class, consumer);
        }
    }

    <T extends Collection<? super ImportedKey>> T addImportedKeys(final String catalog, final String schema,
                                                                  final String table, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptImportedKeys(catalog, schema, table, collection::add);
        return collection;
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
     * @see DatabaseMetaData#getImportedKeys(String, String, String)
     */
    public List<ImportedKey> getImportedKeys(final String catalog, final String schema, final String table)
            throws SQLException {
        return addImportedKeys(catalog, schema, table, new ArrayList<>());
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
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName()
        );
    }

    // ------------------------------------------------------- getIndexInfo(catalog, schema, table, unique, approximate)

    void acceptIndexInfo(final String catalog, final String schema, final String table, final boolean unique,
                         final boolean approximate, final Consumer<? super IndexInfo> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getIndexInfo(catalog, schema, table, unique, approximate)) {
            assert results != null;
            acceptBound(
                    results,
                    IndexInfo.class,
                    consumer
            );
        }
    }

    <T extends Collection<? super IndexInfo>> T addIndexInfo(final String catalog, final String schema,
                                                             final String table, final boolean unique,
                                                             final boolean approximate, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptIndexInfo(catalog, schema, table, unique, approximate, collection::add);
        return collection;
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
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    public List<IndexInfo> getIndexInfo(final String catalog, final String schema, final String table,
                                        final boolean unique, final boolean approximate)
            throws SQLException {
        return addIndexInfo(catalog, schema, table, unique, approximate, new ArrayList<>());
    }

    List<IndexInfo> getIndexInfo(final Table table, final boolean unique, final boolean approximate)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getIndexInfo(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName(),
                unique,
                approximate
        );
    }

    // -------------------------------------------------------------------------- getPrimaryKeys(catalog, schema, table)

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String) getPrimaryKeys(catalog, schema, table)}
     * method with given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog  a value for {@code catalog} parameter.
     * @param schema   a value for {@code schema} parameter.
     * @param table    a value for {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    void acceptPrimaryKeys(final String catalog, final String schema, final String table,
                           final Consumer<? super PrimaryKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getPrimaryKeys(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, PrimaryKey.class, consumer);
        }
    }

    <T extends Collection<? super PrimaryKey>> T addPrimaryKeys(final String catalog, final String schema,
                                                                final String table, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptPrimaryKeys(catalog, schema, table, collection::add);
        return collection;
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
        return addPrimaryKeys(catalog, schema, table, new ArrayList<>());
    }

    List<PrimaryKey> getPrimaryKeys(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPrimaryKeys(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName()
        );
    }

    // ---------------------------- getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern)

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
    void acceptProcedureColumns(final String catalog, final String schemaPattern, final String procedureNamePattern,
                                final String columnNamePattern, final Consumer<? super ProcedureColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getProcedureColumns(
                catalog, schemaPattern, procedureNamePattern, columnNamePattern)) {
            assert results != null;
            acceptBound(results, ProcedureColumn.class, consumer);
        }
    }

    <T extends Collection<? super ProcedureColumn>> T addProcedureColumns(
            final String catalog, final String schemaPattern, final String procedureNamePattern,
            final String columnNamePattern, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern, collection::add);
        return collection;
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
        return addProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern, new ArrayList<>());
    }

    List<ProcedureColumn> getProcedureColumns(final Procedure procedure, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(procedure, "procedure is null");
        return getProcedureColumns(
                procedure.getProcedureCat(),
                procedure.getProcedureSchem(),
                procedure.getProcedureName(),
                columnNamePattern
        );
    }

    // ----------------------------------------------------- getProcedures(catalog, schemaPattern, procedureNamePattern)

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
    void acceptProcedures(final String catalog, final String schemaPattern, final String procedureNamePattern,
                          final Consumer<? super Procedure> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            assert results != null;
            acceptBound(results, Procedure.class, consumer);
        }
    }

    <T extends Collection<? super Procedure>> T addProcedures(final String catalog, final String schemaPattern,
                                                              final String procedureNamePattern, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptProcedures(catalog, schemaPattern, procedureNamePattern, collection::add);
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)
     * getProcedures(catalog, schemaPattern, procedureNamePattern)} method with given arguments, and returns a list of
     * bound values.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedures(String, String, String)
     */
    public List<Procedure> getProcedures(final String catalog, final String schemaPattern,
                                         final String procedureNamePattern)
            throws SQLException {
        return addProcedures(catalog, schemaPattern, procedureNamePattern, new ArrayList<>());
    }

    List<Procedure> getProcedures(final Catalog catalog, final String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getProcedures(catalog.getTableCat(), null, procedureNamePattern);
    }

    List<Procedure> getProcedures(final Schema schema, final String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getProcedures(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                procedureNamePattern
        );
    }

    // ----------------------------- getPseudoColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern)

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
    void acceptPseudoColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                             final String columnNamePattern, final Consumer<? super PseudoColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getPseudoColumns(
                catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            assert results != null;
            acceptBound(results, PseudoColumn.class, consumer);
        }
    }

    <T extends Collection<? super PseudoColumn>> T addPseudoColumns(final String catalog, final String schemaPattern,
                                                                    final String tableNamePattern,
                                                                    final String columnNamePattern, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptPseudoColumns(
                catalog,
                schemaPattern,
                tableNamePattern,
                columnNamePattern,
                collection::add
        );
        return collection;
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
     * @see DatabaseMetaData#getPseudoColumns(String, String, String, String)
     */
    public List<PseudoColumn> getPseudoColumns(final String catalog, final String schemaPattern,
                                               final String tableNamePattern, final String columnNamePattern)
            throws SQLException {
        return addPseudoColumns(
                catalog,
                schemaPattern,
                tableNamePattern,
                columnNamePattern,
                new ArrayList<>()
        );
    }

    List<PseudoColumn> getPseudoColumns(final Table table, final String columnNamePattern) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPseudoColumns(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName(),
                columnNamePattern
        );
    }

    // ---------------------------------------------------------------------------------------------------- getSchemas()

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas()
     */
    void acceptSchemas(final Consumer<? super Schema> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getSchemas()) {
            assert results != null;
            acceptBound(results, Schema.class, v -> {
                assert v.getTableSchem() != null;
                assert !v.getTableSchem().isEmpty();
                consumer.accept(v);
                // ---------------------------------------------------------------------------------------------- tables
            });
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method, and add each bound value to specified collection.
     *
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <T extends Collection<? super Schema>> T addSchemas(final T collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptSchemas(collection::add);
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Schema> getSchemas() throws SQLException {
        return addSchemas(new ArrayList<>());
    }

    // ------------------------------------------------------------------------------ getSchemas(catalog, schemaPattern)

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
    void acceptSchemas(final String catalog, final String schemaPattern, final Consumer<? super Schema> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getSchemas(catalog, schemaPattern)) {
            assert results != null;
            acceptBound(results, Schema.class, consumer);
        }
    }

    <T extends Collection<? super Schema>> T addSchemas(final String catalog, final String schemaPattern,
                                                        final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptSchemas(catalog, schemaPattern, collection::add);
        return collection;
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
        return addSchemas(catalog, schemaPattern, new ArrayList<>());
    }

    List<Schema> getSchemas(final Catalog catalog, final String schemaPattern) throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSchemas(
                catalog.getTableCat(),
                schemaPattern
        );
    }

    // -------------------------------------------------------- getSuperTables(catalog, schemaPattern, tableNamePattern)

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
    void acceptSuperTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                           final Consumer<? super SuperTable> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
            assert results != null;
            acceptBound(results, SuperTable.class, consumer);
        }
    }

    <T extends Collection<? super SuperTable>> T addSuperTables(final String catalog, final String schemaPattern,
                                                                final String tableNamePattern,
                                                                final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptSuperTables(catalog, schemaPattern, tableNamePattern, collection::add);
        return collection;
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
        return addSuperTables(catalog, schemaPattern, tableNamePattern, new ArrayList<>());
    }

    List<SuperTable> getSuperTables(final Schema schema, final String tableNamePattern) throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getSuperTables(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                tableNamePattern
        );
    }

    List<SuperTable> getSuperTables(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getSuperTables(
                Schema.of(table.getTableCat(), table.getTableSchem()),
                table.getTableName()
        );
    }

    // ----------------------------------------------------------- getSuperType(catalog, schemaPattern, typeNamePattern)

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
    void acceptSuperTypes(final String catalog, final String schemaPattern, final String typeNamePattern,
                          final Consumer<? super SuperType> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
            assert results != null;
            acceptBound(results, SuperType.class, consumer);
        }
    }

    <T extends Collection<? super SuperType>> T addSuperTypes(final String catalog, final String schemaPattern,
                                                              final String typeNamePattern, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptSuperTypes(catalog, schemaPattern, typeNamePattern, collection::add);
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getSuperTypes(String, String, String) getSuperTypes(catalog, schemaPattern,
     * typeNamePattern)} method with given arguments, and returns a list of bound values.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter.
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSuperTypes(String, String, String)
     */
    public List<SuperType> getSuperTypes(final String catalog, final String schemaPattern, final String typeNamePattern)
            throws SQLException {
        return addSuperTypes(catalog, schemaPattern, typeNamePattern, new ArrayList<>());
    }

    List<SuperType> getSuperTypes(final Schema schema, final String typeNamePattern) throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getSuperTypes(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                typeNamePattern
        );
    }

    List<SuperType> getSuperTypes(final UDT udt) throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        return getSuperTypes(
                udt.getTypeCat(),
                udt.getTypeSchem(),
                udt.getTypeName()
        );
    }

    // ---------------------------------------------------- getTablePrivileges(catalog, schemaPattern, tableNamePattern)

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
    void acceptTablePrivileges(final String catalog, final String schemaPattern, final String tableNamePattern,
                               final Consumer<? super TablePrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            assert results != null;
            acceptBound(results, TablePrivilege.class, consumer);
        }
    }

    <T extends Collection<? super TablePrivilege>> T addTablePrivileges(final String catalog,
                                                                        final String schemaPattern,
                                                                        final String tableNamePattern,
                                                                        final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptTablePrivileges(
                catalog,
                schemaPattern,
                tableNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)
     * getTablePrivileges(catalog, schemaPattern, tableNamePattern)} method with given arguments, and returns a list of
     * bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTablePrivileges(String, String, String)
     */
    public List<TablePrivilege> getTablePrivileges(final String catalog, final String schemaPattern,
                                                   final String tableNamePattern)
            throws SQLException {
        return addTablePrivileges(
                catalog,
                schemaPattern,
                tableNamePattern,
                new ArrayList<>()
        );
    }

    List<TablePrivilege> getTablePrivileges(final Catalog catalog, final String tableNamePattern) throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getTablePrivileges(
                catalog.getTableCat(),
                null,
                tableNamePattern
        );
    }

    List<TablePrivilege> getTablePrivileges(final Schema schema, final String tableNamePattern) throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getTablePrivileges(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                tableNamePattern
        );
    }

    List<TablePrivilege> getTablePrivileges(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getTablePrivileges(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName()
        );
    }

    // ------------------------------------------------------------------------------------------------- getTableTypes()

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTableTypes()
     */
    void acceptTableTypes(final Consumer<? super TableType> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getTableTypes()) {
            assert results != null;
            acceptBound(
                    results,
                    TableType.class,
                    consumer
            );
        }
    }

    <T extends Collection<? super TableType>> T addTableTypes(final T collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptTableTypes(collection::add);
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<TableType> getTableTypes() throws SQLException {
        return addTableTypes(new ArrayList<>());
    }

    // ------------------------------------------------------ getTables(catalog, schemaPattern, tableNamePattern, types)

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
    void acceptTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                      final String[] types, final Consumer<? super Table> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            assert results != null;
            acceptBound(results, Table.class, consumer);
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
     * @see #acceptTables(String, String, String, String[], Consumer)
     */
    <T extends Collection<? super Table>> T addTables(final String catalog, final String schemaPattern,
                                                      final String tableNamePattern, final String[] types,
                                                      final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptTables(
                catalog,
                schemaPattern,
                tableNamePattern,
                types,
                collection::add
        );
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
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    public List<Table> getTables(final String catalog, final String schemaPattern, final String tableNamePattern,
                                 final String[] types)
            throws SQLException {
        return addTables(catalog, schemaPattern, tableNamePattern, types, new ArrayList<>());
    }

    List<Table> getTables(final Schema schema, final String tableNamePattern, final String[] types)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getTables(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                tableNamePattern,
                types
        );
    }

    // --------------------------------------------------------------------------------------------------- getTypeInfo()

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are added.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTypeInfo()
     */
    void acceptTypeInfo(final Consumer<? super TypeInfo> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getTypeInfo()) {
            assert results != null;
            acceptBound(results, TypeInfo.class, consumer);
        }
    }

    <T extends Collection<? super TypeInfo>> T addTypeInfo(final T collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptTypeInfo(collection::add);
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<TypeInfo> getTypeInfo() throws SQLException {
        return addTypeInfo(new ArrayList<>());
    }

    // --------------------------------------------------------- getUDTs(catalog, schemaPattern, typeNamePattern, types)

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
    void acceptUDTs(final String catalog, final String schemaPattern, final String typeNamePattern,
                    final int[] types, final Consumer<? super UDT> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
            assert results != null;
            acceptBound(results, UDT.class, consumer);
        }
    }

    <T extends Collection<? super UDT>> T addUDTs(final String catalog, final String schemaPattern,
                                                  final String typeNamePattern, final int[] types, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptUDTs(catalog, schemaPattern, typeNamePattern, types, collection::add);
        return collection;
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
     * @see DatabaseMetaData#getUDTs(String, String, String, int[])
     */
    public List<UDT> getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern,
                             final int[] types)
            throws SQLException {
        return addUDTs(catalog, schemaPattern, typeNamePattern, types, new ArrayList<>());
    }

    // ----------------------------------------------------------------------- getVersionColumns(catalog, schema, table)

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
    void acceptVersionColumns(final String catalog, final String schema, final String table,
                              final Consumer<? super VersionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = databaseMetaData.getVersionColumns(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, VersionColumn.class, consumer);
        }
    }

    <T extends Collection<? super VersionColumn>> T addVersionColumns(final String catalog, final String schema,
                                                                      final String table, final T collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        acceptVersionColumns(catalog, schema, table, collection::add);
        return collection;
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
     * @see DatabaseMetaData#getVersionColumns(String, String, String)
     */
    public List<VersionColumn> getVersionColumns(final String catalog, final String schema, final String table)
            throws SQLException {
        return addVersionColumns(catalog, schema, table, new ArrayList<>());
    }

    List<VersionColumn> getVersionColumns(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getVersionColumns(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName()
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Map<Field, _ColumnLabel> getLabeledFields(final Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz is null");
        return Collections.unmodifiableMap(
                classesAndLabeledFields.computeIfAbsent(clazz, c -> Utils.getFieldsAnnotatedWith(c, _ColumnLabel.class))
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    final DatabaseMetaData databaseMetaData;

    // -----------------------------------------------------------------------------------------------------------------
    private final Map<Class<?>, Map<Field, _ColumnLabel>> classesAndLabeledFields = new HashMap<>();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link DatabaseMetaData#getNumericFunctions()}, and returns the result as a list of comma-split
     * elements.
     *
     * @return a list of numeric functions.
     * @throws SQLException if a database error occurs.
     */
    public List<String> getNumericFunctions() throws SQLException {
        return Arrays.stream(databaseMetaData.getNumericFunctions().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Invokes {@link DatabaseMetaData#getSQLKeywords()}, and returns the result as a list of comma-split elements.
     *
     * @return a list of SQL keywords.
     * @throws SQLException if a database error occurs.
     */
    public List<String> getSQLKeywords() throws SQLException {
        return Arrays.stream(databaseMetaData.getSQLKeywords().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Invokes {@link DatabaseMetaData#getStringFunctions()}, and returns the result as a list of comma-split elements.
     *
     * @return a list of string functions.
     * @throws SQLException if a database error occurs.
     */
    public List<String> getStringFunctions() throws SQLException {
        return Arrays.stream(databaseMetaData.getStringFunctions().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Invokes {@link DatabaseMetaData#getSystemFunctions()}, and returns the result as a list of comma-split elements.
     *
     * @return a list of system functions.
     * @throws SQLException if a database error occurs.
     */
    public List<String> getSystemFunctions() throws SQLException {
        return Arrays.stream(databaseMetaData.getSystemFunctions().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Invokes {@link DatabaseMetaData#getTimeDateFunctions()}, and returns the result as a list of comma-split
     * elements.
     *
     * @return a list of time and date functions.
     * @throws SQLException if a database error occurs.
     */
    public List<String> getTimeDateFunctions() throws SQLException {
        return Arrays.asList(databaseMetaData.getTimeDateFunctions().split(","));
    }

    // -----------------------------------------------------------------------------------------------------------------
    Supplier<Connection> connectionSupplier;
}
