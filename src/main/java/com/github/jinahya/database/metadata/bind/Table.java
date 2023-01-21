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
 * A class for binding results of
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ParentOf(VersionColumn.class)
@ParentOf(SuperTable.class)
@ParentOf(PseudoColumn.class)
@ParentOf(PrimaryKey.class)
@ParentOf(IndexInfo.class)
@ParentOf(ImportedKey.class)
@ParentOf(ExportedKey.class)
@ParentOf(Column.class)
@ParentOf(BestRowIdentifier.class)
@ChildOf(Schema.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Table
        extends AbstractMetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    /**
     * The label of the column to which {@link #ATTRIBUTE_NAME_TABLE_CAT} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_CAT} column is bound. The value is {@value}.
     */
    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    /**
     * The label of the column to which {@link #ATTRIBUTE_NAME_TABLE_SCHEM} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_SCHEM} column is bound. The value is {@value}.
     */
    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    /**
     * The label of the column to which {@link #ATTRIBUTE_NAME_TABLE_NAME} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_NAME} column is bound. The value is {@value}.
     */
    public static final String ATTRIBUTE_NAME_TABLE_NAME = "tableName";

    public List<BestRowIdentifier> getBestRowIdentifier(final Context context, final int scope, final boolean nullable)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getBestRowIdentifier(getTableCat(), getTableSchem(), getTableName(), scope, nullable);
    }

    public List<ColumnPrivilege> getColumnPrivileges(final Context context, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getColumnPrivileges(getTableCat(), getTableSchem(), getTableName(), columnNamePattern);
    }

    /**
     * Retrieves columns of this table.
     *
     * @param context           a context.
     * @param columnNamePattern a column name pattern; must match the column name as it is stored in the database
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     */
    public List<Column> getColumns(final Context context, final String columnNamePattern) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getColumns(getTableCat(), getTableSchem(), getTableName(), columnNamePattern);
    }

    public List<CrossReference> getCrossReference(final Context context, String foreignCatalog, String foreignSchema,
                                                  String foreignTable)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getCrossReference(getTableCat(), getTableSchem(), getTableName(), foreignCatalog, foreignSchema,
                                         foreignTable);
    }

    public List<CrossReference> getCrossReference(final Context context, final Table foreignTable) throws SQLException {
        Objects.requireNonNull(foreignTable, "foreignTable is null");
        return getCrossReference(context, foreignTable.getTableCat(), foreignTable.getTableSchem(),
                                 foreignTable.getTableName());
    }

    public List<ExportedKey> getExportedKeys(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getExportedKeys(getTableCat(), getTableSchem(), getTableName());
    }

    public List<ImportedKey> getImportedKeys(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getImportedKeys(getTableCat(), getTableSchem(), getTableName());
    }

    public List<IndexInfo> getIndexInfos(final Context context, final boolean unique, final boolean approximate)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getIndexInfo(getTableCat(), getTableSchem(), getTableName(), unique, approximate);
    }

    public List<PrimaryKey> getPrimaryKeys(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getPrimaryKeys(getTableCat(), getTableSchem(), getTableName());
    }

    public List<PseudoColumn> getPseudoColumns(final Context context, final String columnNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getPseudoColumns(getTableCat(), getTableSchem(), getTableName(), columnNamePattern);
    }

    public List<TablePrivilege> getTablePrivileges(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getTablePrivileges(getTableCat(), getTableSchem(), getTableName());
    }

    public List<VersionColumn> getVersionColumns(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getVersionColumns(getTableCat(), getTableSchem(), getTableName());
    }

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @ColumnLabel("TABLE_TYPE")
    private String tableType;

    @NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("TYPE_CAT")
    private String typeCat;

    @NullableBySpecification
    @ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @NullableBySpecification
    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @NullableBySpecification
    @ColumnLabel("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @NullableBySpecification
    @ColumnLabel("REF_GENERATION")
    private String refGeneration;
}
