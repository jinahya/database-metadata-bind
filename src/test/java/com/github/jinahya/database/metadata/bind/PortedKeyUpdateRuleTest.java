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

class PortedKeyUpdateRuleTest
        extends _IntFieldEnumTest<PortedKey.TableKeyUpdateRule> {

    PortedKeyUpdateRuleTest() {
        super(PortedKey.TableKeyUpdateRule.class);
    }

    @DisplayName("valueOfUpdateRule")
    @EnumSource(PortedKey.TableKeyUpdateRule.class)
    @ParameterizedTest
    void valueOfUpdateRule__(final PortedKey.TableKeyUpdateRule updateRule) {
        assertThat(PortedKey.TableKeyUpdateRule.valueOfFieldValue(updateRule.fieldValueAsInt()))
                .isSameAs(updateRule);
    }
}
