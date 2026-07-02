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
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
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

        assertUdt(instance.getTypeRef(), "", "", "t");
        verify(instance).getEffectiveTypeCat();
        verify(instance).getEffectiveTypeSchem();
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
        verify(instance, never()).getEffectiveTypeCat();
        verify(instance, never()).getEffectiveTypeSchem();
    }

    @Test
    void columnGetTableRef_MapsTableColumns() {
        final var instance = spy(new Column());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");

        assertTable(instance.getTableRef(), "", "", "t");
        verify(instance).getEffectiveTableCat();
        verify(instance).getEffectiveTableSchem();
    }

    @Test
    void columnPrivilegeRefs_MapTableAndColumnColumns() {
        final var instance = spy(new ColumnPrivilege());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");
        instance.setColumnName("c");

        assertTable(instance.getTableRef(), "", "", "t");
        assertColumn(instance.getColumnRef(), "", "", "t", "c");
        verify(instance, atLeastOnce()).getEffectiveTableCat();
        verify(instance, atLeastOnce()).getEffectiveTableSchem();
    }

    @Test
    void tablePrivilegeGetTableRef_MapsTableColumns() {
        final var instance = spy(new TablePrivilege());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");

        assertTable(instance.getTableRef(), "", "", "t");
        verify(instance).getEffectiveTableCat();
        verify(instance).getEffectiveTableSchem();
    }

    @Test
    void primaryKeyRefs_MapTableAndColumnColumns() {
        final var instance = spy(new PrimaryKey());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");
        instance.setColumnName("c");

        assertTable(instance.getTableRef(), "", "", "t");
        assertColumn(instance.getColumnRef(), "", "", "t", "c");
        verify(instance, atLeastOnce()).getEffectiveTableCat();
        verify(instance, atLeastOnce()).getEffectiveTableSchem();
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

        assertTable(instance.getPkTableRef(), "", "", "pt");
        assertColumn(instance.getPkColumnRef(), "", "", "pt", "pc");
        assertTable(instance.getFkTableRef(), "", "", "ft");
        assertColumn(instance.getFkColumnRef(), "", "", "ft", "fc");
        verify(instance, atLeastOnce()).getEffectivePktableCat();
        verify(instance, atLeastOnce()).getEffectivePktableSchem();
        verify(instance, atLeastOnce()).getEffectiveFktableCat();
        verify(instance, atLeastOnce()).getEffectiveFktableSchem();
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

        assertTable(instance.getPkTableRef(), "", "", "pt");
        assertColumn(instance.getPkColumnRef(), "", "", "pt", "pc");
        assertTable(instance.getFkTableRef(), "", "", "ft");
        assertColumn(instance.getFkColumnRef(), "", "", "ft", "fc");
        verify(instance, atLeastOnce()).getEffectivePktableCat();
        verify(instance, atLeastOnce()).getEffectivePktableSchem();
        verify(instance, atLeastOnce()).getEffectiveFktableCat();
        verify(instance, atLeastOnce()).getEffectiveFktableSchem();
    }

    @Test
    void indexInfoRefs_MapTableAndNullableColumnColumns() {
        final var instance = spy(new IndexInfo());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");
        instance.setColumnName("c");

        assertTable(instance.getTableRef(), "", "", "t");
        assertColumn(instance.getColumnRef(), "", "", "t", "c");

        instance.setColumnName(null);
        assertThat(instance.getColumnRef()).isNull();
        verify(instance, atLeastOnce()).getEffectiveTableCat();
        verify(instance, atLeastOnce()).getEffectiveTableSchem();
    }

    @Test
    void pseudoColumnGetTableRef_MapsTableColumns() {
        final var instance = spy(new PseudoColumn());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");

        assertTable(instance.getTableRef(), "", "", "t");
        verify(instance).getEffectiveTableCat();
        verify(instance).getEffectiveTableSchem();
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

        assertUdt(instance.getTypeRef(), "", "", "t");
        assertUdt(instance.getSupertypeRef(), "", "", "st");
        verify(instance).getEffectiveTypeCat();
        verify(instance).getEffectiveTypeSchem();
        verify(instance).getEffectiveSupertypeCat();
        verify(instance).getEffectiveSupertypeSchem();
    }

    @Test
    void superTableRefs_MapSubtableAndSupertableColumns() {
        final var instance = spy(new SuperTable());
        instance.setTableCat(null);
        instance.setTableSchem(null);
        instance.setTableName("t");
        instance.setSupertableName("st");

        assertTable(instance.getTableRef(), "", "", "t");
        assertTable(instance.getSupertableRef(), "", "", "st");
        verify(instance, atLeastOnce()).getEffectiveTableCat();
        verify(instance, atLeastOnce()).getEffectiveTableSchem();
    }

    @Test
    void procedureColumnGetProcedureRef_MapsProcedureColumnsIncludingSpecificName() {
        final var instance = spy(new ProcedureColumn());
        instance.setProcedureCat(null);
        instance.setProcedureSchem(null);
        instance.setProcedureName("p");
        instance.setSpecificName("sp");

        final var result = instance.getProcedureRef();
        assertThat(result.getProcedureCat()).isEmpty();
        assertThat(result.getProcedureSchem()).isEmpty();
        assertThat(result.getProcedureName()).isEqualTo("p");
        assertThat(result.getSpecificName()).isEqualTo("sp");
        verify(instance).getEffectiveProcedureCat();
        verify(instance).getEffectiveProcedureSchem();
    }

    @Test
    void functionColumnGetFunctionRef_MapsFunctionColumnsIncludingSpecificName() {
        final var instance = spy(new FunctionColumn());
        instance.setFunctionCat(null);
        instance.setFunctionSchem(null);
        instance.setFunctionName("f");
        instance.setSpecificName("sf");

        final var result = instance.getFunctionRef();
        assertThat(result.getFunctionCat()).isEmpty();
        assertThat(result.getFunctionSchem()).isEmpty();
        assertThat(result.getFunctionName()).isEqualTo("f");
        assertThat(result.getSpecificName()).isEqualTo("sf");
        verify(instance).getEffectiveFunctionCat();
        verify(instance).getEffectiveFunctionSchem();
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
