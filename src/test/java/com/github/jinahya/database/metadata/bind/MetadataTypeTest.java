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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.text.CaseUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Slf4j
abstract class MetadataTypeTest<T extends MetadataType> {

    MetadataTypeTest(final Class<T> typeClass) {
        super();
        this.typeClass = Objects.requireNonNull(typeClass, "typeClass is null");
    }

    @DisplayName("toString()!blank")
    @Test
    void toString_NotBlank_() {
        // ------------------------------------------------------------------------------------------------------- given
        final var instance = newTypeInstance();
        // -------------------------------------------------------------------------------------------------------- when
        final var string = instance.toString();
        // -------------------------------------------------------------------------------------------------------- then
        assertThat(string).isNotBlank();
    }

    @Test
    void equals__() {
        EqualsVerifier
                .simple()
                .forClass(typeClass)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                .verify();
    }

    @DisplayName("hashCode()I")
    @Test
    void hashCode__() {
        // ------------------------------------------------------------------------------------------------------- given
        final var instance = newTypeInstance();
        // --------------------------------------------------------------------------------------------------- when/then
        assertThatCode(() -> {
            final var hashCode = instance.hashCode();
        }).doesNotThrowAnyException();
    }

    @Nested
    class FieldTest {

        @DisplayName("@ColumnLabel -> has accessors")
        @Test
        void _ShouldHaveAccessors_AnnotatedWithColumnLabel() throws IntrospectionException {
            for (final var field : fieldsAnnotatedWithColumnLabel()) {
                final var declaringClass = field.getDeclaringClass();
                final var info = Introspector.getBeanInfo(declaringClass);
                final var descriptor =
                        stream(info.getPropertyDescriptors())
                                .filter(d -> d.getName().equals(field.getName()))
                                .findAny();
                assertThat(descriptor).isNotEmpty().hasValueSatisfying(d -> {
                    final var reader = d.getReadMethod();
                    assertThat(reader).isNotNull();
                    try {
                        reader.invoke(newTypeInstance());
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    }
                    final var writer = d.getWriteMethod();
                    assertThat(writer)
                            .as("write method of %1$s", descriptor)
                            .isNotNull();
                    if (!field.getType().isPrimitive()) {
                        try {
                            writer.invoke(newTypeInstance(), new Object[] {null});
                        } catch (final ReflectiveOperationException roe) {
                            throw new RuntimeException(roe);
                        }
                    }
                });
            }
        }

        @DisplayName("@_ColumnLabel")
        @Test
        void _TypeIsNotPrimitive_AnnotatedWithColumnLabel() {
            for (final var field : fieldsAnnotatedWithColumnLabel()) {
                assertThat(field.getType())
                        .as("type of %s.%s", typeClass.getSimpleName(), field.getName())
                        .isNotPrimitive();
            }
        }

        @DisplayName("@javax.validation.NotNull")
        @Test
        void __NotNull() {
            for (final var field : fieldsAnnotatedWithColumnLabel()) {
                if (!field.isAnnotationPresent(_NonNullBySpecification.class)) {
                    continue;
                }
                if (field.isAnnotationPresent(_NullableByVendor.class)) {
                    continue;
                }
                assertThat(field.isAnnotationPresent(NotNull.class))
                        .as("%s.%s expected to be annotated with %s", typeClass.getSimpleName(), field.getName(),
                            NotNull.class)
                        .isTrue();
            }
        }

        @DisplayName("@_NullableBySpecification -> @jakarta.annotation.Nullable")
        @Test
        void _ShouldBeAnnotatedWithNullable_AnnotatedWithNullableBySpecification() {
            for (final var field : fieldsAnnotatedWithColumnLabel()) {
                if (!field.isAnnotationPresent(_NullableBySpecification.class)) {
                    continue;
                }
                assertThat(field.getAnnotation(Nullable.class))
                        .as("@%1$s on %2$s.%3$s", Nullable.class, typeClass.getSimpleName(), field.getName())
                        .isNotNull();
            }
        }

        @DisplayName("@_NullableByVendor -> !@jakarta.annotation.Nullable")
        @Test
        void _ShouldBeNotAnnotatedWithNullable_AnnotatedWithNullableByVendor() {
            for (final var field : fieldsAnnotatedWithColumnLabel()) {
                if (!field.isAnnotationPresent(_NullableByVendor.class)) {
                    continue;
                }
                assertThat(field.getAnnotation(Nullable.class))
                        .as("@%1$s on %2$s.%3$s", Nullable.class, typeClass.getSimpleName(), field)
                        .isNull();
            }
        }

        @DisplayName("fieldName = toCamelCase(@ColumnLabel#value)")
        @Test
        void _CamelCasedColumnLabel_() {
            for (final var field : fieldsAnnotatedWithColumnLabel()) {
                final var columnLabel = field.getAnnotation(_ColumnLabel.class);
                assertThat(field.getName())
                        .isEqualTo(CaseUtils.toCamelCase(columnLabel.value(), false, '_'));
            }
        }
    }

    @DisplayName("setXxx(getXxx())")
    @Test
    void accessors() throws Exception {
        final var instance = newTypeInstance();
        final var beanInfo = Introspector.getBeanInfo(typeClass);
        for (final var descriptor : beanInfo.getPropertyDescriptors()) {
            final var reader = descriptor.getReadMethod();
            final var writer = descriptor.getWriteMethod();
            if (reader != null) {
                final var value = reader.invoke(instance);
                if (writer != null) {
                    writer.invoke(instance, value);
                }
            }
            if (writer != null) {
                writer.invoke(instance, (Object) null);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------- typeClass

    T newTypeInstance() {
        try {
            final var constructor = typeClass.getConstructor();
            return constructor.newInstance();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to instantiate " + typeClass, roe);
        }
    }

    T newTypeSpy() {
        return Mockito.spy(newTypeInstance());
    }

    List<Field> fieldsAnnotatedWithColumnLabel() {
        return Stream.of(typeClass.getDeclaredFields()).filter(f -> {
            final var modifier = f.getModifiers();
            if (Modifier.isStatic(modifier)) {
                return false;
            }
            if (!f.isAnnotationPresent(_ColumnLabel.class)) {
                return false;
            }
            return true;
        }).toList();
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> typeClass;
}
