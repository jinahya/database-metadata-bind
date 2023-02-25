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

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[])
 */
@ParentOf(VersionColumn.class)
@ParentOf(PseudoColumn.class)
@ParentOf(PrimaryKey.class)
@ParentOf(IndexInfo.class)
@ParentOf(ImportedKey.class)
@ParentOf(ExportedKey.class)
@ParentOf(Column.class)
@ParentOf(BestRowIdentifier.class)
@ChildOf(Schema.class)
//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Table extends AbstractMetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    public static final Comparator<Table> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Table::getTableType, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Table::getTableId, TableId.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Table> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Table::getTableType, nullsFirst(naturalOrder()))
                    .thenComparing(Table::getTableId, TableId.LEXICOGRAPHIC_ORDER);

    /**
     * The label of the column to which {@link #PROPERTY_NAME_TABLE_CAT} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_CAT} column is bound. The value is {@value}.
     */
    public static final String PROPERTY_NAME_TABLE_CAT = "tableCat";

    /**
     * The label of the column to which {@link #PROPERTY_NAME_TABLE_SCHEM} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_SCHEM} column is bound. The value is {@value}.
     */
    public static final String PROPERTY_NAME_TABLE_SCHEM = "tableSchem";

    /**
     * The label of the column to which {@link #PROPERTY_NAME_TABLE_NAME} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_NAME} column is bound. The value is {@value}.
     */
    public static final String PROPERTY_NAME_TABLE_NAME = "tableName";

    /**
     * The label of the column to which {@link #PROPERTY_NAME_TABLE_TYPE} attribute is bound. The value is {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    /**
     * The name of the attribute from which {@link #COLUMN_LABEL_TABLE_TYPE} column is bound. The value is {@value}.
     */
    public static final String PROPERTY_NAME_TABLE_TYPE = "tableName";

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Table)) return false;
        final Table that = (Table) obj;
        return Objects.equals(getTableId(), that.getTableId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTableId());
    }

    String getTableCatNonNull() {
        return Optional.ofNullable(getTableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
        tableId = null;
    }

    String getTableSchemNonNull() {
        return Optional.ofNullable(getTableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
        tableId = null;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
        tableId = null;
    }

    public TableId getTableId() {
        if (tableId == null) {
            tableId = TableId.of(
                    getTableCatNonNull(),
                    getTableSchemNonNull(),
                    getTableName()
            );
        }
        return tableId;
    }

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient TableId tableId;

    @NullableByVendor("MariaDB")
    @ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
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
