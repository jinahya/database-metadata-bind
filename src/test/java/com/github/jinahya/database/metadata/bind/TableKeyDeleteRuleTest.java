package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2024 Jinahya, Inc.
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class TableKeyDeleteRuleTest extends _IntFieldEnumTest<TableKey.TableKeyDeleteRule> {

    TableKeyDeleteRuleTest() {
        super(TableKey.TableKeyDeleteRule.class);
    }

    @DisplayName("valueOfDeleteRule")
    @EnumSource(TableKey.TableKeyDeleteRule.class)
    @ParameterizedTest
    void valueOfDeleteRule__(final TableKey.TableKeyDeleteRule deleteRule) {
        assertThat(TableKey.TableKeyDeleteRule.valueOfFieldValue(deleteRule.fieldValueAsInt()))
                .isSameAs(deleteRule);
    }
}
