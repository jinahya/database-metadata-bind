package com.github.jinahya.database.metadata.bind;

import java.sql.ResultSet;

enum ResultSetHoldability implements IntFieldEnum<ResultSetHoldability> {

    HOLD_CURSORS_OVER_COMMIT(ResultSet.HOLD_CURSORS_OVER_COMMIT),

    CLOSE_CURSORS_AT_COMMIT(ResultSet.CLOSE_CURSORS_AT_COMMIT);

    ResultSetHoldability(final int rawValue) {
        this.rawValue = rawValue;
    }

    @Override
    public int getRawValue() {
        return rawValue;
    }

    public final int rawValue;
}
