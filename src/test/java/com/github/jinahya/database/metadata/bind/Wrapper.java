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

import java.beans.Introspector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

/**
 * A class for wrapping elements.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <T> element type parameter.
 */
class Wrapper<T> implements Serializable {

    private static final long serialVersionUID = -8704321395991938882L;

    // -------------------------------------------------------------------------
    static final QName NAME = new QName(
            XmlConstants.NS_URI_DATABASE_METADATA_BIND,
            Introspector.decapitalize(Wrapper.class.getSimpleName()));

    // -------------------------------------------------------------------------
    static <T> Wrapper<T> of(final Collection<? extends T> elements) {
        final Wrapper<T> instance = new Wrapper<>();
        instance.getElements().addAll(elements);
        return instance;
    }

    // -------------------------------------------------------------------------
    List<T> getElements() {
        if (elements == null) {
            elements = new ArrayList<>();
        }
        return elements;
    }

    // -------------------------------------------------------------------------
    @XmlAnyElement(lax = true)
    private List<T> elements;
}
