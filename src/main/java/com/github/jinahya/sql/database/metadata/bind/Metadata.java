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
import javax.xml.bind.annotation.XmlTransient;
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
        "catalogs", "catalogSeparator", "catalogTerm", "clientInfoProperties",
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
        "resultSetHoldability", "rowIdLifetimeName",
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


//    // -------------------------------------------------------- suppressionPaths
//    public Set<String> getSuppressionPaths() {
//
//        if (suppressionPaths == null) {
//            suppressionPaths = new TreeSet<String>();
//        }
//
//        return suppressionPaths;
//    }
    // ------------------------------------------------ allProceduresAreCallable
    public boolean getAllProceduresAreCallable() {

        return allProceduresAreCallable;
    }


    public void setAllProceduresAreCallable(
        final boolean allProceduresAreCallable) {

        this.allProceduresAreCallable = allProceduresAreCallable;
    }


    // -------------------------------------------------- allTablesAreSelectable
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


    // ---------------------------------------------------------------- catalogs
    public List<Catalog> getCatalogs() {

        if (catalogs == null) {
            catalogs = new ArrayList<Catalog>();
        }

        return catalogs;
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


    // -------------------------------------------------------- clientProperties
    public List<ClientInfoProperty> getClientInfoProperties() {

        if (clientInfoProperties == null) {
            clientInfoProperties = new ArrayList<ClientInfoProperty>();
        }

        return clientInfoProperties;
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


    // ----------------------------------------------------------- rowIdLifetime
    public RowIdLifetime getRowIdLifetime() {

        return rowIdLifetime;
    }


    public void setRowIdLifetime(final RowIdLifetime rowIdLifetime) {

        this.rowIdLifetime = rowIdLifetime;
    }


    @XmlElement(required = true)
    public String getRowIdLifetimeName() {

        return rowIdLifetime == null ? null : rowIdLifetime.name();
    }


    public void setRowIdLifetimeName(final String rowIdLifetimeName) {

        rowIdLifetime = rowIdLifetimeName == null
                        ? null : RowIdLifetime.valueOf(rowIdLifetimeName);
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


//    @XmlAttribute
//    @XmlList
//    private Set<String> suppressionPaths;
    @Invocation(name = "allProceduresAreCallable")
    @XmlElement(required = true)
    private boolean allProceduresAreCallable;


    @Invocation(name = "allTablesAreSelectable")
    @XmlElement(required = true)
    private boolean allTablesAreSelectable;


    @Invocation(name = "autoCommitFailureClosesAllResultSets")
    @XmlElement(required = true)
    private boolean autoCommitFailureClosesAllResultSets;


    @Invocation(name = "getCatalogs")
    @XmlElementRef
    private List<Catalog> catalogs;


    @Invocation(name = "getCatalogSeparator")
    @XmlElement(nillable = true, required = true)
    private String catalogSeparator;


    @Invocation(name = "getCatalogTerm")
    @XmlElement(nillable = true, required = true)
    private String catalogTerm;


    @Invocation(name = "getClientInfoProperties")
    @XmlElementRef
    private List<ClientInfoProperty> clientInfoProperties;


    @Invocation(name = "dataDefinitionCausesTransactionCommit")
    @XmlElement(required = true)
    private boolean dataDefinitionCausesTransactionCommit;


    @Invocation(name = "dataDefinitionIgnoredInTransactions")
    @XmlElement(required = true)
    private boolean dataDefinitionIgnoredInTransactions;


    @Invocation(name = "getDatabaseMajorVersion")
    @XmlElement(required = true)
    private int databaseMajorVersion;


    @Invocation(name = "getDatabaseMinorVersion")
    @XmlElement(required = true)
    private int databaseMinorVersion;


    @Invocation(name = "getDatabaseProductName")
    @XmlElement(nillable = true, required = true)
    private String databaseProductName;


    @Invocation(name = "getDatabaseProductVersion")
    @XmlElement(nillable = true, required = true)
    private String databaseProductVersion;


    @Invocation(
        name = "getDefaultTransactionIsolation"
    )
    @XmlElement(required = true)
    private int defaultTransactionIsolation;


    @Invocation(
        name = "deletesAreDetected",
        types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElementRef
    private List<DeletesAreDetected> deletesAreDetected;


    @Invocation(name = "doesMaxRowSizeIncludeBlobs")
    @XmlElement(required = true)
    private boolean doesMaxRowSizeIncludeBlobs;


    @Invocation(name = "getDriverMajorVersion")
    @XmlElement(required = true)
    private int driverMajorVersion;


    @Invocation(name = "getDriverMinorVersion")
    @XmlElement(required = true)
    private int driverMinorVersion;


    @Invocation(name = "getDriverName")
    @XmlElement(nillable = true, required = true)
    private String driverName;


    @Invocation(name = "getDriverVersion")
    @XmlElement(required = true)
    private String driverVersion;


    @Invocation(name = "getExtraNameCharacters")
    @XmlElement(required = true)
    private String extraNameCharacters;


    @Invocation(name = "generatedKeyAlwaysReturned")
    @XmlElement(required = true)
    private boolean generatedKeyAlwaysReturned;


    @Invocation(name = "getIdentifierQuoteString")
    @XmlElement(required = true)
    private String identifierQuoteString;


    @Invocation(
        name = "insertsAreDetected",
        types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElementRef
    private List<InsertsAreDetected> insertsAreDetected;


    @Invocation(name = "getJDBCMajorVersion")
    @XmlElement(required = true)
    private int jdbcMajorVersion;


    @Invocation(name = "getJDBCMinorVersion")
    @XmlElement(required = true)
    private int jdbcMinorVersion;


    @Invocation(name = "getMaxBinaryLiteralLength")
    @XmlElement(required = true)
    private int maxBinaryLiteralLength;


    @Invocation(name = "getMaxCatalogNameLength")
    @XmlElement(required = true)
    private int maxCatalogNameLength;


    @Invocation(name = "getMaxCharLiteralLength")
    @XmlElement(required = true)
    private int maxCharLiteralLength;


    @Invocation(name = "getMaxColumnNameLength")
    @XmlElement(required = true)
    private int maxColumnNameLength;


    @Invocation(name = "getMaxColumnsInGroupBy")
    @XmlElement(required = true)
    private int maxColumnsInGroupBy;


    @Invocation(name = "getMaxColumnsInIndex")
    @XmlElement(required = true)
    private int maxColumnsInIndex;


    @Invocation(name = "getMaxColumnsInOrderBy")
    @XmlElement(required = true)
    private int maxColumnsInOrderBy;


    @Invocation(name = "getMaxColumnsInSelect")
    @XmlElement(required = true)
    private int maxColumnsInSelect;


    @Invocation(name = "getMaxColumnsInTable")
    @XmlElement(required = true)
    private int maxColumnsInTable;


    @Invocation(name = "getMaxConnections")
    @XmlElement(required = true)
    private int maxConnections;


    @Invocation(name = "getMaxCursorNameLength")
    @XmlElement(required = true)
    private int maxCursorNameLength;


    @Invocation(name = "getMaxIndexLength")
    @XmlElement(required = true)
    private int maxIndexLength;


    @Invocation(name = "getMaxLogicalLobSize")
    @XmlElement(required = true)
    private long maxLogicalLobSize;


    @Invocation(name = "getMaxProcedureNameLength")
    @XmlElement(required = true)
    private int maxProcedureNameLength;


    @Invocation(name = "getMaxRowSize")
    @XmlElement(required = true)
    private int maxRowSize;


    @Invocation(name = "getMaxSchemaNameLength")
    @XmlElement(required = true)
    private int maxSchemaNameLength;


    @Invocation(name = "getMaxStatementLength")
    @XmlElement(required = true)
    private int maxStatementLength;


    @Invocation(name = "getMaxStatements")
    @XmlElement(required = true)
    private int maxStatements;


    @Invocation(name = "getMaxTableNameLength")
    @XmlElement(required = true)
    private int maxTableNameLength;


    @Invocation(name = "getMaxTablesInSelect")
    @XmlElement(required = true)
    private int maxTablesInSelect;


    @Invocation(name = "getMaxUserNameLength")
    @XmlElement(required = true)
    private int maxUserNameLength;


    @Invocation(name = "getNumericFunctions")
    @XmlElement(required = true)
    private String numericFunctions;


    @Invocation(name = "getProcedureTerm")
    @XmlElement(required = true)
    private String procedureTerm;


    @Invocation(name = "getResultSetHoldability")
    @XmlElement(required = true)
    private int resultSetHoldability;


    @Invocation(name = "getRowIdLifetime")
    @XmlTransient
    private RowIdLifetime rowIdLifetime;


    @Invocation(name = "getStringFunctions")
    @XmlElement(nillable = true, required = true)
    private String stringFunctions;


    @Invocation(name = "getSystemFunctions")
    @XmlElement(nillable = true, required = true)
    private String systemFunctions;


    @Invocation(name = "getTimeDateFunctions")
    @XmlElement(nillable = true, required = true)
    private String timeDateFunctions;


    @Invocation(name = "getSchemas")
    @XmlElementRef
    private List<SchemaName> schemaNames;


    @Invocation(name = "getTableTypes")
    @XmlElementRef
    private List<TableType> tableTypes;


    @Invocation(name = "getTypeInfo")
    @XmlElementRef
    private List<TypeInfo> typeInfo;


    @Invocation(name = "getUserName")
    @XmlElement(required = true)
    private String userName;


    @Invocation(name = "getURL")
    @XmlElement(required = true)
    private String url;


}

