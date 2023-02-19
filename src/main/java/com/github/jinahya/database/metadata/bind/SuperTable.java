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

import java.util.Optional;

/**
 * A entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)}
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTables(String, String, String)
 */
@ChildOf(Schema.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class SuperTable extends AbstractMetadataType {

    private static final long serialVersionUID = 3579710773784268831L;

    public static final String COLUMN_NAME_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_NAME_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_NAME_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_NAME_SUPERTABLE_NAME = "SUPERTABLE_NAME";

    public TableId getTableId() {
        return TableId.of(
                getTableCatNonNull(), getTableSchemNonNull(),
                getTableName()
        );
    }

    public TableId getSupertableId() {
        return TableId.of(getTableCatNonNull(), getTableSchemNonNull(), getSupertableName());
    }

    String getTableCatNonNull() {
        return Optional.ofNullable(getTableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getTableSchemNonNull() {
        return Optional.ofNullable(getTableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    @NullableBySpecification
    @ColumnLabel(COLUMN_NAME_TABLE_CAT)
    private String tableCat;

    @NullableBySpecification
    @ColumnLabel(COLUMN_NAME_TABLE_SCHEM)
    private String tableSchem;

    @ColumnLabel(COLUMN_NAME_TABLE_NAME)
    private String tableName;

    @ColumnLabel(COLUMN_NAME_SUPERTABLE_NAME)
    private String supertableName;
}
