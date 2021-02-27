package com.github.jinahya.database.metadata.bind;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LangTest {

    @Test
    void primitiveBoolean() {
        final Object value = true;
        assertThat(value).isInstanceOf(Boolean.class);
        assertThat(Boolean.class.isInstance(value)).isTrue();
    }
}
