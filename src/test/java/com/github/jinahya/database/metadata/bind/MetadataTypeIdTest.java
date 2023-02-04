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
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
abstract class MetadataTypeIdTest<T extends MetadataTypeId<U>, U extends MetadataType>
        extends MetadataTypeIdTest0<T, U> {

    MetadataTypeIdTest(final Class<T> typeIdClass, final Class<U> typeClass) {
        super(typeIdClass, typeClass);
    }

    @Test
    void shouldBeFinal() {
        final int modifiers = typeIdClass.getModifiers();
        assertThat(Modifier.isFinal(modifiers))
                .as("final modifier of (%1$s)", typeIdClass)
                .isTrue();
    }

    @Nested
    class EqualsTest {

        @Test
        void verifyEquals() {
            EqualsVerifier.forClass(typeIdClass).verify();
        }
    }

    @Nested
    class HashcodeTest {

        @Test
        void __() {
            final var hashCode = typeIdInstanceBuiltEmpty().hashCode();
        }
    }
}
