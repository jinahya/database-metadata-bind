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

@Slf4j
abstract class MetadataTypeTest0<T extends MetadataType> {

    MetadataTypeTest0(final Class<T> typeClass) {
        this.typeClass = requireNonNull(typeClass, "typeClass is null");
    }

    T typeInstance() {
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
            fieldsWithLabel = unmodifiableMap(Utils.getFieldsAnnotatedWith(typeClass, ColumnLabel.class));
        }
        return fieldsWithLabel;
    }

    final Class<T> typeClass;

    private Map<Field, ColumnLabel> fieldsWithLabel;
}
