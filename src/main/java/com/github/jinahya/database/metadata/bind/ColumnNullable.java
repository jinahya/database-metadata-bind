package com.github.jinahya.database.metadata.bind;

import java.sql.DatabaseMetaData;

/**
 * Constants for {@link Column#COLUMN_LABEL_NULLABLE} column values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public enum ColumnNullable implements _IntFieldEnum<ColumnNullable> {

    /**
     * A value for {@link DatabaseMetaData#columnNoNulls}({@value DatabaseMetaData#columnNoNulls}).
     */
    COLUMN_NO_NULLS(DatabaseMetaData.columnNoNulls),// 0

    /**
     * A value for {@link DatabaseMetaData#columnNullable}({@value DatabaseMetaData#columnNullable}).
     */
    COLUMN_NULLABLE(DatabaseMetaData.columnNullable), // 1

    /**
     * A value for {@link DatabaseMetaData#columnNullableUnknown}({@value DatabaseMetaData#columnNullableUnknown}).
     */
    PSEUDO(DatabaseMetaData.columnNullableUnknown) // 2
    ;

    /**
     * Finds the value for specified {@link Column#COLUMN_LABEL_NULLABLE} column value.
     *
     * @param nullable the value of {@link Column#COLUMN_LABEL_NULLABLE} column to match.
     * @return the value matched.
     * @throws IllegalStateException when no value matched.
     */
    public static ColumnNullable valueOfNullable(final int nullable) {
        return _IntFieldEnum.valueOfFieldValue(ColumnNullable.class, nullable);
    }

    ColumnNullable(final int fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public int fieldValueAsInt() {
        return fieldValue;
    }

    private final int fieldValue;
}
