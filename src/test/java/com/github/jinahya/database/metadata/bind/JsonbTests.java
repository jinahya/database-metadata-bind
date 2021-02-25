package com.github.jinahya.database.metadata.bind;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

final class JsonbTests {

    static Jsonb jsonb() {
        final JsonbConfig config = new JsonbConfig().withFormatting(Boolean.TRUE);
        return JsonbBuilder.create(config);
    }

    static <T> String toJson(final Object value, final Class<T> type) {
        return jsonb().toJson(value, type);
    }

    static String toJson(final Object value) {
        return jsonb().toJson(value);
    }

    static <T> T fromJson(final String json, final Class<T> type) {
        return jsonb().fromJson(json, type);
    }

    static <T> void writeToFile(final T object, final String name) throws IOException {
        final File output = Paths.get("target", name + ".json").toFile();
        try (OutputStream stream = new FileOutputStream(output)) {
            jsonb().toJson(object, stream);
        }
    }

    private JsonbTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
