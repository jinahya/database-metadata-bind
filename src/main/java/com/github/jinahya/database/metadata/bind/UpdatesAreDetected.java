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

import jakarta.xml.bind.annotation.XmlRootElement;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#updatesAreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#updatesAreDetected(int)
 */
@XmlRootElement
public class UpdatesAreDetected
        extends AreDetected {

    private static final long serialVersionUID = -7538643762491010895L;

    /**
     * Invokes {@link Context#updatesAreDetected(int)} for all types defined in {@link java.sql.ResultSet} and adds all
     * bound values to specified collection.
     *
     * @param context    a context.
     * @param collection the collection to which bound values are added.
     * @param <C>        type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     */
    public static <C extends Collection<? super UpdatesAreDetected>> C getAllInstances(
            final Context context, final C collection)
            throws SQLException {
        requireNonNull(context, "context is null");
        for (final ResultSetType type : ResultSetType.values()) {
            collection.add(context.updatesAreDetected(type.rawValue()));
        }
        return collection;
    }

    /**
     * Creates a new instance.
     */
    public UpdatesAreDetected() {
        super();
    }
}
