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

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#ownUpdatesAreVisible(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#ownUpdatesAreVisible(int)
 */
@XmlRootElement
public class OwnUpdatesAreVisible
        extends AreVisible {

    private static final long serialVersionUID = -1214124846951421149L;

    /**
     * Invokes {@link Context#ownUpdatesAreVisible(int)} method for all types defined in {@link java.sql.ResultSet} and
     * adds bound values to specified collection.
     *
     * @param context    a context.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see Context#ownUpdatesAreVisible(int)
     * @see ResultSetType
     */
    public static <C extends Collection<? super OwnUpdatesAreVisible>> C getAllInstances(final Context context,
                                                                                         final C collection)
            throws SQLException {
        requireNonNull(context, "context is null");
        requireNonNull(collection, "collection is null");
        for (final ResultSetType type : ResultSetType.values()) {
            collection.add(context.ownUpdatesAreVisible(type.getRawValue()));
        }
        return collection;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public OwnUpdatesAreVisible() {
        super();
    }
}
