package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String,
 * java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
        "tableSchem"
        // -------------------------------------------------------------------------------------------------------------
        ,"functions", "procedures", "tables", "udts"
})
public class Schema extends AbstractChildValue<Catalog> {

    private static final long serialVersionUID = 7457236468401244963L;

    // ---------------------------------------------------------------------------------------- TABLE_SCHEM / tableSchem
    public static final String COLUMN_NAME_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    // ------------------------------------------------------------------------------------ TABLE_CATALOG / tableCatalog
    public static final String COLUMN_NAME_TABLE_CATALOG = "TABLE_CATALOG";

    public static final String ATTRIBUTE_NAME_TABLE_CATALOG = "tableCatalog";

    // -----------------------------------------------------------------------------------------------------------------
    public static Schema newVirtualInstance(final Catalog catalog) {
        final Schema instance = new Schema();
        instance.virtual = Boolean.TRUE;
        instance.setParent(catalog);
        instance.setTableSchem("");
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public Schema() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + ATTRIBUTE_NAME_TABLE_SCHEM + '=' + tableCatalog
               + ',' + ATTRIBUTE_NAME_TABLE_SCHEM + '=' + tableSchem
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Schema that = (Schema) obj;
        return Objects.equals(tableCatalog, that.tableCatalog)
               && Objects.equals(tableSchem, that.tableSchem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCatalog,
                            tableSchem);
    }

    // --------------------------------------------------------------------------------------------------------- virtual

    /**
     * Indicates whether this instance is a virtual instance.
     *
     * @return {@code true} if this instance is a virtual instance; {@code false} otherwise.
     */
    public boolean isVirtual() {
        return virtual != null && virtual;
    }

    // --------------------------------------------------------------------------------------------------------- catalog
    public Catalog getCatalog() {
        return getParent();
    }

    void setCatalog(final Catalog catalog) {
        setParent(catalog);
    }

    // ---------------------------------------------------------------------------------------------------- tableCatalog
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

    // ------------------------------------------------------------------------------------------------------ tableSchem
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

//    // ------------------------------------------------------------------------------------------------------- functions
//    public List<Function> getFunctions() {
//        if (functions == null) {
//            functions = new ArrayList<>();
//        }
//        return functions;
//    }
//
//    // ------------------------------------------------------------------------------------------------------ procedures
//    public List<Procedure> getProcedures() {
//        if (procedures == null) {
//            procedures = new ArrayList<>();
//        }
//        return procedures;
//    }
//
//    // ---------------------------------------------------------------------------------------------------------- tables
//    public List<Table> getTables() {
//        if (tables == null) {
//            tables = new ArrayList<Table>();
//        }
//        return tables;
//    }
//
//    // ------------------------------------------------------------------------------------------------------------ UDTs
//    public List<UDT> getUDTs() {
//        if (UDTs == null) {
//            UDTs = new ArrayList<UDT>();
//        }
//        return UDTs;
//    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbProperty(nillable = true)
    @XmlAttribute(required = false)
    Boolean virtual = Boolean.FALSE;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbProperty(nillable = true)
    @XmlAttribute(required = false)
    @MayBeNull
    @Label(COLUMN_NAME_TABLE_CATALOG)
    @Bind(label = COLUMN_NAME_TABLE_CATALOG, nillable = true)
    private String tableCatalog;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement
    @NotBlank
    @Label(COLUMN_NAME_TABLE_SCHEM)
    @Bind(label = COLUMN_NAME_TABLE_SCHEM)
    private String tableSchem;

    // ------------------------------------------------------------------------------------------------------- functions
    public List<Function> getFunctions() {
        if (functions == null) {
            functions = new ArrayList<>();
        }
        return functions;
    }

    // ------------------------------------------------------------------------------------------------------ procedures
    public List<Procedure> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<>();
        }
        return procedures;
    }

    // ---------------------------------------------------------------------------------------------------------- tables
    public List<Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        return tables;
    }

    // ------------------------------------------------------------------------------------------------------------ UDTs
    public List<UDT> getUdts() {
        if (udts == null) {
            udts = new ArrayList<>();
        }
        return udts;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementWrapper
    @XmlElementRef
    @Invoke(name = "getFunctions",
            types = {String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCatalog", ":tableSchem", "null"})
            }
    )
    private List<Function> functions;

    @XmlElementWrapper
    @XmlElementRef
    @Invoke(name = "getProcedures",
            types = {String.class, String.class, String.class},
            parameters = {
                    @Literals({":tableCatalog", ":tableSchem", "null"})
            }
    )
    private List<Procedure> procedures;

    @XmlElementWrapper
    @XmlElementRef
    @Invoke(name = "getTables",
            types = {String.class, String.class, String.class,
                     String[].class},
            parameters = {
                    @Literals({":tableCatalog", ":tableSchem", "null", "null"})
            }
    )
    private List<Table> tables;

    @XmlElementWrapper
    @XmlElementRef
    @Invoke(name = "getUDTs",
            types = {String.class, String.class, String.class, int[].class},
            parameters = {
                    @Literals({":tableCatalog", ":tableSchem", "null", "null"})
            }
    )
    private List<UDT> udts;
}
