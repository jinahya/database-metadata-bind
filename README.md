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

* [Home](https://github.com/jinahya/database-metadata-bind/wiki) — overview and quick start
* [API Reference](https://github.com/jinahya/database-metadata-bind/wiki/API-Reference) — the bound methods
* [Model Notes](https://github.com/jinahya/database-metadata-bind/wiki/Model-Notes) — binding behavior and catalog/schema/pattern handling
* [Testing and Build](https://github.com/jinahya/database-metadata-bind/wiki/Testing-and-Build) — build requirements and running tests
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
    var context = Context.newInstance(connection);

    // Get all catalogs
    List<Catalog> catalogs = context.getCatalogs();

    // Get all tables (null = don't filter)
    List<Table> tables = context.getTables(null, null, "%", null);

    // Get columns for a specific table
    List<Column> columns = context.getColumns("my_catalog", "my_schema", "my_table", "%");
}
```

See the [wiki](https://github.com/jinahya/database-metadata-bind/wiki) for more examples, catalog/schema `null` handling, and per-driver notes.

## Contributing

Many bindings need testing against real databases. See
[Testing and Build](https://github.com/jinahya/database-metadata-bind/wiki/Testing-and-Build) for how to run the
integration tests (e.g. `ExternalIT`) with your own JDBC driver, URL, and credentials.
