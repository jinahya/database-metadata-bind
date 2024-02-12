package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

final class JakartaXmlBindingTestUtils {

    static <T> void marshal(final String name, final Object value) throws Exception {
        final var jaxbContext = JAXBContext.newInstance("com.github.jinahya.database.metadata.bind");
        final var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final var encoded = URLEncoder.encode(name, StandardCharsets.US_ASCII);
        final var output = new File(new File(".", "target"), encoded + ".xml");
        marshaller.marshal(value, output);
    }

    private JakartaXmlBindingTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
