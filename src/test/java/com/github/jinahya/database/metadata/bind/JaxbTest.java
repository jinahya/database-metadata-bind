package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests for JAXB.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class JaxbTest {

    private static void storeSchema(final JAXBContext context, final BiFunction<String, String, File> locator)
            throws IOException {
        context.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(final String namespaceUri,
                                       final String suggestedFileName)
                    throws IOException {
                final File file = locator.apply(namespaceUri, suggestedFileName);
                final Result output = new StreamResult(file);
                return output;
            }
        });
    }

    private static void printSchema(final JAXBContext context) throws IOException {
        context.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(final String namespaceUri,
                                       final String suggestedFileName)
                    throws IOException {
                final Result output = new StreamResult(System.out);
                output.setSystemId(suggestedFileName);
                return output;
            }
        });
    }

    @Test
    void JAXBContextNewInstance_DoesNotThrow_PackageName() {
        assertDoesNotThrow(() -> JAXBContext.newInstance(getClass().getPackage().getName()));
    }

    @Disabled
    @Test
    void printSchema() throws JAXBException, IOException {
        final JAXBContext context = JAXBContext.newInstance(JaxbTest.class.getPackage().getName());
        printSchema(context);
    }

    @Test
    void storeSchema() throws JAXBException, IOException {
        final Path schemas = Paths.get("target");
        final JAXBContext context = JAXBContext.newInstance(JaxbTest.class.getPackage().getName());
        storeSchema(context, (namespaceUri, suggestedFileName) -> {
            final Path path = schemas.resolve(suggestedFileName);
            return path.toFile();
        });
    }
}
