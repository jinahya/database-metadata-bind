/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import static com.github.jinahya.database.metadata.bind.Utils.fields;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import static java.lang.String.format;
import static java.lang.invoke.MethodHandles.lookup;
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
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

/**
 * Test java beans conformance.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class JavaBeansTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    private static void nillable(final Class<?> klass)
            throws ReflectiveOperationException {
        for (Entry<Field, Bind> entry : fields(klass, Bind.class).entrySet()) {
            final Field field = entry.getKey();
            final Bind bind = entry.getValue();
            if (!bind.nillable()) {
                continue;
            }
            assertFalse(field.getType().isPrimitive(),
                        "@Bind(nillable); " + field);
        }
    }

    private static void unused(final Class<?> klass)
            throws ReflectiveOperationException {
        for (Entry<Field, Bind> entry : fields(klass, Bind.class).entrySet()) {
            final Field field = entry.getKey();
            final Bind bind = entry.getValue();
            if (!bind.unused()) {
                continue;
            }
        }
    }

    private static void reserved(final Class<?> klass)
            throws ReflectiveOperationException {
        for (Entry<Field, Bind> entry : fields(klass, Bind.class).entrySet()) {
            final Field field = entry.getKey();
            final Bind bind = entry.getValue();
            if (!bind.reserved()) {
                continue;
            }
        }
    }

    private static void accessor(final Class<?> klass)
            throws IntrospectionException, ReflectiveOperationException {
        final BeanInfo info = Introspector.getBeanInfo(klass);
        final Map<String, PropertyDescriptor> descriptors
                = Arrays.stream(info.getPropertyDescriptors()).collect(
                        toMap(PropertyDescriptor::getName, identity()));
        for (final Field field : klass.getDeclaredFields()) {
            if (field.getAnnotation(Bind.class) == null) {
                continue;
            }
            final PropertyDescriptor descriptor
                    = descriptors.get(field.getName());
            assertNotNull(descriptor, format("no descriptor: %s", field));
            assertNotNull(descriptor.getReadMethod(),
                          "no read method; " + field);
            if (field.getType().equals(List.class)) {
                continue;
            }
            assertNotNull(descriptor.getWriteMethod(),
                          "no write method; " + field);
        }
    }

    // -------------------------------------------------------------------------
    @Test
    public void test()
            throws URISyntaxException, IOException,
                   ReflectiveOperationException {
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
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    } catch (final IntrospectionException ie) {
                        throw new RuntimeException(ie);
                    }
                });
    }
}
