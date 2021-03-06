package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

// http://blog.bdoughan.com/2012/11/creating-generic-list-wrapper-in-jaxb.html
@XmlTransient
abstract class Wrapper<T extends MetadataType> {

    protected Wrapper(final Class<T> wrappedClass) {
        super();
        this.wrappedClass = requireNonNull(wrappedClass, "wrappedClass is null");
    }

    /**
     * Returns elements wrapped in this wrapper.
     *
     * @return elements wrapped in this wrapper.
     */
    public List<T> getElements() {
        if (elements == null) {
            elements = new ArrayList<>();
        }
        return elements;
    }

    @XmlTransient
    protected final Class<T> wrappedClass;

    @XmlAnyElement(lax = true)
    private List<T> elements;
}
