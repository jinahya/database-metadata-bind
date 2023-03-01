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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding a results of the {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSchemas(String, String)
 */
@_ParentOf(UDT.class)
@_ParentOf(TablePrivilege.class)
@_ParentOf(Table.class)
@_ParentOf(SuperType.class)
@_ParentOf(SuperTable.class)
@_ParentOf(Procedure.class)
@_ParentOf(Function.class)
@_ChildOf(Catalog.class)
public class Schema extends AbstractMetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    public static final Comparator<Schema> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Schema::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Schema> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Schema::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER);

    /**
     * Returns a new instance whose {@code tableCatalog} is {@value Catalog#COLUMN_VALUE_TABLE_CAT_EMPTY} and whose
     * {@code tableSchem} is {@value #COLUMN_VALUE_TABLE_SCHEM_EMPTY}.
     *
     * @return a new virtual instance.
     */
    public static Schema newVirtualInstance() {
        final Schema instance = new Schema();
        instance.setTableCatalog(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
        instance.setTableSchem(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
        return instance;
    }

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * A column value of {@value} for {@value #COLUMN_LABEL_TABLE_SCHEM}.
     */
    public static final String COLUMN_VALUE_TABLE_SCHEM_EMPTY = "";

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCatalog=" + tableCatalog +
               ",tableSchem=" + tableSchem +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Schema)) return false;
        final Schema that = (Schema) obj;
        return Objects.equals(tableCatalog, that.tableCatalog) &&
               Objects.equals(tableSchem, that.tableSchem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCatalog, tableSchem);
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(final String tableCatalog) {
        this.tableCatalog = tableCatalog;
        schemaId = null;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
        schemaId = null;
    }

    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;

    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    String getTableCatalogNonNull() {
        if (tableCatalog == null) {
            return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
        }
        return tableCatalog;
    }

    SchemaId getSchemaId() {
        if (schemaId == null) {
            schemaId = SchemaId.of(
                    getTableCatalogNonNull(),
                    getTableSchem()
            );
        }
        return schemaId;
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient SchemaId schemaId;
}
