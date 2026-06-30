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
}
