package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link Context} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class ContextTest {

    /**
     * Verifies that all {@link DatabaseMetaData} methods returning {@link ResultSet} have corresponding
     * {@code public} methods in {@link Context} with identical signatures that return {@link List}.
     *
     * @throws ReflectiveOperationException if a reflective operation fails.
     */
    @DisplayName("all ...(...)ResultSet method bound")
    @Test
    void assertAllMethodsBound() throws ReflectiveOperationException {
        for (final var method : DatabaseMetaData.class.getMethods()) {
            final int modifiers = method.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                continue;
            }
            if (method.getDeclaringClass() != DatabaseMetaData.class) {
                continue;
            }
            if (!ResultSet.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }
            log.debug("method: {}", method);
            final var name = method.getName();
            {
                final var found = Context.class.getMethod(name, method.getParameterTypes());
                assertThat(found.getModifiers()).satisfies(m -> {
                    assertThat(Modifier.isStatic(m)).isFalse();
                });
                assertThat(found.getReturnType()).isEqualTo(List.class);
            }
        }
    }
}
