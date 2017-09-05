/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */
package com.github.jinahya.sql.database.metadata.bind;

import static com.github.jinahya.sql.database.metadata.bind.Utils.findField;
import java.lang.reflect.Field;

/**
 * A utility class for {@link Invocation}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Invocations {

    // -------------------------------------------------------------------------
    static <T> Object[] invocationValues(final Class<T> klass, final T instance,
                                         final Class<?>[] types,
                                         final String[] literals) 
            throws ReflectiveOperationException {
        final Object[] values = new Object[literals.length];
        for (int i = 0; i < literals.length; i++) {
            if ("null".equals(literals[i])) {
                values[i] = null;
                continue;
            }
            if (literals[i].startsWith(":")) {
//                values[i] = Utils.propertyValue(
//                        literals[i].substring(1), instance);
                final Field field = findField(
                        instance.getClass(), literals[i].substring(1));
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                values[i] = field.get(instance);
                continue;
            }
            if (types[i] == String.class) {
                values[i] = literals[i];
                continue;
            }
            if (types[i].isPrimitive()) {
                types[i] = Utils.wrapperClass(types[i]);
            }
            values[i] = types[i]
                    .getMethod("valueOf", String.class)
                    .invoke(null, literals[i]);
        }
        return values;
    }

    // -------------------------------------------------------------------------
    private Invocations() {
        super();
    }
}
