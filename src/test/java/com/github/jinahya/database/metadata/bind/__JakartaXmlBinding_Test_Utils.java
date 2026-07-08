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
import jakarta.xml.bind.Marshaller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Test utilities for reading/writing metadata bindings as XML via Jakarta XML Binding (JAXB).
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
final class __JakartaXmlBinding_Test_Utils {

    private static JAXBContext context;

    /**
     * Returns a shared context, bootstrapped by package name (see {@code jaxb.index}), which binds every metadata type
     * and {@link MetadataTypeWrapper}. A {@link JAXBContext} is immutable and thread-safe, so it is cached; a
     * {@link Marshaller}/{@link jakarta.xml.bind.Unmarshaller} is not, so a fresh one is created per call.
     *
     * @return a shared {@link JAXBContext}.
     * @throws JAXBException if failed to create.
     */
    private static synchronized JAXBContext context() throws JAXBException {
        if (context == null) {
            context = JAXBContext.newInstance(MetadataType.class.getPackageName());
        }
        return context;
    }

    /**
     * Marshals the specified metadata values&mdash;wrapped in a {@link MetadataTypeWrapper}&mdash;to the specified
     * file, creating parent directories as needed.
     *
     * @param values the values to marshal.
     * @param path   the file to write to (whose parent directory is assumed to exist).
     * @param <T>    the metadata type.
     * @throws JAXBException if failed to marshal.
     */
    static <T extends MetadataType> void marshal(final List<T> values, final Path path) throws JAXBException {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(path, "path is null");
        assert path.getParent() == null || Files.isDirectory(path.getParent());
        final var marshaller = context().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(MetadataTypeWrapper.of(values), path.toFile());
    }

    /**
     * Unmarshals a {@link MetadataTypeWrapper} from the specified file and returns its elements.
     *
     * @param path the file to read from.
     * @param <T>  the metadata type.
     * @return the list of unmarshalled elements.
     * @throws JAXBException if failed to unmarshal.
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

    // -----------------------------------------------------------------------------------------------------------------

    private __JakartaXmlBinding_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
