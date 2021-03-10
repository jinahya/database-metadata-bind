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

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#insertsAreDetected(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class DeletesAreDetected extends AreDetected<DeletesAreDetected> {

    static List<DeletesAreDetected> all(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final List<DeletesAreDetected> result = new ArrayList<>();
        for (final ResultSetType type : ResultSetType.values()) {
            try {
                result.add(context.deletesAreDetected(type));
            } catch (final SQLException sqle) {
                logger.log(Level.WARNING, sqle,
                           () -> String.format("failed to invoke deletesAreDetected(%1$d)", type.getRawValue()));
                context.throwIfNotSuppressed(sqle);
            }
        }
        return result;
    }

    /**
     * Creates a new instance.
     */
    public DeletesAreDetected() {
        super();
    }
}