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

import java.util.Optional;

public interface HasNullableEnum<E extends Enum<E> & NullableEnum<E>> {

    /**
     * Returns current value of {@code nullable} property.
     *
     * @return current value of the {@code nullable} property.
     */
    Integer getNullable();

    /**
     * Replace current value of {@code nullable} property with specified value.
     *
     * @param nullable new value for the {@code nullable} property.
     */
    void setNullable(Integer nullable);

    E getNullableAsEnum();

    default void setNullableAsEnum(final E nullableAsEnum) {
        setNullable(
                Optional.ofNullable(nullableAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }
}
