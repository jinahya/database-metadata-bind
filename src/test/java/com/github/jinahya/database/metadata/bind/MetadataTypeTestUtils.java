package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2024 Jinahya, Inc.
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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.sql.SQLException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatCode;

@Slf4j
final class MetadataTypeTestUtils {

    static <T extends MetadataType> void verifyAccessors(final Class<T> cls, final MetadataType obj) {
        Objects.requireNonNull(cls, "cls is null");
        Objects.requireNonNull(obj, "obj is null");
        final BeanInfo info;
        try {
            info = Introspector.getBeanInfo(cls);
        } catch (final IntrospectionException ie) {
            throw new RuntimeException("failed to get beanInfo from " + cls, ie);
        }
        for (final var descriptor : info.getPropertyDescriptors()) {
            final var reader = descriptor.getReadMethod();
            if (reader == null || !MetadataType.class.isAssignableFrom(reader.getDeclaringClass())) {
                continue;
            }
            if (!reader.canAccess(obj)) {
                reader.setAccessible(true);
            }
            final Object value;
            try {
                value = reader.invoke(obj);
            } catch (final ReflectiveOperationException roe) {
                log.error("failed to invoke {}, on {}", reader, obj, roe);
                continue;
            }
            final var writer = descriptor.getWriteMethod();
            if (writer == null || !MetadataType.class.isAssignableFrom(reader.getDeclaringClass())) {
                continue;
            }
            if (!writer.canAccess(obj)) {
                writer.setAccessible(true);
            }
            try {
                writer.invoke(obj, value);
            } catch (final ReflectiveOperationException roe) {
                log.error("failed to invoke {} with {}, on {}", writer, value, obj);
            }
        }
    }

    private static <T extends MetadataType> void verifyAccessorsHelper(final Class<T> cls, final Object obj) {
        Objects.requireNonNull(cls, "cls is null");
        verifyAccessors(cls, cls.cast(obj));
    }

    static void verify(final MetadataType obj) throws SQLException {
        verifyAccessorsHelper(obj.getClass().asSubclass(MetadataType.class), obj);
//        ValidationTestUtils.requireValid(obj);
        // -------------------------------------------------------------------------------------------------- toString()
        assertThatCode(() -> {
            final var string = obj.toString();
        }).doesNotThrowAnyException();
        // -------------------------------------------------------------------------------------------------- hashCode()
        assertThatCode(() -> {
            final var hashCode = obj.hashCode();
        }).doesNotThrowAnyException();
    }

    private MetadataTypeTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
