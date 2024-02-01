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

import java.util.Objects;

/**
 * A entity class for binding results of the
 * {@link java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)}
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTables(String, String, String)
 */
public class SuperTable extends AbstractMetadataType {

    private static final long serialVersionUID = 3579710773784268831L;

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_SUPERTABLE_NAME = "SUPERTABLE_NAME";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem" + tableSchem +
               ",tableName=" + tableName +
               ",supertableName='" + supertableName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SuperTable)) return false;
        SuperTable that = (SuperTable) obj;
        return Objects.equals(tableCatNonNull(), that.tableCatNonNull()) &&
               Objects.equals(tableSchemNonNull(), that.tableSchemNonNull()) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(supertableName, that.supertableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                tableCatNonNull(),
                tableSchemNonNull(),
                tableName,
                supertableName
        );
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public String getSupertableName() {
        return supertableName;
    }

    public void setSupertableName(final String supertableName) {
        this.supertableName = supertableName;
    }

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_SUPERTABLE_NAME)
    private String supertableName;

    String tableCatNonNull() {
        final String tableCat_ = getTableCat();
        if (tableCat_ != null) {
            return tableCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String tableSchemNonNull() {
        final String tableSchem_ = getTableSchem();
        if (tableSchem_ != null) {
            return tableSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }
}
