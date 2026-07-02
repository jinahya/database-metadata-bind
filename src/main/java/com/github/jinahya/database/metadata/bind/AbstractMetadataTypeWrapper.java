package com.github.jinahya.database.metadata.bind;

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
 * @XmlRootElement(name = "functions")
 * class FunctionWrapper extends AbstractMetadataTypeWrapper<Function> {
 * }
 *}
 * <p>
 * Each element is marshalled under its own {@code @XmlRootElement} name via {@link XmlAnyElement}{@code (lax = true)},
 * with no intermediate wrapper element (i.e. the list is <em>unwrapped</em>). The element types must be known to the
 * {@link jakarta.xml.bind.JAXBContext} &mdash; e.g. by bootstrapping it with the package name (see {@code jaxb.index}).
 *
 * @param <T> the bound {@link MetadataType} type.
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
