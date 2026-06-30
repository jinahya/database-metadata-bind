package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import jakarta.validation.constraints.AssertTrue;
import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getIndexInfo(String, String, String, boolean, boolean)
 */
@_ChildOf(Table.class)
public class IndexInfo
        extends AbstractMetadataType {

    private static final long serialVersionUID = 924040226611181424L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>NON_UNIQUE</code>, <code>TYPE</code>, <code>INDEX_NAME</code>, and
     * <code>ORDINAL_POSITION</code>.
     * </blockquote>
     *
     * @param operator   a unary operator for adjusting string values; applied only to non-{@code null} values.
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
     */
    static Comparator<IndexInfo> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                           final Comparator<? super String> comparator) {
        Objects.requireNonNull(operator, "operator is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final UnaryOperator<String> op = v -> v == null ? null : operator.apply(v);
        return Comparator
                .comparing(IndexInfo::getNonUnique, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(IndexInfo::getType, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(v -> op.apply(v.getIndexName()), comparator)
                .thenComparing(IndexInfo::getOrdinalPosition, Comparator.nullsFirst(Comparator.naturalOrder()));
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------ TABLE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ------------------------------------------------------------------------------------------------------ NON_UNIQUE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_NON_UNIQUE = "NON_UNIQUE";

    // ------------------------------------------------------------------------------------------------- INDEX_QUALIFIER

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_INDEX_QUALIFIER = "INDEX_QUALIFIER";

    // ------------------------------------------------------------------------------------------------------ INDEX_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_INDEX_NAME = "INDEX_NAME";

    // ------------------------------------------------------------------------------------------------------------ TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE = "TYPE";

    /**
     * A column value of {@link DatabaseMetaData#tableIndexStatistic}({@value DatabaseMetaData#tableIndexStatistic}) for
     * the {@value #COLUMN_LABEL_TYPE} column.
     */
    public static final int COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC = DatabaseMetaData.tableIndexStatistic; // 0

    /**
     * A column value of {@link DatabaseMetaData#tableIndexClustered}({@value DatabaseMetaData#tableIndexClustered}) for
     * the {@value #COLUMN_LABEL_TYPE} column.
     */
    public static final int COLUMN_VALUE_TYPE_TABLE_INDEX_CLUSTERED = DatabaseMetaData.tableIndexClustered; // 1

    /**
     * A column value of {@link DatabaseMetaData#tableIndexHashed}({@value DatabaseMetaData#tableIndexHashed}) for the
     * {@value #COLUMN_LABEL_TYPE} column.
     */
    public static final int COLUMN_VALUE_TYPE_TABLE_INDEX_HASHED = DatabaseMetaData.tableIndexHashed;       // 2

    /**
     * A column value of {@link DatabaseMetaData#tableIndexOther}({@value DatabaseMetaData#tableIndexOther}) for the
     * {@value #COLUMN_LABEL_TYPE} column.
     */
    public static final int COLUMN_VALUE_TYPE_TABLE_INDEX_OTHER = DatabaseMetaData.tableIndexOther;         // 3

    static final List<Integer> COLUMN_VALUES_TYPE = List.of(
            COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC,
            COLUMN_VALUE_TYPE_TABLE_INDEX_CLUSTERED,
            COLUMN_VALUE_TYPE_TABLE_INDEX_HASHED,
            COLUMN_VALUE_TYPE_TABLE_INDEX_OTHER
    );

    // ------------------------------------------------------------------------------------------------ ORDINAL_POSITION

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_ORDINAL_POSITION = "ORDINAL_POSITION";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // ----------------------------------------------------------------------------------------------------- ASC_OR_DESC

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_ASC_OR_DESC = "ASC_OR_DESC";

    /**
     * A column value of {@value} for the {@value #COLUMN_LABEL_ASC_OR_DESC} column indicating ascending order.
     */
    public static final String COLUMN_VALUE_ASC_OR_DESC_A = "A";

    /**
     * A column value of {@value} for the {@value #COLUMN_LABEL_ASC_OR_DESC} column indicating descending order.
     */
    public static final String COLUMN_VALUE_ASC_OR_DESC_D = "D";

    static final List<String> COLUMN_VALUES_ASC_OR_DESC = List.of(
            COLUMN_VALUE_ASC_OR_DESC_A,
            COLUMN_VALUE_ASC_OR_DESC_D
    );

    // ----------------------------------------------------------------------------------------------------- CARDINALITY

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CARDINALITY = "CARDINALITY";

    // ----------------------------------------------------------------------------------------------------------- PAGES

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PAGES = "PAGES";

    // ------------------------------------------------------------------------------------------------ FILTER_CONDITION

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FILTER_CONDITION = "FILTER_CONDITION";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    IndexInfo() {
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

    // ---------------------------------------------------------------------------------------------- Jakarta-Validation

    /**
     * Asserts that {@value #COLUMN_LABEL_NON_UNIQUE} is {@code false} when {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}.
     *
     * @return {@code true} if the constraint holds; {@code false} otherwise.
     */
    @AssertTrue(message = "NON_UNIQUE is false when TYPE is tableIndexStatistic(0)")
    private boolean isNonUniqueFalseWhenTypeIsTableIndexStatistic() {
        if (type == null) {
            return true;
        }
        if (type == COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) {
            return nonUnique == null || !nonUnique;
        }
        return true;
    }

    /**
     * Asserts that {@value #COLUMN_LABEL_INDEX_QUALIFIER} is {@code null} when {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}.
     *
     * @return {@code true} if the constraint holds; {@code false} otherwise.
     */
    @AssertTrue(message = "INDEX_QUALIFIER is null when TYPE is tableIndexStatistic(0)")
    private boolean isIndexQualifierNullWhenTypeIsTableIndexStatistic() {
        if (type == null) {
            return true;
        }
        if (type == COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) {
            return indexQualifier == null;
        }
        return true;
    }

    /**
     * Asserts that {@value #COLUMN_LABEL_INDEX_NAME} is {@code null} when {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}.
     *
     * @return {@code true} if the constraint holds; {@code false} otherwise.
     */
    @AssertTrue(message = "INDEX_NAME is null when TYPE is tableIndexStatistic(0)")
    private boolean isIndexNameNullWhenTypeIsTableIndexStatistic() {
        if (type == null) {
            return true;
        }
        if (type == COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) {
            return indexName == null;
        }
        return true;
    }

    /**
     * Asserts that {@value #COLUMN_LABEL_ORDINAL_POSITION} is zero when {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}.
     *
     * @return {@code true} if the constraint holds; {@code false} otherwise.
     */
    @AssertTrue(message = "ORDINAL_POSITION is zero when TYPE is tableIndexStatistic(0)")
    private boolean isOrdinalPositionZeroWhenTypeIsTableIndexStatistic() {
        if (type == null) {
            return true;
        }
        if (type == COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) {
            return ordinalPosition == null || ordinalPosition == 0;
        }
        return true;
    }

    /**
     * Asserts that {@value #COLUMN_LABEL_COLUMN_NAME} is {@code null} when {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}.
     *
     * @return {@code true} if the constraint holds; {@code false} otherwise.
     */
    @AssertTrue(message = "COLUMN_NAME is null when TYPE is tableIndexStatistic(0)")
    private boolean isColumnNameNullWhenTypeIsTableIndexStatistic() {
        if (type == null) {
            return true;
        }
        if (type == COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) {
            return columnName == null;
        }
        return true;
    }

    /**
     * Asserts that {@value #COLUMN_LABEL_ASC_OR_DESC} is {@code null} when {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}.
     *
     * @return {@code true} if the constraint holds; {@code false} otherwise.
     */
    @AssertTrue(message = "ASC_OR_DESC is null when TYPE is tableIndexStatistic(0)")
    private boolean isAscOrDescNullWhenTypeIsTableIndexStatistic() {
        if (type == null) {
            return true;
        }
        if (type == COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) {
            return ascOrDesc == null;
        }
        return true;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @param tableCat the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @param tableSchem the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @param tableName the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
    void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------- nonUnique

    /**
     * Returns the value of {@value #COLUMN_LABEL_NON_UNIQUE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_NON_UNIQUE} column.
     */
    public Boolean getNonUnique() {
        return nonUnique;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_NON_UNIQUE} column.
     *
     * @param nonUnique the value of {@value #COLUMN_LABEL_NON_UNIQUE} column.
     */
    void setNonUnique(final Boolean nonUnique) {
        this.nonUnique = nonUnique;
    }

    // -------------------------------------------------------------------------------------------------- indexQualifier

    /**
     * Returns the value of {@value #COLUMN_LABEL_INDEX_QUALIFIER} column.
     *
     * @return the value of {@value #COLUMN_LABEL_INDEX_QUALIFIER} column.
     */
    @Nullable
    public String getIndexQualifier() {
        return indexQualifier;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_INDEX_QUALIFIER} column.
     *
     * @param indexQualifier the value of {@value #COLUMN_LABEL_INDEX_QUALIFIER} column.
     */
    void setIndexQualifier(final String indexQualifier) {
        this.indexQualifier = indexQualifier;
    }

    // ------------------------------------------------------------------------------------------------------- indexName

    /**
     * Returns the value of {@value #COLUMN_LABEL_INDEX_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_INDEX_NAME} column.
     */
    @Nullable
    public String getIndexName() {
        return indexName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_INDEX_NAME} column.
     *
     * @param indexName the value of {@value #COLUMN_LABEL_INDEX_NAME} column.
     */
    void setIndexName(final String indexName) {
        this.indexName = indexName;
    }

    // ------------------------------------------------------------------------------------------------------------ type

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE} column.
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE} column.
     *
     * @param type the value of {@value #COLUMN_LABEL_TYPE} column.
     */
    void setType(final Integer type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition

    /**
     * Returns the value of {@value #COLUMN_LABEL_ORDINAL_POSITION} column.
     *
     * @return the value of {@value #COLUMN_LABEL_ORDINAL_POSITION} column.
     */
    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_ORDINAL_POSITION} column.
     *
     * @param ordinalPosition the value of {@value #COLUMN_LABEL_ORDINAL_POSITION} column.
     */
    void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ columnName

    /**
     * Returns the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    @Nullable
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @param columnName the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ------------------------------------------------------------------------------------------------------- ascOrDesc

    /**
     * Returns the value of {@value #COLUMN_LABEL_ASC_OR_DESC} column.
     *
     * @return the value of {@value #COLUMN_LABEL_ASC_OR_DESC} column.
     */
    @Nullable
    public String getAscOrDesc() {
        return ascOrDesc;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_ASC_OR_DESC} column.
     *
     * @param ascOrDesc the value of {@value #COLUMN_LABEL_ASC_OR_DESC} column.
     */
    void setAscOrDesc(final String ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

    /**
     * Indicates whether this index column is sorted in <em>ascending</em> order.
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
     * Indicates whether this index column is sorted in <em>descending</em> order.
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

    /**
     * Returns the value of {@value #COLUMN_LABEL_CARDINALITY} column.
     *
     * @return the value of {@value #COLUMN_LABEL_CARDINALITY} column.
     */
    public Long getCardinality() {
        return cardinality;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_CARDINALITY} column.
     *
     * @param cardinality the value of {@value #COLUMN_LABEL_CARDINALITY} column.
     */
    void setCardinality(final Long cardinality) {
        this.cardinality = cardinality;
    }

    /**
     * Returns the number of rows in the table. When {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}, the {@value #COLUMN_LABEL_CARDINALITY} column holds the number
     * of rows in the table, and this method returns it; otherwise this method returns {@code null}.
     *
     * @return the value of the {@value #COLUMN_LABEL_CARDINALITY} column when {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}; {@code null} otherwise.
     */
    @Nullable
    Long getNumberOfRowsInTheTable() {
        if (Objects.equals(getType(), COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getCardinality();
        }
        return null;
    }

    /**
     * Returns the number of unique values in the index. When {@value #COLUMN_LABEL_TYPE} is <em>not</em>
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}, the {@value #COLUMN_LABEL_CARDINALITY} column holds the number
     * of unique values in the index, and this method returns it; otherwise this method returns {@code null}.
     *
     * @return the value of the {@value #COLUMN_LABEL_CARDINALITY} column when {@value #COLUMN_LABEL_TYPE} is
     * <em>not</em>
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}; {@code null} otherwise.
     */
    @Nullable
    Long getNumberOfUniqueValuesInTheIndex() {
        if (!Objects.equals(getType(), COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getCardinality();
        }
        return null;
    }

    // ----------------------------------------------------------------------------------------------------------- pages

    /**
     * Returns the value of {@value #COLUMN_LABEL_PAGES} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PAGES} column.
     */
    public Long getPages() {
        return pages;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PAGES} column.
     *
     * @param pages the value of {@value #COLUMN_LABEL_PAGES} column.
     */
    void setPages(final Long pages) {
        this.pages = pages;
    }

    /**
     * Returns the number of pages used for the table. When {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}, the {@value #COLUMN_LABEL_PAGES} column holds the number of
     * pages used for the table, and this method returns it; otherwise this method returns {@code null}.
     *
     * @return the value of the {@value #COLUMN_LABEL_PAGES} column when {@value #COLUMN_LABEL_TYPE} is
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}; {@code null} otherwise.
     */
    @Nullable
    Long getNumberOfPagesUsedForTheTable() {
        if (Objects.equals(getType(), COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getPages();
        }
        return null;
    }

    /**
     * Returns the number of pages used for the current index. When {@value #COLUMN_LABEL_TYPE} is <em>not</em>
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}, the {@value #COLUMN_LABEL_PAGES} column holds the number of
     * pages used for the current index, and this method returns it; otherwise this method returns {@code null}.
     *
     * @return the value of the {@value #COLUMN_LABEL_PAGES} column when {@value #COLUMN_LABEL_TYPE} is <em>not</em>
     * {@link #COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC}; {@code null} otherwise.
     */
    @Nullable
    Long getNumberOfPagesUsedForTheCurrentIndex() {
        if (!Objects.equals(getType(), COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getPages();
        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------- filterCondition

    /**
     * Returns the value of {@value #COLUMN_LABEL_FILTER_CONDITION} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FILTER_CONDITION} column.
     */
    @Nullable
    public String getFilterCondition() {
        return filterCondition;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FILTER_CONDITION} column.
     *
     * @param filterCondition the value of {@value #COLUMN_LABEL_FILTER_CONDITION} column.
     */
    void setFilterCondition(final String filterCondition) {
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
}
