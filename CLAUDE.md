# CLAUDE

## Project Purpose

Type-safe Java bindings for `java.sql.DatabaseMetaData` ResultSet results. Each `DatabaseMetaData` method returning a `ResultSet` has a corresponding binding class extending `AbstractMetadataType`.

**Core**: `Context` wraps `DatabaseMetaData` and returns `List<T extends MetadataType>` instead of raw `ResultSet`.

## Architecture

- `MetadataType`: Interface all binding classes implement (extends `Serializable`)
- `AbstractMetadataType`: Base class with unmapped columns tracking, basic `equals`/`hashCode`
- `Context`: Main entry point wrapping `DatabaseMetaData`
- Binding classes: Map ResultSet columns to typed fields via `@_ColumnLabel` annotations

## Class Structure Order (MANDATORY)

Follow this exact order with grouping comments:

1. Static factory methods (`// ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS`)
2. Static comparators (`// -----------------------------------------------------------------------------------------------------------------`)
3. Column label constants (`public static final String COLUMN_LABEL_XXX = "XXX"`)
4. Constructors (`// ---------------------------------------------------------------------------------------------------- CONSTRUCTORS`)
5. `java.lang.Object` overrides (`// ------------------------------------------------------------------------------------------------ java.lang.Object`)
6. Bean validation methods (`// ------------------------------------------------------------------------------------------------- Bean-Validation`)
7. Getter/setter pairs grouped by field (`// -------------------------------------------------------------------------------------------------------- fieldName`)
8. Instance fields at end (`// -----------------------------------------------------------------------------------------------------------------`)

## Element Ordering Rule

**CRITICAL**: Elements in same group must follow contextual order based on field definition order.

If fields are `a`, `b`, `c`, then:
- Constants: `COLUMN_LABEL_A`, `COLUMN_LABEL_B`, `COLUMN_LABEL_C`
- Getters/setters: `getA()`, `getB()`, `getC()`
- Static fields: ordered `a`, `b`, `c`

## Binding Rules

### Column Labels
- Every `@_ColumnLabel` must have corresponding `public static final String COLUMN_LABEL_XXX` constant
- Exception: Simple literals can use string directly: `@_ColumnLabel("DATA_TYPE")`
- Prefer constants for consistency

### Field Annotations
- `@_ColumnLabel`: Required for all binding fields
- `@_NullableBySpecification`: Column may be null per JDBC spec
- `@_NotUsedBySpecification`: Column reserved but unused
- `@_ReservedBySpecification`: Similar to `@_NotUsedBySpecification`
- Jakarta Validation: `@Pattern`, `@Positive`, `@AssertTrue` for constraints

### Accessors
- **Getters**: `public`, return field type, JavaDoc: `Returns the value of {@value #COLUMN_LABEL_XXX} column.`
- **Setters**: `protected` (NOT `public` or `private`), JavaDoc: `Sets the value of {@value #COLUMN_LABEL_XXX} column.`
- **Chained setters**: Optional builder pattern: `protected Column tableCat(final String tableCat) { setTableCat(tableCat); return this; }`

### Static Factory Methods
```java
static Column of(final String tableCat, final String tableSchem, final String tableName, final String columnName) {
    final var instance = new Column();
    instance.setTableCat(tableCat);
    instance.setTableSchem(tableSchem);
    instance.setTableName(tableName);
    instance.setColumnName(columnName);
    return instance;
}
```

### Comparators
Add static comparators for JDBC-specified ordering:
```java
static Comparator<Column> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
    return Comparator.comparing(Column::getTableCat, comparator)
            .thenComparing(Column::getTableSchem, comparator)
            .thenComparing(Column::getTableName, comparator)
            .thenComparing(Column::getOrdinalPosition, Comparator.naturalOrder());
}

static Comparator<Column> comparingInSpecifiedOrder(final Context context, final Comparator<? super String> comparator)
        throws SQLException {
    return comparingInSpecifiedOrder(ContextUtils.nullPrecedence(context, comparator));
}
```

## Type Relationships

Use `@_ParentOf` and `@_ChildOf` annotations:
```java
@_ChildOf(Table.class)
@_ParentOf(ColumnPrivilege.class)
public class Column extends AbstractMetadataType { ... }
```

## Context Class Methods

For each `DatabaseMetaData` method returning `ResultSet`, add to `Context`:

1. `void getXxxAndAcceptEach(..., Consumer<? super Xxx> consumer)` - streaming
2. `<C extends Collection<? super Xxx>> C getXxxAndAddAll(..., C collection)` - add to collection
3. `public List<Xxx> getXxx(...)` - public API (returns new `ArrayList<>()`)

Add overloads accepting parent binding objects:
```java
List<Column> getColumns(final Table table, final String columnNamePattern)
List<Column> getColumns(final Table table)  // defaults to "%"
```

## Validation

- Use Jakarta Validation annotations: `@Pattern`, `@Positive`, `@AssertTrue`
- Custom validation: `@AssertTrue(message = "...") private boolean isValid() { ... }`

## equals() and hashCode()

Override based on key identifying fields:
```java
@Override
public boolean equals(final Object obj) {
    if (obj == null || getClass() != obj.getClass()) return false;
    if (!super.equals(obj)) return false;
    final var that = (Column) obj;
    return Objects.equals(getTableCatEffective(), that.getTableCatEffective()) &&
           Objects.equals(getTableSchemEffective(), that.getTableSchemEffective()) &&
           Objects.equals(tableName, that.tableName) &&
           Objects.equals(columnName, that.columnName);
}
```

Note: Some classes use "effective" methods (normalize: uppercase, strip whitespace).

## toString()

Include all significant fields:
```java
@Override
public String toString() {
    return super.toString() + '{' +
           "tableCat=" + tableCat +
           ",tableSchem=" + tableSchem +
           ",tableName=" + tableName +
           ",columnName=" + columnName +
           '}';
}
```

## Comment Style

- Grouping comments: `// -------------------------------------------------------------------------------------------------------- fieldName`
- JavaDoc for public methods
- License header: `/*- ... */` style

## Testing

- Test classes: `*Test.java` (unit), `*IT.java` (integration)
- Test utilities: `MetadataType_Test_Utils`, `__Validation_Test_Utils`
- Use AssertJ and JUnit 5

## Adding New Binding Class Checklist

1. Extend `AbstractMetadataType`
2. Add `@_ColumnLabel` to all ResultSet-mapped fields
3. Define `public static final String COLUMN_LABEL_XXX` constants
4. Add `public` getters and `protected` setters
5. Follow exact class structure order
6. Add `@_ParentOf`/`@_ChildOf` if applicable
7. Override `equals()`, `hashCode()`, `toString()`
8. Add static `of()` factory method if useful
9. Add comparators if JDBC spec defines ordering
10. Add Jakarta Validation annotations
11. Add `Context` methods: `getXxxAndAcceptEach`, `getXxxAndAddAll`, `getXxx`, plus overloads

## Field Visibility Rules

- Binding fields: `private`
- Setters: `protected` (NOT `public`)
- Getters: `public`
- Use wrapper types (`Integer`, `String`) not primitives for null handling

## Unmapped Columns

`AbstractMetadataType` tracks ResultSet columns without corresponding fields in `unmappedColumns` map. Logged as warnings during binding.
