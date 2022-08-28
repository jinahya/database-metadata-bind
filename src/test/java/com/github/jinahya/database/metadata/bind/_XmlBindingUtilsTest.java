package com.github.jinahya.database.metadata.bind;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.testcontainers.shaded.org.apache.commons.io.input.XmlStreamReader;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
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
                XmlStreamReader.class
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