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


import org.apache.commons.lang3.ClassUtils;


/**
 * A utility class for {@link Invocation}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Invocations {


    static <T> Object[] args(final Class<T> beanClass, final T beanInstance,
                             final Class<?>[] types, final String[] names)
        throws ReflectiveOperationException {

        final Object[] values = new Object[names.length];
        for (int i = 0; i < names.length; i++) {
            if ("null".equals(names[i])) {
                values[i] = null;
                continue;
            }
            if (names[i].startsWith(":")) {
//                final Field field
//                    = Reflections.field(beanClass, names[i].substring(1));
////                if (!field.isAccessible()) {
////                    field.setAccessible(true);
////                }
////                values[i] = field.get(beanInstance);
////                values[i] = Beans.getPropertyValue(
////                    beanClass, names[i].substring(1), beanInstance);
//                values[i] = Values.get(field, beanInstance);
                values[i] = Values.get(names[i].substring(1), beanInstance);
                continue;
            }
            if (types[i] == String.class) {
                values[i] = names[i];
                continue;
            }
            if (types[i].isPrimitive()) {
//                types[i] = Reflections.wrapper(types[i]);
                types[i] = ClassUtils.primitiveToWrapper(types[i]);
            }
            values[i] = types[i]
                .getMethod("valueOf", String.class)
                .invoke(null, names[i]);
        }

        return values;
    }


    private Invocations() {

        super();

    }


}

