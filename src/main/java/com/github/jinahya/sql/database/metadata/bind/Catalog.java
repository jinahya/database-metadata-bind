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


package com.github.jinahya.sql.database.metadata.bind;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {"tableCat", "schemas"})
public class Catalog {


    private static final Logger logger = getLogger(Catalog.class.getName());


    @Override
    public String toString() {

        return super.toString() + "{" + "tableCat=" + tableCat + '}';
    }


    // ---------------------------------------------------------------- tableCat
    public String getTableCat() {

        return tableCat;
    }


    public void setTableCat(final String tableCat) {

        this.tableCat = tableCat;
    }


    Catalog tableCat(final String tableCat) {

        setTableCat(tableCat);

        return this;
    }


    // ----------------------------------------------------------------- schemas
    public List<Schema> getSchemas() {

        if (schemas == null) {
            schemas = new ArrayList<Schema>();
        }

        return schemas;
    }


    // ---------------------------------------------------------------- metadata
    public Metadata getMetadata() {

        return metadata;
    }


    //@XmlAttribute
    @Deprecated
    private String getMetadataString() {

        return metadata == null ? null : metadata.toString();
    }


    public void setMetadata(final Metadata metadata) {

        this.metadata = metadata;
    }


    Catalog metadata(final Metadata metadata) {

        setMetadata(metadata);

        return this;
    }


    // -------------------------------------------------------------------------
    @Label("TABLE_CAT")
    @XmlElement(nillable = true, required = true)
    private String tableCat;


    @Invocation(
        name = "getSchemas",
        types = {String.class, String.class},
        argsarr = {
            @InvocationArgs({":tableCat", "null"})
        }
    )
    @XmlElementRef
    private List<Schema> schemas;


    //@XmlTransient
    private transient Metadata metadata;


}

