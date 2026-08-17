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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link MetadataType#getValue(Class, String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class MetadataType_GetValue_Test {

    private static Table newTable() {
        final var table = new Table();
        table.setTableCat("cat");
        return table;
    }

    // ------------------------------------------------------------------------------------------------- mapped  fields
    @DisplayName("getValue(String.class, \"TABLE_CAT\") -> the value of the mapped field")
    @Test
    void getValue__MappedField() {
        assertThat(newTable().getValue(String.class, Table.COLUMN_LABEL_TABLE_CAT)).hasValue("cat");
    }

    @DisplayName("getValue(String.class, \"table_cat\") -> the value of the mapped field")
    @Test
    void getValue__MappedFieldMatchedCaseInsensitively() {
        final var table = newTable();
        for (final var label : new String[] {"table_cat", "Table_Cat", "tAbLe_CaT"}) {
            assertThat(table.getValue(String.class, label))
                    .as("value of %1$s", label)
                    .hasValue("cat");
        }
    }

    @DisplayName("a mapped field holding null -> empty, without falling through to unknown columns")
    @Test
    void getValue__EmptyAndNoFallThrough__WhenMappedFieldIsNull() {
        final var table = new Table();
        assertThat(table.getValue(String.class, Table.COLUMN_LABEL_TABLE_CAT)).isEmpty();
        table.putUnknownColumn(Table.COLUMN_LABEL_TABLE_CAT, "shadowed");
        assertThat(table.getValue(String.class, Table.COLUMN_LABEL_TABLE_CAT)).isEmpty();
    }

    // ------------------------------------------------------------------------------------------------ unknown columns
    @DisplayName("an unknown column is found under the label the binder stored it with")
    @Test
    void getValue__UnknownColumn() {
        final var table = new Table();
        table.putUnknownColumn("VENDOR_EXTENSION", "value");
        assertThat(table.getValue(String.class, "VENDOR_EXTENSION")).hasValue("value");
    }

    @DisplayName("an unknown column is matched case-insensitively, as a mapped field is")
    @Test
    void getValue__UnknownColumnMatchedCaseInsensitively() {
        final var table = new Table();
        table.putUnknownColumn("VENDOR_EXTENSION", "value");
        for (final var label : new String[] {"vendor_extension", "Vendor_Extension"}) {
            assertThat(table.getValue(String.class, label))
                    .as("value of %1$s", label)
                    .hasValue("value");
        }
    }

    @DisplayName("neither a mapped field nor an unknown column -> empty")
    @Test
    void getValue__Empty__WhenUnmapped() {
        assertThat(newTable().getValue(String.class, "NO_SUCH_COLUMN")).isEmpty();
    }
}
