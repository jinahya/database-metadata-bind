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

import jakarta.validation.Validation;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

final class ValidationTestUtils {

    static void requireValid(final Object obj) {
        Objects.requireNonNull(obj, "obj is null");
        try (final var factory = Validation.buildDefaultValidatorFactory()) {
            final var violations = factory.getValidator().validate(obj);
            assertThat(violations)
                    .as("violations of %s: %s", obj, violations)
                    .isEmpty();
        }
    }

    private ValidationTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
