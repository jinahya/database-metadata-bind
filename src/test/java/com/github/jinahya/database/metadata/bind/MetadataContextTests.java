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
import java.io.FileOutputStream;
import java.io.OutputStream;
import static java.lang.invoke.MethodHandles.lookup;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public final class MetadataContextTests {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    static List<Catalog> catalogs(final MetadataContext context) throws Exception {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        final List<Catalog> catalogs = context.getCatalogs();
        if (catalogs.isEmpty()) {
            logger.debug("adding an empty catalog");
            catalogs.add(Catalog.newVirtualInstance(context));
        }
        // catalog/schema
        for (final Catalog catalog : catalogs) {
            if (catalog.getSchemas().isEmpty()) {
                logger.debug("adding an empty schema");
                catalog.getSchemas().add(Schema.newVirtualInstance(context, catalog.getTableCat()));
            }
        }
//        // catalog/schema/crossReferences
//        for (final Catalog catalog : catalogs) {
//            for (final Schema schema : catalog.getSchemas()) {
//                schema.getCrossReferences().addAll(
//                        context.getCrossReferences(schema.getTables()));
//            }
//        }
//        // catalog/crossReferences
//        for (final Catalog catalog : catalogs) {
//            catalog.getCrossReferences().addAll(
//                    context.getCrossReferences(catalog.getTables()));
//        }
        return catalogs;
    }

    static void marshal(final Catalog catalog, final String prefix) throws Exception {
        if (catalog == null) {
            throw new NullPointerException("catalog is null");
        }
        if (prefix == null) {
            throw new NullPointerException("prefix is null");
        }
        final JAXBContext context = JAXBContext.newInstance(Catalog.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        String name = catalog.getTableCat();
        if (Catalog.TABLE_CAT_NONE.equals(name)) {
            name = "virtual";
        }
        final File file = new File("target", prefix + "." + name + ".xml");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            marshaller.marshal(catalog, outputStream);
            outputStream.flush();
        }
    }

    static void store(final Schema schema, final String prefix) throws Exception {
        final JAXBContext context = JAXBContext.newInstance(Schema.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        String name = schema.getTableSchem();
        logger.debug("schema.tableSchem: {}", name);
        if (Schema.TABLE_SCHEM_NONE.equals(name)) {
            name = "virtual";
        }
        final File file = new File("target", prefix + "." + name + ".xml");
        if (file.isFile()) {
            logger.warn("file already exist: {}", file);
        }
        try (OutputStream output = new FileOutputStream(file)) {
            marshaller.marshal(schema, output);
            output.flush();
        }
    }

    // -------------------------------------------------------------------------
    private MetadataContextTests() {
        super();
    }
}
