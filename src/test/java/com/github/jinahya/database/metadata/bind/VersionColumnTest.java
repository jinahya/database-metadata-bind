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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionColumnTest
        extends AbstractMetadataType_Test<VersionColumn> {

    VersionColumnTest() {
        super(VersionColumn.class);
    }

    // ---------------------------------------------------------------------------------------------- Jakarta-Validation
    // Note: these assert the (Bean-Validation-free) predicate logic ported from the 'jakarta' branch. This branch does
    //       NOT depend on Jakarta Bean Validation; the predicates are plain methods, exercised here directly.

    @Test
    void isPseudoColumnValid_HoldsForKnownValuesAndNull() {
        final var instance = newTypeInstance();
        // null -> holds
        instance.setPseudoColumn(null);
        assertThat(instance.isPseudoColumnValid()).isTrue();
        // known values -> hold
        instance.setPseudoColumn(VersionColumn.COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_UNKNOWN);
        assertThat(instance.isPseudoColumnValid()).isTrue();
        instance.setPseudoColumn(VersionColumn.COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_NOT_PSEUDO);
        assertThat(instance.isPseudoColumnValid()).isTrue();
        instance.setPseudoColumn(VersionColumn.COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_PSEUDO);
        assertThat(instance.isPseudoColumnValid()).isTrue();
        // unknown value -> violated
        instance.setPseudoColumn(Integer.MIN_VALUE);
        assertThat(instance.isPseudoColumnValid()).isFalse();
    }
}
