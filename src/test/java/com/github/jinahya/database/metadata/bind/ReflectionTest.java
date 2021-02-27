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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class ReflectionTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    private static void method(Integer v) {
    }

    private static void method(int v) {
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Disabled
    @Test
    void test() throws NoSuchMethodException {

        final boolean b1 = true;
        final Boolean b2 = true;
        assertThat(b1).isInstanceOf(Boolean.class);
        assertThat(b2).isInstanceOf(Boolean.class);

        final int i1 = 1;
        final Integer i2 = 1;
        assertThat(i1).isInstanceOf(Integer.class);
        assertThat(i2).isIn(Integer.class);
        assertThat(i1).isInstanceOf(Number.class);
        assertThat(i2).isInstanceOf(Number.class);

        final long l1 = 1L;
        final Long l2 = 1L;
        assertThat(l1).isInstanceOf(Long.class);
        assertThat(l2).isInstanceOf(Long.class);
        assertThat(l1).isInstanceOf(Number.class);
        assertThat(l2).isInstanceOf(Number.class);

        assertThat(Integer.TYPE).isEqualTo(int.class);

        for (Method method : getClass().getDeclaredMethods()) {
            logger.debug("method: {}", method);
        }

        logger.debug("method with Integer: {}", getClass().getDeclaredMethod("method", Integer.class));
        logger.debug("method with TYPE: {}", getClass().getDeclaredMethod("method", Integer.TYPE));
        logger.debug("method with int: {}", getClass().getDeclaredMethod("method", int.class));
    }

    @Disabled
    @Test
    void listType() throws ReflectiveOperationException {
        final Field field = getClass().getDeclaredField("list");
        final Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            final Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
            final String typeName = elementType.getTypeName();
            logger.debug("typeName: {}", typeName);
        }
    }
}
