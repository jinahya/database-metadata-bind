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


import java.sql.Connection;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public enum ConnectionTransaction {


    TRANSACTION_NONE(Connection.TRANSACTION_NONE), // 0
    TRANSACTION_READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED), // 1
    TRANSACTION_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED), // 2
    TRANSACTION_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);


    public static ConnectionTransaction valueOf(final int constant) {

        for (final ConnectionTransaction value : values()) {
            if (value.constant == constant) {
                return value;
            }
        }

        throw new IllegalArgumentException(
            "no value for constant: " + constant);
    }


    private ConnectionTransaction(final int constant) {

        this.constant = constant;
    }


    private final int constant;


}

