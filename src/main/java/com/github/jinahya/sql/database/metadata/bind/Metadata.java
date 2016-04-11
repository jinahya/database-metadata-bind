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

    public int getJDBCMajorVersion() {
        return JDBCMajorVersion;
    }

    public void setJDBCMajorVersion(int JDBCMajorVersion) {
        this.JDBCMajorVersion = JDBCMajorVersion;
    }

    public int getJDBCMinorVersion() {
        return JDBCMinorVersion;
    }

    public void setJDBCMinorVersion(int JDBCMinorVersion) {
        this.JDBCMinorVersion = JDBCMinorVersion;
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

    public String getRowIdLifetimeName() {

        return rowIdLifetime == null ? null : rowIdLifetime.name();
    }

    public void setRowIdLifetimeName(String rowIdLifetimeName) {

        this.rowIdLifetime = rowIdLifetimeName == null
                             ? null : RowIdLifetime.valueOf(rowIdLifetimeName);
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

    public String getSearchStringEscape() {
        return searchStringEscape;
    }

    public void setSearchStringEscape(String searchStringEscape) {
        this.searchStringEscape = searchStringEscape;
    }

    public String getSQLKewords() {
        return SQLKewords;
    }

    public void setSQLKewords(String SQLKewords) {
        this.SQLKewords = SQLKewords;
    }

    public int getSQLStateType() {
        return SQLStateType;
    }

    public void setSQLStateType(int SQLStateType) {
        this.SQLStateType = SQLStateType;
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

    public List<TableType> getTableTypes() {
        return tableTypes;
    }

    public void setTableTypes(List<TableType> tableTypes) {
        this.tableTypes = tableTypes;
    }

    public String getTimeDateFunctions() {
        return timeDateFunctions;
    }

    public void setTimeDateFunctions(String timeDateFunctions) {
        this.timeDateFunctions = timeDateFunctions;
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

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
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
    @_Invocation(name = "allProceduresAreCallable")
    @XmlElement(required = true)
    private boolean allProceduresAreCallable;

    @_Invocation(name = "allTablesAreSelectable")
    @XmlElement(required = true)
    private boolean allTablesAreSelectable;

    @_Invocation(name = "autoCommitFailureClosesAllResultSets")
    @XmlElement(required = true)
    private boolean autoCommitFailureClosesAllResultSets;

    @_Invocation(name = "getCatalogs")
    @XmlElementRef
    private List<Catalog> catalogs;

    @_Invocation(name = "isCatalogAtStart")
    @XmlElement(required = true)
    private boolean catalogAtStart;

    @_Invocation(name = "getCatalogSeparator")
    @XmlElement(nillable = true, required = true)
    private String catalogSeparator;

    @_Invocation(name = "getCatalogTerm")
    @XmlElement(nillable = true, required = true)
    private String catalogTerm;

    @_Invocation(name = "getClientInfoProperties")
    @XmlElementRef
    private List<ClientInfoProperty> clientInfoProperties;

    @_Invocation(name = "getConnection")
    @XmlTransient
    private Connection connection;

    @XmlElementRef
    private List<CrossReference> crossReferences;

    @_Invocation(name = "dataDefinitionCausesTransactionCommit")
    @XmlElement(required = true)
    private boolean dataDefinitionCausesTransactionCommit;

    @_Invocation(name = "dataDefinitionIgnoredInTransactions")
    @XmlElement(required = true)
    private boolean dataDefinitionIgnoredInTransactions;

    @_Invocation(name = "getDatabaseMajorVersion")
    @XmlElement(required = true)
    private int databaseMajorVersion;

    @_Invocation(name = "getDatabaseMinorVersion")
    @XmlElement(required = true)
    private int databaseMinorVersion;

    @_Invocation(name = "getDatabaseProductName")
    @XmlElement(required = true)
    private String databaseProductName;

    @_Invocation(name = "getDatabaseProductVersion")
    @XmlElement(required = true)
    private String databaseProductVersion;

    @_Invocation(name = "getDefaultTransactionIsolation")
    @XmlElement(required = true)
    private int defaultTransactionIsolation;

    @_Invocation(
            name = "deletesAreDetected",
            types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> deletesAreDetected;

    @_Invocation(name = "doesMaxRowSizeIncludeBlobs")
    @XmlElement(required = true)
    private boolean doesMaxRowSizeIncludeBlobs;

    @_Invocation(name = "getDriverMajorVersion")
    @XmlElement(required = true)
    private int driverMajorVersion;

    @_Invocation(name = "getDriverMinorVersion")
    @XmlElement(required = true)
    private int driverMinorVersion;

    @_Invocation(name = "getDriverName")
    @XmlElement(required = true)
    private String driverName;

    @_Invocation(name = "getDriverVersion")
    @XmlElement(required = true)
    private String driverVersion;

    @_Invocation(name = "getExtraNameCharacters")
    @XmlElement(required = true)
    private String extraNameCharacters;

    @_Invocation(name = "generatedKeyAlwaysReturned")
    @XmlElement(required = true)
    private boolean generatedKeyAlwaysReturned;

    @_Invocation(name = "getIdentifierQuoteString")
    @XmlElement(required = true)
    private String identifierQuoteString;

    @_Invocation(
            name = "insertsAreDetected",
            types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> insertsAreDetected;

    @_Invocation(name = "getJDBCMajorVersion")
    @XmlElement(required = true)
    private int JDBCMajorVersion;

    @_Invocation(name = "getJDBCMinorVersion")
    @XmlElement(required = true)
    private int JDBCMinorVersion;

    @_Invocation(name = "locatorsUpdateCopy")
    @XmlElement(required = true)
    private boolean locatorsUpdateCopy;

    @_Invocation(name = "getMaxBinaryLiteralLength")
    @XmlElement(required = true)
    private int maxBinaryLiteralLength;

    @_Invocation(name = "getMaxCatalogNameLength")
    @XmlElement(required = true)
    private int maxCatalogNameLength;

    @_Invocation(name = "getMaxCharLiteralLength")
    @XmlElement(required = true)
    private int maxCharLiteralLength;

    @_Invocation(name = "getMaxColumnNameLength")
    @XmlElement(required = true)
    private int maxColumnNameLength;

    @_Invocation(name = "getMaxColumnsInGroupBy")
    @XmlElement(required = true)
    private int maxColumnsInGroupBy;

    @_Invocation(name = "getMaxColumnsInIndex")
    @XmlElement(required = true)
    private int maxColumnsInIndex;

    @_Invocation(name = "getMaxColumnsInOrderBy")
    @XmlElement(required = true)
    private int maxColumnsInOrderBy;

    @_Invocation(name = "getMaxColumnsInSelect")
    @XmlElement(required = true)
    private int maxColumnsInSelect;

    @_Invocation(name = "getMaxColumnsInTable")
    @XmlElement(required = true)
    private int maxColumnsInTable;

    @_Invocation(name = "getMaxConnections")
    @XmlElement(required = true)
    private int maxConnections;

    @_Invocation(name = "getMaxCursorNameLength")
    @XmlElement(required = true)
    private int maxCursorNameLength;

    @_Invocation(name = "getMaxIndexLength")
    @XmlElement(required = true)
    private int maxIndexLength;

    @_Invocation(name = "getMaxLogicalLobSize")
    @XmlElement(required = true)
    private long maxLogicalLobSize;

    @_Invocation(name = "getMaxProcedureNameLength")
    @XmlElement(required = true)
    private int maxProcedureNameLength;

    @_Invocation(name = "getMaxRowSize")
    @XmlElement(required = true)
    private int maxRowSize;

    @_Invocation(name = "getMaxSchemaNameLength")
    @XmlElement(required = true)
    private int maxSchemaNameLength;

    @_Invocation(name = "getMaxStatementLength")
    @XmlElement(required = true)
    private int maxStatementLength;

    @_Invocation(name = "getMaxStatements")
    @XmlElement(required = true)
    private int maxStatements;

    @_Invocation(name = "getMaxTableNameLength")
    @XmlElement(required = true)
    private int maxTableNameLength;

    @_Invocation(name = "getMaxTablesInSelect")
    @XmlElement(required = true)
    private int maxTablesInSelect;

    @_Invocation(name = "getMaxUserNameLength")
    @XmlElement(required = true)
    private int maxUserNameLength;

    @_Invocation(name = "nullPlusNonNullIsNull")
    @XmlElement(required = true)
    private boolean nullPlusNonNullIsNull;

    @_Invocation(name = "nullsAreSortedAtEnd")
    @XmlElement(required = true)
    private boolean nullsAreSortedAtEnd;

    @_Invocation(name = "nullsAreSortedAtStart")
    @XmlElement(required = true)
    private boolean nullsAreSortedAtStart;

    @_Invocation(name = "nullsAreSortedHigh")
    @XmlElement(required = true)
    private boolean nullsAreSortedHigh;

    @_Invocation(name = "nullsAreSortedLow")
    @XmlElement(required = true)
    private boolean nullsAreSortedLow;

    @_Invocation(name = "getNumericFunctions")
    @XmlElement(required = true)
    private String numericFunctions;

    @_Invocation(
            name = "othersDeletesAreVisible",
            types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> othersDeletesAreVisible;

    @_Invocation(
            name = "othersInsertsAreVisible",
            types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> othersInsertsAreVisible;

    @_Invocation(
            name = "othersUpdatesAreVisible", types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> othersUpdatesAreVisible;

    @_Invocation(
            name = "ownDeletesAreVisible", types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> ownDeletesAreVisible;

    @_Invocation(
            name = "ownInsertsAreVisible",
            types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> ownInsertsAreVisible;

    @_Invocation(
            name = "ownUpdatesAreVisible", types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // ResultSet.TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // ResultSet.TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // ResultSet.TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> ownUpdatesAreVisible;

    @_Invocation(name = "getProcedureTerm")
    @XmlElement(required = true)
    private String procedureTerm;

    @_Invocation(name = "isReadOnly")
    @XmlElement(required = true)
    private boolean readOnly;

    @_Invocation(name = "getResultSetHoldability")
    @XmlElement(required = true)
    private int resultSetHoldability;

    @_Invocation(name = "getRowIdLifetime")
    @XmlTransient
    private RowIdLifetime rowIdLifetime;

    @_Invocation(name = "getSchemas")
    @XmlElementRef
    private List<SchemaName> schemaNames;

    @_Invocation(name = "getSchemaTerm")
    @XmlElement(required = true)
    private String schemaTerm;

    @_Invocation(name = "getSearchStringEscape")
    @XmlElement(required = true)
    private String searchStringEscape;

    @_Invocation(name = "getSQLKeywords")
    @XmlElement(required = true)
    private String SQLKewords;

    @_Invocation(name = "getSQLStateType")
    @XmlElement(required = true)
    private int SQLStateType;

    @_Invocation(name = "storesLowerCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesLowerCaseIdentifiers;

    @_Invocation(name = "storesLowerCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesLowerCaseQuotedIdentifiers;

    @_Invocation(name = "storesMixedCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesMixedCaseIdentifiers;

    @_Invocation(name = "storesMixedCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesMixedCaseQuotedIdentifiers;

    @_Invocation(name = "storesUpperCaseIdentifiers")
    @XmlElement(required = true)
    private boolean storesUpperCaseIdentifiers;

    @_Invocation(name = "storesUpperCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean storesUpperCaseQuotedIdentifiers;

    @_Invocation(name = "getStringFunctions")
    @XmlElement(nillable = true, required = true)
    private String stringFunctions;

    @_Invocation(name = "supportsAlterTableWithAddColumn")
    @XmlElement(required = true)
    private boolean supportsAlterTableWithAddColumn;

    @_Invocation(name = "supportsAlterTableWithDropColumn")
    @XmlElement(required = true)
    private boolean supportsAlterTableWithDropColumn;

    @_Invocation(name = "supportsANSI92EntryLevelSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92EntryLevelSQL;

    @_Invocation(name = "supportsANSI92FullSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92FullSQL;

    @_Invocation(name = "supportsANSI92IntermediateSQL")
    @XmlElement(required = true)
    private boolean supportsANSI92IntermediateSQL;

    @_Invocation(name = "supportsBatchUpdates")
    @XmlElement(required = true)
    private boolean supportsBatchUpdates;

    @_Invocation(name = "supportsCatalogsInDataManipulation")
    @XmlElement(required = true)
    private boolean supportsCatalogsInDataManipulation;

    @_Invocation(name = "supportsCatalogsInIndexDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInIndexDefinitions;

    @_Invocation(name = "supportsCatalogsInPrivilegeDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInPrivilegeDefinitions;

    @_Invocation(name = "supportsCatalogsInProcedureCalls")
    @XmlElement(required = true)
    private boolean supportsCatalogsInProcedureCalls;

    @_Invocation(name = "supportsCatalogsInTableDefinitions")
    @XmlElement(required = true)
    private boolean supportsCatalogsInTableDefinitions;

    @_Invocation(name = "supportsColumnAliasing")
    @XmlElement(required = true)
    private boolean supportsColumnAliasing;

    @_Invocation(name = "supportsConvert")
    @XmlTransient
    private boolean supportsConvert_;

    @XmlElement
    private List<SDTSDTBoolean> supportsConvert;

    @_Invocation(name = "supportsCoreSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsCoreSQLGrammar;

    @_Invocation(name = "supportsCorrelatedSubqueries")
    @XmlElement(required = true)
    private boolean supportsCorrelatedSubqueries;

    @_Invocation(name = "supportsDataDefinitionAndDataManipulationTransactions")
    @XmlElement(required = true)
    private boolean supportsDataDefinitionAndDataManipulationTransactions;

    @_Invocation(name = "supportsDataManipulationTransactionsOnly")
    @XmlElement(required = true)
    private boolean supportsDataManipulationTransactionsOnly;

    @_Invocation(name = "supportsDifferentTableCorrelationNames")
    @XmlElement(required = true)
    private boolean supportsDifferentTableCorrelationNames;

    @_Invocation(name = "supportsExpressionsInOrderBy")
    @XmlElement(required = true)
    private boolean supportsExpressionsInOrderBy;

    @_Invocation(name = "supportsExtendedSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsExtendedSQLGrammar;

    @_Invocation(name = "supportsFullOuterJoins")
    @XmlElement(required = true)
    private boolean supportsFullOuterJoins;

    @_Invocation(name = "supportsGetGeneratedKeys")
    @XmlElement(required = true)
    private boolean supportsGetGeneratedKeys;

    @_Invocation(name = "supportsGroupBy")
    @XmlElement(required = true)
    private boolean supportsGroupBy;

    @_Invocation(name = "supportsGroupByBeyondSelect")
    @XmlElement(required = true)
    private boolean supportsGroupByBeyondSelect;

    @_Invocation(name = "supportsGroupByUnrelated")
    @XmlElement(required = true)
    private boolean supportsGroupByUnrelated;

    @_Invocation(name = "supportsIntegrityEnhancementFacility")
    @XmlElement(required = true)
    private boolean supportsIntegrityEnhancementFacility;

    @_Invocation(name = "supportsLikeEscapeClause")
    @XmlElement(required = true)
    private boolean supportsLikeEscapeClause;

    @_Invocation(name = "supportsLimitedOuterJoins")
    @XmlElement(required = true)
    private boolean supportsLimitedOuterJoins;

    @_Invocation(name = "supportsMinimumSQLGrammar")
    @XmlElement(required = true)
    private boolean supportsMinimumSQLGrammar;

    @_Invocation(name = "supportsMixedCaseIdentifiers")
    @XmlElement(required = true)
    private boolean supportsMixedCaseIdentifiers;

    @_Invocation(name = "supportsMixedCaseQuotedIdentifiers")
    @XmlElement(required = true)
    private boolean supportsMixedCaseQuotedIdentifiers;

    @_Invocation(name = "supportsMultipleOpenResults")
    @XmlElement(required = true)
    private boolean supportsMultipleOpenResults;

    @_Invocation(name = "supportsMultipleResultSets")
    @XmlElement(required = true)
    private boolean supportsMultipleResultSets;

    @_Invocation(name = "supportsMultipleTransactions")
    @XmlElement(required = true)
    private boolean supportsMultipleTransactions;

    @_Invocation(name = "supportsNamedParameters")
    @XmlElement(required = true)
    private boolean supportsNamedParameters;

    @_Invocation(name = "supportsNonNullableColumns")
    @XmlElement(required = true)
    private boolean supportsNonNullableColumns;

    @_Invocation(name = "supportsOpenCursorsAcrossCommit")
    @XmlElement(required = true)
    private boolean supportsOpenCursorsAcrossCommit;

    @_Invocation(name = "supportsOpenCursorsAcrossRollback")
    @XmlElement(required = true)
    private boolean supportsOpenCursorsAcrossRollback;

    @_Invocation(name = "supportsOpenStatementsAcrossCommit")
    @XmlElement(required = true)
    private boolean supportsOpenStatementsAcrossCommit;

    @_Invocation(name = "supportsOpenStatementsAcrossRollback")
    @XmlElement(required = true)
    private boolean supportsOpenStatementsAcrossRollback;

    @_Invocation(name = "supportsOrderByUnrelated")
    @XmlElement(required = true)
    private boolean supportsOrderByUnrelated;

    @_Invocation(name = "supportsOuterJoins")
    @XmlElement(required = true)
    private boolean supportsOuterJoins;

    @_Invocation(name = "supportsPositionedDelete")
    @XmlElement(required = true)
    private boolean supportsPositionedDelete;

    @_Invocation(name = "supportsPositionedUpdate")
    @XmlElement(required = true)
    private boolean supportsPositionedUpdate;

    @_Invocation(name = "supportsRefCursors")
    @XmlElement(required = true)
    private boolean supportsRefCursors;

    @_Invocation(
            name = "supportsResultSetConcurrency",
            types = {int.class, int.class},
            argsarr = {
                @_InvocationArgs({"1003", "1007"}), // TYPE_FORWARD_ONLY/CONCUR_READ_ONLY
                @_InvocationArgs({"1003", "1008"}), // TYPE_FORWARD_ONLY/CONCUR_UPDATABLE
                @_InvocationArgs({"1004", "1007"}), // TYPE_SCROLL_INSENSITIVE/CONCUR_READ_ONLY
                @_InvocationArgs({"1004", "1008"}), // TYPE_SCROLL_INSENSITIVE/CONCUR_UPDATABLE
                @_InvocationArgs({"1005", "1007"}), // TYPE_SCROLL_SENSITIVE/CONCUR_READ_ONLY
                @_InvocationArgs({"1005", "1008"}) // TYPE_SCROLL_SENSITIVE/CONCUR_UPDATABLE
            }
    )
    @XmlElement
    private List<RSTRSCBoolean> supportsResultSetConcurrency;

    @_Invocation(
            name = "supportsResultSetHoldability",
            types = {int.class},
            argsarr = {
                @_InvocationArgs({"1"}), // CLOSE_CURSORS_AT_COMMIT
                @_InvocationArgs({"2"}) // HOLD_CURSORS_OVER_COMMIT
            }
    )
    @XmlElement
    private List<RSHBoolean> supportsResultSetHoldability;

    @_Invocation(name = "supportsResultSetType", types = {int.class},
                argsarr = {
                    @_InvocationArgs({"1003"}), // TYPE_FORWARD_ONLY
                    @_InvocationArgs({"1004"}), // TYPE_SCROLL_INSENSITIVE
                    @_InvocationArgs({"1005"}) // TYPE_SCROLL_SENSITIVE
                }
    )
    @XmlElement
    private List<RSTBoolean> supportsResultSetType;

    @_Invocation(name = "supportsSavepoints")
    @XmlElement(required = true)
    private boolean supportsSavepoints;

    @_Invocation(name = "supportsSchemasInDataManipulation")
    @XmlElement(required = true)
    private boolean supportsSchemasInDataManipulation;

    @_Invocation(name = "supportsSchemasInIndexDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInIndexDefinitions;

    @_Invocation(name = "supportsSchemasInPrivilegeDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInPrivilegeDefinitions;

    @_Invocation(name = "supportsSchemasInProcedureCalls")
    @XmlElement(required = true)
    private boolean supportsSchemasInProcedureCalls;

    @_Invocation(name = "supportsSchemasInTableDefinitions")
    @XmlElement(required = true)
    private boolean supportsSchemasInTableDefinitions;

    @_Invocation(name = "supportsSelectForUpdate")
    @XmlElement(required = true)
    private boolean supportsSelectForUpdate;

    @_Invocation(name = "supportsStatementPooling")
    @XmlElement(required = true)
    private boolean supportsStatementPooling;

    @_Invocation(name = "supportsStoredFunctionsUsingCallSyntax")
    @XmlElement(required = true)
    private boolean supportsStoredFunctionsUsingCallSyntax;

    @_Invocation(name = "supportsStoredProcedures")
    @XmlElement(required = true)
    private boolean supportsStoredProcedures;

    @_Invocation(name = "supportsSubqueriesInComparisons")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInComparisons;

    @_Invocation(name = "supportsSubqueriesInExists")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInExists;

    @_Invocation(name = "supportsSubqueriesInIns")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInIns;

    @_Invocation(name = "supportsSubqueriesInQuantifieds")
    @XmlElement(required = true)
    private boolean supportsSubqueriesInQuantifieds;

    @_Invocation(name = "supportsTableCorrelationNames")
    @XmlElement(required = true)
    private boolean supportsTableCorrelationNames;

    @_Invocation(name = "supportsTransactionIsolationLevel", types = {int.class},
                argsarr = {
                    @_InvocationArgs({"0"}), // TRANSACTION_NONE
                    @_InvocationArgs({"1"}), // TRANSACTION_READ_UNCOMMITTED
                    @_InvocationArgs({"2"}), // TRANSACTION_READ_COMMITTED
                    @_InvocationArgs({"4"}), // TRANSACTION_REPEATABLE_READ
                    @_InvocationArgs({"8"}) // TRANSACTION_SERIALIZABLE
                }
    )
    @XmlElement
    private List<TILBoolean> supportsTransactionIsolationLevel;

    @_Invocation(name = "supportsTransactions")
    @XmlElement(required = true)
    private boolean supportsTransactions;

    @_Invocation(name = "supportsUnion")
    @XmlElement(required = true)
    private boolean supportsUnion;

    @_Invocation(name = "supportsUnionAll")
    @XmlElement(required = true)
    private boolean supportsUnionAll;

    @_Invocation(name = "getSystemFunctions")
    @XmlElement(required = true)
    private String systemFunctions;

    @_Invocation(name = "getTableTypes")
    @XmlElementRef
    private List<TableType> tableTypes;

    @_Invocation(name = "getTimeDateFunctions")
    @XmlElement(required = true)
    private String timeDateFunctions;

    @_Invocation(name = "getTypeInfo")
    @XmlElementRef
    private List<TypeInfo> typeInfo;

    @_Invocation(
            name = "updatesAreDetected",
            types = {int.class},
            argsarr = {
                @_InvocationArgs({"1003"}), // TYPE_FORWARD_ONLY
                @_InvocationArgs({"1004"}), // TYPE_SCROLL_INSENSITIVE
                @_InvocationArgs({"1005"}) // TYPE_SCROLL_SENSITIVE
            }
    )
    @XmlElement
    private List<RSTBoolean> updatesAreDetected;

    @_Invocation(name = "getUserName")
    @XmlElement(required = true)
    private String userName;

    @_Invocation(name = "getURL")
    @XmlElement(required = true)
    private String URL;

    @_Invocation(name = "usesLocalFilePerTable")
    @XmlElement(required = true)
    private boolean usesLocalFilePerTable;

    @_Invocation(name = "usesLocalFiles")
    @XmlElement(required = true)
    private boolean usesLocalFiles;

}
