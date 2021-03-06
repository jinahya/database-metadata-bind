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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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

    static List<SupportsResultSetConcurrency> list(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsResultSetConcurrency> list = new ArrayList<>();
        for (final ResultSetType type : ResultSetType.values()) {
            for (final ResultSetConcurrency concurrency : ResultSetConcurrency.values()) {
                final SupportsResultSetConcurrency v = new SupportsResultSetConcurrency();
                v.type = type.getRawValue();
                v.typeName = type.name();
                v.concurrency = concurrency.getRawValue();
                v.concurrencyName = concurrency.name();
                try {
                    v.value = context.databaseMetaData.supportsResultSetConcurrency(v.type, v.concurrency);
                } catch (final SQLException sqle) {
                    logger.log(Level.WARNING, sqle,
                               () -> String.format("failed to invoke supportsResultSetConcurrency(%1$d, %2$d)",
                                                   v.type, v.concurrency));
                    if (!context.isSuppressed(sqle)) {
                        throw sqle;
                    }
                }
                list.add(v);
            }
        }
        return list;
    }

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

    public Boolean getValue() {
        return value;
    }

    public void setValue(final Boolean value) {
        this.value = value;
    }

    @XmlAttribute(required = true)
    private int type;

    @XmlAttribute(required = true)
    private String typeName;

    @XmlAttribute(required = true)
    private int concurrency;

    @XmlAttribute(required = true)
    private String concurrencyName;

    @XmlValue
    private Boolean value;
}
