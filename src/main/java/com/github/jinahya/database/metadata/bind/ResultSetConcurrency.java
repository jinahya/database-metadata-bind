package com.github.jinahya.database.metadata.bind;

import java.sql.ResultSet;

enum ResultSetConcurrency implements IntFieldEnum<ResultSetConcurrency> {

    CONCUR_READ_ONLY(ResultSet.CONCUR_READ_ONLY),

    CONCUR_UPDATABLE(ResultSet.CONCUR_UPDATABLE);

    ResultSetConcurrency(final int rawValue) {
        this.rawValue = rawValue;
    }

    @Override
    public int getRawValue() {
        return rawValue;
    }

    public final int rawValue;
}
