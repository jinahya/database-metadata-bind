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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding results of {@link DatabaseMetaData#supportsResultSetHoldability(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#supportsResultSetHoldability(int)
 */
@XmlRootElement
public class SupportsResultSetHoldability
        implements MetadataType {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link Context#supportsResultSetHoldability(int)} method for all holdabilities and adds bound values to
     * specified collection.
     *
     * @param context    a context.
     * @param collection the collection to which bound values are added.
     * @param <C>        the type of {@code collection}
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public static <C extends Collection<? super SupportsResultSetHoldability>> C getAllInstances(final Context context,
                                                                                                 final C collection)
            throws SQLException {
        requireNonNull(context, "context is null");
        for (final ResultSetHoldability value : ResultSetHoldability.values()) {
            collection.add(context.supportsResultSetHoldability(value.getRawValue()));
        }
        return collection;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public SupportsResultSetHoldability() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return super.toString() + '{'
               + "type=" + holdability
               + ",value=" + value
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SupportsResultSetHoldability that = (SupportsResultSetHoldability) obj;
        return holdability == that.holdability
               && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holdability, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getHoldability() {
        return holdability;
    }

    public void setHoldability(final int holdability) {
        this.holdability = holdability;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(final Boolean value) {
        this.value = value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = true)
    private int holdability;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlValue
    private Boolean value;
}
