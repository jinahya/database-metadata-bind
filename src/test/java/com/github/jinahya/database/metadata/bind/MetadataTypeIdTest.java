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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
abstract class MetadataTypeIdTest<T extends MetadataTypeId> {

    MetadataTypeIdTest(final Class<T> typeClass) {
        this.typeClass = requireNonNull(typeClass, "typeClass is null");
    }

    @Test
    void shouldBeFinal() {
        final int modifiers = typeClass.getModifiers();
        assertThat(Modifier.isFinal(modifiers))
                .as("final modifier of (%1$s)", typeClass)
                .isTrue();
    }

    T typeInstance() {
        try {
            final Constructor<T> constructor = typeClass.getDeclaredConstructor();
            if (!constructor.canAccess(this)) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    final Class<T> typeClass;
}
