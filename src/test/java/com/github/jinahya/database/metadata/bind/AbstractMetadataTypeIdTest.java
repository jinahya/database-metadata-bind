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

import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Slf4j
abstract class AbstractMetadataTypeIdTest<T extends AbstractMetadataTypeId<T, U>, U extends MetadataType>
        extends MetadataTypeIdTest<T, U> {

    AbstractMetadataTypeIdTest(final Class<T> typeIdClass, final Class<U> typeClass) {
        super(typeIdClass, typeClass);
    }

    @Nested
    class EqualsTest {

        @Disabled // EqualsAndHashCode.Exclude
        @Test
        void verifyEquals() {
            EqualsVerifier.forClass(typeIdClass)
                    .verify();
        }
    }

    @Nested
    class HashcodeTest {

        @Test
        void __() {
            final var hashCode = typeIdInstance().hashCode();
        }
    }
}
