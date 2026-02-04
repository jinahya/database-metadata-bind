package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class MetadataTypeConstantsTest {

    @Nested
    class PATTERN_REGEXP_YES_OR_NO {

        @ValueSource(strings = {
                MetadataTypeConstants.YES,
                MetadataTypeConstants.NO
        })
        @ParameterizedTest
        void __1(final String value) {
            final var pattern = Pattern.compile(MetadataTypeConstants.PATTERN_REGEXP_YES_OR_NO);
            assertThat(value).matches(pattern);
        }

        @Test
        void __1() {
            final var pattern = Pattern.compile(MetadataTypeConstants.PATTERN_REGEXP_YES_OR_NO);
            assertThat(MetadataTypeConstants.EMPTY).doesNotMatch(pattern);
        }
    }

    @Nested
    class PATTERN_REGEXP_YES_NO_OR_EMPTY {

        @ValueSource(strings = {
                MetadataTypeConstants.YES,
                MetadataTypeConstants.NO,
                MetadataTypeConstants.EMPTY,
        })
        @ParameterizedTest
        void __(final String value) {
            final var pattern = Pattern.compile(MetadataTypeConstants.PATTERN_REGEXP_YES_NO_OR_EMPTY);
            assertThat(value).matches(pattern);
        }
    }
}
