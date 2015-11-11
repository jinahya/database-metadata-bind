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


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "tableSchem", "tableCatalog"
    }
)
public class SchemaName extends AbstractChild<Metadata> {


    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {

        return tableSchem;
    }


    public void setTableSchem(final String tableSchem) {

        this.tableSchem = tableSchem;
    }


    // ------------------------------------------------------------ tableCatalog
    public String getTableCatalog() {

        return tableCatalog;
    }


    public void setTableCatalog(final String tableCatalog) {

        this.tableCatalog = tableCatalog;
    }


    // ---------------------------------------------------------------- metadata
    public Metadata getMetadata() {

        return getParent();
    }


    public void setMetadata(final Metadata metadata) {

        setParent(metadata);
    }


    SchemaName metadata(final Metadata metadata) {

        setMetadata(metadata);

        return this;
    }


    // -------------------------------------------------------------------------
    @Label("TABLE_SCHEM")
    @XmlElement(required = true)
    private String tableSchem;


    @Label("TABLE_CATALOG")
    @NillableBySpecification
    @XmlElement(nillable = true, required = false)
    private String tableCatalog;


}

