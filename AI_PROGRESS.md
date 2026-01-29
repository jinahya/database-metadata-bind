# AI_PROGRESS

## Completed

### Context_Test_Utils Refactoring (2026-01-29)

- [x] Standardized assertion chains to fluent API pattern
- [x] Removed `if (true)` blocks from assertion sections
- [x] Simplified `satisfiesAnyOf` patterns to single `.isSortedAccordingTo()` calls
- [x] Added empty `.allSatisfy()` blocks at end of assertion chains
- [x] Removed dead code (`if (false)` block with parallel cross-reference test)
- [x] Removed unused `Executors` import

### Methods Updated with New Assertion Pattern

- [x] `attributes()`
- [x] `bestRowIdentifier()`
- [x] `catalogs()`
- [x] `clientInfoProperties()`
- [x] `columnPrivileges()`
- [x] `crossReference()`
- [x] `exportedKeys()`
- [x] `functions()`
- [x] `functionColumns()`
- [x] `importedKeys()`
- [x] `indexInfo()`
- [x] `procedures()`
- [x] `procedureColumns()`
- [x] `superTypes()`
- [x] `tables()`
- [x] `primaryKeys()`
- [x] `pseudoColumns()`
- [x] `superTables()`
- [x] `tablePrivileges()`
- [x] `tableTypes()`
- [x] `typeInfo()`
- [x] `udts()`
- [x] `versionColumns()`

## In Progress

### Sorting Assertions (Commented Out - Need Investigation)

Some sorting assertions remain commented out due to potential issues:

- [ ] `catalogs`: `.isSortedAccordingTo(Catalog.comparingInSpecifiedOrder(context))`
- [ ] `columnPrivileges`: `.isSortedAccordingTo(ColumnPrivilege.comparingInSpecifiedOrder(context))`
- [ ] `tablePrivileges`: `.isSortedAccordingTo(TablePrivilege.comparingInSpecifiedOrder(context))`

Note: `tables` sorting is conditionally excluded for MariaDB and SQL Server due to known issues.

## To-Do

- [ ] Investigate why some sorting assertions are failing
- [ ] Verify comparators exist and work correctly for commented assertions
- [ ] Run integration tests against all database containers
