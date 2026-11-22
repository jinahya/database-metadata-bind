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

import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * A single, concrete, <em>generic</em> XML wrapper for a list of {@link MetadataType} values.
 * <p>
 * JAXB (Jakarta XML Binding) cannot marshal a bare {@link java.util.List}: a collection must be held by an
 * {@code @XmlRootElement}-annotated object to become a document. (Jakarta JSON Binding needs no such wrapper &mdash; it
 * serializes a {@link java.util.List} directly to a JSON array &mdash; so this type exists purely for the XML
 * binding.)
 * <p>
 * One class serves every element type: the list is marshalled via {@code @XmlAnyElement(lax = true)}, so each element
 * is written under its own {@code @XmlRootElement} name, and the wrapper itself needs only this one fixed
 * {@value #ROOT_ELEMENT_NAME} root element. The type variable {@code T} is erased at runtime, which is harmless here
 * since element naming is delegated to the children:
 * {@snippet lang = "java":
 * var wrapper = new MetadataTypeWrapper<Schema>();
 * wrapper.getElements().addAll(context.getSchemas(null, null));
 * // marshals to: <dmb:metadataTypes xmlns:dmb="..."><dmb:schema>...</dmb:schema>...</dmb:metadataTypes>
 *}
 * <p>
 * Note the trade-off: a single generic wrapper yields one shared root element ({@value #ROOT_ELEMENT_NAME}) rather than
 * a per-type name (e.g. {@code <schemas>}/{@code <tables>}); the element types must be known to the
 * {@link jakarta.xml.bind.JAXBContext} (e.g. by bootstrapping it with the package name; see {@code jaxb.index}).
 *
 * @param <T> the bound {@link MetadataType} type.
 */
@XmlRootElement(name = MetadataTypeWrapper.ROOT_ELEMENT_NAME)
public class MetadataTypeWrapper<T extends MetadataType> {

    private static final System.Logger logger = System.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * The root element name for instances of this class: {@value}.
     */
    public static final String ROOT_ELEMENT_NAME = "metadataTypes";

    /**
     * Creates a new instance wrapping the given elements.
     *
     * @param elements the elements to wrap.
     * @param <T>      the bound {@link MetadataType} type.
     * @return a new wrapper holding {@code elements}.
     */
    static <T extends MetadataType> MetadataTypeWrapper<T> of(final List<T> elements) {
        final var instance = new MetadataTypeWrapper<T>();
        instance.setElements(elements);
        return instance;
    }

    /**
     * Creates a new, empty instance.
     */
    @SuppressWarnings({
            "java:S2637" // "@NonNull" values should not be set to null
    })
    public MetadataTypeWrapper() {
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
     * Drops, with a warning, each unmarshalled child which is not a {@link MetadataType}.
     * <p>
     * With {@code @XmlAnyElement(lax = true)}, a child the context does not know (e.g., a type written by a newer
     * version of this library) is kept as a DOM element; since {@code T} is erased, it would otherwise sit in
     * {@link #getElements()} and fail, with a {@link ClassCastException}, only when read. See #84.
     *
     * @param unmarshaller the unmarshaller.
     * @param parent       the parent object, if any.
     */
    private void afterUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
        if (elements == null) {
            return;
        }
        final List<?> unknown = ((List<?>) elements).stream().filter(e -> !(e instanceof MetadataType)).toList();
        if (unknown.isEmpty()) {
            return;
        }
        logger.log(System.Logger.Level.WARNING, "dropping {0} unknown element(s): {1}", unknown.size(), unknown);
        ((List<?>) elements).removeAll(unknown);
    }

    /**
     * The wrapped elements, marshalled unwrapped &mdash; each under its own {@code @XmlRootElement} name.
     */
    @XmlAnyElement(lax = true)
    private List<T> elements;
}
