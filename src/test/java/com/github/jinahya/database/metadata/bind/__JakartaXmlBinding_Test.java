package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.JAXBContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

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
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class __JakartaXmlBinding_Test {

    @Test
    void schema() throws Exception {
        final var path = Path.of("target", "dmb.xsd");
        final var parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        final var context = JAXBContext.newInstance(getClass().getPackageName());
        context.generateSchema(new jakarta.xml.bind.SchemaOutputResolver() {

            @Override
            public Result createOutput(final String namespaceUri, final String suggestedFileName)
                    throws IOException {
                final var result = new StreamResult(path.toFile());
                result.setSystemId(path.toUri().toString());
                return result;
            }
        });
        assertThat(path)
                .exists()
                .isRegularFile();
        assertThat(Files.readString(path))
                .contains("<xs:element name=\"metadataTypes\"")
                .contains("nillable=\"true\"");
    }
}
