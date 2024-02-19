# database-metadata-bind

[![Java CI with Maven](https://github.com/jinahya/database-metadata-bind/actions/workflows/maven.yml/badge.svg)](https://github.com/jinahya/database-metadata-bind/actions/workflows/maven.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_database-metadata-bind&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jinahya_database-metadata-bind)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/database-metadata-bind)](https://search.maven.org/artifact/com.github.jinahya/database-metadata-bind)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/database-metadata-bind/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/database-metadata-bind)

A library for binding results of methods defined
in [DatabaseMetaData](http://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html).

## Coordinates

See [Maven Central](https://search.maven.org/artifact/com.github.jinahya/database-metadata-bind) for available versions.

```xml
<dependency>
  <groupId>com.github.jinahya</groupId>
  <artifactId>database-metadata-bind</artifactId>
</dependency>
```

## Usage

All methods, defined in the `DatabaseMetaData`, which each returns a `ResultSet`, are prepared.

```java
class C {
    void m() {
        try (var connection = connect()) {
            var metadata = connection.getDatabaseMetaData();
            var context = Context.newInstance(metadata);
            var catalogs = context.getCatalogs();
            var tables = context.getTables(null, null, "%", null);
        }
    }
}
```

## How to contribute?

A lot of classes/methods defined in this module need to be tested with various kinds of real databases.

### Add your JDBC driver as a test-scoped dependency.

```xml
<dependency>
  ...
  <scope>test</scope>
</dependency>
```

### Run the `ExternalIT` class with `url`, `user`, and `password` parameter.

```commandline
$ mvn \
  -Pfailsafe \
  -Dit.test=ExternalIT \
  -Durl='<your-jdbc-url>' \
  -Duser='<your-own-user>' \
  -Dpassword='<your-own-password>' \
  clean test-compile failsafe:integration-test
```

----

## Links

### Docker
* [Running Oracle XE with TestContainers on Apple Silicon](https://blog.jdriven.com/2022/07/running-oracle-xe-with-testcontainers-on-apple-silicon/)

### MariaDB
* [getTables should be ordered as expected](https://jira.mariadb.org/browse/CONJ-1156)
* [DatabaseMetaData#getFunctions's result not property ordered](https://jira.mariadb.org/browse/CONJ-1158)
* [DatabaseMetaData#getClientInfoProperties not ordered correctly](https://jira.mariadb.org/browse/CONJ-1159)

### MySQL
* [DatabaseMetaData#getTables produces duplicates](https://bugs.mysql.com/bug.php?id=113970&thanks=4)

### PostgreSQL
* [DatabaseMetaData#getFunctionColumns's result has duplicate](https://github.com/pgjdbc/pgjdbc/issues/3127)

### SQL Server
* [DatabaseMetaData#getProcedures not ordered as specified](https://github.com/microsoft/mssql-jdbc/issues/2321)
