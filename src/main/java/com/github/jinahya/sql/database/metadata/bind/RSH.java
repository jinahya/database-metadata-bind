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
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * Constants for holdabilites of ResultSets.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
enum RSH {

    HOLD_CURSORS_OVER_COMMIT(ResultSet.HOLD_CURSORS_OVER_COMMIT),
    CLOSE_CURSORS_AT_COMMIT(ResultSet.CLOSE_CURSORS_AT_COMMIT);

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(RSH.class.getName());

    // -------------------------------------------------------------------------
    public static RSH valueOf(final int holdability) {
        for (final RSH value : values()) {
            if (value.holdability == holdability) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "no value for holdability: " + holdability);
    }

    // -------------------------------------------------------------------------
    private RSH(final int holdability) {
        this.holdability = holdability;
    }

    // -------------------------------------------------------------------------
    private final int holdability;
}
