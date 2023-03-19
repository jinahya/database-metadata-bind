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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@_ChildOf(Table.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class IndexInfo extends AbstractMetadataType {

    private static final long serialVersionUID = 924040226611181424L;

    static final Comparator<IndexInfo> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(IndexInfo::getNonUnique, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getType, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getIndexName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparingInt(IndexInfo::getOrdinalPosition);

    static final Comparator<IndexInfo> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(IndexInfo::getNonUnique, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getType, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getIndexName, nullsFirst(naturalOrder()))
                    .thenComparingInt(IndexInfo::getOrdinalPosition);

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_TYPE = "TYPE";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",nonUnique=" + nonUnique +
               ",indexQualifier=" + indexQualifier +
               ",indexName=" + indexName +
               ",type=" + type +
               ",ordinalPosition=" + ordinalPosition +
               ",columnName=" + columnName +
               ",ascOrDesc=" + ascOrDesc +
               ",cardinality=" + cardinality +
               ",pages=" + pages +
               ",filterCondition=" + filterCondition +
               '}';
    }

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_NotNull
    @_ColumnLabel("NON_UNIQUE")
    private Boolean nonUnique;

    @_NullableBySpecification
    @_ColumnLabel("INDEX_QUALIFIER")
    private String indexQualifier;

    @_NullableBySpecification
    @_ColumnLabel("INDEX_NAME")
    private String indexName;

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_TYPE)
    private Integer type;

    @_NotNull
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @_NullableBySpecification
    @_ColumnLabel("COLUMN_NAME")
    private String columnName;

    @_NullableBySpecification
    @_ColumnLabel("ASC_OR_DESC")
    private String ascOrDesc;

    @_NullableByVendor("Apache Derby")
    @_NotNull
    @_ColumnLabel("CARDINALITY")
    private Long cardinality;

    @_NullableByVendor("Apache Derby")
    @_NotNull
    @_ColumnLabel("PAGES")
    private Long pages;

    @_NullableBySpecification
    @_ColumnLabel("FILTER_CONDITION")
    private String filterCondition;

    enum Type implements _IntFieldEnum<Type> {

        TABLE_INDEX_STATISTIC(DatabaseMetaData.tableIndexStatistic), // 0

        TABLE_INDEX_CLUSTERED(DatabaseMetaData.tableIndexClustered), // 1

        TABLE_INDEX_HASHED(DatabaseMetaData.tableIndexHashed), // 2

        TABLE_INDEX_OTHER(DatabaseMetaData.tableIndexOther); // 3

        public static Type valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(Type.class, fieldValue);
        }

        Type(final short fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    Type getTypeAsEnum() {
        return Optional.ofNullable(getType())
                .map(Type::valueOfFieldValue)
                .orElse(null);
    }

    void setTypeAsEnum(Type typeAsEnum) {
        setType(
                Optional.ofNullable(typeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }
}
