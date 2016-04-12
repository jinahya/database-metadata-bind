database-metadata-bind
====================
[![Build Status](https://travis-ci.org/jinahya/database-metadata-bind.svg?branch=develop)](https://travis-ci.org/jinahya/database-metadata-bind)
[![Dependency Status](https://www.versioneye.com/user/projects/563ccf434d415e0018000001/badge.svg)](https://www.versioneye.com/user/projects/563ccf434d415e0018000001)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/database-metadata-bind.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jinahya%22%20a%3A%22database-metadata-bind%22)
[![javadoc.io](https://img.shields.io/badge/javadoc.io-v1.0.2-blue.svg)](http://www.javadoc.io/doc/com.github.jinahya/database-metadata-bind/1.0.2)
[![Support via Gratipay](https://img.shields.io/gratipay/JSFiddle.svg)](https://gratipay.com/~jinahya/)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/2e056714e9614bf89b860601cbb2b174)](https://www.codacy.com/app/jinahya/database-metadata-bind)
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)

A library binding various information from [DatabaseMetaData](http://docs.oracle.com/javase/7/docs/api/java/sql/DatabaseMetaData.html).

[Wanna donate some?](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=GWDFLJNSZSEGG&lc=KR&item_name=github&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)

## Hierarchy
![Class Diagram](http://jinahya.github.io/database-metadata-bind/site/0.16/apidocs/com/github/jinahya/sql/database/metadata/bind/com.github.jinahya.sql.database.metadata.bind.png)

## Usage
### API Binding
````java
// prepare jdbc information
final Connection connection = connect();
final DatabaseMetaData database = connection.getDataBaseMetaData();

// create context, and add suppressions if required
final MetadataContext context = new MetadataContext(database);
context.suppressions("metadata/schemaNames", "table/pseudoColumns");

// bind various informations
final Metadata metadata = context.getMetadata(); // bind all
final List<Categories> categories = metadata.getCategories();
for (final Category category : categories) {
    final List<Schema> schemas = category.getSchemas();
}
final List<Schema> schemas = context.getSchemas("", null);
final List<Tables> tables = context.getTables(null, null, null); // list all tables
final List<PrimaryKeys> primaryKeys
    = context.getPrimaryKeys("PUBLIC", "SYSTEM_LOBS", "BLOCKS");
````
### XML Binding
All classes are annotated with `@XmlRootElement`.
````java
final UDT udt;
final JAXBContext context = JAXBContext.newInstance(UDT.class);
final Marshaller marshaller = context.createMarshaller();
marshaller.mashal(udt, ...);
````
----
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)
