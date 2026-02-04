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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A class for retrieving information from an instance of {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Context {

    private static final System.Logger logger = System.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final Predicate<Method> READER_PREDICATE = m -> {
        if (m.getDeclaringClass() != DatabaseMetaData.class) {
            return false;
        }
        final int modifiers = m.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            return false;
        }
        if (!Modifier.isPublic(modifiers)) {
            return false;
        }
        if (m.getParameterTypes().length > 0) {
            return false;
        }
        final Class<?> returnType = m.getReturnType();
        if (returnType == void.class) {
            return false;
        }
        if (Collection.class.isAssignableFrom(returnType)) {
            return false;
        }
        if (ResultSet.class.isAssignableFrom(returnType)) {
            return false;
        }
        return true;
    };

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
     * Accepts names and results of {@link PropertyDescriptor#getReadMethod() readable properties}, of the wrapped
     * {@link #metadata}, to specified consumer.
     *
     * @param consumer the consumer to be accepted with each property's name and value.
     * @see #acceptValues(BiConsumer)
     */
    public void acceptProperties(final BiConsumer<? super String, Object> consumer) throws IntrospectionException {
        Objects.requireNonNull(consumer, "consumer is null");
        final BeanInfo info = Introspector.getBeanInfo(DatabaseMetaData.class);
        for (final PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
            final Method reader = descriptor.getReadMethod();
            if (reader == null) {
                continue;
            }
            if (!READER_PREDICATE.test(reader)) {
                continue;
            }
            try {
                final Object result = reader.invoke(metadata);
                consumer.accept(descriptor.getName(), result);
            } catch (final ReflectiveOperationException roe) {
                logger.log(
                        System.Logger.Level.ERROR,
                        () -> String.format("failed to invoke %s with %s", reader, metadata),
                        roe
                );
            }
        }
    }

    /**
     * Accepts <em>simple</em> methods and their results, of the wrapped {@code metadata}, to specified consumer.
     *
     * @param consumer the consumer to be accepted with each method and its result.
     * @see #acceptProperties(BiConsumer)
     */
    public void acceptValues(final BiConsumer<? super Method, Object> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        for (final Method method : DatabaseMetaData.class.getMethods()) {
            if (!READER_PREDICATE.test(method)) {
                continue;
            }
            try {
                final Object result = method.invoke(metadata);
                consumer.accept(method, result);
            } catch (final ReflectiveOperationException roe) {
                logger.log(
                        System.Logger.Level.ERROR,
                        () -> String.format("failed to invoke %s with %s", method, metadata),
                        roe
                );
            }
        }
    }

//    // -----------------------------------------------------------------------------------------------------------------
//    private final Map<Class<?>, Field> unmappedFields = new ConcurrentHashMap<>();
//
//    private void unmappedField(final Class<?> type, final Field field, final _ColumnLabel label) {
//        final var previous = unmappedFields.putIfAbsent(type, field);
//        if (previous != null) {
//            logger.log(
//                    System.Logger.Level.WARNING,
//                    () -> String.format("unmapped field; type: %s, field: %s, label: %s", type.getSimpleName(),
//                                        field.getName(), label.value())
//            );
//        }
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//    private final Map<Class<?>, String> unknownColumns = new ConcurrentHashMap<>();
//
//    private void unknownColumn(final Class<?> type, final String label) {
//        final var previous = unknownColumns.putIfAbsent(type, label);
//        if (previous != null) {
//            logger.log(System.Logger.Level.TRACE,
//                       "unknown column; type: {0}, label: {1}", type.getSimpleName(), label);
//        }
//    }

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
            if (instance instanceof AbstractMetadataType) {
                ((AbstractMetadataType) instance).putUnknownColumn(label, value);
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

    private <C extends Collection<? super T>, T extends MetadataType> C requireNonNullConnection(final C collection) {
        return Objects.requireNonNull(collection, "collection is null");
    }

    // --------------------------------------------------------------------------------------------------- getAttributes
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

    // ----------------------------------------------------------------------------------------------------- getCatalogs
    void getCatalogsAndAcceptEach(final Consumer<? super Catalog> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getCatalogs()) {
            assert results != null;
            acceptBound(results, Catalog.class, consumer);
        }
    }

    <C extends Collection<? super Catalog>> C getCatalogsAndAddAll(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getCatalogsAndAcceptEach(v -> {
            final var changed = collection.add(v);
            assert changed : "duplicate catalog: " + v;
        });
        return collection;
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

    <C extends Collection<? super ClientInfoProperty>> C getClientInfoPropertiesAndAddAll(final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getClientInfoPropertiesAndAcceptEach(
                collection::add
        );
        return collection;
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
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate column privilege: " + v;
                }
        );
        return collection;
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

    <C extends Collection<? super CrossReference>> C
    getCrossReferenceAndAddAll(final String parentCatalog, final String parentSchema, final String parentTable,
                               final String foreignCatalog, final String foreignSchema, final String foreignTable,
                               final C collection)
            throws SQLException {
        requireNonNullConnection(collection);
        getCrossReferenceAndAcceptEach(
                parentCatalog,
                parentSchema,
                parentTable,
                foreignCatalog,
                foreignSchema,
                foreignTable,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate cross reference: " + v;
                }
        );
        return collection;
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
        return getCrossReferenceAndAddAll(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema,
                                          foreignTable,
                                          new ArrayList<>());
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

    <C extends Collection<? super ExportedKey>> C
    getExportedKeysAndAddAll(final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getExportedKeysAndAcceptEach(
                catalog,
                schema,
                table,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate exported key: " + v;
                }
        );
        return collection;
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
    void getFunctionsAndAcceptEach(final String catalog, final String schemaPattern, final String functionNamePattern,
                                   final Consumer<? super Function> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getFunctions(catalog, schemaPattern, functionNamePattern)) {
            assert results != null;
            acceptBound(results, Function.class, consumer);
        }
    }

    <C extends Collection<? super Function>>
    C getFunctionsAndAddAll(final String catalog, final String schemaPattern, final String functionNamePattern,
                            final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getFunctionsAndAcceptEach(
                catalog,
                schemaPattern,
                functionNamePattern,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate function: " + v;
                }
        );
        return collection;
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

    List<Function> getFunctionsOf(final Catalog catalog, @Nullable final String schemaPattern,
                                  @Nullable final String functionNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getFunctions(
                catalog.getTableCat(),
                schemaPattern,
                functionNamePattern
        );
    }

    List<Function> getFunctionsOf(final Schema schema, @Nullable final String functionNamePattern)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getFunctions(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                functionNamePattern
        );
    }

    // ------------------------------ getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern)
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
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate function column: " + v;
                }
        );
        return collection;
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

    /**
     * Retrieves function columns of the specified function.
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

    <C extends Collection<? super ImportedKey>>
    C getImportedKeysAndAddAll(final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getImportedKeysAndAcceptEach(
                catalog,
                schema,
                table,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate imported key: " + v;
                }
        );
        return collection;
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
    List<ImportedKey> getImportedKeys(final Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getImportedKeys(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName()
        );
    }

    // ---------------------------------------------------------------------------------------------------- getIndexInfo
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
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate index info: " + v;
                }
        );
        return collection;
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
    void getPrimaryKeysAndAcceptEach(final String catalog, final String schema, final String table,
                                     final Consumer<? super PrimaryKey> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getPrimaryKeys(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, PrimaryKey.class, consumer);
        }
    }

    <C extends Collection<? super PrimaryKey>>
    C getPrimaryKeysAndAddAll(final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getPrimaryKeysAndAcceptEach(
                catalog,
                schema,
                table,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate primary key: " + v;
                }
        );
        return collection;
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
    void getProcedureColumnsAndAcceptEach(final String catalog, final String schemaPattern,
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

    <C extends Collection<? super ProcedureColumn>>
    C getProcedureColumnsAndAddAll(final String catalog, final String schemaPattern, final String procedureNamePattern,
                                   final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getProcedureColumnsAndAcceptEach(
                catalog,
                schemaPattern,
                procedureNamePattern,
                columnNamePattern,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate procedure column: " + v;
                }
        );
        return collection;
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

    List<ProcedureColumn> getProcedureColumns(final Procedure procedure, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(procedure, "procedure is null");
        return getProcedureColumns(
                procedure.getProcedureCat(), procedure.getProcedureSchem(), procedure.getProcedureName(),
                columnNamePattern
        );
    }

    List<ProcedureColumn> getProcedureColumns(final Procedure procedure) throws SQLException {
        Objects.requireNonNull(procedure, "procedure is null");
        return getProcedureColumns(
                procedure,
                "%"
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
    void getProceduresAndAcceptEach(final String catalog, final String schemaPattern, final String procedureNamePattern,
                                    final Consumer<? super Procedure> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            assert results != null;
            acceptBound(results, Procedure.class, consumer);
        }
    }

    <C extends Collection<? super Procedure>>
    C getProceduresAndAddAll(final String catalog, final String schemaPattern, final String procedureNamePattern,
                             final C collection)
            throws SQLException {
        requireNonNullConnection(collection);
        getProceduresAndAcceptEach(
                catalog,
                schemaPattern,
                procedureNamePattern,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate procedure: " + v;
                }
        );
        return collection;
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
                catalog, schemaPattern, procedureNamePattern,
                new ArrayList<>()
        );
    }

    List<Procedure> getProcedures(final Catalog catalog, final String procedureNamePattern) throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getProcedures(
                catalog.getTableCat(),
                null,
                procedureNamePattern
        );
    }

    List<Procedure> getProcedures(final Catalog catalog) throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getProcedures(
                catalog,
                "%"
        );
    }

    List<Procedure> getProcedures(final Schema schema, final String procedureNamePattern) throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getProcedures(
                schema.getTableCatalog(), schema.getTableSchem(), procedureNamePattern
        );
    }

    List<Procedure> getProcedures(final Schema schema) throws SQLException {
        return getProcedures(
                schema,
                "%"
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
    void getPseudoColumnsAndAcceptEach(final String catalog, final String schemaPattern, final String tableNamePattern,
                                       final String columnNamePattern, final Consumer<? super PseudoColumn> consumer)
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

    <C extends Collection<? super PseudoColumn>>
    C getPseudoColumnsAndAddAll(final String catalog, final String schemaPattern, final String tableNamePattern,
                                final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getPseudoColumnsAndAcceptEach(
                catalog,
                schemaPattern,
                tableNamePattern,
                columnNamePattern,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate pseudo column: " + v;
                }
        );
        return collection;
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

    List<PseudoColumn> getPseudoColumns(final Table table, final String columnNamePattern) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getPseudoColumns(
                table.getTableCat(), table.getTableSchem(), table.getTableName(),
                columnNamePattern
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
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    <C extends Collection<? super Schema>> C getSchemasAndAddAll(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getSchemasAndAcceptEach(
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate schema: " + v;
                }
        );
        return collection;
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
    void getSchemasAndAcceptEach(final String catalog, final String schemaPattern,
                                 final Consumer<? super Schema> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = metadata.getSchemas(catalog, schemaPattern)) {
            assert results != null;
            acceptBound(results, Schema.class, consumer);
        }
    }

    <C extends Collection<? super Schema>>
    C getSchemasAndAddAll(final String catalog, final String schemaPattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getSchemasAndAcceptEach(
                catalog,
                schemaPattern,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate schema: " + v;
                }
        );
        return collection;
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

    List<Schema> getSchemas(final Catalog catalog, final String schemaPattern) throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSchemas(
                catalog.getTableCat(),
                schemaPattern
        );
    }

    List<Schema> getSchemas(final Catalog catalog) throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSchemas(
                catalog,
                "%"
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
    void getSuperTablesAndAcceptEach(final String catalog, final String schemaPattern, final String tableNamePattern,
                                     final Consumer<? super SuperTable> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
            assert results != null;
            acceptBound(results, SuperTable.class, consumer);
        }
    }

    <C extends Collection<? super SuperTable>>
    C getSuperTablesAndAddAll(final String catalog, final String schemaPattern, final String tableNamePattern,
                              final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getSuperTablesAndAcceptEach(
                catalog,
                schemaPattern,
                tableNamePattern,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate super table: " + v;
                }
        );
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method, on the wrapped {@link #metadata},
     * with given arguments, and returns a list of bound values.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} paramter.
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
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName()
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
    void getSuperTypesAndAcceptEach(final String catalog, final String schemaPattern, final String typeNamePattern,
                                    final Consumer<? super SuperType> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
            assert results != null;
            acceptBound(results, SuperType.class, consumer);
        }
    }

    <C extends Collection<? super SuperType>>
    C getSuperTypesAndAddAll(final String catalog, final String schemaPattern, final String typeNamePattern,
                             final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getSuperTypesAndAcceptEach(
                catalog,
                schemaPattern,
                typeNamePattern,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate super type: " + v;
                }
        );
        return collection;
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

    List<SuperType> getSuperTypes(final Catalog catalog, final String schemaPattern, final String typeNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getSuperTypes(
                catalog.getTableCat(),
                schemaPattern,
                typeNamePattern
        );
    }

    List<SuperType> getSuperTypes(final Schema schema, final String typeNamePattern)
            throws SQLException {
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
    void getTablePrivilegesAndAcceptEach(final String catalog, final String schemaPattern,
                                         final String tableNamePattern,
                                         final Consumer<? super TablePrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            assert results != null;
            acceptBound(results, TablePrivilege.class, consumer);
        }
    }

    <C extends Collection<? super TablePrivilege>>
    C getTablePrivilegesAndAddAll(final String catalog, final String schemaPattern, final String tableNamePattern,
                                  final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getTablePrivilegesAndAcceptEach(
                catalog,
                schemaPattern,
                tableNamePattern,
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate table privilege: " + v;
                }
        );
        return collection;
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

    <C extends Collection<? super TableType>> C getTableTypesAndAddAll(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getTableTypesAndAcceptEach(
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate table type: " + v;
                }
        );
        return collection;
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
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate table: " + v;
                }
        );
        return collection;
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

    /**
     * .
     *
     * @param catalog          .
     * @param schemaPattern    .
     * @param tableNamePattern .
     * @param types            .
     * @return .
     * @throws SQLException if a database error occurs
     * @see Catalog#getTableCat()
     * @see #getTables(String, String, String, String[])
     */
    List<Table> getTablesOf(final Catalog catalog, final String schemaPattern,
                            final String tableNamePattern, final String[] types)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getTables(
                catalog.getTableCat(),
                schemaPattern,
                tableNamePattern,
                types
        );
    }

    List<Table> getTablesOf(@Nullable final Schema schema, final String tableNamePattern,
                            @Nullable final String[] types)
            throws SQLException {
        return getTables(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                tableNamePattern,
                types
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

    <C extends Collection<? super TypeInfo>> C getTypeInfoAndAddAll(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getTypeInfoAndAcceptEach(
                v -> {
                    final var changed = collection.add(v);
                    assert changed : "duplicate type info: " + v;
                }
        );
        return collection;
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
    void getUDTsAndAcceptEach(final String catalog, final String schemaPattern, final String typeNamePattern,
                              final int[] types, final Consumer<? super UDT> consumer)
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

    <C extends Collection<? super UDT>>
    C getUDTsAndAddAll(final String catalog, final String schemaPattern, final String typeNamePattern,
                       final int[] types, final C collection)
            throws SQLException {
        requireNonNullConnection(collection);
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

    List<UDT> getUDTs(final Catalog catalog, final String schemaPattern, final String typeNamePattern,
                      final int[] types)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getUDTs(
                catalog.getTableCat(),
                schemaPattern,
                typeNamePattern,
                types
        );
    }

    List<UDT> getUDTs(final Schema schema, final String typeNamePattern, final int[] types)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getUDTs(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                typeNamePattern,
                types
        );
    }

    // ----------------------------------------------------------------------------------------------- getVersionColumns

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
    void getVersionColumnsAndAcceptEach(final String catalog, final String schema, final String table,
                                        final Consumer<? super VersionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (var results = metadata.getVersionColumns(catalog, schema, table)) {
            assert results != null;
            acceptBound(results, VersionColumn.class, consumer);
        }
    }

    <C extends Collection<? super VersionColumn>>
    C getVersionColumnsAndAddAll(final String catalog, final String schema, final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        getVersionColumnsAndAcceptEach(
                catalog,
                schema,
                table,
                v -> {
                    assert !collection.contains(v);
                    collection.add(v);
                }
        );
        return collection;
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
                classesAndLabeledFields.computeIfAbsent(
                        clazz,
                        c -> ContextUtils.getFieldsAnnotatedWith(c, _ColumnLabel.class)
                )
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The wrapped instance of {@link DatabaseMetaData}.
     */
    protected final DatabaseMetaData metadata;

    // -----------------------------------------------------------------------------------------------------------------
    private final Map<Class<?>, Map<Field, _ColumnLabel>> classesAndLabeledFields = new HashMap<>();

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
        return Arrays.stream(metadata.getNumericFunctions().split(","))
                .map(String::strip)
                .filter(v -> !v.isBlank())
                .collect(Collectors.toUnmodifiableList());
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
        return Arrays.stream(metadata.getSQLKeywords().split(","))
                .map(String::strip)
                .filter(v -> !v.isBlank())
                .collect(Collectors.toUnmodifiableList());
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
        return Arrays.stream(metadata.getStringFunctions().split(","))
                .map(String::strip)
                .filter(v -> !v.isBlank())
                .collect(Collectors.toUnmodifiableList());
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
        return Arrays.stream(metadata.getSystemFunctions().split(","))
                .map(String::strip)
                .filter(v -> !v.isBlank())
                .collect(Collectors.toUnmodifiableList());
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
        return Arrays.stream(metadata.getTimeDateFunctions().split(","))
                .map(String::strip)
                .filter(v -> !v.isBlank())
                .collect(Collectors.toList());
    }
}
