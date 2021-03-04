package com.github.jinahya.database.metadata.bind;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.github.jinahya.database.metadata.bind.Utils.logSqlFeatureNotSupportedException;
import static java.util.Objects.requireNonNull;

@XmlRootElement
public class Metadata implements MetadataType {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // -----------------------------------------------------------------------------------------------------------------
    public static Metadata newInstance(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final Metadata instance = new Metadata();
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.allProceduresAreCallable = context.databaseMetaData.allProceduresAreCallable();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.allTablesAreSelectable = context.databaseMetaData.allTablesAreSelectable();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.autoCommitFailureClosesAllResultSets
                    = context.databaseMetaData.autoCommitFailureClosesAllResultSets();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.dataDefinitionCausesTransactionCommit
                    = context.databaseMetaData.dataDefinitionCausesTransactionCommit();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.dataDefinitionIgnoredInTransactions
                    = context.databaseMetaData.dataDefinitionIgnoredInTransactions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        instance.deletesAreDetected = DeletesAreDetected.list(context.databaseMetaData);
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.doesMaxRowSizeIncludeBlobs
                    = context.databaseMetaData.doesMaxRowSizeIncludeBlobs();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.generatedKeyAlwaysReturned
                    = context.databaseMetaData.generatedKeyAlwaysReturned();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // ---------------------------------------------------------------------------------------------------- catalogs
        instance.catalog = context.getCatalogs();
        if (instance.catalog.isEmpty()) {
            instance.catalog.add(Catalog.newVirtualInstance());
        }
        for (final Catalog catalog : instance.catalog) {
            context.getSchemas(catalog);
        }
        final List<Table> tables = instance.catalog.stream()
                .flatMap(c -> c.getSchemas().stream())
                .flatMap(s -> s.getTables().stream())
                .collect(Collectors.toList());
        for (final Table foreign : tables) {
            for (final Table parent : tables) {
                context.getCrossReferences(parent.getTableCat(), parent.getTableSchem(), parent.getTableName(),
                                           foreign.getTableCat(), foreign.getTableSchem(), foreign.getTableName(),
                                           foreign.getCrossReference());
            }
        }
        try {
            instance.catalogSeparator = context.databaseMetaData.getCatalogSeparator();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.catalogTerm = context.databaseMetaData.getCatalogTerm();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // ------------------------------------------------------------------------------------------ clientInfoProperty
        instance.clientInfoProperty = new ArrayList<>();
        context.getClientInfoProperties(instance.clientInfoProperty);
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.databaseMajorVersion = context.databaseMetaData.getDatabaseMajorVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.databaseMinorVersion = context.databaseMetaData.getDatabaseMinorVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.databaseProductName = context.databaseMetaData.getDatabaseProductName();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.databaseProductVersion = context.databaseMetaData.getDatabaseProductVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.defaultTransactionIsolation = context.databaseMetaData.getDefaultTransactionIsolation();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        instance.driverMajorVersion = context.databaseMetaData.getDriverMajorVersion();
        instance.driverMinorVersion = context.databaseMetaData.getDriverMinorVersion();
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.driverName = context.databaseMetaData.getDriverName();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.driverVersion = context.databaseMetaData.getDriverVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.extraNameCharacters = context.databaseMetaData.getExtraNameCharacters();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.identifierQuoteString = context.databaseMetaData.getIdentifierQuoteString();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.JDBCMajorVersion = context.databaseMetaData.getJDBCMajorVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.JDBCMinorVersion = context.databaseMetaData.getJDBCMinorVersion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.maxBinaryLiteralLength = context.databaseMetaData.getMaxBinaryLiteralLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxCatalogNameLength = context.databaseMetaData.getMaxCatalogNameLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxCharLiteralLength = context.databaseMetaData.getMaxCharLiteralLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxColumnNameLength = context.databaseMetaData.getMaxColumnNameLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.maxColumnsInGroupBy = context.databaseMetaData.getMaxColumnsInGroupBy();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxColumnsInIndex = context.databaseMetaData.getMaxColumnsInIndex();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxColumnsInOrderBy = context.databaseMetaData.getMaxColumnsInOrderBy();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxColumnsInSelect = context.databaseMetaData.getMaxColumnsInSelect();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxColumnsInTable = context.databaseMetaData.getMaxColumnsInTable();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.maxConnections = context.databaseMetaData.getMaxConnections();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.maxCursorNameLength = context.databaseMetaData.getMaxCursorNameLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxIndexLength = context.databaseMetaData.getMaxIndexLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxLogicalLobSize = context.databaseMetaData.getMaxLogicalLobSize();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxProcedureNameLength = context.databaseMetaData.getMaxProcedureNameLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxRowSize = context.databaseMetaData.getMaxRowSize();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxSchemaNameLength = context.databaseMetaData.getMaxSchemaNameLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxStatementLength = context.databaseMetaData.getMaxStatementLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxStatements = context.databaseMetaData.getMaxStatements();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxTableNameLength = context.databaseMetaData.getMaxTableNameLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxTablesInSelect = context.databaseMetaData.getMaxTablesInSelect();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.maxUserNameLength = context.databaseMetaData.getMaxUserNameLength();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.numericFunctions = context.databaseMetaData.getNumericFunctions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.procedureTerm = context.databaseMetaData.getProcedureTerm();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.resultSetHoldability = context.databaseMetaData.getResultSetHoldability();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.schemaTerm = context.databaseMetaData.getSchemaTerm();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.searchStringEscape = context.databaseMetaData.getSearchStringEscape();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.SQLKeywords = context.databaseMetaData.getSQLKeywords();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.SQLStateType = context.databaseMetaData.getSQLStateType();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.SQLStateType = context.databaseMetaData.getSQLStateType();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.systemFunctions = context.databaseMetaData.getSystemFunctions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        instance.tableType = new ArrayList<>();
        context.getTableTypes(instance.tableType);
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.timeDateFunctions = context.databaseMetaData.getTimeDateFunctions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // --------------------------------------------------------------------------------------------------- typeInfos
        instance.typeInfo = new ArrayList<>();
        context.getTypeInfo(instance.typeInfo);
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.URL = context.databaseMetaData.getURL();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.userName = context.databaseMetaData.getUserName();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        instance.insertsAreDetected = InsertsAreDetected.list(context.databaseMetaData);
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.catalogAtStart = context.databaseMetaData.isCatalogAtStart();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.readOnly = context.databaseMetaData.isReadOnly();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.locatorsUpdateCopy = context.databaseMetaData.locatorsUpdateCopy();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.nullPlusNonNullIsNull = context.databaseMetaData.nullPlusNonNullIsNull();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.nullsAreSortedAtEnd = context.databaseMetaData.nullsAreSortedAtEnd();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.nullsAreSortedAtStart = context.databaseMetaData.nullsAreSortedAtStart();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.nullsAreSortedHigh = context.databaseMetaData.nullsAreSortedHigh();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.nullsAreSortedLow = context.databaseMetaData.nullsAreSortedLow();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        instance.othersDeletesAreVisible = OthersDeletesAreVisible.list(context.databaseMetaData);
        instance.othersInsertsAreVisible = OthersInsertsAreVisible.list(context.databaseMetaData);
        instance.othersUpdatesAreVisible = OthersUpdatesAreVisible.list(context.databaseMetaData);
        instance.ownDeletesAreVisible = OwnDeletesAreVisible.list(context.databaseMetaData);
        instance.ownInsertsAreVisible = OwnInsertsAreVisible.list(context.databaseMetaData);
        instance.ownUpdatesAreVisible = OwnUpdatesAreVisible.list(context.databaseMetaData);
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.storesLowerCaseIdentifiers = context.databaseMetaData.storesLowerCaseIdentifiers();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.storesLowerCaseQuotedIdentifiers
                    = context.databaseMetaData.storesLowerCaseQuotedIdentifiers();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.storesMixedCaseIdentifiers = context.databaseMetaData.storesMixedCaseIdentifiers();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.storesMixedCaseQuotedIdentifiers
                    = context.databaseMetaData.storesMixedCaseQuotedIdentifiers();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsAlterTableWithAddColumn
                    = context.databaseMetaData.supportsAlterTableWithAddColumn();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsAlterTableWithDropColumn
                    = context.databaseMetaData.supportsAlterTableWithDropColumn();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsANSI92EntryLevelSQL = context.databaseMetaData.supportsANSI92EntryLevelSQL();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsANSI92FullSQL = context.databaseMetaData.supportsANSI92FullSQL();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsANSI92IntermediateSQL = context.databaseMetaData.supportsANSI92IntermediateSQL();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsBatchUpdates = context.databaseMetaData.supportsBatchUpdates();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsCatalogsInDataManipulation
                    = context.databaseMetaData.supportsCatalogsInDataManipulation();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsCatalogsInIndexDefinitions
                    = context.databaseMetaData.supportsCatalogsInIndexDefinitions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsCatalogsInPrivilegeDefinitions
                    = context.databaseMetaData.supportsCatalogsInPrivilegeDefinitions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsCatalogsInProcedureCalls
                    = context.databaseMetaData.supportsCatalogsInProcedureCalls();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsCatalogsInTableDefinitions
                    = context.databaseMetaData.supportsCatalogsInTableDefinitions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsColumnAliasing = context.databaseMetaData.supportsColumnAliasing();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsConvert = context.databaseMetaData.supportsConvert();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        instance.supportsConvert_ = SupportsConvert.list(context.databaseMetaData);
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsCoreSQLGrammar = context.databaseMetaData.supportsCoreSQLGrammar();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsCorrelatedSubqueries = context.databaseMetaData.supportsCorrelatedSubqueries();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsDataDefinitionAndDataManipulationTransactions
                    = context.databaseMetaData.supportsDataDefinitionAndDataManipulationTransactions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsDataManipulationTransactionsOnly
                    = context.databaseMetaData.supportsDataManipulationTransactionsOnly();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsDifferentTableCorrelationNames
                    = context.databaseMetaData.supportsDifferentTableCorrelationNames();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsExpressionsInOrderBy
                    = context.databaseMetaData.supportsExpressionsInOrderBy();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsExtendedSQLGrammar = context.databaseMetaData.supportsExtendedSQLGrammar();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsFullOuterJoins = context.databaseMetaData.supportsFullOuterJoins();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsGetGeneratedKeys = context.databaseMetaData.supportsGetGeneratedKeys();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsGroupBy = context.databaseMetaData.supportsGroupBy();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsGroupByBeyondSelect = context.databaseMetaData.supportsGroupByBeyondSelect();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsGroupByUnrelated = context.databaseMetaData.supportsGroupByUnrelated();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsIntegrityEnhancementFacility
                    = context.databaseMetaData.supportsIntegrityEnhancementFacility();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsLikeEscapeClause = context.databaseMetaData.supportsLikeEscapeClause();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsLimitedOuterJoins = context.databaseMetaData.supportsLimitedOuterJoins();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsMinimumSQLGrammar = context.databaseMetaData.supportsMinimumSQLGrammar();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsMixedCaseIdentifiers = context.databaseMetaData.supportsMixedCaseIdentifiers();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsMixedCaseQuotedIdentifiers
                    = context.databaseMetaData.supportsMixedCaseQuotedIdentifiers();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsMultipleOpenResults = context.databaseMetaData.supportsMultipleOpenResults();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsMultipleResultSets = context.databaseMetaData.supportsMultipleResultSets();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsMultipleTransactions = context.databaseMetaData.supportsMultipleTransactions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsNamedParameters = context.databaseMetaData.supportsNamedParameters();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsNonNullableColumns = context.databaseMetaData.supportsNonNullableColumns();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsOpenCursorsAcrossCommit
                    = context.databaseMetaData.supportsOpenCursorsAcrossCommit();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsOpenCursorsAcrossRollBack
                    = context.databaseMetaData.supportsOpenCursorsAcrossRollback();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsOpenStatementsAcrossCommit
                    = context.databaseMetaData.supportsOpenStatementsAcrossCommit();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsOpenStatementsAcrossRollBack
                    = context.databaseMetaData.supportsOpenStatementsAcrossRollback();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsOrderByUnrelated = context.databaseMetaData.supportsOrderByUnrelated();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsOuterJoins = context.databaseMetaData.supportsOuterJoins();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsPositionedDelete = context.databaseMetaData.supportsPositionedDelete();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsPositionedUpdate = context.databaseMetaData.supportsPositionedUpdate();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsRefCursors = context.databaseMetaData.supportsRefCursors();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        instance.supportsResultSetConcurrency = SupportsResultSetConcurrency.list(context.databaseMetaData);
        instance.supportsResultSetHoldability = SupportsResultSetHoldability.list(context.databaseMetaData);
        instance.supportsResultSetType = SupportsResultSetType.list(context.databaseMetaData);
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsSavepoints = context.databaseMetaData.supportsSavepoints();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsSchemasInDataManipulation
                    = context.databaseMetaData.supportsSchemasInDataManipulation();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsSchemasInIndexDefinitions
                    = context.databaseMetaData.supportsSchemasInIndexDefinitions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsSchemasInPrivilegeDefinitions
                    = context.databaseMetaData.supportsSchemasInPrivilegeDefinitions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsSchemasInProcedureCalls
                    = context.databaseMetaData.supportsSchemasInProcedureCalls();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsSchemasInTableDefinitions
                    = context.databaseMetaData.supportsSchemasInTableDefinitions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsSelectForUpdate = context.databaseMetaData.supportsSelectForUpdate();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
//        try {
//            instance.supportsSharding = context.databaseMetaData.supportsSharding(); // Since 9;
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            Utils.logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        try {
            instance.supportsStatementPooling = context.databaseMetaData.supportsStatementPooling();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsStoredFunctionsUsingCallSyntax
                    = context.databaseMetaData.supportsStoredFunctionsUsingCallSyntax();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsStoredProcedures = context.databaseMetaData.supportsStoredProcedures();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsSubqueriesInComparisons
                    = context.databaseMetaData.supportsSubqueriesInComparisons();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsSubqueriesInExists = context.databaseMetaData.supportsSubqueriesInExists();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsSubqueriesInIns = context.databaseMetaData.supportsSubqueriesInIns();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsSubqueriesInQuantifieds
                    = context.databaseMetaData.supportsSubqueriesInQuantifieds();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsTableCorrelationNames = context.databaseMetaData.supportsTableCorrelationNames();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsTransactionIsolationLevel
                    = SupportsTransactionIsolationLevel.list(context.databaseMetaData);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsTransactions = context.databaseMetaData.supportsTransactions();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.supportsUnion = context.databaseMetaData.supportsUnion();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.supportsUnionAll = context.databaseMetaData.supportsUnionAll();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.updatesAreDetected = UpdatesAreDetected.list(context.databaseMetaData);
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        try {
            instance.usesLocalFilePerTable = context.databaseMetaData.usesLocalFilePerTable();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        try {
            instance.usesLocalFiles = context.databaseMetaData.usesLocalFiles();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            logSqlFeatureNotSupportedException(logger, sqlfnse);
        }
        // -------------------------------------------------------------------------------------------------------------
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private Metadata() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean allProceduresAreCallable;

    @XmlElement(nillable = true)
    public Boolean allTablesAreSelectable;

    @XmlElement(nillable = true)
    public Boolean autoCommitFailureClosesAllResultSets;

    @XmlElement(nillable = true)
    public Boolean dataDefinitionCausesTransactionCommit;

    @XmlElement(nillable = true)
    public Boolean dataDefinitionIgnoredInTransactions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public List<@Valid @NotNull DeletesAreDetected> deletesAreDetected;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean doesMaxRowSizeIncludeBlobs;

    @XmlElement(nillable = true)
    public Boolean generatedKeyAlwaysReturned;

    @XmlElementRef
    public List<@Valid @NotNull Catalog> catalog;

    @XmlElement(nillable = true)
    public String catalogSeparator;

    @XmlElement(nillable = true)
    public String catalogTerm;

    @XmlElementRef
    public List<@Valid @NotNull ClientInfoProperty> clientInfoProperty;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer databaseMajorVersion;

    @XmlElement(nillable = true)
    public Integer databaseMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public String databaseProductName;

    @XmlElement(nillable = true)
    public String databaseProductVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer defaultTransactionIsolation;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer driverMajorVersion;

    @XmlElement(nillable = true)
    public Integer driverMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public String driverName;

    @XmlElement(nillable = true)
    public String driverVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public String extraNameCharacters;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public String identifierQuoteString;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer JDBCMajorVersion;

    @XmlElement(nillable = true)
    public Integer JDBCMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer maxBinaryLiteralLength;

    @XmlElement(nillable = true)
    public Integer maxCatalogNameLength;

    @XmlElement(nillable = true)
    public Integer maxCharLiteralLength;

    @XmlElement(nillable = true)
    public Integer maxColumnNameLength;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer maxColumnsInGroupBy;

    @XmlElement(nillable = true)
    public Integer maxColumnsInIndex;

    @XmlElement(nillable = true)
    public Integer maxColumnsInOrderBy;

    @XmlElement(nillable = true)
    public Integer maxColumnsInSelect;

    @XmlElement(nillable = true)
    public Integer maxColumnsInTable;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer maxConnections;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer maxCursorNameLength;

    @XmlElement(nillable = true)
    public Integer maxIndexLength;

    @XmlElement(nillable = true)
    public Long maxLogicalLobSize;

    @XmlElement(nillable = true)
    public Integer maxProcedureNameLength;

    @XmlElement(nillable = true)
    public Integer maxRowSize;

    @XmlElement(nillable = true)
    public Integer maxSchemaNameLength;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer maxStatementLength;

    @XmlElement(nillable = true)
    public Integer maxStatements;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Integer maxTableNameLength;

    @XmlElement(nillable = true)
    public Integer maxTablesInSelect;

    @XmlElement(nillable = true)
    public Integer maxUserNameLength;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public String numericFunctions;

    @XmlElement(nillable = true)
    public String procedureTerm;

    @XmlElement(nillable = true)
    public Integer resultSetHoldability;

    @XmlElement(nillable = true)
    public String schemaTerm;

    @XmlElement(nillable = true)
    public String searchStringEscape;

    @XmlElement(nillable = true)
    public String SQLKeywords;

    @XmlElement(nillable = true)
    public Integer SQLStateType;

    @XmlElement(nillable = true)
    public String stringFunctions;

    @XmlElement(nillable = true)
    public String systemFunctions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    public List<@NotNull @Valid TableType> tableType;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public String timeDateFunctions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    public List<@Valid @NotNull TypeInfo> typeInfo;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public String URL;

    @XmlElement(nillable = true)
    public String userName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    public List<@Valid @NotNull InsertsAreDetected> insertsAreDetected;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean catalogAtStart;

    @XmlElement(nillable = true)
    public Boolean readOnly;

    @XmlElement(nillable = true)
    public Boolean locatorsUpdateCopy;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean nullPlusNonNullIsNull;

    @XmlElement(nillable = true)
    public Boolean nullsAreSortedAtEnd;

    @XmlElement(nillable = true)
    public Boolean nullsAreSortedAtStart;

    @XmlElement(nillable = true)
    public Boolean nullsAreSortedHigh;

    @XmlElement(nillable = true)
    public Boolean nullsAreSortedLow;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    public List<@Valid @NotNull OthersDeletesAreVisible> othersDeletesAreVisible;

    @XmlElementRef
    public List<@Valid @NotNull OthersInsertsAreVisible> othersInsertsAreVisible;

    @XmlElementRef
    public List<@Valid @NotNull OthersUpdatesAreVisible> othersUpdatesAreVisible;

    @XmlElementRef
    public List<@Valid @NotNull OwnDeletesAreVisible> ownDeletesAreVisible;

    @XmlElementRef
    public List<@Valid @NotNull OwnInsertsAreVisible> ownInsertsAreVisible;

    @XmlElementRef
    public List<@Valid @NotNull OwnUpdatesAreVisible> ownUpdatesAreVisible;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean storesLowerCaseIdentifiers;

    @XmlElement(nillable = true)
    public Boolean storesLowerCaseQuotedIdentifiers;

    @XmlElement(nillable = true)
    public Boolean storesMixedCaseIdentifiers;

    @XmlElement(nillable = true)
    public Boolean storesMixedCaseQuotedIdentifiers;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsAlterTableWithAddColumn;

    @XmlElement(nillable = true)
    public Boolean supportsAlterTableWithDropColumn;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsANSI92EntryLevelSQL;

    @XmlElement(nillable = true)
    public Boolean supportsANSI92FullSQL;

    @XmlElement(nillable = true)
    public Boolean supportsANSI92IntermediateSQL;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsBatchUpdates;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsCatalogsInDataManipulation;

    @XmlElement(nillable = true)
    public Boolean supportsCatalogsInIndexDefinitions;

    @XmlElement(nillable = true)
    public Boolean supportsCatalogsInPrivilegeDefinitions;

    @XmlElement(nillable = true)
    public Boolean supportsCatalogsInProcedureCalls;

    @XmlElement(nillable = true)
    public Boolean supportsCatalogsInTableDefinitions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsColumnAliasing;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsConvert;

    @XmlElementRef
    public List<@Valid @NotNull SupportsConvert> supportsConvert_;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsCoreSQLGrammar;

    @XmlElement(nillable = true)
    public Boolean supportsCorrelatedSubqueries;

    @XmlElement(nillable = true)
    public Boolean supportsDataDefinitionAndDataManipulationTransactions;

    @XmlElement(nillable = true)
    public Boolean supportsDataManipulationTransactionsOnly;

    @XmlElement(nillable = true)
    public Boolean supportsDifferentTableCorrelationNames;

    @XmlElement(nillable = true)
    public Boolean supportsExpressionsInOrderBy;

    @XmlElement(nillable = true)
    public Boolean supportsExtendedSQLGrammar;

    @XmlElement(nillable = true)
    public Boolean supportsFullOuterJoins;

    @XmlElement(nillable = true)
    public Boolean supportsGetGeneratedKeys;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsGroupBy;

    @XmlElement(nillable = true)
    public Boolean supportsGroupByBeyondSelect;

    @XmlElement(nillable = true)
    public Boolean supportsGroupByUnrelated;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsIntegrityEnhancementFacility;

    @XmlElement(nillable = true)
    public Boolean supportsLikeEscapeClause;

    @XmlElement(nillable = true)
    public Boolean supportsLimitedOuterJoins;

    @XmlElement(nillable = true)
    public Boolean supportsMinimumSQLGrammar;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsMixedCaseIdentifiers;

    @XmlElement(nillable = true)
    public Boolean supportsMixedCaseQuotedIdentifiers;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsMultipleOpenResults;

    @XmlElement(nillable = true)
    public Boolean supportsMultipleResultSets;

    @XmlElement(nillable = true)
    public Boolean supportsMultipleTransactions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsNamedParameters;

    @XmlElement(nillable = true)
    public Boolean supportsNonNullableColumns;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsOpenCursorsAcrossCommit;

    @XmlElement(nillable = true)
    public Boolean supportsOpenCursorsAcrossRollBack;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsOpenStatementsAcrossCommit;

    @XmlElement(nillable = true)
    public Boolean supportsOpenStatementsAcrossRollBack;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsOrderByUnrelated;

    @XmlElement(nillable = true)
    public Boolean supportsOuterJoins;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsPositionedDelete;

    @XmlElement(nillable = true)
    public Boolean supportsPositionedUpdate;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsRefCursors;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    public List<@Valid @NotNull SupportsResultSetConcurrency> supportsResultSetConcurrency;

    @XmlElementRef
    public List<@Valid @NotNull SupportsResultSetHoldability> supportsResultSetHoldability;

    @XmlElementRef
    public List<@Valid @NotNull SupportsResultSetType> supportsResultSetType;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsSavepoints;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsSchemasInDataManipulation;

    @XmlElement(nillable = true)
    public Boolean supportsSchemasInIndexDefinitions;

    @XmlElement(nillable = true)
    public Boolean supportsSchemasInPrivilegeDefinitions;

    @XmlElement(nillable = true)
    public Boolean supportsSchemasInProcedureCalls;

    @XmlElement(nillable = true)
    public Boolean supportsSchemasInTableDefinitions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsSelectForUpdate;

    @XmlElement(nillable = true)
    public Boolean supportsSharding;

    @XmlElement(nillable = true)
    public Boolean supportsStatementPooling;

    @XmlElement(nillable = true)
    public Boolean supportsStoredFunctionsUsingCallSyntax;

    @XmlElement(nillable = true)
    public Boolean supportsStoredProcedures;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsSubqueriesInComparisons;

    @XmlElement(nillable = true)
    public Boolean supportsSubqueriesInExists;

    @XmlElement(nillable = true)
    public Boolean supportsSubqueriesInIns;

    @XmlElement(nillable = true)
    public Boolean supportsSubqueriesInQuantifieds;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsTableCorrelationNames;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    public List<@Valid @NotNull SupportsTransactionIsolationLevel> supportsTransactionIsolationLevel;

    @XmlElement(nillable = true)
    public Boolean supportsTransactions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsUnion;

    @XmlElement(nillable = true)
    public Boolean supportsUnionAll;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    public List<@Valid @NotNull UpdatesAreDetected> updatesAreDetected;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean usesLocalFilePerTable;

    @XmlElement(nillable = true)
    public Boolean usesLocalFiles;
}