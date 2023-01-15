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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * A class for binding result of {@link DatabaseMetaData#deletesAreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#deletesAreDetected(int)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class DeletesAreDetected
        extends AreDetected {

    private static final long serialVersionUID = -2202410650688778511L;

    public static Map<Integer, DeletesAreDetected> getAllValues(final Context context) {
        Objects.requireNonNull(context, "context is null");
        return typeStream()
                .mapToObj(t -> {
                    try {
                        return context.deletesAreDetected(t);
                    } catch (final SQLException sqle) {
                        throw new RuntimeException("failed to get deletesAreDetected(" + t + ") on " + context, sqle);
                    }
                })
                .collect(toMap(AreDetected::getType, Function.identity()));
    }
}
