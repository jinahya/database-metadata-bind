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

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@SuperBuilder(toBuilder = true)
abstract class AbstractMetadataType
        implements MetadataType {

    private static final long serialVersionUID = -3285362930174073345L;

    @Override
    public Map<String, Object> getUnmappedValues() {
        if (unmappedValues == null) {
            unmappedValues = new HashMap<>();
        }
        return unmappedValues;
    }

    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private transient Map<String, Object> unmappedValues;
}
