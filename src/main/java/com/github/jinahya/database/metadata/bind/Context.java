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

import org.jspecify.annotations.Nullable;

import java.lang.invoke.MethodHandles;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A class for retrieving information from an instance of {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Context {

    private static final System.Logger logger = System.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Splits a comma-separated string into an unmodifiable list of non-blank, stripped elements.
     *
     * @param commaSeparated a comma-separated string; may be {@code null} (some drivers return {@code null} despite the
     *                       specification).
     * @return an unmodifiable list of elements; empty when {@code commaSeparated} is {@code null} or blank.
     */
    private static List<String> commaSplitToUnmodifiableList(final String commaSeparated) {
        if (commaSeparated == null) {
            return List.of();
        }
        return Arrays.stream(commaSeparated.split(","))
                .map(String::strip)
                .filter(v -> !v.isBlank())
                .toList();
    }

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    /**
     * Creates a new instance from the specified connection.
     *
     * @param connection the connection.
     * @return a new instance.
     * @throws SQLException if a database error occurs.
     * @see Connection#getMetaData()
     * @see #Context(DatabaseMetaData)
     */
    public static Context newInstance(final Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "connection is null");
        return new Context(connection.getMetaData());
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance with specified instance of {@link DatabaseMetaData}.
     *
     * @param metadata the instance of {@link DatabaseMetaData} to proxy.
     */
    public Context(final DatabaseMetaData metadata) {
        super();
        this.metadata = Objects.requireNonNull(metadata, "metadata is null");
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
        final var resultLabels = ContextUtils.getLabels(results);
        final var fieldLabels = new HashMap<>(getLabeledFields(type));
        for (final var i = fieldLabels.entrySet().iterator(); i.hasNext(); ) {
            final var entry = i.next();
            final var field = entry.getKey();
            final _ColumnLabel fieldLabel = entry.getValue();
            if (!resultLabels.remove(fieldLabel.value())) {
                logger.log(
                        System.Logger.Level.WARNING,
                        () -> String.format("unmapped field; label: %s; field: %s", fieldLabel, field)
                );
                i.remove();
                continue;
            }
            try {
                ContextUtils.setFieldValue(field, instance, results, fieldLabel.value());
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException("failed to set " + field, roe);
            }
            i.remove();
        }
        for (final var i = resultLabels.iterator(); i.hasNext(); i.remove()) {
            final String label = i.next();
            final Object value = results.getObject(label);
            logger.log(System.Logger.Level.TRACE,
                       "unknown column; type: {0}, label: {1}, value: {2}", type.getSimpleName(), label, value);
            if (instance instanceof AbstractMetadataType metadata) {
                metadata.putUnknownColumn(label, value);
            }
        }
        assert resultLabels.isEmpty() : "remaining result labels: " + resultLabels;
        assert fieldLabels.isEmpty() : "remaining field labels: " + fieldLabels;
        return instance;
    }

    /**
     * Binds all records into the given type and adds them to the specified consumer.
     *
     * @param results  the records to bind.
     * @param type     the type of instances.
     * @param consumer the consumer to which bound instances are added
     * @param <T>      binding type parameter
     * @throws SQLException if a database error occurs.
     */
    @SuppressWarnings({
            "java:S112", // new RuntimeException
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
        if (!constructor.canAccess(null)) {
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

    // --------------------------------------------------------------------------------------------------- getAttributes

    /**
     * Invokes {@link DatabaseMetaData#getAttributes(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and accepts each bound value to the specified consumer.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for the {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for the {@code attributeNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getAttributes(String, String, String, String)
     */
    void getAttributesAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                                    final String typeNamePattern, final String attributeNamePattern,
                                    final Consumer<? super Attribute> consumer)
            throws SQLException {
        try (var results = metadata.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern)) {
            assert results != null;
            acceptBound(
                    results,
                    Attribute.class,
                    consumer
            );
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getAttributes(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for the {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for the {@code attributeNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <C>                  collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super Attribute>>
    C getAttributesAndAddAll(@Nullable final String catalog, @Nullable final String schemaPattern,
                             final String typeNamePattern, final String attributeNamePattern, final C collection)
            throws SQLException {
        getAttributesAndAcceptEach(
                catalog,
                schemaPattern,
                typeNamePattern,
                attributeNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getAttributes(String, String, String, String)} method and accepts each bound
     * value to the specified consumer.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for the {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for the {@code attributeNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachAttribute(@Nullable final String catalog, @Nullable final String schemaPattern,
                                 final String typeNamePattern, final String attributeNamePattern,
                                 final Consumer<? super Attribute> consumer)
            throws SQLException {
        getAttributesAndAcceptEach(catalog, schemaPattern, typeNamePattern, attributeNamePattern, consumer);
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method, on the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for the {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for the {@code attributeNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Attribute> getAttributes(@Nullable final String catalog, @Nullable final String schemaPattern,
                                         final String typeNamePattern, final String attributeNamePattern)
            throws SQLException {
        return getAttributesAndAddAll(
                catalog,
                schemaPattern,
                typeNamePattern,
                attributeNamePattern,
                new ArrayList<>()
        );
    }

    void forEachAttribute(final Consumer<? super Attribute> consumer) throws SQLException {
        getAttributesAndAcceptEach(null, null, "%", "%", consumer);
    }

    List<Attribute> getAllAttributes() throws SQLException {
        return getAttributes(null, null, "%", "%");
    }

    /**
     * Retrieves attributes of the specified user-defined type.
     *
     * @param udt                  the user-defined type whose attributes are retrieved.
     * @param attributeNamePattern a value for the {@code attributeNamePattern} parameter.
     * @return a list of attributes of the {@code udt}.
     * @throws SQLException if a database error occurs.
     * @see #getAttributes(String, String, String, String)
     */
    List<Attribute> getAttributesOf(final UDT udt, final String attributeNamePattern) throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        return getAttributes(
                udt.getEffectiveTypeCat(),
                udt.getEffectiveTypeSchem(),
                udt.getTypeName(),
                attributeNamePattern
        );
    }

    void forEachAttributeOf(final UDT udt, final String attributeNamePattern,
                            final Consumer<? super Attribute> consumer)
            throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        forEachAttribute(
                udt.getEffectiveTypeCat(),
                udt.getEffectiveTypeSchem(),
                udt.getTypeName(),
                attributeNamePattern,
                consumer
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method, on the
     * wrapped {@link #metadata}, with given arguments, and accepts each bound value to the specified consumer.
     *
     * @param catalog  a value for the {@code catalog} parameter.
     * @param schema   a value for the {@code schema} parameter.
     * @param table    a value for the {@code table} parameter.
     * @param scope    a value for the {@code scope} parameter.
     * @param nullable a value for the {@code nullable} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)
     */
    void getBestRowIdentifierAndAcceptEach(@Nullable final String catalog, @Nullable final String schema,
                                           final String table, final int scope, final boolean nullable,
                                           final Consumer<? super BestRowIdentifier> consumer)
            throws SQLException {
        try (var results = metadata.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            assert results != null;
            acceptBound(
                    results,
                    BestRowIdentifier.class,
                    consumer
            );
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method, on the
     * wrapped {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog    a value for the {@code catalog} parameter.
     * @param schema     a value for the {@code schema} parameter.
     * @param table      a value for the {@code table} parameter.
     * @param scope      a value for the {@code scope} parameter.
     * @param nullable   a value for the {@code nullable} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super BestRowIdentifier>>
    C getBestRowIdentifierAndAddAll(@Nullable final String catalog, @Nullable final String schema, final String table,
                                    final int scope, final boolean nullable, final C collection)
            throws SQLException {
        getBestRowIdentifierAndAcceptEach(
                catalog,
                schema,
                table,
                scope,
                nullable,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method and accepts
     * each bound value to the specified consumer.
     *
     * @param catalog  a value for the {@code catalog} parameter.
     * @param schema   a value for the {@code schema} parameter.
     * @param table    a value for the {@code table} parameter.
     * @param scope    a value for the {@code scope} parameter.
     * @param nullable a value for the {@code nullable} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachBestRowIdentifier(@Nullable final String catalog, @Nullable final String schema,
                                         final String table, final int scope, final boolean nullable,
                                         final Consumer<? super BestRowIdentifier> consumer)
            throws SQLException {
        getBestRowIdentifierAndAcceptEach(catalog, schema, table, scope, nullable, consumer);
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)}
     * method, on the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog  a value for the {@code catalog} parameter.
     * @param schema   a value for the {@code schema} parameter.
     * @param table    a value for the {@code table} parameter.
     * @param scope    a value for the {@code scope} parameter.
     * @param nullable a value for the {@code nullable} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)
     */
    public List<BestRowIdentifier> getBestRowIdentifier(@Nullable final String catalog, @Nullable final String schema,
                                                        final String table, final int scope, final boolean nullable)
            throws SQLException {
        return getBestRowIdentifierAndAddAll(
                catalog,
                schema,
                table,
                scope,
                nullable,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves the optimal set of columns that uniquely identifies a row of the specified table.
     *
     * @param table    the table whose best row identifier is retrieved.
     * @param scope    a value for the {@code scope} parameter.
     * @param nullable a value for the {@code nullable} parameter.
     * @return a list of bound values for the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getBestRowIdentifier(String, String, String, int, boolean)
     */
    List<BestRowIdentifier> getBestRowIdentifierOf(final Table table, final int scope, final boolean nullable)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getBestRowIdentifier(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                scope,
                nullable
        );
    }

    void forEachBestRowIdentifierOf(final Table table, final int scope, final boolean nullable,
                                    final Consumer<? super BestRowIdentifier> consumer)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachBestRowIdentifier(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                scope,
                nullable,
                consumer
        );
    }

    // ----------------------------------------------------------------------------------------------------- getCatalogs

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method, on the wrapped {@link #metadata}, and accepts each bound
     * value to the specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getCatalogs()
     */
    void getCatalogsAndAcceptEach(final Consumer<? super Catalog> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getCatalogs()) {
            assert results != null;
            acceptBound(results, Catalog.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method, on the wrapped {@link #metadata}, and adds each bound
     * value to the specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super Catalog>> C getCatalogsAndAddAll(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getCatalogsAndAcceptEach(
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method and accepts each bound value to the specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachCatalog(final Consumer<? super Catalog> consumer) throws SQLException {
        getCatalogsAndAcceptEach(consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method, on the wrapped {@link #metadata}, and returns a list of
     * bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Catalog> getCatalogs() throws SQLException {
        return getCatalogsAndAddAll(new ArrayList<>());
    }

    // ----------------------------------------------------------------------------------------- getClientInfoProperties

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method, and accepts each bound value to specified
     * consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    void getClientInfoPropertiesAndAcceptEach(final Consumer<? super ClientInfoProperty> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getClientInfoProperties()) {
            assert results != null;
            acceptBound(results, ClientInfoProperty.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method, on the wrapped {@link #metadata}, and adds
     * each bound value to the specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super ClientInfoProperty>> C getClientInfoPropertiesAndAddAll(final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getClientInfoPropertiesAndAcceptEach(
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and accepts each bound value to the specified
     * consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachClientInfoProperty(final Consumer<? super ClientInfoProperty> consumer) throws SQLException {
        getClientInfoPropertiesAndAcceptEach(consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties() getClientInfoProperties()} method, on the wrapped
     * {@link #metadata}, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public List<ClientInfoProperty> getClientInfoProperties() throws SQLException {
        return getClientInfoPropertiesAndAddAll(new ArrayList<>());
    }

    // --------------------------------------------------------------------------------------------- getColumnPrivileges

    /**
     * Invokes {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and accepts each bound value to the specified consumer.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schema            a value for the {@code schema} parameter.
     * @param table             a value for the {@code table} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    void getColumnPrivilegesAndAcceptEach(@Nullable final String catalog, @Nullable final String schema,
                                          final String table, final String columnNamePattern,
                                          final Consumer<? super ColumnPrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            assert results != null;
            acceptBound(results, ColumnPrivilege.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schema            a value for the {@code schema} parameter.
     * @param table             a value for the {@code table} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super ColumnPrivilege>>
    C getColumnPrivilegesAndAddAll(@Nullable final String catalog, @Nullable final String schema, final String table,
                                   final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getColumnPrivilegesAndAcceptEach(
                catalog,
                schema,
                table,
                columnNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)} method and accepts each
     * bound value to the specified consumer.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schema            a value for the {@code schema} parameter.
     * @param table             a value for the {@code table} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachColumnPrivilege(@Nullable final String catalog, @Nullable final String schema,
                                       final String table, final String columnNamePattern,
                                       final Consumer<? super ColumnPrivilege> consumer)
            throws SQLException {
        getColumnPrivilegesAndAcceptEach(catalog, schema, table, columnNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schema            a value for the {@code schema} parameter.
     * @param table             a value for the {@code table} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ColumnPrivilege> getColumnPrivileges(@Nullable final String catalog, @Nullable final String schema,
                                                     final String table, final String columnNamePattern)
            throws SQLException {
        return getColumnPrivilegesAndAddAll(catalog, schema, table, columnNamePattern, new ArrayList<>());
    }

    List<ColumnPrivilege> getColumnPrivilegesOf(final Table table, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getColumnPrivileges(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                columnNamePattern
        );
    }

    void forEachColumnPrivilegeOf(final Table table, final String columnNamePattern,
                                  final Consumer<? super ColumnPrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachColumnPrivilege(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                columnNamePattern,
                consumer
        );
    }

    // ------------------------------------------------------------------------------------------------------ getColumns

    /**
     * Invokes {@link DatabaseMetaData#getColumns(String, String, String, String)} method, on the wrapped metadata, with
     * given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    void getColumnsAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                                 final String tableNamePattern, final String columnNamePattern,
                                 final Consumer<? super Column> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            assert results != null;
            acceptBound(results, Column.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumns(String, String, String, String)} method, on the wrapped metadata, with
     * given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see #getColumnsAndAcceptEach(String, String, String, String, Consumer)
     */
    <C extends Collection<? super Column>> C getColumnsAndAddAll(@Nullable final String catalog,
                                                                 @Nullable final String schemaPattern,
                                                                 final String tableNamePattern,
                                                                 final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getColumnsAndAcceptEach(
                catalog,
                schemaPattern,
                tableNamePattern,
                columnNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumns(String, String, String, String)} method and accepts each bound value
     * to the specified consumer.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schemaPattern     a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for the {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachColumn(@Nullable final String catalog, @Nullable final String schemaPattern,
                              final String tableNamePattern, final String columnNamePattern,
                              final Consumer<? super Column> consumer)
            throws SQLException {
        getColumnsAndAcceptEach(catalog, schemaPattern, tableNamePattern, columnNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumns(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Column> getColumns(@Nullable final String catalog, @Nullable final String schemaPattern,
                                   final String tableNamePattern, final String columnNamePattern)
            throws SQLException {
        return getColumnsAndAddAll(catalog, schemaPattern, tableNamePattern, columnNamePattern, new ArrayList<>());
    }

    void forEachColumn(final Consumer<? super Column> consumer) throws SQLException {
        getColumnsAndAcceptEach(null, null, "%", "%", consumer);
    }

    List<Column> getAllColumns() throws SQLException {
        return getColumns(null, null, "%", "%");
    }

    List<Column> getColumnsOf(final Table table, final String columnNamePattern) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getColumns(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                columnNamePattern
        );
    }

    void forEachColumnOf(final Table table, final String columnNamePattern,
                         final Consumer<? super Column> consumer)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachColumn(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                columnNamePattern,
                consumer
        );
    }

    // ----------------------------------------------------------------------------------------------- getCrossReference

    /**
     * Invokes
     * {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)} method, on the wrapped metadata, with given arguments, and accepts each
     * bound value to specified consumer.
     *
     * @param parentCatalog  a value for {@code parentCatalog} parameter
     * @param parentSchema   a value for {@code parentSchema} parameter
     * @param parentTable    a value for {@code parentTable} parameter
     * @param foreignCatalog a value for {@code foreignCatalog} parameter
     * @param foreignSchema  a value for {@code foreignSchema} parameter
     * @param foreignTable   a value for {@code foreignTable} parameter
     * @param consumer       the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getCrossReference(String, String, String, String, String, String)
     */
    void getCrossReferenceAndAcceptEach(final String parentCatalog, final String parentSchema, final String parentTable,
                                        final String foreignCatalog, final String foreignSchema,
                                        final String foreignTable,
                                        final Consumer<? super CrossReference> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog,
                                                      foreignSchema, foreignTable)) {
            assert results != null;
            acceptBound(results, CrossReference.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method, on the
     * wrapped {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param parentCatalog  a value for the {@code parentCatalog} parameter.
     * @param parentSchema   a value for the {@code parentSchema} parameter.
     * @param parentTable    a value for the {@code parentTable} parameter.
     * @param foreignCatalog a value for the {@code foreignCatalog} parameter.
     * @param foreignSchema  a value for the {@code foreignSchema} parameter.
     * @param foreignTable   a value for the {@code foreignTable} parameter.
     * @param collection     the collection to which bound values are added.
     * @param <C>            collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super CrossReference>> C
    getCrossReferenceAndAddAll(final String parentCatalog, final String parentSchema, final String parentTable,
                               final String foreignCatalog, final String foreignSchema, final String foreignTable,
                               final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getCrossReferenceAndAcceptEach(
                parentCatalog,
                parentSchema,
                parentTable,
                foreignCatalog,
                foreignSchema,
                foreignTable,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method and
     * accepts each bound value to the specified consumer.
     *
     * @param parentCatalog  a value for the {@code parentCatalog} parameter.
     * @param parentSchema   a value for the {@code parentSchema} parameter.
     * @param parentTable    a value for the {@code parentTable} parameter.
     * @param foreignCatalog a value for the {@code foreignCatalog} parameter.
     * @param foreignSchema  a value for the {@code foreignSchema} parameter.
     * @param foreignTable   a value for the {@code foreignTable} parameter.
     * @param consumer       the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachCrossReference(@Nullable final String parentCatalog, @Nullable final String parentSchema,
                                      final String parentTable, @Nullable final String foreignCatalog,
                                      @Nullable final String foreignSchema, final String foreignTable,
                                      final Consumer<? super CrossReference> consumer)
            throws SQLException {
        getCrossReferenceAndAcceptEach(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema,
                                       foreignTable, consumer);
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)} method, on the wrapped {@link #metadata}, with given arguments, and returns
     * a list of bound values.
     *
     * @param parentCatalog  a value for the {@code parentCatalog} parameter
     * @param parentSchema   a value for the {@code parentSchema} parameter
     * @param parentTable    a value for the {@code parentTable} parameter
     * @param foreignCatalog a value for the {@code foreignCatalog} parameter
     * @param foreignSchema  a value for the {@code foreignSchema} parameter
     * @param foreignTable   a value for the {@code foreignTable} parameter
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<CrossReference> getCrossReference(@Nullable final String parentCatalog,
                                                  @Nullable final String parentSchema, final String parentTable,
                                                  @Nullable final String foreignCatalog,
                                                  @Nullable final String foreignSchema, final String foreignTable)
            throws SQLException {
        return getCrossReferenceAndAddAll(
                parentCatalog,
                parentSchema,
                parentTable,
                foreignCatalog,
                foreignSchema,
                foreignTable,
                new ArrayList<>()
        );
    }

    List<CrossReference> getCrossReferenceOf(final Table parentTable, final Table foreignTable) throws SQLException {
        Objects.requireNonNull(parentTable, "parentTable is null");
        Objects.requireNonNull(foreignTable, "foreignTable is null");
        return getCrossReference(
                parentTable.getEffectiveTableCat(),
                parentTable.getEffectiveTableSchem(),
                parentTable.getTableName(),
                foreignTable.getEffectiveTableCat(),
                foreignTable.getEffectiveTableSchem(),
                foreignTable.getTableName()
        );
    }

    void forEachCrossReferenceOf(final Table parentTable, final Table foreignTable,
                                 final Consumer<? super CrossReference> consumer)
            throws SQLException {
        Objects.requireNonNull(parentTable, "parentTable is null");
        Objects.requireNonNull(foreignTable, "foreignTable is null");
        forEachCrossReference(
                parentTable.getEffectiveTableCat(),
                parentTable.getEffectiveTableSchem(),
                parentTable.getTableName(),
                foreignTable.getEffectiveTableCat(),
                foreignTable.getEffectiveTableSchem(),
                foreignTable.getTableName(),
                consumer
        );
    }

    // ------------------------------------------------------------------------------------------------- getExportedKeys

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
    void getExportedKeysAndAcceptEach(final String catalog, final String schema, final String table,
                                      final Consumer<? super ExportedKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getExportedKeys(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, ExportedKey.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog    a value for the {@code catalog} parameter.
     * @param schema     a value for the {@code schema} parameter.
     * @param table      a value for the {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super ExportedKey>> C
    getExportedKeysAndAddAll(final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getExportedKeysAndAcceptEach(
                catalog,
                schema,
                table,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(String, String, String)} method and accepts each bound value to
     * the specified consumer.
     *
     * @param catalog  a value for the {@code catalog} parameter.
     * @param schema   a value for the {@code schema} parameter.
     * @param table    a value for the {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachExportedKey(@Nullable final String catalog, @Nullable final String schema, final String table,
                                   final Consumer<? super ExportedKey> consumer)
            throws SQLException {
        getExportedKeysAndAcceptEach(catalog, schema, table, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method, on
     * the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog a value for the {@code catalog} parameter.
     * @param schema  a value for the {@code schema} parameter.
     * @param table   a value for the {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ExportedKey> getExportedKeys(@Nullable final String catalog, @Nullable final String schema,
                                             final String table)
            throws SQLException {
        return getExportedKeysAndAddAll(
                catalog,
                schema,
                table,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves exported keys of the specified table.
     *
     * @param table the table whose exported keys are retrieved.
     * @return a list of exported keys of the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getExportedKeys(String, String, String)
     */

    List<ExportedKey> getExportedKeysOf(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getExportedKeys(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName()
        );
    }

    void forEachExportedKeyOf(final Table table, final Consumer<? super ExportedKey> consumer) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachExportedKey(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                consumer
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
    void getFunctionsAndAcceptEach(final String catalog, final String schemaPattern, final String functionNamePattern,
                                   final Consumer<? super Function> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getFunctions(catalog, schemaPattern, functionNamePattern)) {
            assert results != null;
            acceptBound(results, Function.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog             a value for the {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @param collection          the collection to which bound values are added.
     * @param <C>                 collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super Function>>
    C getFunctionsAndAddAll(final String catalog, final String schemaPattern, final String functionNamePattern,
                            final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getFunctionsAndAcceptEach(
                catalog,
                schemaPattern,
                functionNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(String, String, String)} method and accepts each bound value to the
     * specified consumer.
     *
     * @param catalog             a value for the {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @param consumer            the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachFunction(@Nullable final String catalog, @Nullable final String schemaPattern,
                                @Nullable final String functionNamePattern,
                                final Consumer<? super Function> consumer)
            throws SQLException {
        getFunctionsAndAcceptEach(catalog, schemaPattern, functionNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and returns a list of bound values.
     *
     * @param catalog             a value for the {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     */
    public List<Function> getFunctions(@Nullable final String catalog, @Nullable final String schemaPattern,
                                       @Nullable final String functionNamePattern)
            throws SQLException {
        return getFunctionsAndAddAll(
                catalog, schemaPattern, functionNamePattern,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves functions, optionally scoped to the specified catalog.
     *
     * @param catalog             the catalog whose {@link Catalog#getTableCat() tableCat} is used for the
     *                            {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getFunctions(String, String, String)
     */
    List<Function> getFunctionsOf(final Catalog catalog, @Nullable final String schemaPattern,
                                  @Nullable final String functionNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getFunctions(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                functionNamePattern
        );
    }

    void forEachFunctionOf(final Catalog catalog, @Nullable final String schemaPattern,
                           @Nullable final String functionNamePattern, final Consumer<? super Function> consumer)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        forEachFunction(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                functionNamePattern,
                consumer
        );
    }

    /**
     * Retrieves functions scoped to the specified schema.
     *
     * @param schema              the schema whose {@link Schema#getTableCatalog() tableCatalog} and
     *                            {@link Schema#getTableSchem() tableSchem} are used for the {@code catalog} and
     *                            {@code schemaPattern} parameters.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getFunctions(String, String, String)
     */
    List<Function> getFunctionsOf(final Schema schema, @Nullable final String functionNamePattern)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getFunctions(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                functionNamePattern
        );
    }

    void forEachFunctionOf(final Schema schema, @Nullable final String functionNamePattern,
                           final Consumer<? super Function> consumer)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        forEachFunction(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                functionNamePattern,
                consumer
        );
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(String, String, String)} method with {@code (null, null, "%")}, and
     * accepts each bound value to the specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     * @see #forEachFunction(String, String, String, Consumer)
     */
    void forEachFunction(final Consumer<? super Function> consumer) throws SQLException {
        getFunctionsAndAcceptEach(null, null, "%", consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctions(String, String, String)} method with {@code (null, null, "%")}, and
     * returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     * @see #getFunctions(String, String, String)
     */
    List<Function> getAllFunctions() throws SQLException {
        return getFunctions(null, null, "%");
    }

    // ------------------------------ getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern)

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and accepts each bound value to the specified consumer.
     *
     * @param catalog             a value for the {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for the {@code columnNamePattern} parameter.
     * @param consumer            the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctionColumns(String, String, String, String)
     */
    void getFunctionColumnsAndAcceptEach(final String catalog, final String schemaPattern,
                                         final String functionNamePattern, final String columnNamePattern,
                                         final Consumer<? super FunctionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getFunctionColumns(catalog, schemaPattern, functionNamePattern,
                                                       columnNamePattern)) {
            assert results != null;
            acceptBound(results, FunctionColumn.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog             a value for the {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for the {@code columnNamePattern} parameter.
     * @param collection          the collection to which bound values are added.
     * @param <C>                 collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super FunctionColumn>>
    C getFunctionColumnsAndAddAll(final String catalog, final String schemaPattern, final String functionNamePattern,
                                  final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getFunctionColumnsAndAcceptEach(
                catalog,
                schemaPattern,
                functionNamePattern,
                columnNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method and accepts each bound
     * value to the specified consumer.
     *
     * @param catalog             a value for the {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for the {@code columnNamePattern} parameter.
     * @param consumer            the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachFunctionColumn(@Nullable final String catalog, @Nullable final String schemaPattern,
                                      final String functionNamePattern, final String columnNamePattern,
                                      final Consumer<? super FunctionColumn> consumer)
            throws SQLException {
        getFunctionColumnsAndAcceptEach(catalog, schemaPattern, functionNamePattern, columnNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)} method, on the wrapped
     * {@link #metadata} ,with specified arguments, and returns a list of bound values.
     *
     * @param catalog             a value for the {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @param columnNamePattern   a value for the {@code columnNamePattern} parameter.
     * @return a list of found values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctionColumns(String, String, String, String)
     * @see #getFunctionColumns(String, String, String, String)
     */
    public List<FunctionColumn> getFunctionColumns(@Nullable final String catalog, @Nullable final String schemaPattern,
                                                   final String functionNamePattern, final String columnNamePattern)
            throws SQLException {
        return getFunctionColumnsAndAddAll(
                catalog, schemaPattern, functionNamePattern, columnNamePattern,
                new ArrayList<>()
        );
    }

    void forEachFunctionColumn(final Consumer<? super FunctionColumn> consumer) throws SQLException {
        getFunctionColumnsAndAcceptEach(null, null, "%", "%", consumer);
    }

    List<FunctionColumn> getAllFunctionColumns() throws SQLException {
        return getFunctionColumns(null, null, "%", "%");
    }

    /**
     * Retrieves function columns of the specified function.
     *
     * @param function          the function whose columns are retrieved.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getFunctionColumns(String, String, String, String)
     */
    List<FunctionColumn> getFunctionColumnsOf(final Function function, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(function, "function is null");
        return getFunctionColumns(
                function.getEffectiveFunctionCat(),
                function.getEffectiveFunctionSchem(),
                function.getFunctionName(),
                columnNamePattern
        );
    }

    void forEachFunctionColumnOf(final Function function, final String columnNamePattern,
                                 final Consumer<? super FunctionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(function, "function is null");
        forEachFunctionColumn(
                function.getEffectiveFunctionCat(),
                function.getEffectiveFunctionSchem(),
                function.getFunctionName(),
                columnNamePattern,
                consumer
        );
    }

    // ------------------------------------------------------------------------------------------------- getImportedKeys

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
    void getImportedKeysAndAcceptEach(@Nullable final String catalog, @Nullable final String schema, final String table,
                                      final Consumer<? super ImportedKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getImportedKeys(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, ImportedKey.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog    a value for the {@code catalog} parameter.
     * @param schema     a value for the {@code schema} parameter.
     * @param table      a value for the {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super ImportedKey>>
    C getImportedKeysAndAddAll(final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getImportedKeysAndAcceptEach(
                catalog,
                schema,
                table,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(String, String, String)} method and accepts each bound value to
     * the specified consumer.
     *
     * @param catalog  a value for the {@code catalog} parameter.
     * @param schema   a value for the {@code schema} parameter.
     * @param table    a value for the {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachImportedKey(@Nullable final String catalog, @Nullable final String schema, final String table,
                                   final Consumer<? super ImportedKey> consumer)
            throws SQLException {
        getImportedKeysAndAcceptEach(catalog, schema, table, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getImportedKeys(String, String, String) getImportedKeys(catalog, schema, table)}
     * method, on the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog a value for {@code catalog} parameter.
     * @param schema  a value for {@code schema} parameter.
     * @param table   a value for {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getImportedKeys(String, String, String)
     */
    public List<ImportedKey> getImportedKeys(@Nullable final String catalog, @Nullable final String schema,
                                             final String table)
            throws SQLException {
        return getImportedKeysAndAddAll(
                catalog,
                schema,
                table,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves imported keys of the specified table.
     *
     * @param table the table whose imported keys are retrieved.
     * @return a list of imported keys of the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getImportedKeys(String, String, String)
     */
    List<ImportedKey> getImportedKeysOf(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getImportedKeys(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName()
        );
    }

    void forEachImportedKeyOf(final Table table, final Consumer<? super ImportedKey> consumer) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachImportedKey(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                consumer
        );
    }

    // ---------------------------------------------------------------------------------------------------- getIndexInfo

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)} method, on the wrapped
     * {@link #metadata}, with given arguments, and accepts each bound value to the specified consumer.
     *
     * @param catalog     a value for the {@code catalog} parameter.
     * @param schema      a value for the {@code schema} parameter.
     * @param table       a value for the {@code table} parameter.
     * @param unique      a value for the {@code unique} parameter.
     * @param approximate a value for the {@code approximate} parameter.
     * @param consumer    the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    void getIndexInfoAndAcceptEach(@Nullable final String catalog, @Nullable final String schema, final String table,
                                   final boolean unique, final boolean approximate,
                                   final Consumer<? super IndexInfo> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getIndexInfo(catalog, schema, table, unique, approximate)) {
            assert results != null;
            acceptBound(
                    results,
                    IndexInfo.class,
                    consumer
            );
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog     a value for the {@code catalog} parameter.
     * @param schema      a value for the {@code schema} parameter.
     * @param table       a value for the {@code table} parameter.
     * @param unique      a value for the {@code unique} parameter.
     * @param approximate a value for the {@code approximate} parameter.
     * @param collection  the collection to which bound values are added.
     * @param <C>         collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super IndexInfo>>
    C getIndexInfoAndAddAll(final String catalog, final String schema, final String table, final boolean unique,
                            final boolean approximate, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getIndexInfoAndAcceptEach(
                catalog,
                schema,
                table,
                unique,
                approximate,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)} method and accepts each
     * bound value to the specified consumer.
     *
     * @param catalog     a value for the {@code catalog} parameter.
     * @param schema      a value for the {@code schema} parameter.
     * @param table       a value for the {@code table} parameter.
     * @param unique      a value for the {@code unique} parameter.
     * @param approximate a value for the {@code approximate} parameter.
     * @param consumer    the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachIndexInfo(@Nullable final String catalog, @Nullable final String schema, final String table,
                                 final boolean unique, final boolean approximate,
                                 final Consumer<? super IndexInfo> consumer)
            throws SQLException {
        getIndexInfoAndAcceptEach(catalog, schema, table, unique, approximate, consumer);
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean) getIndexInfo(catalog, schema,
     * table, unique, approximate)} method, on the wrapped {@link #metadata}, with specified arguments, and returns a
     * list of bound values.
     *
     * @param catalog     a value for the {@code catalog} parameter.
     * @param schema      a value for the {@code schema} parameter.
     * @param table       a value for the {@code table} parameter.
     * @param unique      a value for the {@code unique} parameter.
     * @param approximate a value for the {@code approximate} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    public List<IndexInfo> getIndexInfo(@Nullable final String catalog, @Nullable final String schema,
                                        final String table, final boolean unique, final boolean approximate)
            throws SQLException {
        return getIndexInfoAndAddAll(
                catalog, schema, table, unique, approximate,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves index information of the specified table.
     *
     * @param table       the table whose index information is retrieved.
     * @param unique      a value for the {@code unique} parameter.
     * @param approximate a value for the {@code approximate} parameter.
     * @return a list of bound values for the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getIndexInfo(String, String, String, boolean, boolean)
     */
    List<IndexInfo> getIndexInfoOf(final Table table, final boolean unique, final boolean approximate)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getIndexInfo(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                unique,
                approximate
        );
    }

    void forEachIndexInfoOf(final Table table, final boolean unique, final boolean approximate,
                            final Consumer<? super IndexInfo> consumer)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachIndexInfo(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                unique,
                approximate,
                consumer
        );
    }

    // -------------------------------------------------------------------------------------------------- getPrimaryKeys

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
    void getPrimaryKeysAndAcceptEach(@Nullable final String catalog, @Nullable final String schema, final String table,
                                     final Consumer<? super PrimaryKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getPrimaryKeys(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, PrimaryKey.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog    a value for the {@code catalog} parameter.
     * @param schema     a value for the {@code schema} parameter.
     * @param table      a value for the {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super PrimaryKey>>
    C getPrimaryKeysAndAddAll(@Nullable final String catalog, @Nullable final String schema, final String table,
                              final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getPrimaryKeysAndAcceptEach(
                catalog,
                schema,
                table,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method and accepts each bound value to
     * the specified consumer.
     *
     * @param catalog  a value for the {@code catalog} parameter.
     * @param schema   a value for the {@code schema} parameter.
     * @param table    a value for the {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachPrimaryKey(@Nullable final String catalog, @Nullable final String schema, final String table,
                                  final Consumer<? super PrimaryKey> consumer)
            throws SQLException {
        getPrimaryKeysAndAcceptEach(catalog, schema, table, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String) getPrimaryKeys(catalog, schema, table)}
     * method, on the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog a value for the {@code catalog} parameter.
     * @param schema  a value for the {@code schema} parameter.
     * @param table   a value for the {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<PrimaryKey> getPrimaryKeys(@Nullable final String catalog, @Nullable final String schema,
                                           final String table)
            throws SQLException {
        return getPrimaryKeysAndAddAll(
                catalog,
                schema,
                table,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves primary keys of the specified table.
     *
     * @param table the table whose primary keys are retrieved.
     * @return a list of primary keys of the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getPrimaryKeys(String, String, String)
     */
    List<PrimaryKey> getPrimaryKeysOf(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPrimaryKeys(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName()
        );
    }

    void forEachPrimaryKeyOf(final Table table, final Consumer<? super PrimaryKey> consumer) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachPrimaryKey(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                consumer
        );
    }

    // ---------------------------- getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern)

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)} method with given arguments,
     * and accepts each value to specified consumer.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedureColumns(String, String, String, String)
     */
    void getProcedureColumnsAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                                          final String procedureNamePattern, final String columnNamePattern,
                                          final Consumer<? super ProcedureColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getProcedureColumns(catalog, schemaPattern, procedureNamePattern,
                                                        columnNamePattern)) {
            assert results != null;
            acceptBound(results, ProcedureColumn.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for the {@code columnNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <C>                  collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super ProcedureColumn>>
    C getProcedureColumnsAndAddAll(@Nullable final String catalog, @Nullable final String schemaPattern,
                                   final String procedureNamePattern, final String columnNamePattern,
                                   final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getProcedureColumnsAndAcceptEach(
                catalog,
                schemaPattern,
                procedureNamePattern,
                columnNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)} method and accepts each
     * bound value to the specified consumer.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for the {@code columnNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachProcedureColumn(@Nullable final String catalog, @Nullable final String schemaPattern,
                                       final String procedureNamePattern, final String columnNamePattern,
                                       final Consumer<? super ProcedureColumn> consumer)
            throws SQLException {
        getProcedureColumnsAndAcceptEach(catalog, schemaPattern, procedureNamePattern, columnNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<ProcedureColumn> getProcedureColumns(@Nullable final String catalog,
                                                     @Nullable final String schemaPattern,
                                                     final String procedureNamePattern,
                                                     final String columnNamePattern)
            throws SQLException {
        return getProcedureColumnsAndAddAll(
                catalog, schemaPattern, procedureNamePattern, columnNamePattern,
                new ArrayList<>()
        );
    }

    void forEachProcedureColumn(final Consumer<? super ProcedureColumn> consumer) throws SQLException {
        getProcedureColumnsAndAcceptEach(null, null, "%", "%", consumer);
    }

    List<ProcedureColumn> getAllProcedureColumns() throws SQLException {
        return getProcedureColumns(null, null, "%", "%");
    }

    /**
     * Retrieves columns of the specified procedure.
     *
     * @param procedure         the procedure whose columns are retrieved.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @return a list of bound values for the {@code procedure}.
     * @throws SQLException if a database error occurs.
     * @see #getProcedureColumns(String, String, String, String)
     */
    List<ProcedureColumn> getProcedureColumnsOf(final Procedure procedure, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(procedure, "procedure is null");
        return getProcedureColumns(
                procedure.getEffectiveProcedureCat(), procedure.getEffectiveProcedureSchem(),
                procedure.getProcedureName(),
                columnNamePattern
        );
    }

    void forEachProcedureColumnOf(final Procedure procedure, final String columnNamePattern,
                                  final Consumer<? super ProcedureColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(procedure, "procedure is null");
        forEachProcedureColumn(
                procedure.getEffectiveProcedureCat(),
                procedure.getEffectiveProcedureSchem(),
                procedure.getProcedureName(),
                columnNamePattern,
                consumer
        );
    }

    // --------------------------------------------------------------------------------------------------- getProcedures

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method with
     * given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedures(String, String, String)
     */
    void getProceduresAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                                    final String procedureNamePattern, final Consumer<? super Procedure> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            assert results != null;
            acceptBound(results, Procedure.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <C>                  collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super Procedure>>
    C getProceduresAndAddAll(@Nullable final String catalog, @Nullable final String schemaPattern,
                             final String procedureNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getProceduresAndAcceptEach(
                catalog,
                schemaPattern,
                procedureNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(String, String, String)} method and accepts each bound value to the
     * specified consumer.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter.
     * @param consumer             the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachProcedure(@Nullable final String catalog, @Nullable final String schemaPattern,
                                 final String procedureNamePattern, final Consumer<? super Procedure> consumer)
            throws SQLException {
        getProceduresAndAcceptEach(catalog, schemaPattern, procedureNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method, on
     * the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedures(String, String, String)
     */
    public List<Procedure> getProcedures(@Nullable final String catalog, @Nullable final String schemaPattern,
                                         final String procedureNamePattern)
            throws SQLException {
        return getProceduresAndAddAll(
                catalog,
                schemaPattern,
                procedureNamePattern,
                new ArrayList<>()
        );
    }

    void forEachProcedure(final Consumer<? super Procedure> consumer) throws SQLException {
        getProceduresAndAcceptEach(null, null, "%", consumer);
    }

    List<Procedure> getAllProcedures() throws SQLException {
        return getProcedures(null, null, "%");
    }

    /**
     * Retrieves procedures, optionally scoped to the specified catalog.
     *
     * @param catalog              the catalog whose {@link Catalog#getTableCat() tableCat} is used for the
     *                             {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getProcedures(String, String, String)
     */
    List<Procedure> getProceduresOf(final Catalog catalog, @Nullable final String schemaPattern,
                                    final String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getProcedures(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                procedureNamePattern
        );
    }

    void forEachProcedureOf(final Catalog catalog, @Nullable final String schemaPattern,
                            final String procedureNamePattern, final Consumer<? super Procedure> consumer)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        forEachProcedure(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                procedureNamePattern,
                consumer
        );
    }

    /**
     * Retrieves procedures scoped to the specified schema.
     *
     * @param schema               the schema whose {@link Schema#getTableCatalog() tableCatalog} and
     *                             {@link Schema#getTableSchem() tableSchem} are used for the {@code catalog} and
     *                             {@code schemaPattern} parameters.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getProcedures(String, String, String)
     */
    List<Procedure> getProceduresOf(final Schema schema, final String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getProcedures(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                procedureNamePattern
        );
    }

    void forEachProcedureOf(final Schema schema, final String procedureNamePattern,
                            final Consumer<? super Procedure> consumer)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        forEachProcedure(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                procedureNamePattern,
                consumer
        );
    }

    // ------------------------------------------------------------------------------------------------ getPseudoColumns

    /**
     * Invokes
     * {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method with given arguments, and accepts each bound value to the specified consumer.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPseudoColumns(String, String, String, String)
     */
    void getPseudoColumnsAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                                       final String tableNamePattern, final String columnNamePattern,
                                       final Consumer<? super PseudoColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            assert results != null;
            acceptBound(
                    results, PseudoColumn.class,
                    consumer
            );
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schemaPattern     a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for the {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super PseudoColumn>>
    C getPseudoColumnsAndAddAll(@Nullable final String catalog, @Nullable final String schemaPattern,
                                final String tableNamePattern, final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getPseudoColumnsAndAcceptEach(
                catalog,
                schemaPattern,
                tableNamePattern,
                columnNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method and accepts each bound
     * value to the specified consumer.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schemaPattern     a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for the {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @param consumer          the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachPseudoColumn(@Nullable final String catalog, @Nullable final String schemaPattern,
                                    final String tableNamePattern, final String columnNamePattern,
                                    final Consumer<? super PseudoColumn> consumer)
            throws SQLException {
        getPseudoColumnsAndAcceptEach(catalog, schemaPattern, tableNamePattern, columnNamePattern, consumer);
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method, on the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schemaPattern     a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for the {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPseudoColumns(String, String, String, String)
     */
    public List<PseudoColumn> getPseudoColumns(@Nullable final String catalog, @Nullable final String schemaPattern,
                                               final String tableNamePattern, final String columnNamePattern)
            throws SQLException {
        return getPseudoColumnsAndAddAll(
                catalog, schemaPattern, tableNamePattern, columnNamePattern,
                new ArrayList<>()
        );
    }

    void forEachPseudoColumn(final Consumer<? super PseudoColumn> consumer) throws SQLException {
        getPseudoColumnsAndAcceptEach(null, null, "%", "%", consumer);
    }

    List<PseudoColumn> getAllPseudoColumns() throws SQLException {
        return getPseudoColumns(null, null, "%", "%");
    }

    /**
     * Retrieves pseudo columns of the specified table.
     *
     * @param table             the table whose pseudo columns are retrieved.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @return a list of bound values for the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getPseudoColumns(String, String, String, String)
     */
    List<PseudoColumn> getPseudoColumnsOf(final Table table, final String columnNamePattern) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPseudoColumns(
                table.getEffectiveTableCat(), table.getEffectiveTableSchem(), table.getTableName(),
                columnNamePattern
        );
    }

    void forEachPseudoColumnOf(final Table table, final String columnNamePattern,
                               final Consumer<? super PseudoColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachPseudoColumn(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                columnNamePattern,
                consumer
        );
    }

    // ------------------------------------------------------------------------------------------------------ getSchemas

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas()
     */
    void getSchemasAndAcceptEach(final Consumer<? super Schema> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getSchemas()) {
            assert results != null;
            acceptBound(results, Schema.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method, and add each bound value to the specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super Schema>> C getSchemasAndAddAll(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getSchemasAndAcceptEach(
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method and accepts each bound value to the specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachSchema(final Consumer<? super Schema> consumer) throws SQLException {
        getSchemasAndAcceptEach(consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method, on the wrapped {@link #metadata}, and returns a list of
     * bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Schema> getSchemas() throws SQLException {
        return getSchemasAndAddAll(
                new ArrayList<>()
        );
    }

    // ------------------------------------------------------------------------------------------------------ getSchemas

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
    void getSchemasAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                                 final Consumer<? super Schema> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = metadata.getSchemas(catalog, schemaPattern)) {
            assert results != null;
            acceptBound(results, Schema.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method, on the wrapped {@link #metadata}, with given
     * arguments, and adds each bound value to the specified collection.
     *
     * @param catalog       a value for the {@code catalog} parameter.
     * @param schemaPattern a value for the {@code schemaPattern} parameter.
     * @param collection    the collection to which bound values are added.
     * @param <C>           collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super Schema>>
    C getSchemasAndAddAll(@Nullable final String catalog, @Nullable final String schemaPattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getSchemasAndAcceptEach(
                catalog,
                schemaPattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method and accepts each bound value to the specified
     * consumer.
     *
     * @param catalog       a value for the {@code catalog} parameter.
     * @param schemaPattern a value for the {@code schemaPattern} parameter.
     * @param consumer      the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachSchema(@Nullable final String catalog, @Nullable final String schemaPattern,
                              final Consumer<? super Schema> consumer)
            throws SQLException {
        getSchemasAndAcceptEach(catalog, schemaPattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method, on the wrapped {@link #metadata}, with given
     * arguments, and returns a list of bound values.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Schema> getSchemas(@Nullable final String catalog, @Nullable final String schemaPattern)
            throws SQLException {
        return getSchemasAndAddAll(catalog, schemaPattern, new ArrayList<>());
    }

    /**
     * Retrieves schemas, optionally scoped to the specified catalog.
     *
     * @param catalog       the catalog whose {@link Catalog#getTableCat() tableCat} is used for the {@code catalog}
     *                      parameter.
     * @param schemaPattern a value for the {@code schemaPattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getSchemas(String, String)
     */
    List<Schema> getSchemasOf(final Catalog catalog, @Nullable final String schemaPattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSchemas(
                catalog.getEffectiveTableCat(),
                schemaPattern
        );
    }

    void forEachSchemaOf(final Catalog catalog, @Nullable final String schemaPattern,
                         final Consumer<? super Schema> consumer)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        forEachSchema(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                consumer
        );
    }

    // -------------------------------------------------------------------------------------------------- getSuperTables

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
    void getSuperTablesAndAcceptEach(@Nullable final String catalog, final String schemaPattern,
                                     final String tableNamePattern, final Consumer<? super SuperTable> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
            assert results != null;
            acceptBound(results, SuperTable.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog          a value for the {@code catalog} parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param collection       the collection to which bound values are added.
     * @param <C>              collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super SuperTable>>
    C getSuperTablesAndAddAll(@Nullable final String catalog, final String schemaPattern, final String tableNamePattern,
                              final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getSuperTablesAndAcceptEach(
                catalog,
                schemaPattern,
                tableNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method and accepts each bound value to
     * the specified consumer.
     *
     * @param catalog          a value for the {@code catalog} parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param consumer         the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachSuperTable(@Nullable final String catalog, final String schemaPattern,
                                  final String tableNamePattern, final Consumer<? super SuperTable> consumer)
            throws SQLException {
        getSuperTablesAndAcceptEach(catalog, schemaPattern, tableNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and returns a list of bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<SuperTable> getSuperTables(@Nullable final String catalog, final String schemaPattern,
                                           final String tableNamePattern)
            throws SQLException {
        return getSuperTablesAndAddAll(
                catalog, schemaPattern, tableNamePattern,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves super tables, optionally scoped to the specified catalog.
     *
     * @param catalog          the catalog whose {@link Catalog#getTableCat() tableCat} is used for the {@code catalog}
     *                         parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTables(String, String, String)
     */
    List<SuperTable> getSuperTablesOf(final Catalog catalog, final String schemaPattern,
                                      final String tableNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSuperTables(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                tableNamePattern
        );
    }

    void forEachSuperTableOf(final Catalog catalog, final String schemaPattern,
                             final String tableNamePattern, final Consumer<? super SuperTable> consumer)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        forEachSuperTable(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                tableNamePattern,
                consumer
        );
    }

    /**
     * Retrieves super tables for the specified schema.
     *
     * @param schema           the schema for which super tables are retrieved.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @return a list of bound values for the {@code schema}.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTables(String, String, String)
     */
    List<SuperTable> getSuperTablesOf(final Schema schema, final String tableNamePattern) throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getSuperTables(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                tableNamePattern
        );
    }

    void forEachSuperTableOf(final Schema schema, final String tableNamePattern,
                             final Consumer<? super SuperTable> consumer)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        forEachSuperTable(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                tableNamePattern,
                consumer
        );
    }

    /**
     * Retrieves super tables of the specified table.
     *
     * @param table the table whose super tables are retrieved.
     * @return a list of bound values for the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTables(String, String, String)
     */
    List<SuperTable> getSuperTablesOf(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getSuperTables(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName()
        );
    }

    void forEachSuperTableOf(final Table table, final Consumer<? super SuperTable> consumer) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachSuperTable(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                consumer
        );
    }

    // ---------------------------------------------------------------------------------------------------- getSuperType

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
    void getSuperTypesAndAcceptEach(@Nullable final String catalog, final String schemaPattern,
                                    final String typeNamePattern, final Consumer<? super SuperType> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
            assert results != null;
            acceptBound(results, SuperType.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTypes(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog         a value for the {@code catalog} parameter.
     * @param schemaPattern   a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @param collection      the collection to which bound values are added.
     * @param <C>             collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super SuperType>>
    C getSuperTypesAndAddAll(@Nullable final String catalog, final String schemaPattern,
                             final String typeNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getSuperTypesAndAcceptEach(
                catalog,
                schemaPattern,
                typeNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTypes(String, String, String)} method and accepts each bound value to the
     * specified consumer.
     *
     * @param catalog         a value for the {@code catalog} parameter.
     * @param schemaPattern   a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @param consumer        the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachSuperType(@Nullable final String catalog, final String schemaPattern,
                                 final String typeNamePattern, final Consumer<? super SuperType> consumer)
            throws SQLException {
        getSuperTypesAndAcceptEach(catalog, schemaPattern, typeNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTypes(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and returns a list of bound values.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter.
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<SuperType> getSuperTypes(@Nullable final String catalog, final String schemaPattern,
                                         final String typeNamePattern)
            throws SQLException {
        return getSuperTypesAndAddAll(
                catalog,
                schemaPattern,
                typeNamePattern,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves super types, optionally scoped to the specified catalog.
     *
     * @param catalog         the catalog whose {@link Catalog#getTableCat() tableCat} is used for the {@code catalog}
     *                        parameter.
     * @param schemaPattern   a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTypes(String, String, String)
     */
    List<SuperType> getSuperTypesOf(final Catalog catalog, final String schemaPattern,
                                    final String typeNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSuperTypes(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                typeNamePattern
        );
    }

    void forEachSuperTypeOf(final Catalog catalog, final String schemaPattern,
                            final String typeNamePattern, final Consumer<? super SuperType> consumer)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        forEachSuperType(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                typeNamePattern,
                consumer
        );
    }

    /**
     * Retrieves super types for the specified schema.
     *
     * @param schema          the schema for which super types are retrieved.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @return a list of bound values for the {@code schema}.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTypes(String, String, String)
     */
    List<SuperType> getSuperTypesOf(final Schema schema, final String typeNamePattern)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getSuperTypes(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                typeNamePattern
        );
    }

    void forEachSuperTypeOf(final Schema schema, final String typeNamePattern,
                            final Consumer<? super SuperType> consumer)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        forEachSuperType(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                typeNamePattern,
                consumer
        );
    }

    /**
     * Retrieves super types of the specified user-defined type.
     *
     * @param udt the user-defined type whose super types are retrieved.
     * @return a list of bound values for the {@code udt}.
     * @throws SQLException if a database error occurs.
     * @see #getSuperTypes(String, String, String)
     */
    List<SuperType> getSuperTypesOf(final UDT udt) throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        return getSuperTypes(
                udt.getEffectiveTypeCat(),
                udt.getEffectiveTypeSchem(),
                udt.getTypeName()
        );
    }

    void forEachSuperTypeOf(final UDT udt, final Consumer<? super SuperType> consumer) throws SQLException {
        Objects.requireNonNull(udt, "udt is null");
        forEachSuperType(
                udt.getEffectiveTypeCat(),
                udt.getEffectiveTypeSchem(),
                udt.getTypeName(),
                consumer
        );
    }

    // ---------------------------------------------------------------------------------------------- getTablePrivileges

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments, and accepts each bound value to specified consumer.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param consumer         the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    void getTablePrivilegesAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                                         final String tableNamePattern,
                                         final Consumer<? super TablePrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            assert results != null;
            acceptBound(results, TablePrivilege.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog          a value for the {@code catalog} parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param collection       the collection to which bound values are added.
     * @param <C>              collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super TablePrivilege>>
    C getTablePrivilegesAndAddAll(@Nullable final String catalog, @Nullable final String schemaPattern,
                                  final String tableNamePattern,
                                  final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getTablePrivilegesAndAcceptEach(
                catalog,
                schemaPattern,
                tableNamePattern,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(String, String, String)} method and accepts each bound value
     * to the specified consumer.
     *
     * @param catalog          a value for the {@code catalog} parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param consumer         the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachTablePrivilege(@Nullable final String catalog, @Nullable final String schemaPattern,
                                      final String tableNamePattern,
                                      final Consumer<? super TablePrivilege> consumer)
            throws SQLException {
        getTablePrivilegesAndAcceptEach(catalog, schemaPattern, tableNamePattern, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method,
     * on the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTablePrivileges(String, String, String)
     */
    public List<TablePrivilege> getTablePrivileges(@Nullable final String catalog, @Nullable final String schemaPattern,
                                                   final String tableNamePattern)
            throws SQLException {
        return getTablePrivilegesAndAddAll(
                catalog,
                schemaPattern,
                tableNamePattern,
                new ArrayList<>()
        );
    }

    void forEachTablePrivilege(final Consumer<? super TablePrivilege> consumer) throws SQLException {
        getTablePrivilegesAndAcceptEach(null, null, "%", consumer);
    }

    List<TablePrivilege> getAllTablePrivileges() throws SQLException {
        return getTablePrivileges(null, null, "%");
    }

    /**
     * Retrieves table privileges, optionally scoped to the specified catalog.
     *
     * @param catalog          the catalog whose {@link Catalog#getTableCat() tableCat} is used for the {@code catalog}
     *                         parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getTablePrivileges(String, String, String)
     */
    List<TablePrivilege> getTablePrivilegesOf(final Catalog catalog, @Nullable final String schemaPattern,
                                              final String tableNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getTablePrivileges(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                tableNamePattern
        );
    }

    void forEachTablePrivilegeOf(final Catalog catalog, @Nullable final String schemaPattern,
                                 final String tableNamePattern, final Consumer<? super TablePrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        forEachTablePrivilege(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                tableNamePattern,
                consumer
        );
    }

    /**
     * Retrieves table privileges scoped to the specified schema.
     *
     * @param schema           the schema whose {@link Schema#getTableCatalog() tableCatalog} and
     *                         {@link Schema#getTableSchem() tableSchem} are used for the {@code catalog} and
     *                         {@code schemaPattern} parameters.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getTablePrivileges(String, String, String)
     */
    List<TablePrivilege> getTablePrivilegesOf(final Schema schema, final String tableNamePattern)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getTablePrivileges(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                tableNamePattern
        );
    }

    void forEachTablePrivilegeOf(final Schema schema, final String tableNamePattern,
                                 final Consumer<? super TablePrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        forEachTablePrivilege(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                tableNamePattern,
                consumer
        );
    }

    /**
     * Retrieves table privileges of the specified table.
     *
     * @param table the table whose privileges are retrieved.
     * @return a list of bound values for the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getTablePrivileges(String, String, String)
     */
    List<TablePrivilege> getTablePrivilegesOf(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getTablePrivileges(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName()
        );
    }

    void forEachTablePrivilegeOf(final Table table, final Consumer<? super TablePrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachTablePrivilege(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                consumer
        );
    }

    // --------------------------------------------------------------------------------------------------- getTableTypes

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTableTypes()
     */
    void getTableTypesAndAcceptEach(final Consumer<? super TableType> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getTableTypes()) {
            assert results != null;
            acceptBound(
                    results,
                    TableType.class,
                    consumer
            );
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method, on the wrapped {@link #metadata}, and adds each bound
     * value to the specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super TableType>> C getTableTypesAndAddAll(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getTableTypesAndAcceptEach(
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method and accepts each bound value to the specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachTableType(final Consumer<? super TableType> consumer) throws SQLException {
        getTableTypesAndAcceptEach(consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method, on the wrapped {@link #metadata}, and returns a list of
     * bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<TableType> getTableTypes() throws SQLException {
        return getTableTypesAndAddAll(
                new ArrayList<>()
        );
    }

    // ------------------------------------------------------------------------------------------------------- getTables

    /**
     * Invokes
     * {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
     * method with given arguments, and accepts each bound value to the specified consumer.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @param consumer         the consumer to which values are accepted.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    void getTablesAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                                final String tableNamePattern, @Nullable final String[] types,
                                final Consumer<? super Table> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            assert results != null;
            acceptBound(
                    results,
                    Table.class,
                    consumer
            );
        }
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     * getTables(catalog, schemaPattern, tableNamePattern, types)} method with given arguments, and adds each bound
     * value to the specified collection.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @param collection       the collection to which bound values are added.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super Table>>
    C getTablesAndAddAll(@Nullable final String catalog, @Nullable final String schemaPattern,
                         final String tableNamePattern, @Nullable final String[] types,
                         final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getTablesAndAcceptEach(
                catalog,
                schemaPattern,
                tableNamePattern,
                types,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTables(String, String, String, String[])} method and accepts each bound value
     * to the specified consumer.
     *
     * @param catalog          a value for the {@code catalog} parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param types            a value for the {@code types} parameter.
     * @param consumer         the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachTable(@Nullable final String catalog, @Nullable final String schemaPattern,
                             final String tableNamePattern, @Nullable final String[] types,
                             final Consumer<? super Table> consumer)
            throws SQLException {
        getTablesAndAcceptEach(catalog, schemaPattern, tableNamePattern, types, consumer);
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
     * method, on the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog          a value for the {@code catalog} parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param types            a value for the {@code types} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<Table> getTables(@Nullable final String catalog, @Nullable final String schemaPattern,
                                 final String tableNamePattern, @Nullable final String[] types)
            throws SQLException {
        return getTablesAndAddAll(
                catalog,
                schemaPattern,
                tableNamePattern,
                types,
                new ArrayList<>()
        );
    }

    void forEachTable(final Consumer<? super Table> consumer) throws SQLException {
        getTablesAndAcceptEach(null, null, "%", null, consumer);
    }

    List<Table> getAllTables() throws SQLException {
        return getTables(null, null, "%", null);
    }

    /**
     * Invokes {@link #getTables(String, String, String, String[])} method with the
     * {@link Catalog#getTableCat() tableCat} of the specified catalog and given arguments, and returns a list of bound
     * values.
     *
     * @param catalog          the catalog whose {@link Catalog#getTableCat() tableCat} is used for the {@code catalog}
     *                         parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param types            a value for the {@code types} parameter; may be {@code null}.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see Catalog#getTableCat()
     * @see #getTables(String, String, String, String[])
     */
    List<Table> getTablesOf(final Catalog catalog, @Nullable final String schemaPattern,
                            final String tableNamePattern, @Nullable final String[] types)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getTables(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                tableNamePattern,
                types
        );
    }

    void forEachTableOf(final Catalog catalog, @Nullable final String schemaPattern,
                        final String tableNamePattern, @Nullable final String[] types,
                        final Consumer<? super Table> consumer)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        forEachTable(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                tableNamePattern,
                types,
                consumer
        );
    }

    /**
     * Retrieves tables scoped to the specified schema.
     *
     * @param schema           the schema whose {@link Schema#getTableCatalog() tableCatalog} and
     *                         {@link Schema#getTableSchem() tableSchem} are used for the {@code catalog} and
     *                         {@code schemaPattern} parameters.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param types            a value for the {@code types} parameter; may be {@code null}.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getTables(String, String, String, String[])
     */
    List<Table> getTablesOf(final Schema schema, final String tableNamePattern,
                            @Nullable final String[] types)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getTables(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                tableNamePattern,
                types
        );
    }

    void forEachTableOf(final Schema schema, final String tableNamePattern, @Nullable final String[] types,
                        final Consumer<? super Table> consumer)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        forEachTable(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                tableNamePattern,
                types,
                consumer
        );
    }

    // ----------------------------------------------------------------------------------------------------- getTypeInfo

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method, and accepts each bound value to specified consumer.
     *
     * @param consumer the consumer to which bound values are added.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTypeInfo()
     */
    void getTypeInfoAndAcceptEach(final Consumer<? super TypeInfo> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getTypeInfo()) {
            assert results != null;
            acceptBound(
                    results,
                    TypeInfo.class,
                    consumer
            );
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method, on the wrapped {@link #metadata}, and adds each bound
     * value to the specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super TypeInfo>> C getTypeInfoAndAddAll(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getTypeInfoAndAcceptEach(
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method and accepts each bound value to the specified consumer.
     *
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachTypeInfo(final Consumer<? super TypeInfo> consumer) throws SQLException {
        getTypeInfoAndAcceptEach(consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method, on the wrapped {@link #metadata}, and returns a list of
     * bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<TypeInfo> getTypeInfo() throws SQLException {
        return getTypeInfoAndAddAll(
                new ArrayList<>()
        );
    }

    // --------------------------------------------------------------------------------------------------------- getUDTs

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
     */
    void getUDTsAndAcceptEach(@Nullable final String catalog, @Nullable final String schemaPattern,
                              final String typeNamePattern, @Nullable final int[] types,
                              final Consumer<? super UDT> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
            assert results != null;
            acceptBound(
                    results,
                    UDT.class,
                    consumer
            );
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method, on the wrapped {@link #metadata},
     * with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog         a value for the {@code catalog} parameter.
     * @param schemaPattern   a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @param types           a value for the {@code types} parameter.
     * @param collection      the collection to which bound values are added.
     * @param <C>             collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super UDT>>
    C getUDTsAndAddAll(@Nullable final String catalog, @Nullable final String schemaPattern,
                       final String typeNamePattern, @Nullable final int[] types, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getUDTsAndAcceptEach(
                catalog,
                schemaPattern,
                typeNamePattern,
                types,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method and accepts each bound value to
     * the specified consumer.
     *
     * @param catalog         a value for the {@code catalog} parameter.
     * @param schemaPattern   a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @param types           a value for the {@code types} parameter.
     * @param consumer        the consumer to which bound values are accepted.
     * @throws SQLException if a database error occurs.
     */
    public void forEachUDT(@Nullable final String catalog, @Nullable final String schemaPattern,
                           final String typeNamePattern, @Nullable final int[] types,
                           final Consumer<? super UDT> consumer)
            throws SQLException {
        getUDTsAndAcceptEach(catalog, schemaPattern, typeNamePattern, types, consumer);
    }

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])} method, on
     * the wrapped {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog         a value for the {@code catalog} parameter.
     * @param schemaPattern   a value for the {@code schemaPattern} parameter
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @param types           a value for the {@code type} parameter
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public List<UDT> getUDTs(@Nullable final String catalog, @Nullable final String schemaPattern,
                             final String typeNamePattern, @Nullable final int[] types)
            throws SQLException {
        return getUDTsAndAddAll(
                catalog,
                schemaPattern,
                typeNamePattern,
                types,
                new ArrayList<>()
        );
    }

    void forEachUDT(final Consumer<? super UDT> consumer) throws SQLException {
        getUDTsAndAcceptEach(null, null, "%", null, consumer);
    }

    List<UDT> getAllUDTs() throws SQLException {
        return getUDTs(null, null, "%", null);
    }

    /**
     * Retrieves user-defined types, optionally scoped to the specified catalog.
     *
     * @param catalog         the catalog whose {@link Catalog#getTableCat() tableCat} is used for the {@code catalog}
     *                        parameter.
     * @param schemaPattern   a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @param types           a value for the {@code types} parameter; may be {@code null}.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getUDTs(String, String, String, int[])
     */
    List<UDT> getUDTsOf(final Catalog catalog, @Nullable final String schemaPattern,
                        final String typeNamePattern, @Nullable final int[] types)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getUDTs(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                typeNamePattern,
                types
        );
    }

    void forEachUDTOf(final Catalog catalog, @Nullable final String schemaPattern,
                      final String typeNamePattern, @Nullable final int[] types,
                      final Consumer<? super UDT> consumer)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        forEachUDT(
                catalog.getEffectiveTableCat(),
                schemaPattern,
                typeNamePattern,
                types,
                consumer
        );
    }

    /**
     * Retrieves user-defined types scoped to the specified schema.
     *
     * @param schema          the schema whose {@link Schema#getTableCatalog() tableCatalog} and
     *                        {@link Schema#getTableSchem() tableSchem} are used for the {@code catalog} and
     *                        {@code schemaPattern} parameters.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @param types           a value for the {@code types} parameter; may be {@code null}.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getUDTs(String, String, String, int[])
     */
    List<UDT> getUDTsOf(final Schema schema, final String typeNamePattern, @Nullable final int[] types)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getUDTs(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                typeNamePattern,
                types
        );
    }

    void forEachUDTOf(final Schema schema, final String typeNamePattern, @Nullable final int[] types,
                      final Consumer<? super UDT> consumer)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        forEachUDT(
                schema.getEffectiveTableCatalog(),
                schema.getEffectiveTableSchem(),
                typeNamePattern,
                types,
                consumer
        );
    }

    // ----------------------------------------------------------------------------------------------- getVersionColumns

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method,
     * with given arguments, on the wrapped metadata, and accepts each bound value to specified consumer.
     *
     * @param catalog  a value for {@code catalog} parameter.
     * @param schema   a value for {@code schema} parameter.
     * @param table    a value for {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#getVersionColumns(String, String, String)
     */
    void getVersionColumnsAndAcceptEach(@Nullable final String catalog, @Nullable final String schema,
                                        final String table, final Consumer<? super VersionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getVersionColumns(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, VersionColumn.class, consumer);
        }
    }

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(String, String, String)} method, on the wrapped
     * {@link #metadata}, with given arguments, and adds each bound value to the specified collection.
     *
     * @param catalog    a value for the {@code catalog} parameter.
     * @param schema     a value for the {@code schema} parameter.
     * @param table      a value for the {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        collection type parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     */
    <C extends Collection<? super VersionColumn>>
    C getVersionColumnsAndAddAll(@Nullable final String catalog, @Nullable final String schema, final String table,
                                 final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getVersionColumnsAndAcceptEach(
                catalog,
                schema,
                table,
                collection::add
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(String, String, String)} method and accepts each bound value to
     * the specified consumer.
     *
     * @param catalog  a value for the {@code catalog} parameter.
     * @param schema   a value for the {@code schema} parameter.
     * @param table    a value for the {@code table} parameter.
     * @param consumer the consumer to which bound values are accepted.
     * @throws SQLException if a database access error occurs.
     */
    public void forEachVersionColumn(@Nullable final String catalog, @Nullable final String schema, final String table,
                                     final Consumer<? super VersionColumn> consumer)
            throws SQLException {
        getVersionColumnsAndAcceptEach(catalog, schema, table, consumer);
    }

    /**
     * Invokes
     * {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)
     * getVersionColumns(catalog, schema, table)} method, on the wrapped {@link #metadata}, with given arguments, and
     * returns a list of bound values.
     *
     * @param catalog a value for the {@code catalog} parameter.
     * @param schema  a value for the {@code schema} parameter.
     * @param table   a value for the {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#getVersionColumns(String, String, String)
     */
    public List<VersionColumn> getVersionColumns(@Nullable final String catalog, @Nullable final String schema,
                                                 final String table)
            throws SQLException {
        return getVersionColumnsAndAddAll(
                catalog,
                schema,
                table,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves a description of the specified table's columns that are automatically updated when any value in a row
     * is updated.
     *
     * @param table the table whose version columns are retrieved.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see #getVersionColumns(String, String, String)
     */
    List<VersionColumn> getVersionColumnsOf(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getVersionColumns(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName()
        );
    }

    void forEachVersionColumnOf(final Table table, final Consumer<? super VersionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        forEachVersionColumn(
                table.getEffectiveTableCat(),
                table.getEffectiveTableSchem(),
                table.getTableName(),
                consumer
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the cached map of {@link _ColumnLabel}-annotated fields of the specified class, computing and caching it
     * on the first request.
     *
     * @param clazz the class whose labeled fields are returned.
     * @return an unmodifiable map of labeled fields of the {@code clazz}.
     */
    private Map<Field, _ColumnLabel> getLabeledFields(final Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz is null");
        return classesAndLabeledFields.computeIfAbsent(
                clazz,
                c -> Collections.unmodifiableMap(ContextUtils.getFieldsAnnotatedWith(c, _ColumnLabel.class))
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The wrapped instance of {@link DatabaseMetaData}.
     */
    protected final DatabaseMetaData metadata;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A cache mapping each class to its map of {@link _ColumnLabel}-annotated fields.
     */
    private final Map<Class<?>, Map<Field, _ColumnLabel>> classesAndLabeledFields = new ConcurrentHashMap<>();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link DatabaseMetaData#getNumericFunctions() getNumericFunctions()} method, on the wrapped
     * {@link #metadata}, and returns the result as a list of comma-split elements.
     *
     * @return an unmodifiable list of numeric functions.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getNumericFunctions()
     */
    public List<String> getNumericFunctions() throws SQLException {
        return commaSplitToUnmodifiableList(metadata.getNumericFunctions());
    }

    /**
     * Invokes {@link DatabaseMetaData#getSQLKeywords() getSQLKeywords()} method, on the wrapped {@link #metadata}, and
     * returns the result as a list of comma-split elements.
     *
     * @return an unmodifiable list of SQL keywords.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSQLKeywords()
     */
    public List<String> getSQLKeywords() throws SQLException {
        return commaSplitToUnmodifiableList(metadata.getSQLKeywords());
    }

    /**
     * Invokes {@link DatabaseMetaData#getStringFunctions() getStringFunctions()} method, on the wrapped
     * {@link #metadata}, and returns the result as a list of comma-split elements.
     *
     * @return a list of string functions.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getStringFunctions()
     */
    public List<String> getStringFunctions() throws SQLException {
        return commaSplitToUnmodifiableList(metadata.getStringFunctions());
    }

    /**
     * Invokes {@link DatabaseMetaData#getSystemFunctions() getSystemFunctions()} method, on the wrapped
     * {@link #metadata}, and returns the result as a list of comma-split elements.
     *
     * @return a list of system functions.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSystemFunctions()
     */
    public List<String> getSystemFunctions() throws SQLException {
        return commaSplitToUnmodifiableList(metadata.getSystemFunctions());
    }

    /**
     * Invokes {@link DatabaseMetaData#getTimeDateFunctions() getTimeDateFunctions()} method, on the wrapped
     * {@link #metadata}, and returns the result as a list of comma-split elements.
     *
     * @return a list of time and date functions.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTimeDateFunctions()
     */
    public List<String> getTimeDateFunctions() throws SQLException {
        return commaSplitToUnmodifiableList(metadata.getTimeDateFunctions());
    }
}
