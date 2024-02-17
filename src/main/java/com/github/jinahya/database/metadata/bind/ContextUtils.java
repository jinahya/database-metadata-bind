package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2024 Jinahya, Inc.
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

import java.sql.SQLException;
import java.util.Comparator;

public final class ContextUtils {

    static <T> Comparator<T> nulls(final Context context, final Comparator<? super T> comparator) throws SQLException {
        if (context.metadata.nullsAreSortedAtStart() || context.metadata.nullsAreSortedLow()) {
            return Comparator.nullsFirst(comparator);
        } else {
            return Comparator.nullsLast(comparator);
        }
    }

    private ContextUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
