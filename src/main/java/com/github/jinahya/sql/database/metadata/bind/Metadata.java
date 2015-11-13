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

        final List<Table> list = new ArrayList<Table>();

        for (final Catalog catalog : getCatalogs()) {
            list.addAll(catalog.getTables());
        }

        return list;
    }


    public boolean isAllProceduresAreCallable() {
        return allProceduresAreCallable;
    }


    public void setAllProceduresAreCallable(boolean allProceduresAreCallable) {
        this.allProceduresAreCallable = allProceduresAreCallable;
    }


    public boolean isAllTablesAreSelectable() {
        return allTablesAreSelectable;
    }


    public void setAllTablesAreSelectable(boolean allTablesAreSelectable) {
        this.allTablesAreSelectable = allTablesAreSelectable;
    }


    public boolean isAutoCommitFailureClosesAllResultSets() {
        return autoCommitFailureClosesAllResultSets;
    }


    public void setAutoCommitFailureClosesAllResultSets(boolean autoCommitFailureClosesAllResultSets) {
        this.autoCommitFailureClosesAllResultSets = autoCommitFailureClosesAllResultSets;
    }


    public List<Catalog> getCatalogs() {
        return catalogs;
    }


    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }


    public boolean isCatalogAtStart() {
        return catalogAtStart;
    }


    public void setCatalogAtStart(boolean catalogAtStart) {
        this.catalogAtStart = catalogAtStart;
    }


    public String getCatalogSeparator() {
        return catalogSeparator;
    }


    public void setCatalogSeparator(String catalogSeparator) {
        this.catalogSeparator = catalogSeparator;
    }


    public String getCatalogTerm() {
        return catalogTerm;
    }


    public void setCatalogTerm(String catalogTerm) {
        this.catalogTerm = catalogTerm;
    }


    public List<ClientInfoProperty> getClientInfoProperties() {
        return clientInfoProperties;
    }


    public void setClientInfoProperties(List<ClientInfoProperty> clientInfoProperties) {
        this.clientInfoProperties = clientInfoProperties;
    }


    @Deprecated
    public Connection getConnection() {
        return connection;
    }


    @Deprecated
    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<CrossReference> getCrossReferences() {
        return crossReferences;
    }


    @Override
    public void setCrossReferences(List<CrossReference> crossReferences) {
        this.crossReferences = crossReferences;
    }


    public boolean isDataDefinitionCausesTransactionCommit() {
        return dataDefinitionCausesTransactionCommit;
    }


    public void setDataDefinitionCausesTransactionCommit(boolean dataDefinitionCausesTransactionCommit) {
        this.dataDefinitionCausesTransactionCommit = dataDefinitionCausesTransactionCommit;
    }


    public boolean isDataDefinitionIgnoredInTransactions() {
        return dataDefinitionIgnoredInTransactions;
    }


    public void setDataDefinitionIgnoredInTransactions(boolean dataDefinitionIgnoredInTransactions) {
        this.dataDefinitionIgnoredInTransactions = dataDefinitionIgnoredInTransactions;
    }


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


    public void setDoesMaxRowSizeIncludeBlobs(boolean doesMaxRowSizeIncludeBlobs) {
        this.doesMaxRowSizeIncludeBlobs = doesMaxRowSizeIncludeBlobs;
    }


    public int getDriverMajorVersion() {
        return driverMajorVersion;
    }


    public void setDriverMajorVersion(int driverMajorVersion) {
        this.driverMajorVersion = driverMajorVersion;
    }


    public int getDriverMinorVersion() {
        return driverMinorVersion;
    }


    public void setDriverMinorVersion(int driverMinorVersion) {
        this.driverMinorVersion = driverMinorVersion;
    }


    public String getDriverName() {
        return driverName;
    }


    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }


    public String getDriverVersion() {
        return driverVersion;
    }


    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }


    public String getExtraNameCharacters() {
        return extraNameCharacters;
    }


    public void setExtraNameCharacters(String extraNameCharacters) {
        this.extraNameCharacters = extraNameCharacters;
    }


    public boolean isGeneratedKeyAlwaysReturned() {
        return generatedKeyAlwaysReturned;
    }


    public void setGeneratedKeyAlwaysReturned(boolean generatedKeyAlwaysReturned) {
        this.generatedKeyAlwaysReturned = generatedKeyAlwaysReturned;
    }


    public String getIdentifierQuoteString() {
        return identifierQuoteString;
    }


    public void setIdentifierQuoteString(String identifierQuoteString) {
        this.identifierQuoteString = identifierQuoteString;
    }


    public List<RSTBoolean> getInsertsAreDetected() {
        return insertsAreDetected;
    }


    public void setInsertsAreDetected(List<RSTBoolean> insertsAreDetected) {
        this.insertsAreDetected = insertsAreDetected;
    }


    public int getJdbcMajorVersion() {
        return jdbcMajorVersion;
    }


    public void setJdbcMajorVersion(int jdbcMajorVersion) {
        this.jdbcMajorVersion = jdbcMajorVersion;
    }


    public int getJdbcMinorVersion() {
        return jdbcMinorVersion;
    }


    public void setJdbcMinorVersion(int jdbcMinorVersion) {
        this.jdbcMinorVersion = jdbcMinorVersion;
    }


    public boolean isLocatorsUpdateCopy() {
        return locatorsUpdateCopy;
    }


    public void setLocatorsUpdateCopy(boolean locatorsUpdateCopy) {
        this.locatorsUpdateCopy = locatorsUpdateCopy;
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


    public int getMaxCharLiteralLength() {
        return maxCharLiteralLength;
    }


    public void setMaxCharLiteralLength(int maxCharLiteralLength) {
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


    public boolean isNullPlusNonNullIsNull() {
        return nullPlusNonNullIsNull;
    }


    public void setNullPlusNonNullIsNull(boolean nullPlusNonNullIsNull) {
        this.nullPlusNonNullIsNull = nullPlusNonNullIsNull;
    }


    public boolean isNullsAreSortedAtEnd() {
        return nullsAreSortedAtEnd;
    }


    public void setNullsAreSortedAtEnd(boolean nullsAreSortedAtEnd) {
        this.nullsAreSortedAtEnd = nullsAreSortedAtEnd;
    }


    public boolean isNullsAreSortedAtStart() {
        return nullsAreSortedAtStart;
    }


    public void setNullsAreSortedAtStart(boolean nullsAreSortedAtStart) {
        this.nullsAreSortedAtStart = nullsAreSortedAtStart;
    }


    public boolean isNullsAreSortedHigh() {
        return nullsAreSortedHigh;
    }


    public void setNullsAreSortedHigh(boolean nullsAreSortedHigh) {
        this.nullsAreSortedHigh = nullsAreSortedHigh;
    }


    public boolean isNullsAreSortedLow() {
        return nullsAreSortedLow;
    }


    public void setNullsAreSortedLow(boolean nullsAreSortedLow) {
        this.nullsAreSortedLow = nullsAreSortedLow;
    }


    public String getNumericFunctions() {
        return numericFunctions;
    }


    public void setNumericFunctions(String numericFunctions) {
        this.numericFunctions = numericFunctions;
    }


    public List<RSTBoolean> getOthersDeletesAreVisible() {
        return othersDeletesAreVisible;
    }


    public void setOthersDeletesAreVisible(List<RSTBoolean> othersDeletesAreVisible) {
        this.othersDeletesAreVisible = othersDeletesAreVisible;
    }


    public List<RSTBoolean> getOthersInsertsAreVisible() {
        return othersInsertsAreVisible;
    }


    public void setOthersInsertsAreVisible(List<RSTBoolean> othersInsertsAreVisible) {
        this.othersInsertsAreVisible = othersInsertsAreVisible;
    }


    public List<RSTBoolean> getOthersUpdatesAreVisible() {
        return othersUpdatesAreVisible;
    }


    public void setOthersUpdatesAreVisible(List<RSTBoolean> othersUpdatesAreVisible) {
        this.othersUpdatesAreVisible = othersUpdatesAreVisible;
    }


    public List<RSTBoolean> getOwnDeletesAreVisible() {
        return ownDeletesAreVisible;
    }


    public void setOwnDeletesAreVisible(List<RSTBoolean> ownDeletesAreVisible) {
        this.ownDeletesAreVisible = ownDeletesAreVisible;
    }


    public List<RSTBoolean> getOwnInsertsAreVisible() {
        return ownInsertsAreVisible;
    }


    public void setOwnInsertsAreVisible(List<RSTBoolean> ownInsertsAreVisible) {
        this.ownInsertsAreVisible = ownInsertsAreVisible;
    }


    public List<RSTBoolean> getOwnUpdatesAreVisible() {
        return ownUpdatesAreVisible;
    }


    public void setOwnUpdatesAreVisible(List<RSTBoolean> ownUpdatesAreVisible) {
        this.ownUpdatesAreVisible = ownUpdatesAreVisible;
    }


    public String getProcedureTerm() {
        return procedureTerm;
    }


    public void setProcedureTerm(String procedureTerm) {
        this.procedureTerm = procedureTerm;
    }


    public boolean isReadOnly() {
        return readOnly;
    }


    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }


    public int getResultSetHoldability() {
        return resultSetHoldability;
    }


    public void setResultSetHoldability(int resultSetHoldability) {
        this.resultSetHoldability = resultSetHoldability;
    }


    public RowIdLifetime getRowIdLifetime() {
        return rowIdLifetime;
    }


    public void setRowIdLifetime(RowIdLifetime rowIdLifetime) {
        this.rowIdLifetime = rowIdLifetime;
    }


    public String getSearchStringEscape() {
        return searchStringEscape;
    }


    public void setSearchStringEscape(String searchStringEscape) {
        this.searchStringEscape = searchStringEscape;
    }


    public String getSqlKewords() {
        return sqlKewords;
    }


    public void setSqlKewords(String sqlKewords) {
        this.sqlKewords = sqlKewords;
    }


    public int getSqlStateType() {
        return sqlStateType;
    }


    public void setSqlStateType(int sqlStateType) {
        this.sqlStateType = sqlStateType;
    }


    public boolean isStoresLowerCaseIdentifiers() {
        return storesLowerCaseIdentifiers;
    }


    public void setStoresLowerCaseIdentifiers(boolean storesLowerCaseIdentifiers) {
        this.storesLowerCaseIdentifiers = storesLowerCaseIdentifiers;
    }


    public boolean isStoresLowerCaseQuotedIdentifiers() {
        return storesLowerCaseQuotedIdentifiers;
    }


    public void setStoresLowerCaseQuotedIdentifiers(boolean storesLowerCaseQuotedIdentifiers) {
        this.storesLowerCaseQuotedIdentifiers = storesLowerCaseQuotedIdentifiers;
    }


    public boolean isStoresMixedCaseIdentifiers() {
        return storesMixedCaseIdentifiers;
    }


    public void setStoresMixedCaseIdentifiers(boolean storesMixedCaseIdentifiers) {
        this.storesMixedCaseIdentifiers = storesMixedCaseIdentifiers;
    }


    public boolean isStoresMixedCaseQuotedIdentifiers() {
        return storesMixedCaseQuotedIdentifiers;
    }


    public void setStoresMixedCaseQuotedIdentifiers(boolean storesMixedCaseQuotedIdentifiers) {
        this.storesMixedCaseQuotedIdentifiers = storesMixedCaseQuotedIdentifiers;
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


    public List<SDTSDTBoolean> getSupportsConvert() {
        return supportsConvert;
    }


    public void setSupportsConvert(List<SDTSDTBoolean> supportsConvert) {
        this.supportsConvert = supportsConvert;
    }


    public boolean isSupportsCoreSQLGrammar() {
        return supportsCoreSQLGrammar;
    }


    public void setSupportsCoreSQLGrammar(boolean supportsCoreSQLGrammar) {
        this.supportsCoreSQLGrammar = supportsCoreSQLGrammar;
    }


    public boolean isSupportsCorrelatedSubqueries() {
        return supportsCorrelatedSubqueries;
    }


    public void setSupportsCorrelatedSubqueries(boolean supportsCorrelatedSubqueries) {
        this.supportsCorrelatedSubqueries = supportsCorrelatedSubqueries;
    }


    public boolean isSupportsDataDefinitionAndDataManipulationTransactions() {
        return supportsDataDefinitionAndDataManipulationTransactions;
    }


    public void setSupportsDataDefinitionAndDataManipulationTransactions(boolean supportsDataDefinitionAndDataManipulationTransactions) {
        this.supportsDataDefinitionAndDataManipulationTransactions = supportsDataDefinitionAndDataManipulationTransactions;
    }


    public boolean isSupportsDataManipulationTransactionsOnly() {
        return supportsDataManipulationTransactionsOnly;
    }


    public void setSupportsDataManipulationTransactionsOnly(boolean supportsDataManipulationTransactionsOnly) {
        this.supportsDataManipulationTransactionsOnly = supportsDataManipulationTransactionsOnly;
    }


    public boolean isSupportsDifferentTableCorrelationNames() {
        return supportsDifferentTableCorrelationNames;
    }


    public void setSupportsDifferentTableCorrelationNames(boolean supportsDifferentTableCorrelationNames) {
        this.supportsDifferentTableCorrelationNames = supportsDifferentTableCorrelationNames;
    }


    public boolean isSupportsExpressionsInOrderBy() {
        return supportsExpressionsInOrderBy;
    }


    public void setSupportsExpressionsInOrderBy(boolean supportsExpressionsInOrderBy) {
        this.supportsExpressionsInOrderBy = supportsExpressionsInOrderBy;
    }


    public boolean isSupportsExtendedSQLGrammar() {
        return supportsExtendedSQLGrammar;
    }


    public void setSupportsExtendedSQLGrammar(boolean supportsExtendedSQLGrammar) {
        this.supportsExtendedSQLGrammar = supportsExtendedSQLGrammar;
    }


    public boolean isSupportsFullOuterJoins() {
        return supportsFullOuterJoins;
    }


    public void setSupportsFullOuterJoins(boolean supportsFullOuterJoins) {
        this.supportsFullOuterJoins = supportsFullOuterJoins;
    }


    public boolean isSupportsGetGeneratedKeys() {
        return supportsGetGeneratedKeys;
    }


    public void setSupportsGetGeneratedKeys(boolean supportsGetGeneratedKeys) {
        this.supportsGetGeneratedKeys = supportsGetGeneratedKeys;
    }


    public boolean isSupportsGroupBy() {
        return supportsGroupBy;
    }


    public void setSupportsGroupBy(boolean supportsGroupBy) {
        this.supportsGroupBy = supportsGroupBy;
    }


    public boolean isSupportsGroupByBeyondSelect() {
        return supportsGroupByBeyondSelect;
    }


    public void setSupportsGroupByBeyondSelect(boolean supportsGroupByBeyondSelect) {
        this.supportsGroupByBeyondSelect = supportsGroupByBeyondSelect;
    }


    public boolean isSupportsGroupByUnrelated() {
        return supportsGroupByUnrelated;
    }


    public void setSupportsGroupByUnrelated(boolean supportsGroupByUnrelated) {
        this.supportsGroupByUnrelated = supportsGroupByUnrelated;
    }


    public boolean isSupportsIntegrityEnhancementFacility() {
        return supportsIntegrityEnhancementFacility;
    }


    public void setSupportsIntegrityEnhancementFacility(boolean supportsIntegrityEnhancementFacility) {
        this.supportsIntegrityEnhancementFacility = supportsIntegrityEnhancementFacility;
    }


    public boolean isSupportsLikeEscapeClause() {
        return supportsLikeEscapeClause;
    }


    public void setSupportsLikeEscapeClause(boolean supportsLikeEscapeClause) {
        this.supportsLikeEscapeClause = supportsLikeEscapeClause;
    }


    public boolean isSupportsLimitedOuterJoins() {
        return supportsLimitedOuterJoins;
    }


    public void setSupportsLimitedOuterJoins(boolean supportsLimitedOuterJoins) {
        this.supportsLimitedOuterJoins = supportsLimitedOuterJoins;
    }


    public boolean isSupportsMinimumSQLGrammar() {
        return supportsMinimumSQLGrammar;
    }


    public void setSupportsMinimumSQLGrammar(boolean supportsMinimumSQLGrammar) {
        this.supportsMinimumSQLGrammar = supportsMinimumSQLGrammar;
    }


    public boolean isSupportsMixedCaseIdentifiers() {
        return supportsMixedCaseIdentifiers;
    }


    public void setSupportsMixedCaseIdentifiers(boolean supportsMixedCaseIdentifiers) {
        this.supportsMixedCaseIdentifiers = supportsMixedCaseIdentifiers;
    }


    public boolean isSupportsMixedCaseQuotedIdentifiers() {
        return supportsMixedCaseQuotedIdentifiers;
    }


    public void setSupportsMixedCaseQuotedIdentifiers(boolean supportsMixedCaseQuotedIdentifiers) {
        this.supportsMixedCaseQuotedIdentifiers = supportsMixedCaseQuotedIdentifiers;
    }


    public boolean isSupportsMultipleOpenResults() {
        return supportsMultipleOpenResults;
    }


    public void setSupportsMultipleOpenResults(boolean supportsMultipleOpenResults) {
        this.supportsMultipleOpenResults = supportsMultipleOpenResults;
    }


    public boolean isSupportsMultipleResultSets() {
        return supportsMultipleResultSets;
    }


    public void setSupportsMultipleResultSets(boolean supportsMultipleResultSets) {
        this.supportsMultipleResultSets = supportsMultipleResultSets;
    }


    public boolean isSupportsMultipleTransactions() {
        return supportsMultipleTransactions;
    }


    public void setSupportsMultipleTransactions(boolean supportsMultipleTransactions) {
        this.supportsMultipleTransactions = supportsMultipleTransactions;
    }


    public boolean isSupportsNamedParameters() {
        return supportsNamedParameters;
    }


    public void setSupportsNamedParameters(boolean supportsNamedParameters) {
        this.supportsNamedParameters = supportsNamedParameters;
    }


    public boolean isSupportsNonNullableColumns() {
        return supportsNonNullableColumns;
    }


    public void setSupportsNonNullableColumns(boolean supportsNonNullableColumns) {
        this.supportsNonNullableColumns = supportsNonNullableColumns;
    }


    public boolean isSupportsOpenCursorsAcrossCommit() {
        return supportsOpenCursorsAcrossCommit;
    }


    public void setSupportsOpenCursorsAcrossCommit(boolean supportsOpenCursorsAcrossCommit) {
        this.supportsOpenCursorsAcrossCommit = supportsOpenCursorsAcrossCommit;
    }


    public boolean isSupportsOpenCursorsAcrossRollback() {
        return supportsOpenCursorsAcrossRollback;
    }


    public void setSupportsOpenCursorsAcrossRollback(boolean supportsOpenCursorsAcrossRollback) {
        this.supportsOpenCursorsAcrossRollback = supportsOpenCursorsAcrossRollback;
    }


    public boolean isSupportsOpenStatementsAcrossCommit() {
        return supportsOpenStatementsAcrossCommit;
    }


    public void setSupportsOpenStatementsAcrossCommit(boolean supportsOpenStatementsAcrossCommit) {
        this.supportsOpenStatementsAcrossCommit = supportsOpenStatementsAcrossCommit;
    }


    public boolean isSupportsOpenStatementsAcrossRollback() {
        return supportsOpenStatementsAcrossRollback;
    }


    public void setSupportsOpenStatementsAcrossRollback(boolean supportsOpenStatementsAcrossRollback) {
        this.supportsOpenStatementsAcrossRollback = supportsOpenStatementsAcrossRollback;
    }


    public boolean isSupportsOrderByUnrelated() {
        return supportsOrderByUnrelated;
    }


    public void setSupportsOrderByUnrelated(boolean supportsOrderByUnrelated) {
        this.supportsOrderByUnrelated = supportsOrderByUnrelated;
    }


    public boolean isSupportsOuterJoins() {
        return supportsOuterJoins;
    }


    public void setSupportsOuterJoins(boolean supportsOuterJoins) {
        this.supportsOuterJoins = supportsOuterJoins;
    }


    public boolean isSupportsPositionedDelete() {
        return supportsPositionedDelete;
    }


    public void setSupportsPositionedDelete(boolean supportsPositionedDelete) {
        this.supportsPositionedDelete = supportsPositionedDelete;
    }


    public boolean isSupportsPositionedUpdate() {
        return supportsPositionedUpdate;
    }


    public void setSupportsPositionedUpdate(boolean supportsPositionedUpdate) {
        this.supportsPositionedUpdate = supportsPositionedUpdate;
    }


    public boolean isSupportsRefCursors() {
        return supportsRefCursors;
    }


    public void setSupportsRefCursors(boolean supportsRefCursors) {
        this.supportsRefCursors = supportsRefCursors;
    }


    public List<RSTRSCBoolean> getSupportsResultSetConcurrency() {
        return supportsResultSetConcurrency;
    }


    public void setSupportsResultSetConcurrency(List<RSTRSCBoolean> supportsResultSetConcurrency) {
        this.supportsResultSetConcurrency = supportsResultSetConcurrency;
    }


    public List<RSHBoolean> getSupportsResultSetHoldability() {
        return supportsResultSetHoldability;
    }


    public void setSupportsResultSetHoldability(List<RSHBoolean> supportsResultSetHoldability) {
        this.supportsResultSetHoldability = supportsResultSetHoldability;
    }


    public List<RSTBoolean> getSupportsResultSetType() {
        return supportsResultSetType;
    }


    public void setSupportsResultSetType(List<RSTBoolean> supportsResultSetType) {
        this.supportsResultSetType = supportsResultSetType;
    }


    public boolean isSupportsSavepoints() {
        return supportsSavepoints;
    }


    public void setSupportsSavepoints(boolean supportsSavepoints) {
        this.supportsSavepoints = supportsSavepoints;
    }


    public boolean isSupportsSchemasInDataManipulation() {
        return supportsSchemasInDataManipulation;
    }


    public void setSupportsSchemasInDataManipulation(boolean supportsSchemasInDataManipulation) {
        this.supportsSchemasInDataManipulation = supportsSchemasInDataManipulation;
    }


    public boolean isSupportsSchemasInIndexDefinitions() {
        return supportsSchemasInIndexDefinitions;
    }


    public void setSupportsSchemasInIndexDefinitions(boolean supportsSchemasInIndexDefinitions) {
        this.supportsSchemasInIndexDefinitions = supportsSchemasInIndexDefinitions;
    }


    public boolean isSupportsSchemasInPrivilegeDefinitions() {
        return supportsSchemasInPrivilegeDefinitions;
    }


    public void setSupportsSchemasInPrivilegeDefinitions(boolean supportsSchemasInPrivilegeDefinitions) {
        this.supportsSchemasInPrivilegeDefinitions = supportsSchemasInPrivilegeDefinitions;
    }


    public boolean isSupportsSchemasInProcedureCalls() {
        return supportsSchemasInProcedureCalls;
    }


    public void setSupportsSchemasInProcedureCalls(boolean supportsSchemasInProcedureCalls) {
        this.supportsSchemasInProcedureCalls = supportsSchemasInProcedureCalls;
    }


    public boolean isSupportsSchemasInTableDefinitions() {
        return supportsSchemasInTableDefinitions;
    }


    public void setSupportsSchemasInTableDefinitions(boolean supportsSchemasInTableDefinitions) {
        this.supportsSchemasInTableDefinitions = supportsSchemasInTableDefinitions;
    }


    public boolean isSupportsSelectForUpdate() {
        return supportsSelectForUpdate;
    }


    public void setSupportsSelectForUpdate(boolean supportsSelectForUpdate) {
        this.supportsSelectForUpdate = supportsSelectForUpdate;
    }


    public boolean isSupportsStatementPooling() {
        return supportsStatementPooling;
    }


    public void setSupportsStatementPooling(boolean supportsStatementPooling) {
        this.supportsStatementPooling = supportsStatementPooling;
    }


    public boolean isSupportsStoredFunctionsUsingCallSyntax() {
        return supportsStoredFunctionsUsingCallSyntax;
    }


    public void setSupportsStoredFunctionsUsingCallSyntax(boolean supportsStoredFunctionsUsingCallSyntax) {
        this.supportsStoredFunctionsUsingCallSyntax = supportsStoredFunctionsUsingCallSyntax;
    }


    public boolean isSupportsStoredProcedures() {
        return supportsStoredProcedures;
    }


    public void setSupportsStoredProcedures(boolean supportsStoredProcedures) {
        this.supportsStoredProcedures = supportsStoredProcedures;
    }


    public boolean isSupportsSubqueriesInComparisons() {
        return supportsSubqueriesInComparisons;
    }


    public void setSupportsSubqueriesInComparisons(boolean supportsSubqueriesInComparisons) {
        this.supportsSubqueriesInComparisons = supportsSubqueriesInComparisons;
    }


    public boolean isSupportsSubqueriesInExists() {
        return supportsSubqueriesInExists;
    }


    public void setSupportsSubqueriesInExists(boolean supportsSubqueriesInExists) {
        this.supportsSubqueriesInExists = supportsSubqueriesInExists;
    }


    public boolean isSupportsSubqueriesInIns() {
        return supportsSubqueriesInIns;
    }


    public void setSupportsSubqueriesInIns(boolean supportsSubqueriesInIns) {
        this.supportsSubqueriesInIns = supportsSubqueriesInIns;
    }


    public boolean isSupportsSubqueriesInQuantifieds() {
        return supportsSubqueriesInQuantifieds;
    }


    public void setSupportsSubqueriesInQuantifieds(boolean supportsSubqueriesInQuantifieds) {
        this.supportsSubqueriesInQuantifieds = supportsSubqueriesInQuantifieds;
    }


    public boolean isSupportsTableCorrelationNames() {
        return supportsTableCorrelationNames;
    }


    public void setSupportsTableCorrelationNames(boolean supportsTableCorrelationNames) {
        this.supportsTableCorrelationNames = supportsTableCorrelationNames;
    }


    public List<TILBoolean> getSupportsTransactionIsolationLevel() {
        return supportsTransactionIsolationLevel;
    }


    public void setSupportsTransactionIsolationLevel(List<TILBoolean> supportsTransactionIsolationLevel) {
        this.supportsTransactionIsolationLevel = supportsTransactionIsolationLevel;
    }


    public boolean isSupportsTransactions() {
        return supportsTransactions;
    }


    public void setSupportsTransactions(boolean supportsTransactions) {
        this.supportsTransactions = supportsTransactions;
    }


    public boolean isSupportsUnion() {
        return supportsUnion;
    }


    public void setSupportsUnion(boolean supportsUnion) {
        this.supportsUnion = supportsUnion;
    }


    public boolean isSupportsUnionAll() {
        return supportsUnionAll;
    }


    public void setSupportsUnionAll(boolean supportsUnionAll) {
        this.supportsUnionAll = supportsUnionAll;
    }


    public String getSystemFunctions() {
        return systemFunctions;
    }


    public void setSystemFunctions(String systemFunctions) {
        this.systemFunctions = systemFunctions;
    }


    public String getTimeDateFunctions() {
        return timeDateFunctions;
    }


    public void setTimeDateFunctions(String timeDateFunctions) {
        this.timeDateFunctions = timeDateFunctions;
    }


    public List<SchemaName> getSchemaNames() {
        return schemaNames;
    }


    public void setSchemaNames(List<SchemaName> schemaNames) {
        this.schemaNames = schemaNames;
    }


    public String getSchemaTerm() {
        return schemaTerm;
    }


    public void setSchemaTerm(String schemaTerm) {
        this.schemaTerm = schemaTerm;
    }


    public List<TableType> getTableTypes() {
        return tableTypes;
    }


    public void setTableTypes(List<TableType> tableTypes) {
        this.tableTypes = tableTypes;
    }


    public List<TypeInfo> getTypeInfo() {
        return typeInfo;
    }


    public void setTypeInfo(List<TypeInfo> typeInfo) {
        this.typeInfo = typeInfo;
    }


    public List<RSTBoolean> getUpdatesAreDetected() {
        return updatesAreDetected;
    }


    public void setUpdatesAreDetected(List<RSTBoolean> updatesAreDetected) {
        this.updatesAreDetected = updatesAreDetected;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public boolean isUsesLocalFilePerTable() {
        return usesLocalFilePerTable;
    }


    public void setUsesLocalFilePerTable(boolean usesLocalFilePerTable) {
        this.usesLocalFilePerTable = usesLocalFilePerTable;
    }


    public boolean isUsesLocalFiles() {
        return usesLocalFiles;
    }


    public void setUsesLocalFiles(boolean usesLocalFiles) {
        this.usesLocalFiles = usesLocalFiles;
    }


    // -------------------------------------------------------------------------
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


    @Invocation(name = "isCatalogAtStart")
    @XmlElement(required = true)
    private boolean catalogAtStart;


    @Invocation(name = "getCatalogSeparator")
    @XmlElement(nillable = true, required = true)
    private String catalogSeparator;


    @Invocation(name = "getCatalogTerm")
    @XmlElement(nillable = true, required = true)
    private String catalogTerm;


    @Invocation(name = "getClientInfoProperties")
    @XmlElementRef
    private List<ClientInfoProperty> clientInfoProperties;


    @Invocation(name = "getConnection")
    @XmlTransient
    private Connection connection;


    @XmlElementRef
    private List<CrossReference> crossReferences;


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
    @XmlElement(required = true)
    private String databaseProductName;


    @Invocation(name = "getDatabaseProductVersion")
    @XmlElement(required = true)
    private String databaseProductVersion;


    @Invocation(name = "getDefaultTransactionIsolation")
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
    @XmlElement
    private List<RSTBoolean> deletesAreDetected;


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
    @XmlElement(required = true)
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
    @XmlElement
    private List<RSTBoolean> insertsAreDetected;


    @Invocation(name = "getJDBCMajorVersion")
    @XmlElement(required = true)
    private int jdbcMajorVersion;


    @Invocation(name = "getJDBCMinorVersion")
    @XmlElement(required = true)
    private int jdbcMinorVersion;


    @Invocation(name = "locatorsUpdateCopy")
    @XmlElement(required = true)
    private boolean locatorsUpdateCopy;


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


    @Invocation(name = "nullPlusNonNullIsNull")
    @XmlElement(required = true)
    private boolean nullPlusNonNullIsNull;


    @Invocation(name = "nullsAreSortedAtEnd")
    @XmlElement(required = true)
    private boolean nullsAreSortedAtEnd;


    @Invocation(name = "nullsAreSortedAtStart")
    @XmlElement(required = true)
    private boolean nullsAreSortedAtStart;


    @Invocation(name = "nullsAreSortedHigh")
    @XmlElement(required = true)
    private boolean nullsAreSortedHigh;


    @Invocation(name = "nullsAreSortedLow")
    @XmlElement(required = true)
    private boolean nullsAreSortedLow;


    @Invocation(name = "getNumericFunctions")
    @XmlElement(required = true)
    private String numericFunctions;


    @Invocation(
        name = "othersDeletesAreVisible",
        types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElement
    private List<RSTBoolean> othersDeletesAreVisible;


    @Invocation(
        name = "othersInsertsAreVisible",
        types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElement
    private List<RSTBoolean> othersInsertsAreVisible;


    @Invocation(
        name = "othersUpdatesAreVisible", types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElement
    private List<RSTBoolean> othersUpdatesAreVisible;


    @Invocation(
        name = "ownDeletesAreVisible", types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElement
    private List<RSTBoolean> ownDeletesAreVisible;


    @Invocation(
        name = "ownInsertsAreVisible",
        types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElement
    private List<RSTBoolean> ownInsertsAreVisible;


    @Invocation(
        name = "ownUpdatesAreVisible", types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElement
    private List<RSTBoolean> ownUpdatesAreVisible;


    @Invocation(name = "getProcedureTerm")
    @XmlElement(required = true)
    private String procedureTerm;


    @Invocation(name = "isReadOnly")
    @XmlElement(required = true)
    private boolean readOnly;


    @Invocation(name = "getResultSetHoldability")
    @XmlElement(required = true)
    private int resultSetHoldability;


    @Invocation(name = "getRowIdLifetime")
    @XmlTransient
    private RowIdLifetime rowIdLifetime;


    @Invocation(name = "getSearchStringEscape")
    @XmlElement(required = true)
    private String searchStringEscape;


    @Invocation(name = "getSQLKeywords")
    @XmlElement(required = true)
    private String sqlKewords;


    @Invocation(name = "getSQLStateType")
    @XmlElement(required = true)
    private int sqlStateType;


    @Invocation(name = "storesLowerCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesLowerCaseIdentifiers;


    @Invocation(name = "storesLowerCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesLowerCaseQuotedIdentifiers;


    @Invocation(name = "storesMixedCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesMixedCaseIdentifiers;


    @Invocation(name = "storesMixedCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesMixedCaseQuotedIdentifiers;


    @Invocation(name = "storesUpperCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesUpperCaseIdentifiers;


    @Invocation(name = "storesUpperCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesUpperCaseQuotedIdentifiers;


    @Invocation(name = "getStringFunctions")
    @XmlElement(nillable = true, required = true)
    private String stringFunctions;


    @Invocation(name = "supportsAlterTableWithAddColumn")
    @XmlElement(required = true)
    private boolean supportsAlterTableWithAddColumn;


    @Invocation(name = "supportsAlterTableWithDropColumn")
    @XmlElement(required = true)
    private boolean supportsAlterTableWithDropColumn;


    @Invocation(name = "supportsANSI92EntryLevelSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92EntryLevelSQL;


    @Invocation(name = "supportsANSI92FullSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92FullSQL;


    @Invocation(name = "supportsANSI92IntermediateSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92IntermediateSQL;


    @Invocation(name = "supportsBatchUpdates")
    @XmlElement(required = true)
    private boolean supportsBatchUpdates;


    @Invocation(name = "supportsCatalogsInDataManipulation")
    @XmlElement(required = true)
    private boolean supportsCatalogsInDataManipulation;


    @Invocation(name = "supportsCatalogsInIndexDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInIndexDefinitions;


    @Invocation(name = "supportsCatalogsInPrivilegeDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInPrivilegeDefinitions;


    @Invocation(name = "supportsCatalogsInProcedureCalls")
    @XmlElement(required = true)
    private boolean supportsCatalogsInProcedureCalls;


    @Invocation(name = "supportsCatalogsInTableDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInTableDefinitions;


    @Invocation(name = "supportsColumnAliasing")
    @XmlElement(required = true)
    private boolean supportsColumnAliasing;


    @Invocation(name = "supportsConvert")
    @XmlTransient
    private boolean supportsConvert_;


    @XmlElement
    private List<SDTSDTBoolean> supportsConvert;


    @Invocation(name = "supportsCoreSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsCoreSQLGrammar;


    @Invocation(name = "supportsCorrelatedSubqueries")
    @XmlElement(required = true)
    private boolean supportsCorrelatedSubqueries;


    @Invocation(name = "supportsDataDefinitionAndDataManipulationTransactions")
    @XmlElement(required = true)
    private boolean supportsDataDefinitionAndDataManipulationTransactions;


    @Invocation(name = "supportsDataManipulationTransactionsOnly")
    @XmlElement(required = true)
    private boolean supportsDataManipulationTransactionsOnly;


    @Invocation(name = "supportsDifferentTableCorrelationNames")
    @XmlElement(required = true)
    private boolean supportsDifferentTableCorrelationNames;


    @Invocation(name = "supportsExpressionsInOrderBy")
    @XmlElement(required = true)
    private boolean supportsExpressionsInOrderBy;


    @Invocation(name = "supportsExtendedSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsExtendedSQLGrammar;


    @Invocation(name = "supportsFullOuterJoins")
    @XmlElement(required = true)
    private boolean supportsFullOuterJoins;


    @Invocation(name = "supportsGetGeneratedKeys")
    @XmlElement(required = true)
    private boolean supportsGetGeneratedKeys;


    @Invocation(name = "supportsGroupBy")
    @XmlElement(required = true)
    private boolean supportsGroupBy;


    @Invocation(name = "supportsGroupByBeyondSelect")
    @XmlElement(required = true)
    private boolean supportsGroupByBeyondSelect;


    @Invocation(name = "supportsGroupByUnrelated")
    @XmlElement(required = true)
    private boolean supportsGroupByUnrelated;


    @Invocation(name = "supportsIntegrityEnhancementFacility")
    @XmlElement(required = true)
    private boolean supportsIntegrityEnhancementFacility;


    @Invocation(name = "supportsLikeEscapeClause")
    @XmlElement(required = true)
    private boolean supportsLikeEscapeClause;


    @Invocation(name = "supportsLimitedOuterJoins")
    @XmlElement(required = true)
    private boolean supportsLimitedOuterJoins;


    @Invocation(name = "supportsMinimumSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsMinimumSQLGrammar;


    @Invocation(name = "supportsMixedCaseIdentifiers")
    @XmlElement(required = true)
    private boolean supportsMixedCaseIdentifiers;


    @Invocation(name = "supportsMixedCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean supportsMixedCaseQuotedIdentifiers;


    @Invocation(name = "supportsMultipleOpenResults")
    @XmlElement(required = true)
    private boolean supportsMultipleOpenResults;


    @Invocation(name = "supportsMultipleResultSets")
    @XmlElement(required = true)
    private boolean supportsMultipleResultSets;


    @Invocation(name = "supportsMultipleTransactions")
    @XmlElement(required = true)
    private boolean supportsMultipleTransactions;


    @Invocation(name = "supportsNamedParameters")
    @XmlElement(required = true)
    private boolean supportsNamedParameters;


    @Invocation(name = "supportsNonNullableColumns")
    @XmlElement(required = true)
    private boolean supportsNonNullableColumns;


    @Invocation(name = "supportsOpenCursorsAcrossCommit")
    @XmlElement(required = true)
    private boolean supportsOpenCursorsAcrossCommit;


    @Invocation(name = "supportsOpenCursorsAcrossRollback")
    @XmlElement(required = true)
    private boolean supportsOpenCursorsAcrossRollback;


    @Invocation(name = "supportsOpenStatementsAcrossCommit")
    @XmlElement(required = true)
    private boolean supportsOpenStatementsAcrossCommit;


    @Invocation(name = "supportsOpenStatementsAcrossRollback")
    @XmlElement(required = true)
    private boolean supportsOpenStatementsAcrossRollback;


    @Invocation(name = "supportsOrderByUnrelated")
    @XmlElement(required = true)
    private boolean supportsOrderByUnrelated;


    @Invocation(name = "supportsOuterJoins")
    @XmlElement(required = true)
    private boolean supportsOuterJoins;


    @Invocation(name = "supportsPositionedDelete")
    @XmlElement(required = true)
    private boolean supportsPositionedDelete;


    @Invocation(name = "supportsPositionedUpdate")
    @XmlElement(required = true)
    private boolean supportsPositionedUpdate;


    @Invocation(name = "supportsRefCursors")
    @XmlElement(required = true)
    private boolean supportsRefCursors;


    @Invocation(
        name = "supportsResultSetConcurrency",
        types = {int.class, int.class},
        argsarr = {
            @InvocationArgs({"1003", "1007"}), // TYPE_FORWARD_ONLY/CONCUR_READ_ONLY
            @InvocationArgs({"1003", "1008"}), // TYPE_FORWARD_ONLY/CONCUR_UPDATABLE
            @InvocationArgs({"1004", "1007"}), // TYPE_SCROLL_INSENSITIVE/CONCUR_READ_ONLY
            @InvocationArgs({"1004", "1008"}), // TYPE_SCROLL_INSENSITIVE/CONCUR_UPDATABLE
            @InvocationArgs({"1005", "1007"}), // TYPE_SCROLL_SENSITIVE/CONCUR_READ_ONLY
            @InvocationArgs({"1005", "1008"}) // TYPE_SCROLL_SENSITIVE/CONCUR_UPDATABLE
        }
    )
    @XmlElement
    private List<RSTRSCBoolean> supportsResultSetConcurrency;


    @Invocation(
        name = "supportsResultSetHoldability",
        types = {int.class},
        argsarr = {
            @InvocationArgs({"1"}), // CLOSE_CURSORS_AT_COMMIT
            @InvocationArgs({"2"}) // HOLD_CURSORS_OVER_COMMIT
        }
    )
    @XmlElement
    private List<RSHBoolean> supportsResultSetHoldability;


    @Invocation(name = "supportsResultSetType", types = {int.class},
                argsarr = {
                    @InvocationArgs({"1003"}), // TYPE_FORWARD_ONLY
                    @InvocationArgs({"1004"}), // TYPE_SCROLL_INSENSITIVE
                    @InvocationArgs({"1005"}) // TYPE_SCROLL_SENSITIVE
                }
    )
    @XmlElement
    private List<RSTBoolean> supportsResultSetType;


    @Invocation(name = "supportsSavepoints")
    @XmlElement(required = true)
    private boolean supportsSavepoints;


    @Invocation(name = "supportsSchemasInDataManipulation")
    @XmlElement(required = true)
    private boolean supportsSchemasInDataManipulation;


    @Invocation(name = "supportsSchemasInIndexDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInIndexDefinitions;


    @Invocation(name = "supportsSchemasInPrivilegeDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInPrivilegeDefinitions;


    @Invocation(name = "supportsSchemasInProcedureCalls")
    @XmlElement(required = true)
    private boolean supportsSchemasInProcedureCalls;


    @Invocation(name = "supportsSchemasInTableDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInTableDefinitions;


    @Invocation(name = "supportsSelectForUpdate")
    @XmlElement(required = true)
    private boolean supportsSelectForUpdate;


    @Invocation(name = "supportsStatementPooling")
    @XmlElement(required = true)
    private boolean supportsStatementPooling;


    @Invocation(name = "supportsStoredFunctionsUsingCallSyntax")
    @XmlElement(required = true)
    private boolean supportsStoredFunctionsUsingCallSyntax;


    @Invocation(name = "supportsStoredProcedures")
    @XmlElement(required = true)
    private boolean supportsStoredProcedures;


    @Invocation(name = "supportsSubqueriesInComparisons")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInComparisons;


    @Invocation(name = "supportsSubqueriesInExists")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInExists;


    @Invocation(name = "supportsSubqueriesInIns")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInIns;


    @Invocation(name = "supportsSubqueriesInQuantifieds")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInQuantifieds;


    @Invocation(name = "supportsTableCorrelationNames")
    @XmlElement(required = true)
    private boolean supportsTableCorrelationNames;


    @Invocation(name = "supportsTransactionIsolationLevel", types = {int.class},
                argsarr = {
                    @InvocationArgs({"0"}), // TRANSACTION_NONE
                    @InvocationArgs({"1"}), // TRANSACTION_READ_UNCOMMITTED
                    @InvocationArgs({"2"}), // TRANSACTION_READ_COMMITTED
                    @InvocationArgs({"4"}), // TRANSACTION_REPEATABLE_READ
                    @InvocationArgs({"8"}) // TRANSACTION_SERIALIZABLE
                }
    )
    @XmlElement
    private List<TILBoolean> supportsTransactionIsolationLevel;


    @Invocation(name = "supportsTransactions")
    @XmlElement(required = true)
    private boolean supportsTransactions;


    @Invocation(name = "supportsUnion")
    @XmlElement(required = true)
    private boolean supportsUnion;


    @Invocation(name = "supportsUnionAll")
    @XmlElement(required = true)
    private boolean supportsUnionAll;


    @Invocation(name = "getSystemFunctions")
    @XmlElement(required = true)
    private String systemFunctions;


    @Invocation(name = "getTimeDateFunctions")
    @XmlElement(required = true)
    private String timeDateFunctions;


    @Invocation(name = "getSchemas")
    @XmlElementRef
    private List<SchemaName> schemaNames;


    @Invocation(name = "getSchemaTerm")
    @XmlElement(required = true)
    private String schemaTerm;


    @Invocation(name = "getTableTypes")
    @XmlElementRef
    private List<TableType> tableTypes;


    @Invocation(name = "getTypeInfo")
    @XmlElementRef
    private List<TypeInfo> typeInfo;


    @Invocation(
        name = "updatesAreDetected",
        types = {int.class},
        argsarr = {
            @InvocationArgs({"1003"}), // TYPE_FORWARD_ONLY
            @InvocationArgs({"1004"}), // TYPE_SCROLL_INSENSITIVE
            @InvocationArgs({"1005"}) // TYPE_SCROLL_SENSITIVE
        }
    )
    @XmlElement
    private List<RSTBoolean> updatesAreDetected;


    @Invocation(name = "getUserName")
    @XmlElement(required = true)
    private String userName;


    @Invocation(name = "getURL")
    @XmlElement(required = true)
    private String url;


    @Invocation(name = "usesLocalFilePerTable")
    @XmlElement(required = true)
    private boolean usesLocalFilePerTable;


    @Invocation(name = "usesLocalFiles")
    @XmlElement(required = true)
    private boolean usesLocalFiles;


}

