package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
 * %%
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
 * #L%
 */

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

class ReferenceHelperTest {

    @Test
    void tableGetTypeRef_MapsTypedTableTypeColumns() {
        final var instance = mock(
                Table.class,
                withSettings().spiedInstance(new Table()).defaultAnswer(CALLS_REAL_METHODS).withoutAnnotations()
        );
        instance.setTypeCat(null);
        instance.setTypeSchem(null);
        instance.setTypeName("t");

        assertUdt(instance.getTypeRef(), null, null, "t");
    }

    @Test
    void tableGetTypeRef_ReturnsNullWhenTypeNameIsNull() {
        final var instance = mock(
                Table.class,
                withSettings().spiedInstance(new Table()).defaultAnswer(CALLS_REAL_METHODS).withoutAnnotations()
        );
        instance.setTypeCat("c");
        instance.setTypeSchem("s");
        instance.setTypeName(null);

        assertThat(instance.getTypeRef()).isNull();
    }

    @Test
    void columnGetTableRef_MapsTableColumns() {
        final var instance = spy(new Column());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");

        assertTable(instance.getTableRef(), null, null, "t");
    }

    @Test
    void columnPrivilegeGetColumnRef_MapsTableAndColumnColumns() {
        final var instance = spy(new ColumnPrivilege());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");
        instance.setColumnName("c");

        assertColumn(instance.getColumnRef(), null, null, "t", "c");
        assertTable(instance.getColumnRef().getTableRef(), null, null, "t");
    }

    @Test
    void tablePrivilegeGetTableRef_MapsTableColumns() {
        final var instance = spy(new TablePrivilege());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");

        assertTable(instance.getTableRef(), null, null, "t");
    }

    @Test
    void primaryKeyGetColumnRef_MapsTableAndColumnColumns() {
        final var instance = spy(new PrimaryKey());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");
        instance.setColumnName("c");

        assertColumn(instance.getColumnRef(), null, null, "t", "c");
        assertTable(instance.getColumnRef().getTableRef(), null, null, "t");
    }

    @Test
    void portedKeyRefs_MapPkAndFkColumns() {
        final var instance = spy(new ImportedKey());
        instance.setPktableCat(null);
        instance.setPktableSchem(null);
        instance.setPktableName("pt");
        instance.setPkcolumnName("pc");
        instance.setFktableCat(null);
        instance.setFktableSchem(null);
        instance.setFktableName("ft");
        instance.setFkcolumnName("fc");

        assertColumn(instance.getPkColumnRef(), null, null, "pt", "pc");
        assertTable(instance.getPkColumnRef().getTableRef(), null, null, "pt");
        assertColumn(instance.getFkColumnRef(), null, null, "ft", "fc");
        assertTable(instance.getFkColumnRef().getTableRef(), null, null, "ft");
    }

    @Test
    void crossReferenceRefs_MapPkAndFkColumns() {
        final var instance = spy(new CrossReference());
        instance.setPktableCat(null);
        instance.setPktableSchem(null);
        instance.setPktableName("pt");
        instance.setPkcolumnName("pc");
        instance.setFktableCat(null);
        instance.setFktableSchem(null);
        instance.setFktableName("ft");
        instance.setFkcolumnName("fc");

        assertColumn(instance.getPkColumnRef(), null, null, "pt", "pc");
        assertTable(instance.getPkColumnRef().getTableRef(), null, null, "pt");
        assertColumn(instance.getFkColumnRef(), null, null, "ft", "fc");
        assertTable(instance.getFkColumnRef().getTableRef(), null, null, "ft");
    }

    @Test
    void indexInfoRefs_MapTableAndNullableColumnColumns() {
        final var instance = spy(new IndexInfo());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");
        instance.setColumnName("c");

        assertTable(instance.getTableRef(), null, null, "t");
        assertColumn(instance.getColumnRef(), null, null, "t", "c");

        instance.setColumnName(null);
        assertThat(instance.getColumnRef()).isNull();
    }

    @Test
    void pseudoColumnGetTableRef_MapsTableColumns() {
        final var instance = spy(new PseudoColumn());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");

        assertTable(instance.getTableRef(), null, null, "t");
    }

    @Test
    void superTypeRefs_MapSubtypeAndSupertypeColumns() {
        final var instance = spy(new SuperType());
        instance.setTypeCat(null);
        instance.setTypeSchem(null);
        instance.setTypeName("t");
        instance.setSupertypeCat(null);
        instance.setSupertypeSchem(null);
        instance.setSupertypeName("st");

        assertUdt(instance.getTypeRef(), null, null, "t");
        assertUdt(instance.getSupertypeRef(), null, null, "st");
    }

    @Test
    void superTableRefs_MapSubtableAndSupertableColumns() {
        final var instance = spy(new SuperTable());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");
        instance.setSupertableName("st");

        assertTable(instance.getTableRef(), null, null, "t");
        assertTable(instance.getSupertableRef(), null, null, "st");
    }

    @Test
    void procedureColumnGetProcedureRef_MapsProcedureColumnsIncludingSpecificName() {
        final var instance = spy(new ProcedureColumn());
        instance.setProcedureCat(null);
        instance.setProcedureSchem(null);
        instance.setProcedureName("p");
        instance.setSpecificName("sp");

        final var result = instance.getProcedureRef();
        assertThat(result.getProcedureCat()).isNull();
        assertThat(result.getProcedureSchem()).isNull();
        assertThat(result.getProcedureName()).isEqualTo("p");
        assertThat(result.getSpecificName()).isEqualTo("sp");
    }

    @Test
    void functionColumnGetFunctionRef_MapsFunctionColumnsIncludingSpecificName() {
        final var instance = spy(new FunctionColumn());
        instance.setFunctionCat(null);
        instance.setFunctionSchem(null);
        instance.setFunctionName("f");
        instance.setSpecificName("sf");

        final var result = instance.getFunctionRef();
        assertThat(result.getFunctionCat()).isNull();
        assertThat(result.getFunctionSchem()).isNull();
        assertThat(result.getFunctionName()).isEqualTo("f");
        assertThat(result.getSpecificName()).isEqualTo("sf");
    }

    @Test
    void tableNamespaceRefs_MapCatalogAndSchema() {
        final var instance = new Table();
        instance.setTableCat("c");
        instance.setTableSchem("s");
        final var catalog = instance.getCatalogRef();
        assertThat(catalog).isNotNull();
        assertThat(catalog.getTableCat()).isEqualTo("c");
        final var schema = instance.getSchemaRef();
        assertThat(schema).isNotNull();
        assertThat(schema.getTableCatalog()).isEqualTo("c");
        assertThat(schema.getTableSchem()).isEqualTo("s");

        instance.setTableCat(null);
        instance.setTableSchem(null);
        assertThat(instance.getCatalogRef()).isNull();
        assertThat(instance.getSchemaRef()).isNull();
    }

    @Test
    void udtNamespaceRefs_MapCatalogAndSchema() {
        final var instance = new UDT();
        instance.setTypeCat("c");
        instance.setTypeSchem("s");
        assertThat(instance.getCatalogRef()).isNotNull()
                .satisfies(x -> assertThat(x.getTableCat()).isEqualTo("c"));
        assertThat(instance.getSchemaRef()).isNotNull().satisfies(x -> {
            assertThat(x.getTableCatalog()).isEqualTo("c");
            assertThat(x.getTableSchem()).isEqualTo("s");
        });

        instance.setTypeCat(null);
        instance.setTypeSchem(null);
        assertThat(instance.getCatalogRef()).isNull();
        assertThat(instance.getSchemaRef()).isNull();
    }

    @Test
    void procedureNamespaceRefs_MapCatalogAndSchema() {
        final var instance = new Procedure();
        instance.setProcedureCat("c");
        instance.setProcedureSchem("s");
        assertThat(instance.getCatalogRef()).isNotNull()
                .satisfies(x -> assertThat(x.getTableCat()).isEqualTo("c"));
        assertThat(instance.getSchemaRef()).isNotNull().satisfies(x -> {
            assertThat(x.getTableCatalog()).isEqualTo("c");
            assertThat(x.getTableSchem()).isEqualTo("s");
        });

        instance.setProcedureCat(null);
        instance.setProcedureSchem(null);
        assertThat(instance.getCatalogRef()).isNull();
        assertThat(instance.getSchemaRef()).isNull();
    }

    @Test
    void functionNamespaceRefs_MapCatalogAndSchema() {
        final var instance = new Function();
        instance.setFunctionCat("c");
        instance.setFunctionSchem("s");
        assertThat(instance.getCatalogRef()).isNotNull()
                .satisfies(x -> assertThat(x.getTableCat()).isEqualTo("c"));
        assertThat(instance.getSchemaRef()).isNotNull().satisfies(x -> {
            assertThat(x.getTableCatalog()).isEqualTo("c");
            assertThat(x.getTableSchem()).isEqualTo("s");
        });

        instance.setFunctionCat(null);
        instance.setFunctionSchem(null);
        assertThat(instance.getCatalogRef()).isNull();
        assertThat(instance.getSchemaRef()).isNull();
    }

    @Test
    void schemaGetCatalogRef_MapsCatalog() {
        final var instance = new Schema();
        instance.setTableCatalog("c");
        instance.setTableSchem("s");
        assertThat(instance.getCatalogRef()).isNotNull()
                .satisfies(x -> assertThat(x.getTableCat()).isEqualTo("c"));

        instance.setTableCatalog(null);
        assertThat(instance.getCatalogRef()).isNull();
    }

    private static void assertTable(final Table actual, final String cat, final String schem, final String name) {
        assertThat(actual.getTableCat()).isEqualTo(cat);
        assertThat(actual.getTableSchem()).isEqualTo(schem);
        assertThat(actual.getTableName()).isEqualTo(name);
    }

    private static void assertColumn(final Column actual, final String cat, final String schem, final String table,
                                     final String column) {
        assertThat(actual.getTableCat()).isEqualTo(cat);
        assertThat(actual.getTableSchem()).isEqualTo(schem);
        assertThat(actual.getTableName()).isEqualTo(table);
        assertThat(actual.getColumnName()).isEqualTo(column);
    }

    private static void assertUdt(final UDT actual, final String cat, final String schem, final String name) {
        assertThat(actual.getTypeCat()).isEqualTo(cat);
        assertThat(actual.getTypeSchem()).isEqualTo(schem);
        assertThat(actual.getTypeName()).isEqualTo(name);
    }
}
