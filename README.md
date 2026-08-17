# database-metadata-bind

[![Java CI with Maven](https://github.com/jinahya/database-metadata-bind/actions/workflows/maven.yml/badge.svg?branch=develop)](https://github.com/jinahya/database-metadata-bind/actions/workflows/maven.yml?query=branch%3Adevelop)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_database-metadata-bind&metric=alert_status&branch=develop)](https://sonarcloud.io/summary/new_code?id=jinahya_database-metadata-bind&branch=develop)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jinahya/database-metadata-bind.svg)](https://central.sonatype.com/artifact/io.github.jinahya/database-metadata-bind)
[![Javadoc](https://javadoc.io/badge2/io.github.jinahya/database-metadata-bind/javadoc.svg)](https://javadoc.io/doc/io.github.jinahya/database-metadata-bind)

A library for binding results of methods defined
in [DatabaseMetaData](https://docs.oracle.com/en/java/javase/25/docs/api/java.sql/java/sql/DatabaseMetaData.html).

All 26 methods in `DatabaseMetaData` that return `ResultSet` are bound to type-safe Java classes.

## Documentation

Full documentation lives in the [project wiki](https://github.com/jinahya/database-metadata-bind/wiki):

* [Home](https://github.com/jinahya/database-metadata-bind/wiki) — overview, snapshot, dependencies, basic usage
* [API Reference](https://github.com/jinahya/database-metadata-bind/wiki/API-Reference) — the bound methods
* [Model Notes](https://github.com/jinahya/database-metadata-bind/wiki/Model-Notes) — binding behavior and catalog/schema/pattern handling
* [XML and JSON Binding](https://github.com/jinahya/database-metadata-bind/wiki/XML-and-JSON-Binding) — marshalling bound records
* [Testing and Build](https://github.com/jinahya/database-metadata-bind/wiki/Testing-and-Build) — build requirements and running tests
* [External Integration Tests](https://github.com/jinahya/database-metadata-bind/wiki/External-Integration-Tests) — running `ExternalIT` against your own database
* [Known Issues](https://github.com/jinahya/database-metadata-bind/wiki/Known-Issues) — driver-specific quirks

## Coordinates

See [Maven Central](https://central.sonatype.com/artifact/io.github.jinahya/database-metadata-bind/overview) for available versions.

```xml
<dependency>
  <groupId>io.github.jinahya</groupId>
  <artifactId>database-metadata-bind</artifactId>
</dependency>
```

## Quick start

```java
try (var connection = dataSource.getConnection()) {
    var context = Context.from(connection);

    // Get all catalogs
    List<Catalog> catalogs = context.getCatalogs();

    // Get all tables (null = don't filter)
    List<Table> tables = context.getTables(null, null, "%", null);

    // Get columns for a specific table
    List<Column> columns = context.getColumns("my_catalog", "my_schema", "my_table", "%");
}
```

## Two shapes for every lookup

Each bound method comes in two forms — one that returns a `List`, and one that hands each row to a
`Consumer` as it is read:

```java
List<Table> tables = context.getTables(null, null, "%", null);   // materializes every row
context.forEachTable(null, null, "%", null, table -> { ... });   // one row at a time
```

Both read the same result set and bind the same objects. They differ in *when* you see each row.

Reach for `get*` by default — it is the simpler call and the list is yours to keep. Reach for
`forEach*` when the sweep may be large (`getAllColumns()` materializes every column in the database;
`forEachColumn(...)` holds one row at a time), or when you need to read a driver extension that is
not a simple scalar.

That last case has a catch worth knowing before you pick: a driver may return a `Blob`, `Clob`,
`Array`, or `SQLXML` in `getUnknownColumns()`, and those are locators that die with the result set —
readable inside a `forEach*` callback, already dead by the time a `get*` list reaches you. See
[Home](https://github.com/jinahya/database-metadata-bind/wiki) for the full comparison and
[Model Notes](https://github.com/jinahya/database-metadata-bind/wiki/Model-Notes) for locator
lifetimes and unknown columns.

See the [wiki](https://github.com/jinahya/database-metadata-bind/wiki) for more examples, catalog/schema `null` handling, and per-driver notes.