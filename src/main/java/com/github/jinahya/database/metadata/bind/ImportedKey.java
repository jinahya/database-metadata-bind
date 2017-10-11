/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import java.sql.DatabaseMetaData;
import static java.sql.DatabaseMetaData.importedKeyInitiallyDeferred;
import static java.sql.DatabaseMetaData.importedKeyInitiallyImmediate;
import static java.sql.DatabaseMetaData.importedKeyNotDeferrable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An entity class for imported keys.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getImportedKeys(java.lang.String, java.lang.String,
 * java.lang.String)
 */
@XmlRootElement
public class ImportedKey extends TableKey {

    private static final long serialVersionUID = 1965229912934042881L;

    // -------------------------------------------------------------------------
    public static enum Rule {

        /**
         * Constant for {@link DatabaseMetaData#importedKeyCascade} whose value
         * is {@value DatabaseMetaData#importedKeyCascade}.
         */
        CASCADE(DatabaseMetaData.importedKeyCascade), // 0
        /**
         * Constant for {@link DatabaseMetaData#importedKeyRestrict} whose value
         * is {@value DatabaseMetaData#importedKeyRestrict}.
         */
        RESTRICT(DatabaseMetaData.importedKeyRestrict), // 1
        /**
         * Constant for {@link DatabaseMetaData#importedKeySetNull} whose value
         * is {@value DatabaseMetaData#importedKeySetNull}.
         */
        SET_NULL(DatabaseMetaData.importedKeySetNull), // 2
        /**
         * Constant for {@link DatabaseMetaData#importedKeyNoAction} whose value
         * is {@value DatabaseMetaData#importedKeyNoAction}.
         */
        NO_ACTION(DatabaseMetaData.importedKeyNoAction), // 3
        /**
         * Constant for {@link DatabaseMetaData#importedKeySetDefault} whose
         * value is {@value DatabaseMetaData#importedKeySetDefault}.
         */
        SET_DEFAULT(DatabaseMetaData.importedKeySetDefault); // 4

        // ---------------------------------------------------------------------
        /**
         * Returns the constant whose raw value matches to given. An instance of
         * {@link IllegalArgumentException} will be thrown if no constant
         * matched.
         *
         * @param rawValue the raw value to match
         * @return the matched constant.
         */
        public static Rule valueOf(final int rawValue) {
            for (final Rule value : values()) {
                if (value.rawValue == rawValue) {
                    return value;
                }
            }
            throw new IllegalArgumentException("unknown raw value: " + rawValue);
        }

        // ---------------------------------------------------------------------
        private Rule(final int rawValue) {
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

    public static enum Deferrability {

        /**
         * Constant for {@link DatabaseMetaData#importedKeyInitiallyDeferred}
         * whose value is
         * {@value DatabaseMetaData#importedKeyInitiallyDeferred}.
         */
        INITIALLY_DEFERRED(importedKeyInitiallyDeferred), // 5
        /**
         * Constant for {@link DatabaseMetaData#importedKeyInitiallyImmediate}
         * whose value is
         * {@value DatabaseMetaData#importedKeyInitiallyImmediate}.
         */
        INITIALLY_IMMEDIATE(importedKeyInitiallyImmediate), // 6
        /**
         * Constant for {@link DatabaseMetaData#importedKeyNotDeferrable} whose
         * value is {@value DatabaseMetaData#importedKeyNotDeferrable}.
         */
        NOT_DEFERRABLE(importedKeyNotDeferrable); // 7

        // ---------------------------------------------------------------------
        /**
         * Returns the constant whose raw value matches to given. An instance of
         * {@link IllegalArgumentException} will be thrown if no constant
         * matched.
         *
         * @param rawValue the raw value
         * @return the matched constant.
         */
        public static Deferrability valueOf(final int rawValue) {
            for (final Deferrability value : values()) {
                if (value.rawValue == rawValue) {
                    return value;
                }
            }
            throw new IllegalArgumentException(
                    "unknown raw value: " + rawValue);
        }

        // ---------------------------------------------------------------------
        private Deferrability(final int rawValue) {
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

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance.
     */
    public ImportedKey() {
        super();
    }

    // -------------------------------------------------------------------------
    @Deprecated
    private Table table;
}
