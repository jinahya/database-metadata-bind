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

import java.sql.DatabaseMetaData;

/**
 * Constants for values of {@value TableKey#COLUMN_NAME_DEFERRABILITY} column.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ExportedKey
 * @see ImportedKey
 */
public enum TableKeyDeferrability implements _IntFieldEnum<TableKeyDeferrability> {

    /**
     * Constants for
     * {@link DatabaseMetaData#importedKeyInitiallyDeferred}({@value DatabaseMetaData#importedKeyInitiallyDeferred}).
     */
    IMPORTED_KEY_INITIALLY_DEFERRED(DatabaseMetaData.importedKeyInitiallyDeferred), // 5

    /**
     * Constants for
     * {@link DatabaseMetaData#importedKeyInitiallyImmediate}({@value DatabaseMetaData#importedKeyInitiallyImmediate}).
     */
    IMPORTED_KEY_INITIALLY_IMMEDIATE(DatabaseMetaData.importedKeyInitiallyImmediate), // 6

    /**
     * Constants for
     * {@link DatabaseMetaData#importedKeyNotDeferrable}({@value DatabaseMetaData#importedKeyNotDeferrable}).
     */
    IMPORTED_KEY_SET_NOT_DEFERRABLE(DatabaseMetaData.importedKeyNotDeferrable); // 7

    public static TableKeyDeferrability valueOfDeferrability(final int deferrability) {
        return _IntFieldEnum.valueOfFieldValue(TableKeyDeferrability.class, deferrability);
    }

    TableKeyDeferrability(final int fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public int fieldValueAsInt() {
        return fieldValue;
    }

    private final int fieldValue;
}
