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

import lombok.extern.slf4j.Slf4j;
import org.atteo.evo.inflector.English;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.beans.Introspector;
import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Test utilities for JAXB functionalities.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class JaxbTests {

    // -----------------------------------------------------------------------------------------------------------------
    static String plural(final Class<?> type) {
        String singular = type.getSimpleName();
        singular = Introspector.decapitalize(singular);
        return English.plural(singular);
    }

    static <T> void store(final Class<T> type, final Collection<? extends T> elements, final String name)
            throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(Wrapper.class, type);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final Wrapper<T> wrapper = Wrapper.of(elements);
        final File output = Paths.get("target", name + ".xml").toFile();
        marshaller.marshal(new JAXBElement<>(Wrapper.NAME, Wrapper.class, wrapper), output);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JaxbTests() {
        super();
    }
}
