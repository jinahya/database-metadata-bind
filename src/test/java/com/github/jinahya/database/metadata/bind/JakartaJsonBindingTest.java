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

import jakarta.json.bind.JsonbBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the Jakarta JSON Binding (JSON-B) behavior of the binding types.
 * <p>
 * Unlike JAXB, JSON-B serializes a {@link java.util.List} directly to a JSON array, so no wrapper type is needed.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class JakartaJsonBindingTest {

    @Test
    void toJson_singleType_writesObject_andHidesUnknownColumns() throws Exception {
        final var schema = new Schema();
        schema.setTableSchem("PUBLIC");
        schema.setTableCatalog("CAT");
        schema.putUnknownColumn("EXTRA", "x"); // must NOT appear (getUnknownColumns is @JsonbTransient)
        try (var jsonb = JsonbBuilder.create()) {
            final var json = jsonb.toJson(schema);
            assertThat(json)
                    .contains("\"tableSchem\":\"PUBLIC\"")
                    .contains("\"tableCatalog\":\"CAT\"")
                    .doesNotContain("unknownColumns")
                    .doesNotContain("EXTRA");
        }
    }

    @Test
    void toJson_list_serializesDirectlyToArray_noWrapperNeeded() throws Exception {
        final var s1 = new Schema();
        s1.setTableSchem("PUBLIC");
        final var s2 = new Schema();
        s2.setTableSchem("SYS");
        try (var jsonb = JsonbBuilder.create()) {
            final var json = jsonb.toJson(List.of(s1, s2));
            assertThat(json)
                    .startsWith("[")
                    .endsWith("]")
                    .contains("\"tableSchem\":\"PUBLIC\"")
                    .contains("\"tableSchem\":\"SYS\"");
        }
    }

    // NOTE: no fromJson (deserialization) test -- the binding types have package-private constructors by design
    // (they are library outputs, produced from ResultSets, not deserialized from user JSON), which JSON-B cannot
    // instantiate. Serialization is the supported direction.
}
