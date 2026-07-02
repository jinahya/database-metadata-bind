package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A single, concrete, <em>generic</em> XML wrapper for a list of {@link MetadataType} values.
 * <p>
 * JAXB (Jakarta XML Binding) cannot marshal a bare {@link java.util.List}: a collection must be held by an
 * {@code @XmlRootElement}-annotated object to become a document. (Jakarta JSON Binding needs no such wrapper &mdash; it
 * serializes a {@link java.util.List} directly to a JSON array &mdash; so this type exists purely for the XML binding.)
 * <p>
 * One class serves every element type: the list is marshalled via {@code @XmlAnyElement(lax = true)}, so each element is
 * written under its own {@code @XmlRootElement} name, and the wrapper itself needs only this one fixed
 * {@value #ROOT_ELEMENT_NAME} root element. The type variable {@code T} is erased at runtime, which is harmless here
 * since element naming is delegated to the children:
 * {@snippet lang = "java":
 * var wrapper = MetadataTypeWrapper.of(context.getSchemas(null, null));
 * // marshals to: <metadataTypes><schema>...</schema>...</metadataTypes>
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
     * The wrapped elements, marshalled unwrapped &mdash; each under its own {@code @XmlRootElement} name.
     */
    @XmlAnyElement(lax = true)
    private List<T> elements;
}
