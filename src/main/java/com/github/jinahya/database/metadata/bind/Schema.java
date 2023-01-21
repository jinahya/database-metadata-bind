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

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding a result of {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ParentOf(UDT.class)
@ParentOf(TablePrivilege.class)
@ParentOf(Table.class)
@ParentOf(SuperType.class)
@ParentOf(SuperTable.class)
@ParentOf(Procedure.class)
@ParentOf(Function.class)
@ChildOf(Catalog.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Schema
        extends AbstractMetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_VALUE_TABLE_SCHEM_EMPTY = "";

    public List<Attribute> getAttributes(final Context context, final String typeNamePattern,
                                         final String attributeNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getAttributes(getTableCatalog(), getTableSchem(), typeNamePattern, attributeNamePattern);
    }

    public List<Column> getColumns(final Context context, final String tableNamePattern, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getColumns(getTableCatalog(), getTableSchem(), tableNamePattern, columnNamePattern);
    }

    public List<FunctionColumn> getFunctionColumns(final Context context, final String functionNamePattern,
                                                   final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getFunctionColumns(getTableCatalog(), getTableSchem(), functionNamePattern, columnNamePattern);
    }

    public List<Function> getFunctions(final Context context, final String functionNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getFunctions(getTableCatalog(), getTableSchem(), functionNamePattern);
    }

    public List<ProcedureColumn> getProcedureColumns(final Context context, final String procedureNamePattern,
                                                     final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getProcedureColumns(getTableCatalog(), getTableSchem(), procedureNamePattern, columnNamePattern);
    }

    public List<Procedure> getProcedures(final Context context, final String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getProcedures(getTableCatalog(), getTableSchem(), procedureNamePattern);
    }

    public List<PseudoColumn> getPseudoColumns(final Context context, final String tableNamePattern,
                                               final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getPseudoColumns(getTableCatalog(), getTableSchem(), tableNamePattern, columnNamePattern);
    }

    public List<SuperTable> getSuperTables(final Context context, final String tableNamePattern) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getSuperTables(getTableCatalog(), getTableSchem(), tableNamePattern);
    }

    public List<SuperType> getSuperTypes(final Context context, final String tableNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getSuperTypes(getTableCatalog(), getTableSchem(), tableNamePattern);
    }

    public List<TablePrivilege> getTablePrivileges(final Context context, final String tableNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getTablePrivileges(getTableCatalog(), getTableSchem(), tableNamePattern);
    }

    public List<Table> getTables(final Context context, final String tableNamePattern, final String[] types)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getTables(getTableCatalog(), getTableSchem(), tableNamePattern, types);
    }

    public List<UDT> getUDTs(final Context context, final String typeNamePattern, final int[] types)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getUDTs(getTableCatalog(), getTableSchem(), typeNamePattern, types);
    }

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;

    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;
}
