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


import static java.lang.invoke.MethodHandles.lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ReflectionTest {


    private static final Logger logger = getLogger(lookup().lookupClass());


    private static void method(Integer v) {
    }


    private static void method(int v) {
    }


    @Test(enabled = false)
    public void test() throws NoSuchMethodException {

        final boolean b1 = true;
        final Boolean b2 = true;
        assertTrue(Boolean.class.isInstance(b1));
        assertTrue(Boolean.class.isInstance(b2));

        final int i1 = 1;
        final Integer i2 = 1;
        assertTrue(Integer.class.isInstance(i1));
        assertTrue(Integer.class.isInstance(i2));
        assertTrue(Number.class.isInstance(i1));
        assertTrue(Number.class.isInstance(i2));

        final long l1 = 1L;
        final Long l2 = 1L;
        assertTrue(Long.class.isInstance(l1));
        assertTrue(Long.class.isInstance(l2));
        assertTrue(Number.class.isInstance(l1));
        assertTrue(Number.class.isInstance(l2));

        assertEquals(Integer.TYPE, int.class);

        for (Method method : getClass().getDeclaredMethods()) {
            logger.debug("method: {}", method);
        }

        logger.debug("method with Integer: {}",
                     getClass().getDeclaredMethod("method", Integer.class));
        logger.debug("method with TYPE: {}",
                     getClass().getDeclaredMethod("method", Integer.TYPE));
        logger.debug("method with int: {}",
                     getClass().getDeclaredMethod("method", int.class));
    }


    @Test
    public void listType() throws ReflectiveOperationException {

        final Field field = getClass().getDeclaredField("list");
        final Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            final Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
            final String typeName = elementType.getTypeName();
            logger.debug("typeName: {}", typeName);
        }
    }


    private List<String> list;

}

