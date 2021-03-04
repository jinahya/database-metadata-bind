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
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class SupportsResultSetConcurrency {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    static List<SupportsResultSetConcurrency> list(final DatabaseMetaData m) throws SQLException {
        requireNonNull(m, "m is null");
        final List<SupportsResultSetConcurrency> list = new ArrayList<>();
        for (final ResultSetType type : ResultSetType.values()) {
            for (final ResultSetConcurrency concurrency : ResultSetConcurrency.values()) {
                final SupportsResultSetConcurrency v = new SupportsResultSetConcurrency();
                v.type = type.rawValue;
                v.typeName = type.name();
                v.concurrency = concurrency.rawValue;
                v.concurrencyName = concurrency.name();
                try {
                    v.value = m.supportsResultSetConcurrency(v.type, v.concurrency);
                } catch (final SQLFeatureNotSupportedException sqlfnse) {
                    Utils.logSqlFeatureNotSupportedException(logger, sqlfnse);
                }
                list.add(v);
            }
        }
        return list;
    }

    @XmlAttribute(required = true)
    public int type;

    @XmlAttribute(required = true)
    public String typeName;

    @XmlAttribute(required = true)
    public int concurrency;

    @XmlAttribute(required = true)
    public String concurrencyName;

    @XmlValue
    public Boolean value;
}
