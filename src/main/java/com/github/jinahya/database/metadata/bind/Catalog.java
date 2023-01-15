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
import java.util.function.Consumer;

/**
 * A class for binding results of {@link java.sql.DatabaseMetaData#getCatalogs()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getCatalogs(Consumer)
 */
@ParentOf(Schema.class)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Catalog
        implements MetadataType {

    private static final long serialVersionUID = 6239185259128825953L;

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    /**
     * A value for {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute for virtual instances. The value is {@value}.
     */
    public static final String COLUMN_VALUE_TABLE_CAT_EMPTY = "";

    public List<Schema> getSchemas(final Context context, final String schemaNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getSchemas(getTableCat(), schemaNamePattern);
    }

    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;
}
