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


import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class Annotations {


    static <T extends Annotation> T get(final Class<T> type,
                                        final Field field) {

        return field.getAnnotation(type);
    }


    static <T extends Annotation> T get(final Class<T> type,
                                        final PropertyDescriptor descriptor,
                                        final Class<?> klass) {

        final Method reader = descriptor.getReadMethod();
        if (reader != null) {
            final T value = reader.getAnnotation(type);
            if (value != null) {
                return value;
            }
        }

        final Method writer = descriptor.getReadMethod();
        if (writer != null) {
            final T value = writer.getAnnotation(type);
            if (value != null) {
                return value;
            }
        }

        if (klass != null) {
            final String name = descriptor.getName();
            try {
                final Field field = klass.getDeclaredField(name);
                return get(type, field);
            } catch (final NoSuchFieldException nsfe) {
            }
        }

        return null;
    }


    private Annotations() {

        super();

    }


}

