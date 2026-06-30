package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test verifying that JAXB annotations marshal a binding type and exclude {@code unknownColumns}.
 */
class JakartaBinding_Smoke_Test {

    @Test
    void jaxb_marshalsSchema_toXml() throws Exception {
        final var schema = new Schema();
        schema.setTableSchem("PUBLIC");
        schema.setTableCatalog("CAT");
        schema.putUnknownColumn("EXTRA", "x");
        final var context = JAXBContext.newInstance(Schema.class);
        final var marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final var writer = new StringWriter();
        marshaller.marshal(schema, writer);
        final var xml = writer.toString();
        assertThat(xml)
                .contains("<schema")
                .contains("<tableSchem>PUBLIC</tableSchem>")
                .contains("<tableCatalog>CAT</tableCatalog>")
                .doesNotContain("unknownColumns")
                .doesNotContain("EXTRA");
    }

}
