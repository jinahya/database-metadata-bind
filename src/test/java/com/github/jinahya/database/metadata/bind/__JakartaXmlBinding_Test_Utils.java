package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2024 Jinahya, Inc.
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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.namespace.QName;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

final class __JakartaXmlBinding_Test_Utils {

    static JAXBContext newContext() throws JAXBException {
        return JAXBContext.newInstance(AbstractMetadataType.class);
    }

    static JAXBContext newContext(final Class<?>... classesToBeBound) throws JAXBException {
        final var classes = Arrays.copyOf(classesToBeBound, classesToBeBound.length + 1);
        classes[classesToBeBound.length] = AbstractMetadataType.class;
        return JAXBContext.newInstance(classes);
    }

    static Marshaller createMarshaller() throws JAXBException {
        return newContext().createMarshaller();
    }

    static Marshaller createMarshaller(final Class<?>... classesToBeBound) throws JAXBException {
        return newContext(classesToBeBound).createMarshaller();
    }

    static Unmarshaller createUnmarshaller() throws JAXBException {
        return newContext().createUnmarshaller();
    }

    static void write(final String rootElementName, final String itemElementName, final List<?> values,
                      final Path path) {
        try {
            final var parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            final var classes = values.stream()
                    .map(Object::getClass)
                    .distinct()
                    .toArray(Class<?>[]::new);
            final var marshaller = createMarshaller(classes);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            try (var writer = Files.newBufferedWriter(path)) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<");
                writer.write(rootElementName);
                writer.write(" xmlns=\"");
                writer.write(JakartaXmlBindingConstants.NAMESPACE_URI);
                writer.write("\"");
                writer.write(">\n");
                for (final var value : values) {
                    @SuppressWarnings({"rawtypes", "unchecked"})
                    final var element = new JAXBElement(
                            new QName(JakartaXmlBindingConstants.NAMESPACE_URI, itemElementName,
                                      JakartaXmlBindingConstants.NAMESPACE_PREFIX),
                            value.getClass(),
                            value
                    );
                    marshaller.marshal(element, writer);
                    writer.write('\n');
                }
                writer.write("</");
                writer.write(rootElementName);
                writer.write(">\n");
            }
        } catch (final Exception e) {
            throw new RuntimeException("failed to write xml to " + path, e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private __JakartaXmlBinding_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
