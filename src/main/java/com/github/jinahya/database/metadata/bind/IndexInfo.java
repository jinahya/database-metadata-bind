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
public class IndexInfo
        extends AbstractMetadataType {

    private static final long serialVersionUID = 924040226611181424L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    static Comparator<IndexInfo> comparingInSpecifiedOrder(final Context context,
                                                           final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final var nullSafe = ContextUtils.nullPrecedence(context, comparator);
        return Comparator
                .comparing(IndexInfo::getNonUnique, Comparator.<Boolean>naturalOrder())  // NOT nullable
                .thenComparing(IndexInfo::getType, Comparator.<Integer>naturalOrder())   // NOT nullable
                .thenComparing(IndexInfo::getIndexName, nullSafe)                        // nullable
                .thenComparing(IndexInfo::getOrdinalPosition, Comparator.naturalOrder()); // NOT nullable
    }

    static Comparator<IndexInfo> comparingInSpecifiedOrder(final Context context)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
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

    public static final String COLUMN_VALUE_ASC_OR_DESC_A = "A";

    public static final String COLUMN_VALUE_ASC_OR_DESC_D = "D";

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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (IndexInfo) obj;
        return Objects.equals(tableCat, that.tableCat) &&
               Objects.equals(tableSchem, that.tableSchem) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(indexName, that.indexName) &&
               Objects.equals(ordinalPosition, that.ordinalPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableCat, tableSchem, tableName, indexName, ordinalPosition);
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
    private boolean isIndexNameValid() {
        if (indexName != null) {
            return true;
        }
        return type == null || Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC);
    }

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
    private boolean isAscOrDescValid() {
        return !Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC) || ascOrDesc == null;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
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

    Long getNumberOfRowsInTheTable() {
        if (Objects.equals(getType(), COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getCardinality();
        }
        return null;
    }

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

    Long getNumberOfPagesUsedForTheTable() {
        if (!Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return getPages();
        }
        return null;
    }

    Long getNumberOfPagesUsedForTheCurrentIndex() {
        if (Objects.equals(type, COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC)) {
            return null;
        }
        return getPages();
    }

    // ------------------------------------------------------------------------------------------------- filterCondition

    /**
     * Returns the value of {@value #COLUMN_LABEL_FILTER_CONDITION} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FILTER_CONDITION} column.
     */
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

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_NON_UNIQUE)
    private Boolean nonUnique;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_INDEX_QUALIFIER)
    private String indexQualifier;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_INDEX_NAME)
    private String indexName;

    @_ColumnLabel(COLUMN_LABEL_TYPE)
    private Integer type;

    @_ColumnLabel(COLUMN_LABEL_ORDINAL_POSITION)
    private Integer ordinalPosition;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_ASC_OR_DESC)
    private String ascOrDesc;

    @_ColumnLabel(COLUMN_LABEL_CARDINALITY)
    private Long cardinality;

    @_ColumnLabel(COLUMN_LABEL_PAGES)
    private Long pages;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FILTER_CONDITION)
    private String filterCondition;
}
