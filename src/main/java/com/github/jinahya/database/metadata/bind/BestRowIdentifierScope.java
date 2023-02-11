package com.github.jinahya.database.metadata.bind;

import java.sql.DatabaseMetaData;

/**
 * Constants for {@link BestRowIdentifier#COLUMN_LABEL_SCOPE} column values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public enum BestRowIdentifierScope implements _IntFieldEnum<BestRowIdentifierScope> {

    /**
     * A value for {@link DatabaseMetaData#bestRowTemporary}({@value DatabaseMetaData#bestRowTemporary}).
     */
    BEST_ROW_TEMPORARY(DatabaseMetaData.bestRowTemporary),// 0

    /**
     * A value for {@link DatabaseMetaData#bestRowTransaction}({@value DatabaseMetaData#bestRowTransaction}).
     */
    BEST_ROW_TRANSACTION(DatabaseMetaData.bestRowTransaction), // 1

    /**
     * A value for {@link DatabaseMetaData#bestRowSession}({@value DatabaseMetaData#bestRowSession}).
     */
    BEST_ROW_SESSION(DatabaseMetaData.bestRowSession) // 2
    ;

    /**
     * Finds the value for specified {@link BestRowIdentifier#COLUMN_LABEL_SCOPE} column value.
     *
     * @param scope the value of {@link BestRowIdentifier#COLUMN_LABEL_SCOPE} column to match.
     * @return the value matched.
     * @throws IllegalStateException when no value matched.
     */
    public static BestRowIdentifierScope valueOfScope(final int scope) {
        return _IntFieldEnum.valueOfFieldValue(BestRowIdentifierScope.class, scope);
    }

    BestRowIdentifierScope(final int fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public int fieldValueAsInt() {
        return fieldValue;
    }

    private final int fieldValue;
}
