package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2022 Jinahya, Inc.
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

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class WrapperTest {

    private static List<List<Catalog>> catalogsList() {
        return List.of(
                Collections.emptyList(),
                IntStream.rangeClosed(0, ThreadLocalRandom.current().nextInt(8))
                        .mapToObj(i -> Catalog.builder().tableCat(Integer.toString(i)).build())
                        .collect(Collectors.toList())
        );
    }

    @MethodSource({"catalogsList"})
    @ParameterizedTest
    void __File(final List<Catalog> expected) throws Exception {
        final File file = Files.createTempFile(tempDir, null, null).toFile();
        Wrapper.marshal(Catalog.class, expected, file);
        final List<Catalog> actual = Wrapper.unmarshal(Catalog.class, file);
        assertThat(actual)
                .hasSameSizeAs(expected)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        ;
    }

    @TempDir
    private Path tempDir;
}
