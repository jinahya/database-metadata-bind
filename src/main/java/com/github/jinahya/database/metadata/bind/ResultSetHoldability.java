package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
 * %%
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
 * #L%
 */

import java.sql.ResultSet;

/**
 * Constants for holdabilities defined in {@link ResultSet}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
enum ResultSetHoldability implements IntFieldEnum<ResultSetHoldability> {

    /**
     * Constants for {@link ResultSet#HOLD_CURSORS_OVER_COMMIT}({@value java.sql.ResultSet#HOLD_CURSORS_OVER_COMMIT}).
     */
    HOLD_CURSORS_OVER_COMMIT(ResultSet.HOLD_CURSORS_OVER_COMMIT),

    /**
     * Constants for {@link ResultSet#CLOSE_CURSORS_AT_COMMIT}({@value java.sql.ResultSet#CLOSE_CURSORS_AT_COMMIT}).
     */
    CLOSE_CURSORS_AT_COMMIT(ResultSet.CLOSE_CURSORS_AT_COMMIT);

    /**
     * Returns the value whose {@code rawValue} matches to specified value.
     *
     * @param rawValue the {@code rawValue} to match.
     * @return a value whose {@code rawValue} matches.
     */
    public static ResultSetHoldability valueOfRawValue(final int rawValue) {
        return IntFieldEnums.valueOfRawValue(ResultSetHoldability.class, rawValue);
    }

    ResultSetHoldability(final int rawValue) {
        this.rawValue = rawValue;
    }

    @Override
    public int rawValueAsInt() {
        return rawValue;
    }

    private final int rawValue;
}
