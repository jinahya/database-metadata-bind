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
import org.apache.commons.text.CaseUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
abstract class MetadataTypeTest<T extends MetadataType> extends MetadataTypeTest0<T> {

    private static final Map<Long, Class<?>> SERIAL_VERSION_UIDS = new ConcurrentHashMap<>();

    MetadataTypeTest(final Class<T> typeClass) {
        super(typeClass);
    }

    @DisplayName("Each serialVersionUID should be unique")
    @Test
    void serialVersionUID_Unique_() throws ReflectiveOperationException {
        for (Class<?> c = typeClass; MetadataType.class.isAssignableFrom(c); c = c.getSuperclass()) {
            if (SERIAL_VERSION_UIDS.containsValue(c)) {
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
            final var previous = SERIAL_VERSION_UIDS.put(serialVersionUID, c);
            if (previous != null) {
                log.debug("{}#serialVersionUID({}) conflicts with {}", c, serialVersionUID, previous);
                fail();
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
        final var method = typeClass.getMethod("equals", Object.class);
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
        for (final var field : getFieldsWithColumnLabel().keySet()) {
            final var declaringClass = field.getDeclaringClass();
            final var beanInfo = Introspector.getBeanInfo(declaringClass);
            final var propertyDescriptor =
                    stream(beanInfo.getPropertyDescriptors()).filter(d -> d.getName().equals(field.getName()))
                            .findAny();
            assertThat(propertyDescriptor).isNotEmpty().hasValueSatisfying(d -> {
                final var readMethod = d.getReadMethod();
                assertThat(readMethod).isNotNull();
                try {
                    readMethod.invoke(typeInstance());
                } catch (final ReflectiveOperationException roe) {
                    throw new RuntimeException(roe);
                }
                final var writeMethod = d.getWriteMethod();
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
        for (final var field : fieldsWithUnusedBySpecification().keySet()) {
            assert field.isAnnotationPresent(NotUsedBySpecification.class);
            assertThat(field.getType().isPrimitive())
                    .as("@NotUsedBySpecification on primitive field: %s", field)
                    .isFalse();
        }
    }

    @DisplayName("@NullableBySpecification -> !primitive")
    @Test
    void fields_NotPrimitive_NullableBySpecification() {
        for (final var field : getFieldsWithNullableBySpecification().keySet()) {
            assert field.isAnnotationPresent(NullableBySpecification.class);
            assertThat(field.getType().isPrimitive())
                    .as("@NullableBySpecification on primitive field: %s", field)
                    .isFalse();
        }
    }

    @DisplayName("fields with @MayBeNullByVendor should also be with @XmlElement(nillable = true)")
    @Test
    void fieldsWithMayBeNullByVendor_ShouldBePrimitive_Type() {
        for (final var field : getFieldsWithMayBeNullByVendor().keySet()) {
            assertThat(field.getAnnotation(NullableByVendor.class)).isNotNull();
            assertThat(field.getType().isPrimitive()).isFalse();
        }
    }

    @DisplayName("COLUMN_LABEL <> fieldName")
    @Test
    void columnLabelWithFieldName() {
        getFieldsWithColumnLabel().forEach((f, l) -> {
            final var camelCase = CaseUtils.toCamelCase(l.value(), false, '_');
            assertThat(f.getName())
                    .as("expected name of %1$s", f)
                    .isEqualTo(camelCase);
        });
    }
}
