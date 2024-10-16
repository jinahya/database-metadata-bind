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

/**
 * A class for binding results of the {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSchemas(String, String)
 */

@_ChildOf(Catalog.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Schema
        extends AbstractMetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Schema> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(Schema::getTableCatalog, ContextUtils.nulls(context, comparator))
                .thenComparing(Schema::getTableSchem, ContextUtils.nulls(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------
    static Schema of(final String tableCatalog, final String tableSchem) {
        final Schema instance = new Schema();
        instance.setTableCatalog(tableCatalog);
        instance.setTableSchem(tableSchem);
        return instance;
    }

    // ---------------------------------------------------------------------------------------------------- tableCatalog

    // ------------------------------------------------------------------------------------------------------ tableSchem

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CATALOG)
    @EqualsAndHashCode.Include
    private String tableCatalog;

    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String tableSchem;
}
