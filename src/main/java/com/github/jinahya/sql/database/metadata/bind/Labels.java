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


/**
 * A utility class for {@link Label}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Labels {


    /**
     * Finds {@link Label} value from specified property.
     *
     * @param propertyDescriptor property descriptor
     * @param beanClass bean instance
     *
     * @return an instance of {@link Label} or {@code null} if not found.
     */
    static Label get(final PropertyDescriptor propertyDescriptor,
                     final Class<?> beanClass) {

        return Annotations.getAnnotation(
            Label.class, propertyDescriptor, beanClass);
    }


    private Labels() {

        super();

    }


}

