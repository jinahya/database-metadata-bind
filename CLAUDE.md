# CLAUDE

## Project Overview

Type-safe Java bindings for `java.sql.DatabaseMetaData` ResultSet results.

- **Core**: `Context` wraps `DatabaseMetaData`, returns `List<T extends MetadataType>` instead of `ResultSet`
- **Build**: Maven, Java 11 (source), Java 25 (tests)
- **Package**: `com.github.jinahya.database.metadata.bind`

## Annotations

### Field Annotations

| Annotation | Purpose |
|------------|---------|
| `@_ColumnLabel` | Specifies ResultSet column name (required for all binding fields) |
| `@_NullableBySpecification` | Column may be null per JDBC spec |
| `@_NotUsedBySpecification` | Column reserved but unused |

### Type Annotations

| Annotation | Purpose |
|------------|---------|
| `@_ChildOf` | Declares parent class relationship (repeatable) |
| `@_ParentOf` | Declares child class relationship (repeatable) |

### Field Annotation Order

```java
@org.jspecify.annotations.Nullable          // jspecify (if nullable)
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
7. `java.lang.Object` overrides
8. Bean validation methods
9. Getter/setter pairs (grouped by field)
10. Instance fields

## Comparators

**CRITICAL**: Always apply `nullSafe` to ALL catalog/schema fields. Some JDBC drivers return null for spec-defined non-null fields.

```java
static Comparator<Column> comparingInSpecifiedOrder(final Context context,
                                                    final Comparator<? super String> comparator)
        throws SQLException {
    Objects.requireNonNull(context, "context is null");
    Objects.requireNonNull(comparator, "comparator is null");
    final var nullSafe = ContextUtils.nullPrecedence(context, comparator);
    return Comparator
            .comparing(Column::getTableCat, nullSafe)       // always nullSafe
            .thenComparing(Column::getTableSchem, nullSafe) // always nullSafe
            .thenComparing(Column::getTableName, nullSafe)
            .thenComparing(Column::getOrdinalPosition, Comparator.naturalOrder());
}

static Comparator<Column> comparingInSpecifiedOrder(final Context context) throws SQLException {
    return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
}
```

**Rules**:
- Require `Context` parameter (for null precedence)
- Apply `nullSafe` to ALL catalog/schema fields (`*_CAT`, `*_SCHEM`)
- Use `String.CASE_INSENSITIVE_ORDER` as default

## Accessors

- **Getters**: `public`
- **Setters**: `protected` (NOT `public`)
- Use wrapper types (`Integer`, `String`) not primitives

## Context Methods

For each `DatabaseMetaData` method returning `ResultSet`:

```java
void getXxxAndAcceptEach(..., Consumer<? super Xxx> consumer)
<C extends Collection<? super Xxx>> C getXxxAndAddAll(..., C collection)
public List<Xxx> getXxx(...)  // public API
```

## equals/hashCode/toString

- `equals`/`hashCode`: Based on identifying fields (typically catalog, schema, name)
- `toString`: Include ONLY `@_ColumnLabel` fields in field definition order

## Testing

- Unit tests: `*Test.java` extending `AbstractMetadataType_Test<T>`
- Integration tests: `*IT.java` with TestContainers
- In-memory: `Memory_*_Test.java` (Derby, H2, HSQL, SQLite)

## New Binding Class Checklist

1. Extend `AbstractMetadataType`
2. Add `serialVersionUID`
3. Add `@_ColumnLabel` to all fields
4. Add `@_NullableBySpecification` + `@Nullable` where applicable
5. Define `COLUMN_LABEL_XXX` constants
6. Add `public` getters, `protected` setters
7. Add comparator with nullSafe for catalog/schema fields
8. Override `equals()`, `hashCode()`, `toString()`
9. Add `@_ParentOf`/`@_ChildOf` if applicable
10. Add Context methods
11. Add unit test
