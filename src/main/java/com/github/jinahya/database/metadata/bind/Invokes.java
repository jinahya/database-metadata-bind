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

import java.lang.reflect.Field;

import static com.github.jinahya.database.metadata.bind.Utils.field;
import static com.github.jinahya.database.metadata.bind.Utils.wrapper;

/**
 * A utility class for {@link Invocation}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Invokes {

    // -------------------------------------------------------------------------
    static <T> Object[] arguments(final Class<T> klass, final T instance,
                                  final Class<?>[] types,
                                  final String[] literals)
            throws ReflectiveOperationException {
        final Object[] arguments = new Object[literals.length];
        for (int i = 0; i < literals.length; i++) {
            if ("null".equals(literals[i])) {
                arguments[i] = null;
                continue;
            }
            if (literals[i].startsWith(":")) {
                final String name = literals[i].substring(1);
                final Field field = field(klass, name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                arguments[i] = field.get(instance);
                continue;
            }
            if (types[i] == String.class) {
                arguments[i] = literals[i];
                continue;
            }
            if (types[i].isPrimitive()) {
                types[i] = wrapper(types[i]);
            }
            arguments[i] = types[i]
                    .getMethod("valueOf", String.class)
                    .invoke(null, literals[i]);
        }
        return arguments;
    }

    // -------------------------------------------------------------------------
    private Invokes() {
        super();
    }
}
