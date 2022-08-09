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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#supportsResultSetConcurrency(int, int)
 */
@XmlRootElement
public class SupportsResultSetConcurrency {

    private static final long serialVersionUID = -4192322973387966785L;

    /**
     * Invokes {@link Context#supportsResultSetConcurrency(int, int)} method for all combinations of types and
     * concurrencies and adds bound values to specified collection.
     *
     * @param context    a context.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database access error occurs.
     */
    public static <C extends Collection<? super SupportsResultSetConcurrency>> C getAllInstances(final Context context,
                                                                                                 final C collection)
            throws SQLException {
        requireNonNull(context, "context is null");
        for (final ResultSetType type : ResultSetType.values()) {
            for (final ResultSetConcurrency concurrency : ResultSetConcurrency.values()) {
                collection.add(context.supportsResultSetConcurrency(type.rawValue(), concurrency.rawValue()));
            }
        }
        return collection;
    }

    /**
     * Creates a new instance.
     */
    public SupportsResultSetConcurrency() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() + '{'
               + "type=" + type
               + ",concurrency=" + concurrency
               + ",value=" + value
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SupportsResultSetConcurrency that = (SupportsResultSetConcurrency) obj;
        return type == that.type
               && concurrency == that.concurrency
               && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, concurrency, value);
    }

    // ------------------------------------------------------------------------------------------------------------ type
    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    // ----------------------------------------------------------------------------------------------------- concurrency
    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(final int concurrency) {
        this.concurrency = concurrency;
    }

    // ----------------------------------------------------------------------------------------------------------- value
    public Boolean getValue() {
        return value;
    }

    public void setValue(final Boolean value) {
        this.value = value;
    }

    @XmlAttribute(required = true)
    private int type;

    @XmlAttribute(required = true)
    private int concurrency;

    @XmlValue
    private Boolean value;
}
