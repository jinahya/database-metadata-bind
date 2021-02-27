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

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;

import static java.sql.DatabaseMetaData.*;

/**
 * An entity class for imported keys.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getImportedKeys(String, String, String, Collection)
 */
@XmlRootElement
public class ImportedKey extends TableKey {

    private static final long serialVersionUID = 1965229912934042881L;

    // -------------------------------------------------------------------------
    public enum Rule implements IntFieldEnum<Rule> {

        /**
         * Constant for {@link DatabaseMetaData#importedKeyCascade} whose value is {@value
         * DatabaseMetaData#importedKeyCascade}.
         */
        IMPORTED_KEY_CASCADE(importedKeyCascade), // 0
        /**
         * Constant for {@link DatabaseMetaData#importedKeyRestrict} whose value is {@value
         * DatabaseMetaData#importedKeyRestrict}.
         */
        IMPORTED_KEY_RESTRICT(importedKeyRestrict), // 1
        /**
         * Constant for {@link DatabaseMetaData#importedKeySetNull} whose value is {@value
         * DatabaseMetaData#importedKeySetNull}.
         */
        IMPORTED_KEY_SET_NULL(importedKeySetNull), // 2
        /**
         * Constant for {@link DatabaseMetaData#importedKeyNoAction} whose value is {@value
         * DatabaseMetaData#importedKeyNoAction}.
         */
        IMPORTED_KEY_NO_ACTION(importedKeyNoAction), // 3
        /**
         * Constant for {@link DatabaseMetaData#importedKeySetDefault} whose value is {@value
         * DatabaseMetaData#importedKeySetDefault}.
         */
        IMPORTED_KEY_SET_DEFAULT(importedKeySetDefault); // 4

        // ---------------------------------------------------------------------

        /**
         * Returns the constant whose raw value matches to given. An instance of {@link IllegalArgumentException} will
         * be thrown if no constant matched.
         *
         * @param rawValue the raw value to match
         * @return the matched constant.
         */
        public static Rule valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(Rule.class, rawValue);
        }

        // ---------------------------------------------------------------------
        Rule(final int rawValue) {
            this.rawValue = rawValue;
        }

        // ---------------------------------------------------------------------

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        public int getRawValue() {
            return rawValue;
        }

        // ---------------------------------------------------------------------
        private final int rawValue;
    }

    public enum Deferrability implements IntFieldEnum<Deferrability> {

        /**
         * Constant for {@link DatabaseMetaData#importedKeyInitiallyDeferred} whose value is {@value
         * DatabaseMetaData#importedKeyInitiallyDeferred}.
         */
        IMPORTED_KEY_INITIALLY_DEFERRED(importedKeyInitiallyDeferred), // 5

        /**
         * Constant for {@link DatabaseMetaData#importedKeyInitiallyImmediate} whose value is {@value
         * DatabaseMetaData#importedKeyInitiallyImmediate}.
         */
        IMPORTED_KEY_INITIALLY_IMMEDIATE(importedKeyInitiallyImmediate), // 6

        /**
         * Constant for {@link DatabaseMetaData#importedKeyNotDeferrable} whose value is {@value
         * DatabaseMetaData#importedKeyNotDeferrable}.
         */
        IMPORTED_KEY_NOT_DEFERRABLE(importedKeyNotDeferrable); // 7

        // ---------------------------------------------------------------------

        /**
         * Returns the constant whose raw value matches to given. An instance of {@link IllegalArgumentException} will
         * be thrown if no constant matched.
         *
         * @param rawValue the raw value
         * @return the matched constant.
         */
        public static Deferrability valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(Deferrability.class, rawValue);
        }

        // ---------------------------------------------------------------------
        Deferrability(final int rawValue) {
            this.rawValue = rawValue;
        }

        // ---------------------------------------------------------------------

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        // ---------------------------------------------------------------------
        private final int rawValue;
    }

    // -------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public ImportedKey() {
        super();
    }
}
