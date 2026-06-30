package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

class ColumnTest
        extends AbstractMetadataType_Test<Column> {

    ColumnTest() {
        super(Column.class);
    }

    // ---------------------------------------------------------------------------------------------- Jakarta-Validation
    // Note: these assert the (Bean-Validation-free) predicate logic ported from the 'jakarta' branch. This branch does
    //       NOT depend on Jakarta Bean Validation; the predicates are plain methods, exercised here directly.

    @Test
    void isNullableValid_HoldsForKnownValuesAndNull() {
        final var instance = newTypeInstance();
        // null -> holds
        instance.setNullable(null);
        assertThat(instance.isNullableValid_()).isTrue();
        // known values -> hold
        instance.setNullable(Column.COLUMN_VALUE_NULLABLE_COLUMN_NO_NULLS);
        assertThat(instance.isNullableValid_()).isTrue();
        instance.setNullable(Column.COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE);
        assertThat(instance.isNullableValid_()).isTrue();
        instance.setNullable(Column.COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE_UNKNOWN);
        assertThat(instance.isNullableValid_()).isTrue();
        // unknown value -> violated
        instance.setNullable(Integer.MIN_VALUE);
        assertThat(instance.isNullableValid_()).isFalse();
    }

    @Test
    void isScopeXxxNullWhenDataTypeIsNotRef_HoldsAsSpecified() {
        final var instance = newTypeInstance();
        instance.setDataType(null);
        instance.setScopeCatalog("c");
        instance.setScopeSchema("s");
        instance.setScopeTable("t");
        assertThat(instance.isScopeCatalogNullWhenDataTypeIsNotRef_()).isTrue();
        assertThat(instance.isScopeSchemaNullWhenDataTypeIsNotRef_()).isTrue();
        assertThat(instance.isScopeTableNullWhenDataTypeIsNotRef_()).isTrue();
        instance.setDataType(java.sql.Types.INTEGER);
        assertThat(instance.isScopeCatalogNullWhenDataTypeIsNotRef_()).isFalse();
        assertThat(instance.isScopeSchemaNullWhenDataTypeIsNotRef_()).isFalse();
        assertThat(instance.isScopeTableNullWhenDataTypeIsNotRef_()).isFalse();
        instance.setScopeCatalog(null);
        instance.setScopeSchema(null);
        instance.setScopeTable(null);
        assertThat(instance.isScopeCatalogNullWhenDataTypeIsNotRef_()).isTrue();
        assertThat(instance.isScopeSchemaNullWhenDataTypeIsNotRef_()).isTrue();
        assertThat(instance.isScopeTableNullWhenDataTypeIsNotRef_()).isTrue();
        instance.setDataType(java.sql.Types.REF);
        instance.setScopeCatalog("c");
        instance.setScopeSchema("s");
        instance.setScopeTable("t");
        assertThat(instance.isScopeCatalogNullWhenDataTypeIsNotRef_()).isTrue();
        assertThat(instance.isScopeSchemaNullWhenDataTypeIsNotRef_()).isTrue();
        assertThat(instance.isScopeTableNullWhenDataTypeIsNotRef_()).isTrue();
    }

    @Test
    void isSourceDataTypeNullWhenDataTypeIsNotDistinctOrUserGeneratedRef_HoldsAsSpecified() {
        final var instance = newTypeInstance();
        instance.setSourceDataType(java.sql.Types.INTEGER);
        instance.setDataType(null);
        assertThat(instance.isSourceDataTypeNullWhenDataTypeIsNotDistinctOrUserGeneratedRef_()).isTrue();
        instance.setDataType(java.sql.Types.VARCHAR);
        assertThat(instance.isSourceDataTypeNullWhenDataTypeIsNotDistinctOrUserGeneratedRef_()).isFalse();
        instance.setSourceDataType(null);
        assertThat(instance.isSourceDataTypeNullWhenDataTypeIsNotDistinctOrUserGeneratedRef_()).isTrue();
        instance.setSourceDataType(java.sql.Types.INTEGER);
        instance.setDataType(java.sql.Types.DISTINCT);
        assertThat(instance.isSourceDataTypeNullWhenDataTypeIsNotDistinctOrUserGeneratedRef_()).isTrue();
        instance.setDataType(java.sql.Types.REF);
        assertThat(instance.isSourceDataTypeNullWhenDataTypeIsNotDistinctOrUserGeneratedRef_()).isTrue();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    Column newTypeInstance() {
        final var instance = super.newTypeInstance();
        instance.setTableName("");
        instance.setColumnName("");
        instance.setOrdinalPosition(1);
        return instance;
    }
}
