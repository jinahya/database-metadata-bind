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

import javax.validation.constraints.NotNull;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * A utility class for XML bindings.
 *
 * @param <T> the type of elements to wrap
 */
// http://blog.bdoughan.com/2012/11/creating-generic-list-wrapper-in-jaxb.html
// http://blog.bdoughan.com/2010/12/jaxb-and-immutable-objects.html
final class _XmlBindingUtils {

    private static final Map<Class<?>, Method> UNMARSHAL_METHODS = new WeakHashMap<>();

//    @NotNull
//    static Method unmarshalMethod(@NotNull final Class<?> sourceClass) {
//        Objects.requireNonNull(sourceClass, "sourceClass is null");
//        return UNMARSHAL_METHODS.computeIfAbsent(
//                sourceClass,
//                c -> Arrays.stream(Unmarshaller.class.getMethods())
//                        .filter(m -> "unmarshal".equals(m.getName()))
//                        .filter(m -> {
//                            final int modifiers = m.getModifiers();
//                            return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
//                        })
//                        .filter(m -> {
//                            final Class<?>[] parameterTypes = m.getParameterTypes();
//                            return parameterTypes.length >= 1
////                                   && parameterTypes.length <= 2
//                                   && parameterTypes[0].isAssignableFrom(c);
//                        })
//                        .findFirst()
//                        .orElseThrow(() -> new IllegalArgumentException("no 'unmarshal' method; sourceClass: " + sourceClass))
//        );
//    }

    @NotNull
    static Method unmarshalMethod(@NotNull final Object source) {
        Objects.requireNonNull(source, "source is null");
        return UNMARSHAL_METHODS.computeIfAbsent(
                source.getClass(),
                c -> Arrays.stream(Unmarshaller.class.getMethods())
                        .filter(m -> "unmarshal".equals(m.getName()))
                        .filter(m -> {
                            final int modifiers = m.getModifiers();
                            return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
                        })
                        .filter(m -> {
                            final Class<?>[] parameterTypes = m.getParameterTypes();
                            return parameterTypes.length >= 1
//                                   && parameterTypes.length <= 2
                                   && parameterTypes[0].isAssignableFrom(c);
                        })
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("no 'unmarshal' method; source: " + source))
        );
    }

    private static final Map<Class<?>, Method> MARSHAL_METHODS = new WeakHashMap<>();

    @NotNull
    static Method marshalMethod(@NotNull final Object target) {
        Objects.requireNonNull(target, "target is null");
        return MARSHAL_METHODS.computeIfAbsent(
                target.getClass(),
                c -> Arrays.stream(Marshaller.class.getMethods())
                        .filter(m -> "marshal".equals(m.getName()))
                        .filter(m -> {
                            final int modifiers = m.getModifiers();
                            return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
                        })
                        .filter(m -> {
                            final Class<?>[] parameterTypes = m.getParameterTypes();
                            return parameterTypes.length == 2
                                   && parameterTypes[0] == Object.class
                                   && parameterTypes[1].isAssignableFrom(c);
                        })
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("no 'marshal' method; target: " + target))
        );
    }

    private _XmlBindingUtils() {
        super();
    }
}
