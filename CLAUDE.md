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

## Class Structure (MANDATORY ORDER)

1. `serialVersionUID`
2. Static comparators
3. Column label constants (`COLUMN_LABEL_XXX`)
4. Column value constants (`COLUMN_VALUE_XXX`)
5. Static factory methods
6. Constructors
7. `java.lang.Object` overrides (`equals`, `hashCode`, `toString`)
8. Bean validation methods
9. Getter/setter pairs (grouped per field)
10. Instance fields

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

### equals/hashCode/toString

- `equals`/`hashCode`: Based on identifying fields (catalog, schema, name)
- `toString`: Include ONLY `@_ColumnLabel` fields, in field definition order

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
8. Override `equals()`, `hashCode()`, `toString()`
9. Add `@_ChildOf`/`@_ParentOf` if parent-child relationship exists
10. Add Context methods (`getXxxAndAcceptEach`, `getXxxAndAddAll`, `getXxx`)
11. Add unit test extending `AbstractMetadataType_Test<T>`

## Anti-Patterns (Don't Do)

- Don't add automatic parent/child navigation methods
- Don't use primitive types for fields
- Don't make setters `public`
- Don't skip `nullSafe` on catalog/schema comparators
- Don't add fields without `@_ColumnLabel`
