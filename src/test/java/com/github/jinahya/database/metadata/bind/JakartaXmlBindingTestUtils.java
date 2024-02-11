package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

final class JakartaXmlBindingTestUtils {

    static <T> void marshal(final String jdbcUrl, final Class<T> classToBeBound, final T jaxbElement) throws Exception {
        final var jaxbContext = JAXBContext.newInstance(classToBeBound);
        final var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final var encoded = URLEncoder.encode(jdbcUrl, StandardCharsets.US_ASCII);
        final var output = new File(new File(".", "target"), encoded + ".xml");
        marshaller.marshal(jaxbElement, output);
    }

    private JakartaXmlBindingTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
