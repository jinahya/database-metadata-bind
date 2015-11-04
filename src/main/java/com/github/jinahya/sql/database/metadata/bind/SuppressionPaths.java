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


import static java.beans.Introspector.decapitalize;
import java.lang.reflect.Field;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class SuppressionPaths {


    public static String get(final Field field, final Class<?> klass) {

        if (field == null) {
            throw new NullPointerException("null field");
        }

        if (klass == null) {
            throw new NullPointerException("null klass");
        }

        if (!field.getDeclaringClass().isAssignableFrom(klass)) {
            throw new IllegalArgumentException(
                "klass(" + klass
                + ") is not assignable to the specified field(" + field
                + ")'s declaring class(" + field.getDeclaringClass() + ")");
        }

        return decapitalize(klass.getSimpleName()) + "/" + field.getName();
    }


    public static String get(final Field field) {

        return get(field, field.getDeclaringClass());
    }


    private SuppressionPaths() {

        super();
    }


}

