package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Catalog_Xml_Test extends AbstractCatalogTest {

    @Disabled
    @Test
    void printSchema() throws JAXBException, IOException {
        final JAXBContext context = JAXBContext.newInstance(Catalog.class);
        JaxbTests.printSchema(context);
    }

    @Disabled
    @Test
    void printDocument() throws JAXBException {
        final Catalog value = new Catalog();
        value.setTableCat("tableCat");
        JaxbTests.printInstance(Catalog.class, value);
    }

    @Test
    void marshalUnmarshalAndCompare() throws JAXBException {
        final Catalog expected = new Catalog();
        expected.setTableCat("tableCat");
        final Node marshalled = JaxbTests.marshal(Catalog.class, expected);
        final Catalog actual = JaxbTests.unmarshal(Catalog.class, marshalled);
        assertThat(actual).isEqualTo(expected);
    }
}
