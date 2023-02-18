package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class AttributeId implements MetadataTypeId<AttributeId, Attribute> {

    private static final long serialVersionUID = 7221973324274278465L;

    public static final Comparator<AttributeId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(AttributeId::getUdtId, UDTId.CASE_INSENSITIVE_ORDER)
                    .thenComparingInt(AttributeId::getOrdinalPosition);

    public static final Comparator<AttributeId> COMPARING_NATURAL =
            Comparator.comparing(AttributeId::getUdtId, UDTId.NATURAL_ORDER)
                    .thenComparingInt(AttributeId::getOrdinalPosition);

    static AttributeId of(final UDTId udtId, final String attrName, final int ordinalPosition) {
        Objects.requireNonNull(udtId, "udtId is null");
        Objects.requireNonNull(attrName, "attrName is null");
        if (false && ordinalPosition <= 0) {
            throw new IllegalArgumentException("non-positive ordinalPosition: " + ordinalPosition);
        }
        return builder()
                .udtId(udtId)
                .attrName(attrName)
                .ordinalPosition(ordinalPosition)
                .build();
    }

    static AttributeId of(final String typeCat, final String typeSchem, final String typeName,
                          final String attrName, final int ordinalPosition) {
        return of(UDTId.of(typeCat, typeSchem, typeName), attrName, ordinalPosition);
    }

    public static AttributeId of(final UDTId udtId, final String attrName) {
        return of(udtId, attrName, 1);
    }

    public static AttributeId of(final String typeCat, final String typeSchem, final String typeName,
                                 final String attrName) {
        return of(typeCat, typeSchem, typeName, attrName, 1);
    }

    private final UDTId udtId;

    private final String attrName;

    @EqualsAndHashCode.Exclude
    private final int ordinalPosition;
}
