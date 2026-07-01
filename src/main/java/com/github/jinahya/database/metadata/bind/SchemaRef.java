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
 * A record for carrying schema values.
 *
 * @param tableCatalog a catalog value.
 * @param tableSchem   a schema value.
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public record SchemaRef(@Nullable String tableCatalog, @Nullable String tableSchem)
        implements SchemaView {

    /**
     * Creates a new instance with values from specified view.
     *
     * @param view the view whose values are used.
     */
    public SchemaRef(final SchemaView view) {
        this(view.getTableCatalog(), view.getTableSchem());
    }

    @Override
    @Nullable
    public String getTableCatalog() {
        return tableCatalog;
    }

    @Override
    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }
}
