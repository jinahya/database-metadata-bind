package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

class IndexInfoTest
        extends AbstractMetadataType_Test<IndexInfo> {

    IndexInfoTest() {
        super(IndexInfo.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @org.junit.jupiter.api.Test
    void isAscending_And_isDescending_AreDerivedFromAscOrDesc_AsSpecified() {
        final var instance = newTypeInstance();

        instance.setAscOrDesc(IndexInfo.COLUMN_VALUE_ASC_OR_DESC_A);
        org.assertj.core.api.Assertions.assertThat(instance.isAscending()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isDescending()).isFalse();

        instance.setAscOrDesc(IndexInfo.COLUMN_VALUE_ASC_OR_DESC_D);
        org.assertj.core.api.Assertions.assertThat(instance.isAscending()).isFalse();
        org.assertj.core.api.Assertions.assertThat(instance.isDescending()).isTrue();

        instance.setAscOrDesc(null);
        org.assertj.core.api.Assertions.assertThat(instance.isAscending()).isFalse();
        org.assertj.core.api.Assertions.assertThat(instance.isDescending()).isFalse();
    }

    @org.junit.jupiter.api.Test
    void cardinalityDerivedValues_AreSplitByTableIndexStatistic_AsSpecified() {
        final var instance = newTypeInstance();
        instance.setCardinality(13L);

        instance.setType(IndexInfo.COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC);
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfRowsInTheTable()).isEqualTo(13L);
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfUniqueValuesInTheIndex()).isNull();

        instance.setType(IndexInfo.COLUMN_VALUE_TYPE_TABLE_INDEX_OTHER);
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfRowsInTheTable()).isNull();
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfUniqueValuesInTheIndex()).isEqualTo(13L);

        instance.setType(null);
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfRowsInTheTable()).isNull();
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfUniqueValuesInTheIndex()).isEqualTo(13L);
    }

    @org.junit.jupiter.api.Test
    void pageDerivedValues_AreSplitByTableIndexStatistic_AsSpecified() {
        final var instance = newTypeInstance();
        instance.setPages(17L);

        instance.setType(IndexInfo.COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC);
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfPagesUsedForTheTable()).isEqualTo(17L);
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfPagesUsedForTheCurrentIndex()).isNull();

        instance.setType(IndexInfo.COLUMN_VALUE_TYPE_TABLE_INDEX_OTHER);
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfPagesUsedForTheTable()).isNull();
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfPagesUsedForTheCurrentIndex()).isEqualTo(17L);

        instance.setType(null);
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfPagesUsedForTheTable()).isNull();
        org.assertj.core.api.Assertions.assertThat(instance.getNumberOfPagesUsedForTheCurrentIndex()).isEqualTo(17L);
    }

    // ---------------------------------------------------------------------------------------------- Jakarta-Validation
    // Note: these assert the (Bean-Validation-free) predicate logic ported from the 'jakarta' branch. This branch does
    //       NOT depend on Jakarta Bean Validation; the predicates are plain methods, exercised here directly.

    @org.junit.jupiter.api.Test
    void tableIndexStatisticPredicates_HoldOnlyForConformingStatisticRows() {
        final var instance = newTypeInstance();
        final var statistic = IndexInfo.COLUMN_VALUE_TYPE_TABLE_INDEX_STATISTIC;
        final var other = IndexInfo.COLUMN_VALUE_TYPE_TABLE_INDEX_OTHER;

        // type unknown -> every predicate holds, whatever the values are
        instance.setType(null);
        instance.setNonUnique(Boolean.TRUE);
        instance.setIndexQualifier("q");
        instance.setIndexName("n");
        instance.setOrdinalPosition(7);
        instance.setColumnName("c");
        instance.setAscOrDesc(IndexInfo.COLUMN_VALUE_ASC_OR_DESC_A);
        org.assertj.core.api.Assertions.assertThat(instance.isNonUniqueFalseWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isIndexQualifierNullWhenTypeIsTableIndexStatistic())
                .isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isIndexNameNullWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isOrdinalPositionZeroWhenTypeIsTableIndexStatistic())
                .isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isColumnNameNullWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isAscOrDescNullWhenTypeIsTableIndexStatistic()).isTrue();

        // type is a non-statistic index -> not constrained, holds even with the same values
        instance.setType(other);
        org.assertj.core.api.Assertions.assertThat(instance.isNonUniqueFalseWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isIndexQualifierNullWhenTypeIsTableIndexStatistic())
                .isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isIndexNameNullWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isOrdinalPositionZeroWhenTypeIsTableIndexStatistic())
                .isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isColumnNameNullWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isAscOrDescNullWhenTypeIsTableIndexStatistic()).isTrue();

        // type is tableIndexStatistic with non-conforming values -> every predicate violated
        instance.setType(statistic);
        org.assertj.core.api.Assertions.assertThat(instance.isNonUniqueFalseWhenTypeIsTableIndexStatistic()).isFalse();
        org.assertj.core.api.Assertions.assertThat(instance.isIndexQualifierNullWhenTypeIsTableIndexStatistic())
                .isFalse();
        org.assertj.core.api.Assertions.assertThat(instance.isIndexNameNullWhenTypeIsTableIndexStatistic()).isFalse();
        org.assertj.core.api.Assertions.assertThat(instance.isOrdinalPositionZeroWhenTypeIsTableIndexStatistic())
                .isFalse();
        org.assertj.core.api.Assertions.assertThat(instance.isColumnNameNullWhenTypeIsTableIndexStatistic()).isFalse();
        org.assertj.core.api.Assertions.assertThat(instance.isAscOrDescNullWhenTypeIsTableIndexStatistic()).isFalse();

        // type is tableIndexStatistic with conforming values -> every predicate holds
        instance.setNonUnique(Boolean.FALSE);
        instance.setIndexQualifier(null);
        instance.setIndexName(null);
        instance.setOrdinalPosition(0);
        instance.setColumnName(null);
        instance.setAscOrDesc(null);
        org.assertj.core.api.Assertions.assertThat(instance.isNonUniqueFalseWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isIndexQualifierNullWhenTypeIsTableIndexStatistic())
                .isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isIndexNameNullWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isOrdinalPositionZeroWhenTypeIsTableIndexStatistic())
                .isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isColumnNameNullWhenTypeIsTableIndexStatistic()).isTrue();
        org.assertj.core.api.Assertions.assertThat(instance.isAscOrDescNullWhenTypeIsTableIndexStatistic()).isTrue();
    }
}
