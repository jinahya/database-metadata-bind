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

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@_ChildOf(Table.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class IndexInfo extends AbstractMetadataType {

    private static final long serialVersionUID = 924040226611181424L;

    static final Comparator<IndexInfo> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(IndexInfo::isNonUnique)
                    .thenComparingInt(IndexInfo::getType)
                    .thenComparing(IndexInfo::getIndexName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparingInt(IndexInfo::getOrdinalPosition);

    static final Comparator<IndexInfo> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(IndexInfo::isNonUnique)
                    .thenComparingInt(IndexInfo::getType)
                    .thenComparing(IndexInfo::getIndexName, nullsFirst(naturalOrder()))
                    .thenComparingInt(IndexInfo::getOrdinalPosition);

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_TYPE = "TYPE";

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel("NON_UNIQUE")
    private boolean nonUnique;

    @_NullableBySpecification
    @_ColumnLabel("INDEX_QUALIFIER")
    private String indexQualifier;

    @_NullableBySpecification
    @_ColumnLabel("INDEX_NAME")
    private String indexName;

    @_ColumnLabel(COLUMN_LABEL_TYPE)
    private int type;

    @_ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @_NullableBySpecification
    @_ColumnLabel("COLUMN_NAME")
    private String columnName;

    @_NullableBySpecification
    @_ColumnLabel("ASC_OR_DESC")
    private String ascOrDesc;

    @_ColumnLabel("CARDINALITY")
    private long cardinality;

    @_ColumnLabel("PAGES")
    private long pages;

    @_NullableBySpecification
    @_ColumnLabel("FILTER_CONDITION")
    private String filterCondition;
}
