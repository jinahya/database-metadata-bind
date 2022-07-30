package com.github.jinahya.database.metadata.bind;

import java.sql.DatabaseMetaData;

public enum DeleteRule implements IntFieldEnum<DeleteRule> {

    /**
     * A constant for {@link DatabaseMetaData#importedKeyCascade}({@value DatabaseMetaData#importedKeyCascade}).
     */
    IMPORTED_KEY_CASCADE(DatabaseMetaData.importedKeyCascade),

    /**
     * A constant for {@link DatabaseMetaData#importedKeyRestrict}({@value DatabaseMetaData#importedKeyRestrict}).
     */
    IMPORTED_KEY_RESTRICT(DatabaseMetaData.importedKeyRestrict),

    /**
     * A constant for {@link DatabaseMetaData#importedKeySetNull}({@value DatabaseMetaData#importedKeySetNull}).
     */
    IMPORTED_KEY_SET_NULL(DatabaseMetaData.importedKeySetNull),

    /**
     * A constant for {@link DatabaseMetaData#importedKeyNoAction}({@value DatabaseMetaData#importedKeyNoAction}).
     */
    IMPORTED_KEY_NO_ACTION(DatabaseMetaData.importedKeyNoAction),

    /**
     * A constant for {@link DatabaseMetaData#importedKeySetDefault}({@value DatabaseMetaData#importedKeySetDefault}).
     */
    IMPORTED_KEY_SET_DEFAULT(DatabaseMetaData.importedKeySetDefault);

    public static DeleteRule valueOfRawValue(final int rawValue) {
        return IntFieldEnums.valueOfRawValue(DeleteRule.class, rawValue);
    }

    DeleteRule(final int rawValue) {
        this.rawValue = rawValue;
    }

    @Override
    public int rawValue() {
        return rawValue;
    }

    private final int rawValue;
}
