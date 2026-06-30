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
import org.jspecify.annotations.Nullable;
import org.junit.platform.commons.support.ReflectionSupport;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;

@Slf4j
final class MetadataType_Test_Utils {

    private static String capitalize(final String string) {
        if (Objects.requireNonNull(string, "string is null").isBlank()) {
            throw new IllegalArgumentException("blank string");
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @Nullable
    static Method getterMethod(final Class<?> clazz, final String name, final Class<?> type) {
        Objects.requireNonNull(clazz, "clazz is null");
        Objects.requireNonNull(name, "name is null");
        Objects.requireNonNull(type, "type is null");
        assert !type.isPrimitive() : "type should not be primitive: " + type;
        final var capitalized = capitalize(name);
        return ReflectionSupport.findMethod(clazz, "get" + capitalized).orElse(null);
    }

    @Nullable
    static Method getterMethod(final Field field) {
        Objects.requireNonNull(field, "field is null");
        assert !field.getType().isPrimitive() : "field type should not be primitive: " + field;
        return getterMethod(field.getDeclaringClass(), field.getName(), field.getType());
    }

    @Nullable
    static Method setterMethod(final Class<?> clazz, final String name, final Class<?> type) {
        Objects.requireNonNull(clazz, "clazz is null");
        Objects.requireNonNull(name, "name is null");
        Objects.requireNonNull(type, "type is null");
        assert !type.isPrimitive() : "type should not be primitive: " + type;
        final var capitalized = capitalize(name);
        return ReflectionSupport.findMethod(clazz, "set" + capitalized, type).orElse(null);
    }

    @Nullable
    static Method setterMethod(final Field field) {
        Objects.requireNonNull(field, "field is null");
        assert !field.getType().isPrimitive() : "field type should not be primitive: " + field;
        return setterMethod(field.getDeclaringClass(), field.getName(), field.getType());
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <T extends MetadataType> void verifyAccessors(final Class<T> cls, final T obj) {
        Objects.requireNonNull(cls, "cls is null");
        Objects.requireNonNull(obj, "obj is null");
        final BeanInfo info;
        try {
            info = Introspector.getBeanInfo(cls);
        } catch (final IntrospectionException ie) {
            throw new RuntimeException("failed to get beanInfo from " + cls, ie);
        }
        for (final var descriptor : info.getPropertyDescriptors()) {
            final var name = descriptor.getName();
            final var type = descriptor.getPropertyType();
            // read value via getter
            final var getter =
                    Optional.ofNullable(descriptor.getReadMethod())
                            .orElseGet(() -> getterMethod(cls, name, type));
            if (getter == null) {
                continue;
            }
            final var value = ReflectionSupport.invokeMethod(getter, obj);
            // write value via setter
            Optional.ofNullable(descriptor.getWriteMethod())
                    .or(() -> Optional.ofNullable(setterMethod(cls, name, type)))
                    .ifPresent(m -> ReflectionSupport.invokeMethod(m, obj, value));
        }
    }

    private static <T extends MetadataType> void verifyAccessorsHelper(final Class<T> cls, final Object obj) {
        Objects.requireNonNull(cls, "cls is null");
        verifyAccessors(cls, cls.cast(obj));
    }

    static void verify(final MetadataType obj) {
        verifyAccessorsHelper(obj.getClass().asSubclass(MetadataType.class), obj);
        // toString()
        assertThatCode(obj::toString).doesNotThrowAnyException();
        // hashCode()
        assertThatCode(obj::hashCode).doesNotThrowAnyException();
        // accessors
        verifyAccessorsHelper(obj.getClass(), obj);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private MetadataType_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
