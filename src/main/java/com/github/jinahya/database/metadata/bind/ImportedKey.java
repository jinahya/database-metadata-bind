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
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;

/**
 * A class for binding results of {@link DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getImportedKeys(String, String, String)
 * @see ExportedKey
 */
@_ChildOf(Table.class)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class ImportedKey extends TableKey<ImportedKey> {

    private static final long serialVersionUID = -1900794151555506751L;

    public static final Comparator<ImportedKey> CASE_INSENSITIVE_ORDER = comparingPktableCaseInsensitive();

    public static final Comparator<ImportedKey> LEXICOGRAPHIC_ORDER = comparingPktableLexicographic();

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ImportedKey)) return false;
        return super.equals_((ImportedKey) obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
