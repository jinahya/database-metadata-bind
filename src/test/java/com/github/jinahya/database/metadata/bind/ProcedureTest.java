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

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

class ProcedureTest
        extends AbstractMetadataType_Test<Procedure> {

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

    @Test
    void __() {
        final var p1 = new Procedure();
        p1.setProcedureCat(null);
        p1.setProcedureSchem("DVSYS");
        p1.setProcedureName("GET_TRUST_LEVEL_FOR_IDENTITY");
        p1.setSpecificName(null);

        final var p2 = new Procedure();
        p2.setProcedureCat("DBMS_MACOLS_SESSION");
        p2.setProcedureSchem("DVSYS");
        p2.setProcedureName("IS_MAC_LABEL_SET");
        p2.setSpecificName(null);

        assertThat(
                Comparator.comparing(
                        Procedure::getProcedureCat,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                ).compare(p1, p2)
        ).isNotPositive();

        assertThat(
                Comparator.comparing(
                        Procedure::getProcedureSchem,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                ).compare(p1, p2)
        ).isNotPositive();

        assertThat(
                Comparator.comparing(
                        Procedure::getProcedureName,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                ).compare(p1, p2)
        ).isNotPositive();

        assertThat(
                Comparator.comparing(
                        Procedure::getSpecificName,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                ).compare(p1, p2)
        ).isNotPositive();
    }
}
