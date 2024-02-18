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
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ProcedureTest
        extends AbstractMetadataTypeTest<Procedure> {

    @Nested
    class ProcedureTypeTest
            extends _IntFieldEnumTest<Procedure.ProcedureType> {

        ProcedureTypeTest() {
            super(Procedure.ProcedureType.class);
        }
    }

    ProcedureTest() {
        super(Procedure.class);
    }

    @Override
    Procedure newTypeInstance() {
        final Procedure instance = super.newTypeInstance();
        instance.setProcedureName("");
        instance.setSpecificName("");
        return instance;
    }

    @Nested
    class ProcedureTypeAsEnumTest {

        @Test
        void getProcedureTypeAsEnum__() {
            // GIVEN
            final var spy = Mockito.spy(newTypeInstance());
            // WHEN
            final var procedureTypeAsEnum = spy.getProcedureTypeAsEnum();
            // THEN
            verify(spy, times(1)).getProcedureType();
        }

        @EnumSource(Procedure.ProcedureType.class)
        @ParameterizedTest
        void setProcedureTypeAsEnum__(final Procedure.ProcedureType procedureTypeAsEnum) {
            // GIVEN
            final var spy = Mockito.spy(newTypeInstance());
            // WHEN
            spy.setProcedureTypeAsEnum(procedureTypeAsEnum);
            // THEN
            verify(spy, times(1)).setProcedureType(procedureTypeAsEnum.fieldValueAsInt());
        }
    }
}
