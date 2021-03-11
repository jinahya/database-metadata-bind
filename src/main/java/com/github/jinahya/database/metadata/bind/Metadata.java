package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@XmlRootElement
public class Metadata implements MetadataType {

    private static final long serialVersionUID = 2473494165958655239L;

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final Map<String, Method> METHODS_WITH_NO_PARAMETERS = Collections.unmodifiableMap(
            Arrays.stream(DatabaseMetaData.class.getMethods())
                    .filter(m -> m.getParameterCount() == 0)
                    .peek(f -> {
                        if (!f.isAccessible()) {
                            f.setAccessible(true);
                        }
                    })
                    .collect(Collectors.toMap(Method::getName, java.util.function.Function.identity()))
    );

    // -----------------------------------------------------------------------------------------------------------------
    public static Metadata newInstance(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final Metadata instance = new Metadata();
        // -------------------------------------------------------------------------------------------------------------
        final Set<Field> all = Arrays.stream(Metadata.class.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .collect(Collectors.toSet());
        new HashSet<>(all).stream()
                .filter(f -> !Collection.class.isAssignableFrom(f.getType()))
                .forEach(f -> {
                    Method method = METHODS_WITH_NO_PARAMETERS.get(f.getName());
                    if (method == null) {
                        final String capitalizedWithGet
                                = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                        method = METHODS_WITH_NO_PARAMETERS.get(capitalizedWithGet);
                    }
                    if (method == null) {
                        final String capitalizedWithIs
                                = "is" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                        method = METHODS_WITH_NO_PARAMETERS.get(capitalizedWithIs);
                    }
                    if (method != null) {
                        try {
                            final Object value = method.invoke(context.databaseMetaData);
                            try {
                                f.set(instance, value);
                            } catch (final ReflectiveOperationException roe) {
                                logger.log(Level.SEVERE, roe, () -> format("failed to set %1$s on %2$s", value, f));
                            }
                            all.remove(f);
                        } catch (final IllegalAccessException iae) {
                            throw new RuntimeException(iae);
                        } catch (final InvocationTargetException ite) {
                            final Throwable cause = ite.getCause();
                            if (cause instanceof SQLException) {
                                final SQLException sqle = (SQLException) cause;
                                final Method m = method;
                                logger.log(Level.WARNING, sqle, () -> "failed to invoke " + m);
                                if (!context.isSuppressed(sqle)) {
                                    throw new RuntimeException(sqle);
                                }
                            } else {
                                throw new RuntimeException(cause);
                            }
                        }
                    }
                });
        instance.deletesAreDetected = DeletesAreDetected.getAllInstances(context);
        instance.catalog = context.getCatalogs(new ArrayList<>());
        final List<Table> tables = instance.catalog.stream()
                .flatMap(c -> c.getSchemas().stream())
                .flatMap(s -> s.getTables().stream())
                .collect(Collectors.toList());
        for (final Table foreign : tables) {
            for (final Table parent : tables) {
                context.getCrossReference(parent.getTableCat(), parent.getTableSchem(), parent.getTableName(),
                                          foreign.getTableCat(), foreign.getTableSchem(), foreign.getTableName(),
                                          foreign.getCrossReference());
            }
        }
        try {
            all.remove(Metadata.class.getDeclaredField("catalog"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        instance.clientInfoProperty = new ArrayList<>();
        context.getClientInfoProperties(instance.clientInfoProperty);
        try {
            all.remove(Metadata.class.getDeclaredField("clientInfoProperty"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        instance.tableType = new ArrayList<>();
        context.getTableTypes(instance.tableType);
        try {
            all.remove(Metadata.class.getDeclaredField("tableType"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        instance.typeInfo = new ArrayList<>();
        context.getTypeInfo(instance.typeInfo);
        try {
            all.remove(Metadata.class.getDeclaredField("typeInfo"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        instance.insertsAreDetected = InsertsAreDetected.getAllInstances(context);
        try {
            all.remove(Metadata.class.getDeclaredField("insertsAreDetected"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        instance.othersDeletesAreVisible = OthersDeletesAreVisible.getAllInstances(context);
        instance.othersInsertsAreVisible = OthersInsertsAreVisible.getAllInstances(context);
        instance.othersUpdatesAreVisible = OthersUpdatesAreVisible.getAllInstances(context);
        instance.ownDeletesAreVisible = OwnDeletesAreVisible.all(context);
        instance.ownInsertsAreVisible = OwnInsertsAreVisible.getAllInstances(context);
        instance.ownUpdatesAreVisible = OwnUpdatesAreVisible.getAllInstances(context);
        instance.supportsConvert_ = SupportsConvert.getAllInstances(context);
        instance.supportsResultSetConcurrency = SupportsResultSetConcurrency.getAllInstances(context);
        instance.supportsResultSetHoldability = SupportsResultSetHoldability.getAllInstances(context);
        instance.supportsResultSetType = SupportsResultSetType.getAllInstances(context);
        instance.supportsTransactionIsolationLevel = SupportsTransactionIsolationLevel.getAllInstances(context);
        instance.updatesAreDetected = UpdatesAreDetected.getAllInstances(context);
        all.forEach(f -> {
            try {
                if (f.get(instance) == null) {
                    logger.log(Level.SEVERE, () -> format("unhandled field: %1$s", f));
                }
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException(roe);
            }
        });
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

    @Override
    public String toString() {
        return super.toString() + '{'
               + "allProceduresAreCallable=" + allProceduresAreCallable
               + ",allTablesAreSelectable=" + allTablesAreSelectable
               + ",autoCommitFailureClosesAllResultSets=" + autoCommitFailureClosesAllResultSets
               + ",dataDefinitionCausesTransactionCommit=" + dataDefinitionCausesTransactionCommit
               + ",dataDefinitionIgnoredInTransactions=" + dataDefinitionIgnoredInTransactions
               + ",deletesAreDetected=" + deletesAreDetected
               + ",doesMaxRowSizeIncludeBlobs=" + doesMaxRowSizeIncludeBlobs
               + ",generatedKeyAlwaysReturned=" + generatedKeyAlwaysReturned
               + ",catalog=" + catalog
               + ",catalogSeparator=" + catalogSeparator
               + ",catalogTerm=" + catalogTerm
               + ",clientInfoProperty=" + clientInfoProperty
               + ",databaseMajorVersion=" + databaseMajorVersion
               + ",databaseMinorVersion=" + databaseMinorVersion
               + ",databaseProductName=" + databaseProductName
               + ",databaseProductVersion=" + databaseProductVersion
               + ",defaultTransactionIsolation=" + defaultTransactionIsolation
               + ",driverMajorVersion=" + driverMajorVersion
               + ",driverMinorVersion=" + driverMinorVersion
               + ",driverName=" + driverName
               + ",driverVersion=" + driverVersion
               + ",extraNameCharacters=" + extraNameCharacters
               + ",identifierQuoteString=" + identifierQuoteString
               + ",JDBCMajorVersion=" + JDBCMajorVersion
               + ",JDBCMinorVersion=" + JDBCMinorVersion
               + ",maxBinaryLiteralLength=" + maxBinaryLiteralLength
               + ",maxCatalogNameLength=" + maxCatalogNameLength
               + ",maxCharLiteralLength=" + maxCharLiteralLength
               + ",maxColumnNameLength=" + maxColumnNameLength
               + ",maxColumnsInGroupBy=" + maxColumnsInGroupBy
               + ",maxColumnsInIndex=" + maxColumnsInIndex
               + ",maxColumnsInOrderBy=" + maxColumnsInOrderBy
               + ",maxColumnsInSelect=" + maxColumnsInSelect
               + ",maxColumnsInTable=" + maxColumnsInTable
               + ",maxConnections=" + maxConnections
               + ",maxCursorNameLength=" + maxCursorNameLength
               + ",maxIndexLength=" + maxIndexLength
               + ",maxLogicalLobSize=" + maxLogicalLobSize
               + ",maxProcedureNameLength=" + maxProcedureNameLength
               + ",maxRowSize=" + maxRowSize
               + ",maxSchemaNameLength=" + maxSchemaNameLength
               + ",maxStatementLength=" + maxStatementLength
               + ",maxStatements=" + maxStatements
               + ",maxTableNameLength=" + maxTableNameLength
               + ",maxTablesInSelect=" + maxTablesInSelect
               + ",maxUserNameLength=" + maxUserNameLength
               + ",numericFunctions=" + numericFunctions
               + ",procedureTerm=" + procedureTerm
               + ",resultSetHoldability=" + resultSetHoldability
               + ",schemaTerm=" + schemaTerm
               + ",searchStringEscape=" + searchStringEscape
               + ",SQLKeywords=" + SQLKeywords
               + ",SQLStateType=" + SQLStateType
               + ",stringFunctions=" + stringFunctions
               + ",systemFunctions=" + systemFunctions
               + ",tableType=" + tableType
               + ",timeDateFunctions=" + timeDateFunctions
               + ",typeInfo=" + typeInfo
               + ",URL=" + URL
               + ",userName=" + userName
               + ",insertsAreDetected=" + insertsAreDetected
               + ",catalogAtStart=" + catalogAtStart
               + ",readOnly=" + readOnly
               + ",locatorsUpdateCopy=" + locatorsUpdateCopy
               + ",nullPlusNonNullIsNull=" + nullPlusNonNullIsNull
               + ",nullsAreSortedAtEnd=" + nullsAreSortedAtEnd
               + ",nullsAreSortedAtStart=" + nullsAreSortedAtStart
               + ",nullsAreSortedHigh=" + nullsAreSortedHigh
               + ",nullsAreSortedLow=" + nullsAreSortedLow
               + ",othersDeletesAreVisible=" + othersDeletesAreVisible
               + ",othersInsertsAreVisible=" + othersInsertsAreVisible
               + ",othersUpdatesAreVisible=" + othersUpdatesAreVisible
               + ",ownDeletesAreVisible=" + ownDeletesAreVisible
               + ",ownInsertsAreVisible=" + ownInsertsAreVisible
               + ",ownUpdatesAreVisible=" + ownUpdatesAreVisible
               + ",storesLowerCaseIdentifiers=" + storesLowerCaseIdentifiers
               + ",storesLowerCaseQuotedIdentifiers=" + storesLowerCaseQuotedIdentifiers
               + ",storesMixedCaseIdentifiers=" + storesMixedCaseIdentifiers
               + ",storesMixedCaseQuotedIdentifiers=" + storesMixedCaseQuotedIdentifiers
               + ",supportsAlterTableWithAddColumn=" + supportsAlterTableWithAddColumn
               + ",supportsAlterTableWithDropColumn=" + supportsAlterTableWithDropColumn
               + ",supportsANSI92EntryLevelSQL=" + supportsANSI92EntryLevelSQL
               + ",supportsANSI92FullSQL=" + supportsANSI92FullSQL
               + ",supportsANSI92IntermediateSQL=" + supportsANSI92IntermediateSQL
               + ",supportsBatchUpdates=" + supportsBatchUpdates
               + ",supportsCatalogsInDataManipulation=" + supportsCatalogsInDataManipulation
               + ",supportsCatalogsInIndexDefinitions=" + supportsCatalogsInIndexDefinitions
               + ",supportsCatalogsInPrivilegeDefinitions=" + supportsCatalogsInPrivilegeDefinitions
               + ",supportsCatalogsInProcedureCalls=" + supportsCatalogsInProcedureCalls
               + ",supportsCatalogsInTableDefinitions=" + supportsCatalogsInTableDefinitions
               + ",supportsColumnAliasing=" + supportsColumnAliasing
               + ",supportsConvert=" + supportsConvert
               + ",supportsConvert_=" + supportsConvert_
               + ",supportsCoreSQLGrammar=" + supportsCoreSQLGrammar
               + ",supportsCorrelatedSubqueries=" + supportsCorrelatedSubqueries
               + ",supportsDataDefinitionAndDataManipulationTransactions="
               + supportsDataDefinitionAndDataManipulationTransactions
               + ",supportsDataManipulationTransactionsOnly=" + supportsDataManipulationTransactionsOnly
               + ",supportsDifferentTableCorrelationNames=" + supportsDifferentTableCorrelationNames
               + ",supportsExpressionsInOrderBy=" + supportsExpressionsInOrderBy
               + ",supportsExtendedSQLGrammar=" + supportsExtendedSQLGrammar
               + ",supportsFullOuterJoins=" + supportsFullOuterJoins
               + ",supportsGetGeneratedKeys=" + supportsGetGeneratedKeys
               + ",supportsGroupBy=" + supportsGroupBy
               + ",supportsGroupByBeyondSelect=" + supportsGroupByBeyondSelect
               + ",supportsGroupByUnrelated=" + supportsGroupByUnrelated
               + ",supportsIntegrityEnhancementFacility=" + supportsIntegrityEnhancementFacility
               + ",supportsLikeEscapeClause=" + supportsLikeEscapeClause
               + ",supportsLimitedOuterJoins=" + supportsLimitedOuterJoins
               + ",supportsMinimumSQLGrammar=" + supportsMinimumSQLGrammar
               + ",supportsMixedCaseIdentifiers=" + supportsMixedCaseIdentifiers
               + ",supportsMixedCaseQuotedIdentifiers=" + supportsMixedCaseQuotedIdentifiers
               + ",supportsMultipleOpenResults=" + supportsMultipleOpenResults
               + ",supportsMultipleResultSets=" + supportsMultipleResultSets
               + ",supportsMultipleTransactions=" + supportsMultipleTransactions
               + ",supportsNamedParameters=" + supportsNamedParameters
               + ",supportsNonNullableColumns=" + supportsNonNullableColumns
               + ",supportsOpenCursorsAcrossCommit=" + supportsOpenCursorsAcrossCommit
               + ",supportsOpenCursorsAcrossRollBack=" + supportsOpenCursorsAcrossRollback
               + ",supportsOpenStatementsAcrossCommit=" + supportsOpenStatementsAcrossCommit
               + ",supportsOpenStatementsAcrossRollBack=" + supportsOpenStatementsAcrossRollback
               + ",supportsOrderByUnrelated=" + supportsOrderByUnrelated
               + ",supportsOuterJoins=" + supportsOuterJoins
               + ",supportsPositionedDelete=" + supportsPositionedDelete
               + ",supportsPositionedUpdate=" + supportsPositionedUpdate
               + ",supportsRefCursors=" + supportsRefCursors
               + ",supportsResultSetConcurrency=" + supportsResultSetConcurrency
               + ",supportsResultSetHoldability=" + supportsResultSetHoldability
               + ",supportsResultSetType=" + supportsResultSetType
               + ",supportsSavepoints=" + supportsSavepoints
               + ",supportsSchemasInDataManipulation=" + supportsSchemasInDataManipulation
               + ",supportsSchemasInIndexDefinitions=" + supportsSchemasInIndexDefinitions
               + ",supportsSchemasInPrivilegeDefinitions=" + supportsSchemasInPrivilegeDefinitions
               + ",supportsSchemasInProcedureCalls=" + supportsSchemasInProcedureCalls
               + ",supportsSchemasInTableDefinitions=" + supportsSchemasInTableDefinitions
               + ",supportsSelectForUpdate=" + supportsSelectForUpdate
               + ",supportsSharding=" + supportsSharding
               + ",supportsStatementPooling=" + supportsStatementPooling
               + ",supportsStoredFunctionsUsingCallSyntax=" + supportsStoredFunctionsUsingCallSyntax
               + ",supportsStoredProcedures=" + supportsStoredProcedures
               + ",supportsSubqueriesInComparisons=" + supportsSubqueriesInComparisons
               + ",supportsSubqueriesInExists=" + supportsSubqueriesInExists
               + ",supportsSubqueriesInIns=" + supportsSubqueriesInIns
               + ",supportsSubqueriesInQuantifieds=" + supportsSubqueriesInQuantifieds
               + ",supportsTableCorrelationNames=" + supportsTableCorrelationNames
               + ",supportsTransactionIsolationLevel=" + supportsTransactionIsolationLevel
               + ",supportsTransactions=" + supportsTransactions
               + ",supportsUnion=" + supportsUnion
               + ",supportsUnionAll=" + supportsUnionAll
               + ",updatesAreDetected=" + updatesAreDetected
               + ",usesLocalFilePerTable=" + usesLocalFilePerTable
               + ",usesLocalFiles=" + usesLocalFiles
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Metadata that = (Metadata) obj;
        return Objects.equals(allProceduresAreCallable, that.allProceduresAreCallable)
               && Objects.equals(allTablesAreSelectable, that.allTablesAreSelectable)
               && Objects.equals(autoCommitFailureClosesAllResultSets, that.autoCommitFailureClosesAllResultSets)
               && Objects.equals(dataDefinitionCausesTransactionCommit, that.dataDefinitionCausesTransactionCommit)
               && Objects.equals(dataDefinitionIgnoredInTransactions, that.dataDefinitionIgnoredInTransactions)
               && Objects.equals(deletesAreDetected, that.deletesAreDetected)
               && Objects.equals(doesMaxRowSizeIncludeBlobs, that.doesMaxRowSizeIncludeBlobs)
               && Objects.equals(generatedKeyAlwaysReturned, that.generatedKeyAlwaysReturned)
               && Objects.equals(catalog, that.catalog)
               && Objects.equals(catalogSeparator, that.catalogSeparator)
               && Objects.equals(catalogTerm, that.catalogTerm)
               && Objects.equals(clientInfoProperty, that.clientInfoProperty)
               && Objects.equals(databaseMajorVersion, that.databaseMajorVersion)
               && Objects.equals(databaseMinorVersion, that.databaseMinorVersion)
               && Objects.equals(databaseProductName, that.databaseProductName)
               && Objects.equals(databaseProductVersion, that.databaseProductVersion)
               && Objects.equals(defaultTransactionIsolation, that.defaultTransactionIsolation)
               && Objects.equals(driverMajorVersion, that.driverMajorVersion)
               && Objects.equals(driverMinorVersion, that.driverMinorVersion)
               && Objects.equals(driverName, that.driverName)
               && Objects.equals(driverVersion, that.driverVersion)
               && Objects.equals(extraNameCharacters, that.extraNameCharacters)
               && Objects.equals(identifierQuoteString, that.identifierQuoteString)
               && Objects.equals(JDBCMajorVersion, that.JDBCMajorVersion)
               && Objects.equals(JDBCMinorVersion, that.JDBCMinorVersion)
               && Objects.equals(maxBinaryLiteralLength, that.maxBinaryLiteralLength)
               && Objects.equals(maxCatalogNameLength, that.maxCatalogNameLength)
               && Objects.equals(maxCharLiteralLength, that.maxCharLiteralLength)
               && Objects.equals(maxColumnNameLength, that.maxColumnNameLength)
               && Objects.equals(maxColumnsInGroupBy, that.maxColumnsInGroupBy)
               && Objects.equals(maxColumnsInIndex, that.maxColumnsInIndex)
               && Objects.equals(maxColumnsInOrderBy, that.maxColumnsInOrderBy)
               && Objects.equals(maxColumnsInSelect, that.maxColumnsInSelect)
               && Objects.equals(maxColumnsInTable, that.maxColumnsInTable)
               && Objects.equals(maxConnections, that.maxConnections)
               && Objects.equals(maxCursorNameLength, that.maxCursorNameLength)
               && Objects.equals(maxIndexLength, that.maxIndexLength)
               && Objects.equals(maxLogicalLobSize, that.maxLogicalLobSize)
               && Objects.equals(maxProcedureNameLength, that.maxProcedureNameLength)
               && Objects.equals(maxRowSize, that.maxRowSize)
               && Objects.equals(maxSchemaNameLength, that.maxSchemaNameLength)
               && Objects.equals(maxStatementLength, that.maxStatementLength)
               && Objects.equals(maxStatements, that.maxStatements)
               && Objects.equals(maxTableNameLength, that.maxTableNameLength)
               && Objects.equals(maxTablesInSelect, that.maxTablesInSelect)
               && Objects.equals(maxUserNameLength, that.maxUserNameLength)
               && Objects.equals(numericFunctions, that.numericFunctions)
               && Objects.equals(procedureTerm, that.procedureTerm)
               && Objects.equals(resultSetHoldability, that.resultSetHoldability)
               && Objects.equals(schemaTerm, that.schemaTerm)
               && Objects.equals(searchStringEscape, that.searchStringEscape)
               && Objects.equals(SQLKeywords, that.SQLKeywords)
               && Objects.equals(SQLStateType, that.SQLStateType)
               && Objects.equals(stringFunctions, that.stringFunctions)
               && Objects.equals(systemFunctions, that.systemFunctions)
               && Objects.equals(tableType, that.tableType)
               && Objects.equals(timeDateFunctions, that.timeDateFunctions)
               && Objects.equals(typeInfo, that.typeInfo)
               && Objects.equals(URL, that.URL)
               && Objects.equals(userName, that.userName)
               && Objects.equals(insertsAreDetected, that.insertsAreDetected)
               && Objects.equals(catalogAtStart, that.catalogAtStart)
               && Objects.equals(readOnly, that.readOnly)
               && Objects.equals(locatorsUpdateCopy, that.locatorsUpdateCopy)
               && Objects.equals(nullPlusNonNullIsNull, that.nullPlusNonNullIsNull)
               && Objects.equals(nullsAreSortedAtEnd, that.nullsAreSortedAtEnd)
               && Objects.equals(nullsAreSortedAtStart, that.nullsAreSortedAtStart)
               && Objects.equals(nullsAreSortedHigh, that.nullsAreSortedHigh)
               && Objects.equals(nullsAreSortedLow, that.nullsAreSortedLow)
               && Objects.equals(othersDeletesAreVisible, that.othersDeletesAreVisible)
               && Objects.equals(othersInsertsAreVisible, that.othersInsertsAreVisible)
               && Objects.equals(othersUpdatesAreVisible, that.othersUpdatesAreVisible)
               && Objects.equals(ownDeletesAreVisible, that.ownDeletesAreVisible)
               && Objects.equals(ownInsertsAreVisible, that.ownInsertsAreVisible)
               && Objects.equals(ownUpdatesAreVisible, that.ownUpdatesAreVisible)
               && Objects.equals(storesLowerCaseIdentifiers, that.storesLowerCaseIdentifiers)
               && Objects.equals(storesLowerCaseQuotedIdentifiers, that.storesLowerCaseQuotedIdentifiers)
               && Objects.equals(storesMixedCaseIdentifiers, that.storesMixedCaseIdentifiers)
               && Objects.equals(storesMixedCaseQuotedIdentifiers, that.storesMixedCaseQuotedIdentifiers)
               && Objects.equals(supportsAlterTableWithAddColumn, that.supportsAlterTableWithAddColumn)
               && Objects.equals(supportsAlterTableWithDropColumn, that.supportsAlterTableWithDropColumn)
               && Objects.equals(supportsANSI92EntryLevelSQL, that.supportsANSI92EntryLevelSQL)
               && Objects.equals(supportsANSI92FullSQL, that.supportsANSI92FullSQL)
               && Objects.equals(supportsANSI92IntermediateSQL, that.supportsANSI92IntermediateSQL)
               && Objects.equals(supportsBatchUpdates, that.supportsBatchUpdates)
               && Objects.equals(supportsCatalogsInDataManipulation, that.supportsCatalogsInDataManipulation)
               && Objects.equals(supportsCatalogsInIndexDefinitions, that.supportsCatalogsInIndexDefinitions)
               && Objects.equals(supportsCatalogsInPrivilegeDefinitions, that.supportsCatalogsInPrivilegeDefinitions)
               && Objects.equals(supportsCatalogsInProcedureCalls, that.supportsCatalogsInProcedureCalls)
               && Objects.equals(supportsCatalogsInTableDefinitions, that.supportsCatalogsInTableDefinitions)
               && Objects.equals(supportsColumnAliasing, that.supportsColumnAliasing)
               && Objects.equals(supportsConvert, that.supportsConvert)
               && Objects.equals(supportsConvert_, that.supportsConvert_)
               && Objects.equals(supportsCoreSQLGrammar, that.supportsCoreSQLGrammar)
               && Objects.equals(supportsCorrelatedSubqueries, that.supportsCorrelatedSubqueries)
               && Objects.equals(supportsDataDefinitionAndDataManipulationTransactions,
                                 that.supportsDataDefinitionAndDataManipulationTransactions)
               && Objects.equals(supportsDataManipulationTransactionsOnly,
                                 that.supportsDataManipulationTransactionsOnly)
               && Objects.equals(supportsDifferentTableCorrelationNames, that.supportsDifferentTableCorrelationNames)
               && Objects.equals(supportsExpressionsInOrderBy, that.supportsExpressionsInOrderBy)
               && Objects.equals(supportsExtendedSQLGrammar, that.supportsExtendedSQLGrammar)
               && Objects.equals(supportsFullOuterJoins, that.supportsFullOuterJoins)
               && Objects.equals(supportsGetGeneratedKeys, that.supportsGetGeneratedKeys)
               && Objects.equals(supportsGroupBy, that.supportsGroupBy)
               && Objects.equals(supportsGroupByBeyondSelect, that.supportsGroupByBeyondSelect)
               && Objects.equals(supportsGroupByUnrelated, that.supportsGroupByUnrelated)
               && Objects.equals(supportsIntegrityEnhancementFacility, that.supportsIntegrityEnhancementFacility)
               && Objects.equals(supportsLikeEscapeClause, that.supportsLikeEscapeClause)
               && Objects.equals(supportsLimitedOuterJoins, that.supportsLimitedOuterJoins)
               && Objects.equals(supportsMinimumSQLGrammar, that.supportsMinimumSQLGrammar)
               && Objects.equals(supportsMixedCaseIdentifiers, that.supportsMixedCaseIdentifiers)
               && Objects.equals(supportsMixedCaseQuotedIdentifiers, that.supportsMixedCaseQuotedIdentifiers)
               && Objects.equals(supportsMultipleOpenResults, that.supportsMultipleOpenResults)
               && Objects.equals(supportsMultipleResultSets, that.supportsMultipleResultSets)
               && Objects.equals(supportsMultipleTransactions, that.supportsMultipleTransactions)
               && Objects.equals(supportsNamedParameters, that.supportsNamedParameters)
               && Objects.equals(supportsNonNullableColumns, that.supportsNonNullableColumns)
               && Objects.equals(supportsOpenCursorsAcrossCommit, that.supportsOpenCursorsAcrossCommit)
               && Objects.equals(supportsOpenCursorsAcrossRollback, that.supportsOpenCursorsAcrossRollback)
               && Objects.equals(supportsOpenStatementsAcrossCommit, that.supportsOpenStatementsAcrossCommit)
               && Objects.equals(supportsOpenStatementsAcrossRollback, that.supportsOpenStatementsAcrossRollback)
               && Objects.equals(supportsOrderByUnrelated, that.supportsOrderByUnrelated)
               && Objects.equals(supportsOuterJoins, that.supportsOuterJoins)
               && Objects.equals(supportsPositionedDelete, that.supportsPositionedDelete)
               && Objects.equals(supportsPositionedUpdate, that.supportsPositionedUpdate)
               && Objects.equals(supportsRefCursors, that.supportsRefCursors)
               && Objects.equals(supportsResultSetConcurrency, that.supportsResultSetConcurrency)
               && Objects.equals(supportsResultSetHoldability, that.supportsResultSetHoldability)
               && Objects.equals(supportsResultSetType, that.supportsResultSetType)
               && Objects.equals(supportsSavepoints, that.supportsSavepoints)
               && Objects.equals(supportsSchemasInDataManipulation, that.supportsSchemasInDataManipulation)
               && Objects.equals(supportsSchemasInIndexDefinitions, that.supportsSchemasInIndexDefinitions)
               && Objects.equals(supportsSchemasInPrivilegeDefinitions, that.supportsSchemasInPrivilegeDefinitions)
               && Objects.equals(supportsSchemasInProcedureCalls, that.supportsSchemasInProcedureCalls)
               && Objects.equals(supportsSchemasInTableDefinitions, that.supportsSchemasInTableDefinitions)
               && Objects.equals(supportsSelectForUpdate, that.supportsSelectForUpdate)
               && Objects.equals(supportsSharding, that.supportsSharding)
               && Objects.equals(supportsStatementPooling, that.supportsStatementPooling)
               && Objects.equals(supportsStoredFunctionsUsingCallSyntax, that.supportsStoredFunctionsUsingCallSyntax)
               && Objects.equals(supportsStoredProcedures, that.supportsStoredProcedures)
               && Objects.equals(supportsSubqueriesInComparisons, that.supportsSubqueriesInComparisons)
               && Objects.equals(supportsSubqueriesInExists, that.supportsSubqueriesInExists)
               && Objects.equals(supportsSubqueriesInIns, that.supportsSubqueriesInIns)
               && Objects.equals(supportsSubqueriesInQuantifieds, that.supportsSubqueriesInQuantifieds)
               && Objects.equals(supportsTableCorrelationNames, that.supportsTableCorrelationNames)
               && Objects.equals(supportsTransactionIsolationLevel, that.supportsTransactionIsolationLevel)
               && Objects.equals(supportsTransactions, that.supportsTransactions)
               && Objects.equals(supportsUnion, that.supportsUnion)
               && Objects.equals(supportsUnionAll, that.supportsUnionAll)
               && Objects.equals(updatesAreDetected, that.updatesAreDetected)
               && Objects.equals(usesLocalFilePerTable, that.usesLocalFilePerTable)
               && Objects.equals(usesLocalFiles, that.usesLocalFiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allProceduresAreCallable,
                            allTablesAreSelectable,
                            autoCommitFailureClosesAllResultSets,
                            dataDefinitionCausesTransactionCommit,
                            dataDefinitionIgnoredInTransactions,
                            deletesAreDetected,
                            doesMaxRowSizeIncludeBlobs,
                            generatedKeyAlwaysReturned,
                            catalog,
                            catalogSeparator,
                            catalogTerm,
                            clientInfoProperty,
                            databaseMajorVersion,
                            databaseMinorVersion,
                            databaseProductName,
                            databaseProductVersion,
                            defaultTransactionIsolation,
                            driverMajorVersion,
                            driverMinorVersion,
                            driverName,
                            driverVersion,
                            extraNameCharacters,
                            identifierQuoteString,
                            JDBCMajorVersion,
                            JDBCMinorVersion,
                            maxBinaryLiteralLength,
                            maxCatalogNameLength,
                            maxCharLiteralLength,
                            maxColumnNameLength,
                            maxColumnsInGroupBy,
                            maxColumnsInIndex,
                            maxColumnsInOrderBy,
                            maxColumnsInSelect,
                            maxColumnsInTable,
                            maxConnections,
                            maxCursorNameLength,
                            maxIndexLength,
                            maxLogicalLobSize,
                            maxProcedureNameLength,
                            maxRowSize,
                            maxSchemaNameLength,
                            maxStatementLength,
                            maxStatements,
                            maxTableNameLength,
                            maxTablesInSelect,
                            maxUserNameLength,
                            numericFunctions,
                            procedureTerm,
                            resultSetHoldability,
                            schemaTerm,
                            searchStringEscape,
                            SQLKeywords,
                            SQLStateType,
                            stringFunctions,
                            systemFunctions,
                            tableType,
                            timeDateFunctions,
                            typeInfo,
                            URL,
                            userName,
                            insertsAreDetected,
                            catalogAtStart,
                            readOnly,
                            locatorsUpdateCopy,
                            nullPlusNonNullIsNull,
                            nullsAreSortedAtEnd,
                            nullsAreSortedAtStart,
                            nullsAreSortedHigh,
                            nullsAreSortedLow,
                            othersDeletesAreVisible,
                            othersInsertsAreVisible,
                            othersUpdatesAreVisible,
                            ownDeletesAreVisible,
                            ownInsertsAreVisible,
                            ownUpdatesAreVisible,
                            storesLowerCaseIdentifiers,
                            storesLowerCaseQuotedIdentifiers,
                            storesMixedCaseIdentifiers,
                            storesMixedCaseQuotedIdentifiers,
                            supportsAlterTableWithAddColumn,
                            supportsAlterTableWithDropColumn,
                            supportsANSI92EntryLevelSQL,
                            supportsANSI92FullSQL,
                            supportsANSI92IntermediateSQL,
                            supportsBatchUpdates,
                            supportsCatalogsInDataManipulation,
                            supportsCatalogsInIndexDefinitions,
                            supportsCatalogsInPrivilegeDefinitions,
                            supportsCatalogsInProcedureCalls,
                            supportsCatalogsInTableDefinitions,
                            supportsColumnAliasing,
                            supportsConvert,
                            supportsConvert_,
                            supportsCoreSQLGrammar,
                            supportsCorrelatedSubqueries,
                            supportsDataDefinitionAndDataManipulationTransactions,
                            supportsDataManipulationTransactionsOnly,
                            supportsDifferentTableCorrelationNames,
                            supportsExpressionsInOrderBy,
                            supportsExtendedSQLGrammar,
                            supportsFullOuterJoins,
                            supportsGetGeneratedKeys,
                            supportsGroupBy,
                            supportsGroupByBeyondSelect,
                            supportsGroupByUnrelated,
                            supportsIntegrityEnhancementFacility,
                            supportsLikeEscapeClause,
                            supportsLimitedOuterJoins,
                            supportsMinimumSQLGrammar,
                            supportsMixedCaseIdentifiers,
                            supportsMixedCaseQuotedIdentifiers,
                            supportsMultipleOpenResults,
                            supportsMultipleResultSets,
                            supportsMultipleTransactions,
                            supportsNamedParameters,
                            supportsNonNullableColumns,
                            supportsOpenCursorsAcrossCommit,
                            supportsOpenCursorsAcrossRollback,
                            supportsOpenStatementsAcrossCommit,
                            supportsOpenStatementsAcrossRollback,
                            supportsOrderByUnrelated,
                            supportsOuterJoins,
                            supportsPositionedDelete,
                            supportsPositionedUpdate,
                            supportsRefCursors,
                            supportsResultSetConcurrency,
                            supportsResultSetHoldability,
                            supportsResultSetType,
                            supportsSavepoints,
                            supportsSchemasInDataManipulation,
                            supportsSchemasInIndexDefinitions,
                            supportsSchemasInPrivilegeDefinitions,
                            supportsSchemasInProcedureCalls,
                            supportsSchemasInTableDefinitions,
                            supportsSelectForUpdate,
                            supportsSharding,
                            supportsStatementPooling,
                            supportsStoredFunctionsUsingCallSyntax,
                            supportsStoredProcedures,
                            supportsSubqueriesInComparisons,
                            supportsSubqueriesInExists,
                            supportsSubqueriesInIns,
                            supportsSubqueriesInQuantifieds,
                            supportsTableCorrelationNames,
                            supportsTransactionIsolationLevel,
                            supportsTransactions,
                            supportsUnion,
                            supportsUnionAll,
                            updatesAreDetected,
                            usesLocalFilePerTable,
                            usesLocalFiles);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean allProceduresAreCallable;

    @XmlElement(nillable = true)
    private Boolean allTablesAreSelectable;

    @XmlElement(nillable = true)
    private Boolean autoCommitFailureClosesAllResultSets;

    @XmlElement(nillable = true)
    private Boolean dataDefinitionCausesTransactionCommit;

    @XmlElement(nillable = true)
    private Boolean dataDefinitionIgnoredInTransactions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private List<@Valid @NotNull DeletesAreDetected> deletesAreDetected;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean doesMaxRowSizeIncludeBlobs;

    @XmlElement(nillable = true)
    private Boolean generatedKeyAlwaysReturned;

    @XmlElementRef
    private List<@Valid @NotNull Catalog> catalog;

    @XmlElement(nillable = true)
    private String catalogSeparator;

    @XmlElement(nillable = true)
    private String catalogTerm;

    @XmlElementRef
    private List<@Valid @NotNull ClientInfoProperty> clientInfoProperty;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer databaseMajorVersion;

    @XmlElement(nillable = true)
    private Integer databaseMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String databaseProductName;

    @XmlElement(nillable = true)
    private String databaseProductVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer defaultTransactionIsolation;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer driverMajorVersion;

    @XmlElement(nillable = true)
    private Integer driverMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String driverName;

    @XmlElement(nillable = true)
    private String driverVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String extraNameCharacters;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String identifierQuoteString;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer JDBCMajorVersion;

    @XmlElement(nillable = true)
    private Integer JDBCMinorVersion;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer maxBinaryLiteralLength;

    @XmlElement(nillable = true)
    private Integer maxCatalogNameLength;

    @XmlElement(nillable = true)
    private Integer maxCharLiteralLength;

    @XmlElement(nillable = true)
    private Integer maxColumnNameLength;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer maxColumnsInGroupBy;

    @XmlElement(nillable = true)
    private Integer maxColumnsInIndex;

    @XmlElement(nillable = true)
    private Integer maxColumnsInOrderBy;

    @XmlElement(nillable = true)
    private Integer maxColumnsInSelect;

    @XmlElement(nillable = true)
    private Integer maxColumnsInTable;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer maxConnections;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer maxCursorNameLength;

    @XmlElement(nillable = true)
    private Integer maxIndexLength;

    @XmlElement(nillable = true)
    private Long maxLogicalLobSize;

    @XmlElement(nillable = true)
    private Integer maxProcedureNameLength;

    @XmlElement(nillable = true)
    private Integer maxRowSize;

    @XmlElement(nillable = true)
    private Integer maxSchemaNameLength;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer maxStatementLength;

    @XmlElement(nillable = true)
    private Integer maxStatements;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Integer maxTableNameLength;

    @XmlElement(nillable = true)
    private Integer maxTablesInSelect;

    @XmlElement(nillable = true)
    private Integer maxUserNameLength;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String numericFunctions;

    @XmlElement(nillable = true)
    private String procedureTerm;

    @XmlElement(nillable = true)
    private Integer resultSetHoldability;

    @XmlElement(nillable = true)
    private String schemaTerm;

    @XmlElement(nillable = true)
    private String searchStringEscape;

    @XmlElement(nillable = true)
    private String SQLKeywords;

    @XmlElement(nillable = true)
    private Integer SQLStateType;

    @XmlElement(nillable = true)
    private String stringFunctions;

    @XmlElement(nillable = true)
    private String systemFunctions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@NotNull @Valid TableType> tableType;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String timeDateFunctions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull TypeInfo> typeInfo;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private String URL;

    @XmlElement(nillable = true)
    private String userName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull InsertsAreDetected> insertsAreDetected;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean catalogAtStart;

    @XmlElement(nillable = true)
    private Boolean readOnly;

    @XmlElement(nillable = true)
    private Boolean locatorsUpdateCopy;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean nullPlusNonNullIsNull;

    @XmlElement(nillable = true)
    private Boolean nullsAreSortedAtEnd;

    @XmlElement(nillable = true)
    private Boolean nullsAreSortedAtStart;

    @XmlElement(nillable = true)
    private Boolean nullsAreSortedHigh;

    @XmlElement(nillable = true)
    private Boolean nullsAreSortedLow;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull OthersDeletesAreVisible> othersDeletesAreVisible;

    @XmlElementRef
    private List<@Valid @NotNull OthersInsertsAreVisible> othersInsertsAreVisible;

    @XmlElementRef
    private List<@Valid @NotNull OthersUpdatesAreVisible> othersUpdatesAreVisible;

    @XmlElementRef
    private List<@Valid @NotNull OwnDeletesAreVisible> ownDeletesAreVisible;

    @XmlElementRef
    private List<@Valid @NotNull OwnInsertsAreVisible> ownInsertsAreVisible;

    @XmlElementRef
    private List<@Valid @NotNull OwnUpdatesAreVisible> ownUpdatesAreVisible;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean storesLowerCaseIdentifiers;

    @XmlElement(nillable = true)
    private Boolean storesLowerCaseQuotedIdentifiers;

    @XmlElement(nillable = true)
    private Boolean storesMixedCaseIdentifiers;

    @XmlElement(nillable = true)
    private Boolean storesMixedCaseQuotedIdentifiers;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsAlterTableWithAddColumn;

    @XmlElement(nillable = true)
    private Boolean supportsAlterTableWithDropColumn;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsANSI92EntryLevelSQL;

    @XmlElement(nillable = true)
    private Boolean supportsANSI92FullSQL;

    @XmlElement(nillable = true)
    private Boolean supportsANSI92IntermediateSQL;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsBatchUpdates;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsCatalogsInDataManipulation;

    @XmlElement(nillable = true)
    private Boolean supportsCatalogsInIndexDefinitions;

    @XmlElement(nillable = true)
    private Boolean supportsCatalogsInPrivilegeDefinitions;

    @XmlElement(nillable = true)
    private Boolean supportsCatalogsInProcedureCalls;

    @XmlElement(nillable = true)
    private Boolean supportsCatalogsInTableDefinitions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsColumnAliasing;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsConvert;

    @XmlElementRef
    private List<@Valid @NotNull SupportsConvert> supportsConvert_;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsCoreSQLGrammar;

    @XmlElement(nillable = true)
    private Boolean supportsCorrelatedSubqueries;

    @XmlElement(nillable = true)
    private Boolean supportsDataDefinitionAndDataManipulationTransactions;

    @XmlElement(nillable = true)
    private Boolean supportsDataManipulationTransactionsOnly;

    @XmlElement(nillable = true)
    private Boolean supportsDifferentTableCorrelationNames;

    @XmlElement(nillable = true)
    private Boolean supportsExpressionsInOrderBy;

    @XmlElement(nillable = true)
    private Boolean supportsExtendedSQLGrammar;

    @XmlElement(nillable = true)
    private Boolean supportsFullOuterJoins;

    @XmlElement(nillable = true)
    private Boolean supportsGetGeneratedKeys;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsGroupBy;

    @XmlElement(nillable = true)
    private Boolean supportsGroupByBeyondSelect;

    @XmlElement(nillable = true)
    private Boolean supportsGroupByUnrelated;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsIntegrityEnhancementFacility;

    @XmlElement(nillable = true)
    private Boolean supportsLikeEscapeClause;

    @XmlElement(nillable = true)
    private Boolean supportsLimitedOuterJoins;

    @XmlElement(nillable = true)
    private Boolean supportsMinimumSQLGrammar;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsMixedCaseIdentifiers;

    @XmlElement(nillable = true)
    private Boolean supportsMixedCaseQuotedIdentifiers;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsMultipleOpenResults;

    @XmlElement(nillable = true)
    private Boolean supportsMultipleResultSets;

    @XmlElement(nillable = true)
    private Boolean supportsMultipleTransactions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsNamedParameters;

    @XmlElement(nillable = true)
    private Boolean supportsNonNullableColumns;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsOpenCursorsAcrossCommit;

    @XmlElement(nillable = true)
    private Boolean supportsOpenCursorsAcrossRollback;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsOpenStatementsAcrossCommit;

    @XmlElement(nillable = true)
    private Boolean supportsOpenStatementsAcrossRollback;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsOrderByUnrelated;

    @XmlElement(nillable = true)
    private Boolean supportsOuterJoins;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsPositionedDelete;

    @XmlElement(nillable = true)
    private Boolean supportsPositionedUpdate;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsRefCursors;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull SupportsResultSetConcurrency> supportsResultSetConcurrency;

    @XmlElementRef
    private List<@Valid @NotNull SupportsResultSetHoldability> supportsResultSetHoldability;

    @XmlElementRef
    private List<@Valid @NotNull SupportsResultSetType> supportsResultSetType;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsSavepoints;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsSchemasInDataManipulation;

    @XmlElement(nillable = true)
    private Boolean supportsSchemasInIndexDefinitions;

    @XmlElement(nillable = true)
    private Boolean supportsSchemasInPrivilegeDefinitions;

    @XmlElement(nillable = true)
    private Boolean supportsSchemasInProcedureCalls;

    @XmlElement(nillable = true)
    private Boolean supportsSchemasInTableDefinitions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsSelectForUpdate;

    @XmlElement(nillable = true)
    private Boolean supportsSharding;

    @XmlElement(nillable = true)
    private Boolean supportsStatementPooling;

    @XmlElement(nillable = true)
    private Boolean supportsStoredFunctionsUsingCallSyntax;

    @XmlElement(nillable = true)
    private Boolean supportsStoredProcedures;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsSubqueriesInComparisons;

    @XmlElement(nillable = true)
    private Boolean supportsSubqueriesInExists;

    @XmlElement(nillable = true)
    private Boolean supportsSubqueriesInIns;

    @XmlElement(nillable = true)
    private Boolean supportsSubqueriesInQuantifieds;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsTableCorrelationNames;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull SupportsTransactionIsolationLevel> supportsTransactionIsolationLevel;

    @XmlElement(nillable = true)
    private Boolean supportsTransactions;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean supportsUnion;

    @XmlElement(nillable = true)
    private Boolean supportsUnionAll;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull UpdatesAreDetected> updatesAreDetected;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    private Boolean usesLocalFilePerTable;

    @XmlElement(nillable = true)
    private Boolean usesLocalFiles;
}
