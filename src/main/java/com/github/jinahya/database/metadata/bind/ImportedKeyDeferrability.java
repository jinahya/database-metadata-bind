package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2022 Jinahya, Inc.
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

import javax.xml.bind.annotation.XmlEnum;
import java.sql.DatabaseMetaData;

@XmlEnum
public enum ImportedKeyDeferrability
        implements IntFieldEnum<ImportedKeyDeferrability> {

    /**
     * A constant for
     * {@link DatabaseMetaData#importedKeyInitiallyDeferred}({@value DatabaseMetaData#importedKeyInitiallyDeferred}).
     */
    IMPORTED_KEY_INITIALLY_DEFERRED(DatabaseMetaData.importedKeyInitiallyDeferred), // 5

    /**
     * A constant for
     * {@link DatabaseMetaData#importedKeyInitiallyImmediate}({@value DatabaseMetaData#importedKeyInitiallyImmediate}).
     */
    IMPORTED_KEY_INITIALLY_IMMEDIATE(DatabaseMetaData.importedKeyInitiallyImmediate), // 6

    /**
     * A constant for
     * {@link DatabaseMetaData#importedKeyNotDeferrable}({@value DatabaseMetaData#importedKeyNotDeferrable}).
     */
    IMPORTED_KEY_NOT_DEFERRABLE(DatabaseMetaData.importedKeyNotDeferrable); // 7

    public static ImportedKeyDeferrability valueOfRawValue(final int rawValue) {
        return IntFieldEnums.valueOfRawValue(ImportedKeyDeferrability.class, rawValue);
    }

    ImportedKeyDeferrability(final int rawValue) {
        this.rawValue = rawValue;
    }

    @Override
    public int rawValue() {
        return rawValue;
    }

    private final int rawValue;
}
