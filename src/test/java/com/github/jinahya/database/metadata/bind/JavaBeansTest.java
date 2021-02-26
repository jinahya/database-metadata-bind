package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.github.jinahya.database.metadata.bind.Utils.fields;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Test java beans conformance.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
class JavaBeansTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    private static void nillable(@NonNull final Class<?> klass) {
        for (Entry<Field, Bind> entry : fields(klass, Bind.class).entrySet()) {
            final Field field = entry.getKey();
            final Bind bind = entry.getValue();
            if (!bind.nillable()) {
                continue;
            }
            assertThat(field.getType().isPrimitive()).isFalse();
        }
    }

    private static void unused(@NonNull final Class<?> klass) {
        for (Entry<Field, Bind> entry : fields(klass, Bind.class).entrySet()) {
            final Field field = entry.getKey();
            final Bind bind = entry.getValue();
            if (!bind.unused()) {
                continue;
            }
        }
    }

    private static void reserved(@NonNull final Class<?> klass) {
        for (Entry<Field, Bind> entry : fields(klass, Bind.class).entrySet()) {
            final Field field = entry.getKey();
            final Bind bind = entry.getValue();
            if (!bind.reserved()) {
                continue;
            }
        }
    }

    private static void accessor(@NonNull final Class<?> klass) throws IntrospectionException {
        final BeanInfo info = Introspector.getBeanInfo(klass);
        final Map<String, PropertyDescriptor> descriptors
                = Arrays.stream(info.getPropertyDescriptors()).collect(toMap(PropertyDescriptor::getName, identity()));
        for (final Field field : klass.getDeclaredFields()) {
            if (field.getAnnotation(Bind.class) == null) {
                continue;
            }
            final PropertyDescriptor descriptor = descriptors.get(field.getName());
            assertThat(descriptor).isNotNull();
            assertThat(descriptor.getReadMethod()).isNotNull();
            if (field.getType().equals(List.class)) {
                continue;
            }
            assertThat(descriptor.getWriteMethod()).isNotNull();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void test() throws URISyntaxException, IOException {
        final Package p = getClass().getPackage();
        //final String name = "/" + p.getName().replace('.', '/') + "/jaxb.index";
        final String name = "jaxb.index";
        final URL resource = getClass().getResource(name);
        final Path path = Paths.get(resource.toURI());
        Files.lines(path)
                .filter(l -> !l.startsWith("#"))
                .map(l -> {
                    try {
                        return Class.forName(p.getName() + "." + l);
                    } catch (final ClassNotFoundException cnfe) {
                        throw new RuntimeException(cnfe);
                    }
                })
                .forEach(c -> {
                    try {
                        nillable(c);
                        unused(c);
                        reserved(c);
                        accessor(c);
                    } catch (final IntrospectionException ie) {
                        throw new RuntimeException(ie);
                    }
                });
    }
}
