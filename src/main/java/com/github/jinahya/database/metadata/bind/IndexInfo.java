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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    @SuperBuilder(toBuilder = true)
    static class IndexInfoCategory
            implements Serializable {

        private static final long serialVersionUID = 8326654078451632859L;

        static IndexInfoCategory of(final boolean unique, final boolean approximate) {
            return builder()
                    .unique(unique)
                    .approximate(approximate)
                    .build();
        }

        static final Set<IndexInfoCategory> _VALUES = Collections.unmodifiableSet(
                Stream.of(true, false)
                        .flatMap(u -> Stream.of(IndexInfoCategory.of(u, false),
                                                IndexInfoCategory.of(u, true)))
                        .collect(Collectors.toSet())
        );

        private boolean unique;

        private boolean approximate;
    }

    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @SuperBuilder(toBuilder = true)
    public static class CategorizedIndexInfo
            implements Serializable {

        private static final long serialVersionUID = 3971564160837471251L;

        static CategorizedIndexInfo of(final IndexInfoCategory indexInfoCategory) {
            return builder()
                    .category(indexInfoCategory)
                    .build();
        }

        public boolean isUnique() {
            return Objects.requireNonNull(category, "category is null").isUnique();
        }

        public void setUnique(final boolean unique) {
            Optional.ofNullable(category)
                    .orElseGet(() -> (category = new IndexInfoCategory()))
                    .setUnique(unique);
        }

        public boolean isApproximate() {
            return Objects.requireNonNull(category, "category is null").isApproximate();
        }

        public void setApproximate(final boolean approximate) {
            Optional.ofNullable(category)
                    .orElseGet(() -> (category = new IndexInfoCategory()))
                    .setApproximate(approximate);
        }

        public List<IndexInfo> getIndexInfo() {
            if (indexInfo == null) {
                indexInfo = new ArrayList<>();
            }
            return indexInfo;
        }

        @Deprecated
        public void setIndexInfo(final List<IndexInfo> indexInfo) {
            this.indexInfo = indexInfo;
        }

        @Setter(AccessLevel.PACKAGE)
        @Getter(AccessLevel.PACKAGE)
        private IndexInfoCategory category;

        @Setter(AccessLevel.NONE)
        @Getter(AccessLevel.NONE)
        private List<IndexInfo> indexInfo;
    }

    public static final String COLUMN_NAME_TYPE = "TYPE";

    private boolean isNonUniqueFalseWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return !isNonUnique();
    }

    private boolean isIndexQualifierNullWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getIndexQualifier() == null;
    }

    private boolean isIndexNameNullWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getIndexName() == null;
    }

    private boolean isOrdinalPositionZeroWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getOrdinalPosition() == 0;
    }

    private boolean isColumnNameNullWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getColumnName() == null;
    }

    private boolean isAscOrDescNullWhenTypeIsTableIndexStatistics() {
        if (getType() != DatabaseMetaData.tableIndexStatistic) {
            return true;
        }
        return getAscOrDesc() == null;
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @Override
    public Table extractParent() {
        return Table.builder()
                .tableCat(getTableCat())
                .tableSchem(getTableSchem())
                .tableName(getTableName())
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
