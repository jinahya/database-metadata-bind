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


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MetadataContext {


    public MetadataContext(final DatabaseMetaData databaseMetaData) {

        super();

        if (databaseMetaData == null) {
            throw new NullPointerException("databaseMetaData");
        }

        this.databaseMetaData = databaseMetaData;
    }


    public DatabaseMetaData getDatabaseMetaData() {

        return databaseMetaData;
    }


    public boolean addSuppressionPath(final String suppressionPath) {

        if (suppressionPath == null) {
            throw new NullPointerException("null suppressionPath");
        }

        if (suppressionPaths == null) {
            suppressionPaths = new TreeSet<String>();
        }

        return suppressionPaths.add(suppressionPath);
    }


    public MetadataContext suppressionPath(
        final String suppressionPath, final String... otherSuppressionPaths) {

        addSuppressionPath(suppressionPath);

        if (otherSuppressionPaths != null) {
            for (final String otherSuppressionPath : otherSuppressionPaths) {
                addSuppressionPath(otherSuppressionPath);
            }
        }

        return this;
    }


    boolean suppressed(final String path) {

        if (path == null) {
            throw new NullPointerException("null path");
        }

        if (suppressionPaths == null) {
            return false;
        }

        return suppressionPaths.contains(path);
    }


    public Metadata getMetadata() throws SQLException {

        final Metadata instance = new Metadata();

        instance.setAllProceduresAreCallable(databaseMetaData.allProceduresAreCallable());
        instance.setAllTablesAreSelectable(databaseMetaData.allTablesAreSelectable());

        instance.setCatalogSeparator(databaseMetaData.getCatalogSeparator());
        instance.setCatalogTerm(databaseMetaData.getCatalogTerm());

        instance.setDatabaseMajorVersion(databaseMetaData.getDatabaseMajorVersion());
        instance.setDatabaseMinorVersion(databaseMetaData.getDatabaseMinorVersion());
        instance.setDatabaseProductName(databaseMetaData.getDatabaseProductName());
        instance.setDatabaseProductVersion(databaseMetaData.getDatabaseProductVersion());

        instance.setDriverMajorVersion(databaseMetaData.getDriverMajorVersion());
        instance.setDriverMinorVersion(databaseMetaData.getDriverMinorVersion());
        instance.setDriverName(databaseMetaData.getDriverName());
        instance.setDriverVersion(databaseMetaData.getDriverVersion());

        instance.setNumericFunctions(databaseMetaData.getNumericFunctions());
        instance.setStringFunctions(databaseMetaData.getStringFunctions());
        instance.setSystemFunctions(databaseMetaData.getSystemFunctions());
        instance.setTimeDateFunctions(databaseMetaData.getTimeDateFunctions());

        if (!suppressed("metadata/catalogs")) {
            instance.getCatalogs().addAll(getCatalogs());
        }

        if (!suppressed("metadata/clientProperties")) {
            instance.getClientInfoProperties().addAll(
                getClientInfoProperties());
        }

        if (!suppressed("metadata/typeInfo")) {
            instance.getTypeInfo().addAll(getTypeInfo());
        }

        if (!suppressed("metadata/tableTypes")) {
            instance.getTypeInfo().addAll(getTypeInfo());
        }

        return instance;
    }


    public List<Attribute> getAttributes(final String catalog,
                                         final String schemaPattern,
                                         final String typeNamePattern,
                                         final String attributeNamePattern)
        throws SQLException {

        final List<Attribute> list = new ArrayList<Attribute>();

        final ResultSet resultSet = databaseMetaData.getAttributes(
            catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, Attribute.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<BestRowIdentifier> getBestRowIdentifier(
        final String catalog, final String schema, final String table,
        final int scope, final boolean nullable)
        throws SQLException {

        final List<BestRowIdentifier> list = new ArrayList<BestRowIdentifier>();

        final ResultSet resultSet = databaseMetaData.getBestRowIdentifier(
            catalog, schema, table, scope, nullable);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, BestRowIdentifier.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<Catalog> getCatalogs() throws SQLException {

        final List<Catalog> list = new ArrayList<Catalog>();

        final ResultSet resultSet = databaseMetaData.getCatalogs();
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, Catalog.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        if (list.isEmpty()) {
            final Catalog catalog = new Catalog();
            catalog.setTableCat("");
            list.add(catalog);
        }

        if (!suppressed("catalog/schemas")) {
            for (final Catalog catalog : list) {
                catalog.getSchemas().addAll(
                    getSchema(catalog.getTableCat(), null));
            }
        }

        return list;
    }


    public List<ClientInfoProperty> getClientInfoProperties()
        throws SQLException {

        final List<ClientInfoProperty> list
            = new ArrayList<ClientInfoProperty>();

        final ResultSet resultSet = databaseMetaData.getClientInfoProperties();
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, ClientInfoProperty.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<Column> getColumns(final String catalog,
                                   final String schemaPattern,
                                   final String tableNamePattern,
                                   final String columnNamePattern)
        throws SQLException {

        final List<Column> list = new ArrayList<Column>();

        final ResultSet resultSet = databaseMetaData.getColumns(
            catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, Column.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<ColumnPrivilege> getColumnPrivileges(
        final String catalog, final String schema, final String table,
        final String columnNamePattern)
        throws SQLException {

        final List<ColumnPrivilege> list = new ArrayList<ColumnPrivilege>();

        final ResultSet resultSet = databaseMetaData.getColumnPrivileges(
            catalog, schema, table, columnNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, ColumnPrivilege.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<Function> getFunctions(final String catalog,
                                       final String schemaPattern,
                                       final String functionNamePattern)
        throws SQLException {

        final List<Function> functions = new ArrayList<Function>();

        final ResultSet resultSet = databaseMetaData.getFunctions(
            catalog, schemaPattern, functionNamePattern);
        try {
            while (resultSet.next()) {
                functions.add(ColumnRetriever.retrieve(
                    this, Function.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return functions;
    }


    public List<ExportedKey> getExportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<ExportedKey> list = new ArrayList<ExportedKey>();

        final ResultSet resultSet = databaseMetaData.getExportedKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, ExportedKey.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<ImportedKey> getImportedKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<ImportedKey> list = new ArrayList<ImportedKey>();

        final ResultSet resultSet = databaseMetaData.getImportedKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, ImportedKey.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<IndexInfo> getIndexInfo(
        final String catalog, final String schema, final String table,
        final boolean unique, final boolean approximate)
        throws SQLException {

        final List<IndexInfo> list = new ArrayList<IndexInfo>();

        final ResultSet resultSet = databaseMetaData.getIndexInfo(
            catalog, schema, table, unique, approximate);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, IndexInfo.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<PrimaryKey> getPrimaryKeys(
        final String catalog, final String schema, final String table)
        throws SQLException {

        final List<PrimaryKey> list = new ArrayList<PrimaryKey>();

        final ResultSet resultSet = databaseMetaData.getPrimaryKeys(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, PrimaryKey.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<Procedure> getProcedures(final String catalog,
                                         final String schemaPattern,
                                         final String procedureNamePattern)
        throws SQLException {

        final List<Procedure> list = new ArrayList<Procedure>();

        final ResultSet resultSet = databaseMetaData.getProcedures(
            catalog, schemaPattern, procedureNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, Procedure.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<Schema> getSchema(final String catalog,
                                  final String schemaPattern)
        throws SQLException {

        final List<Schema> list = new ArrayList<Schema>();

        final ResultSet resultSet = databaseMetaData.getSchemas(
            catalog, schemaPattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, Schema.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        if (list.isEmpty()) {
            final Schema schema = new Schema();
            schema.setTableSchem("");
            list.add(schema);
        }

        for (final Schema schema : list) {
            if (!suppressed("schema/functions")) {
                schema.getFunctions().addAll(getFunctions(
                    schema.getTableCatalog(), schema.getTableSchem(), null));
            }
            if (!suppressed("schema/procedures")) {
                schema.getProcedures().addAll(getProcedures(
                    schema.getTableCatalog(), schema.getTableSchem(), null));
            }
            if (!suppressed("schema/tables")) {
                schema.getTables().addAll(getTables(
                    catalog, schemaPattern, null, null));
            }
            if (!suppressed("schema/userDefinedTypes")) {
                schema.getUserDefinedTypes().addAll(getUDTs(
                    catalog, schemaPattern, null, null));
            }
        }

        return list;
    }


    public List<Table> getTables(final String catalog,
                                 final String schemaPattern,
                                 final String tableNamePattern,
                                 final String[] types)
        throws SQLException {

        final List<Table> list = new ArrayList<Table>();

        final ResultSet resultSet = databaseMetaData.getTables(
            catalog, schemaPattern, tableNamePattern, types);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, Table.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        for (final Table table : list) {
            if (!suppressed("table/bestRowIdentifiers")) {
                table.getBestRowIdentifiers().addAll(getBestRowIdentifier(
                    catalog, schemaPattern, table.getTableName(),
                    DatabaseMetaData.bestRowTemporary, true));
                table.getBestRowIdentifiers().addAll(getBestRowIdentifier(
                    catalog, schemaPattern, table.getTableName(),
                    DatabaseMetaData.bestRowTransaction, true));
                table.getBestRowIdentifiers().addAll(getBestRowIdentifier(
                    catalog, schemaPattern, table.getTableName(),
                    DatabaseMetaData.bestRowSession, true));
            }
            if (!suppressed("table/columns")) {
                table.getColumns().addAll(getColumns(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName(), null));
            }
            if (!suppressed("table/columnPrivileges")) {
                table.getColumnPrivileges().addAll(getColumnPrivileges(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName(), null));
            }
            if (!suppressed("table/exportedKeys")) {
                table.getExportedKeys().addAll(getExportedKeys(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
            if (!suppressed("table/importedKeys")) {
                table.getImportedKeys().addAll(getImportedKeys(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
            if (!suppressed("table/indexInfo")) {
                table.getIndexInfo().addAll(getIndexInfo(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName(), false, false));
            }
            if (!suppressed("table/primaryKeys")) {
                table.getPrimaryKeys().addAll(getPrimaryKeys(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
            if (!suppressed("table/tablePrivileges")) {
                table.getTablePrivileges().addAll(getTablePrivileges(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
            if (!suppressed("table/versionColumns")) {
                table.getVersionColumns().addAll(getVersionColumns(
                    table.getTableCat(), table.getTableSchem(),
                    table.getTableName()));
            }
        }

        return list;
    }


    public List<TablePrivilege> getTablePrivileges(
        final String catalog, final String schemaPattern,
        final String tableNamePattern)
        throws SQLException {

        final List<TablePrivilege> list = new ArrayList<TablePrivilege>();

        final ResultSet resultSet = databaseMetaData.getTablePrivileges(
            catalog, schemaPattern, tableNamePattern);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, TablePrivilege.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<TableType> getTableType() throws SQLException {

        final List<TableType> list = new ArrayList<TableType>();

        final ResultSet resultSet = databaseMetaData.getTableTypes();
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, TableType.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<TypeInfo> getTypeInfo() throws SQLException {

        final List<TypeInfo> list = new ArrayList<TypeInfo>();

        final ResultSet resultSet = databaseMetaData.getTypeInfo();
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, TypeInfo.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    public List<UserDefinedType> getUDTs(
        final String catalog, final String schemaPattern,
        final String typeNamePattern, final int[] types)
        throws SQLException {

        final List<UserDefinedType> list = new ArrayList<UserDefinedType>();

        final ResultSet resultSet = databaseMetaData.getUDTs(
            catalog, schemaPattern, typeNamePattern, types);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, UserDefinedType.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        if (!suppressed("userDefinedType/attributes")) {
            for (final UserDefinedType userDefinedType : list) {
                userDefinedType.getAttributes().addAll(getAttributes(
                    userDefinedType.getTypeCat(),
                    userDefinedType.getTypeSchem(),
                    userDefinedType.getTypeName(), null));
            }
        }

        return list;
    }


    public List<VersionColumn> getVersionColumns(final String catalog,
                                                 final String schema,
                                                 final String table)
        throws SQLException {

        final List<VersionColumn> list = new ArrayList<VersionColumn>();

        final ResultSet resultSet = databaseMetaData.getVersionColumns(
            catalog, schema, table);
        try {
            while (resultSet.next()) {
                list.add(ColumnRetriever.retrieve(
                    this, VersionColumn.class, resultSet));
            }
        } finally {
            resultSet.close();
        }

        return list;
    }


    private final DatabaseMetaData databaseMetaData;


    private Set<String> suppressionPaths;


}

