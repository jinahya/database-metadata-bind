/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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


package com.github.jinahya.sql.databasemetadata;


import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.Objects;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
class SuppressionPathBuilder {


    public static SuppressionPathBuilder newInstance(final Field field) {

        Objects.requireNonNull(field, "null field");

        final String className = Introspector.decapitalize(
            field.getDeclaringClass().getSimpleName());

        final String fieldName = field.getName();

        return new SuppressionPathBuilder(className, fieldName);
    }


    /**
     *
     * @param domainName
     * @param className
     * @param fieldName
     */
    private SuppressionPathBuilder(final String className,
                                   final String fieldName) {

        super();

        this.className = Objects.requireNonNull(className);
        this.fieldName = Objects.requireNonNull(fieldName);
    }


    public String getClassName() {

        return className;
    }


    public String getFieldName() {

        return fieldName;
    }


    @Override
    public String toString() {

        return super.toString()
               + "?className=" + className
               + "&fieldName=" + fieldName;
    }


    public String getSuppressionPath() {

        return className + "/" + fieldName;
    }


    private final String className;


    private final String fieldName;


}

