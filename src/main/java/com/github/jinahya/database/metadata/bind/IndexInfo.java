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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getIndexInfo(String, String, String, boolean, boolean)
 */
@_ChildOf(Table.class)
@EqualsAndHashCode(callSuper = true)
public class IndexInfo
        extends AbstractMetadataType {

    private static final long serialVersionUID = 924040226611181424L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<IndexInfo> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(IndexInfo::getNonUnique, Comparator.naturalOrder())
                .thenComparing(IndexInfo::getType, Comparator.naturalOrder())
                .thenComparing(IndexInfo::getIndexName, comparator)
                .thenComparing(IndexInfo::getOrdinalPosition, Comparator.naturalOrder());
    }

    static Comparator<IndexInfo> comparingInSpecifiedOrder(final Context context,
                                                           final Comparator<? super String> comparator)
            throws SQLException {
        return comparingInSpecifiedOrder(
                ContextUtils.nullPrecedence(context, comparator)
        );
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------ TABLE_NAME
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ------------------------------------------------------------------------------------------------------ NON_UNIQUE
    public static final String COLUMN_LABEL_NON_UNIQUE = "NON_UNIQUE";

    // ------------------------------------------------------------------------------------------------- INDEX_QUALIFIER
    public static final String COLUMN_LABEL_INDEX_QUALIFIER = "INDEX_QUALIFIER";

    // ------------------------------------------------------------------------------------------------------ INDEX_NAME
    public static final String COLUMN_LABEL_INDEX_NAME = "INDEX_NAME";

    // ------------------------------------------------------------------------------------------------------------ TYPE
    public static final String COLUMN_LABEL_TYPE = "TYPE";

    public static final int COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC = DatabaseMetaData.tableIndexStatistic; // 0

    public static final int COLUMN_VALUE_TYPE_TABLE_INDEX_CLUSTERED = DatabaseMetaData.tableIndexClustered; // 1

    public static final int COLUMN_VALUE_TYPE_TABLE_INDEX_HASHED = DatabaseMetaData.tableIndexHashed;       // 2

    public static final int COLUMN_VALUE_TYPE_TABLE_INDEX_OTHER = DatabaseMetaData.tableIndexOther;         // 3

    static final List<Integer> TYPE_VALUES = List.of(
            COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC,
            COLUMN_VALUE_TYPE_TABLE_INDEX_CLUSTERED,
            COLUMN_VALUE_TYPE_TABLE_INDEX_HASHED,
            COLUMN_VALUE_TYPE_TABLE_INDEX_OTHER
    );

    // ------------------------------------------------------------------------------------------------ ORDINAL_POSITION
    public static final String COLUMN_LABEL_ORDINAL_POSITION = "ORDINAL_POSITION";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // ----------------------------------------------------------------------------------------------------- ASC_OR_DESC
    public static final String COLUMN_LABEL_ASC_OR_DESC = "ASC_OR_DESC";

    public static final String COLUMN_VALUE_ASC_OR_DESC_A = "A";

    public static final String COLUMN_VALUE_ASC_OR_DESC_D = "D";

    // ----------------------------------------------------------------------------------------------------- CARDINALITY
    public static final String COLUMN_LABEL_CARDINALITY = "CARDINALITY";

    // ----------------------------------------------------------------------------------------------------------- PAGES
    public static final String COLUMN_LABEL_PAGES = "PAGES";

    // ------------------------------------------------------------------------------------------------ FILTER_CONDITION
    public static final String COLUMN_LABEL_FILTER_CONDITION = "FILTER_CONDITION";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    public IndexInfo() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
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

    // ---------------------------------------------------------------------------------------------- Jakarta_Validation

    /**
     * .
     * <blockquote>
     * NON_UNIQUE boolean => Can index values be non-unique. false when TYPE is tableIndexStatistic
     * </blockquote>
     *
     * @return .
     */
    @AssertTrue
    private boolean isNonUniqueValid() {
        if (nonUnique == null || nonUnique) {
            return true;
        }
        return type == null || !Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC);
    }

    /**
     * .
     * <blockquote>
     * INDEX_QUALIFIER String => index catalog (may be null); null when TYPE is tableIndexStatistic
     * </blockquote>
     *
     * @return .
     */
    @AssertTrue
    private boolean isIndexQualifierValid() {
        return !Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) || indexQualifier == null;
    }

    /**
     * .
     * <blockquote>
     * INDEX_NAME String => index name (may be null); null when TYPE is tableIndexStatistic
     * </blockquote>
     *
     * @return .
     */
    @AssertTrue
    private boolean isIndexNameValid() {
        if (indexName != null) {
            return true;
        }
        return type == null || Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC);
    }

    @AssertTrue
    private boolean isTypeValid() {
        return type == null || TYPE_VALUES.contains(type);
    }

    /**
     * .
     * <blockquote>
     * ORDINAL_POSITION short => column sequence number within index; zero when TYPE is tableIndexStatistic
     * </blockquote>
     *
     * @return .
     */
    @AssertTrue
    private boolean isOrdinalPosition() {
        if (ordinalPosition == null || ordinalPosition != 0) {
            return true;
        }
        return type == null || Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC);
    }

    /**
     * .
     * <blockquote>
     * COLUMN_NAME String => column name; null when TYPE is tableIndexStatistic
     * </blockquote>
     *
     * @return .
     */
    @AssertTrue
    private boolean isColumnNameValid() {
        if (columnName != null) {
            return true;
        }
        return type == null || Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC);
    }

    /**
     * <blockquote>
     * SC_OR_DESC String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is
     * not supported; null when TYPE is tableIndexStatistic
     * </blockquote>
     *
     * @return .
     */
    @AssertTrue
    private boolean isAscOrDescValid() {
        return !Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) || ascOrDesc == null;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(@Nullable final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem

    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(@Nullable final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------- nonUnique

    public Boolean getNonUnique() {
        return nonUnique;
    }

    public void setNonUnique(final Boolean nonUnique) {
        this.nonUnique = nonUnique;
    }

    // -------------------------------------------------------------------------------------------------- indexQualifier

    @Nullable
    public String getIndexQualifier() {
        return indexQualifier;
    }

    public void setIndexQualifier(@Nullable final String indexQualifier) {
        this.indexQualifier = indexQualifier;
    }

    // ------------------------------------------------------------------------------------------------------- indexName

    @Nullable
    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(@Nullable final String indexName) {
        this.indexName = indexName;
    }

    // ------------------------------------------------------------------------------------------------------------ type
    public Integer getType() {
        return type;
    }

    public void setType(final Integer type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    @Nullable
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(@Nullable final String columnName) {
        this.columnName = columnName;
    }

    // ------------------------------------------------------------------------------------------------------- ascOrDesc
    @Nullable
    public String getAscOrDesc() {
        return ascOrDesc;
    }

    public void setAscOrDesc(@Nullable final String ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

    /**
     * .
     *
     * @return {@code true} if,  and only if, the current value of {@code ascOrDesc} property is equal to
     * {@value #COLUMN_VALUE_ASC_OR_DESC_A}; {@code false} otherwise.
     */
    boolean isAscending() {
        return Optional.ofNullable(getAscOrDesc())
                .map(v -> v.equals(COLUMN_VALUE_ASC_OR_DESC_A))
                .orElse(false);
    }

    /**
     * .
     *
     * @return {@code true} if, and only if, the current value of {@code ascOrDesc} property is equal to
     * {@value #COLUMN_VALUE_ASC_OR_DESC_D}; {@code false} otherwise.
     */
    boolean isDescending() {
        return Optional.ofNullable(getAscOrDesc())
                .map(v -> v.equals(COLUMN_VALUE_ASC_OR_DESC_D))
                .orElse(false);
    }

    // ----------------------------------------------------------------------------------------------------- cardinality
    public Long getCardinality() {
        return cardinality;
    }

    public void setCardinality(final Long cardinality) {
        this.cardinality = cardinality;
    }

    @Nullable
    Long getNumberOfRowsInTheTable() {
        if (Objects.equals(getType(), COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getCardinality();
        }
        return null;
    }

    @Nullable
    Long getNumberOfUniqueValuesInTheIndex() {
        if (!Objects.equals(getType(), COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getCardinality();
        }
        return null;
    }

    // ----------------------------------------------------------------------------------------------------------- pages
    public Long getPages() {
        return pages;
    }

    public void setPages(final Long pages) {
        this.pages = pages;
    }

    @Nullable
    Long getNumberOfPagesUsedForTheTable() {
        if (!Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getPages();
        }
        return null;
    }

    @Nullable
    Long getNumberOfPagesUsedForTheCurrentIndex() {
        if (Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return null;
        }
        return getPages();
    }

    // ------------------------------------------------------------------------------------------------- filterCondition
    @Nullable
    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(@Nullable final String filterCondition) {
        this.filterCondition = filterCondition;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_NON_UNIQUE)
    private Boolean nonUnique;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_INDEX_QUALIFIER)
    private String indexQualifier;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_INDEX_NAME)
    private String indexName;

    @_ColumnLabel(COLUMN_LABEL_TYPE)
    private Integer type;

    @_ColumnLabel(COLUMN_LABEL_ORDINAL_POSITION)
    private Integer ordinalPosition;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_ASC_OR_DESC)
    private String ascOrDesc;

    @_ColumnLabel(COLUMN_LABEL_CARDINALITY)
    private Long cardinality;

    @_ColumnLabel(COLUMN_LABEL_PAGES)
    private Long pages;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FILTER_CONDITION)
    private String filterCondition;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Table table;

    Table getTable() {
        return table;
    }

    void setTable(final Table table) {
        this.table = table;
    }
}
