/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */


package com.github.jinahya.sql.database.metadata.bind;


import java.beans.IntrospectionException;
import static java.beans.Introspector.decapitalize;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 * A context class for retrieving information from an instance of
 * {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MetadataContext {


    private static final Logger logger = getLogger(Metadata.class.getName());


    private static String suppression(
        final Class<?> beanClass, final PropertyDescriptor propertyDescriptor) {

        return decapitalize(beanClass.getSimpleName()) + "/"
               + propertyDescriptor.getName();
    }


    /**
     * Creates a new instance with given {@code DatabaseMetaData}.
     *
     * @param database the {@code DatabaseMetaData} instance to hold.
     */
    public MetadataContext(final DatabaseMetaData database) {

        super();

        if (database == null) {
            throw new NullPointerException("null database");
        }

        this.database = database;
    }


    private boolean addSuppression(final String suppression) {

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (suppressions == null) {
            suppressions = new TreeSet<String>();
        }

        return suppressions.add(suppression);
    }


    /**
     * Add suppression paths.
     *
     * @param suppression the first suppression
     * @param otherSuppressions other suppressions
     *
     * @return this
     */
    public MetadataContext addSuppressions(
        final String suppression, final String... otherSuppressions) {

        addSuppression(suppression);

        if (otherSuppressions != null) {
            for (final String otherSuppression : otherSuppressions) {
                addSuppression(otherSuppression);
            }
        }

        return this;
    }


    private boolean suppressed(final String suppression) {

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (suppressions == null) {
            return false;
        }

        return suppressions.contains(suppression);
    }


    private void setPropertyValue(final PropertyDescriptor propertyDescriptor,
                                  final Object beanInstance,
                                  final Object propertyValue,
                                  final Object[] invocationArgs)
        throws ReflectiveOperationException, SQLException,
               IntrospectionException {

        final Class<?> propertyType = propertyDescriptor.getPropertyType();
        if (propertyType == List.class) {
            if (propertyValue == null) {
                return;
            }
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) Beans.getPropertyValue(
                propertyDescriptor, beanInstance);
            if (list == null) {
                list = new ArrayList<Object>();
                Beans.setPropertyValue(propertyDescriptor, beanInstance, list);
            }
            final String propertyName = propertyDescriptor.getName();
            final Field field
                = Reflections.findField(beanInstance.getClass(), propertyName);
            final Class<?> type
                = (Class<?>) ((ParameterizedType) field.getGenericType())
                .getActualTypeArguments()[0];
            if (ResultSet.class.isInstance(propertyValue)) {
                bindAll((ResultSet) propertyValue, type, list);
                Reflections.setParent(type, list, beanInstance);
                return;
            }
            list.add(type
                .getDeclaredMethod("valueOf", Object[].class, Object.class)
                .invoke(null, invocationArgs, propertyValue));
            Reflections.setParent(type, list, beanInstance);
            return;
        }

        Beans.setPropertyValue(propertyDescriptor, beanInstance, propertyValue);
    }


    private <T> T bindSingle(final ResultSet resultSet,
                             final Class<T> beanClass, final T beanInstance)
        throws SQLException, ReflectiveOperationException,
               IntrospectionException {

        if (resultSet != null) {
            for (final PropertyDescriptor propertyDescriptor
                 : Beans.getPropertyDescriptors(beanClass, Label.class)) {
                final String suppression
                    = suppression(beanClass, propertyDescriptor);
                if (suppressed(suppression)) {
                    continue;
                }
                final Label label = Labels.get(propertyDescriptor, beanClass);
                try {
                    final Object value = resultSet.getObject(label.value());
                    Beans.setPropertyValue(
                        propertyDescriptor, beanInstance, value);
                } catch (final SQLException sqle) {
                    logger.log(Level.SEVERE,
                               "failed to get value"
                               + "; label=" + label
                               + ", suppression=" + suppression,
                               sqle);
                    throw sqle;
                } catch (final Exception e) {
                    logger.log(Level.SEVERE,
                               "failed to get value"
                               + "; label=" + label
                               + ", suppression=" + suppression,
                               e);
                    throw new RuntimeException(e);
                }
            }
        }

        for (final PropertyDescriptor propertyDescriptor
             : Beans.getPropertyDescriptors(beanClass, Invocation.class)) {
            final String suppression
                = suppression(beanClass, propertyDescriptor);
            if (suppressed(suppression)) {
                continue;
            }
            final Invocation invocation
                = Invocations.get(propertyDescriptor, beanClass);
            final Class<?>[] types = invocation.types();
            final Method method = DatabaseMetaData.class.getMethod(
                invocation.name(), invocation.types());
            for (final InvocationArgs invocationArgs : invocation.argsarr()) {
                final String[] names = invocationArgs.value();
                final Object[] args = Invocations.values(beanClass, beanInstance, types, names);
//                final Object[] args = new Object[names.length];
//                for (int i = 0; i < names.length; i++) {
//                    final String name = names[i];
//                    if ("null".equals(name)) {
//                        args[i] = null;
//                        continue;
//                    }
//                    if (name.startsWith(":")) {
//                        args[i] = Beans.getPropertyValue(
//                            beanClass, name.substring(1), beanInstance);
//                        continue;
//                    }
//                    if (types[i] == String.class) {
//                        args[i] = name;
//                        continue;
//                    }
//                    if (types[i].isPrimitive()) {
//                        types[i] = Reflections.wrapper(types[i]);
//                    }
//                    args[i] = types[i].getMethod("valueOf", String.class)
//                        .invoke(null, name);
//                }
                try {
                    final Object propertyValue = method.invoke(database, args);
                    setPropertyValue(
                        propertyDescriptor, beanInstance, propertyValue, args);
                } catch (final InvocationTargetException ite) {
                    logger.log(Level.SEVERE,
                               "failed to invoke"
                               + "; invocation=" + invocation
                               + ", suppressin=" + suppression,
                               ite);
                    throw ite;
                } catch (final Exception e) {
                    logger.log(Level.SEVERE,
                               "failed to invoke"
                               + "; invocation=" + invocation
                               + ", suppressin=" + suppression,
                               e);
                    throw new RuntimeException(e);
                } catch (final AbstractMethodError ame) {
                    logger.log(Level.SEVERE,
                               "failed to invoke"
                               + "; invocation=" + invocation
                               + ", suppressin=" + suppression,
                               ame);
                    throw ame;
                }
            }
        }

        if (TableDomain.class.isAssignableFrom(beanClass)) {
            final List<Table> tables = ((TableDomain) beanInstance).getTables();
            final List<CrossReference> crossReferences
                = getCrossReferences(tables);
            ((TableDomain) beanInstance).setCrossReferences(crossReferences);
        }

        return beanInstance;
    }


    private <T> T bindSingle(final ResultSet results, final Class<T> type)
        throws SQLException, ReflectiveOperationException,
               IntrospectionException {

        return bindSingle(results, type, type.newInstance());
    }


    private <T> List<? super T> bindAll(final ResultSet results,
                                        final Class<T> type,
                                        final List<? super T> list)
        throws SQLException, ReflectiveOperationException,
               IntrospectionException {

        while (results.next()) {
            list.add(bindSingle(results, type, type.newInstance()));
        }

        return list;
    }


    private <T> List<? super T> bindAll(final ResultSet results,
                                        final Class<T> type)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        return bindAll(results, type, new ArrayList<T>());
    }


    /**
     * Binds all information.
     *
     * @return a Metadata
     *
     * @throws SQLException if a database occurs.
     * @throws ReflectiveOperationException if a reflection erorr occurs
     */
    public Metadata getMetadata()
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final Metadata metadata = bindSingle(null, Metadata.class);

        if (!suppressed("metadata/catalogs")) {
            final List<Catalog> catalogs = metadata.getCatalogs();
            if (catalogs.isEmpty()) {
                final Catalog catalog = new Catalog();
                catalog.setTableCat("");
                catalog.setParent(metadata);
                logger.log(Level.INFO, "adding an empty catalog: {0}",
                           new Object[]{catalog});
                catalogs.add(catalog);
                bindSingle(null, Catalog.class, catalog);
            }
            if (!suppressed("category/schemas")) {
                for (final Catalog catalog : catalogs) {
                    final List<Schema> schemas = catalog.getSchemas();
                    if (schemas.isEmpty()) {
                        final Schema schema = new Schema();
                        schema.setTableCatalog(catalog.getTableCat());
                        schema.setTableSchem("");
                        schema.setParent(catalog);
                        logger.log(Level.INFO, "adding an empty schema: {0}",
                                   new Object[]{schema});
                        schemas.add(schema);
                        bindSingle(null, Schema.class, schema);
                    }
                }
            }
        }

        final List<SDTSDTBoolean> supportsConvert
            = new ArrayList<SDTSDTBoolean>();
        metadata.setSupportsConvert(supportsConvert);
        supportsConvert.add(
            new SDTSDTBoolean()
            .fromType(null)
            .toType(null)
            .value(database.supportsConvert()));
        final Set<Integer> sqlTypes = Reflections.getSqlTypes();
        for (final int fromType : sqlTypes) {
            for (final int toType : sqlTypes) {
                if (fromType == toType) {
//                    continue;
                }
                supportsConvert.add(
                    new SDTSDTBoolean()
                    .fromType(fromType)
                    .toType(toType)
                    .value(database.supportsConvert(fromType, toType)));
            }
        }

//        final List<CrossReference> crossReferences
//            = new ArrayList<CrossReference>();
//        metadata.setCrossReferences(crossReferences);
//        final List<Table> tables = new ArrayList<Table>();
//        for (final Catalog catalog : metadata.getCatalogs()) {
//            for (final Schema schema : catalog.getSchemas()) {
//                tables.addAll(schema.getTables());
//            }
//        }
//        for (final Table fktable : tables) {
//            for (final Table pktable : tables) {
//                crossReferences.addAll(
//                    MetadataContext.this.getCrossReferences(
//                        pktable.getTableCat(), pktable.getTableSchem(),
//                        pktable.getTableName(),
//                        fktable.getTableCat(), fktable.getTableSchem(),
//                        fktable.getTableName()));
//            }
//        }
        return metadata;
    }


    public List<Attribute> getAttributes(final String catalog,
                                         final String schemaPattern,
                                         final String typeNamePattern,
                                         final String attributeNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<Attribute> list = new ArrayList<Attribute>();

        final ResultSet results = database.getAttributes(
            catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        try {
            bindAll(results, Attribute.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<BestRowIdentifier> getBestRowIdentifier(
        final String catalog, final String schema, final String table,
        final int scope, final boolean nullable)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<BestRowIdentifier> list = new ArrayList<BestRowIdentifier>();

        final ResultSet results = database.getBestRowIdentifier(
            catalog, schema, table, scope, nullable);
        try {
            bindAll(results, BestRowIdentifier.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Catalog> getCatalogs()
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<Catalog> list = new ArrayList<Catalog>();

        final ResultSet results = database.getCatalogs();
        try {
            bindAll(results, Catalog.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ClientInfoProperty> getClientInfoProperties()
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<ClientInfoProperty> list
            = new ArrayList<ClientInfoProperty>();

        final ResultSet results = database.getClientInfoProperties();
        try {
            bindAll(results, ClientInfoProperty.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Column> getColumns(final String catalog,
                                   final String schemaPattern,
                                   final String tableNamePattern,
                                   final String columnNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<Column> list = new ArrayList<Column>();

        final ResultSet resultSet = database.getColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            bindAll(resultSet, Column.class, list);
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<ColumnPrivilege> getColumnPrivileges(
        final String catalog, final String schema, final String table,
        final String columnNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<ColumnPrivilege> list = new ArrayList<ColumnPrivilege>();

        final ResultSet results = database.getColumnPrivileges(
            catalog, schema, table, columnNamePattern);
        try {
            bindAll(results, ColumnPrivilege.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<CrossReference> getCrossReferences(
        final String parentCatalog, final String parentSchema,
        final String parentTable,
        final String foreignCatalog, final String foreignSchema,
        final String foreignTable)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<CrossReference> list = new ArrayList<CrossReference>();

        final ResultSet results = database.getCrossReference(
            parentCatalog, parentSchema, parentTable, foreignCatalog,
            foreignSchema, foreignTable);
        try {
            bindAll(results, CrossReference.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<CrossReference> getCrossReferences(
        final Table parentTable,
        final Table foreignTable)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        return getCrossReferences(
            parentTable.getTableCat(), parentTable.getTableSchem(),
            parentTable.getTableName(),
            foreignTable.getTableCat(), foreignTable.getTableSchem(),
            foreignTable.getTableName());
    }


    List<CrossReference> getCrossReferences(final List<Table> tables)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<CrossReference> list = new ArrayList<CrossReference>();

        for (final Table parentTable : tables) {
            for (final Table foreignTable : tables) {
                list.addAll(getCrossReferences(parentTable, foreignTable));
            }
        }

        return list;
    }


    public List<FunctionColumn> getFunctionColumns(
        final String catalog, final String schemaPattern,
        final String functionNamePattern, final String columnNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<FunctionColumn> list = new ArrayList<FunctionColumn>();

        final ResultSet results = database.getFunctionColumns(
            catalog, schemaPattern, functionNamePattern, columnNamePattern);
        try {
            bindAll(results, FunctionColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Function> getFunctions(final String catalog,
                                       final String schemaPattern,
                                       final String functionNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<Function> list = new ArrayList<Function>();

        final ResultSet results = database.getFunctions(
            catalog, schemaPattern, functionNamePattern);
        try {
            bindAll(results, Function.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ExportedKey> getExportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<ExportedKey> list = new ArrayList<ExportedKey>();

        final ResultSet results = database.getExportedKeys(
            catalog, schema, table);
        try {
            bindAll(results, ExportedKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ImportedKey> getImportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<ImportedKey> list = new ArrayList<ImportedKey>();

        final ResultSet results = database.getImportedKeys(
            catalog, schema, table);
        try {
            bindAll(results, ImportedKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<IndexInfo> getIndexInfo(
        final String catalog, final String schema, final String table,
        final boolean unique, final boolean approximate)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<IndexInfo> list = new ArrayList<IndexInfo>();

        final ResultSet results = database.getIndexInfo(
            catalog, schema, table, unique, approximate);
        try {
            bindAll(results, IndexInfo.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<PrimaryKey> getPrimaryKeys(
        final String catalog, final String schema, final String table)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<PrimaryKey> list = new ArrayList<PrimaryKey>();

        final ResultSet results = database.getPrimaryKeys(
            catalog, schema, table);
        try {
            bindAll(results, PrimaryKey.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<ProcedureColumn> getProcedureColumns(
        final String catalog, final String schemaPattern,
        final String procedureNamePattern, final String columnNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<ProcedureColumn> list = new ArrayList<ProcedureColumn>();

        final ResultSet results = database.getProcedureColumns(
            catalog, schemaPattern, procedureNamePattern, columnNamePattern);
        try {
            bindAll(results, ProcedureColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Procedure> getProcedures(final String catalog,
                                         final String schemaPattern,
                                         final String procedureNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<Procedure> list = new ArrayList<Procedure>();

        final ResultSet results = database.getProcedures(
            catalog, schemaPattern, procedureNamePattern);
        try {
            bindAll(results, Procedure.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<PseudoColumn> getPseudoColumns(final String catalog,
                                               final String schemaPattern,
                                               final String tableNamePattern,
                                               final String columnNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<PseudoColumn> list = new ArrayList<PseudoColumn>();

        final ResultSet results = database.getPseudoColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            bindAll(results, PseudoColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<SchemaName> getSchemas()
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<SchemaName> list = new ArrayList<SchemaName>();

        final ResultSet results = database.getSchemas();
        try {
            bindAll(results, SchemaName.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<Schema> getSchemas(final String catalog,
                                   final String schemaPattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<Schema> list = new ArrayList<Schema>();

        final ResultSet results = database.getSchemas(
            catalog, schemaPattern);
        try {
            bindAll(results, Schema.class, list);
        } finally {
            results.close();
        }

        if (list.isEmpty()) {
            final Schema schema = new Schema();
            schema.setTableSchem("");
            list.add(schema);
        }

        return list;
    }


    public List<Table> getTables(final String catalog,
                                 final String schemaPattern,
                                 final String tableNamePattern,
                                 final String[] types)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<Table> list = new ArrayList<Table>();

        final ResultSet results = database.getTables(
            catalog, schemaPattern, tableNamePattern, types);
        try {
            bindAll(results, Table.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<TablePrivilege> getTablePrivileges(
        final String catalog, final String schemaPattern,
        final String tableNamePattern)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<TablePrivilege> list = new ArrayList<TablePrivilege>();

        final ResultSet results = database.getTablePrivileges(
            catalog, schemaPattern, tableNamePattern);
        try {
            bindAll(results, TablePrivilege.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<TableType> getTableTypes()
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<TableType> list = new ArrayList<TableType>();

        final ResultSet results = database.getTableTypes();
        try {
            bindAll(results, TableType.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<TypeInfo> getTypeInfo()
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<TypeInfo> list = new ArrayList<TypeInfo>();

        final ResultSet results = database.getTypeInfo();
        try {
            bindAll(results, TypeInfo.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<UserDefinedType> getUDTs(
        final String catalog, final String schemaPattern,
        final String typeNamePattern, final int[] types)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<UserDefinedType> list = new ArrayList<UserDefinedType>();

        final ResultSet results = database.getUDTs(
            catalog, schemaPattern, typeNamePattern, types);
        try {
            bindAll(results, UserDefinedType.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    public List<VersionColumn> getVersionColumns(final String catalog,
                                                 final String schema,
                                                 final String table)
        throws SQLException, ReflectiveOperationException, IntrospectionException {

        final List<VersionColumn> list = new ArrayList<VersionColumn>();

        final ResultSet results = database.getVersionColumns(
            catalog, schema, table);
        try {
            bindAll(results, VersionColumn.class, list);
        } finally {
            results.close();
        }

        return list;
    }


    private final DatabaseMetaData database;


    private Set<String> suppressions;


}

