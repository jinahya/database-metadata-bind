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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[])
 */

@_ParentOf(Column.class)
@_ParentOf(IndexInfo.class)
@_ParentOf(PseudoColumn.class)
@_ParentOf(VersionColumn.class)
@Setter
@Getter
@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Table
        extends AbstractMetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Table> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .comparing(Table::getTableType, ContextUtils.nulls(context, comparator))
                .thenComparing(Table::getTableCat, ContextUtils.nulls(context, comparator))
                .thenComparing(Table::getTableSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(Table::getTableName, comparator);
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------ TABLE_NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ------------------------------------------------------------------------------------------------------ TABLE_TYPE

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    // -----------------------------------------------------------------------------------------------------------------
    static Table of(final String tableCat, final String tableSchem, final String tableName) {
        final Table instance = new Table();
        instance.setTableCat(tableCat);
        instance.setTableSchem(tableSchem);
        instance.setTableName(tableName);
        return instance;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected Table() {
        super();
        this.tableCat = tableCat;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final Table that = (Table) obj;
        return Objects.equals(tableType, that.tableType) &&
               Objects.equals(tableCat, that.tableCat) &&
               Objects.equals(tableSchem, that.tableSchem) &&
               Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                tableType,
                tableCat,
                tableSchem,
                tableName
        );
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    // ------------------------------------------------------------------------------------------------------- tableShem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
    public String getTableName() {
        return tableName;
    }

    // ------------------------------------------------------------------------------------------------------- tableType

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_TYPE} column.
     */
    public String getTableType() {
        return tableType;
    }

    // -----------------------------------------------------------------------------------------------------------------

    //    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    @EqualsAndHashCode.Include
    private String tableCat;

    //    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    @EqualsAndHashCode.Include
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
    @EqualsAndHashCode.Include
    private String tableType;

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("TYPE_CAT")
    private String typeCat;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("REF_GENERATION")
    private String refGeneration;
}
