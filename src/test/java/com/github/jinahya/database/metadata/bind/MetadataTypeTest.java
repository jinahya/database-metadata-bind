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

import javax.xml.bind.annotation.XmlElement;
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
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
abstract class MetadataTypeTest<T extends MetadataType> {

    MetadataTypeTest(final Class<T> typeClass) {
        this.typeClass = requireNonNull(typeClass, "typeClass is null");
    }

    @Test
    void toString_NonBlank_() {
        assertThat(typeInstance().toString()).isNotBlank();
    }

    @Test
    void equals_Equal_Self() throws ReflectiveOperationException{
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
        for (final Field field : getFieldsWithLabel().keySet()) {
            log.debug("field: {}", field);
            final Class<?> declaringClass = field.getDeclaringClass();
            final BeanInfo beanInfo = Introspector.getBeanInfo(declaringClass);
            final Optional<PropertyDescriptor> propertyDescriptor
                    = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .filter(d -> d.getName().equals(field.getName()))
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
                        writeMethod.invoke(typeInstance(), new Object[]{null});
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    }
                }
            });
        }
    }

    @DisplayName("fields with @Label should also be with @XmlElement(required = true)")
    @Test
    void fieldsWithLabel_ShouldBeAnnotatedWithXmlElementWithRequiredTrue() {
        for (final Field field : getFieldsWithLabel().keySet()) {
            log.debug("field: {}", field);
            assertThat(field.getAnnotation(Label.class)).isNotNull();
            final XmlElement xmlElement = field.getAnnotation(XmlElement.class);
            assertThat(xmlElement).isNotNull();
            assertThat(xmlElement.required()).isTrue();
        }
    }

    @DisplayName("fields with @Unused should also be with @XmlElement(nillable = true)")
    @Test
    void fieldsWithUnused_ShouldBeAnnotatedWithXmlElementWithNillableTrue() {
        for (final Field field : getFieldsWithUnused().keySet()) {
            assertThat(field.getAnnotation(Unused.class)).isNotNull();
            assertThat(field.getAnnotation(XmlElement.class)).isNotNull().satisfies(a -> {
                assertThat(a.nillable()).isTrue();
            });
        }
    }

    @DisplayName("fields with @Unused should not be a primitive type")
    @Test
    void fieldsWithUnused_TypeShouldNotBePrimitive() {
        for (final Field field : getFieldsWithUnused().keySet()) {
            assertThat(field.getAnnotation(Unused.class)).isNotNull();
            assertThat(field.getType().isPrimitive()).isFalse();
        }
    }

    @DisplayName("fields with @MayBeNull should also be with @XmlElement(nillable = true)")
    @Test
    void fieldsWithMayBeNull_ShouldBeAnnotatedWithXmlElementWithNillableTrue() {
        for (final Field field : getFieldsWithMayBeNull().keySet()) {
            log.debug("field : {}", field);
            assertThat(field.getAnnotation(MayBeNull.class)).isNotNull();
            assertThat(field.getAnnotation(XmlElement.class)).isNotNull().satisfies(a -> {
                assertThat(a.nillable()).isTrue();
            });
        }
    }

    @DisplayName("fields with @MayBeNull should not be a primitive type")
    @Test
    void fieldsWithMayBeNull_TypeShouldNotBePrimitive() {
        for (final Field field : getFieldsWithMayBeNull().keySet()) {
            log.debug("field : {}", field);
            assertThat(field.getAnnotation(MayBeNull.class)).isNotNull();
            assertThat(field.getType().isPrimitive()).isFalse();
        }
    }

    @DisplayName("fields with @MayBeNullByVendor should also be with @XmlElement(nillable = true)")
    @Test
    void fieldsWithMayBeNullByVendor_ShouldBeAnnotatedWithXmlElementWithNillableTrue() {
        for (final Field field : getFieldsWithMayBeNullByVendor().keySet()) {
            assertThat(field.getAnnotation(MayBeNullByVendor.class)).isNotNull();
            assertThat(field.getAnnotation(XmlElement.class)).isNotNull().satisfies(a -> {
                assertThat(a.nillable()).isTrue();
            });
        }
    }

    @DisplayName("fields with @MayBeNullByVendor should also be with @XmlElement(nillable = true)")
    @Test
    void fieldsWithMayBeNullByVendor_ShouldBePrimitive_Type() {
        for (final Field field : getFieldsWithMayBeNullByVendor().keySet()) {
            assertThat(field.getAnnotation(MayBeNullByVendor.class)).isNotNull();
            assertThat(field.getType().isPrimitive()).isFalse();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    Map<Field, MayBeNullByVendor> getFieldsWithMayBeNullByVendor() {
        return getFieldsWithLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(MayBeNullByVendor.class) != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getKey().getAnnotation(MayBeNullByVendor.class)));
    }

    Map<Field, MayBeNull> getFieldsWithMayBeNull() {
        return getFieldsWithLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(MayBeNull.class) != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getKey().getAnnotation(MayBeNull.class)));
    }

    Map<Field, Unused> getFieldsWithUnused() {
        return getFieldsWithLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(Unused.class) != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getKey().getAnnotation(Unused.class)));
    }

    Map<Field, Label> getFieldsWithLabel() {
        if (fieldsWithLabel == null) {
            fieldsWithLabel = Collections.unmodifiableMap(Utils.getFieldsAnnotatedWith(typeClass, Label.class));
        }
        return fieldsWithLabel;
    }

    final Class<T> typeClass;

    private Map<Field, Label> fieldsWithLabel;
}
