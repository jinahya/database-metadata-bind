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

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A fail-safe walkthrough for public {@link Context} metadata binding methods.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class ContextMetadataWalkthrough {

    private static final Comparator<String> DIAGNOSTIC_STRING_COMPARATOR =
            Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER);

    /**
     * The maximum number of distinct constraint violations reported by {@link #assertNoConstraintViolations()}.
     */
    private static final int VIOLATIONS_TO_REPORT = 20;

    /**
     * The maximum number of constraint violation descriptions retained. Violations keep being counted past this
     * point; only their (long) descriptions are dropped, so that a driver emitting a violation on every row of a
     * large catalog can't exhaust the heap.
     */
    private static final int VIOLATIONS_TO_RETAIN = 1024;

    record Result(int attempted, int succeeded, int failed) {

    }

    @FunctionalInterface
    interface Sink {

        static Sink noop() {
            return (rootElementName, itemElementName, values) -> {
                // empty
            };
        }

        void accept(String rootElementName, String itemElementName, List<?> values) throws Exception;
    }

    @FunctionalInterface
    private interface Query<T> {

        List<T> get() throws SQLException;
    }

    @FunctionalInterface
    private interface Iteration<T> {

        void accept(Consumer<? super T> consumer) throws SQLException;
    }

    static Result walk(final Context context) {
        return walk(context, Sink.noop());
    }

    static Result walk(final Context context, final Sink sink) {
        return new ContextMetadataWalkthrough(context, sink).walk();
    }

    private ContextMetadataWalkthrough(final Context context, final Sink sink) {
        super();
        this.context = Objects.requireNonNull(context, "context is null");
        this.sink = Objects.requireNonNull(sink, "sink is null");
    }

    private Result walk() {
        final var catalogs = pair("catalogs", "catalogs", "catalog", context::getCatalogs,
                                  c -> context.forEachCatalog(c));
        final var schemas = pair("schemas", "schemas", "schema", context::getSchemas,
                                 c -> context.forEachSchema(c));
        pair("schemas(null, %)", () -> context.getSchemas(null, "%"),
             c -> context.forEachSchema(null, "%", c));
        pair("tableTypes", "tableTypes", "tableType", context::getTableTypes, c -> context.forEachTableType(c));
        pair("typeInfo", "typeInfo", "typeInfo", context::getTypeInfo, c -> context.forEachTypeInfo(c));
        pair("clientInfoProperties", "clientInfoProperties", "clientInfoProperty",
             context::getClientInfoProperties, c -> context.forEachClientInfoProperty(c));

        pair("attributes(null, null, %, %)", "attributes", "attribute",
             () -> context.getAttributes(null, null, "%", "%"),
             c -> context.forEachAttribute(null, null, "%", "%", c));
        pair("columns(null, null, %, %)", "columns", "column",
             () -> context.getColumns(null, null, "%", "%"),
             c -> context.forEachColumn(null, null, "%", "%", c));
        final var functions = pair("functions(null, null, %)", "functions", "function",
                                   () -> context.getFunctions(null, null, "%"),
                                   c -> context.forEachFunction(null, null, "%", c));
        pair("functionColumns(null, null, %, %)", "functionColumns", "functionColumn",
             () -> context.getFunctionColumns(null, null, "%", "%"),
             c -> context.forEachFunctionColumn(null, null, "%", "%", c));
        final var procedures = pair("procedures(null, null, %)", "procedures", "procedure",
                                    () -> context.getProcedures(null, null, "%"),
                                    c -> context.forEachProcedure(null, null, "%", c));
        pair("procedureColumns(null, null, %, %)", "procedureColumns", "procedureColumn",
             () -> context.getProcedureColumns(null, null, "%", "%"),
             c -> context.forEachProcedureColumn(null, null, "%", "%", c));
        pair("pseudoColumns(null, null, %, %)", "pseudoColumns", "pseudoColumn",
             () -> context.getPseudoColumns(null, null, "%", "%"),
             c -> context.forEachPseudoColumn(null, null, "%", "%", c));
        pair("superTables(null, %, %)", "superTables", "superTable",
             () -> context.getSuperTables(null, "%", "%"),
             c -> context.forEachSuperTable(null, "%", "%", c));
        pair("superTypes(null, %, %)", "superTypes", "superType",
             () -> context.getSuperTypes(null, "%", "%"),
             c -> context.forEachSuperType(null, "%", "%", c));
        pair("tablePrivileges(null, null, %)", "tablePrivileges", "tablePrivilege",
             () -> context.getTablePrivileges(null, null, "%"),
             c -> context.forEachTablePrivilege(null, null, "%", c));
        final var tables = pair("tables(null, null, %, null)", "tables", "table",
                                () -> context.getTables(null, null, "%", null),
                                c -> context.forEachTable(null, null, "%", null, c));
        final var udts = pair("udts(null, null, %, null)", "udts", "udt",
                              () -> context.getUDTs(null, null, "%", null),
                              c -> context.forEachUDT(null, null, "%", null, c));

        for (final var table : tables) {
            table(table);
        }
        crossReferences(tables);
        for (final var function : functions) {
            function(function);
        }
        for (final var procedure : procedures) {
            procedure(procedure);
        }
        for (final var udt : udts) {
            udt(udt);
        }

        pair("allAttributes", context::getAllAttributes, c -> context.forEachAttribute(c));
        pair("allColumns", context::getAllColumns, c -> context.forEachColumn(c));
        pair("allFunctions", context::getAllFunctions, c -> context.forEachFunction(c));
        pair("allFunctionColumns", context::getAllFunctionColumns, c -> context.forEachFunctionColumn(c));
        pair("allProcedureColumns", context::getAllProcedureColumns, c -> context.forEachProcedureColumn(c));
        pair("allProcedures", context::getAllProcedures, c -> context.forEachProcedure(c));
        pair("allPseudoColumns", context::getAllPseudoColumns, c -> context.forEachPseudoColumn(c));
        pair("allSuperTables", context::getAllSuperTables, c -> context.forEachSuperTable(c));
        pair("allSuperTypes", context::getAllSuperTypes, c -> context.forEachSuperType(c));
        pair("allTables", context::getAllTables, c -> context.forEachTable(c));
        pair("allTablePrivileges", context::getAllTablePrivileges, c -> context.forEachTablePrivilege(c));
        pair("allUDTs", context::getAllUDTs, c -> context.forEachUDT(c));

        list("numericFunctions", context::getNumericFunctions);
        list("sqlKeywords", context::getSQLKeywords);
        list("stringFunctions", context::getStringFunctions);
        list("systemFunctions", context::getSystemFunctions);
        list("timeDateFunctions", context::getTimeDateFunctions);

        final var result = new Result(attempted, succeeded, failed);
        log.info("context metadata walkthrough completed: {}", result);
        assertNoConstraintViolations();
        return result;
    }

    private void table(final Table table) {
        for (final var scope : BestRowIdentifier.COLUMN_VALUES_SCOPE) {
            for (final var nullable : new boolean[] {true, false}) {
                pair("bestRowIdentifier(" + table + ", " + scope + ", " + nullable + ")",
                     () -> context.getBestRowIdentifier(
                             table.getTableCat(), table.getTableSchem(), table.getTableName(), scope, nullable),
                     c -> context.forEachBestRowIdentifier(
                             table.getTableCat(), table.getTableSchem(), table.getTableName(), scope, nullable, c));
            }
        }
        pair("columnPrivileges(" + table + ")", () -> context.getColumnPrivileges(
                     table.getTableCat(), table.getTableSchem(), table.getTableName(), "%"),
             c -> context.forEachColumnPrivilege(
                     table.getTableCat(), table.getTableSchem(), table.getTableName(), "%", c));
        pair("columns(" + table + ")", () -> context.getColumns(
                     table.getTableCat(), table.getTableSchem(), table.getTableName(), "%"),
             c -> context.forEachColumn(table.getTableCat(), table.getTableSchem(), table.getTableName(), "%", c));
        pair("exportedKeys(" + table + ")", () -> context.getExportedKeys(
                     table.getTableCat(), table.getTableSchem(), table.getTableName()),
             c -> context.forEachExportedKey(table.getTableCat(), table.getTableSchem(), table.getTableName(), c));
        final var importedKeys = pair("importedKeys(" + table + ")", () -> context.getImportedKeys(
                                              table.getTableCat(), table.getTableSchem(), table.getTableName()),
                                      c -> context.forEachImportedKey(table.getTableCat(), table.getTableSchem(),
                                                                      table.getTableName(), c));
        if (firstImportedKey == null && !importedKeys.isEmpty()) {
            // Remember a relationship that genuinely exists, so crossReferences(...) can ask about a
            // pair of tables that actually relate. See issue #76.
            firstImportedKey = importedKeys.get(0);
        }
        for (final var unique : new boolean[] {true, false}) {
            for (final var approximate : new boolean[] {true, false}) {
                pair("indexInfo(" + table + ", " + unique + ", " + approximate + ")",
                     () -> context.getIndexInfo(
                             table.getTableCat(), table.getTableSchem(), table.getTableName(), unique, approximate),
                     c -> context.forEachIndexInfo(
                             table.getTableCat(), table.getTableSchem(), table.getTableName(), unique, approximate,
                             c));
            }
        }
        pair("primaryKeys(" + table + ")", () -> context.getPrimaryKeys(
                     table.getTableCat(), table.getTableSchem(), table.getTableName()),
             c -> context.forEachPrimaryKey(table.getTableCat(), table.getTableSchem(), table.getTableName(), c));
        pair("pseudoColumns(" + table + ")", () -> context.getPseudoColumns(
                     table.getTableCat(), table.getTableSchem(), table.getTableName(), "%"),
             c -> context.forEachPseudoColumn(table.getTableCat(), table.getTableSchem(), table.getTableName(), "%",
                                              c));
        pair("tablePrivileges(" + table + ")", () -> context.getTablePrivileges(
                     table.getTableCat(), table.getTableSchem(), table.getTableName()),
             c -> context.forEachTablePrivilege(table.getTableCat(), table.getTableSchem(), table.getTableName(), c));
        pair("versionColumns(" + table + ")", () -> context.getVersionColumns(
                     table.getTableCat(), table.getTableSchem(), table.getTableName()),
             c -> context.forEachVersionColumn(table.getTableCat(), table.getTableSchem(), table.getTableName(), c));
    }

    /**
     * Exercises {@link Context#getCrossReference(String, String, String, String, String, String)}.
     * <p>
     * Prefers a parent/foreign pair taken from a foreign key the driver actually reported, so the call returns rows and
     * the binding, the column mapping, the comparator, and the field constraints are all exercised. Asking about two
     * arbitrary tables - which is what this method used to do - is almost never a real relationship, so it returned
     * nothing and verified only that the call does not throw.
     */
    private void crossReferences(final List<? extends Table> tables) {
        if (firstImportedKey != null) {
            final var key = firstImportedKey;
            final var rows = pair(
                    "crossReference(" + key.getPktableName() + " <- " + key.getFktableName() + ")",
                    () -> context.getCrossReference(
                            key.getPktableCat(), key.getPktableSchem(), key.getPktableName(),
                            key.getFktableCat(), key.getFktableSchem(), key.getFktableName()),
                    c -> context.forEachCrossReference(
                            key.getPktableCat(), key.getPktableSchem(), key.getPktableName(),
                            key.getFktableCat(), key.getFktableSchem(), key.getFktableName(), c));
            if (rows.isEmpty()) {
                // The driver reported an imported key for this pair but no cross-reference for it. Log the exact
                // arguments: the distinction between null and "" for catalog/schema is the usual cause, and without
                // the values the warning is not actionable.
                log.warn("crossReference returned no row, although getImportedKeys reported one;"
                         + " pk=({}, {}, {}) fk=({}, {}, {})",
                         quoted(key.getPktableCat()), quoted(key.getPktableSchem()), quoted(key.getPktableName()),
                         quoted(key.getFktableCat()), quoted(key.getFktableSchem()), quoted(key.getFktableName()));
            }
            return;
        }
        log.warn("no foreign key reported by this database;"
                 + " crossReference is exercised only for the empty case, and its row binding is unverified");
        if (tables.size() < 2) {
            return;
        }
        final var parent = tables.get(0);
        final var foreign = tables.get(1);
        pair("crossReference(" + parent + ", " + foreign + ")",
             () -> context.getCrossReference(
                     parent.getTableCat(), parent.getTableSchem(), parent.getTableName(),
                     foreign.getTableCat(), foreign.getTableSchem(), foreign.getTableName()),
             c -> context.forEachCrossReference(
                     parent.getTableCat(), parent.getTableSchem(), parent.getTableName(),
                     foreign.getTableCat(), foreign.getTableSchem(), foreign.getTableName(), c));
    }

    private void function(final Function function) {
        pair("functionColumns(" + function + ")",
             () -> context.getFunctionColumns(
                     function.getFunctionCat(), function.getFunctionSchem(), function.getFunctionName(), "%"),
             c -> context.forEachFunctionColumn(
                     function.getFunctionCat(), function.getFunctionSchem(), function.getFunctionName(), "%", c));
    }

    private void procedure(final Procedure procedure) {
        pair("procedureColumns(" + procedure + ")",
             () -> context.getProcedureColumns(
                     procedure.getProcedureCat(), procedure.getProcedureSchem(), procedure.getProcedureName(), "%"),
             c -> context.forEachProcedureColumn(
                     procedure.getProcedureCat(), procedure.getProcedureSchem(), procedure.getProcedureName(), "%", c));
    }

    private void udt(final UDT udt) {
        pair("attributes(" + udt + ")",
             () -> context.getAttributes(udt.getTypeCat(), udt.getTypeSchem(), udt.getTypeName(), "%"),
             c -> context.forEachAttribute(udt.getTypeCat(), udt.getTypeSchem(), udt.getTypeName(), "%", c));
        pair("superTypes(" + udt + ")",
             () -> context.getSuperTypes(udt.getTypeCat(), udt.getTypeSchem(), udt.getTypeName()),
             c -> context.forEachSuperType(udt.getTypeCat(), udt.getTypeSchem(), udt.getTypeName(), c));
    }

    private <T> List<T> pair(final String name, final Query<T> query, final Iteration<T> iteration) {
        final var values = list(name, query);
        each(name, iteration);
        return values;
    }

    private <T> List<T> pair(final String name, final String rootElementName, final String itemElementName,
                             final Query<T> query, final Iteration<T> iteration) {
        final var values = list(name, rootElementName, itemElementName, query);
        each(name, iteration);
        return values;
    }

    private <T> List<T> list(final String name, final Query<T> query) {
        attempted++;
        try {
            final var values = new ArrayList<>(Objects.requireNonNull(query.get(), name + " is null"));
            if (values.stream().anyMatch(Objects::isNull)) {
                throw new NullPointerException(name + " contains null");
            }
            verifyBoundInstances(name, values);
            succeeded++;
            verifyComparator(name, values);
            return values;
        } catch (final SQLException sqle) {
            failed++;
            log.error("failed to get {}", name, sqle);
            return List.of();
        } catch (final AssertionError ae) {
            failed++;
            log.error("failed to verify {}", name, ae);
            return List.of();
        } catch (final RuntimeException re) {
            failed++;
            log.error("failed to verify {}", name, re);
            return List.of();
        }
    }

    private <T> List<T> list(final String name, final String rootElementName, final String itemElementName,
                             final Query<T> query) {
        attempted++;
        try {
            final var values = new ArrayList<>(Objects.requireNonNull(query.get(), name + " is null"));
            if (values.stream().anyMatch(Objects::isNull)) {
                throw new NullPointerException(name + " contains null");
            }
            verifyBoundInstances(name, values);
            succeeded++;
            verifyComparator(name, values);
            try {
                sink.accept(rootElementName, itemElementName, values);
            } catch (final Exception e) {
                failed++;
                log.error("failed to sink {}", name, e);
            }
            return values;
        } catch (final SQLException sqle) {
            failed++;
            log.error("failed to get {}", name, sqle);
            return List.of();
        } catch (final AssertionError ae) {
            failed++;
            log.error("failed to verify {}", name, ae);
            return List.of();
        } catch (final RuntimeException re) {
            failed++;
            log.error("failed to verify {}", name, re);
            return List.of();
        }
    }

    private <T> void each(final String name, final Iteration<? extends T> iteration) {
        attempted++;
        try {
            final var values = new ArrayList<T>();
            iteration.accept(values::add);
            if (values.stream().anyMatch(Objects::isNull)) {
                throw new NullPointerException(name + " iteration contains null");
            }
            verifyBoundInstances(name, values);
            succeeded++;
        } catch (final SQLException sqle) {
            failed++;
            log.error("failed to iterate {}", name, sqle);
        } catch (final AssertionError ae) {
            failed++;
            log.error("failed to verify iteration of {}", name, ae);
        } catch (final RuntimeException re) {
            failed++;
            log.error("failed to verify iteration of {}", name, re);
        }
    }

    private void verifyBoundInstances(final String name, final List<?> values) {
        for (final var value : values) {
            if (value instanceof MetadataType metadataType) {
                recordConstraintViolations(name, metadataType);
                MetadataType_Test_Utils.verify(metadataType);
            } else if (!(value instanceof String)) {
                log.debug("skipping non-metadata value from {}: {}", name, value);
            }
        }
    }

    /**
     * Records, rather than throws, Bean Validation constraint violations of the specified bound instance, so the
     * walkthrough keeps visiting the remaining metadata methods and still fails, in {@link #walk()}, when a driver
     * returned a row which the shipped model declares invalid.
     *
     * @param name  the name of the metadata method which bound the specified instance.
     * @param value the bound instance to validate.
     */
    private void recordConstraintViolations(final String name, final MetadataType value) {
        for (final var violation : __JakartaValidation_Test_Utils.validate(value)) {
            constraintViolationCount++;
            if (constraintViolations.size() < VIOLATIONS_TO_RETAIN) {
                constraintViolations.add(
                        name + ": " + value.getClass().getSimpleName() + '.' + violation.getPropertyPath() + ' '
                        + violation.getMessage() + "; value=" + value
                );
            }
        }
    }

    /**
     * Fails when any bound instance violated a constraint. Unlike an unsupported metadata method, which is tolerated
     * as a {@link SQLException} and merely counted as {@code failed}, a constraint violation means the shipped model
     * and the driver's output disagree, and is not fail-safe.
     */
    private void assertNoConstraintViolations() {
        if (constraintViolationCount == 0) {
            return;
        }
        final var distinct = constraintViolations.stream().distinct().toList();
        final var builder = new StringBuilder()
                .append(constraintViolationCount)
                .append(" bean validation constraint violation(s) on bound metadata instances; ")
                .append(distinct.size())
                .append(" distinct among the first ")
                .append(constraintViolations.size())
                .append(" retained");
        distinct.stream().limit(VIOLATIONS_TO_REPORT).forEach(v -> builder.append("\n\t").append(v));
        if (distinct.size() > VIOLATIONS_TO_REPORT) {
            builder.append("\n\t... and ")
                    .append(distinct.size() - VIOLATIONS_TO_REPORT)
                    .append(" more distinct violation(s)");
        }
        throw new AssertionError(builder.toString());
    }

    private <T> void verifyComparator(final String name, final List<T> values) {
        if (values.size() < 2) {
            return;
        }
        // Diagnostic only: JDBC documents metadata ordering keys, not the string collation used by drivers.
        // String.CASE_INSENSITIVE_ORDER is a sample Java-side policy, not a portable verifier of driver order.
        final Comparator<? super T> comparator;
        try {
            comparator = comparatorFor(values.get(0));
        } catch (final SQLException sqle) {
            log.warn("failed to create diagnostic comparator for {}", name, sqle);
            return;
        } catch (final RuntimeException re) {
            log.warn("failed to select diagnostic comparator for {}", name, re);
            return;
        }
        if (comparator == null) {
            return;
        }
        for (int i = 1; i < values.size(); i++) {
            final var previous = values.get(i - 1);
            final var current = values.get(i);
            final var comparison = comparator.compare(previous, current);
            if (comparison > 0) {
                log.warn(
                        "driver metadata order differs from diagnostic comparator for {} at index {}: " +
                        "comparison={}, previous={}, current={}",
                        name, i, comparison, previous, current
                );
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Comparator<? super T> comparatorFor(final T value) throws SQLException {
        if (value instanceof Attribute) {
            return (Comparator<T>) Attribute.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof BestRowIdentifier) {
            return (Comparator<T>) BestRowIdentifier.comparingInJdbcOrder(context);
        }
        if (value instanceof Catalog) {
            return (Comparator<T>) Catalog.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof ClientInfoProperty) {
            return (Comparator<T>) ClientInfoProperty.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof Column) {
            return (Comparator<T>) Column.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof ColumnPrivilege) {
            return (Comparator<T>) ColumnPrivilege.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof CrossReference) {
            return (Comparator<T>) CrossReference.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof ExportedKey) {
            return (Comparator<T>) ExportedKey.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof Function) {
            return (Comparator<T>) Function.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof FunctionColumn) {
            return (Comparator<T>) FunctionColumn.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof ImportedKey) {
            return (Comparator<T>) ImportedKey.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof IndexInfo) {
            return (Comparator<T>) IndexInfo.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof PrimaryKey) {
            return (Comparator<T>) PrimaryKey.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof Procedure) {
            return (Comparator<T>) Procedure.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof ProcedureColumn) {
            return (Comparator<T>) ProcedureColumn.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof PseudoColumn) {
            return (Comparator<T>) PseudoColumn.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof Schema) {
            return (Comparator<T>) Schema.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof Table) {
            return (Comparator<T>) Table.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof TablePrivilege) {
            return (Comparator<T>) TablePrivilege.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof TableType) {
            return (Comparator<T>) TableType.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        if (value instanceof TypeInfo) {
            return (Comparator<T>) TypeInfo.comparingInJdbcOrder(context);
        }
        if (value instanceof UDT) {
            return (Comparator<T>) UDT.comparingInJdbcOrder(context, DIAGNOSTIC_STRING_COMPARATOR);
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Context context;

    private final Sink sink;

    private final List<String> constraintViolations = new ArrayList<>();

    /**
     * The first foreign key the driver reported while walking tables, or {@code null} if this database has none. Used
     * to give {@link #crossReferences(List)} a pair of tables that genuinely relate.
     */
    private ImportedKey firstImportedKey;

    /**
     * Renders a value so that {@code null} and the empty string are distinguishable in a log line.
     */
    private static String quoted(final String value) {
        return value == null ? "null" : '"' + value + '"';
    }

    private int constraintViolationCount;

    private int attempted;

    private int succeeded;

    private int failed;
}
