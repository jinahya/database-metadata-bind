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

import java.sql.ResultSet;

/**
 * Constants for concurrencies of ResultSets.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
enum RSC {

    CONCUR_READ_ONLY(ResultSet.CONCUR_READ_ONLY),
    CONCUR_UPDATABLE(ResultSet.CONCUR_UPDATABLE);

    public static RSC valueOf(final int concurrency) {
        for (final RSC value : values()) {
            if (value.concurrency == concurrency) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "no value for concurrency: " + concurrency);
    }

    private RSC(final int concurrency) {
        this.concurrency = concurrency;
    }

    private final int concurrency;
}
