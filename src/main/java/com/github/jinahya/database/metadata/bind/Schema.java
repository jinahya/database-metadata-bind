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
import java.sql.SQLException;
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
 * {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "tableSchem",
    // -------------------------------------------------------------------------
    "functions", "procedures", "tables", "UDTs", // -------------------------------------------------------------------------
//    "crossReferences"
})
public class Schema implements Serializable {//extends AbstractTableDomain {

    private static final long serialVersionUID = 7457236468401244963L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(Schema.class.getName());

    // -------------------------------------------------------------------------
    public static final String TABLE_SCHEM_NONE = "";

    // -------------------------------------------------------------------------
//    static Schema newVirtualInstance(final String tableCatalog) {
//        final Schema instance = new Schema();
//        instance.virtual = true;
//        instance.setTableCatalog(tableCatalog);
//        instance.setTableSchem(TABLE_SCHEM_NONE);
//        return instance;
//    }
//
//    static Schema newVirtualInstance(final MetadataContext context,
//                                     final String tableCatalog)
//            throws SQLException {
//        final Schema instance = newVirtualInstance(tableCatalog);
//        if (!context.suppressed("schema/functions")) {
//            instance.getFunctions().addAll(context.getFunctions(
//                    instance.getTableCatalog(), instance.getTableSchem(),
//                    null));
//        }
//        if (!context.suppressed("schema/procedures")) {
//            instance.getProcedures().addAll(context.getProcedures(
//                    instance.getTableCatalog(), instance.getTableSchem(),
//                    null));
//        }
//        if (!context.suppressed("schema/tables")) {
//            instance.getTables().addAll(context.getTables(
//                    instance.getTableCatalog(), instance.getTableSchem(), null,
//                    null));
//        }
//        if (!context.suppressed("schema/UDTs")) {
//            instance.getUDTs().addAll(context.getUDTs(
//                    instance.getTableCatalog(), instance.getTableSchem(), null,
//                    null));
//        }
//        return instance;
//    }

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCatalog=" + tableCatalog
               + ",tableSchem=" + tableSchem
               + "}";
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
    // ------------------------------------------------------------ tableCatalog
    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(final String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    Schema tableCatalog(final String tableCatalog) {
        setTableCatalog(tableCatalog);
        return this;
    }

    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    Schema tableSchem(final String tableSchem) {
        setTableSchem(tableSchem);
        return this;
    }

    // --------------------------------------------------------------- functions
    public List<Function> getFunctions() {
        if (functions == null) {
            functions = new ArrayList<Function>();
        }
        return functions;
    }

//    @Deprecated
//    public void setFunctions(final List<Function> functions) {
//        this.functions = functions;
//    }
    // -------------------------------------------------------------- procedures
    public List<Procedure> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<Procedure>();
        }
        return procedures;
    }

//    @Deprecated
//    public void setProcedures(final List<Procedure> procedures) {
//        this.procedures = procedures;
//    }
    // ------------------------------------------------------------------ tables
//    @Override
    public List<Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<Table>();
        }
        return tables;
    }

//    @Deprecated
//    public void setTables(final List<Table> tables) {
//        this.tables = tables;
//    }
    // -------------------------------------------------------------------- UDTs
    public List<UDT> getUDTs() {
        if (UDTs == null) {
            UDTs = new ArrayList<UDT>();
        }
        return UDTs;
    }

//    @Deprecated
//    public void setUDTs(final List<UDT> UDTs) {
//        this.UDTs = UDTs;
//    }
    // --------------------------------------------------------- crossReferences
//    public List<CrossReference> getCrossReferences() {
//        if (crossReferences == null) {
//            crossReferences = new ArrayList<CrossReference>();
//        }
//        return crossReferences;
//    }
    // -------------------------------------------------------------------------
    @XmlAttribute
    Boolean virtual;

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Label("TABLE_CATALOG")
    @Bind(label = "TABLE_CATALOG", nillable = true)
    @Nillable
    private String tableCatalog;

    // -------------------------------------------------------------------------
    @XmlElement
    @Label("TABLE_SCHEM")
    @Bind(label = "TABLE_SCHEM")
    private String tableSchem;

    // -------------------------------------------------------------------------
    @XmlElementRef
    @Invoke(name = "getFunctions",
            types = {String.class, String.class, String.class},
            parameters = {
                @Literals({":tableCatalog", ":tableSchem", "null"})
            }
    )
    private List<Function> functions;

    @XmlElementRef
    @Invoke(name = "getProcedures",
            types = {String.class, String.class, String.class},
            parameters = {
                @Literals({":tableCatalog", ":tableSchem", "null"})
            }
    )
    private List<Procedure> procedures;

    @XmlElementRef
    @Invoke(name = "getTables",
            types = {String.class, String.class, String.class,
                     String[].class},
            parameters = {
                @Literals({":tableCatalog", ":tableSchem", "null", "null"})
            }
    )
    private List<Table> tables;

    @XmlElementRef
    @Invoke(name = "getUDTs",
            types = {String.class, String.class, String.class, int[].class},
            parameters = {
                @Literals({":tableCatalog", ":tableSchem", "null", "null"})
            }
    )
    private List<UDT> UDTs;

//    @XmlElementRef
//    private List<CrossReference> crossReferences;
}
