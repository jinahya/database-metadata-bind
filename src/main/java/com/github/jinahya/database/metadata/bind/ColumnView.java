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
 * An interface for exposing column values.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface ColumnView
        extends TableView {

    /**
     * Returns a column name.
     *
     * @return a column name.
     */
    @Nullable
    String getColumnName();

    /**
     * Returns a REF scope catalog value.
     *
     * @return a REF scope catalog value.
     */
    @Nullable
    String getScopeCatalog();

    /**
     * Returns an effective REF scope catalog value.
     *
     * @return an effective REF scope catalog value.
     */
    default String getEffectiveScopeCatalog() {
        final var scopeCatalog = getScopeCatalog();
        return scopeCatalog == null ? "" : scopeCatalog;
    }

    /**
     * Returns a REF scope schema value.
     *
     * @return a REF scope schema value.
     */
    @Nullable
    String getScopeSchema();

    /**
     * Returns an effective REF scope schema value.
     *
     * @return an effective REF scope schema value.
     */
    default String getEffectiveScopeSchema() {
        final var scopeSchema = getScopeSchema();
        return scopeSchema == null ? "" : scopeSchema;
    }

    /**
     * Returns a REF scope table value.
     *
     * @return a REF scope table value.
     */
    @Nullable
    String getScopeTable();
}
