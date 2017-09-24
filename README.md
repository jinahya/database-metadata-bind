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

name      |value                            |notes
----------|---------------------------------|-----------
`client`  |version of jdbc client           |
`url`     |connection url                   |
`user`    |username                         |
`password`|password                         |
`paths`   |comma-separated suppression paths|optional

### MySQL

The value of `client` is for [`mysql:mysql-connector-java`](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22mysql%22%20a%3A%22mysql-connector-java%22).

See, for more information, [MySQL Connector/J 5.1 Developer Guide](https://dev.mysql.com/doc/connector-j/5.1/en/).

```sh
$ mvn -Dclient="5.1.44" \
      -Durl="jdbc:mysql://address:port/database" \
      -Duser="username" \
      -Dpassword="password" \
      -Dpaths="schema/tables,schema/functions" \
      -Dtest=MysqlTest \
      test
```

You'll get `target/mysql.xml`.

### MariaDB

The value for `client` is for [`org.mariadb.jdbc:mariadb-java-client`](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.mariadb.jdbc%22%20a%3A%22mariadb-java-client%22).

See, for more information, [About MariaDB Connector/J](https://mariadb.com/kb/en/library/about-mariadb-connector-j/).

```sh
$ mvn -Dclient="2.1.1" \
      -Durl="jdbc:mariadb://address:port/database" \
      -Duser="username" \
      -Dpassword="password" \
      -Dpaths="of/some" \
      -Dtest=MariadbTest \
      test
```

You'll get `target/mariadb.xml`.

### PostreSQL

The value of `client` property is for [`org.postgresql:postgresql`](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.postgresql%22%20a%3A%22postgresql%22).

See, for more information, [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/).

### Oracle

### SQL Server

The value of `client` property is for [`com.microsoft.sqlserver:mssql-jdbc`](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.microsoft.sqlserver%22%20AND%20a%3A%22mssql-jdbc%22).

See, for more information, [Microsoft JDBC Driver for SQL Server](https://docs.microsoft.com/en-us/sql/connect/jdbc/microsoft-jdbc-driver-for-sql-server).

```sh
$ mvn -Dclient="x.y.z" \
      -Durl="jdbc:sqlserver://..." \
      -Duser="username" \
      -Dpassword="password" \
      -Dpaths="of/some" \
      -Dtest=SqlserverTest \
      test
```

You'll get `target/sqlserver.xml`.

----

[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)

