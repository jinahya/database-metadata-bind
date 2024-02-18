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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
abstract class AbstractMetadataTypeTest<T extends AbstractMetadataType>
        extends MetadataTypeTest<T> {

    AbstractMetadataTypeTest(final Class<T> typeClass) {
        super(typeClass);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @DisplayName("should override equals")
    @Test
    void shouldHaveEqualsOverridden__() throws ReflectiveOperationException {
        final var method = typeClass.getMethod("equals", Object.class);
        final var declaringClass = method.getDeclaringClass();
        assertThat(declaringClass)
                .as("declaring class of the %1$s", method)
                .isSameAs(typeClass);
    }

    @DisplayName("should override hashCode")
    @Test
    void shouldHaveHashcodeOverridden__() throws ReflectiveOperationException {
        final var method = typeClass.getMethod("hashCode");
        final var declaringClass = method.getDeclaringClass();
        assertThat(declaringClass)
                .as("declaring class of the %1$s", method)
                .isSameAs(typeClass);
    }
}
