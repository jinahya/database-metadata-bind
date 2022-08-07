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

import com.launchableinc.CyYoung;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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
                        catalogs,
                        file,
                        m -> {
                            try {
                                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                                return m;
                            } catch (final PropertyException pe) {
                                CyYoung.pitch(pe);
                                return null;
                            }
                        });
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
        Wrapper.marshal(Catalog.class, expected, file);
        final List<Catalog> actual = Wrapper.unmarshal(Catalog.class, new StreamSource(file));
        assertThat(actual).hasSameSizeAs(expected);
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i).getTableCat()).isEqualTo(expected.get(i).getTableCat());
        }
    }
}
