database-metadata-bind
====================
[![Build Status](https://travis-ci.org/jinahya/database-metadata-bind.svg?branch=develop)](https://travis-ci.org/jinahya/database-metadata-bind)
[![Dependency Status](https://www.versioneye.com/user/projects/563ccf434d415e0018000001/badge.svg)](https://www.versioneye.com/user/projects/563ccf434d415e0018000001)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/database-metadata-bind.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jinahya%22%20a%3A%22database-metadata-bind%22)
[![Javadocs](http://javadoc.io/badge/com.github.jinahya/database-metadata-bind.svg)](http://javadoc.io/doc/com.github.jinahya/database-metadata-bind)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/2e056714e9614bf89b860601cbb2b174)](https://www.codacy.com/app/jinahya/database-metadata-bind)

A library binding various information from [DatabaseMetaData](http://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html).

## Usage

### API Binding

```java
// prepare jdbc information
final Connection connection = connect();
final DatabaseMetaData metadata = connection.getMetaData();

// create context, and add suppressions if required
final MetadataContext context = new MetadataContext(metadata);
context.suppress("schema/functions", "table/pseudoColumns");

// bind various informations
final List<Catalog> catalogs = context.getCatalogs();
final List<Schema> schemas = context.getSchemas("", null);
final List<Tables> tables = context.getTables(null, null, null); // list all tables
final List<PrimaryKeys> primaryKeys
    = context.getPrimaryKeys("PUBLIC", "SYSTEM_LOBS", "BLOCKS");
```

### XML Binding

Almost all classes are annotated with `@XmlRootElement`.

```java
final UDT udt;
final JAXBContext context = JAXBContext.newInstance(UDT.class);
final Marshaller marshaller = context.createMarshaller();
marshaller.mashal(udt, ...);
```

## Testing

### Memory

Test cases for in-memory databases such as [Derby](https://db.apache.org/derby/), [H2](http://www.h2database.com/html/main.html), [HSQLDB](http://hsqldb.org/), [SQLite](https://www.sqlite.org/) are prepared.

### Embedded

Tests against predefined embeddable databases.

```sh
$ mvn -Pembedded-<server> \
      -Dclient="x.y.z" \
      -Durl="jdbc:...://..." \
      -Duser="some" \
      -Dpassword="some" \
      -Dpaths="of/some,other/others,..." \
      -Dtest=<Server>Test \
      test
$ cat target/embedded.<server>.xml
```

#### Properties

name      |value                            |notes
----------|---------------------------------|-----------
`server`  |target database server           |see below
`client`  |version of target jdbc client    |see below
`paths`   |comma-separated suppression paths|optional

#### Servers, Clients and Tests

database                                                 |`server` is the version of    |`client` is the version of                                 |test prefix            
---------------------------------------------------------|------------|---------------------------------------------------------------|------------------------
[MariaDB](https://mariadb.org/)                          |[`ch.vorburger.mariaDB4j:mariaDB4j`](https://goo.gl/8MmvRc)     |[`ch.vorburger.mariaDB4j:mariaDB4j`](https://goo.gl/nRbU1J)          |`Mariadb`      
[PostgreSQL](https://www.postgresql.org/)                 |[`ru.yandex.qatools.embed:postgresql-embedded`](https://goo.gl/WoH4K9)|[`org.postgresql:postgresql`](https://goo.gl/JgXCaL)|`Postgres`    

### External

Tests against existing external databases.

```sh
$ mvn -Pexternal-<server> \
      -Dclient="x.y.z" \
      -Durl="jdbc:...://..." \
      -Duser="some" \
      -Dpassword="some" \
      -Dpaths="of/some,other/others,..." \
      -Dtest=ExternalTest \
      test
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
`paths`   |comma-separated suppression paths|optional

#### Servers, Clients and URLs

database                                                 |`server`    |`client` is for the version of                                 |`url` prefix            
---------------------------------------------------------|------------|---------------------------------------------------------------|------------------------
[MySQL](https://www.mysql.com/)                          |`mysql`     |[`mysql:mysql-connector-java`](https://goo.gl/BxuJ5a)          |`jdbc:mysql://...`      
[PostgreSQL](https://www.postgresql.org/)                 |`postgresql`|[`org.mariadb.jdbc:mariadb-java-client`](https://goo.gl/6yqVxq)|`jdbc:mariadb://...`    
[SQL Server](https://www.microsoft.com/en-us/sql-server/)|`sqlserver` |[`com.microsoft.sqlserver:mssql-jdbc`](https://goo.gl/cpK94Q)  |`jdbc:sqlserver://...`  
[Oracle](https://www.oracle.com/database/index.html)     |`oracle`    |[`com.oracle.jdbc:ojdbc8`](https://goo.gl/Qe1bPT)              |`jdbc:oracle:thin://...`

----

[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)

