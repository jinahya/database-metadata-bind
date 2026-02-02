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
import jakarta.validation.ValidatorFactory;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

final class __Validation_Test_Utils {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(VALIDATOR_FACTORY::close));
    }

    static void requireValid(final Object object) {
        Objects.requireNonNull(object, "object is null");
        final var violations = VALIDATOR_FACTORY.getValidator().validate(object);
        assertThat(violations)
                .as("violations of %s: %s", object, violations)
                .isEmpty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private __Validation_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
