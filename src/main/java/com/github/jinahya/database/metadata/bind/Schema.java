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
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Optional;

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
public class Schema extends AbstractMetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    /**
     * Returns a new instance whose {@code tableCatalog} is {@value Catalog#COLUMN_VALUE_TABLE_CAT_EMPTY} and whose
     * {@code tableSchem} is {@value #COLUMN_VALUE_TABLE_SCHEM_EMPTY}.
     *
     * @return a new virtual instance.
     */
    public static Schema newVirtualInstance() {
        return builder()
                .tableCatalog(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY)
                .tableSchem(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY)
                .build();
    }

    public static final Comparator<Schema> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Schema::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Schema> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Schema::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER);

    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_VALUE_TABLE_SCHEM_EMPTY = "";

    public SchemaId getSchemaId() {
        return SchemaId.of(
                getTableCatalogNonNull(),
                getTableSchem()
        );
    }

    public CatalogId getCatalogId() {
        return getSchemaId().getCatalogId();
    }

    String getTableCatalogNonNull() {
        return Optional.ofNullable(getTableCatalog())
                .orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;

    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @Accessors(fluent = true)
    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Catalog catalog;
}
