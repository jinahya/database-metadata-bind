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
 * Constants for types defined in {@link ResultSet}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public enum ResultSetType
        implements IntFieldEnum<ResultSetType> {

    /**
     * A constant for {@link ResultSet#TYPE_FORWARD_ONLY}({@value java.sql.ResultSet#TYPE_FORWARD_ONLY}).
     */
    TYPE_FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY), // 1003

    /**
     * A constant for {@link ResultSet#TYPE_SCROLL_INSENSITIVE}({@value java.sql.ResultSet#TYPE_SCROLL_INSENSITIVE}).
     */
    TYPE_SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE), // 1004

    /**
     * A constant for {@link ResultSet#TYPE_SCROLL_SENSITIVE}({@value java.sql.ResultSet#TYPE_SCROLL_SENSITIVE}).
     */
    TYPE_SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE); // 1005

    /**
     * Returns the value whose {@code rawValue} matches to specified value.
     *
     * @param rawValue the {@code rawValue} to match.
     * @return a value whose {@code rawValue} matches.
     */
    public static ResultSetType valueOfRawValue(final int rawValue) {
        return IntFieldEnums.valueOfRawValue(ResultSetType.class, rawValue);
    }

    /**
     * Creates a new instance with specified raw value.
     *
     * @param rawValue the raw value of this constant.
     */
    ResultSetType(final int rawValue) {
        this.rawValue = rawValue;
    }

    @Override
    public int rawValueAsInt() {
        return rawValue;
    }

    private final int rawValue;
}
