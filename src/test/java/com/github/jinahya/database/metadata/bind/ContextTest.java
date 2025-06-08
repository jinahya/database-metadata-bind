package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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

import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link Context} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class ContextTest {

    @DisplayName("...(...)ResultSet")
    @Test
    void assertAllMethodsBound() throws ReflectiveOperationException {
        for (final var method : DatabaseMetaData.class.getMethods()) {
            final int modifiers = method.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                continue;
            }
            if (method.getDeclaringClass() != DatabaseMetaData.class) {
                continue;
            }
            if (method.getParameterCount() == 0) {
                continue;
            }
            if (!ResultSet.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }
            log.debug("method: {}", method);
            final var name = method.getName();
            {
                final var found = Context.class.getMethod(name, method.getParameterTypes());
                assertThat(found.getModifiers()).satisfies(m -> {
                    assertThat(Modifier.isStatic(m)).isFalse();
                    assertThat(Modifier.isPublic(m)).isTrue();
                });
                assertThat(found.getReturnType()).isEqualTo(List.class);
            }
        }
    }

    @Disabled("field enums shall be removed")
    @Test
    void _ShouldBeDefinedAsEnum_AllStaticFieldsDefinedInDatabaseMetaDataClass()
            throws IOException, IllegalAccessException {
        final var fieldValues = new HashMap<Field, Object>();
        for (final var field : DatabaseMetaData.class.getFields()) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                continue;
            }
            if (!Modifier.isFinal(modifiers)) {
                continue;
            }
            if (field.getName().startsWith("sqlState")) {
                continue;
            }
            fieldValues.put(field, field.get(null));
        }
//        log.debug("fieldValues: {}", fieldValues);
        ClassPath.from(getClass().getClassLoader()).getAllClasses()
                .stream()
                .filter(ci -> {
                    return !"module-info".equals(ci.getName());
                })
                .filter(ci -> {
                    return ci.getName().startsWith(getClass().getPackageName());
                })
                .map(ClassPath.ClassInfo::load)
                .filter(Class::isEnum)
                .filter(c -> !c.getName().equals("module-info"))
                .forEach(c -> {
                    log.debug("enum class: {}", c);
                    for (final var constant : c.getEnumConstants()) {
//                        log.debug("constant: {}", constant);
                        final var name = ((Enum<?>) constant).name().replaceAll("_", "");
                        for (final var i = fieldValues.keySet().iterator(); i.hasNext(); ) {
                            final var key = i.next().getName();
                            if (key.equalsIgnoreCase(name)) {
//                                log.debug("key: {}", key);
                                i.remove();
                            }
                        }
                    }
                    if (c.isInstance(_IntFieldEnum.class)) {
                        for (final var constant : c.getEnumConstants()) {
                            log.debug("constant: {}", constant);
                        }
                    }
                    if (c.isInstance(_FieldEnum.class)) {
                        for (final var constant : c.getEnumConstants()) {
                            log.debug("constant: {}", constant);
                        }
                    }
                });
        fieldValues.forEach((f, v) -> {
            log.warn("field: {}, value: {}", f.getName(), v);
        });
        assertThat(fieldValues).isEmpty();
    }
}
