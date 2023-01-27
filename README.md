# database-metadata-bind

[![Java CI with Maven](https://github.com/jinahya/database-metadata-bind/actions/workflows/maven.yml/badge.svg)](https://github.com/jinahya/database-metadata-bind/actions/workflows/maven.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/database-metadata-bind)](https://search.maven.org/artifact/com.github.jinahya/database-metadata-bind)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/database-metadata-bind/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/database-metadata-bind)

A library for binding various information
from [DatabaseMetaData](http://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html).

## Coordinates

See [Maven Central](https://search.maven.org/artifact/com.github.jinahya/database-metadata-bind) for available versions.

```xml
<dependency>
  <groupId>com.github.jinahya</groupId>
  <artifactId>database-metadata-bind</artifactId>
</dependency>
```

## Usage

All methods, defined in the `DatabaseMetaData`, return `ResultSet` is prepared.

```java
try (var connection = connect()) {
    var context = Context.newInstance(connection);
    var catalogs = context.getCatalogs();
    var tables = context.getTables(null, null, "%", null);
}
```
