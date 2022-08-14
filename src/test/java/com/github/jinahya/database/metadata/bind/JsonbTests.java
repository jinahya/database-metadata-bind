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

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Collection;

final class JsonbTests {

    static <T> void writeToFile(final Class<? super T> type, final T value, final String name)
            throws Exception {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            final File file = Paths.get("target", name + ".json").toFile();
            try (var output = new FileOutputStream(file)) {
                jsonb.toJson(value, output);
                output.flush();
            }
        }
    }

    static void writeToFile(final Object value, final String name) throws Exception {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            final File file = Paths.get("target", name + ".json").toFile();
            try (var output = new FileOutputStream(file)) {
                jsonb.toJson(value, output);
                output.flush();
            }
        }
    }

    static <T> void writeToFile(final Collection<? extends T> value, final String name) throws Exception {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            final File file = Paths.get("target", name + ".json").toFile();
            try (var output = new FileOutputStream(file)) {
                jsonb.toJson(value, output);
                output.flush();
            }
        }
    }

    private JsonbTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
