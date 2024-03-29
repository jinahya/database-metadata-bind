package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2023 Jinahya, Inc.
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

interface _IntFieldEnum<E extends Enum<E> & _IntFieldEnum<E>> {

    static <E extends Enum<E> & _IntFieldEnum<E>> E valueOfFieldValue(final Class<E> enumClass, final int fieldValue) {
        for (final E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.fieldValueAsInt() == fieldValue) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("no enum constant for " + fieldValue);
    }

    /**
     * Returns the field value of this constant.
     *
     * @return the field value of this constant.
     */
    int fieldValueAsInt();
}
