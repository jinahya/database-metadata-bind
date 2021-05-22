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
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#supportsResultSetHoldability(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class SupportsResultSetHoldability implements MetadataType {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link Context#supportsResultSetHoldability(int)} method for all holdabilities and returns bound values.
     *
     * @param context a context.
     * @return a list of bound values.
     * @throws SQLException if a database error occurs.
     */
    public static List<SupportsResultSetHoldability> getAllInstances(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsResultSetHoldability> all = new ArrayList<>();
        for (final ResultSetHoldability value : ResultSetHoldability.values()) {
            all.add(context.supportsResultSetHoldability(value.getRawValue()));
        }
        return all;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = true)
    private int holdability;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlValue
    private Boolean value;
}
