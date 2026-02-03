# CLAUDE.md

Instructions for Claude Code when working on this project.

## Project Overview

Type-safe Java bindings for `java.sql.DatabaseMetaData` ResultSet results.

- **Core**: `Context` wraps `DatabaseMetaData`, returns `List<T>` instead of `ResultSet`
- **Build**: Maven, Java 11 (source), Java 25 (tests)
- **Package**: `com.github.jinahya.database.metadata.bind`
- **Coverage**: All 26 JDBC `DatabaseMetaData` methods returning `ResultSet` are bound

## Design Philosophy

**Isolated bindings by design**. After trial-and-error with parent-child trees and relationship navigation, the current approach intentionally keeps binding classes independent:

- No automatic parent/child loading
- No implicit relationship traversal
- User composes queries explicitly via `Context`
- `@_ChildOf`/`@_ParentOf` document relationships without enforcing them

This trades "clever" features for maintainability and predictable behavior.

## Annotations

### Field Annotations

| Annotation | Purpose |
|------------|---------|
| `@_ColumnLabel` | Maps field to ResultSet column (required) |
| `@_NullableBySpecification` | Column may be null per JDBC spec |
| `@_NotUsedBySpecification` | Column reserved/unused in spec |

### Type Annotations

| Annotation | Purpose |
|------------|---------|
| `@_ChildOf` | Documents parent relationship (repeatable) |
| `@_ParentOf` | Documents child relationship (repeatable) |

### Field Annotation Order

```java
@Nullable                                   // jspecify (if applicable)
@_NullableBySpecification                   // project (if nullable by spec)
@_ColumnLabel(COLUMN_LABEL_TABLE_CAT)       // required
private String tableCat;
```

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
6. `// ----- java.lang.Object` — Overrides in order: `toString`, `equals`, `hashCode`
7. `// ----- Jakarta-Validation` — Bean validation methods
8. `// ----- {fieldName}` — Getter/setter pairs, grouped per field (e.g., `// ----- tableCat`)
9. `// -----` — Instance fields (no name, just hyphens)

## Critical Patterns

### Comparators

**Always apply `nullSafe` to catalog/schema fields**. JDBC drivers lie—they return null for spec-defined non-null fields.

```java
static Comparator<Column> comparingInSpecifiedOrder(final Context context,
                                                    final Comparator<? super String> comparator)
        throws SQLException {
    Objects.requireNonNull(context, "context is null");
    Objects.requireNonNull(comparator, "comparator is null");
    final var nullSafe = ContextUtils.nullPrecedence(context, comparator);
    return Comparator
            .comparing(Column::getTableCat, nullSafe)       // nullSafe: always
            .thenComparing(Column::getTableSchem, nullSafe) // nullSafe: always
            .thenComparing(Column::getTableName, nullSafe)
            .thenComparing(Column::getOrdinalPosition, Comparator.naturalOrder());
}

static Comparator<Column> comparingInSpecifiedOrder(final Context context) throws SQLException {
    return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
}
```

Rules:
- Require `Context` parameter (determines null precedence per database)
- `nullSafe` on ALL `*_CAT` and `*_SCHEM` fields
- Default comparator: `String.CASE_INSENSITIVE_ORDER`

### Accessors

- **Getters**: `public`
- **Setters**: `protected` (not `public`)
- **Types**: Wrapper types only (`Integer`, `String`), never primitives

### Context Methods

For each `DatabaseMetaData.getXxx()` returning `ResultSet`:

```java
// Internal helpers
void getXxxAndAcceptEach(..., Consumer<? super Xxx> consumer)
<C extends Collection<? super Xxx>> C getXxxAndAddAll(..., C collection)

// Public API
public List<Xxx> getXxx(...)
```

### toString/equals/hashCode

Methods must appear in this order: `toString`, `equals`, `hashCode`.

- `toString`: Include ONLY `@_ColumnLabel` fields, in field definition order
- `equals`/`hashCode`: Based on identifying fields (catalog, schema, name)

## Testing

| Pattern | Purpose |
|---------|---------|
| `*Test.java` | Unit tests, extend `AbstractMetadataType_Test<T>` |
| `*IT.java` | Integration tests with TestContainers |
| `Memory_*_Test.java` | In-memory databases (Derby, H2, HSQL, SQLite) |

## New Binding Class Checklist

1. Extend `AbstractMetadataType`
2. Add `serialVersionUID`
3. Define `COLUMN_LABEL_XXX` constants
4. Add `@_ColumnLabel` to all fields
5. Add `@_NullableBySpecification` + `@Nullable` where spec allows null
6. Implement `public` getters, `protected` setters
7. Add comparator with `nullSafe` for catalog/schema fields
8. Override `toString()`, `equals()`, `hashCode()` (in this order)
9. Add `@_ChildOf`/`@_ParentOf` if parent-child relationship exists
10. Add Context methods (`getXxxAndAcceptEach`, `getXxxAndAddAll`, `getXxx`)
11. Add unit test extending `AbstractMetadataType_Test<T>`

## Anti-Patterns (Don't Do)

- Don't add automatic parent/child navigation methods
- Don't use primitive types for fields
- Don't make setters `public`
- Don't skip `nullSafe` on catalog/schema comparators
- Don't add fields without `@_ColumnLabel`
