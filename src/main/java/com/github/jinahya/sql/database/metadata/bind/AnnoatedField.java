/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

class AnnoatedField<T extends Annotation> {

    private static final Logger logger
            = getLogger(AnnoatedField.class.getName());

    // -------------------------------------------------------------------------
    public AnnoatedField(final Field field, final T annotation) {
        super();
        if (field == null) {
            throw new NullPointerException("field is null");
        }
        if (annotation == null) {
            throw new NullPointerException("annotation is null");
        }
        this.field = field;
        this.annotation = annotation;
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(
                    field.getDeclaringClass());
        } catch (final IntrospectionException ie) {
//            reader = null;
//            writer = null;
        }
    }

    // -------------------------------------------------------------------------
    private final Field field;

    private final T annotation;

//    private final Method reader;
//    private final Method writer;
}
