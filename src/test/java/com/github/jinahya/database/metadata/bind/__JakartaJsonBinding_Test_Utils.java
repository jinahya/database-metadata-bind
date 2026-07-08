package com.github.jinahya.database.metadata.bind;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2024 Jinahya, Inc.
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

final class __JakartaJsonBinding_Test_Utils {

    static <T extends MetadataType> void write(final List<T> values, final Path path) throws Exception {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(path, "path is null");
        assert path.getParent() == null || Files.isDirectory(path.getParent());
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
             var writer = Files.newBufferedWriter(path)) {
            jsonb.toJson(values, writer);
        }
    }

    static <T extends MetadataType> List<T> read(final Path path) {
        Objects.requireNonNull(path, "path is null");
        // Unsupported: JSON-B cannot instantiate the binding types (their constructors are package-private), and the
        // element type of List<T> is erased at runtime; metadata bindings are write-only for JSON.
        throw new UnsupportedOperationException("reading metadata bindings from JSON is not supported");
    }

    // -----------------------------------------------------------------------------------------------------------------
    private __JakartaJsonBinding_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
