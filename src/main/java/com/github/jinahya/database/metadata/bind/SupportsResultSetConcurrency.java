package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#supportsResultSetConcurrency(int, int)
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class SupportsResultSetConcurrency
        implements MetadataType {

    private static final long serialVersionUID = -4192322973387966785L;

//    /**
//     * Invokes {@link Context#supportsResultSetConcurrency(int, int)} method for all combinations of types and
//     * concurrencies and adds bound values to specified collection.
//     *
//     * @param context    a context.
//     * @param collection the collection to which bound values are added.
//     * @param <C>        the type of {@code collection}
//     * @return given {@code collection}.
//     * @throws SQLException if a database access error occurs.
//     */
//    public static <C extends Collection<? super SupportsResultSetConcurrency>> C getAllInstances(final Context context,
//                                                                                                 final C collection)
//            throws SQLException {
//        Objects.requireNonNull(context, "context is null");
//        Objects.requireNonNull(collection, "collection is null");
//        for (final ResultSetType type : ResultSetType.values()) {
//            for (final ResultSetConcurrency concurrency : ResultSetConcurrency.values()) {
//                collection.add(context.supportsResultSetConcurrency(type.rawValue(), concurrency.rawValue()));
//            }
//        }
//        return collection;
//    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    private int type;

    private int concurrency;

    private Boolean value;
}
