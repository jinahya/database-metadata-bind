package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

abstract class AreVisible {

    static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    static <T extends AreVisible> List<T> list(final Class<T> clazz) {
        requireNonNull(clazz, "clazz is null");
        return Arrays.stream(ResultSetType.values())
                .map(t -> {
                    final T v;
                    try {
                        v = clazz.getDeclaredConstructor().newInstance();
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    }
                    v.type = t.rawValue;
                    v.name = t.name();
                    return v;
                })
                .collect(Collectors.toList());
    }

    @XmlAttribute(required = true)
    public int type;

    @XmlAttribute(required = true)
    public String name;

    @XmlValue
    public Boolean value;
}
