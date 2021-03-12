package com.github.jinahya.database.metadata.bind;

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
