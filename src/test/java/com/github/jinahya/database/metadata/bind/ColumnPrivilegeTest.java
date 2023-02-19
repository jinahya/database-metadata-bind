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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ColumnPrivilegeTest extends AbstractMetadataTypeTest<ColumnPrivilege> {

    ColumnPrivilegeTest() {
        super(ColumnPrivilege.class);
    }

    @Nested
    class ColumnPrivilegeIdTest {

        @Test
        void __() {
            final var instance = typeInstance();
            instance.setTableName("");
            instance.setColumnName("");
            instance.setPrivilege("");
            final var id = instance.getColumnPrivilegeId();
            assertThat(id).isNotNull();
        }
    }

    @Nested
    class IsGrantableAsBooleanTest {

        @Test
        void getIsGrantableAsBoolean__() {
            final var spy = typeSpy();
            final var isGrantableAsBoolean = spy.getIsGrantableAsBoolean();
            assertThat(isGrantableAsBoolean).isNull();
            verify(spy, times(1)).getIsGrantable();
        }

        @Test
        void setIsGrantableAsBoolean__Null() {
            final var spy = typeSpy();
            spy.setIsGrantableAsBoolean(null);
            verify(spy, times(1)).setIsGrantable(null);
        }

        @ValueSource(booleans = {false, true})
        @ParameterizedTest
        void setIsGrantableAsBoolean__(final boolean grantableAsBoolean) {
            final var spy = typeSpy();
            spy.setIsGrantableAsBoolean(grantableAsBoolean);
            verify(spy, times(1)).setIsGrantable(grantableAsBoolean ? "YES" : "NO");
        }
    }
}
