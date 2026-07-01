package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

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

/**
 * An interface for exposing a catalog value.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface CatalogView {

    /**
     * Returns a catalog value.
     *
     * @return a catalog value.
     */
    @Nullable
    String getTableCat();

    /**
     * Returns an effective catalog value.
     *
     * @return an effective catalog value.
     */
    default String getEffectiveTableCat() {
        final var tableCat = getTableCat();
        return tableCat == null ? "" : tableCat;
    }
}
