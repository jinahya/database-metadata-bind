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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
abstract class MetadataTypeTest<T extends MetadataType> {

    private static final Map<Long, Class<?>> SERIAL_VERSION_UIDS = new ConcurrentHashMap<>();

    MetadataTypeTest(final Class<T> typeClass) {
        this.typeClass = requireNonNull(typeClass, "typeClass is null");
    }

    @Test
    void serialVersionUID_Unique_() throws ReflectiveOperationException {
        for (Class<?> c = typeClass; MetadataType.class.isAssignableFrom(c); c = c.getSuperclass()) {
            if (SERIAL_VERSION_UIDS.containsValue(c)) {
                continue;
            }
            final Field field = c.getDeclaredField("serialVersionUID");
            if (field.getDeclaringClass() != c) {
                log.error("{} is not declaring serialVersionUID", c);
                Assertions.fail();
                return;
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            final long serialVersionUID = field.getLong(null);
            final Class<?> previous = SERIAL_VERSION_UIDS.put(serialVersionUID, c);
            if (previous != null) {
                log.debug("{}#serialVersionUID({}) conflicts with {}", c, serialVersionUID, previous);
                Assertions.fail();
                return;
            }
        }
    }

    @Test
    void toString_NonBlank_() {
        assertThat(typeInstance().toString()).isNotBlank();
    }

    @Test
    void equals_Equal_Self() throws ReflectiveOperationException {
        final Method method = typeClass.getMethod("equals", Object.class);
        if (method.getDeclaringClass() == Object.class) {
            return;
        }
        final T obj1 = typeInstance();
        final T obj2 = typeInstance();
        assertThat(obj1).isEqualTo(obj2);
        assertThat(obj2).isEqualTo(obj1);
    }

    @Test
    void hashCode__() {
        assertDoesNotThrow(() -> typeInstance().hashCode());
    }

    @Test
    void fieldsWithLabel_Exist_Accessors() throws IntrospectionException {
        for (final Field field : getFieldsWithColumnLabel().keySet()) {
            final Class<?> declaringClass = field.getDeclaringClass();
            final BeanInfo beanInfo = Introspector.getBeanInfo(declaringClass);
            final Optional<PropertyDescriptor> propertyDescriptor =
                    Arrays.stream(beanInfo.getPropertyDescriptors()).filter(d -> d.getName().equals(field.getName()))
                            .findAny();
            assertThat(propertyDescriptor).isNotEmpty().hasValueSatisfying(d -> {
                final Method readMethod = d.getReadMethod();
                assertThat(readMethod).isNotNull();
                try {
                    readMethod.invoke(typeInstance());
                } catch (final ReflectiveOperationException roe) {
                    throw new RuntimeException(roe);
                }
                final Method writeMethod = d.getWriteMethod();
                assertThat(writeMethod).isNotNull();
                if (!field.getType().isPrimitive()) {
                    try {
                        writeMethod.invoke(typeInstance(), new Object[] {null});
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    }
                }
            });
        }
    }

    @DisplayName("fields with @Unused should not be a primitive type")
    @Test
    void fieldsWithUnused_TypeShouldNotBePrimitive() {
        for (final Field field : fieldsWithUnusedBySpecification().keySet()) {
            assertThat(field.getAnnotation(NotUsedBySpecification.class)).isNotNull();
            assertThat(field.getType().isPrimitive()).isFalse();
        }
    }

    @DisplayName("@NullableBySpecification -> !primitive")
    @Test
    void fields_NotPrimitive_NullableBySpecification() {
        for (final Field field : getFieldsWithNullableBySpecification().keySet()) {
            assertThat(field.getAnnotation(NullableBySpecification.class)).isNotNull();
            assertThat(field.getType().isPrimitive())
                    .as("@NullableBySpecification on primitive field: %s", field)
                    .isFalse();
        }
    }

    @DisplayName("fields with @MayBeNullByVendor should also be with @XmlElement(nillable = true)")
    @Test
    void fieldsWithMayBeNullByVendor_ShouldBePrimitive_Type() {
        for (final Field field : getFieldsWithMayBeNullByVendor().keySet()) {
            assertThat(field.getAnnotation(NullableByVendor.class)).isNotNull();
            assertThat(field.getType().isPrimitive()).isFalse();
        }
    }

    T typeInstance() {
        try {
            final Constructor<T> constructor = typeClass.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    Map<Field, NullableByVendor> getFieldsWithMayBeNullByVendor() {
        return getFieldsWithColumnLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(NullableByVendor.class) != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getKey().getAnnotation(NullableByVendor.class)));
    }

    Map<Field, NullableBySpecification> getFieldsWithNullableBySpecification() {
        return getFieldsWithColumnLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(NullableBySpecification.class) != null).collect(
                        Collectors.toMap(Map.Entry::getKey,
                                         e -> e.getKey().getAnnotation(NullableBySpecification.class)));
    }

    Map<Field, NotUsedBySpecification> fieldsWithUnusedBySpecification() {
        return getFieldsWithColumnLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(NotUsedBySpecification.class) != null).collect(
                        Collectors.toMap(Map.Entry::getKey,
                                         e -> e.getKey().getAnnotation(NotUsedBySpecification.class)));
    }

    Map<Field, ColumnLabel> getFieldsWithColumnLabel() {
        if (fieldsWithLabel == null) {
            fieldsWithLabel = Collections.unmodifiableMap(Utils.getFieldsAnnotatedWith(typeClass, ColumnLabel.class));
        }
        return fieldsWithLabel;
    }

    final Class<T> typeClass;

    private Map<Field, ColumnLabel> fieldsWithLabel;
}
