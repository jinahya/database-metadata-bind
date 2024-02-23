package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class YesNoEmptyConstantsTest {

    @Test
    void __() {
        assertThat("YES").matches(YesNoEmptyConstants.PATTERN_YES_NO_EMPTY);
        assertThat("NO").matches(YesNoEmptyConstants.PATTERN_YES_NO_EMPTY);
        assertThat("").matches(YesNoEmptyConstants.PATTERN_YES_NO_EMPTY);
    }
}