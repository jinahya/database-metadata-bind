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


package com.github.jinahya.sql;


import java.sql.ResultSet;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public enum ResultSetType {


    TYPE_FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),
    TYPE_SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),
    TYPE_SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);


    public static ResultSetType valueOf(final int constant) {

        for (final ResultSetType value : values()) {
            if (value.constant == constant) {
                return value;
            }
        }

        throw new IllegalArgumentException(
            "no value for constant: " + constant);
    }


    private ResultSetType(final int constant) {

        this.constant = constant;
    }


    private final int constant;


}

