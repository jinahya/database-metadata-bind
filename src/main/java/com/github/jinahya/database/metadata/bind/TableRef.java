package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import org.jspecify.annotations.Nullable;

/**
 * A record for carrying table values.
 *
 * @param tableCat   a catalog value.
 * @param tableSchem a schema value.
 * @param tableName  a table name.
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public record TableRef(@Nullable String tableCat, @Nullable String tableSchem, @Nullable String tableName)
        implements TableView {

    /**
     * Creates a new instance with values from specified view.
     *
     * @param view the view whose values are used.
     */
    public TableRef(final TableView view) {
        this(view.getTableCat(), view.getTableSchem(), view.getTableName());
    }

    @Override
    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    @Override
    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    @Override
    @Nullable
    public String getTableName() {
        return tableName;
    }
}
