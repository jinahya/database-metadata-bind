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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Where an object lives on a connection: the catalog value and the schema value a driver reported for it, exactly as
 * reported; either may be {@code null}.
 * <p>
 * A {@code null} component means the row was reported without that level - not "any". To point a later
 * {@link DatabaseMetaData} call at the same place, pass {@link #catalogForLookup()} as its {@code catalog} argument,
 * and {@link #schemaForLookup()} or {@link #schemaPatternForLookup(Context)} as its {@code schema} or
 * {@code schemaPattern} argument. Those map a {@code null} component to {@code ""}, which selects objects
 * <em>without</em> that level, whereas passing {@code null} would not narrow by it at all.
 *
 * @param catalog the catalog value; may be {@code null}.
 * @param schema  the schema value; may be {@code null}.
 * @see ContextNavigationUtils
 */
public record Location(@Nullable String catalog, @Nullable String schema) {

    /**
     * Returns the value to pass as the {@code catalog} argument of a {@link DatabaseMetaData} method to select this
     * catalog. That argument is never a pattern, so no escaping is involved.
     *
     * @return the {@link #catalog() catalog}, or {@code ""} if it is {@code null}.
     */
    public String catalogForLookup() {
        return catalog == null ? "" : catalog;
    }

    /**
     * Returns the value to pass as an exact-match {@code schema} argument - as taken by
     * {@link DatabaseMetaData#getPrimaryKeys(String, String, String)}, for example - to select this schema.
     *
     * @return the {@link #schema() schema}, or {@code ""} if it is {@code null}.
     * @see #schemaPatternForLookup(Context)
     */
    public String schemaForLookup() {
        return schema == null ? "" : schema;
    }

    /**
     * Returns the value to pass as a {@code schemaPattern} argument - as taken by
     * {@link DatabaseMetaData#getTables(String, String, String, String[])}, for example - to select this schema and no
     * other.
     * <p>
     * The schema value is escaped with the specified context's
     * {@linkplain DatabaseMetaData#getSearchStringEscape() search string escape}, so that a {@code _} or {@code %} in
     * it matches only itself. If the driver reports no escape string, the value is returned unescaped, and a schema
     * name containing either character may then also match other schemas.
     *
     * @param context the context whose driver will receive the pattern.
     * @return an escaped pattern matching the {@link #schema() schema}, or {@code ""} if it is {@code null}.
     * @throws SQLException if a database error occurs while reading the search string escape.
     * @see #schemaForLookup()
     */
    public String schemaPatternForLookup(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        if (schema == null) {
            return "";
        }
        return escape(schema, context.metadata.getSearchStringEscape());
    }

    /**
     * Escapes the pattern metacharacters, and the escape string itself, in the specified value.
     *
     * @param value  the value to escape.
     * @param escape the search string escape; may be {@code null} or empty, in which case nothing is escaped.
     * @return the escaped value.
     */
    static String escape(final String value, @Nullable final String escape) {
        if (escape == null || escape.isEmpty()) {
            return value;
        }
        return value.replace(escape, escape + escape)
                .replace("_", escape + "_")
                .replace("%", escape + "%");
    }
}
