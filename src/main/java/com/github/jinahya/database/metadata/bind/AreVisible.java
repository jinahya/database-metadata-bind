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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * An abstract class for binding the result of {@code DatabaseMetaData#...sAreVisible(int)} method.
 *
 * @see OthersDeletesAreVisible
 * @see OthersInsertsAreVisible
 * @see OthersUpdatesAreVisible
 * @see OwnDeletesAreVisible
 * @see OwnInsertsAreVisible
 * @see OwnUpdatesAreVisible
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class AreVisible
        implements Serializable,
                   MetadataType {

    private static final long serialVersionUID = -4539635096087360299L;

    @Override
    public void retrieveChildren(Context context) throws SQLException {
        // no children.
    }

    /**
     * Returns the ResultSet type of this result.
     *
     * @return the ResultSet type of this result
     */
    public int getType() {
        return type;
    }

    /**
     * Replaces the ResultSet type of this result with specified value.
     *
     * @param type new ResultSet type.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Returns the value of this result.
     *
     * @return the value of this result
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * Replaces the value of this result.
     *
     * @param value the new value.
     */
    public void setValue(Boolean value) {
        this.value = value;
    }

    private int type;

    private Boolean value;
}
