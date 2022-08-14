# database-metadata-bind

[![Java CI with Maven](https://github.com/jinahya/database-metadata-bind/actions/workflows/maven.yml/badge.svg)](https://github.com/jinahya/database-metadata-bind/actions/workflows/maven.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/database-metadata-bind)](https://search.maven.org/artifact/com.github.jinahya/database-metadata-bind)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/database-metadata-bind/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/database-metadata-bind)

A library for binding various information
from [DatabaseMetaData](http://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html).

## Usage

```java
try(var connection = connect()){
    final var context = Context.newInstance(connection);
    final var catalogs = context.getCatalogs(null, new ArrayList<>());
    if (catalogs.isEmpty()) {
        catalogs.add(Catalog.newVirtualInstance());
    }
    for (final var catalog : catalogs) {
        catalog.retrieveChildren(context);
    }
}
```

## Gathering metadata from existing databases

```shell
$ mvn -Pfailsafe,external-<server> \
      -Dversion.<client>=x.y.z \
      -Durl=jdbc:...://... \
      -Duser=... \
      -Dpassword=... \
      -Dit.test=ExternalIT \
      verify
...
$ cat target/external.xml
...
$
```

### Properties

name              |value                 |notes
------------------|----------------------|-----------
`<server>`        |server identifier     |see below
`version.<client>`|version of `<client>` |see below
`url`             |connection url        |
`user`            |user                  |
`password`        |password              |

### `<server>` / `<client>`

database  |`<server>`      |`<client>`
----------|----------------|----------------------------------------------
MariaDB   |`mariadb`       |[`mariadb-java-client`][mariadb-java-client]
MySQL     |`mysql`         |[`mysql-connector-java`][mysql-connector-java]
Oracle    |`oracle-ojdbc6` |[`ojdbc6`][ojdbc6]
Oracle    |`oracle-ojdbc8` |[`ojdbc8`][ojdbc8]
Oracle    |`oracle-ojdbc10`|[`ojdbc10`][ojdbc10]
Oracle    |`oracle-ojdbc11`|[`ojdbc11`][ojdbc11]
PostgreSQL|`postgresql`    |[`postgresql`][postgresql]
SQL Server|`sqlserver`     |[`mssql-jdbc`][mysql-jdbc]

e.g.

```shell
$ mvn -Pfailsafe,external-mysql \
      -Dversion.mysql-connector-java=8.0.25
      -Durl=jdbc:mysql://host:port/database
      -Duser=...
      -Dpassword=...
      -Dit.test=ExternalIT \
      verify
...
$ 
```

```shell
$ mvn -Pexternal-oracle-ojdbc11 \
      -Dversion-ojdbc11=21.1.0.0 \
      -Durl=jdbc:oracle:thin:@//host:port/service \
      -Duser=scott \
      -Dpassword=tiger \
      -Dit.test=ExternalIT \
      verify
...
$ 
```

[DatabaseMetaData]: https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/DatabaseMetaData.html

[ResultSet]: https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/ResultSet.html

[mariadb-java-client]: https://search.maven.org/artifact/org.mariadb.jdbc/mariadb-java-client

[mysql-connector-java]: https://search.maven.org/artifact/mysql/mysql-connector-java

[ojdbc6]: https://search.maven.org/artifact/com.oracle.database.jdbc/ojdbc6

[ojdbc8]: https://search.maven.org/artifact/com.oracle.database.jdbc/ojdbc8

[ojdbc10]: https://search.maven.org/artifact/com.oracle.database.jdbc/ojdbc10

[ojdbc11]: https://search.maven.org/artifact/com.oracle.database.jdbc/ojdbc11

[postgresql]: https://search.maven.org/artifact/org.postgresql/postgresql

[mysql-jdbc]: https://search.maven.org/artifact/com.microsoft.sqlserver/mssql-jdbc
