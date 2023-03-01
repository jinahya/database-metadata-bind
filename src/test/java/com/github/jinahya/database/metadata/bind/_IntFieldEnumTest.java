package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2023 Jinahya, Inc.
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

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

abstract class _IntFieldEnumTest<E extends Enum<E> & _IntFieldEnum<E>> {

    _IntFieldEnumTest(final Class<E> enumClass) {
        super();
        this.enumClass = Objects.requireNonNull(enumClass, "enumClass is null");
    }

    @Test
    void fieldValueAsInt_Distinct_() {
        assertThat(enumClass.getEnumConstants())
                .extracting(_IntFieldEnum::fieldValueAsInt)
                .doesNotHaveDuplicates();
    }

    final Class<E> enumClass;
}
