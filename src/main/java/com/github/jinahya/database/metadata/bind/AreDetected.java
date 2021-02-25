package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.lang.invoke.MethodHandles;
import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#deletesAreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
abstract class AreDetected {

    protected static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    static <T extends AreDetected> List<T> list(final Class<T> type) {
        requireNonNull(type, "type is null");
        return Arrays.stream(ResultSetType.values())
                .map(c -> {
                    final T v;
                    try {
                        v = type.getConstructor().newInstance();
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    }
                    v.type = c.rawValue;
                    v.typeName = c.name();
                    return v;
                })
                .collect(Collectors.toList());
    }

    @XmlAttribute(required = true)
    public int type;

    @XmlAttribute(required = true)
    public String typeName;

    @XmlValue
    public Boolean value;
}
