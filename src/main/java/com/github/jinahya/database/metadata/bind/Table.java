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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiPredicate;

import static java.util.Comparator.naturalOrder;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[])
 */

@_ParentOf(VersionColumn.class)
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Table extends AbstractMetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Table> comparingCaseInsensitiveOrder(final Context context) throws SQLException {
        return Comparator.comparing(Table::getTableType, ContextUtils.nulls(context, String.CASE_INSENSITIVE_ORDER))
                .thenComparing(Table::getTableCat, ContextUtils.nulls(context, String.CASE_INSENSITIVE_ORDER))
                .thenComparing(Table::getTableSchem, ContextUtils.nulls(context, String.CASE_INSENSITIVE_ORDER))
                .thenComparing(Table::getTableName, String.CASE_INSENSITIVE_ORDER);
    }

    static Comparator<Table> comparingLexicographicOrder(final Context context) throws SQLException {
        return Comparator.comparing(Table::getTableType, ContextUtils.nulls(context, naturalOrder()))
                .thenComparing(Table::getTableCat, ContextUtils.nulls(context, naturalOrder()))
                .thenComparing(Table::getTableSchem, ContextUtils.nulls(context, naturalOrder()))
                .thenComparing(Table::getTableName);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    // -----------------------------------------------------------------------------------------------------------------
    static final BiPredicate<Table, Catalog> IS_OF_CATALOG = (t, c) -> {
        return Objects.equals(t.tableCat, c.getTableCat());
    };

    static final BiPredicate<Table, Schema> IS_OF_SCHEMA = (t, s) -> {
        return Objects.equals(t.tableCat, s.getTableCatalog()) &&
               Objects.equals(t.tableSchem, s.getTableSchem());
    };

    // -----------------------------------------------------------------------------------------------------------------
    static Table of(final String tableCat, final String tableSchem, final String tableName) {
        final Table instance = new Table();
        instance.setTableCat(tableCat);
        instance.setTableSchem(tableSchem);
        instance.setTableName(tableName);
        return instance;
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

    // ------------------------------------------------------------------------------------------------------- tableShem

    // -----------------------------------------------------------------------------------------------------------------

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    @EqualsAndHashCode.Include
    private String tableCat;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
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
