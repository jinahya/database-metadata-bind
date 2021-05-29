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
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

/**
 * Test utilities for JAXB functionalities.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
final class JaxbTests {

    static QName qName(final String localPart) {
        return new QName(XmlConstants.NS_URI_DATABASE_METADATA_BIND, localPart);
    }

    static <T extends MetadataType> JAXBElement<T> jaxbElement(final String localPart, final Class<T> declaredType,
                                                               final T value) {
        return new JAXBElement<>(qName(localPart), declaredType, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <T> void writeToFile(final Class<? super T> type, final T value, final String name)
            throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(type);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final File output = Paths.get("target", name + ".xml").toFile();
        marshaller.marshal(value, output);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void printSchema(final JAXBContext context) throws IOException {
        requireNonNull(context, "context is null");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        context.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(final String namespaceUri, final String suggestedFileName) throws IOException {
                return new StreamResult(baos) {
                    @Override
                    public String getSystemId() {
                        return "noid";
                    }
                };
            }
        });
        System.out.println(baos.toString());
    }

    static <T> void printInstance(final Class<T> type, final T value) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(type);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(value, baos);
        System.out.print(baos.toString());
    }

    static <T> Node marshal(final Class<T> type, final T value) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(requireNonNull(type, "type is null"));
        final Marshaller marshaller = context.createMarshaller();
        final DOMResult result = new DOMResult();
        marshaller.marshal(value, result);
        return result.getNode();
    }

    static <T> T unmarshal(final Class<T> type, final Node node) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(requireNonNull(type, "type is null"));
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(node, type).getValue();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JaxbTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
