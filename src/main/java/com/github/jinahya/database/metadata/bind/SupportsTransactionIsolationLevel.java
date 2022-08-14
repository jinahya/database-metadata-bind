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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#supportsTransactionIsolationLevel(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#supportsTransactionIsolationLevel(int)
 */
@XmlRootElement
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class SupportsTransactionIsolationLevel
        implements MetadataType {

    private static final long serialVersionUID = -2241618224708597472L;

    /**
     * Invokes {@link Context#supportsTransactionIsolationLevel(int)} method for all transaction isolation levels
     * defined in {@link java.sql.Connection} and adds bound values to specified collection.
     *
     * @param context    a context.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     * @see ConnectionTransactionIsolationLevel
     */
    public static <C extends Collection<? super SupportsTransactionIsolationLevel>> C getAllInstances(
            final Context context, final C collection)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(collection, "collection is null");
        for (final ConnectionTransactionIsolationLevel value : ConnectionTransactionIsolationLevel.values()) {
            collection.add(context.supportsTransactionIsolationLevel(value.rawValueAsInt()));
        }
        return collection;
    }

    @Override
    public void retrieveChildren(Context context) throws SQLException {
        // no children
    }

    @XmlAttribute(required = false)
    public ConnectionTransactionIsolationLevel getLevelAsEnum() {
        return ConnectionTransactionIsolationLevel.valueOfRawValue(getLevel());
    }

    @XmlAttribute(required = true)
    private int level;

    @XmlValue
    private Boolean value;
}
