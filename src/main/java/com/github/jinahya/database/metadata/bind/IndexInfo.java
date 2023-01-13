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

import java.sql.DatabaseMetaData;

/**
 * A class for binding results of {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class IndexInfo
        implements MetadataType,
                   ChildOf<Table> {

    private static final long serialVersionUID = -768486884376018474L;

    public static final String COLUMN_NAME_TYPE = "TYPE";

    @NullableBySpecification
    @ColumnLabel("TABLE_CAT")
    private String tableCat;

    @NullableBySpecification
    @ColumnLabel("TABLE_SCHEM")
    private String tableSchem;

    @ColumnLabel("TABLE_NAME")
    private String tableName;

    @ColumnLabel("NON_UNIQUE")
    private boolean nonUnique;

    @NullableBySpecification
    @ColumnLabel("INDEX_QUALIFIER")
    private String indexQualifier;

    @NullableBySpecification
    @ColumnLabel("INDEX_NAME")
    private String indexName;

    @ColumnLabel(COLUMN_NAME_TYPE)
    private int type;

    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @NullableBySpecification
    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    @NullableBySpecification
    @ColumnLabel("ASC_OR_DESC")
    private String ascOrDesc;

    @ColumnLabel("CARDINALITY")
    private long cardinality;

    @ColumnLabel("PAGES")
    private long pages;

    @NullableBySpecification
    @ColumnLabel("FILTER_CONDITION")
    private String filterCondition;
}
