/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "allProceduresAreCallable", "allTablesAreSelectable",
        "catalogSeparator", "catalogTerm",
        "databaseMajorVersion", "databaseMinorVersion", "databaseProductName",
        "databaseProductVersion",
        "driverMajorVersion", "driverMinorVersion", "driverName",
        "driverVersion",
        "numericFunctions", "stringFunctions", "systemFunctions",
        "timeDateFunctions",
        "catalogs", "clientInfoProperties", "tableTypes", "typeInfo"
    }
)
public class Metadata {


    public static final String SUPPRESSION_PATH_CATALOGS = "metadata/catalogs";


    public static final String SUPPRESSION_PATH_CLIENT_INFO_PROPERTIES
        = "metadata/clientProperties";


    public static final String SUPPRESSION_PATH_TYPE_INFO = "metadata/typeInfo";


    public static final String SUPPRESSION_PATH_TABLE_TYPES
        = "metadata/tabletypes";


    public void print(final PrintStream out) throws JAXBException {

        if (out == null) {
            throw new NullPointerException("null out");
        }

        assert Metadata.class.getAnnotation(XmlRootElement.class) != null;

        final JAXBContext context = JAXBContext.newInstance(Metadata.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(this, out);
    }


    // -------------------------------------------------------- suppressionPaths
    public Set<String> getSuppressionPaths() {

        if (suppressionPaths == null) {
            suppressionPaths = new TreeSet<String>();
        }

        return suppressionPaths;
    }


    // ------------------------------------------------ allProceduresAreCallable
    public boolean getAllProceduresAreCallable() {

        return allProceduresAreCallable;
    }


    public void setAllProceduresAreCallable(
        final boolean allProceduresAreCallable) {

        this.allProceduresAreCallable = allProceduresAreCallable;
    }


    public boolean getAllTablesAreSelectable() {

        return allTablesAreSelectable;
    }


    public void setAllTablesAreSelectable(
        final boolean allTablesAreSelectable) {

        this.allTablesAreSelectable = allTablesAreSelectable;
    }


    public String getCatalogSeparator() {

        return catalogSeparator;
    }


    public void setCatalogSeparator(final String catalogSeparator) {

        this.catalogSeparator = catalogSeparator;
    }


    public String getCatalogTerm() {

        return catalogTerm;
    }


    public void setCatalogTerm(final String catalogTerm) {

        this.catalogTerm = catalogTerm;
    }


    public int getDatabaseMajorVersion() {

        return databaseMajorVersion;
    }


    public void setDatabaseMajorVersion(final int databaseMajorVersion) {

        this.databaseMajorVersion = databaseMajorVersion;
    }


    public int getDatabaseMinorVersion() {

        return databaseMinorVersion;
    }


    public void setDatabaseMinorVersion(final int databaseMinorVersion) {

        this.databaseMinorVersion = databaseMinorVersion;
    }


    public String getDatabaseProductName() {

        return databaseProductName;
    }


    public void setDatabaseProductName(final String databaseProductName) {

        this.databaseProductName = databaseProductName;
    }


    public String getDatabaseProductVersion() {

        return databaseProductVersion;
    }


    public void setDatabaseProductVersion(final String databaseProductVersion) {

        this.databaseProductVersion = databaseProductVersion;
    }


    public int getDriverMajorVersion() {

        return driverMajorVersion;
    }


    public void setDriverMajorVersion(final int driverMajorVersion) {

        this.driverMajorVersion = driverMajorVersion;
    }


    public int getDriverMinorVersion() {

        return driverMinorVersion;
    }


    public void setDriverMinorVersion(final int driverMinorVersion) {

        this.driverMinorVersion = driverMinorVersion;
    }


    public String getDriverName() {

        return driverName;
    }


    public void setDriverName(final String driverName) {

        this.driverName = driverName;
    }


    public String getDriverVersion() {

        return driverVersion;
    }


    public void setDriverVersion(final String driverVersion) {

        this.driverVersion = driverVersion;
    }


    // -------------------------------------------------------- numericFunctions
    public String getNumericFunctions() {

        return numericFunctions;
    }


    public void setNumericFunctions(final String numericFunctions) {

        this.numericFunctions = numericFunctions;
    }


    // --------------------------------------------------------- stringFunctions
    public String getStringFunctions() {

        return stringFunctions;
    }


    public void setStringFunctions(final String stringFunctions) {

        this.stringFunctions = stringFunctions;
    }


    // --------------------------------------------------------- systemFunctions
    public String getSystemFunctions() {

        return systemFunctions;
    }


    public void setSystemFunctions(final String systemFunctions) {

        this.systemFunctions = systemFunctions;
    }


    // ------------------------------------------------------- timeDateFunctions
    public String getTimeDateFunctions() {

        return timeDateFunctions;
    }


    public void setTimeDateFunctions(final String timeDateFunctions) {

        this.timeDateFunctions = timeDateFunctions;
    }


    // ---------------------------------------------------------------- catalogs
    public List<Catalog> getCatalogs() {

        if (catalogs == null) {
            catalogs = new ArrayList<Catalog>();
        }

        return catalogs;
    }


    public List<String> getCatalogNames() {

        final List<String> catalogNames
            = new ArrayList<String>(getCatalogs().size());

        for (final Catalog catalog : getCatalogs()) {
            final String catalogName = catalog.getTableCat();
            assert !catalogNames.contains(catalogName);
            catalogNames.add(catalogName);
        }

        return catalogNames;
    }


    public Catalog getCatalogByName(final String catalogName) {

        if (catalogName == null) {
            throw new NullPointerException("catalogName");
        }

        for (final Catalog catalog : getCatalogs()) {
            if (catalogName.endsWith(catalog.getTableCat())) {
                return catalog;
            }
        }

        return null;
    }


    // -------------------------------------------------------- clientProperties
    public List<ClientInfoProperty> getClientInfoProperties() {

        if (clientInfoProperties == null) {
            clientInfoProperties = new ArrayList<ClientInfoProperty>();
        }

        return clientInfoProperties;
    }


    // -------------------------------------------------------------- tableTypes
    public List<TableType> getTableTypes() {

        if (tableTypes == null) {
            tableTypes = new ArrayList<TableType>();
        }

        return tableTypes;
    }


    // ---------------------------------------------------------------- typeInfo
    public List<TypeInfo> getTypeInfo() {

        if (typeInfo == null) {
            typeInfo = new ArrayList<TypeInfo>();
        }

        return typeInfo;
    }


    @XmlAttribute
    @XmlList
    private Set<String> suppressionPaths;


    @XmlElement(required = true)
    boolean allProceduresAreCallable;


    @XmlElement(required = true)
    boolean allTablesAreSelectable;


    @XmlElement(nillable = true, required = true)
    private String catalogSeparator;


    @XmlElement(nillable = true, required = true)
    private String catalogTerm;


    @XmlElement(required = true)
    private int databaseMajorVersion;


    @XmlElement(required = true)
    private int databaseMinorVersion;


    @XmlElement(nillable = true, required = true)
    private String databaseProductName;


    @XmlElement(nillable = true, required = true)
    private String databaseProductVersion;


    @XmlElement(required = true)
    private int driverMajorVersion;


    @XmlElement(required = true)
    private int driverMinorVersion;


    @XmlElement(nillable = true, required = true)
    private String driverName;


    @XmlElement(nillable = true, required = true)
    private String driverVersion;


    @XmlElement(nillable = true, required = true)
    private String numericFunctions;


    @XmlElement(nillable = true, required = true)
    private String stringFunctions;


    @XmlElement(nillable = true, required = true)
    private String systemFunctions;


    @XmlElement(nillable = true, required = true)
    private String timeDateFunctions;


    @XmlElement(name = "catalog")
    private List<Catalog> catalogs;


    @XmlElement(name = "clientInfoProperty")
    private List<ClientInfoProperty> clientInfoProperties;


    @XmlElement(name = "tableType")
    private List<TableType> tableTypes;


    @XmlElement(name = "typeInfo")
    private List<TypeInfo> typeInfo;


}

