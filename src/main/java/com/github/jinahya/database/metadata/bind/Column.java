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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of {@link DatabaseMetaData#getColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumns(String, String, String, String)
 */
@ParentOf(ColumnPrivilege.class)
@ChildOf(Table.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Column
        extends AbstractMetadataType {

    private static final long serialVersionUID = -409653682729081530L;

    public static final Comparator<Column> COMPARING_TABLE_CAT_TABLE_SCHEM_TABLE_NAME_ORDINAL_POSITION =
            Comparator.comparing(Column::getTableCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Column::getTableSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Column::getTableName)
                    .thenComparingInt(Column::getOrdinalPosition);

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    public static final String COLUMN_LABEL_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

    public static final String COLUMN_LABEL_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    public ColumnId getColumnId() {
        return ColumnId.of(getTableCat(), getTableSchem(), getTableName(), getColumnName());
    }

    /**
     * Retrieves a description of the access rights for this column.
     *
     * @param context a context.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     * @see Context#getColumnPrivileges(String, String, String, String)
     */
    public List<ColumnPrivilege> getColumnPrivileges(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getColumnPrivileges(
                getTableCatNonNull(),
                getTableSchemNonNull(),
                getTableName(),
                getColumnName()
        );
    }

    String getTableCatNonNull() {
        return Optional.ofNullable(getTableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getTableSchemNonNull() {
        return Optional.ofNullable(getTableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @NullableBySpecification
    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @NotUsedBySpecification
    @ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;

    @ColumnLabel("NULLABLE")
    private int nullable;

    @NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @ColumnLabel("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @NullableBySpecification
    @ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @NullableBySpecification
    @ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @NullableBySpecification
    @ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    @NullableBySpecification
    @ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;

    @ColumnLabel(COLUMN_LABEL_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @ColumnLabel(COLUMN_LABEL_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;
}
