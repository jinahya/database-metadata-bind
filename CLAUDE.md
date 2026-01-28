# CLAUDE

## Project Purpose

Type-safe Java bindings for `java.sql.DatabaseMetaData` ResultSet results. Each `DatabaseMetaData` method returning a `ResultSet` has a corresponding binding class extending `AbstractMetadataType`.

**Core**: `Context` wraps `DatabaseMetaData` and returns `List<T extends MetadataType>` instead of raw `ResultSet`.

## Project Metadata

- **Build System**: Maven (parent: jinahya-parent v0.9.9)
- **Java Version**: JDK 11 (source), JDK 25 (tests)
- **License**: Apache License 2.0
- **Module Name**: `com.github.jinahya.database.metadata.bind`
- **Package**: `com.github.jinahya.database.metadata.bind`

## Architecture

- `MetadataType`: Interface all binding classes implement (extends `Serializable`)
- `AbstractMetadataType`: Base class with unmapped columns tracking, basic `equals`/`hashCode`
- `Context`: Main entry point wrapping `DatabaseMetaData` (~2,257 lines)
- `ContextUtils`: Utility methods for binding (reflection, type conversion)
- `Metadata`: Aggregator class for complete schema metadata
- Binding classes: Map ResultSet columns to typed fields via `@_ColumnLabel` annotations

## Binding Classes (33 Total)

### Hierarchy Level 1 (No Parents)
- `Catalog`, `ClientInfoProperty`, `Schema`, `Table`, `TableType`, `TypeInfo`, `UDT`

### Hierarchy Level 2 (Single Parent)
- `Attribute` (@_ChildOf UDT)
- `Column` (@_ChildOf Table, @_ParentOf ColumnPrivilege)
- `PrimaryKey`, `SuperTable`, `VersionColumn`

### Hierarchy Level 3 (Inheritance)
- `PortedKey` (abstract base class)
  - `ImportedKey` (extends PortedKey)
  - `ExportedKey` (extends PortedKey)

### Other Binding Classes
- `BestRowIdentifier`, `ColumnPrivilege`, `CrossReference`, `Function`, `FunctionColumn`
- `IndexInfo`, `Procedure`, `ProcedureColumn`, `PseudoColumn`, `SuperType`, `TablePrivilege`

### Helper Classes
- `SchemaId` (simple POJO for schema identification)
- `MetadataTypeConstants` (shared constants)

## Annotation System

### Field-Level Annotations

| Annotation | Purpose | Meta-Annotation |
|-----------|---------|-----------------|
| `@_ColumnLabel` | Specifies ResultSet column name | None |
| `@_NullableBySpecification` | Column may be null per JDBC spec | `@_Nullable` |
| `@_NotUsedBySpecification` | Column reserved but unused | `@_NotUsed` |
| `@_ReservedBySpecification` | Similar to @_NotUsedBySpecification | `@_NotUsed` |

### Type-Level Annotations

| Annotation | Repeatable | Purpose |
|-----------|-----------|---------|
| `@_ChildOf` | Yes | Declares parent class relationship |
| `@_ParentOf` | Yes | Declares child class relationship |

### Meta-Annotations
- `@_Nullable`: Marks annotation types indicating nullable values
- `@_NotUsed`: Marks annotation types indicating unused values

## Class Structure Order (MANDATORY)

Follow this exact order with grouping comments:

1. License header (`/*- ... */`)
2. Package declaration
3. Imports
4. Class JavaDoc
5. Class declaration with `@_ChildOf`/`@_ParentOf` annotations
6. `serialVersionUID` field
7. Static comparators (`// --------------------------------------------------`)
8. Column label constants (`public static final String COLUMN_LABEL_XXX = "XXX"`)
9. Column value constants (`public static final int COLUMN_VALUE_XXX`)
10. Static factory methods (`// ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS`)
11. Constructors (`// ---------------------------------------------------------------------------------------------------- CONSTRUCTORS`)
12. `java.lang.Object` overrides (`// ------------------------------------------------------------------------------------------------ java.lang.Object`)
13. Bean validation methods (`// ------------------------------------------------------------------------------------------------- Bean-Validation`)
14. Getter/setter pairs grouped by field (`// -------------------------------------------------------------------------------------------------------- fieldName`)
15. Instance fields at end (`// -----------------------------------------------------------------------------------------------------------------`)

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

### Column Value Constants
For enumerated types from JDBC spec, define value constants:
```java
public static final int COLUMN_VALUE_NULLABLE_COLUMN_NO_NULLS = DatabaseMetaData.columnNoNulls;
public static final int COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE = DatabaseMetaData.columnNullable;
public static final int COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE_UNKNOWN = DatabaseMetaData.columnNullableUnknown;

static final List<Integer> COLUMN_VALUES_NULLABLE = List.of(
    COLUMN_VALUE_NULLABLE_COLUMN_NO_NULLS,
    COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE,
    COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE_UNKNOWN
);
```

### Field Annotations
- `@_ColumnLabel`: Required for all binding fields
- `@_NullableBySpecification`: Column may be null per JDBC spec
- `@_NotUsedBySpecification`: Column reserved but unused
- `@_ReservedBySpecification`: Similar to `@_NotUsedBySpecification`
- `@org.jspecify.annotations.Nullable`: Pair with above three annotations for null-safety tooling
- Jakarta Validation: `@Pattern`, `@Positive`, `@AssertTrue` for constraints

**Important**: Fields annotated with `@_NullableBySpecification`, `@_NotUsedBySpecification`, or `@_ReservedBySpecification` must also have `@org.jspecify.annotations.Nullable` for proper null-safety support:
```java
@Nullable
@_NullableBySpecification
@_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
private String tableCat;
```

### Verifying @_NullableBySpecification Annotations

To verify nullable annotations are correct, check the official JDBC specification:
- **Reference**: [DatabaseMetaData (Java SE 8+)](https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html)

For each `DatabaseMetaData` method returning `ResultSet`, the JavaDoc lists columns with descriptions. Look for:
- Explicit: `"(may be null)"` or `"may be null"`
- Conditional: `"null when TYPE is tableIndexStatistic"` or `"null if DATA_TYPE isn't REF"`
- Implicit: `"Null is returned for data types where X is not applicable"`

**Examples from spec**:
| Method | Column | Spec Language | Nullable? |
|--------|--------|---------------|-----------|
| `getColumns` | TABLE_CAT | "table catalog (may be null)" | Yes |
| `getColumns` | CHAR_OCTET_LENGTH | "For any other datatype the returned value is a NULL" | Yes |
| `getColumns` | DECIMAL_DIGITS | "Null is returned for data types where DECIMAL_DIGITS is not applicable" | Yes |
| `getBestRowIdentifier` | COLUMN_SIZE | "Null is returned for data types where the column size is not applicable" | Yes |
| `getIndexInfo` | INDEX_NAME | "null when TYPE is tableIndexStatistic" | Yes |
| `getIndexInfo` | ASC_OR_DESC | "may be null if sort sequence is not supported" | Yes |

**Common nullable patterns**:
- `TABLE_CAT`, `TABLE_SCHEM` - almost always nullable (catalog/schema may be null)
- `*_CAT`, `*_SCHEM` variants - typically nullable
- `REMARKS` - usually nullable
- `DECIMAL_DIGITS` - nullable when not applicable to data type
- `SCOPE_*` fields - nullable when DATA_TYPE isn't REF
- `SOURCE_DATA_TYPE` - nullable when not DISTINCT type
- `*_NAME` for keys (FK_NAME, PK_NAME) - nullable

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
Add static comparators for JDBC-specified ordering. Apply null-safe wrapping only to nullable fields:
```java
static Comparator<Column> comparingInSpecifiedOrder(final Context context,
                                                    final Comparator<? super String> comparator)
        throws SQLException {
    Objects.requireNonNull(context, "context is null");
    Objects.requireNonNull(comparator, "comparator is null");
    final var nullSafe = ContextUtils.nullPrecedence(context, comparator);
    return Comparator
            .comparing(Column::getTableCat, nullSafe)      // nullable - use nullSafe
            .thenComparing(Column::getTableSchem, nullSafe) // nullable - use nullSafe
            .thenComparing(Column::getTableName, comparator) // NOT nullable - use raw comparator
            .thenComparing(Column::getOrdinalPosition, Comparator.naturalOrder());
}

static Comparator<Column> comparingInSpecifiedOrder(final Context context) throws SQLException {
    Objects.requireNonNull(context, "context is null");
    return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
}
```

**Important**:
- Always require `Context` to determine null precedence from database metadata
- Apply `ContextUtils.nullPrecedence()` **ONLY** to fields marked `@_NullableBySpecification`
- Use `String.CASE_INSENSITIVE_ORDER` as default for case-insensitive comparison
- Do NOT create a basic comparator without Context (footgun - won't handle nulls correctly)

**Quick Reference - Comparator Selection**:
| Field Annotation | String Comparator | Integer Comparator |
|------------------|-------------------|---------------------|
| `@_NullableBySpecification` | `nullSafe` | `ContextUtils.nullPrecedence(context, Comparator.<Integer>naturalOrder())` |
| NOT nullable | `comparator` (raw) | `Comparator.naturalOrder()` (raw) |

**Common Nullable Fields** (always check `@_NullableBySpecification` in source):
- `TABLE_CAT`, `TYPE_CAT`, `*_CAT` → typically nullable
- `TABLE_SCHEM`, `TYPE_SCHEM`, `*_SCHEM` → typically nullable
- `TABLE_NAME`, `TYPE_NAME`, `COLUMN_NAME` → usually NOT nullable
- `ORDINAL_POSITION`, `KEY_SEQ` → usually NOT nullable

## Type Relationships

Use `@_ParentOf` and `@_ChildOf` annotations (both are repeatable):
```java
@_ChildOf(Catalog.class)
@_ChildOf(Schema.class)
@_ParentOf(Column.class)
@_ParentOf(BestRowIdentifier.class)
@_ParentOf(ColumnPrivilege.class)
@_ParentOf(IndexInfo.class)
public class Table extends AbstractMetadataType { ... }
```

## Context Class Methods

For each `DatabaseMetaData` method returning `ResultSet`, add to `Context`:

1. `void getXxxAndAcceptEach(..., Consumer<? super Xxx> consumer)` - streaming
2. `<C extends Collection<? super Xxx>> C getXxxAndAddAll(..., C collection)` - add to collection
3. `public List<Xxx> getXxx(...)` - public API (returns new `ArrayList<>()`)

Add overloads accepting parent binding objects:
```java
// Full parameters
List<Column> getColumns(String catalog, String schema, String table, String columnNamePattern)

// Parent object (convenience)
List<Column> getColumns(final Table table, final String columnNamePattern)

// Parent object only (defaults to "%")
List<Column> getColumns(final Table table)
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

### "Effective" Methods Pattern
Some binding classes define `getXxxEffective()` methods for null-safe comparisons:
- Normalize values: uppercase, strip whitespace
- Used in `equals()` and comparators
- Pattern: `return xxx == null ? null : xxx.strip().toUpperCase(Locale.ROOT);`

## toString()

### For those inherits `MetadataType`

**MANDATORY**: Include ONLY fields annotated with `@_ColumnLabel`. Exclude:
- Transient fields (e.g., `unmappedColumns` in `AbstractMetadataType`)
- Helper fields without `@_ColumnLabel`
- Fields from parent classes that are not `@_ColumnLabel` annotated

Include ALL `@_ColumnLabel` fields in the same order as field definitions:
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

For classes extending other binding classes (e.g., `ExportedKey extends PortedKey`), include all `@_ColumnLabel` fields from both the class and its parent(s).

## Serialization

- All binding classes implement `Serializable` (via `MetadataType` interface)
- Each class must define `private static final long serialVersionUID`
- Transient fields (like `unmappedColumns`) are excluded from serialization

## Inheritance: PortedKey Pattern

For shared functionality between similar binding classes:
```java
// Abstract base class
public abstract class PortedKey extends AbstractMetadataType {
    // Shared fields and comparators
}

// Concrete subclasses
public class ImportedKey extends PortedKey { ... }
public class ExportedKey extends PortedKey { ... }
```

## Comment Style

- Grouping comments: `// -------------------------------------------------------------------------------------------------------- fieldName`
- JavaDoc for public methods
- License header: `/*- ... */` style
- Separator lines: 100+ dashes

## Testing

### Test Categories
- **Unit tests**: `*Test.java` - extend `AbstractMetadataType_Test<T>`
- **Integration tests**: `*IT.java` - use TestContainers
- **In-memory tests**: `Memory_*_Test.java` (Derby, H2, HSQL, SQLite)

### Test Utilities
- `MetadataType_Test_Utils`: Verifies accessors and validation
- `Context_Test_Utils`: Comprehensive test utilities (~55k lines)
- `__Validation_Test_Utils`: Jakarta Validation testing
- `AbstractMetadataTypeAssert`, `MetadataTypeAssert`: Custom AssertJ assertions

### Test Framework
- JUnit 5 (`junit-jupiter`)
- AssertJ (`assertj-core`)
- Mockito (`mockito-junit-jupiter`)
- EqualsVerifier (`equalsverifier-nodep`)

### Unit Test Pattern
```java
class ColumnTest extends AbstractMetadataType_Test<Column> {
    ColumnTest() {
        super(Column.class);
    }

    @Override
    SingleTypeEqualsVerifierApi<Column> equalsVerifier() {
        return super.equalsVerifier()
                .withOnlyTheseFields("tableCat", "tableSchem", "tableName", "columnName");
    }

    @Override
    Column newTypeInstance() {
        final var instance = super.newTypeInstance();
        instance.setTableName("");
        instance.setColumnName("");
        instance.setOrdinalPosition(1);
        return instance;
    }
}
```

## Build Configuration

### Maven Profiles
- `failsafe`: Enables integration tests with maven-failsafe-plugin
- `testcontainers`: Adds database container dependencies

### Key Plugins
- `maven-compiler-plugin`: Java 11 source, Java 25 test
- `maven-surefire-plugin`: Unit tests
- `maven-failsafe-plugin`: Integration tests (conditional)
- `jacoco-maven-plugin`: Code coverage
- `lombok-maven-plugin`: Delombok for source generation
- `license-maven-plugin`: Apache v2 headers

### Test Databases
- **In-memory**: H2, Derby, HSQL, SQLite
- **Containers** (via TestContainers): MySQL, PostgreSQL, Oracle, MariaDB, SQL Server, DB2

## Dependencies

### Core (Provided)
- `jakarta.validation-api`: Validation annotations

### Test
- `junit-jupiter`: JUnit 5
- `assertj-core`: Fluent assertions
- `mockito-junit-jupiter`: Mocking
- `equalsverifier-nodep`: equals/hashCode validation
- `hibernate-validator`: Jakarta Validation implementation
- `jackson-databind`: JSON serialization
- `testcontainers`: Docker container databases

## Adding New Binding Class Checklist

1. Extend `AbstractMetadataType`
2. Define `private static final long serialVersionUID`
3. Add `@_ColumnLabel` to all ResultSet-mapped fields
4. Define `public static final String COLUMN_LABEL_XXX` constants
5. Define `public static final int COLUMN_VALUE_XXX` constants if applicable
6. Add `public` getters and `protected` setters
7. Follow exact class structure order
8. Add `@_ParentOf`/`@_ChildOf` if applicable
9. Override `equals()`, `hashCode()`, `toString()`
10. Add static `of()` factory method if useful
11. Add comparators if JDBC spec defines ordering
12. Add Jakarta Validation annotations
13. Add `Context` methods: `getXxxAndAcceptEach`, `getXxxAndAddAll`, `getXxx`, plus overloads
14. Add unit test extending `AbstractMetadataType_Test<T>`

## Field Visibility Rules

- Binding fields: `private`
- Setters: `protected` (NOT `public`)
- Getters: `public`
- Use wrapper types (`Integer`, `String`, `Boolean`) not primitives for null handling

## Unmapped Columns

`AbstractMetadataType` tracks ResultSet columns without corresponding fields in `unmappedColumns` map:
- Stored as `transient Map<String, Object>` (excluded from serialization)
- Logged as warnings during binding
- Useful for discovering vendor-specific columns

## Type Conversion (ContextUtils)

The binding mechanism handles robust type conversion:
- **Boolean**: From direct boolean, String ("Y"/"N", "true"/"false"), Number (0/1)
- **Integer/Long**: From numeric types with range checking
- **String**: Direct from ResultSet

## Quick Reference

### Annotation Prefixes
All project-specific annotations are prefixed with underscore: `@_ColumnLabel`, `@_NullableBySpecification`, etc.

### Constant Naming
- Column labels: `COLUMN_LABEL_XXX`
- Column values: `COLUMN_VALUE_XXX`
- Value lists: `COLUMN_VALUES_XXX`

### File Locations
- Main source: `src/main/java/com/github/jinahya/database/metadata/bind/`
- Tests: `src/test/java/com/github/jinahya/database/metadata/bind/`
- Integration tests: `src/test-testcontainers/` (conditional)
