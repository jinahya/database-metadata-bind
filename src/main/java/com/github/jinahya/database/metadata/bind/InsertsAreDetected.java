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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.DatabaseMetaData;

/**
 * A class for binding results of {@link DatabaseMetaData#insertsAreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DatabaseMetaData#insertsAreDetected(int)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InsertsAreDetected
        extends AreDetected {

    private static final long serialVersionUID = 8464348704439999572L;

//    /**
//     * Invokes {@link Context#insertsAreDetected(int)} method for all types defined in {@link java.sql.ResultSet} and
//     * adds bounds values to specified collection.
//     *
//     * @param context    a context.
//     * @param collection the collection to which bound values are added.
//     * @param <C>        the type of {@code collection}
//     * @return given {@code collection}.
//     * @throws SQLException if a database error occurs.
//     * @see Context#insertsAreDetected(int)
//     * @see ResultSetType
//     */
//    public static <C extends Collection<? super InsertsAreDetected>> C getAllInstances(final Context context,
//                                                                                       final C collection)
//            throws SQLException {
//        Objects.requireNonNull(context, "context is null");
//        Objects.requireNonNull(collection, "collection is null");
//        for (final ResultSetType type : ResultSetType.values()) {
//            collection.add(context.insertsAreDetected(type.rawValue()));
//        }
//        return collection;
//    }
}
