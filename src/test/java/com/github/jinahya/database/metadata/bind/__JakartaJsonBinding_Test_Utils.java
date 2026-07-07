package com.github.jinahya.database.metadata.bind;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.nio.file.Files;
import java.nio.file.Path;

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

    static void write(final Object object, final Path path) {
        try {
            final var parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            final var config = new JsonbConfig().withFormatting(true);
            try (var jsonb = JsonbBuilder.create(config);
                 var writer = Files.newBufferedWriter(path)) {
                jsonb.toJson(object, writer);
            }
        } catch (final Exception e) {
            throw new RuntimeException("failed to write json to " + path, e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private __JakartaJsonBinding_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
