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


package com.github.jinahya.sql.database.meta.data.bind;


import java.io.PrintStream;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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


    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(Metadata.class);


    public static final String SUPPRESSION_PATH_CATALOGS = "metadata/catalogs";


    public static final String SUPPRESSION_PATH_CLIENT_INFO_PROPERTIES
        = "metadata/clientProperties";


    public static final String SUPPRESSION_PATH_TYPE_INFO = "metadata/typeInfo";


    public static final String SUPPRESSION_PATH_TABLE_TYPES
        = "metadata/tabletypes";


    public static Metadata newInstance(final DatabaseMetaData database,
                                       final Suppression suppression)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        final Metadata instance = new Metadata();

        instance.setAllProceduresAreCallable(
            database.allProceduresAreCallable());
        instance.setAllTablesAreSelectable(database.allTablesAreSelectable());

        instance.setCatalogSeparator(database.getCatalogSeparator());
        instance.setCatalogTerm(database.getCatalogTerm());

        instance.setDatabaseMajorVersion(database.getDatabaseMajorVersion());
        instance.setDatabaseMinorVersion(database.getDatabaseMinorVersion());
        instance.setDatabaseProductName(database.getDatabaseProductName());
        instance.setDatabaseProductVersion(database.getDatabaseProductName());

        instance.setDriverMajorVersion(database.getDriverMajorVersion());
        instance.setDriverMinorVersion(database.getDriverMinorVersion());
        instance.setDriverName(database.getDriverName());
        instance.setDriverVersion(database.getDriverVersion());

        for (final String numericFunction
             : database.getNumericFunctions().split(",")) {
            instance.getNumericFunctions().add(numericFunction);
        }

        for (final String stringFunction
             : database.getStringFunctions().split(",")) {
            instance.getStringFunctions().add(stringFunction);
        }

        for (final String systemFunction
             : database.getSystemFunctions().split(",")) {
            instance.getSystemFunctions().add(systemFunction);
        }

        for (final String timeDateFunction
             : database.getTimeDateFunctions().split(",")) {
            instance.getTimeDateFunctions().add(timeDateFunction);
        }

        Catalog.retrieve(database, suppression, instance);

        ClientInfoProperty.retrieve(database, suppression, instance);

        TypeInfo.retrieve(database, suppression, instance);

        TableType.retrieve(database, suppression, instance);

        return instance;
    }


    /**
     * Creates a new instance.
     */
    public Metadata() {

        super();
    }


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


    public List<String> getNumericFunctions() {

        if (numericFunctions == null) {
            numericFunctions = new ArrayList<String>();
        }

        return numericFunctions;
    }


    public List<String> getStringFunctions() {

        if (stringFunctions == null) {
            stringFunctions = new ArrayList<String>();
        }

        return stringFunctions;
    }


    public List<String> getSystemFunctions() {

        if (systemFunctions == null) {
            systemFunctions = new ArrayList<String>();
        }

        return systemFunctions;
    }


    public List<String> getTimeDateFunctions() {

        if (timeDateFunctions == null) {
            timeDateFunctions = new ArrayList<String>();
        }

        return timeDateFunctions;
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


    @XmlElement(required = true)
    private boolean allProceduresAreCallable;


    @XmlElement(required = true)
    private boolean allTablesAreSelectable;


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


    @XmlElement(required = true)
    @XmlList
    private List<String> numericFunctions;


    @XmlElement(required = true)
    @XmlList
    private List<String> stringFunctions;


    @XmlElement(required = true)
    @XmlList
    private List<String> systemFunctions;


    @XmlElement(required = true)
    @XmlList
    private List<String> timeDateFunctions;


    @SuppressionPath(SUPPRESSION_PATH_CATALOGS)
    @XmlElement(name = "catalog")
    List<Catalog> catalogs;


    @SuppressionPath(SUPPRESSION_PATH_CLIENT_INFO_PROPERTIES)
    @XmlElement(name = "clientInfoProperty")
    List<ClientInfoProperty> clientInfoProperties;


    @SuppressionPath(SUPPRESSION_PATH_TABLE_TYPES)
    @XmlElement(name = "tableType")
    List<TableType> tableTypes;


    @SuppressionPath(SUPPRESSION_PATH_TYPE_INFO)
    @XmlElement(name = "typeInfo")
    List<TypeInfo> typeInfo;


}

