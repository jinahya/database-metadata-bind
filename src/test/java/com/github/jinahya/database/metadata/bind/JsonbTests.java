package com.github.jinahya.database.metadata.bind;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

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

    private JsonbTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
