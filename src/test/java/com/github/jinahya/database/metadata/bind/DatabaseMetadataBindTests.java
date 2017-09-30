/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import java.io.File;
import static java.lang.Boolean.TRUE;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.file.Paths;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A utility class for testing.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
final class DatabaseMetadataBindTests {

    private static final Logger logger = getLogger(lookup().lookupClass());

//    // -------------------------------------------------------------------------
//    /**
//     * Marshals given catalogs into {@code target/name.xml}.
//     *
//     * @param catalogs the catalogs to marshal
//     * @param name the name
//     * @throws JAXBException if an XML exception occurs.
//     */
//    static void marshal(final Catalogs catalogs, final String name)
//            throws JAXBException {
//        logger.debug("marshalling {} as {}", catalogs, name);
//        final JAXBContext context = JAXBContext.newInstance(Catalogs.class);
//        final Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(JAXB_FORMATTED_OUTPUT, TRUE);
//        final File output = Paths.get("target", name + ".xml").toFile();
//        logger.info("to {}", output);
//        marshaller.marshal(catalogs, output);
//        logger.info("done");
//    }
//
//    /**
//     * Marshals given list of catalogs into {@code target/name.xml} by wrapping
//     * them within an {@link Catalogs}.
//     *
//     * @param catalogs the list of catalogs to marshal
//     * @param name the name
//     * @throws JAXBException if an XML error occurs.
//     */
//    static void marshal(final List<Catalog> catalogs, final String name)
//            throws JAXBException {
//        DatabaseMetadataBindTests.marshal(Catalogs.of(catalogs), name);
//    }

    // -------------------------------------------------------------------------
    private DatabaseMetadataBindTests() {
        super();
    }
}
