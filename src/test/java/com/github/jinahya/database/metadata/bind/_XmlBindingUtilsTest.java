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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class _XmlBindingUtilsTest {

    private static Stream<Class<?>> targetClassStreamForMarshal() {
        return Stream.of(
                ContentHandler.class,
                File.class,
                Node.class,
                OutputStream.class,
                Result.class,
                Writer.class,
                XMLEventWriter.class,
                XMLStreamWriter.class
        );
    }

    @MethodSource({"targetClassStreamForMarshal"})
    @ParameterizedTest
    void marshalMethod__(final Class<?> targetClass) {
        final var source = Mockito.mock(targetClass);
        final var method = _XmlBindingUtils.marshalMethod(source);
        assertThat(method)
                .isNotNull()
                .satisfies(m -> {
                    assertThat(m.getDeclaringClass())
                            .isSameAs(Marshaller.class);
                })
        ;
    }

    private static Stream<Class<?>> sourceClassStreamForUnmarshal() {
        return Stream.of(
                File.class,
                InputSource.class,
                InputStream.class,
                Node.class,
                Reader.class,
                Source.class,
                URL.class,
                XMLEventReader.class,
                XMLStreamReader.class
        );
    }

    @MethodSource({"sourceClassStreamForUnmarshal"})
    @ParameterizedTest
    void unmarshalMethod__(final Class<?> sourceClass) {
        final var source = Mockito.mock(sourceClass);
        final var method = _XmlBindingUtils.unmarshalMethod(source);
        assertThat(method)
                .isNotNull()
                .satisfies(m -> {
                    assertThat(m.getDeclaringClass())
                            .isSameAs(Unmarshaller.class);
                })
        ;
    }
}
