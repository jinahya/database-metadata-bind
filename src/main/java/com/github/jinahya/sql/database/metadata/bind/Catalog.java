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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getCatalogs()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "tableCat",
    // ---------------------------------------------------------------------
    "crossReferences",
    "schemas"
})
public class Catalog extends AbstractTableDomain {

    private static final long serialVersionUID = 6239185259128825953L;

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + "}";
    }

    // ------------------------------------------------------------- TableDomain
    @Override
    public List<Table> getTables() {
        final List<Table> tables = new ArrayList<Table>();
        for (final Schema schema : getSchemas()) {
            tables.addAll(schema.getTables());
        }
        return tables;
    }

//    // ---------------------------------------------------------------- tableCat
//    public String getTableCat() {
//        return tableCat;
//    }
//
//    public void setTableCat(final String tableCat) {
//        this.tableCat = tableCat;
//    }
    // ----------------------------------------------------------------- schemas
    public List<Schema> getSchemas() {
        if (schemas == null) {
            schemas = new ArrayList<Schema>();
        }
        return schemas;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    Boolean virtual;

    @Getter
    @Setter
    @Labeled("TABLE_CAT")
    @XmlElement(required = true)
    private String tableCat;

    @Invokable(name = "getSchemas",
               types = {String.class, String.class},
               args = {
                   @Literals({":tableCat", "null"})
               }
    )
    @XmlElementRef
    private List<Schema> schemas;
}
