package com.github.jinahya.database.metadata.bind;

import java.sql.DatabaseMetaData;

/**
 * Constants for {@link VersionColumn#COLUMN_LABEL_PSEUDO_COLUMN} column values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public enum VersionColumnPseudoColumn implements _IntFieldEnum<VersionColumnPseudoColumn> {

    /**
     * A value for {@link DatabaseMetaData#versionColumnUnknown}({@value DatabaseMetaData#versionColumnUnknown}).
     */
    VERSION_COLUMN_UNKNOWN(DatabaseMetaData.versionColumnUnknown),// 0

    /**
     * A value for {@link DatabaseMetaData#versionColumnNotPseudo}({@value DatabaseMetaData#versionColumnNotPseudo}).
     */
    VERSION_COLUMN_NOT_PSEUDO(DatabaseMetaData.versionColumnNotPseudo), // 1

    /**
     * A value for {@link DatabaseMetaData#versionColumnPseudo}({@value DatabaseMetaData#versionColumnPseudo}).
     */
    VERSION_COLUMN_PSEUDO(DatabaseMetaData.versionColumnPseudo) // 2
    ;

    /**
     * Finds the value for specified {@link VersionColumn#COLUMN_LABEL_PSEUDO_COLUMN} column value.
     *
     * @param pseudoColumn the value of {@link VersionColumn#COLUMN_LABEL_PSEUDO_COLUMN} column to match.
     * @return the value matched.
     * @throws IllegalStateException when no value matched.
     */
    public static VersionColumnPseudoColumn valueOfPseudoColumn(final int pseudoColumn) {
        return _IntFieldEnum.valueOfFieldValue(VersionColumnPseudoColumn.class, pseudoColumn);
    }

    VersionColumnPseudoColumn(final int fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public int fieldValueAsInt() {
        return fieldValue;
    }

    private final int fieldValue;
}
