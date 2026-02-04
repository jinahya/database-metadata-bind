# Catalogs and Schemas

Say, we call getTables.
https://docs.oracle.com/en/java/javase/25/docs/api/java.sql/java/sql/DatabaseMetaData.html#getTables(java.lang.String,java.lang.String,java.lang.String,java.lang.String%5B%5D)

The results of TABLE_CAT, TABLE_SCHEM may be null.

So what is the **actual** value of a table, when it's not non-blank string?
