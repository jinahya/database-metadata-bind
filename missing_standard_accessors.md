# Missing Standard Accessors Report

This document lists all classes extending/implementing `MetadataType` that have instance fields without standard getter/setter methods.

## Summary

| Class | Missing Getters | Missing Setters |
|-------|-----------------|-----------------|
| PortedKey | 3 | 1 |
| ExportedKey | 3 (inherited) | 1 (inherited) |
| ImportedKey | 3 (inherited) | 1 (inherited) |
| VersionColumn | 0 | 7 |

---

## Detailed Analysis

### 1. PortedKey (Abstract Class)

**File**: `src/main/java/com/github/jinahya/database/metadata/bind/PortedKey.java`

| Field | Type | Has Getter | Has Setter | Issue |
|-------|------|------------|------------|-------|
| updateRule | Integer | No | Yes | Missing getter |
| deleteRule | Integer | No | Yes | Missing getter |
| deferrability | Integer | No | No | Missing both |

**Note**: This is an abstract class. Its subclasses `ExportedKey` and `ImportedKey` inherit these missing accessors.

---

### 2. ExportedKey (extends PortedKey)

**File**: `src/main/java/com/github/jinahya/database/metadata/bind/ExportedKey.java`

Inherits the following missing accessors from `PortedKey`:

| Field | Type | Has Getter | Has Setter | Issue |
|-------|------|------------|------------|-------|
| updateRule | Integer | No | Yes (inherited) | Missing getter |
| deleteRule | Integer | No | Yes (inherited) | Missing getter |
| deferrability | Integer | No | No | Missing both |

---

### 3. ImportedKey (extends PortedKey)

**File**: `src/main/java/com/github/jinahya/database/metadata/bind/ImportedKey.java`

Inherits the following missing accessors from `PortedKey`:

| Field | Type | Has Getter | Has Setter | Issue |
|-------|------|------------|------------|-------|
| updateRule | Integer | No | Yes (inherited) | Missing getter |
| deleteRule | Integer | No | Yes (inherited) | Missing getter |
| deferrability | Integer | No | No | Missing both |

---

### 4. VersionColumn

**File**: `src/main/java/com/github/jinahya/database/metadata/bind/VersionColumn.java`

| Field | Type | Has Getter | Has Setter | Issue |
|-------|------|------------|------------|-------|
| scope | Integer | Yes | No | Missing setter |
| columnName | String | Yes | No | Missing setter |
| dataType | Integer | Yes | No | Missing setter |
| typeName | String | Yes | No | Missing setter |
| columnSize | Integer | Yes | No | Missing setter |
| bufferLength | Integer | Yes | No | Missing setter |
| decimalDigits | Integer | Yes | No | Missing setter |

---

## Classes with Complete Accessors

The following 23 classes have all standard accessors implemented (either manually or via Lombok):

- Attribute
- BestRowIdentifier
- Catalog
- ClientInfoProperty
- Column
- ColumnPrivilege
- CrossReference
- Function
- FunctionColumn
- IndexInfo
- PrimaryKey
- Procedure
- ProcedureColumn
- PseudoColumn
- Schema
- SuperTable
- SuperType
- Table
- TablePrivilege
- TableType
- TypeInfo
- UDT

---

## Recommended Actions

### For PortedKey.java

Add the following methods:

```java
public Integer getUpdateRule() {
    return updateRule;
}

public Integer getDeleteRule() {
    return deleteRule;
}

public Integer getDeferrability() {
    return deferrability;
}

protected void setDeferrability(final Integer deferrability) {
    this.deferrability = deferrability;
}
```

### For VersionColumn.java

Add the following methods:

```java
protected void setScope(final Integer scope) {
    this.scope = scope;
}

protected void setColumnName(final String columnName) {
    this.columnName = columnName;
}

protected void setDataType(final Integer dataType) {
    this.dataType = dataType;
}

protected void setTypeName(final String typeName) {
    this.typeName = typeName;
}

protected void setColumnSize(final Integer columnSize) {
    this.columnSize = columnSize;
}

protected void setBufferLength(final Integer bufferLength) {
    this.bufferLength = bufferLength;
}

protected void setDecimalDigits(final Integer decimalDigits) {
    this.decimalDigits = decimalDigits;
}
```
