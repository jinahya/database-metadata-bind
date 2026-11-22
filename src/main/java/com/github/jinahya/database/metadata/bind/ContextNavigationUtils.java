package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Utilities for finding, through a {@link Context}, where in a connection's catalog/schema tree a known object lives.
 * <p>
 * A connection may see many catalogs and schemas, and which ones - and at which level a given engine's namespace is
 * reported - depends on the connection's URL, its user, and its driver's settings. Code that knows only the name of an
 * object it depends on cannot tell in advance which catalog/schema arguments to pass. The methods here answer that from
 * what the driver reports, as {@link Location}s - catalog/schema pairs - for each kind of object that JDBC places
 * directly in a catalog/schema: tables, procedures, functions, and user-defined types.
 * <h2 id="contract">Common behavior</h2>
 * <p>
 * Every method makes exactly one call of the corresponding {@link DatabaseMetaData} method, with {@code null} for both
 * its {@code catalog} and its {@code schemaPattern} argument, and the specified name pattern passed through verbatim -
 * so unescaped {@code _} and {@code %} in it are wildcards. To search for a literal name containing either character,
 * escape it using the driver's {@linkplain DatabaseMetaData#getSearchStringEscape() search string escape}. Every
 * returned row is bound. A broad pattern on a large database can be expensive.
 * <p>
 * A {@code null} catalog and schema asks the driver not to narrow by either, but what that covers is the driver's to
 * decide: some drivers can be configured to read a {@code null} catalog as the connection's current one, and every
 * driver returns only what the connection's user may see. A result therefore says where this connection reports the
 * object, not where the object exists.
 * <p>
 * Each row yields the pair of its catalog and schema values, exactly as reported - either may be {@code null}, and
 * values are compared exactly, without case folding. {@code getLocationsOfXxx} returns the distinct pairs in the order
 * each is first seen. {@code getOnlyLocationOfXxx} returns the one pair all rows share, and refuses rather than guesses
 * when there is not exactly one:
 * <ul>
 *   <li>{@link NoSuchElementException} when there is no row;</li>
 *   <li>{@link IllegalArgumentException} when the rows are in more than one pair, so the pattern did not identify one
 *       location.</li>
 * </ul>
 * A pair with {@code null} components is a successful result, meaning the object is reported without that level, as
 * on a driver that does not support it. A failure of the underlying call - including a
 * {@link java.sql.SQLFeatureNotSupportedException} from a driver that does not implement it - propagates as is, and
 * is never reported as "no row".
 * <h2 id="handoff">Using a result</h2>
 * <p>
 * Pass the components of a result through {@link Location#catalogForLookup()},
 * {@link Location#schemaForLookup()}, or {@link Location#schemaPatternForLookup(Context)}, not as
 * they are: those map an absent level to {@code ""}, which selects objects without that level, and escape a schema
 * name that is about to be used as a pattern. A schema name passed as a pattern unescaped can match other schemas too.
 * {@snippet lang = "java":
 * // where does this application's ORDERS table live on this connection?
 * final var location = ContextNavigationUtils.getOnlyLocationOfTables(context, "ORDERS");
 * final var tables = context.getTables(
 *         location.catalogForLookup(), location.schemaPatternForLookup(context), "%", null);
 *}
 * <h2 id="routines">Procedures and functions</h2>
 * <p>
 * For procedures and functions the pair is the {@code PROCEDURE_CAT}/{@code PROCEDURE_SCHEM} or
 * {@code FUNCTION_CAT}/{@code FUNCTION_SCHEM} values as that method reports them, and drivers do not all use those
 * columns for a catalog: some report, for a routine declared in a package, the package's name in the catalog column.
 * Routines of one schema in different packages then yield different pairs. Likewise, which routines
 * {@link DatabaseMetaData#getProcedures(String, String, String)} reports - procedures only, or functions as well - is
 * the driver's choice, and can depend on its settings and on the server's version.
 *
 * @see Location
 */
public final class ContextNavigationUtils {

    /**
     * A non-narrowing lookup that accepts each bound row to the specified consumer.
     *
     * @param <T> bound type parameter
     */
    @FunctionalInterface
    private interface Lookup<T> {

        void forEach(Consumer<? super T> consumer) throws SQLException;
    }

    private static <T> Set<Location> distinct(
            final Lookup<T> lookup,
            final java.util.function.Function<? super T, @Nullable String> catalogOf,
            final java.util.function.Function<? super T, @Nullable String> schemaOf)
            throws SQLException {
        final Set<Location> pairs = new LinkedHashSet<>();
        lookup.forEach(row -> pairs.add(new Location(catalogOf.apply(row), schemaOf.apply(row))));
        return pairs;
    }

    private static <T> List<Location> all(
            final Lookup<T> lookup,
            final java.util.function.Function<? super T, @Nullable String> catalogOf,
            final java.util.function.Function<? super T, @Nullable String> schemaOf)
            throws SQLException {
        return new ArrayList<>(distinct(lookup, catalogOf, schemaOf));
    }

    private static <T> Location only(
            final Lookup<T> lookup,
            final java.util.function.Function<? super T, @Nullable String> catalogOf,
            final java.util.function.Function<? super T, @Nullable String> schemaOf,
            final String kind, final String namePattern)
            throws SQLException {
        final var pairs = distinct(lookup, catalogOf, schemaOf);
        if (pairs.isEmpty()) {
            throw new NoSuchElementException("no " + kind + " matches '" + namePattern + "'");
        }
        if (pairs.size() > 1) {
            throw new IllegalArgumentException(
                    kind + "s matching '" + namePattern + "' are in more than one location: " + pairs);
        }
        return pairs.iterator().next();
    }

    // ---------------------------------------------------------------------------------------------------------- tables

    /**
     * Returns the distinct {@code TABLE_CAT}/{@code TABLE_SCHEM} pairs of tables, of any table type, matching the
     * specified table name pattern, as described in <a href="#contract">common behavior</a>.
     *
     * @param context          the context whose tables are searched.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter of
     *                         {@link Context#getTables(String, String, String, String[])}.
     * @return a list of distinct pairs, in the order each is first seen; may be empty but never {@code null}.
     * @throws SQLException if a database error occurs.
     * @see #getLocationsOfTables(Context, String, String[])
     */
    public static List<Location> getLocationsOfTables(final Context context,
                                                      final String tableNamePattern)
            throws SQLException {
        return getLocationsOfTables(context, tableNamePattern, null);
    }

    /**
     * Returns the distinct {@code TABLE_CAT}/{@code TABLE_SCHEM} pairs of tables, of the specified table types,
     * matching the specified table name pattern, as described in <a href="#contract">common behavior</a>.
     *
     * @param context          the context whose tables are searched.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter of
     *                         {@link Context#getTables(String, String, String, String[])}.
     * @param types            a value for the {@code types} parameter of
     *                         {@link Context#getTables(String, String, String, String[])}, as reported by
     *                         {@link Context#getTableTypes()}; {@code null} for all types.
     * @return a list of distinct pairs, in the order each is first seen; may be empty but never {@code null}.
     * @throws SQLException if a database error occurs.
     */
    public static List<Location> getLocationsOfTables(final Context context,
                                                      final String tableNamePattern,
                                                      @Nullable final String[] types)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableNamePattern, "tableNamePattern is null");
        return all(c -> context.forEachTable(null, null, tableNamePattern, types, c),
                   Table::getTableCat, Table::getTableSchem);
    }

    /**
     * Returns the only {@code TABLE_CAT}/{@code TABLE_SCHEM} pair of tables, of any table type, matching the specified
     * table name pattern, as described in <a href="#contract">common behavior</a>.
     * <p>
     * Since every table type is included, a view and a base table of the same name in different schemas make the result
     * ambiguous; narrow by type with {@link #getOnlyLocationOfTables(Context, String, String[])}.
     *
     * @param context          the context whose tables are searched.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter of
     *                         {@link Context#getTables(String, String, String, String[])}; normally an exact table
     *                         name.
     * @return the only pair of the matching tables.
     * @throws SQLException             if a database error occurs.
     * @throws NoSuchElementException   if no table matches.
     * @throws IllegalArgumentException if matching tables are in more than one pair.
     */
    public static Location getOnlyLocationOfTables(final Context context,
                                                   final String tableNamePattern)
            throws SQLException {
        return getOnlyLocationOfTables(context, tableNamePattern, null);
    }

    /**
     * Returns the only {@code TABLE_CAT}/{@code TABLE_SCHEM} pair of tables, of the specified table types, matching the
     * specified table name pattern, as described in <a href="#contract">common behavior</a>.
     *
     * @param context          the context whose tables are searched.
     * @param tableNamePattern a value for the {@code tableNamePattern} parameter of
     *                         {@link Context#getTables(String, String, String, String[])}; normally an exact table
     *                         name.
     * @param types            a value for the {@code types} parameter of
     *                         {@link Context#getTables(String, String, String, String[])}, as reported by
     *                         {@link Context#getTableTypes()}; {@code null} for all types.
     * @return the only pair of the matching tables.
     * @throws SQLException             if a database error occurs.
     * @throws NoSuchElementException   if no table matches.
     * @throws IllegalArgumentException if matching tables are in more than one pair.
     */
    public static Location getOnlyLocationOfTables(final Context context,
                                                   final String tableNamePattern,
                                                   @Nullable final String[] types)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(tableNamePattern, "tableNamePattern is null");
        return only(c -> context.forEachTable(null, null, tableNamePattern, types, c),
                    Table::getTableCat, Table::getTableSchem, "table", tableNamePattern);
    }

    // ------------------------------------------------------------------------------------------------------ procedures

    /**
     * Returns the distinct {@code PROCEDURE_CAT}/{@code PROCEDURE_SCHEM} pairs of procedures matching the specified
     * procedure name pattern, as described in <a href="#contract">common behavior</a> and subject to the
     * <a href="#routines">caveats for procedures and functions</a>.
     *
     * @param context              the context whose procedures are searched.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter of
     *                             {@link Context#getProcedures(String, String, String)}.
     * @return a list of distinct pairs, in the order each is first seen; may be empty but never {@code null}.
     * @throws SQLException if a database error occurs.
     */
    public static List<Location> getLocationsOfProcedures(final Context context,
                                                          final String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureNamePattern, "procedureNamePattern is null");
        return all(c -> context.forEachProcedure(null, null, procedureNamePattern, c),
                   Procedure::getProcedureCat, Procedure::getProcedureSchem);
    }

    /**
     * Returns the only {@code PROCEDURE_CAT}/{@code PROCEDURE_SCHEM} pair of procedures matching the specified
     * procedure name pattern, as described in <a href="#contract">common behavior</a> and subject to the
     * <a href="#routines">caveats for procedures and functions</a>.
     *
     * @param context              the context whose procedures are searched.
     * @param procedureNamePattern a value for the {@code procedureNamePattern} parameter of
     *                             {@link Context#getProcedures(String, String, String)}; normally an exact name.
     * @return the only pair of the matching procedures.
     * @throws SQLException             if a database error occurs.
     * @throws NoSuchElementException   if no procedure matches.
     * @throws IllegalArgumentException if matching procedures are in more than one pair.
     */
    public static Location getOnlyLocationOfProcedures(final Context context,
                                                       final String procedureNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(procedureNamePattern, "procedureNamePattern is null");
        return only(c -> context.forEachProcedure(null, null, procedureNamePattern, c),
                    Procedure::getProcedureCat, Procedure::getProcedureSchem, "procedure", procedureNamePattern);
    }

    // ------------------------------------------------------------------------------------------------------- functions

    /**
     * Returns the distinct {@code FUNCTION_CAT}/{@code FUNCTION_SCHEM} pairs of functions matching the specified
     * function name pattern, as described in <a href="#contract">common behavior</a> and subject to the
     * <a href="#routines">caveats for procedures and functions</a>.
     *
     * @param context             the context whose functions are searched.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter of
     *                            {@link Context#getFunctions(String, String, String)}.
     * @return a list of distinct pairs, in the order each is first seen; may be empty but never {@code null}.
     * @throws SQLException if a database error occurs.
     */
    public static List<Location> getLocationsOfFunctions(final Context context,
                                                         final String functionNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionNamePattern, "functionNamePattern is null");
        return all(c -> context.forEachFunction(null, null, functionNamePattern, c),
                   Function::getFunctionCat, Function::getFunctionSchem);
    }

    /**
     * Returns the only {@code FUNCTION_CAT}/{@code FUNCTION_SCHEM} pair of functions matching the specified function
     * name pattern, as described in <a href="#contract">common behavior</a> and subject to the
     * <a href="#routines">caveats for procedures and functions</a>.
     *
     * @param context             the context whose functions are searched.
     * @param functionNamePattern a value for the {@code functionNamePattern} parameter of
     *                            {@link Context#getFunctions(String, String, String)}; normally an exact name.
     * @return the only pair of the matching functions.
     * @throws SQLException             if a database error occurs.
     * @throws NoSuchElementException   if no function matches.
     * @throws IllegalArgumentException if matching functions are in more than one pair.
     */
    public static Location getOnlyLocationOfFunctions(final Context context,
                                                      final String functionNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(functionNamePattern, "functionNamePattern is null");
        return only(c -> context.forEachFunction(null, null, functionNamePattern, c),
                    Function::getFunctionCat, Function::getFunctionSchem, "function", functionNamePattern);
    }

    // ------------------------------------------------------------------------------------------------------------ UDTs

    /**
     * Returns the distinct {@code TYPE_CAT}/{@code TYPE_SCHEM} pairs of user-defined types, of any kind, matching the
     * specified type name pattern, as described in <a href="#contract">common behavior</a>.
     *
     * @param context         the context whose user-defined types are searched.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter of
     *                        {@link Context#getUDTs(String, String, String, int[])}.
     * @return a list of distinct pairs, in the order each is first seen; may be empty but never {@code null}.
     * @throws SQLException if a database error occurs.
     * @see #getLocationsOfUDTs(Context, String, int[])
     */
    public static List<Location> getLocationsOfUDTs(final Context context,
                                                    final String typeNamePattern)
            throws SQLException {
        return getLocationsOfUDTs(context, typeNamePattern, null);
    }

    /**
     * Returns the distinct {@code TYPE_CAT}/{@code TYPE_SCHEM} pairs of user-defined types, of the specified kinds,
     * matching the specified type name pattern, as described in <a href="#contract">common behavior</a>.
     *
     * @param context         the context whose user-defined types are searched.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter of
     *                        {@link Context#getUDTs(String, String, String, int[])}.
     * @param types           a value for the {@code types} parameter of
     *                        {@link Context#getUDTs(String, String, String, int[])}; {@code null} for all kinds.
     * @return a list of distinct pairs, in the order each is first seen; may be empty but never {@code null}.
     * @throws SQLException if a database error occurs.
     */
    public static List<Location> getLocationsOfUDTs(final Context context,
                                                    final String typeNamePattern,
                                                    @Nullable final int[] types)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeNamePattern, "typeNamePattern is null");
        return all(c -> context.forEachUDT(null, null, typeNamePattern, types, c),
                   UDT::getTypeCat, UDT::getTypeSchem);
    }

    /**
     * Returns the only {@code TYPE_CAT}/{@code TYPE_SCHEM} pair of user-defined types, of any kind, matching the
     * specified type name pattern, as described in <a href="#contract">common behavior</a>.
     *
     * @param context         the context whose user-defined types are searched.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter of
     *                        {@link Context#getUDTs(String, String, String, int[])}; normally an exact name.
     * @return the only pair of the matching user-defined types.
     * @throws SQLException             if a database error occurs.
     * @throws NoSuchElementException   if no user-defined type matches.
     * @throws IllegalArgumentException if matching user-defined types are in more than one pair.
     * @see #getOnlyLocationOfUDTs(Context, String, int[])
     */
    public static Location getOnlyLocationOfUDTs(final Context context,
                                                 final String typeNamePattern)
            throws SQLException {
        return getOnlyLocationOfUDTs(context, typeNamePattern, null);
    }

    /**
     * Returns the only {@code TYPE_CAT}/{@code TYPE_SCHEM} pair of user-defined types, of the specified kinds, matching
     * the specified type name pattern, as described in <a href="#contract">common behavior</a>.
     *
     * @param context         the context whose user-defined types are searched.
     * @param typeNamePattern a value for the {@code typeNamePattern} parameter of
     *                        {@link Context#getUDTs(String, String, String, int[])}; normally an exact name.
     * @param types           a value for the {@code types} parameter of
     *                        {@link Context#getUDTs(String, String, String, int[])}; {@code null} for all kinds.
     * @return the only pair of the matching user-defined types.
     * @throws SQLException             if a database error occurs.
     * @throws NoSuchElementException   if no user-defined type matches.
     * @throws IllegalArgumentException if matching user-defined types are in more than one pair.
     */
    public static Location getOnlyLocationOfUDTs(final Context context,
                                                 final String typeNamePattern,
                                                 @Nullable final int[] types)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(typeNamePattern, "typeNamePattern is null");
        return only(c -> context.forEachUDT(null, null, typeNamePattern, types, c),
                    UDT::getTypeCat, UDT::getTypeSchem, "user-defined type", typeNamePattern);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ContextNavigationUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
