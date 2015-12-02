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
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.CompareToBuilder;


/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getCatalogs()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "tableCat",
        // ---------------------------------------------------------------------
        "crossReferences",
        "schemas"
    }
)
public class Catalog extends AbstractChild<Metadata> implements TableDomain {


    private static final Logger logger
        = Logger.getLogger(Catalog.class.getName());


    public static Comparator<Catalog> natural() {

        return new Comparator<Catalog>() {

            @Override
            public int compare(final Catalog o1, final Catalog o2) {

                //  by catalog name
                return new CompareToBuilder()
                    .append(o1.getTableCat(), o2.getTableCat())
                    .build();
            }

        };
    }


    @Override
    public String toString() {

        return super.toString() + "{"
               + "tableCat=" + tableCat
               + "}";
    }


    @Override
    public List<Table> getTables() {

        final List<Table> tables = new ArrayList<Table>();

        for (final Schema schema : getSchemas()) {
            tables.addAll(schema.getTables());
        }

        return tables;
    }


    @Override
    public List<CrossReference> getCrossReferences() {

        return crossReferences;
    }


    @Override
    public void setCrossReferences(final List<CrossReference> crossReferences) {

        this.crossReferences = crossReferences;
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


    public void setSchemas(List<Schema> schemas) {
        this.schemas = schemas;
    }


    // ---------------------------------------------------------------- metadata
    // just for class digram
    private Metadata getMetadata() {

        return getParent();
    }


//    public void setMetadata(final Metadata metadata) {
//
//        setParent(metadata);
//    }
//
//
//    Catalog metadata(final Metadata metadata) {
//
//        setMetadata(metadata);
//
//        return this;
//    }
    // -------------------------------------------------------------------------
    @Label("TABLE_CAT")
    @XmlElement(required = true)
    private String tableCat;


    @XmlElementRef
    private List<CrossReference> crossReferences;


    @Invocation(
        name = "getSchemas",
        types = {String.class, String.class},
        argsarr = {
            @InvocationArgs({":tableCat", "null"})
        }
    )
    @XmlElementRef
    private List<Schema> schemas;

}

