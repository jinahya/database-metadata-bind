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

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PseudoColumnTest extends AbstractMetadataTypeTest<PseudoColumn> {

    PseudoColumnTest() {
        super(PseudoColumn.class);
    }

    @Test
    void getPseudoColumnId__() {
        // GIVEN
        final var spy = super.typeSpy();
        // WHEN
        final var tableCat = current().nextBoolean() ? "" : null;
        when(spy.getTableCat()).thenReturn(tableCat);
        final var tableSchem = current().nextBoolean() ? "" : null;
        when(spy.getTableSchem()).thenReturn(tableSchem);
        final var tableName = "";
        when(spy.getTableName()).thenReturn(tableName);
        final var columnName = "";
        when(spy.getColumnName()).thenReturn(columnName);
        // THEN
        final var columnId = spy.getPseudoColumnId();
        assertThat(columnId.getTableId().getSchemaId().getCatalogId().getTableCat())
                .isEqualTo(spy.getTableCatNonNull());
        assertThat(columnId.getTableId().getSchemaId().getTableSchem())
                .isEqualTo(spy.getTableSchemNonNull());
        assertThat(columnId.getTableId().getTableName())
                .isEqualTo(tableName);
        assertThat(columnId.getColumnName())
                .isEqualTo(columnName);
    }

    @Test
    void getColumnId__() {
        // GIVEN
        final var spy = super.typeSpy();
        // WHEN
        final var tableCat = current().nextBoolean() ? "" : null;
        when(spy.getTableCat()).thenReturn(tableCat);
        final var tableSchem = current().nextBoolean() ? "" : null;
        when(spy.getTableSchem()).thenReturn(tableSchem);
        final var tableName = "";
        when(spy.getTableName()).thenReturn(tableName);
        final var columnName = "";
        when(spy.getColumnName()).thenReturn(columnName);
        // THEN
        final var columnId = spy.getColumnId();
        assertThat(columnId.getTableId().getSchemaId().getCatalogId().getTableCat())
                .isEqualTo(spy.getTableCatNonNull());
        assertThat(columnId.getTableId().getSchemaId().getTableSchem())
                .isEqualTo(spy.getTableSchemNonNull());
        assertThat(columnId.getTableId().getTableName())
                .isEqualTo(tableName);
        assertThat(columnId.getColumnName())
                .isEqualTo(columnName);
    }
}
