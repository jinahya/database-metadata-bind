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
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#supportsResultSetConcurrency(int, int)
 */
@XmlRootElement
@Data
@NoArgsConstructor
@Slf4j
public class SupportsResultSetConcurrency implements MetadataType {

    private static final long serialVersionUID = -4192322973387966785L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link Context#supportsResultSetConcurrency(int, int)} method for all combinations of types and
     * concurrencies and returns bound values.
     *
     * @param context a context.
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     */
    public static List<SupportsResultSetConcurrency> getAllInstances(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsResultSetConcurrency> all = new ArrayList<>();
        for (final ResultSetType type : ResultSetType.values()) {
            for (final ResultSetConcurrency concurrency : ResultSetConcurrency.values()) {
                all.add(context.supportsResultSetConcurrency(type.getRawValue(), concurrency.getRawValue()));
            }
        }
        return all;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = true)
    private int type;

    @XmlAttribute(required = true)
    private int concurrency;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlValue
    private Boolean value;
}
