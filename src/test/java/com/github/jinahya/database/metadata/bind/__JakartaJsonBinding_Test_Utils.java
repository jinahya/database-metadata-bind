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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Test-only file helpers for Jakarta JSON Binding snapshots.
 * <p>
 * JSON-B can serialize metadata lists directly as JSON arrays, so these helpers do not use {@link MetadataTypeWrapper}.
 * Deserialization still needs the concrete element class because {@code List<T>} loses its element type at runtime.
 */
final class __JakartaJsonBinding_Test_Utils {

    /**
     * Writes the specified metadata values to the specified path as a JSON array.
     * <p>
     * No formatting option is applied; the output shape is the provider default.
     *
     * @param values the values to write.
     * @param path   the path to write to.
     * @param <T>    the metadata type.
     * @throws Exception if values cannot be written.
     */
    static <T extends MetadataType> void write(final List<T> values, final Path path) throws Exception {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(path, "path is null");
        try (var jsonb = JsonbBuilder.create();
             var writer = Files.newBufferedWriter(path)) {
            // NOTE: no explicit flush -- Yasson's toJson(Object, Writer) flushes and closes the writer itself; an
            // explicit writer.flush() afterward would throw "Stream closed".
            jsonb.toJson(values, writer);
        }
    }

    /**
     * Reads metadata values of the specified element type from the specified path, which must hold a JSON array.
     * <p>
     * The supplied element type is used to build a runtime {@code List<elementType>} type for JSON-B.
     *
     * @param path        the path to read from.
     * @param elementType the element type of the resulting list.
     * @param <T>         the metadata type.
     * @return a list of {@code elementType} values read from {@code path}.
     * @throws Exception if values cannot be read.
     */
    static <T extends MetadataType> List<T> read(final Path path, final Class<T> elementType) throws Exception {
        Objects.requireNonNull(path, "path is null");
        Objects.requireNonNull(elementType, "elementType is null");
        try (var jsonb = JsonbBuilder.create();
             var reader = Files.newBufferedReader(path)) {
            return jsonb.fromJson(reader, listType(elementType));
        }
    }

    /**
     * Returns a {@link Type} representing {@code List<elementType>}.
     * <p>
     * The element type is required because the runtime type argument of {@code List<T>} is erased; this reifies it so
     * JSON-B deserializes into {@code List<elementType>}.
     *
     * @param elementType the element type.
     * @return a {@link Type} representing {@code List<elementType>}.
     */
    private static Type listType(final Class<?> elementType) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {elementType};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    private __JakartaJsonBinding_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
