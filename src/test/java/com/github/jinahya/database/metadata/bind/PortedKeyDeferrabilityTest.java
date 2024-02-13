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

import static com.github.jinahya.database.metadata.bind.PortedKey.TableKeyDeferrability.valueOfFieldValue;
import static org.assertj.core.api.Assertions.assertThat;

class PortedKeyDeferrabilityTest extends _IntFieldEnumTest<PortedKey.TableKeyDeferrability> {

    PortedKeyDeferrabilityTest() {
        super(PortedKey.TableKeyDeferrability.class);
    }

    @DisplayName("valueOfDeferrability(deferrability)")
    @EnumSource(PortedKey.TableKeyDeferrability.class)
    @ParameterizedTest
    void valueOfDeferrability__(final PortedKey.TableKeyDeferrability deferrability) {
        assertThat(valueOfFieldValue(deferrability.fieldValueAsInt()))
                .isSameAs(deferrability);
    }
}
