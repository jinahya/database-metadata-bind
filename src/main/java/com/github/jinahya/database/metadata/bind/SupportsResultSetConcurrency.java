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

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#supportsResultSetConcurrency(int, int)
 */
@XmlRootElement
public class SupportsResultSetConcurrency implements MetadataType {

    private static final long serialVersionUID = -4192322973387966785L;

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link Context#supportsResultSetConcurrency(int, int)} method for all combinations of types and
     * concurrencies and returns bound values.
     *
     * @param context a context.
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     */
    public static @NotEmpty List<@Valid @NotNull SupportsResultSetConcurrency> getAllInstances(
            final @NotNull Context context)
            throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsResultSetConcurrency> all = new ArrayList<>();
        for (final ResultSetType type : ResultSetType.values()) {
            for (final ResultSetConcurrency concurrency : ResultSetConcurrency.values()) {
                all.add(context.supportsResultSetConcurrency(type, concurrency));
            }
        }
        return all;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public SupportsResultSetConcurrency() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return super.toString() + '{'
               + "type=" + type
               + ",typeName=" + typeName
               + ",concurrency=" + concurrency
               + ",concurrencyName=" + concurrencyName
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
        return Objects.hash(type,
                            concurrency,
                            value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(final int concurrency) {
        this.concurrency = concurrency;
    }

    public String getConcurrencyName() {
        return concurrencyName;
    }

    public void setConcurrencyName(final String concurrencyName) {
        this.concurrencyName = concurrencyName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Boolean getValue() {
        return value;
    }

    public void setValue(final Boolean value) {
        this.value = value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = true)
    private int type;

    @XmlAttribute
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = true)
    private int concurrency;

    @XmlAttribute
    private String concurrencyName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlValue
    private Boolean value;
}
