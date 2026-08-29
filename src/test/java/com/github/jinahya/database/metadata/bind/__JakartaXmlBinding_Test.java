package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class __JakartaXmlBinding_Test {

    private static final String PACKAGE = "com.github.jinahya.database.metadata.bind";

    private static String marshal(final JAXBContext context, final Object value) throws Exception {
        final var marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final var writer = new StringWriter();
        marshaller.marshal(value, writer);
        return writer.toString();
    }

    @Test
    void marshal_singleType_writesRootAndFields() throws Exception {
        final var schema = new Schema();
        schema.setTableSchem("PUBLIC");
        schema.setTableCatalog("CAT");
        schema.putUnknownColumn("EXTRA", "x"); // must NOT appear (getUnknownColumns is @XmlTransient)
        final var xml = marshal(JAXBContext.newInstance(Schema.class), schema);
        assertThat(xml)
                // root element is namespace-qualified by the package-level @XmlSchema(namespace = ...); fields are not
                .contains("<ns2:schema")
                .contains("<tableSchem>PUBLIC</tableSchem>")
                .contains("<tableCatalog>CAT</tableCatalog>")
                .doesNotContain("unknownColumns")
                .doesNotContain("EXTRA");
    }

    @Test
    void marshal_genericWrapper_writesUnwrappedList() throws Exception {
        final var s1 = new Schema();
        s1.setTableSchem("PUBLIC");
        final var s2 = new Schema();
        s2.setTableSchem("SYS");
        final var wrapper = MetadataTypeWrapper.of(List.of(s1, s2));
        // element types must be known to the context -> bootstrap by package name (jaxb.index)
        final var xml = marshal(JAXBContext.newInstance(PACKAGE), wrapper);
        assertThat(xml)
                // root and nested @XmlRootElement elements are namespace-qualified (ns2:) by @XmlSchema(namespace = ...)
                .contains("<ns2:" + MetadataTypeWrapper.ROOT_ELEMENT_NAME)
                .contains("<ns2:schema")
                .contains("PUBLIC")
                .contains("SYS")
                // "unwrapped": no intermediate <elements> wrapper element
                .doesNotContain("<elements>");
    }

    @Test
    void marshal_genericWrapper_sameClass_differentTypeParameter() throws Exception {
        final var table = new Table();
        table.setTableName("EMP");
        final var xml = marshal(JAXBContext.newInstance(PACKAGE), MetadataTypeWrapper.of(List.of(table)));
        // same wrapper class, T = Table this time; child self-names via its own @XmlRootElement (namespace-qualified)
        assertThat(xml)
                .contains("<ns2:" + MetadataTypeWrapper.ROOT_ELEMENT_NAME)
                .contains("<ns2:table")
                .contains("<tableName>EMP</tableName>");
    }

    @Test
    void jaxbIndex_bootstrapsContextByPackageName() throws Exception {
        // jaxb.index resolves the package-name context without listing every class
        final var context = JAXBContext.newInstance(PACKAGE);
        assertThat(context).isNotNull();
        // an empty Column marshals self-closing (<column/>), so match the start tag only
        assertThat(marshal(context, MetadataTypeWrapper.of(List.of(new Column())))).contains("<column");
    }

    @Test
    void marshal() throws Exception {
        final var path = Path.of("target", "dmb-metadata-types.xml");
        final var parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        final var schema = new Schema();
        schema.setTableSchem("PUBLIC");
        __JakartaXmlBinding_Test_Utils.marshal(List.of(schema), path);
        assertThat(path)
                .exists()
                .isRegularFile();
        assertThat(Files.readString(path))
                .contains("https://github.com/jinahya/database-metadata-bind")
                .contains(":metadataTypes")
                .contains(":schema")
                .contains("<tableSchem>PUBLIC</tableSchem>")
                .doesNotContain("<elements>");
        assertThat(__JakartaXmlBinding_Test_Utils.<Schema>unmarshal(path))
                .singleElement()
                .satisfies(v -> assertThat(v.getTableSchem()).isEqualTo("PUBLIC"));
    }

    @Test
    void schema() throws Exception {
        final var path = Path.of("target", "dmb.xsd");
        final var parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        final var context = JAXBContext.newInstance(PACKAGE);
        context.generateSchema(new jakarta.xml.bind.SchemaOutputResolver() {

            @Override
            public Result createOutput(final String namespaceUri, final String suggestedFileName)
                    throws IOException {
                final var result = new StreamResult(path.toFile());
                result.setSystemId(path.toUri().toString());
                return result;
            }
        });
        assertThat(path)
                .exists()
                .isRegularFile();
        assertThat(Files.readString(path))
                .contains("<xs:element name=\"metadataTypes\"")
                .contains("nillable=\"true\"");
    }
}
