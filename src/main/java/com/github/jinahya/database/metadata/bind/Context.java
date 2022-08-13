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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
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
                log.warning(() -> String.format("unknown label; %1$s on %2$s", label, field));
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
                log.log(Level.SEVERE, String.format("failed to set %1$s", field), roe);
            }
        }
        if (log.isLoggable(Level.FINE)) {
            for (final String l : resultSetLabels) {
                final Object v = results.getObject(l);
                log.fine(() -> String.format("remained result; type: %1$s; label: %2$s, value: %3$s", type, l, v));
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
     * getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern)} method with given arguments and
     * adds bound values to specified collection.
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
                                                                     final C collection) throws SQLException {
        Objects.requireNonNull(typeNamePattern, "typeNamePattern is null");
        Objects.requireNonNull(attributeNamePattern, "attributeNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getAttributes(catalog, schemaPattern, typeNamePattern,
                                                                attributeNamePattern)) {
            if (results == null) {
                log.warning(
                        String.format("null returned; getAttributes(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                      typeNamePattern, attributeNamePattern));
                return collection;
            }
            return bind(results, Attribute.class, collection);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getAttributes(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  typeNamePattern, attributeNamePattern), sqlfnse);
        }
        return collection;
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
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super BestRowIdentifier>> C getBestRowIdentifier(
            final String catalog, final String schema, @NotBlank final String table, final int scope,
            final boolean nullable, @NotNull final C collection) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getBestRowIdentifier(catalog, schema, table, scope, nullable)) {
            if (results == null) {
                log.warning(String.format("null returned; getBestRowIdentifier(%1$s, %1$s, %3$s, %4$d, %5$s)", catalog,
                                          schema, table, scope, nullable));
                return collection;
            }
            return bind(results, BestRowIdentifier.class, collection);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getBestRowIdentifier(%1$s, %1$s, %3$s, %4$d, %5$s)", catalog, schema,
                                  table, scope, nullable), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getCatalogs()} method and adds all bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    @NotNull
    public <C extends Collection<? super Catalog>> C getCatalogs(@NotNull final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getCatalogs()) {
            if (results != null) {
                return bind(results, Catalog.class, collection);
            }
            log.warning("null returned; getCatalogs()");
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING, "not supported; getCatalogs()", sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getClientInfoProperties()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of element of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public <C extends Collection<? super ClientInfoProperty>> C getClientInfoProperties(final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getClientInfoProperties()) {
            if (results != null) {
                return bind(results, ClientInfoProperty.class, collection);
            }
            log.warning("null returned; getClientInfoProperties()");
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING, "not supported; getClientInfoProperties()", sqlfnse);
        }
        return collection;
    }

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
     * @param <C>               the type of elements of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super ColumnPrivilege>> C getColumnPrivileges(
            final String catalog, final String schema, @NotBlank final String table,
            @NotBlank final String columnNamePattern, final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            if (results != null) {
                return bind(results, ColumnPrivilege.class, collection);
            }
            log.warning(
                    String.format("null returned; getColumnPrivileges(%1$s, %2$s, %3$s, %4$s", catalog, schema, table,
                                  columnNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getColumnPrivileges(%1$s, %2$s, %3$s, %4$s", catalog, schema, table,
                                  columnNamePattern), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getColumns(String, String, String, String)} method with given arguments and adds
     * bound values to specified collection.
     *
     * @param catalog           a value for {@code catalog} parameter.
     * @param schemaPattern     a value for {@code schemaPattern} parameter.
     * @param tableNamePattern  a value for {@code tableNameSchema} parameter.
     * @param columnNamePattern a value for {@code columnNamePattern} parameter.
     * @param collection        the collection to which bound values are added.
     * @param <C>               the type of elements of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    @NotNull
    public <C extends Collection<? super Column>> C getColumns(final String catalog, final String schemaPattern,
                                                               @NotBlank final String tableNamePattern,
                                                               @NotBlank final String columnNamePattern,
                                                               @NotNull final C collection) throws SQLException {
        Objects.requireNonNull(tableNamePattern, "tableNamePattern is null");
        Objects.requireNonNull(columnNamePattern, "columnNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern,
                                                             columnNamePattern)) {
            if (results != null) {
                return bind(results, Column.class, collection);
            }
            log.warning(String.format("null returned; getColumns(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                      tableNamePattern, columnNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getColumns(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  tableNamePattern, columnNamePattern), sqlfnse);
        }
        return collection;
    }

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
     * @param <C>            the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super CrossReference>> C getCrossReference(
            final String parentCatalog, final String parentSchema, @NotBlank final String parentTable,
            final String foreignCatalog, final String foreignSchema, @NotBlank final String foreignTable,
            @NotNull final C collection) throws SQLException {
        Objects.requireNonNull(parentTable, "parentTable is null");
        Objects.requireNonNull(foreignTable, "foreignTable is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getCrossReference(parentCatalog, parentSchema, parentTable,
                                                                    foreignCatalog, foreignSchema, foreignTable)) {
            if (results != null) {
                return bind(results, CrossReference.class, collection);
            }
            log.warning(
                    String.format("null returned; getCrossReference(%1$s, %2$s, %3$s, %4$s, %5$s, %6$s)", parentCatalog,
                                  parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getCrossReference(%1$s, %2$s, %3$s, %4$s, %5$s, %6$s)", parentCatalog,
                                  parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getExportedKeys(String, String, String)
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super ExportedKey>> C getExportedKeys(final String catalog,
                                                                                         final String schema,
                                                                                         @NotBlank final String table,
                                                                                         @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getExportedKeys(catalog, schema, table)) {
            if (results != null) {
                return bind(results, ExportedKey.class, collection);
            }
            log.warning(String.format("null returned; getExportedKeys(%1$s, %2$s, %3$s)", catalog, schema, table));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("null returned; getExportedKeys(%1$s, %2$s, %3$s)", catalog, schema, table), sqlfnse);
        }
        return collection;
    }

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
     */
    @NotNull
    public <C extends Collection<? super Function>> C getFunctions(final String catalog, final String schemaPattern,
                                                                   @NotBlank final String functionNamePattern,
                                                                   @NotNull final C collection) throws SQLException {
        Objects.requireNonNull(functionNamePattern, "functionNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getFunctions(catalog, schemaPattern, functionNamePattern)) {
            if (results != null) {
                return bind(results, Function.class, collection);
            }
            log.warning(String.format("null returned; getFunctions(%1$s, %2$s, %3$s)", catalog, schemaPattern,
                                      functionNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getFunctions(%1$s, %2$s, %3$s)", catalog, schemaPattern,
                                  functionNamePattern), sqlfnse);
        }
        return collection;
    }

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
     */
    @NotNull
    public <C extends Collection<? super FunctionColumn>> @NotNull C getFunctionColumns(final String catalog,
                                                                                        final String schemaPattern,
                                                                                        @NotBlank
                                                                                        final String functionNamePattern,
                                                                                        @NotBlank
                                                                                        final String columnNamePattern,
                                                                                        @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(functionNamePattern, "functionNamePattern is null");
        Objects.requireNonNull(columnNamePattern, "columnNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getFunctionColumns(catalog, schemaPattern, functionNamePattern,
                                                                     columnNamePattern)) {
            if (results != null) {
                return bind(results, FunctionColumn.class, collection);
            }
            log.warning(
                    String.format("null returned; getFunctionColumns(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  functionNamePattern, columnNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getFunctionColumns(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  functionNamePattern, columnNamePattern), sqlfnse);
        }
        return collection;
    }

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
    public <C extends Collection<? super ImportedKey>> C getImportedKeys(final String catalog, final String schema,
                                                                         final String table, final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getImportedKeys(catalog, schema, table)) {
            if (results != null) {
                return bind(results, ImportedKey.class, collection);
            }
            log.warning(String.format("null returned; getImportedKeys(%1$s, %2$s, %3$s)", catalog, schema, table));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("null returned; getImportedKeys(%1$s, %2$s, %3$s)", catalog, schema, table), sqlfnse);
        }
        return collection;
    }

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
     * @param <C>         the type of elements of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    @NotNull
    public <C extends Collection<? super IndexInfo>> C getIndexInfo(final String catalog, final String schema,
                                                                    @NotBlank final String table, final boolean unique,
                                                                    final boolean approximate,
                                                                    @NotNull final C collection) throws SQLException {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getIndexInfo(catalog, schema, table, unique, approximate)) {
            if (results != null) {
                return bind(results, IndexInfo.class, collection);
            }
            log.warning(
                    String.format("null returned; getIndexInfo(%1$s, %2$s, %3$s, %4$b, %5$b)", catalog, schema, table,
                                  unique, approximate));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getIndexInfo(%1$s, %2$s, %3$s, %4$b, %5$b)", catalog, schema, table,
                                  unique, approximate), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method with given arguments and adds
     * bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    @NotNull
    public <C extends Collection<? super PrimaryKey>> C getPrimaryKeys(final String catalog, final String schema,
                                                                       @NotBlank final String table,
                                                                       @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getPrimaryKeys(catalog, schema, table)) {
            if (results != null) {
                return bind(results, PrimaryKey.class, collection);
            }
            log.warning(String.format("null results; getPrimaryKeys(%1$s, %2$s, %3$s)", catalog, schema, table));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("null results; getPrimaryKeys(%1$s, %2$s, %3$s)", catalog, schema, table), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)} method with given arguments
     * and adds values to specified collection.
     *
     * @param catalog              a value for {@code catalog} parameter.
     * @param schemaPattern        a value for {@code schemaPattern} parameter.
     * @param procedureNamePattern a value for {@code procedureNamePattern} parameter.
     * @param columnNamePattern    a value for {@code columnNamePattern} parameter.
     * @param collection           the collection to which bound values are added.
     * @param <C>                  the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getProcedureColumns(String, String, String, String)
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super ProcedureColumn>> C getProcedureColumns(final String catalog,
                                                                                                 final String schemaPattern,
                                                                                                 @NotBlank
                                                                                                 final String procedureNamePattern,
                                                                                                 @NotBlank
                                                                                                 final String columnNamePattern,
                                                                                                 @NotNull
                                                                                                 final C collection)
            throws SQLException {
        Objects.requireNonNull(procedureNamePattern, "procedureNamePattern is null");
        Objects.requireNonNull(columnNamePattern, "columnNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getProcedureColumns(catalog, schemaPattern, procedureNamePattern,
                                                                      columnNamePattern)) {
            if (results != null) {
                return bind(results, ProcedureColumn.class, collection);
            }
            log.warning(
                    String.format("null returned; getProcedureColumns(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  procedureNamePattern, columnNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getProcedureColumns(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  procedureNamePattern, columnNamePattern), sqlfnse);
        }
        return collection;
    }

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
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super Procedure>> C getProcedures(final String catalog,
                                                                                     final String schemaPattern,
                                                                                     @NotBlank
                                                                                     final String procedureNamePattern,
                                                                                     @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(procedureNamePattern, "procedureNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getProcedures(catalog, schemaPattern, procedureNamePattern)) {
            if (results != null) {
                return bind(results, Procedure.class, collection);
            }
            log.warning(String.format("null returned; getProcedures(%1$s, %2$s, %3$s)", catalog, schemaPattern,
                                      procedureNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("null returned; getProcedures(%1$s, %2$s, %3$s)", catalog, schemaPattern,
                                  procedureNamePattern), sqlfnse);
        }
        return collection;
    }

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
     * @param <C>               the type of elements in the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getPseudoColumns(String, String, String, String)
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super PseudoColumn>> C getPseudoColumns(final String catalog,
                                                                                           final String schemaPattern,
                                                                                           @NotBlank
                                                                                           final String tableNamePattern,
                                                                                           @NotBlank
                                                                                           final String columnNamePattern,
                                                                                           @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(tableNamePattern, "tableNamePattern is null");
        Objects.requireNonNull(columnNamePattern, "columnNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getPseudoColumns(catalog, schemaPattern, tableNamePattern,
                                                                   columnNamePattern)) {
            if (results != null) {
                return bind(results, PseudoColumn.class, collection);
            }
            log.warning(String.format("null returned; getPseudoColumns(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                      tableNamePattern, columnNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("no supported; getPseudoColumns(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  tableNamePattern, columnNamePattern), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas()
     * @see #getSchemas(String, String, Collection)
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super Schema>> C getSchemas(@NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSchemas()) {
            if (results != null) {
                return bind(results, Schema.class, collection);
            }
            log.warning("null returned; getSchemas()");
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING, "not supported; getSchemas()", sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSchemas(String, String)} method with given arguments and adds bounds values to
     * specified collection.
     *
     * @param catalog       a value for {@code catalog} parameter.
     * @param schemaPattern a value for {@code schemaPattern} parameter.
     * @param collection    the collection to which bound values are added.
     * @param <C>           the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getSchemas(String, String)
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super Schema>> C getSchemas(final String catalog,
                                                                               final String schemaPattern,
                                                                               @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSchemas(catalog, schemaPattern)) {
            if (results != null) {
                return bind(results, Schema.class, collection);
            }
            log.warning(String.format("null returned; getSchemas(%1$s, %2$s)", catalog, schemaPattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING, String.format("not supported; getSchemas(%1$s, %2$s)", catalog, schemaPattern),
                    sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTables(String, String, String)} method with given arguments and adds
     * bounds values to specified collection.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} paramter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param collection       the collection to which bound values are added.
     * @param <C>              the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super SuperTable>> C getSuperTables(final String catalog, @NotNull
    final String schemaPattern, @NotBlank final String tableNamePattern, @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(schemaPattern, "schemaPattern is null");
        Objects.requireNonNull(tableNamePattern, "tableNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSuperTables(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                return bind(results, SuperTable.class, collection);
            }
            log.warning(String.format("null returned; getSuperTables(%1$s, %2$s, %3$s)", catalog, schemaPattern,
                                      tableNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("null returned; getSuperTables(%1$s, %2$s, %3$s)", catalog, schemaPattern,
                                  tableNamePattern), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getSuperTypes(String, String, String)} method with given arguments and adds bound
     * values to specified collection.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter.
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param collection      the collection to which bound values are added.
     * @param <C>             the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super SuperType>> C getSuperTypes(
            final String catalog, /*@NotNull*/ final String schemaPattern, @NotBlank final String typeNamePattern,
            @NotNull final C collection)
            throws SQLException {
//        Objects.requireNonNull(schemaPattern, "schemaPattern is null");
        Objects.requireNonNull(typeNamePattern, "typeNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getSuperTypes(catalog, schemaPattern, typeNamePattern)) {
            if (results != null) {
                return bind(results, SuperType.class, collection);
            }
            log.warning(String.format("getSuperTypes(%1$s, %2$s, %3$S)", catalog, schemaPattern, typeNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("getSuperTypes(%1$s, %2$s, %3$S)", catalog, schemaPattern, typeNamePattern), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param collection       the collection to which bound values are added.
     * @param <C>              the type of elements in the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    @NotNull
    public <C extends Collection<? super TablePrivilege>> C getTablePrivileges(final String catalog,
                                                                               final String schemaPattern,
                                                                               @NotBlank final String tableNamePattern,
                                                                               @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(tableNamePattern, "tableNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern)) {
            if (results != null) {
                return bind(results, TablePrivilege.class, collection);
            }
            log.warning(String.format("null returned; getTablePrivileges(%1$s, %2$s, %3$s)", catalog, schemaPattern,
                                      tableNamePattern));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getTablePrivileges(%1$s, %2$s, %3$s)", catalog, schemaPattern,
                                  tableNamePattern), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTableTypes()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of elements in the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super TableType>> C getTableTypes(@NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTableTypes()) {
            if (results != null) {
                return bind(results, TableType.class, collection);
            }
            log.warning("null returned; getTableTypes()");
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING, "null returned; getTableTypes()", sqlfnse);
        }
        return collection;
    }

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
     * @param <C>              the type of elements in the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super Table>> C getTables(final String catalog,
                                                                             final String schemaPattern,
                                                                             @NotBlank final String tableNamePattern,
                                                                             final String[] types,
                                                                             @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(tableNamePattern, "tableNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            if (results != null) {
                return bind(results, Table.class, collection);
            }
            log.warning(String.format("null returned; getTables(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                      tableNamePattern, Arrays.toString(types)));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getTables(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  tableNamePattern, Arrays.toString(types)), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getTypeInfo()} method and adds bound values to specified collection.
     *
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of elements in the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public <C extends Collection<? super TypeInfo>> C getTypeInfo(final C collection) throws SQLException {
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getTypeInfo()) {
            if (results != null) {
                return bind(results, TypeInfo.class, collection);
            }
            log.warning("null returned; getTypeInfo()");
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING, "not supported; getTypeInfo()", sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])} method with
     * given arguments and adds all bound values to specified collection.
     *
     * @param catalog         a value for {@code catalog} parameter.
     * @param schemaPattern   a value for {@code schemaPattern} parameter
     * @param typeNamePattern a value for {@code typeNamePattern} parameter.
     * @param types           a value for {@code type} parameter
     * @param collection      the collection to which bound values are added.
     * @param <C>             the type of elements of the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @see DatabaseMetaData#getUDTs(String, String, String, int[])
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super UDT>> C getUDTs(final String catalog,
                                                                         final String schemaPattern,
                                                                         @NotBlank final String typeNamePattern,
                                                                         final int[] types, @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(typeNamePattern, "typeNamePattern is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getUDTs(catalog, schemaPattern, typeNamePattern, types)) {
            if (results != null) {
                return bind(results, UDT.class, collection);
            }
            log.warning(String.format("null returned; getUDTs(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                      typeNamePattern, Arrays.toString(types)));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getUDTs(%1$s, %2$s, %3$s, %4$s)", catalog, schemaPattern,
                                  typeNamePattern, Arrays.toString(types)), sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)} method
     * with given arguments and adds bound values to specified collection.
     *
     * @param catalog    a value for {@code catalog} parameter.
     * @param schema     a value for {@code schema} parameter.
     * @param table      a value for {@code table} parameter.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of elements in the {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#getVersionColumns(String, String, String)
     */
    @NotNull
    public <C extends Collection<@Valid @NotNull ? super VersionColumn>> C getVersionColumns(
            final String catalog, final String schema, @NotBlank final String table, @NotNull final C collection)
            throws SQLException {
        Objects.requireNonNull(table, "table is null");
        Objects.requireNonNull(collection, "collection is null");
        try (ResultSet results = databaseMetaData.getVersionColumns(catalog, schema, table)) {
            if (results != null) {
                return bind(results, VersionColumn.class, collection);
            }
            log.warning(String.format("null returned; getVersionColumns(%1$s, %2$s, %3$s)", catalog, schema, table));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.log(Level.WARNING,
                    String.format("not supported; getVersionColumns(%1$s, %2$s, %3$s)", catalog, schema, table),
                    sqlfnse);
        }
        return collection;
    }

    /**
     * Invokes {@link DatabaseMetaData#deletesAreDetected(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#deletesAreDetected(int)
     * @see DeletesAreDetected#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public DeletesAreDetected deletesAreDetected(final int type) throws SQLException {
        final DeletesAreDetected value = new DeletesAreDetected();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.deletesAreDetected(value.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; deletesAreDetected(%1$d)", type));
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#insertsAreDetected(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#insertsAreDetected(int)
     * @see InsertsAreDetected#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public InsertsAreDetected insertsAreDetected(final int type) throws SQLException {
        final InsertsAreDetected value = new InsertsAreDetected();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.insertsAreDetected(value.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; insertsAreDetected(%1$d)", type));
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#updatesAreDetected(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#updatesAreDetected(int)
     * @see UpdatesAreDetected#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public UpdatesAreDetected updatesAreDetected(final int type) throws SQLException {
        final UpdatesAreDetected value = new UpdatesAreDetected();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.updatesAreDetected(value.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; updatesAreDetected(%1$d)", type));
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#othersDeletesAreVisible(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#othersDeletesAreVisible(int)
     * @see OthersDeletesAreVisible#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public OthersDeletesAreVisible othersDeletesAreVisible(final int type) throws SQLException {
        final OthersDeletesAreVisible result = new OthersDeletesAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersDeletesAreVisible(result.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; othersDeletesAreVisible(%1$d)", type));
        }
        return result;
    }

    /**
     * Invokes {@link DatabaseMetaData#othersInsertsAreVisible(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#othersInsertsAreVisible(int)
     * @see OthersInsertsAreVisible#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public OthersInsertsAreVisible othersInsertsAreVisible(final int type) throws SQLException {
        final OthersInsertsAreVisible result = new OthersInsertsAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersInsertsAreVisible(result.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; othersInsertsAreVisible(%1$d)", type));
        }
        return result;
    }

    /**
     * Invokes {@link DatabaseMetaData#othersUpdatesAreVisible(int)} method with specified argument and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#othersUpdatesAreVisible(int)
     * @see OthersUpdatesAreVisible#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public OthersUpdatesAreVisible othersUpdatesAreVisible(final int type) throws SQLException {
        final OthersUpdatesAreVisible result = new OthersUpdatesAreVisible();
        result.setType(type);
        try {
            result.setValue(databaseMetaData.othersUpdatesAreVisible(result.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; othersUpdatesAreVisible(%1$d)", type));
        }
        return result;
    }

    /**
     * Invokes {@link DatabaseMetaData#ownDeletesAreVisible(int)} method with specified value and returns a bound
     * value.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value whose {@code value} property may be {@code null} when the {@link SQLException} suppressed.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#ownDeletesAreVisible(int)
     * @see OwnDeletesAreVisible#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public OwnDeletesAreVisible ownDeletesAreVisible(final int type) throws SQLException {
        final OwnDeletesAreVisible value = new OwnDeletesAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownDeletesAreVisible(value.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; ownDeletesAreVisible(%1$d)", type));
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
     * @see OwnInsertsAreVisible#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public OwnInsertsAreVisible ownInsertsAreVisible(final int type) throws SQLException {
        final OwnInsertsAreVisible value = new OwnInsertsAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownInsertsAreVisible(value.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; ownInsertsAreVisible(%1$d)", type));
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
     * @see OwnUpdatesAreVisible#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public OwnUpdatesAreVisible ownUpdatesAreVisible(final int type) throws SQLException {
        final OwnUpdatesAreVisible value = new OwnUpdatesAreVisible();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.ownUpdatesAreVisible(value.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; ownUpdatesAreVisible(%1$d)", type));
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#supportsConvert(int, int)} method with given arguments and returns a bound
     * value.
     *
     * @param fromType a value for {@code fromType} parameter.
     * @param toType   a value for {@code toType} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsConvert(int, int)
     * @see SupportsConvert#getAllInstances(Context, Collection)
     * @see java.sql.JDBCType
     */
    public SupportsConvert supportsConvert(final int fromType, final int toType) throws SQLException {
        final SupportsConvert value = new SupportsConvert();
        value.setFromType(fromType);
        value.setToType(toType);
        try {
            value.setValue(databaseMetaData.supportsConvert(value.getFromType(), value.getToType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; supportsConvert(%1$d, %2$d)", fromType, toType));
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetConcurrency(int, int)} method with given arguments and returns a
     * bound value.
     *
     * @param type        a value for {@code type} parameter.
     * @param concurrency a value for {@code concurrency} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsResultSetConcurrency(int, int)
     * @see SupportsResultSetConcurrency#getAllInstances(Context, Collection)
     * @see java.sql.JDBCType
     * @see ResultSetConcurrency
     */
    public SupportsResultSetConcurrency supportsResultSetConcurrency(final int type, final int concurrency)
            throws SQLException {
        final SupportsResultSetConcurrency value = new SupportsResultSetConcurrency();
        value.setType(type);
        value.setConcurrency(concurrency);
        try {
            value.setValue(databaseMetaData.supportsResultSetConcurrency(value.getType(), value.getConcurrency()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; supportsResultSetConcurrency(%1$d, %2$d)", type, concurrency));
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetHoldability(int)} method with given argument and returns a bound
     * value.
     *
     * @param holdability a value for {@code holdability} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsResultSetHoldability(int)
     * @see SupportsResultSetHoldability#getAllInstances(Context, Collection)
     * @see ResultSetHoldability
     */
    public SupportsResultSetHoldability supportsResultSetHoldability(final int holdability) throws SQLException {
        final SupportsResultSetHoldability value = new SupportsResultSetHoldability();
        value.setHoldability(holdability);
        try {
            value.setValue(databaseMetaData.supportsResultSetHoldability(value.getHoldability()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; supportsResultSetHoldability(%1$d)", holdability));
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#supportsResultSetType(int)} method with given argument and returns a bound value.
     * The result may have {@code null} {@code value} when the {@link SQLException} has been
     * {@link #suppress(Class) suppressed}.
     *
     * @param type a value for {@code type} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsResultSetType(int)
     * @see SupportsResultSetType#getAllInstances(Context, Collection)
     * @see ResultSetType
     */
    public SupportsResultSetType supportsResultSetType(final int type) throws SQLException {
        final SupportsResultSetType value = new SupportsResultSetType();
        value.setType(type);
        try {
            value.setValue(databaseMetaData.supportsResultSetType(value.getType()));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; supportsResultSetType(%1$d)", type));
        }
        return value;
    }

    /**
     * Invokes {@link DatabaseMetaData#supportsTransactionIsolationLevel(int)} method with given argument and returns a
     * bound value.
     *
     * @param level a value for {@code level} parameter.
     * @return a bound value.
     * @throws SQLException if a database access error occurs.
     * @see DatabaseMetaData#supportsTransactionIsolationLevel(int)
     * @see SupportsTransactionIsolationLevel#getAllInstances(Context, Collection)
     * @see ConnectionTransactionIsolationLevel
     */
    public SupportsTransactionIsolationLevel supportsTransactionIsolationLevel(final int level) throws SQLException {
        final SupportsTransactionIsolationLevel value = new SupportsTransactionIsolationLevel();
        value.setLevel(level);
        try {
            value.setValue(databaseMetaData.supportsTransactionIsolationLevel(level));
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warning(String.format("not supported; supportsTransactionIsolationLevel(%1$d)", level));
        }
        return value;
    }

    private @NotNull Map<@NotNull Field, @NotNull Label> getLabeledFields(final @NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz is null");
        return classesAndLabeledFields.computeIfAbsent(clazz, c -> Utils.getFieldsAnnotatedWith(c, Label.class));
    }

    final DatabaseMetaData databaseMetaData;

    private final Map<Class<?>, Map<Field, Label>> classesAndLabeledFields = new HashMap<>();
}
