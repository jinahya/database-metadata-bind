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
