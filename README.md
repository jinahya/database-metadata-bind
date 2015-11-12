database-metadata-bind
====================
![](https://travis-ci.org/jinahya/database-metadata-bind.svg?branch=develop)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jinahya/database-metadata-bind/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jinahya/database-metadata-bind)
[![GitHub version](https://badge.fury.io/gh/jinahya%2Fdatabase-metadata-bind.svg)](https://badge.fury.io/gh/jinahya%2Fdatabase-metadata-bind)
[![Dependency Status](https://www.versioneye.com/user/projects/563ccf434d415e0018000001/badge.svg?style=flat)](https://www.versioneye.com/user/projects/563ccf434d415e0018000001)

A library binding various information from [DatabaseMetaData](http://docs.oracle.com/javase/7/docs/api/java/sql/DatabaseMetaData.html).


[Wanna donate some?](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=GWDFLJNSZSEGG&lc=KR&item_name=github&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)

<!--
### maven
[maven central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22bit-io%22)
-->

<!--
### jenkins
[jinahya.com/jenkins](https://jinahya.com/jenkins/job/com.github.jinahya%20bit-io/)
-->

## Versions

| Version        | Apidocs | Site | Notes |
| :------        | :------ | :--- | :---- |
| `1.0` | [apidocs](http://jinahya.github.io/database-metadata-bind/site/1.0/apidocs/index.html) | [site](http://jinahya.github.io/database-metadata-bind/site/1.0/index.html)|Not Released Yet|
| `0.9` | [apidocs](http://jinahya.github.io/database-metadata-bind/site/0.9/apidocs/index.html) | [site](http://jinahya.github.io/database-metadata-bind/site/0.9/index.html)|Not Released Yet|
| `0.9-SNAPSHOT` | [apidocs](http://jinahya.github.io/database-metadata-bind/site/0.9-SNAPSHOT/apidocs/index.html) | [site](http://jinahya.github.io/database-metadata-bind/site/0.9-SNAPSHOT/index.html)||
| `0.8` | [apidocs](http://jinahya.github.io/database-metadata-bind/site/0.8/apidocs/index.html) | [site](http://jinahya.github.io/database-metadata-bind/site/0.8/index.html)||

## Hierarchy
  * Metadata
    * Category (`metadata/categories`)
      * Schema (`category/schemas`)
        * Function (`schema/functions`)
          * FunctionColumn (`function/functionColumns`)
        * Procedure (`schema/procedures`)
          * ProcedureColumn (`procedure/procedureColuns`)
        * Table (`schema/tables`)
          * BestRowIdentifier (`table/bestRowIdentifiers`)
          * Column (`table/columns`)
            * ColumnPrivilege (`column/columnPrivileges`)
          * ExportedKey (`table/exportedKeys`)
          * ImportedKey (`table/importedKeys`)
          * IndexInfo (`table/IndexInfo`)
          * PrimaryKey (`table/primaryKeys`)
          * PseudoColumn (`table/pseudoColumns`)
          * TablePrivilege (`table/tablePrivileges`)
          * VersionColumn (`table/versionColumns`)
        * UserDefinedType (`schema/userDefinedTypes`)
    * ClientInfoProperty (`metadata/clientInfoProperties`)
    * SchemaName (`metadata/schemaNames`)
    * TableType (`metadata/tableTypes`)

## Usage
### API Binding
````java
// prepare jdbc information
final Connection connection; // get your own
final DatabaseMetaData database = connection.getDataBaseMetaData();

// create context, and add suppressions if required
final MetadataContext context = new MetaDataContext(database);
context.addSuppressions("metadata/schemaNames", "table/pseudoColumns");

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
final UseDefinedType userDefinedType;
final JAXBContext context = JAXBContext.newInstance(UserDefinedType.class);
final Marshaller marshaller = context.createMarshaller();
marshaller.mashal(userDefinedType, ...);
````
