/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.database.metadata.bind;

/**
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MetadataVerifier {

    //    public static void verify(final Metadata metadata) {
//        Objects.requireNonNull(metadata, "null metadata");
//        // catalogs must be order by catalog.name
//        {
//            final List<Catalog> catalogs = metadata.getCatalogs();
//            for (int i = 1; i < catalogs.size(); i++) {
//                Assert.assertTrue(
//                        catalogs.get(i - 1).getTableCat()
//                        .compareTo(catalogs.get(i).getTableCat())
//                        < 0);
//            }
//        }
////        // catalog names must be order by themselves
////        {
////            final List<String> catalogNames = metadata.getCatalogNames();
////            for (int i = 1; i < catalogNames.size(); i++) {
////                Assert.assertTrue(
////                    catalogNames.get(i - 1).compareTo(catalogNames.get(i))
////                    < 0);
////            }
////        }
//        for (final Catalog catalog : metadata.getCatalogs()) {
//            verify(catalog);
//        }
//    }
//
//    public static void verify(final Catalog catalog) {
//        Objects.requireNonNull(catalog, "null catalog");
//        // schemas must be order by schema.name
//        {
//            final List<Schema> schemas = catalog.getSchemas();
//            for (int i = 1; i < schemas.size(); i++) {
//                Assert.assertTrue(
//                        schemas.get(i - 1).getTableSchem()
//                        .compareTo(schemas.get(i).getTableSchem())
//                        < 0);
//            }
//        }
////        // schema names must be order by themselves
////        {
////            final List<String> schemaNames = catalog.getSchemaNames();
////            for (int i = 1; i < schemaNames.size(); i++) {
////                Assert.assertTrue(
////                    schemaNames.get(i - 1).compareTo(schemaNames.get(i))
////                    < 0);
////            }
////        }
//    }
//
//    public static void verify(final Schema schema) {
//        Objects.requireNonNull(schema, "null schema");
//        // functions must be order by function.name
//        {
//            final List<Function> functions = schema.getFunctions();
//            for (int i = 1; i < functions.size(); i++) {
//                Assert.assertTrue(
//                        functions.get(i - 1).getFunctionName()
//                        .compareTo(functions.get(i).getFunctionName())
//                        < 0);
//            }
//        }
//        // procedures must be order by their names.
//        {
//            final List<Procedure> procedures = schema.getProcedures();
//            for (int i = 1; i < procedures.size(); i++) {
//                Assert.assertTrue(
//                        procedures.get(i - 1).getProcedureName()
//                        .compareTo(procedures.get(i).getProcedureName())
//                        < 0);
//            }
//        }
//        // tables must be order by their names.
//        {
//            final List<Table> tables = schema.getTables();
//            for (int i = 1; i < tables.size(); i++) {
//                Assert.assertTrue(
//                        tables.get(i - 1).getTableName()
//                        .compareTo(tables.get(i).getTableName())
//                        < 0);
//            }
//        }
////        // table names must be order by themselves
////        {
////            final List<String> tableNames = schema.getTableNames();
////            for (int i = 1; i < tableNames.size(); i++) {
////                Assert.assertTrue(
////                    tableNames.get(i - 1).compareTo(tableNames.get(i))
////                    < 0);
////            }
////        }
//    }
    public void printSchema() {
//        final JAXBContext context =
    }
}
