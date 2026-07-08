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
import jakarta.xml.bind.annotation.XmlAnyElement;

import javax.xml.namespace.QName;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class __JakartaXmlBinding_Test_Utils {

    static JAXBContext newContext(final Class<?>... classesToBeBound) throws JAXBException {
        final var classes = Arrays.copyOf(classesToBeBound, classesToBeBound.length + 3);
        classes[classesToBeBound.length] = AbstractMetadataType.class;
        classes[classesToBeBound.length + 1] = MetadataTypeWrapper.class;
        classes[classesToBeBound.length + 2] = StringValues.class;
        return JAXBContext.newInstance(classes);
    }

    static Marshaller createMarshaller(final Class<?>... classesToBeBound) throws JAXBException {
        return newContext(classesToBeBound).createMarshaller();
    }

    static JAXBContext newContext() throws JAXBException {
        return JAXBContext.newInstance(__JakartaXmlBinding_Test_Utils.class.getPackageName());
    }

    static Marshaller createMarshaller() throws JAXBException {
        return newContext().createMarshaller();
    }

    static Unmarshaller createUnmarshaller() throws JAXBException {
        return newContext().createUnmarshaller();
    }

    static <T extends MetadataType> void marshal(final List<T> values, final Path path) {
        try {
            final var marshaller = createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(MetadataTypeWrapper.of(values), path.toFile());
        } catch (final Exception e) {
            throw new RuntimeException("failed to marshal xml to " + path, e);
        }
    }

    static <T extends MetadataType> List<T> unmarshal(final Path path) {
        try {
            final var unmarshaller = createUnmarshaller();
            final var object = unmarshaller.unmarshal(path.toFile());
            if (!(object instanceof MetadataTypeWrapper<?> wrapper)) {
                throw new IllegalStateException("unexpected unmarshalled object: " + object);
            }
            @SuppressWarnings("unchecked")
            final var elements = (List<T>) wrapper.getElements();
            return elements;
        } catch (final Exception e) {
            throw new RuntimeException("failed to unmarshal xml from " + path, e);
        }
    }

    static void writeStrings(final String rootElementName, final String itemElementName,
                             final List<String> values, final Path path) {
        try {
            final var parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            final var marshaller = createMarshaller(String.class);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            final var content = StringValues.of(values.stream()
                                                        .map(v -> element(itemElementName, v))
                                                        .toList());
            @SuppressWarnings({"rawtypes", "unchecked"})
            final var root = new JAXBElement(name(rootElementName), content.getClass(), content);
            marshaller.marshal(root, path.toFile());
        } catch (final Exception e) {
            throw new RuntimeException("failed to write xml to " + path, e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object element(final String itemElementName, final Object value) {
        return new JAXBElement(name(itemElementName), value.getClass(), value);
    }

    private static QName name(final String localPart) {
        return new QName(
                JakartaXmlBindingConstants.NAMESPACE_URI,
                localPart,
                JakartaXmlBindingConstants.NAMESPACE_PREFIX
        );
    }

    private static final class StringValues {

        static StringValues of(final List<?> elements) {
            final var instance = new StringValues();
            instance.getElements().addAll(elements);
            return instance;
        }

        public List<Object> getElements() {
            if (elements == null) {
                elements = new ArrayList<>();
            }
            return elements;
        }

        @XmlAnyElement(lax = true)
        private List<Object> elements;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private __JakartaXmlBinding_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
