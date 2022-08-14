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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static java.util.Objects.requireNonNull;

final class BeanValidationTestUtils {

    static Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    static <T> Set<ConstraintViolation<T>> validate(final T object, final Class<?>... groups) {
        requireNonNull(object, "object is null");
        return validator().validate(object, groups);
    }

    static <T> void requireValid(final T object, final Class<?>... groups) throws ConstraintViolationException {
        requireNonNull(object, "object is null");
        final Set<ConstraintViolation<T>> violations = validator().validate(object, groups);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private BeanValidationTestUtils() {
        super();
    }
}
