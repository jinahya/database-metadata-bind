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

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance from specified connection.
     *
     * @param connection the connection.
     * @return a new instance.
     * @throws SQLException if a database error occurs.
     * @see Connection#getMetaData()
     */
    public static Context newInstance(final Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "connection is null");
        return new Context(connection.getMetaData());
    }

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
                log.log(Level.SEVERE, roe, () -> String.format("failed to invoke %1$s", reader));
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
                log.log(Level.SEVERE, roe, () -> String.format("failed to invoke %1$s", method));
            }
        }
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
        final Set<String> resultLabels = ContextUtils.getLabels(results);
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
                if (value.equals(metadata.getDatabaseProductName())) {
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
                ContextUtils.setFieldValue(field, instance, results, fieldLabel.value());
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

//    /**
//     * Binds all records as given type and adds them to specified collection.
//     *
//     * @param results    the records to bind.
//     * @param type       the type of instances.
//     * @param collection the collection to which bound instances are added
//     * @param <T>        binding type parameter
//     * @param <C>        the type of {@code collection}
//     * @return given {@code collection}.
//     * @throws SQLException if a database error occurs.
//     */
//    @SuppressWarnings({
//            "java:S112", // new RuntimeException
//            "java:S1874", // isAccessible
//            "java:S3011" // setAccessible
//    })
//    private <T extends MetadataType, C extends Collection<? super T>> C bind(
//            final ResultSet results, final Class<T> type, final C collection)
//            throws SQLException {
//        while (results.next()) {
//            final T value;
//            try {
//                final Constructor<T> constructor = type.getDeclaredConstructor();
//                if (!constructor.isAccessible()) {
//                    constructor.setAccessible(true);
//                }
//                value = constructor.newInstance();
//            } catch (final ReflectiveOperationException roe) {
//                throw new RuntimeException("failed to instantiate " + type, roe);
//            }
//            collection.add(bind(results, type, value));
//        }
//        return collection;
//    }

    // ------------------------------------- getAttributes(catalog, schemaPatter, typeNamePattern, attributeNamePattern)
    void acceptAttributes(final String catalog, final String schemaPattern, final String typeNamePattern,
                          final String attributeNamePattern, final Consumer<? super Attribute> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = metadata.getAttributes(
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
     * {@link DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     * getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern)} method, on the wrapped
     * {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param typeNamePattern      a value for the {@code typeNamePattern} parameter.
     * @param attributeNamePattern a value for the {@code attributeNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getAttributes(String, String, String, String)
     */
    public @NotNull List<@Valid @NotNull Attribute> getAttributes(final @Nullable String catalog,
                                                                  final @Nullable String schemaPattern,
                                                                  final @NotBlank String typeNamePattern,
                                                                  final @NotBlank String attributeNamePattern)
            throws SQLException {
        return addAttributes(
                catalog,
                schemaPattern,
                typeNamePattern,
                attributeNamePattern,
                new ArrayList<>()
        );
    }

    @NotNull List<@Valid @NotNull Attribute> getAttributes(final @NotNull UDT udt,
                                                           final @NotBlank String attributeNamePattern)
            throws SQLException {
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
        try (ResultSet results = metadata.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            assert results != null;
            acceptBound(results, BestRowIdentifier.class, consumer);
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
     * {@link DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
     * getBestRowIdentifier(catalog, schema, table, scope, nullable)} method, on the wrapped {@link #metadata}, with
     * given arguments, and returns a list of bound values.
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
    public @NotNull List<@Valid @NotNull BestRowIdentifier> getBestRowIdentifier(final @Nullable String catalog,
                                                                                 final @Nullable String schema,
                                                                                 final @NotBlank String table,
                                                                                 final int scope,
                                                                                 final boolean nullable)
            throws SQLException {
        return addBestRowIdentifier(
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

    // --------------------------------------------------------------------------------------------------- getCatalogs()
    void acceptCatalogs(final Consumer<? super Catalog> consumer) throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = metadata.getCatalogs()) {
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
    public @NotNull List<@Valid @NotNull Catalog> getCatalogs() throws SQLException {
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
        try (ResultSet results = metadata.getClientInfoProperties()) {
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
     * Invokes {@link DatabaseMetaData#getClientInfoProperties() getClientInfoProperties()} method, on the wrapped
     * {@link #metadata}, and returns a list of bound values.
     *
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public @NotNull List<@Valid @NotNull ClientInfoProperty> getClientInfoProperties() throws SQLException {
        return addClientInfoProperties(new ArrayList<>());
    }

    // -------------------------------------------------- getColumnPrivileges(catalog, schema, table, columnNamePattern)
    void acceptColumnPrivileges(final String catalog, final String schema, final String table,
                                final String columnNamePattern, final Consumer<? super ColumnPrivilege> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = metadata.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            assert results != null;
            acceptBound(
                    results,
                    ColumnPrivilege.class,
                    consumer
            );
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
     * java.lang.String) getColumnPrivileges(catalog, schema, table, columnNamePattern)} method, on the wrapped
     * {@link #metadata}, with given arguments, and returns a list of bound values.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schema            a value for the {@code schema} parameter.
     * @param table             a value for the {@code table} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     * @see #getTables(String, String, String, String[])
     */
    public @NotNull List<@Valid @NotNull ColumnPrivilege> getColumnPrivileges(final @Nullable String catalog,
                                                                              final @Nullable String schema,
                                                                              final @NotBlank String table,
                                                                              final @NotBlank String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(columnNamePattern, "columnNamePattern is null");
        return addColumnPrivileges(
                catalog,
                schema,
                table,
                columnNamePattern,
                new ArrayList<>()
        );
    }

    @NotNull List<@Valid @NotNull ColumnPrivilege> getColumnPrivileges(final @NotNull Table table,
                                                                       final @NotBlank String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getColumnPrivileges(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName(),
                columnNamePattern
        );
    }

    @NotNull List<@Valid @NotNull ColumnPrivilege> getAllColumnPrivileges(final @NotNull Table table)
            throws SQLException {
        return getColumnPrivileges(
                table,
                "%"
        );
    }

    @NotNull List<@Valid @NotNull ColumnPrivilege> getColumnPrivileges(final @NotNull Column column)
            throws SQLException {
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
        try (ResultSet results = metadata.getColumns(
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
    public @NotNull List<@Valid @NotNull Column> getColumns(final @Nullable String catalog,
                                                            final @Nullable String schemaPattern,
                                                            final @NotBlank String tableNamePattern,
                                                            final @NotBlank String columnNamePattern)
            throws SQLException {
        return addColumns(
                catalog,
                schemaPattern,
                tableNamePattern,
                columnNamePattern,
                new ArrayList<>()
        );
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

    @NotNull List<@Valid @NotNull Column> getAllColumns(final @NotNull Table table) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        return getColumns(
                table.getTableCat(),
                table.getTableSchem(),
                table.getTableName(),
                "%"
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
     * @see DatabaseMetaData#getCrossReference(String, String, String, String, String, String)
     */
    void acceptCrossReference(final String parentCatalog, final String parentSchema, final String parentTable,
                              final String foreignCatalog, final String foreignSchema, final String foreignTable,
                              final Consumer<? super CrossReference> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = metadata.getCrossReference(
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
     * java.lang.String, java.lang.String) getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog,
     * foreignSchema, foreignTable)} method, on the wrapped {@link #metadata}, with given arguments, and returns a list
     * of bound values.
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
    public @NotNull List<@Valid @NotNull CrossReference> getCrossReference(
            final @Nullable String parentCatalog, final @Nullable String parentSchema,
            final @NotBlank String parentTable,
            final @Nullable String foreignCatalog, final @Nullable String foreignSchema,
            final @NotBlank String foreignTable)
            throws SQLException {
        return addCrossReference(
                parentCatalog,
                parentSchema,
                parentTable,
                foreignCatalog,
                foreignSchema,
                foreignTable,
                new ArrayList<>()
        );
    }

    @NotNull List<@Valid @NotNull CrossReference> getCrossReference(final @NotNull Table parentTable,
                                                                    final @NotNull Table foreighTable)
            throws SQLException {
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
        try (ResultSet results = metadata.getExportedKeys(catalog, schema, table)) {
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
     * @param catalog a value for the {@code catalog} parameter.
     * @param schema  a value for the {@code schema} parameter.
     * @param table   a value for the {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public @NotNull List<@Valid @NotNull ExportedKey> getExportedKeys(final @Nullable String catalog,
                                                                      final @Nullable String schema,
                                                                      final @NotBlank String table)
            throws SQLException {
        return addExportedKeys(
                catalog,
                schema,
                table,
                new ArrayList<>()
        );
    }

    /**
     * Retrieves exported keys of specified table.
     *
     * @param table the table whose exported keys are retrieved.
     * @return a list of exported keys of the {@code table}.
     * @throws SQLException if a database error occurs.
     * @see #getExportedKeys(String, String, String)
     */
    @NotNull List<@Valid @NotNull ExportedKey> getExportedKeys(final @NotNull Table table) throws SQLException {
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
        try (ResultSet results = metadata.getFunctions(catalog, schemaPattern, functionNamePattern)) {
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
     * @param catalog             a value for the {@code catalog} parameter.
     * @param schemaPattern       a value for the {@code schemaPattern} parameter.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getFunctions(String, String, String)
     */
    public @NotNull List<@Valid @NotNull Function> getFunctions(final @Nullable String catalog,
                                                                final @Nullable String schemaPattern,
                                                                final @NotBlank String functionNamePattern)
            throws SQLException {
        return addFunctions(
                catalog,
                schemaPattern,
                functionNamePattern,
                new ArrayList<>()
        );
    }

    // ------------------------------ getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern)

    void acceptFunctionColumns(final String catalog, final String schemaPattern, final String functionNamePattern,
                               final String columnNamePattern, final Consumer<? super FunctionColumn> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        try (ResultSet results = metadata.getFunctionColumns(
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
     * {@link DatabaseMetaData#getFunctionColumns(String, String, String, String) getFunctions((catalog, schemaPattern,
     * functionNamePattern, columnNamePattern)} method, on the wrapped {@link #metadata} ,with specified arguments, and
     * returns a list of bound values.
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
    public @NotNull List<@Valid @NotNull FunctionColumn> getFunctionColumns(final @Nullable String catalog,
                                                                            final @Nullable String schemaPattern,
                                                                            final @NotBlank String functionNamePattern,
                                                                            final @NotBlank String columnNamePattern)
            throws SQLException {
        return addFunctionColumns(
                catalog,
                schemaPattern,
                functionNamePattern,
                columnNamePattern,
                new ArrayList<>()
        );
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
    List<FunctionColumn> getFunctionColumns(final @NotNull Function function, final @NotBlank String columnNamePattern)
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
        try (ResultSet results = metadata.getImportedKeys(catalog, schema, table)) {
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
    public @NotNull List<@Valid @NotNull ImportedKey> getImportedKeys(final @Nullable String catalog,
                                                                      final @Nullable String schema,
                                                                      final @NotBlank String table)
            throws SQLException {
        return addImportedKeys(
                catalog,
                schema,
                table,
                new ArrayList<>()
        );
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
        try (ResultSet results = metadata.getIndexInfo(catalog, schema, table, unique, approximate)) {
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
     * Invokes
     * {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean) getIndexInfo(catalog, schema,
     * table, unique, approximate)} method with specified arguments, and returns a list of bound values.
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
    public @NotNull List<@Valid @NotNull IndexInfo> getIndexInfo(final @Nullable String catalog,
                                                                 final @Nullable String schema,
                                                                 final @NotBlank String table,
                                                                 final boolean unique, final boolean approximate)
            throws SQLException {
        return addIndexInfo(
                catalog,
                schema,
                table,
                unique,
                approximate,
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
        try (ResultSet results = metadata.getPrimaryKeys(catalog, schema, table)) {
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
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String) getPrimaryKeys(catalog, schema, table)}
     * method with given arguments, and returns a list of bound values.
     *
     * @param catalog a value for the {@code catalog} parameter.
     * @param schema  a value for the {@code schema} parameter.
     * @param table   a value for the {@code table} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public @NotNull List<@Valid @NotNull PrimaryKey> getPrimaryKeys(final @Nullable String catalog,
                                                                    final @Nullable String schema,
                                                                    final @NotBlank String table)
            throws SQLException {
        return addPrimaryKeys(
                catalog,
                schema,
                table,
                new ArrayList<>()
        );
    }

    List<PrimaryKey> getPrimaryKeys(final @NotNull Table table) throws SQLException {
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
        try (ResultSet results = metadata.getProcedureColumns(
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
    public @NotNull List<@Valid @NotNull ProcedureColumn> getProcedureColumns(
            final @Nullable String catalog, final @Nullable String schemaPattern,
            final @NotBlank String procedureNamePattern, final @NotBlank String columnNamePattern)
            throws SQLException {
        return addProcedureColumns(
                catalog,
                schemaPattern,
                procedureNamePattern,
                columnNamePattern,
                new ArrayList<>()
        );
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
        try (ResultSet results = metadata.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
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
     * @param catalog              a value for the {@code catalog} parameter.
     * @param schemaPattern        a value for the {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedures(String, String, String)
     */
    public @NotNull List<@Valid @NotNull Procedure> getProcedures(final @Nullable String catalog,
                                                                  final @Nullable String schemaPattern,
                                                                  final @NotBlank String procedureNamePattern)
            throws SQLException {
        return addProcedures(
                catalog,
                schemaPattern,
                procedureNamePattern,
                new ArrayList<>()
        );
    }

    @NotNull List<@Valid @NotNull Procedure> getProcedures(final @NotNull Catalog catalog,
                                                           final @NotBlank String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getProcedures(
                catalog.getTableCat(),
                null,
                procedureNamePattern
        );
    }

    @NotNull List<@Valid @NotNull Procedure> getAllProcedures(@NotNull final Catalog catalog) throws SQLException {
        Objects.requireNonNull(catalog, "catalog is null");
        return getProcedures(catalog, "%");
    }

    @NotNull List<@Valid @NotNull Procedure> getProcedures(final @NotNull Schema schema,
                                                           final @NotBlank String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(schema, "schema is null");
        return getProcedures(
                schema.getTableCatalog(),
                schema.getTableSchem(),
                procedureNamePattern
        );
    }

    @NotNull List<@Valid @NotNull Procedure> getAllProcedures(final @NotNull Schema schema) throws SQLException {
        return getProcedures(
                schema,
                "%"
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
        try (ResultSet results = metadata.getPseudoColumns(
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
     * {@link DatabaseMetaData#getPseudoColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     * getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)} method with given arguments, and
     * returns a list of bound values.
     *
     * @param catalog           a value for the {@code catalog} parameter.
     * @param schemaPattern     a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for the {@code tableNamePattern} parameter.
     * @param columnNamePattern a value for the {@code columnNamePattern} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPseudoColumns(String, String, String, String)
     */
    public @NotNull List<@Valid @NotNull PseudoColumn> getPseudoColumns(final @Nullable String catalog,
                                                                        final @Nullable String schemaPattern,
                                                                        final @NotBlank String tableNamePattern,
                                                                        final @NotBlank String columnNamePattern)
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
        try (ResultSet results = metadata.getSchemas()) {
            assert results != null;
            acceptBound(results, Schema.class, consumer);
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
    public @NotNull List<@Valid @NotNull Schema> getSchemas() throws SQLException {
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
        try (ResultSet results = metadata.getSchemas(catalog, schemaPattern)) {
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
    public @NotNull List<@Valid @NotNull Schema> getSchemas(final @Nullable String catalog,
                                                            final @Nullable String schemaPattern)
            throws SQLException {
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
        try (ResultSet results = metadata.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
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
    public @NotNull List<@Valid @NotNull SuperTable> getSuperTables(final @Nullable String catalog,
                                                                    final String schemaPattern,
                                                                    final @NotBlank String tableNamePattern)
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
        try (ResultSet results = metadata.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
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
    public @NotNull List<@Valid @NotNull SuperType> getSuperTypes(final @Nullable String catalog,
                                                                  final String schemaPattern,
                                                                  final @NotBlank String typeNamePattern)
            throws SQLException {
        return addSuperTypes(catalog, schemaPattern, typeNamePattern, new ArrayList<>());
    }

    @NotNull List<@Valid @NotNull SuperType> getSuperTypes(final @NotNull Schema schema,
                                                           final @NotBlank String typeNamePattern)
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
        try (ResultSet results = metadata.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
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
    public @NotNull List<@Valid @NotNull TablePrivilege> getTablePrivileges(final @Nullable String catalog,
                                                                            final @Nullable String schemaPattern,
                                                                            final @NotBlank String tableNamePattern)
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
        try (ResultSet results = metadata.getTableTypes()) {
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
    public @NotNull List<@Valid @NotNull TableType> getTableTypes() throws SQLException {
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
        try (ResultSet results = metadata.getTables(catalog, schemaPattern, tableNamePattern, types)) {
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
     */
    <T extends Collection<? super Table>> T addTables(final @Nullable String catalog,
                                                      final @Nullable String schemaPattern,
                                                      final @NotBlank String tableNamePattern,
                                                      final @Nullable String[] types,
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
     * getTables(catalog, schemaPattern, tableNamePattern, types)} method, on the wrapped {@link #metadata}, with given
     * arguments, and returns a list of bound values.
     *
     * @param catalog          a value for the {@code catalog} parameter.
     * @param schemaPattern    a value for the {@code schemaPattern} parameter.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter.
     * @param types            a value for the {@code types} parameter.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     * @see #getColumns(String, String, String, String)
     * @see #getExportedKeys(Table)
     * @see #getImportedKeys(String, String, String)
     * @see #getIndexInfo(String, String, String, boolean, boolean)
     * @see #getPrimaryKeys(String, String, String)
     * @see #getPseudoColumns(String, String, String, String)
     * @see #getSuperTables(String, String, String)
     * @see #getVersionColumns(String, String, String)
     */
    public @NotNull List<@Valid @NotNull Table> getTables(final @Nullable String catalog,
                                                          final @Nullable String schemaPattern,
                                                          final @NotBlank String tableNamePattern,
                                                          final @Nullable String[] types)
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
        try (ResultSet results = metadata.getTypeInfo()) {
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
    public @NotNull List<@Valid @NotNull TypeInfo> getTypeInfo() throws SQLException {
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
        try (ResultSet results = metadata.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
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
     * Invokes
     * {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[]) getUDTs(catalog,
     * schemaPattern, typeNamePattern, types)} method, on the wrapped {@link #metadata}, with given arguments, and
     * returns a list of bound values.
     *
     * @param catalog         a value for the {@code catalog} parameter.
     * @param schemaPattern   a value for the {@code schemaPattern} parameter
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter.
     * @param types           a value for the {@code type} parameter
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getUDTs(String, String, String, int[])
     * @see #getAttributes(String, String, String, String)
     */
    public @NotNull List<@Valid @NotNull UDT> getUDTs(final @Nullable String catalog,
                                                      final @Nullable String schemaPattern,
                                                      final @NotBlank String typeNamePattern,
                                                      final @Nullable int[] types)
            throws SQLException {
        return addUDTs(
                catalog,
                schemaPattern,
                typeNamePattern,
                types,
                new ArrayList<>()
        );
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
        try (ResultSet results = metadata.getVersionColumns(catalog, schema, table)) {
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
    public @NotNull List<@Valid @NotNull VersionColumn> getVersionColumns(final @Nullable String catalog,
                                                                          final @Nullable String schema,
                                                                          final @NotBlank String table)
            throws SQLException {
        return addVersionColumns(
                catalog,
                schema,
                table,
                new ArrayList<>()
        );
    }

    @NotNull List<@Valid @NotNull VersionColumn> getVersionColumns(final @NotNull Table table) throws SQLException {
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
                classesAndLabeledFields.computeIfAbsent(clazz,
                                                        c -> ContextUtils.getFieldsAnnotatedWith(c, _ColumnLabel.class))
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
     * Invokes {@link DatabaseMetaData#getNumericFunctions() getNumericFunctions()} method, on the {@link #metadata},
     * and returns the result as a list of comma-split elements.
     *
     * @return a list of numeric functions.
     * @throws SQLException if a database error occurs.
     */
    public @NotNull List<@NotBlank String> getNumericFunctions() throws SQLException {
        return Arrays.stream(metadata.getNumericFunctions().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Invokes {@link DatabaseMetaData#getSQLKeywords() getSQLKeywords()} method, on the wrapped {@link #metadata}, and
     * returns the result as a list of comma-split elements.
     *
     * @return a list of SQL keywords.
     * @throws SQLException if a database error occurs.
     */
    public @NotNull List<@NotBlank String> getSQLKeywords() throws SQLException {
        return Arrays.stream(metadata.getSQLKeywords().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Invokes {@link DatabaseMetaData#getStringFunctions() getStringFunctions()} method, on the wrapped
     * {@link #metadata}, and returns the result as a list of comma-split elements.
     *
     * @return a list of string functions.
     * @throws SQLException if a database error occurs.
     */
    public @NotNull List<@NotBlank String> getStringFunctions() throws SQLException {
        return Arrays.stream(metadata.getStringFunctions().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Invokes {@link DatabaseMetaData#getSystemFunctions() getSystemFunctions()} method, on the wrapped
     * {@link #metadata}, and returns the result as a list of comma-split elements.
     *
     * @return a list of system functions.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSystemFunctions()
     */
    public @NotNull List<@NotBlank String> getSystemFunctions() throws SQLException {
        return Arrays.stream(metadata.getSystemFunctions().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Invokes {@link DatabaseMetaData#getTimeDateFunctions() getTimeDateFunctions()} method, on the wrapped
     * {@link #metadata}, and returns the result as a list of comma-split elements.
     *
     * @return a list of time and date functions.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTimeDateFunctions()
     */
    public @NotNull List<@NotBlank String> getTimeDateFunctions() throws SQLException {
        return Arrays.stream(metadata.getTimeDateFunctions().split(","))
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------------------------------------------------
    Supplier<Connection> connectionSupplier;
}
