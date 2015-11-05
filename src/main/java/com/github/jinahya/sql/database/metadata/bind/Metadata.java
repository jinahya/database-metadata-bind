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
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "allProceduresAreCallable", "allTablesAreSelectable",
        "autoCommitFailureClosesAllResultSets",
        "catalogSeparator", "catalogTerm", "catalogs", "clientInfoProperties",
        "connectionString",
        "dataDefinitionCausesTransactionCommit",
        "dataDefinitionIgnoredInTransactions", "databaseMajorVersion",
        "databaseMinorVersion", "databaseProductName", "databaseProductVersion",
        "defaultTransactionIsolation", "deletesAreDetected",
        "doesMaxRowSizeIncludeBlobs", "driverMajorVersion",
        "driverMinorVersion", "driverName", "driverVersion",
        "extraNameCharacters",
        "generatedKeyAlwaysReturned",
        "identifierQuoteString", "insertsAreDetected",
        "jdbcMajorVersion", "jdbcMinorVersion",
        "maxBinaryLiteralLength", "maxCatalogNameLength",
        "maxCharLiteralLength", "maxColumnNameLength", "maxColumnsInGroupBy",
        "maxColumnsInIndex", "maxColumnsInOrderBy", "maxColumnsInSelect",
        "maxColumnsInTable", "maxConnections", "maxCursorNameLength",
        "maxIndexLength", "maxLogicalLobSize", "maxProcedureNameLength",
        "maxRowSize", "maxSchemaNameLength", "maxStatementLength",
        "maxStatements", "maxTableNameLength", "maxTablesInSelect",
        "maxUserNameLength",
        "numericFunctions",
        "procedureTerm",
        "resultSetHoldability",
        //"rowIdLifetime",
        "schemaNames", "stringFunctions", "systemFunctions",
        "tableTypes", "timeDateFunctions", "typeInfo",
        "url", "userName"}
)
public class Metadata {


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


    // ------------------------------------ autoCommitFailureClosesAllResultSets
    public boolean getAutoCommitFailureClosesAllResultSets() {
        return autoCommitFailureClosesAllResultSets;
    }


    public void setAutoCommitFailureClosesAllResultSets(
        final boolean autoCommitFailureClosesAllResultSets) {

        this.autoCommitFailureClosesAllResultSets
            = autoCommitFailureClosesAllResultSets;
    }


    // -------------------------------------------------------- catalogSeparator
    public String getCatalogSeparator() {

        return catalogSeparator;
    }


    public void setCatalogSeparator(final String catalogSeparator) {

        this.catalogSeparator = catalogSeparator;
    }


    // ------------------------------------------------------------- catalogTerm
    public String getCatalogTerm() {

        return catalogTerm;
    }


    public void setCatalogTerm(final String catalogTerm) {

        this.catalogTerm = catalogTerm;
    }


    // ---------------------------------------------------------------- catalogs
    public List<Catalog> getCatalogs() {

        if (catalogs == null) {
            catalogs = new ArrayList<Catalog>();
        }

        return catalogs;
    }


    // -------------------------------------------------------- clientProperties
    public List<ClientInfoProperty> getClientInfoProperties() {

        if (clientInfoProperties == null) {
            clientInfoProperties = new ArrayList<ClientInfoProperty>();
        }

        return clientInfoProperties;
    }


    // -------------------------------------------------------- connectionString
    public String getConnectionString() {

        return connectionString;
    }


    public void setConnectionString(final String connectionString) {

        this.connectionString = connectionString;
    }


    // ----------------------------------- dataDefinitionCausesTransactionCommit
    public boolean getDataDefinitionCausesTransactionCommit() {

        return dataDefinitionCausesTransactionCommit;
    }


    public void setDataDefinitionCausesTransactionCommit(
        final boolean dataDefinitionCausesTransactionCommit) {

        this.dataDefinitionCausesTransactionCommit
            = dataDefinitionCausesTransactionCommit;
    }


    // -------------------------------------- dataDefinitionIgnoredInTransaction
    public boolean getDataDefinitionIgnoredInTransactions() {

        return dataDefinitionIgnoredInTransactions;
    }


    public void setDataDefinitionIgnoredInTransactions(
        final boolean dataDefinitionIgnoredInTransactions) {

        this.dataDefinitionIgnoredInTransactions
            = dataDefinitionIgnoredInTransactions;
    }


    // ---------------------------------------------------- databaseMajorVersion
    public int getDatabaseMajorVersion() {

        return databaseMajorVersion;
    }


    public void setDatabaseMajorVersion(final int databaseMajorVersion) {

        this.databaseMajorVersion = databaseMajorVersion;
    }


    // ---------------------------------------------------- databaseMinorVersion
    public int getDatabaseMinorVersion() {

        return databaseMinorVersion;
    }


    public void setDatabaseMinorVersion(final int databaseMinorVersion) {

        this.databaseMinorVersion = databaseMinorVersion;
    }


    // ----------------------------------------------------- databaseProductName
    public String getDatabaseProductName() {

        return databaseProductName;
    }


    public void setDatabaseProductName(final String databaseProductName) {

        this.databaseProductName = databaseProductName;
    }


    // -------------------------------------------------- databaseProductVersion
    public String getDatabaseProductVersion() {

        return databaseProductVersion;
    }


    public void setDatabaseProductVersion(final String databaseProductVersion) {

        this.databaseProductVersion = databaseProductVersion;
    }


    // --------------------------------------------- defaultTransactionIsolation
    public int getDefaultTransactionIsolation() {

        return defaultTransactionIsolation;
    }


    public void setDefaultTransactionIsolation(
        final int defaultTransactionIsolation) {

        this.defaultTransactionIsolation = defaultTransactionIsolation;
    }


    // ------------------------------------------------------ deletesAreDetected
    public List<DeletesAreDetected> getDeletesAreDetected() {

        if (deletesAreDetected == null) {
            deletesAreDetected = new ArrayList<DeletesAreDetected>();
        }

        return deletesAreDetected;
    }


    // ---------------------------------------------- doesMaxRowSizeIncludeBlobs
    public boolean getDoesMaxRowSizeIncludeBlobs() {

        return doesMaxRowSizeIncludeBlobs;
    }


    public void setDoesMaxRowSizeIncludeBlobs(
        final boolean doesMaxRowSizeIncludeBlobs) {

        this.doesMaxRowSizeIncludeBlobs = doesMaxRowSizeIncludeBlobs;
    }


    // ------------------------------------------------------ driverMajorVersion
    public int getDriverMajorVersion() {

        return driverMajorVersion;
    }


    public void setDriverMajorVersion(final int driverMajorVersion) {

        this.driverMajorVersion = driverMajorVersion;
    }


    // ------------------------------------------------------ driverMinorVersion
    public int getDriverMinorVersion() {

        return driverMinorVersion;
    }


    public void setDriverMinorVersion(final int driverMinorVersion) {

        this.driverMinorVersion = driverMinorVersion;
    }


    // -------------------------------------------------------------- driverName
    public String getDriverName() {

        return driverName;
    }


    public void setDriverName(final String driverName) {

        this.driverName = driverName;
    }


    // ----------------------------------------------------------- driverVersion
    public String getDriverVersion() {

        return driverVersion;
    }


    public void setDriverVersion(final String driverVersion) {

        this.driverVersion = driverVersion;
    }


    // ----------------------------------------------------- extraNameCharacters
    public String getExtraNameCharacters() {

        return extraNameCharacters;
    }


    public void setExtraNameCharacters(final String extraNameCharacters) {

        this.extraNameCharacters = extraNameCharacters;
    }


    // ---------------------------------------------- generatedKeyAlwaysReturned
    public boolean getGeneratedKeyAlwaysReturned() {

        return generatedKeyAlwaysReturned;
    }


    public void setGeneratedKeyAlwaysReturned(
        final boolean generatedKeyAlwaysReturned) {

        this.generatedKeyAlwaysReturned = generatedKeyAlwaysReturned;
    }


    // --------------------------------------------------- identifierQuoteString
    public String getIdentifierQuoteString() {

        return identifierQuoteString;
    }


    public void setIdentifierQuoteString(final String identifierQuoteString) {

        this.identifierQuoteString = identifierQuoteString;
    }


    // ------------------------------------------------------ insertsAreDetected
    public List<InsertsAreDetected> getInsertsAreDetected() {

        if (insertsAreDetected == null) {
            insertsAreDetected = new ArrayList<InsertsAreDetected>();
        }

        return insertsAreDetected;
    }


    // -------------------------------------------------------- jdbcMajorVersion
    public int getJdbcMajorVersion() {

        return jdbcMajorVersion;
    }


    public void setJdbcMajorVersion(final int jdbcMajorVersion) {

        this.jdbcMajorVersion = jdbcMajorVersion;
    }


    // -------------------------------------------------------- jdbcMinorVersion
    public int getJdbcMinorVersion() {

        return jdbcMinorVersion;
    }


    public void setJdbcMinorVersion(final int jdbcMinorVersion) {

        this.jdbcMinorVersion = jdbcMinorVersion;
    }


    public int getMaxBinaryLiteralLength() {
        return maxBinaryLiteralLength;
    }


    public void setMaxBinaryLiteralLength(int maxBinaryLiteralLength) {
        this.maxBinaryLiteralLength = maxBinaryLiteralLength;
    }


    public int getMaxCatalogNameLength() {
        return maxCatalogNameLength;
    }


    public void setMaxCatalogNameLength(int maxCatalogNameLength) {
        this.maxCatalogNameLength = maxCatalogNameLength;
    }


    // ---------------------------------------------------- maxCharLiteralLength
    public int getMaxCharLiteralLength() {

        return maxCharLiteralLength;
    }


    public void setMaxCharLiteralLength(final int maxCharLiteralLength) {

        this.maxCharLiteralLength = maxCharLiteralLength;
    }


    public int getMaxColumnNameLength() {
        return maxColumnNameLength;
    }


    public void setMaxColumnNameLength(int maxColumnNameLength) {
        this.maxColumnNameLength = maxColumnNameLength;
    }


    public int getMaxColumnsInGroupBy() {
        return maxColumnsInGroupBy;
    }


    public void setMaxColumnsInGroupBy(int maxColumnsInGroupBy) {
        this.maxColumnsInGroupBy = maxColumnsInGroupBy;
    }


    public int getMaxColumnsInIndex() {
        return maxColumnsInIndex;
    }


    public void setMaxColumnsInIndex(int maxColumnsInIndex) {
        this.maxColumnsInIndex = maxColumnsInIndex;
    }


    public int getMaxColumnsInOrderBy() {
        return maxColumnsInOrderBy;
    }


    public void setMaxColumnsInOrderBy(int maxColumnsInOrderBy) {
        this.maxColumnsInOrderBy = maxColumnsInOrderBy;
    }


    public int getMaxColumnsInSelect() {
        return maxColumnsInSelect;
    }


    public void setMaxColumnsInSelect(int maxColumnsInSelect) {
        this.maxColumnsInSelect = maxColumnsInSelect;
    }


    public int getMaxColumnsInTable() {
        return maxColumnsInTable;
    }


    public void setMaxColumnsInTable(int maxColumnsInTable) {
        this.maxColumnsInTable = maxColumnsInTable;
    }


    public int getMaxConnections() {
        return maxConnections;
    }


    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }


    public int getMaxCursorNameLength() {
        return maxCursorNameLength;
    }


    public void setMaxCursorNameLength(int maxCursorNameLength) {
        this.maxCursorNameLength = maxCursorNameLength;
    }


    public int getMaxIndexLength() {
        return maxIndexLength;
    }


    public void setMaxIndexLength(int maxIndexLength) {
        this.maxIndexLength = maxIndexLength;
    }


    public long getMaxLogicalLobSize() {
        return maxLogicalLobSize;
    }


    public void setMaxLogicalLobSize(long maxLogicalLobSize) {
        this.maxLogicalLobSize = maxLogicalLobSize;
    }


    public int getMaxProcedureNameLength() {
        return maxProcedureNameLength;
    }


    public void setMaxProcedureNameLength(int maxProcedureNameLength) {
        this.maxProcedureNameLength = maxProcedureNameLength;
    }


    public int getMaxRowSize() {
        return maxRowSize;
    }


    public void setMaxRowSize(int maxRowSize) {
        this.maxRowSize = maxRowSize;
    }


    public int getMaxSchemaNameLength() {
        return maxSchemaNameLength;
    }


    public void setMaxSchemaNameLength(int maxSchemaNameLength) {
        this.maxSchemaNameLength = maxSchemaNameLength;
    }


    public int getMaxStatementLength() {
        return maxStatementLength;
    }


    public void setMaxStatementLength(int maxStatementLength) {
        this.maxStatementLength = maxStatementLength;
    }


    public int getMaxStatements() {
        return maxStatements;
    }


    public void setMaxStatements(int maxStatements) {
        this.maxStatements = maxStatements;
    }


    public int getMaxTableNameLength() {
        return maxTableNameLength;
    }


    public void setMaxTableNameLength(int maxTableNameLength) {
        this.maxTableNameLength = maxTableNameLength;
    }


    public int getMaxTablesInSelect() {
        return maxTablesInSelect;
    }


    public void setMaxTablesInSelect(int maxTablesInSelect) {
        this.maxTablesInSelect = maxTablesInSelect;
    }


    public int getMaxUserNameLength() {
        return maxUserNameLength;
    }


    public void setMaxUserNameLength(int maxUserNameLength) {
        this.maxUserNameLength = maxUserNameLength;
    }


    // -------------------------------------------------------- numericFunctions
    public String getNumericFunctions() {

        return numericFunctions;
    }


    public void setNumericFunctions(final String numericFunctions) {

        this.numericFunctions = numericFunctions;
    }


    // ----------------------------------------------------------- procedureTerm
    public String getProcedureTerm() {

        return procedureTerm;
    }


    public void setProcedureTerm(final String procedureTerm) {

        this.procedureTerm = procedureTerm;
    }


    // ---------------------------------------------------- resultSetHoldability
    public int getResultSetHoldability() {

        return resultSetHoldability;
    }


    public void setResultSetHoldability(final int resultSetHoldability) {

        this.resultSetHoldability = resultSetHoldability;
    }


//    // ----------------------------------------------------------- rowIdLifetime
//    public RowIdLifetime getRowIdLifetime() {
//
//        return rowIdLifetime;
//    }
//
//
//    public void setRowIdLifetime(final RowIdLifetime rowIdLifetime) {
//
//        this.rowIdLifetime = rowIdLifetime;
//    }
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


    // ------------------------------------------------------------- schemaNames
    public List<SchemaName> getSchemaNames() {

        if (schemaNames == null) {
            schemaNames = new ArrayList<SchemaName>();
        }

        return schemaNames;
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


    // --------------------------------------------------------------------- url
    public String getUrl() {

        return url;
    }


    public void setUrl(final String url) {

        this.url = url;
    }


    // ---------------------------------------------------------------- userName
    public String getUserName() {

        return userName;
    }


    public void setUserName(final String userName) {

        this.userName = userName;
    }


    @XmlAttribute
    @XmlList
    private Set<String> suppressionPaths;


    @XmlElement(required = true)
    boolean allProceduresAreCallable;


    @XmlElement(required = true)
    boolean allTablesAreSelectable;


    @XmlElement(required = true)
    private boolean autoCommitFailureClosesAllResultSets;


    @XmlElement(nillable = true, required = true)
    private String catalogSeparator;


    @XmlElement(nillable = true, required = true)
    private String catalogTerm;


    @XmlElementRef
    private List<Catalog> catalogs;


    @XmlElementRef
    private List<ClientInfoProperty> clientInfoProperties;


    @XmlElement(required = true)
    private String connectionString;


    @XmlElement(required = true)
    private boolean dataDefinitionCausesTransactionCommit;


    @XmlElement(required = true)
    private boolean dataDefinitionIgnoredInTransactions;


    @XmlElement(required = true)
    private int databaseMajorVersion;


    @XmlElement(required = true)
    private int databaseMinorVersion;


    @XmlElement(nillable = true, required = true)
    private String databaseProductName;


    @XmlElement(nillable = true, required = true)
    private String databaseProductVersion;


    @MethodInvocation(
        name = "getDefaultTransactionIsolation"
    )
    @XmlElement(required = true)
    private int defaultTransactionIsolation;


    @MethodInvocation(
        name = "deletesAreDetected",
        types = {Integer.class},
        args = {
            @MethodInvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @MethodInvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @MethodInvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElementRef
    private List<DeletesAreDetected> deletesAreDetected;


    @XmlElement(required = true)
    private boolean doesMaxRowSizeIncludeBlobs;


    @XmlElement(required = true)
    private int driverMajorVersion;


    @XmlElement(required = true)
    private int driverMinorVersion;


    @XmlElement(nillable = true, required = true)
    private String driverName;


    @XmlElement(required = true)
    private String driverVersion;


    @XmlElement(required = true)
    private String extraNameCharacters;


    @XmlElement(required = true)
    private boolean generatedKeyAlwaysReturned;


    @XmlElement(required = true)
    private String identifierQuoteString;


    @MethodInvocation(
        name = "insertsAreDetected",
        types = {Integer.class},
        args = {
            @MethodInvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @MethodInvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @MethodInvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElementRef
    private List<InsertsAreDetected> insertsAreDetected;


    @MethodInvocation(name = "getJDBCMajorVersion")
    @XmlElement(required = true)
    private int jdbcMajorVersion;


    @MethodInvocation(name = "getJDBCMinorVersion")
    @XmlElement(required = true)
    private int jdbcMinorVersion;


    @MethodInvocation(name = "getMaxBinaryLiteralLength")
    @XmlElement(required = true)
    private int maxBinaryLiteralLength;


    @MethodInvocation(name = "getMaxCatalogNameLength")
    @XmlElement(required = true)
    private int maxCatalogNameLength;


    @MethodInvocation(name = "getMaxCharLiteralLength")
    @XmlElement(required = true)
    private int maxCharLiteralLength;


    @MethodInvocation(name = "getMaxColumnNameLength")
    @XmlElement(required = true)
    private int maxColumnNameLength;


    @XmlElement(required = true)
    private int maxColumnsInGroupBy;


    @XmlElement(required = true)
    private int maxColumnsInIndex;


    @XmlElement(required = true)
    private int maxColumnsInOrderBy;


    @XmlElement(required = true)
    private int maxColumnsInSelect;


    @XmlElement(required = true)
    private int maxColumnsInTable;


    @XmlElement(required = true)
    private int maxConnections;


    @XmlElement(required = true)
    private int maxCursorNameLength;


    @XmlElement(required = true)
    private int maxIndexLength;


    @XmlElement(required = true)
    private long maxLogicalLobSize;


    @XmlElement(required = true)
    private int maxProcedureNameLength;


    @XmlElement(required = true)
    private int maxRowSize;


    @XmlElement(required = true)
    private int maxSchemaNameLength;


    @XmlElement(required = true)
    private int maxStatementLength;


    @XmlElement(required = true)
    private int maxStatements;


    @XmlElement(required = true)
    private int maxTableNameLength;


    @XmlElement(required = true)
    private int maxTablesInSelect;


    @XmlElement(required = true)
    private int maxUserNameLength;


    @XmlElement(nillable = true, required = true)
    private String numericFunctions;


    @XmlElement(required = true)
    private String procedureTerm;


    @XmlElement(required = true)
    private int resultSetHoldability;


//    @XmlElement(required = true)
//    private RowIdLifetime rowIdLifetime;
    @XmlElement(nillable = true, required = true)
    private String stringFunctions;


    @XmlElement(nillable = true, required = true)
    private String systemFunctions;


    @XmlElement(nillable = true, required = true)
    private String timeDateFunctions;


    @XmlElementRef
    private List<SchemaName> schemaNames;


    @XmlElementRef//(name = "tableType")
    private List<TableType> tableTypes;


    @XmlElementRef//(name = "typeInfo")
    private List<TypeInfo> typeInfo;


    @XmlElement(required = true)
    private String userName;


    @XmlElement(required = true)
    private String url;


}

