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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

final class Context_DirectMetadataMapping_Test_Utils {

    static <T> List<T> unsupportedAsEmpty(final Query<T> query) throws SQLException {
        try {
            return query.get();
        } catch (final SQLFeatureNotSupportedException ignored) {
            return List.of();
        }
    }

    static <T> void assertDirect(final String name, final Query<T> query, final Iteration<T> iteration)
            throws SQLException {
        final List<T> values;
        try {
            values = query.get();
        } catch (final SQLFeatureNotSupportedException ignored) {
            return;
        }
        final var accepted = new ArrayList<T>();
        try {
            iteration.accept(accepted::add);
        } catch (final SQLFeatureNotSupportedException ignored) {
            throw new AssertionError(name + " query is supported, but iteration is not supported", ignored);
        }
        assertThat(accepted)
                .as(name)
                .doesNotContainNull()
                .extracting(Context_DirectMetadataMapping_Test_Utils::snapshot)
                .containsExactlyElementsOf(values.stream()
                                                   .map(Context_DirectMetadataMapping_Test_Utils::snapshot)
                                                   .toList());
    }

    private static List<String> snapshot(final Object value) {
        return Arrays.stream(value.getClass().getMethods())
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .filter(m -> m.getParameterCount() == 0)
                .filter(m -> m.getDeclaringClass() != Object.class)
                .filter(m -> m.getName().startsWith("get") || m.getName().startsWith("is"))
                .sorted(Comparator.comparing(m -> m.getName()))
                .map(m -> m.getName() + "=" + invoke(m, value))
                .toList();
    }

    private static Object invoke(final java.lang.reflect.Method method, final Object target) {
        try {
            return method.invoke(target);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError("failed to invoke " + method + " on " + target, e);
        }
    }

    @FunctionalInterface
    interface Query<T> {

        List<T> get() throws SQLException;
    }

    @FunctionalInterface
    interface Iteration<T> {

        void accept(Consumer<? super T> consumer) throws SQLException;
    }

    private Context_DirectMetadataMapping_Test_Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
