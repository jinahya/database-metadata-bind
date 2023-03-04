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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.beans.Introspector;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
abstract class MetadataTypeIdTest<T extends MetadataTypeId<T, U>, U extends MetadataType>
        extends _MetadataTypeIdTest<T, U> {

    private static final Map<Long, Class<?>> CLASSES_AND_SERIAL_VERSION_UIDS = new ConcurrentHashMap<>();

    MetadataTypeIdTest(final Class<T> typeIdClass, final Class<U> typeClass) {
        super(typeIdClass, typeClass);
    }

    @Test
    void shouldBeFinal() {
        final var modifiers = typeIdClass.getModifiers();
        assertThat(Modifier.isFinal(modifiers))
                .as("Modifiers.isFinal(%1$s.modifies)", typeIdClass)
                .isTrue();
    }

    @DisplayName("each serialVersionUID should be unique")
    @Test
    void serialVersionUID_Unique_() throws ReflectiveOperationException {
        for (Class<?> c = typeIdClass; MetadataType.class.isAssignableFrom(c); c = c.getSuperclass()) {
            if (CLASSES_AND_SERIAL_VERSION_UIDS.containsValue(c)) {
                continue;
            }
            final var field = c.getDeclaredField("serialVersionUID");
            if (field.getDeclaringClass() != c) {
                log.error("{} is not declaring serialVersionUID", c);
                fail();
                return;
            }
            if (!field.canAccess(null)) {
                field.setAccessible(true);
            }
            final var serialVersionUID = field.getLong(null);
            final var previous = CLASSES_AND_SERIAL_VERSION_UIDS.put(serialVersionUID, c);
            if (previous != null) {
                log.debug("{}#serialVersionUID({}) conflicts with {}", c, serialVersionUID, previous);
                fail();
                return;
            }
        }
    }

    @Test
    void shouldHaveEqualsOverridden__() throws ReflectiveOperationException {
        {
            final var method = typeIdClass.getMethod("equals", Object.class);
            final var declaringClass = method.getDeclaringClass();
            assertThat(declaringClass)
                    .as("declaring class of the %1$s", method)
                    .isSameAs(typeIdClass);
        }
    }

    @Test
    void shouldHaveHashcodeOverridden__() throws ReflectiveOperationException {
        {
            final var method = typeIdClass.getMethod("hashCode");
            final var declaringClass = method.getDeclaringClass();
            assertThat(declaringClass)
                    .as("declaring class of the %1$s", method)
                    .isSameAs(typeIdClass);
        }
    }

    @Nested
    class EqualsTest {

        @Disabled // EqualsAndHashCode.Exclude
        @Test
        void verifyEquals() {
            EqualsVerifier.forClass(typeIdClass)
                    .verify();
        }
    }

    @Disabled
    @Nested
    class HashcodeTest {

        @Test
        void __() {
            final var hashCode = typeIdInstance().hashCode();
        }
    }

    @Disabled
    @Test
    void toString__() throws Exception {
        final var instance = typeIdInstance();
        final var string = instance.toString();
    }

    @Disabled
    @Test
    void equals__() throws Exception {
        final var instance = typeIdInstance();
        assertThat(instance).isEqualTo(typeIdInstance());
    }

    @Disabled
    @Test
    void hashCode__() throws Exception {
        final var instance = typeIdInstance();
        assertThat(instance.hashCode()).hasSameHashCodeAs(typeIdInstance());
    }

    @Disabled
    @Test
    void invokeAccessors() throws Exception {
        final var instance = typeIdInstance();
        final var beanInfo = Introspector.getBeanInfo(typeIdClass);
        for (var descriptor : beanInfo.getPropertyDescriptors()) {
            final var reader = descriptor.getReadMethod();
            final var writer = descriptor.getWriteMethod();
            if (reader != null) {
                final var value = reader.invoke(instance);
                if (writer != null) {
                    writer.invoke(instance, value);
                }
            }
        }
    }
}
