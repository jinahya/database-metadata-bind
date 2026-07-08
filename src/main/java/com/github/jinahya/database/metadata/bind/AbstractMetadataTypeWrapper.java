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

import jakarta.xml.bind.annotation.XmlAnyElement;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base for XML wrappers that marshal a list of {@link MetadataType} values.
 * <p>
 * JAXB (Jakarta XML Binding) cannot marshal a bare {@link java.util.List}: a collection must be held by an
 * {@code @XmlRootElement}-annotated object to become a document. (Jakarta JSON Binding needs no such wrapper &mdash; it
 * serializes a {@link java.util.List} directly to a JSON array &mdash; so these wrappers exist purely for the XML
 * binding.) Concrete subclasses supply the root element, which lets them carry a per-type name:
 * {@snippet lang = "java":
 *
 * @param <T> the bound {@link MetadataType} type.
 * @XmlRootElement(name = "functions") class FunctionWrapper extends AbstractMetadataTypeWrapper<Function> {}}
 * <p>
 * Each element is marshalled under its own {@code @XmlRootElement} name via {@link XmlAnyElement}{@code (lax = true)},
 * with no intermediate wrapper element (i.e. the list is <em>unwrapped</em>). The element types must be known to the
 * {@link jakarta.xml.bind.JAXBContext} &mdash; e.g. by bootstrapping it with the package name (see
 * {@code jaxb.index}).
 */
public abstract class AbstractMetadataTypeWrapper<T extends MetadataType> {

    /**
     * Creates a new instance.
     */
    protected AbstractMetadataTypeWrapper() {
        super();
    }

    /**
     * Returns the wrapped elements.
     *
     * @return a (never-{@code null}, modifiable) list of the wrapped elements.
     */
    public List<T> getElements() {
        if (elements == null) {
            elements = new ArrayList<>();
        }
        return elements;
    }

    /**
     * Replaces the wrapped elements with the given list.
     *
     * @param elements the new list of elements.
     */
    void setElements(final List<T> elements) {
        this.elements = elements;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The wrapped elements, marshalled unwrapped &mdash; each under its own {@code @XmlRootElement} name.
     */
    @XmlAnyElement(lax = true)
    private List<T> elements;
}
