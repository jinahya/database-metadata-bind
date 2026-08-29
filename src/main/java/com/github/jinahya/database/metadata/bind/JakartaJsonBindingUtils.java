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
     * {@link jakarta.json.bind.annotation.JsonbVisibility @JsonbVisibility} declared in {@code package-info}.
     * <p>
     * What this exists for, precisely: <strong>deserialization</strong>, and only because the setters are not
     * {@code public}. JSON-B considers only public members by default, so with stock visibility a binding type
     * <em>serializes</em> perfectly well - the getters are public - but <em>deserializing</em> one yields an instance
     * whose every property is left {@code null}, because there is no visible setter to write through. Making the
     * fields visible instead restores the write side.
     * <p>
     * Two things this is <em>not</em> for. It does not enable serialization, which needs no help. And it does not
     * instantiate anything: {@link PropertyVisibilityStrategy} governs fields and methods only, and a provider reaches
     * the {@code protected} no-argument constructor on its own.
     * <p>
     * The failure this prevents is silent. Removing {@code @JsonbVisibility} breaks no compilation and no
     * serialization; it turns every deserialized instance into a well-formed object with nothing in it. That is what
     * {@code fromJson_withDefaultJsonb_repopulatesFields_viaPackageVisibility} pins.
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
