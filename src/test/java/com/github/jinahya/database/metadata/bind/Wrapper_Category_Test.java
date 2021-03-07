package com.github.jinahya.database.metadata.bind;

import com.launchableinc.CyYoung;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Wrapper_Category_Test {

    @Test
    void marshal__(final @TempDir Path tempDir) throws IOException, JAXBException {
        final File file = Files.createTempFile(tempDir, null, null).toFile();
        final List<Catalog> catalogs = new ArrayList<>();
        catalogs.add(new Catalog());
        catalogs.add(new Catalog());
        Wrapper.marshal(Catalog.class,
                        catalogs, "catalogs", file, m -> {
                            try {
                                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                                return m;
                            } catch (final PropertyException pe) {
                                CyYoung.pitch(pe);
                                return null;
                            }
                        }
        );
        try {
            for (final String line : Files.readAllLines(file.toPath())) {
                System.out.println(line);
            }
        } catch (final IOException ioe) {
            CyYoung.pitch(ioe);
        }
    }

    @Test
    void unmarshal__(final @TempDir Path tempDir) throws IOException, JAXBException {
        final File file = Files.createTempFile(tempDir, null, null).toFile();
        final List<Catalog> expected = new ArrayList<>();
        expected.add(new Catalog());
        expected.add(new Catalog());
        Wrapper.marshal(Catalog.class, expected, "catalogs", file);
        final List<Catalog> actual = Wrapper.unmarshal(Catalog.class, new StreamSource(file));
        assertThat(actual).isEqualTo(expected);
    }
}