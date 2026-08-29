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

import com.google.common.reflect.ClassPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards the navigation-API surface frozen by <a
 * href="https://github.com/jinahya/database-metadata-bind/issues/45">#45</a> (P-036).
 * <p>
 * Three decisions are pinned here, because each is invisible at the call site once it is wrong:
 * <ul>
 *   <li>the {@link Table}-scoped {@code *Of} methods are {@code public} - the published navigation API;</li>
 *   <li>every other {@code *Of} scope ({@link Catalog}, {@link Schema}, {@link UDT}, {@link Procedure},
 *       {@link Function}) is deliberately <em>held back</em>, since driver disagreement over catalog/schema support
 *       bites hardest there;</li>
 *   <li>the {@code get*Ref()} helpers stay package-private, pending the equals/hashCode identity decision.</li>
 * </ul>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class Context_NavigationApiSurface_Test {

    /**
     * Returns the {@code *Of} navigation methods declared by {@link Context}.
     *
     * @return a stream of navigation methods.
     */
    private static Stream<Method> navigationMethods() {
        return Stream.of(Context.class.getDeclaredMethods())
                .filter(m -> m.getName().endsWith("Of"))
                .filter(m -> m.getParameterCount() > 0)
                .filter(m -> MetadataType.class.isAssignableFrom(m.getParameterTypes()[0]));
    }

    @DisplayName("every Table-scoped *Of method is public")
    @Test
    void tableScopedOfMethods_ArePublic() {
        final var offenders = navigationMethods()
                .filter(m -> m.getParameterTypes()[0] == Table.class)
                .filter(m -> !Modifier.isPublic(m.getModifiers()))
                .map(Method::toString)
                .sorted()
                .toList();
        assertThat(offenders)
                .as("Table-scoped *Of methods that are not public")
                .isEmpty();
        // and the set is non-empty, so the filter above cannot pass vacuously
        assertThat(navigationMethods().filter(m -> m.getParameterTypes()[0] == Table.class))
                .as("Table-scoped *Of methods")
                .hasSize(24);
    }

    @DisplayName("*Of methods scoped to any other parent are still held back")
    @Test
    void nonTableScopedOfMethods_AreNotPublic() {
        final var heldBack = List.of(Catalog.class, Schema.class, UDT.class, Procedure.class, Function.class);
        final var offenders = navigationMethods()
                .filter(m -> heldBack.contains(m.getParameterTypes()[0]))
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::toString)
                .sorted()
                .toList();
        assertThat(offenders)
                .as("*Of methods exposed ahead of the #45 sequence")
                .isEmpty();
        assertThat(navigationMethods().filter(m -> heldBack.contains(m.getParameterTypes()[0])))
                .as("held-back *Of methods")
                .hasSize(38);
    }

    @DisplayName("get*Ref() helpers remain package-private")
    @Test
    void refHelpers_ArePackagePrivate() throws IOException {
        final var refHelpers = ClassPath.from(getClass().getClassLoader())
                .getAllClasses()
                .stream()
                .filter(ci -> ci.getPackageName().equals(getClass().getPackageName()))
                .filter(ci -> !ci.getName().equals("module-info"))
                .map(ClassPath.ClassInfo::load)
                .filter(MetadataType.class::isAssignableFrom)
                .flatMap(c -> Stream.of(c.getDeclaredMethods()))
                .filter(m -> m.getName().startsWith("get") && m.getName().endsWith("Ref"))
                .filter(m -> m.getParameterCount() == 0)
                .toList();
        assertThat(refHelpers)
                .as("get*Ref() helpers")
                .hasSize(30);
        assertThat(refHelpers.stream()
                           .filter(m -> Modifier.isPublic(m.getModifiers())
                                        || Modifier.isProtected(m.getModifiers()))
                           .map(Method::toString)
                           .sorted()
                           .toList())
                .as("get*Ref() helpers wider than package-private")
                .isEmpty();
    }
}
