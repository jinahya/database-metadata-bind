package com.github.jinahya.database.metadata.bind;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

final class JakartaJsonBindingTestUtils {

    static <T> void toJson(final String name, final T object) throws Exception {
        final var encoded = URLEncoder.encode(name, StandardCharsets.US_ASCII);
        final var output = new File(new File(".", "target"), encoded + ".json");
        try (var stream = new FileOutputStream(output);
             var jsonb = JsonbBuilder.create(new JsonbConfig().setProperty(JsonbConfig.FORMATTING, Boolean.TRUE))) {
            jsonb.toJson(object, stream);
            stream.flush();
        }
    }

    private JakartaJsonBindingTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
