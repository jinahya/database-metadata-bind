package com.github.jinahya.database.metadata.bind;

import jakarta.xml.bind.JAXBContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class JakartaXmlBindingTest {

    @Test
    void context() throws Exception {
        final var context = JAXBContext.newInstance("com.github.jinahya.database.metadata.bind");
    }
}
