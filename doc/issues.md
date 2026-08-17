# Main Module Issues Analysis

**Date**: 2026-02-04
**Module**: `src/main/java/com/github/jinahya/database/metadata/bind/`

## Summary

| Severity | Open |
|----------|------|
| Critical | 0 |
| Major | 1 |
| Moderate | 2 |
| Minor | 1 |

---

## Critical Issues

(none)

---

## Major Issues

### 4. Missing Comparator Context Parameter Methods [PARTIALLY FIXED]

**Description**: Several binding classes have incomplete comparator method overloads.

**Status**:
- `PrimaryKey.java`: Added new `comparingInSpecifiedOrder(UnaryOperator, Comparator)` method
- `Procedure.java`: Added new `comparingInSpecifiedOrder(UnaryOperator, Comparator)` method
- `BestRowIdentifier.java`: No change needed - ordered by `SCOPE` (Integer), not strings

**New Pattern** (per CLAUDE_COMPARATOR.md and Attribute.java):
```java
static Comparator<T> comparingInSpecifiedOrder(UnaryOperator<String> operator, Comparator<? super String> comparator)
```

---

## Moderate Issues

### 6. Transient Fields Without @_ColumnLabel (SuperTable)

**Description**: SuperTable has 4 transient cache fields without `@_ColumnLabel` annotations.

**File**: `SuperTable.java` (lines 197-203)

**Fields**:
```java
private transient Catalog tableCatalog_;
private transient Schema tableSchema_;
private transient Catalog supertableCatalog_;
private transient Schema supertableSchema_;
```

**Consideration**: While these are cache fields (not ResultSet columns), CLAUDE.md states "Add `@_ColumnLabel` to all fields". Either add exception documentation or annotate appropriately.

---

### 8. Missing Jakarta-Validation Sections

**Description**: Several binding classes with nullable fields lack proper Jakarta-Validation section headers.

**Affected Files**:
- `Catalog.java` (line 114) - Section exists but empty
- `Schema.java` - No Jakarta-Validation section
- `PrimaryKey.java` - No validation methods
- `Table.java` - No validation section
- `UDT.java` - No validation section

**Fix**: Add `// ----- Jakarta-Validation` section headers where validation methods exist or are needed.

---

## Minor Issues

### 10. Commented-Out ParentOf Annotation (UDT)

**Description**: Self-referencing parent relationship is commented out.

**File**: `UDT.java` (line 21)

**Code**: `//@_ParentOf(UDT.class)`

**Consideration**: Either implement properly or remove the commented annotation.

---

## Recommendations

1. **Low Priority**: Add missing comparator overloads with Context parameter
2. **Low Priority**: Document exception for transient cache fields or add appropriate annotations
