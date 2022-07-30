package com.github.jinahya.database.metadata.bind;

import java.sql.DatabaseMetaData;

public enum ImportedKeyDeferrability implements IntFieldEnum<ImportedKeyDeferrability> {

    /**
     * A constant for
     * {@link DatabaseMetaData#importedKeyInitiallyDeferred}({@value DatabaseMetaData#importedKeyInitiallyDeferred}).
     */
    IMPORTED_KEY_INITIALLY_DEFERRED(DatabaseMetaData.importedKeyInitiallyDeferred),

    /**
     * A constant for
     * {@link DatabaseMetaData#importedKeyInitiallyImmediate}({@value DatabaseMetaData#importedKeyInitiallyImmediate}).
     */
    IMPORTED_KEY_INITIALLY_IMMEDIATE(DatabaseMetaData.importedKeyInitiallyImmediate),

    /**
     * A constant for
     * {@link DatabaseMetaData#importedKeyNotDeferrable}({@value DatabaseMetaData#importedKeyNotDeferrable}).
     */
    IMPORTED_KEY_NOT_DEFERRABLE(DatabaseMetaData.importedKeyNotDeferrable);

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
