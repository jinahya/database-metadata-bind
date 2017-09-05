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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
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
    private static final Logger logger = getLogger(Catalog.class.getName());

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

//    // -------------------------------------------------------------------------
//    void bind(final DatabaseMetaData context) throws SQLException {
//        final ResultSet resultSet = context.getSchemas(tableCat, null);
//        try {
//            while (resultSet.next()) {
//                final Schema schema = new Schema();
//                getSchemas().add(schema);
//                schema.setTableSchem(resultSet.getString("TABLE_SCHEM"));
//                schema.setTableCatalog(resultSet.getString("TABLE_CATALOG"));
//            }
//        } finally {
//            resultSet.close();
//        }
//        if (getSchemas().isEmpty()) {
//            logger.log(Level.FINE, "adding an empty schema to {0}", this);
//            final Schema schema = new Schema();
//            schema.virtual = true;
//            schema.setTableCatalog(tableCat);
//            schema.setTableSchem("");
//        }
//        for (final Schema schema : getSchemas()) {
//            schema.bind(context);
//        }
//    }
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

    @XmlElement(required = true)
    @Labeled("TABLE_CAT")
    @Getter
    @Setter
    private String tableCat;

    @XmlElementRef
    @Invokable(name = "getSchemas",
               types = {String.class, String.class},
               args = {
                   @Literals({":tableCat", "null"})
               }
    )
    private List<Schema> schemas;
}
