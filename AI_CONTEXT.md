# AI_CONTEXT

## Tech Stack

- **Language**: Java 11 (source), Java 25 (tests)
- **Build**: Maven (parent: jinahya-parent v0.9.9)
- **Module**: `com.github.jinahya.database.metadata.bind`
- **License**: Apache License 2.0

## Core Architecture

- `MetadataType`: Interface all binding classes implement (extends `Serializable`)
- `AbstractMetadataType`: Base class with unmapped columns tracking
- `Context`: Main entry point wrapping `DatabaseMetaData`
- Binding classes: Map ResultSet columns to typed fields via `@_ColumnLabel` annotations

## Coding Style Rules

### Class Structure Order (MANDATORY)

1. License header (`/*- ... */`)
2. Package declaration
3. Imports
4. Class JavaDoc
5. Class declaration with `@_ChildOf`/`@_ParentOf` annotations
6. `serialVersionUID` field
7. Static comparators
8. Column label constants (`COLUMN_LABEL_XXX`)
9. Column value constants (`COLUMN_VALUE_XXX`)
10. Static factory methods
11. Constructors
12. `java.lang.Object` overrides
13. Bean validation methods
14. Getter/setter pairs grouped by field
15. Instance fields at end

### Accessor Visibility

- Getters: `public`
- Setters: `protected` (NOT `public` or `private`)

### Annotation Prefixes

All project-specific annotations are prefixed with underscore: `@_ColumnLabel`, `@_NullableBySpecification`, etc.

### Comparator Pattern

```java
static Comparator<T> comparingInSpecifiedOrder(final Context context) throws SQLException {
    Objects.requireNonNull(context, "context is null");
    return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
}
```

- Always require `Context` for null precedence
- Apply `ContextUtils.nullPrecedence()` ONLY to `@_NullableBySpecification` fields
- Use `String.CASE_INSENSITIVE_ORDER` as default

## Test Framework

- JUnit 5, AssertJ, Mockito, EqualsVerifier
- Unit tests: `*Test.java` - extend `AbstractMetadataType_Test<T>`
- Integration tests: `*IT.java` - use TestContainers
- In-memory tests: `Memory_*_Test.java` (Derby, H2, HSQL, SQLite)

## Key Test Utilities

- `Context_Test_Utils`: Comprehensive test utilities for all binding types
- `MetadataType_Test_Utils`: Verifies accessors and validation
- `__Validation_Test_Utils`: Jakarta Validation testing

## Directory Structure

```
src/main/java/com/github/jinahya/database/metadata/bind/
  - Context.java (main entry point)
  - MetadataType.java (interface)
  - AbstractMetadataType.java (base class)
  - [Binding classes: Catalog, Table, Column, etc.]

src/test/java/com/github/jinahya/database/metadata/bind/
  - Context_Test_Utils.java (test utilities)
  - *Test.java (unit tests)
```
