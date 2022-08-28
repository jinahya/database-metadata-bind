package com.github.jinahya.database.metadata.bind;

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