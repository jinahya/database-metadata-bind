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

public enum TableKeyUpdateRule implements _IntFieldEnum<TableKeyUpdateRule> {

    /**
     * Constants for {@link DatabaseMetaData#importedKeyCascade}({@value DatabaseMetaData#importedKeyCascade}).
     */
    IMPORTED_KEY_CASCADE(DatabaseMetaData.importedKeyCascade), // 0

    /**
     * Constants for {@link DatabaseMetaData#importedKeyRestrict}({@value DatabaseMetaData#importedKeyRestrict}).
     */
    IMPORTED_KEY_RESTRICT(DatabaseMetaData.importedKeyRestrict), // 1

    /**
     * Constants for {@link DatabaseMetaData#importedKeySetNull}({@value DatabaseMetaData#importedKeySetNull}).
     */
    IMPORTED_KEY_SET_NULL(DatabaseMetaData.importedKeySetNull), // 2

    /**
     * Constants for {@link DatabaseMetaData#importedKeyNoAction}({@value DatabaseMetaData#importedKeyNoAction}).
     */
    IMPORTED_KEY_NO_ACTION(DatabaseMetaData.importedKeyNoAction), // 3

    /**
     * Constants for {@link DatabaseMetaData#importedKeySetDefault}({@value DatabaseMetaData#importedKeySetDefault}).
     */
    IMPORTED_KEY_SET_DEFAULT(DatabaseMetaData.importedKeySetDefault); // 4

    public static TableKeyUpdateRule valueOfUpdateRule(final int updateRule) {
        return _IntFieldEnum.valueOfFieldValue(TableKeyUpdateRule.class, updateRule);
    }

    TableKeyUpdateRule(final int fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public int fieldValueAsInt() {
        return fieldValue;
    }

    private final int fieldValue;
}
