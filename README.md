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

```text
try (var connection = connect()) {
    var context = Context.newInstance(connection);
    var catalogs = context.getCatalogs();
    var tables = context.getTables(null, null, "%", null);
}
```

## How to contribute?

A lot of classes/methods defined in this module need to be tested with various kinds of real databases.

### Add your JDBC driver as a test-scoped dependency.

```xml
<dependency>
  ...
</dependency>
```

### Run the `ExternalIT` class with `url`, `user`, and `password` parameter.

```commandline
$ mvn -Pfailsafe \
  -Dtest=ExternalIT \
  -Durl='<your-jdbc-url>' \
  -Duser='<your-own-user>' \
  -Dpassword='<your-own-password>'
```
