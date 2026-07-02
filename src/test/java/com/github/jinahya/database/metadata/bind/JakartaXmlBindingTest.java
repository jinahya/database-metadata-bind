package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the Jakarta XML Binding (JAXB) annotations on the binding types and {@link MetadataTypeWrapper}.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class JakartaXmlBindingTest {

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
                .contains("<schema")
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
                .contains('<' + MetadataTypeWrapper.ROOT_ELEMENT_NAME + '>')
                .contains("<schema>")
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
        // same wrapper class, T = Table this time; child self-names via its own @XmlRootElement
        assertThat(xml)
                .contains('<' + MetadataTypeWrapper.ROOT_ELEMENT_NAME + '>')
                .contains("<table>")
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
}
