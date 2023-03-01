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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)}
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTables(String, String, String)
 */
@_ChildOf(Schema.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class SuperTable extends AbstractMetadataType {

    private static final long serialVersionUID = 3579710773784268831L;

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_SUPERTABLE_NAME = "SUPERTABLE_NAME";

    // -------------------------------------------------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
        tableId = null;
        sueprtableId = null;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
        tableId = null;
        sueprtableId = null;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public void setTableName(final String tableName) {
        this.tableName = tableName;
        tableId = null;
    }

    // -------------------------------------------------------------------------------------------------- supertableName
    public void setSupertableName(final String supertableName) {
        this.supertableName = supertableName;
        sueprtableId = null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @ColumnLabel(COLUMN_LABEL_SUPERTABLE_NAME)
    private String supertableName;

    // -----------------------------------------------------------------------------------------------------------------
    String getTableCat(final String def) {
        if (tableCat == null) {
            return def;
        }
        return tableCat;
    }

    String getTableCatNonNull() {
        return getTableCat(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getTableSchemNonNull() {
        if (tableSchem == null) {
            return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
        }
        return tableSchem;
    }

    TableId getTableId() {
        if (tableId == null) {
            tableId = TableId.of(
                    getTableCatNonNull(),
                    getTableSchemNonNull(),
                    getTableName()
            );
        }
        return tableId;
    }

    TableId getSupertableId() {
        if (sueprtableId == null) {
            sueprtableId = TableId.of(
                    getTableCatNonNull(),
                    getTableSchemNonNull(),
                    getSupertableName()
            );
        }
        return sueprtableId;
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient TableId tableId;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient TableId sueprtableId;
}
