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
 * {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "tableSchem",
    // -------------------------------------------------------------------------
    "functions", "procedures", "tables", "UDTs"
})
public class Schema implements Serializable {//extends AbstractTableDomain {

    private static final long serialVersionUID = 7457236468401244963L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(Schema.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCatalog=" + tableCatalog
               + ",tableSchem=" + tableSchem
               + "}";
    }

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

    // -------------------------------------------------------------- procedures
    public List<Procedure> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<Procedure>();
        }
        return procedures;
    }

    // ------------------------------------------------------------------ tables
    public List<Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<Table>();
        }
        return tables;
    }

    // -------------------------------------------------------------------- UDTs
    public List<UDT> getUDTs() {
        if (UDTs == null) {
            UDTs = new ArrayList<UDT>();
        }
        return UDTs;
    }

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
}
