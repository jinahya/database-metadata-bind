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
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding a result of {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ChildOf_(Catalog.class)
@ParentOf(UDT.class)
@ParentOf(Table.class)
@ParentOf(Procedure.class)
@ParentOf(Function.class)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Schema
        implements MetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_VALUE_TABLE_SCHEM_EMPTY = "";

    public List<Table> getTables(final Context context, final String tableNamePattern, final String[] types)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getTables(getTableCatalog(), getTableSchem(), tableNamePattern, types);
    }

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;

    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;
}
