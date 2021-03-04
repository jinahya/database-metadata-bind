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
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;

/**
 * A class for binding result of {@link DatabaseMetaData#ownUpdatesAreVisible(int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
public class OwnUpdatesAreVisible extends AreVisible {

    static List<OwnUpdatesAreVisible> list(final DatabaseMetaData databaseMetaData) throws SQLException {
        requireNonNull(databaseMetaData, "databaseMetaData is null");
        final List<OwnUpdatesAreVisible> list = list(OwnUpdatesAreVisible.class);
        for (final OwnUpdatesAreVisible v : list) {
            try {
                v.value = databaseMetaData.ownUpdatesAreVisible(v.type);
            } catch (final SQLFeatureNotSupportedException sqlfnse) {
                logger.log(Level.WARNING, "sql feature not supported", sqlfnse);
            }
        }
        return list;
    }
}
