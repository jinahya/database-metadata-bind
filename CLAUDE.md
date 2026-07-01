# CLAUDE.md

Instructions for Claude Code when working on this project.

## Project Overview

Type-safe Java bindings for `java.sql.DatabaseMetaData` ResultSet results.

- **Core**: `Context` wraps `DatabaseMetaData`, returns `List<T>` instead of `ResultSet`
- **Build**: Maven, Java 17 (source), Java 25 (tests)
- **Package**: `com.github.jinahya.database.metadata.bind`
- **Coverage**: All 26 JDBC `DatabaseMetaData` methods returning `ResultSet` are bound

## Design Philosophy

**Isolated bindings by design**. After trial-and-error with parent-child trees and relationship navigation, the current approach intentionally keeps binding classes independent:

- No automatic parent/child loading
- No implicit relationship traversal
- User composes queries explicitly via `Context`
This trades "clever" features for maintainability and predictable behavior.

## Annotations

### Field Annotations

| Annotation | Purpose |
|------------|---------|
| `@_ColumnLabel` | Maps field to ResultSet column (required) |
| `@_NullableBySpecification` | Column may be null per JDBC spec |
| `@_ReservedBySpecification` | Column reserved for future use per JDBC spec |
| `@_NotUsedBySpecification` | Column unused in spec |

### Field Annotation Order

```java
@Nullable                                   // jspecify (if applicable)
@_NullableBySpecification                   // OR @_ReservedBySpecification (mutually exclusive)
@_ColumnLabel(COLUMN_LABEL_TABLE_CAT)       // required (always last)
private String tableCat;
```

- `@_NullableBySpecification`: Column is nullable per JDBC spec (has meaning, but optional)
- `@_ReservedBySpecification`: Column is reserved for future use (not populated)

## Class Structure (Preferred Order)

Each section is preceded by a single-line hyphen comment ending with the section name, exactly 120 characters total (including 4-space indent). The format is:

```java
    // ---- SECTION_NAME
```

Where `----` is filled with hyphens so the total line length equals 120 characters.

**Examples:**
```java
    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    // ------------------------------------------------------------------------------------------------------- TABLE_CAT
    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS
    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS
    // -----------------------------------------------------------------------------------------------------------------
```

The last example (all hyphens, no name) is used only for the instance fields section at the end of the class.

1. `serialVersionUID`
2. `// ----- COMPARATORS` — Static comparator methods
3. `// ----- {COLUMN_NAME}` — Column label constants (`COLUMN_LABEL_XXX`) and column value constants (`COLUMN_VALUE_XXX`), grouped by column (e.g., `// ----- TABLE_CAT`)
4. `// ----- STATIC_FACTORY_METHODS`
5. `// ----- CONSTRUCTORS`
6. `// ----- java.lang.Object` — `toString` override only
7. `// ----- Jakarta-Validation` — Bean validation methods
8. `// ----- {fieldName}` — Getter/setter pairs, grouped per field (e.g., `// ----- tableCat`)
9. `// -----` — Instance fields (no name, just hyphens)

## Critical Patterns

### Comparators

Sorting is provided by **package-private** static factories `comparingInSpecifiedOrder(...)`
(`PortedKey` uses `comparingPk`/`comparingFk`). They reproduce the ordering documented by the
corresponding `DatabaseMetaData.getXxx()` javadoc. **Return `Comparator<T>`** (never a wildcard —
*Effective Java* Item 31); **accept `Comparator<? super String>`** (consumer position; also lets a
caller pass `java.text.Collator`, which is `Comparator<Object>`).

**No `operator` / `UnaryOperator` parameter, no internal `op` guard** — removed. Collation and
string-null policy belong entirely to the caller's one `Comparator<? super String>`.

#### Overload shape depends on key types

**Types with string keys** (Catalog, Table, Column, UDT, …) — two overloads:

```java
// pure: caller owns collation + string-null placement; non-string keys use fixed nullsFirst
static Comparator<Column> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
    Objects.requireNonNull(comparator, "comparator is null");
    return Comparator
            .<Column, String>comparing(Column::getTableCat, comparator)   // method ref; raw nullable value
            .thenComparing(Column::getTableSchem, comparator)
            .thenComparing(Column::getTableName, comparator)
            .thenComparing(Column::getOrdinalPosition, Comparator.nullsFirst(Comparator.naturalOrder()));
}

// DB-faithful: withDatabaseNullOrdering applied to ALL keys (string AND numeric)
static Comparator<Column> comparingInSpecifiedOrder(final Context context,
                                                    final Comparator<? super String> comparator)
        throws SQLException {
    Objects.requireNonNull(context, "context is null");
    Objects.requireNonNull(comparator, "comparator is null");
    final var s = ContextUtils.withDatabaseNullOrdering(context, comparator, ContextUtils.SortDirection.ASCENDING);
    final var i = ContextUtils.withDatabaseNullOrdering(
            context, Comparator.<Integer>naturalOrder(), ContextUtils.SortDirection.ASCENDING);
    return Comparator
            .<Column, String>comparing(Column::getTableCat, s)
            .thenComparing(Column::getTableSchem, s)
            .thenComparing(Column::getTableName, s)
            .thenComparing(Column::getOrdinalPosition, i);
}
```

**Types whose only sort key is numeric/ordinal** (`BestRowIdentifier` → SCOPE, `TypeInfo` → DATA_TYPE)
— `()` + `(Context)`:

```java
static Comparator<TypeInfo> comparingInSpecifiedOrder() {
    return Comparator.comparing(TypeInfo::getDataType, Comparator.nullsFirst(Comparator.naturalOrder()));
}
static Comparator<TypeInfo> comparingInSpecifiedOrder(final Context context) throws SQLException {
    Objects.requireNonNull(context, "context is null");
    return Comparator.comparing(TypeInfo::getDataType, ContextUtils.withDatabaseNullOrdering(
            context, Comparator.<Integer>naturalOrder(), ContextUtils.SortDirection.ASCENDING));
}
```

#### Rules

- **Non-string keys** (`Integer`/`Boolean`: ORDINAL_POSITION, KEY_SEQ, SCOPE, DATA_TYPE, NON_UNIQUE):
  always wrap with `Comparator.nullsFirst(Comparator.naturalOrder())` (fields are nullable wrappers;
  drivers may return null even where the spec says non-null).
- **String keys**: pass the raw value (`Xxx::getYyy`) to the caller's `comparator`; it must be
  null-safe (`Comparator.nullsFirst(...)`, or `ContextUtils.withDatabaseNullOrdering(...)`).
- **No no-arg `()` for string-keyed types.** `naturalOrder()` on strings is binary and matches no DB
  collation — meaningless in the JDBC domain. A no-arg exists **only** for numeric-only types, where
  the key is a genuine ordinal and `naturalOrder()` reproduces the documented order.
- **The library never emulates DB collation** (JDBC exposes none) — the caller injects it via the
  `comparator` (e.g. `Collator`). Only **null placement** can follow the DB, via the `Context`
  overload's `ContextUtils.withDatabaseNullOrdering(...)` (prefer over the deprecated `nullOrdered`).
- **No comparator for order-unspecified types** (`SuperTable`, `SuperType`, `VersionColumn` — their
  `getXxx` documents no order).
- All factories package-private; return `Comparator<T>`; parameter `Comparator<? super String>`.

### Accessors

- **Getters**: `public`
- **Setters**: package-private (no modifier, not `public` or `protected`)
- **Types**: Wrapper types only (`Integer`, `String`), never primitives

### Context Methods

For each `DatabaseMetaData.getXxx()` returning `ResultSet`:

```java
// Internal helpers
void getXxxAndAcceptEach(..., Consumer<? super Xxx> consumer)
<C extends Collection<? super Xxx>> C getXxxAndAddAll(..., C collection)

// Public API
public List<Xxx> getXxx(...)

// Convenience methods (package-private)
List<Xxx> getXxxOf(ParentType parent, ...)
```

#### Convenience Method Nullability Rule

**The question:** "Can this query be made meaningfully WITHOUT this parent object?"

- **YES** → Parent is `@Nullable`, use `Optional.ofNullable(obj).map(...).orElse(null)`
- **NO** → Parent is required, use `Objects.requireNonNull(obj, "obj is null")` + direct field access

**Parent types and their behavior:**

| Parent Type | Typical Usage | Nullability |
|-------------|---------------|-------------|
| `Table`, `UDT`, `Function`, `Procedure` | Concrete parent for child data | Always required (`requireNonNull`) |
| `Catalog` | Optional filter (provides `catalog`) | Usually `@Nullable` (catalog is typically optional) |
| `Schema` | Depends on base method | `@Nullable` if both catalog AND schemaPattern are optional |

**Example - Schema nullability depends on base method:**

| Base Method | Schema Provides | Schema Nullable? |
|-------------|-----------------|------------------|
| `getProcedures(@Nullable catalog, @Nullable schemaPattern, ...)` | both nullable | ✓ `@Nullable` |
| `getSuperTables(@Nullable catalog, schemaPattern, ...)` | catalog + **schemaPattern** | ✗ `requireNonNull` |

**Semantic meaning:**
- `getProceduresOf(@Nullable Schema)` → "Get procedures, optionally scoped to this schema"
- `getSuperTablesOf(Schema)` → "Get super tables FOR this schema" (schema is the required scope)

### toString

Binding classes override `toString()` only. They intentionally do **not** override `equals()`/`hashCode()` — do not add identity semantics without an explicit design decision.

- `toString`: Include ONLY `@_ColumnLabel` fields, in field definition order

## Testing

| Pattern | Purpose |
|---------|---------|
| `*Test.java` | Unit tests, extend `AbstractMetadataType_Test<T>` |
| `*IT.java` | Integration tests with TestContainers |
| `Memory_*_Test.java` | In-memory databases (H2, HSQL, SQLite) |

## New Binding Class Checklist

1. Extend `AbstractMetadataType`
2. Add `serialVersionUID`
3. Define `COLUMN_LABEL_XXX` constants
4. Add `@_ColumnLabel` to all fields
5. Add `@_NullableBySpecification` + `@Nullable` where spec allows null
6. Implement `public` getters, package-private setters
7. Add comparator per **Comparators** conventions above — `comparingInSpecifiedOrder(Comparator<? super String>)` + `(Context, Comparator<? super String>)` for string-keyed types (or `()` + `(Context)` for numeric-only types). Skip entirely if the `getXxx` documents no order.
8. Override `toString()` only
9. Add Context methods (`getXxxAndAcceptEach`, `getXxxAndAddAll`, `getXxx`)
10. Add unit test extending `AbstractMetadataType_Test<T>`

## Anti-Patterns (Don't Do)

- Don't add automatic parent/child navigation methods
- Don't use primitive types for fields
- Don't make setters `public` or `protected`
- Don't add fields without `@_ColumnLabel`

## Catalog/Schema Null Handling

When querying child objects, pass raw values directly (`null` → `null`):

```java
context.getColumns(table.getTableCat(),    // null → null
                   table.getTableSchem(),  // null → null
                   table.getTableName(),
                   "%");
```

- `null` in result = "not applicable / DB doesn't use catalogs"
- `null` in parameter = "don't filter by catalog"

These semantics align well — a table from a catalog-less database returns `null`, which when passed to a child query means "don't filter by catalog."
