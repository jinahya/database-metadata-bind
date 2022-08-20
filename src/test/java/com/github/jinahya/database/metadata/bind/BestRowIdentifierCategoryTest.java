package com.github.jinahya.database.metadata.bind;

import com.github.jinahya.database.metadata.bind.BestRowIdentifier.BestRowIdentifierCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.json.bind.JsonbBuilder;
import javax.xml.bind.JAXBContext;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BestRowIdentifierCategoryTest {

    @Test
    void xml() throws Exception {
        for (final var expected : BestRowIdentifierCategory._VALUES) {
            final var context = JAXBContext.newInstance(BestRowIdentifierCategory.class);
            final var marshaller = context.createMarshaller();
            final var writer = new StringWriter();
            marshaller.marshal(expected, writer);
            final var xml = writer.toString();
            log.debug("xml: {}", xml);
            final var unmarshaller = context.createUnmarshaller();
            final var actual = (BestRowIdentifierCategory) unmarshaller.unmarshal(new StringReader(xml));
            assertThat(actual)
                    .isEqualTo(expected);
        }
    }

    @Test
    void json() throws Exception {
        try (var jsonb = JsonbBuilder.create()) {
            for (final var expected : BestRowIdentifierCategory._VALUES) {
                final var json = jsonb.toJson(expected);
                log.debug("json: {}", json);
                final var actual = jsonb.fromJson(json, BestRowIdentifierCategory.class);
                assertThat(actual)
                        .isEqualTo(expected);
            }
        }
    }
}