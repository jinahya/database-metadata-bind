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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A utility class for {@link FieldEnum}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class FieldEnums {

    static <E extends Enum<E> & FieldEnum<E, T>, T> List<T> rawValues(final Class<E> enumType) {
        requireNonNull(enumType, "enumType is null");
        return Arrays.stream(enumType.getEnumConstants())
                .map(FieldEnum::getRawValue)
                .collect(Collectors.toList());
    }

    static <E extends Enum<E> & FieldEnum<E, T>, T> E valueOfRawValue(final Class<E> enumType, final T rawValue) {
        requireNonNull(enumType, "enumType is null");
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> Objects.equals(e.getRawValue(), rawValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown raw value: " + rawValue));
    }

    private FieldEnums() {
        throw new AssertionError("instantiation is not allowed");
    }
}
