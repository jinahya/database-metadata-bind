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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#supportsResultSetType(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class SupportsTransactionIsolationLevel {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    static List<SupportsTransactionIsolationLevel> list(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final List<SupportsTransactionIsolationLevel> list = new ArrayList<>();
        {
            final SupportsTransactionIsolationLevel v = new SupportsTransactionIsolationLevel();
            v.level = Connection.TRANSACTION_NONE;
            v.levelName = "TRANSACTION_NONE";
            list.add(v);
        }
        {
            final SupportsTransactionIsolationLevel v = new SupportsTransactionIsolationLevel();
            v.level = Connection.TRANSACTION_READ_UNCOMMITTED;
            v.levelName = "TRANSACTION_READ_UNCOMMITTED";
            list.add(v);
        }
        {
            final SupportsTransactionIsolationLevel v = new SupportsTransactionIsolationLevel();
            v.level = Connection.TRANSACTION_READ_COMMITTED;
            v.levelName = "TRANSACTION_READ_COMMITTED";
            list.add(v);
        }
        {
            final SupportsTransactionIsolationLevel v = new SupportsTransactionIsolationLevel();
            v.level = Connection.TRANSACTION_REPEATABLE_READ;
            v.levelName = "TRANSACTION_REPEATABLE_READ";
            list.add(v);
        }
        {
            final SupportsTransactionIsolationLevel v = new SupportsTransactionIsolationLevel();
            v.level = Connection.TRANSACTION_SERIALIZABLE;
            v.levelName = "TRANSACTION_SERIALIZABLE";
            list.add(v);
        }
        for (final SupportsTransactionIsolationLevel v : list) {
            try {
                v.value = context.databaseMetaData.supportsTransactionIsolationLevel(v.level);
            } catch (final SQLException sqle) {
                logger.log(Level.WARNING, sqle, () -> String.format("failed to invoke supportsTransactionIsolationLevel(%1$d)", v.level));
                context.throwIfNotSuppressed(sqle);
            }
        }
        return list;
    }

    @XmlAttribute(required = true)
    public int level;

    @XmlAttribute(required = true)
    public String levelName;

    @XmlValue
    public Boolean value;
}
