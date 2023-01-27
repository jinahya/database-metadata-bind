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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;

/**
 * A class for binding results of {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ChildOf(Table.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class IndexInfo
        extends AbstractMetadataType {

    private static final long serialVersionUID = 924040226611181424L;

    public static final Comparator<IndexInfo> COMPARING_NON_UNIQUE_TYPE_INDEX_NAME_ORDINAL_POSITION
            = Comparator.comparing(IndexInfo::isNonUnique)
            .thenComparingInt(IndexInfo::getType)
            .thenComparing(IndexInfo::getIndexName, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparingInt(IndexInfo::getOrdinalPosition);

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TYPE = "TYPE";

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
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

    @ColumnLabel(COLUMN_LABEL_TYPE)
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
