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
import org.apache.commons.lang3.builder.CompareToBuilder;


/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "tableSchem",
        // ---------------------------------------------------------------------
        "crossReferences",
        "functions", "procedures",
        //"superTables",
        //"superTypes",
        "tables",
        "UDTs",
        // ---------------------------------------------------------------------
        "unknownResults"
    }
)
public class Schema extends AbstractChild<Catalog>
    implements Comparable<Schema>, TableDomain {


    // by TABLE_CATALOG and TABLE_SCHEM.
    @Override
    public int compareTo(final Schema o) {

        return new CompareToBuilder()
            .append(tableCatalog, o.getTableCatalog())
            .append(tableSchem, o.getTableSchem())
            .build();
    }


    @Override
    public List<CrossReference> getCrossReferences() {
        return crossReferences;
    }


    @Override
    public void setCrossReferences(List<CrossReference> crossReferences) {
        this.crossReferences = crossReferences;
    }


    @Override
    public String toString() {

        return super.toString() + "{"
               + "tableCatalog=" + tableCatalog
               + ", tableSchem=" + tableSchem
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


    // ----------------------------------------------------------------- catalog
    // just for class diagram
    private Catalog getCatalog() {

        return getParent();
    }


//    public void setCatalog(final Catalog catalog) {
//
//        setParent(catalog);
//    }
    // --------------------------------------------------------------- functions
    public List<Function> getFunctions() {

        if (functions == null) {
            functions = new ArrayList<Function>();
        }

        return functions;
    }


    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }


    // -------------------------------------------------------------- procedures
    public List<Procedure> getProcedures() {

        if (procedures == null) {
            procedures = new ArrayList<Procedure>();
        }

        return procedures;
    }


    public void setProcedures(List<Procedure> procedures) {
        this.procedures = procedures;
    }


//    // ------------------------------------------------------------- superTables
//    public List<SuperTable> getSuperTables() {
//
//        if (superTables == null) {
//            superTables = new ArrayList<SuperTable>();
//        }
//
//        return superTables;
//    }
//    // -------------------------------------------------------------- superTypes
//    public List<SuperType> getSuperTypes() {
//
//        if (superTypes == null) {
//            superTypes = new ArrayList<SuperType>();
//        }
//
//        return superTypes;
//    }
    // ------------------------------------------------------------------ tables
    @Override
    public List<Table> getTables() {

        if (tables == null) {
            tables = new ArrayList<Table>();
        }

        return tables;
    }


    public void setTables(List<Table> tables) {
        this.tables = tables;
    }


    // -------------------------------------------------------------------- UDTs
    public List<UDT> getUDTs() {

        if (UDTs == null) {
            UDTs = new ArrayList<UDT>();
        }

        return UDTs;
    }


    public void setUDTs(List<UDT> UDTs) {
        this.UDTs = UDTs;
    }


    // -------------------------------------------------------------------------
    @Label("TABLE_CATALOG")
    @NillableBySpecification
    @XmlAttribute
    private String tableCatalog;


    @Label("TABLE_SCHEM")
    @XmlElement(required = true)
    private String tableSchem;


    @XmlElementRef
    private List<CrossReference> crossReferences;


    @Invocation(
        name = "getFunctions",
        types = {String.class, String.class, String.class},
        argsarr = {
            @InvocationArgs({":tableCatalog", ":tableSchem", "null"})
        }
    )
    @XmlElementRef
    private List<Function> functions;


    @Invocation(
        name = "getProcedures",
        types = {String.class, String.class, String.class},
        argsarr = {
            @InvocationArgs({":tableCatalog", ":tableSchem", "null"})
        }
    )
    @XmlElementRef
    private List<Procedure> procedures;


//    @Invocation(
//        name = "getSuperTables",
//        types = {String.class, String.class, String.class},
//        argsarr = {
//            @InvocationArgs({":tableCatalog", ":tableSchem", "null"})
//        }
//    )
//    @XmlElementRef
//    private List<SuperTable> superTables;
//    @Invocation(
//        name = "getSuperTypes",
//        types = {String.class, String.class, String.class},
//        argsarr = {
//            @InvocationArgs({":tableCatalog", ":tableSchem", "null"})
//        }
//    )
//    @XmlElementRef
//    private List<SuperType> superTypes;
    @Invocation(
        name = "getTables",
        types = {String.class, String.class, String.class, String[].class},
        argsarr = {
            @InvocationArgs({":tableCatalog", ":tableSchem", "null", "null"})
        }
    )
    @XmlElementRef
    private List<Table> tables;


    @Invocation(
        name = "getUDTs",
        types = {String.class, String.class, String.class, int[].class},
        argsarr = {
            @InvocationArgs({":tableCatalog", ":tableSchem", "null", "null"})
        }
    )
    @XmlElementRef
    private List<UDT> UDTs;


    @XmlElement(name = "unknownResult", nillable = true)
    private List<UnknownResult> unknownResults;


}

