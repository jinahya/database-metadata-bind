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

import lombok.extern.slf4j.Slf4j;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serial;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
final class _JsonBindingTestUtils {

    static <T> void test(final Class<T> type, final List<? extends T> expected) throws Exception {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(expected, "expected is null");
        try (var jsonb = JsonbBuilder.create()) {
            final var json = jsonb.toJson(expected);
            final List<T> actual = jsonb.fromJson(json, new ArrayList<T>() {
            }.getClass().getGenericSuperclass());
            assertThat(actual)
                    .hasSameSizeAs(expected)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    static <T> void test(final Context context, final String name, Class<T> type, final List<? extends T> expected)
            throws Exception {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(name, "name is null");
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(expected, "expected is null");
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(Boolean.TRUE))) {
            final var path = Paths.get("target", TestUtils.getFilenamePrefix(context) + " - " + name + ".json");
            try (OutputStream stream = new FileOutputStream(path.toFile())) {
                jsonb.toJson(expected, stream);
                stream.flush();
            }
            try (InputStream stream = new FileInputStream(path.toFile())) {
                final List<T> actual = jsonb.fromJson(stream, new ArrayList<T>() {
                    @Serial
                    private static final long serialVersionUID = -2131648764287495589L;
                }.getClass().getGenericSuperclass());
                for (T t : actual) {
                    log.debug("a: {}", t);
                }
                assertThat(actual)
                        .hasSameSizeAs(expected)
                        .isEqualTo(expected);
            }
        }
    }

    static <T> void serializeAndDeserialize(final Class<T> type, final T expected,
                                            final BiConsumer<? super T, ? super T> consumer)
            throws Exception {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(expected, "expected is null");
        Objects.requireNonNull(consumer, "consumer is null");
        final T actual;
        try (var jsonb = JsonbBuilder.create()) {
            final var json = jsonb.toJson(expected);
            actual = jsonb.fromJson(json, type);
        }
        consumer.accept(expected, actual);
    }

    static <T, C extends Collection<? extends T>> void serializeAndDeserialize(
            final Class<T> type, final C collection, final BiConsumer<? super T, ? super T> consumer)
            throws Exception {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(collection, "collection is null");
        Objects.requireNonNull(consumer, "consumer is null");
        for (final T element : collection) {
            serializeAndDeserialize(type, element, consumer);
        }
    }

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

    static <T> void writeToFile(final Collection<? extends T> value, final String prefix) throws Exception {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            final File file = Paths.get("target", prefix + ".json").toFile();
            try (var output = new FileOutputStream(file)) {
                jsonb.toJson(value, output);
                output.flush();
            }
        }
    }

    static <T> void writeToFile(final Collection<? extends T> value, final Path target) throws Exception {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            final File file = target.toFile();
            try (var output = new FileOutputStream(file)) {
                jsonb.toJson(value, output);
                output.flush();
            }
        }
    }

    static <T> void writeToFile(final Context context, final String postfix, final Collection<? extends T> value)
            throws Exception {
        final var path = Paths.get("target", TestUtils.getFilenamePrefix(context) + " - " + postfix + ".json");
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            try (var output = new FileOutputStream(path.toFile())) {
                jsonb.toJson(value, output);
                output.flush();
            }
        }
    }

    private _JsonBindingTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
