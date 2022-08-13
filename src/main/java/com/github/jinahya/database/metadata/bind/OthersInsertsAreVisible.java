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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.SQLException;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#othersInsertsAreVisible(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#othersInsertsAreVisible(int)
 */
@XmlRootElement
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class OthersInsertsAreVisible
        extends AreVisible {

    private static final long serialVersionUID = 1507862473680389478L;

    /**
     * Invokes {@link Context#othersInsertsAreVisible(int)} method for all types defined in {@link java.sql.ResultSet}
     * and adds bound values to specified collection.
     *
     * @param context    a context.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see ResultSetType
     */
    public static <C extends Collection<? super OthersInsertsAreVisible>> C getAllInstances(final Context context,
                                                                                            final C collection)
            throws SQLException {
        requireNonNull(context, "databaseMetaData is null");
        requireNonNull(collection, "collection is null");
        for (final ResultSetType type : ResultSetType.values()) {
            collection.add(context.othersInsertsAreVisible(type.rawValue()));
        }
        return collection;
    }
}
