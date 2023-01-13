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

/**
 * A entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)}
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class SuperTable
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = -302335602056528563L;

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @Override
    public Schema extractParent() {
        return Schema.builder()
                .tableCatalog(getTableCat())
                .tableSchem(getTableSchem())
                .build();
    }

    @NullableBySpecification
    @ColumnLabel("TABLE_CAT")
    private String tableCat;

    @NullableBySpecification
    @ColumnLabel("TABLE_SCHEM")
    private String tableSchem;

    @ColumnLabel("TABLE_NAME")
    private String tableName;

    @ColumnLabel("SUPERTABLE_NAME")
    private String supertableName;
}
