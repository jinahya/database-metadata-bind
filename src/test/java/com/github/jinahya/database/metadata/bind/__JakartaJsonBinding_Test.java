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
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests Jakarta JSON Binding behavior of the binding types.
 * <p>
 * Unlike JAXB, JSON-B serializes a {@link java.util.List} directly to a JSON array, so no wrapper type is needed.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class __JakartaJsonBinding_Test {

    private static Stream<Class<? extends MetadataType>> metadataTypes() {
        return Stream.of(
                Attribute.class,
                BestRowIdentifier.class,
                Catalog.class,
                ClientInfoProperty.class,
                Column.class,
                ColumnPrivilege.class,
                CrossReference.class,
                ExportedKey.class,
                Function.class,
                FunctionColumn.class,
                ImportedKey.class,
                IndexInfo.class,
                PrimaryKey.class,
                Procedure.class,
                ProcedureColumn.class,
                PseudoColumn.class,
                Schema.class,
                SuperTable.class,
                SuperType.class,
                Table.class,
                TablePrivilege.class,
                TableType.class,
                TypeInfo.class,
                UDT.class,
                VersionColumn.class
        );
    }

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

    // ----------------------------------------------------------------------------------------------------- round-trip
    // A plain JsonbBuilder.create() round-trips the binding types: package-level @JsonbVisibility makes JSON-B bind
    // directly to fields, so it reads/writes the private fields reflectively (setAccessible) and instantiates via the
    // protected constructor (spec-mandated: public OR protected) -- no public setters or custom config needed. The
    // List<T> case remains unsupported because its element type is erased at runtime.

    @Test
    void roundTrip_schema_repopulatesFields_viaFieldAccess() throws Exception {
        final var expected = new Schema();
        expected.setTableSchem("PUBLIC");
        expected.setTableCatalog("CAT");
        expected.putUnknownColumn("EXTRA", "x"); // must NOT survive the round-trip
        try (var jsonb = JsonbBuilder.create()) {
            final var json = jsonb.toJson(expected);
            assertThat(json)
                    .doesNotContain("unknownColumns")
                    .doesNotContain("EXTRA");
            final var actual = jsonb.fromJson(json, Schema.class);
            assertThat(actual.getTableSchem()).isEqualTo("PUBLIC");
            assertThat(actual.getTableCatalog()).isEqualTo("CAT");
            assertThat(actual.getUnknownColumns()).isEmpty();
        }
    }

    @Test
    void roundTrip_schema_preservesNullNillableField() throws Exception {
        final var expected = new Schema();
        expected.setTableSchem("PUBLIC");
        expected.setTableCatalog(null); // @JsonbNillable -> serialized as null, read back as null
        try (var jsonb = JsonbBuilder.create()) {
            final var actual = jsonb.fromJson(jsonb.toJson(expected), Schema.class);
            assertThat(actual.getTableSchem()).isEqualTo("PUBLIC");
            assertThat(actual.getTableCatalog()).isNull();
        }
    }

    @ParameterizedTest
    @MethodSource("metadataTypes")
    void fromJson_instantiatesMetadataType_viaProtectedConstructor(final Class<? extends MetadataType> type)
            throws Exception {
        try (var jsonb = JsonbBuilder.create()) {
            assertThat(jsonb.fromJson("{}", type))
                    .isInstanceOf(type);
        }
    }

    // Pins the key guarantee: a DEFAULT Jsonb (no custom config) deserializes into populated fields, thanks to the
    // package-level @JsonbVisibility field-access strategy. If that annotation were removed, this would regress to
    // silently-empty objects and fail here.
    @Test
    void fromJson_withDefaultJsonb_repopulatesFields_viaPackageVisibility() throws Exception {
        final var json = "{\"tableSchem\":\"PUBLIC\",\"tableCatalog\":\"CAT\"}";
        try (var jsonb = JsonbBuilder.create()) {
            final var actual = jsonb.fromJson(json, Schema.class);
            assertThat(actual.getTableSchem()).isEqualTo("PUBLIC");
            assertThat(actual.getTableCatalog()).isEqualTo("CAT");
        }
    }

    // __JakartaJsonBinding_Test_Utils.write/read round-trips a List<T> through a file; read() takes the element type to defeat
    // List<T> erasure.
    @Test
    void write_read_roundTripsList(@TempDir final Path dir) throws Exception {
        final var s1 = new Schema();
        s1.setTableSchem("PUBLIC");
        final var s2 = new Schema();
        s2.setTableSchem("SYS");
        final var path = dir.resolve("schemas.json");
        __JakartaJsonBinding_Test_Utils.write(List.of(s1, s2), path);
        final var actual = __JakartaJsonBinding_Test_Utils.read(path, Schema.class);
        assertThat(actual)
                .hasSize(2)
                .extracting(Schema::getTableSchem)
                .containsExactly("PUBLIC", "SYS");
    }
}
