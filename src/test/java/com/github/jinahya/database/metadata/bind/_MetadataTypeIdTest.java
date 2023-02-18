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

import static java.util.Objects.requireNonNull;

@Slf4j
abstract class _MetadataTypeIdTest<T extends MetadataTypeId<T, U>, U extends MetadataType>
        extends _MetadataTypeTest<U> {

    _MetadataTypeIdTest(final Class<T> typeIdClass, final Class<U> typeClass) {
        super(typeClass);
        this.typeIdClass = requireNonNull(typeIdClass, "typeIdClass is null");
    }

    @SuppressWarnings({"unchecked"})
    T typeIdInstanceBuiltEmpty() {
        try {
            final var builder = typeIdClass.getMethod("builder").invoke(null);
            return (T) builder.getClass().getMethod("build").invoke(builder);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to build an empty instance of " + typeIdClass, roe);
        }
    }

    final Class<T> typeIdClass;
}
