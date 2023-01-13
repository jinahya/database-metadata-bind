package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.SQLException;
import java.util.Comparator;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getTableTypes()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class TableType
        implements MetadataType {

    private static final long serialVersionUID = -7630634982776331078L;

    public static final Comparator<TableType> COMPARATOR = Comparator.comparing(TableType::getTableType);

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @ColumnLabel("TABLE_TYPE")
    private String tableType;
}
