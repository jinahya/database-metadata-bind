package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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
import jakarta.xml.bind.JAXBException;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Test-only file helpers for Jakarta XML Binding snapshots.
 * <p>
 * These helpers intentionally live in the test tree. They wrap metadata lists in {@link MetadataTypeWrapper}, bootstrap
 * {@link JAXBContext} by this package name through {@code jaxb.index}, and keep a cached context because
 * {@link JAXBContext} is thread-safe after construction. They do not configure formatted XML output; tests that need a
 * specific {@link jakarta.xml.bind.Marshaller} policy should create their own marshaller.
 */
final class __JakartaXmlBinding_Test_Utils {

    private static volatile JAXBContext context;

    /**
     * Returns a shared {@link JAXBContext} for this package.
     * <p>
     * The context is bootstrapped by package name, so the test uses the production {@code jaxb.index} resource and does
     * not list every metadata type manually.
     *
     * @return a shared {@link JAXBContext}.
     * @throws JAXBException if the context cannot be created.
     */
    private static JAXBContext context() throws JAXBException {
        var result = context;
        if (result == null) {
            synchronized (__JakartaXmlBinding_Test_Utils.class) {
                result = context;
                if (result == null) {
                    context = result = JAXBContext.newInstance(MetadataType.class.getPackageName());
                }
            }
        }
        return result;
    }

    /**
     * Marshals the specified metadata values to the specified path.
     * <p>
     * The values are wrapped in {@link MetadataTypeWrapper} so the list can be written as a single XML document.
     *
     * @param values the values to marshal.
     * @param path   the path to write to.
     * @param <T>    the metadata type.
     * @throws JAXBException if the values cannot be marshalled.
     */
    static <T extends MetadataType> void marshal(final List<T> values, final Path path) throws JAXBException {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(path, "path is null");
        final var marshaller = context().createMarshaller();
        marshaller.marshal(MetadataTypeWrapper.of(values), path.toFile());
    }

    /**
     * Unmarshals metadata values from the specified path.
     * <p>
     * The file is expected to contain a {@link MetadataTypeWrapper}; its elements are returned as the requested
     * metadata type.
     *
     * @param path the path to read from.
     * @param <T>  the metadata type.
     * @return unmarshalled metadata values.
     * @throws JAXBException if values cannot be unmarshalled.
     */
    static <T extends MetadataType> List<T> unmarshal(final Path path) throws JAXBException {
        Objects.requireNonNull(path, "path is null");
        final var object = context().createUnmarshaller().unmarshal(path.toFile());
        if (!(object instanceof MetadataTypeWrapper<?> wrapper)) {
            throw new IllegalStateException("not a " + MetadataTypeWrapper.class.getSimpleName() + ": " + object);
        }
        @SuppressWarnings("unchecked")
        final var elements = (List<T>) wrapper.getElements();
        return elements;
    }

    private __JakartaXmlBinding_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
