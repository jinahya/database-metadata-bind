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
 * A class for binding result of {@link DatabaseMetaData#supportsResultSetType(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class SupportsTransactionIsolationLevel implements MetadataType {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link Context#supportsTransactionIsolationLevel(int)} method for all transaction isolation levels
     * defined in {@link java.sql.ResultSet} and returns bound values.
     *
     * @param context a context.
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     */
    public static @NotEmpty List<@Valid @NotNull SupportsTransactionIsolationLevel> getAllInstances(
            final @NotNull Context context)
            throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsTransactionIsolationLevel> all = new ArrayList<>();
        for (final ConnectionTransactionIsolationLevel level : ConnectionTransactionIsolationLevel.values()) {
            all.add(context.supportsTransactionIsolationLevel(level));
        }
        return all;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public SupportsTransactionIsolationLevel() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return super.toString() + '{'
               + "level=" + level
               + ",levelName='" + levelName
               + ",value=" + value
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SupportsTransactionIsolationLevel that = (SupportsTransactionIsolationLevel) obj;
        return level == that.level
               && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level,
                            value);
    }

    // ----------------------------------------------------------------------------------------------------------- level
    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(final String levelName) {
        this.levelName = levelName;
    }

    // ----------------------------------------------------------------------------------------------------------- value
    public Boolean getValue() {
        return value;
    }

    public void setValue(final Boolean value) {
        this.value = value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = true)
    private int level;

    @XmlAttribute
    private String levelName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlValue
    private Boolean value;
}
