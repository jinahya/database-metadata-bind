package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2022 Jinahya, Inc.
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

import com.github.jinahya.database.metadata.bind.BestRowIdentifier.BestRowIdentifierCategory;
import com.github.jinahya.database.metadata.bind.BestRowIdentifier.CategorizedBestRowIdentifiers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.json.bind.JsonbBuilder;
import javax.xml.bind.JAXBContext;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CategorizedBestRowIdentifiersTest {

    @Test
    void xml() throws Exception {
        for (final var category : BestRowIdentifierCategory._VALUES) {
            final var context = JAXBContext.newInstance(CategorizedBestRowIdentifiers.class);
            final var marshaller = context.createMarshaller();
            final var writer = new StringWriter();
            final var expected = CategorizedBestRowIdentifiers.of(category);
            marshaller.marshal(expected, writer);
            final var xml = writer.toString();
            log.debug("xml: {}", xml);
            final var unmarshaller = context.createUnmarshaller();
            final var actual = (CategorizedBestRowIdentifiers) unmarshaller.unmarshal(new StringReader(xml));
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Test
    void json() throws Exception {
        try (var jsonb = JsonbBuilder.create()) {
            for (final var category : BestRowIdentifierCategory._VALUES) {
                final var expected = CategorizedBestRowIdentifiers.of(category);
                final var json = jsonb.toJson(expected);
                log.debug("json: {}", json);
                final var actual = jsonb.fromJson(json, CategorizedBestRowIdentifiers.class);
                assertThat(actual)
                        .usingRecursiveComparison()
                        .isEqualTo(expected);
            }
        }
    }
}
