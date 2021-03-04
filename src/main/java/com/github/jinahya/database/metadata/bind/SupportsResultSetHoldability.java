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
import java.lang.invoke.MethodHandles;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#supportsResultSetHoldability(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class SupportsResultSetHoldability {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    static List<SupportsResultSetHoldability> list(final DatabaseMetaData m) throws SQLException {
        requireNonNull(m, "m is null");
        final List<SupportsResultSetHoldability> list = new ArrayList<>();
        for (final ResultSetHoldability holdability : ResultSetHoldability.values()) {
            final SupportsResultSetHoldability v = new SupportsResultSetHoldability();
            v.holdability = holdability.rawValue;
            v.holdabilityName = holdability.name();
            try {
                v.value = m.supportsResultSetHoldability(v.holdability);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
            }
            list.add(v);
        }
        return list;
    }

    @XmlAttribute(required = true)
    public int holdability;

    @XmlAttribute(required = true)
    public String holdabilityName;

    @XmlValue
    public Boolean value;
}
