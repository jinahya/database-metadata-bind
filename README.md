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
final java.sql.Connection connection = connect();
final java.sql.DatabaseMetaData metadata = connection.getDataBaseMetaData();

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

## Testing for existing databases

You can retrieve information from existing databases. And, possibly, can report issues.

### Properties

name      |value                            |notes
----------|---------------------------------|-----------
`client`  |version of jdbc client           |
`url`     |connection url                   |
`user`    |username                         |
`password`|password                         |
`paths`   |comma-separated suppression paths|optional

### Clients and URLs

database  |`client` is for the version of                                 |url prefix
----------|---------------------------------------------------------------|------------------------
MySQL     |[`mysql:mysql-connector-java`](https://goo.gl/BxuJ5a)          |`jdbc:mysql://...`
PostgreSQL|[`org.mariadb.jdbc:mariadb-java-client`](https://goo.gl/6yqVxq)|`jdbc:mariadb://...`
Oracle    |                                                               |`jdbc:oracle:thin://...`
SQL Server|[`com.microsoft.sqlserver:mssql-jdbc`](https://goo.gl/cpK94Q)  |`jdbc:sqlserver://...`


#### MySQL

#### MariaDB

#### PostreSQL

#### Oracle

Oracle seems don't upload artifact to the central. See [Get Oracle JDBC drivers and UCP from Oracle Maven Repository ](https://blogs.oracle.com/dev2dev/get-oracle-jdbc-drivers-and-ucp-from-oracle-maven-repository-without-ides).

#### SQL Server

----

[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)

