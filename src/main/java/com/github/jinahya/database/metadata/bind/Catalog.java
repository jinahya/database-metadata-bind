/*
 * Copyright 2011 Jin Kwon <jinahya at gmail.com>.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getCatalogs()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getCatalogs()
 */
@XmlRootElement
@XmlType(propOrder = {
    "tableCat",
    // -------------------------------------------------------------------------
    "schemas"
})
public class Catalog implements Serializable {//extends AbstractTableDomain {

    private static final long serialVersionUID = 6239185259128825953L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(Catalog.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + "}";
    }

    // ---------------------------------------------------------------- tableCat
    /**
     * Returns the current value of {@code tableCat} property.
     *
     * @return the current value of {@code tableCat} property.
     */
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Replaces the value of {@code tableCat} property with given.
     *
     * @param tableCat new value for {@code tableCat} property.
     */
    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ----------------------------------------------------------------- schemas
    /**
     * Returns schemas of this catalog.
     *
     * @return a list of schemas
     */
    public List<Schema> getSchemas() {
        if (schemas == null) {
            schemas = new ArrayList<Schema>();
        }
        return schemas;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    Boolean virtual;

    @XmlElement
    @Bind(label = "TABLE_CAT")
    private String tableCat;

    @XmlElementRef
    @Invoke(name = "getSchemas",
            types = {String.class, String.class},
            parameters = {
                @Literals({":tableCat", "null"})
            }
    )
    private List<Schema> schemas;
}
