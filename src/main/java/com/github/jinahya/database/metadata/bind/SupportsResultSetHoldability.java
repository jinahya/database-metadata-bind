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
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#supportsResultSetHoldability(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class SupportsResultSetHoldability implements MetadataType {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link Context#supportsResultSetHoldability(int)} method for all holdabilities and returns bound values.
     *
     * @param context a context.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public static @NotEmpty List<@Valid @NotNull SupportsResultSetHoldability> getAllInstances(
            final @NotNull Context context)
            throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsResultSetHoldability> all = new ArrayList<>();
        for (final ResultSetHoldability holdability : ResultSetHoldability.values()) {
            all.add(context.supportsResultSetHoldability(holdability));
        }
        return all;
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
               + "holdability=" + holdability
               + ",holdabilityName='" + holdabilityName
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
        return Objects.hash(holdability,
                            value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getHoldability() {
        return holdability;
    }

    public void setHoldability(final int holdability) {
        this.holdability = holdability;
    }

    public String getHoldabilityName() {
        return holdabilityName;
    }

    public void setHoldabilityName(final String holdabilityName) {
        this.holdabilityName = holdabilityName;
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
    private int holdability;

    @XmlAttribute
    private String holdabilityName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlValue
    private Boolean value;
}
