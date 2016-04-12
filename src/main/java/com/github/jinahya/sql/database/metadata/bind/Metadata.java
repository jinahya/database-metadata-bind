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

import java.sql.Connection;
import java.sql.RowIdLifetime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * An entity class for holding information from
 * {@link java.sql.DatabaseMetaData}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public class Metadata implements TableDomain {

    @Override
    public List<Table> getTables() {
        final List<Table> tables = new ArrayList<Table>();
        for (final Catalog catalog : getCatalogs()) {
            tables.addAll(catalog.getTables());
        }
        return tables;
    }

    // ------------------------------------------------ allProceduresAreCallable
    public boolean isAllProceduresAreCallable() {
        return allProceduresAreCallable;
    }

    public void setAllProceduresAreCallable(
            final boolean allProceduresAreCallable) {
        this.allProceduresAreCallable = allProceduresAreCallable;
    }

    // -------------------------------------------------- allTablesAreSelectable
    public boolean isAllTablesAreSelectable() {
        return allTablesAreSelectable;
    }

    public void setAllTablesAreSelectable(
            final boolean allTablesAreSelectable) {
        this.allTablesAreSelectable = allTablesAreSelectable;
    }

    // ------------------------------------ autoCommitFailureClosesAllResultSets
    public boolean isAutoCommitFailureClosesAllResultSets() {
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

    @Deprecated
    public void setCatalogs(final List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    // ---------------------------------------------------------- catalogAtStart
    public boolean isCatalogAtStart() {
        return catalogAtStart;
    }

    public void setCatalogAtStart(boolean catalogAtStart) {
        this.catalogAtStart = catalogAtStart;
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

    // ---------------------------------------------------- clientInfoProperties
    public List<ClientInfoProperty> getClientInfoProperties() {
        if (clientInfoProperties == null) {
            clientInfoProperties = new ArrayList<ClientInfoProperty>();
        }
        return clientInfoProperties;
    }

    @Deprecated
    public void setClientInfoProperties(
            final List<ClientInfoProperty> clientInfoProperties) {
        this.clientInfoProperties = clientInfoProperties;
    }

    // -------------------------------------------------------------- connection
    @Deprecated
    public Connection getConnection() {
        return connection;
    }

    @Deprecated
    public void setConnection(final Connection connection) {
        this.connection = connection;
    }

    // --------------------------------------------------------- crossReferences
    @Override
    public List<CrossReference> getCrossReferences() {
        if (crossReferences == null) {
            crossReferences = new ArrayList<CrossReference>();
        }
        return crossReferences;
    }

//    @Override
//    public void setCrossReferences(List<CrossReference> crossReferences) {
//        this.crossReferences = crossReferences;
//    }

    // ----------------------------------- dataDefinitionCausesTransactionCommit
    public boolean isDataDefinitionCausesTransactionCommit() {
        return dataDefinitionCausesTransactionCommit;
    }

    public void setDataDefinitionCausesTransactionCommit(
            final boolean dataDefinitionCausesTransactionCommit) {
        this.dataDefinitionCausesTransactionCommit
                = dataDefinitionCausesTransactionCommit;
    }

    public boolean isDataDefinitionIgnoredInTransactions() {
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

    public void setDatabaseMajorVersion(int databaseMajorVersion) {
        this.databaseMajorVersion = databaseMajorVersion;
    }

    public int getDatabaseMinorVersion() {
        return databaseMinorVersion;
    }

    public void setDatabaseMinorVersion(int databaseMinorVersion) {
        this.databaseMinorVersion = databaseMinorVersion;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    public void setDatabaseProductVersion(String databaseProductVersion) {
        this.databaseProductVersion = databaseProductVersion;
    }

    public int getDefaultTransactionIsolation() {
        return defaultTransactionIsolation;
    }

    public void setDefaultTransactionIsolation(int defaultTransactionIsolation) {
        this.defaultTransactionIsolation = defaultTransactionIsolation;
    }

    public List<RSTBoolean> getDeletesAreDetected() {
        return deletesAreDetected;
    }

    public void setDeletesAreDetected(List<RSTBoolean> deletesAreDetected) {
        this.deletesAreDetected = deletesAreDetected;
    }

    public boolean isDoesMaxRowSizeIncludeBlobs() {
        return doesMaxRowSizeIncludeBlobs;
    }

    public void setDoesMaxRowSizeIncludeBlobs(
            final boolean doesMaxRowSizeIncludeBlobs) {
        this.doesMaxRowSizeIncludeBlobs = doesMaxRowSizeIncludeBlobs;
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

    public String getExtraNameCharacters() {
        return extraNameCharacters;
    }

    public void setExtraNameCharacters(final String extraNameCharacters) {
        this.extraNameCharacters = extraNameCharacters;
    }

    public boolean isGeneratedKeyAlwaysReturned() {
        return generatedKeyAlwaysReturned;
    }

    public void setGeneratedKeyAlwaysReturned(
            final boolean generatedKeyAlwaysReturned) {
        this.generatedKeyAlwaysReturned = generatedKeyAlwaysReturned;
    }

    public String getIdentifierQuoteString() {
        return identifierQuoteString;
    }

    public void setIdentifierQuoteString(final String identifierQuoteString) {
        this.identifierQuoteString = identifierQuoteString;
    }

    // ------------------------------------------------------ insertsAreDetected
    public List<RSTBoolean> getInsertsAreDetected() {
        return insertsAreDetected;
    }

    public void setInsertsAreDetected(final List<RSTBoolean> insertsAreDetected) {
        this.insertsAreDetected = insertsAreDetected;
    }

    // -------------------------------------------------------- JDBCMajorVersion
    public int getJDBCMajorVersion() {
        return JDBCMajorVersion;
    }

    public void setJDBCMajorVersion(int JDBCMajorVersion) {
        this.JDBCMajorVersion = JDBCMajorVersion;
    }

    // -------------------------------------------------------- JDBCMinorVersion
    public int getJDBCMinorVersion() {
        return JDBCMinorVersion;
    }

    public void setJDBCMinorVersion(final int JDBCMinorVersion) {
        this.JDBCMinorVersion = JDBCMinorVersion;
    }

    // ------------------------------------------------------ locatorsUpdateCopy
    public boolean isLocatorsUpdateCopy() {
        return locatorsUpdateCopy;
    }

    public void setLocatorsUpdateCopy(final boolean locatorsUpdateCopy) {
        this.locatorsUpdateCopy = locatorsUpdateCopy;
    }

    // -------------------------------------------------- maxBinaryLiteralLength
    public int getMaxBinaryLiteralLength() {
        return maxBinaryLiteralLength;
    }

    public void setMaxBinaryLiteralLength(final int maxBinaryLiteralLength) {
        this.maxBinaryLiteralLength = maxBinaryLiteralLength;
    }

    // ---------------------------------------------------- maxCatalogNameLength
    public int getMaxCatalogNameLength() {
        return maxCatalogNameLength;
    }

    public void setMaxCatalogNameLength(final int maxCatalogNameLength) {
        this.maxCatalogNameLength = maxCatalogNameLength;
    }

    // ---------------------------------------------------- maxCharLiteralLength
    public int getMaxCharLiteralLength() {
        return maxCharLiteralLength;
    }

    public void setMaxCharLiteralLength(final int maxCharLiteralLength) {
        this.maxCharLiteralLength = maxCharLiteralLength;
    }

    // ----------------------------------------------------- maxColumnNameLength
    public int getMaxColumnNameLength() {
        return maxColumnNameLength;
    }

    public void setMaxColumnNameLength(final int maxColumnNameLength) {
        this.maxColumnNameLength = maxColumnNameLength;
    }

    // ----------------------------------------------------- maxColumnsInGroupBy
    public int getMaxColumnsInGroupBy() {
        return maxColumnsInGroupBy;
    }

    public void setMaxColumnsInGroupBy(final int maxColumnsInGroupBy) {
        this.maxColumnsInGroupBy = maxColumnsInGroupBy;
    }

    // --------------------------------------------------------- maxColumnsIndex
    public int getMaxColumnsInIndex() {
        return maxColumnsInIndex;
    }

    public void setMaxColumnsInIndex(final int maxColumnsInIndex) {
        this.maxColumnsInIndex = maxColumnsInIndex;
    }

    // ----------------------------------------------------- maxColumnsInOrderBy
    public int getMaxColumnsInOrderBy() {
        return maxColumnsInOrderBy;
    }

    public void setMaxColumnsInOrderBy(final int maxColumnsInOrderBy) {
        this.maxColumnsInOrderBy = maxColumnsInOrderBy;
    }

    // ------------------------------------------------------ maxColumnsInSelect
    public int getMaxColumnsInSelect() {
        return maxColumnsInSelect;
    }

    public void setMaxColumnsInSelect(final int maxColumnsInSelect) {
        this.maxColumnsInSelect = maxColumnsInSelect;
    }

    // ------------------------------------------------------- maxColumnsInTable
    public int getMaxColumnsInTable() {
        return maxColumnsInTable;
    }

    public void setMaxColumnsInTable(final int maxColumnsInTable) {
        this.maxColumnsInTable = maxColumnsInTable;
    }

    // ---------------------------------------------------------- maxCollections
    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(final int maxConnections) {
        this.maxConnections = maxConnections;
    }

    // ----------------------------------------------------- maxCursorNameLength
    public int getMaxCursorNameLength() {
        return maxCursorNameLength;
    }

    public void setMaxCursorNameLength(final int maxCursorNameLength) {
        this.maxCursorNameLength = maxCursorNameLength;
    }

    // ---------------------------------------------------------- maxIndexlength
    public int getMaxIndexLength() {
        return maxIndexLength;
    }

    public void setMaxIndexLength(final int maxIndexLength) {
        this.maxIndexLength = maxIndexLength;
    }

    // ------------------------------------------------------- maxLogicalLobSize
    public long getMaxLogicalLobSize() {
        return maxLogicalLobSize;
    }

    public void setMaxLogicalLobSize(final long maxLogicalLobSize) {
        this.maxLogicalLobSize = maxLogicalLobSize;
    }

    // -------------------------------------------------- maxProcedureNameLength
    public int getMaxProcedureNameLength() {
        return maxProcedureNameLength;
    }

    public void setMaxProcedureNameLength(final int maxProcedureNameLength) {
        this.maxProcedureNameLength = maxProcedureNameLength;
    }

    // -------------------------------------------------------------- maxRowSize
    public int getMaxRowSize() {
        return maxRowSize;
    }

    public void setMaxRowSize(final int maxRowSize) {
        this.maxRowSize = maxRowSize;
    }

    // ----------------------------------------------------- maxSchemaNameLength
    public int getMaxSchemaNameLength() {
        return maxSchemaNameLength;
    }

    public void setMaxSchemaNameLength(final int maxSchemaNameLength) {
        this.maxSchemaNameLength = maxSchemaNameLength;
    }

    // ------------------------------------------------------ maxStatementLength
    public int getMaxStatementLength() {
        return maxStatementLength;
    }

    public void setMaxStatementLength(final int maxStatementLength) {
        this.maxStatementLength = maxStatementLength;
    }

    // ----------------------------------------------------------- maxStatements
    public int getMaxStatements() {
        return maxStatements;
    }

    public void setMaxStatements(final int maxStatements) {
        this.maxStatements = maxStatements;
    }

    // ------------------------------------------------------ maxTableNameLength
    public int getMaxTableNameLength() {
        return maxTableNameLength;
    }

    public void setMaxTableNameLength(final int maxTableNameLength) {
        this.maxTableNameLength = maxTableNameLength;
    }

    // ------------------------------------------------------- maxTablesInSelect
    public int getMaxTablesInSelect() {
        return maxTablesInSelect;
    }

    public void setMaxTablesInSelect(final int maxTablesInSelect) {
        this.maxTablesInSelect = maxTablesInSelect;
    }

    // ------------------------------------------------------- maxUserNameLength
    public int getMaxUserNameLength() {
        return maxUserNameLength;
    }

    public void setMaxUserNameLength(final int maxUserNameLength) {
        this.maxUserNameLength = maxUserNameLength;
    }

    // --------------------------------------------------- nullPlusNonNullIsNull
    public boolean isNullPlusNonNullIsNull() {
        return nullPlusNonNullIsNull;
    }

    public void setNullPlusNonNullIsNull(final boolean nullPlusNonNullIsNull) {
        this.nullPlusNonNullIsNull = nullPlusNonNullIsNull;
    }

    // ----------------------------------------------------- nullsAreSortedAtEnd
    public boolean isNullsAreSortedAtEnd() {
        return nullsAreSortedAtEnd;
    }

    public void setNullsAreSortedAtEnd(final boolean nullsAreSortedAtEnd) {
        this.nullsAreSortedAtEnd = nullsAreSortedAtEnd;
    }

    // --------------------------------------------------- nullsAreSortedAtStart
    public boolean isNullsAreSortedAtStart() {
        return nullsAreSortedAtStart;
    }

    public void setNullsAreSortedAtStart(final boolean nullsAreSortedAtStart) {
        this.nullsAreSortedAtStart = nullsAreSortedAtStart;
    }

    // ------------------------------------------------------ nullsAreSortedHigh
    public boolean isNullsAreSortedHigh() {
        return nullsAreSortedHigh;
    }

    public void setNullsAreSortedHigh(final boolean nullsAreSortedHigh) {
        this.nullsAreSortedHigh = nullsAreSortedHigh;
    }

    // ------------------------------------------------------- nullsAreSortedLow
    public boolean isNullsAreSortedLow() {
        return nullsAreSortedLow;
    }

    public void setNullsAreSortedLow(final boolean nullsAreSortedLow) {
        this.nullsAreSortedLow = nullsAreSortedLow;
    }

    // -------------------------------------------------------- numericFunctions
    public String getNumericFunctions() {
        return numericFunctions;
    }

    public void setNumericFunctions(final String numericFunctions) {
        this.numericFunctions = numericFunctions;
    }

    // ------------------------------------------------- othersDeletesAreVisible
    public List<RSTBoolean> getOthersDeletesAreVisible() {
        if (othersDeletesAreVisible == null) {
            othersDeletesAreVisible = new ArrayList<RSTBoolean>();
        }
        return othersDeletesAreVisible;
    }

    @Deprecated
    public void setOthersDeletesAreVisible(
            final List<RSTBoolean> othersDeletesAreVisible) {
        this.othersDeletesAreVisible = othersDeletesAreVisible;
    }

    // ------------------------------------------------- othersInsertsAreVisible
    public List<RSTBoolean> getOthersInsertsAreVisible() {
        if (othersInsertsAreVisible == null) {
            othersInsertsAreVisible = new ArrayList<RSTBoolean>();
        }
        return othersInsertsAreVisible;
    }

    @Deprecated
    public void setOthersInsertsAreVisible(
            final List<RSTBoolean> othersInsertsAreVisible) {
        this.othersInsertsAreVisible = othersInsertsAreVisible;
    }

    // ------------------------------------------------- othersUpdatesAreVisible
    public List<RSTBoolean> getOthersUpdatesAreVisible() {
        if (othersUpdatesAreVisible == null) {
            othersUpdatesAreVisible = new ArrayList<RSTBoolean>();
        }
        return othersUpdatesAreVisible;
    }

    @Deprecated
    public void setOthersUpdatesAreVisible(
            final List<RSTBoolean> othersUpdatesAreVisible) {
        this.othersUpdatesAreVisible = othersUpdatesAreVisible;
    }

    // ---------------------------------------------------- ownDeletesAreVisible
    public List<RSTBoolean> getOwnDeletesAreVisible() {
        if (ownDeletesAreVisible == null) {
            ownDeletesAreVisible = new ArrayList<RSTBoolean>();
        }
        return ownDeletesAreVisible;
    }

    @Deprecated
    public void setOwnDeletesAreVisible(
            final List<RSTBoolean> ownDeletesAreVisible) {
        this.ownDeletesAreVisible = ownDeletesAreVisible;
    }

    // -------------------------------------------------- ownerInsertsAreVisible
    public List<RSTBoolean> getOwnInsertsAreVisible() {
        if (ownInsertsAreVisible == null) {
            ownInsertsAreVisible = new ArrayList<RSTBoolean>();
        }
        return ownInsertsAreVisible;
    }

    @Deprecated
    public void setOwnInsertsAreVisible(
            final List<RSTBoolean> ownInsertsAreVisible) {
        this.ownInsertsAreVisible = ownInsertsAreVisible;
    }

    // ---------------------------------------------------- ownUpdatesAreVisible
    public List<RSTBoolean> getOwnUpdatesAreVisible() {
        if (ownUpdatesAreVisible == null) {
            ownUpdatesAreVisible = new ArrayList<RSTBoolean>();
        }
        return ownUpdatesAreVisible;
    }

    @Deprecated
    public void setOwnUpdatesAreVisible(
            final List<RSTBoolean> ownUpdatesAreVisible) {
        this.ownUpdatesAreVisible = ownUpdatesAreVisible;
    }

    // ----------------------------------------------------------- procedureTerm
    public String getProcedureTerm() {
        return procedureTerm;
    }

    public void setProcedureTerm(final String procedureTerm) {
        this.procedureTerm = procedureTerm;
    }

    // ---------------------------------------------------------------- readOnly
    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
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

    public String getRowIdLifetimeName() {

        return rowIdLifetime == null ? null : rowIdLifetime.name();
    }

    public void setRowIdLifetimeName(String rowIdLifetimeName) {

        this.rowIdLifetime = rowIdLifetimeName == null
                             ? null : RowIdLifetime.valueOf(rowIdLifetimeName);
    }

    // ------------------------------------------------------------- schemaNames
    public List<SchemaName> getSchemaNames() {
        if (schemaNames == null) {
            schemaNames = new ArrayList<SchemaName>();
        }
        return schemaNames;
    }

    public void setSchemaNames(final List<SchemaName> schemaNames) {
        this.schemaNames = schemaNames;
    }

    // -------------------------------------------------------------- schemaTerm
    public String getSchemaTerm() {
        return schemaTerm;
    }

    public void setSchemaTerm(final String schemaTerm) {
        this.schemaTerm = schemaTerm;
    }

    // ------------------------------------------------------ searchStringEscape
    public String getSearchStringEscape() {
        return searchStringEscape;
    }

    public void setSearchStringEscape(final String searchStringEscape) {
        this.searchStringEscape = searchStringEscape;
    }

    // ------------------------------------------------------------- SQLKeywords
    public String getSQLKewords() {
        return SQLKewords;
    }

    public void setSQLKewords(final String SQLKewords) {
        this.SQLKewords = SQLKewords;
    }

    // ------------------------------------------------------------ SQLStateType
    public int getSQLStateType() {
        return SQLStateType;
    }

    public void setSQLStateType(final int SQLStateType) {
        this.SQLStateType = SQLStateType;
    }

    // ---------------------------------------------- StoresLowerCaseIdentifiers
    public boolean isStoresLowerCaseIdentifiers() {
        return storesLowerCaseIdentifiers;
    }

    public void setStoresLowerCaseIdentifiers(
            final boolean storesLowerCaseIdentifiers) {
        this.storesLowerCaseIdentifiers = storesLowerCaseIdentifiers;
    }

    // ---------------------------------------- storesLowerCaseQuotedIdentifiers
    public boolean isStoresLowerCaseQuotedIdentifiers() {
        return storesLowerCaseQuotedIdentifiers;
    }

    public void setStoresLowerCaseQuotedIdentifiers(
            final boolean storesLowerCaseQuotedIdentifiers) {
        this.storesLowerCaseQuotedIdentifiers
                = storesLowerCaseQuotedIdentifiers;
    }

    // ---------------------------------------------- storesMixedCaseIdentifiers
    public boolean isStoresMixedCaseIdentifiers() {
        return storesMixedCaseIdentifiers;
    }

    public void setStoresMixedCaseIdentifiers(
            final boolean storesMixedCaseIdentifiers) {
        this.storesMixedCaseIdentifiers = storesMixedCaseIdentifiers;
    }

    // ---------------------------------------- storesMixedCaseQuotedIdentifiers
    public boolean isStoresMixedCaseQuotedIdentifiers() {
        return storesMixedCaseQuotedIdentifiers;
    }

    public void setStoresMixedCaseQuotedIdentifiers(
            final boolean storesMixedCaseQuotedIdentifiers) {
        this.storesMixedCaseQuotedIdentifiers
                = storesMixedCaseQuotedIdentifiers;
    }

    public boolean isStoresUpperCaseIdentifiers() {
        return storesUpperCaseIdentifiers;
    }

    public void setStoresUpperCaseIdentifiers(boolean storesUpperCaseIdentifiers) {
        this.storesUpperCaseIdentifiers = storesUpperCaseIdentifiers;
    }

    public boolean isStoresUpperCaseQuotedIdentifiers() {
        return storesUpperCaseQuotedIdentifiers;
    }

    public void setStoresUpperCaseQuotedIdentifiers(boolean storesUpperCaseQuotedIdentifiers) {
        this.storesUpperCaseQuotedIdentifiers = storesUpperCaseQuotedIdentifiers;
    }

    public String getStringFunctions() {
        return stringFunctions;
    }

    public void setStringFunctions(String stringFunctions) {
        this.stringFunctions = stringFunctions;
    }

    public boolean isSupportsAlterTableWithAddColumn() {
        return supportsAlterTableWithAddColumn;
    }

    public void setSupportsAlterTableWithAddColumn(boolean supportsAlterTableWithAddColumn) {
        this.supportsAlterTableWithAddColumn = supportsAlterTableWithAddColumn;
    }

    public boolean isSupportsAlterTableWithDropColumn() {
        return supportsAlterTableWithDropColumn;
    }

    public void setSupportsAlterTableWithDropColumn(boolean supportsAlterTableWithDropColumn) {
        this.supportsAlterTableWithDropColumn = supportsAlterTableWithDropColumn;
    }

    public boolean isSupportsANSI92EntryLevelSQL() {
        return supportsANSI92EntryLevelSQL;
    }

    public void setSupportsANSI92EntryLevelSQL(boolean supportsANSI92EntryLevelSQL) {
        this.supportsANSI92EntryLevelSQL = supportsANSI92EntryLevelSQL;
    }

    public boolean isSupportsANSI92FullSQL() {
        return supportsANSI92FullSQL;
    }

    public void setSupportsANSI92FullSQL(boolean supportsANSI92FullSQL) {
        this.supportsANSI92FullSQL = supportsANSI92FullSQL;
    }

    public boolean isSupportsANSI92IntermediateSQL() {
        return supportsANSI92IntermediateSQL;
    }

    public void setSupportsANSI92IntermediateSQL(boolean supportsANSI92IntermediateSQL) {
        this.supportsANSI92IntermediateSQL = supportsANSI92IntermediateSQL;
    }

    public boolean isSupportsBatchUpdates() {
        return supportsBatchUpdates;
    }

    public void setSupportsBatchUpdates(boolean supportsBatchUpdates) {
        this.supportsBatchUpdates = supportsBatchUpdates;
    }

    public boolean isSupportsCatalogsInDataManipulation() {
        return supportsCatalogsInDataManipulation;
    }

    public void setSupportsCatalogsInDataManipulation(boolean supportsCatalogsInDataManipulation) {
        this.supportsCatalogsInDataManipulation = supportsCatalogsInDataManipulation;
    }

    public boolean isSupportsCatalogsInIndexDefinitions() {
        return supportsCatalogsInIndexDefinitions;
    }

    public void setSupportsCatalogsInIndexDefinitions(boolean supportsCatalogsInIndexDefinitions) {
        this.supportsCatalogsInIndexDefinitions = supportsCatalogsInIndexDefinitions;
    }

    public boolean isSupportsCatalogsInPrivilegeDefinitions() {
        return supportsCatalogsInPrivilegeDefinitions;
    }

    public void setSupportsCatalogsInPrivilegeDefinitions(boolean supportsCatalogsInPrivilegeDefinitions) {
        this.supportsCatalogsInPrivilegeDefinitions = supportsCatalogsInPrivilegeDefinitions;
    }

    public boolean isSupportsCatalogsInProcedureCalls() {
        return supportsCatalogsInProcedureCalls;
    }

    public void setSupportsCatalogsInProcedureCalls(boolean supportsCatalogsInProcedureCalls) {
        this.supportsCatalogsInProcedureCalls = supportsCatalogsInProcedureCalls;
    }

    public boolean isSupportsCatalogsInTableDefinitions() {
        return supportsCatalogsInTableDefinitions;
    }

    public void setSupportsCatalogsInTableDefinitions(boolean supportsCatalogsInTableDefinitions) {
        this.supportsCatalogsInTableDefinitions = supportsCatalogsInTableDefinitions;
    }

    public boolean isSupportsColumnAliasing() {
        return supportsColumnAliasing;
    }

    public void setSupportsColumnAliasing(boolean supportsColumnAliasing) {
        this.supportsColumnAliasing = supportsColumnAliasing;
    }

    public boolean isSupportsConvert_() {
        return supportsConvert_;
    }

    public void setSupportsConvert_(boolean supportsConvert_) {
        this.supportsConvert_ = supportsConvert_;
    }

    // --------------------------------------------------------- supportsConvert
    public List<SDTSDTBoolean> getSupportsConvert() {
        if (supportsConvert == null) {
            supportsConvert = new ArrayList<SDTSDTBoolean>();
        }
        return supportsConvert;
    }

    @Deprecated
    public void setSupportsConvert(final List<SDTSDTBoolean> supportsConvert) {
        this.supportsConvert = supportsConvert;
    }

    // -------------------------------------------------- supportsCoreSQLGrammar
    public boolean isSupportsCoreSQLGrammar() {
        return supportsCoreSQLGrammar;
    }

    public void setSupportsCoreSQLGrammar(
            final boolean supportsCoreSQLGrammar) {
        this.supportsCoreSQLGrammar = supportsCoreSQLGrammar;
    }

    // -------------------------------------------- supportsCorrelatedSubqueries
    public boolean isSupportsCorrelatedSubqueries() {
        return supportsCorrelatedSubqueries;
    }

    public void setSupportsCorrelatedSubqueries(
            final boolean supportsCorrelatedSubqueries) {
        this.supportsCorrelatedSubqueries = supportsCorrelatedSubqueries;
    }

    // -------------------- supportsDataDefinitionAndDataManipulationTransaction
    public boolean isSupportsDataDefinitionAndDataManipulationTransactions() {
        return supportsDataDefinitionAndDataManipulationTransactions;
    }

    public void setSupportsDataDefinitionAndDataManipulationTransactions(
            final boolean supportsDataDefinitionAndDataManipulationTransactions) {
        this.supportsDataDefinitionAndDataManipulationTransactions
                = supportsDataDefinitionAndDataManipulationTransactions;
    }

    // -------------------------------- supportsDataManipulationTransactionsOnly
    public boolean isSupportsDataManipulationTransactionsOnly() {
        return supportsDataManipulationTransactionsOnly;
    }

    public void setSupportsDataManipulationTransactionsOnly(
            final boolean supportsDataManipulationTransactionsOnly) {
        this.supportsDataManipulationTransactionsOnly
                = supportsDataManipulationTransactionsOnly;
    }

    // ---------------------------------- supportsDifferentTableCorrelationNames
    public boolean isSupportsDifferentTableCorrelationNames() {
        return supportsDifferentTableCorrelationNames;
    }

    public void setSupportsDifferentTableCorrelationNames(
            final boolean supportsDifferentTableCorrelationNames) {
        this.supportsDifferentTableCorrelationNames
                = supportsDifferentTableCorrelationNames;
    }

    // -------------------------------------------- supportsExpressionsInOrderBy
    public boolean isSupportsExpressionsInOrderBy() {
        return supportsExpressionsInOrderBy;
    }

    public void setSupportsExpressionsInOrderBy(
            final boolean supportsExpressionsInOrderBy) {
        this.supportsExpressionsInOrderBy = supportsExpressionsInOrderBy;
    }

    // ---------------------------------------------- supportsExtendedSQLGrammar
    public boolean isSupportsExtendedSQLGrammar() {
        return supportsExtendedSQLGrammar;
    }

    public void setSupportsExtendedSQLGrammar(
            final boolean supportsExtendedSQLGrammar) {
        this.supportsExtendedSQLGrammar = supportsExtendedSQLGrammar;
    }

    // -------------------------------------------------- supportsFullOuterJoins
    public boolean isSupportsFullOuterJoins() {
        return supportsFullOuterJoins;
    }

    public void setSupportsFullOuterJoins(boolean supportsFullOuterJoins) {
        this.supportsFullOuterJoins = supportsFullOuterJoins;
    }

    // ------------------------------------------------ supportsGetGeneratedKeys
    public boolean isSupportsGetGeneratedKeys() {
        return supportsGetGeneratedKeys;
    }

    public void setSupportsGetGeneratedKeys(
            final boolean supportsGetGeneratedKeys) {
        this.supportsGetGeneratedKeys = supportsGetGeneratedKeys;
    }

    // --------------------------------------------------------- supportsGroupBy
    public boolean isSupportsGroupBy() {
        return supportsGroupBy;
    }

    public void setSupportsGroupBy(final boolean supportsGroupBy) {
        this.supportsGroupBy = supportsGroupBy;
    }

    // --------------------------------------------- supportsGroupByBeyondSelect
    public boolean isSupportsGroupByBeyondSelect() {
        return supportsGroupByBeyondSelect;
    }

    public void setSupportsGroupByBeyondSelect(
            final boolean supportsGroupByBeyondSelect) {
        this.supportsGroupByBeyondSelect = supportsGroupByBeyondSelect;
    }

    // ------------------------------------------------ supportsGroupByUnrelated
    public boolean isSupportsGroupByUnrelated() {
        return supportsGroupByUnrelated;
    }

    public void setSupportsGroupByUnrelated(
            final boolean supportsGroupByUnrelated) {
        this.supportsGroupByUnrelated = supportsGroupByUnrelated;
    }

    // ------------------------------------ supportsIntegrityEnhancementFacility
    public boolean isSupportsIntegrityEnhancementFacility() {
        return supportsIntegrityEnhancementFacility;
    }

    public void setSupportsIntegrityEnhancementFacility(
            final boolean supportsIntegrityEnhancementFacility) {
        this.supportsIntegrityEnhancementFacility
                = supportsIntegrityEnhancementFacility;
    }

    // ------------------------------------------------ supportsLikeEscapeClause
    public boolean isSupportsLikeEscapeClause() {
        return supportsLikeEscapeClause;
    }

    public void setSupportsLikeEscapeClause(
            final boolean supportsLikeEscapeClause) {
        this.supportsLikeEscapeClause = supportsLikeEscapeClause;
    }

    // ----------------------------------------------- supportsLimitedOuterJoins
    public boolean isSupportsLimitedOuterJoins() {
        return supportsLimitedOuterJoins;
    }

    public void setSupportsLimitedOuterJoins(
            final boolean supportsLimitedOuterJoins) {
        this.supportsLimitedOuterJoins = supportsLimitedOuterJoins;
    }

    // ----------------------------------------------- supportsMinimumSQLGrammar
    public boolean isSupportsMinimumSQLGrammar() {
        return supportsMinimumSQLGrammar;
    }

    public void setSupportsMinimumSQLGrammar(
            final boolean supportsMinimumSQLGrammar) {
        this.supportsMinimumSQLGrammar = supportsMinimumSQLGrammar;
    }

    // -------------------------------------------- supportsMixedCaseIdentifiers
    public boolean isSupportsMixedCaseIdentifiers() {
        return supportsMixedCaseIdentifiers;
    }

    public void setSupportsMixedCaseIdentifiers(
            final boolean supportsMixedCaseIdentifiers) {
        this.supportsMixedCaseIdentifiers = supportsMixedCaseIdentifiers;
    }

    // -------------------------------------- supportsMixedCaseQuotedIdentifiers
    public boolean isSupportsMixedCaseQuotedIdentifiers() {
        return supportsMixedCaseQuotedIdentifiers;
    }

    public void setSupportsMixedCaseQuotedIdentifiers(
            final boolean supportsMixedCaseQuotedIdentifiers) {
        this.supportsMixedCaseQuotedIdentifiers
                = supportsMixedCaseQuotedIdentifiers;
    }

    // --------------------------------------------- supportsMultipleOpenResults
    public boolean isSupportsMultipleOpenResults() {
        return supportsMultipleOpenResults;
    }

    public void setSupportsMultipleOpenResults(
            final boolean supportsMultipleOpenResults) {
        this.supportsMultipleOpenResults = supportsMultipleOpenResults;
    }

    // ---------------------------------------------- supportsMultipleResultSets
    public boolean isSupportsMultipleResultSets() {
        return supportsMultipleResultSets;
    }

    public void setSupportsMultipleResultSets(
            final boolean supportsMultipleResultSets) {
        this.supportsMultipleResultSets = supportsMultipleResultSets;
    }

    // -------------------------------------------- supportsMultipleTransactions
    public boolean isSupportsMultipleTransactions() {
        return supportsMultipleTransactions;
    }

    public void setSupportsMultipleTransactions(
            final boolean supportsMultipleTransactions) {
        this.supportsMultipleTransactions = supportsMultipleTransactions;
    }

    // ------------------------------------------------- supportsNamedParameters
    public boolean isSupportsNamedParameters() {
        return supportsNamedParameters;
    }

    public void setSupportsNamedParameters(
            final boolean supportsNamedParameters) {
        this.supportsNamedParameters = supportsNamedParameters;
    }

    // ---------------------------------------------- supportsNonNullableColumns
    public boolean isSupportsNonNullableColumns() {
        return supportsNonNullableColumns;
    }

    public void setSupportsNonNullableColumns(
            final boolean supportsNonNullableColumns) {
        this.supportsNonNullableColumns = supportsNonNullableColumns;
    }

    // ----------------------------------------- supportsOpenCursorsAcrossCommit
    public boolean isSupportsOpenCursorsAcrossCommit() {
        return supportsOpenCursorsAcrossCommit;
    }

    public void setSupportsOpenCursorsAcrossCommit(
            final boolean supportsOpenCursorsAcrossCommit) {
        this.supportsOpenCursorsAcrossCommit = supportsOpenCursorsAcrossCommit;
    }

    // --------------------------------------- supportsOpenCursorsAcrossRollback
    public boolean isSupportsOpenCursorsAcrossRollback() {
        return supportsOpenCursorsAcrossRollback;
    }

    public void setSupportsOpenCursorsAcrossRollback(
            final boolean supportsOpenCursorsAcrossRollback) {
        this.supportsOpenCursorsAcrossRollback
                = supportsOpenCursorsAcrossRollback;
    }

    // -------------------------------------- supportsOpenStatementsAcrossCommit
    public boolean isSupportsOpenStatementsAcrossCommit() {
        return supportsOpenStatementsAcrossCommit;
    }

    public void setSupportsOpenStatementsAcrossCommit(
            final boolean supportsOpenStatementsAcrossCommit) {
        this.supportsOpenStatementsAcrossCommit
                = supportsOpenStatementsAcrossCommit;
    }

    // ------------------------------------ supportsOpenStatementsAcrossRollback
    public boolean isSupportsOpenStatementsAcrossRollback() {
        return supportsOpenStatementsAcrossRollback;
    }

    public void setSupportsOpenStatementsAcrossRollback(
            final boolean supportsOpenStatementsAcrossRollback) {
        this.supportsOpenStatementsAcrossRollback
                = supportsOpenStatementsAcrossRollback;
    }

    // ------------------------------------------------ supportsOrderByUnrelated
    public boolean isSupportsOrderByUnrelated() {
        return supportsOrderByUnrelated;
    }

    public void setSupportsOrderByUnrelated(
            final boolean supportsOrderByUnrelated) {
        this.supportsOrderByUnrelated = supportsOrderByUnrelated;
    }

    // ------------------------------------------------------ supportsOuterJoins
    public boolean isSupportsOuterJoins() {
        return supportsOuterJoins;
    }

    public void setSupportsOuterJoins(final boolean supportsOuterJoins) {
        this.supportsOuterJoins = supportsOuterJoins;
    }

    // ------------------------------------------------ supportsPositionedDelete
    public boolean isSupportsPositionedDelete() {
        return supportsPositionedDelete;
    }

    public void setSupportsPositionedDelete(
            final boolean supportsPositionedDelete) {
        this.supportsPositionedDelete = supportsPositionedDelete;
    }

    // -------------------------------------------------- supportsPositionUpdate
    public boolean isSupportsPositionedUpdate() {
        return supportsPositionedUpdate;
    }

    public void setSupportsPositionedUpdate(
            final boolean supportsPositionedUpdate) {
        this.supportsPositionedUpdate = supportsPositionedUpdate;
    }

    // ------------------------------------------------------ supportsRefCursors
    public boolean isSupportsRefCursors() {
        return supportsRefCursors;
    }

    public void setSupportsRefCursors(final boolean supportsRefCursors) {
        this.supportsRefCursors = supportsRefCursors;
    }

    // -------------------------------------------- supportsResultSetConcurrency
    public List<RSTRSCBoolean> getSupportsResultSetConcurrency() {
        if (supportsResultSetConcurrency == null) {
            supportsResultSetConcurrency = new ArrayList<RSTRSCBoolean>();
        }
        return supportsResultSetConcurrency;
    }

    public void setSupportsResultSetConcurrency(
            final List<RSTRSCBoolean> supportsResultSetConcurrency) {
        this.supportsResultSetConcurrency = supportsResultSetConcurrency;
    }

    // -------------------------------------------- supportsResultSetHoldability
    public List<RSHBoolean> getSupportsResultSetHoldability() {
        if (supportsResultSetHoldability == null) {
            supportsResultSetHoldability = new ArrayList<RSHBoolean>();
        }
        return supportsResultSetHoldability;
    }

    public void setSupportsResultSetHoldability(
            final List<RSHBoolean> supportsResultSetHoldability) {
        this.supportsResultSetHoldability = supportsResultSetHoldability;
    }

    // --------------------------------------------------- supportsResultSetType
    public List<RSTBoolean> getSupportsResultSetType() {
        if (supportsResultSetType == null) {
            supportsResultSetType = new ArrayList<RSTBoolean>();
        }
        return supportsResultSetType;
    }

    public void setSupportsResultSetType(
            final List<RSTBoolean> supportsResultSetType) {
        this.supportsResultSetType = supportsResultSetType;
    }

    // ------------------------------------------------------ supportsSavepoints
    public boolean isSupportsSavepoints() {
        return supportsSavepoints;
    }

    public void setSupportsSavepoints(final boolean supportsSavepoints) {
        this.supportsSavepoints = supportsSavepoints;
    }

    // --------------------------------------- supportsSchemasInDataManipulation
    public boolean isSupportsSchemasInDataManipulation() {
        return supportsSchemasInDataManipulation;
    }

    public void setSupportsSchemasInDataManipulation(
            final boolean supportsSchemasInDataManipulation) {
        this.supportsSchemasInDataManipulation
                = supportsSchemasInDataManipulation;
    }

    // --------------------------------------- supportsSchemasInIndexDefinitions
    public boolean isSupportsSchemasInIndexDefinitions() {
        return supportsSchemasInIndexDefinitions;
    }

    public void setSupportsSchemasInIndexDefinitions(
            final boolean supportsSchemasInIndexDefinitions) {
        this.supportsSchemasInIndexDefinitions
                = supportsSchemasInIndexDefinitions;
    }

    // ----------------------------------- supportsSchemasInPrivilegeDefinitions
    public boolean isSupportsSchemasInPrivilegeDefinitions() {
        return supportsSchemasInPrivilegeDefinitions;
    }

    public void setSupportsSchemasInPrivilegeDefinitions(
            final boolean supportsSchemasInPrivilegeDefinitions) {
        this.supportsSchemasInPrivilegeDefinitions
                = supportsSchemasInPrivilegeDefinitions;
    }

    // ----------------------------------------- supportsSchemasInProcedureCalls
    public boolean isSupportsSchemasInProcedureCalls() {
        return supportsSchemasInProcedureCalls;
    }

    public void setSupportsSchemasInProcedureCalls(
            final boolean supportsSchemasInProcedureCalls) {
        this.supportsSchemasInProcedureCalls = supportsSchemasInProcedureCalls;
    }

    // --------------------------------------- supportsSchemasInTableDefinitions
    public boolean isSupportsSchemasInTableDefinitions() {
        return supportsSchemasInTableDefinitions;
    }

    public void setSupportsSchemasInTableDefinitions(
            final boolean supportsSchemasInTableDefinitions) {
        this.supportsSchemasInTableDefinitions
                = supportsSchemasInTableDefinitions;
    }

    // ------------------------------------------------- supportsSelectForUpdate
    public boolean isSupportsSelectForUpdate() {
        return supportsSelectForUpdate;
    }

    public void setSupportsSelectForUpdate(
            final boolean supportsSelectForUpdate) {
        this.supportsSelectForUpdate = supportsSelectForUpdate;
    }

    // ------------------------------------------------ supportsStatementPolling
    public boolean isSupportsStatementPooling() {
        return supportsStatementPooling;
    }

    public void setSupportsStatementPooling(
            final boolean supportsStatementPooling) {
        this.supportsStatementPooling = supportsStatementPooling;
    }

    // ---------------------------------- supportsStoredFunctionsusingCallSyntax
    public boolean isSupportsStoredFunctionsUsingCallSyntax() {
        return supportsStoredFunctionsUsingCallSyntax;
    }

    public void setSupportsStoredFunctionsUsingCallSyntax(
            final boolean supportsStoredFunctionsUsingCallSyntax) {
        this.supportsStoredFunctionsUsingCallSyntax
                = supportsStoredFunctionsUsingCallSyntax;
    }

    // ------------------------------------------------ supportsStoredProcedures
    public boolean isSupportsStoredProcedures() {
        return supportsStoredProcedures;
    }

    public void setSupportsStoredProcedures(
            final boolean supportsStoredProcedures) {
        this.supportsStoredProcedures = supportsStoredProcedures;
    }

    // ----------------------------------------- supportsSubqueriesInComparisons
    public boolean isSupportsSubqueriesInComparisons() {
        return supportsSubqueriesInComparisons;
    }

    public void setSupportsSubqueriesInComparisons(
            final boolean supportsSubqueriesInComparisons) {
        this.supportsSubqueriesInComparisons = supportsSubqueriesInComparisons;
    }

    // ---------------------------------------------- supportsSubqueriesInExists
    public boolean isSupportsSubqueriesInExists() {
        return supportsSubqueriesInExists;
    }

    public void setSupportsSubqueriesInExists(
            final boolean supportsSubqueriesInExists) {
        this.supportsSubqueriesInExists = supportsSubqueriesInExists;
    }

    // ------------------------------------------------- supportsSubqueriesInIns
    public boolean isSupportsSubqueriesInIns() {
        return supportsSubqueriesInIns;
    }

    public void setSupportsSubqueriesInIns(
            final boolean supportsSubqueriesInIns) {
        this.supportsSubqueriesInIns = supportsSubqueriesInIns;
    }

    // ----------------------------------------- supportsSubqueriesInQuantifieds
    public boolean isSupportsSubqueriesInQuantifieds() {
        return supportsSubqueriesInQuantifieds;
    }

    public void setSupportsSubqueriesInQuantifieds(
            final boolean supportsSubqueriesInQuantifieds) {
        this.supportsSubqueriesInQuantifieds = supportsSubqueriesInQuantifieds;
    }

    // ------------------------------------------- supportsTableCorrelationNames
    public boolean isSupportsTableCorrelationNames() {
        return supportsTableCorrelationNames;
    }

    public void setSupportsTableCorrelationNames(
            final boolean supportsTableCorrelationNames) {
        this.supportsTableCorrelationNames = supportsTableCorrelationNames;
    }

    // --------------------------------------- supportsTransactionIsolationLevel
    public List<TILBoolean> getSupportsTransactionIsolationLevel() {
        if (supportsTransactionIsolationLevel == null) {
            supportsTransactionIsolationLevel = new ArrayList<TILBoolean>();
        }
        return supportsTransactionIsolationLevel;
    }

    @Deprecated
    public void setSupportsTransactionIsolationLevel(
            final List<TILBoolean> supportsTransactionIsolationLevel) {
        this.supportsTransactionIsolationLevel
                = supportsTransactionIsolationLevel;
    }

    // ---------------------------------------------------- supportsTransactions
    public boolean isSupportsTransactions() {
        return supportsTransactions;
    }

    public void setSupportsTransactions(final boolean supportsTransactions) {
        this.supportsTransactions = supportsTransactions;
    }

    // ----------------------------------------------------------- supportsUnion
    public boolean isSupportsUnion() {
        return supportsUnion;
    }

    public void setSupportsUnion(final boolean supportsUnion) {
        this.supportsUnion = supportsUnion;
    }

    // -------------------------------------------------------- supportsUnionAll
    public boolean isSupportsUnionAll() {
        return supportsUnionAll;
    }

    public void setSupportsUnionAll(final boolean supportsUnionAll) {
        this.supportsUnionAll = supportsUnionAll;
    }

    // --------------------------------------------------------- systemFunctions
    public String getSystemFunctions() {
        return systemFunctions;
    }

    public void setSystemFunctions(final String systemFunctions) {
        this.systemFunctions = systemFunctions;
    }

    // -------------------------------------------------------------- tableTypes
    public List<TableType> getTableTypes() {
        if (tableTypes == null) {
            tableTypes = new ArrayList<TableType>();
        }
        return tableTypes;
    }

    @Deprecated
    public void setTableTypes(final List<TableType> tableTypes) {
        this.tableTypes = tableTypes;
    }

    // ------------------------------------------------------- timeDateFunctions
    public String getTimeDateFunctions() {
        return timeDateFunctions;
    }

    public void setTimeDateFunctions(final String timeDateFunctions) {
        this.timeDateFunctions = timeDateFunctions;
    }

    // ---------------------------------------------------------------- typeInfo
    public List<TypeInfo> getTypeInfo() {
        if (typeInfo == null) {
            typeInfo = new ArrayList<TypeInfo>();
        }
        return typeInfo;
    }

    @Deprecated
    public void setTypeInfo(final List<TypeInfo> typeInfo) {
        this.typeInfo = typeInfo;
    }

    // ------------------------------------------------------ updatesAreDetected
    public List<RSTBoolean> getUpdatesAreDetected() {
        if (updatesAreDetected == null) {
            updatesAreDetected = new ArrayList<RSTBoolean>();
        }
        return updatesAreDetected;
    }

    @Deprecated
    public void setUpdatesAreDetected(
            final List<RSTBoolean> updatesAreDetected) {
        this.updatesAreDetected = updatesAreDetected;
    }

    // ---------------------------------------------------------------- userName
    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    // --------------------------------------------------------------------- URL
    public String getURL() {
        return URL;
    }

    public void setURL(final String URL) {
        this.URL = URL;
    }

    // --------------------------------------------------- usesLocalFilePerTable
    public boolean isUsesLocalFilePerTable() {
        return usesLocalFilePerTable;
    }

    public void setUsesLocalFilePerTable(final boolean usesLocalFilePerTable) {
        this.usesLocalFilePerTable = usesLocalFilePerTable;
    }

    // ---------------------------------------------------------- usesLocalFiles
    public boolean isUsesLocalFiles() {
        return usesLocalFiles;
    }

    public void setUsesLocalFiles(final boolean usesLocalFiles) {
        this.usesLocalFiles = usesLocalFiles;
    }

    // -------------------------------------------------------------------------
    @Invoke(name = "allProceduresAreCallable")
    @XmlElement(required = true)
    private boolean allProceduresAreCallable;

    @Invoke(name = "allTablesAreSelectable")
    @XmlElement(required = true)
    private boolean allTablesAreSelectable;

    @Invoke(name = "autoCommitFailureClosesAllResultSets")
    @XmlElement(required = true)
    private boolean autoCommitFailureClosesAllResultSets;

    @Invoke(name = "getCatalogs")
    @XmlElementRef
    private List<Catalog> catalogs;

    @Invoke(name = "isCatalogAtStart")
    @XmlElement(required = true)
    private boolean catalogAtStart;

    @Invoke(name = "getCatalogSeparator")
    @XmlElement(nillable = true, required = true)
    private String catalogSeparator;

    @Invoke(name = "getCatalogTerm")
    @XmlElement(nillable = true, required = true)
    private String catalogTerm;

    @Invoke(name = "getClientInfoProperties")
    @XmlElementRef
    private List<ClientInfoProperty> clientInfoProperties;

    @Invoke(name = "getConnection")
    @XmlTransient
    private Connection connection;

    @XmlElementRef
    private List<CrossReference> crossReferences;

    @Invoke(name = "dataDefinitionCausesTransactionCommit")
    @XmlElement(required = true)
    private boolean dataDefinitionCausesTransactionCommit;

    @Invoke(name = "dataDefinitionIgnoredInTransactions")
    @XmlElement(required = true)
    private boolean dataDefinitionIgnoredInTransactions;

    @Invoke(name = "getDatabaseMajorVersion")
    @XmlElement(required = true)
    private int databaseMajorVersion;

    @Invoke(name = "getDatabaseMinorVersion")
    @XmlElement(required = true)
    private int databaseMinorVersion;

    @Invoke(name = "getDatabaseProductName")
    @XmlElement(required = true)
    private String databaseProductName;

    @Invoke(name = "getDatabaseProductVersion")
    @XmlElement(required = true)
    private String databaseProductVersion;

    @Invoke(name = "getDefaultTransactionIsolation")
    @XmlElement(required = true)
    private int defaultTransactionIsolation;

    @Invoke(name = "deletesAreDetected",
            types = {int.class},
            args = {
                @Literals({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @Literals({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> deletesAreDetected;

    @Invoke(name = "doesMaxRowSizeIncludeBlobs")
    @XmlElement(required = true)
    private boolean doesMaxRowSizeIncludeBlobs;

    @Invoke(name = "getDriverMajorVersion")
    @XmlElement(required = true)
    private int driverMajorVersion;

    @Invoke(name = "getDriverMinorVersion")
    @XmlElement(required = true)
    private int driverMinorVersion;

    @Invoke(name = "getDriverName")
    @XmlElement(required = true)
    private String driverName;

    @Invoke(name = "getDriverVersion")
    @XmlElement(required = true)
    private String driverVersion;

    @Invoke(name = "getExtraNameCharacters")
    @XmlElement(required = true)
    private String extraNameCharacters;

    @Invoke(name = "generatedKeyAlwaysReturned")
    @XmlElement(required = true)
    private boolean generatedKeyAlwaysReturned;

    @Invoke(name = "getIdentifierQuoteString")
    @XmlElement(required = true)
    private String identifierQuoteString;

    @Invoke(name = "insertsAreDetected",
            types = {int.class},
            args = {
                @Literals({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @Literals({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> insertsAreDetected;

    @Invoke(name = "getJDBCMajorVersion")
    @XmlElement(required = true)
    private int JDBCMajorVersion;

    @Invoke(name = "getJDBCMinorVersion")
    @XmlElement(required = true)
    private int JDBCMinorVersion;

    @Invoke(name = "locatorsUpdateCopy")
    @XmlElement(required = true)
    private boolean locatorsUpdateCopy;

    @Invoke(name = "getMaxBinaryLiteralLength")
    @XmlElement(required = true)
    private int maxBinaryLiteralLength;

    @Invoke(name = "getMaxCatalogNameLength")
    @XmlElement(required = true)
    private int maxCatalogNameLength;

    @Invoke(name = "getMaxCharLiteralLength")
    @XmlElement(required = true)
    private int maxCharLiteralLength;

    @Invoke(name = "getMaxColumnNameLength")
    @XmlElement(required = true)
    private int maxColumnNameLength;

    @Invoke(name = "getMaxColumnsInGroupBy")
    @XmlElement(required = true)
    private int maxColumnsInGroupBy;

    @Invoke(name = "getMaxColumnsInIndex")
    @XmlElement(required = true)
    private int maxColumnsInIndex;

    @Invoke(name = "getMaxColumnsInOrderBy")
    @XmlElement(required = true)
    private int maxColumnsInOrderBy;

    @Invoke(name = "getMaxColumnsInSelect")
    @XmlElement(required = true)
    private int maxColumnsInSelect;

    @Invoke(name = "getMaxColumnsInTable")
    @XmlElement(required = true)
    private int maxColumnsInTable;

    @Invoke(name = "getMaxConnections")
    @XmlElement(required = true)
    private int maxConnections;

    @Invoke(name = "getMaxCursorNameLength")
    @XmlElement(required = true)
    private int maxCursorNameLength;

    @Invoke(name = "getMaxIndexLength")
    @XmlElement(required = true)
    private int maxIndexLength;

    @Invoke(name = "getMaxLogicalLobSize")
    @XmlElement(required = true)
    private long maxLogicalLobSize;

    @Invoke(name = "getMaxProcedureNameLength")
    @XmlElement(required = true)
    private int maxProcedureNameLength;

    @Invoke(name = "getMaxRowSize")
    @XmlElement(required = true)
    private int maxRowSize;

    @Invoke(name = "getMaxSchemaNameLength")
    @XmlElement(required = true)
    private int maxSchemaNameLength;

    @Invoke(name = "getMaxStatementLength")
    @XmlElement(required = true)
    private int maxStatementLength;

    @Invoke(name = "getMaxStatements")
    @XmlElement(required = true)
    private int maxStatements;

    @Invoke(name = "getMaxTableNameLength")
    @XmlElement(required = true)
    private int maxTableNameLength;

    @Invoke(name = "getMaxTablesInSelect")
    @XmlElement(required = true)
    private int maxTablesInSelect;

    @Invoke(name = "getMaxUserNameLength")
    @XmlElement(required = true)
    private int maxUserNameLength;

    @Invoke(name = "nullPlusNonNullIsNull")
    @XmlElement(required = true)
    private boolean nullPlusNonNullIsNull;

    @Invoke(name = "nullsAreSortedAtEnd")
    @XmlElement(required = true)
    private boolean nullsAreSortedAtEnd;

    @Invoke(name = "nullsAreSortedAtStart")
    @XmlElement(required = true)
    private boolean nullsAreSortedAtStart;

    @Invoke(name = "nullsAreSortedHigh")
    @XmlElement(required = true)
    private boolean nullsAreSortedHigh;

    @Invoke(name = "nullsAreSortedLow")
    @XmlElement(required = true)
    private boolean nullsAreSortedLow;

    @Invoke(name = "getNumericFunctions")
    @XmlElement(required = true)
    private String numericFunctions;

    @Invoke(name = "othersDeletesAreVisible",
            types = {int.class},
            args = {
                @Literals({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @Literals({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> othersDeletesAreVisible;

    @Invoke(name = "othersInsertsAreVisible",
            types = {int.class},
            args = {
                @Literals({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @Literals({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> othersInsertsAreVisible;

    @Invoke(name = "othersUpdatesAreVisible", types = {int.class},
            args = {
                @Literals({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @Literals({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> othersUpdatesAreVisible;

    @Invoke(name = "ownDeletesAreVisible", types = {int.class},
            args = {
                @Literals({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @Literals({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> ownDeletesAreVisible;

    @Invoke(name = "ownInsertsAreVisible",
            types = {int.class},
            args = {
                @Literals({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @Literals({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> ownInsertsAreVisible;

    @Invoke(name = "ownUpdatesAreVisible", types = {int.class},
            args = {
                @Literals({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @Literals({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> ownUpdatesAreVisible;

    @Invoke(name = "getProcedureTerm")
    @XmlElement(required = true)
    private String procedureTerm;

    @Invoke(name = "isReadOnly")
    @XmlElement(required = true)
    private boolean readOnly;

    @Invoke(name = "getResultSetHoldability")
    @XmlElement(required = true)
    private int resultSetHoldability;

    @Invoke(name = "getRowIdLifetime")
    @XmlTransient
    private RowIdLifetime rowIdLifetime;

    @Invoke(name = "getSchemas")
    @XmlElementRef
    private List<SchemaName> schemaNames;

    @Invoke(name = "getSchemaTerm")
    @XmlElement(required = true)
    private String schemaTerm;

    @Invoke(name = "getSearchStringEscape")
    @XmlElement(required = true)
    private String searchStringEscape;

    @Invoke(name = "getSQLKeywords")
    @XmlElement(required = true)
    private String SQLKewords;

    @Invoke(name = "getSQLStateType")
    @XmlElement(required = true)
    private int SQLStateType;

    @Invoke(name = "storesLowerCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesLowerCaseIdentifiers;

    @Invoke(name = "storesLowerCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesLowerCaseQuotedIdentifiers;

    @Invoke(name = "storesMixedCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesMixedCaseIdentifiers;

    @Invoke(name = "storesMixedCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesMixedCaseQuotedIdentifiers;

    @Invoke(name = "storesUpperCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesUpperCaseIdentifiers;

    @Invoke(name = "storesUpperCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesUpperCaseQuotedIdentifiers;

    @Invoke(name = "getStringFunctions")
    @XmlElement(nillable = true, required = true)
    private String stringFunctions;

    @Invoke(name = "supportsAlterTableWithAddColumn")
    @XmlElement(required = true)
    private boolean supportsAlterTableWithAddColumn;

    @Invoke(name = "supportsAlterTableWithDropColumn")
    @XmlElement(required = true)
    private boolean supportsAlterTableWithDropColumn;

    @Invoke(name = "supportsANSI92EntryLevelSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92EntryLevelSQL;

    @Invoke(name = "supportsANSI92FullSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92FullSQL;

    @Invoke(name = "supportsANSI92IntermediateSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92IntermediateSQL;

    @Invoke(name = "supportsBatchUpdates")
    @XmlElement(required = true)
    private boolean supportsBatchUpdates;

    @Invoke(name = "supportsCatalogsInDataManipulation")
    @XmlElement(required = true)
    private boolean supportsCatalogsInDataManipulation;

    @Invoke(name = "supportsCatalogsInIndexDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInIndexDefinitions;

    @Invoke(name = "supportsCatalogsInPrivilegeDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInPrivilegeDefinitions;

    @Invoke(name = "supportsCatalogsInProcedureCalls")
    @XmlElement(required = true)
    private boolean supportsCatalogsInProcedureCalls;

    @Invoke(name = "supportsCatalogsInTableDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInTableDefinitions;

    @Invoke(name = "supportsColumnAliasing")
    @XmlElement(required = true)
    private boolean supportsColumnAliasing;

    @Invoke(name = "supportsConvert")
    @XmlTransient
    private boolean supportsConvert_;

    @XmlElement
    private List<SDTSDTBoolean> supportsConvert;

    @Invoke(name = "supportsCoreSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsCoreSQLGrammar;

    @Invoke(name = "supportsCorrelatedSubqueries")
    @XmlElement(required = true)
    private boolean supportsCorrelatedSubqueries;

    @Invoke(name = "supportsDataDefinitionAndDataManipulationTransactions")
    @XmlElement(required = true)
    private boolean supportsDataDefinitionAndDataManipulationTransactions;

    @Invoke(name = "supportsDataManipulationTransactionsOnly")
    @XmlElement(required = true)
    private boolean supportsDataManipulationTransactionsOnly;

    @Invoke(name = "supportsDifferentTableCorrelationNames")
    @XmlElement(required = true)
    private boolean supportsDifferentTableCorrelationNames;

    @Invoke(name = "supportsExpressionsInOrderBy")
    @XmlElement(required = true)
    private boolean supportsExpressionsInOrderBy;

    @Invoke(name = "supportsExtendedSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsExtendedSQLGrammar;

    @Invoke(name = "supportsFullOuterJoins")
    @XmlElement(required = true)
    private boolean supportsFullOuterJoins;

    @Invoke(name = "supportsGetGeneratedKeys")
    @XmlElement(required = true)
    private boolean supportsGetGeneratedKeys;

    @Invoke(name = "supportsGroupBy")
    @XmlElement(required = true)
    private boolean supportsGroupBy;

    @Invoke(name = "supportsGroupByBeyondSelect")
    @XmlElement(required = true)
    private boolean supportsGroupByBeyondSelect;

    @Invoke(name = "supportsGroupByUnrelated")
    @XmlElement(required = true)
    private boolean supportsGroupByUnrelated;

    @Invoke(name = "supportsIntegrityEnhancementFacility")
    @XmlElement(required = true)
    private boolean supportsIntegrityEnhancementFacility;

    @Invoke(name = "supportsLikeEscapeClause")
    @XmlElement(required = true)
    private boolean supportsLikeEscapeClause;

    @Invoke(name = "supportsLimitedOuterJoins")
    @XmlElement(required = true)
    private boolean supportsLimitedOuterJoins;

    @Invoke(name = "supportsMinimumSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsMinimumSQLGrammar;

    @Invoke(name = "supportsMixedCaseIdentifiers")
    @XmlElement(required = true)
    private boolean supportsMixedCaseIdentifiers;

    @Invoke(name = "supportsMixedCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean supportsMixedCaseQuotedIdentifiers;

    @Invoke(name = "supportsMultipleOpenResults")
    @XmlElement(required = true)
    private boolean supportsMultipleOpenResults;

    @Invoke(name = "supportsMultipleResultSets")
    @XmlElement(required = true)
    private boolean supportsMultipleResultSets;

    @Invoke(name = "supportsMultipleTransactions")
    @XmlElement(required = true)
    private boolean supportsMultipleTransactions;

    @Invoke(name = "supportsNamedParameters")
    @XmlElement(required = true)
    private boolean supportsNamedParameters;

    @Invoke(name = "supportsNonNullableColumns")
    @XmlElement(required = true)
    private boolean supportsNonNullableColumns;

    @Invoke(name = "supportsOpenCursorsAcrossCommit")
    @XmlElement(required = true)
    private boolean supportsOpenCursorsAcrossCommit;

    @Invoke(name = "supportsOpenCursorsAcrossRollback")
    @XmlElement(required = true)
    private boolean supportsOpenCursorsAcrossRollback;

    @Invoke(name = "supportsOpenStatementsAcrossCommit")
    @XmlElement(required = true)
    private boolean supportsOpenStatementsAcrossCommit;

    @Invoke(name = "supportsOpenStatementsAcrossRollback")
    @XmlElement(required = true)
    private boolean supportsOpenStatementsAcrossRollback;

    @Invoke(name = "supportsOrderByUnrelated")
    @XmlElement(required = true)
    private boolean supportsOrderByUnrelated;

    @Invoke(name = "supportsOuterJoins")
    @XmlElement(required = true)
    private boolean supportsOuterJoins;

    @Invoke(name = "supportsPositionedDelete")
    @XmlElement(required = true)
    private boolean supportsPositionedDelete;

    @Invoke(name = "supportsPositionedUpdate")
    @XmlElement(required = true)
    private boolean supportsPositionedUpdate;

    @Invoke(name = "supportsRefCursors")
    @XmlElement(required = true)
    private boolean supportsRefCursors;

    @Invoke(name = "supportsResultSetConcurrency",
            types = {int.class, int.class},
            args = {
                @Literals({"1003", "1007"}), // TYPE_FORWARD_ONLY/CONCUR_READ_ONLY
                @Literals({"1003", "1008"}), // TYPE_FORWARD_ONLY/CONCUR_UPDATABLE
                @Literals({"1004", "1007"}), // TYPE_SCROLL_INSENSITIVE/CONCUR_READ_ONLY
                @Literals({"1004", "1008"}), // TYPE_SCROLL_INSENSITIVE/CONCUR_UPDATABLE
                @Literals({"1005", "1007"}), // TYPE_SCROLL_SENSITIVE/CONCUR_READ_ONLY
                @Literals({"1005", "1008"}) // TYPE_SCROLL_SENSITIVE/CONCUR_UPDATABLE
            }
    )
    @XmlElement
    private List<RSTRSCBoolean> supportsResultSetConcurrency;

    @Invoke(name = "supportsResultSetHoldability",
            types = {int.class},
            args = {
                @Literals({"1"}), // CLOSE_CURSORS_AT_COMMIT
                @Literals({"2"}) // HOLD_CURSORS_OVER_COMMIT
            }
    )
    @XmlElement
    private List<RSHBoolean> supportsResultSetHoldability;

    @Invoke(name = "supportsResultSetType",
            types = {int.class},
            args = {
                @Literals({"1003"}), // TYPE_FORWARD_ONLY
                @Literals({"1004"}), // TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> supportsResultSetType;

    @Invoke(name = "supportsSavepoints")
    @XmlElement(required = true)
    private boolean supportsSavepoints;

    @Invoke(name = "supportsSchemasInDataManipulation")
    @XmlElement(required = true)
    private boolean supportsSchemasInDataManipulation;

    @Invoke(name = "supportsSchemasInIndexDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInIndexDefinitions;

    @Invoke(name = "supportsSchemasInPrivilegeDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInPrivilegeDefinitions;

    @Invoke(name = "supportsSchemasInProcedureCalls")
    @XmlElement(required = true)
    private boolean supportsSchemasInProcedureCalls;

    @Invoke(name = "supportsSchemasInTableDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInTableDefinitions;

    @Invoke(name = "supportsSelectForUpdate")
    @XmlElement(required = true)
    private boolean supportsSelectForUpdate;

    @Invoke(name = "supportsStatementPooling")
    @XmlElement(required = true)
    private boolean supportsStatementPooling;

    @Invoke(name = "supportsStoredFunctionsUsingCallSyntax")
    @XmlElement(required = true)
    private boolean supportsStoredFunctionsUsingCallSyntax;

    @Invoke(name = "supportsStoredProcedures")
    @XmlElement(required = true)
    private boolean supportsStoredProcedures;

    @Invoke(name = "supportsSubqueriesInComparisons")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInComparisons;

    @Invoke(name = "supportsSubqueriesInExists")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInExists;

    @Invoke(name = "supportsSubqueriesInIns")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInIns;

    @Invoke(name = "supportsSubqueriesInQuantifieds")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInQuantifieds;

    @Invoke(name = "supportsTableCorrelationNames")
    @XmlElement(required = true)
    private boolean supportsTableCorrelationNames;

    @Invoke(name = "supportsTransactionIsolationLevel",
            types = {int.class},
            args = {
                @Literals({"0"}), // TRANSACTION_NONE
                @Literals({"1"}), // TRANSACTION_READ_UNCOMMITTED
                @Literals({"2"}), // TRANSACTION_READ_COMMITTED
                @Literals({"4"}), // TRANSACTION_REPEATABLE_READ
                @Literals({"8"}) // TRANSACTION_SERIALIZABLE
            }
    )
    @XmlElement
    private List<TILBoolean> supportsTransactionIsolationLevel;

    @Invoke(name = "supportsTransactions")
    @XmlElement(required = true)
    private boolean supportsTransactions;

    @Invoke(name = "supportsUnion")
    @XmlElement(required = true)
    private boolean supportsUnion;

    @Invoke(name = "supportsUnionAll")
    @XmlElement(required = true)
    private boolean supportsUnionAll;

    @Invoke(name = "getSystemFunctions")
    @XmlElement(required = true)
    private String systemFunctions;

    @Invoke(name = "getTableTypes")
    @XmlElementRef
    private List<TableType> tableTypes;

    @Invoke(name = "getTimeDateFunctions")
    @XmlElement(required = true)
    private String timeDateFunctions;

    @Invoke(name = "getTypeInfo")
    @XmlElementRef
    private List<TypeInfo> typeInfo;

    @Invoke(name = "updatesAreDetected",
            types = {int.class},
            args = {
                @Literals({"1003"}), // TYPE_FORWARD_ONLY
                @Literals({"1004"}), // TYPE_SCROLL_INSENSITIVE
                @Literals({"1005"}) // TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> updatesAreDetected;

    @Invoke(name = "getUserName")
    @XmlElement(required = true)
    private String userName;

    @Invoke(name = "getURL")
    @XmlElement(required = true)
    private String URL;

    @Invoke(name = "usesLocalFilePerTable")
    @XmlElement(required = true)
    private boolean usesLocalFilePerTable;

    @Invoke(name = "usesLocalFiles")
    @XmlElement(required = true)
    private boolean usesLocalFiles;
}
