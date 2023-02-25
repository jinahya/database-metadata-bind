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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public final class AttributeId extends AbstractMetadataTypeId<AttributeId, Attribute> {

    private static final long serialVersionUID = 7221973324274278465L;

    public static final Comparator<AttributeId> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(AttributeId::getUdtId, UDTId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(AttributeId::getAttrName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<AttributeId> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(AttributeId::getUdtId, UDTId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(AttributeId::getAttrName);

    static AttributeId of(final UDTId udtId, final String attrName) {
        Objects.requireNonNull(udtId, "udtId is null");
        Objects.requireNonNull(attrName, "attrName is null");
        return builder()
                .udtId(udtId)
                .attrName(attrName)
                .build();
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
               "udtId=" + udtId +
               ",attrName=" + attrName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AttributeId)) return false;
        final AttributeId that = (AttributeId) obj;
        return Objects.equals(udtId, that.udtId) &&
               Objects.equals(attrName, that.attrName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(udtId, attrName);
    }

    private final UDTId udtId;

    private final String attrName;
}
