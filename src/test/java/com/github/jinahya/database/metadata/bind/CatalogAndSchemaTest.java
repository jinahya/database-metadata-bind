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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CatalogAndSchemaTest {

    @Test
    void forLookup__nullComponentsBecomeEmpty() {
        final var pair = new CatalogAndSchema(null, null);
        assertThat(pair.catalogForLookup()).isEmpty();
        assertThat(pair.schemaForLookup()).isEmpty();
    }

    @Test
    void forLookup__valuesPassThroughUnescaped() {
        final var pair = new CatalogAndSchema("c_1", "s_1");
        assertThat(pair.catalogForLookup()).isEqualTo("c_1");
        assertThat(pair.schemaForLookup()).isEqualTo("s_1");
    }

    @Test
    void schemaPatternForLookup__nullSchema_EmptyWithoutAskingTheDriver() throws SQLException {
        final var metadata = mock(DatabaseMetaData.class);
        assertThat(new CatalogAndSchema("c", null).schemaPatternForLookup(Context.of(metadata))).isEmpty();
        verifyNoInteractions(metadata);
    }

    @Test
    void schemaPatternForLookup__escapedWithTheDriversEscape() throws SQLException {
        final var metadata = mock(DatabaseMetaData.class);
        when(metadata.getSearchStringEscape()).thenReturn("\\");
        assertThat(new CatalogAndSchema(null, "A_B%").schemaPatternForLookup(Context.of(metadata)))
                .isEqualTo("A\\_B\\%");
    }

    @Test
    void schemaPatternForLookup__nullContext() {
        assertThatThrownBy(() -> new CatalogAndSchema(null, "s").schemaPatternForLookup(null))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {
            "plain    | \\   | plain",
            "A_B      | \\   | A\\_B",
            "50%      | \\   | 50\\%",
            "a\\b     | \\   | a\\\\b",
            "a\\_b    | \\   | a\\\\\\_b",
            "A_B      | !    | A!_B",
            "A_B      | NULL | A_B",
            "A_B      | ''   | A_B",
    })
    void escape(final String value, final String escape, final String expected) {
        assertThat(CatalogAndSchema.escape(value, escape)).isEqualTo(expected);
    }
}
