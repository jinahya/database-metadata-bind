package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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

    static List<SupportsTransactionIsolationLevel> list(final DatabaseMetaData m) throws SQLException {
        requireNonNull(m, "m is null");
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
                v.value = m.supportsTransactionIsolationLevel(v.level);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
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
