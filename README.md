database-metadata-bind
====================
[![Build Status](https://travis-ci.org/jinahya/database-metadata-bind.svg?branch=develop)](https://travis-ci.org/jinahya/database-metadata-bind)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/database-metadata-bind.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jinahya%22%20a%3A%22database-metadata-bind%22)
[![Javadocs](http://javadoc.io/badge/com.github.jinahya/database-metadata-bind.svg)](http://javadoc.io/doc/com.github.jinahya/database-metadata-bind)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/2e056714e9614bf89b860601cbb2b174)](https://www.codacy.com/app/jinahya/database-metadata-bind)

A library binding various information from [DatabaseMetaData](http://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html).

## Usage

```java
// create a context from a connection
final Connection connection = connect();
final Context context = Context.newInstance(connection);

// invoke methods
final List<Catalog> catalogs = context.getCatalogs();
final List<Schema> schemas = context.getSchemas("", null);
final List<Table> tables = context.getTables(null, null, null); // list all tables
final List<PrimaryKey> primaryKeys
        = context.getPrimaryKeys("PUBLIC", "SYSTEM_LOBS", "BLOCKS");

// bind all
final Metadata metadata = Metadata.newInstance(context);
```
## Testing

### Memory

Test cases for in-memory databases such as [Derby](https://db.apache.org/derby/), [H2](http://www.h2database.com/html/main.html), [HSQLDB](http://hsqldb.org/) and [SQLite](https://www.sqlite.org/) are prepared.
See `target/memory.<name>.metadata.xml` files.

### Container

TODO

### External

Tests against existing databases.

```sh
$ mvn -Pfailsafe,external-<server> \
      -Dversion.<client>="x.y.z" \
      -Durl="jdbc:...://..." \
      -Duser="some" \
      -Dpassword="some" \
      -Dit.test=ExternalIT \
      verify
$ cat target/external.xml
```

#### Properties

name      |value                            |notes
----------|---------------------------------|-----------
`server`  |target database server           |see below
`client`  |version of target jdbc client    |see below
`url`     |connection url                   |The first argument of [DriverManager#getConnection](https://goo.gl/9q4zW7)
`user`    |username                         |The second argument of [DriverManager#getConnection](https://goo.gl/9q4zW7)
`password`|password                         |The third argument of [DriverManager#getConnection](https://goo.gl/9q4zW7)

#### Servers, Clients and URLs

database  |`server`        |`<client>` is the version of                  
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
$ mvn -Pexternal-oracle-ojdbc11 \
      -Dversion.ojdbc11=21.1.0.0 \
      -Durl="jdbc:oracle:thin:@//host:port/service" \
      -Duser=scott \
      -Dpassword=tiger \
      -Dit.test=ExternalIT \
      verify
$
```

----

[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)

[mariadb-java-client]: https://search.maven.org/artifact/org.mariadb.jdbc/mariadb-java-client
[mysql-connector-java]: https://search.maven.org/artifact/mysql/mysql-connector-java
[ojdbc6]: https://search.maven.org/artifact/com.oracle.database.jdbc/ojdbc6
[ojdbc8]: https://search.maven.org/artifact/com.oracle.database.jdbc/ojdbc8
[ojdbc10]: https://search.maven.org/artifact/com.oracle.database.jdbc/ojdbc10
[ojdbc11]: https://search.maven.org/artifact/com.oracle.database.jdbc/ojdbc11
[postgresql]: https://search.maven.org/artifact/org.postgresql/postgresql
[mysql-jdbc]: https://search.maven.org/artifact/com.microsoft.sqlserver/mssql-jdbc 

