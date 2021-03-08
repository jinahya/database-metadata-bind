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

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final Map<String, Method> METHODS_WITH_NO_PARAMETERS = Collections.unmodifiableMap(
            Arrays.stream(DatabaseMetaData.class.getMethods())
                    .filter(m -> m.getParameterCount() == 0)
                    .peek(f -> f.setAccessible(true))
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
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.allProceduresAreCallable = context.databaseMetaData.allProceduresAreCallable();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.allTablesAreSelectable = context.databaseMetaData.allTablesAreSelectable();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.autoCommitFailureClosesAllResultSets
//                    = context.databaseMetaData.autoCommitFailureClosesAllResultSets();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.dataDefinitionCausesTransactionCommit
//                    = context.databaseMetaData.dataDefinitionCausesTransactionCommit();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.dataDefinitionIgnoredInTransactions
//                    = context.databaseMetaData.dataDefinitionIgnoredInTransactions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
        instance.deletesAreDetected = DeletesAreDetected.all(context);
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.doesMaxRowSizeIncludeBlobs
//                    = context.databaseMetaData.doesMaxRowSizeIncludeBlobs();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.generatedKeyAlwaysReturned
//                    = context.databaseMetaData.generatedKeyAlwaysReturned();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // ---------------------------------------------------------------------------------------------------- catalogs
        instance.catalog = context.getCatalogs(new ArrayList<>());
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
            all.remove(Metadata.class.getDeclaredField("catalog"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
//        try {
//            instance.catalogSeparator = context.databaseMetaData.getCatalogSeparator();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.catalogTerm = context.databaseMetaData.getCatalogTerm();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // ------------------------------------------------------------------------------------------ clientInfoProperty
        instance.clientInfoProperty = new ArrayList<>();
        context.getClientInfoProperties(instance.clientInfoProperty);
        try {
            all.remove(Metadata.class.getDeclaredField("clientInfoProperty"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.databaseMajorVersion = context.databaseMetaData.getDatabaseMajorVersion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.databaseMinorVersion = context.databaseMetaData.getDatabaseMinorVersion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.databaseProductName = context.databaseMetaData.getDatabaseProductName();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.databaseProductVersion = context.databaseMetaData.getDatabaseProductVersion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.defaultTransactionIsolation = context.databaseMetaData.getDefaultTransactionIsolation();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        instance.driverMajorVersion = context.databaseMetaData.getDriverMajorVersion();
//        instance.driverMinorVersion = context.databaseMetaData.getDriverMinorVersion();
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.driverName = context.databaseMetaData.getDriverName();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.driverVersion = context.databaseMetaData.getDriverVersion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.extraNameCharacters = context.databaseMetaData.getExtraNameCharacters();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.identifierQuoteString = context.databaseMetaData.getIdentifierQuoteString();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.JDBCMajorVersion = context.databaseMetaData.getJDBCMajorVersion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.JDBCMinorVersion = context.databaseMetaData.getJDBCMinorVersion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.maxBinaryLiteralLength = context.databaseMetaData.getMaxBinaryLiteralLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxCatalogNameLength = context.databaseMetaData.getMaxCatalogNameLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxCharLiteralLength = context.databaseMetaData.getMaxCharLiteralLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxColumnNameLength = context.databaseMetaData.getMaxColumnNameLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.maxColumnsInGroupBy = context.databaseMetaData.getMaxColumnsInGroupBy();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxColumnsInIndex = context.databaseMetaData.getMaxColumnsInIndex();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxColumnsInOrderBy = context.databaseMetaData.getMaxColumnsInOrderBy();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxColumnsInSelect = context.databaseMetaData.getMaxColumnsInSelect();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxColumnsInTable = context.databaseMetaData.getMaxColumnsInTable();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.maxConnections = context.databaseMetaData.getMaxConnections();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.maxCursorNameLength = context.databaseMetaData.getMaxCursorNameLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxIndexLength = context.databaseMetaData.getMaxIndexLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxLogicalLobSize = context.databaseMetaData.getMaxLogicalLobSize();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxProcedureNameLength = context.databaseMetaData.getMaxProcedureNameLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxRowSize = context.databaseMetaData.getMaxRowSize();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxSchemaNameLength = context.databaseMetaData.getMaxSchemaNameLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxStatementLength = context.databaseMetaData.getMaxStatementLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxStatements = context.databaseMetaData.getMaxStatements();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxTableNameLength = context.databaseMetaData.getMaxTableNameLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxTablesInSelect = context.databaseMetaData.getMaxTablesInSelect();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.maxUserNameLength = context.databaseMetaData.getMaxUserNameLength();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.numericFunctions = context.databaseMetaData.getNumericFunctions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.procedureTerm = context.databaseMetaData.getProcedureTerm();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.resultSetHoldability = context.databaseMetaData.getResultSetHoldability();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.schemaTerm = context.databaseMetaData.getSchemaTerm();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.searchStringEscape = context.databaseMetaData.getSearchStringEscape();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.SQLKeywords = context.databaseMetaData.getSQLKeywords();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.SQLStateType = context.databaseMetaData.getSQLStateType();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.SQLStateType = context.databaseMetaData.getSQLStateType();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.systemFunctions = context.databaseMetaData.getSystemFunctions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
        instance.tableType = new ArrayList<>();
        context.getTableTypes(instance.tableType);
        try {
            all.remove(Metadata.class.getDeclaredField("tableType"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.timeDateFunctions = context.databaseMetaData.getTimeDateFunctions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // --------------------------------------------------------------------------------------------------- typeInfos
        instance.typeInfo = new ArrayList<>();
        context.getTypeInfo(instance.typeInfo);
        try {
            all.remove(Metadata.class.getDeclaredField("typeInfo"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.URL = context.databaseMetaData.getURL();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.userName = context.databaseMetaData.getUserName();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
        instance.insertsAreDetected = InsertsAreDetected.all(context);
        try {
            all.remove(Metadata.class.getDeclaredField("insertsAreDetected"));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.catalogAtStart = context.databaseMetaData.isCatalogAtStart();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.readOnly = context.databaseMetaData.isReadOnly();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.locatorsUpdateCopy = context.databaseMetaData.locatorsUpdateCopy();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.nullPlusNonNullIsNull = context.databaseMetaData.nullPlusNonNullIsNull();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.nullsAreSortedAtEnd = context.databaseMetaData.nullsAreSortedAtEnd();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.nullsAreSortedAtStart = context.databaseMetaData.nullsAreSortedAtStart();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.nullsAreSortedHigh = context.databaseMetaData.nullsAreSortedHigh();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.nullsAreSortedLow = context.databaseMetaData.nullsAreSortedLow();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
        instance.othersDeletesAreVisible = OthersDeletesAreVisible.all(context);
        instance.othersInsertsAreVisible = OthersInsertsAreVisible.all(context);
        instance.othersUpdatesAreVisible = OthersUpdatesAreVisible.all(context);
        instance.ownDeletesAreVisible = OwnDeletesAreVisible.all(context);
        instance.ownInsertsAreVisible = OwnInsertsAreVisible.all(context);
        instance.ownUpdatesAreVisible = OwnUpdatesAreVisible.all(context);
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.storesLowerCaseIdentifiers = context.databaseMetaData.storesLowerCaseIdentifiers();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.storesLowerCaseQuotedIdentifiers
//                    = context.databaseMetaData.storesLowerCaseQuotedIdentifiers();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.storesMixedCaseIdentifiers = context.databaseMetaData.storesMixedCaseIdentifiers();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.storesMixedCaseQuotedIdentifiers
//                    = context.databaseMetaData.storesMixedCaseQuotedIdentifiers();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsAlterTableWithAddColumn
//                    = context.databaseMetaData.supportsAlterTableWithAddColumn();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsAlterTableWithDropColumn
//                    = context.databaseMetaData.supportsAlterTableWithDropColumn();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsANSI92EntryLevelSQL = context.databaseMetaData.supportsANSI92EntryLevelSQL();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsANSI92FullSQL = context.databaseMetaData.supportsANSI92FullSQL();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsANSI92IntermediateSQL = context.databaseMetaData.supportsANSI92IntermediateSQL();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsBatchUpdates = context.databaseMetaData.supportsBatchUpdates();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsCatalogsInDataManipulation
//                    = context.databaseMetaData.supportsCatalogsInDataManipulation();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsCatalogsInIndexDefinitions
//                    = context.databaseMetaData.supportsCatalogsInIndexDefinitions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsCatalogsInPrivilegeDefinitions
//                    = context.databaseMetaData.supportsCatalogsInPrivilegeDefinitions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsCatalogsInProcedureCalls
//                    = context.databaseMetaData.supportsCatalogsInProcedureCalls();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsCatalogsInTableDefinitions
//                    = context.databaseMetaData.supportsCatalogsInTableDefinitions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsColumnAliasing = context.databaseMetaData.supportsColumnAliasing();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsConvert = context.databaseMetaData.supportsConvert();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        instance.supportsConvert_ = SupportsConvert.list(context);
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsCoreSQLGrammar = context.databaseMetaData.supportsCoreSQLGrammar();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsCorrelatedSubqueries = context.databaseMetaData.supportsCorrelatedSubqueries();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsDataDefinitionAndDataManipulationTransactions
//                    = context.databaseMetaData.supportsDataDefinitionAndDataManipulationTransactions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsDataManipulationTransactionsOnly
//                    = context.databaseMetaData.supportsDataManipulationTransactionsOnly();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsDifferentTableCorrelationNames
//                    = context.databaseMetaData.supportsDifferentTableCorrelationNames();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsExpressionsInOrderBy
//                    = context.databaseMetaData.supportsExpressionsInOrderBy();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsExtendedSQLGrammar = context.databaseMetaData.supportsExtendedSQLGrammar();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsFullOuterJoins = context.databaseMetaData.supportsFullOuterJoins();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsGetGeneratedKeys = context.databaseMetaData.supportsGetGeneratedKeys();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsGroupBy = context.databaseMetaData.supportsGroupBy();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsGroupByBeyondSelect = context.databaseMetaData.supportsGroupByBeyondSelect();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsGroupByUnrelated = context.databaseMetaData.supportsGroupByUnrelated();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsIntegrityEnhancementFacility
//                    = context.databaseMetaData.supportsIntegrityEnhancementFacility();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsLikeEscapeClause = context.databaseMetaData.supportsLikeEscapeClause();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsLimitedOuterJoins = context.databaseMetaData.supportsLimitedOuterJoins();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsMinimumSQLGrammar = context.databaseMetaData.supportsMinimumSQLGrammar();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsMixedCaseIdentifiers = context.databaseMetaData.supportsMixedCaseIdentifiers();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsMixedCaseQuotedIdentifiers
//                    = context.databaseMetaData.supportsMixedCaseQuotedIdentifiers();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsMultipleOpenResults = context.databaseMetaData.supportsMultipleOpenResults();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsMultipleResultSets = context.databaseMetaData.supportsMultipleResultSets();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsMultipleTransactions = context.databaseMetaData.supportsMultipleTransactions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsNamedParameters = context.databaseMetaData.supportsNamedParameters();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsNonNullableColumns = context.databaseMetaData.supportsNonNullableColumns();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsOpenCursorsAcrossCommit
//                    = context.databaseMetaData.supportsOpenCursorsAcrossCommit();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsOpenCursorsAcrossRollback
//                    = context.databaseMetaData.supportsOpenCursorsAcrossRollback();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsOpenStatementsAcrossCommit
//                    = context.databaseMetaData.supportsOpenStatementsAcrossCommit();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsOpenStatementsAcrossRollback
//                    = context.databaseMetaData.supportsOpenStatementsAcrossRollback();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsOrderByUnrelated = context.databaseMetaData.supportsOrderByUnrelated();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsOuterJoins = context.databaseMetaData.supportsOuterJoins();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsPositionedDelete = context.databaseMetaData.supportsPositionedDelete();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsPositionedUpdate = context.databaseMetaData.supportsPositionedUpdate();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsRefCursors = context.databaseMetaData.supportsRefCursors();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
        instance.supportsResultSetConcurrency = SupportsResultSetConcurrency.list(context);
        instance.supportsResultSetHoldability = SupportsResultSetHoldability.list(context);
        instance.supportsResultSetType = SupportsResultSetType.list(context);
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsSavepoints = context.databaseMetaData.supportsSavepoints();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsSchemasInDataManipulation
//                    = context.databaseMetaData.supportsSchemasInDataManipulation();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsSchemasInIndexDefinitions
//                    = context.databaseMetaData.supportsSchemasInIndexDefinitions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsSchemasInPrivilegeDefinitions
//                    = context.databaseMetaData.supportsSchemasInPrivilegeDefinitions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsSchemasInProcedureCalls
//                    = context.databaseMetaData.supportsSchemasInProcedureCalls();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsSchemasInTableDefinitions
//                    = context.databaseMetaData.supportsSchemasInTableDefinitions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsSelectForUpdate = context.databaseMetaData.supportsSelectForUpdate();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsSharding = context.databaseMetaData.supportsSharding(); // Since 9;
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            Utils.logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsStatementPooling = context.databaseMetaData.supportsStatementPooling();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsStoredFunctionsUsingCallSyntax
//                    = context.databaseMetaData.supportsStoredFunctionsUsingCallSyntax();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsStoredProcedures = context.databaseMetaData.supportsStoredProcedures();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsSubqueriesInComparisons
//                    = context.databaseMetaData.supportsSubqueriesInComparisons();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsSubqueriesInExists = context.databaseMetaData.supportsSubqueriesInExists();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsSubqueriesInIns = context.databaseMetaData.supportsSubqueriesInIns();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsSubqueriesInQuantifieds
//                    = context.databaseMetaData.supportsSubqueriesInQuantifieds();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsTableCorrelationNames = context.databaseMetaData.supportsTableCorrelationNames();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
        instance.supportsTransactionIsolationLevel = SupportsTransactionIsolationLevel.list(context);
//        try {
//            instance.supportsTransactions = context.databaseMetaData.supportsTransactions();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.supportsUnion = context.databaseMetaData.supportsUnion();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.supportsUnionAll = context.databaseMetaData.supportsUnionAll();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
        instance.updatesAreDetected = UpdatesAreDetected.all(context);
        // -------------------------------------------------------------------------------------------------------------
//        try {
//            instance.usesLocalFilePerTable = context.databaseMetaData.usesLocalFilePerTable();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
//        try {
//            instance.usesLocalFiles = context.databaseMetaData.usesLocalFiles();
//        } catch (final SQLFeatureNotSupportedException sqlfnse) {
//            logSqlFeatureNotSupportedException(logger, sqlfnse);
//        }
        // -------------------------------------------------------------------------------------------------------------
        all.forEach(f -> {
            try {
                if (f.get(instance) == null) {
                    logger.log(Level.WARNING, () -> format("unhandled field: %1$s", f));
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
    @Invoke
    public Boolean allProceduresAreCallable;

    @XmlElement(nillable = true)
    @Invoke
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
    public Boolean supportsOpenCursorsAcrossRollback;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    public Boolean supportsOpenStatementsAcrossCommit;

    @XmlElement(nillable = true)
    public Boolean supportsOpenStatementsAcrossRollback;

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
