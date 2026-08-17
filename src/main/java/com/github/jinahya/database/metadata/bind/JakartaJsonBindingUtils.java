package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import jakarta.json.bind.config.PropertyVisibilityStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Utilities for Jakarta JSON Binding.
 */
public final class JakartaJsonBindingUtils {

    /**
     * A {@link PropertyVisibilityStrategy} that makes all fields visible and all methods invisible, so JSON-B binds
     * directly to fields. The binding types apply this package-wide through
     * {@link jakarta.json.bind.annotation.JsonbVisibility @JsonbVisibility} declared in {@code package-info}, so an
     * ordinary {@link jakarta.json.bind.JsonbBuilder#create()} instance both serializes and deserializes them correctly
     * despite their private fields, non-public setters, and {@code protected} constructors.
     *
     * @apiNote This type is {@code public} so a Jakarta JSON Binding provider can instantiate it reflectively from the
     * {@code @JsonbVisibility} annotation. Application code normally does not need to reference this type directly.
     */
    public static final class FieldAccessVisibilityStrategy
            implements PropertyVisibilityStrategy {

        @Override
        public boolean isVisible(final Field field) {
            return true;
        }

        @Override
        public boolean isVisible(final Method method) {
            return false;
        }
    }

    private JakartaJsonBindingUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
