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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static org.mockito.Mockito.spy;

@Slf4j
abstract class _MetadataTypeTest<T extends MetadataType> {

    _MetadataTypeTest(final Class<T> typeClass) {
        this.typeClass = requireNonNull(typeClass, "typeClass is null");
    }

    T typeInstance() {
//        try { // just for the coverage
//            final var builder = typeClass.getMethod("builder").invoke(null);
//            final var built = builder.getClass().getMethod("build").invoke(builder);
//        } catch (final ReflectiveOperationException roe) {
//            throw new RuntimeException(roe);
//        }
        try {
            final var constructor = typeClass.getDeclaredConstructor();
            if (!constructor.canAccess(null)) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to find/invoke default constructor", roe);
        }
    }

    T typeSpy() {
        return spy(typeInstance());
    }

    Map<Field, _NullableByVendor> getFieldsWithMayBeNullByVendor() {
        return getFieldsWithColumnLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(_NullableByVendor.class) != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getKey().getAnnotation(_NullableByVendor.class)));
    }

    Map<Field, _NullableBySpecification> getFieldsWithNullableBySpecification() {
        return getFieldsWithColumnLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(_NullableBySpecification.class) != null).collect(
                        Collectors.toMap(Map.Entry::getKey,
                                         e -> e.getKey().getAnnotation(_NullableBySpecification.class)));
    }

    Map<Field, _NotUsedBySpecification> fieldsWithUnusedBySpecification() {
        return getFieldsWithColumnLabel().entrySet().stream()
                .filter(e -> e.getKey().getAnnotation(_NotUsedBySpecification.class) != null).collect(
                        Collectors.toMap(Map.Entry::getKey,
                                         e -> e.getKey().getAnnotation(_NotUsedBySpecification.class)));
    }

    Map<Field, _ColumnLabel> getFieldsWithColumnLabel() {
        if (fieldsWithLabel == null) {
            fieldsWithLabel = unmodifiableMap(Utils.getFieldsAnnotatedWith(typeClass, _ColumnLabel.class));
        }
        return fieldsWithLabel;
    }

    final Class<T> typeClass;

    private Map<Field, _ColumnLabel> fieldsWithLabel;
}
