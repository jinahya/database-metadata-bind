package com.github.jinahya.database.metadata.bind;

import com.github.jinahya.database.metadata.bind.IndexInfo.CategorizedIndexInfo;
import com.github.jinahya.database.metadata.bind.IndexInfo.IndexInfoCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.json.bind.JsonbBuilder;
import javax.xml.bind.JAXBContext;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CategorizedIndexInfoTest {

    @Test
    void xml() throws Exception {
        for (final var category : IndexInfoCategory._VALUES) {
            final var context = JAXBContext.newInstance(CategorizedIndexInfo.class);
            final var marshaller = context.createMarshaller();
            final var writer = new StringWriter();
            final var expected = CategorizedIndexInfo.of(category);
            marshaller.marshal(expected, writer);
            final var xml = writer.toString();
            log.debug("xml: {}", xml);
            final var unmarshaller = context.createUnmarshaller();
            final var actual = (CategorizedIndexInfo) unmarshaller.unmarshal(new StringReader(xml));
            assertThat(actual)
                    .isEqualTo(expected);
        }
    }

    @Test
    void json() throws Exception {
        try (var jsonb = JsonbBuilder.create()) {
            for (final var category : IndexInfoCategory._VALUES) {
                final var expected = CategorizedIndexInfo.of(category);
                final var json = jsonb.toJson(expected);
                log.debug("json: {}", json);
                final var actual = jsonb.fromJson(json, CategorizedIndexInfo.class);
                assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(expected);
            }
        }
    }
}