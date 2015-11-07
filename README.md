database-metadata-bind
====================
![](https://travis-ci.org/jinahya/database-metadata-bind.svg?branch=develop)

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
| `0.6-SNAPSHOT` | [apidocs](http://jinahya.github.io/database-metadata-bind/site/0.6-SNAPSHOT/apidocs/index.html) | [site](http://jinahya.github.io/database-metadata-bind/site/0.6-SNAPSHOT/index.html)||
| `0.5`          | [apidocs](http://jinahya.github.io/database-metadata-bind/site/0.5/apidocs/index.html) | [site](http://jinahya.github.io/database-metadata-bind/site/0.5/index.html)||

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
