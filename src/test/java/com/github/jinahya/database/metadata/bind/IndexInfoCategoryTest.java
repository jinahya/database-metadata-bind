package com.github.jinahya.database.metadata.bind;

import com.github.jinahya.database.metadata.bind.IndexInfo.IndexInfoCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.json.bind.JsonbBuilder;
import javax.xml.bind.JAXBContext;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class IndexInfoCategoryTest {

    @Test
    void xml() throws Exception {
        for (final var expected : IndexInfoCategory._VALUES) {
            final var context = JAXBContext.newInstance(IndexInfoCategory.class);
            final var marshaller = context.createMarshaller();
            final var writer = new StringWriter();
            marshaller.marshal(expected, writer);
            final var xml = writer.toString();
            log.debug("xml: {}", xml);
            final var unmarshaller = context.createUnmarshaller();
            final var actual = (IndexInfoCategory) unmarshaller.unmarshal(new StringReader(xml));
            assertThat(actual)
                    .isEqualTo(expected);
        }
    }

    @Test
    void json() throws Exception {
        for (final var expected : IndexInfoCategory._VALUES) {
            final var jsonb = JsonbBuilder.create();
            final var json = jsonb.toJson(expected);
            log.debug("json: {}", json);
            final var actual = jsonb.fromJson(json, IndexInfoCategory.class);
            assertThat(actual)
                    .isEqualTo(expected);
        }
    }
}