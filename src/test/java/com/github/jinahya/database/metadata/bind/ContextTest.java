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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A class for testing {@link Metadata} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class ContextTest {

    @Test
    void assertAllMethodsBound() throws ReflectiveOperationException {
        for (final Method method : DatabaseMetaData.class.getMethods()) {
            final int modifier = method.getModifiers();
            if (Modifier.isStatic(modifier)) {
                continue;
            }
            if (method.getDeclaringClass() != DatabaseMetaData.class) {
                continue;
            }
            if (method.getParameterCount() == 0) {
                continue;
            }
            if (ResultSet.class.isAssignableFrom(method.getReturnType())) {
                final Class<?>[] parameterTypes
                        = Arrays.copyOf(method.getParameterTypes(), method.getParameterCount() + 1);
                parameterTypes[parameterTypes.length - 1] = Collection.class;
                final Method bound = Context.class.getMethod(method.getName(), parameterTypes);
                continue;
            }
            final Method bound = Context.class.getMethod(method.getName(), method.getParameterTypes());
        }
    }

    @Test
    void newInstance_NullPointerException_Null() {
        assertThatThrownBy(() -> Context.newInstance(null))
                .isInstanceOf(NullPointerException.class);
    }
}
