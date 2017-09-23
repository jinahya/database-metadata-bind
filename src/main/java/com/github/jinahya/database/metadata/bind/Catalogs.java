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

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A wrapper class for {@link Catalog}s.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@XmlRootElement
public class Catalogs extends Plural<Catalog> {

    private static final long serialVersionUID = -3733224702437178402L;

    // -------------------------------------------------------------------------
    static Catalogs newInstance(final List<Catalog> catalogs) {
        final Catalogs instance = new Catalogs();
        instance.getCatalogs().addAll(catalogs);
        return instance;
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    /**
     * Returns a list of {@link Catalog}s.
     *
     * @return a list of {@link Catalog}.
     */
    @XmlElement(name = "catalog")
    public List<Catalog> getCatalogs() {
        return getElements();
    }
}
