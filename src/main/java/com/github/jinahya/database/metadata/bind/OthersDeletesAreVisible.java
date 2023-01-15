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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#othersDeletesAreVisible(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#othersDeletesAreVisible(int)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class OthersDeletesAreVisible
        extends AreVisible {

    private static final long serialVersionUID = -758432214659840058L;

    public static List<OthersDeletesAreVisible> getAllValues(final Context context) {
        Objects.requireNonNull(context, "context is null");
        return typeStream()
                .mapToObj(t -> {
                    try {
                        return context.othersDeletesAreVisible(t);
                    } catch (final SQLException sqle) {
                        throw new RuntimeException("failed to get othersDeletesAreVisible(" + t + ")", sqle);
                    }
                })
                .collect(Collectors.toList());
    }
}
