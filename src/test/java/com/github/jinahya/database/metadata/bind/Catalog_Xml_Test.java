package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Catalog_Xml_Test {

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
