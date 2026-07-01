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
 * An interface for exposing schema values.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface SchemaView {

    /**
     * Returns a schema value.
     *
     * @return a schema value.
     */
    @Nullable
    String getTableSchem();

    /**
     * Returns an effective schema value.
     *
     * @return an effective schema value.
     */
    default String getEffectiveTableSchem() {
        final var tableSchem = getTableSchem();
        return tableSchem == null ? "" : tableSchem;
    }

    /**
     * Returns a catalog value.
     *
     * @return a catalog value.
     */
    @Nullable
    String getTableCatalog();

    /**
     * Returns an effective catalog value.
     *
     * @return an effective catalog value.
     */
    default String getEffectiveTableCatalog() {
        final var tableCatalog = getTableCatalog();
        return tableCatalog == null ? "" : tableCatalog;
    }
}
