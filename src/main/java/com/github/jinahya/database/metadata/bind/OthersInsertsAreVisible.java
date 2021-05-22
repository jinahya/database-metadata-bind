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

import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link java.sql.DatabaseMetaData#othersInsertsAreVisible(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see Context#othersInsertsAreVisible(int)
 */
@XmlRootElement
@NoArgsConstructor
public class OthersInsertsAreVisible
        extends AreVisible {

    private static final long serialVersionUID = 1507862473680389478L;

    /**
     * Invokes {@link Context#othersInsertsAreVisible(int)} method for all types defined in {@link java.sql.ResultSet}
     * and returns bound values.
     *
     * @param context a context.
     * @return a list of bound values.
     * @throws SQLException if a database access error occurs.
     */
    public static List<OthersInsertsAreVisible> getAllInstances(final Context context) throws SQLException {
        requireNonNull(context, "databaseMetaData is null");
        final List<OthersInsertsAreVisible> all = new ArrayList<>();
        for (final ResultSetType type : ResultSetType.values()) {
            all.add(context.othersInsertsAreVisible(type.getRawValue()));
        }
        return all;
    }
}
