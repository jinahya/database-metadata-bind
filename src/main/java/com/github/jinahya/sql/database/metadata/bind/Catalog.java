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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
    // -------------------------------------------------------------------------
    "schemas",
    // -------------------------------------------------------------------------
    "crossReferences"
})
public class Catalog implements Serializable {//extends AbstractTableDomain {

    private static final long serialVersionUID = 6239185259128825953L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(Catalog.class.getName());

    public static final String TABLE_CAT_NONE = "";

    // -------------------------------------------------------------------------
    static Catalog newVirtualInstance() {
        final Catalog instance = new Catalog();
        instance.virtual = true;
        instance.setTableCat(TABLE_CAT_NONE);
        return instance;
    }

    static Catalog newVirtualInstance(final MetadataContext context)
            throws SQLException, ReflectiveOperationException {
        final Catalog instance = newVirtualInstance();
        instance.getSchemas().addAll(
                context.getSchemas(instance.getTableCat(), null));
        return instance;
    }

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + "}";
    }

    // ---------------------------------------------------------------- tableCat
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

    // ------------------------------------------------------------------ tables
    @XmlTransient
    public List<Table> getTables() {
        final List<Table> tables = new ArrayList<Table>();
        for (final Schema schema : getSchemas()) {
            tables.addAll(schema.getTables());
        }
        return tables;
    }

    // --------------------------------------------------------- crossReferences
    public List<CrossReference> getCrossReferences() {
        if (crossReferences == null) {
            crossReferences = new ArrayList<CrossReference>();
        }
        return crossReferences;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    Boolean virtual;

    @XmlElement
    @Label("TABLE_CAT")
    @Bind(label = "TABLE_CAT")
    @Setter
    @Getter
    private String tableCat;

    @XmlElementRef
    @Invoke(name = "getSchemas",
            types = {String.class, String.class},
            parameters = {
                @Literals({":tableCat", "null"})
            }
    )
    private List<Schema> schemas;

    @XmlElementRef
    private List<CrossReference> crossReferences;
}
