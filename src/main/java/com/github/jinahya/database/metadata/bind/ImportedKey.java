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

import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;

/**
 * A class for binding results of {@link DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getImportedKeys(String, String, String, Collection)
 */
@XmlRootElement
@NoArgsConstructor
public class ImportedKey extends TableKey {

    private static final long serialVersionUID = 1478290412906203629L;

    // ---------------------------------------------------------------------------------------- UPDATE_RULE / updateRule
    public static final String COLUMN_NAME_UPDATE_RULE = "UPDATE_RULE";

    public static final String ATTRIBUTE_NAME_UPDATE_RULE = "updateRule";

    // ---------------------------------------------------------------------------------------- DELETE_RULE / deleteRule
    public static final String COLUMN_NAME_DELETE_RULE = "DELETE_RULE";

    public static final String ATTRIBUTE_NAME_DELETE_RULE = "deleteRule";

    // ----------------------------------------------------------------------------------- DEFERRABILITY / deferrability
    public static final String COLUMN_NAME_DEFERRABILITY = "DEFERRABILITY";

    public static final String ATTRIBUTE_NAME_DEFERRABILITY = "deferrability";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for {@value com.github.jinahya.database.metadata.bind.ImportedKey#COLUMN_NAME_UPDATE_RULE} column
     * values and {@value com.github.jinahya.database.metadata.bind.ImportedKey#COLUMN_NAME_DELETE_RULE} column values
     * of a result of {@link DatabaseMetaData#getImportedKeys(String, String, String)} method.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DatabaseMetaData#getImportedKeys(String, String, String)
     */
    public enum Rule implements IntFieldEnum<Rule> {

        /**
         * Constant for {@link DatabaseMetaData#importedKeyCascade}({@value java.sql.DatabaseMetaData#importedKeyCascade}).
         */
        IMPORTED_KEY_CASCADE(DatabaseMetaData.importedKeyCascade), // 0

        /**
         * Constant for {@link DatabaseMetaData#importedKeyRestrict}({@value java.sql.DatabaseMetaData#importedKeyRestrict}).
         */
        IMPORTED_KEY_RESTRICT(DatabaseMetaData.importedKeyRestrict), // 1

        /**
         * Constant for {@link DatabaseMetaData#importedKeySetNull}({@value java.sql.DatabaseMetaData#importedKeySetNull}).
         */
        IMPORTED_KEY_SET_NULL(DatabaseMetaData.importedKeySetNull), // 2

        /**
         * Constant for {@link DatabaseMetaData#importedKeyNoAction}({@value java.sql.DatabaseMetaData#importedKeyNoAction}).
         */
        IMPORTED_KEY_NO_ACTION(DatabaseMetaData.importedKeyNoAction), // 3

        /**
         * Constant for {@link DatabaseMetaData#importedKeySetDefault}({@value java.sql.DatabaseMetaData#importedKeySetDefault}).
         */
        IMPORTED_KEY_SET_DEFAULT(DatabaseMetaData.importedKeySetDefault); // 4

        /**
         * Returns the constant whose raw value matches to given. An instance of {@link IllegalArgumentException} will
         * be thrown if no constant matched.
         *
         * @param rawValue the raw value to match
         * @return the matched constant.
         */
        public static Rule valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Rule.class, rawValue);
        }

        /**
         * Creates a new instance with specified raw value.
         *
         * @param rawValue the raw value.
         */
        Rule(final int rawValue) {
            this.rawValue = rawValue;
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        /**
         * The raw value of this constant.
         */
        private final int rawValue;
    }

    /**
     * Constants for {@value com.github.jinahya.database.metadata.bind.ImportedKey#COLUMN_NAME_DEFERRABILITY} column
     * values of a result of {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)}
     * method.
     */
    public enum Deferrability implements IntFieldEnum<Deferrability> {

        /**
         * Constant for {@link DatabaseMetaData#importedKeyInitiallyDeferred}({@value
         * java.sql.DatabaseMetaData#importedKeyInitiallyDeferred}).
         */
        IMPORTED_KEY_INITIALLY_DEFERRED(DatabaseMetaData.importedKeyInitiallyDeferred), // 5

        /**
         * Constant for {@link DatabaseMetaData#importedKeyInitiallyImmediate}({@value
         * java.sql.DatabaseMetaData#importedKeyInitiallyImmediate}).
         */
        IMPORTED_KEY_INITIALLY_IMMEDIATE(DatabaseMetaData.importedKeyInitiallyImmediate), // 6

        /**
         * Constant for {@link DatabaseMetaData#importedKeyNotDeferrable}({@value java.sql.DatabaseMetaData#importedKeyNotDeferrable}).
         */
        IMPORTED_KEY_NOT_DEFERRABLE(DatabaseMetaData.importedKeyNotDeferrable); // 7

        /**
         * Creates a new instance with specified raw value.
         *
         * @param rawValue the raw value.
         */
        Deferrability(final int rawValue) {
            this.rawValue = rawValue;
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        /**
         * The raw value of this constant.
         */
        private final int rawValue;
    }
}
