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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class Catalog implements Serializable {


    public static final String SUPPRESSION_PATH_SCHEMAS = "catalog/schemas";


    // ---------------------------------------------------------------- tableCat
    public String getTableCat() {

        return tableCat;
    }


    public void setTableCat(final String tableCat) {

        this.tableCat = tableCat;
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


    public void setMetadata(final Metadata metadata) {

        this.metadata = metadata;
    }


    public List<String> getSchemaNames() {

        final List<String> schemaNames
            = new ArrayList<String>(getSchemas().size());

        for (final Schema schema : getSchemas()) {
            final String schemaName = schema.getTableSchem();
            assert !schemaNames.contains(schemaName);
            schemaNames.add(schemaName);
        }

        return schemaNames;
    }


    public Schema getSchemaByName(final String tableSchem) {

        if (tableSchem == null) {
            throw new NullPointerException("tableSchem");
        }

        for (final Schema schema : getSchemas()) {
            if (tableSchem.equals(schema.getTableSchem())) {
                return schema;
            }
        }

        return null;
    }


    @ColumnLabel("TABLE_CAT")
    @XmlElement(nillable = true, required = true)
    private String tableCat;


    @XmlElementRef
    private List<Schema> schemas;


    @XmlTransient
    private Metadata metadata;


}

