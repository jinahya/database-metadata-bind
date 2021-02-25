package com.github.jinahya.database.metadata.bind;

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
