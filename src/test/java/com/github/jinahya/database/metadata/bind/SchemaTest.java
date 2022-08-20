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

class SchemaTest extends MetadataTypeTest<Schema> {

    SchemaTest() {
        super(Schema.class);
    }

    @Test
    void newVirtualInstance__() {
        final var actual = Schema.newVirtualInstance();
        assertThat(actual)
                .isNotNull()
                .satisfies(v -> {
                    assertThat(v.getVirtual())
                            .as("a new virtual instance's value of isVirtual()")
                            .isTrue();
                    assertThat(v.getTableCatalog())
                            .isEqualTo(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
                    assertThat(v.getTableSchem())
                            .isEqualTo(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
                })
        ;
    }
}
