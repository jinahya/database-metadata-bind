package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

class CatalogTest {

    @Test
    void _XMLSchema_() throws JAXBException, IOException {
        final JAXBContext context = JAXBContext.newInstance(Catalog.class);
        JaxbTests.printSchema(context);
    }

    @Test
    void _XML_() {
        final Catalog catalog = new Catalog();
        catalog.setTableCat("tableCat");
    }
}
