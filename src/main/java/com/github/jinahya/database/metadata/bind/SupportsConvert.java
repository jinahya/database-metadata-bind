package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.lang.invoke.MethodHandles;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#supportsConvert(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class SupportsConvert {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    static List<SupportsConvert> list(final DatabaseMetaData m) throws SQLException {
        requireNonNull(m, "m is null");
        final List<SupportsConvert> list = new ArrayList<>();
        final List<JDBCType> types = Arrays.stream(JDBCType.values())
                .filter(v -> "java.sql".equals(v.getVendor()))
                .collect(Collectors.toList());
        for (final JDBCType fromType : types) {
            for (final JDBCType toType : types) {
                final SupportsConvert v = new SupportsConvert();
                v.fromType = fromType.getVendorTypeNumber();
                v.fromTypeName = fromType.getName();
                v.toType = toType.getVendorTypeNumber();
                v.toTypeName = toType.getName();
                try {
                    v.value = m.supportsConvert(v.fromType, v.toType);
                } catch (final SQLFeatureNotSupportedException sqlfnse) {
                    Utils.logSqlFeatureNotSupportedException(logger, sqlfnse);
                }
                list.add(v);
            }
        }
        return list;
    }

    @XmlAttribute(required = true)
    public int fromType;

    @XmlAttribute(required = true)
    public String fromTypeName;

    @XmlAttribute(required = true)
    public int toType;

    @XmlAttribute(required = true)
    public String toTypeName;

    @XmlValue
    public Boolean value;
}
